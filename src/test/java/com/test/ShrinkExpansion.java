package com.test;

public class ShrinkExpansion {

	public ShrinkExpansion() {
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * 最近邻插值法
	 * @param original	原图片
	 * @param newW		新图片长度
	 * @param newH		新图片宽度
	 */
	public int[][] ResizeNear01(int[][] original, int newW , int newH){
		
		int[][] rtn = new int[newH][newW];

	    int oldW = original[0].length;
	    int oldH = original.length;
	    float fw = (float)oldW / newW;
	    float fh = (float)oldH / newH;

	    int x0=0, y0=0;
	    for(int y=0; y<newH; y++){
	        y0 = (int)(y * fh);
	        for(int x=0; x<newW; x++){
	            x0 = (int)(x * fw);
	            rtn[y][x] = original[y0][x0];
	        }
	    }
	    return rtn;
	}

}
