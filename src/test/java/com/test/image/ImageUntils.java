package com.test.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class ImageUntils {

	public static int isBlack(int colorInt) {
		Color color = new Color(colorInt);
		if (color.getRed() + color.getGreen() + color.getBlue() <= 100) {
			return 1;
		}
		return 0;
	}

	public static int isWhite(int colorInt) {
		Color color = new Color(colorInt);
		if (color.getRed() + color.getGreen() + color.getBlue() > 100) {
			return 1;
		}
		return 0;
	}

	public static BufferedImage removeBlank(BufferedImage img) throws Exception {
		int width = img.getWidth();
		int height = img.getHeight();
		int start = 0;
		int end = 0;
		Label1: for (int y = 0; y < height; ++y) {
			int count = 0;
			for (int x = 0; x < width; ++x) {
				if (isWhite(img.getRGB(x, y)) == 1) {
					count++;
				}
				if (count >= 1) {
					start = y;
					break Label1;
				}
			}
		}
		Label2: for (int y = height - 1; y >= 0; --y) {
			int count = 0;
			for (int x = 0; x < width; ++x) {
				if (isWhite(img.getRGB(x, y)) == 1) {
					count++;
				}
				if (count >= 1) {
					end = y;
					break Label2;
				}
			}
		}
		return img.getSubimage(0, start, width, end - start + 1);
	}

	public static List<BufferedImage> splitImage(BufferedImage img) throws Exception {
		List<BufferedImage> subImgs = new ArrayList<BufferedImage>();
		int width = img.getWidth();
		int height = img.getHeight();
		List<Integer> weightlist = new ArrayList<Integer>();
		for (int x = 0; x < width; ++x) {
			int count = 0;
			for (int y = 0; y < height; ++y) {
				if (isWhite(img.getRGB(x, y)) == 1) {
					count++;
				}
			}
			weightlist.add(count);
		}
		for (int i = 0; i < weightlist.size();) {
			int length = 0;
			while (weightlist.get(i++) > 1) {
				length++;
			}
			if (length > 12) {
				subImgs.add(removeBlank(img.getSubimage(i - length - 1, 0, length / 2, height)));
				subImgs.add(removeBlank(img.getSubimage(i - length / 2 - 1, 0, length / 2, height)));
			} else if (length > 3) {
				subImgs.add(removeBlank(img.getSubimage(i - length - 1, 0, length, height)));
			}
		}
		return subImgs;
	}

	/**
	 * 图像切分 如果不能平分 长度跟宽度最后一个最大
	 * 
	 * @param imageInfo[图片宽度][图片长度]
	 * @param wightArea
	 *            图片每块的长度
	 * @param heightArea
	 *            图片每块的宽度
	 * @return int[图片宽度][图片长度]
	 * @throws Exception
	 */
	public static ImageUnit[][] syncopateImage(int[][] imageInfo, int cellWidth, int cellHeight) throws Exception {

		int imageWidth = imageInfo[0].length;
		int imageHeight = imageInfo.length;

		int widthNum = imageWidth / cellWidth;
		int heightNum = imageHeight / cellHeight;

		if (imageWidth % cellWidth != 0)
			widthNum++;
		if (imageHeight % cellHeight != 0)
			heightNum++;

		ImageUnit[][] rtn = new ImageUnit[heightNum][widthNum];

		for (int i = 0; i < heightNum; i++) {
			for (int j = 0; j < widthNum; j++) {
				int beginW = j * cellWidth;
				int endW = (j + 1) * cellWidth;
				int beginH = i * cellHeight;
				int endH = (i + 1) * cellHeight;

				if (endW > imageWidth)
					endW = imageWidth;
				if (endH > imageHeight)
					endH = imageHeight;

				int index = 0;
				if (((endH - beginH) * (endW - beginW)) < 0) {
					System.out.println((endH - beginH) * (endW - beginW));
				}
				int[] imageArea = new int[(endH - beginH) * (endW - beginW)];
				for (int y = beginH; y < endH; y++) {
					for (int x = beginW; x < endW; x++) {
						imageArea[index] = imageInfo[y][x];
						index++;
					}
				}
				ImageUnit imgUnit = new ImageUnit(imageArea, endW - beginW, endH - beginH);
				rtn[i][j] = imgUnit;
			}
		}
		return rtn;
	}

	/**
	 * 图片合并
	 * 
	 * @param imageUnits
	 * @return
	 */
	public static int[] mergeImage(ImageUnit[][] imageUnits) {

		int H = 0, W = 0;
		for (ImageUnit[] imageUnitH : imageUnits) {
			if (W == 0) {
				for (ImageUnit imgUnit : imageUnitH) {
					W += imgUnit.getImageWidth();
				}
			}
			H += imageUnitH[0].getImageHeight();
		}

		int[] rtn = new int[H * W];
		int index = 0;
		for (ImageUnit[] imageUnitH : imageUnits) {
			for (int i = 0; i < imageUnitH[0].getImageHeight(); i++) {
				for (ImageUnit imgUnit : imageUnitH) {
					for (int cell : imgUnit.getImageInfo()[i]) {
						rtn[index] = cell;
						index++;
					}
				}
			}
		}
		return rtn;
	}

	/**
	 * 图片合并
	 * 
	 * @param imageUnits
	 * @return
	 */
	public static int[][] mergeImage2(ImageUnit[][] imageUnits) {

		int H = 0, W = 0;
		for (ImageUnit[] imageUnitH : imageUnits) {
			if (W == 0) {
				for (ImageUnit imgUnit : imageUnitH) {
					W += imgUnit.getImageWidth();
				}
			}
			H += imageUnitH[0].getImageHeight();
		}

		int[] rtnBefore = new int[H * W];
		int index = 0;
		for (ImageUnit[] imageUnitH : imageUnits) {
			for (int i = 0; i < imageUnitH[0].getImageHeight(); i++) {
				for (ImageUnit imgUnit : imageUnitH) {
					for (int cell : imgUnit.getImageInfo()[i]) {
						rtnBefore[index] = cell;
						index++;
					}
				}
			}
		}

		int[][] rtn = new int[H][W];
		for (int i = 0; i < H; i++) {
			for (int j = 0; j < W; j++) {
				rtn[i][j] = rtnBefore[i * W + j];
			}
		}
		return rtn;
	}

}
