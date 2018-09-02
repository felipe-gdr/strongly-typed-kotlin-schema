package generator

class FieldBuilder(val name: String) {
    private val className = name.capitalize()

    private val classTemplate =  "class $className(alias: String? = null) : Field<ScalarType>(ScalarType(), " +
            "\"$name\", alias)"
    private val functionTemplate = "fun $name(type: Type, alias: String? = null) = doInit(type, $className(alias))"

    fun build() = "$classTemplate\n$functionTemplate"
}

private class ArgumentBuilder<T>(val name: String, val type: Class<T>, val defaultValue: T?) {
    fun build() {
        val argument = "$name: "
    }
}


