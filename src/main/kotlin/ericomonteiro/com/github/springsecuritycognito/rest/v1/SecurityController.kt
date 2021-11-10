package ericomonteiro.com.github.springsecuritycognito.rest.v1

import ericomonteiro.com.github.springsecuritycognito.model.User
import ericomonteiro.com.github.springsecuritycognito.rest.dto.LoginRequestDto
import ericomonteiro.com.github.springsecuritycognito.rest.dto.LoginResponseDto
import ericomonteiro.com.github.springsecuritycognito.service.SecurityService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/security")
class SecurityController(
    private val securityService: SecurityService
) {

    @PostMapping("/login")
    fun authenticate(@RequestBody loginRequestDto: LoginRequestDto): ResponseEntity<LoginResponseDto> {
        println(loginRequestDto)
        return ResponseEntity.ok(securityService.login(loginRequestDto))
    }

    @GetMapping("/user")
    fun listUsers(): ResponseEntity<List<User>> {
        return ResponseEntity.ok(securityService.listUsers())
    }

    @GetMapping("/user/authenticated")
    fun user(): ResponseEntity<User> {
        return ResponseEntity.ok(securityService.userAuthenticated(SecurityContextHolder.getContext().authentication as JwtAuthenticationToken))
    }

}