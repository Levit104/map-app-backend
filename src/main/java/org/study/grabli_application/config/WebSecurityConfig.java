package org.study.grabli_application.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.study.grabli_application.security.AuthenticationFailureHandler;
import org.study.grabli_application.security.AuthenticationSuccessHandler;
import org.study.grabli_application.security.CustomAuthenticationManager;
import org.study.grabli_application.security.LogoutHandler;
import org.study.grabli_application.service.UserService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private UserService userService;

  @Override
  protected void configure(AuthenticationManagerBuilder auth) {

    AuthenticationProvider provider = new CustomAuthenticationManager(userService, bCryptPasswordEncoder());
    auth.authenticationProvider(provider);
  }

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .csrf()
        .disable()
        .authorizeRequests()
        .antMatchers("/grabli").permitAll()
        .antMatchers("/**").permitAll()
        .anyRequest().authenticated()
        .and()
        .formLogin()
        .loginPage("/login")
        .successHandler(new AuthenticationSuccessHandler())
        .failureHandler(new AuthenticationFailureHandler())
        .permitAll()
        .and()
        .logout()
        .permitAll()
        .logoutSuccessHandler(new LogoutHandler());
  }
}
