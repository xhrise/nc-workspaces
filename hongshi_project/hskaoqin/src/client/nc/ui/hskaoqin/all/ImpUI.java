package nc.ui.hskaoqin.all;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.hskaoqin.all.IKaoQin;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ToftPanel;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.RefEditEvent;
import nc.ui.pub.beans.RefEditListener;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.calendar.UICalendar;
import nc.ui.pub.tools.BannerDialog;
import nc.ui.sm.login.NCAppletStub;
import nc.vo.pfxx.pub.PfxxVocabulary;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDateTime;

public class ImpUI extends ToftPanel implements PfxxVocabulary,
		ActionListener, ListSelectionListener {
	private JLabel lbl_predate = null;
	private UIRefPane ivjDate_p = null;
	private JLabel lbl_aftdate = null;
	private UIRefPane ivjDate_a = null;	
	private JLabel lbl_corp = null;
	private JTextField txt_corp = null;
	
	private JButton btn_Imp = null;

	JScrollPane scrollPane = null;

	UIPanel pnlCenter = null;

	UIPanel pnlBottom = null;

	public ImpUI() {
		super();
		
		initialize();
	}
	
	/**
	 * 时间参照-开始
	 * 
	 * @return
	 */
	private UIRefPane getPreDate() {
		if (this.ivjDate_p == null) {
			try {
				this.ivjDate_p = new UIRefPane() {
					@Override
					public void onButtonClicked() {
						UICalendar calendar = new UICalendar(this,
								getMinDateStr(), getMaxDateStr());
						try {
							String strServerTime = NCAppletStub.getInstance()
									.getParameter("SERVERTIME");

							UFDateTime datetime = new UFDateTime(strServerTime);
							UFDate date = new UFDate(datetime.getDate()
									.toString());

							calendar.setNewdate(date);
						} catch (ClassCastException e) {
						}
						this.m_iReturnButtonCode = calendar.showModal();
						if (getReturnButtonCode() == 1) {
							this.isKeyPressed = false;
							this.isButtonClicked = true;
							setProcessFocusLost(false);
							setText(calendar.getCalendarString());
						}
						calendar.destroy();
					}
				};
				this.ivjDate_p.getUITextField().getInputMap().put(
						KeyStroke.getKeyStroke(10, 0), "enter");

				this.ivjDate_p.getUITextField().getActionMap().put("enter",
						new AbstractAction() {
							public void actionPerformed(ActionEvent e) {
								// XlsReadUI.this.getUser().requestFocus();
							}
						});
				this.ivjDate_p.setRefNodeName("日历");
				this.ivjDate_p.setName("Date");
				this.ivjDate_p.setBounds(165, 20, 95, 22);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjDate_p;
	}
	
	/**
	 * 时间参照-结束
	 * 
	 * @return
	 */
	private UIRefPane getAftDate() {
		if (this.ivjDate_a == null) {
			try {
				this.ivjDate_a = new UIRefPane() {
					@Override
					public void onButtonClicked() {
						UICalendar calendar = new UICalendar(this,
								getMinDateStr(), getMaxDateStr());
						try {
							String strServerTime = NCAppletStub.getInstance()
									.getParameter("SERVERTIME");

							UFDateTime datetime = new UFDateTime(strServerTime);
							UFDate date = new UFDate(datetime.getDate()
									.toString());

							calendar.setNewdate(date);
						} catch (ClassCastException e) {
						}
						this.m_iReturnButtonCode = calendar.showModal();
						if (getReturnButtonCode() == 1) {
							this.isKeyPressed = false;
							this.isButtonClicked = true;
							setProcessFocusLost(false);
							setText(calendar.getCalendarString());
						}
						calendar.destroy();
					}
				};
				this.ivjDate_a.getUITextField().getInputMap().put(
						KeyStroke.getKeyStroke(10, 0), "enter");

				this.ivjDate_a.getUITextField().getActionMap().put("enter",
						new AbstractAction() {
							public void actionPerformed(ActionEvent e) {
								// XlsReadUI.this.getUser().requestFocus();
							}
						});
				this.ivjDate_a.setRefNodeName("日历");
				this.ivjDate_a.setName("Date");
				this.ivjDate_a.setBounds(335, 20, 95, 22);
			} catch (Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return this.ivjDate_a;
	}

	/**
	 * 每当部件抛出异常时被调用
	 */
	private void handleException(Throwable e) {
		Logger.error("", e);
	}

	// 加载界面
	private void initialize() {
		String strServerTime = NCAppletStub.getInstance().getParameter(
				"SERVERTIME");

		UFDateTime datetime = new UFDateTime(strServerTime);

		getPreDate().setText(datetime.getDate().toString());
		getAftDate().setText(datetime.getDate().toString());
		setSize(774, 200);
		add(getPnlCenter(), null);
	}

	public UIPanel getPnlCenter() {

		if (pnlCenter == null) {
			pnlCenter = new UIPanel();
			pnlCenter.setBorder(new EtchedBorder());
			pnlCenter.setLayout(null);
			
			lbl_predate = new JLabel();
			lbl_predate.setText("开始日期");
			lbl_predate.setBounds(110, 20, 50, 22);
			pnlCenter.add(lbl_predate, null);

			pnlCenter.add(getPreDate(), null);
			
			lbl_aftdate = new JLabel();
			lbl_aftdate.setText("结束日期");
			lbl_aftdate.setBounds(280, 20, 50, 22);
			pnlCenter.add(lbl_aftdate, null);

			pnlCenter.add(getAftDate(), null);

			
			lbl_corp = new JLabel();
			lbl_corp.setText("导入公司");
			lbl_corp.setBounds(350, 20, 50, 22);
//			pnlCenter.add(lbl_corp, null);
			
//			IKaoQin kq = (IKaoQin)NCLocator.getInstance().lookup(IKaoQin.class.getName());
//			List<String> units = null;
//			try {
//				units = kq.getUnits(this.getClientEnvironment().getConfigAccount().getDataSourceName());
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			
//			UIComboBox combo = new UIComboBox(units.toArray());
//			combo.setBounds(400, 20, 200, 22);
			
			txt_corp = new JTextField();
			txt_corp.setText("3101浙江红狮水泥股份有限公司");
			txt_corp.setBounds(400, 20, 200, 22);
			txt_corp.setEditable(false);
//			pnlCenter.add(txt_corp, null);

			
			pnlCenter.add(getBtn_Imp(), null);
		}
		return pnlCenter;
	}

	/**
	 * 开始导入
	 * @return
	 */
	public JButton getBtn_Imp() {
		
		if (btn_Imp == null) {
			btn_Imp = new JButton();
			btn_Imp.setBounds(300, 50, 72, 21);
			btn_Imp.setText("开始导入");
			btn_Imp.addActionListener(new java.awt.event.ActionListener() {
				public void actionPerformed(java.awt.event.ActionEvent e) {
					
					final String sourceName = getClientEnvironment().getCorporation().getUnitcode();
					
					if ("".equals(ivjDate_p.getText())) {
						showErrorMessage("开始日期不能为空!\n\t");
						return;
					}
					if ("".equals(ivjDate_a.getText())) {
						showErrorMessage("结束日期不能为空!\n\t");
						return;
					}
					// 该操作是耗时操作，所以启用系统运行提示框
					Runnable runImpDoc = new Runnable() {
						public void run() {
							String result = "";
							BannerDialog dialog = new BannerDialog(
									getPnlCenter());
							dialog.start();
							try {
								IKaoQin kaoqin = (IKaoQin) NCLocator.getInstance().lookup(
										IKaoQin.class.getName());
								result=kaoqin.impkaoqindata(ivjDate_p.getText().substring(2, 10), ivjDate_a.getText().substring(2, 10) , sourceName );
							} catch (Exception e) {
								dialog.end();
								e.printStackTrace();
								return;
							} finally {
								result = result.replace("\0", "");
								MessageDialog.showHintDlg(null, "导入提示", result);
							}
							dialog.end();// 销毁系统运行提示框
						}
					};
					new Thread(runImpDoc).start();
				}
			});
		}
		return btn_Imp;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onButtonClicked(ButtonObject arg0) {
		// TODO Auto-generated method stub

	}

	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub

	}

}
