package com.test.wordGenerate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import com.test.wordGenerate.entry.WordsDb;

public class GenerateWords {
	private static final int MIN_INDEX = 19968;
	private static final int MAX_INDEX = 40869;
	private static final String CR = "\r\n";
	private static final String TAB = "\t";

	public void execute(String fileName) throws IOException {
		File f = new File("/Users/jack/Desktop/" + fileName);
		FileWriter fw = new FileWriter(f);
		fw.write("字符" + TAB + "Unicode十进制" + TAB + "Unicode十六进制" + TAB + TAB + "GBK十进制" + TAB + "GBK十六进制" + CR);
		fw.write("==================================================================================" + CR);
		int GBKCode;
		for (int i = MIN_INDEX; i <= MAX_INDEX; i++) {
			GBKCode = getGBKCode(i);
			fw.write((char) i + TAB + i + TAB + TAB + Integer.toHexString(i) + TAB + TAB + TAB + GBKCode + TAB + TAB
					+ Integer.toHexString(GBKCode) + CR);
		}
		fw.flush();
		System.out.println("Done!");
	}

	private int getGBKCode(int unicodeCode) throws UnsupportedEncodingException {
		char c = (char) unicodeCode;
		byte[] bytes = (c + "").getBytes("GBK");
		return ((bytes[0] & 255) << 8) + (bytes[1] & 255);
	}

	public void genWordCode(String fileName) throws Exception {
		WordsDb db = new WordsDb();
		Map<String, int[]> dbs = new HashMap<>();
		for (int i = MIN_INDEX; i <= MAX_INDEX; i++) {
			int GBKCode = getGBKCode(i);
			dbs.put(String.valueOf((char) i), GenerateWordUntil.get32PxWordInfo(String.valueOf((char) i)));
		}
		db.setWordDB(dbs);
		File file = new File("C://Users//Administrator//Desktop//WordsDB.dat");
		ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(file));
		os.writeObject(db);
		os.flush();
		os.close();
	}

	public static void main(String[] args) throws Exception {
		// new GenerateWords().execute("汉字编码一览表.txt");
		new GenerateWords().genWordCode("汉");
	}
}
