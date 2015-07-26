
package com.casmall.dts.admin.splashHandlers;

import java.io.IOException;
import java.util.ArrayList;

import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.preferences.ScopedPreferenceStore;
import org.eclipse.ui.splash.AbstractSplashHandler;

import com.casmall.common.CryptoUtil;
import com.casmall.common.DConstants;
import com.casmall.dts.common.DTSConstants;
import com.casmall.dts.common.ImageRepository;
import com.casmall.dts.ui.preferences.DTSPreConstants;
import com.casmall.usr.domain.CmUsrInfDTO;
import com.casmall.usr.mgr.CmUsrInfMgr;
import com.swtdesigner.SWTResourceManager;

/**
 * @since 3.3
 * 
 */
public class InteractiveSplashHandler extends AbstractSplashHandler {
	private ScopedPreferenceStore preferences = new ScopedPreferenceStore(ConfigurationScope.INSTANCE, DTSConstants.PLUGIN_ID);
	private final static int F_LABEL_HORIZONTAL_INDENT = 250;
	private final static int F_BUTTON_WIDTH_HINT = 83;
	private final static int F_BUTTON_HEIGHT_HINT = 36;
	private final static int F_TEXT_WIDTH_HINT = 164;
	private final static int F_COLUMN_COUNT = 3;
	
	private Composite fCompositeLogin;
	
	private Text fTextUsername;
	
	private Text fTextPassword;
	
	private CLabel fButtonOK;
	
	private CLabel fButtonCancel;
	
	private boolean fAuthenticated;
	
	/**
	 * 
	 */
	public InteractiveSplashHandler() {
		fCompositeLogin = null;
		fTextUsername = null;
		fTextPassword = null;
		fButtonOK = null;
		fButtonCancel = null;
		fAuthenticated = false;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.splash.AbstractSplashHandler#init(org.eclipse.swt.widgets.Shell)
	 */
	public void init(final Shell splash) {
		// Store the shell
		super.init(splash);
		// Configure the shell layout
		configureUISplash();
		// Create UI
		createUI();		
		// Create UI listeners
		createUIListeners();
		// Force the splash screen to layout
		splash.layout(true);
		// Keep the splash screen visible and prevent the RCP application from 
		// loading until the close button is clicked.
		if("".equals(fTextUsername.getText())){
			fTextUsername.setFocus();
		}else{
			fTextPassword.setFocus();
		}
		if(preferences.getBoolean(DTSPreConstants.GN_AUTO_LOGIN_FLAG)){
			handleButtonOKWidgetSelected();
		}
		doEventLoop();
	}
	
	/**
	 * 
	 */
	private void doEventLoop() {
		Shell splash = getSplash();
		while (fAuthenticated == false) {
			if (splash.getDisplay().readAndDispatch() == false) {
				splash.getDisplay().sleep();
			}
		}
	}

	/**
	 * 
	 */
	private void createUIListeners() {
		// Create the OK button listeners
		createUIListenersButtonOK();
		// Create the cancel button listeners
		createUIListenersButtonCancel();
	}

	/**
	 * 
	 */
	private void createUIListenersButtonCancel() {
		fButtonCancel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				handleButtonCancelWidgetSelected();
			}
		});
	}

	/**
	 * 
	 */
	private void handleButtonCancelWidgetSelected() {
		// Abort the loading of the RCP application
		getSplash().getDisplay().close();
		System.exit(0);		
	}
	
	/**
	 * 
	 */
	private void createUIListenersButtonOK() {
		fButtonOK.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				handleButtonOKWidgetSelected();
			}
		});		
	}

	/**
	 * 
	 */
	private void handleButtonOKWidgetSelected() {
		String username = fTextUsername.getText();
		String password = fTextPassword.getText();

		if(username.trim().length()==0){
			MessageDialog.openError(
					getSplash(),
					"Authentication Failed",  //$NON-NLS-1$
					"사용자 ID를 입력하세요.");  //$NON-NLS-1$
			fTextUsername.setFocus();
			return;
		}
		if(password.trim().length()==0){
			MessageDialog.openError(
					getSplash(),
					"Authentication Failed",  //$NON-NLS-1$
					"비밀번호를 입력하세요.");  //$NON-NLS-1$
			fTextPassword.setFocus();
			return;
		}
		
		try{
			CmUsrInfMgr mgr = new CmUsrInfMgr();
			if(!mgr.checkLogin(username.trim(), password.trim())){
				MessageDialog.openInformation(getSplash(),"Login check","ID가 없거나 비밀번호가 잘못되었습니다.");
				fTextUsername.setText("");
				fTextPassword.setText("");
				fTextUsername.setBackgroundImage(SWTResourceManager.getImage(InteractiveSplashHandler.class, "/"+ImageRepository.BG_LOGIN_ID));
				fTextPassword.setBackgroundImage(SWTResourceManager.getImage(InteractiveSplashHandler.class, "/"+ImageRepository.BG_LOGIN_PW));
				fTextUsername.setFocus();
				return;
			}
			CmUsrInfDTO uDto = new CmUsrInfDTO();
			uDto.setLgn_id(username.trim());
			ArrayList<CmUsrInfDTO> list = mgr.selectUsrInf(uDto );
			if( !DTSConstants.CD_USR_GRD_ROOT.equals(list.get(0).getAth_grd()) ){
				MessageDialog.openWarning(getSplash(), "권한오류", "권한이 부족합니다. 관리자 권한 ID로 로그인하세요.");
				return;
			}
			try {
				preferences.setValue(DTSPreConstants.GN_AUTO_LOGIN_ID, username.trim());
				preferences.save();
			} catch (IOException ie) {
				ie.printStackTrace();
			}
			fAuthenticated = true;
		}catch(Exception e){
			e.printStackTrace();
			MessageDialog.openError(
					getSplash(),
					"Authentication Failed",  //$NON-NLS-1$
					"프로그램 실행 중 오류가 발생했습니다. 구입업체에 문의하세요. \n\n"+e.getMessage());  //$NON-NLS-1$
			return;
		}
	}
	
	/**
	 * 
	 */
	private void createUI() {
		// Create the login panel
		createUICompositeLogin();
		// Create the blank spanner
		createUICompositeBlank();
		// Create the user name label
		createUILabelUserName();
		// Create the user name text widget
		createUITextUserName();
		// Create the password label
		createUILabelPassword();
		// Create the password text widget
		createUITextPassword();
		// Create the blank label
		createUILabelBlank();
		// Create the OK button
		createUIButtonOK();
		// Create the cancel button
		createUIButtonCancel();
	}		
	
	/**
	 * 
	 */
	private void createUIButtonCancel() {
		// Create the button
		fButtonCancel = new CLabel(fCompositeLogin, SWT.NONE);
		fButtonCancel.setBackgroundImage(SWTResourceManager.getImage(InteractiveSplashHandler.class, "/"+ImageRepository.BTN_LOGIN_CANCEL));
		Cursor cursor = new Cursor(Display.getDefault(), SWT.CURSOR_HAND);  
		fButtonCancel.setCursor(cursor);	

		// Configure layout data
		GridData data = new GridData(SWT.FILL, SWT.FILL, false, false);
		data.widthHint = F_BUTTON_WIDTH_HINT;	
		data.heightHint = F_BUTTON_HEIGHT_HINT;
		fButtonCancel.setLayoutData(data);
	}

	/**
	 * 
	 */
	private void createUIButtonOK() {
		// Create the button
		fButtonOK = new CLabel(fCompositeLogin, SWT.NONE);
		fButtonOK.setBackgroundImage(SWTResourceManager.getImage(InteractiveSplashHandler.class, "/"+ImageRepository.BTN_LOGIN));
		Cursor cursor = new Cursor(Display.getDefault(), SWT.CURSOR_HAND);  
		fButtonOK.setCursor(cursor);	
		// Configure layout data
		GridData data = new GridData(SWT.FILL, SWT.FILL, false, false);
		data.heightHint = F_BUTTON_HEIGHT_HINT;
		data.widthHint = F_BUTTON_WIDTH_HINT;
		fButtonOK.setLayoutData(data);
	}

	/**
	 * 
	 */
	private void createUILabelBlank() {
		Label label = new Label(fCompositeLogin, SWT.NONE);
		label.setVisible(false);
	}

	/**
	 * 
	 */
	private void createUITextPassword() {
		// Create the text widget
		int style = SWT.PASSWORD | SWT.BORDER;
		fTextPassword = new Text(fCompositeLogin, style);
		fTextPassword.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		fTextPassword.setFont(SWTResourceManager.getFont("Arial Baltic", 14, SWT.BOLD));
		if(preferences.getBoolean(DTSPreConstants.GN_AUTO_LOGIN_FLAG)){
			try {
	            fTextPassword.setText(CryptoUtil.decrypt3DES(preferences.getString(DTSPreConstants.GN_AUTO_LOGIN_PW)));
            } catch (Exception e1) {
	            e1.printStackTrace();
            }
		}
		if("".equals(fTextPassword.getText()))
			fTextPassword.setBackgroundImage(SWTResourceManager.getImage(InteractiveSplashHandler.class, "/"+ImageRepository.BG_LOGIN_PW));

		fTextPassword.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(fTextPassword.getText().trim().length()>0){
					fTextPassword.setBackgroundImage(null);
				}else{
					fTextPassword.setBackgroundImage(SWTResourceManager.getImage(InteractiveSplashHandler.class, "/"+ImageRepository.BG_LOGIN_PW));
				}
				if(e.keyCode == DConstants.KEY_ENTER){
					handleButtonOKWidgetSelected();
				}
			}
		});
		// Configure layout data
		GridData data = new GridData(SWT.NONE, SWT.NONE, false, false);
		data.widthHint = F_TEXT_WIDTH_HINT;
		data.horizontalSpan = 2;
		fTextPassword.setLayoutData(data);		
	}

	/**
	 * 
	 */
	private void createUILabelPassword() {
		// Create the label
		Label label = new Label(fCompositeLogin, SWT.NONE);
		label.setFont(SWTResourceManager.getFont("Arial Baltic", 14, SWT.BOLD));
		// Configure layout data
		GridData data = new GridData();
		data.horizontalIndent = F_LABEL_HORIZONTAL_INDENT;
		label.setLayoutData(data);					
	}

	/**
	 * 
	 */
	private void createUITextUserName() {
		// Create the text widget
		fTextUsername = new Text(fCompositeLogin, SWT.BORDER);
		fTextUsername.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		fTextUsername.setFont(SWTResourceManager.getFont("Arial Baltic", 14, SWT.BOLD));
		fTextUsername.setText(preferences.getString(DTSPreConstants.GN_AUTO_LOGIN_ID));
		if("".equals(fTextUsername.getText()))
			fTextUsername.setBackgroundImage(SWTResourceManager.getImage(InteractiveSplashHandler.class, "/"+ImageRepository.BG_LOGIN_ID));

		fTextUsername.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if(fTextUsername.getText().trim().length()>0){
					fTextUsername.setBackgroundImage(null);
				}else{
					fTextUsername.setBackgroundImage(SWTResourceManager.getImage(InteractiveSplashHandler.class, "/"+ImageRepository.BG_LOGIN_ID));
				}
				if(e.keyCode == DConstants.KEY_ENTER){
					if(fTextUsername.getText().trim().length()>0){
						fTextPassword.setFocus();
					}
				}
			}
		});
		
		// Configure layout data
		GridData data = new GridData(SWT.NONE, SWT.NONE, false, false);
		data.widthHint = F_TEXT_WIDTH_HINT;
		data.horizontalSpan = 2;
		fTextUsername.setLayoutData(data);		
	}

	/**
	 * 
	 */
	private void createUILabelUserName() {
		// Create the label
		Label label = new Label(fCompositeLogin, SWT.NONE);
		label.setFont(SWTResourceManager.getFont("Arial Baltic", 14, SWT.BOLD));
		// Configure layout data
		GridData data = new GridData();
		data.horizontalIndent = F_LABEL_HORIZONTAL_INDENT;
		label.setLayoutData(data);		
	}

	/**
	 * 
	 */
	private void createUICompositeBlank() {
		Composite spanner = new Composite(fCompositeLogin, SWT.NONE);
		GridData data = new GridData(SWT.FILL, SWT.FILL, false, false);
		data.heightHint = 13;
		data.horizontalSpan = F_COLUMN_COUNT;
		spanner.setLayoutData(data);
	}

	/**
	 * 
	 */
	private void createUICompositeLogin() {
		// Create the composite
		fCompositeLogin = new Composite(getSplash(), SWT.BORDER);
		GridLayout layout = new GridLayout(F_COLUMN_COUNT, false);
		layout.verticalSpacing = 4;
		fCompositeLogin.setLayout(layout);		
	}

	/**
	 * 
	 */
	private void configureUISplash() {
		// Configure layout
		FillLayout layout = new FillLayout(); 
		getSplash().setLayout(layout);
		// Force shell to inherit the splash background
		getSplash().setBackgroundMode(SWT.INHERIT_FORCE);
	}
	
}

