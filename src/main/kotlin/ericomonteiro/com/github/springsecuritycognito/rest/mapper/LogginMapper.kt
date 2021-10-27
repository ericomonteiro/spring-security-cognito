package ericomonteiro.com.github.springsecuritycognito.rest.mapper

import com.amazonaws.services.cognitoidp.model.AuthenticationResultType
import ericomonteiro.com.github.springsecuritycognito.rest.dto.LoginResponseDto

fun AuthenticationResultType.toLoginResponseDto() = LoginResponseDto(
    accessToken = this.accessToken,
    expiresIn = this.expiresIn,
    tokenType = this.tokenType,
    refreshToken = this.refreshToken,
    idToken = this.idToken
)