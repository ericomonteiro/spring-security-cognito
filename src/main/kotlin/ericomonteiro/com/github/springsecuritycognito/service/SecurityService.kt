package ericomonteiro.com.github.springsecuritycognito.service

import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider
import com.amazonaws.services.cognitoidp.model.*
import ericomonteiro.com.github.springsecuritycognito.model.User
import ericomonteiro.com.github.springsecuritycognito.rest.dto.LoginRequestDto
import ericomonteiro.com.github.springsecuritycognito.rest.dto.LoginResponseDto
import ericomonteiro.com.github.springsecuritycognito.rest.dto.SignUpDto
import ericomonteiro.com.github.springsecuritycognito.rest.mapper.UserMapper
import ericomonteiro.com.github.springsecuritycognito.rest.mapper.toLoginResponseDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.util.Base64
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec


@Service
class SecurityService(
    @Value("\${aws.cognito.client-id}")
    private val userPoolClientId: String,

    @Value("\${aws.cognito.client-secret}")
    private val userPoolClientSecret: String,


    @Value("\${aws.cognito.pool-id}")
    private val userPoolId: String,

    @Value("\${aws.cognito.sign-up.group-name}")
    private val defaultSignUpGroupName: String,

    private val cognitoClient: AWSCognitoIdentityProvider,
    private val userMapper: UserMapper
) {

    fun login(loginRequestDto: LoginRequestDto): LoginResponseDto {
        println(userPoolClientId)

        val result = cognitoClient.initiateAuth(initiateAuthRequest(loginRequestDto))
        return result.authenticationResult.toLoginResponseDto()
    }

    fun listUsers(): List<User> =
        userMapper.fromCognitoUserList(
            cognitoClient.listUsers(
                ListUsersRequest().withUserPoolId(userPoolId)
            ).users
        )

    fun userAuthenticated(authentication: JwtAuthenticationToken): User {
        val result = cognitoClient.getUser(
            GetUserRequest()
                .withAccessToken(authentication.token.tokenValue)
        )
        return userMapper.fromCognitoUserResult(result)
    }

    fun createUser(user: SignUpDto): SignUpResult {
        val result = cognitoClient.signUp(
            SignUpRequest()
                .withClientId(userPoolClientId)
                .withSecretHash(calculateSecretHash(user.username))
                .withUsername(user.username)
                .withPassword(user.password)
                .withUserAttributes(
                    listOf(
                        AttributeType().withName("email").withValue(user.email),
                        AttributeType().withName("phone_number").withValue(user.phone)
                    )
                )
        )

        cognitoClient.adminAddUserToGroup(
            AdminAddUserToGroupRequest()
                .withUserPoolId(userPoolId)
                .withGroupName(defaultSignUpGroupName)
                .withUsername(user.username)
        )

        return result
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