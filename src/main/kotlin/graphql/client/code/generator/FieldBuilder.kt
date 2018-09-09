package graphql.client.code.generator

class FieldBuilder(val name: String) {
    val className = name.capitalize()
    val arguments = ArrayList<ArgumentBuilder>()

    fun addArgument(argumentBuilder: ArgumentBuilder): FieldBuilder {
        arguments.add(argumentBuilder)
        return this
    }
}

class ArgumentBuilder(val name: String) {
    lateinit var type: String
    var defaultValue: String? = null

    fun type(type: String): ArgumentBuilder {
        this.type = type
        return this
    }

    fun defaultValue(value: String?): ArgumentBuilder {
        this.defaultValue = value
        return this
    }
}

