package ericomonteiro.com.github.springsecuritycognito.model

data class User(
    val id: String,
    val username: String,
    val roles: List<String>
)
