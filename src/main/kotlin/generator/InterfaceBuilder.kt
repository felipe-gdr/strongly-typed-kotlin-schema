package generator

class InterfaceBuilder(val name: String) {
    private val fields = ArrayList<FieldBuilder>()

    fun addField(fieldBuilder: FieldBuilder): InterfaceBuilder {
        fields.add(fieldBuilder)
        return this
    }

    fun build(): String {
        val fieldsString = fields.joinToString("\n") { field -> field.build() }
        val interfaceString = """
class $name : Interface() {
$fieldsString
}
"""
       return interfaceString
    }
}