package com.ssafyhome.api.sgis;

import com.ssafyhome.model.dto.api.SgisAccessToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SGISUtil {

	@Value("${sgis.API-KEY}")
	private String apiKey;

	@Value("${sgis.secret}")
	private String secret;

	private final SGISClient sgisClient;
	private static final Map<String, Object> store = new ConcurrentHashMap<>();

	public SGISUtil(SGISClient sgisClient) {

		this.sgisClient = sgisClient;
	}

	public String getAccessToken() {

		if (store.containsKey("sgis.access_token")) {
			SgisAccessToken accessToken = (SgisAccessToken) store.get("sgis.access_token");
			if (Long.parseLong(accessToken.getResult().getAccessTimeout()) + 4 * 60 * 60 < Instant.now().getEpochSecond()) {
				return accessToken.getResult().getAccessToken();
			}
		}

		SgisAccessToken accessToken = sgisClient.getAccessToken(apiKey, secret);
		store.put("sgis.access_token", accessToken);
		return accessToken.getResult().getAccessToken();
	}
}
