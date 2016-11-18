package com.test.image.until.circleWords;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import com.test.image.SegmentFactory;
import com.test.image.entry.ImageUnit;
import com.test.image.entry.Poit;
import com.test.image.entry.Rectangel;

public class CircleWordsUntil extends CircleWordsCommon{

	private ImageUnit image;
	
	private int[][] imageInfo;
	
	private int imageWidth;
	
	private int imageHeight;
	
	private int background;
	
	private Rectangel[][] rectangels;
	
	public CircleWordsUntil(ImageUnit image, int background) {
		this.image=image;
		this.imageInfo=image.getImageInfo();
		this.imageWidth=image.getImageWidth();
		this.imageHeight=image.getImageHeight();
		this.background=background;
	}

	public static void main(String[] args) throws Exception {
		//File file = new File("/Users/jack/Desktop/111.png");
		File file = new File("C://Users//Administrator//Desktop//1.jpg");
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
		
		CircleWordsUntil ddd=new CircleWordsUntil(new ImageUnit(imgg), -1);
		ddd.circleFirst();
		
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
	
	public void circleFirst(){
		List<Rectangel> first = generatePoints();
		autoCircleWords(first);
		if(first!=null&&first.size()>2){
			int biggest = 0 , small = 0 ,biggestIndex=0 , smallIndex=0 ,index=0;
			for(Rectangel rectangel:first){
				int diagonalSquare = rectangel.getDiagonalSquare();
				if(biggest<diagonalSquare){
					biggest = diagonalSquare;
					biggestIndex = index;
				}
				if(small>diagonalSquare){
					small = diagonalSquare;
					smallIndex = index;
				}
				index++;
			}
			/*first.remove(smallIndex);
			first.remove(biggestIndex);*/
		}

		for(Rectangel rectangel:first){
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

	
	/**
	 * 自动扩张收缩 并且将空的矩形给自动删除  
	 * 并且把不是文字的矩形给删掉
	 * @param rectangelList
	 */
	public void autoCircleWords(List<Rectangel> rectangelList){
		int i=0;
		List<Rectangel> needRemove = new ArrayList<Rectangel>();
		for(Rectangel rectangel:rectangelList){
			System.out.println(++i);
			automaticCircleSingleWord(rectangel);
			Poit beginPoit = rectangel.getBeginPont();
			if((beginPoit.getX()-4)==rectangel.getLeftUper().getX()&&(beginPoit.getX()+4==rectangel.getRightDown().getX())){
				//删掉 空矩形
				needRemove.add(rectangel);
			}
			//删掉 不是文字的矩形   如果两条边的差>两条边的和／2
			if(Math.abs(rectangel.getHeight()-rectangel.getWidth())>(rectangel.getHeight()+rectangel.getWidth())/4){
				needRemove.add(rectangel);
			}
		}
		for(Rectangel rectangel:needRemove){
			rectangelList.remove(rectangel);
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
			int h = imageHeight/20;
			int w = imageWidth/20;
			for (int i = 0; i < 19; i++) {
				for (int j = 0; j < 19; j++) {
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
			for (int i = 0; i < y-1; i++) {
				for (int j = 0; j < x-1; j++) {
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
		int checkResult = judgeEdgeIsExitWord(rectangel);
		while (checkResult!=0) {
			if((checkResult&4)!=0){
				autoExpansionToRight(rectangel);
			}
			if((checkResult&1)!=0){
				autoExpansionToLeft(rectangel);
			}
			if((checkResult&8)!=0){
				autoExpansionToUp(rectangel);
			}
			if((checkResult&2)!=0){
				autoExpansionToDown(rectangel);
			}
			checkResult = judgeEdgeIsExitWord(rectangel);
		}
		Poit poit = rectangel.getBeginPont();
		int beginLeftX = poit.getX()-4;
		int beginLeftY = poit.getY()-4;
		int beginRightX = poit.getX()+4;
		int beginRightY = poit.getY()+4;
		if(beginLeftX==rectangel.getLeftUper().getX()&&(beginRightX==rectangel.getRightDown().getX())){
		}else{
			if(beginLeftX==rectangel.getLeftUper().getX()){
				autoShrinkToRight(rectangel);
			}
			if(beginLeftY==rectangel.getLeftUper().getY()){
				autoShrinkToDown(rectangel);
			}
			if(beginRightX==rectangel.getRightDown().getX()){
				autoShrinkToLeft(rectangel);
			}
			if(beginRightY==rectangel.getRightDown().getY()){
				autoShrinkToUp(rectangel);
			}
		}
	}
	

	/**
	 * 根据条件判断矩形是否扩张
	 * @param dege
	 * @return
	 */
	public boolean isExpansion(int[] dege){
		int diffCount = 0;
		for (int i = 0; i < dege.length-1; i++) {
			if((dege[i]&dege[i+1])!=background){
				return true;
			}
			if(dege[i]!=background){
				diffCount++;
			}
		}
		if(diffCount>2){
			return true;
		}
		return false;
	}
	
	
	/**
	 * 根据条件判断矩形是否 收缩
	 * @param dege
	 * @return
	 */
	public boolean isShrink(int[] dege){
		for(int pix:dege){
			if(pix!=background){
				return false;
			}
		}
		return true;
	}
	/**
	 * 根据矩形当前的位置  收缩(左边 向右收缩)
	 * @param rectangel
	 */
	private void autoShrinkToRight(Rectangel rectangel){
		boolean condition = true;
		out:while(condition){
			int left = rectangel.getLeftUper().getX();
			int[] edge = new int[imageHeight];
			int y1 = rectangel.getLeftUper().getY();
			int y2 = rectangel.getRightDown().getY();
			if(y1==y2){
				break;
			}
			for (int i = 0; i < imageHeight; i++) {
				edge[i] = this.imageInfo[i][left];
			}
			int[] dd = new int[y2-y1];
			int index=0;
			for (int i = y1; i < y2; i++) {
				dd[index]=edge[i];
				index++;
			}
			if(isShrink(dd)){
				condition = true;
				if(rectangel.getLeftUper().getX()==rectangel.getRightDown().getX()){
					break out;
				}
				//向右收缩
				rectangel.expansionLeftSize(-1);
			}else{
				condition = false;
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
			
			int[] dd = new int[y2-y1];
			int index = 0;
			for (int i = y1; i < y2; i++) {
				//连续两个点的颜色跟背景色不一样
				dd[index] = edge[i];
				index++;
				
			}
			if(isExpansion(dd)){
				if(rectangel.getRightDown().getX()>=imageWidth-1){
					condition = false;
					break;
				}
				condition = true;
				//向右扩张
				rectangel.expansionRightSize(1);
			}else{
				condition = false;
			}
			
		}
	}

	/**
	 * 根据矩形当前的位置  收缩(右边 向左收缩)
	 * @param rectangel
	 */
	private void autoShrinkToLeft(Rectangel rectangel){
		boolean condition = true;
		out:while(condition){
			int right = rectangel.getRightDown().getX();
			int[] edge = new int[imageHeight];
			for (int i = 0; i < imageHeight; i++) {
				edge[i] = this.imageInfo[i][right];
			}
			int y1 = rectangel.getLeftUper().getY();
			int y2 = rectangel.getRightDown().getY();
			if(y1==y2){
				break;
			}
			int[] dd = new int[y2-y1];
			int index=0;
			for (int i = y1; i < y2; i++) {
				dd[index]=edge[i];
				index++;
			}
			
			if(isShrink(dd)){
				condition = true;
				if(rectangel.getLeftUper().getX()==rectangel.getRightDown().getX()){
					break out;
				}
				//向左收缩
				rectangel.expansionRightSize(-1);
			}else{
				condition = false;
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
			int[] dd = new int[y2-y1];
			int index = 0;
			for (int i = y1; i < y2; i++) {
				//连续两个点的颜色跟背景色不一样
				dd[index] = edge[i];
				index++;
				
			}
			if(isExpansion(dd)){
				if(left<=0){
					condition = false;
					break;
				}
				condition = true;
				//向左扩张
				rectangel.expansionLeftSize(1);
			}else{
				condition = false;
			}
		}
	}

	/**
	 * 根据矩形当前的位置  收缩(下边 向上收缩)
	 * @param rectangel
	 */
	private void autoShrinkToUp(Rectangel rectangel){
		boolean condition = true;
		out:while(condition){
			int buttom = rectangel.getRightDown().getY();
			int[] edge = imageInfo[buttom];
			
			int x1 = rectangel.getLeftUper().getX();
			int x2 = rectangel.getRightDown().getX();
			if(x1==x2){
				break;
			}
			int[] dd = new int[x2-x1];
			int index=0;
			for (int i = x1; i < x2; i++) {
				dd[index]=edge[i];
				index++;
			}
			if(isShrink(dd)){
				condition = true;
				if(rectangel.getLeftUper().getY()==rectangel.getRightDown().getY()){
					break out;
				}
				rectangel.expansionDownSize(-1);
			}else{
				condition = false;
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
			
			int[] dd = new int[x2-x1];
			int index=0;
			for (int i = x1; i < x2; i++) {
				dd[index]=edge[i];
				index++;
			}
			//连续两个点的颜色跟背景色不一样
			if(isExpansion(dd)){
				if(rectangel.getLeftUper().getY()<=0){
					condition = false;
					break;
				}
				condition = true;
				rectangel.expansionUpSize(1);
			}else{
				condition = false;
			}
		}
	}
	
 	/**
	 * 根据矩形当前的位置  收缩(上边 向下收缩)
	 * @param rectangel
	 */
 	private void autoShrinkToDown(Rectangel rectangel){
 		boolean condition = true;
		out:while(condition){
			int top = rectangel.getLeftUper().getY();
			int[] edge = imageInfo[top];
			
			int x1 = rectangel.getLeftUper().getX();
			int x2 = rectangel.getRightDown().getX();
			if(x1==x2){
				break out;
			}
			int[] dd = new int[x2-x1];
			int index=0;
			for (int i = x1; i < x2; i++) {
				dd[index]=edge[i];
				index++;
			}
			if(isShrink(dd)){
				condition = true;
				if(rectangel.getLeftUper().getY()==rectangel.getRightDown().getY()){
					break out;
				}
				rectangel.expansionUpSize(-1);
			}else{
				condition = false;
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
			
			int[] dd = new int[x2-x1];
			int index=0;
			for (int i = x1; i < x2; i++) {
				dd[index]=edge[i];
				index++;
			}
			
			if(isExpansion(dd)){
				if(rectangel.getRightDown().getY()>=imageHeight-1){
					condition = false;
					break;
				}
				condition = true;
				rectangel.expansionDownSize(1);
			}else{
				condition = false;
			}
		}
	}
	
	/**
	 * 判断这个矩形的四条边 哪一天压住了字
	 * 用十六进制表示  
	 * 1000 --> 8  代表顶边 压住了字
	 * 0001 --> 1  代表左边 压住了字
	 * 0010 --> 2  代表底边 压住了字
	 * 0100 --> 4  代表右边 压住了字
	 * @param rectangel
	 * @return
	 */
	public int judgeEdgeIsExitWord(Rectangel rectangel){
		int[] topEdge = imageInfo[rectangel.getLeftUper().getY()];
		int x1 = rectangel.getLeftUper().getX();
		int x2 = rectangel.getRightDown().getX();
		int y1 = rectangel.getLeftUper().getY();
		int y2 = rectangel.getRightDown().getY();
		
		int checkResult = 0;
		if(x2>=imageWidth-1){
			//说明已经超出了 该矩形的右边 已经超出了照片的长度  或者正好压住照片的右边  所以此次右边不能继续再扩张了
		}else{
			int[] rightEdge = new int[imageHeight];
			for (int i = 0; i < imageHeight; i++) {
				rightEdge[i] = this.imageInfo[i][x2];
			}
			for (int i = y1; i < y2-1; i++) {
				if ((rightEdge[i]&rightEdge[i+1])!=background) {
					checkResult = checkResult + 4;
					break;
				}
			}
		}
		
		if(y2>=imageHeight-1){
			//说明已经超出了 该矩形的下边 已经超出了照片的长度  或者正好压住照片的下边  所以此次下边不能继续再扩张了
		}else{
			int[] buttomEdge = imageInfo[rectangel.getRightDown().getY()];
			for (int i = x1; i < x2-1; i++) {
				if((buttomEdge[i]&buttomEdge[i+1])!=background){
					checkResult = checkResult + 2;
					break;
				}
			}
		}
		
		if(x1<=0){
			//说明已经超出了 该矩形的左边 已经超出了照片的长度  或者正好压住照片的左边  所以此次左边不能继续再扩张了
		}else{
			int[] leftEdge = new int[imageHeight];
			for (int i = 0; i < imageHeight; i++) {
				leftEdge[i] = this.imageInfo[i][x1];
			}
			for (int i = y1; i < y2-1; i++) {
				if ((leftEdge[i]&leftEdge[i+1])!=background) {
					checkResult = checkResult + 1;
					break;
				}
			}
		}
		if(y1<=0){
			//说明已经超出了 该矩形的上边 已经超出了照片的长度  或者正好压住照片的上边  所以此次上边不能继续再扩张了
		}else{
			for (int i = x1; i < x2-1; i++) {
				if((topEdge[i]&topEdge[i+1])!=background){
					checkResult = checkResult + 8;
					break;
				}
			}
		}
		return checkResult;
	}
}
