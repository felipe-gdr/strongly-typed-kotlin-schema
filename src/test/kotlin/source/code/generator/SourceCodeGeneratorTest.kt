package source.code.generator

import org.junit.Assert.assertEquals
import org.junit.Test

class SourceCodeGeneratorTest {

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
        val result = createClass(name = "User", open = true)

        assertEquals(expected, result)
    }

    @Test
    fun when__class_which_extends_other_is_created__then_generate_class_with_extension() {
        val expected = "class User : Person()"
        val result = createClass("User") {
            extends("Person")
        }

        assertEquals(expected, result)
    }

    @Test
    fun when__class_which_extends_typed_class_is_created__then_generate_class_with_typed_extension() {
        val expected = "class Admin : Person<User>()"
        val result = createClass("Admin") {
            extends(superClassName = "Person", superClassType = "User")
        }

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
                +argument(name = "name", type = "String", defaultValue = "null")
                +argument(name = "age", type = "Int", required = false)
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
            function(name = "doStuff", returnType = "String") {
                arguments {
                    +argument(name = "stuff", type = "String")
                }
                body("return stuff")
            }
        }

        assertEquals(expected, result)
    }

    @Test
    fun when__function_has_arguments_and_no_body__then_generate_function_with_arguments() {
        val expected = """
class User {
    fun doStuff(stuff: String) = stuff
}
""".trimIndent()
        val result = createClass("User") {
            function("doStuff") {
                arguments {
                    +argument(name = "stuff", type = "String")
                }
                returnValue = "stuff"
            }
        }

        assertEquals(expected, result)
    }

    @Test
    fun when__extended_class_has_constructor_call__then_generate_extended_class() {
        val expected = "class User(name: String) : Person(name)"

        val result = createClass("User") {
            constructor {
                +argument(name = "name", type = "String")
            }
            extends("Person") {
                constructorCall {
                    +"name"
                }
            }
        }

        assertEquals(expected, result)
    }

    @Test
    fun when__inner_classes_are_used__then_code_is_properly_formatted() {
        val expected = """
class User {
    class Address {
        class City
    }
}
""".trimLineBreaks()

        val result = createClass("User") {
            innerClass("Address") {
                innerClass("City")
            }
        }

        assertEquals(expected, result)
    }
}

