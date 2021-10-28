package ericomonteiro.com.github.springsecuritycognito.rest.mapper

import ericomonteiro.com.github.springsecuritycognito.model.User
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.stereotype.Component

@Component
class UserMapper {
    fun fromAuthentication(auth: Authentication): User {
        val principal = auth.principal as Jwt
        return User(
            id = auth.name,
            username = principal.claims["username"].toString(),
            roles = auth.authorities.map { it.authority.removePrefix("ROLE_") }
        )
    }
}