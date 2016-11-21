package com.test.wordGenerate;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Random;

import javax.imageio.ImageIO;

import com.test.image.SegmentFactory;

public class GenerateWordUntil {

	public static void outputImage(int w, int h, OutputStream os, String code) throws IOException {
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
		for (int i = 0; i < 32; i++) {
			for (int j = 0; j < 32; j++) {
				if(image.getRGB(j, i)==-1){
					System.out.print( 0+ " ");
				}else{
					System.out.print( 1+ " ");
				}
			}
			System.out.println();
		}
		ImageIO.write(image, "jpg", os);
	}

	public static void main(String[] args) throws Exception {

		outputImage(32, 32,new FileOutputStream(new File("/Users/jack/Desktop/个.jpg")), "个");
	}

	public void segment() throws Exception {
		File dir = new File("/Users/jack/Desktop/" + "教28" + ".jpg");

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
		File wf = new File("/Users/jack/Desktop/教otsuThresh.jpg");
		ImageIO.write(image, "jpg", wf);
	}

}
