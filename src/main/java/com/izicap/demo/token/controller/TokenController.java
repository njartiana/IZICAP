package com.izicap.demo.token.controller;


import com.izicap.demo.token.controller.dtos.TokenResponse;
import com.izicap.demo.token.controller.dtos.UserAndPassword;

import com.izicap.demo.token.entities.Token;
import com.izicap.demo.token.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/tokens")

public class TokenController {
    @Autowired
    TokenService tokenService;

	@RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TokenResponse> getOrCreateToken(@RequestBody UserAndPassword userAndPassword) {
        Token t = tokenService.getOrCreateToken(userAndPassword.getUsername(), userAndPassword.getPassword());
        return new ResponseEntity<>(new TokenResponse(t), t.isCreated() ? HttpStatus.CREATED : HttpStatus.OK);
    }
}
