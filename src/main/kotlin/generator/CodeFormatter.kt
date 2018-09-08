package generator

fun String.trimLineBreaks(): String {
    return this.trim('\n', '\r')
}

interface Element {
    fun render(builder: StringBuilder, indent: String)
}

class Property(val name: String, val type: String, val required: Boolean, defaultValue: String) {

    fun getter(definition: String) {

    }
}

abstract class Callable(val keyword: String, val name: String, val returnType: String?) : Element {
    private val arguments = Arguments()
    private var body: String? = null

    var returnValue: String? = null
        set(value) {
            body = null
            field = value
        }

    fun body(text: String) {
        returnValue = null
        body = text
    }

    fun arguments(init: Arguments.() -> Unit): Arguments {
        return arguments.apply(init)
    }

    override fun render(builder: StringBuilder, indent: String) {
        if (body != null) {
            builder.append("$indent$keyword $name")
            builder.append(if (arguments.isEmpty()) "()" else arguments.toString())
            builder.append(if (returnType == null) "" else " : $returnType")
            builder.append(""" {
$indent$indent$body
$indent}
""".trimLineBreaks())
        } else if (returnValue != null) {
            builder.append("$indent$keyword $name() = $returnValue")
        }
    }
}

private class Function(name: String, returnType: String?) : Callable("fun", name, returnType)

class FunctionCall(val functionName: String) {
    val parameters = ArrayList<Parameter>()

}

class Parameter(val name: String) {

}

class Argument internal constructor(val name: String, val type: String, private val required: Boolean, private val defaultValue: String?) {
    override fun toString(): String {
        return "$name: $type${if (required) "" else "?"}${if (defaultValue == null) "" else " = $defaultValue"}"
    }
}

class Arguments internal constructor() {
    private val arguments = ArrayList<Argument>()

    internal fun isEmpty() = arguments.isEmpty()

    operator fun Argument.unaryPlus() {
        arguments.add(this)
    }

    override fun toString(): String {
        return "(${arguments.joinToString(", ") { it.toString() }})"
    }
}

class _Class internal constructor(private val name: String, private val open: Boolean, private val anExtends: Extends?) : Element {
    private val elements = ArrayList<Element>()
    private var primaryConstructor = Arguments()

    fun function(name: String, returnType: String? = null, init: Callable.() -> Unit = {}): Callable {
        val function = Function(name, returnType)
        elements.add(function)
        return function.apply(init)
    }

    fun constructor(init: Arguments.() -> Unit): Arguments {
        return primaryConstructor.apply(init)
    }

    fun argument(name: String, type: String, required: Boolean = true, defaultValue: String? = null) =
            Argument(name, type, required, defaultValue)

    fun argument(name: String, type: String, defaultValue: String? = null) =
            Argument(name, type, true, defaultValue)

    override fun render(builder: StringBuilder, indent: String) {
        builder.append("${if (open) "open " else ""}class $name")
        builder.append("${anExtends ?: ""}")
        builder.append(if (primaryConstructor.isEmpty()) "" else primaryConstructor.toString())

        if (elements.isNotEmpty()) {
            builder.append(" {\n")
            elements.map { it.render(builder, indent) }
            builder.append("\n}")
        }
    }

    override fun toString(): String {
        val stringBuilder = StringBuilder()
        render(stringBuilder, "    ")
        return stringBuilder.toString()
    }

    class Extends(private val superClassName: String, private val superClassType: String?) {
        override fun toString(): String {
            return " : $superClassName${if (superClassType != null) "<$superClassType>" else ""}()"
        }
    }
}

fun extends(superClassName: String, superClassType: String? = null): _Class.Extends {
    return _Class.Extends(superClassName, superClassType)
}

fun createClass(name: String, anExtends: _Class.Extends? = null, init: _Class.() -> Unit = {}): String {
    return createClass(name, false, anExtends, init)
}

fun createClass(name: String, open: Boolean = false, anExtends: _Class.Extends? = null, init: _Class.() -> Unit = {}): String {
    return _Class(name, open, anExtends).apply(init).toString()
}

