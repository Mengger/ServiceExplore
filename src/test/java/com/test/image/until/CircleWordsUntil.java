package com.test.image.until;

import java.util.ArrayList;
import java.util.List;

import com.test.image.entry.ImageUnit;
import com.test.image.entry.Rectangel;
import com.test.image.entry.Rectangel.Poit;

public class CircleWordsUntil {

	private ImageUnit image;
	
	private int[][] imageInfo;
	
	private int imageWidth;
	
	private int imageHeight;
	
	private int background;
	
	private Rectangel[][] rectangels;
	
	public CircleWordsUntil(ImageUnit image,int background) {
		this.image=image;
		this.imageInfo=image.getImageInfo();
		this.imageWidth=image.getImageWidth();
		this.imageHeight=image.getImageHeight();
		this.background=background;
	}
	
	public void autoCircleWords(List<Rectangel> rectanelList){
		for(Rectangel rectangel:rectanelList){
			automaticCircleSingleWord(rectangel);
		}
	}

	/**
	 * 第一次截取点的信息
	 * @return
	 */
	private List<Rectangel> generatePoints(){
		List<Rectangel> rectangelList = new ArrayList<>();
		//首次产生16个点，在产生这个点之前  要确认 图片的大小  一个汉字要是显示清楚 至少是16*16像素
		if(imageHeight>100&&imageWidth>100){
			int h = imageHeight/5;
			int w = imageWidth/5;
			for (int i = 0; i < 5; i++) {
				for (int j = 0; j < 5; j++) {
					int centerX = w*(j+1);
					int centerY = h*(i+1);
					Rectangel rectangel = new Rectangel(centerX-4, centerY-4, centerX+4, centerY+4);
					rectangel.setBeginPont(centerX, centerY);
					rectangelList.add(rectangel);
				}
			}
		}else{
			//图片过小
			int x = imageWidth/20;
			int y = imageHeight/20;
			if(x>5) x=5;
			if(y>5) y=5;
			int w = imageWidth/x;
			int h = imageHeight/y;
			for (int i = 0; i < y; i++) {
				for (int j = 0; j < x; j++) {
					int centerX = w*(j+1);
					int centerY = h*(i+1);
					Rectangel rectangel = new Rectangel(centerX-4, centerY-4, centerX+4, centerY+4);
					rectangel.setBeginPont(centerX, centerY);
					rectangelList.add(rectangel);
				}
			}
		}
		return rectangelList;
	}
	
	
	/**
	 * 单个矩形 自动扩张 收缩 圈字
	 * @param rectangel
	 */
	public void automaticCircleSingleWord(Rectangel rectangel){
		boolean condition = true;
		while (condition) {
			autoExpansionToRight(rectangel);
			autoExpansionToLeft(rectangel);
			autoExpansionToUp(rectangel);
			autoExpansionToDown(rectangel);
			condition = judgeEdgeIsExitWord(rectangel);
		}
		Poit poit = rectangel.getBeginPont();
		int beginLeftX = poit.getX()-4;
		int beginLeftY = poit.getY()-4;
		int beginRightX = poit.getX()+4;
		int beginRightY = poit.getY()+4;
		if(beginLeftX<rectangel.getLeftUper().getX()){
			autoShrinkToRight(rectangel);
		}
		if(beginLeftY<rectangel.getLeftUper().getY()){
			autoShrinkToDown(rectangel);
		}
		if(beginRightX>rectangel.getRightDown().getX()){
			autoShrinkToLeft(rectangel);
		}
		if(beginRightY>rectangel.getRightDown().getY()){
			autoShrinkToUp(rectangel);
		}
	}
	
	
	/**
	 * 根据矩形当前的位置  收缩(左边 向右收缩)
	 * @param rectangel
	 */
	private void autoShrinkToRight(Rectangel rectangel){
		boolean condition = true;
		while(condition){
			int left = rectangel.getLeftUper().getX();
			int[] edge = new int[imageHeight];
			for (int i = 0; i < imageHeight; i++) {
				edge[i] = this.imageInfo[i][left];
			}
			int y1 = rectangel.getLeftUper().getY();
			int y2 = rectangel.getRightDown().getY();
			for (int i = y1; i < y2-1; i++) {
				//连续两个点的颜色跟背景色一样
				if((edge[i]&edge[i+1])==background){
					condition = true;
					//向右收缩
					rectangel.expansionRightSize(-1);
					break;
				}else{
					condition = false;
				}
			}
		}
	}
	
	/**
	 * 根据当前矩形的位置，让矩形自动向右适配(扩张)
	 * @param rectangel
	 */
	private void autoExpansionToRight(Rectangel rectangel){
		boolean condition = true;
		while(condition){
			int right = rectangel.getRightDown().getX();
			int[] edge = new int[imageHeight];
			for (int i = 0; i < imageHeight; i++) {
				edge[i] = this.imageInfo[i][right];
			}
			int y1 = rectangel.getLeftUper().getY();
			int y2 = rectangel.getRightDown().getY();
			for (int i = y1; i < y2-1; i++) {
				//连续两个点的颜色跟背景色不一样
				if((edge[i]&edge[i+1])!=background){
					condition = true;
					//向右扩张
					rectangel.expansionRightSize(1);
					break;
				}else{
					condition = false;
				}
			}
			
		}
	}

	/**
	 * 根据矩形当前的位置  收缩(右边 向左收缩)
	 * @param rectangel
	 */
	private void autoShrinkToLeft(Rectangel rectangel){
		boolean condition = true;
		while(condition){
			int right = rectangel.getRightDown().getX();
			int[] edge = new int[imageHeight];
			for (int i = 0; i < imageHeight; i++) {
				edge[i] = this.imageInfo[i][right];
			}
			int y1 = rectangel.getLeftUper().getY();
			int y2 = rectangel.getRightDown().getY();
			for (int i = y1; i < y2-1; i++) {
				//连续两个点的颜色跟背景色一样
				if((edge[i]&edge[i+1])==background){
					condition = true;
					//向左扩张
					rectangel.expansionLeftSize(-1);
					break;
				}else{
					condition = false;
				}
			}
		}
	}
	
	/**
	 * 根据当前矩形的位置，让矩形自动向左适配(扩张)
	 * @param rectangel
	 */
	private void autoExpansionToLeft(Rectangel rectangel){
		boolean condition = true;
		while(condition){
			int left = rectangel.getLeftUper().getX();
			int[] edge = new int[imageHeight];
			for (int i = 0; i < imageHeight; i++) {
				edge[i] = this.imageInfo[i][left];
			}
			int y1 = rectangel.getLeftUper().getY();
			int y2 = rectangel.getRightDown().getY();
			for (int i = y1; i < y2-1; i++) {
				//连续两个点的颜色跟背景色不一样
				if((edge[i]&edge[i+1])!=background){
					condition = true;
					//向左扩张
					rectangel.expansionLeftSize(1);
					break;
				}else{
					condition = false;
				}
			}
		}
	}

	/**
	 * 根据矩形当前的位置  收缩(下边 向上收缩)
	 * @param rectangel
	 */
	private void autoShrinkToUp(Rectangel rectangel){
		boolean condition = true;
		while(condition){
			int buttom = rectangel.getRightDown().getY();
			int[] edge = imageInfo[buttom];
			
			int x1 = rectangel.getLeftUper().getX();
			int x2 = rectangel.getRightDown().getX();
			for (int i = x1; i < x2-1; i++) {
				//连续两个点的颜色跟背景色一样
				if((edge[i]&edge[i+1])==background){
					condition = true;
					rectangel.expansionUpSize(-1);
					break;
				}else{
					condition = false;
				}
			}
		}
	}
	/**
	 * 根据当前矩形的位置，让矩形自动向上适配(扩张)
	 * @param rectangel
	 */
 	private void autoExpansionToUp(Rectangel rectangel){
		boolean condition = true;
		while(condition){
			int top = rectangel.getLeftUper().getY();
			int[] edge = imageInfo[top];
			
			int x1 = rectangel.getLeftUper().getX();
			int x2 = rectangel.getRightDown().getX();
			for (int i = x1; i < x2-1; i++) {
				//连续两个点的颜色跟背景色不一样
				if((edge[i]&edge[i+1])!=background){
					condition = true;
					rectangel.expansionUpSize(1);
					break;
				}else{
					condition = false;
				}
			}
		}
	}
	
 	/**
	 * 根据矩形当前的位置  收缩(上边 向下收缩)
	 * @param rectangel
	 */
 	private void autoShrinkToDown(Rectangel rectangel){
 		boolean condition = true;
		while(condition){
			int top = rectangel.getLeftUper().getY();
			int[] edge = imageInfo[top];
			
			int x1 = rectangel.getLeftUper().getX();
			int x2 = rectangel.getRightDown().getX();
			for (int i = x1; i < x2-1; i++) {
				//连续两个点的颜色跟背景色一样
				if((edge[i]&edge[i+1])==background){
					condition = true;
					rectangel.expansionDownSize(-1);
					break;
				}else{
					condition = false;
				}
			}
		}
 	}
 	
	/**
	 * 根据当前矩形的位置，让矩形自动向下适配(扩张)
	 * @param rectangel
	 */
	private void autoExpansionToDown(Rectangel rectangel){
		boolean condition = true;
		while(condition){
			int buttom = rectangel.getRightDown().getY();
			int[] edge = imageInfo[buttom];
			
			int x1 = rectangel.getLeftUper().getX();
			int x2 = rectangel.getRightDown().getX();
			for (int i = x1; i < x2-1; i++) {
				//连续两个点的颜色跟背景色不一样
				if((edge[i]&edge[i+1])!=background){
					condition = true;
					rectangel.expansionDownSize(1);
					break;
				}else{
					condition = false;
				}
			}
		}
	}
	
	/**
	 * 判断这个矩形的四条边是否压住字
	 * @param rectangel
	 * @return
	 */
	public boolean judgeEdgeIsExitWord(Rectangel rectangel){
		int[] topEdge = imageInfo[rectangel.getLeftUper().getY()];
		int x1 = rectangel.getLeftUper().getX();
		int x2 = rectangel.getRightDown().getX();
		for (int i = x1; i < x2-1; i++) {
			if((topEdge[i]&topEdge[i+1])!=background){
				return true;
			}
		}
		
		int[] buttomEdge = imageInfo[rectangel.getRightDown().getY()];
		for (int i = x1; i < x2-1; i++) {
			if((buttomEdge[i]&buttomEdge[i+1])!=background){
				return true;
			}
		}
		
		int[] rightEdge = new int[imageHeight];
		for (int i = 0; i < imageHeight; i++) {
			rightEdge[i] = this.imageInfo[i][x2];
		}
		int y1 = rectangel.getLeftUper().getY();
		int y2 = rectangel.getRightDown().getY();
		for (int i = y1; i < y2-1; i++) {
			if ((rightEdge[i]&rightEdge[i+1])!=background) {
				return true;
			}
		}
		
		int[] leftEdge = new int[imageHeight];
		for (int i = 0; i < imageHeight; i++) {
			leftEdge[i] = this.imageInfo[i][x1];
		}
		for (int i = y1; i < y2-1; i++) {
			if ((leftEdge[i]&leftEdge[i+1])!=background) {
				return true;
			}
		}
		
		return false;
	}
}
