package ericomonteiro.com.github.springsecuritycognito.rest.mapper

import com.amazonaws.services.cognitoidp.model.GetUserResult
import com.amazonaws.services.cognitoidp.model.UserType
import ericomonteiro.com.github.springsecuritycognito.model.User
import org.springframework.stereotype.Component

@Component
class UserMapper {
    fun fromCognitoUserResult(cognitoUserResult: GetUserResult) =
        User(
            id = cognitoUserResult.userAttributes.first { it.name.equals("sub") }.value,
            username = cognitoUserResult.username,
            email = cognitoUserResult.userAttributes.firstOrNull { it.name.equals("email") }?.value ?: "",
            phone = cognitoUserResult.userAttributes.firstOrNull { it.name.equals("phone_number") }?.value ?: "",
        )

    fun fromCognitoUser(cognitoUser: UserType): User =
        User(
            id = cognitoUser.attributes.first { it.name.equals("sub") }.value,
            username = cognitoUser.username,
            email = cognitoUser.attributes.firstOrNull { it.name.equals("email") }?.value ?: "",
            phone = cognitoUser.attributes.firstOrNull { it.name.equals("phone_number") }?.value ?: "",
        )

    fun fromCognitoUserList(cognitoUserList: List<UserType>): List<User> =
        cognitoUserList.map { fromCognitoUser(it) }.toList()
}