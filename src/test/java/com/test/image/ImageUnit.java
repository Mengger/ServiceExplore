package com.test.image;

/**
 * 图片遍历按照行遍历
 * @author Administrator
 *
 */
public class ImageUnit {
	
	//图片像素信息
	private int[][] imageInfo;
	
	//图片长度
	private int imageWidth = 0;
	
	//图片宽度
	private int imageHeight = 0;
	
	public int getImageWidth(){
		return imageWidth;
	}
	
	public int getImageHeight(){
		return imageHeight;
	}
	
	public int[][] getImageInfo(){
		return this.imageInfo;
	}
	
	/**
	 * 
	 * @param imageInfo[图片宽度][图片长度]
	 */
	public ImageUnit(int[][] imageInfo){
		this.imageInfo = imageInfo;
		this.imageHeight = imageInfo.length;
		this.imageWidth = imageInfo[0].length;
	}
	
	/**
	 * 
	 * @param imageInfo[图片宽度][图片长度]
	 */
	public void updateImageUnit(int[][] imageInfo){
		this.imageInfo = imageInfo;
		this.imageHeight = imageInfo.length;
		this.imageWidth = imageInfo[0].length;
	}
	
	/**
	 * 
	 * @param img		图片块的所有像素的信息
	 * @param width		图片块的长度
	 * @param height	图片块的宽度
	 * @throws Exception
	 */
	public ImageUnit(int[] img,int width,int height) throws Exception{
		if(img.length!=width*height){
			throw new Exception("img info error, please check img info and make sure img's length equals width*height");
		}
		imageInfo = new int[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				imageInfo[i][j] = img[i*width+j];
			}
		}
		this.imageHeight = height;
		this.imageWidth = width;
	}
	
	/**
	 * 获取该方块的所有信息
	 * @return
	 */
	public int[] getImageInfos(){
		int[] rtn = new int[imageHeight*imageWidth];
		int index = 0;
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				rtn[index] = imageInfo[i][j];
				index++;
			}
		}
		return rtn;
	}
	
}
