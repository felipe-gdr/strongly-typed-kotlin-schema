package graphql.client.code.generator

import source.code.generator.createClass

class TypeBuilder(val name: String) {
    private val fields = ArrayList<FieldBuilder>()

    fun addField(fieldBuilder: FieldBuilder): TypeBuilder {
        fields.add(fieldBuilder)
        return this
    }

    fun build(): String {
        return createClass(name = name, open = true) {
            extends("Type") {
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
                        field.arguments.forEach { arg ->
                            +argument(name = arg.name, type = arg.type, required = false, defaultValue = arg.defaultValue
                                    ?: "null")
                        }

                        +argument(name = "alias", type = "String", required = false, defaultValue = "null")
                    }
                    returnValue = "doInit(${field.className}(alias))"
                }
                property(name = field.name, type = field.className) {
                    getter("${field.name}()")
                }
            }
        }
    }
}
