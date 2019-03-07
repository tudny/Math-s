package math.app.classes;

import javafx.scene.control.Button;

import java.util.ArrayList;

public class OwnArrayList<Object> extends ArrayList<Object> {

	private Integer width;
	private Integer height;

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public void mySet(Integer x, Integer y, Object element){
		set(width * y + x, element);
	}

	public Object myGet(Integer x, Integer y){
		return get(width * y + x);
	}

	public OwnArrayList(){
		super();
	}

	public OwnArrayList(Integer x, Integer y){
		super(x * y);
		width = x;
		height = y;
	}

	public int xIndexOf(Object element){
		return indexOf(element) % width;
	}

	public int yIndexOf(Object element){
		return indexOf(element) / width;
	}
}
