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

    @Test fun when__query_name_is_used__then_generated_query_string_contains_it() {
        val expected = "query MyQuery { viewer { login, name, email, pullRequests(last:5) { id } } }"

        val result = query("MyQuery") {
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

    @Test fun when__fields_have_aliases__then_generated_query_string_contains_aliases() {
        val expected = "query { aViewer: viewer { aLogin: login, myName: name, someEmail: email, aPullRequest: pullRequests(last:5) { theBestId: id } } }"

        val result = query {
            viewer(alias="aViewer") {
                login(alias="aLogin")
                name(alias="myName")
                email(alias="someEmail")
                pullRequests(last = 5, alias="aPullRequest") {
                    id(alias="theBestId")
                }
            }
        }

        assertEquals(expected, result)
    }
}
