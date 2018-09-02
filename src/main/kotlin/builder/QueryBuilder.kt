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
    fun fieldsToString(): String =
            (if (fields.isEmpty()) "" else fields.joinToString(separator = ", ", prefix = " { ", postfix = " }"))
}

open class Interface {
    fun <T : Field<*>> doInit(objectType: ObjectType, field: T, init: T.() -> Unit = {}): T {
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

    private fun argumentsToString(): String =
            (if (arguments.isEmpty()) "" else arguments.joinToString(separator = ",", prefix = "(", postfix = ")"))

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

open class Object<T : ObjectType>(type: T, private val parent: Object<out ObjectType>? = null, name: String,
                                  alias: String? = null) : Field<T>(type, name, alias) {
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

class Query(queryName: String?) :
        Object<QueryType>(QueryType(), name = "query${if (queryName != null) " $queryName" else ""}") {
    private val fragments: MutableCollection<Fragment<out ObjectType>> = ArrayList()

    override fun addFragment(fragment: Fragment<out ObjectType>) {
        if (!fragments.contains(fragment)) {
            fragments.add(fragment)
        }
    }

    private fun fragmentsToString(): String =
            (if (fragments.isEmpty()) "" else fragments.joinToString(separator = " ", prefix = " "))

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
fun <T : ObjectType> fragment(name: String, on: KClass<T>, init: T.() -> Unit): Fragment<T> =
        Fragment(name, of = on.createInstance().apply(init))

fun <T : ObjectType, O : Object<T>> O.useFragment(fragment: Fragment<T>) {
    addFragment(fragment)

    type.fields.add(object : Field<ScalarType>(ScalarType(), fragment.name) {
        override fun toString(): String = "...$fieldName"
    })
}
