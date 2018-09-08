package generator

import org.junit.Assert.assertEquals
import org.junit.Test

class CodeFormatterTest {

    @Test
    fun trimLineBreak__when_string_without_line_break__then_return_string_itself() {
        val expected = "test"
        val result = "test".trimLineBreaks()

        assertEquals(expected, result)
    }

    @Test
    fun trimLineBreak__when_string_with_line_break_at_start_and_end__then_return_string_without_line_breaks() {
        val expected = "test"
        val result = """

test

""".trimLineBreaks()

        assertEquals(expected, result)
    }

    @Test
    fun trimLineBreak__when_string_with_line_break_inside_content__then_return_string_with_line_break() {
        val expected = """test 1

test 2"""
        val result = """
test 1

test 2
""".trimLineBreaks()

        assertEquals(expected, result)
    }

    @Test
    fun when__simple_class_is_created__then_generate_simple_class_code() {
        val expected = "class User"
        val result = createClass("User")

        assertEquals(expected, result)
    }

    @Test
    fun when__open_class_is_created__then_generate_open_class_code() {
        val expected = "open class User"
        val result = createClass("User", open=true)

        assertEquals(expected, result)
    }

    @Test
    fun when__class_which_extends_other_is_created__then_generate_class_with_extension() {
        val expected = "class User : Person()"
        val result = createClass("User", extends("Person"))

        assertEquals(expected, result)
    }

    @Test
    fun when__class_which_extends_typed_class_is_created__then_generate_class_with_typed_extension() {
        val expected = "class Admin : Person<User>()"
        val result = createClass("Admin", extends("Person", "User"))

        assertEquals(expected, result)
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

        assertEquals(expected, result)
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

        assertEquals(expected, result)
    }

    @Test
    fun when__class_has_constructor__then_generate_primary_constructor() {
        val expected = """
class User(name: String = null, age: Int?)
""".trimIndent()
        val result = createClass("User") {
            constructor {
                +argument("name", "String", "null")
                +argument("age", "Int", false)
            }
        }

        assertEquals(expected, result)
    }

    @Test
    fun when__function_has_arguments__then_generate_function_with_arguments() {
        val expected = """
class User {
    fun doStuff(stuff: String) : String {
        return stuff
    }
}
""".trimIndent()
        val result = createClass("User") {
            function("doStuff", "String") {
                arguments {
                    +argument("stuff", "String")
                }
                body("return stuff")
            }
        }

        assertEquals(expected, result)
    }
}

