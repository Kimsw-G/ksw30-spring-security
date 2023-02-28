package com.security.las.config.auth;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.security.las.config.auth.provider.FacebookUserInfo;
import com.security.las.config.auth.provider.GoogleUserInfo;
import com.security.las.config.auth.provider.OAuth2UserInfo;
import com.security.las.model.User;
import com.security.las.repository.UserRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info(userRequest);
        log.info(super.loadUser(userRequest));
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info(oAuth2User.getAttributes());

        

        OAuth2UserInfo oAuth2UserInfo = null;
        switch (userRequest.getClientRegistration().getRegistrationId()) {
            case "google":
                oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
                break;
            case "facebook":
                oAuth2UserInfo = new FacebookUserInfo(oAuth2User.getAttributes());
                break;
            case "naver":

                break;
            default:
                log.info("지원하지 않는 OAuth 입니다");
                break;
        }
        Optional<User> userOptional = userRepository.findByProviderAndProviderId(oAuth2UserInfo.getProvider(),
                oAuth2UserInfo.getProviderId());

        User user;
        if (userOptional.isPresent()) {
            user = userOptional.get();
            // user가 존재하면 update 해주기
            user.setEmail(oAuth2UserInfo.getEmail());
            userRepository.save(user);
        } else {
            // user의 패스워드가 null이기 때문에 OAuth 유저는 일반적인 로그인을 할 수 없음.
            user = User.builder()
                    .username(oAuth2UserInfo.getProvider() + "_" + oAuth2UserInfo.getProviderId())
                    .email(oAuth2UserInfo.getEmail())
                    .password(passwordEncoder.encode("놀자곰"))
                    .role("ROLE_USER")
                    .provider(oAuth2UserInfo.getProvider())
                    .providerId(oAuth2UserInfo.getProviderId())
                    .build();
            userRepository.save(user);
        }

        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }

}
