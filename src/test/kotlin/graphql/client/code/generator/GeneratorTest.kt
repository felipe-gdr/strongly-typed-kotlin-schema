package graphql.client.code.generator

import org.junit.Assert.assertEquals
import org.junit.Test

class GeneratorTest {
    @Test
    fun when__scalars_are_defined__then_scalar_classes_are_generated() {
        val schema = """
scalar URI
scalar Date
"""
        val expected = """
class URI : ScalarType()
class Date : ScalarType()
""".trim()

        val generator = Generator()

        val result = generator.generate(schema).trim()

        assertEquals(expected, result)
    }

    @Test
    fun when__interface_is_defined__then_interface_class_is_generated() {
        val schema = """
interface Actor {
  avatarUrl: String
}
"""
        val expected = """
class Actor : Interface() {
    class AvatarUrl(alias: String? = null) : Field<ScalarType>(ScalarType(), "avatarUrl", alias)
    fun avatarUrl(type: Type, alias: String? = null) = doInit(type, AvatarUrl(alias))
}
""".trim()

        val generator = Generator()

        val result = generator.generate(schema).trim()

        assertEquals(expected, result)
    }

    @Test
    fun when__interface_is_defined_containing_type_with_argument__then_arguments_are_present_in_generated_code() {
        val schema = """
interface Actor {
  avatarUrl(size: Int): String
}
"""
        val expected = """
class Actor : Interface() {
    class AvatarUrl(alias: String? = null) : Field<ScalarType>(ScalarType(), "avatarUrl", alias)
    fun avatarUrl(type: Type, size: Int? = null, alias: String? = null) = doInit(type, AvatarUrl(alias)).set("size", size)
}
""".trim()

        val generator = Generator()

        val result = generator.generate(schema).trim()

        assertEquals(expected, result)
    }

    @Test
    fun when__default_value_is_used_in_interface_field__then_default_value_is_present_in_generated_code() {
        val schema = """
interface Actor {
  avatarUrl(size: Int = 400): String
}
"""
        val expected = """
class Actor : Interface() {
    class AvatarUrl(alias: String? = null) : Field<ScalarType>(ScalarType(), "avatarUrl", alias)
    fun avatarUrl(type: Type, size: Int? = 400, alias: String? = null) = doInit(type, AvatarUrl(alias)).set("size", size)
}
""".trim()

        val generator = Generator()

        val result = generator.generate(schema).trim()

        assertEquals(expected, result)
    }

    @Test
    fun when__type_is_defined__then_generated_code_contains_type_classes() {
        val schema = """
type User {
  name: String
}
"""
        val expected = """
open class User : Type() {
    class Name(alias: String? = null) : Field<ScalarType>(ScalarType(), "name", alias)
    fun name(alias: String? = null) = doInit(Name(alias))
    var name: Name? = null
        get() = name()
}
""".trim()

        val generator = Generator()

        val result = generator.generate(schema).trim()

        assertEquals(expected, result)
    }
}
