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
                .map { it.name().text }
                .forEach { builder.addField(it)}

        stringBuilder.append(builder.build()).append("\n")
    }

    fun generatedCode() = stringBuilder.toString()
}

