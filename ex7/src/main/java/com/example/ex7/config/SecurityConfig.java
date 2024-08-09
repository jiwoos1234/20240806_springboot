package com.example.ex7.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.config.annotation.web.configurers.oauth2.client.OAuth2LoginConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private static final String[] AUTH_WHITElIST = {
      "/", "/sample/all"
  };

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /* SecurityFilterChain Bean 역할 :
  세션 인증 기반 방식으로 대부분의 Spring Security에 대한 설정으로 다룰 수 있다. */
  @Bean
  protected SecurityFilterChain config(HttpSecurity httpSecurity)
      throws Exception {
    // csrf 사용안하는 설정
    httpSecurity.csrf(httpSecurityCsrfConfigurer -> {
      httpSecurityCsrfConfigurer.disable();
    });

    // authorizeHttpRequests :: 선별적으로 접속을 제한하는 메서드
    // 모든 페이지가 인증을 받도록 되어 있는 상태
    // httpSecurity.authorizeHttpRequests(auth -> auth.anyRequest().authenticated());
    httpSecurity.authorizeHttpRequests(
        auth -> auth.requestMatchers(AUTH_WHITElIST).permitAll()
            .requestMatchers("/sample/admin/**").hasRole("ADMIN")
            .requestMatchers("/sample/manager/**").access(
                new WebExpressionAuthorizationManager(
                    "hasRole('ADMIN') or hasRole('MANAGER')")
            )
            .anyRequest().authenticated());

    // formLogin()를 정의해야만 해도 자동생성된 로그인페이지로 이동가능.
    httpSecurity.formLogin(new Customizer<FormLoginConfigurer<HttpSecurity>>() {
      @Override
      public void customize(FormLoginConfigurer<HttpSecurity> httpSecurityFormLoginConfigurer) {
        httpSecurityFormLoginConfigurer
//            .loginPage("/sample/login")
//            .loginProcessingUrl("/sample/login")
//            .defaultSuccessUrl("/")
            .successHandler(new AuthenticationSuccessHandler() {
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
            });
      }
    });
    // logout() 정의 안해도 로그아웃 페이지 사용 가능. 사용자 로그아웃 페이지 지정할 때사용
    httpSecurity.logout(new Customizer<LogoutConfigurer<HttpSecurity>>() {
      @Override
      public void customize(LogoutConfigurer<HttpSecurity> httpSecurityLogoutConfigurer) {
        httpSecurityLogoutConfigurer
            // logoutUrl() 설정할 경우 html action 주소 또한 같이 적용해야 함.
            // logoutUrl()으로 인해 기존 logout 주소 이동은 가능하나 기능은 사용 안됨.
            .logoutUrl("/logout")
            .logoutSuccessUrl("/") // 로그아웃 후에 돌아갈 페이지 설정
            //.logoutSuccessHandler((request, response, authentication) -> {
              // logout 후에 개별적으로 여러가지 상황에 대하여 적용 가능한 설정
            //})
            .invalidateHttpSession(true); // 서버 세션을 무효화, false도 클라이언트측 무효화
      }
    });
    httpSecurity.oauth2Login(httpSecurityOAuth2LoginConfigurer -> httpSecurityOAuth2LoginConfigurer.successHandler(new AuthenticationSuccessHandler() {
      @Override
      public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        response.sendRedirect(request.getContextPath()+"/sample/all");
      }
    }));
    return httpSecurity.build();
  }

  // InMemory 방식으로 UserDetailsService(인증 관리 객체) 사용
  /*@Bean
  public UserDetailsService userDetailsService() {
    UserDetails user1 = User.builder()
        .username("user1")
        .password("$2a$10$XGw3jOo9mQSoij4/so.6H.BtSRWpgPze6ZWMuc7ntyFFWqVNbcmBe")
        .roles("USER")
        .build();
    UserDetails manager = User.builder()
        .username("manager")
        .password("$2a$10$AEHcuzENZx7OLeA.s8e.t.CvhE/a/GZf.ZKTPEBIKLv8g03zChnD2")
        .roles("MANAGER")
        .build();
    UserDetails admin = User.builder()
        .username("admin")
        .password(passwordEncoder().encode("1"))
        .roles("ADMIN", "MANAGER")
        .build();
    List<UserDetails> list = new ArrayList<>();
    list.add(user1);
    list.add(manager);
    list.add(admin);
    return new InMemoryUserDetailsManager(list);

  }*/

}
