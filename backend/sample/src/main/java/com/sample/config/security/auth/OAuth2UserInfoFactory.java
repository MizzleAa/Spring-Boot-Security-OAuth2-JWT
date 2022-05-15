package com.sample.config.security.auth;

import java.util.Map;

import com.sample.advice.assertThat.DefaultAssert;
import com.sample.config.security.auth.company.Facebook;
import com.sample.config.security.auth.company.Github;
import com.sample.config.security.auth.company.Google;
import com.sample.config.security.auth.company.Kakao;
import com.sample.config.security.auth.company.Naver;
import com.sample.domain.entity.user.Provider;

public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if(registrationId.equalsIgnoreCase(Provider.google.toString())) {
            return new Google(attributes);
        } else if (registrationId.equalsIgnoreCase(Provider.facebook.toString())) {
            return new Facebook(attributes);
        } else if (registrationId.equalsIgnoreCase(Provider.github.toString())) {
            return new Github(attributes);
        } else if (registrationId.equalsIgnoreCase(Provider.naver.toString())) {
            return new Naver(attributes);
        } else if (registrationId.equalsIgnoreCase(Provider.kakao.toString())) {
            return new Kakao(attributes);
        } else {
            DefaultAssert.isAuthentication("해당 oauth2 기능은 지원하지 않습니다.");
        }
        return null;
    }
}
