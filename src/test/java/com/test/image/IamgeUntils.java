package com.test.image;

public class IamgeUntils {
	
	
	/**
	 * 图像切分
	 * 如果不能平分  长度跟宽度最后一个最大
	 * @param imageInfo[图片宽度][图片长度]
	 * @param wightArea		图片长 分成几块
	 * @param heightArea	图片宽分成几块
	 * @return int[图片宽度][图片长度]
	 */
	public static ImageUnit[][] syncopateImage(int[][] imageInfo,int wightArea,int heightArea){
		ImageUnit[][] rtn = new ImageUnit[wightArea][heightArea];
		
		int imageWidth = imageInfo[0].length;
		int imageHeight = imageInfo.length;
		
		int eachWidth = imageWidth/wightArea;
		int eachHeight = imageHeight/heightArea;
		
		for (int i = 0; i < heightArea; i++) {
			for (int j = 0; j < wightArea; j++) {


				//rtn[j][i] = ;
			}
		}
		
		for (int i = 0; i < imageHeight; i++) {
			for (int j = 0; j < imageWidth; j++) {
				
				int a = imageInfo[i][j];
			}
		}
		return null;
	}
	
	
	/**
	 * 图片合并
	 * @param imageUnits
	 * @return
	 */
	public static int[][] mergeImage(ImageUnit[][] imageUnits){
		return null;
	}

}
