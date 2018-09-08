package generator

class ScalarBuilder(val name: String) {
    fun build() = createClass(name, extends("ScalarType"))
}
