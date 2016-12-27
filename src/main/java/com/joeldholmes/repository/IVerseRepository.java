package com.joeldholmes.repository;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.joeldholmes.entity.VerseEntity;

@Repository
public interface IVerseRepository extends MongoRepository<VerseEntity, String>{
	
	static final Logger logger = Logger.getLogger(IVerseRepository.class);
	
	//Bible
	@Query("{\"_id\": ?0, \"religiousText\": \"bible\"}")
	VerseEntity getBibleVerseById(String id);
		
	@Query("{\"religiousText\": \"bible\",\"version\": ?0, \"book\": ?1,\"chapter\": ?2, \"verse\": ?3 }")
	VerseEntity getSingleBibleVerse(String version, String book, int chapter, int verse);
	
	@Query("{\"religiousText\": \"bible\",\"version\": ?0, \"book\": ?1,\"chapter\": ?2, \"verse\": { $lte:?4, $gte:?3 }}")
	List<VerseEntity> getBibleVersesInChapter(String version, String book, int chapter, int startVerse, int endVerse);	
	
	@Query("{\"religiousText\": \"bible\",\"version\": ?0, \"book\": ?1,\"chapter\": ?2}")
	List<VerseEntity> getBibleVersesInChapter(String version, String book, int chapter);	
	
	@Query("{\"religiousText\": \"bible\",\"version\": ?0, \"book\": ?1,\"chapter\": { $lte:?3, $gte:?2 }}")
	List<VerseEntity> getBibleVersesInChapterRange(String version, String book, int startChapter, int endChapter);	
	
	@Query("{$text: {$search: ?0}, \"religiousText\":\"bible\"}, {score: {$meta: \"textScore\"}}).sort({score:{$meta:\"textScore\"}}")
	List<VerseEntity> searchAllBibleText(String term);
		
	@Query("{$text: {$search: ?1}, \"religiousText\":\"bible\", \"version\": ?0}, {score: {$meta: \"textScore\"}}).sort({score:{$meta:\"textScore\"}}")
	List<VerseEntity> searchAllBibleTextAndVersion(String version, String term);
		
	//Find All
	@Query("{\"_id\": {$in: ?0}}")
	List<VerseEntity> findAll(List<String> ids);

}
