package com.test.image.until;

import com.test.image.entry.ImageUnit;

public class ImageSegmentAdapt extends ImageSegmentation {

	/**
	 * 一维最大熵分割算法
	 * 获取阀值
	 * @param pix	图片灰度流
	 * @param w		图片长度
	 * @param h		图片宽度
	 * @return		图片分割阀值
	 */
	public static int segment(ImageUnit imageUnit) {
		return segment(imageUnit.getImageInfos(), imageUnit.getImageWidth(), imageUnit.getImageHeight());
	}

	/**
	 * 大律法(otsu阀值分割)
	 * 获取阀值
	 * @param pix	图片灰度流
	 * @param w		图片长度
	 * @param h		图片宽度
	 * @return		图片分割阀值
	 */
	public static int otsuThresh(ImageUnit imageUnit) {
		return otsuThresh(imageUnit.getImageInfos(), imageUnit.getImageWidth(), imageUnit.getImageHeight());
	}

	/**
	 * 二维最大熵分割算法
	 * 获取阀值
	 * @param pix	图片灰度流
	 * @param w		图片长度
	 * @param h		图片宽度
	 * @return		图片分割阀值
	 */
	public static int segment2(ImageUnit imageUnit) {
		return segment2(imageUnit.getImageInfos(), imageUnit.getImageWidth(), imageUnit.getImageHeight());
	}

	/**
	 * 最佳阀值分割
	 * 获取阀值
	 * @param pix	图片灰度流
	 * @param w		图片长度
	 * @param h		图片宽度
	 * @return		图片分割阀值
	 */
	public static int bestThresh(ImageUnit imageUnit) {
		return bestThresh(imageUnit.getImageInfos(), imageUnit.getImageWidth(), imageUnit.getImageHeight());
	}

	/**
	 * 阀值分割算法
	 * @param pix
	 * @param iw
	 * @param ih
	 * @param th
	 * @return
	 */
	public static int[][] segmentByTh(int[] pix, int iw, int ih, int th) {
		int[][] im = new int[ih][iw];
		int t;
		for (int i = 0; i < ih; i++) {
			for (int j = 0; j < iw; j++) {
				t = pix[i*iw+j] & 0xff;
				if (t > th)
					im[i][j] = (255 << 24) | (255 << 16) | (255 << 8) | 255;// 背景色
				else
					im[i][j] = (255 << 24) | (0 << 16) | (0 << 8) | 0; // 前景色为
			}
		}
		return im;
	}
}
