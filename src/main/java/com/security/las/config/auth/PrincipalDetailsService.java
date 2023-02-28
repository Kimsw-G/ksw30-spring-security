package com.security.las.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.security.las.model.User;
import com.security.las.repository.UserRepository;

import lombok.extern.log4j.Log4j2;

// loginProcessUrl("/login") 
// login 요청시 자동으로 UserDetailsService 타입으로 IOC된 loadUserByUsername 함수가 실행
@Service
@Log4j2
public class PrincipalDetailsService implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername");
        log.info(username);
        User user = userRepository.findByUsername(username);
        log.info(user);
        return new PrincipalDetails(user);
    }
}
