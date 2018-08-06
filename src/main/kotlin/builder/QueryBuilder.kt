package builder

import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

// TODO: Add KDoc based on introspection results

// TODO: Add support to directives (https://graphql.org/learn/queries/#directives)
// TODO: Add support to interfaces (https://graphql.org/learn/schema/#interfaces)
// TODO: Add support to inline fragments (https://graphql.org/learn/queries/#inline-fragments)
// TODO: Add support to metafields (https://graphql.org/learn/queries/#meta-fields)
open class Field(val fieldName: String, private val alias: String? = null) {
    private val arguments: MutableList<Argument> = ArrayList()

    fun <T : Field> T.set(name: String, value: Any?): T {
        if (value != null) {
            val argument = Argument(name, value)

            if (arguments.contains(argument)) {
                throw IllegalStateException("Duplicated argument ${argument.name} found for ${this.fieldName}")
            }

            arguments.add(argument)
        }
        return this
    }

    private fun argumentsToString(): String = (if (arguments.isEmpty()) "" else arguments.joinToString(separator = ",", prefix = "(", postfix = ")"))

    override fun toString(): String {
        return "${if (alias != null) "$alias: " else ""}$fieldName" + argumentsToString()

    }

    override fun equals(other: Any?): Boolean {
        if (other is Field) {
            return other.fieldName == fieldName && other.alias == alias
        }

        return false
    }

    override fun hashCode(): Int = fieldName.hashCode() * 31 + (alias?.hashCode() ?: 0)
}

open class Object(fieldName: String, private val parent: Object? = null, alias: String? = null) : Field(fieldName, alias) {
    val fields: MutableList<Field> = ArrayList()

    fun <T : Field> doInit(field: T, init: T.() -> Unit = {}): T {
        field.init()

        if (fields.contains(field)) {
            throw IllegalStateException("Duplicated child field '${field.fieldName}' found for '${this.fieldName}'")
        }

        fields.add(field)

        return field
    }

    open fun <T : Object> addFragment(fragment: Fragment<T>) {
        parent?.addFragment(fragment)
    }

    fun fieldsToString(): String = (if (fields.isEmpty()) "" else fields.joinToString(separator = ", ", prefix = " { ", postfix = " }"))

    override fun toString(): String {
        return super.toString() + fieldsToString()
    }
}

// TODO: add support to variables in Arguments (https://graphql.org/learn/queries/#variables)
class Argument(val name: String, private val value: Any) {
    override fun toString() = "$name:$value"

    override fun equals(other: Any?): Boolean {
        if (other is Argument) {
            return other.name == name
        }

        return false
    }

    override fun hashCode(): Int = name.hashCode()
}

class Query(name: String?) : Object("query${if (name != null) " $name" else ""}") {
    private val fragments: MutableCollection<Fragment<out Object>> = ArrayList()

    override fun <T : Object> addFragment(fragment: Fragment<T>) {
        if(!fragments.contains(fragment)) {
            fragments.add(fragment)
        }
    }

    private fun fragmentsToString(): String = (if (fragments.isEmpty()) "" else fragments.joinToString(separator = " ", prefix = " "))

    override fun toString(): String {
        return super.toString() + fragmentsToString()
    }
}

fun query(name: String? = null, init: Query.() -> Unit): String = Query(name).apply(init).toString()

data class Fragment<T : Object>(val name: String, val of: T) {
    override fun toString(): String {
        return "fragment $name on ${of::class.simpleName}${of.fieldsToString()}"
    }
}

fun <T : Object> fragment(name: String, on: KClass<T>, init: T.() -> Unit): Fragment<T> = Fragment(name, of = on.createInstance().apply(init))

fun <T : Object> T.useFragment(fragment: Fragment<T>) {
    addFragment(fragment)

    fields.add(object : Field(fragment.name) {
        override fun toString(): String = "...$fieldName"
    })
}


// ---- Domain specific: GitHub ---- //

fun Query.viewer(alias: String? = null, init: Viewer.() -> Unit) = doInit(Viewer(this, alias), init)

// TODO: avoid having the type and the field share the same class hierarchy. This is bad, it allows, for example, the
// consumer to create a fragment using the type () as the "class type"
abstract class User(fieldName: String, parent: Object? = null, alias: String? = null) : Object(fieldName, parent, alias) {
    class Login(alias: String? = null) : Field("login", alias)
    class Email(alias: String? = null) : Field("email", alias)
    class Name(alias: String? = null) : Field("name", alias)

    fun login(alias: String? = null) = doInit(Login(alias))
    fun email(alias: String? = null) = doInit(Email(alias))
    fun name(alias: String? = null) = doInit(Name(alias))

    var email: Email? = null
        get() {
            return doInit(Email())
        }
    var login: Login? = null
        get() {
            return doInit(Login())
        }
    var name: Name? = null
        get() {
            return doInit(Name())
        }

    fun pullRequests(alias: String? = null, first: Int? = null, last: Int? = null, init: PullRequestConnection.() -> Unit) =
            doInit(PullRequestConnection(this, alias), init)
                    .set("first", first)
                    .set("last", last)

}
class Viewer(parent: Object? = null, alias: String? = null) : User("viewer", parent, alias)

class PullRequestConnection(parent: Object, alias: String? = null) : Object("pullRequests", parent, alias) {
    fun nodes(alias: String? = null, init: PullRequest.() -> Unit) =
            doInit(PullRequest(this, alias), init)
}

class PullRequest(parent: Object, alias: String? = null) : Object("nodes", parent, alias) {
    class Body(alias: String? = null) : Field("body", alias)
    class Id(alias: String? = null) : Field("id", alias)

    fun body(alias: String? = null) = doInit(Body(alias))
    fun id(alias: String? = null) = doInit(Id(alias))

    var body: Body? = null
        get() {
            return doInit(Body())
        }

    var id: Id? = null
        get() {
            return doInit(Id())
        }
}
