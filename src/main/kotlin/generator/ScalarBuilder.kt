package generator

class ScalarBuilder(val name: String) {
    private val template = "class \$name : ScalarType()"

    fun build() = template.replace("\$name", name)
}