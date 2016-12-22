package com.joeldholmes.services.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.joeldholmes.exceptions.ServiceException;
import com.joeldholmes.services.interfaces.IReligiousTextIndexService;
import com.joeldholmes.utils.ErrorCodes;

@Service("ReligiousTextIndexService")
@Scope("singleton")
public class ReligiousTextIndexService implements IReligiousTextIndexService {
	
	private int[] quran = new int[]{7, 286, 200, 176, 120, 165, 206, 75, 129, 109, 123, 111, 43, 52, 99, 128, 111, 110, 98, 135, 112, 78, 118, 64, 77, 227, 93, 88, 69, 60, 34, 30, 73, 54, 45, 83, 182, 88, 75, 85, 54, 53, 89, 59, 37, 35, 38, 29, 18, 45, 60, 49, 62, 55, 78, 96, 29, 22, 24, 13, 14, 11, 11, 18, 12, 12, 30, 52, 52, 44, 28, 28, 20, 56, 40, 31, 50, 40, 46, 42, 29, 19, 36, 25, 22, 17, 19, 26, 30, 20, 15, 21, 11, 8, 8, 19, 5, 8, 8, 11, 11, 8, 3, 9, 5, 4, 7, 3, 6, 3, 5, 4, 5, 6};
	private int[] tao = new int[]{4, 4, 3, 3, 2, 1, 2, 3, 2, 3, 1, 2, 3, 3, 4, 2, 2, 2, 2, 2, 1, 3, 3, 1, 4, 2, 2, 2, 2, 4, 3, 5, 2, 3, 2, 3, 3, 7, 3, 2, 3, 3, 2, 3, 2, 2, 2, 3, 3, 4, 4, 5, 3, 4, 4, 3, 3, 3, 3, 3, 4, 4, 3, 4, 3, 3, 4, 1, 2, 3, 2, 4, 2, 2, 3, 4, 4, 4, 3, 5, 3};
	private Map<String, int[]> bible = new HashMap<String, int[]>();
	private Map<String, Integer> quranChapterNames = new HashMap<String, Integer>();
	
	protected ReligiousTextIndexService(){
		//No code, singleton, only one instance.
	}
	
	@PostConstruct
	private void init() throws ServiceException{
		bible.put("judges", new int[]{36, 23, 31, 24, 31, 40, 25, 35, 57, 18, 40, 15, 25, 20, 20, 31, 13, 31, 30, 48, 25});
		bible.put("micah", new int[]{16, 13, 12, 13, 15, 16, 20});
		bible.put("proverbs", new int[]{33, 22, 35, 27, 23, 35, 27, 36, 18, 32, 31, 28, 25, 35, 33, 33, 28, 24, 29, 30, 31, 29, 35, 34, 28, 28, 27, 28, 27, 33, 31});
		bible.put("revelation", new int[]{20, 29, 22, 11, 14, 17, 17, 13, 21, 11, 19, 17, 18, 20, 8, 21, 18, 24, 21, 15, 27, 21});
		bible.put("deuteronomy", new int[]{46, 37, 29, 49, 33, 25, 26, 20, 29, 22, 32, 32, 18, 29, 23, 22, 20, 22, 21, 20, 23, 30, 25, 22, 19, 19, 26, 68, 29, 20, 30, 52, 29, 12});
		bible.put("haggai", new int[]{15, 23});
		bible.put("3 john", new int[]{14});
		bible.put("1 kings", new int[]{53, 46, 28, 34, 18, 38, 51, 66, 28, 29, 43, 33, 34, 31, 34, 34, 24, 46, 21, 43, 29, 53});
		bible.put("mark", new int[]{45, 28, 35, 41, 43, 56, 37, 38, 50, 52, 33, 44, 37, 72, 47, 20});
		bible.put("matthew", new int[]{25, 23, 17, 25, 48, 34, 29, 34, 38, 42, 30, 50, 58, 36, 39, 28, 27, 35, 30, 34, 46, 46, 39, 51, 46, 75, 66, 20});
		bible.put("1 thessalonians", new int[]{10, 20, 13, 18, 28});
		bible.put("daniel", new int[]{21, 49, 30, 37, 31, 28, 28, 27, 27, 21, 45, 13});
		bible.put("malachi", new int[]{14, 17, 18, 6});
		bible.put("colossians", new int[]{29, 23, 25, 18});
		bible.put("ruth", new int[]{22, 23, 18, 22});
		bible.put("genesis", new int[]{31, 25, 24, 26, 32, 22, 24, 22, 29, 32, 32, 20, 18, 24, 21, 16, 27, 33, 38, 18, 34, 24, 20, 67, 34, 35, 46, 22, 35, 43, 55, 32, 20, 31, 29, 43, 36, 30, 23, 23, 57, 38, 34, 34, 28, 34, 31, 22, 33, 26});
		bible.put("2 samuel", new int[]{27, 32, 39, 12, 25, 23, 29, 18, 13, 19, 27, 31, 39, 33, 37, 23, 29, 33, 43, 26, 22, 51, 39, 25});
		bible.put("obadiah", new int[]{21});
		bible.put("esther", new int[]{22, 23, 15, 17, 14, 14, 10, 17, 32, 3});
		bible.put("exodus", new int[]{22, 25, 22, 31, 23, 30, 25, 32, 35, 29, 10, 51, 22, 31, 27, 36, 16, 27, 25, 26, 36, 31, 33, 18, 40, 37, 21, 43, 46, 38, 18, 35, 23, 35, 35, 38, 29, 31, 43, 38});
		bible.put("jeremiah", new int[]{19, 37, 25, 31, 31, 30, 34, 22, 26, 25, 23, 17, 27, 22, 21, 21, 27, 23, 15, 18, 14, 30, 40, 10, 38, 24, 22, 17, 32, 24, 40, 44, 26, 22, 19, 32, 21, 28, 18, 16, 18, 22, 13, 30, 5, 28, 7, 47, 39, 46, 64, 34});
		bible.put("ephesians", new int[]{23, 22, 21, 32, 33, 24});
		bible.put("habakkuk", new int[]{17, 20, 19});
		bible.put("luke", new int[]{80, 52, 38, 44, 39, 49, 50, 56, 62, 42, 54, 59, 35, 35, 32, 31, 37, 43, 48, 47, 38, 71, 56, 53});
		bible.put("song of solomon", new int[]{17, 17, 11, 16, 16, 13, 13, 14});
		bible.put("jonah", new int[]{17, 10, 10, 11});
		bible.put("acts", new int[]{26, 47, 26, 37, 42, 15, 60, 40, 43, 48, 30, 25, 52, 28, 41, 40, 34, 28, 41, 38, 40, 30, 35, 27, 27, 32, 44, 31});
		bible.put("job", new int[]{22, 13, 26, 21, 27, 30, 21, 22, 35, 22, 20, 25, 28, 22, 35, 22, 16, 21, 29, 29, 34, 30, 17, 25, 6, 14, 23, 28, 25, 31, 40, 22, 33, 37, 16, 33, 24, 41, 30, 24, 34, 17});
		bible.put("titus", new int[]{16, 15, 15});
		bible.put("2 timothy", new int[]{18, 26, 17, 22});
		bible.put("james", new int[]{27, 26, 18, 17, 20});
		bible.put("2 john", new int[]{13});
		bible.put("isaiah", new int[]{31, 22, 26, 6, 30, 13, 25, 22, 21, 34, 16, 6, 22, 32, 9, 14, 14, 7, 25, 6, 17, 25, 18, 23, 12, 21, 13, 29, 24, 33, 9, 20, 24, 17, 10, 22, 38, 22, 8, 31, 29, 25, 28, 28, 25, 13, 15, 22, 26, 11, 23, 15, 12, 17, 13, 12, 21, 14, 21, 22, 11, 12, 19, 12, 25, 24});
		bible.put("2 thessalonians", new int[]{12, 17, 18});
		bible.put("1 chronicles", new int[]{54, 55, 24, 43, 26, 81, 40, 40, 44, 14, 47, 40, 14, 17, 29, 43, 27, 17, 19, 8, 30, 19, 32, 31, 31, 32, 34, 21, 30});
		bible.put("1 timothy", new int[]{20, 15, 16, 16, 25, 21});
		bible.put("leviticus", new int[]{17, 16, 17, 35, 19, 30, 38, 36, 24, 20, 47, 8, 59, 57, 33, 34, 16, 30, 37, 27, 24, 33, 44, 23, 55, 46, 34});
		bible.put("1 peter", new int[]{25, 25, 22, 19, 14});
		bible.put("psalms", new int[]{6, 12, 8, 8, 12, 10, 17, 9, 20, 18, 7, 8, 6, 7, 5, 11, 15, 50, 14, 9, 13, 31, 6, 10, 22, 12, 14, 9, 11, 12, 24, 11, 22, 22, 28, 12, 40, 22, 13, 17, 13, 11, 5, 26, 17, 11, 9, 14, 20, 23, 19, 9, 6, 7, 23, 13, 11, 11, 17, 12, 8, 12, 11, 10, 13, 20, 7, 35, 36, 5, 24, 20, 28, 23, 10, 12, 20, 72, 13, 19, 16, 8, 18, 12, 13, 17, 7, 18, 52, 17, 16, 15, 5, 23, 11, 13, 12, 9, 9, 5, 8, 28, 22, 35, 45, 48, 43, 13, 31, 7, 10, 10, 9, 8, 18, 19, 2, 29, 176, 7, 8, 9, 4, 8, 5, 6, 5, 6, 8, 8, 3, 18, 3, 3, 21, 26, 9, 8, 24, 13, 10, 7, 12, 15, 21, 10, 20, 14, 9, 6});
		bible.put("zephaniah", new int[]{18, 15, 20});
		bible.put("joel", new int[]{20, 32, 21});
		bible.put("nahum", new int[]{15, 13, 19});
		bible.put("jude", new int[]{25});
		bible.put("2 chronicles", new int[]{17, 18, 17, 22, 14, 42, 22, 18, 31, 19, 23, 16, 22, 15, 19, 14, 19, 34, 11, 37, 20, 12, 21, 27, 28, 23, 9, 27, 36, 27, 21, 33, 25, 33, 27, 23});
		bible.put("hosea", new int[]{11, 23, 5, 19, 15, 11, 16, 14, 17, 15, 12, 14, 16, 9});
		bible.put("zechariah", new int[]{21, 13, 10, 14, 11, 15, 14, 23, 17, 12, 17, 14, 9, 21});
		bible.put("nehemiah", new int[]{11, 20, 32, 23, 19, 19, 73, 18, 38, 39, 36, 47, 31});
		bible.put("john", new int[]{51, 25, 36, 54, 47, 71, 53, 59, 41, 42, 57, 50, 38, 31, 27, 33, 26, 40, 42, 31, 25});
		bible.put("1 john", new int[]{10, 29, 24, 21, 21});
		bible.put("lamentations", new int[]{22, 22, 66, 22, 22});
		bible.put("amos", new int[]{15, 16, 15, 13, 27, 14, 17, 14, 15});
		bible.put("1 samuel", new int[]{28, 36, 21, 22, 12, 21, 17, 22, 27, 27, 15, 25, 23, 52, 35, 23, 58, 30, 24, 42, 15, 23, 29, 22, 44, 25, 12, 25, 11, 31, 13});
		bible.put("joshua", new int[]{18, 24, 17, 24, 15, 27, 26, 35, 27, 43, 23, 24, 33, 15, 63, 10, 18, 28, 51, 9, 45, 34, 16, 33});
		bible.put("1 corinthians", new int[]{31, 16, 23, 21, 13, 20, 40, 13, 27, 33, 34, 31, 13, 40, 58, 24});
		bible.put("ezra", new int[]{11, 70, 13, 24, 17, 22, 28, 36, 15, 44});
		bible.put("romans", new int[]{32, 29, 31, 25, 21, 23, 25, 39, 33, 21, 36, 21, 14, 23, 33, 27});
		bible.put("2 corinthians", new int[]{24, 17, 18, 18, 21, 18, 16, 24, 15, 18, 33, 21, 14});
		bible.put("hebrews", new int[]{14, 18, 19, 16, 14, 20, 28, 13, 28, 39, 40, 29, 25});
		bible.put("ezekiel", new int[]{28, 10, 27, 17, 17, 14, 27, 18, 11, 22, 25, 28, 23, 23, 8, 63, 24, 32, 14, 49, 32, 31, 49, 27, 17, 21, 36, 26, 21, 26, 18, 32, 33, 31, 15, 38, 28, 23, 29, 49, 26, 20, 27, 31, 25, 24, 23, 35});
		bible.put("2 peter", new int[]{21, 22, 18});
		bible.put("philippians", new int[]{30, 30, 21, 23});
		bible.put("numbers", new int[]{54, 34, 51, 49, 31, 27, 89, 26, 23, 36, 35, 16, 33, 45, 41, 50, 13, 32, 22, 29, 35, 41, 30, 25, 18, 65, 23, 31, 40, 16, 54, 42, 56, 29, 34, 13});
		bible.put("philemon", new int[]{25});
		bible.put("galatians", new int[]{24, 21, 29, 31, 26, 18});
		bible.put("2 kings", new int[]{18, 25, 27, 44, 27, 33, 20, 29, 37, 36, 21, 21, 25, 29, 38, 20, 41, 37, 37, 21, 26, 20, 37, 20, 30});
		bible.put("ecclesiastes", new int[]{18, 26, 22, 16, 20, 12, 29, 17, 18, 20, 10, 14});
		
		quranChapterNames.put("The Light".toLowerCase(), 24);
		quranChapterNames.put("The Criterion".toLowerCase(), 25);
		quranChapterNames.put("The Poets".toLowerCase(), 26);
		quranChapterNames.put("The Ant".toLowerCase(), 27);
		quranChapterNames.put("Ta Ha".toLowerCase(), 20);
		quranChapterNames.put("The Prophets".toLowerCase(), 21);
		quranChapterNames.put("The Pilgrimage".toLowerCase(), 22);
		quranChapterNames.put("The Believers".toLowerCase(), 23);
		quranChapterNames.put("The Narrative".toLowerCase(), 28);
		quranChapterNames.put("The Spider".toLowerCase(), 29);
		quranChapterNames.put("Women".toLowerCase(), 4);
		quranChapterNames.put("The Spoils Of War".toLowerCase(), 8);
		quranChapterNames.put("The Banishment".toLowerCase(), 59);
		quranChapterNames.put("The Pleading One".toLowerCase(), 58);
		quranChapterNames.put("The Beneficient".toLowerCase(), 55);
		quranChapterNames.put("The Moon".toLowerCase(), 54);
		quranChapterNames.put("The Iron".toLowerCase(), 57);
		quranChapterNames.put("The Great Event".toLowerCase(), 56);
		quranChapterNames.put("The Scatterers".toLowerCase(), 51);
		quranChapterNames.put("Qaf".toLowerCase(), 50);
		quranChapterNames.put("The Star".toLowerCase(), 53);
		quranChapterNames.put("The Mountain".toLowerCase(), 52);
		quranChapterNames.put("The Men".toLowerCase(), 114);
		quranChapterNames.put("The Overwhelming".toLowerCase(), 88);
		quranChapterNames.put("The Daybreak".toLowerCase(), 89);
		quranChapterNames.put("The Flame".toLowerCase().toLowerCase(), 111);
		quranChapterNames.put("The Help".toLowerCase(), 110);
		quranChapterNames.put("The Dawn".toLowerCase(), 113);
		quranChapterNames.put("The Unity".toLowerCase(), 112);
		quranChapterNames.put("The Cleaving Asund".toLowerCase(), 82);
		quranChapterNames.put("The Defrauders".toLowerCase(), 83);
		quranChapterNames.put("He Frowned".toLowerCase(), 80);
		quranChapterNames.put("The Covering Up".toLowerCase(), 81);
		quranChapterNames.put("The Night-Comer".toLowerCase(), 86);
		quranChapterNames.put("The Most High".toLowerCase(), 87);
		quranChapterNames.put("The Bursting Asund".toLowerCase(), 84);
		quranChapterNames.put("The Mansions Of The Stars".toLowerCase(), 85);
		quranChapterNames.put("The Family Of Imran".toLowerCase(), 3);
		quranChapterNames.put("The Elevated Place".toLowerCase(), 7);
		quranChapterNames.put("The Heavenly Fount".toLowerCase(), 108);
		quranChapterNames.put("The Unbelievers".toLowerCase(), 109);
		quranChapterNames.put("The Multiplicatio".toLowerCase(), 102);
		quranChapterNames.put("Time".toLowerCase(), 103);
		quranChapterNames.put("The Assaulters".toLowerCase(), 100);
		quranChapterNames.put("The Terrible Calam".toLowerCase(), 101);
		quranChapterNames.put("The Qureaish".toLowerCase(), 106);
		quranChapterNames.put("The Daily Necessar".toLowerCase(), 107);
		quranChapterNames.put("The Slanderer".toLowerCase(), 104);
		quranChapterNames.put("The Elephant".toLowerCase(), 105);
		quranChapterNames.put("The Companies".toLowerCase(), 39);
		quranChapterNames.put("Suad".toLowerCase(), 38);
		quranChapterNames.put("The Allies".toLowerCase(), 33);
		quranChapterNames.put("The Adoration".toLowerCase(), 32);
		quranChapterNames.put("Luqman".toLowerCase(), 31);
		quranChapterNames.put("The Romans".toLowerCase(), 30);
		quranChapterNames.put("The Rangers".toLowerCase(), 37);
		quranChapterNames.put("Ya Seen".toLowerCase(), 36);
		quranChapterNames.put("The Originator".toLowerCase(), 35);
		quranChapterNames.put("Saba".toLowerCase(), 34);
		quranChapterNames.put("The Examined One".toLowerCase(), 60);
		quranChapterNames.put("The Ranks".toLowerCase(), 61);
		quranChapterNames.put("Friday".toLowerCase(), 62);
		quranChapterNames.put("The Hypocrites".toLowerCase(), 63);
		quranChapterNames.put("Loss And Gain".toLowerCase(), 64);
		quranChapterNames.put("The Divorce".toLowerCase(), 65);
		quranChapterNames.put("The Prohibition".toLowerCase(), 66);
		quranChapterNames.put("The Kingdom".toLowerCase(), 67);
		quranChapterNames.put("The Pen".toLowerCase(), 68);
		quranChapterNames.put("The Sure Calamity".toLowerCase(), 69);
		quranChapterNames.put("The Cow".toLowerCase(), 2);
		quranChapterNames.put("The Cattle".toLowerCase(), 6);
		quranChapterNames.put("The Shaking".toLowerCase(), 99);
		quranChapterNames.put("The Clear Evidence".toLowerCase(), 98);
		quranChapterNames.put("The Sun".toLowerCase(), 91);
		quranChapterNames.put("The City".toLowerCase(), 90);
		quranChapterNames.put("The Early Hours".toLowerCase(), 93);
		quranChapterNames.put("The Night".toLowerCase(), 92);
		quranChapterNames.put("The Fig".toLowerCase(), 95);
		quranChapterNames.put("The Expansion".toLowerCase(), 94);
		quranChapterNames.put("The Majesty".toLowerCase(), 97);
		quranChapterNames.put("The Clot".toLowerCase(), 96);
		quranChapterNames.put("Hud".toLowerCase(), 11);
		quranChapterNames.put("Yunus".toLowerCase(), 10);
		quranChapterNames.put("The Thunder".toLowerCase(), 13);
		quranChapterNames.put("Yusuf".toLowerCase(), 12);
		quranChapterNames.put("The Rock".toLowerCase(), 15);
		quranChapterNames.put("Ibrahim".toLowerCase(), 14);
		quranChapterNames.put("The Israelites".toLowerCase(), 17);
		quranChapterNames.put("The Bee".toLowerCase(), 16);
		quranChapterNames.put("Marium".toLowerCase(), 19);
		quranChapterNames.put("The Cave".toLowerCase(), 18);
		quranChapterNames.put("The Victory".toLowerCase(), 48);
		quranChapterNames.put("The Chambers".toLowerCase(), 49);
		quranChapterNames.put("The Sandhills".toLowerCase(), 46);
		quranChapterNames.put("Muhammad".toLowerCase(), 47);
		quranChapterNames.put("The Evident Smoke".toLowerCase(), 44);
		quranChapterNames.put("The Kneeling".toLowerCase(), 45);
		quranChapterNames.put("The Counsel".toLowerCase(), 42);
		quranChapterNames.put("The Embellishment".toLowerCase(), 43);
		quranChapterNames.put("The Believer".toLowerCase(), 40);
		quranChapterNames.put("Ha Mim".toLowerCase(), 41);
		quranChapterNames.put("The Opening".toLowerCase(), 1);
		quranChapterNames.put("The Food".toLowerCase(), 5);
		quranChapterNames.put("Repentance".toLowerCase(), 9);
		quranChapterNames.put("The Emissaries".toLowerCase(), 77);
		quranChapterNames.put("The Man".toLowerCase(), 76);
		quranChapterNames.put("The Resurrection".toLowerCase(), 75);
		quranChapterNames.put("The Clothe Done".toLowerCase(), 74);
		quranChapterNames.put("The Wrapped Up".toLowerCase(), 73);
		quranChapterNames.put("The Jinn".toLowerCase(), 72);
		quranChapterNames.put("Nuh".toLowerCase(), 71);
		quranChapterNames.put("The Ways Of Ascent".toLowerCase(), 70);
		quranChapterNames.put("Those Who Pull Out".toLowerCase(), 79);
		quranChapterNames.put("The Great Event".toLowerCase(), 78);
	}

	@Override
	public int maxBibleBookChapters(String book) throws ServiceException {
		if(book == null || book.trim().isEmpty()){
			throw new ServiceException(ErrorCodes.NULL_INPUT, "Book cannot be null or empty");
		}
		book = book.toLowerCase().trim();
		int[] chapters = bible.get(book);
		if(chapters != null){
			return chapters.length;
		}
		else{
			return 0;
		}
	}

	@Override
	public int maxBibleBookChapterVerses(String book, int chapter) throws ServiceException {
		if(book == null || book.trim().isEmpty()){
			throw new ServiceException(ErrorCodes.NULL_INPUT, "Book cannot be null or empty");
		}
		if(chapter<1){
			throw new ServiceException(ErrorCodes.INVALID_INPUT, "Chapter must be greater than 0");
		}
		chapter -=1;
		book = book.trim().toLowerCase();
		int[] chapters = bible.get(book);
		if(chapters != null){
			if(chapter<chapters.length){
				return chapters[chapter];
			}
			else{
				return 0;
			}
		}
		else{
			return 0;
		}
	}

	@Override
	public int maxQuranChapters(){
		return quran.length;
	}

	@Override
	public int maxQuranChapterVerses(int chapter) throws ServiceException {
		if(chapter<1){
			throw new ServiceException(ErrorCodes.INVALID_INPUT, "Chapter must be greater than 0");
		}
		chapter -=1;
		if(chapter<quran.length){
			return quran[chapter];
		}
		else{
			return 0;
		}
	}

	@Override
	public int maxTaoChapters() throws ServiceException {
		return tao.length;
	}

	@Override
	public int maxTaoChapterVerses(int chapter) throws ServiceException {
		if(chapter<1){
			throw new ServiceException(ErrorCodes.INVALID_INPUT, "Chapter must be greater than 0");
		}
		chapter -=1;
		if(chapter<tao.length){
			return tao[chapter];
		}
		else{
			return 0;
		}
	}
	
	@Override
	public int quranChapterNameLookup(String name) throws ServiceException {
		if(name == null || name.isEmpty()){
			throw new ServiceException(ErrorCodes.NULL_INPUT, "Chapter name must not be null");
		}
		Integer chapter = quranChapterNames.get(name.toLowerCase());
		if(chapter == null){
			throw new ServiceException(ErrorCodes.INVALID_INPUT, "Invalid Chapter Name");
		}
		return chapter.intValue();
	}
	
	

}
