package builder

import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

open class ScalarType

open class ObjectType : ScalarType() {
    val fields: MutableList<Field<*>> = ArrayList()

    fun <T : Field<*>> doInit(field: T, init: T.() -> Unit = {}): T {
        field.init()

        if (fields.contains(field)) {
            throw IllegalStateException("Duplicated child field '${field.fieldName}' found for '\${this.fieldName}'")
        }

        fields.add(field)

        return field
    }

    // TODO: the fields list cannot really be empty at this point. Consumers **have to** add at least 1 field to an object declaration
    fun fieldsToString(): String = (if (fields.isEmpty()) "" else fields.joinToString(separator = ", ", prefix = " { ", postfix = " }"))

}

open class Interface {
    fun <T : Field<*>> doInit(objectType : ObjectType, field: T, init: T.() -> Unit = {}): T {
        objectType.doInit(field, init)

        return field
    }
}
// TODO: Add KDoc based on introspection results

// TODO: Add support to directives (https://graphql.org/learn/queries/#directives)
// TODO: Add support to inline fragments (https://graphql.org/learn/queries/#inline-fragments)
// TODO: Add support to metafields (https://graphql.org/learn/queries/#meta-fields)
open class Field<T : ScalarType>(val type: T, val fieldName: String, private val alias: String? = null) {
    val arguments: MutableList<Argument> = ArrayList()

    private fun argumentsToString(): String = (if (arguments.isEmpty()) "" else arguments.joinToString(separator = ",", prefix = "(", postfix = ")"))

    override fun toString(): String {
        return "${if (alias != null) "$alias: " else ""}$fieldName${argumentsToString()}" +
                if (type is ObjectType) type.fieldsToString() else ""

    }

    override fun equals(other: Any?): Boolean {
        if (other is Field<*>) {
            return other.fieldName == fieldName && other.alias == alias
        }

        return false
    }

    override fun hashCode(): Int = fieldName.hashCode() * 31 + (alias?.hashCode() ?: 0)
}

fun <T : Field<*>> T.set(name: String, value: Any?): T {
    if (value != null) {
        val argument = Argument(name, value)

        if (arguments.contains(argument)) {
            throw IllegalStateException("Duplicated argument ${argument.name} found for $name")
        }

        arguments.add(argument)
    }
    return this
}


open class Object<T : ObjectType>(type: T, private val parent: Object<out ObjectType>? = null, name: String, alias: String? = null) : Field<T>(type, name, alias) {
    open fun addFragment(fragment: Fragment<out ObjectType>) {
        // TODO stop add references to parent with the single goal of supporting fragments.
        // maybe add a reference to the query all the way down the field tree
        parent?.addFragment(fragment)
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

class QueryType : ObjectType()

class Query(queryName: String?) : Object<QueryType>(QueryType(), name = "query${if (queryName != null) " $queryName" else ""}") {
    private val fragments: MutableCollection<Fragment<out ObjectType>> = ArrayList()

    override fun addFragment(fragment: Fragment<out ObjectType>) {
        if (!fragments.contains(fragment)) {
            fragments.add(fragment)
        }
    }

    private fun fragmentsToString(): String = (if (fragments.isEmpty()) "" else fragments.joinToString(separator = " ", prefix = " "))

    override fun toString(): String {
        return super.toString() + fragmentsToString()
    }
}

fun query(name: String? = null, init: Query.() -> Unit): String = Query(name).apply(init).toString()

data class Fragment<T : ObjectType>(val name: String, val of: T) {
    override fun toString(): String {
        return "fragment $name on ${of::class.simpleName}${of.fieldsToString()}"
    }
}

// TODO add support to fragments that use fragments.
fun <T : ObjectType> fragment(name: String, on: KClass<T>, init: T.() -> Unit): Fragment<T> = Fragment(name, of = on.createInstance().apply(init))

fun <T : ObjectType, O : Object<T>> O.useFragment(fragment: Fragment<T>) {
    addFragment(fragment)

    type.fields.add(object : Field<ScalarType>(ScalarType(), fragment.name) {
        override fun toString(): String = "...$fieldName"
    })
}


// ---- Domain specific: GitHub ---- //

fun Query.viewer(alias: String? = null, init: Viewer.() -> Unit) = type.doInit(Viewer(alias = alias, parent = this), init)

class ActorInterface : Interface() {
    class Login(alias: String? = null) : Field<ScalarType>(ScalarType(), "login", alias)

    fun login(objectType: ObjectType, alias: String? = null) = doInit(objectType, Login(alias))
}

class NodeInterface : Interface() {
    class Id(alias: String? = null) : Field<ScalarType>(ScalarType(), "id", alias)

    fun id(objectType: ObjectType, alias: String? = null) = doInit(objectType, Id(alias))
}

open class User : ObjectType() {
    private val actorInterface = ActorInterface()
    private val nodeInterface = NodeInterface()

    class Email(alias: String? = null) : Field<ScalarType>(ScalarType(), "email", alias)
    class Name(alias: String? = null) : Field<ScalarType>(ScalarType(), "name", alias)

    fun login(alias: String? = null) = actorInterface.login(this, alias)
    fun id(alias: String? = null) = nodeInterface.id(this, alias)
    fun email(alias: String? = null) = doInit(Email(alias))
    fun name(alias: String? = null) = doInit(Name(alias))

    var login: ActorInterface.Login? = null
        get() = actorInterface.login(this)

    var id: NodeInterface.Id? = null
        get() = nodeInterface.id(this)

    var email: Email? = null
        get() = email()

    var name: Name? = null
        get() = name()

    fun pullRequests(parent: Object<*>? = null, alias: String? = null, first: Int? = null, last: Int? = null, init: PullRequestConnection.() -> Unit) =
            doInit(PullRequestConnection(parent, alias), init)
                    .set("first", first)
                    .set("last", last)

}

class Viewer(parent: Object<*>, alias: String? = null) : Object<User>(User(), parent, "viewer", alias) {
    fun login(alias: String? = null) = type.login(alias)
    fun id(alias: String? = null) = type.id(alias)
    fun email(alias: String? = null) = type.email(alias)
    fun name(alias: String? = null) = type.name(alias)

    var login: ActorInterface.Login? = null
        get() = type.login

    var id: NodeInterface.Id? = null
        get() = type.id

    var email: User.Email? = null
        get() = type.email

    var name: User.Name? = null
        get() = type.name

    fun pullRequests(alias: String? = null, first: Int? = null, last: Int? = null, init: PullRequestConnection.() -> Unit) =
            type.pullRequests(this, alias, first, last, init)
}

class PullRequestConnectionType : ObjectType() {
    fun nodes(parent:Object<*>, alias: String? = null, init: PullRequestNode.() -> Unit) =
            doInit(PullRequestNode(parent, alias), init)
}

class PullRequestConnection(parent: Object<*>?, alias: String? = null) :
        Object<PullRequestConnectionType>(PullRequestConnectionType(), parent = parent, name = "pullRequests", alias = alias) {
    fun nodes(alias: String? = null, init: PullRequestNode.() -> Unit) = type.nodes(this, alias, init)
}

class PullRequest : ObjectType() {
    class Body(alias: String? = null) : Field<ScalarType>(ScalarType(), "body", alias)
    class Id(alias: String? = null) : Field<ScalarType>(ScalarType(), "id", alias)

    fun body(alias: String? = null) = doInit(Body(alias))
    fun id(alias: String? = null) = doInit(Id(alias))

    var body: Body? = null
        get() = body()

    var id: Id? = null
        get() = id()
}

class PullRequestNode(parent: Object<*>, alias: String? = null) : Object<PullRequest>(PullRequest(), parent, "nodes", alias) {
    fun body(alias: String? = null) = type.body(alias)
    fun id(alias: String? = null) = type.id(alias)

    var body: PullRequest.Body? = null
        get() = type.body

    var id: PullRequest.Id? = null
        get() = type.id
}
