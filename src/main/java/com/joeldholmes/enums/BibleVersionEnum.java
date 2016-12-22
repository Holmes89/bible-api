package com.joeldholmes.enums;

import java.util.ArrayList;
import java.util.List;

public enum BibleVersionEnum {
	AMP("amp", "Amplified"),
	ASV("asv", "American Standard Version"),
	CEV("cev", "Contemporary English Version"),
	DARBY("darby", "Darby Version"),
	ESV("esv", "English Standard Version"),
	KJV("kjv", "King James Version"),
	MSG("msg", "The Message"),
	NASB("nasb","New American Standard Bible"),
	NIV("niv", "New International Version"),
	NKJV("nkjv", "New King James Version"),
	NLT("nlt", "New Living Translation"),
	NRSV("nrsv", "New Revised Standard Version"),
	YLT("ylt", "Young's Literal Translation");
	
	private String abbr;
	private String fullName;
	
	BibleVersionEnum(String abbr, String fullName){
		this.abbr = abbr;
		this.fullName = fullName;
	}
	
	public String getAbbr(){
		return this.abbr;
	}
	
	public String getFullName(){
		return this.fullName;
	}
	
	public static List<String> getAllAbbreviations(){
		List<String> abbrs = new ArrayList<String>();
		for(BibleVersionEnum e: BibleVersionEnum.values()){
			abbrs.add(e.getAbbr());
		}
		return abbrs;
	}
	
	public static BibleVersionEnum findByAbbreviation(String abbr){
		for(BibleVersionEnum e: BibleVersionEnum.values()){
			if(abbr.equalsIgnoreCase(e.getAbbr())){
				return e;
			}
		}
		return null;
	}
}
