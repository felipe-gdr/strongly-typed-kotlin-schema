package generator

class FieldBuilder(val name: String) {
    private val arguments = ArrayList<ArgumentBuilder>()

    fun addArgument(argumentBuilder: ArgumentBuilder):FieldBuilder {
        arguments.add(argumentBuilder)
        return this
    }

    fun build(): String {
        val className = name.capitalize()

        val classTemplate =  "class $className(alias: String? = null) : Field<ScalarType>(ScalarType(), \"$name\", alias)"
        val functionTemplate = "fun $name(type: Type, " +
                arguments.joinToString(","){it.buildFunctionArgs()} +
                (if (arguments.isEmpty()) "" else ", ") +
                "alias: String? = null) = doInit(type, $className(alias))" +
                arguments.joinToString(",") {it.buildSetter()}

        return "$classTemplate\n$functionTemplate"
    }
}

class ArgumentBuilder(val name: String) {
    private lateinit var type: String
    private var defaultValue: String? = null

    fun type(type: String): ArgumentBuilder {
        this.type = type
        return this
    }

    fun defaultValue(value: String?): ArgumentBuilder {
        this.defaultValue = value
        return this
    }

    fun buildSetter() = ".set(\"$name\", $name)"
    fun buildFunctionArgs() = "$name: $type? = ${defaultValue?:"null"}"
}

