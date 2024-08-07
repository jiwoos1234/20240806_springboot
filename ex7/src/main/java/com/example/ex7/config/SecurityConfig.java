package com.example.ex7.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.Authentication;
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
import java.util.List;

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

    // authorizeHttpRequests :: 선별적으로 접속을 제한하는 메서드
    // 모든 페이지가 인증을 받도록 되어 있는 상태
    // httpSecurity.authorizeHttpRequests(auth -> auth.anyRequest().authenticated());
    httpSecurity.authorizeHttpRequests(
        auth -> auth.requestMatchers(AUTH_WHITElIST).permitAll()
            .requestMatchers("/sample/admin/**").hasRole("ADMIN")
            .requestMatchers("/sample/member/**").access(
                new WebExpressionAuthorizationManager(
                    "hasRole('ADMIN') or hasRole('MEMBER')")
            )
            .anyRequest().authenticated());

    // formLogin()를 정의만 해도 자동생성된 로그인페이지로 이동가능.
    httpSecurity.formLogin(new Customizer<FormLoginConfigurer<HttpSecurity>>() {
      @Override
      public void customize(FormLoginConfigurer<HttpSecurity> httpSecurityFormLoginConfigurer) {
        httpSecurityFormLoginConfigurer
            .loginPage("/sample/login")
            .loginProcessingUrl("/sample/login")
            .successHandler(new AuthenticationSuccessHandler() {
              @Override
              public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

              }
            })
            .defaultSuccessUrl("/");
      }
    });

    // logout() 정의 안해도 로그아웃 페이지 사용 가능. 사용자 로그아웃 페이지 지정할 때 사용
    httpSecurity.logout(new Customizer<LogoutConfigurer<HttpSecurity>>() {
      @Override
      public void customize(LogoutConfigurer<HttpSecurity> httpSecurityLogoutConfigurer) {
        httpSecurityLogoutConfigurer
            // logoutUrl() 설정할 경우 html action 주소 또한 같이 적용해야함.
            // logoutUrl()으로 인해 기존의 logout 주소 이동은 가능하나 기능은 사용 안됨.
            .logoutUrl("/sample/logout")
            .logoutSuccessUrl("/")  // 로그아웃 후에 돌아갈 페이지 설정
            .logoutSuccessHandler((request, response, authentication) -> {
              // logout 후에 개별적으로 여러가지 상황에 대하여 적용 가능한 설정
            })
            .invalidateHttpSession(true);  // 서버의 세션을 무효화, false도 클라이언트측 무효화
      }
    });


    return httpSecurity.build();
  }

  // InMemory 방식으로 계정의 권한 관리
  @Bean
  public UserDetailsService userDetailsService() {
    UserDetails user1 = User.builder()
        .username("user1")
        .password("$2a$10$MRnZN7Txf0otF6Ln0sWszOobI9xIiW0oMeIn2zgqatnFP5uPmzNFe")
        .roles("USER")
        .build();
    UserDetails member = User.builder()
        .username("member")
        .password("$2a$10$klM7YL4.EK86ZrdmzOhwhOKIIkOqdUG2XrvMTXdDoub3HKjMpHMNO")
        .roles("MEMBER")
        .build();
    UserDetails admin = User.builder()
        .username("admin")
        .password(passwordEncoder().encode("1"))
        .roles("ADMIN", "MEMBER")
        .build();
    List<UserDetails> list = new ArrayList<>();
    list.add(user1);
    list.add(member);
    list.add(admin);
    return new InMemoryUserDetailsManager(list);
  }

}