package ericomonteiro.com.github.springsecuritycognito.service

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider
import com.amazonaws.services.cognitoidp.model.AuthFlowType
import com.amazonaws.services.cognitoidp.model.InitiateAuthRequest
import ericomonteiro.com.github.springsecuritycognito.rest.dto.LoginRequestDto
import ericomonteiro.com.github.springsecuritycognito.rest.dto.LoginResponseDto
import ericomonteiro.com.github.springsecuritycognito.rest.mapper.toLoginResponseDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


@Service
class SecurityService(
    @Value("\${aws.cognito.user-pool.client-id}")
    private val userPoolClientId: String,

    @Value("\${aws.cognito.user-pool.client-secret}")
    private val userPoolClientSecret: String,

    private val cognitoClient: AWSCognitoIdentityProvider
) {

    fun login(loginRequestDto: LoginRequestDto): LoginResponseDto {
        println(userPoolClientId)

        val result = cognitoClient.initiateAuth(initiateAuthRequest(loginRequestDto))
        return result.authenticationResult.toLoginResponseDto()
    }

    private fun initiateAuthRequest(loginRequestDto: LoginRequestDto) = InitiateAuthRequest()
        .withClientId(userPoolClientId)
        .withAuthFlow(AuthFlowType.USER_PASSWORD_AUTH)
        .withAuthParameters(mapOf(
            "USERNAME" to loginRequestDto.username,
            "PASSWORD" to loginRequestDto.password,
            "SECRET_HASH" to calculateSecretHash(loginRequestDto.username)
        ))


    private fun calculateSecretHash(userName: String): String? {
        val HMAC_SHA256_ALGORITHM = "HmacSHA256"
        val signingKey = SecretKeySpec(
            userPoolClientSecret.toByteArray(StandardCharsets.UTF_8),
            HMAC_SHA256_ALGORITHM
        )
        return try {
            val mac = Mac.getInstance(HMAC_SHA256_ALGORITHM)
            mac.init(signingKey)
            mac.update(userName.toByteArray(StandardCharsets.UTF_8))
            val rawHmac = mac.doFinal(userPoolClientId.toByteArray(StandardCharsets.UTF_8))
            Base64.getEncoder().encodeToString(rawHmac)
        } catch (e: Exception) {
            throw RuntimeException("Error while calculating ")
        }
    }

}