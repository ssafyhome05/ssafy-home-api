package com.ssafyhome.util;

import com.ssafyhome.user.dao.EmailSecretRepository;
import com.ssafyhome.user.entity.EmailSecretEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;
import java.util.Random;

@Component
public class SecretUtil {

	@Value("${find-password.algorithm}")
	private String ALGORITHM;

	@Value("${find-password.key}")
	private String KEY;

	private final EmailSecretRepository emailSecretRepository;

	public SecretUtil(EmailSecretRepository emailSecretRepository) {

		this.emailSecretRepository = emailSecretRepository;
	}

	/**
	 * 이메일 암호 전송을 위한 무작위 문자열 생성 -> addSecretOnRedis
	 * @param length
	 * @return
	 */
	public String makeRandomString(int length) {

		char[] chars = new char[length];
		for (int i = 0; i < length; i++) {
			int random = new Random().nextInt(26 + 26 + 10);
			switch (random / 26) {
				case 0 -> random += 65;
				case 1 -> random += 97 - 26;
				case 2 -> random += 48 - 52;
			}
			chars[i] = (char)random;
		}
		return String.valueOf(chars);
	}

	/**
	 * 입력받은 email 로 문자열
	 * @param email
	 * @param secret
	 */
	public void addSecretOnRedis(String email, String secret) {

		if(emailSecretRepository.existsById(email)) emailSecretRepository.deleteById(email);
		emailSecretRepository.save(new EmailSecretEntity(email, secret));
	}

	
	
	public boolean checkSecret(String email, String secret) {

		Optional<EmailSecretEntity> secretEntity = emailSecretRepository.findById(email);
		String redisSecret = secretEntity.get().getSecret();
		return secret.equals(redisSecret);
	}

	
	/**
	 * 체크 성공 즉시 암호키를 redis 에서 제거
	 * @param email
	 */
	
	public void removeSecretOnRedis(String email) {

		emailSecretRepository.deleteById(email);
	}

	/**
	 * 무작위 문자열 value 를 암호화 : Cipher -> AES 사용
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public String encrypt(String value) throws Exception {

		SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.ENCRYPT_MODE, secretKey);
		byte[] encrypted = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
		return Base64.getEncoder().encodeToString(encrypted);
	}

	
	public String decrypt(String encrypted) throws Exception {

		SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(StandardCharsets.UTF_8), ALGORITHM);
		Cipher cipher = Cipher.getInstance(ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, secretKey);
		byte[] decoded = Base64.getDecoder().decode(encrypted);
		byte[] decrypted = cipher.doFinal(decoded);
		return new String(decrypted, StandardCharsets.UTF_8);
	}
}
