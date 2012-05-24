/*
 *
 * �������ڣ� 2004-11-5
 * ���ߣ�licp
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.ui.sm.cmenu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.UIManager;

import nc.bs.uap.sf.facility.SFServiceFacility;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.hotkey.HotkeyUtil;
import nc.vo.ml.IProductCode;
import nc.vo.sm.login.LoginSessBean;
@SuppressWarnings("serial")
public class QuickLoginDlg extends UIDialog implements ActionListener{

	/* ���� Javadoc��
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		Object o = e.getSource();
		if (o.equals(getLoginBtn())) {
			closeOK();
		}else if(o.equals(getCancelButton())){
			closeCancel();
		}

	}
	private JPanel jContentPanel = null;  //  @jve:decl-index=0:visual-constraint="22,19"
	private UIPanel butnPanel = null;
	private UIPanel inputPanel = null;
	private UILabel corpLabel = null;
	private UIRefPane corpRefPane = null;
//	private UILabel dateLabel = null;
//	private UIRefPane dateRefPane = null;
	private UILabel languageLabel = null;
	private UIComboBox langugaeCombox = null;
	private UIButton loginBtn = null;
	private UIButton cancelButton = null;
	//
	private LoginSessBean protoLSB = null;
	/**
	 *
	 */
//	public QuickLoginDlg() {
//		super();
//			initialize();
//	// TODO �Զ����ɹ��캯�����
//	}

	/**
	 * @param parent
	 */
//	public QuickLoginDlg(Container parent) {
//		super(parent);
//			initialize();
//	// TODO �Զ����ɹ��캯�����
//	}

	/**
	 * @param parent
	 * @param title
	 */
	public QuickLoginDlg(Container parent, String title, LoginSessBean lsb) {
		super(parent, title);
		protoLSB = lsb;
			initialize();
	// TODO �Զ����ɹ��캯�����
	}

	/**
	 * @param owner
	 */
//	public QuickLoginDlg(Frame owner) {
//		super(owner);
//			initialize();
//	// TODO �Զ����ɹ��캯�����
//	}

	/**
	 * @param owner
	 * @param title
	 */
//	public QuickLoginDlg(Frame owner, String title) {
//		super(owner, title);
//			initialize();
//	// TODO �Զ����ɹ��캯�����
//	}

	/**
	 * This method initializes this
	 *
	 * @return void
	 */
	private void initialize() {
        this.setContentPane(getJContentPanel());
        this.setSize(359, 249);

	}
	/**
	 * This method initializes jPanel
	 *
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPanel() {
		if (jContentPanel == null) {
			jContentPanel = new JPanel();
			jContentPanel.setLayout(new BorderLayout());
			jContentPanel.add(getButnPanel(), java.awt.BorderLayout.SOUTH);
			jContentPanel.add(getInputPanel(), java.awt.BorderLayout.CENTER);
		}
		return jContentPanel;
	}
	/**
	 * This method initializes UIPanel
	 *
	 * @return nc.ui.pub.beans.UIPanel
	 */
	private UIPanel getButnPanel() {
		if (butnPanel == null) {
			butnPanel = new UIPanel();
			butnPanel.setPreferredSize(new java.awt.Dimension(10,40));
			butnPanel.add(getLoginBtn(), null);
			butnPanel.add(getCancelButton(), null);

		}
		return butnPanel;
	}
	/**
	 * This method initializes UIPanel1
	 *
	 * @return nc.ui.pub.beans.UIPanel
	 */
	private UIPanel getInputPanel() {
		if (inputPanel == null) {
			corpLabel = new UILabel();

			//dateLabel = new UILabel();
			languageLabel = new UILabel();
			inputPanel = new UIPanel();
			inputPanel.setLayout(null);
			corpLabel.setBounds(62, 65, 52, 22);
			corpLabel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("smcomm","UC000-0000404")/*@res "��˾"*/);
			corpLabel.setToolTipText(nc.ui.ml.NCLangRes.getInstance().getStrByID("smcomm","UC000-0000404")/*@res "��˾"*/);
//			dateLabel.setBounds(62, 110, 52, 22);
//			dateLabel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("smcomm","UC000-0002313")/*@res "����"*/);
//			dateLabel.setToolTipText(nc.ui.ml.NCLangRes.getInstance().getStrByID("smcomm","UC000-0002313")/*@res "����"*/);
			languageLabel.setBounds(62, 110, 52, 22);
			languageLabel.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("smcomm","UPP1005-000194")/*@res "����"*/);
			languageLabel.setToolTipText(nc.ui.ml.NCLangRes.getInstance().getStrByID("smcomm","UPP1005-000194")/*@res "����"*/);
			
			inputPanel.add(corpLabel, null);
			inputPanel.add(getCorpRefPane(), null);
			//inputPanel.add(dateLabel, null);
			//inputPanel.add(getDateRefPane(), null);
			inputPanel.add(languageLabel, null);
			inputPanel.add(getLangugaeCombox(), null);
		}
		return inputPanel;
	}
	/**
	 * This method initializes refPane
	 *
	 * @return nc.ui.sm.power.RefPane
	 */
	private UIRefPane getCorpRefPane() {
		if (corpRefPane == null) {
			corpRefPane = new UIRefPane();
			corpRefPane.setRefNodeName("��˾Ŀ¼(����)");
			corpRefPane.setBounds(150, 65, 142, 22);
			nc.vo.bd.CorpVO[] corps = null;
			try{
				corps = SFServiceFacility.getLogin3Service().getValidateCorpByUserId(getProtoLSB().getDataSourceName(), getProtoLSB().getUserId());
			}catch(Exception e){
				e.printStackTrace();
			}
//			Vector v = new Vector();
			int count = corps == null ? 0 : corps.length;
            String[] pk_corps = new String[count];
			for (int i = 0; i < count; i++) {
                pk_corps[i] = corps[i].getPk_corp();
//				Vector vtemp = new Vector();
//				vtemp.add(corps[i].getUnitcode());
//				vtemp.add(corps[i].getUnitname());
//				vtemp.add(corps[i].getPk_corp());
//				vtemp.add(corps[i].getFathercorp());
//				v.add(vtemp);
			}
//			corpRefPane.getUITextField().setEditable(false);
            corpRefPane.getRefModel().setFilterPks(pk_corps);
//			corpRefPane.getRefModel().setData(v);
			corpRefPane.setPK(getProtoLSB().getPk_crop());
			corpRefPane.setEditable(true);
		}
		return corpRefPane;
	}
	/**
	 * This method initializes UIRefPane
	 *
	 * @return nc.ui.pub.beans.UIRefPane
	 */
//	private UIRefPane getDateRefPane() {
//		if (dateRefPane == null) {
////			dateRefPane = new UIRefPane();
//			dateRefPane = new UIRefPane(){
//					public void onButtonClicked() {
//						//���ӵ������ڴ��������ڲ���������
//						nc.ui.pub.beans.calendar.UICalendar calendar =
//							new nc.ui.pub.beans.calendar.UICalendar(this, getMinDateStr(), getMaxDateStr());
//						try {
//							
//							String datestr = getUITextField().getText();
//							if(!StringUtil.isEmpty(datestr))
//							{
//								UFDate date = new UFDate(datestr);
//								calendar.setNewdate(date);
//							}
//						} catch (ClassCastException e) {
//						}
//						m_iReturnButtonCode = calendar.showModal();
//						if (getReturnButtonCode() == nc.ui.pub.beans.UIDialog.ID_OK) {
//							isKeyPressed = false;
//							isButtonClicked = true;
//							setProcessFocusLost(false);
//							setText(calendar.getCalendarString());
//						}
//						calendar.destroy();
//					}
//				};
//				getDateRefPane().setText(getProtoLSB().getWorkDate());
//				dateRefPane.setBounds(150, 110, 142, 23);
//				dateRefPane.setRefNodeName("����");
//				dateRefPane.setName("Date");
////				dateRefPane.getUITextField().setBorder(BorderFactory.createMatteBorder(1,1,1,1,new java.awt.Color(132,132,132)));
////				dateRefPane.getUIButton().setBackground(new java.awt.Color(216,214,214));
//		}
//		return dateRefPane;
//	}
	/**
	 * This method initializes UIComboBox
	 *
	 * @return nc.ui.pub.beans.UIComboBox
	 */
	private UIComboBox getLangugaeCombox() {
		if (langugaeCombox == null) {
			langugaeCombox = new UIComboBox();
			langugaeCombox.setBounds(150, 110, 142, 22);
			langugaeCombox.setName("Language");
			langugaeCombox.setToolTipText(nc.ui.ml.NCLangRes.getInstance().getStrByID("smcomm","UPP1005-000195")/*@res "ѡ������"*/);
			java.awt.Component[] compChild = langugaeCombox.getComponents();
			for(int i=0;i<compChild.length;i++){
				if (compChild[i] instanceof JButton){
					compChild[i].addKeyListener(new java.awt.event.KeyAdapter(){
					   public void keyReleased(KeyEvent e) {
						   //����س���������һ�����
								  switch (e.getKeyChar()) {
									case KeyEvent.VK_ENTER :
										e.getComponent().transferFocus();
										e.consume();
										return;
									default :
										return;
								}
					   }
					});
					compChild[i].addFocusListener(new java.awt.event.FocusListener(){
						//��������б��ý���ʱ������ť����ɫ
						public void focusGained(java.awt.event.FocusEvent e) {
							Object o = UIManager.get("textHighlight");
							if (o instanceof Color) {
								Color oColor = (Color) o;
								e.getComponent().setBackground(oColor);
							}
						}
						public void focusLost(java.awt.event.FocusEvent e) {
								e.getComponent().setBackground(Color.white);
						}
					});
					compChild[i].setBackground(Color.white);
//					((JButton)compChild[i]).setBorder(BorderFactory.createLineBorder(new java.awt.Color(132,132,132)));
					break;
				}
			}
//			langugaeCombox.setFont(new java.awt.Font("����", 0, 12));
//			langugaeCombox.setBackground(new java.awt.Color(255,255,255));
			//
			nc.vo.ml.Language[] langTypes = NCLangRes.getInstance().getAllLanguages();//getUsedLang();
			for (int i = 0,n = langTypes == null ? 0 : langTypes.length; i < n; i++){
				getLangugaeCombox().addItem(langTypes[i]);
				if (langTypes[i].getCode().equals(getProtoLSB().getLanguage())) {
					getLangugaeCombox().setSelectedItem(langTypes[i]);
				}
			}
			//
		}
		return langugaeCombox;
	}
	/**
	 * This method initializes UIButton
	 *
	 * @return nc.ui.pub.beans.UIButton
	 */
	private UIButton getLoginBtn() {
		if (loginBtn == null) {
			loginBtn = new UIButton();
			loginBtn.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("smcomm","UPP1005-000196")/*@res "��¼"*/);
			HotkeyUtil.setHotkey(loginBtn,'L');/*@res "��¼"*/
			
			loginBtn.addActionListener(this);
		}
		return loginBtn;
	}
	/**
	 * This method initializes UIButton
	 *
	 * @return nc.ui.pub.beans.UIButton
	 */
	private UIButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new UIButton();
			HotkeyUtil.setHotkeyAndText(cancelButton,'C',nc.ui.ml.NCLangRes.getInstance().getStrByID(IProductCode.PRODUCTCODE_COMMON,"UC001-0000008"));/*@res "ȡ��"*/
			cancelButton.addActionListener(this);
		}
		return cancelButton;
	}


	public LoginSessBean getLoginSessBean(){
		LoginSessBean lsb = new LoginSessBean();
		//corp
		String corpCode = getCorpRefPane().getText().trim();
		String pk_corp = null;
		if (corpCode.equals("")) {
			pk_corp = "";
		} else {
			pk_corp = getCorpRefPane().getRefPK();
			if(pk_corp == null)
				pk_corp = "";
		}
		lsb.setPk_corp(pk_corp);
		lsb.setCorpCode(corpCode);
		//date
		//String workDate = getDateRefPane().getRefCode();
		//lsb.setWorkDate(workDate);
		//language
		nc.vo.ml.Language selLang = (nc.vo.ml.Language)getLangugaeCombox().getSelectedItem();
		String language = selLang.getCode();
		lsb.setLanguage(language);
		//other
		lsb.setUserCode(getProtoLSB().getUserCode());
		lsb.setPassword(getProtoLSB().getPassword());
		lsb.setAccountId(getProtoLSB().getAccountId());
		lsb.setDataSourceName(getProtoLSB().getDataSourceName());
		lsb.setUserScreenWidth(getProtoLSB().getUserScreenWidth());
		lsb.setIsUserResolutionValid(getProtoLSB().isUserResolutionValid());
		lsb.setUserScreenHeight(getProtoLSB().getUserScreenHeight());
		lsb.setIsLogin(getProtoLSB().isLogin());
		return lsb;
	}

	/**
	 * @return ���� protoLSB��
	 */
	private LoginSessBean getProtoLSB() {
		return protoLSB;
	}


}  //  @jve:decl-index=0:visual-constraint="10,10"