package ericomonteiro.com.github.springsecuritycognito.rest.v1

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.annotation.security.RolesAllowed


@RestController
@RequestMapping("/auth-test")
class SecurityTestController {
    @GetMapping(value = ["/anonymous"])
    fun getAnonymous(): ResponseEntity<String> {
        return ResponseEntity.ok("Hello Anonymous")
    }

    @RolesAllowed("guest")
    @GetMapping(value = ["/all-user"])
    fun getAllUser(): ResponseEntity<String> {
        return ResponseEntity.ok("Hello All User")
    }

    @RolesAllowed("user")
    @GetMapping(value = ["/user"])
    fun getUser(): ResponseEntity<String> {
        return ResponseEntity.ok("Hello User")
    }

    @RolesAllowed("admin")
    @GetMapping(value = ["/admin"])
    fun getAdmin(): ResponseEntity<String> {
        return ResponseEntity.ok("Hello Admin")
    }
}