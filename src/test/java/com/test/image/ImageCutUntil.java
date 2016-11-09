package com.test.image;

public class ImageCutUntil {

	public void split(int[][] img) {

		int height = img.length;//img.height 原图高度
		int width = img[0].length;//img.width 原图宽度

		int[] xnum = new int[height];

		//扫描每行白点的个数；累加起来
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (img[i][j] == 255) // 原图数据存放在temp中temp是一位数组
					xnum[i]++; // 累加每行出现的白点数存放在xnum[]中
			}
		}

		//根据每行的累加值可以找出每行字符上下两层的边界
		int flag = 1;
		int xn = 0;// xn是第几行文字
		int[] x1 = new int[height], x2 = new int[height];// x1[xn]记录xn行字符的上边界
		
		//x2[xn]记录xn行字符的下边界
		for (int i = 0; i < height; i++) {
			if (flag == 0) {
				//flag 用来标记
				if (xnum[i] > 10) {
					//当出现白点数大于10个时，把相应行作为这行文字的上边缘
					x1[xn] = i; //x1[xn]记录xn行字符的上边界
					flag = 1;
				}
			}
			if (flag == 1) {
				if (xnum[i] == 0) {
					//扫描上边界后继续扫描当出现某行白点为0时，即为这行文字下边缘
					x2[xn] = i; // x2[xn]记录xn行字符的下边界
					flag = 0;
					xn++; //xn 用来记录第几行；
				}
			}
		}

		//填充每行每个字所占的空间，使其全为白色
		for (int m = 0; m < xn; m++) //xn为第xn行字符串
			// 行 覆盖文字区域 成为矩形；
			for (int i = x1[m]; i <= x2[m]; i++) {
				for (int j = 0; j < width; j++) {
					//处理后的数据放在temp中
					if (img[i][j] == 255) {
						for (int k = x1[m]; k <= x2[m]; k++) //x2记录m行字符的下边界
							img[i][j] = 255; //x1记录m行字符的下边界
					}
				}
			}

	}

}
