package kery

import java.util.*

// TODO: Add KDoc based on introspection results

// TODO: create two base classes (Object and Field), one for fields with mandatory children (e.g. graphql.pullRequests) and
//       one for fields with no children (e.g. name, email, login)
// TODO: Add support to directives (https://graphql.org/learn/queries/#directives)
// TODO: Add support to interfaces (https://graphql.org/learn/schema/#interfaces)
// TODO: Add support to inline fragments (https://graphql.org/learn/queries/#inline-fragments)
// TODO: Add support to metafields (https://graphql.org/learn/queries/#meta-fields)
open class Field(val fieldName: String) {
    val children: MutableList<Field> = ArrayList()
    val attributes: MutableList<Attribute> = ArrayList()

    override fun toString(): String {
        return fieldName +
                (if (attributes.isEmpty()) "" else attributes.joinToString(separator = ",", prefix = "(", postfix = ")")) +
                (if (children.isEmpty()) "" else children.joinToString(separator = ", ", prefix = " { ", postfix = " }"))
    }

    override fun equals(other: Any?): Boolean {
        if(other is Field) {
            return other.fieldName == fieldName
        }

        return false
    }

    override fun hashCode(): Int = fieldName.hashCode()
}

// TODO: rename Attribute to Argument
// TODO: add support to variables in Arguments (https://graphql.org/learn/queries/#variables)
class Attribute(val name: String, val value: Any) {
    override fun toString() = "$name:$value"

    override fun equals(other: Any?): Boolean {
        if(other is Attribute) {
            return other.name == name
        }

        return false
    }

    override fun hashCode(): Int = name.hashCode()
}

fun <T : Field> T.set(name: String, value: Any?): T {
    if (value != null) {
        val attribute = Attribute(name, value)

        if(attributes.contains(attribute)) {
            throw IllegalStateException("Duplicated attribute ${attribute.name} found for ${this.fieldName}")
        }

        attributes.add(attribute)
    }
    return this
}

fun <T : Field> Field.doInit(field: T, init: T.() -> Unit): T {
    field.init()

    if(children.contains(field)) {
        throw IllegalStateException("Duplicated child field ${field.fieldName} found for ${this.fieldName}")
    }

    children.add(field)
    return field
}

class Query : Field("query")

// TODO: Add support to aliases (https://graphql.org/learn/queries/#aliases)
// TODO: Add support to operation names in queries (https://graphql.org/learn/queries/#operation-name)
fun query(init: Query.() -> Unit): String = Query().apply(init).toString()

// ---- Domain specific: GitHub ---- //
class Viewer : Field("viewer") {
    fun email(): Email? = email
    fun login(): Login? = login
    fun name(): Name? = name

    var email: Email? = null
        get() {
            return doInit(Email()) {}
        }
    var login: Login? = null
        get() {
            return doInit(Login()) {}
        }
    var name: Name? = null
        get() {
            return doInit(Name()) {}
        }

    fun Viewer.pullRequests(first: Int? = null, last: Int? = null, init: PullRequests.() -> Unit) =
            doInit(PullRequests(), init).set("first", first).set("last", last)

}

class Login : Field("login")
class Email : Field("email")
class Name : Field("name")
class Id : Field("id")
class PullRequests : Field("pullRequests") {
    var id: Id? = null
        get() {
            return doInit(Id()) {}
        }
    fun id(): Id? = id
}

fun Query.viewer(init: Viewer.() -> Unit) = doInit(Viewer(), init)

