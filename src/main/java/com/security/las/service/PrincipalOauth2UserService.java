package com.security.las.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.security.las.config.PrincipalDetails;
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

        String provider = userRequest.getClientRegistration().getClientId();
        String providerId = oAuth2User.getAttribute("stub");
        String username = provider + "_" + providerId;
        String password = passwordEncoder.encode("놀자곰");
        String email = oAuth2User.getAttribute("email");
        String role = "ROLE_USER";

        User user = userRepository.findByUsername(username);
        if(user == null){ // 중복 x
            user = new User(username, password, email, role, null);
            userRepository.save(user);
        }

        return new PrincipalDetails(user,oAuth2User.getAttributes());
    }
        
}
