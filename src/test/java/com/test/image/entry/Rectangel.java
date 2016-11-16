package com.test.image.entry;

public class Rectangel {
	//左上角
	private Poit leftUper = new Poit();
	
	//右下角
	private Poit rightDown = new Poit();
	
	//中间点
	private Poit beginPont = new Poit();
	//矩形长度
	private int width;
	//宽度
	private int height;
	
	public Rectangel(){
	}
	
	public Rectangel(int leftUperX,int leftUperY,int rightDownX,int rightDownY){
		this.leftUper.x=leftUperX;
		this.leftUper.y=leftUperY;
		this.rightDown.x=rightDownX;
		this.rightDown.y=rightDownY;
		this.height=rightDownY-leftUperY;
		this.width=rightDownX-leftUperX;
	}
	
	public int getDiagonalSquare(){
		return width*width+height*height;
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public void setBeginPont(Poit beginPont) {
		this.beginPont = beginPont;
	}

	@Override
	public boolean equals(Object obj){
		if(obj instanceof Rectangel){
			Rectangel rectangel = (Rectangel)obj;
			if((rectangel.getRightDown().getX()==this.rightDown.x)&&(rectangel.getRightDown().getY()==this.rightDown.y)
				&&(rectangel.getLeftUper().getX()==this.leftUper.x)&&(rectangel.getLeftUper().getY()==this.leftUper.y)
				&&(rectangel.getBeginPont().getX()==this.beginPont.x)&&(rectangel.getBeginPont().getY()==this.beginPont.y)){
				return true;
			}
			return false;
		}
		return false;
	}
	
	public Poit getBeginPont() {
		return beginPont;
	}

	public void setBeginPont(int x,int y) {
		this.beginPont.x = x;
		this.beginPont.y = y;
	}

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
	
	/**
	 *向左移动 
	 * @param size
	 */
	public void moveLeft(int size){
		this.rightDown.x=this.rightDown.x-size;
		this.leftUper.x=this.leftUper.x-size;
	}
	
	/**
	 * 向右移动
	 * @param size
	 */
	public void moveRight(int size){
		this.rightDown.x=this.rightDown.x+size;
		this.leftUper.x=this.leftUper.x+size;
	}
	
	/**
	 * 向上移动
	 * @param size
	 */
	public void moveUp(int size){
		this.rightDown.y=this.rightDown.y+size;
		this.leftUper.y=this.leftUper.y+size;
	}
	
	/**
	 * 向下移动
	 * @param size
	 */
	public void moveDown(int size){
		this.rightDown.y=this.rightDown.y-size;
		this.leftUper.y=this.leftUper.y-size;
	}
	

}
