package com.ssafyhome.api.common;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import feign.codec.Decoder;
import feign.optionals.OptionalDecoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

public class FeignXmlDecoderConfig {

	@Bean
	Decoder feignDecoder() {

		XmlMapper xmlMapper = new XmlMapper();
		xmlMapper.enable(DeserializationFeature.UNWRAP_ROOT_VALUE);

		ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters(
				new MappingJackson2HttpMessageConverter(xmlMapper)
		);

		return new OptionalDecoder(new ResponseEntityDecoder(new SpringDecoder(objectFactory)));
	}
}
