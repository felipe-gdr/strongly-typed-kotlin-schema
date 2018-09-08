package generator

import com.sun.scenario.effect.impl.prism.PrEffectHelper.render

interface Element {
    fun render(builder: StringBuilder, indent: String)
}

class Property(val name: String, val type: String, val required: Boolean, defaultValue: String) {

    fun getter(definition: String) {

    }
}

class Function(val name: String) : Element {
    var body: String? = null

    var returnValue: String? = null
        set(value) {
            body = null
            field = value
        }

    fun body(text: String) {
        returnValue = null
        body = text
    }

    override fun render(builder: StringBuilder, indent: String) {
        if(body != null) {
            builder.append("""${indent}fun $name() {
$indent$indent$body
$indent}""")
        } else if(returnValue != null) {
            builder.append("${indent}fun $name() = $returnValue")
        }
    }
}

class FunctionCall(val functionName: String) {
    val parameters = ArrayList<Parameter>()

}

class Parameter(val name: String) {

}

class Argument(val name: String, val type: String, val required: Boolean = false, val defaultValue: String = "") {

}

class Constructor {
   val arguments = ArrayList<Argument>()
}


class _Class(val name: String, val open: Boolean, val extends: ClassExtends?) : Element {
    class ClassExtends(val superClassName: String, val superClassType: String?) {
        override fun toString(): String {
            return " : ${superClassName}${if (superClassType != null) "<$superClassType>" else ""}()"
        }
    }

    val elements = ArrayList<Element>()

    fun function(name: String, init: Function.() -> Unit = {}): Function {
        val function = Function(name)
        elements.add(function)
        return function.apply(init)
    }

    fun property(name: String, type: String, required: Boolean = false, defaultValue: String = "") {
//        elements.add(Property(name, type, required, defaultValue))
    }

    override fun render(builder: StringBuilder, indent: String) {
        builder.append("${if (open) "open " else ""}class $name${extends ?: ""}")

        if(elements.isNotEmpty()) {
            builder.append(" {\n")
            elements.map { it.render(builder, indent)}
            builder.append("\n}")
        }

    }

    override fun toString(): String {
        val stringBuilder = StringBuilder()
        render(stringBuilder, "    ")
        return stringBuilder.toString()
    }
}

fun extends(superClassName: String, superClassType: String? = null): _Class.ClassExtends {
   return _Class.ClassExtends(superClassName, superClassType)
}

fun createClass(name: String, extends: _Class.ClassExtends? = null, init: _Class.() -> Unit = {}): String {
    return createClass(name, false, extends, init)
}

fun createClass(name: String, open: Boolean = false, extends: _Class.ClassExtends? = null, init: _Class.() -> Unit = {}): String {
    return _Class(name, open, extends).apply(init).toString()
}

