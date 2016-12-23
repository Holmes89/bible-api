package com.joeldholmes;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import io.katharsis.spring.boot.KatharsisConfigV2;

@Configuration
@Import(KatharsisConfigV2.class)
public class BibleApiApplicationConfig {
	
}
