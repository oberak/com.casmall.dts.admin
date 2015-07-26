package com.casmall.dts.admin.print.rulers;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.rulers.RulerProvider;

public class ElementRuler implements Serializable {

	public static final String PROPERTY_CHILDREN = "children changed";
	public static final String PROPERTY_UNIT = "units changed";

	static final long serialVersionUID = 1;

	protected PropertyChangeSupport listeners = new PropertyChangeSupport(this);
	private int unit;
	private boolean horizontal;
	private List<ElementGuide> guides = new ArrayList<ElementGuide>();

	public ElementRuler(boolean isHorizontal) {
		this(isHorizontal, RulerProvider.UNIT_PIXELS);
	}

	public ElementRuler(boolean isHorizontal, int unit) {
		horizontal = isHorizontal;
		setUnit(unit);
	}

	public void addGuide(ElementGuide guide) {
		if (!guides.contains(guide)) {
			guide.setHorizontal(!isHorizontal());
			guides.add(guide);
			listeners.firePropertyChange(PROPERTY_CHILDREN, null, guide);
		}
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		listeners.addPropertyChangeListener(listener);
	}

	// the returned list should not be modified
	public List<ElementGuide> getGuides() {
		return guides;
	}

	public int getUnit() {
		return unit;
	}

	public boolean isHidden() {
		return false;
	}

	public boolean isHorizontal() {
		return horizontal;
	}

	public void removeGuide(ElementGuide guide) {
		if (guides.remove(guide)) {
			listeners.firePropertyChange(PROPERTY_CHILDREN, null, guide);
		}
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		listeners.removePropertyChangeListener(listener);
	}

	public void setHidden(boolean isHidden) {
	}

	public void setUnit(int newUnit) {
		if (unit != newUnit) {
			int oldUnit = unit;
			unit = newUnit;
			listeners.firePropertyChange(PROPERTY_UNIT, oldUnit, newUnit);
		}
	}

}