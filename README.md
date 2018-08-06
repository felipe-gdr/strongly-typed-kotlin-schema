# Strongly typed schema builder


### Simple Query

GraphQL query:

```graphqls
{
  viewer {
    login
    name
    email
  }
}
```

Kotlin:
```kotlin
val queryString = query {
    viewer {
        login
        name
        email
    }
}
```

### With variables

GraphQL query:

```graphqls
{
  viewer {
    login
    name
    email
    pullRequests(last: 5) {
      nodes {
        body
      }
    }
  }
}
```

Kotlin:
```kotlin
val queryString = query {
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
```

### With named query

GraphQL query:
```
query MyQuery {
  viewer {
    login
    name
    email
    pullRequests(last: 5) {
      nodes {
        body
      }
    }
  }
}
```

Kotlin:
```kotlin
val queryString = query("MyQuery") {
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
```

### With field aliases

GraphQL query:
```
{
  aViewer: viewer {
    aLogin: login
    myName: name
    someEmail: email
    aPullRequest: pullRequests(last: 5) {
      nodes {
        aBody: body
      }
    }
  }
}
```

Kotlin:
```kotlin
val queryString = query {
    viewer(alias = "aViewer") {
        login(alias = "aLogin")
        name(alias = "myName")
        email(alias = "someEmail")
        pullRequests(last = 5, alias = "aPullRequest") {
            nodes {
                body(alias = "aBody")
            }
        }
    }
}
```

### With fragments

GraphQL query:
```
{
  aViewer: viewer {
    ...viewerFragment
  }
  anotherViewer: viewer {
    ...viewerFragment
  }
}

fragment viewerFragment on User {
  login
  name
}

```

Kotlin:
```kotlin
val fragment = fragment(name = "viewerFragment", on = User::class) {
    login
    name
}

val queryString = query {
    viewer(alias = "aViewer") {
        useFragment(fragment)
    }
    viewer(alias = "anotherViewer") {
        useFragment(fragment)
    }
}
```
