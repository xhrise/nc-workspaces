/*
 * @(#)ILoginGuidePanel.java 1.0 2001-8-15
 *
 * Copyright 2005 UFIDA Software Co. Ltd. All rights reserved.
 * UFIDA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */
package nc.ui.sm.login;

import java.applet.AppletStub;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import nc.bs.logging.Logger;
import nc.ui.sm.login.LoginUISupport.LoginUIEvent;
import nc.vo.sm.config.Account;
import nc.vo.sm.login.IControlConstant;
import nc.vo.sm.login.ILoginInfo;
import nc.vo.sm.login.IViewConstant;
import nc.vo.sm.login.LoginSessBean;

/**
 * ��¼�������Applet��
 * <p>
 * �ڿ��ӻ���������������룺
 * 
 * <pre>
 * if (e.getSource() == getLoginPanel().getPassword()) {
 *     getLoginButton().requestFocus();
 *     connEtoC2(e);
 * }
 * </pre>
 * 
 * <DL>
 * <DT><B>Provider:</B></DT>
 * <DD>NC-UAP</DD>
 * </DL>
 * 
 * @author �Լ̽�
 * @author ���ھ�
 * @since 2.0
 */
@SuppressWarnings("serial")
public class Login2 extends JPanel//JApplet 
implements IViewConstant, IControlConstant {
    private LoginUISupport uiSupport = null;
    
    private JPanel compPanel = null;
    
    private JComponent[] lbls = null;
    private JComponent[] comps = null;
    
    private SpringLayout sl = null;
   
    
    /**
     * ��ʼ��СӦ�ó���
     * 
     * @see #start
     * @see #stop
     * @see #destroy
     */
    public void init() {
        try {
//            Object o = KeyboardFocusManager.getCurrentKeyboardFocusManager().getDefaultFocusTraversalPolicy();
//
//            System.out.println("/////????////:"+o);
            // user code begin {1}
            setFocusTraversalPolicyProvider(false);
            // zsb+:�������ý������������������ˢ��ʱ����װ��
            try{
            nc.ui.pub.style.Style.refreshStyle();
            }catch(Exception e){
                e.printStackTrace();
            }
            //
            sl = new SpringLayout();
            // �״�ʹ��ClientEnvironmentʱ������һ���µ�Instance:
            nc.ui.pub.ClientEnvironment.newInstance().setDesktopApplet(ClientAssistant.getApplet());
            //
            uiSupport = new LoginUISupport(){

				@SuppressWarnings("unchecked")
				protected void updateLoginInfo(LoginSessBean lsb) throws Exception {
					String loginInfoItf = ClientAssistant.getApplet().getParameter("LoginInfoItf");
					if(loginInfoItf!=null && loginInfoItf.trim().length() > 0){
						try {
							Class c = Class.forName(loginInfoItf);
							Object o = c.newInstance();
							if(o instanceof ILoginInfo){
								((ILoginInfo)o).decorateLoginInfo(lsb);
							}else{
								String errMsg = "class "+loginInfoItf +" must implement "+ILoginInfo.class.getName();
								Logger.error(errMsg);
								throw new Exception(errMsg);
							}
						} catch (Exception e) {
							e.printStackTrace();
							throw e;
						}
					}
				}
            
            };
            // user code end
            setName("Login");

            //
//            lbls = new JComponent[5];//modify by houcq
//            lbls[0] = uiSupport.getLblAccount();
//            lbls[1] = uiSupport.getLblCorp();
//            lbls[2] = uiSupport.getLblDate();
//            lbls[3] = uiSupport.getLblUser();
//            lbls[4] = uiSupport.getLblUserPWD();
            lbls = new JComponent[4];//modify by houcq 2011-10-27
            lbls[0] = uiSupport.getLblAccount();
            lbls[1] = uiSupport.getLblCorp();
            lbls[2] = uiSupport.getLblUser();
            lbls[3] = uiSupport.getLblUserPWD();
            comps = new JComponent[4];
            comps[0] =uiSupport.getCbbAccount();
            comps[1] =uiSupport.getRpCorp();
            comps[2] =uiSupport.getTfUser();
            comps[3] =uiSupport.getPfUserPWD();
            uiSupport.addUIUpdateListener(new LoginUISupport.IUIUpdateListener(){
                public void uiUpdate(LoginUIEvent e) {
                    Object sour = e.getSource();
                    if(sour.equals(uiSupport.getCbbLanguage())){
                        getCompPanel().remove(uiSupport.getLblLoginResult());
                    }
                    if(sour.equals(uiSupport.getCbbLanguage()) || sour.equals(uiSupport.getOptionBtn())){
                        layoutUI();
                    }else if(sour.equals(uiSupport.getLoginBtn())){
                        if(e.getID() == LoginUIEvent.LOGIN_START){
                            loginStart();
                        }else if(e.getID() == LoginUIEvent.LOGIN_END){
                            loginEnd();
                        }
                    } else if(sour.equals(uiSupport.getCbbAccount())){
                        // ϵͳ����Ա���ף�
                        Account selAccount = (Account)uiSupport.getCbbAccount().getSelectedItem();
                        if (selAccount.getDataSourceName().equals(IControlConstant.SYS_ADM_DATASOURCE)) {
                            uiSupport.getRpCorp().setEnabled(false);
                            // getRpCorp().getUITextField().setEnabled(false);
                            // getRpCorp().getUIButton().setEnabled(false);
                        } else {
                            uiSupport.getRpCorp().setEnabled(true);
                            // getRpCorp().getUIButton().setEnabled(true);
                            // getRpCorp().getUITextField().setEnabled(true);
                            // ��ջ��棺
                        }

                      uiSupport.getTfUser().setText("");
                      uiSupport.getPfUserPWD().setText("");
                    }
                }
            });

            
            setLayout(new BorderLayout());
            add(uiSupport.getTopPanel(), BorderLayout.NORTH);
            add(uiSupport.getBottomPanel(), BorderLayout.SOUTH);
            
            JPanel centerPane = uiSupport.getCenterPanel();
            centerPane.setLayout(null);
            centerPane.add(getCompPanel());
            add(centerPane, BorderLayout.CENTER);
            Dimension d = ClientAssistant.getApplet().getSize();
            setSize(d);
            setPreferredSize(d);
            

        } catch (java.lang.Throwable th) {
            th.printStackTrace();
        }
    }
    public String getParameter(String name) {
     return NCAppletStub.getInstance().getParameter(name);
    }

    public void start() {
        getCompPanel().remove(uiSupport.getLblLoginResult());
        // ����û������Ϊ��,����Ĭ�Ͻ���,�������,��Ĭ�Ͻ����õ����������
        if (uiSupport.getTfUser().getText().trim().equals(""))
            uiSupport.getTfUser().requestFocus();
        else
            uiSupport.getPfUserPWD().requestFocus();
    }

    /**
     * Comment
     */
    public void login_KeyReleased(java.awt.event.KeyEvent keyEvent) {
        if (keyEvent.getKeyCode() == java.awt.event.KeyEvent.VK_F1) {
            uiSupport.getHelpBtn().doClick();
            // helpButton_ActionPerformed();
        }
    }

    public final void setStubToMySelf(AppletStub stub) {
        //super.setStub(stub);
    }
    private void loginEnd(){
        uiSupport.setUIEnable(true);
        String text = uiSupport.getLblLoginResult().getText();
        if(text != null && text.trim().length() > 0){
            getCompPanel().add(uiSupport.getLblLoginResult());
            sl.putConstraint(SpringLayout.WEST, uiSupport.getLblLoginResult(), 0, SpringLayout.WEST, getCompPanel());
            sl.putConstraint(SpringLayout.NORTH, uiSupport.getLblLoginResult(), 0, SpringLayout.NORTH, getCompPanel());
        }
        getCompPanel().remove(uiSupport.getLblLoginFlash());
        getCompPanel().validate();
        getCompPanel().repaint();
        
    }
    private void loginStart(){
        getCompPanel().remove(uiSupport.getLblLoginResult());
        uiSupport.setUIEnable(false);
        getCompPanel().add(uiSupport.getLblLoginFlash());
        JComponent com = uiSupport.getLoginBtn();
        sl.putConstraint(SpringLayout.WEST, uiSupport.getLblLoginFlash(), 0, SpringLayout.WEST, com);
        if(uiSupport.isShowOptionPanel()){
            com = uiSupport.getOptionPanel();
        }
        sl.putConstraint(SpringLayout.NORTH, uiSupport.getLblLoginFlash(), 6, SpringLayout.SOUTH, com);
        
        getCompPanel().validate();
        getCompPanel().repaint();
    }

   
    private JPanel getCompPanel() {
        if(compPanel == null){
            compPanel = uiSupport.getLoginCompsPanel();
            
            compPanel.setLayout(sl);
            for (int i = 0; i < lbls.length; i++) { 
                compPanel.add(lbls[i]);
            }
            for (int i = 0; i < comps.length; i++) {
                compPanel.add(comps[i]);
            }
            
            
            compPanel.add(uiSupport.getLoginBtn());
            compPanel.add(uiSupport.getOptionBtn());
            layoutUI();
            
        }
        return compPanel;
    }
    private void layoutUI(){

        int dy = 45;
        int maxX = 0;
        for (int i = 0; i < lbls.length; i++) {
            sl.putConstraint(SpringLayout.WEST, lbls[i], 0, SpringLayout.WEST, getCompPanel());
            if(i > 0)
                dy += lbls[i-1].getPreferredSize().height + 6;
            sl.putConstraint(SpringLayout.NORTH, lbls[i], dy, SpringLayout.NORTH, getCompPanel());
            maxX = Math.max(maxX, lbls[i].getPreferredSize().width);
        }
        dy = 45;
        for (int i = 0; i < comps.length; i++) {
            sl.putConstraint(SpringLayout.WEST, comps[i], maxX + 2, SpringLayout.WEST, getCompPanel());
            if(i > 0)
                dy +=  comps[i -1].getPreferredSize().height + 6;
            sl.putConstraint(SpringLayout.NORTH, comps[i],dy, SpringLayout.NORTH, getCompPanel()) ;
        }
        
        sl.putConstraint(SpringLayout.NORTH, uiSupport.getLoginBtn(), 6, SpringLayout.SOUTH, comps[comps.length -1]);
        sl.putConstraint(SpringLayout.WEST, uiSupport.getLoginBtn(), 0, SpringLayout.WEST, comps[comps.length -1]);
        sl.putConstraint(SpringLayout.NORTH, uiSupport.getOptionBtn(), 6, SpringLayout.SOUTH, comps[comps.length -1]);
        sl.putConstraint(SpringLayout.WEST, uiSupport.getOptionBtn(), 8, SpringLayout.EAST, uiSupport.getLoginBtn());
        //
        JPanel optionPanel = uiSupport.getOptionPanel();
        if(uiSupport.isShowOptionPanel()){
            getCompPanel().add(optionPanel);
            sl.putConstraint(SpringLayout.WEST, optionPanel, 0, SpringLayout.WEST, uiSupport.getLoginBtn());
            sl.putConstraint(SpringLayout.NORTH, optionPanel, 6, SpringLayout.SOUTH, uiSupport.getLoginBtn());
        }else{
            getCompPanel().remove(optionPanel);
        }
        getCompPanel().validate();
        getCompPanel().repaint();
        
    }
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		ImageIcon icon =ClientUIConfig.getInstance().getLoginUIBGIcon(); 
		if(icon != null){
			icon.paintIcon(this, g, 0, 0);
		}
	}

    
}