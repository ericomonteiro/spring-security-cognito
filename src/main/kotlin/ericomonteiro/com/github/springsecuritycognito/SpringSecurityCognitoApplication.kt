package ericomonteiro.com.github.springsecuritycognito

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity

@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
class SpringSecurityCognitoApplication

fun main(args: Array<String>) {
	runApplication<SpringSecurityCognitoApplication>(*args)
}
