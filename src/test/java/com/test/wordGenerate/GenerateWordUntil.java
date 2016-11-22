package com.test.wordGenerate;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.test.image.SegmentFactory;

public class GenerateWordUntil {

	
	/**
	 * 根据字体 获取对应的点位信息
	 * @param code
	 * @return
	 * @throws Exception 
	 */
	public static int[] get32PxWordInfo(String code) throws Exception {
		int w=32,  h=32;
		int verifySize = code.length();
		BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = image.createGraphics();
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		g2.setColor(Color.white);// 设置边框色
		g2.fillRect(0, 0, w, h);
		g2.setColor(Color.black);
		Font font = new Font("楷体", Font.PLAIN, 32);
		g2.setFont(font);
		char[] chars = code.toCharArray();
		for (int i = 0; i < verifySize; i++) {
			g2.drawChars(chars, i, 1, 0, 28);
		}
		g2.dispose();
		
		
		int[][] img111 = new int[h][w];
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				img111[i][j] = image.getRGB(j, i);
			}
		}
		
		SegmentFactory seg = new SegmentFactory();
		int[][] imgg = seg.segmentByotsuThresh(img111);
		
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				if(imgg[i][j]==-1){
					System.out.print( 0+ " ");
				}else{
					System.out.print( 1+ " ");
				}
				image.setRGB(j, i, imgg[i][j]);
			}
			System.out.println();
		}
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		
		String path = "/Users/jack/Desktop/dbs/words/";
		ImageIO.write(image, "png", new File(path+code+".png"));
		
		int[] word = new int[32];
		for (int i = 0; i < 32; i++) {
			int cellValue = 0x0;
			for (int j = 0; j < 32; j++) {
				if(imgg[i][j]==-1){
					System.out.print( 0+ " ");
				}else{
					System.out.print( 1+ " ");
					cellValue = (0x1<<(30-j))|cellValue;
				}
			}
			System.out.println();
			word[i] = cellValue;
		}
		return word;
	}

	
	
	public static void segment() throws Exception {
		File dir = new File("C://Users//Administrator//Desktop//个.jpg");
		
		BufferedImage image = ImageIO.read(dir);
		int h = image.getHeight();
		int w = image.getWidth();
		int[][] img = new int[h][w];
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				img[i][j] = image.getRGB(j, i);
			}
		}
		SegmentFactory seg = new SegmentFactory();
		int[][] imgg = seg.segmentByotsuThresh(img);
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				image.setRGB(j, i, imgg[i][j]);
			}
		}
		File wf = new File("C://Users//Administrator//Desktop//个seg.jpg");
		ImageIO.write(image, "jpg", wf);
	}

}
