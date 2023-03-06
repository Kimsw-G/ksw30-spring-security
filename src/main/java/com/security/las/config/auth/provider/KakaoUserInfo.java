package com.security.las.config.auth.provider;

import java.util.Map;

public class KakaoUserInfo implements OAuth2UserInfo{

    private Map<String, Object> attributes;
    private Map<String, Object> accountAttributes;
    private Map<String, Object> propertiesAttributes;

    public KakaoUserInfo(Map<String, Object> attributes){
        this.attributes = attributes;
        this.accountAttributes = (Map)attributes.get("kakao_account");
        this.propertiesAttributes = (Map)attributes.get("properties");
    }
    
    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }
    
    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getEmail() {
        return (String)accountAttributes.get("email");
    }

    @Override
    public String getName() {
        return (String)propertiesAttributes.get("nickname");
    }
    
}
