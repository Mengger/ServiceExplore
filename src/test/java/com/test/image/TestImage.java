package com.test.image;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import com.test.Test;
import com.test.image.until.ImageUntils;

public class TestImage {

	public static void main(String[] args) throws Exception {
		String fileName =  "ssssss.jpg";
		File file = new File("C://Users//Administrator//Desktop//img//1//" + fileName);
		String a = fileName.substring(0, fileName.indexOf("."));
		new File("C://Users//Administrator//Desktop//img//" + a).mkdirs();
		new Test();
		
		BufferedImage bi = (BufferedImage) ImageIO.read(file);
		
		
		int index = 0;
		for(BufferedImage b:ImageUntils.splitImage(bi)){
			new File("C://Users//Administrator//Desktop//img//1//seg").mkdir();
			ImageIO.write(b, "jpg",new File("C://Users//Administrator//Desktop//img//1//seg//"+index+".jpg"));
			index++;
		}
	}
	
	
}
