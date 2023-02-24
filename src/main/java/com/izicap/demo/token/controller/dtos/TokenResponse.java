package com.izicap.demo.token.controller.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.izicap.demo.token.entities.Token;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
@NoArgsConstructor
public class TokenResponse {
    private String token;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date creationDate;

    @Temporal(TemporalType.TIMESTAMP)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expirationDate;

    public TokenResponse(Token token) {
        this.token = token.getToken();
        this.creationDate = token.getCreationDate();
        this.expirationDate = token.getExpirationDate();
    }
}
