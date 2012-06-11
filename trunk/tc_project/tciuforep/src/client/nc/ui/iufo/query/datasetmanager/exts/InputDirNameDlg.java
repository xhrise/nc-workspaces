package nc.ui.iufo.query.datasetmanager.exts;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.WindowConstants;

import nc.pub.iufo.exception.UFOSrvException;
import nc.ui.iufo.datasetmanager.DataSetDefBO_Client;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UITextField;
import nc.vo.iufo.datasetmanager.DataSetDirVO;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.exception.MessageException;
import com.ufsoft.iufo.resource.StringResource;

public class InputDirNameDlg extends UIDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DataSetDirVO dirVO;

	private UITextField txtCode;

	private UITextField txtName;

	private String pk_parent_dir;

	public String getDirName() {
		return txtName.getText();
	}

	public void setDirName(String name) {
		this.txtName.setText(name);
	}
	
	public String getRemark(){
		return txtCode.getText();
	}
	
	public void setRemark(String remark){
		this.txtCode.setText(remark);
	}

//	public String getCode() {
//		return txtCode.getText();
//	}
//
//	public void setCode(String code) {
//		this.txtCode.setText(code);
//	}

	/**
	 * Create the dialog
	 * @i18n miufo00749=����
	 * @i18n miufo00750=Ŀ¼���ƣ�
	 * @i18n miufo1000758=ȷ��
	 * @i18n miufo1000757=ȡ��
	 * @i18n uiufo20236=��ע��
	 */
	public InputDirNameDlg(Container parent, String pk_parent_dir, DataSetDirVO dirVO) {
		super(parent);
		this.dirVO = dirVO;
		this.pk_parent_dir = pk_parent_dir;
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setModal(true);
		setTitle(StringResource.getStringResource("miufo00749"));
		getContentPane().setLayout(null);
		setBounds(100, 100, 384, 169);

		final UILabel label = new UILabel();
		label.setText(StringResource.getStringResource("miufo00750"));
		label.setBounds(26, 24, 66, 18);
		getContentPane().add(label);

		txtName = new UITextField();
		txtName.setBounds(98, 22, 253, 22);
		getContentPane().add(txtName);

		final UIButton buttonOK = new UIButton();

		final UIButton buttonCancel = new UIButton();

		buttonOK.setText(StringResource.getStringResource("miufo1000758"));
		buttonOK.setBounds(98, 98, 85, 22);
		getContentPane().add(buttonOK);

		buttonCancel.setText(StringResource.getStringResource("miufo1000757"));
		buttonCancel.setBounds(205, 98, 85, 22);
		getContentPane().add(buttonCancel);

		final UILabel label_1 = new UILabel();
		label_1.setText(StringResource.getStringResource("uiufo20236"));
		label_1.setBounds(26, 61, 66, 18);
		getContentPane().add(label_1);

		txtCode = new UITextField();
		txtCode.setBounds(98, 59, 253, 22);
		getContentPane().add(txtCode);

		buttonOK.addActionListener(new ActionListener() {
			/**
			 * @i18n miufo1000155=����
			 * @i18n miufo00751=Ŀ¼���Ʋ���Ϊ��
			 * @i18n miufo00752=Ŀ¼���Ʋ����ظ�
			 */
			public void actionPerformed(final ActionEvent arg0) {

				try {
					if (getDirName().trim().equals(""))
					{
						MessageDialog.showErrorDlg(InputDirNameDlg.this
								.getParent(), StringResource
										.getStringResource("miufo1000155"), StringResource.getStringResource("miufo00751"));
						return;
					}
//					if (getCode().trim().equals(""))
//					{
//						MessageDialog.showErrorDlg(InputDirNameDlg.this
//								.getParent(), "����", "Ŀ¼���벻��Ϊ��");
//						return;
//					}
					
					DataSetDirVO dirName = DataSetDefBO_Client.loadDirByName(
							InputDirNameDlg.this.pk_parent_dir, getDirName());
					if (dirName == null  // û�ҵ�ͬ��Ŀ¼
							||
							((dirName != null) && // �ҵ�ͬ��Ŀ¼
							(InputDirNameDlg.this.dirVO != null &&  
									InputDirNameDlg.this.dirVO.getPk_datasetdir().
									equals(dirName.getPk_datasetdir()))))// ���Һ͵�ǰĿ¼��PK��ͬ
					{
						//closeOK();
					} else {
						MessageDialog.showErrorDlg(InputDirNameDlg.this
								.getParent(), StringResource
										.getStringResource("miufo1000155"), StringResource.getStringResource("miufo00752"));
						return;
					}
					
//					DataSetDirVO dirCode = DataSetDefBO_Client.loadDirByCode(getCode());
//					if (dirCode == null  // û�ҵ�ͬ��Ŀ¼
//							||
//							((dirCode != null) && // �ҵ�ͬ��Ŀ¼
//							(InputDirNameDlg.this.dirVO != null && InputDirNameDlg.this.dirVO
//									.getPk_datasetdir().equals(dirCode.getPk_datasetdir()))))// ���Һ͵�ǰĿ¼��PK��ͬ
//					{
//						closeOK();
//					} else {
//						MessageDialog.showErrorDlg(InputDirNameDlg.this
//								.getParent(), "����", "Ŀ¼���벻���ظ�");
//					}
					closeOK();
			

				} catch (UFOSrvException e) {
					AppDebug.debug(e);
					throw new MessageException(e.getMessage());
				}
			}
		});
		buttonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent arg0) {
				closeCancel();
			}
		});
	}

}
