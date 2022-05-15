package com.sample.config.security.auth.company;

import java.util.Map;

import com.sample.config.security.auth.OAuth2UserInfo;
import com.sample.domain.entity.user.Provider;

public class Google extends OAuth2UserInfo{

    public Google(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        
        return (String) attributes.get("sub");
    }

    @Override
    public String getName() {
        
        return (String) attributes.get("name");
    }

    @Override
    public String getEmail() {
        
        return (String) attributes.get("email");
    }

    @Override
    public String getImageUrl() {
        
        return (String) attributes.get("picture");
    }
    
    @Override
    public String getProvider(){
        return Provider.google.toString();
    }
}
