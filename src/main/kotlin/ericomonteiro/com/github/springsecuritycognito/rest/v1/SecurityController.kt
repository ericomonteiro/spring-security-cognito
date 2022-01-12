package ericomonteiro.com.github.springsecuritycognito.rest.v1

import com.amazonaws.services.cognitoidp.model.NotAuthorizedException
import com.amazonaws.services.cognitoidp.model.SignUpResult
import ericomonteiro.com.github.springsecuritycognito.model.User
import ericomonteiro.com.github.springsecuritycognito.rest.dto.LoginRequestDto
import ericomonteiro.com.github.springsecuritycognito.rest.dto.LoginResponseDto
import ericomonteiro.com.github.springsecuritycognito.rest.dto.RestApiError
import ericomonteiro.com.github.springsecuritycognito.rest.dto.SignUpDto
import ericomonteiro.com.github.springsecuritycognito.service.SecurityService
import java.net.URI
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/security")
class SecurityController(
    private val securityService: SecurityService
) {
    private val logger: Logger = LoggerFactory.getLogger(SecurityController::class.java)

    @PostMapping("/login")
    fun authenticate(@RequestBody loginRequestDto: LoginRequestDto): ResponseEntity<LoginResponseDto> {
        println(loginRequestDto)
        return ResponseEntity.ok(securityService.login(loginRequestDto))
    }

    @PostMapping("/signup")
    fun signUp(@RequestBody signUpDto: SignUpDto): ResponseEntity<SignUpResult> {
        return ResponseEntity.created(URI("")).body(securityService.createUser(signUpDto))
    }

    @GetMapping("/user")
    fun listUsers(): ResponseEntity<List<User>> {
        return ResponseEntity.ok(securityService.listUsers())
    }

    @GetMapping("/user/authenticated")
    fun user(): ResponseEntity<User> {
        return ResponseEntity.ok(securityService.userAuthenticated(SecurityContextHolder.getContext().authentication as JwtAuthenticationToken))
    }

    @ExceptionHandler(NotAuthorizedException::class)
    fun notAuthorizedExceptionExceptionHandler(ex: NotAuthorizedException): ResponseEntity<RestApiError> =
        ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(RestApiError(title = ex.errorCode, details = ex.errorMessage))

}