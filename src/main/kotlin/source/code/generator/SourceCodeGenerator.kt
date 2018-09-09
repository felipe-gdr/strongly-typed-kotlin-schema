package source.code.generator

fun String.trimLineBreaks(): String {
    return this.trim('\n', '\r')
}

fun String.indent(indent: String, indentLevel: Int): String {
    return IntRange(1, indentLevel).fold(this) { acc, _ -> "$indent$acc" }
}

interface Element {
    fun render(builder: StringBuilder, indent: String, indentLevel: Int)
}

class Property(val name: String, val type: String, private val defaultValue: String?) : Element {
    private var getterDef: String? = null

    fun getter(definition: String) {
        getterDef = definition
    }

    override fun render(builder: StringBuilder, indent: String, indentLevel: Int) {
        builder.append("var $name: $type? = ${defaultValue ?: "null"}".indent(indent, indentLevel))
        if (getterDef != null) {
            builder.append("\n")
            builder.append("get() = $getterDef".indent(indent, indentLevel + 1))
        }
    }
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

    override fun render(builder: StringBuilder, indent: String, indentLevel: Int) {
        builder.append("$keyword $name".indent(indent, indentLevel))
        builder.append(if (arguments.isEmpty()) "()" else arguments.toString())
        builder.append(if (returnType == null) "" else " : $returnType")

        if (body != null) {
            builder.append(""" {
${"$indent$body".indent(indent, indentLevel)}
${"}".indent(indent, indentLevel)}
""".trimLineBreaks())
        } else if (returnValue != null) {
            builder.append(" = $returnValue")
        }
    }
}

private class Function(name: String, returnType: String?) : Callable("fun", name, returnType)

//class FunctionCall(private val functionName: String, private val hasReceiver: Boolean = false) {
//    private val parameters = Parameters()
//
//    override fun toString(): String {
//        return "${if (hasReceiver) "." else ""}${functionName ?: ""}$parameters"
//    }
//}

class _Class internal constructor(private val name: String, private val open: Boolean) : Element {
    private val elements = ArrayList<Element>()
    private var primaryConstructor: Arguments? = null
    private var extends: Extends? = null

    fun extends(superClassName: String, superClassType: String? = null, init: Extends.() -> Unit = {}): Extends? {
        extends = Extends(superClassName, superClassType).apply(init)
        return extends
    }

    fun function(name: String, returnType: String? = null, init: Callable.() -> Unit = {}): Callable {
        val function = Function(name, returnType)
        elements.add(function)
        return function.apply(init)
    }

    fun constructor(init: Arguments.() -> Unit): Arguments? {
        primaryConstructor = Arguments().apply(init)
        return primaryConstructor
    }

    fun argument(name: String, type: String, required: Boolean = true, defaultValue: String? = null) =
            Argument(name, type, required, defaultValue)

    fun argument(name: String, type: String, defaultValue: String? = null) =
            Argument(name, type, true, defaultValue)

    fun property(name: String, type: String, defaultValue: String? = null, init: Property.() -> Unit = {}): Property {
        val property = Property(name, type, defaultValue).apply(init)
        elements.add(property)
        return property
    }

    fun innerClass(name: String, open: Boolean = false, init: _Class.() -> Unit = {}): _Class {
        val innerClass = _Class(name, open).apply(init)
        elements.add(innerClass)
        return innerClass
    }

    override fun render(builder: StringBuilder, indent: String, indentLevel: Int) {
        builder.append("${if (open) "open " else ""}class $name".indent(indent, indentLevel))
        builder.append(primaryConstructor ?: "")
        builder.append(extends ?: "")

        if (elements.isNotEmpty()) {
            builder.append(" {\n")
            elements.forEach {
                it.render(builder, indent, indentLevel + 1)
                builder.append("\n")
            }
            builder.append("}".indent(indent, indentLevel))
        }
    }

    override fun toString(): String {
        val stringBuilder = StringBuilder()
        render(stringBuilder, "    ", 0)
        return stringBuilder.toString()
    }

    class Extends(private val superClassName: String, private val superClassType: String?) {
        private var constructorCall = Parameters()

        fun constructorCall(init: Parameters.() -> Unit = {}): Parameters {
            return constructorCall.apply(init)
        }

        override fun toString(): String {
            return " : $superClassName${if (superClassType != null) "<$superClassType>" else ""}$constructorCall"
        }

        class Parameters {
            private val parameters = ArrayList<String>()

            operator fun String.unaryPlus() {
                parameters.add(this)
            }

            override fun toString(): String {
                return "(${parameters.joinToString(", ")})"
            }
        }
    }
}

fun createClass(name: String, open: Boolean = false, init: _Class.() -> Unit = {}): String {
    return _Class(name, open).apply(init).toString()
}

