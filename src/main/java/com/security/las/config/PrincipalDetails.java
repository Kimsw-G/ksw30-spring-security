package com.security.las.config;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.security.las.model.User;

import lombok.Data;

// /login 주소 요청을 낚아서 로그인을 진행
// 로그인 진행 완료시 security session 생성 (Security ContextHolder)
// Object => Authentication 타입 ~ 안에 User정보 필요
// User Object => UserDetails 타입 객체

// Securiy Session => Authentication  => UserDetails
@Data
public class PrincipalDetails implements UserDetails, OAuth2User{

    private User user;
    private Map<String,Object> attributes;

    // 일반 로그인
    public PrincipalDetails(User user){
        this.user = user;
    }
    // OAuth 로그인
    public PrincipalDetails(User user, Map<String,Object> attributes){
        this.user = user;
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // User의 권한을 리턴
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority(){
                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public String getName() {
        return null;
    }
    
}
