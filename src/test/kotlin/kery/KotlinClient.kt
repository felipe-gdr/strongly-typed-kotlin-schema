package kery

import org.junit.Assert.assertEquals
import org.junit.Test


class KotlinClient {

    @Test fun when__structure_is_correct__then_string_is_correctly_built() {
        val expected = "query { viewer { login, name, email, pullRequests(last:5) { id } } }"

        val result = query {
            viewer {
                login
                name
                email
                pullRequests(last = 5) {
                    id
                }
            }
        }

        assertEquals(expected, result)
    }

    @Test(expected = IllegalStateException::class)
    fun when__same_child_field_is_defined_twice__then_an_exception_is_thrown() {
        query {
            viewer {
                login
                name
                email
                pullRequests(last = 5) {
                    login
                }
            }
        }
    }
}
