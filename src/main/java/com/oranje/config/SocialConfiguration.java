package com.oranje.config;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.web.SignInAdapter;

import com.oranje.domain.Authority;
import com.oranje.domain.User;
import com.oranje.security.AuthoritiesConstants;
import com.oranje.security.SecurityUtils;

@Configuration
public class SocialConfiguration {

	@Bean
	public SignInAdapter authSignInAdapter() {
		return (userId, connection, request) -> {
			if (connection != null) {
				UserProfile userProfile = connection.fetchUserProfile();

				User newUser = new User();

				Set<Authority> auths = new HashSet<>();
				Authority auth = new Authority();
				auth.setName(AuthoritiesConstants.USER);
				auths.add(auth);

				newUser.setActivated(true);
				newUser.setLogin(userProfile.getUsername());
				newUser.setAuthorities(auths);
				if (userProfile.getEmail()!=null) {
					newUser.setEmail(userProfile.getEmail());	
				}
				
				newUser.setFirstName(userProfile.getFirstName());
				newUser.setLastName(userProfile.getLastName());
				newUser.setPassword("twitteruser");
				newUser.setCreatedBy("Social");
				newUser.setSocial(true);
				SecurityUtils.authenticateSocial(newUser);
			}
			return null;
		};
	}
}