package ericomonteiro.com.github.springsecuritycognito.rest.dto

data class LoginResponseDto(
    val accessToken: String,
    val expiresIn: Int,
    val tokenType: String,
    val refreshToken: String,
    val idToken: String
)
