package com.ssafyhome.user.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@AllArgsConstructor
@RedisHash(value = "email-secret", timeToLive = 5 * 60)
public class EmailSecretEntity {

	@Id
	private String email;
	private String secret;
}
