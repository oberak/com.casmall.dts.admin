package com.casmall.dts.admin.print.edit;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.List;

import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;

import com.casmall.dts.admin.print.edit.policies.ElementEditLayoutPolicy;
import com.casmall.dts.admin.print.model.BaseElement;

/**
 * ElementPartBase 처리 최상위 EditPart
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
		// ElementChangeLayoutCommand 정책등록
//		installEditPolicy(EditPolicy.LAYOUT_ROLE, new ElementEditLayoutPolicy());
		installEditPolicy(EditPolicy.PRIMARY_DRAG_ROLE, new ElementEditLayoutPolicy());
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		// 변경이 있을 경우 다시 그리기
		if (evt.getPropertyName().equals(BaseElement.PROPERTY_LAYOUT))
			refreshVisuals();
		// 추가 시 
		if (evt.getPropertyName().equals(BaseElement.PROPERTY_ADD)) refreshChildren();
		// 삭제 시
		if (evt.getPropertyName().equals(BaseElement.PROPERTY_REMOVE)) refreshChildren(); 
	}

	@Override
	public void activate() {
		super.activate();
		// listener 등록 처리
		((BaseElement) getModel()).addPropertyChangeListener(this);
	}

	@Override
	public void deactivate() {
		((BaseElement) getModel()).removePropertyChangeListener(this);
		super.deactivate();
	}
}