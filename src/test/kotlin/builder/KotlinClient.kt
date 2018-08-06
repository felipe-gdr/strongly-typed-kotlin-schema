package builder

import org.junit.Assert.assertEquals
import org.junit.Test

class KotlinClient {

    @Test
    fun when__structure_is_correct__then_string_is_correctly_built() {
        val expected = "query { viewer { login, name, email, pullRequests(last:5) { nodes { body, id } } } }"

        val result = query {
            viewer {
                login
                name
                email
                pullRequests(last = 5) {
                    nodes {
                        body
                        id
                    }
                }
            }
        }

        assertEquals(expected, result)
    }

    @Test
    fun when__order_of_field_declaration_changes__then_generated_query_string_respects_the_order() {
        val expected = "query { viewer { pullRequests(last:5) { nodes { body } }, name, email, login } }"


        val result = query {
            viewer {
                pullRequests(last = 5) {
                    nodes {
                        body
                    }
                }
                name
                email
                login
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

    @Test
    fun when__query_name_is_used__then_generated_query_string_contains_it() {
        val expected = "query MyQuery { viewer { login, name, email, pullRequests(last:5) { nodes { body } } } }"

        val result = query("MyQuery") {
            viewer {
                login
                name
                email
                pullRequests(last = 5) {
                    nodes {
                        body
                    }
                }
            }
        }

        assertEquals(expected, result)
    }

    @Test
    fun when__fields_have_aliases__then_generated_query_string_contains_aliases() {
        val expected = "query { aViewer: viewer { aLogin: login, myName: name, someEmail: email, aPullRequest: pullRequests(last:5) { nodes { aBody: body, pullRequestId: id } } } }"

        val result = query {
            viewer(alias = "aViewer") {
                login(alias = "aLogin")
                name(alias = "myName")
                email(alias = "someEmail")
                pullRequests(last = 5, alias = "aPullRequest") {
                    nodes {
                        body(alias = "aBody")
                        id(alias= "pullRequestId")
                    }
                }
            }
        }

        assertEquals(expected, result)
    }

    @Test
    fun when__same_field_is_declared_twice_with_different_aliases__then_generated_string_contains_both_definitions() {
        val expected = "query { aViewer: viewer { login, name, email, pullRequests(last:5) { nodes { body } } }, anotherViewer: viewer { login } }"

        val result = query {
            viewer(alias = "aViewer") {
                login
                name
                email
                pullRequests(last = 5) {
                    nodes {
                        body
                    }
                }
            }
            viewer(alias = "anotherViewer") {
                login
            }
        }

        assertEquals(expected, result)
    }

//    @Test
//    fun when__fragment_is_defined__then_resulting_string_contains_fragment_syntax() {
//        val expected = "fragment viewerFragment on Viewer { login, name }"
//
//        val viewerFragment = fragment(name = "viewerFragment", on = Viewer::class) {
//            login
//            name
//        }
//
//        assertEquals(expected, viewerFragment.toString())
//    }
//
//    @Test
//    fun when__fragment_is_define_together_with_aliases__then_generated_query_string_contains_fragment_and_aliases() {
//        val expected = "fragment viewerFragment on Viewer { aLogin: login, myName: name, someEmail: email, aPullRequest: pullRequests(last:5) { nodes { body } } }"
//
//        val viewerFragment = fragment(name = "viewerFragment", on = Viewer::class) {
//            login(alias = "aLogin")
//            name(alias = "myName")
//            email(alias = "someEmail")
//            pullRequests(last = 5, alias = "aPullRequest") {
//                nodes {
//                    body
//                }
//            }
//        }
//
//        assertEquals(expected, viewerFragment.toString())
//    }
//
//    @Test
//    fun when__fragment_is_used_in_query_then_resulting_string_contains_fragment_definition_and_usage() {
//        val expected = "query { viewer { ...viewerFragment } } fragment viewerFragment on Viewer { login, name }"
//
//        val fragment = fragment(name = "viewerFragment", on = Viewer::class) {
//            login
//            name
//        }
//
//        val result = query {
//           viewer {
//               useFragment(fragment)
//           }
//        }
//
//        assertEquals(expected, result)
//    }
//
//    @Test
//    fun when__fragment_is_used_twice_in_query__then_resulting_string_contains_two_usages_and_just_one_fragment_definition() {
//        val expected = "query { aViewer: viewer { ...viewerFragment }, anotherViewer: viewer { ...viewerFragment } } fragment viewerFragment on UserType { login, name }"
//
//        val fragment = fragment(name = "viewerFragment", on = UserType::class) {
//            login
//            name
//        }
//
//        val result = query {
//            viewer(alias = "aViewer") {
//                useFragment(fragment)
//            }
//            viewer(alias = "anotherViewer") {
//                useFragment(fragment)
//            }
//        }
//
//        assertEquals(expected, result)
//    }
}
