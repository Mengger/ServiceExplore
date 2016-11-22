package com.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.test.image.SegmentFactory;
import com.test.wordGenerate.entry.WordsDb;

public class ImageCompareUntil {

	private static WordsDb WDB;
	
	static {
		File file = new File("/Users/jack/Desktop/dbs/WordsDB.dat");
		try {
			FileInputStream fio = new FileInputStream(file);
			ObjectInputStream io = new ObjectInputStream(fio);
			WDB = (WordsDb)io.readObject();
			io.close();
			fio.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static int[] Test() throws Exception{
		/*BufferedImage img = ImageIO.read(new File("C://Users//Administrator//Desktop//个seg.jpg"));
		System.out.println(img.getHeight()+"*********"+img.getWidth());
		
		int[] rtn = new int[32];
		int cellValue = 0;
		for (int i = 0; i < img.getHeight(); i++) {
			for (int j = 0; j < img.getWidth(); j++) {
				if(img.getRGB(j, i)==-1){
					System.out.print( 0+ " ");
				}else{
					System.out.print( 1+ " ");
					cellValue = cellValue|0x1<<(30-j);
				}
			}
			System.out.println();
			rtn[i]=cellValue;
		}
		return rtn;*/
		
		
		File file = new File("/Users/jack/Desktop/cutImg/22.png");
		BufferedImage image = ImageIO.read(file);
		int h=image.getHeight();
		int w=image.getWidth();
		int[][] img = new int[h][w];
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				img[i][j] = image.getRGB(j, i);
			}
		}
		
		int[][] newImg = new ShrinkExpansion().ResizeNear01(img, 28, 28);
		BufferedImage im = new BufferedImage(32, 32, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				im.setRGB(j, i, newImg[i][j]);
			}
		}
		try {
			ImageIO.write(im, "png",new File("/Users/jack/Desktop/dddd.png"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int[] rtn = new int[32];
		
		SegmentFactory seg = new SegmentFactory();
		int[][] imgg = seg.segmentByotsuThresh(newImg);
		for (int i = 0; i < imgg.length; i++) {
			int cellValue = 0;
			for (int j = 0; j < imgg[0].length; j++) {
				if(imgg[i][j]==-1){
					System.out.print("0 ");
				}else{
					cellValue = (0x1<<(30-j))|cellValue;
					System.out.print("1 ");
				}
			}
			System.out.println();
			rtn[i]=cellValue;
		}
		
		return rtn;
	}
	
	public static void main(String[] args) throws Exception{
		ImageCompareUntil a = new ImageCompareUntil();
		WordsDb dbs = a.WDB;
		Map<String, int[]> wdb = dbs.getWordDB();
		int[] co = a.Test();
		Map<String, Integer> result = new HashMap<>();
		for (String key:wdb.keySet()) {
			int[] value = wdb.get(key);
			int index = 0;
			int difficultCount = 0;
			for(int c:co){
				difficultCount += numberOf1(value[index]^c);
				index++;
			}
			result.put(key, difficultCount);
		}
		
		int small = 1000;
		String keyWords = "";
		for(String key:result.keySet()){
			if(result.get(key)<small){
				small=result.get(key);
				keyWords=key;
			}
		}
		System.out.println("keyWords***********"+keyWords);
		System.out.println("small**************"+small);
		
		for(int val:wdb.get(keyWords)){
			String v = Integer.toBinaryString(val);
			int length = v.length();
			if(length<32){
				int leave = 32-length;
				StringBuffer newV = new StringBuffer();
				for (int i = 0; i <leave; i++) {
					newV.append("0");
				}
				v=newV.toString()+v;
			}
			
			for (int i = 0; i < v.length(); i++) {
				System.out.print(v.substring(i, i+1)+" ");
				if(i == v.length()-1){
					System.out.println();
				}
			}
		}
		
		
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		
		
		int[] value = wdb.get("大");
		int index = 0;
		int difficultCount = 0;
		for(int c:co){
			difficultCount += numberOf1(value[index]^c);
			index++;
		}
		System.out.println(difficultCount);
		
		
	}
	
	public static int numberOf1(int n){
        int count = 0;
        while(n!=0){
            n = n&(n-1);
            count++;
        }
        return count;
    }
}
