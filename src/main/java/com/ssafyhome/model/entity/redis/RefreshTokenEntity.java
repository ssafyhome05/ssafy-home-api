package com.ssafyhome.model.entity.redis;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Data
@AllArgsConstructor
@RedisHash(value = "refresh-token", timeToLive = 24 * 60 * 60)
public class RefreshTokenEntity {

  @Id
  private String refreshToken;
  private String userId;
}
