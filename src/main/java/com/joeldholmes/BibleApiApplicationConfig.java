package com.joeldholmes;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import io.katharsis.spring.boot.v3.KatharsisConfigV3;

@Configuration
@Import(value={KatharsisConfigV3.class})
@ComponentScan(basePackages={"com.joeldholmes.controllers", "com.joeldholmes.repository", "com.joeldholmes.services"})
public class BibleApiApplicationConfig {
	
}
