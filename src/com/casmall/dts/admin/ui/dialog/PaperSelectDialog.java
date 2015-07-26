package com.casmall.dts.admin.ui.dialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.FontRegistry;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewerEditor;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationEvent;
import org.eclipse.jface.viewers.ColumnViewerEditorActivationStrategy;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableColorProvider;
import org.eclipse.jface.viewers.ITableFontProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.nebula.jface.gridviewer.GridTableViewer;
import org.eclipse.nebula.jface.gridviewer.GridViewerEditor;
import org.eclipse.nebula.widgets.grid.Grid;
import org.eclipse.nebula.widgets.grid.GridColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import com.casmall.common.StringUtil;
import com.casmall.dts.admin.print.editor.PrintWizardEditor;
import com.casmall.dts.admin.print.editor.PrintWizardEditorInput;
import com.casmall.dts.biz.domain.TsPrtInfDTO;
import com.casmall.dts.biz.mgr.TsPrtManager;
import com.casmall.dts.common.ColorRepository;
import com.casmall.dts.common.DTSConstants;
import com.casmall.usr.domain.CmUsrInfDTO;
import com.casmall.usr.mgr.SessionManager;
import com.swtdesigner.SWTResourceManager;

/**
 * �μ⼳�� ��ȸ
 * 
 * @author OBERAK
 */
public class PaperSelectDialog extends Dialog {
	protected static Log logger = LogFactory.getLog(PaperSelectDialog.class);
			
	protected final Font titleFont = SWTResourceManager.getFont("Arial Black", 18, SWT.BOLD);
	protected final Font defaultFont = SWTResourceManager.getFont("Arial Black", 16, SWT.BOLD);
	
	protected Object result;
	protected Shell shell;
	private Composite compCenter;
	
	private String[] colProp = { "no", "prt_nm", "bss_cdnt_x", "bss_cdnt_y" };
	private String[] colName = { "No", "�μ⼳�� ��", "����-x", "����-y"};
	/** �÷� �ʺ� : �ۼ�Ʈ */
	private int[] colWidth = { 10, 50, 20, 20 };
	private int[] colAlign = { SWT.CENTER, SWT.LEFT, SWT.RIGHT, SWT.RIGHT };

	private GridTableViewer gridViewer;
	private ArrayList<TsPrtInfDTO> listData;
	private CmUsrInfDTO user;
	private Text txtSearch;
	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public PaperSelectDialog(Shell parent, int style) {
		super(parent, style);
		setText("�μ� ���� ��ȸ");
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public Object open() {
		user = SessionManager.getInstance().getUsr();
		createContents();
		shell.open();
		shell.layout();
		init();
		
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(624, 672);
		shell.setText(getText());
		shell.setLayout(new GridLayout(1, false));
		shell.setBackground(ColorRepository.getColor(ColorRepository.BG_CONTENTS));
//		shell.setBackgroundMode(SWT.TRANSPARENT);
		
		Label lblTitle = new Label(shell, SWT.NONE);
		lblTitle.setBackground(SWTResourceManager.getColor(65, 105, 225));
		lblTitle.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		lblTitle.setFont(titleFont);
		lblTitle.setText("�μ⼳�� ��ȸ");
		
		compCenter = new Composite(shell, SWT.NONE);
		compCenter.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, false, 1, 1));
		compCenter.setLayout(new GridLayout(6, false));
		
		Label lblSearch = new Label(compCenter, SWT.NONE);
		lblSearch.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblSearch.setFont(defaultFont);
		lblSearch.setText("�μ⼳�� ��:");
		
		txtSearch = new Text(compCenter, SWT.BORDER);
		txtSearch.setFont(defaultFont);
		txtSearch.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		Button btnSearch = new Button(compCenter, SWT.NONE);
		btnSearch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				select(txtSearch.getText());
			}
		});
		btnSearch.setFont(defaultFont);
		btnSearch.setText(" �� ȸ ");
		
		Button btnNew = new Button(compCenter, SWT.NONE);
		btnNew.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		btnNew.setFont(defaultFont);
		btnNew.setText(" �� �� ");
		btnNew.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onClickNew();
			}
		});
		
		Button btnDelete = new Button(compCenter, SWT.NONE);
		btnDelete.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		btnDelete.setFont(defaultFont);
		btnDelete.setText(" �� �� ");
		btnDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onClickDelete();
			}
		});
		Button btnCancel = new Button(compCenter, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		btnCancel.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false));
		btnCancel.setFont(defaultFont);
		btnCancel.setText(" ��  �� ");
		
		initGird();
	}// createContents

	protected void onClickNew() {
		IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
		try {
	        page.openEditor(new PrintWizardEditorInput(""), PrintWizardEditor.ID, false);
        } catch (PartInitException e) {
	        if (logger.isErrorEnabled()) {
	            logger.error("�μ⼳�� �ű� ���� - "+e.getMessage());
            }
	        e.printStackTrace();
        }
		shell.close();
    }

	protected void onClickDelete() {
		Grid g = gridViewer.getGrid();
		if(g.getSelectionIndex() == -1){
			MessageDialog.openInformation(shell, "������ ���� �ʿ�", "���õ� �����Ͱ� �����ϴ�. \n\n��Ͽ��� ���� �� �۾��ϼ���.");
			return;
		}else{
			if (listData != null && listData.size() > g.getSelectionIndex()) {
				boolean rtn = MessageDialog.openConfirm(shell, "���� Ȯ��", "�μ������� �����Ͻðڽ��ϱ�?\n\n�������-"+listData.get(g.getSelectionIndex()).getPrt_nm());
				if(!rtn){
					return;
				}
				try {
					TsPrtManager prtMgr = new TsPrtManager();
					TsPrtInfDTO dto = listData.get(g.getSelectionIndex());
					dto.setDel_yn(DTSConstants.FLAG_Y);
					dto.setEdt_dt(new Date());
					dto.setEdt_id(user.getLgn_id());
					prtMgr.updateTsPrtInf(dto);
	                // ��� ����ȸ
	                select(null);
                } catch (IOException e) {
                	MessageDialog.openError(shell, "���� ����", "������ ���� �� ������ �߻��Ͽ����ϴ�. \n\n"+e.getMessage());
                	return;
                }
			}
		}
    }

	/**
	 * init
	 */
	private void init() {
		select(null);
    }// initData
	
	private void select(String str) {
		TsPrtManager prtMgr = new TsPrtManager();
		TsPrtInfDTO param = new TsPrtInfDTO();
		param.setPrt_nm(str);
		listData = prtMgr.selectTsPrtInf(param);
		
		gridViewer.setInput(listData.toArray(new TsPrtInfDTO[0]));
	}
	
	private void initGird() {
		gridViewer = new GridTableViewer(shell, SWT.V_SCROLL | SWT.BORDER);
		gridViewer.setLabelProvider(new ListGridLabelProvider());
		gridViewer.setContentProvider(new ListGridContentProvider());
		gridViewer.getGrid().setSelectionEnabled(true);
		gridViewer.getGrid().setFont(SWTResourceManager.getFont("Arial", 12, SWT.NORMAL));
		gridViewer.getGrid().setItemHeight(28);
		
		gridViewer.setCellEditors(new CellEditor[] { new TextCellEditor(gridViewer.getGrid()),
		        new TextCellEditor(gridViewer.getGrid()) });
		gridViewer.setCellModifier(new ICellModifier() {

			public boolean canModify(Object element, String property) {
				return false;
			}

			public Object getValue(Object element, String property) {
				return element.toString();
			}

			public void modify(Object element, String property, Object value) {

			}
		});
		gridViewer.setColumnProperties(colProp);
		ColumnViewerEditorActivationStrategy actSupport = new ColumnViewerEditorActivationStrategy(gridViewer) {
			protected boolean isEditorActivationEvent(ColumnViewerEditorActivationEvent event) {
				return event.eventType == ColumnViewerEditorActivationEvent.TRAVERSAL
				        || event.eventType == ColumnViewerEditorActivationEvent.MOUSE_DOUBLE_CLICK_SELECTION
				        || (event.eventType == ColumnViewerEditorActivationEvent.KEY_PRESSED && event.keyCode == SWT.CR);
			}
		};
		GridViewerEditor.create(gridViewer, actSupport, ColumnViewerEditor.TABBING_HORIZONTAL
		        | ColumnViewerEditor.TABBING_MOVE_TO_ROW_NEIGHBOR | ColumnViewerEditor.TABBING_VERTICAL
		        | ColumnViewerEditor.KEYBOARD_ACTIVATION);

		for (int i = 0; i < colName.length; i++) {
			GridColumn column = null;
			column = new GridColumn(gridViewer.getGrid(), SWT.NONE);
			column.setWidth(colWidth[i]);
			column.setAlignment(colAlign[i]);
			column.setText(colName[i]);
			column.setResizeable(true);
		}

		gridViewer.getGrid().setLinesVisible(true);
		gridViewer.getGrid().setHeaderVisible(true);
		gridViewer.getGrid().addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent evt) {
				Grid g = (Grid) evt.getSource();
				if (listData != null && listData.size() > g.getSelectionIndex()) {
					TsPrtInfDTO vo = listData.get(g.getSelectionIndex());

					try {
						IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
						page.openEditor(new PrintWizardEditorInput(""+vo.getPrt_seq()), PrintWizardEditor.ID, false);
						shell.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
		Grid grid = gridViewer.getGrid();
		grid.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				int width = 0;
				for(int i=0;i<gridViewer.getGrid().getColumnCount();i++){
					width = (int)(colWidth[i]*gridViewer.getGrid().getClientArea().width/100);
					if(i==gridViewer.getGrid().getColumnCount()-1){
						width+=5;
					}
					gridViewer.getGrid().getColumn(i).setWidth(width);
				}

				int add = gridViewer.getGrid().getBounds().width/100 - 7;
				gridViewer.getGrid().setFont(SWTResourceManager.getFont("Arial", 10 + (add>0?add:0), SWT.NORMAL));
			}
		});
		grid.setRowsResizeable(true);

		grid.setLineColor(ColorRepository.getColor(ColorRepository.GRID_LINE));
		// grid.setBackgroundMode(SWT.INHERIT_FORCE);
		GridData gridViewerLData = new GridData();
		gridViewerLData.grabExcessHorizontalSpace = true;
		gridViewerLData.horizontalAlignment = GridData.FILL;
		gridViewerLData.grabExcessVerticalSpace = true;
		gridViewerLData.verticalAlignment = GridData.FILL;
		gridViewerLData.verticalIndent = 5;
		gridViewer.getGrid().setLayoutData(gridViewerLData);
	}// initGird
	
	public class ListGridLabelProvider extends LabelProvider implements ITableLabelProvider, ITableFontProvider,
	        ITableColorProvider {
		FontRegistry registry = new FontRegistry();

		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}

		public String getColumnText(Object element, int columnIndex) {
			TsPrtInfDTO data = (TsPrtInfDTO) element;
			if("No".equalsIgnoreCase(colProp[columnIndex])){
				return String.valueOf(listData.indexOf(data)+1);
			}
			Object o = StringUtil.invoke(data, StringUtil.makeGetter(colProp[columnIndex]), null);
			return StringUtil.extractString(o, 0);
		}

		public Font getFont(Object element, int columnIndex) {

			return null;
		}

		public Color getBackground(Object element, int columnIndex) {
			TsPrtInfDTO data = (TsPrtInfDTO) element;
			if (listData.indexOf(data) % 2 == 0) {
				return ColorRepository.getColor(ColorRepository.GRID_ODD_BG);
			}
			return null;
		}

		public Color getForeground(Object element, int columnIndex) {
			TsPrtInfDTO data = (TsPrtInfDTO) element;
			if (listData.indexOf(data) % 2 == 0) {
				return Display.getCurrent().getSystemColor(SWT.COLOR_DARK_BLUE);
			}
			return null;
		}
	}// class ListGridLabelProvider
	
	private class ListGridContentProvider implements IStructuredContentProvider {
		public Object[] getElements(Object inputElement) {
			return (TsPrtInfDTO[]) inputElement;
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}// class ListGridContentProvider
}
