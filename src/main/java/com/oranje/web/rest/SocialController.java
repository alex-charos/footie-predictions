package com.oranje.web.rest;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.view.RedirectView;

import com.oranje.domain.Authority;
import com.oranje.domain.User;
import com.oranje.repository.AuthorityRepository;
import com.oranje.repository.UserRepository;
import com.oranje.security.AuthoritiesConstants;
import com.oranje.security.SecurityUtils;

@RestController
public class SocialController {

	private final ProviderSignInUtils signInUtils;

	@Inject
	private UserRepository userRepository;

	@Inject
	private AuthorityRepository authRepository;

	@Autowired
	public SocialController(ConnectionFactoryLocator connectionFactoryLocator,
			UsersConnectionRepository connectionRepository) {
		signInUtils = new ProviderSignInUtils(connectionFactoryLocator, connectionRepository);
	}

	@RequestMapping(value = "/signup")
	public RedirectView signup(WebRequest request) {
		Connection<?> connection = signInUtils.getConnectionFromSession(request);
		if (connection != null) {
			UserProfile userProfile = connection.fetchUserProfile();

			User newUser = new User();
			newUser.setSocial(true);
			Optional<User> u = userRepository.findOneByLogin(userProfile.getUsername());
			
			if (u.isPresent()) {
				newUser =u.get();
			}
			
			
			
			Set<Authority> auths = new HashSet<>();
			auths.add(authRepository.findOne(AuthoritiesConstants.USER));

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
			
			newUser = userRepository.save(newUser);
			
			SecurityUtils.authenticateSocial(newUser);

			signInUtils.doPostSignUp(connection.getDisplayName(), request);
		}
		return new RedirectView("");
	}
}
