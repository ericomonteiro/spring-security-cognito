package ericomonteiro.com.github.springsecuritycognito.rest.dto

data class SignUpDto(
    val username: String,
    val password: String,
    val email:String,
    val phone: String
)