package generator

class FieldBuilder(val name: String, private val isInterface: Boolean = false) {
    private val className = name.capitalize()
    private val arguments = ArrayList<ArgumentBuilder>()

    fun addArgument(argumentBuilder: ArgumentBuilder):FieldBuilder {
        arguments.add(argumentBuilder)
        return this
    }

    fun buildClass(): String =
            "class $className(alias: String? = null) : Field<ScalarType>(ScalarType(), \"$name\", alias)"

    fun buildFunction(): String =
            "fun $name(${if (isInterface) "type: Type, " else ""}" +
            arguments.joinToString(","){it.buildFunctionArgs()} +
            (if (arguments.isEmpty()) "" else ", ") +
            "alias: String? = null) = doInit(${if (isInterface) "type, " else ""}$className(alias))" +
            arguments.joinToString(",") {it.buildSetter()}

    fun buildGetter(): String =
            """var $name: $className? = null
get() = $name()
""".trim()
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

