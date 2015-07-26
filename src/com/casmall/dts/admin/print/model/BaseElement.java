package com.casmall.dts.admin.print.model;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.ui.views.properties.IPropertySource;

import com.casmall.dts.admin.print.model.properties.ElementPropertySource;
import com.casmall.dts.admin.print.rulers.ElementGuide;

/**
 * 최상위 모델
 * @author oberak
 */
public abstract class BaseElement implements IAdaptable{
	/**
	 * 좌표의 위치와 크기를 저장하는 변수
	 */
	private Rectangle layout;
	
	private List<BaseElement> children;
	
	private BaseElement parent;
	
	/** 항목설명:Outline에 표기 */
	private String name = "";
	
	/** ID */
	private String id = "";
	
	/** property sheet */
	private IPropertySource propertySource = null;

	/** 위치, 크기: 단위 mm */
	private double posX;
	private double posY;
	private double width;
	private double height;
	
	/**
	 * 레이아웃의 변경시 사용하는 프로퍼티
	 */
	public static final String PROPERTY_LAYOUT = "PROPERTY_LAYOUT";
	/** Element 추가 */
	public static final String PROPERTY_ADD = "PROPERTY_ADD";
	/** Element 삭제 */
	public static final String PROPERTY_REMOVE = "PROPERTY_REMOVE";
	/** DESC 변경 */
	public static final String PROPERTY_NAME = "PROPERTY_NAME";

	/** 변경 통지 처리 listener */
	private PropertyChangeSupport listeners;
	
	/** guide */
	private ElementGuide verticalGuide, horizontalGuide;
	
	public BaseElement() {
		layout = new Rectangle(0, 0, 200, 30);
		children = new ArrayList<BaseElement>();
		
		listeners = new PropertyChangeSupport(this);
		getListeners().firePropertyChange(PROPERTY_ADD, null, parent);
	}

	public Rectangle getLayout() {
		return layout;
	}

	public void setLayout(Rectangle newLayout) {
		Rectangle oldLayout = layout;
		layout = newLayout;

		posX = (layout.x)/10.0;
		posY = (layout.y)/10.0;

		width = layout.width/10.0;
		height = layout.height/10.0;
		
		getListeners().firePropertyChange(PROPERTY_LAYOUT, oldLayout, newLayout);
	}
	
	public boolean addChlid(BaseElement child) {
		boolean b = children.add(child);
		if (b) {
			child.setParent(this);
			getListeners().firePropertyChange(PROPERTY_ADD, null, child);
		}
		return b;
	}
	
	public boolean removeChild(BaseElement child) {
		boolean b = children.remove(child);
		if (b)
			getListeners().firePropertyChange(PROPERTY_REMOVE, child, null);
		return b;
	}

	public List<BaseElement> getChildrenArray() {
		return children;
	}

	public BaseElement getParent() {
		return parent;
	}

	public void setParent(BaseElement parent) {
		this.parent = parent;
	}
	
	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
		if (adapter == IPropertySource.class) {
			if (propertySource == null)
				propertySource = new ElementPropertySource(this);
			return propertySource;
		}
		return null;
	}
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
		listeners.addPropertyChangeListener(listener);
	}
	
	public void removePropertyChangeListener(PropertyChangeListener listener) {
		listeners.removePropertyChangeListener(listener);
	}
	public PropertyChangeSupport getListeners() {
		return listeners;
	}
	public boolean contains(BaseElement child) {
		return children.contains(child);
	}
	
	public ElementGuide getVerticalGuide() {
		return verticalGuide;
	}

	public void setVerticalGuide(ElementGuide verticalGuide) {
		this.verticalGuide = verticalGuide;
	}

	public ElementGuide getHorizontalGuide() {
		return horizontalGuide;
	}

	public void setHorizontalGuide(ElementGuide horizontalGuide) {
		this.horizontalGuide = horizontalGuide;
	}

	public String getName() {
    	return name;
    }

	public void setName(String name) {
		String oldName = this.name;
    	this.name = name;
    	getListeners().firePropertyChange(PROPERTY_NAME, oldName, name);
    }

	public String getId() {
    	return id;
    }

	public void setId(String id) {
    	this.id = id;
    }

	@Override
    public Object clone() throws CloneNotSupportedException {
	    return super.clone();
    }

	public double getPosX() {
    	return posX;
    }

	public void setPosX(double posX) {
		this.posX = posX;

		Rectangle newLayout = new Rectangle((int)Math.round((posX)*10), layout.y, layout.width, layout.height);
		setLayout(newLayout);
    }

	public double getPosY() {
    	return posY;
    }

	public void setPosY(double posY) {
    	this.posY = posY;
    	
		Rectangle newLayout = new Rectangle(layout.x, (int)Math.round((posY)*10), layout.width, layout.height);
		setLayout(newLayout);

    }

	public double getWidth() {
    	return width;
    }

	public void setWidth(double width) {
    	this.width = width;
		Rectangle newLayout = new Rectangle(layout.x, layout.y, (int)Math.round(width*10), layout.height);
		setLayout(newLayout);
    }

	public double getHeight() {
    	return height;
    }

	public void setHeight(double height) {
    	this.height = height;
    	
		Rectangle newLayout = new Rectangle(layout.x, layout.y, layout.width, (int)Math.round(height*10));
		setLayout(newLayout);
    }
}