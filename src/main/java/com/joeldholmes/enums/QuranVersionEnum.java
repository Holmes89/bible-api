package com.joeldholmes.enums;

public enum QuranVersionEnum {
	PICKTHALL("pickthall"),
	SHAKIR("shakir");
	
	private String name;
	 
	QuranVersionEnum(String name){
		this.name = name;
	}
	
	public String getName(){
		return this.name;
	}
	
	public static QuranVersionEnum findByName(String name){
		for(QuranVersionEnum e: QuranVersionEnum.values()){
			if(name.equalsIgnoreCase(e.name())){
				return e;
			}
		}
		return null;
	}
}
