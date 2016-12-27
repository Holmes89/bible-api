package com.joeldholmes.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

@Configuration
@EnableMongoRepositories
class MongoDbConfig extends AbstractMongoConfiguration {
	
	@Value("${db.name:bible-api}")
	String database;
	
	@Value("${db.host:ds145148.mlab.com}")
	String host;
	
	@Value("${db.port:45148}")
	Integer port;
	
	@Value("${db.username:holmes89}")
	String userName;

	@Value("${db.password:public}")
	String password;
		
	@Override
	public Mongo mongo() throws Exception {
		MongoClient mongoClient;
		if((userName==null)&&(password==null)){
			mongoClient = new MongoClient(new ServerAddress(host, port));
		}
		else{
			MongoCredential credential = MongoCredential.createCredential(userName, database, password.toCharArray());
			mongoClient = new MongoClient(new ServerAddress(host, port), Arrays.asList(credential));
		}
		return mongoClient;
	}
	
	@Override
	protected String getDatabaseName() {
		return database;
	}
	
	@Override
	public MongoTemplate mongoTemplate() throws Exception{
		return new MongoTemplate(mongo(), database);
	}
}