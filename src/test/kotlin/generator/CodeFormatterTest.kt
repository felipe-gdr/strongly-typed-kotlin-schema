package generator

import org.junit.Assert
import org.junit.Test

class CodeFormatterTest {

    @Test
    fun when__simple_class_is_created__then_generate_simple_class_code() {
        val expected = "class User"
        val result = createClass("User")

        Assert.assertEquals(expected, result)
    }

    @Test
    fun when__open_class_is_created__then_generate_open_class_code() {
        val expected = "open class User"
        val result = createClass("User", open=true)

        Assert.assertEquals(expected, result)
    }

    @Test
    fun when__class_which_extends_other_is_created__then_generate_class_with_extension() {
        val expected = "class User : Person()"
        val result = createClass("User", extends("Person"))

        Assert.assertEquals(expected, result)
    }

    @Test
    fun when__class_which_extends_typed_class_is_created__then_generate_class_with_typed_extension() {
        val expected = "class Admin : Person<User>()"
        val result = createClass("Admin", extends("Person", "User"))

        Assert.assertEquals(expected, result)
    }

    @Test
    fun when__function_with_direct_return_is_created__then_generate_function_with_direct_return() {
        val expected = """
class User {
    fun speak() = "hello"
}
""".trimIndent()
        val result = createClass("User") {
            function("speak") {
                returnValue = "\"hello\""
            }
        }

        Assert.assertEquals(expected, result)
    }

    @Test
    fun when__function_with_body_is_created__then_generate_function_with_body() {
        val expected = """
class User {
    fun speak() {
        return "hello"
    }
}
""".trimIndent()
        val result = createClass("User") {
            function("speak") {
                body("return \"hello\"")
            }
        }

        Assert.assertEquals(expected, result)
    }
}

