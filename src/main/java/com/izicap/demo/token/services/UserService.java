package com.izicap.demo.token.services;

import com.izicap.demo.token.entities.Token;
import com.izicap.demo.token.entities.User;
import com.izicap.demo.token.errors.ConflictException;
import com.izicap.demo.token.errors.ForbiddenException;
import com.izicap.demo.token.errors.UnauthorizedException;
import com.izicap.demo.token.repositories.TokenRepository;
import com.izicap.demo.token.repositories.UserRepository;

import lombok.Builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    public UserService(TokenRepository tokenRepository, UserRepository userRepository) {
		super();
		this.tokenRepository = tokenRepository;
		this.userRepository = userRepository;
	}

	@Autowired
    private TokenRepository tokenRepository ;

    @Autowired
    private UserRepository userRepository;

    public User createUser(String tokenString, String username, String password) {

        // retrieve mathcing token from db
        Optional<Token> token = tokenRepository.findTokenByToken(tokenString).stream()
                .filter(t -> t.getExpirationDate().after(new Date()))
                .findAny();

        if (!token.isPresent()) {
            throw new UnauthorizedException();
        }

        if (token.get().getUser().getUserName().equals("admin")) {
            if (userRepository.findUserByUserName(username).size() > 0) {
                throw new ConflictException();
            }
            return userRepository.save(User.builder().userName(username).password(password).build());
        }
        throw new ForbiddenException();
    }

    public List<User> all() {
        Specification alwaysTrue = (root, criteriaQuery, criteriaBuilder) -> criteriaBuilder.conjunction();

        return userRepository.findAll(alwaysTrue);
    }
}
