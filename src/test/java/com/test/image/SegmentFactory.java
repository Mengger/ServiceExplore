package com.test.image;

public class SegmentFactory {

	/**
	 * 一维最大熵分割算法
	 * @param imageInfo
	 * @param cellWidth
	 * @param cellHeight
	 * @return
	 * @throws Exception
	 */
	public int[][] segmentByseg1(int[][] imageInfo,int cellWidth,int cellHeight) throws Exception{
		return segment(imageInfo, cellWidth, cellHeight, "segmentByseg1");
	}
	
	/**
	 * 二维最大熵分割算法
	 * @param imageInfo
	 * @param cellWidth
	 * @param cellHeight
	 * @return
	 * @throws Exception
	 */
	public int[][] segmentByseg2(int[][] imageInfo,int cellWidth,int cellHeight) throws Exception{
		return segment(imageInfo, cellWidth, cellHeight, "segmentByseg2");
	}
	
	/**
	 * 大律法(otsu阀值分割)
	 * @param imageInfo
	 * @param cellWidth
	 * @param cellHeight
	 * @return
	 * @throws Exception
	 */
	public int[][] segmentByotsuThresh(int[][] imageInfo,int cellWidth,int cellHeight) throws Exception{
		return segment(imageInfo, cellWidth, cellHeight, "segmentByotsuThresh");
	}
	
	/**
	 * 最佳阀值分割
	 * @param imageInfo
	 * @param cellWidth
	 * @param cellHeight
	 * @return
	 * @throws Exception
	 */
	public int[][] segmentBybestThresh(int[][] imageInfo,int cellWidth,int cellHeight) throws Exception{
		return segment(imageInfo, cellWidth, cellHeight, "segmentBybestThresh");
	}
	
	
	/**
	 * 一维最大熵分割算法
	 * @param imageInfo
	 * @return
	 * @throws Exception
	 */
	public int[][] segmentByseg1(int[][] imageInfo) throws Exception{
		return segment(imageInfo, "segmentByseg1");
	}
	
	/**
	 * 二维最大熵分割算法
	 * @param imageInfo
	 * @return
	 * @throws Exception
	 */
	public int[][] segmentByseg2(int[][] imageInfo) throws Exception{
		return segment(imageInfo,  "segmentByseg2");
	}
	
	/**
	 * 大律法(otsu阀值分割)
	 * @param imageInfo
	 * @return
	 * @throws Exception
	 */
	public int[][] segmentByotsuThresh(int[][] imageInfo) throws Exception{
		return segment(imageInfo,  "segmentByotsuThresh");
	}
	
	/**
	 * 最佳阀值分割
	 * @param imageInfo
	 * @return
	 * @throws Exception
	 */
	public int[][] segmentBybestThresh(int[][] imageInfo) throws Exception{
		return segment(imageInfo, "segmentBybestThresh");
	}
	
	
	/**
	 * 一维最大熵分割算法
	 * @param imageInfo
	 * @return
	 * @throws Exception
	 */
	public int[][] segmentByseg1(int[] imageInfo,int h,int w) throws Exception{
		return segment(imageInfo, h, w, "segmentByseg1");
	}
	
	/**
	 * 二维最大熵分割算法
	 * @param imageInfo
	 * @return
	 * @throws Exception
	 */
	public int[][] segmentByseg2(int[] imageInfo,int h,int w) throws Exception{
		return segment(imageInfo, h, w,  "segmentByseg2");
	}
	
	/**
	 * 大律法(otsu阀值分割)
	 * @param imageInfo
	 * @return
	 * @throws Exception
	 */
	public int[][] segmentByotsuThresh(int[] imageInfo,int h,int w) throws Exception{
		return segment(imageInfo, h, w,  "segmentByotsuThresh");
	}
	
	/**
	 * 最佳阀值分割
	 * @param imageInfo
	 * @return
	 * @throws Exception
	 */
	public int[][] segmentBybestThresh(int[] imageInfo,int h,int w) throws Exception{
		return segment(imageInfo, h, w, "segmentBybestThresh");
	}
	
	
	private int[][] segment(int[][] imageInfo,String module) throws Exception{
		int h = imageInfo.length;
		int w = imageInfo[0].length;
		int[] needDealImage = new int[h*w];
		int index = 0;
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				needDealImage[index] = imageInfo[i][j];
				index++;
			}
		}
		return segment(needDealImage, h,w,module);
	}
	
	
	private int[][] segment(int[] imageInfo,int h,int w,String module) throws Exception{
		int segmentValue = 0;
		if("segmentBybestThresh".equals(module)){
			segmentValue = ImageSegmentAdapt.bestThresh(imageInfo,w,h);
		}else if("segmentByotsuThresh".equals(module)){
			segmentValue = ImageSegmentAdapt.otsuThresh(imageInfo,w,h);
		}else if("segmentByseg2".equals(module)){
			segmentValue = ImageSegmentAdapt.segment2(imageInfo,w,h);
		}else if("segmentByseg1".equals(module)){
			segmentValue = ImageSegmentAdapt.segment(imageInfo,w,h);
		}
		return ImageSegmentAdapt.segmentByTh(imageInfo, w, h, segmentValue);
	}
	
	
	private int[][] segment(int[][] imageInfo,int cellWidth,int cellHeight,String module) throws Exception{
		ImageUnit[][] syncopateImageAreas = ImageUntils.syncopateImage(imageInfo, cellWidth, cellHeight);
		for (ImageUnit[] imageWidths:syncopateImageAreas) {
			for(ImageUnit img:imageWidths){
				int segmentValue = 0;
				if("segmentBybestThresh".equals(module)){
					segmentValue = ImageSegmentAdapt.bestThresh(img);
				}else if("segmentByotsuThresh".equals(module)){
					segmentValue = ImageSegmentAdapt.otsuThresh(img);
				}else if("segmentByseg2".equals(module)){
					segmentValue = ImageSegmentAdapt.segment2(img);
				}else if("segmentByseg1".equals(module)){
					segmentValue = ImageSegmentAdapt.segment(img);
				}
				int[][] segmentResult = ImageSegmentAdapt.segmentByTh(img.getImageInfos(), img.getImageWidth(), img.getImageHeight(), segmentValue);
				img.updateImageUnit(segmentResult);
			}
		}
		return ImageUntils.mergeImage2(syncopateImageAreas);
	}
}
