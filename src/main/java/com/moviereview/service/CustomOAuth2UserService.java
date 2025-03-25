package com.moviereview.service;

import com.moviereview.domain.user.User;
import com.moviereview.repository.UserRepository;
import lombok.RequiredArgsConstructor;
    // import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
    // import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
    // import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
    // import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService { //extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    // @Override
    // public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    //     OAuth2User oauth2User = super.loadUser(userRequest);
    //     String provider = userRequest.getClientRegistration().getRegistrationId();
    //     String providerId = oauth2User.getAttribute("sub");
    //     String email = oauth2User.getAttribute("email");
    //     String name = oauth2User.getAttribute("name");
    //     String picture = oauth2User.getAttribute("picture");

    //     User user = userRepository.findByEmail(email)
    //         .orElseGet(() -> createUser(email, name, picture, provider, providerId));

    //     return new CustomOAuth2User(user);
    // }

    // @Transactional
    // private User createUser(String email, String name, String picture, String provider, String providerId) {
    //     User user = User.builder()
    //         .email(email)
    //         .name(name)
    //         .profileImage(picture)
    //         .provider(provider)
    //         .providerId(providerId)
    //         .build();

    //     return userRepository.save(user);
    // }
} 