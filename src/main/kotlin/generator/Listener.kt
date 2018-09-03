package generator

import antlr.tool.GraphQLBaseListener;
import antlr.tool.GraphQLParser

class Listener : GraphQLBaseListener() {
    private val stringBuilder = StringBuilder()

    override fun enterScalarDefinition(ctx: GraphQLParser.ScalarDefinitionContext) {
        val name = ctx.name().text

        val builder = ScalarBuilder(name)

        stringBuilder.append(builder.build()).append("\n")
    }

    override fun enterInterfaceDefinition(ctx: GraphQLParser.InterfaceDefinitionContext) {
        val name = ctx.name().text

        val builder = InterfaceBuilder(name)

        ctx.interfaceFieldSet().interfaceField()
                .forEach { field ->
                    val fieldBuilder = FieldBuilder(field.name().text)

                    field.typeArguments()?.typeArgument()?.forEach { argument ->
                        val argumentName = argument.name().text
                        val type = argument.type().typeName().text
                        val defaultValue = argument.defaultValue()?.value()?.text

                        val argumentBuilder = ArgumentBuilder(argumentName)
                                .type(type)
                                .defaultValue(defaultValue)

                        fieldBuilder.addArgument(argumentBuilder)
                    }

                    builder.addField(fieldBuilder)
                }

        stringBuilder.append(builder.build()).append("\n")
    }

    fun generatedCode() = stringBuilder.toString()
}

