package com.casmall.dts.admin.print.edit.policies;

import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.SnapToGuides;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.XYLayoutEditPolicy;
import org.eclipse.gef.requests.ChangeBoundsRequest;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.gef.rulers.RulerProvider;

import com.casmall.dts.admin.print.commands.ElementChangeLayoutCommand;
import com.casmall.dts.admin.print.commands.ElementCreateCommand;
import com.casmall.dts.admin.print.edit.PaperElementPart;
import com.casmall.dts.admin.print.model.BaseElement;
import com.casmall.dts.admin.print.rulers.ElementGuide;
import com.casmall.dts.admin.print.rulers.commands.ChangeGuideCommand;

/**
 * ElementChangeLayoutCommand command 를 관리하는 EditPolicy
 * 
 * ElementBase layout 변경 대응
 * 
 * @author oberak
 */
public class ElementEditLayoutPolicy extends XYLayoutEditPolicy {
	@Override
	protected Command createChangeConstraintCommand(EditPart child, Object constraint) {
		// Ruler 관련 수정
		// ElementChangeLayoutCommand command = new
		// ElementChangeLayoutCommand();
		//
		// command.setModel(child.getModel());
		// command.setConstraint((Rectangle) constraint);
		// return command;
		return null;
	}

	@Override
	protected Command getCreateCommand(CreateRequest request) {
		if (request.getType() == REQ_CREATE && getHost() instanceof PaperElementPart) {
			ElementCreateCommand command = new ElementCreateCommand();
			command.setParent(getHost().getModel());
			command.setElement(request.getNewObject());

			Rectangle constraint = (Rectangle) getConstraintFor(request);
			BaseElement be = (BaseElement)getHost().getModel();

			constraint.x = (constraint.x < 0) ? 0 : constraint.x;
			constraint.y = (constraint.y < 0) ? 0 : constraint.y;
			constraint.width = (constraint.width <= 0) ? be.getLayout().width : constraint.width;
			constraint.height = (constraint.height <= 0) ? be.getLayout().height : constraint.height;
			command.setLayout(constraint);

			return command;
		}		
		return null;
	}

	@Override
	protected Command createChangeConstraintCommand(ChangeBoundsRequest request, EditPart child, Object constraint) {
		// Ruler 관련 
		ElementChangeLayoutCommand command = new ElementChangeLayoutCommand();
		BaseElement element = (BaseElement) child.getModel();
		command.setModel(child.getModel());
		command.setConstraint((Rectangle) constraint);

		Command result = command;

		if ((request.getResizeDirection() & PositionConstants.NORTH_SOUTH) != 0) {
			Integer guidePos = (Integer) request.getExtendedData().get(SnapToGuides.KEY_HORIZONTAL_GUIDE);
			if (guidePos != null) {
				result = chainGuideAttachmentCommand(request, element, result, true);
			} else if (element.getHorizontalGuide() != null) {
				int alignment = element.getHorizontalGuide().getAlignment(element);
				int edgeBeingResized = 0;
				if ((request.getResizeDirection() & PositionConstants.NORTH) != 0)
					edgeBeingResized = -1;
				else
					edgeBeingResized = 1;
				if (alignment == edgeBeingResized)
					result = result.chain(new ChangeGuideCommand(element, true));
			}
		}

		if ((request.getResizeDirection() & PositionConstants.EAST_WEST) != 0) {
			Integer guidePos = (Integer) request.getExtendedData().get(SnapToGuides.KEY_VERTICAL_GUIDE);
			if (guidePos != null) {
				result = chainGuideAttachmentCommand(request, element, result, false);
			} else if (element.getVerticalGuide() != null) {
				int alignment = element.getVerticalGuide().getAlignment(element);
				int edgeBeingResized = 0;
				if ((request.getResizeDirection() & PositionConstants.WEST) != 0)
					edgeBeingResized = -1;
				else
					edgeBeingResized = 1;
				if (alignment == edgeBeingResized)
					result = result.chain(new ChangeGuideCommand(element, false));
			}
		}

		if (request.getType().equals(REQ_MOVE_CHILDREN) || request.getType().equals(REQ_ALIGN_CHILDREN)) {
			result = chainGuideAttachmentCommand(request, element, result, true);
			result = chainGuideAttachmentCommand(request, element, result, false);
			result = chainGuideDetachmentCommand(request, element, result, true);
			result = chainGuideDetachmentCommand(request, element, result, false);
		}

		return result;
	}

	protected Command chainGuideAttachmentCommand(Request request, BaseElement element, Command cmd, boolean horizontal) {
		// Ruler 관련
		Command result = cmd;

		// Attach to guide, if one is given
		Integer guidePos = (Integer) request.getExtendedData().get(
		        horizontal ? SnapToGuides.KEY_HORIZONTAL_GUIDE : SnapToGuides.KEY_VERTICAL_GUIDE);
		if (guidePos != null) {
			int alignment = ((Integer) request.getExtendedData().get(
			        horizontal ? SnapToGuides.KEY_HORIZONTAL_ANCHOR : SnapToGuides.KEY_VERTICAL_ANCHOR)).intValue();
			ChangeGuideCommand cgm = new ChangeGuideCommand(element, horizontal);
			cgm.setNewGuide(findGuideAt(guidePos.intValue(), horizontal), alignment);
			result = result.chain(cgm);
		}

		return result;
	}

	protected Command chainGuideDetachmentCommand(Request request, BaseElement element, Command cmd, boolean horizontal) {
		// Ruler 관련 
		Command result = cmd;

		// Detach from guide, if none is given
		Integer guidePos = (Integer) request.getExtendedData().get(
		        horizontal ? SnapToGuides.KEY_HORIZONTAL_GUIDE : SnapToGuides.KEY_VERTICAL_GUIDE);
		if (guidePos == null)
			result = result.chain(new ChangeGuideCommand(element, horizontal));

		return result;
	}

	protected ElementGuide findGuideAt(int pos, boolean horizontal) {
		RulerProvider provider = ((RulerProvider) getHost().getViewer().getProperty(
		        horizontal ? RulerProvider.PROPERTY_VERTICAL_RULER : RulerProvider.PROPERTY_HORIZONTAL_RULER));
		return (ElementGuide) provider.getGuideAt(pos);
	}
}