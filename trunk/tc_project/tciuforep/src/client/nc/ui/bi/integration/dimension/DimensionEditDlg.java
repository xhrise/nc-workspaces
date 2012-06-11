/*
 * �������� 2006-2-13
 *
 */
package nc.ui.bi.integration.dimension;

import nc.pub.iufo.exception.CommonException;
import nc.ui.iufo.resmng.uitemplate.ResEditObjUI;

import com.ufida.web.WebException;
import com.ufida.web.comp.Area;
import com.ufida.web.comp.WebLabel;
import com.ufida.web.comp.WebTextField;
import com.ufida.web.container.WebGridLayout;
import com.ufida.web.container.WebPanel;
import com.ufida.web.html.Div;
import com.ufsoft.iufo.resource.StringResource;

/**
 * �½����޸�ά�ȵĽ�����
 * 
 */
public class DimensionEditDlg extends ResEditObjUI {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2233407045478145004L;

	/**
	 * @i18n mbidim00045=ά�����
	 */
	public final static String TITLE = StringResource.getStringResource("mbidim00045");

	// ά����Ϣ
	/**
	 * @i18n miufo1003134=����
	 */
	private final static String LBL_DIMNAME = StringResource.getStringResource("miufo1003134");

	/**
	 * @i18n miufo1001012=����
	 */
	private final static String LBL_DIMCODE = StringResource.getStringResource("miufo1001012");

	/**
	 * @i18n miufo1001390=����
	 */
	public final static String LBL_DIMTYPE = StringResource.getStringResource("miufo1001390");

	/**
	 * @i18n miufo1003136=˵��
	 */
	private final static String LBL_NOTE = StringResource.getStringResource("miufo1003136");

	// private final static String LBL_TABLENAME = "ά��";

	// ������ʽ
	/**
	 * @i18n mbidim00046=������ʽ
	 */
	public final static String LBL_CREATE_MODE = StringResource.getStringResource("mbidim00046");

	/**
	 * @i18n miufopublic242=�½�
	 */
	public final static String RD_NEW = StringResource.getStringResource("miufopublic242");

	/**
	 * @i18n miufo1001490=����
	 */
	public final static String RD_REFER = StringResource.getStringResource("miufo1001490");

	public final static String RD_NEW_VALUE = "0";

	public final static String RD_REFER_VALUE = "1";

	public final static String RD_COPY_VALUE = "2";

	// ��Ϣ���
	private WebPanel m_subPanel = null;

	private WebLabel[] lbls = null;

	// ά������
	private WebTextField m_tfName = null;

	// ά�ȱ���
	private WebTextField m_tfCode = null;

	// // ά��
	// private WebHiddenField m_tfTableName = null;
//
//	// ά������
//	private WebChoice m_cmbType = null;

	// ά��˵��
	private WebTextField m_tfNote = null;

//	// ������ʽ���
//	private WebPanel m_createPanel = null;
//
//	private WebLabel lbl_choice = null;
//
//	// ������ʽѡ��
//	private WebRadioButton m_rdCreateType = new WebRadioButton();
//
//	// ����/���Ƶ�ά��ѡ��
//	private WebChoice m_cmbRefer = new WebChoice();

	protected WebPanel getFieldPane() {
		if (fieldPane == null) {
			fieldPane = new WebPanel();
			fieldPane.setLayout(new WebGridLayout(8, 2));
			// ��ʼ�������
			addComHead(fieldPane);
			// ����ģ��������
			fieldPane.add(getDimensionContentPanel(), new Area(4, 1, 1, 4));

		}
		return fieldPane;
	}
	protected boolean isNeedResetBtn(){
        return false;
    }

	/**
	 * ����ҳ������ ÿ��ҳ��ˢ�¾����� liulp 2005-12-06
	 */
	protected void setData() throws WebException {
		String[] lblNames = new String[] { LBL_DIMCODE, LBL_DIMNAME, LBL_NOTE }; // ά�����ơ����롢���͡�˵��

		for (int i = 0; i < lblNames.length; i++) {
			lbls[i].setValue(lblNames[i]);
		}

//		m_cmbType.setItems(new String[][] {
//				new String[] { String.valueOf(IDimension.STANDARD_DIMENSION_TYPE), DimRescource.DIM_COMM },
//				new String[] { String.valueOf(IDimension.TIME_DIMENSION_TYPE), DimRescource.DIM_TIME } });


		DimensionForm dimForm = getDimForm();
		if (dimForm == null) {
			return;
		}

		// #���๫����ֵ
		doSetData(dimForm);

		m_tfName.setValue(dimForm.getDimName());
		m_tfCode.setValue(dimForm.getDimCode());
//		m_cmbType.setValue(dimForm.getDimType());
		m_tfNote.setValue(dimForm.getDimNote());

		Div div = new Div();
		div.setID("DimCreateTypePanelDiv");
		div.setName("DimCreateTypePanelDiv");
		div.addElement(dimForm.getCreateTypePanel());
		getDimensionContentPanel().add(div, new Area(6, 1, 3, 1));		
		if(dimForm.isModifyUI() == false){
		//	this.getDocument().appendOnLoad("onDimCreateTypeChanged();");
		}
		
//		// �޸�״̬��ʱ�Ȳ���ʾ�������ͺ�����ά��
//		if (dimForm.getDimCode() == null) {// TODO �Դ����ж����������Ǳ��棬δ��׼ȷ
//			getDimensionContentPanel().add(getCreatePanel(), new Area(6, 1, 3, 1));
//			
//			lbl_choice.setValue(LBL_CREATE_MODE);
//			m_rdCreateType.setItems(new String[][] { new String[] { RD_NEW_VALUE, RD_NEW },
//					new String[] { RD_REFER_VALUE, RD_REFER } // , new String[] {
//					// RD_COPY_VALUE,
//					// RD_COPY }
//					});
//
//			m_rdCreateType.setValue(dimForm.getCreateType() == null ? RD_NEW_VALUE : dimForm.getCreateType());
//
//			DimensionVO[] refDims = (DimensionVO[]) DimensionSrv.getReferencable();
//			int len = refDims == null ? 0 : refDims.length;
//			String[][] items = new String[len + 1][];
//			items[0] = new String[] { " ", " " };
//			for (int i = 0; i < len; i++) {
//				items[i + 1] = new String[] { refDims[i].getDimID(), refDims[i].getDimname() };
//			}
//			m_cmbRefer.setItems(items);
//		}
		
		super.setData();
		
		setWindowWidth(400);
        setWindowHeight(400);	
        this.disableAutoResize();
	}

	 protected void setWindowSize(){    	
	    }
	 
	protected WebPanel getDimensionContentPanel() {
		if (m_subPanel == null) {
			try {
				m_subPanel = new WebPanel();
				WebGridLayout subLayout = new WebGridLayout(8, 3);
				m_subPanel.setLayout(subLayout);

				lbls = new WebLabel[3];
				// ������ؼ�
				for (int i = 0; i < lbls.length; i++) {
					lbls[i] = new WebLabel();
					m_subPanel.add(lbls[i], new Area(i + 1, 1, 1, 1));
				}

				m_tfCode = new WebTextField();
				m_tfCode.setID("dimCode");
				m_tfCode.setName("dimCode");
				m_tfCode.setMaxlength(30);
				m_tfCode.setVld_NoNull(true);
				m_subPanel.add(m_tfCode, new Area(1, 2, 1, 1));

				m_tfName = new WebTextField();
				m_tfName.setID("dimName");
				m_tfName.setName("dimName");
				m_tfName.setMaxlength(62);
				m_tfName.setVld_NoNull(true);
				m_subPanel.add(m_tfName, new Area(2, 2, 1, 1));
				
//				m_cmbType = new WebChoice();
//				m_cmbType.setID("dimType");
//				m_cmbType.setName("dimType");
//				m_cmbType.setOnChange("onDimTypetChanged();");
				
//				m_subPanel.add(m_cmbType, new Area(3, 2, 1, 1));
				m_tfNote = new WebTextField();
				m_tfNote.setID("dimNote");
				m_tfNote.setName("dimNote");
				m_tfNote.setMaxlength(128);
				//m_tfNote.setRows(4);
				m_subPanel.add(m_tfNote, new Area(3, 2, 1, 1));

			//	m_subPanel.add(new BR(), new Area(4, 1, 1, 1));

				return m_subPanel;
			} catch (CommonException e) {
				throw e;
			} catch (Exception e) {
				throw new CommonException("miufo10000"); // δ����Ĵ���
			}
		}
		return m_subPanel;
	}

//	private WebPanel getCreatePanel() {
//		if (m_createPanel == null) {
//
//			m_createPanel = new WebPanel();
//			WebGridLayout subLayout = new WebGridLayout(3, 3);
//			m_createPanel.setLayout(subLayout);
//
//			lbl_choice = new WebLabel();
//			m_createPanel.add(lbl_choice, new Area(1, 1, 2, 1));
//
//			m_rdCreateType = new WebRadioButton();
//			m_rdCreateType.setID("createType");
//			m_rdCreateType.setName("createType");
//			m_createPanel.add(m_rdCreateType, new Area(2, 2, 2, 1));
//
//			m_cmbRefer = new WebChoice();
//			m_cmbRefer.setID("refDimID");
//			m_cmbRefer.setName("refDimID");
//			m_createPanel.add(m_cmbRefer, new Area(3, 2, 1, 1));
//
//		}
//		return m_createPanel;
//	}

	/**
	 * ������������Form����
	 * 
	 * @return
	 */
	private DimensionForm getDimForm() {
		DimensionForm dimForm = (DimensionForm) getActionForm(DimensionForm.class.getName());
		return dimForm;
	}
}
 