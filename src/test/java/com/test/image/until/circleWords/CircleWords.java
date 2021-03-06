package com.test.image.until.circleWords;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
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
		File file = new File("/Users/jack/Desktop/111.png");
		//File file = new File("C://Users//Administrator//Desktop//8.jpg");
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
		
	/*	for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				//image.setRGB(j, i, -16777216);
				image.setRGB(j, i, ddd.imageInfo[i][j]);
			}
		}*/
		
		//File wf = new File("C://Users//Administrator//Desktop//333.png");
		File wf = new File("/Users/jack/Desktop/555.jpg");
		ImageIO.write(image, "jpg", wf);
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
		String path = "/Users/jack/Desktop/cutImg/";
		int index=0;
		for(Rectangel rectangel:getRectangels(transverseScannerImage())){
			int w = rectangel.getWidth();
			int h = rectangel.getHeight();
			int[][] imgInfo = cutImageByRectangel(rectangel);
			BufferedImage im = new BufferedImage(rectangel.getWidth(), rectangel.getHeight(), BufferedImage.TYPE_INT_RGB);
			for (int i = 0; i < h; i++) {
				for (int j = 0; j < w; j++) {
					im.setRGB(j, i, imgInfo[i][j]);
				}
			}
			try {
				ImageIO.write(im, "png",new File(path+index+".png"));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			index++;
			
			//将矩形内的填充为黑色
			/*int x1 = rectangel.getLeftUper().getX();
			int x2 = rectangel.getRightDown().getX();
			int y1 = rectangel.getLeftUper().getY();
			int y2 = rectangel.getRightDown().getY();
			for (int i = y1; i < y2; i++) {
				for (int j = x1; j < x2; j++) {
					imageInfo[i][j]=-16777216;
				}
			}*/
			
			
			//画出矩形的四条边
			/*int top = rectangel.getLeftUper().getY();
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
			}*/
		}
	}
	
	/**
	 * 根据矩形 切出 图片的详细信息
	 * @param rectangel
	 * @return
	 */
	public int[][] cutImageByRectangel(Rectangel rectangel){ 
		int h = rectangel.getHeight();
		int w = rectangel.getWidth();
		
		int x0 = rectangel.getLeftUper().getX();
		int x1 = rectangel.getRightDown().getX();
		
		int y0 = rectangel.getLeftUper().getY();
		int y1 = rectangel.getRightDown().getY();
		
		int[][] rtn = new int[h][w];
		
		int y=0;
		for (int i = y0; i < y1; i++) {
			int x=0;
			for (int j = x0; j < x1; j++) {
				rtn[y][x] = imageInfo[i][j];
				x++;
			}
			y++;
		}
		return rtn;
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
					isTopEdge = !isTopEdge;
				}
			}else{
				if(pixNum[i]==0){
					buttomEdgeIndex.add(i);
					isTopEdge = !isTopEdge;
				}
			}
			
		}
		for (int i = 0; i < topEdgeIndex.size(); i++) {
			int y1 = topEdgeIndex.get(i);
			int y2 = buttomEdgeIndex.get(i);
			int[] endWisePress = new int[imageWidth];
			for (int x = 0; x < imageWidth; x++) {
				int endWise = 0;
				for (int y = y1; y < y2; y++) {
					if(imageInfo[y][x]!=background){
						endWise++;
					}
				}
				endWisePress[x] = endWise;
			}
			
			boolean isLeftEdge = true;
			List<Integer> leftIndex = new ArrayList<>();
			List<Integer> rightIndex = new ArrayList<>();
			
			for (int j = 0; j < endWisePress.length; j++) {
				if(isLeftEdge){
					if(endWisePress[j]!=0){
						leftIndex.add(j);
						isLeftEdge = !isLeftEdge;
					}
				}else{
					if(endWisePress[j]==0){
						rightIndex.add(j);
						isLeftEdge = !isLeftEdge;
					}
				}
				
			}
			
			for (int j = 0; j < leftIndex.size(); j++) {
				rtn.add(new Rectangel(leftIndex.get(j), y1, rightIndex.get(j), y2));
			}
			
		}
		return rtn;
	}
	
}
