package com.ssafyhome.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Redis 사용을 위한 설정 파일
 * Redis connection을 두가지 사용하는데 Lettuce와 Redisson을 사용
 *
 * Lettuce?
 * - Refresh Token 및 Email Secret을 임시적으로 저장할때 사용
 *
 * Redisson?
 * - Lettuce가 spring boot redis기본 커넥션 풀에 포함되어 있고 가볍지만 Lock을 지원하는 메서드가 없어서 구현해야함
 * - 직접 구현하다 보니 스핀락으로 구현하게 되는데 여러번의 요청에 의해 레디스에 부하가 심할 수 있음
 * - 반면 Redisson의 RLock의 경우 Pub-Sub방식으로 구현되어 있어 부하가 덜함
 *
 * 하지만 Connectiong을 2개를 유지하므로 네트워크 리소스를 더 사용하게 된다는 단점이 있다.
 */
@Configuration
public class RedisConfig {

  @Value("${spring.data.redis.host}")
  private String host;

  @Value("${spring.data.redis.port}")
  private int port;

  /**
   * Lettuce Connect
   */
  @Bean
  public RedisConnectionFactory redisConnectionFactory() {

    return new LettuceConnectionFactory(host, port);
  }

  /**
   * Redisson Connect
   */
  @Bean
  public RedissonClient redissonClient() {

    Config config = new Config();
    config.useSingleServer().setAddress("redis://" + host + ":" + port);
    return Redisson.create(config);
  }

  /**
   * Redisson을 이용해서 분산락을 걸고 Task가 성공됨을 저장하는 Repository를 따로 안만들고 Templete을 활용하기 위
   * */
  @Bean
  public RedisTemplate<String, Object> redisTemplate() {

    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory());
    return template;
  }
}
