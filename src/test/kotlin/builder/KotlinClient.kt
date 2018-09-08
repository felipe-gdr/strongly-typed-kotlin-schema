package builder

import example.PullRequest
import example.User
import example.viewer
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
                login
                name
                email
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
        val expected =
                "query { aViewer: viewer { aLogin: login, myName: name, someEmail: email, aPullRequest: pullRequests(last:5) { nodes { aBody: body, pullRequestId: id } } } }"
        val result = query {
            viewer(alias = "aViewer") {
                login(alias = "aLogin")
                name(alias = "myName")
                email(alias = "someEmail")
                pullRequests(last = 5, alias = "aPullRequest") {
                    nodes {
                        body(alias = "aBody")
                        id(alias = "pullRequestId")
                    }
                }
            }
        }

        assertEquals(expected, result)
    }

    @Test
    fun when__same_field_is_declared_twice_with_different_aliases__then_generated_string_contains_both_definitions() {
        val expected =
                "query { aViewer: viewer { login, name, email, pullRequests(last:5) { nodes { body } } }, anotherViewer: viewer { login } }"
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

    @Test
    fun when__fragment_is_defined__then_resulting_string_contains_fragment_syntax() {
        val expected = "fragment viewerFragment on User { login, name }"
        val viewerFragment = fragment(name = "viewerFragment", on = User::class) {
            login
            name
        }

        assertEquals(expected, viewerFragment.toString())
    }

    @Test
    fun when__fragment_is_define_together_with_aliases__then_generated_query_string_contains_fragment_and_aliases() {
        val expected =
                "fragment viewerFragment on User { aLogin: login, myName: name, someEmail: email, aPullRequest: pullRequests(last:5) { nodes { body } } }"
        val viewerFragment = fragment(name = "viewerFragment", on = User::class) {
            login(alias = "aLogin")
            name(alias = "myName")
            email(alias = "someEmail")
            pullRequests(last = 5, alias = "aPullRequest") {
                nodes {
                    body
                }
            }
        }

        assertEquals(expected, viewerFragment.toString())
    }

    @Test
    fun when__fragment_is_used_in_query_then_resulting_string_contains_fragment_definition_and_usage() {
        val expected = "query { viewer { ...viewerFragment } } fragment viewerFragment on User { login, name }"
        val fragment = fragment(name = "viewerFragment", on = User::class) {
            login
            name
        }
        val result = query {
            viewer {
                useFragment(fragment)
            }
        }

        assertEquals(expected, result)
    }

    @Test
    fun when__same_fragment_is_used_twice_in_query__then_resulting_string_contains_two_usages_and_just_one_fragment_definition() {
        val expected =
                "query { aViewer: viewer { ...viewerFragment }, anotherViewer: viewer { ...viewerFragment } } fragment viewerFragment on User { login, name }"
        val fragment = fragment(name = "viewerFragment", on = User::class) {
            login
            name
        }
        val result = query {
            viewer(alias = "aViewer") {
                useFragment(fragment)
            }
            viewer(alias = "anotherViewer") {
                useFragment(fragment)
            }
        }

        assertEquals(expected, result)
    }

    @Test
    fun when__two_fragments_are_used_in_query__then_resulting_string_contains_two_usages_and_just_two_fragment_definitions() {
        val expected =
                "query { aViewer: viewer { ...viewerFragment, pullRequests(first:5) { nodes { ...pullRequestFragment } } } } fragment viewerFragment on User { login, name } fragment pullRequestFragment on PullRequest { body, id }"
        val userFragment = fragment(name = "viewerFragment", on = User::class) {
            login
            name
        }
        val pullRequestFragment = fragment(name = "pullRequestFragment", on = PullRequest::class) {
            body
            id
        }
        val result = query {
            viewer(alias = "aViewer") {
                useFragment(userFragment)
                pullRequests(first = 5) {
                    nodes {
                        this.useFragment(pullRequestFragment)
                    }
                }
            }
        }

        assertEquals(expected, result)
    }

    @Test
    fun when__fragment_is_used_inside_another_fragment__then_resulting_string_contains_two_usages_and_just_two_fragment_definitions() {
        val expected =
                "query { aViewer: viewer { ...viewerFragment } } fragment viewerFragment on User { login, name, pullRequests { nodes { ...pullRequestFragment } } } fragment pullRequestFragment on PullRequest { body, id }"
        val pullRequestFragment = fragment(name = "pullRequestFragment", on = PullRequest::class) {
            body
            id
        }
        val userFragment = fragment(name = "viewerFragment", on = User::class) {
            login
            name
            pullRequests(first = 5) {
                nodes {
                    useFragment(pullRequestFragment)
                }
            }
        }
        val result = query {
            viewer(alias = "aViewer") {
                useFragment(userFragment)
            }
        }

        assertEquals(expected, result)
    }

    @Test
    fun when__field_inherited_from_one_interface_is_used__then_resulting_string_contains_that_field() {
        val expected = "query { viewer { login } }"
        val result = query {
            viewer {
                login
            }
        }

        assertEquals(expected, result)
    }

    @Test
    fun when__field_inherited_from_one_interface_is_used_with_alias__then_resulting_string_contains_that_field_with_alias() {
        val expected = "query { viewer { fieldFromInterface: login } }"
        val result = query {
            viewer {
                login("fieldFromInterface")
            }
        }

        assertEquals(expected, result)
    }

    @Test
    fun when__fields_inherited_from_two_interfaces_are_used__then_resulting_string_contains_those_fields() {
        val expected = "query { viewer { login, id } }"
        val result = query {
            viewer {
                login
                id
            }
        }

        assertEquals(expected, result)
    }

    @Test
    fun when__fields_inherited_from_two_interfaces_are_used_with_aliases__then_resulting_string_contains_those_fields_with_aliases() {
        val expected = "query { viewer { fromOneInterface: login, fromAnotherInterface: id } }"
        val result = query {
            viewer {
                login("fromOneInterface")
                id("fromAnotherInterface")
            }
        }

        assertEquals(expected, result)
    }

    @Test
    fun when__field_inherited_from_one_interface_is_used_passing_argument__then_resulting_string_contains_that_field_with_argument() {
        val expected = "query { viewer { avatarUrl(size:100) } }"
        val result = query {
            viewer {
                avatarUrl(size = 100)
            }
        }

        assertEquals(expected, result)
    }
}
