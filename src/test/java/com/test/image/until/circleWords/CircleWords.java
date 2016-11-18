package com.test.image.until.circleWords;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.test.image.SegmentFactory;
import com.test.image.entry.ImageUnit;
import com.test.image.entry.Rectangel;

public class CircleWords {

	private ImageUnit image;
	
	private int[][] imageInfo;
	
	private int imageWidth;
	
	private int imageHeight;
	
	private int background;
	
	private Rectangel[][] rectangels;
	
	public static void main(String[] args) throws Exception {
		//File file = new File("/Users/jack/Desktop/111.png");
		File file = new File("C://Users//Administrator//Desktop//8.jpg");
		BufferedImage image = ImageIO.read(file);
		int h=image.getHeight();
		int w=image.getWidth();
		int[][] img = new int[h][w];
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				img[i][j] = image.getRGB(j, i);
			}
		}
		SegmentFactory seg = new SegmentFactory();
		int[][] imgg = seg.segmentByseg1(img);
		
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				//image.setRGB(j, i, -16777216);
				image.setRGB(j, i, imgg[i][j]);
			}
		}
		
		CircleWords ddd=new CircleWords(new ImageUnit(imgg), -1);
		ddd.deal();
		
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				//image.setRGB(j, i, -16777216);
				image.setRGB(j, i, ddd.imageInfo[i][j]);
			}
		}
		
		File wf = new File("C://Users//Administrator//Desktop//333.png");
		//File wf = new File("/Users/jack/Desktop/222.png");
		ImageIO.write(image, "png", wf);
		//ImageUnit imageUnit = new ImageUnit(img);
	}
	
	public CircleWords(ImageUnit image, int background) {
		this.image=image;
		this.imageInfo=image.getImageInfo();
		this.imageWidth=image.getImageWidth();
		this.imageHeight=image.getImageHeight();
		this.background=background;
	}
	
	
	public void deal(){
		
		for(Rectangel rectangel:getRectangels(transverseScannerImage())){
			/*int x1 = rectangel.getLeftUper().getX();
			int x2 = rectangel.getRightDown().getX();
			int y1 = rectangel.getLeftUper().getY();
			int y2 = rectangel.getRightDown().getY();
			for (int i = y1; i < y2; i++) {
				for (int j = x1; j < x2; j++) {
					imageInfo[i][j]=-16777216;
				}
			}*/
			int top = rectangel.getLeftUper().getY();
			int x1 = rectangel.getLeftUper().getX();
			int x2 = rectangel.getRightDown().getX();
			for (int i = x1; i < x2; i++) {
				imageInfo[top][i]=-16777216;
			}
			
			int buttom = rectangel.getRightDown().getY();
			for (int i = x1; i < x2; i++) {
				imageInfo[buttom][i]=-16777216;
			}
			
			int y1 = rectangel.getLeftUper().getY();
			int y2 = rectangel.getRightDown().getY();
			for (int i = 0; i < imageHeight; i++) {
				if(i>y1&&i<=y2){
					this.imageInfo[i][x1]=-16777216;
				}
			}
			for (int i = 0; i < imageHeight; i++) {
				if(i>=y1&&i<=y2){
					this.imageInfo[i][x2]=-16777216;
				}
			}
		}
	}
	/*
	 * 横向扫描图片 
	 */
	public int[] transverseScannerImage(){
		int[] pixNum = new int[imageHeight];
		for (int i = 0; i < imageHeight; i++) {
			int num = 0;
			for (int j = 0; j < imageWidth; j++) {
				if(imageInfo[i][j]!=background){
					num++;
				}
			}
			pixNum[i]=num;
		}
		return pixNum;
	}
	
	public List<Rectangel> getRectangels(int[] pixNum){
		List<Rectangel> rtn = new ArrayList<>();
		List<Integer> topEdgeIndex = new ArrayList<>();
		List<Integer> buttomEdgeIndex = new ArrayList<>();
		boolean isTopEdge = true;
		for (int i = 0; i < pixNum.length; i++) {
			
			if(isTopEdge){
				if(pixNum[i]!=0){
					topEdgeIndex.add(i);
				}
				isTopEdge = !isTopEdge;
			}else{
				if(pixNum[i]==0){
					buttomEdgeIndex.add(i);
				}
				isTopEdge = !isTopEdge;
			}
			
		}
		for (int i = 0; i < topEdgeIndex.size(); i++) {
			int y1 = topEdgeIndex.get(i);
			int y2 = buttomEdgeIndex.get(i);
			int[] endWisePress = new int[imageWidth];
			int index = 0;
			for (int x = 0; x < imageWidth; x++) {
				int endWise = 0;
				for (int y = y1; i < y2; i++) {
					if(imageInfo[y][x]!=background){
						endWise++;
					}
				}
				endWisePress[index] = endWise;
				index++;
			}
			
			boolean isLeftEdge = true;
			List<Integer> leftIndex = new ArrayList<>();
			List<Integer> rightIndex = new ArrayList<>();
			
			for (int j = 0; j < endWisePress.length; j++) {
				if(isLeftEdge){
					if(pixNum[i]!=0){
						leftIndex.add(j);
					}
					isLeftEdge = !isLeftEdge;
				}else{
					if(pixNum[i]==0){
						rightIndex.add(j);
					}
					isLeftEdge = !isLeftEdge;
				}
				
			}
			
			for (int j = 0; j < leftIndex.size(); j++) {
				rtn.add(new Rectangel(leftIndex.get(j), y1, rightIndex.get(j), y2));
			}
			
		}
		return rtn;
	}
	
}
