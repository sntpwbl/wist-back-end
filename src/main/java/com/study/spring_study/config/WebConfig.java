package com.study.spring_study.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.mediatype.hal.Jackson2HalModule;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.xml.MappingJackson2XmlHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Bean
    MappingJackson2XmlHttpMessageConverter xmlHttpMessageConverter() {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.registerModule(new Jackson2HalModule()); // Necessário para suporte ao HATEOAS
        return new MappingJackson2XmlHttpMessageConverter(xmlMapper);
    }

    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(xmlHttpMessageConverter());
    }
    
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(false);
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer){
	
	configurer.favorParameter(false)
	.ignoreAcceptHeader(false)
	.useRegisteredExtensionsOnly(false)
	.defaultContentType(MediaType.APPLICATION_JSON)
	.mediaType("json", MediaType.APPLICATION_JSON)
    .mediaType("xml", MediaType.APPLICATION_XML);
}
}
