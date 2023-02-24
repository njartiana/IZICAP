package com.izicap.demo.token.controller.dtos;

import com.izicap.demo.token.entities.User;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserResponse {

    private Long id;
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	private String username;
    
    public UserResponse(User u) {
        this.id = u.getId();
        this.username = u.getUserName();
    }
}
