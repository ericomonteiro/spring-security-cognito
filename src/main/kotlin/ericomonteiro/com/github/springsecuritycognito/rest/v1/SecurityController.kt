package ericomonteiro.com.github.springsecuritycognito.rest.v1

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider
import com.amazonaws.services.cognitoidp.model.ListUserPoolsRequest
import com.amazonaws.services.cognitoidp.model.ListUserPoolsResult
import ericomonteiro.com.github.springsecuritycognito.rest.dto.LoginDto
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping


@Controller
@RequestMapping("/security")
class SecurityController(
    private val cognitoClient: AWSCognitoIdentityProvider
) {

    @PostMapping("/login")
    fun authenticate(@RequestBody loginDto: LoginDto): ResponseEntity<String> {
        println(loginDto)
        return ResponseEntity.status(HttpStatus.OK).body("OK")
    }

    @GetMapping("/user-pool")
    fun listAllUserPools() {
        val listUserPoolsRequest = ListUserPoolsRequest()
        listUserPoolsRequest.maxResults = 10
        val response = cognitoClient.listUserPools(listUserPoolsRequest)
        response.userPools.forEach {
            println(it.name)
        }
    }

}