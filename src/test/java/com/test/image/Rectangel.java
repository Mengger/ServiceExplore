package com.test.image;

public class Rectangel {
	//左上角
	private Poit leftUper;
	
	//右下角
	private Poit rightDown;
	
	
	public Poit getLeftUper() {
		return leftUper;
	}

	public void setLeftUper(Poit leftUper) {
		this.leftUper = leftUper;
	}

	public Poit getRightDown() {
		return rightDown;
	}

	public void setRightDown(Poit rightDown) {
		this.rightDown = rightDown;
	}

	/**
	 * 向右扩大 size
	 * @param size
	 */
	public void expansionRightSize(int size){
		this.rightDown.x=this.rightDown.x+size;
	}
	
	/**
	 * 向左扩大 size
	 * @param size
	 */
	public void expansionLeftSize(int size){
		this.leftUper.x=this.leftUper.x-size;
	}
	
	/**
	 * 向上扩大 size
	 * @param size
	 */
	public void expansionUpSize(int size){
		this.leftUper.y=this.leftUper.y-size;
	}
	
	/**
	 * 向下扩大 size
	 * @param size
	 */
	public void expansionDownSize(int size){
		this.rightDown.y=this.rightDown.y+size;
	}
	
	

	public class Poit{
		private int x;
		private int y;
		public int getX() {
			return x;
		}
		public void setX(int x) {
			this.x = x;
		}
		public int getY() {
			return y;
		}
		public void setY(int y) {
			this.y = y;
		}
		
	}

}
