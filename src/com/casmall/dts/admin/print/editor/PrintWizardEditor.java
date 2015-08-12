package com.casmall.dts.admin.print.editor;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.parts.ScrollableThumbnail;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.CreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.rulers.RulerProvider;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.AlignmentAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.MatchHeightAction;
import org.eclipse.gef.ui.actions.MatchWidthAction;
import org.eclipse.gef.ui.actions.ToggleGridAction;
import org.eclipse.gef.ui.actions.ToggleRulerVisibilityAction;
import org.eclipse.gef.ui.actions.ToggleSnapToGeometryAction;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.parts.ContentOutlinePage;
import org.eclipse.gef.ui.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.gef.ui.rulers.RulerComposite;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;

import com.casmall.dts.admin.print.actions.CopyElementAction;
import com.casmall.dts.admin.print.actions.CutElementAction;
import com.casmall.dts.admin.print.actions.PasteElementAction;
import com.casmall.dts.admin.print.commands.ArrowKeyHandler;
import com.casmall.dts.admin.print.commands.PrintWizardContextMenuProvider;
import com.casmall.dts.admin.print.common.PrintConstants;
import com.casmall.dts.admin.print.edit.AnnotatedEditPartFactory;
import com.casmall.dts.admin.print.edit.tree.AnnotatedTreeEditPartFactory;
import com.casmall.dts.admin.print.model.BoxElement;
import com.casmall.dts.admin.print.model.DataBoxElement;
import com.casmall.dts.admin.print.model.ElementCreationFactory;
import com.casmall.dts.admin.print.model.FixBoxElement;
import com.casmall.dts.admin.print.model.LineElement;
import com.casmall.dts.admin.print.model.PaperElement;
import com.casmall.dts.admin.print.model.QRCodeElement;
import com.casmall.dts.admin.print.model.TextBoxElement;
import com.casmall.dts.admin.print.rulers.ElementRuler;
import com.casmall.dts.admin.print.rulers.ElementRulerProvider;

public class PrintWizardEditor extends GraphicalEditorWithFlyoutPalette {
	public static final String ID = PrintWizardEditor.class.getName();

	private PaperElement model;
	private KeyHandler keyHandler;
	/** Ruler */
	private RulerComposite rulerComp;
	private OutlinePage op;
	
	public PrintWizardEditor() {
		setEditDomain(new DefaultEditDomain(this));
	}

	@Override
	protected void createGraphicalViewer(Composite parent) {
		// Ruler 관련
		rulerComp = new RulerComposite(parent, SWT.NONE);
		super.createGraphicalViewer(rulerComp);
		rulerComp.setGraphicalViewer((ScrollingGraphicalViewer)getGraphicalViewer());
		op = new OutlinePage();
	}
	
	@Override
	protected Control getGraphicalControl() {
		// Ruler 관련
		return rulerComp;
	}
	
	@Override
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		GraphicalViewer viewer = getGraphicalViewer();
		// 루트 에디트파트를 정의
		viewer.setEditPartFactory(new AnnotatedEditPartFactory());
//		viewer.setEditPartFactory(new ElementEditPartFactory());
		// 줌 액션을 등록
		configureZoomLevel();
		// 키 핸들러를 설정
		configureKeyHandler();
		// 컨텍스트 메뉴를 등록
		viewer.setContextMenu(new PrintWizardContextMenuProvider(
				viewer, getActionRegistry()));

		// 기타 액션들을 정의 (룰러, 지오메트리, 그리드) -----
		// Ruler
		configureRuler();
		// Grid
		configureGeometry();
		configureGrid();
	}

	@Override
	protected void initializeGraphicalViewer() {
		GraphicalViewer viewer = getGraphicalViewer();

		model = new PaperElement();

		PrintWizardEditorInput ei = (PrintWizardEditorInput)this.getEditorInput();
		model = ei.getPaper();
		this.setPartName(model.getName());
		
		viewer.setContents(model);
	}

	@Override
	public void doSave(IProgressMonitor monitor) {
		PrintWizardEditorInput ei = (PrintWizardEditorInput)this.getEditorInput();
		try {
	        ei.save(false, null);
        } catch (Exception e) {
        	MessageDialog.openError(getSite().getShell(), "저장 오류", "데이터 처리 중 오류가 발생하였습니다.\n\n"+e.getMessage());
	        e.printStackTrace();
        }
		// 재조회
		model = ei.getPaper();
		this.setPartName(model.getName());
		
		getGraphicalViewer().setContents(model);
		op.setContents(model);
		
		// 수정마크(*) 삭제 및 save 메뉴 disable 처리
		getCommandStack().markSaveLocation();
	}

	@Override
    public void doSaveAs() {
		PrintWizardEditorInput ei = (PrintWizardEditorInput)this.getEditorInput();
		try {
			InputDialog dlg = new InputDialog(getSite().getShell(),
		            "설명입력", "인쇄설정 설명을 입력하세요.", model.getName(), null);
		        if (dlg.open() == Window.OK) {
			        ei.save(true, dlg.getValue());
		        }else{
		        	return;
		        }
        } catch (Exception e) {
        	MessageDialog.openError(getSite().getShell(), "저장 오류", "데이터 처리 중 오류가 발생하였습니다.\n\n"+e.getMessage());
	        e.printStackTrace();
        }
		// 재조회
		model = ei.getPaper();
		this.setPartName(model.getName());
		
		getGraphicalViewer().setContents(model);
		op.setContents(model);
		getCommandStack().markSaveLocation();
    }
	
	@Override
    public boolean isSaveAsAllowed() {
	   return true;
    }

	public void print(){
		// TODO sample print test
		
	}
	@Override
	public boolean isDirty() {
		return getCommandStack().isDirty();
	}

	@Override
	public void commandStackChanged(EventObject event) {
		// 변경사항 알림 => SAVE, SAVE_ALL 버튼 활성화 됨
		firePropertyChange(IEditorPart.PROP_DIRTY);
		super.commandStackChanged(event);
	}
	
	@SuppressWarnings("unchecked")
    @Override
	protected void createActions() {
		super.createActions();
		
//		getEditorSite().getActionBars().setGlobalActionHandler(ActionFactory.PRINT.getId(), getActionRegistry().getAction(ActionFactory.PRINT.getId()));
		
		// Copy 용
		ActionRegistry registry = getActionRegistry();
		
		IAction action = new CopyElementAction(this);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());
		
		action = new CutElementAction(this);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());
		
		action = new PasteElementAction(this);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());
		
		// 정렬
		action = new MatchWidthAction(this);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());
		
		action = new MatchHeightAction(this);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new AlignmentAction((IWorkbenchPart)this, PositionConstants.LEFT);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new AlignmentAction((IWorkbenchPart)this, PositionConstants.RIGHT);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new AlignmentAction((IWorkbenchPart)this, PositionConstants.TOP);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new AlignmentAction((IWorkbenchPart)this, PositionConstants.BOTTOM);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new AlignmentAction((IWorkbenchPart)this, PositionConstants.CENTER);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new AlignmentAction((IWorkbenchPart)this, PositionConstants.MIDDLE);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());
	}

	/**
	 * Zoom 설정
	 */
	private void configureZoomLevel() {
		GraphicalViewer viewer = getGraphicalViewer();

		ScalableRootEditPart rootEditPart = new ScalableRootEditPart();
		viewer.setRootEditPart(rootEditPart);

		ZoomManager manager = rootEditPart.getZoomManager();
		getActionRegistry().registerAction(new ZoomInAction(manager));
		getActionRegistry().registerAction(new ZoomOutAction(manager));

		double[] zoomLevels = new double[] { 0.25, 0.5, 0.75, 1.0, 1.5, 2.0, 2.5, 3.0, 4.0, 5.0, 10.0, 20.0 };
		manager.setZoomLevels(zoomLevels);
		
		// Zoom 배율
		manager.setZoom(1.0);
		
		List<String> contributions = new ArrayList<String>();
		contributions.add(ZoomManager.FIT_ALL);
		contributions.add(ZoomManager.FIT_HEIGHT);
		contributions.add(ZoomManager.FIT_WIDTH);
		manager.setZoomLevelContributions(contributions);
	}

	/**
	 * Ruler 설정
	 */
	private void configureRuler() {
		GraphicalViewer viewer = getGraphicalViewer();		
		
		viewer.setProperty(RulerProvider.PROPERTY_VERTICAL_RULER, 
				new ElementRulerProvider(new ElementRuler(false)));
		viewer.setProperty(RulerProvider.PROPERTY_HORIZONTAL_RULER, 
				new ElementRulerProvider(new ElementRuler(true)));
		viewer.setProperty(RulerProvider.PROPERTY_RULER_VISIBILITY, true);	
		
		IAction action = new ToggleRulerVisibilityAction(getGraphicalViewer());
		getActionRegistry().registerAction(action);
	}
	
	/**
	 * Key 처리
	 */
	private void configureKeyHandler() {
		GraphicalViewer viewer = getGraphicalViewer();

		// TODO getSite().getSelectionProvider() is null >> 화살표로 Object이동 안됨
		keyHandler = new ArrowKeyHandler(viewer, getSite().getSelectionProvider(), getCommandStack());

		// Zoom 관련 Key 처리
		keyHandler.put(KeyStroke.getPressed(SWT.DEL, 127, 0), getActionRegistry().getAction(ActionFactory.DELETE.getId()));
		keyHandler.put(KeyStroke.getPressed('+', SWT.KEYPAD_ADD, 0), getActionRegistry().getAction(GEFActionConstants.ZOOM_IN));
		keyHandler.put(KeyStroke.getPressed('-', SWT.KEYPAD_SUBTRACT, 0),
		        getActionRegistry().getAction(GEFActionConstants.ZOOM_OUT));

		// Mouse Zoom in/out
//		viewer.setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.NONE), MouseWheelZoomHandler.SINGLETON);

		viewer.setKeyHandler(keyHandler);
	}

	/**
	 * Geometry config
	 */
	private void configureGeometry() {
		GraphicalViewer viewer = getGraphicalViewer();

		viewer.setProperty(SnapToGeometry.PROPERTY_SNAP_ENABLED, true);	
		
		IAction action = new ToggleSnapToGeometryAction(viewer);
		getActionRegistry().registerAction(action);
	}
	
	/**
	 * Gird config
	 */
	private void configureGrid() {
		GraphicalViewer viewer = getGraphicalViewer();
		
		viewer.setProperty(SnapToGrid.PROPERTY_GRID_ENABLED, true);
		viewer.setProperty(SnapToGrid.PROPERTY_GRID_VISIBLE, true);
		
		// 초반 위치 지정
//		viewer.setProperty(SnapToGrid.PROPERTY_GRID_ORIGIN, new Point(0,0));
		
		// 가로/세로 격자
		viewer.setProperty(SnapToGrid.PROPERTY_GRID_SPACING, new Dimension(10,10));
		
		IAction action = new ToggleGridAction(viewer);
		getActionRegistry().registerAction(action);
	}
	
	@Override
	public Object getAdapter(@SuppressWarnings("rawtypes") Class type) {
		if (type == ZoomManager.class)
			return ((ScalableRootEditPart) getGraphicalViewer().getRootEditPart()).getZoomManager();
		else if (type == IContentOutlinePage.class)
			return op;
		else
			return super.getAdapter(type);
	}

	@Override
	protected PaletteRoot getPaletteRoot() {
		PaletteRoot root = new PaletteRoot();
		
		PaletteGroup controlGroup = new PaletteGroup("Control");
		root.add(controlGroup);
		
		SelectionToolEntry selectionToolEntry = new SelectionToolEntry();
		controlGroup.add(selectionToolEntry);
		controlGroup.add(new MarqueeToolEntry());
				
		PaletteDrawer elementDrawer = new PaletteDrawer("Element",
				ImageDescriptor.createFromFile(PrintWizardEditor.class, PrintConstants.IMG_ELEMENT));
		elementDrawer.add(new CreationToolEntry("사각형", "사각형을 생성합니다.",
				new ElementCreationFactory(BoxElement.class),
				ImageDescriptor.createFromFile(PrintWizardEditor.class, PrintConstants.IMG_BOX),
				ImageDescriptor.createFromFile(PrintWizardEditor.class, PrintConstants.IMG_BOX_LARGE)));
		
		elementDrawer.add(new CreationToolEntry("QRCode", "QRCode를 생성합니다.",
				new ElementCreationFactory(QRCodeElement.class),
				ImageDescriptor.createFromFile(PrintWizardEditor.class, PrintConstants.IMG_QRCODE),
				ImageDescriptor.createFromFile(PrintWizardEditor.class, PrintConstants.IMG_QRCODE_LARGE)));
		
		elementDrawer.add(new CreationToolEntry("라인", "라인을 생성합니다.",
				new ElementCreationFactory(LineElement.class),
				ImageDescriptor.createFromFile(PrintWizardEditor.class, PrintConstants.IMG_LINE),
				ImageDescriptor.createFromFile(PrintWizardEditor.class, PrintConstants.IMG_LINE_LARGE)));

		elementDrawer.add(new CreationToolEntry("고정 문자", "고정 문자열을 생성합니다.",
				new ElementCreationFactory(TextBoxElement.class),
				ImageDescriptor.createFromFile(PrintWizardEditor.class, PrintConstants.IMG_TEXTBOX),
				ImageDescriptor.createFromFile(PrintWizardEditor.class, PrintConstants.IMG_TEXTBOX_LARGE)));
		
		elementDrawer.add(new CreationToolEntry("계량 항목", "계량 자료로부터 가져올 항목을 생성합니다.",
				new ElementCreationFactory(DataBoxElement.class),
				ImageDescriptor.createFromFile(PrintWizardEditor.class, PrintConstants.IMG_DATA),
				ImageDescriptor.createFromFile(PrintWizardEditor.class, PrintConstants.IMG_DATA_LARGE)));

		elementDrawer.add(new CreationToolEntry("고정 항목", "고정 항목을 생성합니다.",
				new ElementCreationFactory(FixBoxElement.class),
				ImageDescriptor.createFromFile(PrintWizardEditor.class, PrintConstants.IMG_FIX),
				ImageDescriptor.createFromFile(PrintWizardEditor.class, PrintConstants.IMG_FIX_LARGE)));

		root.add(elementDrawer);
		
		root.setDefaultEntry(selectionToolEntry);
		root.setVisible(true);
		
		return root;
	}
	/**
	 * Outline View 와 Overview 를 inner class로 구성
	 * 
	 * @author oberak
	 */
	class OutlinePage extends ContentOutlinePage {

		private SashForm sash;
		private ScrollableThumbnail thumbnail;
		private DisposeListener disposeListener;

		public OutlinePage() {
			super(new TreeViewer());
		}
		
		public void setContents(Object contents){
			getViewer().setContents(contents);
		}
		
		@Override
		public void createControl(Composite parent) {
			sash = new SashForm(parent, SWT.VERTICAL);

			getViewer().createControl(sash);

			getViewer().setEditDomain(getEditDomain());
			getViewer().setEditPartFactory(new AnnotatedTreeEditPartFactory());
//			getViewer().setEditPartFactory(new ElementTreeEditPartFactory());
			getViewer().setContents(model);

			getSelectionSynchronizer().addViewer(getViewer());

			// overview
			Canvas canvas = new Canvas(sash, SWT.BORDER);
			LightweightSystem lws = new LightweightSystem(canvas);

			RootEditPart rootEditPart = getGraphicalViewer().getRootEditPart();
			thumbnail = new ScrollableThumbnail((Viewport) ((ScalableRootEditPart) rootEditPart).getFigure());
			thumbnail.setSource(((ScalableRootEditPart) rootEditPart).getLayer(LayerConstants.PRINTABLE_LAYERS));

			lws.setContents(thumbnail);

			disposeListener = new DisposeListener() {
				@Override
				public void widgetDisposed(DisposeEvent e) {
					if (thumbnail != null) {
						thumbnail.deactivate();
						thumbnail = null;
					}
				}
			};
			getGraphicalViewer().getControl().addDisposeListener(disposeListener);
			
			// Copy 용
			IActionBars bars = getSite().getActionBars();
			ActionRegistry ar = getActionRegistry();
		
			bars.setGlobalActionHandler(ActionFactory.COPY.getId(),
					ar.getAction(ActionFactory.COPY.getId()));
			bars.setGlobalActionHandler(ActionFactory.PASTE.getId(),
					ar.getAction(ActionFactory.PASTE.getId()));
		}

		@Override
		public void init(IPageSite pageSite) {
			super.init(pageSite);

			// 액션을 등록
			ActionRegistry registry = getActionRegistry();
			IActionBars bars = pageSite.getActionBars();

			String id = ActionFactory.UNDO.getId();
			bars.setGlobalActionHandler(id, registry.getAction(id));

			id = ActionFactory.REDO.getId();
			bars.setGlobalActionHandler(id, registry.getAction(id));

			id = ActionFactory.DELETE.getId();
			bars.setGlobalActionHandler(id, registry.getAction(id));

			bars.updateActionBars();
			
			getViewer().setContextMenu(new PrintWizardContextMenuProvider(
					getViewer(), getActionRegistry()));
		}

		@Override
		public Control getControl() {
			return sash;
		}

		@Override
		public void dispose() {
			getSelectionSynchronizer().removeViewer(getViewer());
			Control control = getGraphicalViewer().getControl();
			if (control != null && !control.isDisposed())
				control.removeDisposeListener(disposeListener);				
			super.dispose();
		}	
	}// OutlinePage class
}// class
