package com.izicap.demo.token.repositories;

import com.izicap.demo.token.entities.Token;
import com.izicap.demo.token.entities.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TokenRepository extends CrudRepository<Token, Long>, JpaSpecificationExecutor<Token> {
    List<Token> findTokenByUser(User user);
    List<Token> findTokenByToken(String token);
}
