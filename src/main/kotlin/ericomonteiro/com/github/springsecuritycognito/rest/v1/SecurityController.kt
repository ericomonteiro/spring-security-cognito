package ericomonteiro.com.github.springsecuritycognito.rest.v1

import ericomonteiro.com.github.springsecuritycognito.rest.dto.LoginRequestDto
import ericomonteiro.com.github.springsecuritycognito.rest.dto.LoginResponseDto
import ericomonteiro.com.github.springsecuritycognito.service.SecurityService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


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

}