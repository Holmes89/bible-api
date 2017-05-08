package com.joeldholmes.integration;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.removeHeaders;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.document;
import static org.springframework.restdocs.restassured.RestAssuredRestDocumentation.documentationConfiguration;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.restdocs.JUnitRestDocumentation;
import org.springframework.restdocs.restassured.RestDocumentationFilter;
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
	

	protected RestDocumentationFilter documentationFilter = document("{ClassName}/{methodName}",
			preprocessResponse(prettyPrint()));

	@LocalServerPort
	int port;
	
	private RequestSpecification spec;

    @Before
    public final void before() {
    	RestAssured.port = port;
    	RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    	
		this.spec = new RequestSpecBuilder()
				.addFilter(documentationConfiguration(this.restDocumentation))
				.addFilter(documentationFilter)
    			.build();
		
    }

    
    @Test
    public void testConnection(){
    	given(this.spec)
		.get("/resourcesInfo").then().statusCode(200);
    }
    
    @Test
    public void testById(){
    	given(this.spec)
    	.pathParam("id", "1 Peter 1:3 NLT")
    	.get("/api/verses/{id}")
    	.then()
    	.statusCode(200)
    	.body("data.attributes.book", equalTo("1 Peter"))
    	.body("data.attributes.chapter", equalTo(1))
    	.body("data.attributes.verse", equalTo(3))
    	.body("data.attributes.version", equalTo("nlt"));
    	
    }
    
    @Test
    public void testByAllByIds(){
    	given(this.spec)
    	.get("/api/verses/1 Peter 1:3 NLT,Joel 2:5 NIV")
    	.then()
    	.statusCode(200)
    	.body("data", hasSize(2))
    	.body("data[0].attributes.book", equalTo("1 Peter"))
    	.body("data[0].attributes.chapter", equalTo(1))
    	.body("data[0].attributes.verse", equalTo(3))
    	.body("data[0].attributes.version", equalTo("nlt"))
    	.body("data[1].attributes.book", equalTo("Joel"))
    	.body("data[1].attributes.chapter", equalTo(2))
    	.body("data[1].attributes.verse", equalTo(5))
    	.body("data[1].attributes.version", equalTo("niv"));
    }
    
//    
//    @Test
//    public void testDisplayVerse(){
//    	given(this.spec)
//    	.queryParam("filter[displayVerse]", "Joel 1:2")
//    	.accept("application/vnd.api+json;charset=UTF-8") 
//		.get("/api/verses/").
//    	then()
//    	.statusCode(200)
//    	.body("data", hasSize(1))
//    	.body("data[0].attributes.book", equalTo("Joel"))
//    	.body("data[0].attributes.chapter", equalTo(1))
//    	.body("data[0].attributes.verse", equalTo(2));
//    }
//    
//    @Test
//    public void testSearchVerse(){
//    	given(this.spec)
//    	.queryParam("filter[verseContent]", "Joel")
//    	.queryParam("filter[version]", "NIV")
//    	.accept("application/vnd.api+json;charset=UTF-8") 
//		.get("/api/verses/").
//    	then()
//    	.statusCode(200)
//    	.body("data", hasSize(21));
//    }
//    
//    @Test
//    public void testExactVerse(){
//    	given(this.spec)
//    	.queryParam("filter[version]", "nlt")
//    	.queryParam("filter[book]", "Joel")
//    	.queryParam("filter[chapter]", "1")
//    	.queryParam("filter[verse]", "1")
//    	.queryParam("filter[endChapter]", "2")
//    	.queryParam("filter[endVerse]", "3")
//    	.accept("application/vnd.api+json;charset=UTF-8") 
//		.get("/api/verses/").
//    	then()
//    	.statusCode(200)
//    	.body("data", hasSize(1));
//    }
// 
}
