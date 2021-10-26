package ericomonteiro.com.github.springsecuritycognito.config.security

import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter

@Configuration
class SecurityConfiguration : WebSecurityConfigurerAdapter() {

    override fun configure(http: HttpSecurity) {
        http
            .authorizeRequests()
            .antMatchers("/security*").permitAll()
            .anyRequest().permitAll()
            .and()
            .csrf().disable()
            .cors().disable()
    }

}