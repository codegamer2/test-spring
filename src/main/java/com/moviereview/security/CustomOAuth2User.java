package com.moviereview.security;

import com.moviereview.domain.user.User;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
// import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@Getter
public class CustomOAuth2User { //} implements OAuth2User {
    private final User user;
    private final Map<String, Object> attributes;

    public CustomOAuth2User(User user) {
        this.user = user;
        this.attributes = Collections.emptyMap();
    }

    // @Override
    // public Map<String, Object> getAttributes() {
    //     return attributes;
    // }

    // @Override
    // public Collection<? extends GrantedAuthority> getAuthorities() {
    //     return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
    // }

    // @Override
    // public String getName() {
    //     return user.getUsername();
    // }
} 