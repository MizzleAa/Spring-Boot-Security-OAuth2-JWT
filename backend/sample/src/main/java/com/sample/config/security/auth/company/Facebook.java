package com.sample.config.security.auth.company;

import java.util.Map;

import com.sample.config.security.auth.OAuth2UserInfo;
import com.sample.domain.entity.user.Provider;

public class Facebook extends OAuth2UserInfo{

    public Facebook(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getId() {
        return (String) attributes.get("id");
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
        
        if(attributes.containsKey("picture")) {
            
            Map<String, Object> pictureObj = (Map<String, Object>) attributes.get("picture");
            if(pictureObj.containsKey("data")) {
                Map<String, Object>  dataObj = (Map<String, Object>) pictureObj.get("data");
                if(dataObj.containsKey("url")) {
                    return (String) dataObj.get("url");
                }
            }
        }
        return null;
    }

    @Override
    public String getProvider(){
        return Provider.facebook.toString();
    }

}
