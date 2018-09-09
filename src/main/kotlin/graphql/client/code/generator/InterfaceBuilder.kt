package graphql.client.code.generator

import source.code.generator.createClass

class InterfaceBuilder(val name: String) {
    private val fields = ArrayList<FieldBuilder>()

    fun addField(fieldBuilder: FieldBuilder): InterfaceBuilder {
        fields.add(fieldBuilder)
        return this
    }

    fun build(): String {
        return createClass(name) {
            extends("Interface") {
                constructorCall()
            }

            fields.forEach { field ->
                innerClass(field.className) {
                    constructor {
                        +argument(name = "alias", type = "String", required = false, defaultValue = "null")
                    }
                    extends("Field", "ScalarType") {
                        constructorCall {
                            +"ScalarType()"
                            +"\"${field.name}\""
                            +"alias"
                        }
                    }
                }
                function(field.name) {
                    arguments {
                        +argument("type", "Type")

                        field.arguments.forEach { arg ->
                            +argument(name = arg.name, type = arg.type, required = false, defaultValue = arg.defaultValue
                                    ?: "null")
                        }

                        +argument(name = "alias", type = "String", required = false, defaultValue = "null")
                    }
                    returnValue = "doInit(type, ${field.className}(alias))${field.arguments.joinToString("") { ".set(\"${it.name}\", ${it.name})" }}"
                }
            }
        }
    }
}
