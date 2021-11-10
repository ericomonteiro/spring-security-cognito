package ericomonteiro.com.github.springsecuritycognito.config.aws


import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProvider
import com.amazonaws.services.cognitoidp.AWSCognitoIdentityProviderClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AWSClients (
    private val awsCredentials: AWSCredentials,

    @Value("\${aws.region}")
    private val awsRegion: String
) {

    @Bean
    fun cognitoClient(): AWSCognitoIdentityProvider =
        AWSCognitoIdentityProviderClient
            .builder()
            .withRegion(awsRegion)
            .withCredentials(AWSStaticCredentialsProvider(awsCredentials))
            .build()

}