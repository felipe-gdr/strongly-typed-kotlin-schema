package generator

class TypeBuilder(val name: String) {
    private val fields = ArrayList<FieldBuilder>()

    fun addField(fieldBuilder: FieldBuilder): TypeBuilder {
        fields.add(fieldBuilder)
        return this
    }

    fun build(): String {
        val fieldsString = fields.joinToString("\n")
        { field -> "${field.buildClass()}\n${field.buildFunction()}\n${field.buildGetter()}" }
        return """
open class $name : Type() {
$fieldsString
}
"""
    }
}