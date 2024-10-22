package com.ssafyhome;

import com.ssafyhome.api.sgis.SGISClient;
import com.ssafyhome.api.sgis.SGISUtil;
import com.ssafyhome.model.dto.SgisGeoCode;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("SGIS openfeign test")
public class SGISOpenFeignTest {

	private SGISClient sgisClient;
	private SGISUtil sgisUtil;

	@Autowired
	public void setSGISClient(
			SGISClient sgisClient,
			SGISUtil sgisUtil
	) {

		this.sgisClient = sgisClient;
		this.sgisUtil = sgisUtil;
	}

	@Test
	public void testAccessToken() {

		System.out.println(sgisUtil.getAccessToken());
	}

	@Test
	public void convertAddressToGeoJson(){

		SgisGeoCode sgisGeoCode = sgisClient.getGeocode(
				sgisUtil.getAccessToken(),
				"역삼동 718-5"
		);

		System.out.println(sgisGeoCode);
	}
}
