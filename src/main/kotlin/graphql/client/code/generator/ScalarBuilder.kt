package graphql.client.code.generator

import source.code.generator.createClass

class ScalarBuilder(val name: String) {
    fun build() = createClass(name) {
        extends("ScalarType")
    }
}
