package com.example.ex7.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
    UserDetails principal = (UserDetails) authentication.getPrincipal();
    Collection<GrantedAuthority> authors =
        (Collection<GrantedAuthority>) principal.getAuthorities();
    List<String> result = authors.stream().map(new Function<GrantedAuthority, String>() {
      @Override
      public String apply(GrantedAuthority grantedAuthority) {
        return grantedAuthority.getAuthority();
      }
    }).collect(Collectors.toList());
    System.out.println(">>" + result.toString());
    for (int i = 0; i < result.size(); i++) {
      if (result.get(i).equals("ROLE_ADMIN")) {
        response.sendRedirect(request.getContextPath() + "/sample/admin");
      } else if (result.get(i).equals("ROLE_MANAGER")) {
        response.sendRedirect(request.getContextPath() + "/sample/manager");
      } else {
        response.sendRedirect(request.getContextPath() + "/sample/all");
      }
      break;
    }
  }
}