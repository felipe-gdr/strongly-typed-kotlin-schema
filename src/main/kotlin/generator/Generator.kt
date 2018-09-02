package generator

import antlr.tool.GraphQLLexer
import antlr.tool.GraphQLParser
import org.antlr.v4.runtime.ANTLRInputStream
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.tree.ParseTreeWalker
import java.io.ByteArrayInputStream

class Generator {
    fun generate(schema: String): String {
        val byteInputStream = ByteArrayInputStream(schema.toByteArray())

        val antlrInputStream = ANTLRInputStream(byteInputStream)

        val lexer = GraphQLLexer(antlrInputStream)

        val tokens = CommonTokenStream(lexer)

        val parser = GraphQLParser(tokens)

        val tree = parser.document()

        val walker = ParseTreeWalker()

        val listener = Listener()

        walker.walk(listener, tree)

        return listener.generatedCode()
    }
}
