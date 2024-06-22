package org.study.grabli_application.security;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import java.util.Collections;
import org.study.grabli_application.entity.User;
import org.study.grabli_application.service.UserService;

@RequiredArgsConstructor(onConstructor_ = {@Autowired})
public class CustomAuthenticationManager implements AuthenticationProvider {

  private final UserService userService;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    String username = authentication.getPrincipal() + "";
    String password = authentication.getCredentials() + "";

    UserDetails user;

    try {

      user = userService.loadUserByUsername(username);
    } catch (UsernameNotFoundException ex) {

      User newUser = new User();
      newUser.setUsername(username);
      newUser.setPassd(password);
      user = userService.saveUser(newUser);
    }

    if (!bCryptPasswordEncoder.matches(password, user.getPassword())) {
      throw new BadCredentialsException("Пароль не верный");
    }

    return new UsernamePasswordAuthenticationToken(username, null, Collections.singletonList(DefaultRole.DEFAULT));
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
