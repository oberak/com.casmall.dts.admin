package com.casmall.dts.admin.print.edit;
import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.draw2d.IFigure;
import org.eclipse.gef.CompoundSnapToHelper;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.SnapToGuides;
import org.eclipse.gef.SnapToHelper;
import org.eclipse.gef.rulers.RulerProvider;

import com.casmall.dts.admin.print.figure.PaperElementFigure;

/**
 * BodyElement �� �����ϴ� EditPart
 *  
 * @author oberak
 */
public class PaperElementPart extends BaseElementPart {

	@Override
	protected IFigure createFigure() {
		IFigure figure = new PaperElementFigure();
		return figure;
	}

	@Override
	protected void refreshVisuals() {
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		super.propertyChange(evt);
	}
	
	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class key) {
		// Ruler ����
		if (key == SnapToHelper.class) {
			List<SnapToHelper> snaps = new ArrayList<SnapToHelper>();
						
			Boolean b = (Boolean)getViewer().getProperty(RulerProvider.PROPERTY_RULER_VISIBILITY);
			if (b != null && b)
				snaps.add(new SnapToGuides(this));
			
			// Grid
			// ������ ������ �޴� ��,	Grid �� ���� ������ �ٸ� �͵��� ó���� ���� ����
			b = (Boolean)getViewer().getProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED);
			if (b != null && b)
				snaps.add(new SnapToGeometry(this));
			
			b = (Boolean) getViewer().getProperty(SnapToGrid.PROPERTY_GRID_ENABLED);
			if (b != null && b)
				snaps.add(new SnapToGrid(this));
			
			
			if (snaps.size() == 0) return null;
			if (snaps.size() == 1) return snaps.get(0);
			
			SnapToHelper[] ss = snaps.toArray(new SnapToHelper[0]);
			
			return new CompoundSnapToHelper(ss);
		}
		
		return super.getAdapter(key);
	}
}