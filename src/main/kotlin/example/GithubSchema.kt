package example

import builder.*

fun Query.viewer(alias: String? = null, init: Viewer.() -> Unit) = type.doInit(Viewer(alias = alias, parent = this), init)

class ActorInterface : Interface() {
    class Login(alias: String? = null) : Field<ScalarType>(ScalarType(), "login", alias)

    fun login(objectType: ObjectType, alias: String? = null) = doInit(objectType, Login(alias))
}

class NodeInterface : Interface() {
    class Id(alias: String? = null) : Field<ScalarType>(ScalarType(), "id", alias)

    fun id(objectType: ObjectType, alias: String? = null) = doInit(objectType, Id(alias))
}

open class User : ObjectType() {
    private val actorInterface = ActorInterface()
    private val nodeInterface = NodeInterface()

    class Email(alias: String? = null) : Field<ScalarType>(ScalarType(), "email", alias)
    class Name(alias: String? = null) : Field<ScalarType>(ScalarType(), "name", alias)

    fun login(alias: String? = null) = actorInterface.login(this, alias)
    fun id(alias: String? = null) = nodeInterface.id(this, alias)
    fun email(alias: String? = null) = doInit(Email(alias))
    fun name(alias: String? = null) = doInit(Name(alias))

    var login: ActorInterface.Login? = null
        get() = actorInterface.login(this)

    var id: NodeInterface.Id? = null
        get() = nodeInterface.id(this)

    var email: Email? = null
        get() = email()

    var name: Name? = null
        get() = name()

    fun pullRequests(parent: Object<*>? = null, alias: String? = null, first: Int? = null, last: Int? = null, init: PullRequestConnection.() -> Unit) =
            doInit(PullRequestConnection(parent, alias), init)
                    .set("first", first)
                    .set("last", last)

}

class Viewer(parent: Object<*>, alias: String? = null) : Object<User>(User(), parent, "viewer", alias) {
    fun login(alias: String? = null) = type.login(alias)
    fun id(alias: String? = null) = type.id(alias)
    fun email(alias: String? = null) = type.email(alias)
    fun name(alias: String? = null) = type.name(alias)

    var login: ActorInterface.Login? = null
        get() = type.login

    var id: NodeInterface.Id? = null
        get() = type.id

    var email: User.Email? = null
        get() = type.email

    var name: User.Name? = null
        get() = type.name

    fun pullRequests(alias: String? = null, first: Int? = null, last: Int? = null, init: PullRequestConnection.() -> Unit) =
            type.pullRequests(this, alias, first, last, init)
}

class PullRequestConnectionType : ObjectType() {
    fun nodes(parent: Object<*>, alias: String? = null, init: PullRequestNode.() -> Unit) =
            doInit(PullRequestNode(parent, alias), init)
}

class PullRequestConnection(parent: Object<*>?, alias: String? = null) :
        Object<PullRequestConnectionType>(PullRequestConnectionType(), parent = parent, name = "pullRequests", alias = alias) {
    fun nodes(alias: String? = null, init: PullRequestNode.() -> Unit) = type.nodes(this, alias, init)
}

class PullRequest : ObjectType() {
    class Body(alias: String? = null) : Field<ScalarType>(ScalarType(), "body", alias)
    class Id(alias: String? = null) : Field<ScalarType>(ScalarType(), "id", alias)

    fun body(alias: String? = null) = doInit(Body(alias))
    fun id(alias: String? = null) = doInit(Id(alias))

    var body: Body? = null
        get() = body()

    var id: Id? = null
        get() = id()
}

class PullRequestNode(parent: Object<*>, alias: String? = null) : Object<PullRequest>(PullRequest(), parent, "nodes", alias) {
    fun body(alias: String? = null) = type.body(alias)
    fun id(alias: String? = null) = type.id(alias)

    var body: PullRequest.Body? = null
        get() = type.body

    var id: PullRequest.Id? = null
        get() = type.id
}
