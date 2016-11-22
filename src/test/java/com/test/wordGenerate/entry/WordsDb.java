package com.test.wordGenerate.entry;

import java.io.Serializable;
import java.util.Map;

public class WordsDb implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private Map<String, int[]> wordDB;


	public Map<String, int[]> getWordDB() {
		return wordDB;
	}


	public void setWordDB(Map<String, int[]> wordDB) {
		this.wordDB = wordDB;
	}


	public WordsDb() {
	}


	public WordsDb(Map<String, int[]> wordDB) {
		super();
		this.wordDB = wordDB;
	}
	

}
