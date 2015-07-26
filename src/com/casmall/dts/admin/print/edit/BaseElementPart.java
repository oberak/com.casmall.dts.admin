package com.casmall.dts.admin.print.edit;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import com.casmall.dts.admin.print.edit.policies.ElementEditLayoutPolicy;
import com.casmall.dts.admin.print.model.BaseElement;

/**
 * ElementPartBase ó�� �ֻ��� EditPart
 * 
 * @author oberak
 */
abstract class BaseElementPart extends AbstractGraphicalEditPart implements PropertyChangeListener {

	@Override
	protected List<BaseElement> getModelChildren() {
		return ((BaseElement) getModel()).getChildrenArray();
	}

	@Override
	protected void createEditPolicies() {
		// ElementChangeLayoutCommand ��å���
//		installEditPolicy(EditPolicy.LAYOUT_ROLE, new ElementEditLayoutPolicy());
		installEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE, new ElementEditLayoutPolicy());
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// ������ ���� ��� �ٽ� �׸���
		if (evt.getPropertyName().equals(BaseElement.PROPERTY_LAYOUT))
			refreshVisuals();
		// �߰� �� 
		if (evt.getPropertyName().equals(BaseElement.PROPERTY_ADD)) refreshChildren();
		// ���� ��
		if (evt.getPropertyName().equals(BaseElement.PROPERTY_REMOVE)) refreshChildren(); 
	}

	@Override
	public void activate() {
		super.activate();
		// listener ��� ó��
		((BaseElement) getModel()).addPropertyChangeListener(this);
	}

	@Override
	public void deactivate() {
		((BaseElement) getModel()).removePropertyChangeListener(this);
		super.deactivate();
	}
}