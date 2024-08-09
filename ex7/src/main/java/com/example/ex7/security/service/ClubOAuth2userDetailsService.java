package com.example.ex7.security.service;

import com.example.ex7.entity.ClubMember;
import com.example.ex7.entity.ClubMemberRole;
import com.example.ex7.repository.ClubMemberRepository;
import com.example.ex7.security.dto.ClubMemberAuthDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@Service
@RequiredArgsConstructor
public class ClubOAuth2userDetailsService extends DefaultOAuth2UserService {
  private final ClubMemberRepository clubMemberRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    log.info("============= userRequest: " + userRequest);
    OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate =
        new DefaultOAuth2UserService();
    OAuth2User oAuth2User = delegate.loadUser(userRequest); //서비스에서 가져온 유저정보
    String registrationId = userRequest.getClientRegistration().getRegistrationId();
    SocialType socialType = getSocialType(registrationId.trim().toString());
    String userNameAttributeName = userRequest.getClientRegistration()
        .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();
    log.info("userNameAttributeName >> " + userNameAttributeName);
    Map<String, Object> attributes = oAuth2User.getAttributes();
    for (Map.Entry<String, Object> entry : attributes.entrySet()) {
      System.out.println(entry.getKey() + ":" + entry.getValue());
    }
    String email = null;
    if (socialType.name().equals("GOOGLE"))
      email = oAuth2User.getAttribute("email");
    log.info("Email: " + email);
    ClubMember clubMember = saveSocialMember(email);
    ClubMemberAuthDTO clubMemberAuthDTO = new ClubMemberAuthDTO(
        clubMember.getEmail(),
        clubMember.getPassword(),
        clubMember.getCno(),
        true,
        clubMember.getRoleSet().stream().map(
                role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
            .collect(Collectors.toList())
        , attributes
    );
    clubMemberAuthDTO.setName(clubMember.getName());
    log.info("clubMemberAuthDTO: " + clubMemberAuthDTO);
    return clubMemberAuthDTO;
  }

  private ClubMember saveSocialMember(String email) {
    Optional<ClubMember> result = clubMemberRepository.findByEmail(email);
    if (result.isPresent()) return result.get();

    ClubMember clubMember = ClubMember.builder()
        .email(email)
        .password(passwordEncoder.encode("1"))
        .fromSocial(true)
        .build();
    clubMember.addMemberRole(ClubMemberRole.USER);
    clubMemberRepository.save(clubMember);
    return clubMember;
  }

  private SocialType getSocialType(String registrationId) {
    if (SocialType.NAVER.name().equals(registrationId)) {
      return SocialType.NAVER;
    }
    if (SocialType.KAKAO.name().equals(registrationId)) {
      return SocialType.KAKAO;
    }
    return SocialType.GOOGLE;
  }

  enum SocialType {
    KAKAO, NAVER, GOOGLE
  }
}

