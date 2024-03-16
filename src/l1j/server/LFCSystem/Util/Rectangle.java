package l1j.server.LFCSystem.Util;

import l1j.server.server.utils.CommonUtil;

public class Rectangle {
	public static final int LEFTPOS 	= 0;
	public static final int TOPPOS 		= 1;
	public static final int RIGHTPOS 	= 2;
	public static final int BOTTOMPOS	= 3;

	private int[] _rect;
	
	public Rectangle(){
		init(0, 0, 0, 0);
	}
	public Rectangle(int left, int top, int right, int bottom){
		init(left, top, right, bottom);
	}
	public Rectangle(Rectangle rt){
		init(rt.getLeft(), rt.getTop(), rt.getRight(), rt.getBottom());
	}
	private void init(int left, int top, int right, int bottom){
		_rect = new int[]{left, top, right, bottom};		
	}
	public void setLeft(int i){
		_rect[LEFTPOS] = i;
	}
	public void setTop(int i){
		_rect[TOPPOS] = i;
	}
	public void setRight(int i){
		_rect[RIGHTPOS] = i;
	}
	public void setBottom(int i){
		_rect[BOTTOMPOS] = i;
	}
	public int getLeft(){
		return _rect[LEFTPOS];
	}
	public int getTop(){
		return _rect[TOPPOS];
	}
	public int getRight(){
		return _rect[RIGHTPOS];
	}
	public int getBottom(){
		return _rect[BOTTOMPOS];
	}
	public void setWidth(int i){
		_rect[RIGHTPOS] = _rect[LEFTPOS] + i;
	}
	public void setHeight(int i){
		_rect[BOTTOMPOS] = _rect[TOPPOS] + i;
	}
	public int getWidth(){
		return _rect[RIGHTPOS] - _rect[LEFTPOS];
	}
	public int getHeight(){
		return _rect[BOTTOMPOS] - _rect[TOPPOS];
	}
	public int getRandomX(){
		return CommonUtil.random(_rect[RIGHTPOS] - _rect[LEFTPOS]) + 1 + _rect[LEFTPOS];
	}
	
	public int getRandomY(){
		return CommonUtil.random(_rect[BOTTOMPOS] - _rect[TOPPOS]) + 1 + _rect[TOPPOS];
	}
	
	public void reduce(){
		_rect[LEFTPOS]++;
		_rect[TOPPOS]++;
		_rect[RIGHTPOS]--;
		_rect[BOTTOMPOS]--;
	}
}

