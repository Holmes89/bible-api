package com.joeldholmes.integration;

import static com.jayway.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.restassured.operation.preprocess.RestAssuredPreprocessors.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.test.context.junit4.SpringRunner;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.builder.RequestSpecBuilder;
import com.jayway.restassured.specification.RequestSpecification;

import io.katharsis.resource.registry.ResourceRegistry;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class BibleApiIntegrationTests {

	@Rule
	public JUnitRestDocumentation restDocumentation = new JUnitRestDocumentation("target/generated-snippets");
	
	@LocalServerPort
	int port;
	
	@Autowired
	ResourceRegistry registry;
	
	private RequestSpecification spec;

    @Before
    public final void before() {
    	RestAssured.port = port;
    	this.spec = new RequestSpecBuilder()
				.addFilter(documentationConfiguration(restDocumentation)).build();
    }

    
    @Test
    public void testConnection(){
    	given(this.spec)
    	.accept("application/vnd.api+json;charset=UTF-8")
		.filter(document("resources"))
		.get("/resourcesInfo").then().statusCode(200);
    }
    
    @Test
    public void testDisplayVerse(){
    	given(this.spec)
    	.queryParam("filter[displayVerse]", "Joel 1:2")
    	.accept("application/vnd.api+json;charset=UTF-8") 
    	.filter(document("displayVerse",
				preprocessRequest(
					prettyPrint(), 
					modifyUris().host("bible.jholmestech.com").port(8080)),
				preprocessResponse(prettyPrint()),
				requestParameters( 
					parameterWithName("filter[displayVerse]").description("Find by formatted verse"))))
		.get("/api/verses/").
    	then()
    	.statusCode(200)
    	.body("data", hasSize(1))
    	.body("data[0].attributes.book", equalTo("Joel"))
    	.body("data[0].attributes.chapter", equalTo(1))
    	.body("data[0].attributes.verse", equalTo(2));
    }
    
    @Test
    public void testSearchVerse(){
    	given(this.spec)
    	.queryParam("filter[search]", "Joel")
    	.accept("application/vnd.api+json;charset=UTF-8") 
		.filter(document("search",
					preprocessRequest(
						prettyPrint(), 
						modifyUris().host("bible.jholmestech.com").port(8080)),
					preprocessResponse(prettyPrint()),
					requestParameters( 
						parameterWithName("filter[version]").description("Bible Version").optional(),
						parameterWithName("filter[search]").description("Search term"))) 
		)
		.get("/api/verses/").
    	then()
    	.statusCode(200)
    	.body("data", hasSize(21));
    }
 
}
