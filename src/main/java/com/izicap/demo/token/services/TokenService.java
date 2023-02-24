package com.izicap.demo.token.services;

import com.izicap.demo.token.entities.Token;
import com.izicap.demo.token.entities.*;

import com.izicap.demo.token.errors.UnauthorizedException;
import com.izicap.demo.token.repositories.TokenRepository;
import com.izicap.demo.token.repositories.UserRepository;

import lombok.Builder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TokenService {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private UserRepository userRepository;

    MessageDigest digest;

    @PostConstruct
    public void initUsers() throws NoSuchAlgorithmException {
        digest = MessageDigest.getInstance("SHA-256");
        userRepository.save(User.builder()
                .userName("admin")
                .password("admin")
                .build());
    }

    public Token getOrCreateToken(String username, String password) {
        User user = checkCredentials(username, password);
        return getOrCreateToken(user);
    }

    User checkCredentials(String username, String password) throws UnauthorizedException {
        List<User> users = userRepository.findUserByUserName(username);
        if (users.size() > 0 && users.get(0).getPassword().equals(password)) {
            return users.get(0);
        }
        throw new UnauthorizedException();
    }

    // get an already existing token or create one if needed.
    private Token getOrCreateToken(User user) {
        Optional<Token> token = tokenRepository.findTokenByUser(user).stream()
                .filter(t -> t.getExpirationDate().after(new Date()))
                .findAny();

        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.SECOND, 60);  // number of seconds to add

        // if it exists, update the expiration date
        if (token.isPresent()) {
            Token toUpdate = token.get();
            toUpdate.setExpirationDate(c.getTime());
            return tokenRepository.save(toUpdate);
        }


        // if it doens't exist, create it.
        return tokenRepository.save(Token.builder()
                .creationDate(new Date())
                .expirationDate(c.getTime())
                .user(user)
                .created(true)
                .token(hash("" +new Date().getTime()))
                .build());
    }

    private String hash(String message) {
        return bytesToHex(digest.digest(
                message.getBytes(StandardCharsets.UTF_8)));
    }

    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
