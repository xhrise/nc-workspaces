package nc.ui.eh.trade.z0200301;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultComboBoxModel;

import nc.ui.pub.beans.UIDialog;

/**
 * 牌价调整单
 * 
 * @author 王明 2008-03-24 下午04:03:18
 */
@SuppressWarnings("serial")
public class QueryDialog extends UIDialog implements ActionListener {
	// 执行按钮
	private nc.ui.pub.beans.UIButton UIButtonComm = null;
	private nc.ui.pub.beans.UIButton UIButtonCanel = null;
	private javax.swing.JPanel ivjUIDialogContentPane = null;
	private nc.ui.pub.beans.UILabel UILabel1 = null;
	private nc.ui.pub.beans.UILabel UILabel2 = null;
	private nc.ui.pub.beans.UILabel UILabel3 = null;
	private nc.ui.pub.beans.UIComboBox ComboBoxto = null;

	private String value = null;

	private nc.ui.pub.beans.UITextField chooseField = null;

	@SuppressWarnings("deprecation")
	public QueryDialog() {
		super();
		initialize();
	}

	// 按钮
	private nc.ui.pub.beans.UIButton getUIButtonComm() {
		if (UIButtonComm == null) {
			UIButtonComm = new nc.ui.pub.beans.UIButton();
			UIButtonComm.setName("UIButtonComm");
			UIButtonComm.setText("确定");
			UIButtonComm.setBounds(90, 110, 55, 22);

		}
		return UIButtonComm;
	}

	// 按钮
	private nc.ui.pub.beans.UIButton getUIButtonCanel() {
		if (UIButtonCanel == null)
			UIButtonCanel = new nc.ui.pub.beans.UIButton();
		UIButtonCanel.setName("UIButtonCanel");
		UIButtonCanel.setText("取消");
		UIButtonCanel.setBounds(170, 110, 55, 22);
		return UIButtonCanel;
	}

	// 初始话
	private void initialize() {
		setName("QueryDialog");
		this.setTitle("查询条件");
		setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
		// setSize(330, 210);
		setSize(330, 180);

		setContentPane(getUIDialogContentPane());

		getUIButtonComm().addActionListener(this);
		getUIButtonCanel().addActionListener(this);
	}

	// 增加按钮等工作
	private javax.swing.JPanel getUIDialogContentPane() {
		if (ivjUIDialogContentPane == null) {

			ivjUIDialogContentPane = new javax.swing.JPanel();
			ivjUIDialogContentPane.setName("UIDialogContentPane");
			ivjUIDialogContentPane.setLayout(null);
			getUIDialogContentPane().add(getUIButtonComm(),
					getUIButtonComm().getName());
			getUIDialogContentPane().add(getUIButtonCanel(),
					getUIButtonCanel().getName());
			getUIDialogContentPane()
					.add(getUILabel1(), getUILabel1().getName());
			getUIDialogContentPane()
					.add(getUILabel2(), getUILabel2().getName());
			getUIDialogContentPane()
					.add(getUILabel3(), getUILabel3().getName());
			getUIDialogContentPane().add(getComboBoxto(),
					getComboBoxto().getName());
			getUIDialogContentPane().add(getUITextField(),
					getUITextField().getName());
		}
		return ivjUIDialogContentPane;
	}

	private Component getUILabel1() {
		if (UILabel1 == null) {
			UILabel1 = new nc.ui.pub.beans.UILabel();
			UILabel1.setName("Label1");
			UILabel1.setText("查询条件：");
			UILabel1.setBounds(10, 15, 60, 20);
		}
		return UILabel1;
	}

	private Component getUILabel2() {
		if (UILabel2 == null) {
			UILabel2 = new nc.ui.pub.beans.UILabel();
			UILabel2.setName("Label2");
			UILabel2.setText("从第");
			UILabel2.setBounds(20, 55, 40, 20);
		}
		return UILabel2;
	}

	private Component getUILabel3() {
		if (UILabel3 == null) {
			UILabel3 = new nc.ui.pub.beans.UILabel();
			UILabel3.setName("Label3");
			UILabel3.setText("位开始的查询值为:");
			UILabel3.setBounds(110, 55, 100, 20);
		}
		return UILabel3;
	}

	private Component getComboBoxto() {
		if (ComboBoxto == null) {
			ComboBoxto = new nc.ui.pub.beans.UIComboBox();
			DefaultComboBoxModel invalue = new DefaultComboBoxModel(
					//new String[] { "1", "2", "3", "4", "5", "6", "7", "8" });
			new String[] { "1", "2", "3", "4", "5", "6", "7", "8" ,"9","10","11","12"});
			ComboBoxto.setModel(invalue);
			ComboBoxto.setName("ComboBoxto");
			ComboBoxto.setBounds(60, 55, 40, 20);
		}
		return ComboBoxto;
	}

	private Component getUITextField() {
		if (chooseField == null) {
			chooseField = new nc.ui.pub.beans.UITextField();
			chooseField.setName("chooseField");
			chooseField.setBounds(220, 55, 80, 20);
		}
		return chooseField;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getUIButtonComm()) {
			value = UIButtonComm.getText();
			getPriceVo();
			this.closeOK();
		}
		if (e.getSource() == getUIButtonCanel()) {
			value = UIButtonCanel.getText();
			this.closeOK();

		}
	}

	public String[] getPriceVo() {
		String[] values = new String[2];
		String ComboBoxt = ComboBoxto.getSelectdItemValue() == null ? ""
				: ComboBoxto.getSelectdItemValue().toString();
		String chooseF = chooseField.getText();
		values[0] = ComboBoxt;
		values[1] = chooseF;
		return values;
	}

	public String getOkCanel() {
		return value;
	}
}
