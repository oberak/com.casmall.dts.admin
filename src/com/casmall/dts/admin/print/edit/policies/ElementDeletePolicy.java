package com.casmall.dts.admin.print.edit.policies;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.editpolicies.ComponentEditPolicy;
import org.eclipse.gef.requests.GroupRequest;

import com.casmall.dts.admin.print.commands.ElementDeleteCommand;

public class ElementDeletePolicy extends ComponentEditPolicy {

	@Override
	protected Command createDeleteCommand(GroupRequest deleteRequest) {
		ElementDeleteCommand command = new ElementDeleteCommand();
		command.setModel(getHost().getModel());
		command.setParentModel(getHost().getParent().getModel());
		return command;
	}
}