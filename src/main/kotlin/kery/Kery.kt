package kery

import java.util.*
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

    fun argumentsToString(): String = (if (arguments.isEmpty()) "" else arguments.joinToString(separator = ",", prefix = "(", postfix = ")"))

    override fun toString(): String {
        return "${if (alias != null) "$alias: " else ""}$fieldName" + argumentsToString()

    }


    override fun equals(other: Any?): Boolean {
        if (other is Field) {
            return other.fieldName == fieldName
        }

        return false
    }

    override fun hashCode(): Int = fieldName.hashCode()
}

open class Object(fieldName: String, alias: String? = null) : Field(fieldName, alias) {
    val children: MutableList<Field> = ArrayList()

    fun <T : Field> doInit(field: T, init: T.() -> Unit = {}): T {
        field.init()

        if (children.contains(field)) {
            throw IllegalStateException("Duplicated child field ${field.fieldName} found for ${this.fieldName}")
        }

        children.add(field)

        return field
    }

    fun childrenToString(): String = (if (children.isEmpty()) "" else children.joinToString(separator = ", ", prefix = " { ", postfix = " }"))

    override fun toString(): String {
        return super.toString() + childrenToString()

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

class Query(name: String?) : Object("query${if (name != null) " $name" else ""}")

fun query(name: String? = null, init: Query.() -> Unit): String = Query(name).apply(init).toString()

class Fragment<T : Object>(val name: String, val of: T) {
    override fun toString(): String {
        return "fragment $name on ${of::class.simpleName}${of.childrenToString()}"
    }
}

fun <T : Object> fragment(name: String, on: KClass<T>, init: T.() -> Unit): Fragment<T> = Fragment(name, of = on.createInstance().apply(init))

fun <T : Object> T.useFragment(fragment: Fragment<T>) {
    children.add(object : Field(fragment.name) {
        override fun toString(): String = "...$fieldName"
    })
}


// ---- Domain specific: GitHub ---- //

fun Query.viewer(alias: String? = null, init: Viewer.() -> Unit) = doInit(Viewer(alias), init)

class Viewer(alias: String? = null) : Object("viewer", alias) {
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

    fun pullRequests(alias: String? = null, first: Int? = null, last: Int? = null, init: PullRequests.() -> Unit) =
            doInit(PullRequests(alias), init).set("first", first).set("last", last)

}

class PullRequests(alias: String? = null) : Object("pullRequests", alias) {
    class Id(alias: String? = null) : Field("id", alias)

    fun id(alias: String? = null) = doInit(Id(alias))
    var id: Id? = null
        get() {
            return doInit(Id()) {}
        }
}


