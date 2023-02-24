package com.izicap.demo.token.entities;

import lombok.*;

import javax.persistence.*;

@ToString
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Builder
@Getter
public class User {

	static public String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long id;

	@Column(name = "username", nullable = true)
	private String userName;

	@Column(name = "password", nullable = true)
	private String password;
}

