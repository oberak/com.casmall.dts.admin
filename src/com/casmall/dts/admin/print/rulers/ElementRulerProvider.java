package com.casmall.dts.admin.print.rulers;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.gef.commands.Command;
import org.eclipse.gef.rulers.RulerChangeListener;
import org.eclipse.gef.rulers.RulerProvider;

import com.casmall.dts.admin.print.rulers.commands.CreateGuideCommand;
import com.casmall.dts.admin.print.rulers.commands.DeleteGuideCommand;
import com.casmall.dts.admin.print.rulers.commands.MoveGuideCommand;

public class ElementRulerProvider extends RulerProvider {

	private ElementRuler ruler;
	private PropertyChangeListener rulerListener = new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals(ElementRuler.PROPERTY_CHILDREN)) {
				ElementGuide guide = (ElementGuide) evt.getNewValue();
				if (getGuides().contains(guide)) {
					guide.addPropertyChangeListener(guideListener);
				} else {
					guide.removePropertyChangeListener(guideListener);
				}
				for (int i = 0; i < listeners.size(); i++) {
					((RulerChangeListener) listeners.get(i)).notifyGuideReparented(guide);
				}
			} else {
				for (int i = 0; i < listeners.size(); i++) {
					((RulerChangeListener) listeners.get(i)).notifyUnitsChanged(ruler.getUnit());
				}
			}
		}
	};
	private PropertyChangeListener guideListener = new PropertyChangeListener() {
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals(ElementGuide.PROPERTY_CHILDREN)) {
				for (int i = 0; i < listeners.size(); i++) {
					((RulerChangeListener) listeners.get(i)).notifyPartAttachmentChanged(evt.getNewValue(), evt.getSource());
				}
			} else {
				for (int i = 0; i < listeners.size(); i++) {
					((RulerChangeListener) listeners.get(i)).notifyGuideMoved(evt.getSource());
				}
			}
		}
	};

	public ElementRulerProvider(ElementRuler ruler) {
		this.ruler = ruler;
		// Unit : 표시단위 pixls
		ruler.setUnit(RulerProvider.UNIT_PIXELS); // RulerProvider.UNIT_CENTIMETERS
		this.ruler.addPropertyChangeListener(rulerListener);
		List<?> guides = getGuides();
		for (int i = 0; i < guides.size(); i++) {
			((ElementGuide) guides.get(i)).addPropertyChangeListener(guideListener);
		}
	}

	public List<?> getAttachedModelObjects(Object guide) {
		return new ArrayList<Object>(((ElementGuide) guide).getParts());
	}

	public Command getCreateGuideCommand(int position) {
		return new CreateGuideCommand(ruler, position);
	}

	public Command getDeleteGuideCommand(Object guide) {
		return new DeleteGuideCommand((ElementGuide) guide, ruler);
	}

	public Command getMoveGuideCommand(Object guide, int pDelta) {
		return new MoveGuideCommand((ElementGuide) guide, pDelta);
	}

	public int[] getGuidePositions() {
		List<?> guides = getGuides();
		int[] result = new int[guides.size()];
		for (int i = 0; i < guides.size(); i++) {
			result[i] = ((ElementGuide) guides.get(i)).getPosition();
		}
		return result;
	}

	public Object getRuler() {
		return ruler;
	}

	public int getUnit() {
		return ruler.getUnit();
	}

	public void setUnit(int newUnit) {
		ruler.setUnit(newUnit);
	}

	public int getGuidePosition(Object guide) {
		return ((ElementGuide) guide).getPosition();
	}

	public List<?> getGuides() {
		return ruler.getGuides();
	}

}