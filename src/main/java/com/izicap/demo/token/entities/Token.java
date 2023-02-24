package com.izicap.demo.token.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@ToString
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class Token {

	static public String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long id;

	@Column(name = "token", nullable = false)
	private String token;

	public static String getDATE_PATTERN() {
		return DATE_PATTERN;
	}

	public static void setDATE_PATTERN(String dATE_PATTERN) {
		DATE_PATTERN = dATE_PATTERN;
	}

	@Column(name = "creation_date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date creationDate;

	@Temporal(TemporalType.TIMESTAMP)
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@Column(name = "exipiration_date", nullable = false)
	private Date expirationDate;

	@ManyToOne
	@JoinColumn(name="user_id", nullable=false)
	private User user;

	@Transient
	private boolean created = false;
}




