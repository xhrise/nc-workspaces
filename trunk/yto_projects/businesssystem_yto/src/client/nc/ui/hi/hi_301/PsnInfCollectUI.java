package nc.ui.hi.hi_301;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.bs.uap.lock.PKLock;
import nc.bs.util.SleepTime;
import nc.bs.util.Uriread;
import nc.itf.hi.HIDelegator;
import nc.itf.hr.bd.ICorpWorkout;
import nc.itf.hr.comp.IAttachment;
import nc.itf.hr.pub.PubDelegator;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.bd.def.IDefdoc;
import nc.itf.uap.bd.psn.IPsncl;
import nc.itf.yto.util.IFilePost;
import nc.itf.yto.util.IGener;
import nc.itf.yto.util.IReadmsg;
import nc.jdbc.framework.processor.BeanListProcessor;
import nc.ui.bd.ref.AbstractRefModel;
import nc.ui.bd.ref.DefaultRefTreeModel;
import nc.ui.bd.ref.busi.PsndocDefaulRefModel;
import nc.ui.hi.hi_301.ref.PsnClsRef;
import nc.ui.hi.hi_301.trigger.IAccessible;
import nc.ui.hi.hi_301.trigger.ITriggerListener;
import nc.ui.hi.hi_301.trigger.TriggerEvent;
import nc.ui.hi.hi_301.trigger.TriggerTable;
import nc.ui.hi.hi_302.BatchCancelAffirmDlg;
import nc.ui.hi.hi_302.UIFileChooserForOutport;
import nc.ui.hi.hi_303.CardBrowseMainPanel;
import nc.ui.hi.hi_303.CardReportViewFrame;
import nc.ui.hi.hi_306.DocLinkAddData;
import nc.ui.hi.ref.DeptRefModel;
import nc.ui.hi.ref.DutyRef;
import nc.ui.hi.ref.JobRef;
import nc.ui.hi.ref.SpaDeptdocRefGridTreeModel;
import nc.ui.hi.ref.SpaDeptdocRefTreeModel;
import nc.ui.hr.comp.attachment.AttachmentDialog;
import nc.ui.hr.comp.combinesort.Attribute;
import nc.ui.hr.comp.combinesort.SortConfigDialog;
import nc.ui.hr.comp.quicksearch.IQuickSearch;
import nc.ui.hr.comp.quicksearch.QsbUtil;
import nc.ui.hr.comp.quicksearch.SearchType;
import nc.ui.hr.frame.HrQueryDialog;
import nc.ui.hr.frame.IQueryFieldValueEditor;
import nc.ui.hr.global.Global;
import nc.ui.hr.global.GlobalTool;
import nc.ui.hr.plugins.PluginsUtil;
import nc.ui.hr.pub.carddef.HrCardReportViewPanel;
import nc.ui.hr.pub.carddef.word.RefDataTypeHelper;
import nc.ui.hr.pub.carddef.word.WordUtil;
import nc.ui.hr.tools.trans.FileTransClient;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.ClientEnvironment;
import nc.ui.pub.IFuncWindow;
import nc.ui.pub.MenuButton;
import nc.ui.pub.MenuCheckboxItem;
import nc.ui.pub.beans.ExtTabbedPane;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIDialog;
import nc.ui.pub.beans.UIFileChooser;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIPopupMenu;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UISplitPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITextArea;
import nc.ui.pub.beans.UITextField;
import nc.ui.pub.beans.UITree;
import nc.ui.pub.beans.ValueChangedEvent;
import nc.ui.pub.beans.ValueChangedListener;
import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.ui.pub.beans.constenum.IConstEnum;
import nc.ui.pub.bill.BillCardLayout;
import nc.ui.pub.bill.BillData;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillEditListener;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.BillModel;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.pub.bill.BillScrollPane;
import nc.ui.pub.bill.BillTabbedPane;
import nc.ui.pub.bill.BillTableMouseListener;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pub.linkoperate.ILinkMaintain;
import nc.ui.pub.linkoperate.ILinkMaintainData;
import nc.ui.pub.print.IDataSource;
import nc.ui.pub.print.PrintEntry;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.pub.tools.BannerDialog;
import nc.ui.querytemplate.CriteriaChangedEvent;
import nc.ui.querytemplate.ICriteriaChangedListener;
import nc.ui.querytemplate.IQueryTemplateTotalVOProcessor;
import nc.ui.querytemplate.QueryTempletService;
import nc.ui.querytemplate.filtereditor.DefaultFilterEditor;
import nc.ui.querytemplate.meta.FilterMeta;
import nc.ui.querytemplate.valueeditor.DefaultFieldValueEditor;
import nc.ui.sm.login.ClientAssistant;
import nc.ui.uap.sf.SFClientUtil;
import nc.vo.bd.def.DefdocVO;
import nc.vo.bd.psndoc.PsndocConsItmVO;
import nc.vo.hi.hi_301.CtrlDeptVO;
import nc.vo.hi.hi_301.GeneralVO;
import nc.vo.hi.hi_301.PersonEAVO;
import nc.vo.hi.hi_301.SubTable;
import nc.vo.hi.hi_301.ValidException;
import nc.vo.hi.hi_301.validator.AbstractValidator;
import nc.vo.hi.hi_301.validator.FieldValidator;
import nc.vo.hi.pub.CommonValue;
import nc.vo.hr.bd.setdict.FlddictVO;
import nc.vo.hr.pub.carddef.HrReportDataVO;
import nc.vo.hr.pub.carddef.RptAuthVO;
import nc.vo.hr.pub.carddef.RptDefVO;
import nc.vo.hr.pub.carddef.word.WordVO;
import nc.vo.hr.tools.pub.StringUtils;
import nc.vo.hr.validate.IDValidateUtil;
import nc.vo.om.om_013.WorkoutResultVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.ValidationException;
import nc.vo.pub.bill.BillTempletBodyVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.msg.UserNameObject;
import nc.vo.pub.query.ConditionVO;
import nc.vo.pub.query.QueryConditionVO;
import nc.vo.pub.query.QueryTempletTotalVO;
import nc.vo.querytemplate.TemplateInfo;
import nc.vo.yto.business.OperationMsg;
import nc.vo.yto.business.PsnbasdocVO;
import nc.vo.yto.business.PsndocVO;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.ufsoft.report.ContextVO;
import com.ufsoft.report.sysplugin.excel.ExcelExpUtil;
import com.ufsoft.table.CellsModel;
/**
 * ��Ա�ɼ�����
 * 
 * @author ZHANGYAN
 * 
 */
public class PsnInfCollectUI extends nc.ui.hr.base.HRToftPanel implements ListSelectionListener, 
IDataSource,TableModelListener ,BillEditListener,IQueryFieldValueEditor,ILinkMaintain,IQuickSearch{
	private boolean isBeginEdit = false;
	protected final int SPLIT_MAX = 1000;
	protected final int SPLIT_STANDARD = 200;
	protected boolean codeflag = true; 
	public HrQueryDialog queryDialog = null;
	private UFBoolean isNeedAFirm = null;
	// magic number!!
	private final static String PSNCODE_AUTO_GENERATE = "1";
	// ֻҪ������༭��ť.
	protected boolean isUpdated = false;
	protected Hashtable htbEdit = new Hashtable();
	protected Hashtable htbRegularEdit = new Hashtable();
	// ��Ա������Χѡ��,������Ҫ������ʵ��������ѡ�������     
	public class Item {
		// �ù�����Χ�����ͣ���Ӧ�����ݿ��е�bd_psndoc.psnscope
		private int value;
		// ��ʾ����
		private String name;
		// �ù�����Χ��Ӧ��ģ���nodekey
		private String nodeKey;
		// ��ģ���beanName
		private String cardName;
		// �ķ�Χ��ģ���Ƿ��Ѿ����أ�
		private boolean templetLoaded;
		public void setCardName(String cn) {
			cardName = cn;
		}
		public String getCardName() {
			return cardName;
		}
		public void setTempletLoaded(boolean b) {
			templetLoaded = b;
		}
		public boolean isTempletLoaded() {
			return templetLoaded;
		}
		public void setNodeKey(String key) {
			nodeKey = key;
		}
		public String getNodeKey() {
			return nodeKey;
		}
		public void setValue(int v) {
			value = v;
		}
		public int getValue() {
			return value;
		}
		public void setName(String n) {
			name = n;
		}
		public String getName() {
			return name;
		}		
		public Item() {
		}
		public Item(int v, String key, String cn, String n) {
			setValue(v);
			setNodeKey(key);
			setCardName(cn);
			setName(n);
		}
		public String toString() {
			// ���ǹ��ʻ�
			return name;
		}
	}

	// ��������ʵ�ִ�����Ŀ�����Ĵ���
	class AccessibleItem implements IAccessible {
		private BillItem destItem;
		private PsnInfCollectUI mainUI;
		public AccessibleItem(BillItem billItem, PsnInfCollectUI ui) {
			destItem = billItem;
			mainUI = ui;
		}
		public BillItem getBillItem() {
			return destItem;
		}
		public BillItem getBillItem(String tablefield) {
			return mainUI.getBillItem(tablefield);
		}
		public String getSelCorpPK() {
			return mainUI.getSelCorpPK();
		}
		private void setComponentValue(Object value) {
			// �����������
			switch (destItem.getDataType()) {
			// �ַ���������С����ʱ�䶼ʹ�ò��յ�textfield
			case IBillItem.STRING: // �ַ�
			case IBillItem.INTEGER: // ����
			case IBillItem.DECIMAL: // С��
			case IBillItem.TIME: // ʱ��
			case IBillItem.DATE: // ����
				UIRefPane ref = (UIRefPane) getBillItem().getComponent();
				UITextField text = ref.getUITextField();
				text.setText(value == null ? "" : value.toString());
				break;
				// ���ա��Զ����������ʹ�ò���
			case IBillItem.UFREF: // ����
			case IBillItem.USERDEF: // �Զ������
				ref = (UIRefPane) getBillItem().getComponent();
				ref.setPK(value);
				break;
				// �߼���ʹ�ø�ѡ��
			case IBillItem.BOOLEAN: // �߼�
				UICheckBox checkBox = (UICheckBox) getBillItem().getComponent();
				checkBox.setSelected(value == null ? false : ((Boolean) value)
						.booleanValue());
				break;
				// ����������
			case IBillItem.COMBO: // ����
				UIComboBox comboBox = (UIComboBox) getBillItem().getComponent();
				comboBox.setSelectedItem(value);
				break;
				// ���ı���ͼƬ���������
			case IBillItem.TEXTAREA: // ���ı�
				UITextArea area = (UITextArea) getBillItem().getComponent();
				area.setText(value == null ? "" : value.toString());
				break;
			case IBillItem.IMAGE: // ͼƬ
			case IBillItem.OBJECT: // ͼƬ
				break;
			}
		}
		private void setTableValue(Object value) {
			BillModel model = getCard().getBillModel();
			int row = getEditLineNo();
			model.setValueAt(value, row, destItem.getKey());
		}
		public void setValue(Object value) {
			if (destItem.getPos() == IBillItem.HEAD) {
				setComponentValue(value);
			} else {
				setTableValue(value);
			}
		}
	}
	
	/**
	 * ���ѡ��˵�
	 * <br>Create Date:2010-4-20
	 * @author caizl
	 */
	 public class QuickMenuBar extends MenuButton{
		 
		private static final long serialVersionUID = -7632237146153839773L;
		
		private UIPopupMenu quickMenu = new UIPopupMenu();
		
		private List<MenuCheckboxItem> checkList = new ArrayList<MenuCheckboxItem>();
		
		public List<MenuCheckboxItem> getCheckList() {
			return checkList;
		}

		public void setCheckList(List<MenuCheckboxItem> checkList) {
			this.checkList = checkList;
		}

		public QuickMenuBar(){
			super("��");
			setPreferredSize(new Dimension(14,13));
			setBackground(new Color(236,244,251));
			setPopupMenu(quickMenu);
			initMenuItem();
			
			addMouseListener(new MouseAdapter(){
				public void mouseClicked(MouseEvent mouseevent)
	            {
					quickMenu.show(QuickMenuBar.this, 1, 13);
	            }
			});
		}
		
		private void initMenuItem() {
			IDefdoc iDefdoc = NCLocator.getInstance().lookup(IDefdoc.class);
			try {
				DefdocVO[] defdocVOs = iDefdoc.queryByWhere(" pk_defdoclist in (select pk_defdoclist from bd_defdoclist where doclistcode='HR254') order by pk_defdoc desc");
				MenuCheckboxItem menuCheckboxItem = null;
				if (defdocVOs != null && defdocVOs.length > 0) {
					for (DefdocVO defdocVO : defdocVOs) {
						menuCheckboxItem = new MenuCheckboxItem(defdocVO
								.getDocname());
						menuCheckboxItem
								.setActionCommand(defdocVO.getDoccode());
						menuCheckboxItem
								.addActionListener(new MenuActionListener(
										quickMenu, menuCheckboxItem));
						quickMenu.add(menuCheckboxItem);
						if (defdocVO.getDoccode().endsWith("psncode")
								|| defdocVO.getDoccode().endsWith("psnname")) {
							menuCheckboxItem.setSelected(true);
						}
						checkList.add(menuCheckboxItem);
					}
				}
			} catch (BusinessException e) {
				Logger.error(e.getMessage());
				e.printStackTrace();
			}
		}
		
	}
	 
	private class MenuActionListener implements ActionListener{

		private JPopupMenu jPopupMenu;
		
		private JCheckBoxMenuItem jCheckBoxMenuItem;
		
		public MenuActionListener(JPopupMenu jPopupMenu, JCheckBoxMenuItem jCheckBoxMenuItem){
			this.jPopupMenu = jPopupMenu;
			this.jCheckBoxMenuItem = jCheckBoxMenuItem;
		}
		
		public void actionPerformed(ActionEvent actionevent) {
			jPopupMenu.setVisible(true);
			//jCheckBoxMenuItem.setBorderPainted(true);
		}
		
	}

	private QuickMenuBar quickMenuBar = new QuickMenuBar();
	
	protected QuickMenuBar getQuickMenuBar() {
		return quickMenuBar;
	}
	// ��ǰ�༭���ݣ���
	private static final int EDIT_NONE = 0;
	// ��ǰ�༭����:����
	private static final int EDIT_MAIN = 1;
	// ��ǰ�༭����:�Ӽ�
	private static final int EDIT_SUB = 2;
	// ��ǰ�༭���ݣ�����˳��
	private static final int EDIT_SORT = 3;
	// V35 add ��Ա���õı༭״̬
	private static final int EDIT_REF = 4;
	//v53 add �ؼ���Ա���ӱ༭״̬
//	protected static final int EDIT_KEYPSN = 5;
	//v53 add ��Ƹ��Ա�༭״̬
	protected static final int EDIT_RETURN = 6;

	// ������Ա
	protected boolean adding;
	// ��ǰ�༭����
	protected int editType;
	// ��ǰ�༭��
	protected int editLineNo;
	// ��ǰ�Ƿ����ڱ༭��
	protected boolean editLine;
	// ��Ա�ɼ��Զ�����, �Զ����ɻ����ֹ�����
	private String psncodeAutoGenerate = null;
	// ���ݱ���VO
	private nc.vo.pub.billcodemanage.BillCodeObjValueVO objBillCodeVO = null;
	private GeneralVO[] subBackupVOs = null;// wangkf add ���������Ӽ�����Ϣ��
	private Vector vBookConds = new Vector();// wangkf add �������������ӵ���Ա������
	protected int intodocdirect = 0;//add by zhyan v53 ת����Ա��������0��ֱ��ת�룬1����ְ���� 
	protected boolean caneditkeypsn = false;//add by zhyan v53 �Ƿ���������Ա��Ϣά�����޸Ĺؼ���Ա��Ϣ
	protected int prounit = 1;//add by zhyan v502 ��ͬ�������� 1:�� 0:��
	protected int conunit = 1;//add by zhyan v502 ��ͬ���� 1:�� 0:�� ��ͬ�Ӽ�

	protected int CARD_ADD = 2000;

	protected int CARD_EDIT = 2001;

	protected int CARD_MAIN_BROWSE = 20021;

	protected int CARD_CHILD_BROWSE = 20022;

	protected int LIST_BROWSE = 2003;

	protected int LIST_INIT = 20031;

	protected int LIST_GROUP = 2004;

	protected int LIST_INIT_GROUP = 20041;

	protected int CARD_GROUP = 2005;

	protected int EMPLOYEE_REF_LIST = 2006;

	protected int EMPLOYEE_REF_CARD = 2007;

	protected int EMPLOYEE_REF_EDIT = 20061;

	protected int EMPLOYEE_REF_CARD_BROWSE = 20062;

	protected int EMPLOYEE_REF_LIST_BROWSE = 20063;

	// �ؼ���Ա״̬
	protected int KEYPSN_LIST = 2008;

	protected int KEYPSN_CARD = 200801;

	protected int KEYPSN_CARD_EDIT = 200802;
	// ҵ���Ӽ��༭
	protected int CARD_EDIT_SUB_TRACEABLE = 20010101;
	// ��ҵ���Ӽ��༭
	protected int CARD_EDIT_SUB_UNTRACEABLE = 20010101; 
	//
	private Vector<String> vDelPkPsndocSub = new Vector<String>();
	//
	private boolean showordervisible = false;
	// �Ƿ����ò��ŵ���Ȩ��
	Boolean isUsedDeptPower = null;
	// ɾ����Ա�Ӽ���Ϣʱ�󱣴�ʱ������Ӽ���ϢΪ�գ������Ӽ���Ϣ���Ӧ�˸�����Ϣ���߹�����Ϣ����Ϣ���Ӧ�������Ϣ��
	private Vector<GeneralVO> vDelSubVOs = new Vector<GeneralVO>();
	/**
	 * �õ��Ƿ��Զ��������
	 */
	public String getPsncodeAutoGenerateParam() {
		try {
			if (psncodeAutoGenerate == null) {
				//Ч���Ż�
				//psncodeAutoGenerate = PubDelegator.getIParValue().getParaString("0001", "HI_CODECRTTYPE");
				psncodeAutoGenerate = getPara("HI_CODECRTTYPE");
				if (psncodeAutoGenerate == null) {
					psncodeAutoGenerate = PSNCODE_AUTO_GENERATE;
				}
			}
		} catch (Exception e) {
			reportException(e);
			showHintMessage(e.getMessage());
		}
		return psncodeAutoGenerate;
	}

	protected ButtonObject bnEmployeeReference = new ButtonObject(			
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPP600704-000201")/* @res "��Ա����" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPP600704-000201")/* @res "��Ա����" */, 0, "��Ա����");

	protected ButtonObject bnApplicate = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPP600704-000202")/* @res "����" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPP600704-000202")/* @res "����" */, 0, "����");

	protected ButtonObject bnBatchApplicate = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPT600704-000312")/* @res "��������" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPT600704-000312")/* @res "��������" */, 0, "��������");

	protected ButtonObject bnAffirm = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPP600704-000203")/* @res "ȷ������" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPP600704-000203")/* @res "ȷ������" */, 0, "ȷ������");

	protected ButtonObject bnQueryAffirm = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPP600704-000204")/* @res "�鿴����" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPP600704-000204")/* @res "�鿴����" */, 0, "�鿴����");
	// V55 add
	protected ButtonObject bnCancelAffirm = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPT600704-000305")/* @res "ȡ������" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPT600704-000305")/* @res "ȡ������" */, 0, "ȡ������");

	protected ButtonObject bnList = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPP600704-000184")/* @res "�����б�" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPP600704-000184")/* @res "�����б�" */, 0, "�����б�");

	protected ButtonObject bnBatchAddSet = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPP600704-000185")/* @res "���������Ӽ�" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPP600704-000185")/* @res "���������Ӽ�" */, 0, "���������Ӽ�");

	protected ButtonObject bnChildEdit = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPP600704-000186")/* @res "�Ӽ��༭" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPP600704-000186")/* @res "�Ӽ��༭" */, 0, "�Ӽ��༭");

	protected ButtonObject bnUpRecord = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPP600704-000187")/* @res "��һҳ" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPP600704-000187")/* @res "��һҳ" */, 0, "��һҳ");

	protected ButtonObject bnDownRecord = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPP600704-000188")/* @res "��һҳ" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPP600704-000188")/* @res "��һҳ" */, 0, "��һҳ");

	protected ButtonObject bnToFirst = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPP600704-000189")/* @res "��ҳ" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPP600704-000189")/* @res "��ҳ" */, 0, "��ҳ");

	protected ButtonObject bnToLast = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPP600704-000190")/* @res "ĩҳ" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPP600704-000190")/* @res "ĩҳ" */, 0, "ĩҳ");

	protected ButtonObject bnListItemSet = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPP600704-000191")/* @res "��Ŀ����" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPP600704-000191")/* @res "��Ŀ����" */, 0, "��Ŀ����");

	protected ButtonObject bnAdd = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"upt600704-000148")/* @res "����" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"upt600704-000148")/* @res "����" */, 0, "����");

	protected ButtonObject bnDel = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"upt600704-000150")/* @res "ɾ��" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"upt600704-000150")/* @res "ɾ��" */, 0, "ɾ��");

	protected ButtonObject bnSave = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"upt600704-000154")/* @res "����" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"upt600704-000154")/* @res "����" */, 0, "����");

	protected ButtonObject bnCancel = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"upt600704-000155")/* @res "ȡ��" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"upt600704-000155")/* @res "ȡ��" */, 0, "ȡ��");

	protected ButtonObject bnSort = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPP600704-000055")/* @res "����˳��" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPP600704-000055")/* @res "����˳��" */, 0, "����˳��");

	protected ButtonObject bnSetSort = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"upt600704-000149")/* @res "����" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"upt600704-000149")/* @res "����" */, 0, "����");

	protected ButtonObject bnQuery = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"upt600704-000159")/* @res "��ѯ" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"upt600704-000159")/* @res "��ѯ" */, 0, "��ѯ");

	protected ButtonObject bnBatch = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"upt600704-000164")/* @res "�����޸�" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"upt600704-000164")/* @res "�����޸�" */, 0, "�����޸�");

	protected ButtonObject bnPrint = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"upt600704-000153")/* @res "��ӡ" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"upt600704-000153")/* @res "��ӡ" */, 0, "��ӡ");

	protected ButtonObject bnFresh = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"upt600704-000156")/* @res "ˢ��" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"upt600704-000156")/* @res "ˢ��" */, 0, "ˢ��");

	protected ButtonObject bnIntoDoc = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"upt600704-000152")/* @res "ת����Ա����" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"upt600704-000152")/* @res "ת����Ա����" */, 0, "ת����Ա����");

	protected ButtonObject bnSmUser = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPP600704-000056")/* @res "���������û�" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPP600704-000056")/* @res "���������û�" */, 0, "���������û�");

	// ����������ť
	protected ButtonObject bnEdit = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"upt600704-000167")/* @res "�޸�" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"upt600704-000167")/* @res "�޸�" */, 0, "�޸�");

	protected ButtonObject bnChildOp = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"upt600704-000160")/* @res "�Ӽ�����" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"upt600704-000160")/* @res "�Ӽ�����" */, 0, "�Ӽ�����");//v50 del

	protected ButtonObject bnReturn = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"upt600704-000168")/* @res "����" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"upt600704-000168")/* @res "����" */, 0, "����");

	// �Ӽ�������ť
	protected ButtonObject bnAddChild = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"upt600704-000145")/* @res "������" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"upt600704-000145")/* @res "������" */, 0, "����");

	protected ButtonObject bnInsertChild = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"upt600704-000157")/* @res "������" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"upt600704-000157")/* @res "������" */, 0, "������");

	protected ButtonObject bnUpdateChild = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"upt600704-000146")/* @res "�༭��" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"upt600704-000146")/* @res "�༭��" */, 0, "�༭��");

	protected ButtonObject bnDelChild = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"upt600704-000147")/* @res "ɾ����" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"upt600704-000147")/* @res "ɾ����" */, 0, "ɾ��");

	// Ա�������ĵ���Ϣ���Ĳ�����ť
	protected ButtonObject bnUpload = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"upt600704-000161")/* @res "�ϴ�" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"upt600704-000161")/* @res "�ϴ�" */, 0, "����");

	// ��Ƭ�ͻ����ᰴť
	protected ButtonObject bnCard = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"upt600704-000165")/* @res "��Ƭ" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"upt600704-000165")/* @res "��Ƭ" */, 0, "��Ƭ");
	
	protected ButtonObject bnView = new ButtonObject("Ԥ��","Ԥ��", 0, "Ԥ��");
	protected ButtonObject bnBatchExport = new ButtonObject("��������","��������", 0, "��������");
	protected ButtonObject[] grpCardChild = { bnView, bnBatchExport };

	protected ButtonObject bnBook = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"upt600704-000166")/* @res "������" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"upt600704-000166")/* @res "������" */, 0, "������");

	// ����ť
	protected ButtonObject bnSubsetMove = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPT600704-000273")/* "�����Ӽ�˳��" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPT600704-000273")/* "�����Ӽ�˳��" */, 0, "�����Ӽ�˳��");//v50 add 

	// V31SP1
	protected ButtonObject bnExportPic = new ButtonObject(
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPP600704-000182")/* @res "��Ƭ����" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPP600704-000182")/* @res "��Ƭ����" */, 0, "��Ƭ����");

	// V53
	protected ButtonObject bnIndocApp = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("600704", "UPT600704-000280"),
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPT600704-000280"), 0, "��ְ����");

	//
	// �ؼ���Ա���Ӱ�ť
	protected ButtonObject bnPsnGroup = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("600704", "UPT600704-000281"),
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPT600704-000281"), 0, "�ؼ���Ա��ά��");

	protected ButtonObject bnAddPsnGroup = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("600704", "UPT600704-000282"),
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPT600704-000282"), 0, "���ӹؼ���Ա��");

	protected ButtonObject bnEditPsnGroup = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("600704", "UPT600704-000283"),
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPT600704-000283"), 0, "�޸Ĺؼ���Ա��");

	protected ButtonObject bnDelPsnGroup = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("600704", "UPT600704-000284"),
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPT600704-000284"), 0, "ɾ���ؼ���Ա��");

	// ��Ա��ά��
	protected ButtonObject[] grpPsnGroup = { bnAddPsnGroup, bnEditPsnGroup,
			bnDelPsnGroup };

	protected ButtonObject bnKeyPsn = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("600704", "UPT600704-000285"),
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPT600704-000285"), 0, "�ؼ���Աά��");

	protected ButtonObject bnAddKeyPsn = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("600704", "UPT600704-000286"),
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPT600704-000286"), 0, "����ѡ������");
	protected ButtonObject bnMulAddKeyPsn = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("600704", "UPT600704-000328"),
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPT600704-000328"), 0, "����ѡ������");
	protected ButtonObject bnEditKeyPsn = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("600704", "UPT600704-000287"),
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPT600704-000287"), 0, "�޸Ĺؼ���Ա");

	protected ButtonObject bnDelKeyPsn = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("600704", "UPT600704-000288"),
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPT600704-000288"), 0, "ɾ���ؼ���Ա");

	protected ButtonObject bnSealKeyPsn = new ButtonObject(nc.ui.ml.NCLangRes
			.getInstance().getStrByID("600704", "UPT600704-000289"),
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPT600704-000289"), 0, "���ؼ���Ա");

	protected ButtonObject[] keyPsnmaintain = { bnAddKeyPsn,bnMulAddKeyPsn,bnEditKeyPsn,bnDelKeyPsn,bnSealKeyPsn};//bnEditKeyPsn,

	// �ؼ���Ա�б���������
	protected ButtonObject[] grpKeyPsnList = { bnPsnGroup, bnKeyPsn, bnBatch,
			bnBatchAddSet, bnUpload, bnCard, bnBook,bnExportPic, bnListItemSet, bnSort,bnSetSort,
			bnQuery, bnFresh, bnPrint };

	// ��Ƭ���������
	protected ButtonObject[] grpKeyPsnCard = { bnEdit, bnChildEdit, bnSave,
			bnCancel, bnUpload, bnCard,bnToFirst, bnUpRecord, bnDownRecord, bnToLast,bnSubsetMove,
			bnList };

	//
	// Ա����Ϣ�ɼ�
	/* V35 �б��� �����ӡ����༭����ɾ�����������޸ġ������������Ӽ�������Ƭ���������᡿�����򡿡���ѯ����ת����Ա����������Ŀ���á���ˢ�¡�����ӡ�� */
	protected ButtonObject[] grpCollect = { bnAdd, bnEdit, bnDel, bnBatch,
			bnBatchAddSet, bnIntoDoc, bnUpload, bnCard, bnBook, bnListItemSet, bnSort, bnSetSort,
			bnQuery, bnFresh, bnPrint };// ,
	//Ա����Ϣά��
	protected ButtonObject[] grpSortMaintain = { bnEdit, bnDel, bnBatch,
			bnBatchAddSet, bnEmployeeReference, bnSmUser, bnUpload, bnCard, bnBook,
			bnExportPic, bnListItemSet, bnSort, bnSetSort, bnQuery,  bnFresh,
			bnPrint};

	// ��Ա���õ��б���

	// ����showorder�����Ӱ�ť
	protected ButtonObject[] grpShoworder = { bnSave, bnCancel };
	// ��Ա����
	protected ButtonObject[] grpEmployeeReference = { bnApplicate,bnBatchApplicate,
			bnQueryAffirm, bnAffirm, bnCancelAffirm };

	// ά���ڵ��޸��Ӱ�ť
	/*
	 * ��Ƭ�� ���༭����ɾ����[�Ӽ��༭] [����] [ȡ��]����һ��������һ��������ͷ��������ĩ����[�б�]��ˢ�¡� [������][������][�༭��]
	 * [ɾ����]
	 */
	protected ButtonObject[] grpCardDisplayMaintain = { bnEdit, bnDel,
			bnChildEdit, bnSave, bnCancel, bnUpload,bnCard, bnToFirst, bnUpRecord,
			bnDownRecord, bnToLast, bnSubsetMove, bnList };

	protected ButtonObject[] grpCardRef = { bnEmployeeReference, bnSave,
			bnCancel, bnList };

	// �ɼ���Ƭ��ʾ��ť
	protected ButtonObject[] grpCardDisplayCollect = { bnAdd, bnEdit, bnDel,
			bnChildEdit, bnSave, bnCancel, bnUpload, bnToFirst, bnUpRecord,
			bnDownRecord, bnToLast, bnSubsetMove, bnList };

	// V35
	protected ButtonObject[] grpChild = { bnAddChild, bnInsertChild, bnUpdateChild, bnDelChild };

	// ���������ʷ
	protected Stack bnHistory = new Stack();
	// ����Ȩ�޹�����䣬������Ȩ��ʱΪ��
	protected String powerSql = "";
	// �����������
	protected CtrlDeptVO ctrlDeptRoot;
	// ��ǰ��Ա�б����Ϣ
	protected GeneralVO[] psnList;
	// ��Ϊ��ӡʱ��Ҫ���������Ϣ��������չ�Զ�����Ϣ������Ա�б��в������Ϣ�Ͳ��������Ϣ��sql����������˳��һ�£�distinct����ģ�
	protected GeneralVO[] psnListForPrint;
	// ��ǰ��ѯ���
	protected GeneralVO[] queryResult = new GeneralVO[0];
	// ��Ա���ղ��ŷ��໺��
	protected Hashtable psnDeptCache = new Hashtable();
	// ����Ȩ��
	protected String deptPowerInit = " 0 = 0 ";



	// ��Ա������Ϣ���ϣ�����������ģ���ʶ
	private static final String[][] traceTables = {
		{ "hi_psndoc_deptchg", "HI" },/* ��ְ��� */
		{ "hi_psndoc_ctrt", "HRCM" },/* �Ͷ���ͬ */
		{ "hi_psndoc_part", "HI" },/* ��ְ��� */
		{ "hi_psndoc_training", "TRM" },/* ��ѵ��¼ */
		{ "hi_psndoc_ass", "PE" },/* ���˼�¼ */
		{ "hi_psndoc_retire", "HI" },/* ���˴��� */
		{ "hi_psndoc_orgpsn", "HI" },/* ������֯ */
		{ "hi_psndoc_psnchg", "HI" }, /* Ա������ */
		{ "hi_psndoc_dimission", "HI" } /* ��ְ��� */
	};
	// ������Ϣ��ӳ�䣬��Ҫ��Ϊ�˸��ٴ�ȡ
	private static final Hashtable traceTableMap = new Hashtable();

	/**
	 * ��ʼ����Ա��Ϣ����ӳ���
	 */
	private void initTraceTableMap() {
		for (int i = 0; i < traceTables.length; i++) {
			traceTableMap.put(traceTables[i][0], traceTables[i][1]);
		}
	}
	
	protected void initButtonGroup() throws Exception {
		bnCard.addChileButtons(grpCardChild);
	}

	// ��ְ����
	protected final Item[] jobTypeItems = new Item[] {
			new Item(0, "fullwork", "WorkCard", nc.ui.ml.NCLangRes
					.getInstance().getStrByID("600704", "UPP600704-000170")/* "ȫְ" */),
					new Item(1, "parttime", "WorkCard", nc.ui.ml.NCLangRes
							.getInstance().getStrByID("600704", "UPP600704-000171")/* "��ְ" */),
							new Item(2, "work", "WorkCard", nc.ui.ml.NCLangRes.getInstance()
									.getStrByID("600704", "UPP600704-000172")/* "���" */),
									new Item(3, "work", "WorkCard", nc.ui.ml.NCLangRes.getInstance()
											.getStrByID("600704", "UPP600704-000174")/* "����" */),
											new Item(4, "despatch", "WorkCard", nc.ui.ml.NCLangRes.getInstance()
													.getStrByID("600704", "UPT600704-000307")/* "����" */),		
													new Item(-1, "all", "WorkCard", nc.ui.ml.NCLangRes.getInstance()
															.getStrByID("600704", "UPP600704-000198")/* "ȫ��" */) };

	// ��Ա������Χ
	protected final Item[] psnScopeItems = new Item[] {
			new Item(CommonValue.PSNCLSCOPE_WORK, "work", "WorkCard",
					nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
					"UPP600704-000059")/* @res "��ְ��Ա" */),
					new Item(CommonValue.PSNCLSCOPE_DISMISS, "dismiss", "DismissCard",
							nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
							"UPP600704-000060")/* @res "��Ƹ��Ա" */),
							new Item(CommonValue.PSNCLSCOPE_RETIRE, "retire", "RetireCard",
									nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
									"UPP600704-000061")/* @res "������Ա" */),
									new Item(CommonValue.PSNCLSCOPE_LEAVE, "leave", "LeaveCard",
											nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
											"UPP600704-000062")/* @res "������Ա" */),
											new Item(CommonValue.PSNCLSCOPE_OTHER, "other", "OtherCard",
													nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
													"UPP600704-000063")/* @res "������Ա" */) };

	// ��ǰ��Ա��������Ϣ
	protected PersonEAVO person;
	// ��ǰ��Ա������Ϣ����
	protected HashMap persons = new HashMap();
	// ��ǰ��ѯ����,��ʼ��Ϊ������
	protected ConditionVO[] conditions = new ConditionVO[0];
	// ���ò�ѯ����
	protected String wheresql = "";
	// ��Ա�б���Ҫ��ʾ����Ŀ
	protected Pair[] listItems = null;
	// �����Ի���
	protected GotoDialog gotoDialog = null;
	// ���ȶԻ���
	protected ProgressDialog progressDialog = null;
	protected SortableBillScrollPane ivjPsnList = null;
	// �����޸ĶԻ���
	protected BatchUpdateDlg batchDlg = null;
	// ��ѯ�Ի���
	protected QueryConditionClient queryDlg = null;
	public String[] tablesFromQueryDLG = null;
	
	// ��ӡ�Ի���
	protected PrintEntry printEntry = null;
	protected int recordcount = 0;
	private static Hashtable headTables = null;
	private UITree ivjDeptTree = null;
	private UIPanel ivjPsnCard = null;
	private UIScrollPane ivjUIScrollPane = null;
	private UISplitPane ivjUISplitPane = null;
	private UIComboBox ivjCmbPsncl = null;
	private UIPanel ivjNorthPanel = null;
	private UILabel ivjUILabel1 = null;
	private HIBillCardPanel ivjWorkCard = null;
	private HIBillCardPanel ivjDismissCard = null;
	private HIBillCardPanel ivjLeaveCard = null;
	private HIBillCardPanel ivjOtherCard = null;
	private HIBillCardPanel ivjRetireCard = null;

	IvjEventHandler ivjEventHandler = new IvjEventHandler();
	static {
		headTables = new Hashtable();
		headTables.put("bd_psndoc", "bd_psndoc");
		headTables.put("bd_psnbasdoc", "bd_psnbasdoc");// bd_accpsndoc
	}

	private UIPanel ivjCenterPanel = null;
	SortConfigDialog configDialog = null; // ����Ի���
	// Registered Triggers
	TriggerTable triggerTable = new TriggerTable();
	private boolean isLoadedBasicData = false;// wangkf add ���Ա����������͸�������Ƿ���ص�
	private boolean isLoadedAccData = false;
	String autopsncode = null;// ����Ա�������Զ�����ʱ�������ȡ��������һ�����ɵı�š�����㡾���桿��Ѹ��ֶ���ա�
	String isUniquePsncodeInGroup = null;// �Ƿ����� ������Ψһ�Ĳ���
	protected boolean isQuery = false;// ����Ƿ��ѯ��
	// add by sunxj 2010-01-03 ���ٲ�ѯ��� start
	protected boolean isQuickSearch = false;// ����Ƿ���ٲ�ѯ��
	protected String quickWherePart = null;
	protected String unloadPsn = null;
	// add by sunxj 2010-01-03 ���ٲ�ѯ��� end
	
	protected boolean isQuickQuery = false; //����Ƿ����
	protected String quickDLGsql = "";
	
	protected HashMap hmSortCondition = new HashMap();// wangkf add ������µ�����������
	private Vector vTempUploadFile = new Vector();
	private boolean isRelateToAcc = false;// �Ƿ����ֶ��븨�����������
	public String[] allCorpPks = null;	//���й�����˾
	public String corpPK = Global.getCorpPK();
	public String curTempCorpPk = Global.getCorpPK();
	// �������˾�Ĳ���Ȩ��SQL���
	protected Hashtable deptPowerlist = new Hashtable();
	protected boolean isMaintain = false;
	// ��ǰ��ְ��Ա�Ƿ��Ǳ���˾��ְ��Ա
	protected boolean isPartPsnCurCorp = false;
	// ��ǰ��ʾ����
	protected boolean isShowBillTemp = false;
	// ��Ա���չ�˾���໺��
	protected Hashtable psnCorpCache = new Hashtable();
	public DefaultTreeModel treemodel = null;
	private UIPanel ivjCenterPanelRight = null;
	private UISplitPane ivjUISplitPaneVertical = null;
	private UIComboBox ivjCmbJobType = null;
	private UILabel ivjUILabelType = null;

	private boolean isrehire = false;//��Ƹ��־,Ĭ��Ϊ���Ƿ�Ƹ
	private boolean isbackrehire = false;//��Ƹ��־,Ĭ��Ϊ������Ƹ
	private String rehirePsnbaspk = null;//��Ƹ��Ա����������
	private String rehirePsnpk = null;//��Ƹ��Ա������Ϣ������
	private String rehirePsnBelonpk = null;//��Ƹ��Ա������˾����

	class IvjEventHandler implements java.awt.event.ItemListener,
	java.awt.event.MouseListener,
	javax.swing.event.TreeSelectionListener {
		public void itemStateChanged(java.awt.event.ItemEvent e) {
			if (e.getSource() == PsnInfCollectUI.this.getCmbPsncl()) {
				connEtoC1(e);
			} else if (e.getSource() == PsnInfCollectUI.this.getCmbJobType()) {

				cmbJobTypItemStateChanged(e);
			}
		};
		public void mouseClicked(java.awt.event.MouseEvent e) {
			if (e.getSource() == PsnInfCollectUI.this.getDeptTree()) {
				connEtoC3(e);
			}
		};
		public void mouseEntered(java.awt.event.MouseEvent e) {
		};
		public void mouseExited(java.awt.event.MouseEvent e) {
		};
		public void mousePressed(java.awt.event.MouseEvent e) {
		};
		public void mouseReleased(java.awt.event.MouseEvent e) {
		};
		public void valueChanged(javax.swing.event.TreeSelectionEvent e) {
			if (e.getSource() == PsnInfCollectUI.this.getDeptTree()) {
				listSelectRow = -1;
				connEtoC2(e);
			}
		};
	};

	protected int splitLocation = SPLIT_MAX;//��¼�Ƿ���ʾ�Ӽ�״̬��������л�ʱʹ��

	private CheckUniqueDlg checkAddDlg = null;	

	// �������˾����Ա���Ȩ��SQL���
	protected Hashtable psnclPowerlist = new Hashtable();

	// ��ǰ�Ƿ���ж��б༭
	private boolean isEditSub = false;
	/**
	 * V35 �б������Ӧ�¼�
	 * @param mouseEvent
	 */
	public void PsnListMouseClicked(BillMouseEnent mouseEvent) {
		try {
			if (isEmployeeRef()) {
				switchButtonGroup(EMPLOYEE_REF_CARD);
				setButtonGroup(EMPLOYEE_REF_CARD);
			} else {
				switchButtonGroup(CARD_GROUP);
				setButtonGroup(CARD_GROUP);
			}
			listSelectRow = getPsnList().getTable().getSelectedRow();
			loadPsnInfo(listSelectRow);// װ�� ��ǰ����Ա����
			
			if (isEmployeeRef()) {
				setButtonsState(EMPLOYEE_REF_CARD_BROWSE);
			} else {
				setButtonsState(CARD_MAIN_BROWSE);
				setButtonsState(CARD_CHILD_BROWSE);
			}
			getCard().setPosMaximized(-1);

		} catch (Exception e) {
			Util.getTopFrame(this).setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			reportException(e);
			showErrorMessage(e.getMessage());
		}
	}

	/**
	 * PsnInfCollectUI ������ע�⡣
	 */
	public PsnInfCollectUI() {
		super();
		initialize();
	}

	/**
	 * �˴����뷽�������� 
	 * �������ڣ�(2004-10-19 15:57:09)
	 * @param parentvo nc.vo.hi.hi_301.CtrlDeptVO
	 */
	protected DefaultMutableTreeNode addDeptNodeToTree(DefaultMutableTreeNode parentnode, CtrlDeptVO parentvo) {
		// ��Ӻ���
		int childCorpCount = 0;
		Vector children = null;
		if (parentnode == null) {
			parentnode = new DefaultMutableTreeNode(parentvo);
		}
		if (parentvo.nodeType == CtrlDeptVO.CORP){
			children = parentvo.getDeptchildren();
			if(parentvo.getChildren()==null){
				childCorpCount = 0;
			}else{
				childCorpCount = parentvo.getChildren().size();
			}
		}else if (parentvo.nodeType == CtrlDeptVO.DEPT) {
			children = parentvo.getChildren();
		}
		if (children != null) {
			for (int i = 0; i < children.size(); i++) {
				CtrlDeptVO childvo = (CtrlDeptVO) children.elementAt(i);
				DefaultMutableTreeNode childNode = addDeptNodeToTree(null,childvo);
				if(parentvo.nodeType==CtrlDeptVO.CORP){//����ӽڵ��ǵĸ��ڵ�Ϊ��˾����ѽڵ�������ӹ�˾֮��
					if(childCorpCount>=0){
						int index = parentnode.getChildCount()-childCorpCount;
						parentnode.insert(childNode, index);
					}
				}else{
					parentnode.add(childNode);
				}
			}
		}
		return parentnode;
	}

	/**
	 * ������Աʱ�ı��涯�������� 
	 * �������ڣ�(2004-5-27 10:04:31)
	 * @param eavO  nc.vo.hi.hi_301.PersonEAVO
	 * @exception java.lang.Exception �쳣˵����
	 */
	private void addPerson(PersonEAVO eavo) throws java.lang.Exception {
		String pk_psndoc =null;
		if(isrehire){//��Ƹ����
			String pk_psnbasdoc = getRehirePsnbaspk();
			eavo.getAccpsndocVO().setAttributeValue("pk_psnbasdoc",pk_psnbasdoc);
			eavo.getPsndocVO().setAttributeValue("isreturn", "Y");
			eavo.getPsndocVO().setAttributeValue("pk_psnbasdoc",pk_psnbasdoc);
			eavo.getAccpsndocVO().setAttributeValue("indocflag","Y");
			//			pk_psndoc = getRehirePsnpk();
			//��Ա��Ƹ,��Ҫ���ӹ�����Ϣ,�޸���Ա������Ϣ
			eavo.getAccpsndocVO().setAttributeValue("pk_corp", Global.getCorpPK());
			pk_psndoc = HIDelegator.getPsnInf().insertMain(eavo.getPsndocVO(), eavo.getAccpsndocVO(), eavo.getAccpsndocVO(), getObjVO());//����������Ϊ��Ƹ��־
		}else if (isbackrehire){//��Ƹ����
			String pk_psnbasdoc = getRehirePsnbaspk();
			eavo.getAccpsndocVO().setAttributeValue("pk_psnbasdoc",pk_psnbasdoc);
			eavo.getPsndocVO().setAttributeValue("pk_psnbasdoc",pk_psnbasdoc);
			eavo.getAccpsndocVO().setAttributeValue("indocflag","Y");
			//			pk_psndoc = getRehirePsnpk();
			//��Ա��Ƹ,��Ҫ���ӹ�����Ϣ,�޸���Ա������Ϣ
			eavo.getAccpsndocVO().setAttributeValue("pk_corp", Global.getCorpPK());
			pk_psndoc = HIDelegator.getPsnInf().insertMain(eavo.getPsndocVO(), eavo.getAccpsndocVO(), eavo.getAccpsndocVO(), getObjVO());//����������Ϊ��Ƹ��־
		}
		else{
			//���沢������Ա����
			pk_psndoc = HIDelegator.getPsnInf().insertMain(eavo.getPsndocVO(), eavo.getAccpsndocVO(), null, getObjVO());
		}
		eavo.getPsndocVO().setAttributeValue("pk_psndoc", pk_psndoc);
		eavo.setPk_psndoc(pk_psndoc);
		eavo.getAccpsndocVO().setAttributeValue("belong_pk_corp",Global.getCorpPK());
		// ������Ա����
		String strWhere = " and bd_psndoc.pk_psndoc = '" + pk_psndoc + "' ";
		// ���¸���Ա��Ϣ
		GeneralVO[] gvos = HIDelegator.getPsnInf().queryByCondition(
				Global.getCorpPK(), null, powerSql, getListField(), strWhere);

		// ����Ա��ѯ�������Ա�б��м���������Ա
		GeneralVO[] result = new GeneralVO[queryResult.length + 1];
		System.arraycopy(queryResult, 0, result, 0, queryResult.length);
		if (gvos != null && gvos.length > 0) {
			eavo.getAccpsndocVO().setAttributeValue("pk_psnbasdoc", gvos[0].getAttributeValue("pk_psnbasdoc"));
			result[queryResult.length] = gvos[0];
		}
		queryResult = result;
		vBookConds.addElement(pk_psndoc);// ���ڻ�����Ĳ�ѯ����
		// �������
		psnDeptCache.clear();
		psnCorpCache.clear();

		// ����ѡ�����ڵ㣨���Ų������ܲ��ǵ�ǰ�Ĳ���)
		deptTreeValueChanged(null,false);
		listSelectRow = getRowInPsnList(eavo, psnList);// V35
		bodyTabbedPane_stateChanged(null);
		if (listSelectRow == -1) {
			person = eavo;
		}
	}

	/**
	 * ������Դ�봥������ʹ�ô��������������� 
	 * �������ڣ�(2004-5-17 20:19:19)
	 * @param srcItem nc.ui.pub.bill.BillItem ����Դ��BillItem
	 * @param destItem  nc.ui.pub.bill.BillItem ���������BillItem
	 * @param listener  nc.ui.hi.hi_301.trigger.ITriggerListener ����������
	 */
	private void addTriggerListener(final BillItem srcItem,final BillItem destItem, final ITriggerListener listener) {

		// ���ô�������
		listener.setTarget(new AccessibleItem(destItem, this));
		// ��ô���Դ���
		Component srcComponent = srcItem.getComponent();
		// �������������Ӵ���������
		switch (srcItem.getDataType()) {
		// �ַ���������С����ʱ�䶼ʹ�ò��յ�textfield
		case IBillItem.STRING: // �ַ�
			UIRefPane ref = (UIRefPane) srcComponent;
			UITextField text = ref.getUITextField();
			ref.setButtonFireEvent(true);
			text.addFocusListener(new FocusListener() {
				public void focusLost(FocusEvent e) {
					SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							if("ID".equalsIgnoreCase((srcItem.getKey()))){
								String id = srcItem.getValue();
								if(id!=null&&id.length()!=0){
									if(id.length()!=15&&id.length()!=18){
//										MessageDialog.showWarningDlg(getCard(), nc.ui.ml.NCLangRes.getInstance().getStrByID("600704", "UPP600704-000009"), "��������֤����Ϊ��15λ��18λ!");
									}else{
										TriggerEvent event = new TriggerEvent(srcItem);
										listener.trigger(event);
									}
								}
							}
						}
					});
				}
				public void focusGained(FocusEvent e) {
				}
			});
			break;
		case IBillItem.INTEGER: // ����
		case IBillItem.DECIMAL: // С��
		case IBillItem.TIME: // ʱ��
			ref = (UIRefPane) srcComponent;
			text = ref.getUITextField();
			ref.setButtonFireEvent(true);
			text.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					TriggerEvent event = new TriggerEvent(srcItem);
					listener.trigger(event);
				}
			});
			break;
			// ���ڡ����ա��Զ����������ʹ�ò���
		case IBillItem.DATE: // ����
		case IBillItem.UFREF: // ����
		case IBillItem.USERDEF: // �Զ������
			ref = (UIRefPane) srcComponent;
			ref.setButtonFireEvent(true);
			ref.addValueChangedListener(new ValueChangedListener() {
				public void valueChanged(ValueChangedEvent e) {
					TriggerEvent event = new TriggerEvent(srcItem);
					listener.trigger(event);
				}
			});
			break;
			// �߼���ʹ�ø�ѡ��
		case IBillItem.BOOLEAN: // �߼�
			final UICheckBox checkBox = (UICheckBox) srcComponent;
			checkBox.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					TriggerEvent event = new TriggerEvent(srcItem);
					listener.trigger(event);
				}
			});
			break;
			// ����������
		case IBillItem.COMBO: // ����
			final UIComboBox comboBox = (UIComboBox) srcComponent;
			comboBox.addItemListener(new ItemListener() {
				public void itemStateChanged(ItemEvent e) {
					TriggerEvent event = new TriggerEvent(srcItem);
					listener.trigger(event);
				}
			});
			break;
			// ���ı���ͼƬ���������
		case IBillItem.TEXTAREA: // ���ı�
		case IBillItem.IMAGE: // ͼƬ
		case IBillItem.OBJECT: // ͼƬ
			break;
		}
	}
	//add by zhyan v50 
	private RecieverSelectDlg userSelectDlg = null; 
	public RecieverSelectDlg getUserSelectDlg() {
		if (userSelectDlg == null) {
			userSelectDlg = new RecieverSelectDlg(this,app_pk_corp,getModuleName());
		}	
		return userSelectDlg;
	}


	/**
	 * ����ı�ǩ�л��������¼����� 
	 * �������ڣ�(2004-5-12 14:26:19)
	 * @param event javax.swing.event.ChangeEvent
	 */
	protected void bodyTabbedPane_stateChanged(ChangeEvent event) {
		String tableCode = getBodyTableCode();
		getCard().setBodyMenuShow(tableCode, false);
		getCard().addEditListener(this);
		loadPsnChildInfo(listSelectRow, tableCode);
		if (getButtonGroup() == CARD_GROUP) {
			switchButtonGroup(CARD_GROUP);
			setChildButtonState();
			setBillTempletState(isEditing());// setEditState(false); || isAdding()
		} else if (getButtonGroup() == EMPLOYEE_REF_CARD) {
			setBillTempletState(isEditing());
			setButtonsState(EMPLOYEE_REF_EDIT);
			if ("bd_psnbasdoc".equalsIgnoreCase(tableCode)) {
				setHeadBillItemEditable("bd_psnbasdoc", false);
			}
		} else {
			setButtonsState(LIST_BROWSE);
		}
	}

	/**
	 * ȡ���༭����Ϣ���� 
	 * �������ڣ�(2004-5-26 21:37:48)
	 */
	private void cancelEditMain() {
		if (!isAdding()) {
			getCard().setBillValueVO(person);// �����ǰΪά��״̬����ԭ����ֵ�ָ���������
			CircularlyAccessibleValueObject[] records = person
			.getTableVO(getCurrentSetCode());// ��ǰ��Ϣ�������м�¼
			getCard().getBillModel().setBodyDataVO(records);
		} else {
			getCard().resumeValue();// ������ձ༭����
		}
		setBillTempletState(false);
		setEditState(false);
	}

	/**
	 * ȡ���༭�ӱ�Ĳ����� 
	 * �������ڣ�(2004-5-26 21:42:21)
	 */
	private void cancelEditSub() {
		// ����ȡ���༭�ӱ���Ϣ��ʱ��ť״̬
		setCancelEditSubButtonState();
		// �ӱ�ҳǩ�ָ�����
		getCard().getBodyTabbedPane().setEnabled(true);
		// �༭�����ֹ��
		setBillTempletState(false);
		if (isEditLine()) {
			// ֹͣ�༭
			getCard().stopEditing();
			// �ָ��ϵ�����
			BillItem[] billitems = getCard().getBillModel().getBodyItems();
			for (int i = 0; i < billitems.length; i++) {
				if (billitems[i].getDataType() == BillItem.UFREF) {
					((UIRefPane) billitems[i].getComponent()).setPK(null);
					((UIRefPane) billitems[i].getComponent()).setText(null);
				}
			}
			getCard().getBillModel().setBodyDataVO(subBackupVOs);
			person.setTableVO(getBodyTableCode(), subBackupVOs);
		} else {
			// ����ѡ�е�ǰ�༭�У�����ɾ��
			int line = getEditLineNo();
			getCard().getBillTable().getSelectionModel().setSelectionInterval(line, line);
			getCard().delLine();
			// ���ӱ��¼��ɾ������
			SubTable subtable = person.getSubTable(getBodyTableCode());
			subtable.getRecords().removeElementAt(line);
		}
		if (isEditSub()) {
			setEditSub(false);
			vDelPkPsndocSub.clear();
			// 
			vDelSubVOs.clear();
		}
		if("hi_psndoc_edu".equalsIgnoreCase(getBodyTableCode())){
			loadPsnChildInfo(listSelectRow, getBodyTableCode());
		}
	}

	/**
	 * �����ȡ�������������� 
	 * �������ڣ�(2004-5-26 21:35:47)
	 */
	private void cancelSort() throws java.lang.Exception {
		setCancelSortButtonState();
		// ����������
		returnToMain();
		// ����ѡ��ǰ�ڵ�
		deptTreeValueChanged(null);
		// ��������в��ɱ༭
		getPsnList().getTableModel().getItemByKey("showorder").setEdit(false);
		getPsnList().getTableModel().getItemByKey("showorder").setEnabled(false);
	}

	/**
	 * �˴����뷽�������� 
	 * �������ڣ�(2004-9-25 15:19:02)
	 */
	private void changeBillTempRef() {
		String mainrefcorp = null;
		mainrefcorp = (String) psnList[listSelectRow].getAttributeValue("pk_corp");
		BillItem clItem = getBillItem("bd_psndoc.pk_psncl");
		UIRefPane psnclsref = (UIRefPane) clItem.getComponent();
		nc.ui.hi.hi_301.ref.PsnClsRef psncls = null;
		psncls = new nc.ui.hi.hi_301.ref.PsnClsRef(mainrefcorp);
		psnclsref.setRefType(1);
		psnclsref.setRefInputType(1);
		psnclsref.setRefModel(psncls);

		BillItem deptItem = getBillItem("bd_psndoc.pk_deptdoc");
		UIRefPane deptref = (UIRefPane) deptItem.getComponent();
		SpaDeptdocRefTreeModel dept = null;
		dept = new SpaDeptdocRefTreeModel(mainrefcorp, false);
		//
		setWhereToModel(dept,
		"((bd_deptdoc.canceled = 'N') or ( bd_deptdoc.canceled is null) )");
		deptref.setRefType(1);
		deptref.setRefInputType(1);
		deptref.setRefModel(dept);
		BillItem jobItem = getBillItem("bd_psndoc.pk_om_job");
		UIRefPane jobref = (UIRefPane) jobItem.getComponent();
		nc.ui.hi.ref.JobRef job = null;
		job = new nc.ui.hi.ref.JobRef(mainrefcorp, false);
		jobref.setRefType(1);
		jobref.setRefInputType(1);
		jobref.setRefModel(job);

		BillItem dutyItem = getBillItem("bd_psndoc.dutyname");
		UIRefPane dutyref = (UIRefPane) dutyItem.getComponent();
		nc.ui.hi.ref.DutyRef duty = null;// DutySimpleRef--��DutyRef
		duty = new nc.ui.hi.ref.DutyRef(mainrefcorp);
		dutyref.setRefType(1);
		dutyref.setRefInputType(1);
		dutyref.setRefModel(duty);
		// hi_psndoc_deptchg.pk_psncl
		/*
		BillItem clItem2 = getBillItem("hi_psndoc_deptchg.pk_psncl");
		UIRefPane psnclsref2 = (UIRefPane) clItem2.getComponent();
		nc.ui.hi.hi_301.ref.PsnClsRef psncls2 = new nc.ui.hi.hi_301.ref.PsnClsRef(mainrefcorp);// wangkf fixed curTempCorpPk->mainrefcorp
		psnclsref2.setRefType(1);
		psnclsref2.setRefInputType(1);
		psnclsref2.setRefModel(psncls2);
		 */
		BillItem deptItem2 = getBillItem("hi_psndoc_deptchg.pk_deptdoc");
		UIRefPane deptref2 = (UIRefPane) deptItem2.getComponent();
		SpaDeptdocRefTreeModel dept2 = new SpaDeptdocRefTreeModel(mainrefcorp);
		//
		setWhereToModel(dept2,
		"((bd_deptdoc.canceled = 'N') or ( bd_deptdoc.canceled is null) )");

		deptref2.setRefType(1);
		deptref2.setRefInputType(1);
		deptref2.setRefModel(dept2);

		BillItem jobItem2 = getBillItem("hi_psndoc_deptchg.pk_postdoc");
		UIRefPane jobref2 = (UIRefPane) jobItem2.getComponent();
		nc.ui.hi.ref.JobRef job2 = null;
		job2 = new nc.ui.hi.ref.JobRef(mainrefcorp, false);
		// curTempCorpPk->mainrefcorp
		jobref2.setRefType(1);
		jobref2.setRefInputType(1);
		jobref2.setRefModel(job2);
		UIRefPane dutyref2 = (UIRefPane) dutyItem.getComponent();
		nc.ui.hi.ref.DutyRef duty2 = null;
		duty2 = new nc.ui.hi.ref.DutyRef(mainrefcorp);// wangkf fixed
		// curTempCorpPk->mainrefcorp
		dutyref2.setRefType(1);
		dutyref2.setRefInputType(1);
		dutyref2.setRefModel(duty2);
		// hi_psndoc_req.pk_postrequire_h
		BillItem postrequire_hItem = getBillItem("hi_psndoc_req.pk_postrequire_h");
		UIRefPane postrequire_hItemref = (UIRefPane) postrequire_hItem
		.getComponent();
		nc.ui.hi.ref.RequireRef postrequire_h = new nc.ui.hi.ref.RequireRef(
				curTempCorpPk);
		postrequire_hItemref.setRefType(1);
		postrequire_hItemref.setRefInputType(1);
		postrequire_hItemref.setRefModel(postrequire_h);
		// hi_psndoc_req.pk_postrequire_b
		BillItem postrequire_bItem = getBillItem("hi_psndoc_req.pk_postrequire_b");
		UIRefPane postrequire_bItemref = (UIRefPane) postrequire_bItem
		.getComponent();
		nc.ui.hi.ref.RequireLevelRef postrequire_b = new nc.ui.hi.ref.RequireLevelRef(
				curTempCorpPk);
		postrequire_bItemref.setRefType(1);
		postrequire_bItemref.setRefInputType(1);
		postrequire_bItemref.setRefModel(postrequire_b);
		//
		BillItem[] items2 = getCard().getBodyItems();// ((HIBillData)
		// getCard().getBillData()).getBodyAllItems();
		if (items2 != null && items2.length > 0) {
			for (int i = 0; i < items2.length; i++) {
				if (items2[i].getDataType() == 5) { // ��������
					UIRefPane Myref = (UIRefPane) items2[i].getComponent();
					String refNodeName = Myref.getRefNodeName();

					if (Util.isDefdocPk(items2[i].getRefType())) {
						UIRefPane ref = (UIRefPane) items2[i].getComponent();
						ref.getRefModel().setCacheEnabled(false);
						if ((items2[i].getKey().equalsIgnoreCase(
						"bd_psndoc.jobrank")
						|| items2[i].getKey().equalsIgnoreCase(
						"bd_psndoc.jobseries") || items2[i]
						                                 .getKey().equalsIgnoreCase("bd_psndoc.series"))) {
							ref
							.getRefModel()
							.setWherePart(
									"pk_defdoclist = '"
									+ items2[i].getRefType()
									+ "'  and (pk_corp= '0001' or pk_corp= '"
									+ mainrefcorp
									+ "')  and (sealflag is null or sealflag<>'Y')");
						} else {
							ref
							.getRefModel()
							.setWherePart(
									"pk_defdoclist = '"
									+ items2[i].getRefType()
									+ "'  and (pk_corp= '0001' or pk_corp= '"
									+ curTempCorpPk
									+ "')  and (sealflag is null or sealflag<>'Y')");
						}
					} else if ("��Ա����".equals(refNodeName)
							|| "��Ա����HR".equals(refNodeName)) {
						if (Myref != null) {
							Myref.getRefModel().setUseDataPower(false);
							if (getModuleName().equalsIgnoreCase("600704")) {
								setWhereToModel(
										Myref.getRefModel(),
								"((bd_deptdoc.canceled = 'N' and bd_deptdoc.hrcanceled = 'N') or ( bd_deptdoc.canceled is null and bd_deptdoc.hrcanceled is null))");
							} else {
								setWhereToModel(Myref.getRefModel(),
								"((bd_deptdoc.canceled = 'N') or ( bd_deptdoc.canceled is null) )");
							}
						}
					} else if ("���ŵ���".equals(refNodeName)
							|| "���ŵ���HR".equals(refNodeName)
							|| "<nc.ui.hi.ref.DeptRefModel>"
							.equals(refNodeName)) {
						if (Myref != null) {
							setWhereToModel(Myref.getRefModel(),
							"((bd_deptdoc.canceled = 'N') or ( bd_deptdoc.canceled is null) )");

						}
					}
				}
			}
		}
		BillItem[] items = getCard().getHeadItems();// ((HIBillData)
		if (items != null && items.length > 0) {
			for (int i = 0; i < items.length; i++) {
				if (items[i].getDataType() == 5) { // ��������
					UIRefPane Myref = (UIRefPane) items[i].getComponent();
					String refNodeName = Myref.getRefNodeName();

					if (Util.isDefdocPk(items[i].getRefType())) {
						UIRefPane ref = (UIRefPane) items[i].getComponent();
						ref.getRefModel().setCacheEnabled(false);
						ref
						.getRefModel()
						.setWherePart(
								"pk_defdoclist = '"
								+ items[i].getRefType()
								+ "'  and (pk_corp= '0001' or pk_corp= '"
								+ curTempCorpPk
								+ "')  and (sealflag is null or sealflag<>'Y')");
					} else if ("��Ա����".equals(refNodeName)
							|| "��Ա����HR".equals(refNodeName)) {
						if (Myref != null) {
							Myref.getRefModel().setUseDataPower(false);
							if (getModuleName().equalsIgnoreCase("600704")) {
								setWhereToModel(
										Myref.getRefModel(),
								"((bd_deptdoc.canceled = 'N' and bd_deptdoc.hrcanceled = 'N') or ( bd_deptdoc.canceled is null and bd_deptdoc.hrcanceled is null))");
							} else {
								setWhereToModel(Myref.getRefModel(),
								"((bd_deptdoc.canceled = 'N') or ( bd_deptdoc.canceled is null) )");
							}
						}
					} else if ("���ŵ���".equals(refNodeName)
							|| "���ŵ���HR".equals(refNodeName)
							|| "<nc.ui.hi.ref.DeptRefModel>"
							.equals(refNodeName)) {
						if (Myref != null) {
							setWhereToModel(Myref.getRefModel(),
							"((bd_deptdoc.canceled = 'N') or ( bd_deptdoc.canceled is null) )");
						}
					}
				}
			}
		}
		System.gc();
	}

	/**
	 * �ı�������յĹ�˾������
	 */
	private void changeBillTempRef(String pk_corp) {
		String mainrefcorp = pk_corp;
		BillItem clItem = getBillItem("bd_psndoc.pk_psncl");
		UIRefPane psnclsref = (UIRefPane) clItem.getComponent();
		nc.ui.hi.hi_301.ref.PsnClsRef psncls = null;
		psncls = new nc.ui.hi.hi_301.ref.PsnClsRef(mainrefcorp);
		psnclsref.setRefType(1);
		psnclsref.setRefInputType(1);
		psnclsref.setRefModel(psncls);
		BillItem deptItem = getBillItem("bd_psndoc.pk_deptdoc");
		UIRefPane deptref = (UIRefPane) deptItem.getComponent();
		SpaDeptdocRefTreeModel dept = null;
		dept = new SpaDeptdocRefTreeModel(mainrefcorp, false);
		//
		setWhereToModel(dept,"((bd_deptdoc.canceled = 'N') or ( bd_deptdoc.canceled is null) )");


		deptref.setRefType(1);
		deptref.setRefInputType(1);

		deptref.setRefModel(dept);
		// bd_psndoc.pk_om_job
		BillItem jobItem = getBillItem("bd_psndoc.pk_om_job");
		UIRefPane jobref = (UIRefPane) jobItem.getComponent();
		nc.ui.hi.ref.JobRef job = null;
		job = new nc.ui.hi.ref.JobRef(mainrefcorp, false);
		jobref.setRefType(1);
		jobref.setRefInputType(1);
		jobref.setRefModel(job);
		// bd_accpsndoc.dutyname
		BillItem dutyItem = getBillItem("bd_psndoc.dutyname");// bd_accpsndoc
		UIRefPane dutyref = (UIRefPane) dutyItem.getComponent();
		nc.ui.hi.ref.DutyRef duty = null;
		duty = new nc.ui.hi.ref.DutyRef(mainrefcorp);
		dutyref.setRefType(1);
		dutyref.setRefInputType(1);
		dutyref.setRefModel(duty);
		// hi_psndoc_deptchg.pk_psncl
		BillItem clItem2 = getBillItem("hi_psndoc_deptchg.pk_psncl");
		UIRefPane psnclsref2 = (UIRefPane) clItem2.getComponent();
		nc.ui.hi.hi_301.ref.PsnClsRef psncls2 = new nc.ui.hi.hi_301.ref.PsnClsRef(
				mainrefcorp);// wangkf fixed curTempCorpPk->mainrefcorp
		psnclsref2.setRefType(1);
		psnclsref2.setRefInputType(1);
		psnclsref2.setRefModel(psncls2);
		// hi_psndoc_deptchg.pk_deptdoc
		BillItem deptItem2 = getBillItem("hi_psndoc_deptchg.pk_deptdoc");
		UIRefPane deptref2 = (UIRefPane) deptItem2.getComponent();
		SpaDeptdocRefTreeModel dept2 = new SpaDeptdocRefTreeModel(mainrefcorp);// wangkf
		//
		setWhereToModel(dept2,
		"((bd_deptdoc.canceled = 'N') or ( bd_deptdoc.canceled is null) )");


		deptref2.setRefType(1);
		deptref2.setRefInputType(1);

		deptref2.setRefModel(dept2);
		// hi_psndoc_deptchg.pk_postdoc
		BillItem jobItem2 = getBillItem("hi_psndoc_deptchg.pk_postdoc");
		UIRefPane jobref2 = (UIRefPane) jobItem2.getComponent();
		nc.ui.hi.ref.JobRef job2 = null;
		job2 = new nc.ui.hi.ref.JobRef(mainrefcorp, false);// wangkf fixed
		// curTempCorpPk->mainrefcorp
		jobref2.setRefType(1);
		jobref2.setRefInputType(1);
		jobref2.setRefModel(job2);
		// hi_psndoc_deptchg.pk_om_duty
		// BillItem dutyItem2 = getBillItem("hi_psndoc_deptchg.pk_om_duty");
		UIRefPane dutyref2 = (UIRefPane) dutyItem.getComponent();
		nc.ui.hi.ref.DutyRef duty2 = null;
		duty2 = new nc.ui.hi.ref.DutyRef(mainrefcorp);// wangkf fixed
		// curTempCorpPk->mainrefcorp
		dutyref2.setRefType(1);
		dutyref2.setRefInputType(1);
		dutyref2.setRefModel(duty2);
		// hi_psndoc_req.pk_postrequire_h
		BillItem postrequire_hItem = getBillItem("hi_psndoc_req.pk_postrequire_h");
		UIRefPane postrequire_hItemref = (UIRefPane) postrequire_hItem
		.getComponent();
		nc.ui.hi.ref.RequireRef postrequire_h = new nc.ui.hi.ref.RequireRef(
				mainrefcorp);
		postrequire_hItemref.setRefType(1);
		postrequire_hItemref.setRefInputType(1);
		postrequire_hItemref.setRefModel(postrequire_h);
		// hi_psndoc_req.pk_postrequire_b
		BillItem postrequire_bItem = getBillItem("hi_psndoc_req.pk_postrequire_b");
		UIRefPane postrequire_bItemref = (UIRefPane) postrequire_bItem
		.getComponent();
		nc.ui.hi.ref.RequireLevelRef postrequire_b = new nc.ui.hi.ref.RequireLevelRef(
				mainrefcorp);
		postrequire_bItemref.setRefType(1);
		postrequire_bItemref.setRefInputType(1);
		postrequire_bItemref.setRefModel(postrequire_b);
		//
		BillItem[] items2 = getCard().getBodyItems();// ((HIBillData)
		// getCard().getBillData()).getBodyAllItems();
		if (items2 != null && items2.length > 0) {
			for (int i = 0; i < items2.length; i++) {
				if (items2[i].getDataType() == 5) { // ��������
					UIRefPane Myref = (UIRefPane) items2[i].getComponent();
					String refNodeName = Myref.getRefNodeName();

					if (Util.isDefdocPk(items2[i].getRefType())) {
						UIRefPane ref = (UIRefPane) items2[i].getComponent();
						ref.getRefModel().setCacheEnabled(false);
						if ((items2[i].getKey().equalsIgnoreCase(
						"bd_psndoc.jobrank")
						|| items2[i].getKey().equalsIgnoreCase(
						"bd_psndoc.jobseries") || items2[i]
						                                 .getKey().equalsIgnoreCase("bd_psndoc.series"))) {
							ref
							.getRefModel()
							.setWherePart(
									"pk_defdoclist = '"
									+ items2[i].getRefType()
									+ "'  and (pk_corp= '0001' or pk_corp= '"
									+ mainrefcorp
									+ "')  and (sealflag is null or sealflag<>'Y')");
						}
					} else if ("��Ա����".equals(refNodeName)
							|| "��Ա����HR".equals(refNodeName)) {
						if (Myref != null) {
							Myref.getRefModel().setUseDataPower(false);
							if (getModuleName().equalsIgnoreCase("600704")) {
								setWhereToModel(
										Myref.getRefModel(),
								"((bd_deptdoc.canceled = 'N' and bd_deptdoc.hrcanceled = 'N') or ( bd_deptdoc.canceled is null and bd_deptdoc.hrcanceled is null))");
							} else {
								setWhereToModel(Myref.getRefModel(),
								"((bd_deptdoc.canceled = 'N') or ( bd_deptdoc.canceled is null) )");
							}
						}
					} else if ("���ŵ���".equals(refNodeName)
							|| "���ŵ���HR".equals(refNodeName)
							|| "<nc.ui.hi.ref.DeptRefModel>"
							.equals(refNodeName)) {
						if (Myref != null) {
							setWhereToModel(Myref.getRefModel(),
							"((bd_deptdoc.canceled = 'N') or ( bd_deptdoc.canceled is null) )");
						}
					}
				}
			}
		}
		BillItem[] items = getCard().getHeadItems();// ((HIBillData)
		// getCard().getBillData()).getHeadItems();
		if (items != null && items.length > 0) {
			for (int i = 0; i < items.length; i++) {
				if (items[i].getDataType() == 5) { // ��������
					UIRefPane Myref = (UIRefPane) items[i].getComponent();
					String refNodeName = Myref.getRefNodeName();

					if (Util.isDefdocPk(items[i].getRefType())) {
						UIRefPane ref = (UIRefPane) items[i].getComponent();
						ref.getRefModel().setCacheEnabled(false);
						ref
						.getRefModel()
						.setWherePart(
								"pk_defdoclist = '"
								+ items[i].getRefType()
								+ "'  and (pk_corp= '0001' or pk_corp= '"
								+ mainrefcorp
								+ "')  and (sealflag is null or sealflag<>'Y')");
					} else if ("��Ա����".equals(refNodeName)
							|| "��Ա����HR".equals(refNodeName)) {
						if (Myref != null) {
							Myref.getRefModel().setUseDataPower(false);
							if (getModuleName().equalsIgnoreCase("600704")) {
								setWhereToModel(
										Myref.getRefModel(),
								"((bd_deptdoc.canceled = 'N' and bd_deptdoc.hrcanceled = 'N') or ( bd_deptdoc.canceled is null and bd_deptdoc.hrcanceled is null))");
							} else {
								setWhereToModel(Myref.getRefModel(),
								"((bd_deptdoc.canceled = 'N') or ( bd_deptdoc.canceled is null) )");
							}
						}
					} else if ("���ŵ���".equals(refNodeName)
							|| "���ŵ���HR".equals(refNodeName)
							|| "<nc.ui.hi.ref.DeptRefModel>"
							.equals(refNodeName)) {
						if (Myref != null) {
							setWhereToModel(Myref.getRefModel(),
							"((bd_deptdoc.canceled = 'N') or ( bd_deptdoc.canceled is null) )");
						}
					}
				}
			}
		}
		System.gc();
	}

	/**
	 * 
	 *
	 */
	private void changePsnclScope() {
		try {
			// ���õ�ǰ���Ϊæ״̬
			{
				Util.getTopFrame(this).setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				// ��ǰѡ�й�����Χ����
				Item item = (Item) getCmbPsncl().getSelectedItem();
				if (!item.isTempletLoaded()) {
					// �����͵�ģ��û�м��������
					initBillTemp();
					initUI();
				}
				// ��ʾ��Ӧ��ҳ
				CardLayout layout = (CardLayout) getPsnCard().getLayout();
				layout.show(getPsnCard(), item.getCardName());

			}
			// ˢ�����л���
			psnList = null;
			queryResult = null;
			person = null;
			psnDeptCache.clear();
			psnCorpCache.clear();
			persons.clear();
			//update by sunxj 2010-02-03 ���ٲ�ѯ��� start
//			if ((queryDlg != null && isQuery)) {// ��¼�Ƿ��ѯ����
			if ((queryDlg != null && isQuery) || isQuickSearch) {// ��¼�Ƿ��ѯ����
				// ��ʼ��ȱʡ����
				initPsnListData();
			}
			//update by sunxj 2010-02-03 ���ٲ�ѯ��� start
			// �ָ���ť״̬
			Util.getTopFrame(this).setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		} catch (Exception e) {
			e.printStackTrace();
			{
				Util.getTopFrame(this).setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
			reportException(e);
			showErrorMessage(e.getMessage());
		}
	}

	/**
	 * 
	 */
	private final static int[] PSNCLSCOPE_GROUP = {
		nc.vo.hi.pub.CommonValue.PSNCLSCOPE_WORK, // ��ְ
		nc.vo.hi.pub.CommonValue.PSNCLSCOPE_DISMISS,// ��Ƹ
		nc.vo.hi.pub.CommonValue.PSNCLSCOPE_RETIRE, // ����
		nc.vo.hi.pub.CommonValue.PSNCLSCOPE_LEAVE, // ����
		nc.vo.hi.pub.CommonValue.PSNCLSCOPE_OTHER // ����
	};

	/**
	 * �õ�ѡ�е���Ա��������Χ
	 * @return
	 */
	private int getSelPsnclscope() {
		int index = getCmbPsncl().getSelectedIndex();
		if (index < 0 || index > PSNCLSCOPE_GROUP.length - 1)
			return -1;// δ�����ĳ��ָ
		return PSNCLSCOPE_GROUP[index];
	}

	/**
	 * ��Ա������Χ�仯ʱ���²��ա� 
	 * �������ڣ�(2004-7-21 16:07:44)
	 */
	public void changePsnInfRef() {
		if (queryDlg != null) {
			try {
				// ��ò�ѯģ�����
				nc.ui.pub.beans.UIRefPane psnref = (UIRefPane)queryDlg.getValueRefObjectByFieldCode("bd_psndoc.pk_psndoc");
				psnref.setRefType(1);
				psnref.setRefInputType(1);

				SpaDeptdocRefGridTreeModel psnrefmodel = new SpaDeptdocRefGridTreeModel();
				String wheresql = " bd_deptdoc.pk_deptdoc = V_HR_PSNDOC_MULTI_JOB.pk_deptdoc and bd_psndoc.dr = 0 "
					+ "and bd_psndoc.indocflag = '" + getIndocflag() + "' ";

				psnrefmodel.setCacheEnabled(false);
				if (getModuleName().equals("600707") ||getModuleName().equals("600708")) {
					wheresql += "and bd_psndoc.psnclscope = " + getSelPsnclscope();
				}

				psnrefmodel.setUseDataPower(false);
				if (getModuleName().equalsIgnoreCase("600704")) {
					setWhereToModel(
							psnrefmodel,
							"((bd_deptdoc.canceled = 'N' and bd_deptdoc.hrcanceled = 'N') or " +
					"( bd_deptdoc.canceled is null and bd_deptdoc.hrcanceled is null))");
				} else {
					setWhereToModel(psnrefmodel,
					"((bd_deptdoc.canceled = 'N') or ( bd_deptdoc.canceled is null) )");
					if ((powerSql.length() > 0 && powerSql.trim().startsWith("select"))&& !getModuleName().equals("600704")) {
						setWhereToModel(psnrefmodel,"( bd_deptdoc.pk_deptdoc in (" + powerSql + ") )");
					}
				}
				// ��Ա���Ȩ��
				//�������Ż�
//				if (GlobalTool.isUsedDataPower("bd_psncl", Global.getCorpPK())) {
//					String powerPsnclSql = nc.ui.hr.global.GlobalTool
//					.getPowerSql("bd_psncl", Global.getUserID(), Global.getCorpPK());
//					if (powerPsnclSql != null && powerPsnclSql.length() > 0) {
//						wheresql += " and bd_psndoc.pk_psncl in ("
//							+ powerPsnclSql + ") ";
//					}
//				}
				String powerPsnclSql = nc.ui.hr.global.GlobalTool
				.getPowerSql("bd_psncl", Global.getUserID(), Global.getCorpPK());
				if (powerPsnclSql != null && powerPsnclSql.length() > 0) {
					wheresql += " and bd_psndoc.pk_psncl in ("
						+ powerPsnclSql + ") ";
				}
				psnrefmodel.setWherePart(wheresql);	
				psnref.setRefModel(psnrefmodel);
//				queryDlg.setValueRef("bd_psndoc", "bd_psndoc.pk_psndoc", psnref);
//				ConditionVO[] vvos = queryDlg.getConditionVO(); 
//				queryDlg.initData();
//				if(vvos!=null&&vvos.length>0){
//				queryDlg.changeDefaultConditions(vvos);
//				}
			} catch (Exception e) {
			}
		}
	}

	public void adjustQueryDialog(){
		if (queryDialog == null) return;
		QueryTempletService qts = new QueryTempletService();
		Item item = (Item) getCmbPsncl().getSelectedItem();
		//�������Ƿ�Ƹ���ɼ�
		if (item.getValue() ==0 && (getModuleName().equalsIgnoreCase("600707") || getModuleName().equalsIgnoreCase("600708"))) {
			qts.addFilterByCode(queryDialog, "hi_psndoc_deptchg.isreturn");
		}else if(item.getValue() !=0 && (getModuleName().equalsIgnoreCase("600707") || getModuleName().equalsIgnoreCase("600708"))){
			qts.removeFilterByCode(queryDialog, "hi_psndoc_deptchg.isreturn");
		}
		//���ò��չ���������
		qts.fireCriteriaChanged(queryDialog, "bd_psndoc.pk_psndoc");

		qts.fireCriteriaChanged(queryDialog, "bd_psndoc.pk_psncl");
	}
	/**
	 * ���ڵ�����ԡ� 
	 * �������ڣ�(2004-6-5 9:06:19)
	 */
	protected boolean checkEntrance() {// wangkf fixed void->boolean
		return true;// wangkf add
	}

	/**
	 * ����������Ч�ԡ�
	 * @param eavo
	 * @param tableCodes
	 * @throws java.lang.Exception
	 */
	private void validateInput(PersonEAVO eavo, String[] tableCodes)
	throws java.lang.Exception {
		try {
			// ����ά����ʶ
			setMaintainMarkBeforeValidation(eavo);
			// ���tableCodes���������Ч��
			if (tableCodes != null)
				for (int i = 0; i < tableCodes.length; i++) {
					eavo.validate(tableCodes[i]);
				}
			// ɾ��ά����ʶ
			eavo.getPsndocVO().removeAttributeName("maintain");
		} catch (ValidException ve) {
			// ���廯������Ϣλ��
			traceErrorPoint(ve);

		}
	}

	/**
	 * ת����Ա�������˱����Ѿ��в��ţ���顣 
	 * �������ڣ�(2004-5-30 13:22:15)
	 * @exception java.lang.Exception  �쳣˵����
	 */
	private void checkPsnListDept(GeneralVO[] psnListTemp)
	throws java.lang.Exception {
		if (psnListTemp != null) {
			for (int i = 0; i < psnListTemp.length; i++) {
				String pk_deptdoc = (String) psnListTemp[i].getAttributeValue("pk_deptdoc");
				String psnname = (String) psnListTemp[i].getAttributeValue("psnname");
				FieldValidator.validate(psnname+ nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
				"UPP600704-000067")/* @res "�Ĳ���" */,
				"notnull", pk_deptdoc);
			}
		}
	}

	/**
	 * ת����Ա�������˱����Ѿ�����Ա���룬��顣
	 * 
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	private void checkPsnListPsnCode(GeneralVO[] psnListTemp)
	throws ValidationException {
		if (psnListTemp != null) {
			for (int i = 0; i < psnListTemp.length; i++) {
				String psncode = (String) psnListTemp[i].getAttributeValue("psncode");
				String psnname = (String) psnListTemp[i].getAttributeValue("psnname");
				try {
//					FieldValidator.validate(psnname + "����Ա����", "notnull",psncode);
					FieldValidator.validate(psnname + nc.ui.ml.NCLangRes.getInstance().getStrByID("600700", 
							"UPP600700-000107")/* @res "����Ա����" */, 
							"notnull", psncode);
				} catch (Exception e) {
					throw new ValidationException(e.getMessage());
				}
			}
		}
	}
	/**
	 * 
	 * @param psnListTemp
	 */
	private void autoSetInDocPsnCode(GeneralVO[] psnListTemp)throws java.lang.Exception{
		if (psnListTemp == null) {
			return ;
		}
		retrivePsncodeInGroup();
		Vector v = new Vector();
		GeneralVO[] vos = null;
		for (int i = 0; i < psnListTemp.length; i++) {
			String pk_psndoc = (String) psnListTemp[i].getAttributeValue("pk_psndoc");
			Object psncode = psnListTemp[i].getAttributeValue("psncode");
			String newpsncode = null;
			if (psncode == null||"".equalsIgnoreCase(psncode.toString().trim())) {
				if ("Y".equalsIgnoreCase(isUniquePsncodeInGroup)) // ����Ǽ�����Ψһ���򰴼��Ų�����Ա����
				{
					newpsncode = HIDelegator.getBillcodeRule().getBillCode_RequiresNew("BS", "0001", null,getObjVO());
				} else {
					newpsncode = HIDelegator.getBillcodeRule().getBillCode_RequiresNew("BS", Global.getCorpPK(),null, getObjVO());
				}

				GeneralVO vo = new GeneralVO();
				vo.setAttributeValue("pk_psndoc", pk_psndoc);
				vo.setAttributeValue("psncode", newpsncode);
				v.addElement(vo);	
			}
		}
		if (v.size() > 0) {
			vos = new GeneralVO[v.size()];
			v.copyInto(vos);
		}	
		HIDelegator.getPsnInf().batchUpdatePsnCode(vos);

	}

	/**
	 * ��Ա��Χ���������Ӧ�¼��������
	 */
	public void cmbPsnclItemStateChanged(java.awt.event.ItemEvent itemEvent) {
		if (itemEvent.getStateChange() == ItemEvent.SELECTED) {
			// if (isInitByPsnclScope) {// �ڵ�һ��������ģ��ʱ�������ô˷��� V35 add
			Item item = (Item) getCmbPsncl().getSelectedItem();
			if (item.getValue() > 0) {// && item.getValue()!= 5
				getUILabelType().setVisible(false);
				getCmbJobType().setVisible(false);
			} else {
				getUILabelType().setVisible(true);
				getCmbJobType().setVisible(true);
			}
			// ���ѡ�У���������ģ����̡߳�
			//changePsnInfRef();
			//�ı���Ա��Χ��Ҫ�ı��ѯģ�壬��adjustQueryDialogȡ��changePsnInfRef������
			adjustQueryDialog();
			changePsnclScope();
			//���ѡ��������Ա������ȡ�����á���ť���ã����򲻿��á�
			if (item.getValue() == 5) {
				bnCancelAffirm.setEnabled(true);
			}else{
				bnCancelAffirm.setEnabled(false);
			}
			updateButtons();
			listSelectRow = -1;
		}
	}

	public void cmbJobTypItemStateChanged(java.awt.event.ItemEvent e) {
		try {
			// ������ְ���͹�����Ա
			Util.getTopFrame(this).setCursor(
					Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			// ˢ�����л���
			psnList = null;
			queryResult = null;
			person = null;
			psnDeptCache.clear();
			psnCorpCache.clear();
			persons.clear();
			listSelectRow = -1;
			if (queryDlg != null) {// ��¼�Ƿ��ѯ����
				initPsnListData();
			}
			String tableCode = getBodyTableCode();
			getCard().setBodyMenuShow(tableCode, false);
			person = loadPsnChildInfo(listSelectRow, tableCode);
			// �ָ���ť״̬
			Util.getTopFrame(this).setCursor(
					Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		} catch (java.lang.Throwable ivjExc) {
			// user code begin {3}

			handleException(ivjExc);
		}
	}

	/**
	 * 
	 * @param arg1
	 */
	public void connEtoC1(java.awt.event.ItemEvent arg1) {
		try {
			this.cmbPsnclItemStateChanged(arg1);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	/**
	 * 
	 * @param arg1
	 */
	private void connEtoC2(javax.swing.event.TreeSelectionEvent arg1) {
		try {
			this.deptTreeValueChanged(arg1);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	/**
	 * 
	 * @param arg1
	 */
	private void connEtoC3(java.awt.event.MouseEvent arg1) {
		try {
			this.deptTreeMouseClicked(arg1);
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	/**
	 * ������¼��Ψһ��У��
	 * @param tablecode
	 * @param onePerson
	 * @return
	 * @throws java.lang.Exception
	 */
	public boolean dataUniqueValidate(String tablecode, PersonEAVO onePerson)
	throws java.lang.Exception {
		if (!"bd_psnbasdoc".equalsIgnoreCase(tablecode))
			return true;
		String message = null;
		ValidException vException = null;
		PsndocConsItmVO[] uniqueFields = HIDelegator.getPsnDocQueryService().getPsndocUniqueFields();
		if (uniqueFields != null && uniqueFields.length > 0) {
			// ---Ψһ����ֶηǿ�У��--begin
			String nullMsg = null;
			for (int i = 0; i < uniqueFields.length; i++) {			
				String field_name = uniqueFields[i].getField_name();
				String display_name = null;// conitmUniqueFields[i].getItem_name();				
				Object value = null;
//				if ("psnname".equalsIgnoreCase(field_name)) {
//				value = onePerson.getPsndocVO().getAttributeValue(
//				field_name);
//				} else {
				value = onePerson.getAccpsndocVO().getAttributeValue(
						field_name);
//				}
				if (value == null
						|| value.toString().trim().equalsIgnoreCase("")) {
					if (nullMsg == null) {
						nullMsg = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("600704", "UPP600704-000241")/*
						 * @res
						 * "Ψһ����ֶ�"
						 */
						+ ":"
						+ display_name
						+ nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("600704",
						"UPP600704-000156")/*
						 * @res
						 * "����Ϊ��"
						 */
						+ " ,\n";
					} else {
						nullMsg += display_name
						+ nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
						.getStrByID("600704",
						"UPP600704-000156")/*
						 * @res
						 * "����Ϊ��"
						 */
						+ " ,\n";
					}
				}
				if (nullMsg != null) {
					vException = new ValidException(nullMsg);
					vException.setTableCode(tablecode);
					vException.setFieldCode(field_name);
					traceErrorPoint(vException);
				}
			}
			onePerson.getAccpsndocVO().setAttributeValue("pk_psnbasdoc",
					onePerson.getPk_psnbasdoc());
//			onePerson.getAccpsndocVO().setAttributeValue("psnname",
//			onePerson.getPsndocVO().getAttributeValue("psnname"));
			String uniqueMsg = HIDelegator.getPsnInf().dataUniqueValidate(
					Global.getCorpPK(), uniqueFields,
					onePerson.getAccpsndocVO());
			onePerson.getAccpsndocVO().removeAttributeName("pk_psnbasdoc");
			if (uniqueMsg != null) {
				message = nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
				.getStrByID("600704", "UPP600704-000242")/*
				 * @res
				 * "����Ա�Ѿ�����,����ϸ��ϢΪ:"
				 */
				+ uniqueMsg;
				vException = new ValidException(message);
				vException.setTableCode(tablecode);
				vException.setFieldCode(uniqueFields[0].getField_name());
			}
		} else {
			String psnname = (String) onePerson.getAccpsndocVO().getAttributeValue("psnname");
			String id = (String) onePerson.getAccpsndocVO().getAttributeValue("id");
			String pk_psnbasdoc = (String) onePerson.getPsndocVO().getAttributeValue("pk_psnbasdoc");
			String pk_psndoc = (String) onePerson.getPsndocVO().getAttributeValue("pk_psndoc");
			if (id != null && id.trim().length() > 0) {
				String whereid = "";
				if (id.length() == 18) {
					String id15 = id.substring(0, 6) + id.substring(8, 17);
					whereid = "(ltrim(rtrim(bd_psnbasdoc.id))||bd_psnbasdoc.psnname = '"
						+ id
						+ psnname
						+ "' or ltrim(rtrim(bd_psnbasdoc.id))||bd_psnbasdoc.psnname = '"
						+ id15 + psnname + "')";
				}else if(id.length() ==15){//v53
					IDValidateUtil idutil = new IDValidateUtil(id);
					String id18 = idutil.getUpgradeId();//v53
					whereid = "(ltrim(rtrim(bd_psnbasdoc.id))||bd_psnbasdoc.psnname = '"
						+ id
						+ psnname
						+ "' or ltrim(rtrim(bd_psnbasdoc.id))||bd_psnbasdoc.psnname = '"
						+ id18 + psnname + "')";
				}
				else {
					whereid = "ltrim(rtrim(bd_psnbasdoc.id))||bd_psnbasdoc.psnname = '"
						+ id + psnname + "'";
				}
				String sql = "select 1 from bd_psnbasdoc where " + whereid
				+ "and bd_psnbasdoc.pk_psnbasdoc <> '" + pk_psnbasdoc
				+ "'";

				boolean exists = HIDelegator.getPsnInf().isRecordExist(sql);
				if (exists) {

					GeneralVO vo = HIDelegator.getPsnInf().queryPsnInfo(pk_psndoc, id, psnname);
//					if(vo!=null){
					String errordisplayText = "  ";// ��
					String psncode = errordisplayText;
					String deptname = errordisplayText;
					String omjob = errordisplayText;
					String unitname = errordisplayText;	
					if(vo!=null){
						psncode = (String) vo.getAttributeValue("psncode");
						deptname = (String) vo.getAttributeValue("deptname");
						deptname = (deptname == null ? errordisplayText: deptname.trim());
						omjob = (String) vo.getAttributeValue("jobname");
						omjob = (omjob == null ? errordisplayText : omjob.trim());
						unitname = (String) vo.getAttributeValue("unitname");
						unitname = (unitname == null ? errordisplayText: unitname.trim());
					}
					if (psncode == null || psncode.length() == 0) {
						if (id != null && id.trim().length() == 18) {
							String id15 = id.substring(0, 6) + id.substring(8, 17);
							vo = HIDelegator.getPsnInf().queryPsnInfo(pk_psndoc, id15, psnname);
							if (vo != null) {
								psncode = (String) vo.getAttributeValue("psncode");
								deptname = (String) vo.getAttributeValue("deptname");
								deptname = (deptname == null ? errordisplayText: deptname.trim());
								omjob = (String) vo.getAttributeValue("jobname");
								omjob = (omjob == null ? errordisplayText: omjob.trim());
								unitname = (String) vo.getAttributeValue("unitname");
								unitname = (unitname == null ? errordisplayText: unitname.trim());
							}
						}else if(id != null && id.trim().length() == 15){
							IDValidateUtil idutil = new IDValidateUtil(id);
							String id18 = idutil.getUpgradeId();//v53
							vo = HIDelegator.getPsnInf().queryPsnInfo(pk_psndoc, id18, psnname);
							if (vo != null) {
								psncode = (String) vo.getAttributeValue("psncode");
								deptname = (String) vo.getAttributeValue("deptname");
								deptname = (deptname == null ? errordisplayText: deptname.trim());
								omjob = (String) vo.getAttributeValue("jobname");
								omjob = (omjob == null ? errordisplayText: omjob.trim());
								unitname = (String) vo.getAttributeValue("unitname");
								unitname = (unitname == null ? errordisplayText: unitname.trim());
							}
						}
					}
					throw new Exception(unitname
							+ nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
							.getStrByID("600704", "UPP600704-000126")/*
							 * @res
							 * "���������֤����Ϊ "
							 */
							+ id
							+ nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
							.getStrByID("600704", "UPP600704-000127")/*
							 * @res
							 * ",��Ա����Ϊ "
							 */
							+ psnname
							+ nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
							.getStrByID("600704", "UPP600704-000128")/*
							 * @res
							 * ",��Ա����Ϊ "
							 */
							+ psncode
							+ nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
							.getStrByID("600704", "UPP600704-100001")/*
							 * @res
							 * "�ļ�¼,������ "
							 */
							+ deptname
							+ nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
							.getStrByID("600704", "UPP600704-100002")/*
							 * @res
							 * "����, "
							 */
							+ omjob
							+ nc.vo.ml.NCLangRes4VoTransl.getNCLangRes()
							.getStrByID("600704", "UPP600704-100003")/*
							 * @res
							 * "��λ "
							 */);
				}
			}
		}

		if (vException != null) {
			traceErrorPoint(vException);
		}
		return true;
	}

	/**
	 * У��ǿա� 
	 * �������ڣ�(2005-5-9 11:49:00)
	 * @param tablecode java.lang.String
	 * @param vos nc.vo.hi.hi_301.GeneralVO[]
	 */
	public boolean dataNotNullValidate(String tablecode, GeneralVO[] vos,
			int curRow) throws java.lang.Exception { 
		String message = null;
		ValidException vException = null;// wangkf add
		if ("bd_psndoc,bd_psnbasdoc".indexOf(tablecode) >= 0) {
			BillItem[] headItems = getCard().getBillData().getHeadTailItems();
			if (headItems != null && headItems.length > 0) {
				for (int i = 0; i < headItems.length; i++) {
					BillItem item = headItems[i];
					if (!item.isNull() || !isNULL(item.getValue()))
						continue;
					if (item.getKey().equalsIgnoreCase("psncode")
							&& isPsncodeAutoGenerate())
						continue;
					message = item.getName();
					message += nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"600704", "UPP600704-000156")/*
							 * @res "����Ϊ��"
							 */;
					// wangkf add
					vException = new ValidException(message);
					vException.setTableCode(item.getTableCode());
					vException.setFieldCode(item.getKey());
					vException.setLineNo(curRow);// -1
					break;
				}
			}
			// �ɼ��ڵ㣬������ò��ŵ���Ȩ�ޣ��������벿��
			if(isUsedDeptPower==null)isUsedDeptPower = GlobalTool.isUsedDataPower("bd_deptdoc", "���ŵ���",Global.getCorpPK());
			if ("600704".equalsIgnoreCase(getModuleName()) && isUsedDeptPower) {
				BillItem item = getCard().getHeadItem("pk_deptdoc");
				if (item == null || isNULL(item.getValue())) {
					message = item.getName();
					message += nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"600704", "UPP600704-000156")/*
							 * @res "����Ϊ��"
							 */;
					// wangkf add
					vException = new ValidException(message);
					vException.setTableCode(item.getTableCode());
					vException.setFieldCode(item.getKey());
					vException.setLineNo(curRow);// -1
				}
			}
		} else {
			BillItem[] bodyItems = getCard().getBillData()
			.getBodyItemsForTable(tablecode);
			if (bodyItems != null && bodyItems.length > 0) {
				for (int j = 0; j < bodyItems.length; j++) {
					BillItem item = bodyItems[j];
					Object value = vos[0].getAttributeValue(item.getKey());
					if (!item.isNull() || !isNULL(value))
						continue;
					message = item.getName();
					message += nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"600704", "UPP600704-000156")/* @res "����Ϊ��" */;
					vException = new ValidException(message);
					vException.setTableCode(item.getTableCode());
					vException.setFieldCode(item.getKey());
					vException.setLineNo(curRow);
					break;
				}
			}
		}
		if (vException != null) {
			traceErrorPoint(vException);
		}
		return true;
	}

	/**
	 * У��ǿա� 
	 * �������ڣ�(2005-5-9 11:49:00)
	 * @param tablecode  java.lang.String
	 * @param vos  nc.vo.hi.hi_301.GeneralVO[]
	 */
	public boolean dataNotNullValidateforRef(int curRow)
	throws java.lang.Exception {
		String message = null;
		ValidException vException = null;
		BillItem[] headItems = getCard().getBillData().getHeadTailItems();
		final String keys = "psncode,psnname,pk_psncl,pk_deptdoc";
		if (headItems != null) {
			for (int i = 0; i < headItems.length; i++) {
				if (keys.indexOf(headItems[i].getKey()) < 0)
					continue;
				if (!headItems[i].isNull() || !isNULL(headItems[i].getValue()))
					continue;

				if (headItems[i].getKey().equalsIgnoreCase("psncode")
						&& isPsncodeAutoGenerate()) {
					continue;
				}
				message = headItems[i].getName();
				message += nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"600704", "UPP600704-000156")/*
						 * @res "����Ϊ��"
						 */;
				// wangkf add
				vException = new ValidException(message);
				vException.setTableCode(headItems[i].getTableCode());
				vException.setFieldCode(headItems[i].getKey());
				vException.setLineNo(curRow);// -1
				break;
			}
		}

		if (vException != null) {
			traceErrorPoint(vException);
		}
		return true;
	}

	/**
	 * ��queryResult�н���ǰpsnList�ж�Ӧ��Աȫ��ɾ���� ������Աת���뵵���󣬽���ǰ��Աɾ���� 
	 * �������ڣ�(2004-5-25 13:45:37)
	 */
	private void deleteListFromResult(GeneralVO[] curIntoDocData) {
		// ��ɾ����Ľ�����浽v
		Vector v = new Vector();
		// ����queryResult�е�ÿһ��Ԫ��,��psnList���ҵ�ǰԪ��
		for (int i = 0; i < queryResult.length; i++) {
			int j;
			for (j = 0; j < curIntoDocData.length; j++) {
				if (curIntoDocData[j].getAttributeValue("pk_psndoc").equals(
						queryResult[i].getAttributeValue("pk_psndoc")))
					break;
			}
			// ���psnList��������ǰԪ�أ���ӵ�v
			if (j == curIntoDocData.length)
				v.addElement(queryResult[i]);
		}

		// ת��������
		queryResult = (GeneralVO[]) v.toArray(new GeneralVO[0]);
	}

	/**
	 * ������persons���ҳ���Ա������pk_psndoc����Ա������֮ɾ���� 
	 * �������ڣ�(2004-5-19 11:05:14)
	 * @return nc.vo.hi.hi_301.GeneralVO[]
	 * @param persons
	 *            nc.vo.hi.hi_301.GeneralVO[]
	 * @param pk_psndoc
	 *            java.lang.String
	 */
	protected GeneralVO[] deletePsnFromArray(GeneralVO[] persons, String pk_psndoc) {
		// ˳�����
		int i;
		for (i = 0; i < persons.length; i++) {
			String pk = (String) persons[i].getAttributeValue("pk_psndoc");
			if (pk != null && pk.equals(pk_psndoc)) {
				break;
			}
		}

		// û�в��Ҹ��ˣ���ֱ�ӷ���ԭ������
		if (i == persons.length)
			return persons;

		// ���鳤����С1������ͨ��arraycopy��Чɾ����Ԫ�ء�
		GeneralVO[] newPersons = new GeneralVO[persons.length - 1];
		System.arraycopy(persons, 0, newPersons, 0, i);
		System.arraycopy(persons, i + 1, newPersons, i, persons.length - i - 1);

		return newPersons;
	}

	/**
	 * Comment
	 */
	public void deptTreeMouseClicked(java.awt.event.MouseEvent mouseEvent)throws Exception {
		try {
			if (mouseEvent.getClickCount() == 2) {
				// System.out.println("˫���¼�");
				TreePath path = getDeptTree().getPathForLocation(mouseEvent.getX(), mouseEvent.getY());
				if (path == null)
					return;
				DefaultMutableTreeNode node = getSelectedNode();
				if (node == null) {
					return;
				}
				CtrlDeptVO corpvo = (CtrlDeptVO) node.getUserObject();
				if (!corpvo.isControlled()|| corpvo.nodeType == CtrlDeptVO.DEPT) {
					return;
				}
				if (corpvo.isLoadDept()) {
					return;
				}
				Util.getTopFrame(this).setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				corpvo.removeAllDeptChild();
				String strDeptPower = getDeptPowerByCorp(corpvo.getPk_corp());
				boolean isUsedPower = strDeptPower.length() > 3;

				corpvo.setModuleCode(getModuleName());
				boolean includeHrCanceld = getincludeCancleDept().isSelected();//v53
				corpvo = HIDelegator.getPsnInf().queryCorpCtrlDepts(Global.getUserID(), corpvo, isUsedPower,includeHrCanceld);
				node.setUserObject(corpvo);
				corpvo.setLoadDept(true);
				addDeptNodeToTree(node, corpvo);

				getDeptTreeModel().reload(node);
				getDeptTree().setCellRenderer(new DeptTreeCellRender());
				getDeptTree().expandPath(path);
				Util.getTopFrame(this).setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			}
		} catch (Exception e) {
			Util.getTopFrame(this).setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
			reportException(e);
			showErrorMessage(e.getMessage());
		}
	}
	/**
	 * �������Ľڵ㱻ѡ��ʱ�������¼�����
	 */
	protected void deptTreeValueChanged(
			javax.swing.event.TreeSelectionEvent treeSelectionEvent)
	throws java.lang.Exception {
		// Ĭ����Ҫ����ˢ��
		deptTreeValueChanged(treeSelectionEvent, true);
	}

	/**
	 * �������Ľڵ㱻ѡ��ʱ�������¼�����
	 */
	protected void deptTreeValueChanged(javax.swing.event.TreeSelectionEvent treeSelectionEvent,boolean isNeedQuery)throws java.lang.Exception {
		// ��ǰѡ�еĽڵ�
		DefaultMutableTreeNode node = getSelectedNode();
		if (node == null)
			return;
		// �����ǰ�ڵ�Ϊ���ɿ��Ʋ��ţ����
		if (queryDlg != null && isQuery && isNeedQuery) {
			queryResult();
		}
		// add by sunxj 2010-02-03 ���ٲ�ѯ���  start
		else if(isQuickSearch && isNeedQuery){
			queryResult(quickWherePart);
		}
		// add by sunxj 2010-02-03 ���ٲ�ѯ���  end
		psnList = queryResult;
		// 
		getPsnList().setBodyData(psnList);
		getPsnList().getTableModel().execLoadFormula();

		String recordCountDecrip = nc.ui.ml.NCLangRes.getInstance().getStrByID("600704", "UPP600704-000192")/* @res "����" */;
		String tiao = nc.ui.ml.NCLangRes.getInstance().getStrByID("600704","UPP600704-000240")/* @res "��" */;
		//Integer maxline = PubDelegator.getIParValue().getParaInt("0001", "HI_MAXLINE");//Ч���Ż�
		Integer maxline =Integer.parseInt(getPara("HI_MAXLINE"));
		String max = ","
			+ nc.ui.ml.NCLangRes.getInstance().getStrByID("600704","UPP600704-000239")/* @res "��ʾǰ" */
			+ (maxline ==null?1000:maxline.intValue())
			+ nc.ui.ml.NCLangRes.getInstance().getStrByID("600704","UPP600704-000240")/* @res "��" */;
		if (recordcount <= (maxline==null?1000:maxline.intValue())) {
			max = "";
		}
		getUILabelRecords().setText(recordCountDecrip + recordcount + tiao + max + "   ");

		setTreeSelectButtonState(node);
	}

	/**
	 * ���õ�ǰ�ӱ�ĵ�no�пɱ༭�� �������ڣ�(2004-5-26 21:22:27)
	 * 
	 * @param no
	 *            int
	 */
	private void enableTableLine(int no) {
		// ��ֹ�������ܱ༭
		setTableLineEditable(getCard().getBillModel(), -1, false);

		// ���ø��пɱ༭
		setTableLineEditable(getCard().getBillModel(), no, true);

		// ���õ�ǰֵ������������
		String tableCode = getBodyTableCode();
		String[] seqFields = (String[]) triggerTable.fieldAOE.get(tableCode);

		// ���û�д����������˳�
		if (seqFields == null)
			return;

		// ��õ�ǰ��¼
		CircularlyAccessibleValueObject record = person.getTableVO(tableCode)[getEditLineNo()];
		for (int i = 0; i < seqFields.length; i++) {
			BillItem item = getBillItem(tableCode + "." + seqFields[i]);
			if (item == null)
				continue;
			if (item.getDataType() == IBillItem.UFREF) {
				// ����ǲ������ͣ�����Ҫ����
				// �����������������ֵʱ���Ѿ���������������UIRefPane����ֵʱ�����Զ������¼���ɵ�
				Object v = record.getAttributeValue(Util.PK_PREFIX + item.getKey());
				if (v == null)
					continue;
				((UIRefPane) item.getComponent()).setPK(v);
			}
		}
	}

	/**
	 * ���õ�ǰ�ӱ�ĵ�no�пɱ༭�� wangkf add �༭�е��ô˷���
	 * 
	 * @param no
	 *            int
	 */
	private void enableTableLineModify(int no) {
		// ��ֹ�������ܱ༭
		setTableLineEditable(getCard().getBillModel(), -1, false);

		// ���ø��пɱ༭
		setTableLineEditable(getCard().getBillModel(), no, true);
	}

	/**
	 * V31SP1 ��Ƭ���� ���ܡ� �������ڣ�(2005-4-28 14:29:10)
	 */
	public void exportPic() {
		GeneralVO[] psnPic = new GeneralVO[0];
		String strFileName = "";
		try {
			// �ж��Ƿ�ѡ������
			int row = getPsnList().getTable().getSelectedRow();
			if (row != -1 && row < psnList.length) {
				// ȡ��ǰ��Ա������
				String[] keys = getTempPsnPk();
				String pk_psnbasdoc = keys[0];
				// ��ǰ��¼��˾
				String corp = Global.getCorpPK();
				// ������Ա����
				psnPic = HIDelegator.getPsnInf().queryByPsnPK(corp,pk_psnbasdoc);
				if (psnPic == null || psnPic.length <= 0) {
					String photoTitle = nc.ui.ml.NCLangRes.getInstance().getStrByID("600704", "UPP600704-000017")/* @res "����" */;
					String photoMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("600704", "UPP600704-000183")/* @res "��Ա��û����Ƭ" */;
					nc.ui.pub.beans.MessageDialog.showHintDlg(this, photoTitle,photoMsg);
					return;
				}
			} else {
				return;
			}
			// ��ȡ���ʵ��ļ�����:����+����+��������
			strFileName = "/" + strFileName+ psnList[row].getAttributeValue("psncode").toString();
			strFileName = strFileName+ psnList[row].getAttributeValue("psnname").toString();
			String photoDeptName = (psnList[row].getAttributeValue("deptname") == null ? ""
					: psnList[row].getAttributeValue("deptname").toString());
			strFileName = strFileName + photoDeptName;
			strFileName = strFileName + ".jpg";
			File file = new File(strFileName);
			getFileChooser().setFileSelectionMode(javax.swing.JFileChooser.FILES_AND_DIRECTORIES);
			getFileChooser().setSelectedFile(file);
			int userOperate = getFileChooser().showSaveDialog(this);
			if (userOperate == 0) {// 0:����,1:����
				file = getFileChooser().getSelectedFile();
				if (file == null) {// �����ߵ��˷�֧--wangkf ˵��
//					nc.ui.pub.beans.MessageDialog.showHintDlg(this, "����","��û��ѡ��Ҫ���ص�Ŀ¼��");
					nc.ui.pub.beans.MessageDialog.showHintDlg(this, 
							nc.ui.ml.NCLangRes.getInstance().getStrByID("600700", 
									"UPP600700-000108")/* @res "����" */, 
							nc.ui.ml.NCLangRes.getInstance().getStrByID("600700", 
									"UPP600700-000109")/* @res "��û��ѡ��Ҫ���ص�Ŀ¼��" */
					);
					return;
				}
				byte[] data = (byte[]) psnPic[0].getFieldValue("photo");
				FileOutputStream outstream = new FileOutputStream(file);
				outstream.write(data);
				outstream.flush();
				outstream.close();
			}
		} catch (Exception e) {
			reportException(e);
		}
		return;
	}

	/**
	 * �Ӳ�ѯ���queryResult������Ա����Ϊpk_psndoc����Ա�����ظ�����queryResult�е������� 
	 * �������ڣ�(2004-5-20 14:27:15)
	 * @return int
	 * @param psn  nc.vo.hi.hi_301.GeneralVO
	 */
	private int findPersonInQueryResult(String pk_psndoc) {
		// ���queryResult������-1
		if (queryResult == null){
			return -1;
		}
		// ��Ա������null,����-1
		if (pk_psndoc == null){
			return -1;
		}
		// ˳����Ҹ���
		for (int i = 0; i < queryResult.length; i++) {
			String pk = (String) queryResult[i].getAttributeValue("pk_psndoc");
			if (pk != null && pk.equals(pk_psndoc))
				return i;
		}
		// û���ҵ�,����-1
		return -1;
	}

	/**
	 * V35 add wangkf ��ָ���������� �����������ݵ�λ��
	 * @param pk
	 * @param pk_field
	 * @param datas
	 * @return
	 */
	protected int findPersonPos(String pk, String pk_field, GeneralVO[] datas) {
		if (datas == null) {
			return -1;
		}
		if (pk == null) {
			return -1;
		}
		// ˳����Ҹ���
		for (int i = 0; i < datas.length; i++) {
			String pk_src = (String) datas[i].getAttributeValue(pk_field);
			if (pk_src != null && pk_src.equals(pk))
				return i;
		}
		return -1;
	}

	/**
	 * ������¼�� 
	 * �������ڣ�(2004-7-14 19:12:37)
	 * @exception java.lang.Exception �쳣˵����
	 */
	public void freeLockPsn(String psnpk) throws java.lang.Exception {
		try {
			PKLock.getInstance().releaseLock(psnpk,nc.ui.hr.global.Global.getUserID(), null);
		} catch (Exception e) {
			reportException(e);
			throw e;
		}
	}

	/**
	 * �õ����е���������ʽ���� Ҳ���Ƿ������ж����������ı��ʽ
	 */
	public java.lang.String[] getAllDataItemExpress() {
		return PersonEAVO.mainFields;
	}

	public java.lang.String[] getAllDataItemNames() {
		return null;
	}

	/**
	 * @return ���� attributesOfSort��
	 */
	public Vector getAttributesOfSort() {
		/** ��������* */
		java.util.Vector attributes = null;
		DefaultMutableTreeNode node = getSelectedNode();
		if (node == null) {
			attributes = (java.util.Vector) hmSortCondition.get("all");
		} else {
			CtrlDeptVO dept = getSelectedDept();
			if (node.isRoot()) {
				attributes = (java.util.Vector) hmSortCondition.get("all");
			} else if (dept.isControlled()) {
				if (dept.getNodeType() == CtrlDeptVO.DEPT) {
					attributes = (java.util.Vector) hmSortCondition.get(dept.getPk_dept());
				} else if (dept.getNodeType() == CtrlDeptVO.CORP) {
					attributes = (java.util.Vector) hmSortCondition.get(dept.getPk_corp());
				}
			}
		}
		return attributes;
	}

	/**
	 * �˴����뷽�������� 
	 * �������ڣ�(2004-5-30 16:15:45)
	 * @return nc.ui.hi.hi_301.BatchUpdateDlg
	 * @exception java.lang.Exception  �쳣˵����
	 */
	protected BatchUpdateDlg getBatchDlg() throws java.lang.Exception {
		if (batchDlg == null) {
			batchDlg = new BatchUpdateDlg(this, nc.ui.ml.NCLangRes
					.getInstance().getStrByID("600704", "upt600704-000164")/*
					 * @res
					 * "�����޸�"
					 */);
			batchDlg.setLocationRelativeTo(this);
		}
		return batchDlg;
	}

	/**
	 * ��ȡ����ģ����ֶ�ӳ����Ӹ�ӳ������ܹ����ٲ�ѯ���ֶε����ԡ� 
	 * �������ڣ�(2004-5-25 14:19:39)
	 * @return java.util.Hashtable
	 */
	private Hashtable getBillBodyMap() {
		return getCard().getBillBodyMap();
	}

	/**
	 * ����tablefield��ø��ֶε�BillItem�� 
	 * @param tablefield
	 * @return
	 */
	private BillItem getBillItem(String tablefield) {
		int dot = tablefield.indexOf(".");
		String tableCode = tablefield.substring(0, dot);
		String fieldCode = tablefield.substring(dot + 1);
		if (headTables.get(tableCode) != null)
			return getCard().getHeadItem(fieldCode);
		else
			return getCard().getBodyItem(tableCode, fieldCode);
	}

	/**
	 * ���ص�ǰ�ڵ�ĵ���ģ�����͡� 
	 * �������ڣ�(2004-5-12 20:50:29)
	 * @return java.lang.String
	 */
	protected String getBillType() {
		return getModuleName();
	}

	/**
	 * ���ص�ǰ����ѡ�е����������ơ� 
	 * �������ڣ�(2004-5-26 14:26:22)
	 * @return java.lang.String
	 */
	protected String getBodyTableCode() {
		return getCard().getBodyTableCode(getCard().getBodyPanel());
	}

	/**
	 * ���ص�ǰ����ѡ�е���������ʾ���ơ� 
	 * �������ڣ�(2004-5-25 16:11:23)
	 * @return java.lang.String
	 */
	private String getBodyTableName() {
		return getCard().getBillData().getBodyTableName(getBodyTableCode());
	}

	/**
	 * ���ص�ǰ���ݡ� 
	 * �������ڣ�(2004-5-11 21:08:01)
	 * @return nc.ui.hi.hi_301.HIBillCardPanel
	 */
	protected HIBillCardPanel getCard() {
		// ��ǰ�ڵ�����Ա��Ϣ�ɼ��ڵ�ʹ����ְģ��
		return getWorkCard();
	}

	/**
	 * ���� CenterPanel ����ֵ��
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* ���棺�˷������������ɡ� */
	private nc.ui.pub.beans.UIPanel getCenterPanel() {
		if (ivjCenterPanel == null) {
			try {
				ivjCenterPanel = new nc.ui.pub.beans.UIPanel();
				ivjCenterPanel.setName("CenterPanel");
				ivjCenterPanel.setLayout(new java.awt.BorderLayout());
				getCenterPanel().add(getUISplitPane(), "Center");
				ivjCenterPanel.addComponentListener(new ComponentAdapter() {
					public void componentResized(ComponentEvent event) {
						if (isShowBillTemp) {// ����ǿ�Ƭ����
							getUISplitPane().setDividerLocation(0);
						}
					}
				});
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjCenterPanel;
	}

	/**
	 * ���� CenterPanelRight ����ֵ��
	 * @return nc.ui.pub.beans.UIPanel
	 */
	/* ���棺�˷������������ɡ� */
	protected nc.ui.pub.beans.UIPanel getCenterPanelRight() {
		if (ivjCenterPanelRight == null) {
			try {
				ivjCenterPanelRight = new nc.ui.pub.beans.UIPanel();
				ivjCenterPanelRight.setName("CenterPanelRight");
				ivjCenterPanelRight.setLayout(new java.awt.BorderLayout());
				getCenterPanelRight().add(getUISplitPaneVertical(), "Center");
				ivjCenterPanelRight
				.addComponentListener(new ComponentAdapter() {
					public void componentResized(ComponentEvent event) {
						if (splitLocation == SPLIT_MAX
								&& (getIsSelSet().isVisible())) {// ������б����
							getUISplitPaneVertical()
							.setDividerLocation(SPLIT_MAX);
						}
						if (isShowBillTemp) {// ����ǿ�Ƭ����
							getUISplitPaneVertical().setDividerLocation(0);
						}
					}
				});
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjCenterPanelRight;
	}

	/**
	 * ������rootΪ���������ڵ� 
	 * �������ڣ�(2004-5-10 15:13:57)
	 * @return javax.swing.tree.DefaultMutableTreeNode
	 * @param root  nc.vo.hi.hi_301.CtrlDeptVO
	 */
	protected DefaultMutableTreeNode getChildTree(CtrlDeptVO root) {
		// ��װ��
		DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(root);
		// ��Ӻ���
		Vector children = root.getChildren();
		if (children != null) {
			for (int i = 0; i < children.size(); i++) {
				CtrlDeptVO vo = (CtrlDeptVO) children.elementAt(i);
				if (vo.isControlled()) {
					DefaultMutableTreeNode childNode = getChildTree((CtrlDeptVO) children.elementAt(i));
					rootNode.add(childNode);
				}
			}
		}
		return rootNode;
	}



	/**
	 * ���� CmbJobType ����ֵ��
	 * @return nc.ui.pub.beans.UIComboBox
	 */
	/* ���棺�˷������������ɡ� */
	protected nc.ui.pub.beans.UIComboBox getCmbJobType() {
		if (ivjCmbJobType == null) {
			try {
				ivjCmbJobType = new nc.ui.pub.beans.UIComboBox();
				ivjCmbJobType.setName("CmbJobType");
				ivjCmbJobType.setFont(new java.awt.Font("dialog", 0, 12));
				ivjCmbJobType.setPreferredSize(new java.awt.Dimension(60, 20));
				ivjCmbJobType.setTranslate(true);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjCmbJobType;
	}

	/**
	 * ���� CmbPsncl ����ֵ��
	 * @return nc.ui.pub.beans.UIComboBox
	 */
	protected nc.ui.pub.beans.UIComboBox getCmbPsncl() {
		if (ivjCmbPsncl == null) {
			try {
				ivjCmbPsncl = new nc.ui.pub.beans.UIComboBox();
				ivjCmbPsncl.setName("CmbPsncl");
				ivjCmbPsncl.setFont(new java.awt.Font("dialog", 0, 12));
				ivjCmbPsncl.setPreferredSize(new java.awt.Dimension(80, 20));
				ivjCmbPsncl.setTranslate(true);				
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjCmbPsncl;
	}

	/**
	 * ˢ�����򴰿ڵ��ֶ��б�
	 */
	private void refreshSortDialogField(){
		if (configDialog==null) return;
		Pair[] items = getListItems();
		Vector vecPair = new Vector();
		if (items != null && items.length > 0) {
			for (int i = 0; i < items.length; i++) {
				// ��ְ���͡���Ա��𲻼�������
				// modified by zhangdd, �޸�ʹ����Ա�������ʱ����ʹ������
				if ("jobtypename".equals(items[i].getCode())/* || "psnclassname".equals(items[i].getCode())*/)
					continue;
				String fieldcode = null;
				if ("pk_psncl".equals(items[i].getCode())) {
					fieldcode = "bd_psncl.psnclassname";
				} else {
					if (items[i].getTablecode() == null) {
						fieldcode = items[i].getCode();
					} else {
						fieldcode = items[i].getTablecode() + "." + items[i].getCode();
					}
				}
				nc.ui.hr.comp.sort.Pair fieldPair = new nc.ui.hr.comp.sort.Pair(fieldcode, items[i].getName());
				vecPair.addElement(fieldPair);
			}
		}
		//������Щ����Ҫ���ӹ�˾˳��ŵȡ�
		String[] fieldcodes = new String[]{"bd_corp.showorder","bd_corp.innercode","bd_deptdoc.showorder","bd_deptdoc.innercode"};
		String[] fieldnames = new String[]{nc.ui.ml.NCLangRes.getInstance().getStrByID("6007","UPT6007-000229")/*"��˾˳���"*/,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("6007","UPT6007-000231")/*"��˾���κ�"*/,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("6007","UPT6007-000230")/*"����˳���"*/,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("6007","UPT6007-000232")/*"���ż��κ�"*/};
		for(int j=0; j<fieldcodes.length;j++){
			vecPair.addElement(new nc.ui.hr.comp.sort.Pair(fieldcodes[j],fieldnames[j]));
		}
		nc.ui.hr.comp.sort.Pair[] listSortItems = new nc.ui.hr.comp.sort.Pair[vecPair
		                                                                      .size()];
		vecPair.copyInto(listSortItems);
		configDialog.setFields(listSortItems);
	}
	/**
	 * �˴����뷽�������� 
	 * �������ڣ�(2004-8-16 9:25:33)
	 * @return nc.ui.hi.hi_301.SortConfigDialog
	 */
	public SortConfigDialog getConfigDialog() {
		if (configDialog!=null) return configDialog;

		Pair[] items = getListItems();
		Vector vecPair = new Vector();
		if (items != null && items.length > 0) {
			for (int i = 0; i < items.length; i++) {
				// ��ְ���͡���Ա��𲻼�������
				// modified by zhangdd, �޸�ʹ����Ա�������ʱ����ʹ������
				if ("jobtypename".equals(items[i].getCode())/* || "psnclassname".equals(items[i].getCode())*/)
					continue;
				String fieldcode = null;
				if ("pk_psncl".equals(items[i].getCode())) {
					fieldcode = "bd_psncl.psnclassname";
				} else {
					if (items[i].getTablecode() == null) {
						fieldcode = items[i].getCode();
					} else {
						fieldcode = items[i].getTablecode() + "." + items[i].getCode();
					}
				}
				nc.ui.hr.comp.sort.Pair fieldPair = new nc.ui.hr.comp.sort.Pair(fieldcode, items[i].getName());
				vecPair.addElement(fieldPair);
			}
		}
		//������Щ����Ҫ���ӹ�˾˳��ŵȡ�
		String[] fieldcodes = new String[]{"bd_corp.showorder","bd_corp.innercode","bd_deptdoc.showorder","bd_deptdoc.innercode"};
		String[] fieldnames = new String[]{nc.ui.ml.NCLangRes.getInstance().getStrByID("6007","UPT6007-000229")/*"��˾˳���"*/
				,nc.ui.ml.NCLangRes.getInstance().getStrByID("6007","UPT6007-000231")/*"��˾���κ�"*/,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("6007","UPT6007-000230")/*"����˳���"*/,
				nc.ui.ml.NCLangRes.getInstance().getStrByID("6007","UPT6007-000232")/*"���ż��κ�"*/};
		for(int j=0; j<fieldcodes.length;j++){
			vecPair.addElement(new nc.ui.hr.comp.sort.Pair(fieldcodes[j],fieldnames[j]));
		}
		nc.ui.hr.comp.sort.Pair[] listSortItems = new nc.ui.hr.comp.sort.Pair[vecPair.size()];
		vecPair.copyInto(listSortItems);
		configDialog = new SortConfigDialog(Util.getTopFrame(this), true);
		configDialog.setAllowNullSort(true);
		configDialog.setFields(listSortItems);
		configDialog.setModuleCode(getModuleName());

		configDialog.setLocationRelativeTo(this);
		configDialog.btnLoad_ActionPerformed(null);
		if(configDialog.getSortingFields()==null || configDialog.getSortingFields().size()<1){
			Vector<Attribute> defaultSortingFields = new Vector<Attribute>();
			defaultSortingFields.add(new Attribute(new nc.ui.hr.comp.sort.Pair("bd_corp.showorder",nc.ui.ml.NCLangRes.getInstance().getStrByID("6007","UPT6007-000229")/*"��˾˳���"*/),true));
			defaultSortingFields.add(new Attribute(new nc.ui.hr.comp.sort.Pair("bd_deptdoc.showorder",nc.ui.ml.NCLangRes.getInstance().getStrByID("6007","UPT6007-000230")/*"����˳���"*/),true));
			defaultSortingFields.add(new Attribute(new nc.ui.hr.comp.sort.Pair("bd_psndoc.showorder",nc.ui.ml.NCLangRes.getInstance().getStrByID("600704","UPT600704-000325")/*"��Ա˳���"*/),true));
			configDialog.setSortingFields(defaultSortingFields);
			configDialog.btnLoad_ActionPerformed(null);
		}
		return configDialog;
	}

	/**
	 * ��ȡ��ǰ�������ĸ���㡣 
	 * �������ڣ�(2004-5-10 8:41:00)
	 * @return nc.vo.hi.hi_301.CtrlDeptVO
	 */
	protected nc.vo.hi.hi_301.CtrlDeptVO getCtrlDeptRoot() {
		return ctrlDeptRoot;
	}

	/**
	 * �õ���ǰ��ѯ��صĲ�ѯ����
	 * @return
	 */
	protected ConditionVO[] getCurConditions() {
		ConditionVO[] cvos = null;
		// û��ѡ�����ڵ�
		if (getSelectedNode() == null) {
			cvos = conditions;
		}
		// ���ѡ�еĽڵ��ǲ��Žڵ���Ҫ��Ӳ�������
		CtrlDeptVO deptvo = (CtrlDeptVO) (getSelectedNode().getUserObject());
		if (deptvo.getNodeType() == CtrlDeptVO.DEPT) {
			ConditionVO vo = new ConditionVO();
			vo.setDataType(BillItem.UFREF);
			vo.setTableCode("bd_psndoc");
			vo.setFieldCode("bd_psndoc.pk_deptdoc");
			vo.setOperaCode("=");
			vo.setValue(deptvo.getPk_dept());
			if (conditions == null) {
				cvos = new ConditionVO[1];
				cvos[0] = vo;
			} else {
				cvos = new ConditionVO[conditions.length + 1];
				System.arraycopy(conditions, 0, cvos, 0, conditions.length);
				cvos[conditions.length] = vo;
			}
		} else {// ѡ���˸��ڵ�
			cvos = conditions;
		}
		Vector vBookTemp = new Vector();
		ConditionVO[] cvosAdd = null;
		ConditionVO[] cvosNew = null;
		if (vBookConds.size() > 0) {
			for (int i = 0; i < vBookConds.size(); i++) {
				ConditionVO vo = new ConditionVO();
				vo.setLogic(false);
				vo.setDataType(BillItem.UFREF);
				vo.setTableCode("bd_psndoc");
				vo.setFieldCode("bd_psndoc.pk_psndoc");
				vo.setOperaCode("=");
				vo.setValue((String) vBookConds.elementAt(i));
				vBookTemp.addElement(vo);
			}
			cvosAdd = new ConditionVO[vBookTemp.size()];
			vBookTemp.copyInto(cvosAdd);
			if (cvos == null) {
				cvos = cvosAdd;
			} else {
				cvosNew = new ConditionVO[cvos.length + cvosAdd.length];
				System.arraycopy(cvos, 0, cvosNew, 0, cvos.length);
				for (int c = 0; c < cvosAdd.length; c++) {
					cvosNew[cvos.length + c] = cvosAdd[c];
				}
				cvos = cvosNew;
			}
		}
		// wangkf add ���Ա� ��Ӣ��/���� �ĳ� ��������
		if (cvos != null) {
			for (int c = 0; c < cvos.length; c++) {
				if (cvos[c] != null&& "bd_psnbasdoc.sex".equals(cvos[c].getFieldCode())
						&& "male".equals(cvos[c].getValue())) {
					cvos[c].setValue("��");
				} else if (cvos[c] != null&& "bd_psnbasdoc.sex".equals(cvos[c].getFieldCode())
						&& "female".equals(cvos[c].getValue())) {
					cvos[c].setValue("Ů");
				} else if (cvos[c] != null&& "hi_psndoc_ctrt.iconttype".equals(cvos[c].getFieldCode())) {
					int index = cvos[c].getComboIndex();
					if (index > 1) {
						index++;
					}
					cvos[c].setValue(Integer.toString(index));
				}
			}
		}
		return cvos;
	}

	/**
	 * ������������������飬���������ֻ��Ϊ 1 ���� 2 
	 * ���� null : û������ ���� 1 : �������� ���� 2 : ˫������
	 */
	public java.lang.String[] getDependentItemExpressByExpress(
			java.lang.String itemName) {
		return null;
	}

	/**
	 * �õ�������˾�Ƿ������˲��ֵ���Ȩ�ޡ� 
	 * �������ڣ�(2004-10-22 15:36:49)
	 * @return java.lang.String
	 * @param pk_corp java.lang.String
	 */
	public String getDeptPowerByCorp(String corppk) throws Exception {
		String power = (String) deptPowerlist.get(corppk);
		if (power != null){
			return power;
		}
		
		//add by lianglj 2012-11-27 ���Ӳ��������ϼ���˾�Ƿ���Բ鿴�¼����ŵ���Ȩ�޲���
		//false,�������ӹ�˾��true��Ȩ�޿���
		boolean isdeptpwflag = PubDelegator.getIParValue().getParaBoolean(Global.getCorpPK(), "HI_CDEPTCONTROL").booleanValue();
		//����������������¼���˾�������Ʋ���Ȩ�ޡ�dusx 2008.11.24
		if (!isdeptpwflag&&!corppk.equals(Global.getCorpPK())){
			deptPowerlist.put(corppk, "0=0");
			return "0=0";
		}
		if (isdeptpwflag&&!"0001".equals(Global.getCorpPK())) {
			boolean useDeptPower = GlobalTool.isUsedDataPower("bd_deptdoc",
					"���ŵ���", corppk);
			if (useDeptPower /* && !"600704".equals(getModuleName()) */) {
				power = GlobalTool.getPowerSql("bd_deptdoc",
						Global.getUserID(), corppk);
				if(power==null){
					power = "0=0";
				}
				deptPowerlist.put(corppk, power);
				return power;
			} else {
				deptPowerlist.put(corppk, "0=0");
				return "0=0";
			}
			
		} 
		
		return "0=0";
	}

	/**
	 * ���� DeptTree ����ֵ��
	 * @return nc.ui.pub.beans.UITree
	 */
	protected nc.ui.pub.beans.UITree getDeptTree() {
		if (ivjDeptTree == null) {
			try {
				ivjDeptTree = new nc.ui.pub.beans.UITree();
				ivjDeptTree.setName("DeptTree");
				ivjDeptTree.setBounds(0, 0, 160, 120);				
				ivjDeptTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjDeptTree;
	}

	/**
	 * ����ctrlDeptRoot������model�� 
	 * �������ڣ�(2004-5-10 15:08:02)
	 * @return javax.swing.tree.DefaultTreeModel
	 */
	protected DefaultTreeModel getDeptTreeModel() {
		if (treemodel == null) {
			// �����ǰ������Ϊ�գ����ؿ�
			if (ctrlDeptRoot == null){
				return null;
			}
			// ����getChildTree��֯��
			DefaultMutableTreeNode root = getChildTree(ctrlDeptRoot);
			// ��װ������model����
			treemodel = new DefaultTreeModel(root);
		}
		return treemodel;
	}

	protected void setDeptTreeModel(DefaultTreeModel newtreemodel){
		this.treemodel = newtreemodel;
	}
	/**
	 * ���� DismissCard ����ֵ��
	 * @return nc.ui.hi.hi_301.HIBillCardPanel
	 */
	protected HIBillCardPanel getDismissCard() {
		if (ivjDismissCard == null) {
			try {
				ivjDismissCard = new nc.ui.hi.hi_301.HIBillCardPanel(allRelatedPara);
				ivjDismissCard.setName("DismissCard");				
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjDismissCard;
	}

	/**
	 * ��ȡ��ǰ���ڱ༭������� �������ڣ�(2004-5-17 11:35:26)
	 * 
	 * @return java.lang.String[]
	 */
	private String[] getEditingTables() throws Exception {
		// ������ڱ༭��ͷ���������б�ͷ�������
		// ����ֻ���浱ǰ���ڱ༭��
		if (getEditType() == EDIT_MAIN || getEditType() == EDIT_RETURN) {
			return getCard().getBillData().getHeadTableCodes();
		} else {
			return new String[] { getBodyTableCode() };
		}
	}

	/**
	 * ��ȡ��ǰ���������ڱ༭���кš� 
	 * �������ڣ�(2004-5-12 14:51:25)
	 *  @return int
	 */
	private int getEditLineNo() {
		return editLineNo;
	}

	/**
	 * ��ȡ��ǰ���ڱ༭���ͣ��༭���򡢱༭�����༭�ӱ�
	 *  �������ڣ�(2004-5-12 11:40:28)
	 * @return int
	 */
	protected int getEditType() {
		return editType;
	}

	private nc.ui.pub.beans.UIFileChooser ivjFileChooser = null;

	/**
	 * �˴����뷽��˵���� 
	 * �������ڣ�(2005-4-28 14:24:14)
	 */
	public nc.ui.pub.beans.UIFileChooser getFileChooser() {
		if (ivjFileChooser == null) {
			try {
				ivjFileChooser = new nc.ui.pub.beans.UIFileChooser();
				ivjFileChooser.setName("FileChooser");
				ivjFileChooser.setBounds(17, 301, 433, 293);
				ExampleFileFilter JPGFilter = new ExampleFileFilter(new String[] { "jpg", "bmp" }, "Image Files");
				ivjFileChooser.setFileFilter(JPGFilter);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjFileChooser;
	}

	/**
	 * ��ȡ������е������Ի���
	 */
	protected GotoDialog getGotoDialog() {
		if (gotoDialog == null) {
			// ע��Ҫָ�������ڣ�������ģ̬�Ի���
			gotoDialog = new GotoDialog(Util.getTopFrame(this), true);
			// ���Ի����λ������Ϊ����
			gotoDialog.setLocationRelativeTo(this);
		}
		return gotoDialog;
	}

	/**
	 * �˴����뷽�������� 
	 * �������ڣ�(2004-6-3 12:03:10)	 * 
	 * @return java.lang.String
	 */
	protected String getIndocflag() {
		return "N";
	}

	/**
	 * ��ȡ��ǰ���ڱ༭�ı�tableCodes�����ݡ� 
	 * �������ڣ�(2004-5-17 11:30:33)	 * 
	 * @param tableCodes String[] ��ǰ���ڱ༭�ı�
	 * @return nc.vo.hi.hi_301.PersonEAVO ��Щ������ݣ������PersonEAVO��
	 */
	private PersonEAVO getInputData(String[] tableCodes) throws Exception{
		// ��ǰ��Ա����
		PersonEAVO eavo;
		// �����ǰ���������Ա���������༭״̬Ϊ�༭�������������״̬������������һ��PersonEAVO����
		if (getEditType() == EDIT_MAIN && isAdding()) {
			person = null;
			eavo = new PersonEAVO();
		}else if(getEditType() == EDIT_RETURN  && isAdding()) {
			person = null;
			eavo = new PersonEAVO();
		}else {
			eavo = (PersonEAVO) person.clone();
		}
		// ɾ���༭��
		if (getEditType() == EDIT_SUB && getCard().getBillTable().getCellEditor() != null) {
			getCard().getBillTable().getCellEditor().stopCellEditing();
			getCard().getBillTable().removeEditor();
		}
		// ��ȡ����
		getCard().getBillValueVOExtended(eavo);
		if (getEditType() == EDIT_MAIN || getEditType() == EDIT_RETURN || getEditType() == EDIT_REF) {
			// �����ǰ�༭״̬Ϊ�༭����
			if (isAdding()) {
				// ������������Ա
				// ���ø��ֱ�ʾ����˾(pk_corp)���뵵��ʶ(indocflag�����ֶ�ȱʡΪY������Ҫ���ã����͸������е�indocflag��ͬ������)
				eavo.getPsndocVO().setAttributeValue("pk_corp",Global.getCorpPK());
				eavo.getPsndocVO().setAttributeValue("indocflag",new nc.vo.pub.lang.UFBoolean('N'));
				eavo.getPsndocVO().setAttributeValue("pk_psnbasdoc", getRehirePsnbaspk());
				eavo.setPk_psnbasdoc(getRehirePsnbaspk());
			}

			//�˴�������Ա������ù�����Χ��
			String pk_psncl = (String)eavo.getPsndocVO().getAttributeValue("pk_psncl");
			if (pk_psncl!=null && !"".equals(pk_psncl)){
				eavo.getPsndocVO().setAttributeValue("psnclscope", getPsnclscopeAccordingPsncl(pk_psncl));
			}

		} else {
			// ά���ӱ�
			// ����Ա���������õ������ӱ��еļ�¼��
			String[] keys = getTempPsnPk();
			String pk_psndoc = keys[1];
			for (int i = 0; i < tableCodes.length; i++) {
				// ��ȡ���༭�ӱ�����м�¼
				CircularlyAccessibleValueObject[] records = eavo.getTableVO(tableCodes[i]);
				// �����жϣ���������������û�д���򽫸��Ƶļ�¼�еĴ������
				if ("hi_psndoc_deptchg".equalsIgnoreCase(tableCodes[i])) {
					int row = getEditLineNo();
					GeneralVO vo = (GeneralVO) records[row];
					BillItem[] items = getCard().getBillModel().getBodyItems();
					//
					for (int k = 0; k < items.length; k++) {
						if (items[k].isShow()) {
							if (vo.getAttributeValue(items[k].getKey()) == null) {
								vo.removeAttributeName(Util.PK_PREFIX+ items[k].getKey());
							}						
						}
					}
				}

				//v50 add ����ɼ��ڵ���Ĭ�ϴ����ĵ�ǰ��˾
//				records = setCorp(records,tableCodes[i]);
				// ��ǰ���Ƿ���ͬ�ڼ�¼����
				boolean isCurrent = AbstractValidator.isCurrentTable(tableCodes[i]);
				if (records != null) {
					int len = records.length;
					for (int j = 0; j < len; j++) {
						// change by zhyan 2006-03-24
						// �Ӽ�����˳��仯�����ӵ��������recordnum:0,1,2,3,4����Խ���¼Խ�ϣ�record=���ʱlastflag����Y
						// recordnum:0,1,2,3,4����Խ���¼Խ�ϣ�record=0ʱlastflag����Y
						records[j].setAttributeValue("recordnum", new Integer(len - j - 1));
						// ͬ�ڼ�¼ lastflag = 'Y'
						records[j].setAttributeValue("lastflag", new UFBoolean(j == len - 1 || isCurrent));
						// �����ӱ��¼����Ա�����ͱ���
						records[j].setAttributeValue("pk_psndoc", pk_psndoc);
						records[j].setAttributeValue("pk_psnbasdoc", keys[0]);
						//
						GeneralVO vo = (GeneralVO) records[j];
						BillItem[] items = getCard().getBillModel().getBodyItems();
						// ѭ���жϺ�����߼�ֵ�ֶ�û�б༭������Ϊ��N��
						for (int k = 0; k < items.length; k++) {
							if (items[k].isShow()
									&& items[k].getDataType() == BillItem.BOOLEAN) {
								if (vo.getAttributeValue(items[k].getKey()) == null) {
									vo.setAttributeValue(items[k].getKey(),
											new UFBoolean(false));
								}
							}
						}
					}
				}
			}
		}
		return eavo;
	}

	private int getPsnclscopeAccordingPsncl(String pk_psncl) throws Exception{

		IPsncl ipsncl = (IPsncl) NCLocator.getInstance().lookup(
				IPsncl.class.getName());
		nc.vo.bd.b05.PsnclVO psncl = ipsncl.findPsnclVOByPk(pk_psncl);
		if (psncl == null) {
			throw new BusinessException(nc.vo.ml.NCLangRes4VoTransl
					.getNCLangRes().getStrByID("600704", "upt600704-000042")/*
					 * @res
					 * "��Ա���"
					 */
					+ nc.vo.ml.NCLangRes4VoTransl.getNCLangRes().getStrByID(
							"600704", "UPP600704-000003")/*
							 * @res
							 * "�ü�¼�Ѿ���ɾ��,��ˢ�º����ԣ�"
							 */);
		}
		// 
		int psnclscope = 0;
		if (psncl != null) {
			psnclscope = psncl.getPsnclscope();

		}
		return psnclscope;
	}
	/**
	 * v50 add ������Ӽ��й�˾����name->pk,ǰ���ǲɼ��ܱ༭ҵ���Ӽ�,ά�����ܱ༭
	 * @param curtablename
	 * @return
	 */
	private CircularlyAccessibleValueObject[] setCorp(CircularlyAccessibleValueObject[] records,String curtablename){
		if ("hi_psndoc_dimission".equalsIgnoreCase(curtablename)
				||"hi_psndoc_ctrt".equalsIgnoreCase(curtablename)
				||"hi_psndoc_retire".equalsIgnoreCase(curtablename)
				||"hi_psndoc_training".equalsIgnoreCase(curtablename)
				||"hi_psndoc_orgpsn".equalsIgnoreCase(curtablename)
				||"hi_psndoc_psnchg".equalsIgnoreCase(curtablename)) {
			int row = getEditLineNo();
			if(row==-1)
				return records;
			GeneralVO vo = (GeneralVO) records[row];
			if (vo.getAttributeValue("pk_corp") != null) {
				vo.setAttributeValue("pk_corp", Global.getCorpPK());
			}
			if ("hi_psndoc_ctrt".equalsIgnoreCase(curtablename)) {				
				if (vo.getAttributeValue("pk_majorcorp") != null) {
					vo.setAttributeValue("pk_majorcorp", Global.getCorpPK());
				}
			}
		}
		else if ("hi_psndoc_ass".equalsIgnoreCase(curtablename)) {
			int row = getEditLineNo();
			GeneralVO vo = (GeneralVO) records[row];
			if (vo.getAttributeValue("pk_corpperson") != null) {
				vo.setAttributeValue("pk_corpperson", Global.getCorpPK());
			} else if (vo.getAttributeValue("pk_corpassess") != null) {
				vo.setAttributeValue("pk_corpassess", Global.getCorpPK());
			}
		}
		return records;
	}
	/**
	 * ���ظ��������ƣ����д���
	 */
	public java.lang.String[] getItemValuesByExpress(
			java.lang.String itemExpress) {
		try {
			if (psnList == null)
				return null;
			if (psnList.length == 0)
				return new String[0];
			String[] values = new String[psnList.length];
			for (int i = 0; i < values.length; i++) {
				String printtable = null;
				String printfield = null;
				// ������"__" ��ͷ�Ľ���ת��
				String fieldprefix = "__";// UFAGE
				if (itemExpress.indexOf(fieldprefix) > 0) {
					printtable = itemExpress.substring(0, itemExpress.indexOf(fieldprefix));
					printfield = itemExpress.substring(itemExpress.indexOf(fieldprefix) + 2);
				}
				// ��Ա������Χ ��������
				if ("psnclscope".equals(itemExpress)) {
					Integer intPsnclscope = (Integer) psnList[i].getAttributeValue(itemExpress);
					Object v = getPsnclscopeName(psnScopeItems, intPsnclscope);
					values[i] = (v == null ? null : v.toString());
				} 
				else if ("sex".equals(itemExpress)|| "bloodtype".equals(itemExpress)) {
					Object obj = psnList[i].getAttributeValue(itemExpress);
					BillItem billItem = getBillItem("bd_psnbasdoc."+ itemExpress);
					String reftype = billItem.getRefType();// IS,:,��:male,Ů:female
					String dispname = null;
					if (reftype != null && obj != null) {
						String[] first = reftype.split(",");
						for (int ii = 0; ii < first.length; ii++) {
							if (first[ii] != null&& first[ii].startsWith(obj.toString())) {
								String[] second = first[ii].split(":");
								dispname = (second == null ? null: second[second.length - 1]);
							}
						}
					}
					values[i] = (dispname == null ? " " : dispname);
				} else {
					if (printfield == null) {
						printfield = itemExpress;
					}
					Object v = psnList[i].getAttributeValue(printfield);
					values[i] = (v == null ? " " : v.toString());
					if (printtable != null && printfield != null && v != null) { 
						BillItem billItem = getBillItem(printtable + "."+ printfield);
						if (billItem != null) {
							int itemDataType = billItem.getDataType();
							String pkName = null;
							if (itemDataType == BillItem.UFREF|| itemDataType == BillItem.USERDEF) {
								UIRefPane refPane = (UIRefPane) billItem.getComponent();
								refPane.setPK(v);
								pkName = refPane.getRefName();
								values[i] = (pkName == null ? " " : pkName);
							} else if (itemDataType == BillItem.COMBO) {
								UIComboBox combox = (UIComboBox) billItem.getComponent();
								String dispname = (combox == null ? null: combox.getName());
								values[i] = (dispname == null ? " " : dispname);
							}
						}
					}
				}
			}
			return values;
		} catch (Exception e) {
			reportException(e);
			showErrorMessage(e.getMessage());
			return null;
		}
	}

	/**
	 * ���� LeaveCard ����ֵ��
	 * @return nc.ui.hi.hi_301.HIBillCardPanel
	 */
	protected HIBillCardPanel getLeaveCard() {
		if (ivjLeaveCard == null) {
			try {
				ivjLeaveCard = new nc.ui.hi.hi_301.HIBillCardPanel(allRelatedPara);
				ivjLeaveCard.setName("LeaveCard");		
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjLeaveCard;
	}

	/**
	 * �˴����뷽�������� 
	 * �������ڣ�(2004-5-18 10:55:48)
	 * @return java.lang.String[]
	 */
	private Pair[] getListItems() {
		return listItems;
	}

	protected HashMap hmField = new HashMap();
	/**
	 * Ĭ����ʾ����
	 */
	protected void initHmField() {
		hmField.put("psncode", "bd_psndoc.psncode");
		hmField.put("psnname", "bd_psnbasdoc.psnname");
		hmField.put("unitname", "bd_corp.unitname");
		hmField.put("deptcode", "bd_deptdoc.deptcode");
		hmField.put("deptname", "bd_deptdoc.deptname");
		hmField.put("psnclassname", "bd_psncl.psnclassname");
		hmField.put("jobname", "om_job.jobname");
		hmField.put("jobtypename", "0 as jobtypeflag");
		hmField.put("showorder", "bd_psndoc.showorder");
		if(intodocdirect ==1 && getModuleName().equalsIgnoreCase("600704")){
			hmField.put("approveflagname", "bd_psnbasdoc.approveflag");
		}
	}

	/**
	 * V35 add �õ��б���ֶ�
	 * @return
	 */
	protected String getListField() {
		Pair[] listItemsTmp = listItems;
		if (listItems == null) {
			listItemsTmp = getListItemsDefault();
		}
		String listfield = "";
		for (int i = 0; i < listItemsTmp.length; i++) {
			// modified by zhangdd���޸���Ա��Ϣ�ɼ���Ŀ���ñ��������
			if("belong_pk_corp".equals(listItemsTmp[i].getCode())) continue;

			String fullfield = listItemsTmp[i].getTablecode() == null ? listItemsTmp[i].getCode()
					: listItemsTmp[i].getTablecode() + "."+ listItemsTmp[i].getCode();
			fullfield = (hmField.get(fullfield) == null ? fullfield: (String) hmField.get(fullfield));
			if ("bd_psnbasdoc.approveflagname".equalsIgnoreCase(fullfield)) {
				fullfield = "bd_psnbasdoc.approveflag as approveflagname";
			}
			listfield += fullfield;
			if (i < listItemsTmp.length - 1) {
				listfield += ",";
			}
		}
		//dusx�޸�2009.6.4
		if(listfield.endsWith(",")) listfield = listfield.substring(0, listfield.length()-1);
		return listfield;
	}

	/**
	 * ͨ����Ա������ѯһ����Ա����Ϣʱ����б���Ϣ
	 * @return
	 */
	protected String getListFieldForOne() {
		Pair[] listItemsTmp = listItems;
		if (listItems == null) {
			listItemsTmp = getListItemsDefault();
		}

		String listfield = "";
		for (int i = 0; i < listItemsTmp.length; i++) {
			String fullfield = listItemsTmp[i].getTablecode() == null ? listItemsTmp[i].getCode()
					: listItemsTmp[i].getTablecode() + "."+ listItemsTmp[i].getCode();
			if ("bd_psnbasdoc.approveflagname".equalsIgnoreCase(fullfield)) {
				fullfield = "bd_psnbasdoc.approveflag as approveflagname";
			}
			if ("jobtypename".equalsIgnoreCase(fullfield)) {
				fullfield = "0 as jobtypeflag";
			}
			listfield += fullfield;
			if (i < listItemsTmp.length - 1) {
				listfield += ",";
			}
		}
		//dusx�޸�2009.6.4
		if(listfield.endsWith(",")) listfield = listfield.substring(0, listfield.length()-1);
		
		return listfield;
	}
	/**
	 * �����ڿ���������ýڵ��š� 
	 * �������ڣ�(2003-2-27 12:28:41)
	 * @return java.lang.String
	 */
	public String getModuleCodeWithDebug() {
		return "600704";
	}

	/**
	 * ���ظ�����Դ��Ӧ�Ľڵ����
	 */
	public java.lang.String getModuleName() {
		return "600704";
	}
	//�����ѳ�������
	private UICheckBox includeCancleDept = null;

	protected UICheckBox getincludeCancleDept() {
		if (includeCancleDept == null) {
			try {
				includeCancleDept = new UICheckBox();
				includeCancleDept.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("600704", "UPT600704-000279"));//�����ѳ�������
				includeCancleDept.setFont(new java.awt.Font("dialog", 0, 12));
				includeCancleDept.setPreferredSize(new java.awt.Dimension(93, 20));
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return includeCancleDept;
	}
	//������ʷ��Ա��
	private UICheckBox includeHisPersonGroup = null;

	protected UICheckBox getIncludeHisPersonGroup() {
		if (includeHisPersonGroup == null) {
			try {
				includeHisPersonGroup = new UICheckBox();
				includeHisPersonGroup.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("600704", "UPT600704-000295"));//"������ʷ��Ա��"
				includeHisPersonGroup.setPreferredSize(new java.awt.Dimension(120, 20));
				includeHisPersonGroup.setFont(new java.awt.Font("dialog", 0, 12));
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return includeHisPersonGroup;
	}
	//������ʷ��Ա
	private UICheckBox includeHisPerson = null;

	protected UICheckBox getIncludeHisPerson() {
		if (includeHisPerson == null) {
			try {
				includeHisPerson = new UICheckBox();
				includeHisPerson.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("600704", "UPT600704-000296"));//������ʷ��Ա
				includeHisPerson.setFont(new java.awt.Font("dialog", 0, 12));
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return includeHisPerson;
	}
	//�Ƿ���ʾ�Ӽ�
	private UICheckBox ivjIsSelSet = null;

	protected UICheckBox getIsSelSet() {
		if (ivjIsSelSet == null) {
			try {
				ivjIsSelSet = new UICheckBox();
				ivjIsSelSet.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("600704", "UPP600704-000195"));//�Ƿ���ʾ�Ӽ�
				ivjIsSelSet.setPreferredSize(new java.awt.Dimension(69, 20));
				ivjIsSelSet.setFont(new java.awt.Font("dialog", 0, 12));
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjIsSelSet;
	}
	
	private UIPanel quickQueryPanel = null;
	protected UIPanel getQuickQueryPanel() {
		if (quickQueryPanel == null) {
			try {
				quickQueryPanel = new UIPanel();
				quickQueryPanel.setName("queryEastPanel");
				quickQueryPanel.setPreferredSize(new java.awt.Dimension(130,30));//WestPanel.setPreferredSize(new java.awt.Dimension(750, 30));
				quickQueryPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
				//getQuickQueryPanel().add(getUILabelName(), getUILabelName().getName());
				getQuickQueryPanel().add(getUItfName(), getUItfName().getName());
				//getQuickQueryPanel().add(getUILabelJobRank(), getUILabelJobRank().getName());
				//getQuickQueryPanel().add(getUIRefDutyRank(), getUIRefDutyRank().getName());
				getQuickQueryPanel().add(getQuickQueryButton(), getQuickQueryButton().getName());
				getQuickQueryPanel().add(getQuickMenuBar());

			} catch (Throwable e) {
				handleException(e);
			}
		}
		return quickQueryPanel;
	}
	private nc.ui.pub.beans.UILabel ivjUILabelName = null;
	protected nc.ui.pub.beans.UILabel getUILabelName() {
		if (ivjUILabelName == null) {
			try {
				ivjUILabelName = new nc.ui.pub.beans.UILabel();
				ivjUILabelName.setName("UILabelName");
				ivjUILabelName.setText("����");
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjUILabelName;
	}
	
	// ���ٲ�ѯ����Ա����TextField
	private nc.ui.pub.beans.UITextField ivjUItfName = null;
	protected nc.ui.pub.beans.UITextField getUItfName() {
		if (ivjUItfName == null) {
			try {
				ivjUItfName = new nc.ui.pub.beans.UITextField();
				ivjUItfName.setName("UItfName");
				ivjUItfName.setMaxLength(50);
				ivjUItfName.addKeyListener(new KeyAdapter(){
					public void keyTyped(KeyEvent keyevent)
		            {
						if (keyevent.getKeyChar() == '\n') {
							getQuickQueryButton().doClick();
							ivjUItfName.requestFocusInWindow();
						}
		            }
				});
				
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjUItfName;
	}
	
	private nc.ui.pub.beans.UILabel ivjUILabelJobRank = null;
	protected nc.ui.pub.beans.UILabel getUILabelJobRank() {
		if (ivjUILabelJobRank == null) {
			try {
				ivjUILabelJobRank = new nc.ui.pub.beans.UILabel();
				ivjUILabelJobRank.setName("UILabelJobRank");
				ivjUILabelJobRank.setText("ְ��");
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjUILabelJobRank;
	}
	
	private nc.ui.pub.beans.UIRefPane ivjUIRefDutyRank = null; //ְ�񼶱����
	
	private nc.ui.pub.beans.UIButton quickQuery = null;
	protected nc.ui.pub.beans.UIButton getQuickQueryButton() {
		if (quickQuery == null) {
			try {
				quickQuery = new nc.ui.pub.beans.UIButton();
				quickQuery.setName("quickQuery");
				quickQuery.setText("���");
				quickQuery.setPreferredSize(new java.awt.Dimension(30, 20));
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return quickQuery;
	}
	
	protected ButtonObject[] grpQuery = new ButtonObject[]{ bnUpload, bnCard, bnBook,/*bnCard2, bnBook2,*/
			bnExportPic, bnListItemSet, /*bnSort,*/ bnSetSort, bnQuery,  bnFresh,
			bnPrint,/*bnUpdatePsnnamePinyin*/ };
	protected ButtonObject[] grpCardDisplayQuery = new ButtonObject[]{ bnUpload,/*bnCard2,*/bnToFirst, bnUpRecord,
			bnDownRecord, bnToLast, bnList };
	
	/**
	 * ���������¼���������
	 * @throws java.lang.Exception
	 */
	protected void onBatchExport() throws java.lang.Exception {

		GeneralVO[] intoDocData = getSelectPsnListData();
		if (intoDocData == null ||intoDocData.length<1 ) {
			showWarningMessage("��ѡ��������������Ա��");
			return;
		}

		UIFileChooserForOutport digFile = getDigOutport();
		int returnVal = digFile.showDialog(this, NCLangRes.getInstance().getStrByID("10241201", "UPP10241201-000107")/* @res "����"*/);
		if (returnVal == JFileChooser.APPROVE_OPTION) {
			
			new Thread(new Runnable() {
				public void run() {
					BannerDialog dialog = new BannerDialog(PsnInfCollectUI.this);
					doOutput(dialog);
				}
			}).start();
			
		}
	}
	
	private UIFileChooserForOutport digOutport = null;
	private UIFileChooserForOutport getDigOutport() {
		if (digOutport == null ){
			digOutport = new UIFileChooserForOutport(); 
		}
		return digOutport;
	}
	
	private void doOutput(BannerDialog waitDlg) {

		String strFileName = getDigOutport().getSelectedFile().getPath();
		try{
			waitDlg.start();
//			String startText = "�ǼǱ�������������Ժ�......";
//			String title = "��ʾ";
		
			GeneralVO[] intoDocData = getSelectPsnListData();
			
			//ȡ�õǼǱ�ģ��
			RptDefVO curTemplate = null;
			RptAuthVO auVO = getDigOutport().getSelectedRpt();
			if (auVO != null) {
				
				RptDefVO[] vos = HIDelegator.getIHrReport().queryDefVOByCondition(" d.pk_rpt_def='"+auVO.getPk_rpt_def()+"' ");
				if(vos==null || vos.length<=0){
					throw new BusinessException("��ѯ��Ƭģ�巢���쳣");
				}
				curTemplate = vos[0];
			}else {
				MessageDialog.showHintDlg(this, null, "��ѡ��Ƭ��ģ�壡");
				return;
			}
			
			if (auVO.getRpt_type()==nc.vo.hr.pub.carddef.CommonValue.RPT_TYPE_CARD){
				//
				ContextVO context = new ContextVO();
				for(int i = 0; i < intoDocData.length; i++){
					String pk_psndoc = (String) intoDocData[i].getAttributeValue("pk_psndoc");
					String psnname = (String) intoDocData[i].getAttributeValue("psnname");
					String pk_psndoc_sub = (String) intoDocData[i].getAttributeValue("pk_psndoc_sub");
					int jobType = (Integer) intoDocData[i].getAttributeValue("jobTypeFlag");
					RptDefVO defVO = HrCardReportViewPanel.queryRptDefVoByPk(auVO.getPk_rpt_def());
					CellsModel cm = (CellsModel)defVO.getObj_rpt_def();
					if(cm==null){
						continue;
					}
					HrReportDataVO rptData = HrCardReportViewPanel.queryReportData(
							pk_psndoc, 
							pk_psndoc_sub, jobType!=0, 
							auVO.getPk_rpt_def(), 
							cm);
//							cmcopy.setCellsAuth(auth);
					CardBrowseMainPanel mp = new CardBrowseMainPanel(null);
					mp.getPreviewPanel().processPreviewCellsModel(cm);
					mp.getPreviewPanel().putReportVOIntoCellsModel(rptData, cm);
							
//					context.setName("������Ƭ");
					context.setName(NCLangRes.getInstance().getStrByID("600700", "UPP600700-000364")/* @res "������Ƭ" */);
					HSSFWorkbook workBook = ExcelExpUtil.createWorkBook(context,cm,null);
					saveWorkBook2Local(strFileName,psnname+".xls",workBook);
				}//end for
			}else if (auVO.getRpt_type()==nc.vo.hr.pub.carddef.CommonValue.RPT_TYPE_WORD){
				
				//���licenseУ��
				 if(!isCheckLicense_wordcard) {
					 MessageDialog.showErrorDlg(ClientAssistant.getApplet(), null,
					 "�ò��û������license��license�Ѿ����ڡ�");
					 return;
				 }
				
				//
				WordVO wordVO = (WordVO)curTemplate.getObj_rpt_def();
				Map refDataTypeMap = RefDataTypeHelper.getRefDataTypeMap(Global.getCorpPK());
				if (wordVO == null) {
					MessageDialog.showErrorDlg(this, "����","��ģ��Ϊ��ģ��");
					return;
				}
				if(!getDigOutport().isOne()){
					for (int i = 0; i < intoDocData.length; i++) {
						String pk_psndoc = (String) intoDocData[i].getAttributeValue("pk_psndoc");
						String psnname = (String) intoDocData[i].getAttributeValue("psnname");
						String deptname = (String) intoDocData[i].getAttributeValue("deptname");
			
						Calendar calendar = Calendar.getInstance();
						int year = calendar.get(Calendar.YEAR);
						int month = calendar.get(Calendar.MONTH) + 1;
						int day = calendar.get(Calendar.DATE);
						int hour = calendar.get(Calendar.HOUR_OF_DAY);
						int minute = calendar.get(Calendar.MINUTE);
						int second = calendar.get(Calendar.SECOND);
						String date = year + (month>=10?Integer.toString(month):"0"+month) + (day>=10?Integer.toString(day):"0"+day)
									+ (hour>=10?Integer.toString(hour):"0"+hour)
									+ (minute>=10?Integer.toString(minute):"0"+minute)
									+ (second>=10?Integer.toString(second):"0"+second);
			
						String fileName = date + "_" + curTemplate.getRpt_name() + "_" + deptname + "_" + psnname;
						WordUtil.output(refDataTypeMap, pk_psndoc, wordVO.getConditionMap(), wordVO.getWordDocument(), strFileName, fileName, false);
					}
				}else {
					strFileName = System.getProperty("user.home") + "/" + "TEMPLETE_REGISTER" + "/";
					String[] fileNames = new String[intoDocData.length];
					for (int i = 0; i < intoDocData.length; i++) {
						String pk_psndoc = (String) intoDocData[i].getAttributeValue("pk_psndoc");
						fileNames[i] = pk_psndoc;
						WordUtil.output(refDataTypeMap, pk_psndoc, wordVO.getConditionMap(), wordVO.getWordDocument(), strFileName, pk_psndoc, false);
					}
					strFileName = getDigOutport().getSelectedFile().getPath();
					WordUtil.output(fileNames, strFileName);
				}
			}
			MessageDialog.showHintDlg(this, null, "��Ƭ����ɹ�!");
			waitDlg.end();
		} catch (BusinessException e) {
			MessageDialog.showErrorDlg(this, null,e.getMessage());
		}
		catch (Exception e) {
			MessageDialog.showErrorDlg(this, null, "�ǼǱ������������!");
			e.printStackTrace();
		} finally {
			waitDlg.end();
		}
	}
	
    /**
     * �������ɵ�Excel�ļ������û�����
     * @param parent
     * @param workBook
     * @param strFilePostfix
     */
    private static void saveWorkBook2Local(String dir, String fileName, HSSFWorkbook workBook) {
        if(!StringUtils.hasText(dir) || 
        		!StringUtils.hasText(fileName) || 
        		workBook==null){
            return;
        }
        
        // ����filename�еķǷ��ַ�
        fileName = StringUtils.replace(fileName, "/", "-");
        fileName = StringUtils.replace(fileName, "\\", "-");
        fileName = StringUtils.replace(fileName, ":", "-");
        fileName = StringUtils.replace(fileName, "*", "-");
        fileName = StringUtils.replace(fileName, "?", "-");
        fileName = StringUtils.replace(fileName, "\"", "-");
        fileName = StringUtils.replace(fileName, "<", "-");
        fileName = StringUtils.replace(fileName, ">", "-");
        fileName = StringUtils.replace(fileName, "|", "-");
       
		File file = new File(dir+"/"+fileName);
		FileOutputStream stream = null;
		try {
			stream = new FileOutputStream(file);
			workBook.write(stream);
		} catch (Throwable e) {
			e.printStackTrace();
		}finally{
			try {
				stream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }
	
	//�Ӽ���ʾ��ʷ
	private UICheckBox ivjIsShowSetHistory = null;//v50 add

	protected UICheckBox getIsShowSetHistory() {
		if (ivjIsShowSetHistory == null) {
			try {
				ivjIsShowSetHistory = new UICheckBox();
				ivjIsShowSetHistory.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("600704", "UPT600704-000277"));//�Ӽ���ʾ��ʷ
				ivjIsShowSetHistory.setPreferredSize(new java.awt.Dimension(93, 20));
				ivjIsShowSetHistory.setFont(new java.awt.Font("dialog", 0, 12));
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjIsShowSetHistory;
	}

	protected boolean IsLookHistory(){
		if(getIsShowSetHistory().isSelected()){
			return true;
		}else{
			return false;
		}
	}
	private UIPanel WestPanel = null;

	protected UIPanel getWestPanel() {
		if (WestPanel == null) {
			try {
				WestPanel = new UIPanel();
				WestPanel.setName("WestPanel");
				WestPanel.setPreferredSize(new java.awt.Dimension(624, 30));
				WestPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
				getWestPanel().add(getLbScope(), getLbScope().getName());
				getWestPanel().add(getCmbPsncl(), getCmbPsncl().getName());
				getWestPanel().add(getUILabelType(), getUILabelType().getName());
				getWestPanel().add(getCmbJobType(), getCmbJobType().getName());
				getWestPanel().add(getincludeChildDeptPsn(),getincludeChildDeptPsn().getName());//v5.3 add
				getWestPanel().add(getincludeCancleDept(),getincludeCancleDept().getName());//v5.3 add
				getWestPanel().add(getIncludeHisPersonGroup(), getIncludeHisPersonGroup().getName());
				getWestPanel().add(getIncludeHisPerson(), getIncludeHisPerson().getName());
				getWestPanel().add(getIsSelSet(), getIsSelSet().getName());
				getWestPanel().add(getIsShowSetHistory(), getIsShowSetHistory().getName());

			} catch (Throwable e) {
				handleException(e);
			}
		}
		return WestPanel;
	}
	
	private UIPanel EastPanel = null;

	protected UIPanel getEestPanel() {
		if (EastPanel == null) {
			try {
				EastPanel = new UIPanel();
				EastPanel.setName("EastPanel");
				//EastPanel.setPreferredSize(new java.awt.Dimension(400, 30));
				EastPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
				//getEestPanel().add(getQuickQueryPanel(), "West");
				getEestPanel().add(getUILabelRecords(), "East");
			} catch (Throwable e) {
				handleException(e);
			}
		}
		return EastPanel;
	}

	/**
	 * ���� NorthPanel ����ֵ��
	 * @return nc.ui.pub.beans.UIPanel
	 */
	protected nc.ui.pub.beans.UIPanel getNorthPanel() {
		if (ivjNorthPanel == null) {
			try {
				ivjNorthPanel = new nc.ui.pub.beans.UIPanel();
				ivjNorthPanel.setName("NorthPanel");
				ivjNorthPanel.setPreferredSize(new java.awt.Dimension(1000, 30));
				ivjNorthPanel.setLayout(new BorderLayout());
				getNorthPanel().add(getWestPanel(), "West");
				getNorthPanel().add(getEestPanel(), "East");
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjNorthPanel;
	}

	/**
	 * ��Ա�ɼ��Զ����� :��õ��ݱ��VO�� 
	 * �������ڣ�(2004-5-19 16:48:20)
	 */
	public nc.vo.pub.billcodemanage.BillCodeObjValueVO getObjVO() {
		if (objBillCodeVO == null) {
			objBillCodeVO = new nc.vo.pub.billcodemanage.BillCodeObjValueVO();
			objBillCodeVO.setAttributeValue("��˾", Global.getCorpPK());
			objBillCodeVO.setAttributeValue("����Ա", Global.getUserID());
		}
		return objBillCodeVO;
	}

	/**
	 * ���� OtherCard ����ֵ��
	 * @return nc.ui.hi.hi_301.HIBillCardPanel
	 */
	protected HIBillCardPanel getOtherCard() {
		if (ivjOtherCard == null) {
			try {
				ivjOtherCard = new nc.ui.hi.hi_301.HIBillCardPanel(allRelatedPara);
				ivjOtherCard.setName("OtherCard");
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjOtherCard;
	}

	/**
	 * �˴����뷽�������� 
	 * �������ڣ�(2004-5-31 14:26:07)
	 * @return nc.ui.pub.print.PrintEntry
	 */
	private PrintEntry getPrintEntry() throws Exception {
		if (printEntry == null) {
			printEntry = new PrintEntry(this, this);
			printEntry.setTemplateID(Global.getCorpPK(), getModuleName(),Global.getUserID(), null, null);
		}
		return printEntry;
	}

	/**
	 * ���� PsnCard ����ֵ��
	 * @return nc.ui.pub.beans.UIPanel
	 */
	protected nc.ui.pub.beans.UIPanel getPsnCard() {
		if (ivjPsnCard == null) {
			try {
				ivjPsnCard = new nc.ui.pub.beans.UIPanel();
				ivjPsnCard.setName("PsnCard");
				ivjPsnCard.setPreferredSize(new java.awt.Dimension(100, 21));
				ivjPsnCard.setLayout(new java.awt.CardLayout());
				getPsnCard().add(getWorkCard(), getWorkCard().getName());
				getPsnCard().add(getDismissCard(), getDismissCard().getName());
				getPsnCard().add(getRetireCard(), getRetireCard().getName());
				getPsnCard().add(getLeaveCard(), getLeaveCard().getName());
				getPsnCard().add(getOtherCard(), getOtherCard().getName());
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjPsnCard;
	}

	/**
	 * �õ���Ա������Χ������
	 * @param psnScopes
	 * @param psnclscope
	 * @return
	 */
	private String getPsnclscopeName(final Item[] psnScopes, Integer psnclscope) {
		if (psnScopes == null || psnclscope == null) {
			return "0";
		}
		String name = null;
		for (int i = 0; i < psnScopes.length; i++) {
			if (psnScopes[i].getValue() == psnclscope.intValue()) {
				name = psnScopes[i].getName();
				break;
			}
		}
		return name;
	}

	/**
	 * ���� PsnList ����ֵ��
	 * @return nc.ui.hi.hi_301.SortableBillScrollPane
	 */
	public SortableBillScrollPane getPsnList() {
		if (ivjPsnList == null) {
			try {
				ivjPsnList = new nc.ui.hi.hi_301.SortableBillScrollPane();
				ivjPsnList.setName("PsnList");
				ivjPsnList.setBorder(javax.swing.border.LineBorder.createGrayLineBorder());
				ivjPsnList.getTable().setTranslate(true);
				ivjPsnList.getTable().setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
				ivjPsnList.getTable().getSelectionModel().addListSelectionListener(this);
				// �����Ҽ���ť���ɼ�
				ivjPsnList.setBBodyMenuShow(false);
				ivjPsnList.setModuleName(getModuleName());
				ivjPsnList.removeTableSortListener();
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjPsnList;
	}

	/**
	 * ��ȡ��Ա�б��BillModel 
	 * �������ڣ�(2003-6-6 16:52:56)
	 * @return nc.ui.pub.bill.BillModel
	 */
	public BillModel getPsnListModel() {
		// ��ȡ��ǰ��ʾ��
		Pair[] items = getListItems();
		// ���ɱ�ͷ
		BillItem[] biaBody = new BillItem[items.length];
		Vector vExtension = new Vector();
		for (int i = 0; i < items.length; i++) {
			if (items[i].getDataType() == CommonValue.DATATYPE_UFREF) {//
				// ��ʾ
				biaBody[i] = new BillItem();
				biaBody[i].setName(items[i].getName());
				biaBody[i].setKey(items[i].getCode() + "_name");
				biaBody[i].setWidth(100);
				biaBody[i].setEnabled(true);
				biaBody[i].setEdit(false);
				biaBody[i].setShow(items[i].isVisible());
				biaBody[i].setNull(false);
				String[] loadFormula = getLoadFormula(items[i]);
				biaBody[i].setLoadFormula(loadFormula);
				vExtension.addElement(biaBody[i]);
				// ����������
				BillItem billItem = new BillItem();
				billItem.setName(items[i].getName() + "����");
				billItem.setKey(items[i].getCode());
				billItem.setWidth(100);
				billItem.setEnabled(true);
				billItem.setEdit(false);
				billItem.setShow(false);// debug false
				billItem.setNull(false);
				vExtension.addElement(billItem);
			} else {
				biaBody[i] = new BillItem();
				biaBody[i].setName(items[i].getName());
				if (items[i].getDataType() == CommonValue.DATATYPE_BOOLEAN) {
					biaBody[i].setDataType(BillItem.BOOLEAN);
				}   else {
					biaBody[i].setDataType(BillItem.STRING);
				}
				// ���������ֵ����
				if (items[i].getCode().equalsIgnoreCase("showorder")) {
					biaBody[i].setDataType(BillItem.INTEGER);
				}
				biaBody[i].setKey(items[i].getCode());
				biaBody[i].setWidth(100);
				biaBody[i].setEnabled(true);
				biaBody[i].setEdit(false);
				biaBody[i].setShow(items[i].isVisible());
				biaBody[i].setNull(false);
				vExtension.addElement(biaBody[i]);
			}
		}
		biaBody = new BillItem[vExtension.size()];
		vExtension.copyInto(biaBody);
		// ����
		BillModel billModel = new BillModel();
		billModel.setBodyItems(biaBody);
		return billModel;
	}

	private HashMap hmRefType = null;

	protected HashMap getRefTypeHash() {
		if (hmRefType == null) {
			try {
				hmRefType = HIDelegator.getIInfoSet().getRefTypeHashAll();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return hmRefType;
	}
	/**
	 * ���ع�ʽ����
	 * @param pair
	 * @return
	 */
	private String[] getLoadFormula(Pair pair) {
		String[] loadFormula = null;
		if (pair.getDataType() == CommonValue.DATATYPE_UFREF) {
			final UIRefPane ref = new UIRefPane();
			ref.setReturnCode(false);
			ref.setButtonFireEvent(true);
			String RefType = pair.getRefType();
			if (Util.isDefdocPk(RefType)) {// �����ǰ���Զ��嵵������
				loadFormula = new String[] { pair.getCode() + "_name"
						+ "->getColValue(bd_defdoc,docname,pk_defdoc,"
						+ pair.getCode() + ")" };
			} else {
				HashMap refTypeHash = getRefTypeHash();
				GeneralVO refvo = (GeneralVO) refTypeHash.get(RefType);
				if (refvo != null) {
					String tablename = (String) refvo.getAttributeValue("tablename");
					String namefield = (String) refvo.getAttributeValue("namefield");
					String pkfield = (String) refvo.getAttributeValue("pkfield");
					loadFormula = new String[] { pair.getCode() + "_name"
							+ "->getColValue(" + tablename + "," + namefield
							+ "," + pkfield + "," + pair.getCode() + ")" };
				}
			}
		}
		return loadFormula;
	}

	/**
	 * ��ȡ��ǰ��Ա�б����������ȥ������Ա���� 
	 * �������ڣ�(2004-5-31 10:32:16)
	 * @return java.lang.String[]
	 */
	public String[] getselectPsnPksExceptRef(String tableCode) {
		if (psnList == null)
			return null;
		int[] rows = getPsnList().getHeadSelectedRows();
		if(rows.length<1) return null;

		Vector v = new Vector();
		try {			
			for (int i = 0; i < rows.length; i++) {
				if (((Integer) psnList[rows[i]].getAttributeValue("jobtypeflag")).intValue() == 0
						&& Global.getCorpPK().equals((String) psnList[rows[i]].getAttributeValue("belong_pk_corp"))
						&&Global.getCorpPK().equals((String) psnList[rows[i]].getAttributeValue("pk_corp"))) {
					if (tableCode.equalsIgnoreCase("hi_psndoc_deptchg")
							|| tableCode.equalsIgnoreCase("hi_psndoc_ctrt")
							|| tableCode.equalsIgnoreCase("hi_psndoc_part")
							|| tableCode.equalsIgnoreCase("hi_psndoc_training")
							|| tableCode.equalsIgnoreCase("hi_psndoc_ass")
							|| tableCode.equalsIgnoreCase("hi_psndoc_retire")
							|| tableCode.equalsIgnoreCase("hi_psndoc_orgpsn")
							|| tableCode.equalsIgnoreCase("hi_psndoc_psnchg")
							|| tableCode.equalsIgnoreCase("hi_psndoc_dimission")
							|| tableCode.equalsIgnoreCase("bd_psndoc"))
						v.addElement((String) psnList[rows[i]].getAttributeValue("pk_psndoc"));
					else
						v.addElement((String) psnList[rows[i]].getAttributeValue("pk_psnbasdoc"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String[] pks = new String[v.size()];
		v.copyInto(pks);
		return pks;
	}
	/**
	 * ���ز�ѯģ�壬��ʼ����ѯ�����ڲ�����
	 * @return
	 * @throws Exception
	 */
	protected QueryConditionClient getQueryDlg() throws Exception {
		if (queryDlg == null) {
			queryDlg = new QueryConditionClient(this);
			// ���ز�ѯģ��
			queryDlg.setTempletID(Global.getCorpPK(), getModuleName(),Global.getUserID(), null);

			queryDlg.setMultiTableFlag(true);
			queryDlg.setLocationRelativeTo(this);
			//���ò�ѯ
			queryDlg.getUIPanelNormal().add(new NormalQueryPanel(),BorderLayout.CENTER); 
			queryDlg.getUIPanelNormal().updateUI();
			queryDlg.hideUnitButton();
			queryDlg.setNormalShow(true);
			// �����Զ��嵵������
			nc.vo.pub.query.QueryConditionVO[] conditionvos = queryDlg.getConditionDatas();
			if (conditionvos != null && conditionvos.length > 0) {
				for (int i = 0; i < conditionvos.length; i++) {
					if (conditionvos[i].getDataType().intValue() == 5) {// ��������
						if (Util.isDefdocPk(conditionvos[i].getConsultCode())) {
							nc.ui.hi.pub.PsnInfFldRefPane ref = new nc.ui.hi.pub.PsnInfFldRefPane(
									conditionvos[i].getConsultCode());
							queryDlg.setValueRef(	conditionvos[i].getTableCode(),conditionvos[i].getFieldCode(), ref);
						} else if ("��Ա����".equals(conditionvos[i].getConsultCode())
								|| "��Ա����HR".equals(conditionvos[i].getConsultCode())) {
							nc.ui.pub.beans.UIRefPane psndocref = new nc.ui.pub.beans.UIRefPane();
							psndocref.setRefNodeName(conditionvos[i].getConsultCode());
							psndocref.getRefModel().setUseDataPower(false);
							if (getModuleName().equals("600704")) {
								setWhereToModel(
										psndocref.getRefModel(),
										"((bd_deptdoc.canceled = 'N' and bd_deptdoc.hrcanceled = 'N') or " +
								"( bd_deptdoc.canceled is null and bd_deptdoc.hrcanceled is null))");
							} else {
								setWhereToModel(psndocref.getRefModel(),
								"((bd_deptdoc.canceled = 'N') or ( bd_deptdoc.canceled is null) )");
							}
							// ��Ա���Ȩ��
							//�������Ż�
							String psnwhere = psndocref.getRefModel().getWherePart();
//							if (GlobalTool.isUsedDataPower("bd_psncl", Global.getCorpPK())) {
								String powerPsnclSql = nc.ui.hr.global.GlobalTool
								.getPowerSql("bd_psncl", Global.getUserID(), Global.getCorpPK());
								if (powerPsnclSql != null && powerPsnclSql.length() > 0) {
									psnwhere += " and bd_psndoc.pk_psncl in ("
										+ powerPsnclSql + ") ";
								}
//							}
							psndocref.getRefModel().setWherePart(psnwhere);							
							String tablecode = conditionvos[i].getTableCode();
							String fieldcode = conditionvos[i].getFieldCode();
							if (tablecode != null && fieldcode != null) {
								queryDlg.setValueRef(tablecode, fieldcode,psndocref);
							}
						} else if ("���ŵ���".equals(conditionvos[i].getConsultCode())
								|| "���ŵ���HR".equals(conditionvos[i].getConsultCode())
								|| "<nc.ui.hi.ref.DeptRefModel>".equals(conditionvos[i].getConsultCode())) {
							nc.ui.pub.beans.UIRefPane deptref = new nc.ui.pub.beans.UIRefPane();
							deptref.setRefType(1);
							deptref.setRefInputType(1);
							if ("<nc.ui.hi.ref.DeptRefModel>".equals(conditionvos[i].getConsultCode())) {
								nc.ui.hi.ref.DeptRefModel deptModel = new nc.ui.hi.ref.DeptRefModel();
								deptref.setRefModel(deptModel);
							} else {
								deptref.setRefNodeName(conditionvos[i].getConsultCode());
							}
							/*
							if (getModuleName().equals("600704")) {
								deptref.getRefModel().setUseDataPower(false);
								setWhereToModel(
										deptref.getRefModel(),
										"((bd_deptdoc.canceled = 'N' and bd_deptdoc.hrcanceled = 'N') or "
												+ "( bd_deptdoc.canceled is null and bd_deptdoc.hrcanceled is null))");
							} else {*/
							setWhereToModel(deptref.getRefModel(),
							"((bd_deptdoc.canceled = 'N') or ( bd_deptdoc.canceled is null) )");
							// }
							String tablecode = conditionvos[i].getTableCode();
							String fieldcode = conditionvos[i].getFieldCode();
							if (tablecode != null && fieldcode != null) {
								queryDlg.setValueRef(tablecode, fieldcode,
										deptref);
							}
						}
					}
				}
			}
			SpaDeptdocRefGridTreeModel psnrefmodel = null;
			String wheresql = " bd_deptdoc.pk_deptdoc = V_HR_PSNDOC_MULTI_JOB.pk_deptdoc and bd_psndoc.dr = 0 "
				+ "and bd_psndoc.indocflag = '" + getIndocflag() + "' ";
			if (getModuleName().equals("600707")
					|| getModuleName().equals("600704")|| getModuleName().equals("600708")) {
				psnrefmodel = new SpaDeptdocRefGridTreeModel();
				psnrefmodel.setIsLimit(0);
				String deptsql = "0=0";
				if ((powerSql.length() > 0 && powerSql.trim().startsWith(
				"select"))
				&& !getModuleName().equals("600704")) {
					deptsql = " bd_deptdoc.pk_deptdoc in (" + powerSql + ")";
				}
				psnrefmodel.setClassWherePart(deptsql);
			}
			psnrefmodel.setCacheEnabled(false);
			if (getModuleName().equals("600707")||getModuleName().equals("600708")) {
				wheresql += "and bd_psndoc.psnclscope = " + getSelPsnclscope();
			}
			psnrefmodel.setWherePart(wheresql);
			nc.ui.pub.beans.UIRefPane psnref = new nc.ui.pub.beans.UIRefPane();
			psnref.setRefType(1);
			psnref.setRefInputType(1);
			psnrefmodel.setUseDataPower(false);
			if (getModuleName().equalsIgnoreCase("600704")) {
				setWhereToModel(
						psnrefmodel,
						"((bd_deptdoc.canceled = 'N' and bd_deptdoc.hrcanceled = 'N') or " +
				"( bd_deptdoc.canceled is null and bd_deptdoc.hrcanceled is null))");
			} else {
				setWhereToModel(psnrefmodel,
				"((bd_deptdoc.canceled = 'N') or ( bd_deptdoc.canceled is null) )");
				if ((powerSql.length() > 0 && powerSql.trim().startsWith("select"))&& !getModuleName().equals("600704")) {
					setWhereToModel(psnrefmodel,"( bd_deptdoc.pk_deptdoc in (" + powerSql + ") )");
				}
			}
			psnref.setRefModel(psnrefmodel);
			// ��Ա���Ȩ��
			//�������Ż�
			String psnwhere = psnrefmodel.getWherePart();
//			if (GlobalTool.isUsedDataPower("bd_psncl", Global.getCorpPK())) {
				String powerPsnclSql = nc.ui.hr.global.GlobalTool
				.getPowerSql("bd_psncl", Global.getUserID(), Global.getCorpPK());
				if (powerPsnclSql != null && powerPsnclSql.length() > 0) {
					psnwhere += " and bd_psndoc.pk_psncl in ("
						+ powerPsnclSql + ") ";
				}
//			}
			psnrefmodel.setWherePart(psnwhere);		
			queryDlg.setValueRef("bd_psndoc", "bd_psndoc.pk_psndoc", psnref);

			if (getModuleName().equals("600707")|| getModuleName().equalsIgnoreCase("600704") ||getModuleName().equals("600708")) {
				nc.ui.hi.ref.JobRef jobmodel = new nc.ui.hi.ref.JobRef();
				String where = jobmodel.getWherePart();
				if (powerSql.length() > 0
						&& powerSql.trim().startsWith("select")
						&& !getModuleName().equalsIgnoreCase("600704")) {
					where += " and om_job.pk_deptdoc in (" + powerSql + ")";
				} else {
					where += " and (0=0)";
				}
				jobmodel.setWherePart(where);
				jobmodel.setRefTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID("600704", "UPP600704-000068")/*
				 * @res
				 * "��λ����"
				 */);
				nc.ui.pub.beans.UIRefPane jobref = new nc.ui.pub.beans.UIRefPane();
				jobref.setRefType(1);
				jobref.setRefInputType(1);
				jobref.setRefModel(jobmodel);
				queryDlg.setValueRef("bd_psndoc", "bd_psndoc.pk_om_job", jobref);
				// ���ŵ�������
				nc.ui.pub.beans.UIRefPane deptref = new nc.ui.pub.beans.UIRefPane();
				deptref.setRefType(1);
				deptref.setRefInputType(1);
				deptref.setRefNodeName("���ŵ���HR");
//				if (getModuleName().equalsIgnoreCase("600704")) {
//				deptref.getRefModel().setUseDataPower(false);
//				}
				// �Ƿ�����¼�
				deptref.setIncludeSubShow(true);
				// wangkf ����ѯ�������߷��Ĳ���
				if (getModuleName().equalsIgnoreCase("600704")) {
					setWhereToModel(
							deptref.getRefModel(),
							"((bd_deptdoc.canceled = 'N' and bd_deptdoc.hrcanceled = 'N') or " +
					"( bd_deptdoc.canceled is null and bd_deptdoc.hrcanceled is null))");
				} else {
					setWhereToModel(deptref.getRefModel(),
					"((bd_deptdoc.canceled = 'N') or ( bd_deptdoc.canceled is null) )");
				}
				queryDlg.setValueRef("bd_psndoc", "bd_psndoc.pk_deptdoc",deptref);
			}
			// ������Ա�����ʾ�ѷ������
			nc.ui.pub.beans.UIRefPane psnclsref = new nc.ui.pub.beans.UIRefPane();
			psnclsref.setRefType(1);
			psnclsref.setRefInputType(1);
			psnclsref.setRefNodeName("��Ա���");
			psnclsref.getRefModel().setSealedDataShow(true);
			queryDlg.setValueRef("bd_psndoc", "bd_psndoc.pk_psncl", psnclsref);
		}
		return queryDlg;
	}

	/**
	 * ��ѯģ��
	 * 
	 * �������ڣ�(2002-4-27 11:18:09)
	 * 
	 * @return nc.ui.pub.query.QueryConditionClient
	 */
	public HrQueryDialog getQueryDialog()
	{

	    if(queryDialog == null)
	    {
	    	TemplateInfo tempinfo = new TemplateInfo();
			tempinfo.setPk_Org(ClientEnvironment.getInstance().getCorporation().getPrimaryKey());
			tempinfo.setCurrentCorpPk(Global.getCorpPK());
			tempinfo.setFunNode( getModuleName());
			tempinfo.setUserid(Global.getUserID());
			
			queryDialog = new HrQueryDialog( this, tempinfo);
			
			queryDialog.setFieldValueEditor(this);
			//�ɼ��ڵ�����״ֻ̬���С�δ�������͡�δ��׼��
			if (getModuleName().equals("600704")){
				IConstEnum[] STATUS =
					new DefaultConstEnum[]{
						new DefaultConstEnum(0, nc.ui.ml.NCLangRes.getInstance().getStrByID("600700","UPP600700-000362")/*@res "δ����"*/),
						new DefaultConstEnum(2, nc.ui.ml.NCLangRes.getInstance().getStrByID("600700","UPP600700-000363")/*@res "δ��׼"*/)};

				queryDialog.setFieldValueElementEnum("bd_psnbasdoc.approveflag",STATUS);
			}
			//��ͬ����
			queryDialog.setFieldValueElementEnum(
					"hi_psndoc_ctrt.iconttype", new DefaultConstEnum[] {
							new DefaultConstEnum(new Integer(1), nc.ui.ml.NCLangRes.getInstance().getStrByID("600704","UPP600704-000045")/*@res "ǩ��"*/),
							new DefaultConstEnum(new Integer(4), nc.ui.ml.NCLangRes.getInstance().getStrByID("600704","UPP600704-000046")/*@res "���"*/),
							new DefaultConstEnum(new Integer(3), nc.ui.ml.NCLangRes.getInstance().getStrByID("600704","UPP600704-000047")/*@res "��ǩ"*/),
							new DefaultConstEnum(new Integer(6), nc.ui.ml.NCLangRes.getInstance().getStrByID("600704","UPP600704-000049")/*@res "���"*/),
							new DefaultConstEnum(new Integer(5), nc.ui.ml.NCLangRes.getInstance().getStrByID("common","UC001-0000032")/*@res "��ֹ"*/) });

			
			queryDialog.setMultiTable(true);
			//���Ӽ���(��Ա��Χ�ı�ʱҪ����һЩ���յ�����)
			queryDialog.registerCriteriaEditorListener(new ICriteriaChangedListener(){
				public void criteriaChanged(CriteriaChangedEvent event) {
//					if(event.getEventtype()==CriteriaChangedEvent.FILTER_CHANGED
//							|| event.getEventtype()==CriteriaChangedEvent.FILTEREDITOR_INITIALIZED){
					if( event.getEventtype()==CriteriaChangedEvent.FILTEREDITOR_INITIALIZED){
						if(event.getFieldCode().equals("bd_psndoc.pk_psndoc")){ 
							//�˴�Ҫ������
							if (getModuleName().equals("600704")) return;
							String pk_corp = Global.getCorpPK();
							DefaultFilterEditor dfe = (DefaultFilterEditor) event.getFiltereditor();
							nc.ui.pub.beans.UIRefPane psnref =(UIRefPane)((DefaultFieldValueEditor)dfe.getFieldValueEditor()).getFieldValueElemEditor().getFieldValueElemEditorComponent();

							if(getSelPsnclscope()!=0 &&(getModuleName().equalsIgnoreCase("600707") || getModuleName().equalsIgnoreCase("600708"))){
								psnref.setRefModel(new PsndocDefaulRefModel("��Ա����"));
								psnref.getRefModel().setPk_corp(Global.getCorpPK());
								String psnwhere = " bd_psndoc.indocflag = 'Y' and bd_psndoc.psnclscope = " + getSelPsnclscope();
								try {
									//�������Ż�
//									if (GlobalTool.isUsedDataPower("bd_psncl", pk_corp)) {
										String powerPsnclSql = nc.ui.hr.global.GlobalTool.getPowerSql(
												"bd_psncl", Global.getUserID(), pk_corp);
										if (powerPsnclSql != null && powerPsnclSql.length() > 0) {
											psnwhere += " and bd_psndoc.pk_psncl in (" + powerPsnclSql
											+ ") ";
										}
//									}
								} catch (Exception e) {
									e.printStackTrace();
								}
								((nc.ui.bd.ref.busi.PsndocDefaulRefModel) psnref
										.getRefModel())
										.setWherePart(psnwhere);
								return;
							}

					        
							psnref.setRefType(1);
							psnref.setRefInputType(1);
							SpaDeptdocRefGridTreeModel psnrefmodel = new SpaDeptdocRefGridTreeModel();
							String wheresql = " bd_deptdoc.pk_deptdoc = V_HR_PSNDOC_MULTI_JOB.pk_deptdoc and bd_psndoc.dr = 0 "
									+ "and bd_psndoc.indocflag = '" + getIndocflag() + "' ";

							psnrefmodel.setCacheEnabled(false);
							if (getModuleName().equalsIgnoreCase("600707") || getModuleName().equalsIgnoreCase("600708")) {
								wheresql += "and bd_psndoc.psnclscope = " + getSelPsnclscope();
							}

							psnrefmodel.setUseDataPower(false);
							if (getModuleName().equalsIgnoreCase("600704")) {
								setWhereToModel(
										psnrefmodel,
										"((bd_deptdoc.canceled = 'N' and bd_deptdoc.hrcanceled = 'N') or " +
										"( bd_deptdoc.canceled is null and bd_deptdoc.hrcanceled is null))");
							} else {
								setWhereToModel(psnrefmodel,
										"((bd_deptdoc.canceled = 'N') or ( bd_deptdoc.canceled is null) )");
								if ((powerSql.length() > 0 && powerSql.trim().startsWith("select"))&& !getModuleName().equals("600704")) {
									setWhereToModel(psnrefmodel,"( bd_deptdoc.pk_deptdoc in (" + powerSql + ") )");
								}
							}
							// ��Ա���Ȩ��
							//�������Ż�
							try{
//								if (GlobalTool.isUsedDataPower("bd_psncl", Global.getCorpPK())) {
									String powerPsnclSql = nc.ui.hr.global.GlobalTool
											.getPowerSql("bd_psncl", Global.getUserID(), Global.getCorpPK());
									if (powerPsnclSql != null && powerPsnclSql.length() > 0) {
										wheresql += " and bd_psndoc.pk_psncl in ("
												+ powerPsnclSql + ") ";
									}
//								}
							} catch(Exception e){
								e.printStackTrace();
							}
							psnrefmodel.setWherePart(wheresql);	
							psnref.setRefModel(psnrefmodel); 
						}else if(event.getFieldCode().equals("bd_psndoc.pk_psncl")&&!getModuleName().equalsIgnoreCase("600704")){//������Ա����ѡ��Χ
							DefaultFilterEditor dfe = (DefaultFilterEditor) event.getFiltereditor();
					        nc.ui.pub.beans.UIRefPane psnref =(UIRefPane)((DefaultFieldValueEditor)dfe.getFieldValueEditor()).getFieldValueElemEditor().getFieldValueElemEditorComponent();
					        String pk_cl = psnref.getRefPK();
					        String code_cl = psnref.getRefCode();
					        psnref.getRefModel().addWherePart(" and psnclscope ="+getSelPsnclscope());
					        psnref.setPK(pk_cl);
					        
						}
					}
				}
			})	;  
			if (getModuleName().equalsIgnoreCase("600707") || getModuleName().equalsIgnoreCase("600708")) {
				queryDialog.registerQueryTemplateTotalVOProceeor(new IQueryTemplateTotalVOProcessor() {
					public void processQueryTempletTotalVO(
							QueryTempletTotalVO totalVO) {
						if (((Item) getCmbPsncl().getSelectedItem()).getValue()==0) return;
						if (totalVO != null) {
							QueryConditionVO[] qryconds = totalVO.getConditionVOs();
							for (QueryConditionVO qcvo : qryconds) {
								if (qcvo.getFieldCode().equals("hi_psndoc_deptchg.isreturn"))
								{
									qcvo.setIfDefault(new UFBoolean(false));
								}
							}
						}
					}
				});
			}
	    }
	    return queryDialog;
	
	}
	/**
	 * ��õ�ǰ��Ĺ����ֶΡ� 
	 * �������ڣ�(2004-5-30 15:21:00)
	 * @return java.util.Hashtable
	 */
	protected Hashtable getRelationMap() {
		return getCard().getRelationMap();
	}

	/**
	 * ���� RetireCard ����ֵ��
	 * @return nc.ui.hi.hi_301.HIBillCardPanel
	 */
	protected HIBillCardPanel getRetireCard() {
		if (ivjRetireCard == null) {
			try {
				ivjRetireCard = new nc.ui.hi.hi_301.HIBillCardPanel(allRelatedPara);
				ivjRetireCard.setName("RetireCard");
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjRetireCard;
	}

	public String getSelCorpPK() {
		return corpPK;
	}

	/**
	 * �˴����뷽�������� 
	 * �������ڣ�(2004-6-3 11:37:59)
	 * @return nc.vo.hi.hi_301.CtrlDeptVO
	 */
	protected CtrlDeptVO getSelectedDept() {
		// ��ȡ��ǰѡ��Ĳ���
		DefaultMutableTreeNode node = getSelectedNode();
		if (node == null)
			return null;
		return (CtrlDeptVO) node.getUserObject();
	}

	/**
	 * �˴����뷽�������� 
	 * �������ڣ�(2004-6-3 12:08:14)
	 * @return int
	 */
	protected Item getSelectedItem() {
		return (Item) getCmbPsncl().getSelectedItem();
	}

	/**
	 * �˴����뷽�������� 
	 * �������ڣ�(2004-6-3 11:36:53)
	 * @return javax.swing.tree.DefaultMutableTreeNode
	 */
	protected DefaultMutableTreeNode getSelectedNode() {
		// ��ȡ��ǰѡ��Ĳ���
		TreePath path = getDeptTree().getSelectionPath();
		if (path == null)
			return null;
		return (DefaultMutableTreeNode) path.getLastPathComponent();
	}

//	/**
//	 * ȡ�ð���ָ���Ӽ����˵���Ϣ
//	 * @author:wangkf ������
//	 */
//	protected PersonEAVO[] getSubInfo(String tableCode, String[] pk_psndocs) {
//		if (pk_psndocs == null || pk_psndocs.length < 1) {
//			return null;
//		}
//		PersonEAVO[] curPersons = new PersonEAVO[pk_psndocs.length];
//		try {
//			for (int i = 0; i < pk_psndocs.length; i++) {
//				curPersons[i] = new PersonEAVO();
//				curPersons[i].setPk_psndoc(pk_psndocs[i]);
//				GeneralVO[] infoVOs = HIDelegator.getPsnInf().queryPersonInfo(pk_psndocs[i], tableCode);
//				// ��ӵ���Ա���ݶ�����
//				SubTable subtable = new SubTable();
//				subtable.setTableCode(tableCode);
//				if (infoVOs != null) {// wangkf add
//					transPkToName(tableCode, infoVOs);
//					subtable.setRecordArray(infoVOs);
//				}
//				curPersons[i].addSubtable(subtable);
//			}
//		} catch (Exception e) {
//			reportException(e);
//			showErrorMessage(e.getMessage());
//		}
//		return curPersons;
//	}

	/**
	 * ��ȫ��ǰ���ڲ�������Ա�������� 
	 * �������ڣ�(2004-5-17 11:11:38)
	 * @return java.lang.String
	 */
	private String[] getTempPsnPk() {
		String[] keys = new String[2];
		// �����Ա���ݻ�û�г�ʼ��
		if (person == null) {
			if (listSelectRow != -1 && listSelectRow < psnList.length) {
				keys[0] = (String) psnList[listSelectRow].getAttributeValue("pk_psnbasdoc");
				keys[1] = (String) psnList[listSelectRow].getAttributeValue("pk_psndoc");
				return keys;
			} else {
				// û��ѡ���κ��ˣ�����null
				return null;
			}
		}
		// ���ص�ǰ�˵���Ա����
		keys[0] = (String) person.getPk_psnbasdoc();
		keys[1] = (String) person.getPk_psndoc();
		return keys;
	}

	/**
	 * �˴����뷽�������� 
	 * �������ڣ�(2004-6-3 12:02:24)
	 * @return int
	 */
	protected int getTempPsnScope() {
		return -1;
	}

	/**
	 * ����ʵ�ָ÷���������ҵ�����ı��⡣
	 * @version (00-6-6 13:33:25)
	 * @return java.lang.String
	 */
	public String getTitle() {
		return nc.ui.ml.NCLangRes.getInstance().getStrByID("600704","UPP600704-000069")/* @res "Ա����Ϣ�ɼ�" */;
	}

	/**
	 * �õ���ǰѡ�еĹ�˾������Ǹ����õ����ϵ����й�˾����һ���϶��ǵ�ǰ��˾��������˾���ڵڶ����� 
	 * �������ڣ�(2004-10-21 8:59:02)
	 * wangkf fixed :�����ӹ�˾�뵱ǰ��˾���е�һ����
	 * @return java.lang.String
	 */
	public String getTreeSelCorpPK() throws Exception {
		DefaultMutableTreeNode node = getSelectedNode();

		if (node == null || node.isRoot()) {
			//dusx �޸�2008.11.26�����Ǹ����򷵻ص�¼��˾��
//			if (allCorpPks == null) {
//			CtrlDeptVO[] Corpvos = HIDelegator.getPsnInf().queryAllRelatedCorps(Global.getUserID(), Global.getCorpPK(), true);
//			if(Corpvos!=null){
//			allCorpPks = new String[Corpvos.length];
//			for(int i=0;i<Corpvos.length;i++){
//			allCorpPks[i]= Corpvos[i].getPk_corp();
//			}
//			}

//			}
			return Global.getCorpPK();
		} else {
			CtrlDeptVO corpvo = (CtrlDeptVO) node.getUserObject();
			return  corpvo.getPk_corp() ;
		}
		//return allCorpPks;
		//dusx�޸ģ�2008.11.24
//		String nowcorp = "";

//		if (node == null || node.isRoot()) {
//		nowcorp = Global.getCorpPK();
//		}else{
//		CtrlDeptVO corpvo = (CtrlDeptVO) node.getUserObject();
//		nowcorp = corpvo.getPk_corp() ;
//		}
//		//����ǲ������¼�����ֻ����ѡ��ĵ�λ��
//		if(!getincludeChildDeptPsn().isSelected()){
//		return new String[]{nowcorp};
//		}

//		String[] selectCorppks = null;
//		CtrlDeptVO[] Corpvos = HIDelegator.getPsnInf().queryAllRelatedCorps(Global.getUserID(), nowcorp, true);
//		if(Corpvos!=null){
//		selectCorppks = new String[Corpvos.length];
//		for(int i=0;i<Corpvos.length;i++){
//		selectCorppks[i]= Corpvos[i].getPk_corp();
//		}
//		}

//		return selectCorppks;
	}
	/**
	 * ��ѯ���й�˾
	 * @return
	 * @throws Exception
	 */
	public String[] getAllCorpPks() throws Exception {

		if(allCorpPks ==null){
			CtrlDeptVO[] Corpvos = HIDelegator.getPsnInf().queryAllRelatedCorps(Global.getUserID(), Global.getCorpPK(), true);
			if(Corpvos!=null){
				allCorpPks = new String[Corpvos.length];
				for(int i=0;i<Corpvos.length;i++){
					allCorpPks[i]= Corpvos[i].getPk_corp();
				}
			}
		}
		return allCorpPks;
	}

	/**
	 * ���� ������Χ��
	 * @return nc.ui.pub.beans.UILabel
	 */
	protected nc.ui.pub.beans.UILabel getLbScope() {
		if (ivjUILabel1 == null) {
			try {
				ivjUILabel1 = new nc.ui.pub.beans.UILabel();
				ivjUILabel1.setName("UILabel1");
				ivjUILabel1.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"600704", "upt600704-000029")/* @res "������Χ" */);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjUILabel1;
	}

	private nc.ui.pub.beans.UILabel ivjUILabelRecords = null;
	/**
	 * 
	 * @return
	 */
	protected nc.ui.pub.beans.UILabel getUILabelRecords() {
		if (ivjUILabelRecords == null) {
			try {
				ivjUILabelRecords = new nc.ui.pub.beans.UILabel();
				ivjUILabelRecords.setName("UILabelRecords");
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjUILabelRecords;
	}

	/**
	 * ���� UILabelType ����ֵ��
	 * @return nc.ui.pub.beans.UILabel
	 */
	protected nc.ui.pub.beans.UILabel getUILabelType() {
		if (ivjUILabelType == null) {
			try {
				ivjUILabelType = new nc.ui.pub.beans.UILabel();
				ivjUILabelType.setName("UILabelType");
				ivjUILabelType.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("600704", "UPP600704-000065")/* @res "��ְ����" */);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjUILabelType;
	}

	/**
	 * ���� UIScrollPane1 ����ֵ��
	 * @return nc.ui.pub.beans.UIScrollPane
	 */
	protected nc.ui.pub.beans.UIScrollPane getUIScrollPane() {
		if (ivjUIScrollPane == null) {
			try {
				ivjUIScrollPane = new nc.ui.pub.beans.UIScrollPane();
				ivjUIScrollPane.setName("UIScrollPane");
				ivjUIScrollPane.setBorder(javax.swing.border.LineBorder.createGrayLineBorder());
				getUIScrollPane().setViewportView(getDeptTree());
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjUIScrollPane;
	}

	/**
	 * ���� UISplitPane1 ����ֵ��
	 * @return nc.ui.pub.beans.UISplitPane
	 */
	protected nc.ui.pub.beans.UISplitPane getUISplitPane() {
		if (ivjUISplitPane == null) {
			try {
				ivjUISplitPane = new nc.ui.pub.beans.UISplitPane(1);
				ivjUISplitPane.setName("UISplitPane");
				ivjUISplitPane.setDividerSize(4);
//				ivjUISplitPane.setBorder(new javax.swing.border.EmptyBorder(0,0, 0, 0));
				ivjUISplitPane.setDividerLocation(180);
				getUISplitPane().add(getUIScrollPane(), "left");
				getUISplitPane().add(getCenterPanelRight(), "right");
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjUISplitPane;
	}

	/**
	 * ���� UISplitPaneVertical ����ֵ
	 * @return nc.ui.pub.beans.UISplitPane
	 */
	protected nc.ui.pub.beans.UISplitPane getUISplitPaneVertical() {
		if (ivjUISplitPaneVertical == null) {
			try {
				ivjUISplitPaneVertical = new nc.ui.pub.beans.UISplitPane(javax.swing.JSplitPane.VERTICAL_SPLIT);
				ivjUISplitPaneVertical.setName("UISplitPaneVertical");
				ivjUISplitPaneVertical.setDividerLocation(200);
				ivjUISplitPaneVertical.setDividerSize(4);
				getUISplitPaneVertical().add(getPsnList(), "top");
				getUISplitPaneVertical().add(getPsnCard(), "bottom");
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjUISplitPaneVertical;
	}

	/**
	 * ��ȡ��ǰ������Χ������ڵ㣬ȱʡΪ��ǰ�ڵ������չ�ڵ�ţ��������������ء� 
	 * �������ڣ�(2004-6-1 9:36:41)
	 * @return java.lang.String
	 * @param nodeKey java.lang.String
	 */
	protected String getVirtualModuleCode(String nodeKey) {
		String moduleCode = getModuleName().substring(0,
				getModuleName().length() - 2);
		String ext;
		if (nodeKey == null)
			ext = "91";
		else if (nodeKey.equals("work"))
			ext = "91";
		else if (nodeKey.equals("dismiss"))
			ext = "93";
		else if (nodeKey.equals("retire"))
			ext = "95";
		else if (nodeKey.equals("leave"))
			ext = "97";
		else if (nodeKey.equals("other"))
			ext = "99";
		else
			ext = "91";
		return moduleCode + ext;
	}

	/**
	 * ���� PsnCard ����ֵ��
	 * @return nc.ui.hi.hi_301.HIBillCardPanel
	 */
	protected HIBillCardPanel getWorkCard() {
		if (ivjWorkCard == null) {
			try {
				ivjWorkCard = new nc.ui.hi.hi_301.HIBillCardPanel(allRelatedPara);
				ivjWorkCard.setName("WorkCard");
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjWorkCard;
	}

	/**
	 * ÿ�������׳��쳣ʱ������
	 * @param exception  java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {
		Logger.error("--------- " + nc.ui.ml.NCLangRes.getInstance().getStrByID("600700", 
				"UPP600700-000361")/* @res "δ��׽�����쳣" */ + " ---------");
		exception.printStackTrace(System.out);
	}

	/**
	 * ��ͷҳǩѡ��ʱ��Ӧ���¼����� 
	 * �������ڣ�(2004-5-13 16:26:18)
	 * @param event javax.swing.event.ChangeEvent
	 */
	protected void headTabbedPane_stateChanged(ChangeEvent event) {
		try {
			// û��ѡ���κ����򷵻�
			if (person == null){
				return;
			}
			// ��ǰ��Ա������
			String[] keys = getTempPsnPk();
			String tablecode = getCard().getHeadTabbedPane()
			.getSelectedTableCode();
			loadPersonMain(keys[0], keys[1], tablecode);
			if(getEditType() == EDIT_MAIN){
				getCard().transferFocusTo(0);
			}
		} catch (Exception e) {
			reportException(e);
			showErrorMessage(e.getMessage());
		}
	}


	/**
	 * ���ڵ���ģ�治֧���Զ��嵵������Ҫ�ֹ����� 
	 * �������ڣ�(2004-5-15 10:05:14)
	 * @param items nc.ui.pub.bill.BillItem[]
	 */
	private void initBillItems(BillItem[] items) throws Exception {
		// ����items
		for (int i = 0; i < items.length; i++) {
			final BillItem item = items[i];
			if (item.getDataType() == BillItem.UFREF) {
				// ��������е��Զ��嵵��
				final UIRefPane ref = (UIRefPane) item.getComponent();
				ref.setReturnCode(false);
				ref.setButtonFireEvent(true);
				String refNodeName = ref.getRefNodeName();
				if (item.getPos() == IBillItem.BODY) {
					if (item.getTableCode().equalsIgnoreCase("hi_psndoc_part")&& "pk_psncl".equals(item.getKey())) {
						String wherepart = ref.getRefModel().getWherePart();
						if (wherepart == null || wherepart.trim().length() == 0) {
							wherepart = " psnclscope = 0 ";
						} else {
							String whereScope = " (psnclscope = 0 ) and (";
							if (wherepart.indexOf(whereScope) < 0) {
								wherepart = whereScope + wherepart + ")";
							}
						}
						ref.getRefModel().setWherePart(wherepart);
					} else if (item.getTableCode().equalsIgnoreCase(
					"hi_psndoc_deptchg")) {
						if ("pk_post".equals(item.getKey()))
							ref.setEditable(false);
						// 
						else if ("pk_psncl".equals(item.getKey())) {
							String wherepart = ref.getRefModel().getWherePart();
							if (wherepart == null
									|| wherepart.trim().length() == 0) {
								wherepart = "( psnclscope = 0 or  psnclscope = 5 ) ";
							} else {
								String whereScope = " (psnclscope = 0 or  psnclscope = 5 ) and (";
								if (wherepart.indexOf(whereScope) < 0) {
									wherepart = whereScope + wherepart + ")";
								}
							}
							ref.getRefModel().setWherePart(wherepart);
						}
					}
					// wangkf add ֧�ֽ�����ѵ����ѵ���Ķ�ѡ
					if ("tra_type".equals(item.getKey())|| "tra_mode".equals(item.getKey())) {
						ref.setMultiSelectedEnabled(true);
					}
					ref.addValueChangedListener(new ValueChangedListener() {
						public void valueChanged(ValueChangedEvent e) {
							if (!isBatchAddSet()) {
								String pk = ref.getRefPK();
								String pkname = ref.getRefName();
								String itemKey = item.getKey();
								// wangkf add ֧�ֽ�����ѵ����ѵ������ѵ��ʽ�Ķ�ѡ
								if ("tra_type".equals(itemKey)|| "tra_mode".equals(itemKey)) {
									String[] traTypePks = ref.getRefPKs();
									if (traTypePks != null) {
										for (int jj = 0; jj < traTypePks.length; jj++) {
											if (jj == 0) {
												pk = traTypePks[jj];
											} else {
												pk += ",";
												pk += traTypePks[jj];
											}
										}
									}
								}
								CircularlyAccessibleValueObject record = person.getTableVO(item.getTableCode())[getEditLineNo()];
								record.setAttributeValue(Util.PK_PREFIX+ itemKey, pk);
								record.setAttributeValue(itemKey, pkname);
							}
						}
					});
				}
				if(item.getPos() == IBillItem.HEAD){
					if (item.getTableCode().equalsIgnoreCase("bd_psndoc")&& "pk_om_job".equals(item.getKey())) {
						if(ref!=null&&ref.getRefModel()!=null){
							if(ref.getRefModel().getWherePart()!=null&&ref.getRefModel().getWherePart().trim().length()>0){
								String wherepart = "  (om_job.isabort ='Y' or om_job.isabort= 'N') and om_job.pk_corp= '"+Global.getCorpPK() +"'";
								ref.getRefModel().setWherePart(wherepart);
							}else{
								ref.getRefModel().setWherePart("om_job.pk_corp= '"+Global.getCorpPK() +"'");
							}
						}
					}
				}
				if ("���ŵ���".equals(refNodeName) || "���ŵ���HR".equals(refNodeName)) {

					if (ref != null) {
						setWhereToModel(ref.getRefModel(),
						"((bd_deptdoc.canceled = 'N'  or bd_deptdoc.canceled is null))");

						// �����ܲ��ŵ���Ȩ�޿���
						if (item.getTableCode().equalsIgnoreCase(
						"hi_psndoc_deptchg")
						&& "pk_deptdoc".equals(item.getKey())) {
							ref.getRefModel().setUseDataPower(false);
						}
						if (item.getTableCode().equalsIgnoreCase(
						"hi_psndoc_part")
						&& "pk_deptdoc".equals(item.getKey())) {
							ref.getRefModel().setUseDataPower(false);
						}
					}
				}
				if ("��Ա����HR".equals(refNodeName) || "��Ա����".equals(refNodeName)) {
					if (ref != null) {
						ref.getRefModel().setUseDataPower(false);
						if (getModuleName().equals("600704")) {
							setWhereToModel(ref.getRefModel(),
							"((canceled = 'N' and hrcanceled = 'N') or ( canceled is null and hrcanceled is null))");
						} else {
							setWhereToModel(ref.getRefModel(),
							"((bd_deptdoc.canceled = 'N'  or bd_deptdoc.canceled is null))");
						}
					}
				}
				// �����ǰ���Զ��嵵������
				if (Util.isDefdocPk(refNodeName)) {
					Util.setDefdocRefModel(ref, refNodeName);
					// ����ĩ������
					ref.setNotLeafSelectedEnabled(item.isM_bNotLeafSelectedEnabled());
				}
			} else if (item.getDataType() == BillItem.COMBO) {
				if (item.getPos() == IBillItem.BODY) {
					final UIComboBox box = (UIComboBox) item.getComponent();
					box.addItemListener(new ItemListener() {
						public void itemStateChanged(ItemEvent event) {
							if(getEditLineNo()<0){
								return;
							}
							if (!isBatchAddSet) {
								CircularlyAccessibleValueObject record = person.getTableVO(item.getTableCode())[getEditLineNo()];
								String itemKey = item.getKey();
								Object obj = box.getSelectedItem();
								if (obj instanceof ComboBillItem.ComboItem) {
									ComboBillItem.ComboItem comboItem = (ComboBillItem.ComboItem) box.getSelectedItem();
									Object value = comboItem.getValue();
									record.setAttributeValue(Util.PK_PREFIX+ itemKey, value);
									record.setAttributeValue(itemKey, comboItem.getName());
								} else if (obj instanceof java.lang.String) {
									record.setAttributeValue(itemKey, obj.toString());
								}
							}
						}
					});
				}
			}
			else if (item.getDataType() == BillItem.BOOLEAN) {
				if (item.getPos() == IBillItem.BODY) {
					final UICheckBox check = (UICheckBox) item.getComponent();
					if (item.getTableCode().equalsIgnoreCase("hi_psndoc_edu")&& "lasteducation".equals(item.getKey())) {
						check.addActionListener(new CheckChangeListener());
					}
				}
			}
		}
	}
	/**
	 * v50 ������ѧ����Ϣ���ѧ������
	 * @author zhyan
	 *
	 */
	public class CheckChangeListener implements ActionListener{
		public void actionPerformed(ActionEvent event) {
			if (!isBatchAddSet) {
				String tablecode = getCurrentSetCode();
				CircularlyAccessibleValueObject[] oldrecords = person.getTableVO(tablecode);
				CircularlyAccessibleValueObject record = getCurrnentBillModel().getBodyValueRowVO(getEditLineNo(),"nc.vo.hi.hi_301.GeneralVO");
				if (((UFBoolean)record.getAttributeValue("lasteducation")).booleanValue()) {
					if (oldrecords != null && oldrecords.length > 0) {
						for (int i = 0; i < oldrecords.length; i++) {
							if(i!=getEditLineNo()){
								getCurrnentBillModel().setValueAt("N", i, "lasteducation");
							}
						}
					}
				}else {
					if (oldrecords != null && oldrecords.length > 0) {
						for (int i = 0; i < oldrecords.length; i++) {
							if(i!=getEditLineNo()){
								getCurrnentBillModel().setValueAt(oldrecords[i].getAttributeValue("lasteducation"), i, "lasteducation");
							}
						}
					}
				}
			}
		}
	}

	private boolean psncodeCanEdit = false;
	/**
	 * ��ʼ������ģ�塣 
	 * �������ڣ�(2004-5-9 17:05:25)
	 */
	protected void initBillTemp() throws Exception {
		// ���ص���ģ��
		loadTemplet();
		
		//��¼����ģ����bd_psndoc.psncode�Ŀɱ༭����(Ϊ�ˡ��޸ġ�ʱ�������Ƿ���Ա༭)��dusx add 2009.4.29
		psncodeCanEdit = getBillItem("bd_psndoc.psncode").isEdit();
		// ����ģ��Ĵ�����
		initBillTrigger();
		// ����ģ���ĳЩ����
		initTempAttr();
		// ��ʼ��Ϊ���ɱ༭
		getCard().setEnabled(false);
//		getCard().setShowMenuBar(false);
		getCard().setVisible(false);// V35 add
		// Ϊ�������Ӽ�����ʵ����¼�������
		initTabbedPaneListener();
		// Ϊ�Ӽ��ı���ӱ༭�¼�����������ѡ���¼�������
		initSubTableListener();
	}

	/**
	 * ��ʼ��ģ��Ĵ������� 
	 * �������ڣ�(2004-5-17 20:06:14)
	 */
	private void initBillTrigger() {
		// ������������
		for (int i = 0; i < triggerTable.triggers.length; i++) {
			// ����Դ
			String srcField = (String) triggerTable.triggers[i][0];
			// ��������
			String destField = (String) triggerTable.triggers[i][1];
			// ������
			ITriggerListener listener = (ITriggerListener) triggerTable.triggers[i][2];
			// ��ȡ����Դ������
			BillItem srcItem = getBillItem(srcField);
			// ��ȡ��������������
			BillItem destItem = getBillItem(destField);
			// ��鴥��Դ�ʹ��������Ƿ���Ч
			if (srcItem == null || destItem == null){
				continue;
			}
			// ʹ�ô�����������Դ�ʹ��������������
			addTriggerListener(srcItem, destItem, listener);
		}
	}

	/**
	 * ��ʼ����ť����ʼ״̬�� 
	 * �������ڣ�(2004-5-9 16:27:02)
	 */
	protected void initButton() throws Exception {
		switchButtonGroup(LIST_INIT_GROUP);// V35
		setButtonsState(LIST_INIT);// V35
	}

	/**
	 * ��ʼ������
	 */
	public void initConnections() throws java.lang.Exception {
		getCmbPsncl().addItemListener(ivjEventHandler);
		getCmbJobType().addItemListener(ivjEventHandler);
		getDeptTree().addTreeSelectionListener(ivjEventHandler);
		getDeptTree().addMouseListener(ivjEventHandler);
		getPsnList().addMouseListener(new BillTableMouseListener() {
			public void mouse_doubleclick(BillMouseEnent arg0) {
				if (EDIT_SORT != getEditType()) {
					PsnListMouseClicked(arg0);
				}
			}
		});

		getincludeCancleDept().addActionListener(new ActionListener()  {// �����ѳ��������¼���Ӧv5.3 add
			public void actionPerformed(ActionEvent e) {
				if (getincludeCancleDept().isSelected()) {
					deptTreeChanged(true);
				} else {
					deptTreeChanged(false);
				}

			}
		});
		getincludeChildDeptPsn().addActionListener(new ActionListener() {
			// �����¼���Ա�¼���Ӧv5.5 add
			public void actionPerformed(ActionEvent e) {
				connEtoC2(null);
			}
		});
		getIncludeHisPersonGroup().addActionListener(new ActionListener(){//������ʷ��Ա��v5.3 add �����Ա��
			public void actionPerformed(ActionEvent e){

				if (getIncludeHisPersonGroup().isSelected()) {									
					psnGroupTreeChanged(true);
				} else {
					psnGroupTreeChanged(false);
				}

			}
		});
		getIncludeHisPerson().addActionListener(new ActionListener(){//������ʷ�ؼ���Աv5.3 add ���ڽ������ڵĹؼ���Ա
			public void actionPerformed(ActionEvent e){

				if (getIncludeHisPerson().isSelected()) {									
					onRefresh();
				} else {
					onRefresh();
				}

			}
		});

		getIsSelSet().addActionListener(new ActionListener() {//�Ƿ���ʾ�Ӽ��¼���Ӧ
			public void actionPerformed(ActionEvent e) {
				if (getIsSelSet().isSelected()) {
					String tableCode = getBodyTableCode();
					person = loadPsnChildInfo(listSelectRow, tableCode);
					getUISplitPaneVertical().setDividerLocation(SPLIT_STANDARD);
					getUISplitPaneVertical().setEnabled(true);
					splitLocation = SPLIT_STANDARD;
				} else {
					getUISplitPaneVertical().setDividerLocation(SPLIT_MAX);
					getUISplitPaneVertical().setEnabled(false);
					splitLocation = SPLIT_MAX;
				}
			}
		});
		getIsShowSetHistory().addActionListener(new ActionListener() {//�Ƿ���ʾ��ʷ�¼���Ӧ
			public void actionPerformed(ActionEvent e) {
				int temprow = getPsnList().getTable().getSelectedRow();
				if(temprow==-1){
					return;
				}else{
					String tableCode = getBodyTableCode();
					loadPsnChildInfo(temprow, tableCode);
				}
			}
		});
	}
	/**

	 * ����ID�����ڵ�

	 * @param node

	 * @param id

	 * @return

	 */

	private DefaultMutableTreeNode findTreeNodeByID(DefaultMutableTreeNode node, String pk) {

		if (node == null || !StringUtils.hasText(pk)) {

			return null;

		}
		if (((CtrlDeptVO) node.getUserObject()).getNodeType() == CtrlDeptVO.CORP) {

			if (pk.equals(((CtrlDeptVO) node.getUserObject()).getPk_corp())) {
				return node;
			}
		}
		if (((CtrlDeptVO) node.getUserObject()).getNodeType() == CtrlDeptVO.DEPT) {

			if (pk.equals(((CtrlDeptVO) node.getUserObject()).getPk_dept())) {
				return node;
			}
		}

		int c = node.getChildCount();
		DefaultMutableTreeNode tmpNode = null;

		for (int i = 0; i < c; i++) {
			tmpNode = findTreeNodeByID((DefaultMutableTreeNode) node.getChildAt(i), pk);

			if (tmpNode != null) {

				return tmpNode;

			}

		}

		return null;

	}
	/**

	 * ����ѡ�еı���

	 * @param rptID

	 */

	public void setSelectPath(String pk) {

		if (!StringUtils.hasText(pk)) {

			return;

		}// end if

		DefaultMutableTreeNode root = (DefaultMutableTreeNode) getDeptTree().getModel().getRoot();

		int c = root.getChildCount();

		DefaultMutableTreeNode selNode = null;

		for (int i = 0; i < c; i++) {

			selNode = findTreeNodeByID((DefaultMutableTreeNode) root.getChildAt(i),pk);

			if (selNode != null) {

//				setSelectedNode(selNode);

				TreePath path = new TreePath(selNode.getPath());

				getDeptTree().setSelectionPath(path);

				getDeptTree().expandPath(path);

				break;

			}// end if

		}// end for

	}

	/**
	 * �Ƿ���ʾ��������
	 * 
	 * @param select
	 */
	protected void psnGroupTreeChanged(boolean select) {

	}
	/**
	 * �Ƿ���ʾ��������
	 * �����Ƿ������������ˢ����
	 * @param select
	 */
	protected void deptTreeChanged(boolean select) {
		try {			
			DefaultMutableTreeNode node = getSelectedNode();
			if(node ==null){
				return;
			}			
			// �ɼ�״̬����ά��״̬
			setCtrlDeptRoot(HIDelegator.getIHRhiQBS().queryRelatCorps(Global.getUserID(), Global.getCorpPK(), true));

			// ����getChildTree��֯��
			DefaultMutableTreeNode root = getChildTree(ctrlDeptRoot);
			// ��װ������model����
			treemodel = new DefaultTreeModel(root);
			DefaultTreeModel treeModel = getDeptTreeModel();
			if (treeModel != null) {
				getDeptTree().setModel(treeModel);
				getDeptTree().setCellRenderer(new DeptTreeCellRender());
			}

			CtrlDeptVO corpvo = (CtrlDeptVO) node.getUserObject();
//			boolean expand = false;
			if(corpvo.nodeType == CtrlDeptVO.CORP && corpvo.getPk_corp().equalsIgnoreCase(Global.getCorpPK())&&corpvo.isLoadDept()){
				corpvo.removeAllDeptChild();
				String strDeptPower = getDeptPowerByCorp(corpvo.getPk_corp());
				boolean isUsedPower = strDeptPower.length() > 3;

				corpvo.setModuleCode(getModuleName());
//				boolean includeHrCanceld = getincludeCancleDept().isSelected();//v53
				corpvo = HIDelegator.getPsnInf().queryCorpCtrlDepts(Global.getUserID(), corpvo, isUsedPower,select);
				node.setUserObject(corpvo);
				corpvo.setLoadDept(true);
				addDeptNodeToTree(node, corpvo);

				getDeptTreeModel().reload(node);
				getDeptTree().setCellRenderer(new DeptTreeCellRender());
//				getDeptTree().expandPath(path);
			}
			queryResult = new GeneralVO[0];
			psnList = queryResult;
			getPsnList().setBodyData(psnList);	
			recordcount = 0;

			String recordCountDecrip = nc.ui.ml.NCLangRes.getInstance().getStrByID("600704", "UPP600704-000192")/* @res "����" */;
			String tiao = nc.ui.ml.NCLangRes.getInstance().getStrByID("600704","UPP600704-000240")/* @res "��" */;

			Integer maxline = PubDelegator.getIParValue().getParaInt("0001", "HI_MAXLINE");

			String max = ","
				+ nc.ui.ml.NCLangRes.getInstance().getStrByID("600704","UPP600704-000239")/* @res "��ʾǰ" */
				+ (maxline ==null?1000:maxline.intValue())
				+ nc.ui.ml.NCLangRes.getInstance().getStrByID("600704","UPP600704-000240")/* @res "��" */;
			if (recordcount <= (maxline==null?1000:maxline.intValue())) {
				max = "";
			}
			getUILabelRecords().setText(recordCountDecrip + recordcount + tiao + max + "   ");
		} catch (Exception e) {
			reportException(e);
			showErrorMessage(e.getMessage());
		}
	}
	/**
	 * ��ʼ�����ݣ� ������Ա�������Ϳ�Ƭģ�塣 
	 * �������ڣ�(2004-5-9 16:27:54)
	 */
	private void initData() throws Exception {
		initHmField();// V35 add
		// ��ʼ����Ա������Χ
		initPsnScope();
		// ��ʼ����Ա������
		initDeptTree();
		// V35
		initListHeadItems();
		// ��ʼ����Ա�б�
		initPsnList();
//		// ����װ��ʱ��
//		long now = System.currentTimeMillis();
		// ��ʼ����Ƭģ��
		initBillTemp();
		if("600704".equalsIgnoreCase(getModuleName())){
			// ��ʼ����Ա��Ϣ����ӳ���
			initTraceTableMap();
		}
	}

	/**
	 * ��ʼ����Ա�������� 
	 * �������ڣ�(2004-5-9 17:08:41)
	 */
	protected void initDeptTree() throws Exception {
		// ��ȡ��ǰ�û���ID����ǰ��¼��˾���Ƿ����ò���Ȩ��,���ò���Ȩ��ʱ��Ȩ������
		String userID = Global.getUserID();
		String pk_corp = Global.getCorpPK();
		//�������Ż�
		
//		if (isUsedDeptPower) {//
//			powerSql = GlobalTool.getPowerSql("bd_deptdoc", userID, pk_corp);
//			if (powerSql == null) {
//				powerSql = "0=0";
//			}
//			deptPowerlist.put(pk_corp, powerSql);
//		} else {
//			deptPowerlist.put(pk_corp, "0=0");
//		}
		
		powerSql = GlobalTool.getPowerSql("bd_deptdoc", userID, pk_corp);
		if (powerSql == null) {
			powerSql = "0=0";
		}
		deptPowerlist.put(pk_corp, powerSql);
		// �ɼ�״̬����ά��״̬
		setCtrlDeptRoot(HIDelegator.getIHRhiQBS().queryRelatCorps(Global.getUserID(), Global.getCorpPK(), true));
		// ��Ա������,������model
		DefaultTreeModel treeModel = getDeptTreeModel();
		if (treeModel != null) {
			getDeptTree().setModel(treeModel);
			getDeptTree().setCellRenderer(new DeptTreeCellRender());
		}
	}

	/**
	 * ��ʼ���ࡣ
	 */
	protected boolean isCheckLicense_wordcard = false;
	protected void initialize() {
		
		//���licenseУ��
		isCheckLicense_wordcard = PluginsUtil.checkLicense("a87e8795-f6d2-4ef3-ae93-a858c2da3941");
		
		//��ʼ������
		initPara();
		try {

			bnSave.setEnabled(false);
			if (!checkEntrance()) {
				return;
			}
			setName("PsnInfCollectUI");
			setSize(864, 541);
			add(getNorthPanel(), "North");
			add(getCenterPanel(), "Center");
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		try {
			if(getModuleName().equalsIgnoreCase("600704")){
				intodocdirect = getIndocType();
				if(intodocdirect ==1){
					ButtonObject[] grpCollectIndoc = { bnAdd, bnEdit, bnDel, bnBatch,
							bnBatchAddSet,bnUpload, bnCard, bnBook,bnIndocApp, bnListItemSet, bnSort,bnSetSort,
							bnQuery, bnFresh, bnPrint };// ,
					grpCollect = grpCollectIndoc;
				}
			}
			if(getModuleName().equalsIgnoreCase("600707") || getModuleName().equalsIgnoreCase("600708")){
				caneditkeypsn = getCaneditKeypsn();
			}			// ��ʼ����ť
			initButtonGroup();
			initButton();
			// ��ʼ������
			initData();
			// ��ʼ����������
			initUI();
			// ��ʼ������
			initConnections();
			//��ȡ��ͬ�������޺ͺ�ͬ���޲��� v502 add by zhyan �ŵ�HiBillCardPanel�е�filter������ѯ			
			setTreeSelectButtonState((DefaultMutableTreeNode) (getDeptTree()
					.getModel().getRoot()));

			getUISplitPaneVertical().setEnabled(false);
		} catch (Exception e) {
			reportException(e);
			showHintMessage(e.getMessage());
		}
	}
	/**
	 * ��ȡ�ɼ���ʽ����
	 * 
	 */
	protected int getIndocType() {
		int indoc = 0;
		try {
			//Ч���Ż�start
//			indoc = PubDelegator.getIParValue().getParaInt(Global.getCorpPK(), "HI_INDOC").intValue();
			indoc = Integer.parseInt(getPara("HI_INDOC"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return indoc;
	}

	/**
	 * ��ȡ�༭�ؼ���Ա����
	 * 
	 */
	protected boolean getCaneditKeypsn() {
		boolean canedit = false;
		try {
			//Ч���Ż�
			//canedit = PubDelegator.getIParValue().getParaBoolean(Global.getCorpPK(), "HI_KEYPERSON").booleanValue();
			canedit = UFBoolean.valueOf((getPara("HI_KEYPERSON"))).booleanValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return canedit;
	}


	boolean isListDefault = true;
	/**
	 * 
	 * 
	 */
	protected void initListHeadItems() {
		try {			
			GeneralVO[] itemVOs = HIDelegator.getPsnInf().queryListItem(Global.getCorpPK(), getModuleName(), "seted");
			Pair[] pairs = convertToPairs(itemVOs);
			if (pairs == null || pairs.length == 0) {
				setListItems(getListItemsDefault());
				setListDefault(true);
			} else {
				setListItems(pairs);
				setListDefault(false);
			}
		} catch (Exception e) {
			reportException(e);
			showHintMessage(e.getMessage());
		}
	}

	/**
	 * ��ʼ����Ա�б� 
	 * �������ڣ�(2004-5-11 9:28:35)
	 */
	protected void initPsnList() {
		// ������Ա�б��tableModel
		getPsnList().setTableModel(getPsnListModel());
		getPsnList().getTable().getSelectionModel().addListSelectionListener(this);
		// ����ѡ��״̬Ϊ����ѡ��
		getPsnList().getTable().setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		// ������ʾ�к�
		getPsnList().setRowNOShow(true);
		getPsnList().removeTableSortListener();
		getPsnList().getTable().getModel().addTableModelListener(this);

		getPsnList().setHeadMultiSelected(true);
	}

	/**
	 * ��ѯ��Ա�б����Ա���ݡ� 
	 * �������ڣ�(2004-5-19 11:55:58)
	 */
	protected void initPsnListData() throws Exception {
		performQuery();
	}

	/**
	 * ��ʼ����Ա������Χѡ���б�ĳ�ʼ���ݡ� 
	 * �������ڣ�(2004-5-11 10:07:14)
	 */
	protected void initPsnScope() {
		getLbScope().setVisible(false);
		getCmbPsncl().setVisible(false);
		getUILabelType().setVisible(false);
		getCmbJobType().setVisible(false);
		getIsShowSetHistory().setVisible(false);//v50 add 
	}

	/**
	 * Ϊ�Ӽ��ı���ӱ༭�¼�����������ѡ���¼������� 
	 * �������ڣ�(2004-5-26 15:10:22)
	 */
	private void initSubTableListener() {
		// Ϊ�Ӽ��ı���ӱ༭�¼�����������ѡ���¼�������
		String[] tableCodes = getCard().getBillData().getBodyTableCodes();
		for (int i = 0; i < tableCodes.length; i++) {
			// ��ӱ༭������
			BillScrollPane bsp = getCard().getBodyPanel(tableCodes[i]);
			//add by zhyan 2006-12-23 ȥ������ģ������˫���Զ�����
			bsp.removeTableSortListener();
			UITable table = bsp.getTable();
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			// �����ѡ���¼�
			table.getSelectionModel().addListSelectionListener(
					new ListSelectionListener() {
						public void valueChanged(ListSelectionEvent event) {
							subTable_valueChanged(event);
						}
					});
		}
	}

	/**
	 * Ϊ�������Ӽ�����ʵ����¼������� 
	 * �������ڣ�(2004-5-26 15:06:31)
	 */
	private void initTabbedPaneListener() {
		// ��ͷ
		getCard().getHeadTabbedPane().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				headTabbedPane_stateChanged(event);
			}
		});
		// ����
		getCard().getBodyTabbedPane().addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent event) {
				bodyTabbedPane_stateChanged(event);
			}
		});
		//
		getCard().addEditListener(this);
	}

	/**
	 * ����ģ���ĳЩ���� 
	 * �������ڣ�(2004-5-15 10:02:02)
	 */
	private void initTempAttr() throws Exception {
		// ��ȡ����ģ������
		BillData billData = getCard().getBillData();
		// ���ñ�ͷ
		BillItem[] items = billData.getHeadItems();
		initBillItems(items);
		// ���ñ���
		String[] tableCodes = billData.getBodyTableCodes();
		for (int i = 0; i < tableCodes.length; i++) {
			items = billData.getBodyItemsForTable(tableCodes[i]);
			if (items != null)
				initBillItems(items);
		}
		//��¼ת�����ڵĳ�ʼ����
		boolean regularedit = getBillItem("bd_psndoc.regulardata").isEdit();
		if (htbRegularEdit.get("bd_psndoc.regulardata") == null) {
			htbRegularEdit.put("bd_psndoc.regulardata", new Boolean(regularedit));
		}
		//test
		
	}

	/**
	 * ��ʼ�����棬����ĳЩ���ԣ������塣 
	 * �������ڣ�(2004-5-11 10:21:07)
	 */
	private void initUI() {
		Util.initComponent(this);
		// ��ʼ������ģ�����׷����Ϣ��ҳǩ��ǰ�󱳾�ɫ��
		if (Util.TR_BGCOLOR == null && Util.TR_FGCOLOR == null){
			return;
		}
		BillTabbedPane btp = getCard().getBodyTabbedPane();
		int count = btp.getTabCount();
		for (int i = 0; i < count; i++) {
			BillScrollPane bsp = (BillScrollPane) btp.getComponentAt(i);
			String tableCode = getCard().getBodyTableCode(bsp);
			if (getCard().getTraceTables() != null&& getCard().getTraceTables().get(tableCode) != null) {
				if (Util.TR_BGCOLOR != null)
					btp.setBackgroundAt(i, Util.TR_BGCOLOR);
				if (Util.TR_FGCOLOR != null)
					btp.setForegroundAt(i, Util.TR_FGCOLOR);
			}
		}
	}

	/**
	 * ��ǰ�Ƿ����������Ա�� 
	 * @return boolean
	 */
	public boolean isAdding() {
		return adding;
	}

	/**
	 * ��ǰѡ�е��ӱ��Ƿ���׷����Ϣ���� �ɼ��ڵ�һͬ�Դ���ά���ڵ���д����Ҫ������
	 * 
	 * @return boolean
	 */
	protected boolean isBodyTraceTable() {
		// ׷����Ϣ������ʾ������<��ʼ
		return getCard().getTraceTables().get(getBodyTableCode()) != null;
	}

	/**
	 * ��ǰѡ�е��ӱ��Ƿ���׷����Ϣ����
	 * �ɼ��ڵ�"�����Ӽ�˳��"��,ά���ڵ�����Դ�
	 * @return boolean
	 */
	protected boolean Canmoved() {
		return traceTableMap.get(getBodyTableCode())!=null;
	}

	/**
	 * �����tablecode��field�ֶ��Ƿ�ɱ༭�� 
	 * @param tableCode
	 * @param field
	 * @return
	 */
	private boolean isEditable(String tableCode, String field) {
		// ��ȡģ��ӳ��ĸ��ٻ���
		Hashtable table = (Hashtable) getBillBodyMap().get(tableCode);
		// û�иñ�
		if (table == null){
			return false;
		}
		// �Ӹñ��ȡ��ȡ���ֶε�����
		BillTempletBodyVO fieldVO = (BillTempletBodyVO) table.get(field);
		if (fieldVO == null){
			// û�и��ֶ�
			return false;
		}
		// �Ƿ�ɱ༭��ʶ
		Boolean edit = (Boolean) fieldVO.getEditflag();
		// Ϊ����Ϊ���ɱ༭
		if (edit == null){
			return false;
		}
		// �����Ƿ�ɱ༭
		return edit.booleanValue();
	}

	/**
	 * ��ǰ�Ƿ����ڱ༭�ӱ��ĳһ�У���Ҫ���ֱ༭�к���ӡ�������
	 * @return boolean
	 */
	private boolean isEditLine() {
		return editLine;
	}
	/**
	 * �ж��Ƿ�Ϊ��
	 * @param obj
	 * @return
	 */
	public boolean isNULL(Object obj) {
		if (obj == null || obj.equals(""))
			return true;
		return false;
	}

	/**
	 * ���ظ��������Ƿ�Ϊ������ ������ɲ������㣻��������ֻ��Ϊ�ַ������� �硰������Ϊ�������������롱Ϊ��������
	 * IDataSource�ӿڷ���������ʵ��
	 */
	public boolean isNumber(java.lang.String itemExpress) {
		return false;
	}

	/**
	 * �ж��Ƿ��븨���������
	 * 
	 * @param record
	 * @param tableCode
	 * @return boolean
	 */
	protected boolean isUpdateAccRel(String[] fields, String tableCode) {
		// ��ù���ӳ���
		Hashtable map = getRelationMap();
		// �Ƿ��޸���
		boolean updated = false;
		if (fields != null) {
			for (int i = 0; i < fields.length; i++) {
				if (tableCode.equalsIgnoreCase("hi_psndoc_deptchg")
						|| tableCode.equalsIgnoreCase("hi_psndoc_dimission")) {// �ɼ��ڵ㲻ͬ����ְ��¼����ְ��¼������
					continue;
				}
				// ӳ����ȡ����Ϊ"table.field"
				String key = tableCode + "." + fields[i];
				String acc_fldcode = (String) map.get(key);
				if (acc_fldcode != null) {
					updated = true;
				}
			}
		}
		return updated;
	}

	/**
	 * ������Աpk_psndoc���������Ϣ
	 * @param pk_psnbasdoc
	 * @param pk_psndoc
	 * @param tableCode
	 * @throws Exception
	 */
	protected void loadPersonMain(String pk_psnbasdoc, String pk_psndoc,String tableCode) throws Exception {
		if (isEmployeeRef()) {// �����������Ա���⴦��			
			CircularlyAccessibleValueObject record = null;
			if (tableCode.equals("bd_psndoc")) {
				GeneralVO temprecord = person.getPsndocVO();
				if (temprecord == null) {
					if (!isEditing() && pk_psndoc != null) {
						GeneralVO[] personVOs = HIDelegator.getPsnInf().queryPersonInfo(pk_psndoc, "hi_psndoc_ref");// ���ǻ�����Ϣ�����Ϣ
						temprecord = (GeneralVO) personVOs[0].clone();
					}
				}

				// V35 add
				if (!isEditing() && temprecord != null) {
					if (!"E0020603".equals(getModuleName())
							&& !"E0020303".equals(getModuleName())
							&& !"E0041203".equals(getModuleName())) {// ���鿴������˾��Ա����ʱ����˾�Զ�������Ϊ��--begin---
						String psncorp = (String) temprecord.getAttributeValue("pk_corp");
						GeneralVO[] tempVOs = new GeneralVO[] { temprecord };
						if (!Global.getCorpPK().equals(psncorp)) {
							tempVOs = setDefItem(tempVOs, "");
						}
						temprecord = tempVOs[0];
					}
				}
				person.setPsndocVO(temprecord);
				
				if(person != null && person.getPsndocVO() != null && person.getPsndocVO().getAttributeValue("dutyname") != null&&!isLoadedBasicData){
					BillItem dutyItem = getBillItem("bd_psndoc.dutyname");
					UIRefPane dutyref = (UIRefPane) dutyItem.getComponent();
					nc.ui.hi.ref.DutyRef duty = null;// DutySimpleRef--��DutyRef
					duty = new nc.ui.hi.ref.DutyRef();		
					if(CommonValue.GROUPCODE!=Global.getCorpPK()){
						duty.setWherePart(
							"(duty.pk_corp = '"
								+ nc.vo.hi.pub.CommonValue.GROUPCODE
								+ "' or duty.pk_corp = '"
								+ person.getPsndocVO().getAttributeValue("pk_corp").toString()
								+ "')  and sery.pk_defdoc=duty.series");
					}
					dutyref.setRefType(1);
					dutyref.setRefInputType(1);
					dutyref.setRefModel(duty);
					dutyref.setPK(person.getPsndocVO().getAttributeValue("dutyname"));
				}
				
				if (!isEditing()) {// wangkf add �༭״̬ʱ�������½������� !isAdding()
					// !m_bnSave.isEnabled()
					record = person.getPsndocVO();
					// ����ֵ
					if (!isLoadedBasicData) {// wangkf add
						getCard().setBillValueVO(person);
						isLoadedBasicData = true;
					}
					getBillItem("bd_psndoc.clerkcode").setEdit(false);
				} else {
					record = null;
				}
				//add by lianglj 2012-03-20 ��Ա���õ�ʱ����ʾ��Ա���Զ�����Ϣֵ
				setRefBillValueVO();
				
			} else if (tableCode.equals("bd_psnbasdoc")) {// V35���Ĵ���--��
				GeneralVO tempaccpsndoc = person.getAccpsndocVO();
				if (tempaccpsndoc == null) {
					GeneralVO[] personVOs = HIDelegator.getPsnInf()
					.queryMainPersonInfo(pk_psnbasdoc,
							Global.getCorpPK(), "bd_psnbasdoc",
							GlobalTool.getFuncParserWithoutWa());
					tempaccpsndoc = (GeneralVO) personVOs[0].clone();
				}
				person.setAccpsndocVO(tempaccpsndoc);
				record = person.getAccpsndocVO();
				// ����ֵ
				if (!isLoadedAccData) {
					getCard().setBillValueVO(person);
					isLoadedAccData = true;
				}
				getBillItem("bd_psndoc.clerkcode").setEdit(false);
			}
			// ���������¼�
			if (record != null) {
				sequenceFire(tableCode, record);
			}
			if (tableCode.equals("bd_psndoc") && record != null&& Util.isDutyDependJobSeries) {
				BillItem item = getBillItem("bd_psndoc.jobseries");
				((UIRefPane) item.getComponent()).setPK((String) record.getAttributeValue("jobseries"));
			}
		} else {
			int jobtype = 0;
			if (isMaintain && listSelectRow >= 0) {
				jobtype = ((Integer) psnList[listSelectRow].getAttributeValue("jobtypeflag")).intValue();
			}
			CircularlyAccessibleValueObject record = null;
			if (tableCode.equals("bd_psndoc")) {
				GeneralVO temprecord = person.getPsndocVO();
				if (temprecord == null) {
					GeneralVO[] personVOs = HIDelegator.getPsnInf()
					.queryMainPersonInfo(pk_psndoc, Global.getCorpPK(),
							"bd_psndoc", GlobalTool.getFuncParserWithoutWa());// ���ǻ�����Ϣ�����Ϣ
					temprecord = (GeneralVO) personVOs[0].clone();
				}
				if (jobtype > 0) {
					String jianzhipk_corp = (String) psnList[listSelectRow]
					                                         .getAttributeValue("man_pk_corp");
					if (jianzhipk_corp != null&& !jianzhipk_corp.equals(Global.getCorpPK())) {
						String[] names = temprecord.getAttributeNames();
						if (names != null && names.length > 0) {
							for (int i = 0; i < names.length; i++) {
								temprecord.setAttributeValue(names[i], null);
							}
						}
					}				
					temprecord.setAttributeValue("psncode",psnList[listSelectRow].getAttributeValue("psncode"));
					temprecord.setAttributeValue("psnname",psnList[listSelectRow].getAttributeValue("psnname"));
					temprecord.setAttributeValue("pk_deptdoc",psnList[listSelectRow].getAttributeValue("pk_deptdoc"));
					temprecord.setAttributeValue("pk_psncl",psnList[listSelectRow].getAttributeValue("pk_psncl"));
					temprecord.setAttributeValue("pk_om_job",psnList[listSelectRow].getAttributeValue("pk_om_job"));
					// v35 add
					temprecord.setAttributeValue("dutyname",psnList[listSelectRow].getAttributeValue("pk_om_duty"));// pk_om_duty
					temprecord.setAttributeValue("series",psnList[listSelectRow].getAttributeValue("series"));
					temprecord.setAttributeValue("jobrank",psnList[listSelectRow].getAttributeValue("jobrank"));
					temprecord.setAttributeValue("jobseries",psnList[listSelectRow].getAttributeValue("jobseries"));
				}
//				else{
//				if (!"E0020603".equals(getModuleName())
//				&& !"E0020303".equals(getModuleName())
//				&& !"E0041203".equals(getModuleName())) {
//				temprecord.setAttributeValue("pk_deptdoc",psnList[listSelectRow].getAttributeValue("pk_deptdoc"));
//				temprecord.setAttributeValue("pk_psncl",psnList[listSelectRow].getAttributeValue("pk_psncl"));
//				temprecord.setAttributeValue("pk_om_job",psnList[listSelectRow].getAttributeValue("pk_om_job"));
//				// v35 add
//				temprecord.setAttributeValue("dutyname",psnList[listSelectRow].getAttributeValue("pk_om_duty"));// pk_om_duty
//				temprecord.setAttributeValue("series",psnList[listSelectRow].getAttributeValue("series"));
//				temprecord.setAttributeValue("jobrank",psnList[listSelectRow].getAttributeValue("jobrank"));
//				temprecord.setAttributeValue("jobseries",psnList[listSelectRow].getAttributeValue("jobseries"));
//				}
//				}
				if (!"E0020603".equals(getModuleName())
						&& !"E0020303".equals(getModuleName())
						&& !"E0041203".equals(getModuleName())) {// ���鿴������˾��Ա����ʱ����˾�Զ�������Ϊ��--begin---
					String psncorp = (String) temprecord.getAttributeValue("pk_corp");
					GeneralVO[] tempVOs = new GeneralVO[] { temprecord };
					if (!Global.getCorpPK().equals(psncorp)) {
						tempVOs = setDefItem(tempVOs, "");
					}
					temprecord = tempVOs[0];
				}
				person.setPsndocVO(temprecord);
				
				if(person != null && person.getPsndocVO() != null && person.getPsndocVO().getAttributeValue("dutyname") != null){
					BillItem dutyItem = getBillItem("bd_psndoc.dutyname");
					UIRefPane dutyref = (UIRefPane) dutyItem.getComponent();
					nc.ui.hi.ref.DutyRef duty = null;// DutySimpleRef--��DutyRef
					duty = new nc.ui.hi.ref.DutyRef();		
					if(CommonValue.GROUPCODE!=Global.getCorpPK()){
						GeneralVO psnVo = person.getPsndocVO();
						String pkCorp = "";
						if ( psnVo.getAttributeValue("pk_corp") != null ){
							pkCorp = psnVo.getAttributeValue("pk_corp").toString();
						}
						duty.setWherePart(
							"(duty.pk_corp = '"
								+ nc.vo.hi.pub.CommonValue.GROUPCODE
								+ "' or duty.pk_corp = '"
								+ pkCorp
								+ "')  and sery.pk_defdoc=duty.series");
					}
					dutyref.setRefType(1);
					dutyref.setRefInputType(1);
					dutyref.setRefModel(duty);
					dutyref.setPK(person.getPsndocVO().getAttributeValue("dutyname"));
				}
				
				if (!isEditing()) {// wangkf add �༭״̬ʱ�������½������� !isAdding()
					// !m_bnSave.isEnabled()
					record = person.getPsndocVO();
					// ����ֵ
					if (!isLoadedBasicData) {// wangkf add ��Ҫ��ϸ�����޸�
						getCard().setBillValueVO(person);
						isLoadedBasicData = true;
					}
					getBillItem("bd_psndoc.clerkcode").setEdit(false);
					getBillItem("bd_psndoc.regulardata").setEdit(false);
				} else {
					record = null;
				}
			} else if (tableCode.equals("bd_psnbasdoc")) {// V35���Ĵ���--��
				GeneralVO tempaccpsndoc = person.getAccpsndocVO();
				if (tempaccpsndoc == null) {
					GeneralVO[] personVOs = HIDelegator.getPsnInf().queryMainPersonInfo(pk_psnbasdoc, Global.getCorpPK(), "bd_psnbasdoc",
							GlobalTool.getFuncParserWithoutWa());
					tempaccpsndoc = personVOs[0] == null ? null: (GeneralVO) personVOs[0].clone();
				}
				if (!"E0020603".equals(getModuleName())&& !"E0020303".equals(getModuleName())
						&& !"E0041203".equals(getModuleName())) {
					// ���鿴������˾��Ա����ʱ����˾�Զ�������Ϊ��
					GeneralVO bdpsnrecord = person.getPsndocVO();
					String psncorp = null;
					if (bdpsnrecord != null) {
						psncorp = (String) bdpsnrecord.getAttributeValue("pk_corp");
					}
					GeneralVO[] tempVOs = new GeneralVO[] { tempaccpsndoc };
					if (!Global.getCorpPK().equals(psncorp)) {
						tempVOs = setDefItem(tempVOs, "");
					}
					tempaccpsndoc = tempVOs[0];
				}
				person.setAccpsndocVO(tempaccpsndoc);
				if (!isEditing()) {// wangkf add �༭״̬ʱ�������½�������
					// !m_bnSave.isEnabled()
					record = person.getAccpsndocVO();
					// ����ֵ
					if (!isLoadedAccData) {// wangkf add
						getCard().setBillValueVO(person);
						isLoadedAccData = true;
					}
					getBillItem("bd_psndoc.clerkcode").setEdit(false);
				} else {
					record = null;
				}

			}
			// ���������¼�
			if (record != null) {
				sequenceFire(tableCode, record);
			}
			if (tableCode.equals("bd_psndoc") && record != null
					&& Util.isDutyDependJobSeries) {// bd_accpsndoc
				BillItem item = getBillItem("bd_psndoc.jobseries");
				((UIRefPane) item.getComponent()).setPK((String) record
						.getAttributeValue("jobseries"));
			}
		}
	}
	
	//��Ա���õ�ʱ������ģ������ݣ���Ա������Ϣ����ʾ���ż��Զ�����Ϣ add by lianglj 2012-03-20
	private void setRefBillValueVO( ){
		String pk_psndoc = person.getPk_psndoc();
		GeneralVO temprecord = null;
		if(pk_psndoc != null){
			String[] pk_psndocs = {pk_psndoc};
			GeneralVO[] personVOs;
			try {
				personVOs = HIDelegator.getPsnInf().queryPsndocInfo(pk_psndocs,Global.getCorpPK() );
				temprecord = (GeneralVO) personVOs[0].clone();
				String[] tempfiled = temprecord.getAttributeNames();
				for(int i=0;i<tempfiled.length;i++){
					if(tempfiled[i].contains("groupdef")&&getBillItem("bd_psndoc."+tempfiled[i])!=null){
						getBillItem("bd_psndoc."+tempfiled[i]).setValue(temprecord.getAttributeValue(tempfiled[i]));
					}
					//start add by fanghfa 20120727 ����Ա���ý��棬�ѡ�ְ�񼶱�(pk_dutyrank)��������Ҳ��ԭ������Ϣ��¼�д������������ù����¼�У�
					else if(tempfiled[i].contains("pk_dutyrank")&&getBillItem("bd_psndoc."+tempfiled[i])!=null){
						getBillItem("bd_psndoc."+tempfiled[i]).setValue(temprecord.getAttributeValue(tempfiled[i]));
					}
					//end add by fanghfa 20120727
					else{
						temprecord.removeAttributeName(tempfiled[i]);
					}
				}
				person.setPsndocVO(temprecord);
			} catch (BusinessException e) {
				e.printStackTrace();
			}// ���ǻ�����Ϣ�����Ϣ
		}
	}

	/**
	 * װ�ص���ģ�塣 
	 * �������ڣ�(2004-5-12 15:06:12)
	 */
	protected void loadTemplet() {
		// �ɼ���Ա��������������������ȡ����ģ��
		loadTempletByModuleCode(getModuleName());
	}

	/**
	 * ����ģ�� v50 �ı䷽ʽ
	 * 
	 * @param moduleCode
	 */
	protected void loadTempletByModuleCode(String moduleCode) {
		// ����ģ��
		String nodekey = null;
		if(moduleCode.startsWith("60079")){
			nodekey = moduleCode;
		}
		try {
			getCard().loadTemplet(getModuleName(), null, Global.getUserID(),
					Global.getCorpPK(), nodekey);
			// V35 add
			getCard().setPosMaximized(BillItem.BODY);
			getCard().getBodyTabbedPane().setTabLayoutPolicy(
					ExtTabbedPane.LIST_SCROLL_TAB_LAYOUT);
			getCard().setShowMenuBar(false);
		} catch (Exception e) {
			e.printStackTrace();
			// û�У���ʹ��ȱʡģ��
			getCard().loadTemplet(getModuleName(), null, Global.getUserID(), Global.getCorpPK(),nodekey);
			getCard().setPosMaximized(BillItem.BODY);
			getCard().getBodyTabbedPane().setTabLayoutPolicy(JTabbedPane.WRAP_TAB_LAYOUT);
			getCard().setShowMenuBar(false);
		}
	}

	/**
	 * ����¼
	 * @param psnpk
	 * @return
	 * @throws java.lang.Exception
	 */
	public boolean lockPsn(String psnpk) throws java.lang.Exception {
		try {
			return PKLock.getInstance().acquireLock(psnpk,nc.ui.hr.global.Global.getUserID(), null);
		} catch (Exception e) {
			reportException(e);
			throw e;
		}
	}
	/**
	 * ��Ƹ��Ա�޸���Ա������,ֻΪ��ְ����������Ա
	 *
	 */
	private void changePsncl() {
		UIRefPane psncl = (UIRefPane) getBillItem("bd_psndoc.pk_psncl").getComponent();
		nc.ui.bd.ref.AbstractRefModel refmodel = psncl.getRefModel();
		refmodel.setWherePart(" psnclscope in ( 0,5 ) and pk_corp in('"+Global.getCorpPK()+"','0001')");
		psncl.setRefModel(refmodel);
	}
	/**
	 * �����Ա�¼�����
	 * @throws java.lang.Exception
	 */
	private void onAdd() throws java.lang.Exception {
		setAdding(true);// ����Ϊ���״̬
		
		if (!loadedToTempPkCorp.equalsIgnoreCase(Global.getCorpPK())) {// ֻ�ı�һ�β���
			changeBillTempRef(Global.getCorpPK());
			loadedToTempPkCorp = Global.getCorpPK();
		}
		
		ResetJobRef();
		
		
		//�����������õ�addnew֮ǰ��Ϊ���Ǵ������ֶ�Ĭ��ֵ���������ϡ�
		UIRefPane psncl = (UIRefPane) getBillItem("bd_psndoc.pk_psncl").getComponent();
		psncl.setRefModel(new PsnClsRef());
		UIRefPane jobRef = (UIRefPane) getBillItem("bd_psndoc.pk_om_job").getComponent();			
		if (jobRef != null) {
			jobRef.getRefModel().setWherePart(" om_job.pk_corp ='"+Global.getCorpPK()+"'");
		}
		
		getCard().addNew();
		person = null;
		isLoadedAccData = true;
		isLoadedBasicData = true;
		onEdit();
		// ���³�ʼ������
		if (checkAddDlg != null) {
			checkAddDlg.initDate();
		}
		// ��������Ψһ�������Ի���
		getAddCheckDlg().onReset();
		if (getAddCheckDlg().showModal() == UIDialog.ID_OK) {
			BillItem[] items = checkAddDlg.getAllBillItems();
			GeneralVO vo = checkAddDlg.getDatavo();
			isrehire = checkAddDlg.isRehire();
			isbackrehire = checkAddDlg.isBackrehire();		

			if(isrehire){
				setEditType(EDIT_RETURN);
				String rehirepsncorp = checkAddDlg.getRehirePersonCreatcorp();
				setRehirePsnBelonpk(rehirepsncorp);
				String pk_psnbasdoc = checkAddDlg.getRehirePersonpk();
				setRehirePsnbaspk(pk_psnbasdoc);
				String pk_psndoc = checkAddDlg.getRehirePersonworkpk();
				setRehirePsnpk(pk_psndoc);
				loadRehirePerson(pk_psnbasdoc ,rehirepsncorp,pk_psndoc);
				changePsncl();
				//Ĭ���ڸ�
				BillItem poststatItem = getBillItem("bd_psndoc.poststat");
				if(poststatItem !=null){
					poststatItem.setValue(UFBoolean.TRUE);
				}
			}else if (isbackrehire){
				setEditType(EDIT_RETURN);
				String rehirepsncorp = checkAddDlg.getRehirePersonCreatcorp();
				setRehirePsnBelonpk(rehirepsncorp);
				String pk_psnbasdoc = checkAddDlg.getRehirePersonpk();
				setRehirePsnbaspk(pk_psnbasdoc);
				String pk_psndoc = checkAddDlg.getRehirePersonworkpk();
				setRehirePsnpk(pk_psndoc);
				loadRehirePerson(pk_psnbasdoc ,rehirepsncorp,pk_psndoc);
				changePsncl();
				//Ĭ���ڸ�
				BillItem poststatItem = getBillItem("bd_psndoc.poststat");
				if(poststatItem !=null){
					poststatItem.setValue(UFBoolean.TRUE);
				}
			}else {
				for (int i = 0; i < items.length; i++) {
					BillItem item = getCard().getHeadItem(items[i].getKey());
					item.setValue(vo.getAttributeValue(items[i].getKey()));
					if (htbEdit.get(items[i].getKey()) == null) {
						htbEdit.put(items[i].getKey(), new Boolean(item
								.isEdit()));
					}
					item.setEdit(false);
					if ("id".equalsIgnoreCase(items[i].getKey())) {// ���ó������ڣ�����,���Ϻ�
						getCard().getHeadItem("birthdate").setValue(
								vo.getAttributeValue("birthdate"));
						getCard().getHeadItem("sex").setValue(
								vo.getAttributeValue("sex"));
						getCard().getHeadItem("ssnum").setValue(
								vo.getAttributeValue("ssnum"));

					}
				}


			}
			getCard().getBodyTabbedPane().setEnabled(false);
			getCard().execHeadEditFormulas();
			getCard().transferFocusTo(0);//��ͷ��ý���
		} else {
			onCancelOp();
		}
	}

	private void ResetJobRef() {
		BillItem jobItem = getBillItem("bd_psndoc.pk_om_job");
		UIRefPane jobref = (UIRefPane) jobItem.getComponent();
		nc.ui.hi.ref.JobRef job = null;
		job = new nc.ui.hi.ref.JobRef(Global.getCorpPK(), false);
		jobref.setRefType(1);
		jobref.setRefInputType(1);
		jobref.setRefModel(job);
	}
	/**
	 * v53
	 * ���ط�Ƹ��Ա��Ϣ
	 *
	 */
	public void loadRehirePerson(String pk_psnbasdoc,String rehirepsncorp,String pk_psndoc) throws Exception {
		GeneralVO tempaccpsndoc = null;
		if (tempaccpsndoc == null) {
			GeneralVO[] personVOs = HIDelegator.getPsnInf().queryMainPersonInfo(
					pk_psnbasdoc, Global.getCorpPK(), "bd_psnbasdoc",
					GlobalTool.getFuncParserWithoutWa()); 
			tempaccpsndoc = personVOs[0] == null ? null: (GeneralVO) personVOs[0].clone();
		}
		PersonEAVO rehireperson = new PersonEAVO();
		tempaccpsndoc.removeAttributeName("indocflag");
		tempaccpsndoc.setAttributeValue("pk_corp",Global.getCorpPK());
		tempaccpsndoc.setAttributeValue("belong_pk_corp",Global.getCorpPK());
		rehireperson.setAccpsndocVO(tempaccpsndoc);
		//�����Ƿ񱾹�˾��Ƹ,��Ҫ��ѯԭ��˾��Ϣ,ȡ��Ա����

		if(rehirepsncorp.equalsIgnoreCase(Global.getCorpPK())){//����˾��Ƹ,������Ϣ����
			GeneralVO temppsndoc = null;
			if (temppsndoc == null) {
				// 
				GeneralVO[] personVOs = HIDelegator.getPsnInf()
				.queryMainPersonInfo(pk_psndoc, Global.getCorpPK(),
						"bd_psndoc", GlobalTool.getFuncParserWithoutWa());
				temppsndoc = personVOs[0] == null ? null
						: (GeneralVO) personVOs[0].clone();
			}
			if(temppsndoc !=null){
				temppsndoc.removeAttributeName("pk_psncl");
				temppsndoc.removeAttributeName("outdutydate");
				temppsndoc.setAttributeValue("indutydate", Global.getLogDate());
			}
			getBillItem("bd_psndoc.outdutydate").setEnabled(true);
			rehireperson.setPsndocVO(temppsndoc);
			getCard().setBillValueVO(rehireperson);
		}else{//�繫˾��Ƹ,����й�����Ϣ�����
			GeneralVO temppsndoc = null;
			if (temppsndoc == null) {
				GeneralVO[] personVOs = HIDelegator.getPsnInf().queryPersonInfo(pk_psnbasdoc, "bd_psndoc",Global.getCorpPK());
				if(personVOs!=null && personVOs.length>0){
					temppsndoc =(GeneralVO) personVOs[0].clone();
				}				
			}
			GeneralVO[] personVOss = HIDelegator.getPsnInf().queryPersonInfo(pk_psnbasdoc, "bd_psndoc",rehirepsncorp);
			if(temppsndoc !=null){
				temppsndoc.removeAttributeName("pk_psncl");	
				temppsndoc.removeAttributeName("outdutydate");
				temppsndoc.setAttributeValue("indutydate", Global.getLogDate());
			}
			rehireperson.setPsndocVO(temppsndoc);
			getCard().setBillValueVO(rehireperson);
			//����ǿ繫˾��Ƹ�����ұ���˾����ǰ�Ĺ�����Ϣ
			if(personVOss!=null && temppsndoc==null){
				//���ű���Ψһ������£���Ҫ����ԭ��Ա����
				if("Y".equalsIgnoreCase(isUniquePsncodeInGroup)){
					Object psncode = personVOss[0].getAttributeValue("psncode");
					if(psncode!=null){
						getBillItem("bd_psndoc.psncode").setValue(psncode);
					}
					//��˾����Ψһ�������
				}else{
					//�Զ�����
					if((getPsncodeAutoGenerateParam() != null) && getPsncodeAutoGenerateParam().trim().equals("1")){
						//�Զ����ɱ��룬ֱ��ת��Ա����,��ѱ༭ʱ�����ı����ù���
						if (intodocdirect == 0) {// ֱ��ת��Ա����,��ѱ༭ʱ�����ı����ù���
							getBillItem("bd_psndoc.psncode").setValue(autopsncode);
						}
						//����ְ�������򽫱������ÿգ�������ͨ�����ٲ������룬���ǻ������������,��ֹ���룬�����һ��
						else if (intodocdirect == 1) {
							resetBillcode();
							getBillItem("bd_psndoc.psncode").setValue("");
						}
						//�ֹ����룬����ԭ��Ա����
					}else{
						Object psncode = personVOss[0].getAttributeValue("psncode");
						if(psncode!=null){
							getBillItem("bd_psndoc.psncode").setValue(psncode);
						}
					}
				}
			}
		}
		if(rehireperson.getPsndocVO()!=null && rehireperson.getPsndocVO().getAttributeValue("pk_deptdoc")!= null){
			String pk_deptdoc =(String)rehireperson.getPsndocVO().getAttributeValue("pk_deptdoc");
			Object pk_om_job =rehireperson.getPsndocVO().getAttributeValue("pk_om_job");
			UIRefPane jobRef = (UIRefPane) getBillItem("bd_psndoc.pk_om_job").getComponent();			
			if (jobRef != null) {
				jobRef.getRefModel().setWherePart(" om_job.pk_deptdoc ='"+pk_deptdoc+"'");
				jobRef.getRefModel().clearData();
//				//dusx�޸ģ���һ�䲻�����ã�ʹ����������Բ��ս���ˢ��
//				((nc.ui.hi.hi_301.ref.JobRef)jobRef.getRefModel()).setPk_deptdoc(pk_deptdoc);
//				jobRef.getRefModel().reloadData();
//				//dusx�޸Ľ���2009.6.25
			}
			if(pk_om_job!=null){
				jobRef.setPK((String)pk_om_job);
			}
		}
//		String psncode = null;
//		if ("Y".equalsIgnoreCase(isUniquePsncodeInGroup)) {// ����Ǽ�����Ψһ���򰴼��Ų�����Ա����			
//		if ((getPsncodeAutoGenerateParam() != null) && getPsncodeAutoGenerateParam().trim().equals("1")){//�Զ�����
//		if(intodocdirect == 0){//ֱ��ת��Ա����
//		psncode = HIDelegator.getBillcodeRule().getBillCode_RequiresNew("BS", "0001", null, getObjVO());
//		getBillItem("bd_psndoc.psncode").setValue(psncode);					
//		}else if(intodocdirect == 1){//����ְ����
//		psncode = "";
//		getBillItem("bd_psndoc.psncode").setValue(psncode);
//		}
//		}else{
//		psncode = "";
//		getBillItem("bd_psndoc.psncode").setValue(psncode);
//		}

//		} else {//��˾Ψһ
//		if ((getPsncodeAutoGenerateParam() != null) && getPsncodeAutoGenerateParam().trim().equals("1")) {//�Զ�����
//		if(rehirepsncorp.equalsIgnoreCase(Global.getCorpPK())){//����˾��Ƹ,������Ϣ����
//		if (intodocdirect == 0) {// ֱ��ת��Ա����
//		psncode = HIDelegator.getBillcodeRule().getBillCode_RequiresNew("BS", Global.getCorpPK(), null, getObjVO());
//		getBillItem("bd_psndoc.psncode").setValue(psncode);
//		} else if (intodocdirect == 1) {// ����ְ����
//		psncode = "";
//		getBillItem("bd_psndoc.psncode").setValue(psncode);
//		}
//		} else {//�繫˾
//		if (intodocdirect == 0) {// ֱ��ת��Ա����
//		psncode = HIDelegator.getBillcodeRule().getBillCode_RequiresNew("BS", Global.getCorpPK(), null, getObjVO());
//		getBillItem("bd_psndoc.psncode").setValue(psncode);
//		} else if (intodocdirect == 1) {// ����ְ����
//		psncode = "";
//		getBillItem("bd_psndoc.psncode").setValue(psncode);
//		}
//		}
//		}else{
//		psncode = "";
//		getBillItem("bd_psndoc.psncode").setValue(psncode);
//		}
//		}
		isLoadedAccData = true;

	}

	/**
	 * v53
	 * ���ط�Ƹ��Ա�Ӽ���Ϣ
	 *
	 */
	public void loadRehirePersonChildInfo(String pk_psnbasdoc,String rehirepsncorp,String pk_psndoc) throws Exception {

		try {			
			if (person == null) {
				person = new PersonEAVO();
				person.setPk_psndoc(pk_psndoc);
				person.setPk_psnbasdoc(pk_psnbasdoc);
			}
			String tablecode = getBodyTableCode();
			//�Ƿ���ҵ���Ӽ�
			boolean isTraceTable = getCard().getTraceTables().get(tablecode) != null;

			//v50 add 
			if(isTraceTable){//�����ҵ���Ӽ�
				//��ѯ��ҵ���Ӽ��Ƿ�����鿴��ʷ
				boolean look = HIDelegator.getPsnInf().isTraceTableLookHistory(tablecode);
				//�õ��������û��Ƿ�ѡ��鿴��ʷ����
				boolean islookhistory = IsLookHistory();
				//�Ƿ��ǹ�����˾��Ա��belong_pk_corp,������pk_corp
				boolean isbelongcorp = rehirepsncorp.equalsIgnoreCase(Global.getCorpPK());

				//��ҵ���Ӽ�����鿴��ʷ���û�ѡ�в鿴��ʷ���Ҹ���Ա�Ǳ���˾��Ա�������²�ѯ�Ӽ����ݣ���Ϊ�Ƿ���ʾ��ʷ���ܱ仯
				if(look && islookhistory && isbelongcorp){
					//����Ѿ���ѯ�����������ҵ���Ӽ�����
					if (person.getSubtables().get(tablecode) != null) {
						person.removeSubtable(tablecode);
					}
					//��ѯ�ñ�ļ�¼��ҵ���Ӽ�����鿴��ʷ������
					loadPsnChildSub(person.getPk_psndoc(),person.getPk_psnbasdoc(),tablecode,isTraceTable,islookhistory,person.getPk_psndoc());				
				}else{
					//����Ѿ���ѯ�����������ҵ���Ӽ�����
					if (person.getSubtables().get(tablecode) != null) {
						person.removeSubtable(tablecode);
					}
					// ���ص�ǰ��Ա�ĸñ����Ϣ��¼��ҵ���Ӳ��鿴��ʷ
					loadPsnChildSub(person.getPk_psndoc(),person.getPk_psnbasdoc(),tablecode,isTraceTable,false,person.getPk_psndoc());											
				}
			}else{//�����ҵ���Ӽ�
				if (person.getSubtables().get(tablecode) == null) {
					// ���ص�ǰ��Ա�ĸñ����Ϣ��¼����ҵ���Ӽ�Ĭ�ϲ鿴��ʷ
					loadPsnChildSub(person.getPk_psndoc(),person.getPk_psnbasdoc(),tablecode, isTraceTable,true,person.getPk_psndoc());
				}
			}			
			CircularlyAccessibleValueObject[] records = person.getTableVO(tablecode);// ��ǰ��Ϣ�������м�¼
			getCard().getBillModel().setBodyDataVO(records);// ���ñ������

		} catch (Exception e) {
			reportException(e);
			showErrorMessage(e.getMessage());
		}
	}

	/**
	 * V35 add ��ѡ����е��������õ�����������
	 * @param selectrow
	 * @param newRow
	 * @return
	 */
	private GeneralVO setSelRowToNewRow(int selectrow, int newRow) {
		GeneralVO vo = new GeneralVO();
		if (selectrow >= 0) {
			if (person != null) {
				CircularlyAccessibleValueObject[] records = person.getTableVO(getCurrentSetCode());
				if (records != null && records.length > 0) {
					vo = (GeneralVO) records[selectrow].clone();
					if ("hi_psndoc_edu".equalsIgnoreCase(getCurrentSetCode())) {
						vo.setAttributeValue("lasteducation", null);
					}
					getCard().getBillModel(getCurrentSetCode()).setBodyRowVO(vo, newRow);
					// �����ؼ�
				}
			}
		}
		//�������һ�еġ��Ƿ��ڸڡ�Ϊtrue�����������Ϊfalse fengwei 2009-09-21
		//-----------------------------------------------------
		if (person != null) {
			CircularlyAccessibleValueObject[] records = person.getTableVO(getCurrentSetCode());
			if (records != null && records.length > 0) {
				for(CircularlyAccessibleValueObject rec : records){
					if("hi_psndoc_deptchg".equalsIgnoreCase(getCurrentSetCode())){
						rec.setAttributeValue("poststat", UFBoolean.FALSE);
					}
				}
			}
		}
		//-----------------------------------------------------
		
		if ("hi_psndoc_ctrt".equalsIgnoreCase(getCurrentSetCode())) {
			vo.setAttributeValue("icontstate", nc.ui.ml.NCLangRes.getInstance().getStrByID("600704", "UPP600704-000050")/* @res "����" */);
			vo.setAttributeValue(Util.PK_PREFIX + "icontstate", new Integer(2));
			vo.setAttributeValue("iconttype", nc.ui.ml.NCLangRes.getInstance().getStrByID("600704", "UPP600704-000072")/* @res "ǩ��" */);
			vo.setAttributeValue(Util.PK_PREFIX + "iconttype", new Integer(1));
			vo.setAttributeValue("pk_corp", getClientEnvironment().getCorporation().getUnitname());
			vo.setAttributeValue("$pk_corp", Global.getCorpPK());
			vo.setAttributeValue("dsigndate", Global.getLogDate());
			vo.setAttributeValue("pk_majorcorp", getClientEnvironment().getCorporation().getUnitname());
			vo.setAttributeValue("$pk_majorcorp", Global.getCorpPK());
		}
		else 
			if ("hi_psndoc_deptchg".equalsIgnoreCase(getCurrentSetCode())) {
				vo.setAttributeValue(Util.PK_PREFIX + "pk_corp", Global.getCorpPK());
				vo.setAttributeValue("pk_corp", getClientEnvironment().getCorporation().getUnitname());
				vo.setAttributeValue("poststat", UFBoolean.TRUE);
			}
			else 
				if ("hi_psndoc_dimission".equalsIgnoreCase(getCurrentSetCode())
						||"hi_psndoc_retire".equalsIgnoreCase(getCurrentSetCode())
						||"hi_psndoc_training".equalsIgnoreCase(getCurrentSetCode())
						||"hi_psndoc_orgpsn".equalsIgnoreCase(getCurrentSetCode())
						||"hi_psndoc_psnchg".equalsIgnoreCase(getCurrentSetCode())
						||"hi_psndoc_keypsn".equalsIgnoreCase(getCurrentSetCode())) {
					vo.setAttributeValue("pk_corp", getClientEnvironment().getCorporation().getUnitname());
				}else
					if ("hi_psndoc_ass".equalsIgnoreCase(getCurrentSetCode())) {
						vo.setAttributeValue("pk_corpperson", getClientEnvironment().getCorporation().getUnitname());
						vo.setAttributeValue("pk_corpassess", getClientEnvironment().getCorporation().getUnitname());
					}
					else//��������ʱ�޶�Ӧ��ְ��¼
						if ("hi_psndoc_work".equalsIgnoreCase(getCurrentSetCode())) {
							vo.setAttributeValue("pk_deptchg", null);
						}
		vo.setAttributeValue("pk_psndoc_sub", null);
		return vo;
	}

	/**
	 * �����ӱ��¼
	 * @throws java.lang.Exception
	 */
	protected void onAddChild() throws java.lang.Exception {
		if("600707".equalsIgnoreCase(getModuleName())){
			String sql = " select 1 from hi_psndoc_keypsn where pk_psndoc ='"+person.getPk_psndoc()+"'and enddate is null";
			boolean iskeypsn =HIDelegator.getPsnInf().isRecordExist(sql);
			if(iskeypsn && !caneditkeypsn){
				showWarningMessage(  nc.ui.ml.NCLangRes.getInstance().getStrByID("600707","UPT600707-000239")/* @res "����ԱΪ�ؼ���Ա,�뵽�ؼ���Ա����ڵ�ά������Ϣ��"*/);
				return;
			}
		}
		//V56���ӣ�У���Ƿ����δ��˵ģ���Ա�䶯�����ݡ�
		HIDelegator.getPsnInf().checkIfExistsUnAuditBillofPSN(new String[]{person.getPk_psndoc()},getBodyTableCode());

		// 
		if (!isEditSub()) {
			if (!lockPsn(person.getPk_psndoc() + getBodyTableCode())) {

				showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"600704", "UPP600704-000070")/*
						 * @res "����Ա��"
						 */
						+ getBodyTableName()
						+ nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
						"UPP600704-000071")/*
						 * @res "�Ӽ������������û����������Ժ����ԣ�"
						 */);
				return;
			}
		}
		// ���õ���ģ�����ҳǩ�����л�
		getCard().getBodyTabbedPane().setEnabled(false);
		// ���ڱ༭�ӱ�
		setEditType(EDIT_SUB);		
		// �������޸���
		setEditLine(false);
		// �������ڱ༭�к�
		int rowcount = getCard().getBillTable().getRowCount();
		setEditLineNo(rowcount);
		int selectrow = getCard().getBillTable(getCurrentSetCode()).getSelectedRow();
		// ���һ����
		getCard().removeEditListener(getBodyTableCode());

		// ��ʧȥ����ò�������
		TableCellEditor editor = getCard().getBillTable().getCellEditor();
		if (editor != null) {
			editor.stopCellEditing();
		}

		getCard().getBodyPanel().addLine();
		getCard().addEditListener(this);
		GeneralVO copyVO = setSelRowToNewRow(selectrow, rowcount);
		// ���õ���ģ��Ϊ�ɱ༭״̬
		setBillTempletState(true);
		// Ϊ��ǰ�ӱ����һ�пռ�¼
		SubTable subtable = person.getSubTable(getBodyTableCode());
		subtable.getRecords().addElement(copyVO);

		// setSelRowRefValueNull(getBodyTableCode());
		// ��ְ�������Ĭ��ֵ wangkf add
		if (getBodyTableCode().equalsIgnoreCase("hi_psndoc_deptchg")) {
			if (nc.ui.hi.hi_301.Util.isDutyDependJobSeries) {
				UIRefPane dutyRef = null;
				UIRefPane jobRef = null;
				BillItem item = getBillItem("hi_psndoc_deptchg.pk_om_duty");
				if (item != null) {
					dutyRef = (UIRefPane) (item.getComponent());
				}
				jobRef = (UIRefPane) getBillItem("hi_psndoc_deptchg.pk_postdoc").getComponent();
				String oldWhere = dutyRef.getRefModel().getWherePart();
				if (jobRef != null) {
					String pk_jobseries = (String) jobRef.getRefValue("om_job.jobseries");
					if (oldWhere.indexOf("and duty.series ='") >= 0) {
						oldWhere = oldWhere.substring(0, oldWhere.indexOf("and duty.series ='"));
					}
					if (pk_jobseries != null) {
						String newSql = oldWhere + " and duty.series ='"+ pk_jobseries.trim() + "'";
						dutyRef.getRefModel().setWherePart(newSql);
					} else {
						dutyRef.getRefModel().setWherePart(oldWhere);
					}
				}
			}
			getCard().getBodyPanel().getTableModel().setBodyDataVO(
					(CircularlyAccessibleValueObject[]) subtable.getRecords().toArray(new CircularlyAccessibleValueObject[0]));
		} else if (getBodyTableCode().equalsIgnoreCase("hi_psndoc_ctrt")
				||getBodyTableCode().equalsIgnoreCase("hi_psndoc_dimission")
				||"hi_psndoc_retire".equalsIgnoreCase(getCurrentSetCode())
				||"hi_psndoc_training".equalsIgnoreCase(getCurrentSetCode())
				||"hi_psndoc_orgpsn".equalsIgnoreCase(getCurrentSetCode())
				||"hi_psndoc_ass".equalsIgnoreCase(getCurrentSetCode())
				||"hi_psndoc_psnchg".equalsIgnoreCase(getCurrentSetCode())
				||"hi_psndoc_keypsn".equalsIgnoreCase(getCurrentSetCode())) {
			getCard().getBodyPanel().getTableModel().setBodyDataVO(
					(CircularlyAccessibleValueObject[]) subtable.getRecords().toArray(new CircularlyAccessibleValueObject[0]));
		}
		if (isNotMultiEdit()) {
			// ���ð�ť״̬
			setAddChildButtonState();
			// ���õ�ǰ�пɱ༭
			enableTableLine(getEditLineNo());
		} else {
			if (!isEditSub()) {
				setEditSub(true);		
				getCard().getBodyPanel().getTableModel().updateValue();
			}
			// ���ð�ť״̬
			setMultEditChildButtonState();
		}
		setUnEditable();//v50 add
		//��ֹ��ͷ���ʱ�༭�Ӽ����������Ӽ�
		getCard().setPosMaximized(-1);

	}
	/**
	 * v50 add ������ҪĬ�ϵ��ֶ�,ҵ���Ӽ���pk_corp��
	 *
	 */
	private void setUnEditable(){
		if (getBodyTableCode().equalsIgnoreCase("hi_psndoc_ctrt")) {
			//v50 add
			UIRefPane corpref = null;
			BillItem item = getBillItem("hi_psndoc_ctrt.pk_corp");
			if(item!=null){
				corpref = (UIRefPane) (item.getComponent());
				corpref.setPK(Global.getCorpPK());
				corpref.setText(getClientEnvironment().getCorporation().getUnitname());
				corpref.setEnabled(false);
				corpref.setEditable(false);
			}
			BillItem itemMajor = getBillItem("hi_psndoc_ctrt.pk_majorcorp");
			if (itemMajor != null) {
				corpref = (UIRefPane) (itemMajor.getComponent());
				corpref.setPK(Global.getCorpPK());
				corpref.setText(getClientEnvironment().getCorporation().getUnitname());
				//corpref.setEnabled(false);	
				//corpref.setEditable(false);
			}

		}else if (getBodyTableCode().equalsIgnoreCase("hi_psndoc_dimission")) {
			//v50 add
			UIRefPane corpref = null;
			BillItem item = getBillItem("hi_psndoc_dimission.pk_corp");
			if(item!=null){
				corpref = (UIRefPane) (item.getComponent());
				corpref.setPK(Global.getCorpPK());
				corpref.setText(getClientEnvironment().getCorporation().getUnitname());
				corpref.setEnabled(false);
				corpref.setEditable(false);
			}
		}else if (getBodyTableCode().equalsIgnoreCase("hi_psndoc_retire")) {
			//v50 add
			UIRefPane corpref = null;
			BillItem item = getBillItem("hi_psndoc_retire.pk_corp");
			if(item!=null){
				corpref = (UIRefPane) (item.getComponent());
				corpref.setPK(Global.getCorpPK());
				corpref.setText(getClientEnvironment().getCorporation().getUnitname());
				corpref.setEnabled(false);
				corpref.setEditable(false);
			}
		}else if (getBodyTableCode().equalsIgnoreCase("hi_psndoc_training")) {
			//v50 add
			UIRefPane corpref = null;
			BillItem item = getBillItem("hi_psndoc_training.pk_corp");
			if(item!=null){
				corpref = (UIRefPane) (item.getComponent());
				corpref.setPK(Global.getCorpPK());
				corpref.setText(getClientEnvironment().getCorporation().getUnitname());
				corpref.setEnabled(false);
				corpref.setEditable(false);
			}
		}else if (getBodyTableCode().equalsIgnoreCase("hi_psndoc_orgpsn")) {
			//v50 add
			UIRefPane corpref = null;
			BillItem item = getBillItem("hi_psndoc_orgpsn.pk_corp");
			if(item!=null){
				corpref = (UIRefPane) (item.getComponent());
				corpref.setPK(Global.getCorpPK());
				corpref.setText(getClientEnvironment().getCorporation().getUnitname());
				corpref.setEnabled(false);
				corpref.setEditable(false);
			}
		}else if (getBodyTableCode().equalsIgnoreCase("hi_psndoc_ass")) {
			// v50 add
			UIRefPane corpref = null;
			BillItem item = getBillItem("hi_psndoc_ass.pk_corpperson");
			if (item != null) {
				corpref = (UIRefPane) (item.getComponent());
				corpref.setPK(Global.getCorpPK());
				corpref.setText(getClientEnvironment().getCorporation().getUnitname());
				corpref.setEnabled(false);
				corpref.setEditable(false);
			}
			// v50 add
			UIRefPane assessref = null;
			BillItem assessitem = getBillItem("hi_psndoc_ass.pk_corpassess");
			if (item != null) {
				assessref = (UIRefPane) (assessitem.getComponent());
				assessref.setPK(Global.getCorpPK());
				assessref.setText(getClientEnvironment().getCorporation().getUnitname());
				assessref.setEnabled(false);
				assessref.setEditable(false);
			}
		}else if (getBodyTableCode().equalsIgnoreCase("hi_psndoc_psnchg")) {
			//v50 add
			UIRefPane corpref = null;
			BillItem item = getBillItem("hi_psndoc_psnchg.pk_corp");
			if(item!=null){
				corpref = (UIRefPane) (item.getComponent());
				corpref.setPK(Global.getCorpPK());
				corpref.setText(getClientEnvironment().getCorporation().getUnitname());
				corpref.setEnabled(false);
				corpref.setEditable(false);
			}
		}

	}

	/**
	 * �����޸İ�ť��Ӧ�¼�������
	 * @throws java.lang.Exception
	 */
	public void onBatch() throws java.lang.Exception {
		if(getPsnList().getHeadSelectedRows().length<1){
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("600710","UPT600710-000047")/*@res ���ڸ�ѡ���й�ѡҪ�޸ĵ���Ա!"*/);
			return;
		}
		// ���������޸ĵ�ʹ�õĵ���ģ��
		getBatchDlg().setHIBillCardPanel(getCard());
		getBatchDlg().showModal();
		// ��������޸Ľ��
		BatchUpdateDlg.BatchResult batchResult = getBatchDlg().getBatchResult();
		if (batchResult != null) {
			// ȷ��֮��
			GeneralVO value = batchResult.getValue();
			String tableCode = batchResult.getTableCode();
			String fieldCode = batchResult.getFieldCode();
			//V56���ӣ�У���Ƿ����δ��˵ģ���Ա�䶯�����ݡ�
			HIDelegator.getPsnInf().checkIfExistsUnAuditBillofPSN(getselectPsnPksExceptRef("bd_psndoc"),tableCode);

			// ��������
			HIDelegator.getPsnInf().batchUpdate(getselectPsnPksExceptRef(tableCode),
					tableCode, fieldCode, value.getAttributeValue(fieldCode),getModuleName(),Global.getCorpPK());
			// wangkf add ���¸������еĹ����ֶ�
			String key = tableCode + "." + fieldCode;
			String acc_fldcode = (String) getRelationMap().get(key);
			if (acc_fldcode != null) {
				String relateTable = acc_fldcode.substring(0, acc_fldcode
						.indexOf("."));
				// ��������ǰ���淢���䶯���������ݿ�
				HIDelegator.getPsnInf().batchUpdate(getselectPsnPksExceptRef(tableCode), relateTable, acc_fldcode,
						value.getAttributeValue(fieldCode),getModuleName(),Global.getCorpPK());
			}
			//�建��
			persons.clear();
			// ˢ�½���
			onRefresh();
		}
	}


	/**
	 * �����ᰴť��Ӧ�¼�������
	 * @throws java.lang.Exception
	 */
	protected void onBook() throws java.lang.Exception {
		// ��õ�ǰ��Ա�б������
		DefaultMutableTreeNode node = getSelectedNode();
		if (node == null)
			return;
		CtrlDeptVO dept = getSelectedDept();
		Vector conds = new Vector();
		if (conditions != null) {
			// ��ӵ�ǰ��ѯ����
			for (int i = 0; i < conditions.length; i++) {
				conds.addElement(conditions[i]);
			}
		}
		if (!node.isRoot()) {
			//update by sunxj 2010-02-03 ���ٲ�ѯ��� start
//			if (dept != null && isQuery) {
			if (dept != null && (isQuery || isQuickSearch)) {
			//update by sunxj 2010-02-03 ���ٲ�ѯ���  end
				if (dept.getNodeType() == CtrlDeptVO.DEPT) {
					// ���Ӵ˲�������vo�ˣ����������Ѿ���wheresql���ˣ����ҿ����˰����¼��������
//					ConditionVO deptCond = new ConditionVO();
//					deptCond.setTableCode("bd_psndoc");
//					deptCond.setFieldCode("bd_psndoc.pk_deptdoc");
//					deptCond.setOperaCode("=");
//					deptCond.setValue(dept.getPk_dept());
//					conds.addElement(deptCond);
				} else {
					// ���Ӵ˹�˾����vo�ˣ����������Ѿ���wheresql���ˣ����ҿ����˰����¼��������
//					ConditionVO deptCond = new ConditionVO();
//					deptCond.setTableCode("bd_psndoc");
//					deptCond.setFieldCode("bd_psndoc.pk_corp");
//					deptCond.setOperaCode("=");
//					deptCond.setValue(dept.getPk_corp());
//					conds.addElement(deptCond);
				}
			}
			if (vBookConds.size() > 0) {
				for (int i = 0; i < vBookConds.size(); i++) {
					ConditionVO vo = new ConditionVO();
					//update by sunxj 2010-02-03 ���ٲ�ѯ��� start
//					if (!isQuery) {
					if (!isQuery && !isQuickSearch) {
					//update by sunxj 2010-02-03 ���ٲ�ѯ���  end
						if (i == 0) {
							vo.setLogic(true);
						} else {
							vo.setLogic(false);
						}
					} else {
						vo.setLogic(false);
					}
					vo.setDataType(5);
					vo.setTableCode("bd_psndoc");
					vo.setFieldCode("bd_psndoc.pk_psndoc");
					vo.setOperaCode("=");
					vo.setValue((String) vBookConds.elementAt(i));
					conds.addElement(vo);
				}
			}
		}
		PsnInfReportDlg dlg = new PsnInfReportDlg(this);
		//ע�������order��������Ա�ɼ�/ά����ѯʱ��order����һ��
//		String order = null;
//		if (getModuleName().equals("600704")){
//		order = " order by bd_psndoc.showorder asc,bd_corp.unitcode asc,bd_deptdoc.deptcode asc,bd_psndoc.psncode asc,om_job.jobcode asc,bd_psncl.psnclasscode asc,om_duty.dutycode asc ";
//		}else if (getModuleName().equals("600707")){
//		Item item = (Item) getCmbPsncl().getSelectedItem();
//		if (item != null){
//		if (item.getValue()>0){
//		order = " order by bd_psndoc.showorder asc,bd_corp.unitcode asc,bd_deptdoc.deptcode asc,bd_psndoc.psncode asc,om_job.jobcode asc,bd_psncl.psnclasscode asc,om_duty.dutycode asc ";
//		}else {
//		order = " order by bd_psndoc.showorder asc,bd_corp.unitcode asc,bd_deptdoc.deptcode asc,bd_psndoc.psncode asc,om_job.jobcode asc,hi_psndoc_deptchg.jobtype asc,bd_psncl.psnclasscode asc,om_duty.dutycode asc ";
//		}
//		}
//		}
		String order = getConfigDialog().getOrderStr();
		dlg.setOrder(order);
		if (getModuleName().equalsIgnoreCase("600707") || getModuleName().equalsIgnoreCase("600708")) {
			Item itemcl = (Item) getCmbPsncl().getSelectedItem();
			Item itemjob = (Item) getCmbJobType().getSelectedItem();
			if (itemcl != null && itemcl.getValue() == 0 && itemjob != null
					&& itemjob.getValue() != -1) {
				// ������ְ��������
				ConditionVO cond = new ConditionVO();
				cond.setTableCode("bd_psndoc");
				cond.setFieldCode("bd_psndoc.jobtypeflag");
				cond.setOperaCode("=");
				cond.setDataType(1);
				cond.setValue("" + itemjob.getValue() + "");
				conds.addElement(cond);
			}
		}
		dlg.setCorpWhereStr(getTreeSelCorpPK());
		dlg.setQueryConditionVOs((ConditionVO[]) conds.toArray(new ConditionVO[0]));
		dlg.setIPsnScope(getTempPsnScope());
		dlg.setIndocflag(getIndocflag());
		//�����Ƿ�Ҫ�����¼���˾�����ţ�
		dlg.setIncludesubcorp(getincludeChildDeptPsn().isSelected());
		String dlg_wheresql = getQueryDialog().getWhereSQL();

		if (isQuickQuery) {
			dlg_wheresql = quickDLGsql;
		}
		// dlg.setNormalsql(wheresql);
		if (dlg_wheresql != null)
			dlg.setNormalsql(wheresql + " and " + dlg_wheresql);
		else
			dlg.setNormalsql(wheresql);
		//�ɼ��鿴�����������Ҫ�Ͳɼ��Ĳ�ѯ��������һ�£�Ĭ�ϲ�ѯδ����ְ���뵥����Ա��
		if (getModuleName().equals("600704")){
			if(dlg.getNormalsql().indexOf("bd_psnbasdoc.approveflag") <0){
				dlg.setNormalsql( dlg.getNormalsql()+" and (bd_psnbasdoc.approveflag in (0,1) or bd_psnbasdoc.approveflag is null) ");
			}
		}
		
		String power = (String) deptPowerlist.get(getTreeSelCorpPK());
		if (power != null && power.length() > 4) {
			dlg.setDeptStr(" bd_psndoc.pk_deptdoc in (" + power + ") ");
		} else {
			dlg.setDeptStr(" 1 = 1 ");
		}
		dlg.showDetail();
	}

	/**
	 * �����ť�¼��������� 
	 */
	private void onBrowse() throws Exception {
		// ��õ�ǰѡ���У�������Ƿ���Ч
		int row = getCard().getBillTable().getSelectedRow();
		int rowCount = getCard().getBillTable().getRowCount();
		if (row < 0 || rowCount == 0 || row >= rowCount){
			return;
		}
		// ��õ�ǰ�ӱ�����м�¼
		CircularlyAccessibleValueObject[] records = person.getTableVO(getBodyTableCode());
		// ��õ�ǰѡ���е����ĵ�λ��
		String remotePath = (String) records[row].getAttributeValue("vdocloc");
		// ����ĵ���Ϊ�գ������
		if (remotePath != null)
			FileTransClient.browseServerFile(remotePath);
	}

	/**
	 * ��Ӧ��ť�¼���
	 */
	public void onButtonClicked(nc.ui.pub.ButtonObject bo) {
		try {
			if (bo == bnAdd) {
				onAdd();
			} else if (bo == bnSave) {
				onSave();
			} else if (bo == bnCancel) {
				onCancelOp();
			} else if (bo == bnSort) {
				onSort();
			} else if (bo == bnReturn) {
				onReturn();
			} else if (bo == bnEdit) {
				onEdit();
			} else if (bo == bnDel) {
				onDel();
			} else if (bo == bnAddChild) {
				onAddChild();
			} else if (bo == bnInsertChild) {
				onInsertChild();
			} else if (bo == bnUpdateChild) {
				onUpdateChild();
			} else if (bo == bnDelChild) {
				onDelChild();
			} else if (bo == bnIntoDoc) {
				onIntoDoc();
			} else if (bo == bnUpload) {
				onUpload();
			} else if (bo == bnFresh) {
				onRefresh();
			} else if (bo == bnSmUser) {
				onSmUser();
			} else if (bo == bnBatch) {
				onBatch();
			} else if (bo == bnQuery) {
				onQuery();
			} else if (bo == bnPrint) {
				onPrint();
			} 
			else if (bo == bnCard) {
				onCard();
			} 
			else if (bo == bnBook) {
				onBook();
			} else if (bo == bnExportPic) {
				exportPic();
			} else if (bo == bnList) {
				onList();
			} else if (bo == bnUpRecord) {
				onUpRecord();
			} else if (bo == bnDownRecord) {
				onDownRecord();
			} else if (bo == bnToFirst) {
				onToFirst();
			} else if (bo == bnToLast) {
				onToLast();
			} else if (bo == bnListItemSet) {
				onListItemSet();
			} else if (bo == bnBatchAddSet) {
				onBatchAddSet();
			} else if (bo == bnApplicate) {
				onApplicate();
			} else if (bo == bnBatchApplicate) {
				onBatchApplicate();
			} else if (bo == bnAffirm) {
				onAffirm();
			} else if (bo == bnQueryAffirm) {
				onQueryAffirm();
			} else if (bo == bnCancelAffirm) {
				onCancelAffirm();				
			} else if (bo ==bnSubsetMove){
				onSubsetMove();
			} else if (bo ==bnIndocApp){
				onOpenIndocApp();
			}else if (bo == bnSetSort) {//����
				onSetOrderAndSort();
			} else if (bo == bnView) {
				onCard();
			} else if (bo == bnBatchExport){
				onBatchExport();
			}
		} catch (Exception e) {
			reportException(e);
			String erromsg = e.getMessage();
			//modified by zhangdd, ��У�鹫ʽ�ṩ֧��
			if (erromsg.equalsIgnoreCase("ValidateFormulasValue")) return;
			//�Ѿ���ʾ�����쳣��������ʾ��ֱ���˳���dusx add
			if(erromsg.startsWith("HAVEPOPMESSAGE")) return;
			if(e.getMessage().startsWith("psncode")){
				erromsg = e.getMessage().substring(7);
				codeflag = false;
			}
			showErrorMessage(erromsg);
		}
	}

	protected boolean isEmployeeRef = false;

	public static final int TYPE_USERID = 0;

	public static final int TYPE_PK_CORP = 1;

	/**
	 * 
	 *����ְ����ڵ�
	 */
	protected void onOpenIndocApp()  throws java.lang.Exception{
		DocLinkAddData linkdata = new DocLinkAddData();
		if(psnList==null || psnList.length<1){
			SFClientUtil.showNode("600705", 2);
		}
		//�ҵ�����˾����Ա
		Vector<GeneralVO> volist = new Vector<GeneralVO>();
		int[] rows = getPsnList().getHeadSelectedRows();
		if(rows.length<1) SFClientUtil.showNode("600705", 2);
		for(int i=0;i<rows.length;i++){
			GeneralVO vo = psnList[rows[i]];
			vo.setAttributeValue("pk_psndoc_showname", vo.getAttributeValue("psnname"));
			if(Global.getCorpPK().equals(vo.getAttributeValue("pk_corp")))
				volist.add(vo);
		}
		GeneralVO[] vos =new GeneralVO[volist.size()];
		volist.copyInto(vos);
		if(vos==null || vos.length<1){
			SFClientUtil.showNode("600705", 2);
		}
		//����ɼ��ڵ��ѯ����Ա����ֱ�Ӵ�����ڵ�󣬽��б��е���Ա�����µ����뵥

		linkdata.setPsnList(vos);
		
		//wangli add 20121205 ����Ƿ������δ��
		CheckIndocData(vos);

		boolean succ = false;
		IFuncWindow window = SFClientUtil.findOpenedFuncWindow("600705");
		if (window != null) {
			succ = window.closeWindow();
		}else{
			succ = true;
		}
		//
		if (succ) {
			SFClientUtil.openNodeLinkedADD("600705",linkdata);// �򿪽ڵ�
		}else{
//			showWarningMessage("�����Ѿ���,û����ȷ�ر�!");
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("600700", 
					"UPP600700-000114")/* @res "�����Ѿ���,û����ȷ�ر�!" */);
		}

	}

	/**
	 * �鿴���밴ť��Ӧ�¼�
	 * @throws java.lang.Exception
	 */
	public void onQueryAffirm() throws java.lang.Exception {
		// �鿴�������Ա
		nc.ui.hi.hi_301.RefAffirmDlg queryAffirm = new nc.ui.hi.hi_301.RefAffirmDlg(this, true);
		queryAffirm.setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
		"UPP600704-000204")/* @res "�鿴����" */);
		queryAffirm.setCancelBtnVisible(false);
		queryAffirm.showModal();
	}
	/**
	 * �����Ӽ�˳��ť��Ӧ�¼� v50 add 
	 * @throws java.lang.Exception
	 */
	public void onSubsetMove() throws java.lang.Exception {
		String currentSetCode = getCurrentSetCode();
		CircularlyAccessibleValueObject[] records = person.getTableVO(currentSetCode);// ��ǰ��Ϣ�������м�¼
		if (records != null && records.length > 1) {
			// �����Ӽ�˳��
			MoveSubsetDlg movesubset = new MoveSubsetDlg(this);
			movesubset.setTitle(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"600704", "UPT600704-000273")/* @res "�����Ӽ�˳��" */);
			movesubset.showModal();
			if(movesubset.getResult()==UIDialog.ID_OK){
				CircularlyAccessibleValueObject[] returnrecords = movesubset.getBodyData();
				SubTable subtable = new SubTable();
				subtable.setTableCode(currentSetCode);
				if (returnrecords != null) {
					subtable.setRecordArray(returnrecords);
				}
				if(person.getSubTable(currentSetCode)!=null){
					person.getSubtables().put(currentSetCode, returnrecords);
				}
				person.addSubtable(subtable);
			}
			loadPsnChildInfo(listSelectRow, currentSetCode)	;		

		}else{
//			showErrorMessage("��ǰ�Ӽ�����Ҫ����˳��");
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("600700", 
					"UPP600700-000115")/* @res "��ǰ�Ӽ�����Ҫ����˳��" */);
			return;
		}
	}

	String app_pk_corp = null;// ���빫˾

	/**
	 *���ñ༭����
	 */
	protected void setHeadItemEnableInRef() {
		setHeadBillItemEditable("bd_psnbasdoc", false);
		setHeadBillItemEditable("bd_psndoc", false);
		getBillItem("bd_psndoc.psncode").setEdit(true);
		getBillItem("bd_psndoc.psncode").setEnabled(true);
		// ��Ա���
		BillItem clItem = getBillItem("bd_psndoc.pk_psncl");
		UIRefPane psnclsref = (UIRefPane) clItem.getComponent();
		String wherepart = psnclsref.getRefModel().getWherePart();
		if (wherepart == null || wherepart.trim().length() == 0) {
			wherepart = " psnclscope = 5 ";
		} else if (wherepart.indexOf("psnclscope = 5") < 0) {
			String whereScope = " (psnclscope = 5) and (";
			wherepart = whereScope + wherepart + ")";
		}
		psnclsref.getRefModel().setWherePart(wherepart);
		// ���Ų���
		BillItem deptItem = getBillItem("bd_psndoc.pk_deptdoc");
		UIRefPane deptref = (UIRefPane) deptItem.getComponent();
		setWhereToModel(deptref.getRefModel(),
		"((canceled = 'N' and hrcanceled = 'N') or ( canceled is null and hrcanceled is null))");
		getBillItem("bd_psndoc.pk_psncl").setEdit(true);
		getBillItem("bd_psndoc.pk_psncl").setEnabled(true);
		getBillItem("bd_psndoc.pk_deptdoc").setEdit(true);
		getBillItem("bd_psndoc.pk_deptdoc").setEnabled(true);
		getBillItem("bd_psndoc.pk_om_job").setEdit(true);
		getBillItem("bd_psndoc.pk_om_job").setEnabled(true);
		getBillItem("bd_psndoc.jobrank").setEdit(true);
		getBillItem("bd_psndoc.jobrank").setEnabled(true);
		getBillItem("bd_psndoc.series").setEdit(true);
		getBillItem("bd_psndoc.series").setEnabled(true);
		getBillItem("bd_psndoc.dutyname").setEdit(true);
		getBillItem("bd_psndoc.dutyname").setEnabled(true);
		getBillItem("bd_psndoc.jobseries").setEdit(true);
		getBillItem("bd_psndoc.jobseries").setEnabled(true);

		//����ҵ���߼����Ƶÿɱ༭��
		getBillItem("bd_psndoc.pk_psncl").setEdit(true);
		getBillItem("bd_psndoc.pk_deptdoc").setEdit(true);
		getBillItem("bd_psndoc.pk_om_job").setEdit(true);

		// ��λ���У���λ�ȼ���ְ��
		UIRefPane job = (UIRefPane) (getBillItem("bd_psndoc.pk_om_job").getComponent());
		String pk_job = job.getRefPK();
		if (pk_job == null || pk_job.length() != 20) {

			getBillItem("bd_psndoc.jobrank").setEnabled(getBillItem("bd_psndoc.jobrank").isEdit());// true
			getBillItem("bd_psndoc.jobseries").setEdit(false);
			getBillItem("bd_psndoc.dutyname").setEnabled(getBillItem("bd_psndoc.dutyname").isEdit());// true
			getBillItem("bd_psndoc.series").setEnabled(getBillItem("bd_psndoc.series").isEdit());// true

			UIRefPane dutynameRef = (UIRefPane) (getBillItem("bd_psndoc.dutyname").getComponent());
			String pkduty = dutynameRef.getRefPK();
			if (nc.ui.hi.hi_301.Util.isDutyDependJobSeries) {
				if (pkduty != null && pkduty.trim().length() == 20) {
					getBillItem("bd_psndoc.jobseries").setEnabled(false);
					getBillItem("bd_psndoc.jobseries").setEdit(false);
				} else {
					getBillItem("bd_psndoc.jobseries").setEnabled(getBillItem("bd_psndoc.jobseries").isEdit());// true
					getBillItem("bd_psndoc.jobseries").setEdit(getBillItem("bd_psndoc.jobseries").isEdit());
				}
			}
		} else {
			getBillItem("bd_psndoc.jobrank").setEnabled(false);
			getBillItem("bd_psndoc.jobrank").setEdit(false);
			getBillItem("bd_psndoc.jobseries").setEdit(false);
			String pk_duty = (String) job.getRefValue("om_job.pk_om_duty");
			if (pk_duty != null && pk_duty.trim().length() == 20) {
				getBillItem("bd_psndoc.dutyname").setEnabled(false);
				getBillItem("bd_psndoc.series").setEnabled(false);
				getBillItem("bd_psndoc.dutyname").setEdit(false);
				getBillItem("bd_psndoc.series").setEdit(false);
			} else {
				getBillItem("bd_psndoc.dutyname").setEnabled(getBillItem("bd_psndoc.dutyname").isEdit());
				getBillItem("bd_psndoc.dutyname").setEdit(getBillItem("bd_psndoc.dutyname").isEdit());
				UIRefPane dutynameRef = (UIRefPane) (getBillItem("bd_psndoc.dutyname")
						.getComponent());
				String pkduty = dutynameRef.getRefPK();
				if (pkduty != null && pkduty.trim().length() == 20) {
					getBillItem("bd_psndoc.series").setEnabled(false);
					getBillItem("bd_psndoc.series").setEdit(false);
				} else {
					getBillItem("bd_psndoc.series").setEnabled(
							getBillItem("bd_psndoc.series").isEdit());// true
				}
			}
		}
		if (!curTempCorpPk.equalsIgnoreCase(Global.getCorpPK())|| isPartPsnCurCorp) {
			// wangkf fixed isPartPsnCurCorp ->!isPartPsnCurCorp
			getBillItem("bd_psndoc.jobrank").setEnabled(false);
			getBillItem("bd_psndoc.jobseries").setEnabled(false);
			getBillItem("bd_psndoc.dutyname").setEnabled(false);
			getBillItem("bd_psndoc.series").setEnabled(false);
			setBillItemEnabled("bd_psndoc.timecardid",false);
		}
		
		//fengwei 2010-0903 ������Ա���Ӻ��޸�ʱ�������Զ�������Ա༭ start
		BillItem[] items = getCard().getHeadShowItems("bd_psndoc");
		for (BillItem billItem : items) {
			String strKey = billItem.getKey(); 
			if(strKey.startsWith("groupdef")){
				getBillItem("bd_psndoc." + strKey).setEdit(true);
				getBillItem("bd_psndoc." + strKey).setEnabled(true);
			}
		}
		//fengwei 2010-0903 ������Ա���Ӻ��޸�ʱ�������Զ�������Ա༭ end

	}

	protected void setHeadItemEnable() {
		setHeadBillItemEditable("bd_psnbasdoc", true);
		setHeadBillItemEditable("bd_psndoc", true);
		getBillItem("bd_psndoc.psncode").setEdit(false);
		getBillItem("bd_psndoc.psncode").setEnabled(false);
		getBillItem("bd_psndoc.pk_psncl").setEdit(false);
		getBillItem("bd_psndoc.pk_psncl").setEnabled(false);
		getBillItem("bd_psndoc.pk_deptdoc").setEdit(false);
		getBillItem("bd_psndoc.pk_deptdoc").setEnabled(false);
		getBillItem("bd_psndoc.pk_om_job").setEdit(false);
		getBillItem("bd_psndoc.pk_om_job").setEnabled(false);
	}

	String oldpsncode = null;

	/**
	 * V35 ���� --��Ա���� �������밴ť��Ӧ�¼�
	 */
	public void onApplicate() throws java.lang.Exception {
		// ����ApplicatePsnRefTreeModel�࣬�õ�һ���ض���UIRefPane
		UIRefPane psndocref = new ApplicatePsnRefTreeModel().getRefPane(this);
		psndocref.getRef().showModal();
		String pk_psnbasdoc = (String) psndocref.getRefValue("bd_psnbasdoc.pk_psnbasdoc");
		app_pk_corp = (String) psndocref.getRefValue("bd_psnbasdoc.pk_corp");
		oldpsncode = (String) psndocref.getRefValue("bd_psndoc.psncode");
		String pk_psndoc = (String) psndocref.getRefValue("bd_psndoc.pk_psndoc");
		// ������Ա���նԻ���ѡ��ά����Ա
		// ����������ѯ����Ա����Ϣ���л�����Ƭ״̬ ������Ա��Ϣ״̬���ƣ������û��Ƿ�����
		if (pk_psnbasdoc != null) {
			getCard().resumeValue();
			setEmployeeRef(true);
			setEditState(true);
			setEditType(EDIT_REF);
			person = new PersonEAVO();
			person.setPk_psnbasdoc(pk_psnbasdoc);
			person.setPk_psndoc(pk_psndoc);
			// del by zhyan 2006-05-17 ������Աʱֻ�ܿ���������������Ϣ���ܼ�false����ture
			isLoadedBasicData = true;// wangkf add 20060515
			isLoadedAccData = true;// wangkf add 20060515
			loadPersonMain(pk_psnbasdoc, null, "bd_psnbasdoc");
			switchButtonGroup(EMPLOYEE_REF_CARD);
			setButtonsState(EMPLOYEE_REF_EDIT);
			setBillTempletState(true);
			setHeadItemEnableInRef();
			getBillItem("bd_psnbasdoc.psnname").setValue(person.getAccpsndocVO().getAttributeValue("psnname"));
			getBillItem("bd_psndoc.psncode").setValue(oldpsncode);
		}
		psndocref.getRefModel().clearData();
		// ������Ա��Ϣ״̬������Ϣ���浽��ͬ���У�ע���ڱ���֮�󣬰���Ա��Ϣ״̬�޸�Ϊ������״̬
	}

	/**
	 * ��Ա���õ��������밴ť�¼���v55����
	 * @throws Exception
	 */
	public void onBatchApplicate() throws Exception{

	}
	/**
	 * V35 ���� --��Ա���� ȷ�����ð�ť��Ӧ�¼�
	 */
	public void onAffirm() throws java.lang.Exception {
		getRefAffirmDlg().setCancelBtnVisible(true);
		getRefAffirmDlg().showModal();
	}

	/**
	 * V35 ���� ���������Ӽ���ť��Ӧ�¼�
	 */
	public void onBatchAddSet() throws java.lang.Exception {
		if(getPsnList().getHeadSelectedRows().length<1){
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("600710","UPT600710-000046")/*@res "���ڸ�ѡ���й�ѡҪ�����Ӽ�����Ա!"*/);
			return;
		}
		BatchAddSetDialog batchAddsetDlg = new BatchAddSetDialog(this,false,getModuleName(),true);
		batchAddsetDlg.setLocationRelativeTo(this);
		batchAddsetDlg.showModal();
	}

	/**
	 * V35 ���� ������ť��Ӧ�¼�
	 */
	public void onToFirst() throws java.lang.Exception {
		listSelectRow = 0;
		person = null;
		person = loadPsnInfo(listSelectRow);
		setButtonsState(CARD_MAIN_BROWSE);
	}

	/**
	 * V35 ���� ĩ����ť��Ӧ�¼�
	 */
	public void onToLast() throws java.lang.Exception {
		listSelectRow = psnList.length - 1;
		person = null;
		person = loadPsnInfo(listSelectRow);
		setButtonsState(CARD_MAIN_BROWSE);

	}

	/**
	 * V35 ���� ��һ����ť��Ӧ�¼�
	 */
	public void onUpRecord() throws java.lang.Exception {
		listSelectRow -= 1;
		person = null;
		person = loadPsnInfo(listSelectRow);
		setButtonsState(CARD_MAIN_BROWSE);
	}

	/**
	 * V35 ���� ��һ����ť��Ӧ�¼�
	 */
	public void onDownRecord() throws java.lang.Exception {
		listSelectRow += 1;
		person = null;
		person = loadPsnInfo(listSelectRow);
		setButtonsState(CARD_MAIN_BROWSE);
	}
	/**
	 * 
	 * @param itemVOs
	 * @return
	 */
	protected Pair[] convertToPairs(GeneralVO[] itemVOs) {
		Vector vv = new Vector();
		Pair[] pairs = null;
		boolean includeshoworder = false;
		if (itemVOs != null && itemVOs.length > 0) {
			Pair unitpair = new Pair("unitname",nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"upt600704-000047")/* @res "��˾����" */, 0, "","bd_corp", true);
			vv.addElement(unitpair);

			for (int i = 0; i < itemVOs.length; i++) 
			{
				String tablecode = (String) itemVOs[i].getAttributeValue("setcode");
				String code = (String) itemVOs[i].getAttributeValue("fldcode");
				if ("showorder".equals(code)){
					includeshoworder = true;
					if (getModuleName().equals("600710")) tablecode="hi_psndoc_keypsn";
				}
				String name = (String) itemVOs[i].getAttributeValue("fldname");
				Integer datatype = (Integer) itemVOs[i].getAttributeValue("datatype");
				int intDatatype = (datatype == null ? 0 : datatype.intValue());
				String reftype = (String) itemVOs[i].getAttributeValue("fldreftype");
				Pair pair = new Pair(code, name, intDatatype, reftype,tablecode, true);
				vv.addElement(pair);
			}
			if(intodocdirect ==1 && getModuleName().equalsIgnoreCase("600704")){

				Pair approveflagnamepair = new Pair("approveflagname",nc.ui.ml.NCLangRes.getInstance().getStrByID("600700", 
						"UPP600700-000360")/* @res "��Ա״̬" */, 0, "","bd_psnbasdoc", true);
				vv.addElement(approveflagnamepair);
			}

			//Ϊ����keypsngroupname������groupname��
			if (getModuleName().equals("600710")) {
				Pair gourppair = new Pair("keypsngroupname",nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"600710", "UPP600710-000026")/* @res"��Ա��"*/, 0, "","hi_keypsn_group", true);
				vv.addElement(gourppair);

				if (!includeshoworder){
					Pair showorderpair = new Pair("showorder",nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
					"UPT600704-000271")/*"���"*/ , BillItem.INTEGER, "","hi_psndoc_keypsn", false);
					vv.addElement(showorderpair);
				}
			} else {

				if (getModuleName().equalsIgnoreCase("600707") || getModuleName().equalsIgnoreCase("600708")) {
					Pair jobtypepair = new Pair("jobtypename",nc.ui.ml.NCLangRes.getInstance().getStrByID("600704","UPP600704-000065")/* @res "��ְ����" */, 0, "",
							"hi_psndoc_deptchg", true);
					vv.addElement(jobtypepair);
				}
				if (!includeshoworder){
					Pair showorderpair = new Pair("showorder",nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
					"UPT600704-000271")/*"���"*/ , BillItem.INTEGER, "","bd_psndoc", false);
					vv.addElement(showorderpair);
				}
			}

			if (vv.size() > 0) {
				pairs = new Pair[vv.size()];
				vv.copyInto(pairs);
			}
		}
		return pairs;
	}

	boolean itemchange = false;

	/**
	 * V35 ����
	 * 
	 * @throws java.lang.Exception
	 */
	public void onListItemSet() throws java.lang.Exception {
		// ����Pair
		if (getListItemSetDlg().showModal() != UIDialog.ID_OK) {
			return;
		}
		if (isItemchange()) {
			GeneralVO[] itemVOs = getSelectItemVos();
			Pair[] pairs = convertToPairs(itemVOs);
			if (pairs != null && pairs.length > 0) {
				setListItems(pairs);
			} else {
				setListItems(getListItemsDefault());
			}
			// ˢ�����ı�ͷ
			// �������ñ�ͷ���ݣ�ע���޸Ĳ�ѯ���ݵķ���
			initPsnList();
			deptTreeValueChanged(null);
		}
	}

	/**
	 * V35 ���������б�
	 * @throws java.lang.Exception
	 */
	public void onList() throws java.lang.Exception {
		if (isUpdated) {// ����༭������ˢ��psnList wangkf add
			if (person != null) {
				String curpk = person.getPk_psndoc();
				// onRefresh();
				// ������Ա����
				ConditionVO cond = new ConditionVO();
				cond.setFieldCode("bd_psndoc.pk_psndoc");
				cond.setOperaCode("=");
				cond.setValue(curpk);

				// ���¸���Ա��Ϣ
				GeneralVO[] gvos = HIDelegator.getPsnInf().queryByCondition(
						Global.getCorpPK(), new ConditionVO[] { cond },
						powerSql, getListField(), wheresql);
				if (gvos != null && gvos.length > 0) {
					listSelectRow = findPersonPos(curpk, "pk_psndoc", psnList);//getPsnList().getTable().getSelectedRow();
					if (listSelectRow >= 0
							&& listSelectRow < queryResult.length) {
						queryResult[listSelectRow] = gvos[0];
						getPsnList().setBodyData(queryResult);
						getPsnList().getTableModel().execLoadFormula();
					}
				}
			}
		}

		isUpdated = false;
		setEditState(false);
		setEmployeeRef(false);
		switchButtonGroup(LIST_GROUP);
		setButtonGroup(LIST_GROUP);
		setButtonsState(LIST_BROWSE);
		int selectRow = listSelectRow;
		if (listSelectRow == -1) {
			selectRow = getPsnList().getTable().getRowCount() - 1;// ��λ�����һ��
			listSelectRow = selectRow;
		}
		if (selectRow >= 0) {
			getPsnList().getTable().addRowSelectionInterval(selectRow,
					selectRow);
			getPsnList().getTable().addColumnSelectionInterval(0, 0);
			getPsnList().repaint();
		}
		isShowBillTemp = false;
	}

	/**
	 * ��ѡ��ȡ����ť����Ӧ�¼���
	 * @throws java.lang.Exception
	 */
	protected void onCancelOp() throws java.lang.Exception {
		
		boolean isFromREF = false;
		if (getEditType() == EDIT_SORT) {
			cancelSort();
		} else if (getEditType() == EDIT_MAIN) {
			try {
				if (isAdding()) {
					// �ָ�Ψһ�ֶεı༭״̬
					BillItem[] items = checkAddDlg.getAllBillItems();
					for (int i = 0; i < items.length; i++) {
						BillItem item = getCard()
						.getHeadItem(items[i].getKey());
						Object obj = htbEdit.get(items[i].getKey());
						if (obj != null) {
							item.setEdit(((Boolean) obj).booleanValue());
						} else {
							item.setEdit(false);
						}
					}

				}
				//�����������Ա����Ҫ�ָ���ͷ�ı༭״̬ 
				//added on 2009-12-18 for ������Ա�����ù�˾��Ӧ���ܹ��޸ĸ�����Ϣ by fengwei
				if(isRefPerson){
					resumeEditState();
				}
				cancelEditMain();// �����ǰ���ڱ༭����Ϣ��
				if (!isAdding()) { // �޸�ȡ������
					freeLockPsn(person.getPk_psndoc());
				} else if (paramIsValid()) {
					if(codeflag){
						resetBillcode();
					}
					codeflag = true;
				}
				if(isAdding()&&listSelectRow>=0){					
					loadPsnInfo(listSelectRow);
				}
			} catch (Exception e) {
			} finally {
				if (!isAdding()) {
					freeLockPsn(person.getPk_psndoc());
				}
			}
			setButtonsState(CARD_MAIN_BROWSE);
			setAdding(false);
		} else if (getEditType() == EDIT_REF) {
			// �ָ���ͷ�ֶεı༭״̬
			//added on 2009-12-18 for ������Ա�����ù�˾��Ӧ���ܹ��޸ĸ�����Ϣ by fengwei
			resumeEditState();
			
			getCard().resumeValue();// ������ձ༭����
			isFromREF = true;
			setBillTempletState(false);
			if (isEditing() && paramIsValid()) {
				if(codeflag){
					resetBillcode();
				}
				codeflag = true;
			}
			setButtonsState(EMPLOYEE_REF_CARD_BROWSE);
			setEditState(false);
			setEmployeeRef(false);
		}else if(getEditType() == EDIT_RETURN){
			// �ָ�Ψһ�ֶεı༭״̬
			BillItem[] items = checkAddDlg.getAllBillItems();
			for (int i = 0; i < items.length; i++) {
				BillItem item = getCard()
				.getHeadItem(items[i].getKey());
				Object obj = htbEdit.get(items[i].getKey());
				if (obj != null) {
					item.setEdit(((Boolean) obj).booleanValue());
				} else {
					item.setEdit(false);
				}
			}
			cancelEditMain();// �����ǰ���ڱ༭����Ϣ��
			if (paramIsValid()) {
				if(codeflag){
					resetBillcode();
				}
				codeflag = true;
			}
			setButtonsState(CARD_MAIN_BROWSE);
			setAdding(false);
			isrehire = false;
			setBillTempletState(false);
			setEditState(false);
		}
		else {
			try {
				cancelEditSub();// �����ǰ���ڱ༭�����ӱ�
				freeLockPsn(person.getPk_psndoc() + getBodyTableCode());// �ӱ�༭����
			} catch (Exception e) {
			} finally {
				freeLockPsn(person.getPk_psndoc() + getBodyTableCode());
				vDelPkPsndocSub.clear();
				// 
				vDelSubVOs.clear();
			}
			setButtonsState(CARD_MAIN_BROWSE);
			setButtonsState(CARD_CHILD_BROWSE);
			// ����
			subBackupVOs = null;
		}

		getCard().getBodyTabbedPane().setEnabled(true);
		if(listSelectRow>=0&&!isFromREF){
			loadPsnInfo(listSelectRow);
		}
		// ����ְԱ����ؼ����ɱ༭
		getBillItem("bd_psndoc.clerkcode").setEdit(false);
		//
	}
	
	/**
     * �ָ���ͷ�ֶεı༭״̬
     * @author fengwei on 2009-12-18
     * @return
     */
    private void resumeEditState() {
    	// �ָ���ͷ�ֶεı༭״̬
    	resumeHeadBillItemEditable("bd_psnbasdoc");
    	resumeHeadBillItemEditable("bd_psndoc");
    }        

    /**
     * �ָ��ֶεı༭״̬
     * @author fengwei on 2009-12-18
     * @return
     */
    protected void resumeHeadBillItemEditable(String table) {
    	BillItem[] items = getCard().getHeadShowItems(table);
    	if (items == null || items.length < 1)
            return;

    	// �����Ƿ���Ա༭
    	for (int i = 0; i < items.length; i++) {
            BillItem item = getCard().getHeadItem(items[i].getKey());
            Object obj = htbEdit.get(items[i].getKey());
            if (obj != null) {
            	item.setEdit(((Boolean) obj).booleanValue());
            } else {
                item.setEdit(false);
            }
    	}        
    }

	/**
	 * ���ݲ����Ƿ���ȷ
	 * @return
	 */
	private boolean isPsncodeAutoGenerate() {
		return getPsncodeAutoGenerateParam().equalsIgnoreCase(PSNCODE_AUTO_GENERATE);
	}
	/**
	 * ���ݺ��Ƿ��Ѿ�����䲢�Ҳ�Ϊ��
	 * @return
	 */
	private boolean autopsnbillcodeIsValid() {
		return (autopsncode != null && autopsncode.trim().length() > 0);
	}

	public void tableChanged2(TableModelEvent e) {
		// 
		if (getEditType() == EDIT_SORT) {
			isBeginEdit = true;
			int row = e.getFirstRow();
			GeneralVO[] sortPsnList = (GeneralVO[]) getPsnList()
			.getTableModel().getBodyValueVOs(
					"nc.vo.hi.hi_301.GeneralVO");
			GeneralVO[] oldPsnList = (GeneralVO[]) getPsnList().getBodyData();
			if (sortPsnList == null || sortPsnList.length < 1) {
				sortPsnList = oldPsnList;
			}
			Integer currentshoworder = ((Integer) sortPsnList[row]
			                                                  .getAttributeValue("showorder"));// .intValue();
			String pk_psndoc = (String) oldPsnList[row]
			                                       .getAttributeValue("pk_psndoc");
			for (int i = 0; i < oldPsnList.length; i++) {
				if (oldPsnList[i].getAttributeValue("pk_psndoc").equals(
						pk_psndoc)
						&& i != row) {
					getPsnList().getTableModel().setValueAt(currentshoworder,
							i, "showorder");
				}
			}

			isBeginEdit = false;
		}

	}

	public void tableChanged(TableModelEvent e) {
		if(!isBeginEdit)
			tableChanged2(e);

	}
	/**
	 * �����Ƿ���ȷ�����ڻ��˵��ݺ�֮ǰ�������ж�
	 * 
	 * @return
	 */
	private boolean paramIsValid() {
		return (isPsncodeAutoGenerate() && autopsnbillcodeIsValid());
	}
	/**
	 * �ع�billcode������autopsnbillcode
	 * @throws Exception
	 */
	private void resetBillcode() throws Exception {
		// ����Ǽ�����Ψһ���򰴼��Ų�����Ա����
		String pkcorp = ("Y".equalsIgnoreCase(isUniquePsncodeInGroup)) ? "0001" : Global.getCorpPK();
		HIDelegator.getBillcodeRule().returnBillCodeOnDelete(pkcorp,"BS", autopsncode, getObjVO());
		autopsncode = null;
	}

	/**
	 * ��Ƭ��ť��Ӧ�¼���������
	 * @throws java.lang.Exception
	 */
	protected void onCard() throws java.lang.Exception {
		// ��ȡ��ǰ��Աpk
		String[] keys = getTempPsnPk();
		String pk_psndoc = keys[1];
		String pk_psnbasdoc = "";
		String pk_psndoc_sub = "";
		int jobtype = 0;
		if (pk_psndoc != null) {
			// ��ʾ��Ƭ
			GeneralVO vo = null;
			int row = getPsnList().getTable().getSelectedRow();
//			vo = psnList[row];// queryResult
//			pk_psnbasdoc = (String) vo.getAttributeValue("pk_psnbasdoc");
//			pk_psndoc_sub = (String) vo.getAttributeValue("pk_psndoc_sub");
//			Object o = vo.getAttributeValue("jobtypeflag");
//			if (o != null) {
//				jobtype = ((Integer) o).intValue();
//			}
			CardReportViewFrame dlg = new CardReportViewFrame(getTopFrame(this));
			dlg.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			dlg.resetDlg(getClientEnvironment().getUser().getPrimaryKey(),corpPK, psnList,row);
			
			int screenWidth = (int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().getWidth();
			int screenHeight = (int)java.awt.Toolkit.getDefaultToolkit().getScreenSize().getHeight();
			if (screenWidth<=1024){
				dlg.setSize(screenWidth,screenHeight);
			}else{
				dlg.setSize(1024,768);
			}
			
			int x = (int) (screenWidth - dlg.getWidth()) / 2;
			int y = (int) (screenHeight - dlg.getHeight()) / 2;
			if (x < 0)x = 0;
			if (y < 0)y = 0;
			dlg.setLocation(x, y);
			
			dlg.showModal();
		}
	}

	/**
	 * ��õ�ǰ���������ڣ���Ҫ���ڶԻ���ĸ����ڡ�
	 * �������ڣ�(2004-5-18 11:55:22)
	 * @return java.awt.Frame
	 */
	public static Frame getTopFrame(Component component) {

		//�𼶻�ȡ����ĸ������ֱ��������
		Container parent = component.getParent();
		while (!(parent == null || parent instanceof Frame))
			parent = parent.getParent();

		return (Frame) parent;
	}

	/**
	 * ���Ǹ����onClosing���������ݱ༭���ڵĹرգ�ֻҪ�漰���Ƿ񱣴��˳� ������ʾ
	 */
	public boolean onClosing() {
		if (bnSave.isEnabled()) {// 
			String title = nc.ui.ml.NCLangRes.getInstance().getStrByID("600704", "UPP600704-000020")/* @res "��ʾ" */;
			String question = nc.ui.ml.NCLangRes.getInstance().getStrByID("600704", "UPT600704-000244")/* @res "�Ƿ񱣴�" */;
			int operatestate = MessageDialog.showYesNoCancelDlg(this, title,question);
			if (operatestate == MessageDialog.ID_YES|| operatestate == MessageDialog.ID_OK) {
				try {
					onSave();
					return true;
				} catch (Exception ex) {
					reportException(ex);
					showErrorMessage(ex.getMessage());
					return false;
				}
			} else if (operatestate == MessageDialog.ID_NO|| operatestate == MessageDialog.ID_NOTOALL) {
				try {
					if (getEditType() == EDIT_MAIN) {
						// ���������Ч���ع�billcode������autopsnbillcode
						if (paramIsValid()) {
							if(codeflag){
								resetBillcode();
							}
							codeflag = true;
						}
					} else if (getEditType() == EDIT_SUB) {
					} else if (getEditType() == EDIT_SORT) {
						return true;
					}
				} catch (Exception e) {
					reportException(e);
					showErrorMessage(e.getMessage());
				}
				if (person != null) {
					try {
						// �ͷ���Ա����
						String pk_psndoc = null;
						if (getEditType() == EDIT_MAIN) {
							pk_psndoc = person.getPk_psndoc();
						} else if (getEditType() == EDIT_SUB) {
							pk_psndoc = person.getPk_psndoc()+ getBodyTableCode();
						}
						else if (getEditType() == EDIT_REF) {
							return true;
						}
						freeLockPsn(pk_psndoc);
					} catch (Exception e) {
						reportException(e);
						showErrorMessage(e.getMessage());
						return true;
					}
				}
				return true;
			} else {
				return false;
			}
		} else {
			return true;
		}
	}
	/**
	 * ɾ����ťѡ��ʱ��Ӧ�¼���ά���ڵ�����д�� 
	 * @throws java.lang.Exception
	 */
	protected void onDel() throws java.lang.Exception {
		String pkpsndoc = "";
		try {
			if (listSelectRow < 0) { 
				return;
			}
			int oriselectrow = listSelectRow;//��¼ɾ��ǰ����Ա���б��е�λ��
			// ȷ��ɾ��
			GeneralVO psn = psnList[listSelectRow];
			pkpsndoc = (String) psn.getAttributeValue("pk_psndoc");
			String pk_psnbasdoc = (String) psn.getAttributeValue("pk_psnbasdoc");
			String isreturn = (String) psn.getAttributeValue("isreturn");

			String sql1 = " select 1 from bd_psndoc where psnclscope in(2) and indocflag = 'Y'and pk_psnbasdoc ='"+pk_psnbasdoc+"'";// and pk_corp ='"+Global.getCorpPK()+"'
			String sql2 = " select 1 from hi_docapply_b where pk_psndoc ='"+pkpsndoc+"'";
			boolean[] isexists = HIDelegator.getPsnInf().isRecordExists(new String[]{sql1,sql2});
			
			boolean existrehire =isexists[0];//��Ƹ��Ա
			boolean existdoc =isexists[1];
			if (existdoc) {
//				showWarningMessage("����Ա������ְ���뵥�У�����ɾ����");
				showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("600700", 
						"UPP600700-000117")/* @res "����Ա������ְ���뵥�У�����ɾ����" */);
				return;
			}
			if (!lockPsn(pkpsndoc)) {
				showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"600704", "UPP600704-000073")/*@res* "����Ա���ڱ������û����������Ժ����ԣ�"*/);
				return;
			}
			int useroperate = MessageDialog.showYesNoDlg(this,nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPP600704-000020")/* @res "��ʾ" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704","UPP600704-000074")/* @res "�Ƿ�ɾ��" */
			+ psn.getAttributeValue("psnname")+nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"600704", "UPP600704-000075")/* @res "��"*/);
			if (useroperate == MessageDialog.ID_OK|| useroperate == MessageDialog.ID_YES) {
				// ��ȡҪɾ������Ա����ǰ����Ա����ǰ��˾
				String pk_corp = Global.getCorpPK();
				// �Ƚ�ɾ����־��Ϊ1
				HIDelegator.getPsnInf().updateDeleteFlag(pkpsndoc, true);
				if ("Y".equalsIgnoreCase(isreturn)){//����Ƿ�Ƹ��Ա������Ƹ��Ա��ֻɾ��������Ϣ
					HIDelegator.getPsnInf().deletePersonRehire(psn, pk_corp,0);	
				}else if(existrehire){
					HIDelegator.getPsnInf().deletePersonRehire(psn, pk_corp,1);	
				}else{
					// ɾ��
					HIDelegator.getPsnInf().deletePersonnotinDoc(psn, pk_corp);
				}
				// ɾ����¼�ĸ�����Ϣ
				IAttachment attachment = (IAttachment) NCLocator.getInstance()
				.lookup(IAttachment.class.getName());
				attachment.deleteAttachment((String) psn
						.getFieldValue("pk_psndoc"), "bd_psndoc");

				//����
				String pkcorp = ("Y".equalsIgnoreCase(isUniquePsncodeInGroup)) ? "0001" : Global.getCorpPK();
				HIDelegator.getBillcodeRule().returnBillCodeOnDelete(pkcorp,"BS", (String) psn.getAttributeValue("psncode"), getObjVO());

				// �Ӳ�ѯ�����ɾ������
				queryResult = deletePsnFromArray(queryResult, pkpsndoc);
				// �ӵ�ǰ��ʾ�����ɾ������
				psnList = deletePsnFromArray(psnList, pkpsndoc);
				// ˢ�µ�ǰ��Ա�б�
				getPsnList().setBodyData(psnList);//�˲�����listSelectRow��Ϊ-1
				getPsnList().getTableModel().execLoadFormula();
				// ˢ�µ�ǰ�����й��ڸò��ŵ���Ա��Ϣ,Fixed Bug
				psnDeptCache.clear(); // put(getSelectedDept(),psnList);
				psnCorpCache.clear();// wangkf add

				freeLockPsn(pkpsndoc);
				if (psnList != null && psnList.length > 0) {
					listSelectRow = oriselectrow;//�ָ�ɾ��ǰ����Ա���б��е�λ��
				}else{
					listSelectRow = -1;
				}
				if (getButtonGroup() == CARD_GROUP) {
					if (psnList != null && psnList.length > 0) {
						if (listSelectRow == 0) {
							listSelectRow--;
							onDownRecord();
						} else if (listSelectRow > 0) {
							onUpRecord();
						}
					} else {
						listSelectRow = -1;
						getCard().resumeValue();
						bnChildEdit.setEnabled(false);
						bnEdit.setEnabled(false);
						bnDel.setEnabled(false);
						updateButtons();
					}
				} else {
					onRefresh();
				}
			} else {
				freeLockPsn(pkpsndoc);
				return;
			}
		} catch (Exception e) {
			reportException(e);
			showErrorMessage(e.getMessage());
			// ���ɾ��ʧ��,�򽫱�־��Ϊ0
			HIDelegator.getPsnInf().updateDeleteFlag(pkpsndoc, false);
			freeLockPsn(pkpsndoc);
		}
	}

	/**
	 * (����ɾ��)ɾ����ťѡ��ʱ��Ӧ�¼���ά���ڵ�����д�� 
	 * @throws java.lang.Exception
	 */
	protected void onDelmul() throws java.lang.Exception {
		int[] rows = getPsnList().getHeadSelectedRows();
		if(rows.length<1){
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("600710","UPT600710-000051")/*@res "��ѡ��Ҫɾ������Ա!"*/);
		}
		int oriselectrow = rows[0];//��¼ɾ��λ��
		GeneralVO[] selectPsns = new GeneralVO[rows.length];
		String[] pkpsndocs=new String[rows.length];
		String[] psnnames=new String[rows.length];
		String[] isreturns=new String[rows.length];
		boolean[] existrehires = new boolean[rows.length];
		try {
			for( int i=0;i<rows.length;i++){
				// ȷ��ɾ��
				GeneralVO psn = psnList[rows[i]];
				String pkpsndoc = (String) psn.getAttributeValue("pk_psndoc");
				String pk_psnbasdoc = (String) psn.getAttributeValue("pk_psnbasdoc");
				String psnname = (String)psn.getAttributeValue("psnname");
				String isreturn = (String) psn.getAttributeValue("isreturn");
				String pk_corp = (String) psn.getAttributeValue("pk_corp");
				selectPsns[i] = psn;
				pkpsndocs[i] =pkpsndoc;
				psnnames[i]=psnname;
				isreturns[i]=isreturn;
				if(!Global.getCorpPK().equals(pk_corp) ){
					freeLockPsns(pkpsndocs);
//					showWarningMessage(psnname+":"+"���ǵ�ǰ��˾��Ա������ɾ����");
					showWarningMessage(psnname + nc.ui.ml.NCLangRes.getInstance().getStrByID("600700", 
							"UPP600700-000359")/* @res "���ǵ�ǰ��˾��Ա������ɾ����"*/);
					return;
				}
				String sql1 = " select 1 from bd_psndoc where psnclscope in(2) and indocflag = 'Y'and pk_psnbasdoc ='"+pk_psnbasdoc+"'";// and pk_corp ='"+Global.getCorpPK()+"'
				boolean existrehire = HIDelegator.getPsnInf().isRecordExist(sql1);//��Ƹ��Ա
				existrehires[i] = existrehire;
				String sql2 = " select 1 from hi_docapply_b where pk_psndoc ='"+pkpsndoc+"'";
				boolean existdoc = HIDelegator.getPsnInf().isRecordExist(sql2);
				if (existdoc) {
					freeLockPsns(pkpsndocs);
//					showWarningMessage(psnname+":"+"����Ա������ְ���뵥�У�����ɾ����");
					showWarningMessage(psnname + nc.ui.ml.NCLangRes.getInstance().getStrByID("600700", 
							"UPP600700-000117")/* @res "����Ա������ְ���뵥�У�����ɾ����"*/);
					return;
				}
				if (!lockPsn(pkpsndoc)) {
					freeLockPsns(pkpsndocs);
					showWarningMessage(psnname+":"+nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"600704", "UPP600704-000073")/*@res* "����Ա���ڱ������û����������Ժ����ԣ�"*/);
					return;
				}
			}
			int useroperate = MessageDialog.showYesNoDlg(this,nc.ui.ml.NCLangRes.getInstance().getStrByID("600704","UPP600704-000020")/* @res "��ʾ" */,nc.ui.ml.NCLangRes.getInstance().getStrByID("600704","UPP600704-000074")/* @res "�Ƿ�ɾ��" */
					+nc.ui.ml.NCLangRes.getInstance().getStrByID("600704", "UPP600704-000075")/* @res "��"*/);
			if (useroperate != MessageDialog.ID_OK&& useroperate != MessageDialog.ID_YES) {
				freeLockPsns(pkpsndocs);
				return;
			}
		} catch (Exception e) {
			freeLockPsns(pkpsndocs);
			reportException(e);
			showErrorMessage(e.getMessage());
			return;
		}


		String pk_corp = Global.getCorpPK();
		for(int i=0;i<pkpsndocs.length;i++){
			try{
				// �Ƚ�ɾ����־��Ϊ1
				HIDelegator.getPsnInf().updateDeleteFlag(pkpsndocs[i], true);
				if ("Y".equalsIgnoreCase(isreturns[i])){//����Ƿ�Ƹ��Ա������Ƹ��Ա��ֻɾ��������Ϣ
					HIDelegator.getPsnInf().deletePersonRehire(selectPsns[i], pk_corp,0);	
				}else if(existrehires[i]){
					HIDelegator.getPsnInf().deletePersonRehire(selectPsns[i], pk_corp,1);	
				}else{
					// ɾ��
					HIDelegator.getPsnInf().deletePersonnotinDoc(selectPsns[i], pk_corp);
				}
				// ɾ����¼�ĸ�����Ϣ
				IAttachment attachment = (IAttachment) NCLocator.getInstance().lookup(IAttachment.class.getName());
				attachment.deleteAttachment((String) selectPsns[i]
				                                                .getFieldValue("pk_psndoc"), "bd_psndoc");

				//����
				String pkcorp = ("Y".equalsIgnoreCase(isUniquePsncodeInGroup)) ? "0001" : Global.getCorpPK();
				HIDelegator.getBillcodeRule().returnBillCodeOnDelete(pkcorp,"BS", (String) selectPsns[i].getAttributeValue("psncode"), getObjVO());
			}catch(Exception e){
				freeLockPsn(pkpsndocs[i]);
				reportException(e);
				showErrorMessage(e.getMessage());
				// ���ɾ��ʧ��,�򽫱�־��Ϊ0
				HIDelegator.getPsnInf().updateDeleteFlag(pkpsndocs[i], false);
				return;
			}
			// �Ӳ�ѯ�����ɾ������
			queryResult = deletePsnFromArray(queryResult, pkpsndocs[i]);
			// �ӵ�ǰ��ʾ�����ɾ������
			psnList = deletePsnFromArray(psnList, pkpsndocs[i]);
			freeLockPsn(pkpsndocs[i]);
		}
		// ˢ�µ�ǰ��Ա�б�
		getPsnList().setBodyData(psnList);//�˲�����listSelectRow��Ϊ-1
		getPsnList().getTableModel().execLoadFormula();
		// ˢ�µ�ǰ�����й��ڸò��ŵ���Ա��Ϣ,Fixed Bug
		psnDeptCache.clear(); // put(getSelectedDept(),psnList);
		psnCorpCache.clear();// wangkf add


		if (psnList != null && psnList.length > 0) {
			listSelectRow = oriselectrow;//�ָ�ɾ��ǰ����Ա���б��е�λ��
		}else{
			listSelectRow = -1;
		}
		onRefresh();
	}

	private void freeLockPsns(String[] pkpsndocs) throws Exception {
		for(String pkpsndoc:pkpsndocs){
			if(pkpsndoc!=null)	freeLockPsn(pkpsndoc);
		}
	}
	/**
	 * ɾ���в�����Ӧ�¼�������
	 * @throws java.lang.Exception
	 */
	protected void onDelChild() throws java.lang.Exception {
		try {
			if("600707".equalsIgnoreCase(getModuleName())){
				String sql = " select 1 from hi_psndoc_keypsn where pk_psndoc ='"+person.getPk_psndoc()+"'and enddate is null";
				boolean iskeypsn =HIDelegator.getPsnInf().isRecordExist(sql);
				if(iskeypsn && !caneditkeypsn){
					showWarningMessage(  nc.ui.ml.NCLangRes.getInstance().getStrByID("600707","UPT600707-000239")/* @res "����ԱΪ�ؼ���Ա,�뵽�ؼ���Ա����ڵ�ά������Ϣ��"*/);
					return;
				}
			}
			//V56���ӣ�У���Ƿ����δ��˵ģ���Ա�䶯�����ݡ�
			HIDelegator.getPsnInf().checkIfExistsUnAuditBillofPSN(new String[]{person.getPk_psndoc()},getBodyTableCode());

			if (!isEditSub()) {
				if (!lockPsn(person.getPk_psndoc() + getBodyTableCode())) {

					showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"600704", "UPP600704-000070")/*
							 * @res "����Ա��"
							 */
							+ getBodyTableName()
							+ nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
							"UPP600704-000071")/*
							 * @res "�Ӽ������������û����������Ժ����ԣ�"
							 */);
					return;
				}
			}
			
			// ���ѡ���е���Ч��
			int row = getCard().getBillTable().getSelectedRow();
			if (row == -1 || getCard().getBillTable().getRowCount() == 0) {
				freeLockPsn(person.getPk_psndoc() + getBodyTableCode());
				return;
			}
			// ȷ��ɾ��
			int user_operate = MessageDialog.showYesNoDlg(this,nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPP600704-000020")/* @res "��ʾ" */,
			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704","UPP600704-000077")/* @res "�Ƿ�Ҫɾ�������¼��" */);
			if (user_operate == MessageDialog.ID_OK
					|| user_operate == MessageDialog.ID_YES) {
				// ���ӱ��¼��ɾ������
				SubTable subtable = person.getSubTable(getBodyTableCode());
				GeneralVO vo = (GeneralVO) subtable.getRecords().elementAt(
						row);
				subtable.getRecords().removeElementAt(row);
				if (isNotMultiEdit()) {
					// �����ݿ���ɾ������
					HIDelegator.getPsnInf().deleteChild(getBodyTableCode(),
							person.getPk_psndoc(), vo);
					// ͬ��������Ϣ---
					boolean updated = isUpdateAccRel(vo.getAttributeNames(), getBodyTableCode());// wangkf
					// add
					if (updated) {
						GeneralVO voTemp = new GeneralVO();
						voTemp.setAttributeValue("pk_psnbasdoc", person
								.getPk_psnbasdoc());
						voTemp.setAttributeValue("pk_psndoc", person
								.getPk_psndoc());
						GeneralVO relVO = HIDelegator.getPsnInf().updateAccRel(
								vo.getAttributeNames(), getRelationMap(),
								getBodyTableCode(), voTemp);
						// V35 modify
						HashMap hmRelVO = new HashMap();
						String[] fields = relVO.getAttributeNames();
						GeneralVO psnvo = new GeneralVO();
						GeneralVO baspsnvo = new GeneralVO();
						String mainpk = person.getPk_psndoc();
						psnvo.setAttributeValue("pk_psnbasdoc", person
								.getPk_psnbasdoc());
						psnvo.setAttributeValue("pk_psndoc", person
								.getPk_psndoc());
						baspsnvo.setAttributeValue("pk_psnbasdoc", person
								.getPk_psnbasdoc());
						baspsnvo.setAttributeValue("pk_psndoc", person
								.getPk_psndoc());
						boolean isRelPsndoc = false;
						boolean isRelBasPsndoc = false;
						for (int i = 0; i < fields.length; i++) {
							if (fields[i].indexOf(".") < 0) {
								continue;
							}
							String table = fields[i].substring(0, fields[i]
							                                             .indexOf("."));
							if ("bd_psndoc".equalsIgnoreCase(table)) {
								String field = fields[i].substring(fields[i]
								                                          .indexOf(".") + 1);
								psnvo.setAttributeValue(field, relVO
										.getAttributeValue(fields[i]));
								person.getPsndocVO().setAttributeValue(field,
										relVO.getAttributeValue(fields[i]));
								isRelPsndoc = true;
							} else if ("bd_psnbasdoc".equalsIgnoreCase(table)) {
								String field = fields[i].substring(fields[i]
								                                          .indexOf(".") + 1);
								baspsnvo.setAttributeValue(field, relVO
										.getAttributeValue(fields[i]));
								person.getAccpsndocVO().setAttributeValue(
										field,
										relVO.getAttributeValue(fields[i]));
								isRelBasPsndoc = true;
							}

						}
						if (isRelPsndoc)
							hmRelVO.put("bd_psndoc", psnvo);
						if (isRelBasPsndoc)
							hmRelVO.put("bd_psnbasdoc", baspsnvo);
						HIDelegator.getPsnInf().updateRelTable(hmRelVO);
						// ���½�������
						if (isRelPsndoc) {
							String[] field = psnvo.getAttributeNames();
							for (int i = 0; i < field.length; i++) {
								if ("pk_psnbasdoc".equals(field[i])
										|| "pk_psndoc".equals(field[i])) {
									continue;
								}
								BillItem item = getCard().getHeadItem(field[i]);
								if (item != null) {
									Object value = psnvo
									.getFieldValue(field[i]);
									if (value == null
											|| "#null#"
											.equals(value.toString())) {
										item.setValue(null);
										value = null;
									} else {
										item.setValue(value);
									}
									psnList[listSelectRow].setAttributeValue(
											field[i], value);
								}
							}
						}
						if (isRelBasPsndoc) {
							String[] field = baspsnvo.getAttributeNames();
							for (int i = 0; i < field.length; i++) {
								if ("pk_psnbasdoc".equals(field[i])
										|| "pk_psndoc".equals(field[i])) {
									continue;
								}
								BillItem item = getCard().getHeadItem(field[i]);
								if (item != null) {
									Object value = baspsnvo
									.getFieldValue(field[i]);
									if (value == null
											|| "#null#"
											.equals(value.toString())) {
										item.setValue(null);
										value = null;
									} else {
										item.setValue(value);
									}
									psnList[listSelectRow].setAttributeValue(
											field[i], value);
								}
							}
						}
					}
					isRelateToAcc = updated;
					// ���ð�ť״̬
					setChildButtonState();
					updateButtons();	
					setEditLineNo(-1);
					// ɾ��ѡ����
					getCard().delLine();
				} else {
					if (!isEditSub()) {
						setEditSub(true);	
						// ���õ���ģ�����ҳǩ�����л�
						getCard().getBodyTabbedPane().setEnabled(false);
						getCard().getBodyPanel().getTableModel().updateValue();
					}
					String pk_psndoc_sub = (String) vo
					.getAttributeValue("pk_psndoc_sub");
					if (pk_psndoc_sub != null
							&& pk_psndoc_sub.trim().length() > 0) {
						// ����ɾ���Ӽ�¼����
						vDelPkPsndocSub.addElement(pk_psndoc_sub);
						//
						vDelSubVOs.addElement(vo);
					}
					setEditLineNo(-1);
					// ɾ��ѡ����
					getCard().delLine();
					// ���ð�ť״̬
					setMultEditChildButtonState();
				}
			} else {
				freeLockPsn(person.getPk_psndoc() + getBodyTableCode());
				return;
			}
		} catch (Exception e) {
			reportException(e);
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("600704", "UPP600704-000078")/*
			 * @res
			 * "ɾ��ʧ�ܣ�"
			 */
					+ nc.ui.ml.NCLangRes.getInstance().getStrByID("600700", "UPP600700-000358")/*
					 * @res
					 * "\nԭ�����£�\n"
					 */
					+ e.getMessage());
			freeLockPsn(person.getPk_psndoc() + getBodyTableCode());
		} finally {
			freeLockPsn(person.getPk_psndoc() + getBodyTableCode());
		}
	}
	/**
	 * ���ذ�ť��Ӧ�¼�����������Ա��Ϣ��¼�����Ӽ�ʹ��
	 * 
	 * @throws java.lang.Exception
	 */
	protected void onDownload() throws java.lang.Exception {
		// ��õ�ǰ�ӱ������ĵ��������м�¼
		CircularlyAccessibleValueObject[] records = person.getTableVO(getBodyTableCode());
		int selRow = getCard().getBillTable().getSelectedRow();
		if (selRow < 0 || selRow >= records.length) {
			showErrorMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("600704", "UPP600704-000079")/* 
																		@res "��ѡ��һ����¼��" */);
			return;
		}
		// ��õ�ǰ��¼���ĵ�
		String remotePath = (String) records[selRow].getAttributeValue("vdocloc");
		// ����Ƿ����ĵ�
		if (remotePath != null) {
			// ��ʾ�����ļ��Ի���
			UIFileChooser fileChooser = new UIFileChooser();
			fileChooser.showSaveDialog(this);
			// ȡ�ñ����ļ�
			File file = fileChooser.getSelectedFile();
			// ȡ��
			if (file == null){
				return;
			}
			// �����ļ��������ļ���
			FileTransClient.download(remotePath, file.getCanonicalPath());
		}
	}
	/**
	 * 
	 * @param isEnabled
	 */
	private void setBillTempletState(boolean isEnabled) {
		if (getEditType() == EDIT_MAIN) {
			if (isEnabled) {
				setBillItemEditable();
			}			
			getCard().setEnabled(isEnabled);
			getCard().getBillModel(getCurrentSetCode()).setEnabled(false);
		} else if (getEditType() == EDIT_SUB) {// �Ӽ�����
			getCard().setEnabled(false);
			getCard().getBillModel(getCurrentSetCode()).setEnabled(isEnabled);
		} else if (getEditType() == EDIT_REF) {
			getCard().setEnabled(isEnabled);
		}else if ( getEditType() == EDIT_RETURN){
			getCard().setEnabled(isEnabled);
		}

	}

	/**
	 * �༭������ť��Ӧ�¼���������
	 * @throws java.lang.Exception
	 */
	protected void onEdit() throws java.lang.Exception {
		//V56���ӣ�У���Ƿ����δ��˵ģ���Ա�䶯�����ݡ�
		if (listSelectRow != -1) {
			String pk_psndoc = (String) psnList[listSelectRow].getAttributeValue("pk_psndoc");
			HIDelegator.getPsnInf().checkIfExistsUnAuditBillofPSN(new String[]{pk_psndoc},"maintable");
		}
		// 
		if (getBillItem("bd_psndoc.amcode") != null)
			getBillItem("bd_psndoc.amcode").setLength(10);// ��ֹ�������ݿ��еĳ���
		if (!isAdding()) { // �༭,����
			if (listSelectRow != -1) {
				loadPsnInfo(listSelectRow);// װ�� ��ǰ����Ա����
			}
			if("600707".equalsIgnoreCase(getModuleName())){
				String sql = " select 1 from hi_psndoc_keypsn where pk_psndoc ='"+person.getPk_psndoc()+"'and enddate is null";
				boolean iskeypsn =HIDelegator.getPsnInf().isRecordExist(sql);
				if(iskeypsn && !caneditkeypsn){
					showWarningMessage(  nc.ui.ml.NCLangRes.getInstance().getStrByID("600707","UPT600707-000239")/* @res "����ԱΪ�ؼ���Ա,�뵽�ؼ���Ա����ڵ�ά������Ϣ��"*/);
					return;
				}
			}
			if (!lockPsn(person.getPk_psndoc())) {
				showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("600704", "UPP600704-000073")/*
				 * @res
				 * "����Ա���ڱ������û����������Ժ����ԣ�"
				 */);
				return;
			}
			if("600704".equalsIgnoreCase(getModuleName())){
				String isreturn = (String) psnList[listSelectRow].getAttributeValue("isreturn");
				if("Y".equalsIgnoreCase(isreturn)){//&& "600704".equalsIgnoreCase(getModuleName()
					changePsncl();
				}
			}
			setEditState(true);
			isUpdated = true;

		}
		if (isEmployeeRef()) {// ��Ա����
			setEditType(EDIT_REF);
			switchButtonGroup(EMPLOYEE_REF_CARD);
			setButtonGroup(EMPLOYEE_REF_CARD);
			setButtonsState(EMPLOYEE_REF_EDIT);
			setEditState(true);
			setHeadBillItemEditable("bd_psnbasdoc", false);
		} else {
			//�����ǰ���ǿ�Ƭ���棬����뿨Ƭ������ͷ����Ĭ����ʾ
			if(getButtonGroup() != CARD_GROUP){
				getCard().setPosMaximized(-1);
			}
			switchButtonGroup(CARD_GROUP);
			setButtonGroup(CARD_GROUP);
			setButtonsState(CARD_EDIT);
			setEditType(EDIT_MAIN);// ���ñ༭����Ϊ�༭����Ϣ��
		}
		// ����ĳЩ��Ŀ�Ŀɱ༭��
		if (nc.ui.hi.hi_301.Util.isDutyDependJobSeries) {
			UIRefPane dutyRef = null;
			UIRefPane jobRef = null;
			BillItem item = getBillItem("bd_psndoc.dutyname");
			if (item != null) {
				dutyRef = (UIRefPane) (item.getComponent());
			}
			jobRef = (UIRefPane) getBillItem("bd_psndoc.pk_om_job").getComponent();
			String oldWhere = dutyRef.getRefModel().getWherePart();
			if (jobRef != null) {
				String pk_jobseries = (String) jobRef.getRefValue("om_job.jobseries");
				if (oldWhere.indexOf("and duty.series ='") >= 0) {
					oldWhere = oldWhere.substring(0, oldWhere.indexOf("and duty.series ='"));
				}
				if (pk_jobseries != null) {
					String newSql = oldWhere + " and duty.series ='"+ pk_jobseries.trim() + "'";
					dutyRef.getRefModel().setWherePart(newSql);
				} else {
					dutyRef.getRefModel().setWherePart(oldWhere);
				}
			}
		}
		if (isAdding()) {
			// ��ǰѡ�еĽڵ�
			DefaultMutableTreeNode node = getSelectedNode();
			if (node == null){
				return;
			}
			CtrlDeptVO dept = getSelectedDept();// ��ǰѡ�еĲ���
			// ��Ա�ɼ��Զ����� ȡ��Ա����
			retrivePsncodeInGroup();

			if ("Y".equalsIgnoreCase(isUniquePsncodeInGroup)) {// ����Ǽ�����Ψһ���򰴼��Ų�����Ա����
				//������Ա�ɼ���ʽ���ñ���
				if ((getPsncodeAutoGenerateParam() != null) && getPsncodeAutoGenerateParam().trim().equals("1")){
					if(intodocdirect == 0){
						autopsncode = HIDelegator.getBillcodeRule().getBillCode_RequiresNew("BS", "0001", null, getObjVO());
						getBillItem("bd_psndoc.psncode").setValue(autopsncode);						
					}else if(intodocdirect == 1){
						autopsncode = "";
						getBillItem("bd_psndoc.psncode").setValue(autopsncode);
					}
				}
			} else {
				if ((getPsncodeAutoGenerateParam() != null) && getPsncodeAutoGenerateParam().trim().equals("1")) {
					if(intodocdirect == 0){
						autopsncode = HIDelegator.getBillcodeRule().getBillCode_RequiresNew("BS", Global.getCorpPK(), null, getObjVO());
						getBillItem("bd_psndoc.psncode").setValue(autopsncode);						
					}else if(intodocdirect == 1){
						autopsncode = "";
						getBillItem("bd_psndoc.psncode").setValue(autopsncode);
					}
				}
			}
			// ���ѡ�е��Ǹ���㣨��˾������Ҫ��ʾ���б���Ա�����в�ѯ���
			if (!node.isRoot() && isAdding()&& dept.getNodeType() == CtrlDeptVO.DEPT) {
				if (dept.isControlled()) {
					BillItem item = getBillItem("bd_psndoc.pk_deptdoc");
					UIRefPane deptRef = (UIRefPane) item.getComponent();
					deptRef.setAutoCheck(true);
					deptRef.setPK(dept.getPk_dept());
					// ���ø�λ����
					UIRefPane job = (UIRefPane) getBillItem("bd_psndoc.pk_om_job").getComponent();
					nc.ui.bd.ref.AbstractRefModel refModel = job.getRefModel();
					nc.ui.hi.hi_301.ref.JobRef jobRef;
					if (refModel instanceof nc.ui.hi.hi_301.ref.JobRef) {
						jobRef = new nc.ui.hi.hi_301.ref.JobRef();
						nc.ui.hi.hi_301.ref.JobRef oldRef = (nc.ui.hi.hi_301.ref.JobRef) refModel;
						jobRef.setPk_deptdoc(oldRef.getPk_deptdoc());
					} else {
						jobRef = new nc.ui.hi.hi_301.ref.JobRef();
					}
					jobRef.setPk_deptdoc(dept.getPk_dept());
					job.setRefModel(jobRef);
					job.setPK("");
					job.setText("");
				}
			}
		} else {// wangkf add --�޸�ʱ�����¸�λ�Ĳ��ա�
			String pk_deptdoc = (String) person.getPsndocVO().getAttributeValue("pk_deptdoc");
			String pk_om_job = (String) person.getPsndocVO().getAttributeValue("pk_om_job");
			UIRefPane job = (UIRefPane) getBillItem("bd_psndoc.pk_om_job").getComponent();
			nc.ui.bd.ref.AbstractRefModel refModel = job.getRefModel();
			nc.ui.hi.hi_301.ref.JobRef jobRef;
			if (refModel instanceof nc.ui.hi.hi_301.ref.JobRef) {
				jobRef = new nc.ui.hi.hi_301.ref.JobRef();
				nc.ui.hi.hi_301.ref.JobRef oldRef = (nc.ui.hi.hi_301.ref.JobRef) refModel;
				jobRef.setPk_deptdoc(oldRef.getPk_deptdoc());
			} else {
				jobRef = new nc.ui.hi.hi_301.ref.JobRef();
			}
			jobRef.setPk_deptdoc(pk_deptdoc);
			job.setRefModel(jobRef);
			job.setPK(pk_om_job);
		}
		setBillTempletState(true);

		getCard().transferFocusTo(0);		
	}
	/**
	 * 
	 * @throws BusinessException
	 */
	private void retrivePsncodeInGroup() throws BusinessException {
		//Ч���Ż�
		//isUniquePsncodeInGroup = PubDelegator.getIParValue().getParaString("0001", "HI_CODEUNIQUE");
		isUniquePsncodeInGroup = getPara("HI_CODEUNIQUE");
		if(isUniquePsncodeInGroup==null){
			isUniquePsncodeInGroup = "Y";
		}
	}

	/**
	 * �����а�ť��Ӧ�¼���������
	 * @throws java.lang.Exception
	 */
	protected void onInsertChild() throws java.lang.Exception {
		//V56���ӣ�У���Ƿ����δ��˵ģ���Ա�䶯�����ݡ�
		HIDelegator.getPsnInf().checkIfExistsUnAuditBillofPSN(new String[]{person.getPk_psndoc()},getBodyTableCode());

		if (!isEditSub()) {
			if (!lockPsn(person.getPk_psndoc() + getBodyTableCode())) {
				showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"600704", "UPP600704-000070")/*
						 * @res "����Ա��"
						 */
						+ getBodyTableName()
						+ nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
						"UPP600704-000071")/*
						 * @res "�Ӽ������������û����������Ժ����ԣ�"
						 */);
				return;
			}
		}


		// ���õ���ģ�����ҳǩ�����л�
		getCard().getBodyTabbedPane().setEnabled(false);
		// ���ڱ༭�ӱ�
		setEditType(EDIT_SUB);
		// �������޸���
		setEditLine(false);
		// �������ڱ༭�к�
		int row = getCard().getBillTable().getSelectedRow();
		getCard().removeEditListener(getBodyTableCode());
		getCard().getBodyPanel().insertLine(row, 1);
		setEditLineNo(row);
		GeneralVO copyVO = setSelRowToNewRow(row, row);
		// ���õ���ģ��Ϊ�ɱ༭״̬
		setBillTempletState(true);
		// Ϊ��ǰ�ӱ����һ�пռ�¼
		SubTable subtable = person.getSubTable(getCard().getBodyTableCode(getCard().getBodyPanel()));
		subtable.getRecords().insertElementAt(copyVO, row);// wangkf
		getCard().removeEditListener(getBodyTableCode());
		// setSelRowRefValueNull(getBodyTableCode());
		// �Ͷ���ͬ����Ĭ��ֵ
		if (getBodyTableCode().equalsIgnoreCase("hi_psndoc_ctrt")) {
			Vector gvos = subtable.getRecords();
			GeneralVO infoVO = (GeneralVO) gvos.elementAt(row);
			infoVO.setAttributeValue("icontstate", nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPP600704-000050")/* @res "����" */);
			infoVO.setAttributeValue(Util.PK_PREFIX + "icontstate",new Integer(2));
			infoVO.setAttributeValue("iconttype", nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPP600704-000072")/* @res "ǩ��" */);
			infoVO.setAttributeValue(Util.PK_PREFIX + "iconttype", new Integer(1));
			getCard().getBodyPanel().getTableModel().setBodyDataVO((CircularlyAccessibleValueObject[]) gvos
					.toArray(new CircularlyAccessibleValueObject[0]));
		}
		if (getBodyTableCode().equalsIgnoreCase("hi_psndoc_dimission")
				||"hi_psndoc_retire".equalsIgnoreCase(getCurrentSetCode())
				||"hi_psndoc_training".equalsIgnoreCase(getCurrentSetCode())
				||"hi_psndoc_orgpsn".equalsIgnoreCase(getCurrentSetCode())
				||"hi_psndoc_ass".equalsIgnoreCase(getCurrentSetCode())) {
			getCard().getBodyPanel().getTableModel().setBodyDataVO(
					(CircularlyAccessibleValueObject[]) subtable.getRecords().toArray(new CircularlyAccessibleValueObject[0]));

		}
		// ��ְ�������Ĭ��ֵ wangkf add
		if (getBodyTableCode().equalsIgnoreCase("hi_psndoc_deptchg")) {
			Vector gvos = subtable.getRecords();
			GeneralVO infoVO = (GeneralVO) gvos.elementAt(row);
			infoVO.setAttributeValue("pk_corp", getClientEnvironment().getCorporation().getUnitname());
			infoVO.setAttributeValue(Util.PK_PREFIX + "pk_corp", corpPK);
			getCard().getBodyPanel().getTableModel().setBodyDataVO((CircularlyAccessibleValueObject[]) gvos
					.toArray(new CircularlyAccessibleValueObject[0]));
			if (nc.ui.hi.hi_301.Util.isDutyDependJobSeries) {
				UIRefPane dutyRef = null;
				UIRefPane jobRef = null;
				BillItem item = getBillItem("hi_psndoc_deptchg.pk_om_duty");
				if (item != null) {
					dutyRef = (UIRefPane) (item.getComponent());
				}
				jobRef = (UIRefPane) getBillItem("hi_psndoc_deptchg.pk_postdoc").getComponent();
				String oldWhere = dutyRef.getRefModel().getWherePart();
				if (jobRef != null) {
					String pk_jobseries = (String) jobRef.getRefValue("om_job.jobseries");
					if (oldWhere.indexOf("and duty.series ='") >= 0) {
						oldWhere = oldWhere.substring(0, oldWhere.indexOf("and duty.series ='"));
					}
					if (pk_jobseries != null) {
						String newSql = oldWhere + " and duty.series ='"+ pk_jobseries.trim() + "'";
						dutyRef.getRefModel().setWherePart(newSql);
					} else {
						dutyRef.getRefModel().setWherePart(oldWhere);
					}
				}
			}
		}
		// 
		if (isNotMultiEdit()) {
			// ���ð�ť״̬
			setInsertChildButtonState();
			// ���õ�ǰ�пɱ༭
			enableTableLine(getEditLineNo());
		} else {
			if (!isEditSub()) {
				setEditSub(true);		
				getCard().getBodyPanel().getTableModel().updateValue();
			}
			// ���ð�ť״̬
			setMultEditChildButtonState();
		}
	}

	/**
	 * �Ƿ񳬱�
	 * @param intoDocData
	 * @return
	 * @throws Exception
	 */
	protected boolean isExceedWorkout(GeneralVO[] intoDocData) throws Exception {
		boolean isExceedWorkout = true;
		// ��λ���ƺ��������
		String corpMsg = "";
		String pk_corp =getClientEnvironment().getCorporation().getPrimaryKey();
		Vector v = new Vector();
		for (int i = 0; i < intoDocData.length; i++) {
			String pk = (String) intoDocData[i].getFieldValue("pk_psndoc");
			v.addElement(pk);
		}
		String[] pk_psndocs = null;
		if (v.size() > 0) {
			pk_psndocs = new String[v.size()];
			v.copyInto(pk_psndocs);
		}				
		ICorpWorkout corpwork = (ICorpWorkout) NCLocator.getInstance().lookup(ICorpWorkout.class.getName());
		WorkoutResultVO result = corpwork.checkWorkout(pk_corp, Global.getUserID(),Global.getLogYear(), pk_psndocs);
		if (result != null) {
			corpMsg = result.getMessage();
			boolean lock = result.isSyncLock();
			boolean pass = result.isPassTest();
			boolean iscorpoverout = result.isCorpOverWorkout();
			boolean isdeptoverout = result.isDeptOverWorkout();
			boolean isjoboverout = result.isJobOverWorkout();
			if (!lock) {
				showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("600705", "UPP600705-000018")/*
				 * @res
				 * "���ƹ������������û����������Ժ����ԣ�"
				 */);
				isExceedWorkout = false;
				return isExceedWorkout;
			}
			if (!pass) {
//				showWarningMessage(corpMsg+"\n �������½���Ա!");
				showWarningMessage(corpMsg + nc.ui.ml.NCLangRes.getInstance().getStrByID("600700", 
						"UPP600700-000118")/* @res "\n �������½���Ա!" */);
				isExceedWorkout = false;
				return isExceedWorkout;
			}


			String msg = "";
			if(corpMsg!=null&&!"".equalsIgnoreCase(corpMsg)){
				msg += corpMsg;
			}
			if (!"".equalsIgnoreCase(msg) && (iscorpoverout||isdeptoverout||isjoboverout)) {
				String title = nc.ui.ml.NCLangRes.getInstance().getStrByID("600722", "UPP600722-000036")/* @res "��ʾ" */;
				String question = msg+ nc.ui.ml.NCLangRes.getInstance().getStrByID("600704","UPP600704-000199")/* @res "�Ƿ�ת����Ա����" */;
				int operatestate = MessageDialog.showYesNoDlg(this, title, question);
				if (operatestate == MessageDialog.ID_YES|| operatestate == MessageDialog.ID_OK) {
//					isExceedWorkout = true;
					if (!"".equalsIgnoreCase(corpMsg) && iscorpoverout) {// ֻ�й�˾����ŷ���Ϣ
						sendMsg(nc.ui.ml.NCLangRes.getInstance().getStrByID("600704","UPP600704-000236")/* @res "������ʾ" */, corpMsg);
					}
				} else {
					isExceedWorkout = false;
				}
			}
		}
		return isExceedWorkout;
	}
	/**
	 * 
	 * @param title
	 * @param msg
	 */
	protected void sendMsg(String title, String msg) {
		nc.vo.pub.msg.UserNameObject[] userRecievers = null;
		String[] keys = null;
		try {
			//Ч���Ż�
			//String recieverPks = PubDelegator.getIParValue().getParaString(Global.getCorpPK(), "HI_MESSAGE");
			String recieverPks = getPara("HI_MESSAGE");
			if (recieverPks != null) {
				keys = getReceivePk(recieverPks);
				userRecievers = HIDelegator.getPsnInf().getUserObj(keys);
				nc.vo.pub.msg.CommonMessageVO msgVo = new nc.vo.pub.msg.CommonMessageVO();
				msgVo.setActionType("MSN");
				msgVo.setTitle(title);
				msgVo.setMessageContent(msg);
				msgVo.setReceiver(userRecievers);
				msgVo.setSendDataTime(Global.getServerTime());
				msgVo.setSender(Global.getUserID());
				HIDelegator.getPFMessage().insertCommonMsg(msgVo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			reportException(e);
		}
	}
	/**
	 * ������Ϣ
	 * @param title
	 * @param msg
	 * @param userRecievers
	 */
	public void sendMsg(String title, String msg,nc.vo.pub.msg.UserNameObject[] userRecievers) {
		try {
			nc.vo.pub.msg.CommonMessageVO msgVo = new nc.vo.pub.msg.CommonMessageVO();
			msgVo.setTitle(title);
			msgVo.setActionType("MSN");
			msgVo.setMessageContent(msg);
			msgVo.setReceiver(userRecievers);
			msgVo.setSendDataTime(Global.getServerTime());
			msgVo.setSender(Global.getUserID());
			HIDelegator.getPFMessage().insertCommonMsg(msgVo);
		} catch (Exception e) {
			e.printStackTrace();
			reportException(e);
		}
	}
	/**
	 * v50 add 
	 * @param pks
	 * @return
	 */
	private String[] getReceivePk(String pks) {
		String[] recievepks = null;
		recievepks = pks.split(",");
		return recievepks;
	}
	
	//wangli add ���ת������������ְ����ʱ���������ǲ���û��ֵ 20121205
	private void CheckIndocData(GeneralVO[] intoDocData) throws ValidationException{
		
		BillTempletBodyVO[] items = getCard().getTempletVO().getBodyVO();
		
		StringBuffer message = new StringBuffer();
		
		
		Vector<BillTempletBodyVO> vbd_psnbasdonull = new Vector<BillTempletBodyVO>();
		Vector<BillTempletBodyVO> vbd_psndonull = new Vector<BillTempletBodyVO>();
		
		BillTempletBodyVO[] btbbd_psnbasdonull = null;
		BillTempletBodyVO[] btbbd_psndonull = null;
		
		for (int j = 0; j < items.length; j++) {
			if (items[j].getNullflag()){
				if (items[j].getTableCode().equals("bd_psnbasdoc")){
					vbd_psnbasdonull.add(items[j]);
				}else if (items[j].getTableCode().equals("bd_psndoc")){
					vbd_psndonull.add(items[j]);
				}
			}
		}
		
		if 	(vbd_psnbasdonull.size() > 0 || vbd_psndonull.size() > 0){
			
			btbbd_psnbasdonull = new BillTempletBodyVO[vbd_psnbasdonull.size()];
			btbbd_psndonull = new BillTempletBodyVO[vbd_psndonull.size()];
			
			
			vbd_psnbasdonull.toArray(btbbd_psnbasdonull);
			vbd_psndonull.toArray(btbbd_psndonull);
			
			for (GeneralVO indodata:intoDocData){
				if (null!=indodata&&null!=indodata.getAttributeValue("pk_psndoc")&&null!=indodata.getAttributeValue("pk_psnbasdoc")){

					try {
						GeneralVO[] vos = HIDelegator.getPsnInf().queryMainPersonInfo(
								(String) indodata.getAttributeValue("pk_psnbasdoc"), Global.getCorpPK(), "bd_psnbasdoc",
								GlobalTool.getFuncParserWithoutWa());
						
						GeneralVO[] vos2 = HIDelegator.getPsnInf().queryMainPersonInfo(
								(String) indodata.getAttributeValue("pk_psndoc"), Global.getCorpPK(), "bd_psndoc",
								GlobalTool.getFuncParserWithoutWa()); 
						
						
						for (GeneralVO vo : vos) {
							for (int i = 0; i < btbbd_psnbasdonull.length; i++) {
								if (null == vo
										.getAttributeValue(btbbd_psnbasdonull[i]
												.getItemkey())) {
									message.append(vo
											.getAttributeValue("psnname")
											+ ":[" 
											+ btbbd_psnbasdonull[i].getTable_name()
											+ "]"
											+ btbbd_psnbasdonull[i]
													.getDefaultshowname()
											+ nc.ui.ml.NCLangRes.getInstance()
													.getStrByID("600704",
															"UPP600704-000156")/**
									 * @res
									 *      "����Ϊ��"
									 */
											+ "\n") ;
								}
							}
						}

						for (GeneralVO vo : vos2) {
							for (int i = 0; i < btbbd_psndonull.length; i++) {
								if (null == vo
										.getAttributeValue(btbbd_psndonull[i]
												.getItemkey())) {
									message.append(vo
											.getAttributeValue("psnname")
											+ ":[" 
											+ btbbd_psndonull[i]
													.getTable_name()
											+ "]"
											+ btbbd_psndonull[i]
													.getDefaultshowname()
											+ nc.ui.ml.NCLangRes.getInstance()
													.getStrByID("600704",
															"UPP600704-000156")/**
									 * @res
									 *      "����Ϊ��"
									 */
									        + "\n") ;
								}
							}
						}
						
					} catch (BusinessException e) {
						reportException(e);
						showErrorMessage(e.getMessage());
					} 
				}
			}
			if (message.length()>0){
				throw new ValidationException(message.toString());
			}
		}
	}

	private List<PsndocVO> PsndocAdd = new ArrayList<PsndocVO>();
	private List<PsndocVO> PsndocAdd2 = new ArrayList<PsndocVO>();
	
	private List<PsnbasdocVO> PsnbasdocAdd = new ArrayList<PsnbasdocVO>();
	private List<PsnbasdocVO> PsnbasdocAdd2 = new ArrayList<PsnbasdocVO>();
	
	private List<PsndocVO> PsndocUpdate = new ArrayList<PsndocVO>();
	private List<PsndocVO> PsndocUpdate2 = new ArrayList<PsndocVO>();
	
	private List<PsnbasdocVO> PsnbasdocUpdate = new ArrayList<PsnbasdocVO>();
	private List<PsnbasdocVO> PsnbasdocUpdate2 = new ArrayList<PsnbasdocVO>();

	/**
	 * 
	 * @param intoDocData
	 * @throws java.lang.Exception
	 */
	private void intoDoc(GeneralVO[] intoDocData) throws java.lang.Exception {
		IGener gener = (IGener) NCLocator.getInstance().lookup(IGener.class.getName());
		IFilePost filepost = (IFilePost) NCLocator.getInstance().lookup(IFilePost.class.getName());
		OperationMsg opmsg = new OperationMsg();
		opmsg.setOptime(Global.getClientEnvironment().getDate().toString());
		opmsg.setUnitcode(Global.getClientEnvironment().getCorporation().getUnitcode());
		opmsg.setUsercode(Global.getClientEnvironment().getUser().getUserCode());
		opmsg.setUsername(Global.getClientEnvironment().getUser().getUserName());

		if(intoDocData==null || intoDocData.length<1) return;
			Vector vPkPsn = new Vector();
		
			//wangli add ����Ƿ������δ��
			CheckIndocData(intoDocData);
		
			if ((getPsncodeAutoGenerateParam() != null)
					&& getPsncodeAutoGenerateParam().trim().equals("0")) {
				checkPsnListPsnCode(intoDocData);
			}else{
				autoSetInDocPsnCode(intoDocData);
			}
			checkPsnListDept(intoDocData);
			if (isExceedWorkout(intoDocData) && intoDocData != null) {// V35 add
				String pk_psndocs = "";
				String pk_psnbasdocs = "";
				// Ч���Ż�start ��ѭ�������Ƶ������
				for (int i = 0; i < intoDocData.length; i++) {
					String pk = (String) intoDocData[i].getFieldValue("pk_psndoc");
					pk_psndocs += "'" + pk + "',";
//					if (!lockPsn(pk)) {
//						showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("600704", "UPP600704-000082")/*
//						 * @res
//						 * "��ǰ�б��е���Ա:"
//						 */
//								+ intoDocData[i].getAttributeValue("psnname").toString()
//								+ nc.ui.ml.NCLangRes.getInstance().getStrByID("600704", "UPP600704-000083")/*
//								 * @res
//								 * "���ڱ������û�����������ʧ��"
//								 */);
//						for (int k = 0; k < v.size(); k++) {
//							freeLockPsn((String) v.elementAt(k));
//						}
//						return;
//					}
//					String sql = " select 1 from bd_psndoc where pk_psndoc ='"+pk+"'and indocflag ='Y'";
//					boolean isinto = HIDelegator.getPsnInf().isRecordExist(sql);
//					if(isinto){
//						showWarningMessage(intoDocData[i].getAttributeValue("psnname").toString()+"�Ѿ�ת����Ա�����������ٴ���ְ!");
//						return;
//					}
//					v.addElement(pk);
					if (!vPkPsn.contains(pk)) {// wangkf add
						vPkPsn.addElement(pk);
					}
				}

//			 ��Ա��Ϣ�ɼ���ת����Ա����ʱͬ�����м�� add by river for 2011-09-14
			if(pk_psndocs.length() > 0)
				pk_psndocs = pk_psndocs.substring(0 , pk_psndocs.length() - 1);
			
			IReadmsg msg = (IReadmsg) NCLocator.getInstance().lookup(IReadmsg.class.getName());
			PsndocVO[] psndocvos = (PsndocVO[])msg.getGeneralVOs(PsndocVO.class, " pk_psndoc in ("+pk_psndocs+")");
			
			for(PsndocVO psndoc : psndocvos) {
				pk_psnbasdocs += "'" + psndoc.getAttributeValue("pk_psnbasdoc") + "',";
				String retStr = filepost.postFile(Uriread.uriPath() , 
						gener.generateXml4(psndoc, "RequestPsndoc", "psn", "add" , opmsg));
			
				String[] strs = retStr.split("<success>");
				String retMsg = "";
				if (strs.length > 1)
					retMsg = strs[1].substring(0, strs[1].indexOf("<"));
				
				if (retMsg.equals("false") || strs.length <= 1) {
					PsndocAdd.add(psndoc);
				}
			}
			
			if(PsndocAdd.size() > 0 ) {
				Thread th1 = new Thread() {
					public void run() {
						
						IGener gener = (IGener) NCLocator.getInstance().lookup(IGener.class.getName());
						IFilePost filepost = (IFilePost) NCLocator.getInstance().lookup(IFilePost.class.getName());
					
						try {
							if(true) {
								this.sleep(SleepTime.Time);
								OperationMsg opmsg = new OperationMsg();
								opmsg.setOptime(Global.getClientEnvironment().getDate().toString());
								opmsg.setUnitcode(Global.getClientEnvironment().getCorporation().getUnitcode());
								opmsg.setUsercode(Global.getClientEnvironment().getUser().getUserCode());
								opmsg.setUsername(Global.getClientEnvironment().getUser().getUserName());
								
								for(PsndocVO psn : PsndocAdd) {
									String retStr = filepost.postFile(Uriread.uriPath() , 
											gener.generateXml4(psn, "RequestPsndoc", "psn", "update" , opmsg));
									
									String[] strs = retStr.split("<success>");
									String retMsg = "";
									if (strs.length > 1)
										retMsg = strs[1].substring(0, strs[1].indexOf("<"));
								
									if(retMsg.equals("false") || strs.length <= 1)
										PsndocAdd2.add(psn);
								}
								
								PsndocAdd = PsndocAdd2;
								
								PsndocAdd2 = new ArrayList<PsndocVO>();
								
//								if(PsndocAdd.size() == 0)
//									break;
								
								
							}
							System.out.println("<<<<<<  ��Ա���������߳�ֹͣ�� >>>>>>");
							System.out.println("<<<<<<  �̺߳�: " + this.getId() + " >>>>>>");
							this.stop();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				};
				
				th1.start();
			}
			
			if(pk_psnbasdocs.length() > 0 ) 
				pk_psnbasdocs = pk_psnbasdocs.substring(0 , pk_psnbasdocs.length() - 1 );
			
			PsnbasdocVO[] psnbasdocvos = (PsnbasdocVO[])msg.getGeneralVOs(PsnbasdocVO.class, " pk_psnbasdoc in ("+pk_psnbasdocs+")");
			for(PsnbasdocVO psnbasdocvo : psnbasdocvos) {
				String retStr = filepost.postFile(Uriread.uriPath() , 
						gener.generateXml6(psnbasdocvo, "RequestPsnbasdoc", "psnbas", "add"));
			
				String[] strs = retStr.split("<success>");
				String retMsg = "";
				if (strs.length > 1)
					retMsg = strs[1].substring(0, strs[1].indexOf("<"));

				if (retMsg.equals("false") || strs.length <= 1) {
					PsnbasdocAdd.add(psnbasdocvo);
				}
			}
			
			if(PsnbasdocAdd.size() > 0 ) {
				Thread th2 = new Thread() {
					public void run() {
						IGener gener = (IGener) NCLocator.getInstance().lookup(IGener.class.getName());
						IFilePost filepost = (IFilePost) NCLocator.getInstance().lookup(IFilePost.class.getName());
					
						
						try {
							if(true) {
								this.sleep(SleepTime.Time);
							
								for(PsnbasdocVO psn : PsnbasdocAdd) {
									String retStr = filepost.postFile(Uriread.uriPath() , 
											gener.generateXml6(psn, "RequestPsnbasdoc", "psn", "update"));
									
									String[] strs = retStr.split("<success>");
									String retMsg = "";
									if (strs.length > 1)
										retMsg = strs[1].substring(0, strs[1].indexOf("<"));
								
									if(retMsg.equals("false") || strs.length <= 1)
										PsnbasdocAdd2.add(psn);
								}
								
								PsnbasdocAdd = PsnbasdocAdd2;
								
								PsnbasdocAdd2 = new ArrayList<PsnbasdocVO>();
								
//								if(PsnbasdocAdd.size() == 0)
//									break;
								
								
							}
							
							System.out.println("<<<<<<  ��Ա�������������߳�ֹͣ�� >>>>>>");
							System.out.println("<<<<<<  �̺߳�: " + this.getId() + " >>>>>>");
							this.stop();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				};
				
				th2.start();
			}

				// wangkf add------
				String[] pk_psndocs2 = null;
				if (vPkPsn.size() > 0) {
					pk_psndocs2 = new String[vPkPsn.size()];
					vPkPsn.copyInto(pk_psndocs2);
				}	
		
				HIDelegator.getPsnInf().intoDoc(intoDocData, pk_psndocs2,Global.getUserID());
				// Ч���Ż�end
				// ���б��е��˴Ӳ�ѯ�����ɾ�����Ѿ���ת�뵵������)
				deleteListFromResult(intoDocData);
				// ������ŵ���Ա�б���
				psnDeptCache.clear();
				psnCorpCache.clear();
				// ����ѡ�е�ǰ����
				deptTreeValueChanged(null);
				listSelectRow = -1;
			}
		
	}
	/**
	 * 
	 * @param srcData
	 * @param groupPk
	 * @return
	 */
	private HashMap groupByPk(GeneralVO[] srcData, String groupPk) {
		HashMap hm = new HashMap();
		if (srcData != null) {
			Vector vPool = new Vector();
			for (int i = 0; i < srcData.length; i++) {
				vPool.addElement(srcData[i]);
			}
			Vector vSameGroup = new Vector();
			while (vPool.size() > 0) {
				GeneralVO firstVO = (GeneralVO) vPool.elementAt(0);
				String firstKey = (String) firstVO.getAttributeValue(groupPk);
				vSameGroup.addElement(firstVO);
				for (int i = 1; i < vPool.size(); i++) {
					GeneralVO nextVO = (GeneralVO) vPool.elementAt(i);
					String nextKey = (String) nextVO.getAttributeValue(groupPk);
					if (firstKey != null && nextKey != null
							&& firstKey.equalsIgnoreCase(nextKey)) {
						vSameGroup.addElement(nextVO);
						vPool.remove(nextVO);
						i--;
					}
				}
				vPool.remove(firstVO);
				if (vSameGroup.size() > 0) {
					GeneralVO[] vos = new GeneralVO[vSameGroup.size()];
					vSameGroup.copyInto(vos);
					hm.put(firstKey, vos);
					vSameGroup.removeAllElements();
				}
			}
		}
		return hm;
	}

	/**
	 * �õ���Ҫ�����жϵ����� V35 add
	 * @param intoDocData
	 * @return
	 */
	private GeneralVO[] getWorkoutData(GeneralVO[] intoDocData) {
		GeneralVO[] vos = null;
		Vector v = new Vector();
		if (intoDocData != null) {
			for (int i = 0; i < intoDocData.length; i++) {
				Integer intPsnclscope = (Integer) intoDocData[i]
				                                              .getAttributeValue("psnclscope");// ֻ�ж���Ա����Ϊ��ְ
				if (intPsnclscope != null && intPsnclscope.intValue() == 0) {
					v.addElement(intoDocData[i]);
				}
			}
		}
		if (v.size() > 0) {
			vos = new GeneralVO[v.size()];
			v.copyInto(vos);
		}
		return vos;
	}

	/**
	 * ����ǰ�б��е���Աת�뵵��
	 * @throws java.lang.Exception
	 */
	private void onIntoDoc() throws java.lang.Exception {
		GeneralVO[] intoDocData = getSelectPsnListData();
		if (intoDocData == null ||intoDocData.length<1 ) {
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("600710","UPT600710-000050")/*@res "���ڸ�ѡ���й�ѡҪת����Ա��������Ա!"*/);
			return;
		}
		intoDoc(getSelectPsnListDataOfMyCorp());
	}


	/**
	 * �˴����뷽�������� �������ڣ�(2004-5-31 14:22:07)
	 * 
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	protected void onPrint() throws java.lang.Exception {
		if (getPsnList().getTableModel().getRowCount() == 0) {
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID("6007","UPT600704-000295")/*@res "��ǰû����Ҫ��ӡ�����ݡ�"*/);
			return;
		}
		UITable uitable = getPsnList().getTable();

		nc.ui.pub.print.PrintDirectEntry print = nc.ui.report.base.PrintManager.getDirectPrinter(uitable, null);
		java.awt.Font font = new java.awt.Font("dialog", java.awt.Font.BOLD, 30);

		print.setTitle(getTitle());
		print.setTitleFont(font); 
		print.preview();

	}


	/**
	 * ��ѯ������
	 * @throws java.lang.Exception
	 */
	protected void onQuery() {//throws java.lang.Exception 

		try{
			
			vBookConds.removeAllElements();
			//������ѯ����
			getQueryDlg().getUIPanelNormal().setLayout(new BorderLayout());
//			// 
//			if (getModuleName().equalsIgnoreCase("600707")
//			&& ((Item) getCmbPsncl().getSelectedItem()).getValue() == 0) {
//			((NormalQueryPanel)getQueryDlg().getUIPanelNormal().getComponent(0)).getcheckIncludeReturn().setVisible(true);
//			}else{
//			((NormalQueryPanel)getQueryDlg().getUIPanelNormal().getComponent(0)).getcheckIncludeReturn().setVisible(false);
//			}			
			getQueryDialog().showModal();
			if (getQueryDialog().getResult() == UIDialog.ID_OK) {
				isQuery = true;
				//update by sunxj 2010-02-03 ���ٲ�ѯ���  start
				isQuickSearch= false;
				//update by sunxj 2010-02-03 ���ٲ�ѯ���  end
				hmSortCondition.clear();
				onRefresh();
			}
		}
		catch (Exception e) {
			reportException(e);
			showErrorMessage(e.getMessage());
		}
	}

	/**
	 * ˢ�½�����Ӧ�¼���������
	 * @throws java.lang.Exception
	 */
	public void onRefresh() {
		try {
			//update by sunxj 2010-02-03 ���ٲ�ѯ��� start
//			if (queryDlg == null) {
			if (queryDlg == null && !isQuickSearch) {
				return;
			}
			//update by sunxj 2010-02-03 ���ٲ�ѯ��� end
			// ˢ�����л���
			psnList = null;
			queryResult = null;
			recordcount = 0;
			person = null;
			psnDeptCache.clear();
			psnCorpCache.clear();
			persons.clear();
			// ǿ�ƻ�������
			System.gc();
			hmSortCondition.clear();
			listSelectRow = -1;
			// ���²�ѯ��ˢ����Ա�б�
			performQuery();
		}catch(Exception ex){
			reportException(ex);
			showErrorMessage(ex.getMessage());
		}
	}
	/**
	 * �༭����ķ��ذ�ť��
	 * @throws java.lang.Exception
	 */
	private void onReturn() throws java.lang.Exception {
		// ֱ�ӵ��÷��ط���
		returnToMain();
	}
	private String userpks = null;

	private UserNameObject[] userRecievers = null;
	/**
	 * ���水ť��Ӧ�¼���������
	 * @throws java.lang.Exception
	 */
	public void onSave() throws java.lang.Exception {
		String pkpsn = "";
		try {
			if (isEmployeeRef()) {// �����Ա������
				String[] tableCodes = new String[] { "bd_psndoc" };// ��ȡ��ǰ���ڱ༭�ı�
				PersonEAVO eavo = getInputData(tableCodes);// ��ȡ��������
				eavo.setJobtype(person.getJobType());
				eavo.getPsndocVO().setAttributeValue("pk_corp",Global.getCorpPK());
				eavo.getPsndocVO().setAttributeValue("indocflag",new nc.vo.pub.lang.UFBoolean('Y'));
				dataNotNullValidateforRef(-1);
				validateInput(eavo, tableCodes);
				// dataUniqueValidate("bd_psnbasdoc",eavo);//����Ψһ��У�� ��Ҫ����������У��
				eavo.setPk_psnbasdoc(person.getPk_psnbasdoc());
				eavo.setPk_psndoc(person.getPk_psndoc());
				saveHiRef(eavo);
				person = eavo;				
//				String msgContent = "�������� ��Ա����:"+ eavo.getPsndocVO().getAttributeValue("psnname").toString() 
//				+ ",��Ա����:" + oldpsncode;
				String msgContent = nc.ui.ml.NCLangRes.getInstance().getStrByID("600700", 
						"UPP600700-000120")/* @res "�������� ��Ա����:" */
					+ eavo.getPsndocVO().getAttributeValue("psnname").toString() 
					+ nc.ui.ml.NCLangRes.getInstance().getStrByID("600700", 
							"UPP600700-000121")/* @res ",��Ա����:" */ 
					+ oldpsncode;
				//add by zhyan 2006-09-20 ������Ϣ���öԻ���
				getUserSelectDlg().setApp_pk_corp(app_pk_corp);
				getUserSelectDlg().refreshContent();
				getUserSelectDlg().setLocationRelativeTo(this);
				getUserSelectDlg().showModal();
				if(userSelectDlg.getResult()== UIDialog.ID_OK ){
					HIDelegator.getPsnInf().insertRecievers(app_pk_corp, getUserpks(),1);//������Ա��Ϣ������
				}
//				sendMsg("��������", msgContent, getUserRecievers());
				sendMsg(nc.ui.ml.NCLangRes.getInstance().getStrByID("600700", "UPP600700-000122")/* @res "��������" */
						, msgContent, getUserRecievers());
				oldpsncode = null;
				setEmployeeRef(false);
			} else {
				save();
			}
			setEditState(false);
			// ��������л���������ð�ť״̬
			if (getEditType() == EDIT_SORT) {
				setSaveSortBottonState();
				// ������������ַ��������棬������ѡ��ǰ���Žڵ�
				returnToMain();
				deptTreeValueChanged(null);
				if (getIsSelSet().isSelected()) {
					getUISplitPaneVertical().setDividerLocation(SPLIT_STANDARD);
					getUISplitPaneVertical().setEnabled(true);
					splitLocation = SPLIT_STANDARD;
				} else {
					getUISplitPaneVertical().setDividerLocation(SPLIT_MAX);
					getUISplitPaneVertical().setEnabled(true);
					splitLocation = SPLIT_MAX;
				}
			} else if (getEditType() == EDIT_MAIN) {
				if (isAdding()) {// �������ӱ����ؼ��༭����
					BillItem[] items = checkAddDlg.getAllBillItems();
					for (int i = 0; i < items.length; i++) {
						BillItem item = getCard()
						.getHeadItem(items[i].getKey());
						Object obj = htbEdit.get(items[i].getKey());
						if (obj != null) {
							item.setEdit(((Boolean) obj).booleanValue());
						} else {
							item.setEdit(false);
						}
					}
					setAdding(false);
				}
				getCard().setEnabled(false);// ���õ�ǰ���ݱ༭״̬Ϊ���ɱ༭
				//Ч���Ż�start
				if (!isAdding()) { // �޸ı������
					pkpsn = person.getPk_psndoc();
//					freeLockPsn(person.getPk_psndoc());
				}
				//Ч���Ż�end
				setButtonsState(CARD_MAIN_BROWSE);
			} else if (getEditType() == EDIT_REF) {//
				getCard().setEnabled(false);// ���õ�ǰ���ݱ༭״̬Ϊ���ɱ༭
				setButtonsState(EMPLOYEE_REF_CARD_BROWSE);
			}else if (getEditType() == EDIT_RETURN) {//
				setAdding(false);
				setButtonsState(CARD_MAIN_BROWSE);
			}			
			else {
				getCard().setEnabled(false);// ���õ�ǰ���ݱ༭״̬Ϊ���ɱ༭
				getCard().getBodyTabbedPane().setEnabled(true);// �ָ�����ҳǩΪ��ѡ��
				if (getEditType() == EDIT_SUB) {
					if (isEditLine()) // �ӱ��б༭����
						freeLockPsn(person.getPk_psndoc() + getBodyTableCode());
				}
				setButtonsState(CARD_MAIN_BROWSE);
				setButtonsState(CARD_CHILD_BROWSE);
			}
			getBillItem("bd_psndoc.clerkcode").setEdit(false);// ����ְԱ����ؼ����ɱ༭
			autopsncode = null;// wangkf add ��Ա�����Զ����ɣ���ȡ��������벻ȡ�š�
			isrehire = false;
			isbackrehire = false;
			getCard().getBodyTabbedPane().setEnabled(true);
		} finally {
			if (getEditType() == EDIT_MAIN) {
				if (!isAdding()) {
					freeLockPsn(pkpsn);
				}
			} else if (getEditType() == EDIT_SUB) {
				if (!isEditLine()) {
					freeLockPsn(person.getPk_psndoc() + getBodyTableCode());
				}
			}
		}
	}



	/**
	 * ��������û���ť��Ӧ�¼��������� �������ڣ�(2004-5-30 13:10:36)
	 * 
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	private void onSmUser() throws java.lang.Exception {

		// ���ѡ���е���Ч��
		int rowCount = getPsnList().getTable().getRowCount();
		if (rowCount == 0)
			return;
		// ��ʾ��������û��Ի���
		SmUserAddDlg smUserDlg = new SmUserAddDlg(this);
		smUserDlg.setLocationRelativeTo(this);
		smUserDlg.showModal();
	}

	/**
	 * ����ť������Ӧ�¼��������� �������ڣ�(2004-5-11 8:33:56)
	 * 
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	protected void onSort() throws java.lang.Exception {
		// װ����������v50 update zhyan
		EditShowOrder();
		// ����ѡ��״̬
		bnHistory.push(getButtons());
		// ���ð�ť��״̬
		setButtons(grpShoworder);
		bnSave.setEnabled(true);
		bnCancel.setEnabled(true);
		updateButtons();
		// ��󻯷ָ����������ɲ���
		getUISplitPane().setDividerLocation(0);
		getUISplitPane().setDividerSize(0);
		getUISplitPane().setEnabled(false);
		getUISplitPaneVertical().setDividerLocation(SPLIT_MAX);
		getUISplitPaneVertical().setEnabled(true);
		// ���ر���panel
		getNorthPanel().setVisible(false);
		// ���ñ༭״̬Ϊ����
		setEditType(EDIT_SORT);
	}

	/**
	 * �޸��в�����ť��Ӧ�¼��������� 
	 * �������ڣ�(2004-5-21 15:16:22)
	 * @exception java.lang.Exception �쳣˵����
	 */
	protected void onUpdateChild() throws java.lang.Exception {		
		if("600707".equalsIgnoreCase(getModuleName())){
			String sql = " select 1 from hi_psndoc_keypsn where pk_psndoc ='"+person.getPk_psndoc()+"'and enddate is null";
			boolean iskeypsn =HIDelegator.getPsnInf().isRecordExist(sql);
			if(iskeypsn && !caneditkeypsn){
				showWarningMessage( nc.ui.ml.NCLangRes.getInstance().getStrByID("600707","UPT600707-000239")/* @res "����ԱΪ�ؼ���Ա,�뵽�ؼ���Ա����ڵ�ά������Ϣ��"*/);
				return;
			}
		}
		//V56���ӣ�У���Ƿ����δ��˵ģ���Ա�䶯�����ݡ�
		HIDelegator.getPsnInf().checkIfExistsUnAuditBillofPSN(new String[]{person.getPk_psndoc()},getBodyTableCode());

		if (!isEditSub()) {
			if (!lockPsn(person.getPk_psndoc() + getBodyTableCode())) {
				showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
						"600704", "UPP600704-000070")/*
						 * @res "����Ա��"
						 */
						+ getBodyTableName()
						+ nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
						"UPP600704-000071")/*
						 * @res "�Ӽ������������û����������Ժ����ԣ�"
						 */);
				return;
			}
		}

		// wangkf add
		if (getCard().getBillTable().getSelectedRow() < 0) {
			showWarningMessage(nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"600704", "UPP600704-000085")/* @res "��ѡ����" */);
			return;
		}
		// �����ӱ�ҳǩΪ����ѡ����ֹ�л�ҳǩ
		getCard().getBodyTabbedPane().setEnabled(false);
		// ���ñ༭����Ϊ�༭�ӱ�
		setEditType(EDIT_SUB);
		// ���ڱ༭��
		setEditLine(true);
		// �������ڱ༭���к�
		int row = getCard().getBillTable().getSelectedRow();
		setEditLineNo(row);
		// ���õ�ǰ����ģ��ɱ༭	
		setBillTempletState(true);
		// ��ȡ��ǰ�Ӽ�����
		CircularlyAccessibleValueObject[] records = person.getTableVO(getBodyTableCode());
		GeneralVO vo = (GeneralVO) records[row];
		if (records != null) {
			if (subBackupVOs == null) {
				subBackupVOs = new GeneralVO[records.length];
				for (int i = 0; i < subBackupVOs.length; i++) {
					subBackupVOs[i] = (GeneralVO) records[i].clone();
				}
			} else {
				vo = subBackupVOs[row];
			}
		}
		// Ŀǰ�Ĳ����Ѿ��ܹ�����������
		getCard().getBillModel().setBodyRowVO(vo, row);
		// 
		if (isNotMultiEdit()) {
			// ���ð�ť״̬
			setUpdateChildButtonState();
			// ���õ�ǰ���ڱ༭����
			enableTableLineModify(getEditLineNo());
		} else {
			if (!isEditSub()) {
				setEditSub(true);		
				getCard().getBodyPanel().getTableModel().updateValue();
			}
			// ���ð�ť״̬
			setMultEditChildButtonState();
		}

		// ����ĳЩ��Ŀ�ı༭��
		String tableCode = getBodyTableCode();
		if ("hi_psndoc_deptchg".equalsIgnoreCase(tableCode)) {
			setHiDeptchgEditable(vo);
			if (nc.ui.hi.hi_301.Util.isDutyDependJobSeries) {
				UIRefPane dutyRef = null;
				UIRefPane jobRef = null;
				BillItem item = getBillItem("hi_psndoc_deptchg.pk_om_duty");
				if (item != null) {
					dutyRef = (UIRefPane) (item.getComponent());
				}
				jobRef = (UIRefPane) getBillItem("hi_psndoc_deptchg.pk_postdoc")
				.getComponent();
				String oldWhere = dutyRef.getRefModel().getWherePart();
				if (jobRef != null) {
					String pk_jobseries = (String) jobRef
					.getRefValue("om_job.jobseries");
					if (oldWhere.indexOf("and duty.series ='") >= 0) {
						oldWhere = oldWhere.substring(0, oldWhere
								.indexOf("and duty.series ='"));
					}
					if (pk_jobseries != null) {
						String newSql = oldWhere + " and duty.series ='"
						+ pk_jobseries.trim() + "'";
						dutyRef.getRefModel().setWherePart(newSql);
					} else {
						dutyRef.getRefModel().setWherePart(oldWhere);
					}
				}
			}
		}else if ("hi_psndoc_keypsn".equalsIgnoreCase(tableCode)){
			setHiKeyPsnEditable(vo);
		}		
	}

	/**
	 * �ϴ���Ա�ĸ����ĵ���ť��Ӧ�����¼������� �������ڣ�(2004-5-25 16:30:05)
	 * 
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	protected void onUpload() throws java.lang.Exception {
		if (listSelectRow < 0) {
			return;
		}
		// ��ǰѡ����Ա����
		String pk_psnbasdoc = (String) psnList[listSelectRow]
		                                       .getAttributeValue("pk_psnbasdoc");

		//����ǲ��ǹؼ���Ա
		boolean caneditattachment = true;
		if("600707".equalsIgnoreCase(getModuleName())){
			String sql = " select 1 from hi_psndoc_keypsn where pk_psndoc ='"+(String) psnList[listSelectRow].getAttributeValue("pk_psndoc")+"'and enddate is null";
			boolean iskeypsn =HIDelegator.getPsnInf().isRecordExist(sql);
			if(iskeypsn && !caneditkeypsn){
				caneditattachment = false;
			}
		}

		// ����ÿ�ζ�ʵ�������ڣ���ʹ��ͬһ�����ڣ�Ϊ�˱���·����
		if(attdlg ==null){
			attdlg = new AttachmentDialog(this, pk_psnbasdoc,"bd_psndoc");
		}else{
			attdlg.setObjectPk(pk_psnbasdoc);
		}
		//��ǰ���޸ġ���ť���ǿ��ã��򸽼����޸ģ�����ֻ�ܲ鿴��
		if (!bnEdit.isEnabled()||!caneditattachment) attdlg.setFileBtnUsable(7);
		attdlg.showModal();
	}
	private AttachmentDialog attdlg = null;
	
	/**
	 * ���ݲ�ѯ�Ի��������������Ȩ�ޡ���Ա��Χ��ѯ��Ա�б� 
	 * �������ڣ�(2004-5-13 11:42:59)
	 * @exception java.lang.Exception �쳣˵����
	 */
	private void performQuery() throws java.lang.Exception {
		// �������æ
		Frame thisFrame = Util.getTopFrame(this);
		try {
			if (thisFrame != null)
				thisFrame.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
			// ѡ�и��ڵ�
			TreePath path = getDeptTree().getSelectionPath();
			if (path == null) {// !isRoot(path) ||
				// �����ǰû��ѡ�л���ѡ�нڵ㲻�Ǹ������ѡ������
				Object root = getDeptTree().getModel().getRoot();
				getDeptTree().setSelectionPath(
						new TreePath(new Object[] { root }));
			} else {
				getDeptTree().setSelectionPath(path);
				// ˢ�¸�����ѡ���¼�
				deptTreeValueChanged(null);
			}
		} finally { // �������Ϊȱʡ
			if (thisFrame != null)
				thisFrame.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		}
	}

	protected int listSelectRow = -1;// �б���ѡ�еĵ�ǰ��

	/**
	 * ��Ա�б�ѡ���¼��� 
	 * �������ڣ�(2004-5-13 15:53:49)
	 * @param event javax.swing.event.ListSelectionEvent
	 */
	private void psnListValueChange(ListSelectionEvent event) {
		try {
			// V35 ������Ա���Ӽ���Ϣ
			listSelectRow = getPsnList().getTable().getSelectedRow();
			String tableCode = getBodyTableCode();
			getCard().setBodyMenuShow(tableCode, false);
			person = null;
			if(getIsSelSet().isSelected()){
				person = loadPsnChildInfo(listSelectRow, tableCode);
			}
			// V35 ����״̬�仯��1����ť
			setButtonsState(LIST_BROWSE);
			// ����ǰѡ�е��˵���������״̬����
			setPsnNameOnBottom();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ѯ��Ա�б� 
	 * �������ڣ�(2004-6-1 11:02:41)
	 * @exception java.lang.Exception �쳣˵����
	 */
	protected void queryResult() throws java.lang.Exception {

		vBookConds.clear();


		// ��ǰ��¼��˾
		String selectcorp = getTreeSelCorpPK();
		ConditionVO[] cvos = conditions;
		//Integer recordcounts = PubDelegator.getIParValue().getParaInt("0001", "HI_MAXLINE");//Ч���Ż�
		Integer recordcounts =Integer.parseInt(getPara("HI_MAXLINE"));

		recordcounts = (recordcounts == null ? new Integer(1000) : recordcounts);
		// ������Ա�ɼ�������ѯ
		// wangkf add ���Ա� ��Ӣ��/���� �ĳ� ��������
		if (cvos != null) {
			for (int c = 0; c < cvos.length; c++) {
				if (cvos[c] != null&& "bd_psnbasdoc.sex".equals(cvos[c].getFieldCode())&& "male".equals(cvos[c].getValue())) {
					cvos[c].setValue("��");
				} else if (cvos[c] != null&& "bd_psnbasdoc.sex".equals(cvos[c].getFieldCode())&& "female".equals(cvos[c].getValue())) {
					cvos[c].setValue("Ů");
				} else if (cvos[c] != null&& "hi_psndoc_ctrt.iconttype".equals(cvos[c].getFieldCode())) {
					int index = cvos[c].getComboIndex();
					if (index > 1) {
						index++;
					}
					cvos[c].setValue(Integer.toString(index));
				}
			}
		}
		wheresql ="";
		// ������ѡ��ڵ��Լ��Ƿ�����¼���Ա��Ӳ�ѯ����
		if (getSelectedNode() != null) {

			CtrlDeptVO deptvo = (CtrlDeptVO) (getSelectedNode().getUserObject());
			// ���ѡ�еĽڵ��ǲ��Žڵ���Ҫ��Ӳ�������
			if (deptvo.getNodeType() == CtrlDeptVO.DEPT) {
				//���ǰ����¼�����
				if (getincludeChildDeptPsn().isSelected()) {
					// 
					wheresql += " and bd_psndoc.pk_deptdoc in ";
					wheresql += " ( select pk_deptdoc from bd_deptdoc where pk_corp = '";
					wheresql += selectcorp;
					wheresql += "' and innercode like '" + deptvo.getInnercode().trim()+"%') ";
				} else {
					wheresql += " and bd_psndoc.pk_deptdoc = '"
						+ deptvo.getPk_dept() + "' ";
				}
			}
		} 
		if(!getincludeCancleDept().isSelected()){
			wheresql+= " and (bd_deptdoc.hrcanceled = 'N' or bd_psndoc.pk_deptdoc is null)";
		}
		recordcount = 0;

		String DLGwheresql = getQueryDialog().getWhereSQL();
		HashSet<String> hstables = getQueryDialog().getUsedTableName();
		String[] DLGtables = hstables.toArray(new String[0]);
		tablesFromQueryDLG = DLGtables;
		//��ȡ�����ֶΡ�
		//Ŀǰʹ�ú�̨�����轫order by���룬����Ҫ��֤���е��ֶ�is selected.
		String orderbyclause = getConfigDialog().getOrderStr();
		orderbyclause = orderbyclause.replaceAll("bd_psnbasdoc.approveflagname", "bd_psnbasdoc.approveflag");
		Vector<Attribute> orderfields = getConfigDialog().getSortingFields();
		//��һ�汾����ȫ���ñ�����tablename__fieldname���Ͳ�������ظ�

		String selectfieldclause = getListField();
		for(int i=0;i<orderfields.size();i++){
			if (selectfieldclause.indexOf(orderfields.get(i).toString())<0){
				//��systemsort������Ϊ�˷�ֹ�ֶβ����������
				//��bd_corp.showorder��bd_deptdoc.showorder���ڰ��в�ѯʱ���������һselect * ....����showorder��sql�ᱨ��
				selectfieldclause+=(","+orderfields.get(i).getAttribute().getCode()+" as systemsort"+i);
			}
		}
		selectfieldclause = selectfieldclause.replaceAll("bd_psnbasdoc.approveflagname", "bd_psnbasdoc.approveflag");

		//v50 update ���ù�˾ѭ����ѯ,֧�ּ�������Ա����
		if (selectcorp != null && selectcorp.length() > 0) {
			recordcount += HIDelegator.getPsnInf()
			.queryRecordCountByCondition_Collect(false, selectcorp,
					DLGwheresql, DLGtables, Global.getCorpPK(),
					Global.getUserID(), wheresql,
					GlobalTool.getFuncParserWithoutWa(),getincludeChildDeptPsn().isSelected());
			GeneralVO[] result = HIDelegator.getPsnInf()
			.queryByCondition_Collect(false, selectcorp, DLGwheresql,
					DLGtables, Global.getCorpPK(), Global.getUserID(),
					selectfieldclause, wheresql, recordcounts.intValue(),
					GlobalTool.getFuncParserWithoutWa(),orderbyclause,getincludeChildDeptPsn().isSelected());
			queryResult = result;
		}
		else {
			queryResult = new GeneralVO[0];
		}
	}

	/**
	 * ���������档 
	 * �������ڣ�(2004-5-10 19:59:56)
	 */
	protected void returnToMain() {
		// ����������ֶα�������������û�У���Ҫ���ء�
		if (!showordervisible)
			getPsnList().hideTableCol("showorder");
		// ��ȡ��ʷ��ť���ָ�
		ButtonObject[] buttons = (ButtonObject[]) bnHistory.pop();
		setButtons(buttons);
		updateButtons();
		if (getEditType() != EDIT_SORT) {
			// ����������
			showPanel(getUISplitPane().getName());
		} else {
			// ��󻯷ָ����������ɲ���
			getUISplitPane().setDividerLocation(150);
			getUISplitPane().setDividerSize(4);
			getUISplitPane().setEnabled(true);
			getNorthPanel().setVisible(true);
		}
		// �����ڱ༭״̬
		setEditType(EDIT_NONE);
		// ���õ�ǰ������ʾ״̬Ϊ��ʾ������
		isShowBillTemp = false;
	}

	/**
	 * �����ͱ������ݡ� 
	 * �������ڣ�(2004-5-14 16:03:12)
	 * @exception java.lang.Exception �쳣˵����
	 */
	private void save() throws java.lang.Exception {
		IGener gener = (IGener) NCLocator.getInstance().lookup(IGener.class.getName());
		IFilePost filepost = (IFilePost) NCLocator.getInstance().lookup(IFilePost.class.getName());

		// ���򱣴�
		if (getEditType() == EDIT_SORT) {
			saveShowOrder();//v50 add
			return;
		}
		// ��������֮��ı���
		String[] tableCodes = getEditingTables();// ��ȡ��ǰ���ڱ༭�ı�
		PersonEAVO eavo = getInputData(tableCodes);// ��ȡ��������
		if (getEditType() == EDIT_MAIN || getEditType() == EDIT_RETURN) {
			if (isAdding()) {
				eavo.setJobtype(0);
			} else {
				eavo.setJobtype(person.getJobType());
			}
			// �ǿ�У��
			dataNotNullValidate(tableCodes[0], null, -1);
			// 
			dataUniqueValidate("bd_psnbasdoc", eavo);// ����Ψһ��У��

		} else {
			if (isEditSub()) {
				// 
				CircularlyAccessibleValueObject[] records = eavo
				.getTableVO(tableCodes[0]);
				GeneralVO vo = null;
				if (records != null) {
					for (int i = 0; i < records.length; i++) {
						vo = (GeneralVO) records[i];
						dataNotNullValidate(tableCodes[0],
								new GeneralVO[] { vo }, i);
						recordNotNullValidate(tableCodes[0], vo, i);
					}
				}
			} else {
				int row = getEditLineNo();
				CircularlyAccessibleValueObject[] records = eavo
				.getTableVO(tableCodes[0]);
				GeneralVO vo = (GeneralVO) records[row];
				dataNotNullValidate(tableCodes[0], new GeneralVO[] { vo }, row);
				recordNotNullValidate(tableCodes[0], vo, row);
			}
		}
		
		// modified by zhangdd, ����֤��ʽ�ṩ֧��
		if (!getCard().getBillData().execValidateFormulas())
			throw new BusinessException("ValidateFormulasValue");

		BillItem ref = getBillItem("bd_psnbasdoc.ssum");
		if(ref !=null){
			ref.getComponent().requestFocus();
		}
		eavo.getPsndocVO().setAttributeValue("psnname",eavo.getAccpsndocVO().getFieldValue("psnname"));
		validateInput(eavo, tableCodes);// ���������Ч��
		if (getEditType() == EDIT_MAIN || getEditType() == EDIT_RETURN) {
			saveMain(eavo);

// ��Ա��Ϣά�����ӻ��޸ĺ�ı��������add by river for 2011-08-30
			try {
				if (!eavo.getParentVO().getAttributeValue("approveflagName")
						.equals("δ����")) {
					if (!eavo.getParentVO().getAttributeValue("approveflagName")
							.equals("UPPpublic-000621")) {
	
						// ��ȡ��ǰ��Ա������
						GeneralVO psnvo = eavo.getPsndocVO();
						
						IReadmsg msg = (IReadmsg) NCLocator.getInstance().lookup(IReadmsg.class);
						PsndocVO psndocvo = ((PsndocVO[])msg.getGeneralVOs(PsndocVO.class, " pk_psndoc = '"+eavo.getPk_psndoc()+"'"))[0];
						PsnbasdocVO psnbasdocvo = ((PsnbasdocVO[])msg.getGeneralVOs(PsnbasdocVO.class, " pk_psnbasdoc = '"+eavo.getPk_psnbasdoc()+"'"))[0];
						
						OperationMsg opmsg = new OperationMsg();
						opmsg.setOptime(Global.getClientEnvironment().getDate().toString());
						opmsg.setUnitcode(Global.getClientEnvironment().getCorporation().getUnitcode());
						opmsg.setUsercode(Global.getClientEnvironment().getUser().getUserCode());
						opmsg.setUsername(Global.getClientEnvironment().getUser().getUserName());
						
						String retStr = filepost.postFile(Uriread.uriPath() , 
								gener.generateXml4(psndocvo, "RequestPsndoc", "psn", "update" , opmsg));
						
						String[] strs = retStr.split("<success>");
						String retMsg = "";
						if (strs.length > 1)
							retMsg = strs[1].substring(0, strs[1].indexOf("<"));

						if (retMsg.equals("false") || strs.length <= 1) {
							PsndocUpdate.add(psndocvo);
							Thread th3 = new Thread() {
								public void run() {
									IGener gener = (IGener) NCLocator.getInstance().lookup(IGener.class.getName());
									IFilePost filepost = (IFilePost) NCLocator.getInstance().lookup(IFilePost.class.getName());
									OperationMsg opmsg = new OperationMsg();
									opmsg.setOptime(Global.getClientEnvironment().getDate().toString());
									opmsg.setUnitcode(Global.getClientEnvironment().getCorporation().getUnitcode());
									opmsg.setUsercode(Global.getClientEnvironment().getUser().getUserCode());
									opmsg.setUsername(Global.getClientEnvironment().getUser().getUserName());
									
									try {
										if(true) {
											this.sleep(SleepTime.Time);
										
											for(PsndocVO psn : PsndocUpdate) {
												String retStr = filepost.postFile(Uriread.uriPath() , 
														gener.generateXml4(psn, "RequestPsndoc", "psn", "add" , opmsg));
												
												String[] strs = retStr.split("<success>");
												String retMsg = "";
												if (strs.length > 1)
													retMsg = strs[1].substring(0, strs[1].indexOf("<"));
												
												if(retMsg.equals("false") || strs.length <= 1)
													PsndocUpdate2.add(psn);
											
											}
											
											PsndocUpdate = PsndocUpdate2;
											
											PsndocUpdate2 = new ArrayList<PsndocVO>();
											
//											if(PsndocUpdate.size() == 0)
//												break;
										}
										System.out.println("<<<<<<  ��Ա�����޸��߳�ֹͣ�� >>>>>>");
										System.out.println("<<<<<<  �̺߳�: " + this.getId() + " >>>>>>");
										this.stop();
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
							};
							
							th3.start();
						}
					
						retStr = filepost.postFile(Uriread.uriPath() , 
								gener.generateXml6(psnbasdocvo, "RequestPsnbasdoc", "psnbas", "update"));
						
						strs = retStr.split("<success>");
						retMsg = "";
						if (strs.length > 1)
							retMsg = strs[1].substring(0, strs[1].indexOf("<"));

						if (retMsg.equals("false") || strs.length <= 1) {
							PsnbasdocUpdate.add(psnbasdocvo);
							Thread th4 = new Thread() {
								public void run() {
									IGener gener = (IGener) NCLocator.getInstance().lookup(IGener.class.getName());
									IFilePost filepost = (IFilePost) NCLocator.getInstance().lookup(IFilePost.class.getName());
								
									
									try {
										if(true) {
											this.sleep(SleepTime.Time);
										
											for(PsnbasdocVO psnbas : PsnbasdocUpdate) {
												String retStr = filepost.postFile(Uriread.uriPath() , 
														gener.generateXml6(psnbas, "RequestPsnbasdoc", "psnbas", "add"));
												
												String[] strs = retStr.split("<success>");
												String retMsg = "";
												if (strs.length > 1)
													retMsg = strs[1].substring(0, strs[1].indexOf("<"));
												
												if(retMsg.equals("false") || strs.length <= 1)
													PsnbasdocUpdate2.add(psnbas);
											
											}
											
											PsnbasdocUpdate = PsnbasdocUpdate2;
											
											PsnbasdocUpdate2 = new ArrayList<PsnbasdocVO>();
											
//											if(PsnbasdocUpdate.size() == 0)
//												break;
										}
										
										System.out.println("<<<<<<  ��Ա���������޸��߳�ֹͣ�� >>>>>>");
										System.out.println("<<<<<<  �̺߳�: " + this.getId() + " >>>>>>");
										this.stop();
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
							};
							
							th4.start();
						}
						
					} 
				}
			} catch(Exception ex) {
				ex.printStackTrace();
			}

		} else {
			if (isEditSub()) {
				saveChilds(eavo, tableCodes[0]);
				setEditSub(false);
				vDelPkPsndocSub.clear();
				//
				vDelSubVOs.clear();
				// ���¼�������
				loadPsnChildInfo(listSelectRow, tableCodes[0]);
			} else {
				saveChild(eavo, tableCodes[0]);
			}
			setRefBillItemDefaultValue(tableCodes[0]);
		}
		persons.clear();
	}
	/**
	 * ����������ؼ���Ĭ��ѡ�񣬱�֤�ܹ�ִ��itemstatchange�¼��� �������ڣ�(2004-5-21 8:43:21)
	 * 
	 * @param tableCode
	 *            java.lang.String ��Ϣ������
	 */
	private void setRefBillItemDefaultValue(String tableCode) {
		int ieditline = getEditLineNo();
		setEditLineNo(-1);
		BillItem[] items = getCard().getBillData().getBodyItemsForTable(
				tableCode);
		if (items != null && items.length > 0) {
			for (int i = 0; i < items.length; i++) {
				if (items[i].getDataType() == IBillItem.COMBO) {
					UIComboBox box = (UIComboBox) items[i].getComponent();
					box.setSelectedIndex(-1);
				}
			}
		}
		setEditLineNo(ieditline);
	}

	/**
	 * �����Ӽ������� 
	 * �������ڣ�(2004-5-21 8:43:21)
	 * @param eavo nc.vo.hi.hi_301.PersonEAVO
	 * @param tableCode java.lang.String
	 * @exception java.lang.Exception �쳣˵����
	 */
	private void saveChild(PersonEAVO eavo, String tableCode)throws java.lang.Exception {
		// ��ȡ��ǰ�Ӽ�����
		CircularlyAccessibleValueObject[] records = eavo.getTableVO(tableCode);
		String pk_psnbasdoc = eavo.getPk_psnbasdoc();
		int row = getEditLineNo();
		GeneralVO vo = (GeneralVO) records[row];
		vo.setAttributeValue("pk_psnbasdoc", pk_psnbasdoc);
		// �Ͷ���ͬģ����빫˾����ֵ
		if (tableCode.equalsIgnoreCase("hi_psndoc_ctrt")) {
			vo.setAttributeValue("pk_corp", getClientEnvironment().getCorporation().getUnitname());
			vo.setAttributeValue(Util.PK_PREFIX + "pk_corp",nc.ui.hr.global.Global.getCorpPK());
			vo.setAttributeValue("isrefer", new UFBoolean("Y"));
			vo.setAttributeValue("pk_locusedcorp", nc.ui.hr.global.Global.getCorpPK());		
			
			//[NCdp202946765]:shien ����Ա��Ϣ�ڵ��޸���Ա��ͬ��Ϣ����ͬ���嵥λʱ���޸ı����
			//����һ���������ٷ������鿴ʱ�����ݶ�ʧ����Ϊ���ڵ�λ����鿴����������
			//vo.setAttributeValue("pk_majorcorp", getClientEnvironment().getCorporation().getUnitname());
			//vo.setAttributeValue(Util.PK_PREFIX + "pk_majorcorp",nc.ui.hr.global.Global.getCorpPK());
			if(vo.getAttributeValue("pk_majorcorp")== null){
				vo.setAttributeValue("pk_majorcorp", getClientEnvironment().getCorporation().getUnitname());
				vo.setAttributeValue(Util.PK_PREFIX + "pk_majorcorp",nc.ui.hr.global.Global.getCorpPK());
			}
			vo.setAttributeValue("pk_user", Global.getUserID());
			vo.setAttributeValue("operatedate", Global.getLogDate());
		}
		//v50 add
		if (getBodyTableCode().equalsIgnoreCase("hi_psndoc_dimission")
				||"hi_psndoc_retire".equalsIgnoreCase(getCurrentSetCode())
				||"hi_psndoc_training".equalsIgnoreCase(getCurrentSetCode())
				||"hi_psndoc_orgpsn".equalsIgnoreCase(getCurrentSetCode())
				||"hi_psndoc_psnchg".equalsIgnoreCase(getCurrentSetCode())
				||"hi_psndoc_keypsn".equalsIgnoreCase(getCurrentSetCode())) {
			vo.setAttributeValue("pk_corp", getClientEnvironment().getCorporation().getUnitname());
			vo.setAttributeValue(Util.PK_PREFIX + "pk_corp",nc.ui.hr.global.Global.getCorpPK());
		}
		if ("hi_psndoc_ass".equalsIgnoreCase(getCurrentSetCode())) {
			vo.setAttributeValue("pk_corpperson", getClientEnvironment().getCorporation().getUnitname());
			vo.setAttributeValue(Util.PK_PREFIX + "pk_corpperson",nc.ui.hr.global.Global.getCorpPK());
			Object o = vo.getAttributeValue(Util.PK_PREFIX + "pk_corpassess");
			if (o == null) {
				vo.setAttributeValue("pk_corpassess", getClientEnvironment().getCorporation().getUnitname());
				vo.setAttributeValue(Util.PK_PREFIX + "pk_corpassess",nc.ui.hr.global.Global.getCorpPK());
				vo.setAttributeValue("pk_corp",nc.ui.hr.global.Global.getCorpPK());
			}else{
				vo.setAttributeValue("pk_corp",(String)o);
			}
		}
		if (tableCode.equalsIgnoreCase("hi_psndoc_part")) {
			Object objJobtype = vo.getAttributeValue(Util.PK_PREFIX + "jobtype");
			Integer jobtype = null;
			if (jobtype instanceof Integer) {
				jobtype = (Integer) objJobtype;
			}
			if (jobtype != null) {
				vo.setJobTypeFlag(jobtype);
			}
		}
		// ��ְ����Ӽ�д�빫˾����
		if (tableCode.equalsIgnoreCase("hi_psndoc_deptchg")) {
			vo.setAttributeValue("pk_corp", getClientEnvironment().getCorporation().getUnitname());
			vo.setAttributeValue(Util.PK_PREFIX + "pk_corp",nc.ui.hr.global.Global.getCorpPK());
			if (((Integer) vo.getAttributeValue("recordnum")).intValue() == 0) {
				for (int i = 0; i < records.length; i++) {
					GeneralVO vo2 = (GeneralVO) records[i];
					if (((Integer) vo2.getAttributeValue("recordnum")).intValue() == 1) {
						if (vo2.getAttributeValue("enddate") == null|| "".equals(vo2.getAttributeValue("enddate").
								toString().trim())) {
							nc.vo.pub.lang.UFDate enddate = ((nc.vo.pub.lang.UFDate) vo.getAttributeValue("begindate"))
							.getDateBefore(1);
							vo2.setAttributeValue("enddate", enddate);
							getCard().getBodyPanel().getTableModel().setValueAt(enddate, i, "enddate");
						}
						break;
					}
				}
			} else if (((Integer) vo.getAttributeValue("recordnum")).intValue() > 0
					&& (vo.getAttributeValue("enddate") == null || "".equals(vo.getAttributeValue("enddate").
							toString().trim()))) {// ��������ʱ���ѵ�ǰ�е���һ����¼�Ŀ�ʼ���ڵ�ǰһ����Ϊ��ǰ�еĽ�������
				for (int i = 0; i < records.length; i++) {
					GeneralVO vo1 = (GeneralVO) records[i];
					if (((Integer) vo1.getAttributeValue("recordnum")).intValue() + 1 == ((Integer) vo
							.getAttributeValue("recordnum")).intValue()) {
						nc.vo.pub.lang.UFDate enddate = ((nc.vo.pub.lang.UFDate) vo1
								.getAttributeValue("begindate")).getDateBefore(1);
						vo.setAttributeValue("enddate", enddate);
						getCard().getBodyPanel().getTableModel().setValueAt(enddate, row, "enddate");
						break;
					}
				}
			}
		}
		if (vo.getAttributeValue("pk_psndoc_sub") == null) {
			// V35 add У���ֶ�ֵ���ظ�
			if (!PubDelegator.getISetdict().checkRecordUnique(tableCode,
					(String) vo.getAttributeValue("pk_psnbasdoc"),
					getUniqueFld(tableCode), vo, null, "pk_psndoc_sub", 0)) {
				String pk_psndoc_sub = HIDelegator.getPsnInf().insertChild(tableCode, person.getPk_psndoc(), vo);
				records[row].setAttributeValue("pk_psndoc_sub", pk_psndoc_sub);
			} else {
				FlddictVO[] fldvos = getUniqueFld(tableCode);			
				String msg = getCard().getBillData().getBodyTableName(getCurrentSetCode())
				+ nc.ui.ml.NCLangRes.getInstance().getStrByID("600704","UPP600704-000197")/* @res "�Ӽ�" */;
				for (int i = 0; i < fldvos.length; i++) {
					msg += fldvos[i].getFldname();
					if (i < fldvos.length - 1) {
						msg += ",";
					}
				}
				msg += ","+ nc.ui.ml.NCLangRes.getInstance().getStrByID("600704","UPP600704-000196")/* @res "�����Ϣ�������ظ�" */;
				throw new Exception(msg);
			}
		} else {
			// V35 add У���ֶ�ֵ���ظ�
			if (!PubDelegator.getISetdict().checkRecordUnique(tableCode,
					(String) vo.getAttributeValue("pk_psnbasdoc"),
					getUniqueFld(tableCode), vo,
					(String) vo.getAttributeValue("pk_psndoc_sub"),
					"pk_psndoc_sub", 1)) {
				HIDelegator.getPsnInf().updateChild(tableCode,person.getPk_psndoc(), vo);
			} else {
				FlddictVO[] fldvos = getUniqueFld(tableCode);
				String msg = getCard().getBillData().getBodyTableName(getCurrentSetCode())
				+ nc.ui.ml.NCLangRes.getInstance().getStrByID("600704","UPP600704-000197")/* @res "�Ӽ�" */;
				for (int i = 0; i < fldvos.length; i++) {
					msg += fldvos[i].getFldname();
					if (i < fldvos.length - 1) {
						msg += ",";
					}
				}
				msg += ","+ nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
				"UPP600704-000196")/* @res "�����Ϣ�������ظ�" */;
				throw new Exception(msg);
			}
		}
		person = eavo;// wangkf add
		if (records.length > 0) {
			boolean updated = isUpdateAccRel(vo.getAttributeNames(), tableCode);
			if (updated) {
				GeneralVO voTemp = new GeneralVO();
				voTemp.setAttributeValue("pk_psnbasdoc", person.getPk_psnbasdoc());
				voTemp.setAttributeValue("pk_psndoc", person.getPk_psndoc());
				GeneralVO relVO = HIDelegator.getPsnInf().updateAccRel(
						vo.getAttributeNames(), getRelationMap(),
						tableCode, voTemp);
				HashMap hmRelVO = new HashMap();
				String[] fields = relVO.getAttributeNames();
				GeneralVO psnvo = new GeneralVO();
				GeneralVO baspsnvo = new GeneralVO();
				psnvo.setAttributeValue("pk_psnbasdoc", person.getPk_psnbasdoc());
				psnvo.setAttributeValue("pk_psndoc", person.getPk_psndoc());
				baspsnvo.setAttributeValue("pk_psnbasdoc", person.getPk_psnbasdoc());
				baspsnvo.setAttributeValue("pk_psndoc", person.getPk_psndoc());
				boolean isRelPsndoc = false;
				boolean isRelBasPsndoc = false;
				for (int i = 0; i < fields.length; i++) {
					if (fields[i].indexOf(".") < 0) {
						continue;
					}
					String table = fields[i].substring(0, fields[i].indexOf("."));
					if ("bd_psndoc".equalsIgnoreCase(table)) {
						String field = fields[i].substring(fields[i].indexOf(".") + 1);
						psnvo.setAttributeValue(field, relVO.getAttributeValue(fields[i]));
						person.getPsndocVO().setAttributeValue(field,relVO.getAttributeValue(fields[i]));
						isRelPsndoc = true;
					} else if ("bd_psnbasdoc".equalsIgnoreCase(table)) {
						String field = fields[i].substring(fields[i].indexOf(".") + 1);
						baspsnvo.setAttributeValue(field, relVO.getAttributeValue(fields[i]));
						person.getAccpsndocVO().setAttributeValue(field,relVO.getAttributeValue(fields[i]));
						isRelBasPsndoc = true;
					}
				}
				if (isRelPsndoc)
					hmRelVO.put("bd_psndoc", psnvo);
				if (isRelBasPsndoc)
					hmRelVO.put("bd_psnbasdoc", baspsnvo);
				HIDelegator.getPsnInf().updateRelTable(hmRelVO);
				//���½�������
				if(isRelPsndoc){
					String[] field = psnvo.getAttributeNames();
					for(int i=0;i<field.length;i++){
						if("pk_psnbasdoc".equals(field[i])||"pk_psndoc".equals(field[i])){
							continue;
						}
						BillItem item = getCard().getHeadItem(field[i]);
						if(item!=null){
							Object value = psnvo.getFieldValue(field[i]);
							if(value==null||"#null#".equals(value.toString())){
								item.setValue(null);
								value = null;
							}else{
								item.setValue(value);
							}
							psnList[listSelectRow].setAttributeValue(field[i],value);
						}
					}
				}
				if(isRelBasPsndoc){
					String[] field = baspsnvo.getAttributeNames();
					for(int i=0;i<field.length;i++){
						if("pk_psnbasdoc".equals(field[i])||"pk_psndoc".equals(field[i])){
							continue;
						}
						BillItem item = getCard().getHeadItem(field[i]);
						if(item!=null){
							Object value = baspsnvo.getFieldValue(field[i]);
							if(value==null||"#null#".equals(value.toString())){
								item.setValue(null);
								value = null;
							}else{
								item.setValue(value);
							}
							psnList[listSelectRow].setAttributeValue(field[i],value);
						}
					}
				}
			}
			isRelateToAcc = updated;
		}
		subBackupVOs = null;
	}
	
	/**
	 * У���ֶ������ظ� by dusx
	 * @param tableCode
	 * @param records
	 * @throws java.lang.Exception
	 */
	private void checkReduplicateField(String tableCode, CircularlyAccessibleValueObject[] records) throws java.lang.Exception {
		FlddictVO[] flddictVOs =getUniqueFld(tableCode);
		if (flddictVOs == null || flddictVOs.length<1||records==null||records.length<1) return;
		for(int i=0;i<flddictVOs.length;i++){
			String fieldcode = flddictVOs[i].getFldcode();
			String fieldname = flddictVOs[i].getFldname();
			HashMap<Object,Integer> values = new HashMap<Object,Integer>();
			values.clear();
			for(int row=0;row<records.length;row++){
				Object value = records[row].getAttributeValue(fieldcode);
				if ("".equals(value)||value==null) continue;
				if (flddictVOs[i].getDatatype()==5){//������false
					if(value instanceof Boolean){
						if(value.equals(new Boolean(false))) continue;
					}
					if(value instanceof UFBoolean){
						if(value.equals(new UFBoolean(false))) continue;
					}
				}
				if (values.containsKey(value)){
					int row1 = values.get(value) ;
//					throw new BusinessException("�ֶΡ�"+fieldname+"���ڵ�"+(row1+1)+"�к͵�"+(row+1)+"�г����ظ������޸ģ�");
					throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID("600700", "UPP600700-000123")/* @res "�ֶΡ�" */
							+ fieldname 
							+ nc.ui.ml.NCLangRes.getInstance().getStrByID("600700", "UPP600700-000124")/* @res "���ڵ�" */
							+ (row1+1) 
							+ nc.ui.ml.NCLangRes.getInstance().getStrByID("600700", "UPP600700-000125")/* @res "�к͵�" */ 
							+ (row+1)
							+ nc.ui.ml.NCLangRes.getInstance().getStrByID("600700", 
									"UPP600700-000126")/* @res "�г����ظ������޸ģ�" */);
				}
				values.put(value, row);
			}
		}
	}
	/**
	 * ��������Ӽ������� 
	 * �������ڣ�(2004-5-21 8:43:21)
	 * @param eavo nc.vo.hi.hi_301.PersonEAVO
	 * @param tableCode java.lang.String
	 * @exception java.lang.Exception �쳣˵����
	 */
	private void saveChilds(PersonEAVO eavo, String tableCode)throws java.lang.Exception {
		// ��ȡ��ǰ�Ӽ�����
		CircularlyAccessibleValueObject[] records = eavo.getTableVO(tableCode);
		GeneralVO[] vos = new GeneralVO[records.length];
		String pk_psnbasdoc = eavo.getPk_psnbasdoc();
		
		//У���Ӽ��в����ظ��ֶ��Ƿ����ظ�
		checkReduplicateField(tableCode,records);
		
		for (int i = 0; i < records.length; i++) {
			GeneralVO vo = (GeneralVO) records[i];
			vo.setAttributeValue("pk_psnbasdoc", pk_psnbasdoc);
			// �Ͷ���ͬģ����빫˾����ֵ
			if (tableCode.equalsIgnoreCase("hi_psndoc_ctrt")) {
				vo.setAttributeValue("pk_corp", getClientEnvironment()
						.getCorporation().getUnitname());
				vo.setAttributeValue(Util.PK_PREFIX + "pk_corp",
						nc.ui.hr.global.Global.getCorpPK());
				vo.setAttributeValue("isrefer", new UFBoolean("Y"));
				vo.setAttributeValue("pk_locusedcorp", nc.ui.hr.global.Global
						.getCorpPK());
				vo.setAttributeValue("pk_majorcorp", getClientEnvironment()
						.getCorporation().getUnitname());
				vo.setAttributeValue(Util.PK_PREFIX + "pk_majorcorp",
						nc.ui.hr.global.Global.getCorpPK());
				vo.setAttributeValue("pk_majorcorp", nc.ui.hr.global.Global
						.getCorpPK());
				vo.setAttributeValue("pk_user", Global.getUserID());
				vo.setAttributeValue("operatedate", Global.getLogDate());
			}
			// v50 add
			if ( "hi_psndoc_retire".equalsIgnoreCase(getCurrentSetCode())
					|| "hi_psndoc_training"
					.equalsIgnoreCase(getCurrentSetCode())
					|| "hi_psndoc_orgpsn".equalsIgnoreCase(getCurrentSetCode())					
					|| "hi_psndoc_keypsn".equalsIgnoreCase(getCurrentSetCode())) {
				vo.setAttributeValue("pk_corp", getClientEnvironment()
						.getCorporation().getUnitname());
				vo.setAttributeValue(Util.PK_PREFIX + "pk_corp",
						nc.ui.hr.global.Global.getCorpPK());
			}
			if ("hi_psndoc_ass".equalsIgnoreCase(getCurrentSetCode())) {
				vo.setAttributeValue("pk_corpperson", getClientEnvironment().getCorporation().getUnitname());
				vo.setAttributeValue(Util.PK_PREFIX + "pk_corpperson",
						nc.ui.hr.global.Global.getCorpPK());
				Object o = vo.getAttributeValue(Util.PK_PREFIX
						+ "pk_corpassess");
				if (o == null) {
					vo.setAttributeValue("pk_corpassess",
							nc.ui.hr.global.Global.getCorpPK());
					vo.setAttributeValue("pk_corp", nc.ui.hr.global.Global
							.getCorpPK());
				} else {
					vo.setAttributeValue("pk_corp", (String) o);
				}
			}
			if (vo.getAttributeValue("pk_psndoc_sub") == null) {
				vo.setStatus(nc.vo.pub.VOStatus.NEW);
			} else {
				vo.setStatus(nc.vo.pub.VOStatus.UPDATED);
			}
			vos[i] = vo;
		}
		// ȡ��ɾ���Ӽ���¼��Ϣ
		String[] delPkPsndocSubs = null;
		if (vDelPkPsndocSub.size() > 0) {
			delPkPsndocSubs = new String[vDelPkPsndocSub.size()];
			vDelPkPsndocSub.copyInto(delPkPsndocSubs);
		}
		// ���������Ӽ���¼��Ϣ
		HIDelegator.getPsnInf().saveSubSetInfos(tableCode,
				person.getPk_psndoc(), vos, delPkPsndocSubs);
		person = eavo;

		//Ҫ�ӵ���ģ����ȡ���ֶ����顣2009-4-24 dusx 
		BillItem[] items = getCard().getBillModel().getBodyItems();
		String[] fieldnames = new String[items.length] ;
		for(int i=0;i<items.length;i++){
			fieldnames[i] = items[i].getKey();
		}

		if (records.length > 0 || vDelSubVOs.size()>0) {
			boolean updated = isUpdateAccRel(fieldnames, tableCode);
			if (updated) {
				GeneralVO voTemp = new GeneralVO();
				voTemp.setAttributeValue("pk_psnbasdoc", person.getPk_psnbasdoc());
				voTemp.setAttributeValue("pk_psndoc", person.getPk_psndoc());
				GeneralVO relVO = HIDelegator.getPsnInf().updateAccRel(
						fieldnames, getRelationMap(),
						tableCode, voTemp);
				HashMap hmRelVO = new HashMap();
				String[] fields = relVO.getAttributeNames();
				GeneralVO psnvo = new GeneralVO();
				GeneralVO baspsnvo = new GeneralVO();
				psnvo.setAttributeValue("pk_psnbasdoc", person.getPk_psnbasdoc());
				psnvo.setAttributeValue("pk_psndoc", person.getPk_psndoc());
				baspsnvo.setAttributeValue("pk_psnbasdoc", person.getPk_psnbasdoc());
				baspsnvo.setAttributeValue("pk_psndoc", person.getPk_psndoc());
				boolean isRelPsndoc = false;
				boolean isRelBasPsndoc = false;
				for (int i = 0; i < fields.length; i++) {
					if (fields[i].indexOf(".") < 0) {
						continue;
					}
					String table = fields[i].substring(0, fields[i].indexOf("."));
					if ("bd_psndoc".equalsIgnoreCase(table)) {
						String field = fields[i].substring(fields[i].indexOf(".") + 1);
						psnvo.setAttributeValue(field, relVO.getAttributeValue(fields[i]));
						person.getPsndocVO().setAttributeValue(field,relVO.getAttributeValue(fields[i]));
						isRelPsndoc = true;
					} else if ("bd_psnbasdoc".equalsIgnoreCase(table)) {
						String field = fields[i].substring(fields[i].indexOf(".") + 1);
						baspsnvo.setAttributeValue(field, relVO.getAttributeValue(fields[i]));
						person.getAccpsndocVO().setAttributeValue(field,relVO.getAttributeValue(fields[i]));
						isRelBasPsndoc = true;
					}
				}
				if (isRelPsndoc)
					hmRelVO.put("bd_psndoc", psnvo);
				if (isRelBasPsndoc)
					hmRelVO.put("bd_psnbasdoc", baspsnvo);
				HIDelegator.getPsnInf().updateRelTable(hmRelVO);
				//���½�������
				if(isRelPsndoc){
					String[] field = psnvo.getAttributeNames();
					for(int i=0;i<field.length;i++){
						if("pk_psnbasdoc".equals(field[i])||"pk_psndoc".equals(field[i])){
							continue;
						}
						BillItem item = getCard().getHeadItem(field[i]);
						if(item!=null){
							Object value = psnvo.getFieldValue(field[i]);
							if(value==null||"#null#".equals(value.toString())){
								item.setValue(null);
								value = null;
							}else{
								item.setValue(value);
							}
							psnList[listSelectRow].setAttributeValue(field[i],value);
						}
					}
				}
				if(isRelBasPsndoc){
					String[] field = baspsnvo.getAttributeNames();
					for (int i = 0; i < field.length; i++) {
						if ("pk_psnbasdoc".equals(field[i])
								|| "pk_psndoc".equals(field[i])) {
							continue;
						}
						BillItem item = getCard().getHeadItem(field[i]);
						if (item != null) {
							Object value = baspsnvo.getFieldValue(field[i]);
							if (value == null
									|| "#null#".equals(value.toString())) {
								item.setValue(null);
								value = null;
							} else {
								item.setValue(value);
							}
							psnList[listSelectRow].setAttributeValue(field[i],
									value);
						}
					}
				}
			}
			isRelateToAcc = updated;
		}
		subBackupVOs = null;
	}
	/**
	 * ����������Ա��Ϣ�� �������ڣ�(2004-5-21 8:41:14)
	 * 
	 * @param eavo
	 *            nc.vo.hi.hi_301.PersonEAVO
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	private void saveHiRef(PersonEAVO eavo) throws java.lang.Exception {
		if (getIsNeedAFirm() ){//���浽hi_psndoc_ref
			eavo.getPsndocVO().setAttributeValue("pk_corp",Global.getCorpPK());
			eavo.getPsndocVO().setAttributeValue("indocflag",new nc.vo.pub.lang.UFBoolean('Y'));
			eavo.getPsndocVO().setAttributeValue("userid",Global.getUserID());
			eavo.getPsndocVO().setAttributeValue("appdate", Global.getLogDate());
			eavo.getPsndocVO().setAttributeValue("psnclscope", new Integer(5));
			eavo.getPsndocVO().setAttributeValue("tbm_prop", new Integer(2));
			eavo.getPsndocVO().setAttributeValue("pk_psnbasdoc",eavo.getPk_psnbasdoc());
			eavo.getPsndocVO().setAttributeValue("oripk_psndoc",eavo.getPk_psndoc());
			String[] fieldNames = eavo.getPsndocVO().getAttributeNames();
			for (int i = 0; i < fieldNames.length; i++) {
				//fengwei on 2010-09-13 ������Աʱ�������޸��Զ�������ҿ��Ա���
				if (/*fieldNames[i].indexOf("def") > 0 ||*/ fieldNames[i].startsWith("$")) {
					eavo.getPsndocVO().removeAttributeName(fieldNames[i]);
				}
			}
			eavo.getPsndocVO().removeAttributeName("isreferenced");

		}else{//���浽bd_psndoc

			eavo.getPsndocVO().setAttributeValue("pk_corp",Global.getCorpPK());
			eavo.getPsndocVO().setAttributeValue("indocflag",new nc.vo.pub.lang.UFBoolean('Y'));
			eavo.getPsndocVO().setAttributeValue("psnclscope", new Integer(5));
			eavo.getPsndocVO().setAttributeValue("tbm_prop", new Integer(2));
			eavo.getPsndocVO().setAttributeValue("isreferenced", "Y");
			eavo.getPsndocVO().setAttributeValue("pk_psnbasdoc",eavo.getPk_psnbasdoc());
			String[] fieldNames = eavo.getPsndocVO().getAttributeNames();
			for (int i = 0; i < fieldNames.length; i++) {
				//fengwei on 2010-09-13 ������Աʱ�������޸��Զ�������ҿ��Ա���
				if (/*fieldNames[i].indexOf("def") > 0
						||*/ fieldNames[i].startsWith("$")
						|| fieldNames[i].startsWith("ori")) {
					eavo.getPsndocVO().removeAttributeName(fieldNames[i]);
				}
			}

		}
		String pk_psndoc = HIDelegator.getPsnInf().insertHiRef(eavo.getPsndocVO(),getIsNeedAFirm());
		eavo.getPsndocVO().setAttributeValue("pk_psndoc", pk_psndoc);


	}

	/**
	 * ���������� 
	 * �������ڣ�(2004-5-21 8:41:14)
	 * @param eavo nc.vo.hi.hi_301.PersonEAVO
	 * @exception java.lang.Exception   �쳣˵����
	 */
	private void saveMain(PersonEAVO eavo) throws java.lang.Exception {
		// �༭����״̬
		if (isAdding()) {
			addPerson(eavo);
			BillItem item = getBillItem("bd_psnbasdoc.belong_pk_corp");
			if (item != null) {
				item.setValue(Global.getCorpPK());
			}
		} else {
			eavo.getPsndocVO().setAttributeValue("jobtypeflag",new Integer(eavo.getJobType()));
			// ���µ�ǰ��Ա����Ϣ
			String pk_psnbasdoc = eavo.getPk_psnbasdoc();
			eavo.getAccpsndocVO().setAttributeValue("pk_psnbasdoc",pk_psnbasdoc);
			// ������˾
			String belong_pk_corp = (String) psnList[listSelectRow]
			                                         .getAttributeValue("belong_pk_corp");			
			Item item = (Item) getCmbPsncl().getSelectedItem();
			if(!Global.getCorpPK().equalsIgnoreCase(belong_pk_corp)&&item != null && item.getValue() == 5){//������Ա�޸���Ҫ����н�ʽӿ� v50
				GeneralVO accpsndocVO = eavo.getAccpsndocVO();
				accpsndocVO.setAttributeValue("curret_pk_corp", Global.getCorpPK());
				HIDelegator.getPsnInf().updateRefPersonMain(eavo.getPsndocVO(), accpsndocVO, null,Global.getLogDate());
			}else{
				HIDelegator.getPsnInf().updateMain(eavo.getPsndocVO(), eavo.getAccpsndocVO(), null);
			}
			// ���µ�ǰ��Ա������������Ϣ
			person.setPsndocVO(eavo.getPsndocVO());
			person.setAccpsndocVO(eavo.getAccpsndocVO());
			// ��õ�ǰ���༭��Ա������
			String pk_psndoc = (String) eavo.getPsndocVO().getAttributeValue("pk_psndoc");
			Vector v = new Vector();
			// ˢ�µ�ǰ��Ա��Ϣ
			ConditionVO cond = new ConditionVO();
			cond.setFieldCode("bd_psndoc.pk_psndoc");
			cond.setOperaCode("=");
			cond.setValue(pk_psndoc);
			v.addElement(cond);
			if ("600707".equalsIgnoreCase(getModuleCode())) {
				ConditionVO cond1 = new ConditionVO();
				cond1.setFieldCode("bd_psndoc.indocflag");
				cond1.setOperaCode("=");
				cond1.setValue("Y");
				v.addElement(cond1);
				//
				ConditionVO cond2 = new ConditionVO();
				cond2.setFieldCode("hi_psndoc_deptchg.jobtype");
				cond2.setOperaCode("=");
				cond2.setDataType(CommonValue.DATATYPE_INTEGER);
				cond2.setValue("" + person.getJobType() + "");
				v.addElement(cond2);
			} else {
				ConditionVO cond1 = new ConditionVO();
				cond1.setFieldCode("bd_psndoc.indocflag");
				cond1.setOperaCode("=");
				cond1.setValue("N");
				v.addElement(cond1);
			}

			ConditionVO[] conds = new ConditionVO[v.size()];
			v.copyInto(conds);
//			GeneralVO[] gvos = HIDelegator.getPsnInf().queryByCondition(
//			Global.getCorpPK(), conds, powerSql, getListField(),wheresql);
			GeneralVO[] gvos = HIDelegator.getPsnInf().queryByCondition(
					Global.getCorpPK(), conds, powerSql, getListField()," and (1=1) ");
			final String[] keys = { "deptcode", "deptname", "jobtypeflag",
					"psnclassname", "pk_om_job", "jobname", "pk_deptdoc",
					"pk_psncl", "pk_om_duty", "jobrank", "jobseries", "series" };
			int i = findPersonInQueryResult(pk_psndoc);
			if (i != -1 && (gvos != null && gvos.length > 0)) {
				if (eavo.getJobType() > 0) { // ����ʽ��Ա���滻Ϊԭ����
					for (int j = 0; j < keys.length; j++) {
						Object value = queryResult[j]
						                           .getAttributeValue(keys[j]);
						gvos[0].setAttributeValue(keys[j], value);
					}
				}
				queryResult[i] = gvos[0];
				// ������壬����ѡ��
				psnDeptCache.clear();
				psnCorpCache.clear();
				person = eavo;// wangkf add
				listSelectRow = findPersonPos(pk_psndoc, "pk_psndoc", psnList);
				if (person != null|| person.getSubtables().get("hi_psndoc_deptchg") != null) {
					person.getSubtables().remove("hi_psndoc_deptchg");
					bodyTabbedPane_stateChanged(null);
				}
			}
		}
	}

	/**
	 * ȫ���ű�������Ľ���� 
	 * @throws java.lang.Exception
	 */
	protected void saveShowOrder() throws java.lang.Exception {
		//��ʧȥ����ò�������
		TableCellEditor editor = getPsnList().getTable().getCellEditor();
		if (editor != null) {
			editor.stopCellEditing();
		}
		// ��ȡ��ǰ��������Ľ��
		GeneralVO[] sortPsnList = (GeneralVO[]) getPsnList().getTableModel().getBodyValueVOs("nc.vo.hi.hi_301.GeneralVO");
		GeneralVO[] oldPsnList = (GeneralVO[]) getPsnList().getBodyData();
		HashMap	psnhash = new HashMap();
		// ��ȡ������Ա������
		String[] pk_psndocs = new String[sortPsnList.length];
		for (int i = 0; i < pk_psndocs.length; i++){
			pk_psndocs[i] = (String) oldPsnList[i].getAttributeValue("pk_psndoc");
			if(sortPsnList[i].getAttributeValue("showorder")!=null && sortPsnList[i].getAttributeValue("showorder")!=""){
				if(((Integer)sortPsnList[i].getAttributeValue("showorder")).intValue()<0){
					throw new BusinessException(nc.ui.ml.NCLangRes.getInstance().getStrByID("600704","UPT600704-000274")/*"��Ų���С��0"*/);
				}
				if(!sortPsnList[i].getAttributeValue("showorder").equals(oldPsnList[i].getAttributeValue("showorder"))){
					psnhash.put(pk_psndocs[i], sortPsnList[i].getAttributeValue("showorder"));
				}

				oldPsnList[i].setAttributeValue("showorder", sortPsnList[i].getAttributeValue("showorder"));
			}else{
				psnhash.put(pk_psndocs[i], new Integer(999999));//���Ϊ�������ó�Ĭ��ֵ(������ʾ��)
				oldPsnList[i].setAttributeValue("showorder", "");
			}
		}
		HIDelegator.getPsnInf().updateShoworder(pk_psndocs,psnhash);

	}


	/**
	 * �˴����뷽�������� �������ڣ�(2004-8-16 16:22:15)
	 */
	private void sequenceFire(String tableCode,CircularlyAccessibleValueObject record) {
		 /** Ч���Ż� BATCH START */
		/*����ʹ������ȡ���գ�ҳ���߼��������⣬ֻ�øĻ�ԭ����*/
//        BatchMatchContext.getShareInstance().setInBatchMatch(true);
//        BatchMatchContext.getShareInstance().clear();
//        try {
        	String[] seqFields = (String[]) triggerTable.fieldAOE.get(tableCode);
        	if (seqFields == null)
        		return;
        	for (int i = 0; i < seqFields.length; i++) {
        		BillItem item = getBillItem(tableCode + "." + seqFields[i]);
        		if (item == null)
        			continue;
        		if (item.getDataType() == IBillItem.UFREF) {
        			// ����ǲ������ͣ�����Ҫ����
        			// �����������������ֵʱ���Ѿ���������������UIRefPane����ֵʱ�����Զ������¼���ɵ�
        			Object v = null;
        			String key = null;
        			if (item.getPos() == BillItem.BODY) {
        				key = Util.PK_PREFIX + item.getKey();
        				v = record.getAttributeValue(Util.PK_PREFIX + item.getKey());
        			} else {
        				key = item.getKey();
        				v = record.getAttributeValue(item.getKey());
        			}
        			if (v == null)
        				continue;

        			((UIRefPane) item.getComponent()).setPK(v);
//        			((UIRefPane) item.getComponent()).setValue(v.toString());
        			((UIRefPane) item.getComponent()).repaint();
        			record.setAttributeValue(key, v);
        		}
        	}
//        	///***BATCH END***/
//        	BatchMatchContext.getShareInstance().executeBatch();
//        }finally{
//        	BatchMatchContext.getShareInstance().setInBatchMatch(false);
//
//        }
	}

	/**
	 * ���õ������а�ťʱ���ð�ť״̬ 
	 * �������ڣ�(2004-5-26 21:26:36)
	 */
	protected void setAddChildButtonState() {
		// ��ǰѡ��ҳǩ�Ƿ���׷����Ϣ��
		if (isNotMultiEdit()) {
			bnAddChild.setEnabled(false);
			bnUpdateChild.setEnabled(false);
			bnInsertChild.setEnabled(false);
			bnDelChild.setEnabled(false);
		} else {
			bnAddChild.setEnabled(true);
			bnUpdateChild.setEnabled(true);
			bnInsertChild.setEnabled(true);
			bnDelChild.setEnabled(true);
		}
		// ���ð�ť״̬
		bnAdd.setEnabled(false);
		bnEdit.setEnabled(false);
		bnDel.setEnabled(false);
		bnList.setEnabled(false);
		bnSave.setEnabled(true);
		bnCancel.setEnabled(true);
		bnUpload.setEnabled(false);
		bnUpRecord.setEnabled(false);
		bnDownRecord.setEnabled(false);
		bnToFirst.setEnabled(false);
		bnToLast.setEnabled(false);
		bnSubsetMove.setEnabled(false);
		// ���°�ť״̬
		updateButtons();
	}

	/**
	 * ���ñ༭״̬Ϊ������ 
	 * �������ڣ�(2004-5-14 15:52:50)
	 * @param newAdding boolean
	 */
	private void setAdding(boolean newAdding) {
		adding = newAdding;
	}

	protected void setHeadBillItemEditable(String table, boolean isEnabled) {
		BillItem[] items = getCard().getHeadShowItems(table);
		if (items == null || items.length < 1)
			return;
		// �����ͷ�༭״̬
		for (int i = 0; i < items.length; i++) {
			if (htbEdit.get(items[i].getKey()) == null) {
				htbEdit.put(items[i].getKey(), new Boolean(items[i]
				                                                 .isEdit()));
			}
		}
		// �����Ƿ���Ա༭
		if(getEditType() == EDIT_REF||isRefPerson){
			for (int i = 0; i < items.length; i++) {
				items[i].setEdit(isEnabled);
				items[i].setEnabled(isEnabled);
			}
		}else{			
			for (int i = 0; i < items.length; i++) {
				// ��������Ϣ��ɱ༭
				if (items[i].getKey().indexOf(CommonValue.UFFORMULA_DATA) >= 0) {
					items[i].setEdit(false);
					items[i].setEnabled(false);
				} else {
					Object obj = htbEdit.get(items[i].getKey());
					if (obj != null) {
						items[i].setEdit(((Boolean) obj).booleanValue());
						items[i].setEnabled(((Boolean) obj).booleanValue());
					} else {
						items[i].setEdit(false);
						items[i].setEnabled(false);
					}
				}
			}
		}
	}

	protected boolean isRefPerson = false;// �Ƿ����õ���Ա

	/**
	 * ����������Ŀ�Ĳ�ͬ״̬������Ŀ�Ŀɱ༭�ԡ� 
	 * �������ڣ�(2004-9-30 10:27:18)
	 */
	protected void setBillItemEditable() {
		Item psnclitem = (Item) getCmbPsncl().getSelectedItem();
		if (psnclitem != null && psnclitem.getValue() == 5) {
			// ���ñ�ǲ��ɱ༭
			setBillItemEnabled("bd_psndoc.isreferenced",false);
			setBillItemEdit("bd_psndoc.isreferenced",false);
			// ������ڲ��ɱ༭
			// ���ñ�ǲ��ɱ༭
			setBillItemEnabled("bd_psndoc.sealdate",false);
			setBillItemEdit("bd_psndoc.sealdate",false);
		}
		// ֻ��������Ա���ñ�־Ϊtrueʱ�����⴦��
		if (isRefPerson) {
			setHeadItemEnableInRef();
			return;
		} else {
			setHeadItemEnable();
		}
		// -��Ա�ɼ��Զ�����--- begin
		if (isPsncodeAutoGenerate()) {
			getBillItem("bd_psndoc.psncode").setEdit(false);
		} else {
			//dusx �޸ģ����ֶ�ͬʱӦ�ð��յ���ģ���ϵĿɱ༭���Դ��� 20009-4-29
			//getBillItem("bd_psndoc.psncode").setEdit(true);// true
			getBillItem("bd_psndoc.psncode").setEdit(psncodeCanEdit);
		}
		// -��Ա�ɼ��Զ�����----end	
		//���ɼ��ڵ��⿼�ڿ��Ų��ܱ༭
		if (!getModuleName().equals("600704")) {
			setBillItemEdit("bd_psndoc.timecardid",false);
		}
		if (!getModuleName().equals("600704")) {
			getBillItem("bd_psndoc.pk_psncl").setEdit(false);
			getBillItem("bd_psndoc.pk_deptdoc").setEdit(false);
			getBillItem("bd_psndoc.pk_om_job").setEdit(false);
		} else {
			getBillItem("bd_psndoc.pk_psncl").setEdit(true);
			getBillItem("bd_psndoc.pk_deptdoc").setEdit(true);
			getBillItem("bd_psndoc.pk_om_job").setEdit(true);
		}			
		//ת�����ڿؼ�
		getBillItem("bd_psndoc.regular").setEdit(getBillItem("bd_psndoc.regular").isEdit());
		UICheckBox checkBoxregular = (UICheckBox) getBillItem("bd_psndoc.regular").getComponent();
		// ����ת�����ڿؼ����ɱ༭
		if (checkBoxregular.isSelected()){
			Object obj = htbRegularEdit.get("bd_psndoc.regulardata");
			if (obj != null) {
				getBillItem("bd_psndoc.regulardata").setEnabled(((Boolean)obj).booleanValue());
				getBillItem("bd_psndoc.regulardata").setEdit(((Boolean)obj).booleanValue());
			} else {
				getBillItem("bd_psndoc.regulardata").setEnabled(false);
				getBillItem("bd_psndoc.regulardata").setEdit(false);
			}
		}
		else{
			getBillItem("bd_psndoc.regulardata").setEnabled(false);
			getBillItem("bd_psndoc.regulardata").setEdit(false);
		}


		UICheckBox checkBox = (UICheckBox) getBillItem("bd_psndoc.clerkflag").getComponent();
		// ����ְԱ����ؼ����ɱ༭
		if (checkBox.isSelected()){
			getBillItem("bd_psndoc.clerkcode").setEnabled(true);
			getBillItem("bd_psndoc.clerkcode").setEdit(true);
		}
		else{
			getBillItem("bd_psndoc.clerkcode").setEnabled(false);
			getBillItem("bd_psndoc.clerkcode").setEdit(false);
		}
		// ��λ���У���λ�ȼ���ְ��
		UIRefPane job = (UIRefPane) (getBillItem("bd_psndoc.pk_om_job").getComponent());
		String pk_job = job.getRefPK();
		if (pk_job == null || pk_job.length() != 20) {
			if(getModuleName().equals("600704")){
				getBillItem("bd_psndoc.jobrank").setEnabled(getBillItem("bd_psndoc.jobrank").isEdit());// true
				getBillItem("bd_psndoc.jobseries").setEdit(false);
				getBillItem("bd_psndoc.dutyname").setEnabled(getBillItem("bd_psndoc.dutyname").isEdit());// true
				getBillItem("bd_psndoc.series").setEnabled(getBillItem("bd_psndoc.series").isEdit());// true
			}else{
				getBillItem("bd_psndoc.jobrank").setEdit(false);// true
				getBillItem("bd_psndoc.jobseries").setEdit(false);
				getBillItem("bd_psndoc.dutyname").setEdit(false);// true
				getBillItem("bd_psndoc.series").setEdit(false);// true
			}
			UIRefPane dutynameRef = (UIRefPane) (getBillItem("bd_psndoc.dutyname").getComponent());
			String pkduty = dutynameRef.getRefPK();
			if (nc.ui.hi.hi_301.Util.isDutyDependJobSeries) {
				if (pkduty != null && pkduty.trim().length() == 20) {
					getBillItem("bd_psndoc.jobseries").setEnabled(false);
					getBillItem("bd_psndoc.jobseries").setEdit(false);
				} else {
					if(getModuleName().equals("600704")){
						getBillItem("bd_psndoc.jobseries").setEnabled(getBillItem("bd_psndoc.jobseries").isEdit());// true
						getBillItem("bd_psndoc.jobseries").setEdit(getBillItem("bd_psndoc.jobseries").isEdit());
					}else{
						getBillItem("bd_psndoc.jobseries").setEnabled(false);// true
						getBillItem("bd_psndoc.jobseries").setEdit(false);
					}
				}
			}
		} else {
			getBillItem("bd_psndoc.jobrank").setEnabled(false);
			getBillItem("bd_psndoc.jobrank").setEdit(false);
			getBillItem("bd_psndoc.jobseries").setEdit(false);
			String pk_duty = (String) job.getRefValue("om_job.pk_om_duty");
			if (pk_duty != null && pk_duty.trim().length() == 20) {
				getBillItem("bd_psndoc.dutyname").setEnabled(false);
				getBillItem("bd_psndoc.series").setEnabled(false);
				getBillItem("bd_psndoc.dutyname").setEdit(false);
				getBillItem("bd_psndoc.series").setEdit(false);
			} else {
				if (getModuleName().equals("600704")) {
					getBillItem("bd_psndoc.dutyname").setEnabled(getBillItem("bd_psndoc.dutyname").isEdit());
					getBillItem("bd_psndoc.dutyname").setEdit(getBillItem("bd_psndoc.dutyname").isEdit());
					UIRefPane dutynameRef = (UIRefPane) (getBillItem("bd_psndoc.dutyname")
							.getComponent());
					String pkduty = dutynameRef.getRefPK();
					if (pkduty != null && pkduty.trim().length() == 20) {
						getBillItem("bd_psndoc.series").setEnabled(false);
						getBillItem("bd_psndoc.series").setEdit(false);
					} else {
						getBillItem("bd_psndoc.series").setEnabled(
								getBillItem("bd_psndoc.series").isEdit());// true
					}
				}else{
					getBillItem("bd_psndoc.dutyname").setEnabled(false);
					getBillItem("bd_psndoc.series").setEnabled(false);
					getBillItem("bd_psndoc.dutyname").setEdit(false);
					getBillItem("bd_psndoc.series").setEdit(false);
				}
			}
		}
		if (!curTempCorpPk.equalsIgnoreCase(Global.getCorpPK())|| isPartPsnCurCorp) {
			// wangkf fixed isPartPsnCurCorp ->!isPartPsnCurCorp
			getBillItem("bd_psndoc.jobrank").setEnabled(false);
			getBillItem("bd_psndoc.jobseries").setEnabled(false);
			getBillItem("bd_psndoc.dutyname").setEnabled(false);
			getBillItem("bd_psndoc.series").setEnabled(false);			
			setBillItemEnabled("bd_psndoc.timecardid",false);
		}
	}
	/**
	 */
	public void setBillItemEnabled(String itemkey, boolean isEnabled) {
		if (getBillItem(itemkey) != null)
			getBillItem(itemkey).setEnabled(isEnabled);
	}
	/**
	 */
	public void setBillItemEdit(String itemkey, boolean isEdit) {
		if (getBillItem(itemkey) != null)
			getBillItem(itemkey).setEdit(isEdit);
	}

	/**
	 * ���ݽ���״̬�趨��ť��״̬�� ��ͬ��ĳ�ֽ���״̬�£������Ʋ�ͬ�����ڵ���Ҫ�в�ͬ�İ�ť״̬ʱ��
	 * ���Կ�����setBtnStatWithBuss�ķ����ٲ���һ�� 
	 * �������ڣ�(2003-3-27 20:09:48)
	 * @param stat int
	 */
	public void setBtnsStatWithUIStat(int stat) {
	}

	/**
	 * �˴����뷽�������� 
	 * �������ڣ�(2004-6-1 8:46:01)
	 */
	protected void setButtons() {
		// �ɼ�״̬
		setButtons(grpCollect);
	}

	/**
	 * ����ȡ���༭����ʱ��ť��״̬�� 
	 * �������ڣ�(2004-5-26 21:53:32)
	 */
	protected void setCancelEditMainButtonState() {
		// ����ȡ���༭����ʱ��״̬
		bnAdd.setEnabled(true);
		bnEdit.setEnabled(true);
		bnDel.setEnabled(true);
		bnChildEdit.setEnabled(true);
		bnList.setEnabled(true);
		bnSave.setEnabled(false);
		bnCancel.setEnabled(false);
		bnUpload.setEnabled(true);
		bnChildOp.setEnabled(!isAdding());
		bnReturn.setEnabled(true);
		// ���°�ť״̬
		updateButtons();
	}

	/**
	 * �����༭�С������к�ɾ�����Ƿ���ã��жϹ�����: ����Ǳ༭״̬�������Ƕ����ã� 
	 * ����ֻ�е�ǰ�����ô���1�ſ��ã�
	 * ��Ϊȡ����Ҫɾ���������С�
	 * �������ڣ�(2004-5-26 21:48:07)
	 */
	private void setCancelEditSubButtonState() {

		// �����༭�С������к�ɾ�����Ƿ���ã��жϹ�����:
		// ����Ǳ༭״̬�������Ƕ����ã�
		// ����ֻ�е�ǰ�����ô���1�ſ��ã���Ϊȡ����Ҫɾ���������С�
		boolean enabled = isEditLine() ? true : (getCard().getBillTable().getRowCount() > 1);
		// ���ð�ť״̬
		bnAddChild.setEnabled(true);
		bnUpdateChild.setEnabled(enabled);
		bnInsertChild.setEnabled(enabled);
		bnDelChild.setEnabled(enabled);
		bnSave.setEnabled(false);
		bnCancel.setEnabled(false);
		bnReturn.setEnabled(true);
		bnUpload.setEnabled(true);
		// ���°�ť״̬
		updateButtons();
	}

	/**
	 * ����ȡ�����������İ�ť״̬�� 
	 * �������ڣ�(2004-5-27 9:21:58)
	 */
	protected void setCancelSortButtonState() {
		// ���ð�ť״̬
		bnSave.setEnabled(false);
		bnCancel.setEnabled(false);
		// ���°�ť
		updateButtons();
	}

	/**
	 * ����ǵ����Ӽ���������ļ������İ�ť v50 add
	 */
	protected void expendChildEditButons() {
		bnChildEdit.addChileButtons(grpChild);
	}

	/**
	 * �����Ӳ�����ť��״̬�� �������ڣ�(2004-5-25 15:15:08)
	 */
	protected void setChildButtonState() {
		// ��ǰѡ���к�����
		int row = getCard().getBillTable().getSelectedRow();
		int count = getCard().getBillTable().getRowCount();
		// ��ǰѡ��ҳǩ�Ƿ���׷����Ϣ��
		boolean traceable = isBodyTraceTable();
		// �����Ƿ���׷����Ϣ�����������а�ť״̬
		String indoc = "N";
		boolean bemoved = false;
		boolean isreturn = false;
		if (person != null) {			
			if (listSelectRow >= 0) {
				Object o = psnList[listSelectRow].getAttributeValue("indocflag");
				if (o != null) {
					isreturn = indoc.equals(o) ? false : true;					
				}
			}			
			bemoved = true;
		}
		//v50 add
		boolean move = Canmoved();
		bnSubsetMove.setEnabled(!move&&bemoved);
		if (isreturn) {
			bnAddChild.setEnabled(!traceable);
			if (row != -1 && count > 0 && row < count) {
				// ѡ����һ��
				bnUpdateChild.setEnabled(!traceable);
				bnDelChild.setEnabled(!traceable);
				bnInsertChild.setEnabled(!traceable);
			} else {
				// û��ѡ��
				bnUpdateChild.setEnabled(false);
				bnDelChild.setEnabled(false);
				bnInsertChild.setEnabled(false);
			}
		}else{
			bnAddChild.setEnabled(true);
			if (row != -1 && count > 0 && row < count ) {
				// ѡ����һ��
				bnUpdateChild.setEnabled(true);
				bnDelChild.setEnabled(true);
				bnInsertChild.setEnabled(true);
			} else {
				// û��ѡ��
				bnUpdateChild.setEnabled(false);
				bnDelChild.setEnabled(false);
				bnInsertChild.setEnabled(false);
			}
		}
		updateButtons();
	}

	/**
	 * ���ò������ĸ���㡣 
	 * �������ڣ�(2004-5-10 8:41:00)
	 * @param newCtrlDeptRoot nc.vo.hi.hi_301.CtrlDeptVO
	 */
	protected void setCtrlDeptRoot(nc.vo.hi.hi_301.CtrlDeptVO newCtrlDeptRoot) {
		ctrlDeptRoot = newCtrlDeptRoot;
	}

	/**
	 * wangkf add ���� ��˾�Զ�����Ϊ ��
	 * @param infoVOs
	 * @param value
	 */
	protected GeneralVO[] setDefItem(GeneralVO[] infoVOs, String value) {
		if (infoVOs == null || infoVOs.length < 1) {
			return null;
		}
		String prefix = "corpdef";
		for (int i = 0; i < infoVOs.length; i++) {
			String[] fields = infoVOs[i].getAttributeNames();
			if (fields == null || fields.length < 1)
				break;

			for (int j = 0; j < fields.length; j++) {
				if (fields[j] == null)
					continue;
				String field = null;
				String fieldprefix = null;
				if (fields[j].indexOf(".") >= 0) {
					field = fields[j].substring(fields[j].indexOf(".") + 1);
				} else {
					field = fields[j];
				}
				if (field.length() > prefix.length()) {
					fieldprefix = field.substring(0, prefix.length());
				}
				if (prefix.equals(fieldprefix)) {
					infoVOs[i].setAttributeValue(field, value);
				}
			}
		}
		return infoVOs;
	}

	/**
	 * �˴����뷽�������� 
	 * �������ڣ�(2004-10-25 21:03:27)
	 * @param power java.lang.String
	 */
	public void setDeptPowerInit(String power) {
		deptPowerInit = power;
	}

	/**
	 * �����Ƿ����ڱ༭�С� 
	 * �������ڣ�(2004-5-12 14:51:25)
	 * @param newEditLine  boolean
	 */
	private void setEditLine(boolean newEditLine) {
		editLine = newEditLine;
	}

	/**
	 * �������ڱ༭���кš� 
	 * �������ڣ�(2004-5-12 14:51:25)
	 * @param newEditLineNo  int
	 */
	private void setEditLineNo(int newEditLineNo) {
		editLineNo = newEditLineNo;
	}

	/**
	 * ���ñ༭���͡� 
	 * �������ڣ�(2004-5-12 11:40:28)
	 * @param newEditType int
	 */
	protected void setEditType(int newEditType) {
		editType = newEditType;
	}

	/**
	 * ������Ա��ְ��Ϣ�ӱ��ĳЩ��Ŀɱ༭�ԡ�
	 */
	private void setHiDeptchgEditable(GeneralVO vo) {
		UIRefPane job = (UIRefPane) (getBillItem("hi_psndoc_deptchg.pk_postdoc").getComponent());
		String pk_job = (String) vo.getFieldValue("pk_postdoc");// Util.PK_PREFIX
		job.setPK(pk_job);
		if (pk_job == null || pk_job.length() != 20) {
			getBillItem("hi_psndoc_deptchg.pk_jobrank").getComponent().setEnabled(true);
			getBillItem("hi_psndoc_deptchg.pk_om_duty").getComponent().setEnabled(true);
			getBillItem("hi_psndoc_deptchg.pk_detytype").getComponent().setEnabled(true);
			UIRefPane dutynameRef = (UIRefPane) (getBillItem("hi_psndoc_deptchg.pk_om_duty").getComponent());
			String pkduty = dutynameRef.getRefPK();
			if (nc.ui.hi.hi_301.Util.isDutyDependJobSeries) {
				if (pkduty != null && pkduty.trim().length() == 20) {
					getBillItem("hi_psndoc_deptchg.pk_jobserial").getComponent().setEnabled(false);
				} else {
					getBillItem("hi_psndoc_deptchg.pk_jobserial").getComponent().setEnabled(true);
				}
			} else {
				getBillItem("hi_psndoc_deptchg.pk_jobserial").getComponent().setEnabled(true);
			}
		} else {
			getBillItem("hi_psndoc_deptchg.pk_jobrank").getComponent().setEnabled(false);
			getBillItem("hi_psndoc_deptchg.pk_jobserial").getComponent().setEnabled(false);
			String pk_duty = (String) job.getRefValue("om_job.pk_om_duty");
			if (pk_duty != null && pk_duty.trim().length() == 20) {
				getBillItem("hi_psndoc_deptchg.pk_om_duty").getComponent().setEnabled(false);
				getBillItem("hi_psndoc_deptchg.pk_detytype").getComponent().setEnabled(false);
			} else {
				getBillItem("hi_psndoc_deptchg.pk_om_duty").getComponent().setEnabled(true);
				UIRefPane dutynameRef = (UIRefPane) (getBillItem("hi_psndoc_deptchg.pk_om_duty").getComponent());
				String pkduty = dutynameRef.getRefPK();
				if (pkduty != null && pkduty.trim().length() == 20) {
					getBillItem("hi_psndoc_deptchg.pk_detytype").getComponent().setEnabled(false);
				} else {
					getBillItem("hi_psndoc_deptchg.pk_detytype").getComponent().setEnabled(true);
				}
			}
		}
		getCard().updateUI();
	}

	/**
	 * ������Ա�ؼ���Ա��Ϣ�ӱ��ĳЩ��Ŀɱ༭�ԡ�
	 */
	protected void setHiKeyPsnEditable(GeneralVO vo) {
		Object enddate = vo.getAttributeValue("enddate");
		if (enddate != null) {
			getBillItem("hi_psndoc_keypsn.begindate").getComponent().setEnabled(false);
			getBillItem("hi_psndoc_keypsn.enddate").getComponent().setEnabled(false);
			getBillItem("hi_psndoc_keypsn.pk_keypsn_group").getComponent().setEnabled(false);
			getBillItem("hi_psndoc_keypsn.pk_corp").getComponent().setEnabled(false);			
		} else {
			getBillItem("hi_psndoc_keypsn.begindate").getComponent().setEnabled(true);
			getBillItem("hi_psndoc_keypsn.enddate").getComponent().setEnabled(true);
			getBillItem("hi_psndoc_keypsn.pk_keypsn_group").getComponent().setEnabled(true);
			getBillItem("hi_psndoc_keypsn.pk_corp").getComponent().setEnabled(true);			
		}
		getCard().updateUI();
	}

	/**
	 * ����ѡ�в����к�İ�ť״̬�� 
	 * �������ڣ�(2004-5-27 9:09:29)
	 */
	protected void setInsertChildButtonState() {

		// ����ѡ�в����к�İ�ť״̬��
		if (isNotMultiEdit()) {
			bnAddChild.setEnabled(false);
			bnUpdateChild.setEnabled(false);
			bnInsertChild.setEnabled(false);
			bnDelChild.setEnabled(false);
		} else {
			bnAddChild.setEnabled(true);
			bnUpdateChild.setEnabled(true);
			bnInsertChild.setEnabled(true);
			bnDelChild.setEnabled(true);
		}
		bnAdd.setEnabled(false);
		bnEdit.setEnabled(false);
		bnDel.setEnabled(false);
		bnList.setEnabled(false);
		bnSave.setEnabled(true);
		bnCancel.setEnabled(true);
		bnReturn.setEnabled(false);
		bnUpload.setEnabled(false);
		bnUpRecord.setEnabled(false);
		bnDownRecord.setEnabled(false);
		bnToFirst.setEnabled(false);
		bnToLast.setEnabled(false);
		bnSubsetMove.setEnabled(false);//v50
		// ���°�ť״̬
		updateButtons();
	}
	/**
	 * ���ö��б༭�Ӽ���ť״̬ �������ڣ�(2004-5-27 9:09:29)
	 */
	protected void setMultEditChildButtonState() {
		// ��ǰѡ���к�����
		int row = getCard().getBillTable().getSelectedRow();
		int count = getCard().getBillTable().getRowCount();
		// ����ѡ�в����к�İ�ť״̬��
		bnAddChild.setEnabled(true);
		if (row != -1 && count > 0 && row < count) {
			bnUpdateChild.setEnabled(true);
			bnInsertChild.setEnabled(true);
			bnDelChild.setEnabled(true);
		} else {
			bnUpdateChild.setEnabled(true);
			bnInsertChild.setEnabled(false);
			bnDelChild.setEnabled(true);
		}
		//
		bnAdd.setEnabled(false);
		bnEdit.setEnabled(false);
		bnDel.setEnabled(false);
		bnUpload.setEnabled(false);
		bnList.setEnabled(false);
		bnSave.setEnabled(true);
		bnCancel.setEnabled(true);
		bnReturn.setEnabled(false);
		bnUpRecord.setEnabled(false);
		bnDownRecord.setEnabled(false);
		bnToFirst.setEnabled(false);
		bnToLast.setEnabled(false);
		bnSubsetMove.setEnabled(false);// v50
		// ���°�ť״̬
		updateButtons();
	}

	/**
	 * ������Ա��Ϣ��ʾ�б����Ϣ� �������ڣ�(2004-5-18 10:55:48)
	 * 
	 * @param newListItems
	 *            java.lang.String[]
	 */
	private void setListItems(Pair[] newListItems) {
		listItems = newListItems;
	}

	/**
	 * �˴����뷽�������� 
	 * �������ڣ�(2004-6-1 10:28:30)
	 */
	protected void setMaintainMarkBeforeValidation(PersonEAVO eavo) {
	}

	/**
	 * ���ݵ�ǰ����״̬�趨����ռ�ı༭״̬�� 
	 * �������ڣ�(2003-3-27 20:08:38)
	 * @param stat  int
	 */
	public void setPanelStat(int stat) {
	}

	/**
	 * ����ǰѡ����Ա���������ڴ��ڱ����ϡ� 
	 * �������ڣ�(2004-6-5 10:06:35)
	 */
	private void setPsnNameOnBottom() {
		if (psnList == null)
			return;
		int row = getPsnList().getTable().getSelectedRow();
		int rowCount = getPsnList().getTable().getRowCount();
		if (rowCount == 0)
			return;
		if (row < 0 || row >= rowCount || row >= psnList.length)
			return;

		// ȷ��ɾ��
		GeneralVO psn = ((GeneralVO[]) getPsnList().getBodyData())[row];
		String psnname = nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
		"UPP600704-000090")/* @res "��ǰѡ����Ա�ǣ�" */
		+ psn.getAttributeValue("psnname");
		showHintMessage(psnname);
	}

	/**
	 * ���õ�row��1���Ƿ���Ա༭��editable=true:���Ա༭��editable=false�����Ա༭ wangkf add
	 * @param row
	 * @param editable
	 */
	private void setRowState(BillModel model, int row, boolean editable) {
		// ѭ������
		int count = model.getColumnCount();
		// ��ǰ��ı���
		String tableCode = getBodyTableCode();
		// ��ǰ��ı༭��
		BillItem[] billItems = model.getBodyItems();
		for (int i = 0; i < count; i++) {
			// ��ǰ�ֶα���
			String key = billItems[i].getKey();
			// �Ƿ���Ա༭
			boolean isEdit = isEditable(tableCode, key);
			if (getBodyTableCode().equalsIgnoreCase("hi_psndoc_ctrt")) {
				if (row == 0 && key.equalsIgnoreCase("iconttype")) {
					// ����˳���󣬵�һ�к�ͬ���Ͳ��ܱ༭
					isEdit = false;
				}
			} else if (getBodyTableCode()
					.equalsIgnoreCase("hi_psndoc_training")) {
				if (key.equalsIgnoreCase("tra_mode_name")) {
					isEdit = false;
				}
			}
			// ���ñ༭״̬
			boolean newEditable = (editable ? isEdit : false);
			getCard().setCellEditable(row, key, newEditable);
			getCard().getBodyPanel().repaint();
			getCard().updateUI();
		}

	}

	/**
	 * ���ñ�������������İ�ť״̬�� 
	 * �������ڣ�(2004-5-27 9:21:58)
	 */
	protected void setSaveMainButtonState() {
		// ���ð�ť״̬
		bnAdd.setEnabled(true);
		bnEdit.setEnabled(true);
		bnSave.setEnabled(false);
		bnCancel.setEnabled(false);
		bnChildOp.setEnabled(true);
		bnReturn.setEnabled(true);
		// ���°�ť
		updateButtons();
	}

	/**
	 * ���ñ������������İ�ť״̬�� 
	 * �������ڣ�(2004-5-27 9:21:58)
	 */
	protected void setSaveSortBottonState() {
		// ���ð�ť״̬
		bnSave.setEnabled(false);
		bnCancel.setEnabled(false);
		// ���°�ť
		updateButtons();
		// ��������в��ɱ༭
		getPsnList().getTableModel().getItemByKey("showorder").setEdit(false);
		getPsnList().getTableModel().getItemByKey("showorder").setEnabled(false);
	}

	/**
	 * ���ò���ֵΪ�ա� 
	 * �������ڣ�(2004-8-2 15:49:40)
	 * @param tableCode java.lang.String
	 */
	private void setSelRowRefValueNull(String tableCode) {
		BillItem[] items = getCard().getBillModel().getBodyItems();
		for (int i = 0; i < items.length; i++) {
			if (items[i].getDataType() == 5) { // ��������
				UIRefPane ref = (UIRefPane) items[i].getComponent();
				ref.setPK("");
				ref.setName("");
			}
		}
	}

	/**
	 * ����row�б༭״̬�����row��-1����ʾ�������е��еı༭״̬�� 
	 * �������ڣ�(2004-5-20 20:53:39)
	 * @param model nc.ui.pub.bill.BillModel Ҫ���õı��billModel
	 * @param row int Ҫ���õ��У����Ϊ-1����ʾ�������е���
	 * @param editable boolean �Ƿ�ɱ༭��״̬
	 */
	private void setTableLineEditable(BillModel model, int row, boolean editable) {
		if (row == -1) {
			// Ϊ-1ʱ�ݹ���ã����������е�״̬
			int rowCount = getCard().getBillTable().getRowCount();
			for (int i = 0; i < rowCount; i++) {
				setRowState(model, i, editable);
			}
		} else {
			setRowState(model, row, editable);
		}
	}

	/**
	 * �������ڵ�ѡ��ʱ��ť״̬�� 
	 * �������ڣ�(2004-5-14 9:49:28)
	 */
	protected void setTreeSelectButtonState(DefaultMutableTreeNode node) {
		// ��ǰѡ�еĲ���
		CtrlDeptVO dept = (CtrlDeptVO) node.getUserObject();
		// �޸ġ�ɾ������Ƭ����������û�
		bnEdit.setEnabled(false);
		bnDel.setEnabled(false);
		bnCard.setEnabled(false);
		bnUpload.setEnabled(false);
		bnSmUser.setEnabled(false);
		bnQuery.setEnabled(true);
		// ����Ǹ����
		if (node.isRoot()) {
			bnEdit.setEnabled(false);
			bnAdd.setEnabled(false);
			bnSort.setEnabled(false);
		} else if (dept.isControlled()) {
			if (Global.getCorpPK().equals(dept.getPk_corp())) {
				// ����ǿɿ��Ʋ��Žڵ�
				if (listSelectRow != -1)
					bnEdit.setEnabled(true);
				bnAdd.setEnabled(true);
			} else {
				bnEdit.setEnabled(false);
				bnAdd.setEnabled(false);
				bnSort.setEnabled(false);
			}
		} else {
			// ���ɿ��Ʋ��Žڵ�
			bnEdit.setEnabled(false);
			bnAdd.setEnabled(false);
			bnSort.setEnabled(false);
		}
		if (node.isRoot() || dept.isControlled()) {
			// �������߿ɿز��Ű�ť
			if (psnList != null && psnList.length > 0) {
				if (Global.getCorpPK().equals(dept.getPk_corp())) {
					if (listSelectRow != -1)
						bnEdit.setEnabled(true);
					bnBatch.setEnabled(true);
					bnIntoDoc.setEnabled(true);
					bnBatchAddSet.setEnabled(getBatchAddSetButtonState());
				} else {
					bnEdit.setEnabled(false);
					bnBatch.setEnabled(false);
					bnIntoDoc.setEnabled(false);
					bnBatchAddSet.setEnabled(false);
					bnSort.setEnabled(false);
				}
				// ��Ա������0ʱ
				bnBook.setEnabled(true);
				bnPrint.setEnabled(true);

			} else {
				bnEdit.setEnabled(false);
				// ��Ա������0ʱ
				bnBatch.setEnabled(false);
				bnBatchAddSet.setEnabled(false);
				bnBook.setEnabled(false);
				bnPrint.setEnabled(false);
				bnIntoDoc.setEnabled(false);
				bnSort.setEnabled(false);
			}
		}
		if (node.isRoot()) {
			bnBook.setEnabled(false);
		}
		if ("600707".equals(getModuleName())||"600710".equals(getModuleName())||"600708".equals(getModuleName())) {
			if (listSelectRow == -1) {// ���û��ѡ����Ա������ѡ����Ƭ����
				bnExportPic.setEnabled(false);
			} else {
				bnExportPic.setEnabled(true);
			}
		}
		// ���°�ť״̬
		updateButtons();
	}

	protected boolean getBatchAddSetButtonState() {
		String bodytablecode = getWorkCard().getCurrentBodyTableCode();
		boolean isEnabled = false;
		if (psnList != null && psnList.length > 0) {
			if(getCard().getTraceTables()!=null){
				if (getCard().getTraceTables().get(bodytablecode) != null) {// getBodyTableCode()
					isEnabled = false;
				} else {
					isEnabled = true;
				}
			}
		}
		if(bodytablecode.equalsIgnoreCase("hi_psndoc_keypsn")){
			isEnabled = false;
		}
		return isEnabled;
	}

	protected boolean recordNotNullValidate(String tableCode, GeneralVO vo,
			int curRow) throws Exception {
		BillItem[] items = getCard().getBillModel().getBodyItems();
		String key = "";
		for (int i = 0; i < items.length; i++) {
			if (items[i].isShow()) {
				if (vo.getAttributeValue(items[i].getKey()) == null) {
					vo.removeAttributeName(Util.PK_PREFIX + items[i].getKey());
				}
				if (vo.getFieldValue(items[i].getKey()) != null) {
					return true;
				} else if (key == null) {
					key = items[i].getKey();
				}
			}
		}
//		String message = "���ܱ�����У�";
		String message = nc.ui.ml.NCLangRes.getInstance().getStrByID("600700", "UPP600700-000127")/* @res "���ܱ�����У�" */;
		ValidException vException = new ValidException(message);
		vException.setTableCode(tableCode);
		vException.setFieldCode(key);
		vException.setLineNo(curRow);// -1
		traceErrorPoint(vException);
		return false;

	}

	/**
	 * �����޸��в�����ť���º�İ�ť״̬�� �������ڣ�(2004-5-27 9:33:15)
	 */
	protected void setUpdateChildButtonState() {
		// ���ð�ť״̬
		bnAddChild.setEnabled(false);
		bnAdd.setEnabled(false);
		bnEdit.setEnabled(false);
		bnDel.setEnabled(false);
		bnList.setEnabled(false);
		bnAddChild.setEnabled(false);
		bnUpdateChild.setEnabled(false);
		bnInsertChild.setEnabled(false);
		bnDelChild.setEnabled(false);
		bnSave.setEnabled(true);
		bnCancel.setEnabled(true);
		bnUpload.setEnabled(false);
		bnUpRecord.setEnabled(false);
		bnDownRecord.setEnabled(false);
		bnToFirst.setEnabled(false);
		bnToLast.setEnabled(false);
		bnSubsetMove.setEnabled(false);//v50
		// ���°�ť״̬
		updateButtons();
	}

	/**
	 * Ϊ ����������� --wangkf add
	 * 
	 * @param refmodel
	 * @param whereSql
	 * @return
	 */
	public String setWhereToModel(nc.ui.bd.ref.AbstractRefModel refmodel,String whereSql) {

		String whereSqlTemp = refmodel.getWherePart();
		if (whereSqlTemp != null && whereSqlTemp.length() > 0
				&& whereSql != null && whereSql.length() > 0) {
			if (whereSqlTemp.indexOf(whereSql) < 0) {
				whereSqlTemp += " and " + whereSql;
			}
		} else {
			whereSqlTemp = whereSql;
		}
		refmodel.setWherePart(whereSqlTemp);

		if (refmodel instanceof SpaDeptdocRefTreeModel) {
			SpaDeptdocRefTreeModel refTreemodel = (SpaDeptdocRefTreeModel) refmodel;
			refTreemodel.setClassWherePart(whereSql);
		} else if (refmodel instanceof SpaDeptdocRefGridTreeModel) {
			SpaDeptdocRefGridTreeModel refTreemodel = (SpaDeptdocRefGridTreeModel) refmodel;
			refTreemodel.setClassWherePart(whereSql);
		}
		return whereSqlTemp;
	}

	/**
	 * �˴����뷽�������� 
	 * �������ڣ�(2004-6-2 17:12:11)
	 * @param name  java.lang.String
	 */
	protected void showPanel(String name) {
		// CardLayout layout = (CardLayout) getCardPanelDown().getLayout();
		// layout.show(getCardPanelDown(), name);
	}


	/**
	 * v50 add 
	 * ȫ�����ڽ���������Ӧ�¼���
	 * @throws java.lang.Exception
	 */
	private void EditShowOrder() throws java.lang.Exception {
		getPsnList().setBodyData(psnList);
		getPsnList().removeTableSortListener();
		getPsnList().getTableModel().execLoadFormula();
		showordervisible = getPsnList().getTableModel().getItemByKey("showorder").isShow();
		if (!showordervisible) getPsnList().showTableCol("showorder");//hideTableCol(strKey).getTableModel().getItemByKey("showorder").setShow(true);
		getPsnList().getTableModel().getItemByKey("showorder").setEdit(true);
		getPsnList().getTableModel().getItemByKey("showorder").setEnabled(true);
	}

	/**
	 * ��Ա��Ϣ�ӱ���ѡ���¼������� Called whenever the value of the selection changes.
	 * 
	 * @param e
	 *            the event that characterizes the change.
	 */
	private void subTable_valueChanged(javax.swing.event.ListSelectionEvent e) {
		if (getCard().getBodyTabbedPane().isEnabled()) {
			setChildButtonState();// ���Ǳ༭״̬�����ð�ť״̬
			updateButtons();
		}
	}

	/**
	 * �˴����뷽�������� �������ڣ�(2004-6-5 10:57:58)
	 * 
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	private void traceErrorPoint(ValidException ve) throws java.lang.Exception {
		// ���廯������Ϣλ��
		String tableCode = ve.getTableCode();
		String tableName = null;
		// �����涨λ�������
		if (getEditType() == EDIT_MAIN || getEditType() == EDIT_REF || getEditType() == EDIT_RETURN ) {
			tableName = getCard().getBillData().getHeadTableName(tableCode);
			getCard().getHeadTabbedPane().setSelectedIndex(tableCode.equals("bd_psndoc") ? 1 : 0);
		} else{
			tableName = getCard().getBillData().getBodyTableName(tableCode);
		}
		// �����㶨λ�����屨�����
		String fieldCode = ve.getFieldCode();
		if (fieldCode != null && !"id".equalsIgnoreCase(fieldCode)) {
			// �����BillItem
			BillItem errItem = getBillItem(tableCode + "." + fieldCode);
			if (errItem != null && errItem.getPos() == BillItem.HEAD) {

				// ��ͷ��������ж�λ������Ҫ��ý���
				Component errComponent = errItem.getComponent();

				// �����ַ������͵�����򣬷�ֹ��������ʱ����ʾ�������ظ���trigger
				errComponent.requestFocus();
				if (errComponent instanceof UIRefPane)
					// ��Ref��Ҫȫѡ
					((UIRefPane) errComponent).getUITextField().selectAll();
			}
		}

		// ������֯������Ϣ���Ա�ͳһ��ʾ
		String message = tableName;
		if (ve.getLineNo() != -1)
			message += nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPP600704-000091")/* @res "��" */
			+ (ve.getLineNo() + 1)
			+ nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
			"UPP600704-000092")/* @res "��" */;
		message += ":" + ve.getMessage();

		if(ve.getLevel() == ValidException.LEVEL_CANACCEPT){
			//���ǿ������̵��쳣���Լ������档����ʾ���ѡ�
			showWarningMessage(message);
		}else if(ve.getLevel() == ValidException.LEVEL_DECIDEBYUSER){
			if(showOkCancelMessage(ve.getMessage()+"\n"+nc.ui.ml.NCLangRes.getInstance().getStrByID("6007", "UPT6007-000237")/*�Ƿ������*/) ==UIDialog.ID_CANCEL) {
				throw new Exception("HAVEPOPMESSAGE"+message);//�����׳��쳣��Ϊ����ֹ�����к���Ĳ�������������ʾ��
			} 
		}else{
			// �����׳����д�����ʾ���쳣
			throw new Exception(message);
		}
	}

	/**
	 * �˴����뷽�������� �������ڣ�(2004-6-2 11:41:11)
	 * 
	 * @param infoVOs
	 *            nc.vo.hi.hi_301.GeneralVO[]
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	protected void transPkToName(String tableCode, GeneralVO[] infoVOs)
	throws java.lang.Exception {
		if (infoVOs == null || infoVOs.length < 1)
			return;

		for (int i = 0; i < infoVOs.length; i++) {
			transPkToName(tableCode, infoVOs[i]);
		}
	}

	/**
	 * �˴����뷽�������� �������ڣ�(2004-6-2 11:42:26)
	 * 
	 * @param infoVOs
	 *            nc.vo.hi.hi_301.GeneralVO
	 * @exception java.lang.Exception
	 *                �쳣˵����
	 */
	private void transPkToName(String tableCode, GeneralVO infoVO)
	throws java.lang.Exception {
		if (infoVO == null) {
			return;
		}
		String[] fieldCodes = infoVO.getAttributeNames();
		for (int i = 0; i < fieldCodes.length; i++) {
			BillItem item = getBillItem(tableCode + "." + fieldCodes[i]);
			if (item == null)
				continue;
			switch (item.getDataType()) {
			case BillItem.UFREF: {
				Object value = infoVO.getAttributeValue(fieldCodes[i]);
				UIRefPane ref = (UIRefPane) item.getComponent();

				String pk_corp = (String) infoVO.getFieldValue("pk_corp");
				if (ref != null && (pk_corp != null || "".equals(pk_corp))
						&& !tableCode.equalsIgnoreCase("hi_psndoc_ctrt") //v5.0 �Ӽ�����ʾpk_corp�������ٴ�����
						&& !tableCode.equalsIgnoreCase("hi_psndoc_dimission")
						&& !tableCode.equalsIgnoreCase("hi_psndoc_retire")
						&& !tableCode.equalsIgnoreCase("hi_psndoc_training")
						&& !tableCode.equalsIgnoreCase("hi_psndoc_orgpsn")
						&& !tableCode.equalsIgnoreCase("hi_psndoc_deptchg")
						&& !tableCode.equalsIgnoreCase("hi_psndoc_psnchg")
						&& !tableCode.equalsIgnoreCase("hi_psndoc_keypsn")
				) {
					ref.setPk_corp(pk_corp);
					ref.getRefModel().setPk_corp(pk_corp);
				}
				//ȡ�ò��յĿɱ༭��
				boolean isEnable = ref.getRefModel().isRefEnable();
				//���ò��ղ��ɱ༭���������ղ���������Ȩ�޿���;�����������
				ref.getRefModel().setisRefEnable(false);
				ref.setPK(value);
				String name = ref.getRefName();
				if (name == null) {
					name = Util.retrieveNameOf(tableCode + "." + fieldCodes[i],
							(String) value);
				}
				//�������ò��յĿɱ༭��
				ref.getRefModel().setisRefEnable(isEnable);		
				infoVO.setAttributeValue(fieldCodes[i], name);
				infoVO.setAttributeValue(Util.PK_PREFIX + fieldCodes[i], value);
			}
			break;
			case BillItem.COMBO: {
				String refType = item.getRefType();
				if (refType == null)
					continue;
				Object value = infoVO.getAttributeValue(fieldCodes[i]);
				if (value == null) {
					continue;
				}

				java.util.StringTokenizer token = new StringTokenizer(refType,
				",");
				while (token.hasMoreTokens()) {
					String temp = (String) token.nextElement();
					int index = temp.indexOf(":");
					if (index > 0) {
						if (value.toString().equals(temp.substring(0, index))) {
							infoVO.setAttributeValue(fieldCodes[i], temp
									.substring(index + 1));
							infoVO.setAttributeValue(Util.PK_PREFIX
									+ fieldCodes[i], value);
							break;
						}
					}
				}
			}
			break;
			default:
				break;
			}
		}
	}
	/**
	 * V35 ���� �������ش˷���
	 * 
	 * @param btnGroupType
	 */
	protected void switchButtonGroup(int btnGroupType) {
		try {
			if (btnGroupType == CARD_GROUP) {//��Ƭ����
				getIsSelSet().setVisible(false);
				getincludeCancleDept().setVisible(false);
				getincludeChildDeptPsn().setVisible(false);
				getDeptTree().setEnabled(false);
				getIncludeHisPersonGroup().setVisible(false);
				getIncludeHisPerson().setVisible(false);
				getUISplitPane().setDividerLocation(0);
				getUISplitPane().setEnabled(false);

				getUISplitPaneVertical().setDividerLocation(0);
				getUISplitPaneVertical().setEnabled(false);

				BillCardLayout layout = (BillCardLayout)getCard().getLayout();

//				����ֵΪ-1ʱ,ȡ�����,��ԭΪĬ����ʾ��ʽ
//				if(getEditType()==EDIT_MAIN){
//				getCard().setPosMaximized(BillItem.HEAD);
//				}else if(getEditType()==EDIT_SUB){
//				getCard().setPosMaximized(-1);
//				}else{
//				getCard().setPosMaximized(-1);
//				}
				getCard().setShowMenuBar(true);
				expendChildEditButons();
				setButtons(grpCardDisplayCollect);
				// modified by zhangdd, ��Ƭ����û��"��Ƭ"��ť�Ͳ�Ҫ������
//				bnCard.removeChildButton(bnBatchExport);
			} else if (btnGroupType == LIST_GROUP) {//�б����
				getIsSelSet().setVisible(true);
				getincludeCancleDept().setVisible(true);
				getincludeChildDeptPsn().setVisible(true);
				getDeptTree().setEnabled(true);
				getIncludeHisPersonGroup().setVisible(false);
				getIncludeHisPerson().setVisible(false);

				getUISplitPane().setDividerLocation(180);
				getUISplitPane().setDividerSize(4);
				getUISplitPane().setEnabled(true);

				getUISplitPaneVertical().setDividerLocation(splitLocation);
				getUISplitPaneVertical().setEnabled(true);
				getCard().setShowMenuBar(false);
				getCard().setPosMaximized(BillItem.BODY);				
				setButtons(grpCollect);// �б���
				// modified by zhangdd, ��Ƭ����û��"��Ƭ"��ť�Ͳ�Ҫ������
//				bnCard.addChildButton(bnBatchExport);
			} else if (btnGroupType == LIST_INIT_GROUP) {//�б�����ʼ״̬
				bnEmployeeReference.addChileButtons(grpEmployeeReference);
				setButtons(grpCollect);// �б���
				getIncludeHisPersonGroup().setVisible(false);
				getIncludeHisPerson().setVisible(false);

				getUISplitPaneVertical().setDividerLocation(SPLIT_MAX);
				getUISplitPaneVertical().setEnabled(true);
				boolean show = getCard().isShowMenuBar();
				getCard().setShowMenuBar(!show);
				getCard().setShowMenuBar(false);
				getCard().setPosMaximized(BillItem.BODY);
				setButtons(grpCollect);// �б���
			} 
			updateButtons();
		} catch (Exception e) {
			reportException(e);
			showErrorMessage(e.getMessage());
		}

	}

	/**
	 * V35 ����
	 * 
	 * @param btnState
	 */
	protected void setButtonsState(int btnState) {
		if (btnState == CARD_EDIT) {
			for (int i = 0; i < grpCardDisplayCollect.length; i++) {
				grpCardDisplayCollect[i].setEnabled(false);
			}
			bnSave.setEnabled(true);
			bnCancel.setEnabled(true);
			bnUpload.setEnabled(false);
			//
			isShowBillTemp = true;
		} else if (btnState == CARD_MAIN_BROWSE) {
			for (int i = 0; i < grpCardDisplayCollect.length; i++) {
				grpCardDisplayCollect[i].setEnabled(true);
			}
			bnSave.setEnabled(false);
			bnCancel.setEnabled(false);			
			setUpDownState();
			if (getEditType() == EDIT_MAIN || getEditType() == EDIT_RETURN) {
				if (isAdding()) {
					bnChildEdit.setEnabled(false);
					bnEdit.setEnabled(false);
					bnDel.setEnabled(false);
					bnUpload.setEnabled(false);
				} else {
					bnChildEdit.setEnabled(true);
					bnEdit.setEnabled(true);
					bnDel.setEnabled(true);
					bnUpload.setEnabled(true);
				}
			}
			setChildButtonState();
			if (person != null) {
				String belong_pk_corp = null;
				if (listSelectRow >= 0) {
					belong_pk_corp = (String) psnList[listSelectRow]
					                                  .getAttributeValue("belong_pk_corp");// ������˾
				}
				if (!Global.getCorpPK().equalsIgnoreCase(belong_pk_corp)) {					
					bnEdit.setEnabled(false);
					bnDel.setEnabled(false);
					bnChildEdit.setEnabled(false);
					bnSubsetMove.setEnabled(false);
					bnUpload.setEnabled(false);
				} else {
					bnEdit.setEnabled(true);
					bnDel.setEnabled(true);
					bnChildEdit.setEnabled(true);
					bnUpload.setEnabled(true);
				}
			}
			//
			isShowBillTemp = true;
		} else if (btnState == LIST_INIT) {
			for (int i = 0; i < grpCollect.length; i++) {
				grpCollect[i].setEnabled(true);
			}
			//
			isShowBillTemp = false;
		} else if (btnState == LIST_BROWSE) {
			if (listSelectRow != -1) {// ���ĳ����ʱ�԰�ť״̬�Ŀ���---begin--
				GeneralVO psn = ((GeneralVO[]) getPsnList().getBodyData())[listSelectRow];
				String belong_pk_corp = (String) psn
				.getAttributeValue("belong_pk_corp");// ������˾
				bnCard.setEnabled(true);
				// ��Ա�б����1���ܵ���˳��
				int count = getPsnList().getTable().getRowCount();
				if (count > 0) {
					bnSort.setEnabled(true);
				} else {
					bnSort.setEnabled(false);
				}
				if (!Global.getCorpPK().equalsIgnoreCase(belong_pk_corp)) {
					bnEdit.setEnabled(false);
					bnDel.setEnabled(false);
					bnChildEdit.setEnabled(false);
					bnBatchAddSet.setEnabled(false);
					bnSort.setEnabled(false);
					//bnUpload.setEnabled(false);
					//��ʹ�ǵ�¼��˾��Ҳ���Բ鿴������dusx
					bnUpload.setEnabled(true);
				} else {
					bnEdit.setEnabled(true);
					bnDel.setEnabled(true);
					bnBatchAddSet.setEnabled(getBatchAddSetButtonState());// ���������Ӽ��ж�
					bnUpload.setEnabled(true);
				}
			}
			//
			isShowBillTemp = false;

		} else if (btnState == CARD_CHILD_BROWSE) {
			bnSave.setEnabled(false);
			bnCancel.setEnabled(false);
			setChildButtonState();
			if (person != null) {
				String belong_pk_corp = null;
				if (listSelectRow >= 0) {
					belong_pk_corp = (String) psnList[listSelectRow]
					                                  .getAttributeValue("belong_pk_corp");// ������˾
				}
				if (!Global.getCorpPK().equalsIgnoreCase(belong_pk_corp)) {
					bnSubsetMove.setEnabled(false);
				}
			}
			//
			isShowBillTemp = true;
		}
		updateButtons();
	}

	/**
	 * V35 new Add
	 * 
	 */
	protected void setUpDownState() {
		// �Ƚϵ�ǰ�ˣ�person��������psnList�����е�λ�ã����з�����Ҫ�޸ģ�ֱ�Ӱ���ǰ���޸�
		if (getPosInList(person, psnList) == 1) {
			bnUpRecord.setEnabled(false);
			bnToFirst.setEnabled(false);
		} else {
			bnUpRecord.setEnabled(true);
			bnToFirst.setEnabled(true);
		}
		if ((psnList == null || psnList.length == 0)
				|| getPosInList(person, psnList) == psnList.length) {
			bnDownRecord.setEnabled(false);
			bnToLast.setEnabled(false);
		} else {
			bnDownRecord.setEnabled(true);
			bnToLast.setEnabled(true);
		}
		if(listSelectRow<0){
			bnUpRecord.setEnabled(false);
			bnToFirst.setEnabled(false);
			bnDownRecord.setEnabled(false);
			bnToLast.setEnabled(false);
		}
	}

	/**
	 * V35 new Add ��Ա���õİ�ť״̬
	 * 
	 */
	protected void setRefButtoState() {
		bnEmployeeReference.setEnabled(true);
		bnApplicate.setEnabled(true);
		bnBatchApplicate.setEnabled(true);
		bnAffirm.setEnabled(true);
		bnQueryAffirm.setEnabled(true);
		if (getCmbPsncl().getSelectedItem()!=null &&((Item) getCmbPsncl().getSelectedItem()).getValue() == 5){
			bnCancelAffirm.setEnabled(true);
		}else{
			bnCancelAffirm.setEnabled(false);
		}
	}

	/**
	 * V35 ����
	 * 
	 * @param person
	 * @param psnList
	 * @return
	 */
	protected int getPosInList(PersonEAVO person, GeneralVO[] psnList) {
		if (psnList == null || psnList.length == 0) {
			return 1;
		}
		int curpsnpos = 1;
		if (isAdding()) {// ��������ʱ�Ĵ��� listSelectRow == -1
			if (person != null) {
				String curpsnpk = person.getPk_psndoc();
				for (int i = 0; i < psnList.length; i++) {
					if (curpsnpk.equalsIgnoreCase(psnList[i].getAttributeValue(
					"pk_psndoc").toString())) {
						curpsnpos = i + 1;
					}
				}
			} else {
				curpsnpos = listSelectRow + 1;// psnList.length;
			}
		} else {// ���б���ѡ��ĳ��ʱ�Ĵ���
			curpsnpos = listSelectRow + 1;
		}

		return curpsnpos;
	}

	/**
	 * ���ظ��Ӽ���Ϣ
	 * @param selectRow
	 * @param tablecode
	 * @return
	 */
	protected PersonEAVO loadPsnChildInfo(int selectRow, String tablecode) {
		try {
			if (selectRow == -1) {
				getCard().getBillModel().setBodyDataVO(null);
				return null;
			}
			if (person == null) {
				String selectPkPsn = (String) psnList[selectRow].getAttributeValue("pk_psndoc");
				String selectpkpsnbasdoc = (String) psnList[selectRow].getAttributeValue("pk_psnbasdoc");
				person = new PersonEAVO();
				person.setPk_psndoc(selectPkPsn);
				person.setPk_psnbasdoc(selectpkpsnbasdoc);
			}
			//�Ƿ���ҵ���Ӽ�
			boolean isTraceTable = getCard().getTraceTables().get(tablecode) != null;
			String pk_corp = (String)psnList[selectRow].getAttributeValue("man_pk_corp");
			String currentcorp = (String)psnList[selectRow].getAttributeValue("pk_corp");
			if(pk_corp == null){
				pk_corp =Global.getCorpPK();
			}
			//�ж��Ƿ��Ƿ�Ƹ������Ƹ
			String indoc = "N";
			boolean isreturn = false;
			if (person != null) {			
				if (listSelectRow >= 0) {
					Object o = psnList[listSelectRow].getAttributeValue("indocflag");
					if (o != null) {
						isreturn = indoc.equals(o) ? false : true;					
					}
				}
			}
			//v50 add 
			if(isTraceTable){//�����ҵ���Ӽ�
				//��ѯ��ҵ���Ӽ��Ƿ�����鿴��ʷ
				boolean look = HIDelegator.getPsnInf().isTraceTableLookHistory(tablecode);
				//�õ��������û��Ƿ�ѡ��鿴��ʷ����
				boolean islookhistory = IsLookHistory();

				//�Ƿ��ǹ�����˾��Ա��belong_pk_corp,������pk_corp
				boolean isbelongcorp = psnList[selectRow].getAttributeValue("belong_pk_corp").equals(Global.getCorpPK());
				//�Ƿ��ְ��Ա
				boolean isPart = psnList[selectRow].getJobTypeFlag().intValue()==0 ? false:true;
				//��Ƹ��Ƹ��Ա�ڲɼ��ڵ㲻�ܲ鿴ҵ���Ӽ�
				if(isreturn && getModuleName().equalsIgnoreCase("600704")){
					SubTable subtable = new SubTable();
					subtable.setTableCode(tablecode);
					GeneralVO[] infoVOs = new GeneralVO[0];
					subtable.setRecordArray(infoVOs);
					person.addSubtable(subtable);
				}else{
					if(isPart){//��ְ��Ա
						//��ҵ���Ӽ�����鿴��ʷ���û�ѡ�в鿴��ʷ���Ҹ���Ա�Ǳ���˾��Ա�������²�ѯ�Ӽ����ݣ���Ϊ�Ƿ���ʾ��ʷ���ܱ仯
						if(look && islookhistory && isbelongcorp){//����˾��ְ��Ա
							//����Ѿ���ѯ�����������ҵ���Ӽ�����
							if (person.getSubtables().get(tablecode) != null) {
								person.removeSubtable(tablecode);
							}
							//��ѯ�ñ�ļ�¼��ҵ���Ӽ�����鿴��ʷ������
							loadPsnChildSub(pk_corp,person.getPk_psnbasdoc(),tablecode,isTraceTable,islookhistory,person.getPk_psndoc());				
						}else{//�繫˾��ְ��Ա
							if (tablecode.equalsIgnoreCase("hi_psndoc_dimission")
									|| tablecode.equalsIgnoreCase("hi_psndoc_deptchg")
									|| tablecode.equalsIgnoreCase("hi_psndoc_retire")
									|| tablecode.equalsIgnoreCase("hi_psndoc_psnchg")) {// ���ڿ絥λ��ְ���ڼ�ְ��˾�У����ܲ鿴��ְ��Ա����ְ��Ϣ����ְ��Ϣ��������������������
								//����Ѿ���ѯ�����������ҵ���Ӽ�����
								if (person.getSubtables().get(tablecode) != null) {
									person.removeSubtable(tablecode);
								}
								SubTable subtable = new SubTable();
								subtable.setTableCode(tablecode);
								person.addSubtable(subtable);
							}else{
								//����Ѿ���ѯ�����������ҵ���Ӽ�����
								if (person.getSubtables().get(tablecode) != null) {
									person.removeSubtable(tablecode);
								}
								// ���ص�ǰ��Ա�ĸñ����Ϣ��¼��ҵ���Ӳ��鿴��ʷ
								//loadPartPsnChildSub(person.getPk_psndoc(),Global.getCorpPK(),tablecode);
								loadPartPsnChildSub(person.getPk_psndoc(),currentcorp,tablecode);
							}
						}
					}else{//�Ǽ�ְ��Ա
						//��ҵ���Ӽ�����鿴��ʷ���û�ѡ�в鿴��ʷ���Ҹ���Ա�Ǳ���˾��Ա�������²�ѯ�Ӽ����ݣ���Ϊ�Ƿ���ʾ��ʷ���ܱ仯
						if(look && islookhistory && isbelongcorp){
							//����Ѿ���ѯ�����������ҵ���Ӽ�����
							if (person.getSubtables().get(tablecode) != null) {
								person.removeSubtable(tablecode);
							}
							//��ѯ�ñ�ļ�¼��ҵ���Ӽ�����鿴��ʷ������
							loadPsnChildSub(pk_corp,person.getPk_psnbasdoc(),tablecode,isTraceTable,islookhistory,person.getPk_psndoc());				
						}else{
							//����Ѿ���ѯ�����������ҵ���Ӽ�����
							if (person.getSubtables().get(tablecode) != null) {
								person.removeSubtable(tablecode);
							}
							// ���ص�ǰ��Ա�ĸñ����Ϣ��¼��ҵ���Ӳ��鿴��ʷ
							loadPsnChildSub(pk_corp,person.getPk_psnbasdoc(),tablecode,isTraceTable,false,person.getPk_psndoc());											
						}
					}
				}
			}else{//�����ҵ���Ӽ�
				if (person.getSubtables().get(tablecode) != null) {
					person.removeSubtable(tablecode);
				}
				// ���ص�ǰ��Ա�ĸñ����Ϣ��¼����ҵ���Ӽ�Ĭ�ϲ鿴��ʷ
				loadPsnChildSub(pk_corp,person.getPk_psnbasdoc(),tablecode, isTraceTable,true,person.getPk_psndoc());
			}			
			CircularlyAccessibleValueObject[] records = person.getTableVO(tablecode);// ��ǰ��Ϣ�������м�¼
			getCard().getBillModel().setBodyDataVO(records);// ���ñ������

		} catch (Exception e) {
			reportException(e);
			showErrorMessage(e.getMessage());
		}
		return person;
	}

	/**
	 * ������Ա�Ӽ���Ϣ
	 * @param pk_psndoc
	 * @param pk_psnbasdoc
	 * @param tablecode
	 * @param isTraceTable
	 * @param lookhistory
	 */
	protected void loadPsnChildSub(String pk_corp,String pk_psnbasdoc,String tablecode,boolean isTraceTable,
			boolean lookhistory,String pk_psndoc) {
		try {
			GeneralVO[] infoVOs = null;
			boolean look = lookhistory;
			//���ص�ǰ��Ա�ĸñ����Ϣ��¼����ҵ���Ӽ�Ĭ�ϲ鿴��ʷ
			if("600704".equalsIgnoreCase(getModuleName())){
				look = true;
			}
			infoVOs = HIDelegator.getPsnInf().queryPersonInfo(pk_corp,
					pk_psnbasdoc, tablecode, isTraceTable, look, pk_psndoc,
					Global.getCorpPK());
			if (!"E0020603".equals(getModuleName())&& !"E0020303".equals(getModuleName())
					&& !"E0041203".equals(getModuleName())) {
				// ���鿴������˾��Ա����ʱ����˾�Զ�������Ϊ��
				if (person.getPsndocVO() != null) {
					String psncorp = (String) person.getPsndocVO().getAttributeValue("pk_corp");
					if (!Global.getCorpPK().equals(psncorp)) {
						infoVOs = setDefItem(infoVOs, "");
					}
				}
			}
			SubTable subtable = new SubTable();
			subtable.setTableCode(tablecode);
			if (infoVOs != null) {
				transPkToName(tablecode, infoVOs);
				subtable.setRecordArray(infoVOs);
			}
			person.addSubtable(subtable);
		} catch (Exception e) {
			reportException(e);
			showErrorMessage(e.getMessage());
		}
	}

	/**
	 * ������Ա�Ӽ���Ϣ
	 * @param pk_psndoc
	 * @param pk_psnbasdoc
	 * @param tablecode
	 * @param isTraceTable
	 * @param lookhistory
	 */
	protected void loadPartPsnChildSub(String pk_psndoc,String pk_corp,String tablecode) {
		try {
			GeneralVO[] infoVOs = null;
			//���ص�ǰ��Ա�ĸñ����Ϣ��¼����ҵ���Ӽ�Ĭ�ϲ鿴��ʷ
			infoVOs = HIDelegator.getPsnInf().queryPartPersonChildInfo(pk_psndoc, pk_corp, tablecode);
			if (!"E0020603".equals(getModuleName())&& !"E0020303".equals(getModuleName())
					&& !"E0041203".equals(getModuleName())) {
				// ���鿴������˾��Ա����ʱ����˾�Զ�������Ϊ��
				if (person.getPsndocVO() != null) {
					String psncorp = (String) person.getPsndocVO().getAttributeValue("pk_corp");
					if (!Global.getCorpPK().equals(psncorp)) {
						infoVOs = setDefItem(infoVOs, "");
					}
				}
			}
			SubTable subtable = new SubTable();
			subtable.setTableCode(tablecode);
			if (infoVOs != null) {
				transPkToName(tablecode, infoVOs);
				subtable.setRecordArray(infoVOs);
			}
			person.addSubtable(subtable);
		} catch (Exception e) {
			reportException(e);
			showErrorMessage(e.getMessage());
		}
	}

	private String loadedToTempPkCorp = Global.getCorpPK();// ���ڼ�¼�л�����ģ���в����еĹ�˾

	/**
	 * 
	 * @param selectRow
	 * @param tablecode
	 * @return
	 */
	protected PersonEAVO loadPsnMainInfo(int selectRow, String tablecode) {
		try {

			if (psnList == null || psnList.length == 0 || selectRow == -1) {
				getCard().getBillModel().setBodyDataVO(null);
				return null;
			}
			String selectPkPsn = (String) psnList[selectRow].getAttributeValue("pk_psndoc");
			String selectPkPsnBas = (String) psnList[selectRow].getAttributeValue("pk_psnbasdoc");
			String key = getPersonsKey(psnList[selectRow]);
			// �˴���Ҫ�޸ģ�������һ�����ʵ�key����ͬһ���˵Ķ����Ϣ������ǹ�����ΧΪ��ְ����ְ��¼������
			person = (PersonEAVO) persons.get(key);
			if (person == null) {
				person = new PersonEAVO();
				person.setPk_psndoc(selectPkPsn);
				person.setPk_psnbasdoc(selectPkPsnBas);
				persons.put(key, person);
			}
			int jobtype = 0;
			if (isMaintain && listSelectRow >= 0 && !isEmployeeRef()) {
				jobtype = ((Integer) psnList[listSelectRow]
				                             .getAttributeValue("jobtypeflag")).intValue();
			}
			String psncorppk = (String) psnList[listSelectRow].getAttributeValue("pk_corp");
			String pk_psnbasdoc = (String) psnList[listSelectRow].getAttributeValue("pk_psnbasdoc");
			String pk_psndoc = (String) psnList[listSelectRow].getAttributeValue("pk_psndoc");// getTempPsnPk();
			String psncorp = psncorppk;
			if (jobtype > 0) {// ��ְ������Ա
				GeneralVO[] vos = HIDelegator.getPsnInf().queryMainPersonInfo(
						pk_psndoc, Global.getCorpPK(), "bd_psndoc",
						GlobalTool.getFuncParserWithoutWa()); 
				psncorp = (String) vos[0].getAttributeValue("pk_corp");// ��Աʵ�ʹ�˾
				isPartPsnCurCorp = !(psncorppk.equals(psncorp));// �жϼ�ְ��Ա�Ƿ�Ϊ������˾�ڵ�ǰ��ѯ��˾��ְ��
			} else {
				isPartPsnCurCorp = false;//
			}
			//���滻�����ˣ������ǰ��˾����Ա���ڹ�˾�����Ų�����ʾ���������ݡ�
//			if (isPartPsnCurCorp) {
//				changeBillTempRef();
//				loadedToTempPkCorp = psncorppk;
//			}
			
			String[] tableCodes = getCard().getBillData().getHeadTableCodes();
			for (int i = 0; i < tableCodes.length; i++) {
				loadPersonMain(pk_psnbasdoc, pk_psndoc, tableCodes[i]);
			}
			getCard().execHeadLoadFormulas();
			getCard().setEnabled(false);
			String psnname = nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"600704", "UPP600704-000090")/* @res "��ǰѡ����Ա�ǣ�" */
					+ (String) psnList[listSelectRow]
					                   .getAttributeValue("psnname");
			showHintMessage(psnname);

		} catch (Exception e) {
			reportException(e);
			showErrorMessage(e.getMessage());
		}
		return person;
	}

	/**
	 * V35 add
	 * 
	 * @param curselectRow
	 * @return
	 * @throws Exception
	 */
	protected PersonEAVO loadPsnInfo(int curselectRow) throws Exception {
		try {
			isLoadedBasicData = false;
			isLoadedAccData = false;
			BillItem item = getBillItem("bd_psndoc.pk_om_job");
			if (item != null) {
				UIRefPane ref = (UIRefPane) item.getComponent();
				if (ref != null) {
					nc.ui.bd.ref.AbstractRefModel m = ref.getRefModel();
					if (m instanceof nc.ui.hi.hi_301.ref.JobRef) {
						nc.ui.hi.hi_301.ref.JobRef refm = new nc.ui.hi.hi_301.ref.JobRef();
						refm.setPk_deptdoc((String) null);
						refm.setCorpPK(Global.getCorpPK());
						ref.setRefModel(refm);
					}
				}
			}
			setAdding(false);// ���ñ༭״̬
			loadPsnMainInfo(curselectRow, null);
			if (!isEmployeeRef()) {
				String tableCode = getBodyTableCode();
				getCard().setBodyMenuShow(tableCode, false);
				loadPsnChildInfo(curselectRow, tableCode);
			}
		} catch (Exception e) {
			reportException(e);
			showErrorMessage(e.getMessage());
		}
		return person;
	}

	/**
	 * 
	 * @param eavo
	 * @param psnLists
	 * @return
	 */
	private int getRowInPsnList(PersonEAVO eavo, GeneralVO[] psnLists) {
		if (psnLists == null || psnLists.length <1) {
			return -1;
		}
		String key = getPersonsKey(eavo.getPsndocVO());
		int pos = -1;
		for (int i = 0; i < psnLists.length; i++) {
			String comparedKey = getPersonsKey(psnLists[i]);
			if (key.equalsIgnoreCase(comparedKey)) {
				pos = i;
				break;
			}
		}
		return pos;
	}

	private String getPersonsKey(GeneralVO oneperson) {
		String key = (String) oneperson.getAttributeValue("pk_psndoc");
		//V55 add 
		if (oneperson.getAttributeValue("pk_deptdoc") != null) {
			key += (String) oneperson.getAttributeValue("pk_deptdoc");
		}
		return key;
	}

	private boolean isEdit = false;

	protected void setEditState(boolean isEdit) {
		this.isEdit = isEdit;
	}

	private boolean isEditing() {
		return isEdit;
	}

	private int intButtonGroup = LIST_GROUP;

	public int getButtonGroup() {
		return intButtonGroup;
	}

	public void setButtonGroup(int buttonGroup) {
		intButtonGroup = buttonGroup;
	}

	public BillModel getPsnListModelDlg() {
		// ��ȡ��ǰ��ʾ��
		Pair[] items = getListItems();
		Vector vExtension = new Vector();
		// ���ɱ�ͷ
		BillItem[] biaBody = new BillItem[items.length + 1];
		biaBody[0] = new BillItem();
		biaBody[0].setName(nc.ui.ml.NCLangRes.getInstance().getStrByID(
				"600704", "UPP600704-000033"));// "ѡ��"
		biaBody[0].setWidth(50);
		biaBody[0].setEnabled(true);
		biaBody[0].setEdit(true);
		biaBody[0].setShow(true);
		biaBody[0].setNull(false);
		biaBody[0].setDataType(BillItem.BOOLEAN);
		((UICheckBox) biaBody[0].getComponent())
		.setHorizontalAlignment(UICheckBox.CENTER);
		vExtension.addElement(biaBody[0]);
		for (int i = 0; i < items.length; i++) {
			if (items[i].getDataType() == CommonValue.DATATYPE_UFREF) {//
				// ��ʾ
				biaBody[i + 1] = new BillItem();
				biaBody[i + 1].setName(items[i].getName());
				biaBody[i + 1].setKey(items[i].getCode() + "_name");
				biaBody[i + 1].setWidth(100);
				biaBody[i + 1].setEnabled(true);
				biaBody[i + 1].setEdit(false);
				biaBody[i + 1].setShow(items[i].isVisible());
				biaBody[i + 1].setNull(false);
				String[] loadFormula = getLoadFormula(items[i]);
				biaBody[i + 1].setLoadFormula(loadFormula);
				vExtension.addElement(biaBody[i + 1]);
				// ����������
				BillItem billItem = new BillItem();
				billItem.setName(items[i].getName() + "����");
				billItem.setKey(items[i].getCode());
				billItem.setWidth(100);
				billItem.setEnabled(true);
				billItem.setEdit(false);
				billItem.setShow(false);// debug false
				billItem.setNull(false);
				vExtension.addElement(billItem);
			} else {
				biaBody[i + 1] = new BillItem();
				biaBody[i + 1].setName(items[i].getName());
				if (items[i].getDataType() == CommonValue.DATATYPE_BOOLEAN) {
					biaBody[i + 1].setDataType(BillItem.BOOLEAN);
				} else {
					biaBody[i + 1].setDataType(BillItem.STRING);
				}
				biaBody[i + 1].setKey(items[i].getCode());
				biaBody[i + 1].setWidth(100);
				biaBody[i + 1].setEnabled(true);
				biaBody[i + 1].setEdit(false);
				biaBody[i + 1].setShow(items[i].isVisible());
				biaBody[i + 1].setNull(false);
				vExtension.addElement(biaBody[i + 1]);
			}
		}
		biaBody = new BillItem[vExtension.size()];
		vExtension.copyInto(biaBody);

		// ����
		BillModel billModel = new BillModel();
		billModel.setBodyItems(biaBody);

		return billModel;
	}

	private HashMap hmIntoDoc = null;// ��ž���ת����Ա����������

	public final String INTO_DOC = "into_doc";// ��ǰ�б���ת����Ա����������

	public final String NOT_INTO_DOC = "not_into_doc";// ��ǰ�б��в�ת����Ա����������

	public HashMap getHmIntoDoc() {
		return hmIntoDoc;
	}

	public void setHmIntoDoc(HashMap hmIntoDoc) {
		this.hmIntoDoc = hmIntoDoc;
	}

	public GeneralVO[] getPsnListData() {
		return psnList;
	}
	/**
	 * �õ�ѡ�е������б�
	 * @return
	 */
	public GeneralVO[] getSelectPsnListData() {
		int[] rows = getPsnList().getHeadSelectedRows();
		if(rows.length<1) return null;
		Vector v = new Vector();
		for (int i = 0; i < rows.length; i++) {
			v.add(psnList[rows[i]]);
		}
		GeneralVO[] selectpsnList = new GeneralVO[v.size()];
		v.copyInto(selectpsnList);

		return selectpsnList;
	}
	/**
	 * �õ�����˾ѡ�е������б�
	 * @return
	 */
	public GeneralVO[] getSelectPsnListDataOfMyCorp() {
		int[] rows = getPsnList().getHeadSelectedRows();
		if(rows.length<1) return null;
		Vector v = new Vector();
		for (int i = 0; i < rows.length; i++) {
			if( Global.getCorpPK().equals((String) psnList[rows[i]].getAttributeValue("belong_pk_corp"))
					&&Global.getCorpPK().equals((String) psnList[rows[i]].getAttributeValue("pk_corp"))){
				v.add(psnList[rows[i]]);
			}
		}
		GeneralVO[] selectpsnList = new GeneralVO[v.size()];
		v.copyInto(selectpsnList);

		return selectpsnList;
	}
	private nc.ui.hi.hi_301.IntoDocDlg ivjIntoDocDlg = null;

	protected nc.ui.hi.hi_301.IntoDocDlg getIntoDocDlg() {
		try {
			ivjIntoDocDlg = new nc.ui.hi.hi_301.IntoDocDlg(this);
			ivjIntoDocDlg.setName("IntoDocDlg");
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		return ivjIntoDocDlg;
	}

	private nc.ui.hi.hi_301.ListItemSetDlg ivjListItemSetDlg = null;

	protected nc.ui.hi.hi_301.ListItemSetDlg getListItemSetDlg() {
		try {
			ivjListItemSetDlg = new nc.ui.hi.hi_301.ListItemSetDlg(this);
			ivjListItemSetDlg.setName("ListItemSetDlg");
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		return ivjListItemSetDlg;
	}

	private nc.ui.hi.hi_301.RefAffirmDlg ivjRefAffirmDlg = null;

	protected nc.ui.hi.hi_301.RefAffirmDlg getRefAffirmDlg() {
		try {
			ivjRefAffirmDlg = new nc.ui.hi.hi_301.RefAffirmDlg(this, false);
			ivjRefAffirmDlg.setName("RefAffirmDlg");
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		return ivjRefAffirmDlg;
	}

	private GeneralVO[] selectItemVos;

	/**
	 * @return ���� selectItemVos��
	 */
	public GeneralVO[] getSelectItemVos() {
		return selectItemVos;
	}

	/**
	 * @param selectItemVos
	 *            Ҫ���õ� selectItemVos��
	 */
	public void setSelectItemVos(GeneralVO[] selectItemVos) {
		this.selectItemVos = selectItemVos;
	}

	private Pair[] listItemsDefault = new Pair[] {//ά���Ͳ�����ְ�Ĳɼ�
			new Pair("psncode", nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"600704", "upt600704-000025")/* @res "��Ա����" */,"bd_psndoc", true),
					new Pair("psnname", nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"600704", "UPP600704-000064")/* @res "��Ա����" */,"bd_psnbasdoc", true),
							new Pair("unitname", nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"600704", "upt600704-000047")/* @res "��˾����" */,"bd_corp", true),
									new Pair("deptcode", nc.ui.ml.NCLangRes.getInstance().getStrByID(
											"600704", "upt600704-000046")/* @res "���ű���" */,"bd_deptdoc", true),
											new Pair("deptname", nc.ui.ml.NCLangRes.getInstance().getStrByID(
													"600704", "upt600704-000001")/* @res "��������" */,"bd_deptdoc", true),
													new Pair("psnclassname",
															nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
															"upt600704-000042")/* @res "��Ա���" */, true),
															new Pair("jobname", nc.ui.ml.NCLangRes.getInstance().getStrByID(
																	"600704", "upt600704-000002")/* @res "��λ����" */, true),
																	new Pair("jobtypename",
																			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
																			"UPP600704-000065")/* @res "��ְ����" */, true),
																			new Pair("showorder", nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
																			"UPT600704-000325")/*"��Ա˳���"*/ ,"bd_psndoc",true),};

	private Pair[] listItemsDefaultGet = new Pair[] {//����ְ�Ĳɼ�
			new Pair("psncode", nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"600704", "upt600704-000025")/* @res "��Ա����" */,"bd_psndoc", true),
					new Pair("psnname", nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"600704", "UPP600704-000064")/* @res "��Ա����" */, "bd_psnbasdoc",true),
							new Pair("unitname", nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"600704", "upt600704-000047")/* @res "��˾����" */,"bd_corp", true),
									new Pair("deptcode", nc.ui.ml.NCLangRes.getInstance().getStrByID(
											"600704", "upt600704-000046")/* @res "���ű���" */,"bd_deptdoc", true),
											new Pair("deptname", nc.ui.ml.NCLangRes.getInstance().getStrByID(
													"600704", "upt600704-000001")/* @res "��������" */,"bd_deptdoc", true),
													new Pair("psnclassname",
															nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
															"upt600704-000042")/* @res "��Ա���" */, true),
															new Pair("jobname", nc.ui.ml.NCLangRes.getInstance().getStrByID(
																	"600704", "upt600704-000002")/* @res "��λ����" */, true),
																	new Pair("jobtypename",
																			nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
																			"UPP600704-000065")/* @res "��ְ����" */, true),
//																			new Pair("approveflagname","��Ա״̬", true),
																			new Pair("approveflagname",
																					nc.ui.ml.NCLangRes.getInstance().getStrByID(
																							"600700", "UPP600700-000360")/* @res "��Ա״̬" */, true),
																			new Pair("showorder", nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
																			"UPT600704-000325")/*"��Ա˳���"*/ ,"bd_psndoc",true),};

	protected Pair[] listItemsKeyPsnDefault = new Pair[] {//�ؼ���Ա����
			new Pair("psncode", nc.ui.ml.NCLangRes.getInstance().getStrByID(
					"600704", "upt600704-000025")/* @res "��Ա����" */, "bd_psndoc",true),
					new Pair("psnname", nc.ui.ml.NCLangRes.getInstance().getStrByID(
							"600704", "UPP600704-000064")/* @res "��Ա����" */, "bd_psnbasdoc",true),
							new Pair("unitname", nc.ui.ml.NCLangRes.getInstance().getStrByID(
									"600704", "upt600704-000047")/* @res "��˾����" */, "bd_corp",true),
									new Pair("keypsngroupname",nc.ui.ml.NCLangRes.getInstance().getStrByID(
											"600710", "UPP600710-000026")/* @res"��Ա��"*/,true),
											new Pair("deptcode", nc.ui.ml.NCLangRes.getInstance().getStrByID(
													"600704", "upt600704-000046")/* @res "���ű���" */, "bd_deptdoc",true),
													new Pair("deptname", nc.ui.ml.NCLangRes.getInstance().getStrByID(
															"600704", "upt600704-000001")/* @res "��������" */,"bd_deptdoc", true),
															new Pair("psnclassname",
																	nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
																	"upt600704-000042")/* @res "��Ա���" */, true),
																	new Pair("jobname", nc.ui.ml.NCLangRes.getInstance().getStrByID(
																			"600704", "upt600704-000002")/* @res "��λ����" */, true),


																			new Pair("showorder", nc.ui.ml.NCLangRes.getInstance().getStrByID("600704",
																			"UPT600704-000325")/*"��Ա˳���"*/ ,"bd_psndoc",true),};
	/**
	 * ���ݲ�ͬ�ڵ�����Ĭ���б�
	 * @return
	 */
	public Pair[] getListItemsDefault() {
		if(intodocdirect ==1 && getModuleName().equalsIgnoreCase("600704")){
			return (Pair[]) Collections.unmodifiableList(
					Arrays.asList(listItemsDefaultGet)).toArray().clone();
		}else{
			return (Pair[]) Collections.unmodifiableList(
					Arrays.asList(listItemsDefault)).toArray().clone();
		}
	}

	public BillItem[] getCurrentSetItems() {
		BillData billData = getCard().getBillData();
		BillItem[] returnItems = billData.getBodyItemsForTable(getCard()
				.getCurrentBodyTableCode());
		return returnItems;
	}

	public BillModel getCurrnentBillModel(){
		return getCard().getBillModel(getCard()
				.getCurrentBodyTableCode());
	}

	public String getCurrentSetCode() {
		return getCard().getCurrentBodyTableCode();
	}

	private boolean isBatchAddSet = false;

	/**
	 * @return ���� isBatchAddSet��
	 */
	public boolean isBatchAddSet() {
		return isBatchAddSet;
	}

	/**
	 * @param isBatchAddSet
	 *            Ҫ���õ� isBatchAddSet��
	 */
	public void setBatchAddSet(boolean isBatchAddSet) {
		this.isBatchAddSet = isBatchAddSet;
	}

	HashMap hmUniqueFld = new HashMap();

	public FlddictVO[] getUniqueFld(String tableCode) {
		FlddictVO[] flds = (FlddictVO[]) hmUniqueFld.get(tableCode);
		if (flds == null) {// || flds.length == 0
			try {
				flds = PubDelegator.getISetdict().querySingleFld(tableCode,
						Global.getCorpPK());
				if(flds!=null && flds.length>1 && tableCode.equalsIgnoreCase("hi_psndoc_edu")){
					FlddictVO[] tempvo = null;
					Vector v = new Vector();
					for(int i =0; i<flds.length;i++){
						if(flds[i].getFldcode().equalsIgnoreCase("lasteducation")){
							continue;
						}else{
							v.addElement(flds[i]);
						}
					}
					tempvo = new FlddictVO[v.size()];
					v.copyInto(tempvo);
					flds = tempvo;
				}
				hmUniqueFld.put(tableCode, flds);
			} catch (Exception e) {
				e.printStackTrace();
				reportException(e);
			}
		}
		return flds;
	}

	/**
	 * @return ���� isEmployeeRef��
	 */
	public boolean isEmployeeRef() {
		return isEmployeeRef;
	}

	/**
	 * @param isEmployeeRef
	 *            Ҫ���õ� isEmployeeRef��
	 */
	public void setEmployeeRef(boolean isEmployeeRef) {
		this.isEmployeeRef = isEmployeeRef;
	}

	/**
	 * @return Returns the isListDefault.
	 */
	public boolean isListDefault() {
		return isListDefault;
	}

	/**
	 * @param isListDefault
	 *            The isListDefault to set.
	 */
	public void setListDefault(boolean isListDefault) {
		this.isListDefault = isListDefault;
	}

	/**
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */
	public void valueChanged(ListSelectionEvent e) {
		psnListValueChange(e);
	}

	/**
	 * @return ���� itemchange��
	 */
	public boolean isItemchange() {
		return itemchange;
	}

	/**
	 * @param itemchange
	 *            Ҫ���õ� itemchange��
	 */
	public void setItemchange(boolean itemchange) {
		this.itemchange = itemchange;
	}

	private CheckUniqueDlg getAddCheckDlg() {
		if (checkAddDlg == null) {
			checkAddDlg = new CheckUniqueDlg(this);
		}
		return checkAddDlg;
	}

	public void setUserpks(String userpks) {
		this.userpks = userpks;
	}

	public UserNameObject[] getUserRecievers() {
		return userRecievers;
	}

	public void setUserRecievers(UserNameObject[] userRecievers) {
		this.userRecievers = userRecievers;
	}

	public String getUserpks() {
		return userpks;
	}

	public String getRehirePsnbaspk() {
		return rehirePsnbaspk;
	}

	public void setRehirePsnbaspk(String rehirePsnbaspk) {
		this.rehirePsnbaspk = rehirePsnbaspk;
	}

	public String getRehirePsnBelonpk() {
		return rehirePsnBelonpk;
	}

	public void setRehirePsnBelonpk(String rehirePsnBelonpk) {
		this.rehirePsnBelonpk = rehirePsnBelonpk;
	}

	public String getRehirePsnpk() {
		return rehirePsnpk;
	}

	public void setRehirePsnpk(String rehirePsnpk) {
		this.rehirePsnpk = rehirePsnpk;
	}
	//���������¼���Ա
	private UICheckBox includeChildDeptPsn = null;

	protected UICheckBox getincludeChildDeptPsn() {
		if (includeChildDeptPsn == null) {
			try {
				includeChildDeptPsn = new UICheckBox();
				includeChildDeptPsn.setText(nc.ui.ml.NCLangRes.getInstance().getStrByID("600704", "UPT600704-000302"));//�����¼���Ա
				includeChildDeptPsn.setFont(new java.awt.Font("dialog", 0, 12));
				includeChildDeptPsn.setPreferredSize(new java.awt.Dimension(93, 20));
				// Ĭ�ϰ����¼���Ա
				//includeChildDeptPsn.setSelected(true);
			} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return includeChildDeptPsn;
	}
	/**
	 * ȡ�����ð�ť��Ӧ�¼�
	 * @throws java.lang.Exception
	 */
	public void onCancelAffirm() throws java.lang.Exception {
//		if (listSelectRow < 0) { 
//		return;
//		}
//		// �鿴�������Ա
//		CancelAffirmDlg cancelAffirm = new CancelAffirmDlg(this);
//		// Ĭ��Ϊ��ǰ��½����
//		cancelAffirm.setInputDate(nc.ui.hr.global.Global.getLogDate());
//		cancelAffirm.showModal();
//		if (cancelAffirm.getResult() == UIDialog.ID_CANCEL)
//		return;

//		//�õ��û���������ڣ�Ĭ��Ϊϵͳ��¼����
//		UFDate userDate = cancelAffirm.getInputDate() != null ? cancelAffirm
//		.getInputDate() : new UFDate(nc.ui.hr.global.Global
//		.getLogDate().toString());
//		HashMap hmRelVO = new HashMap();

//		GeneralVO psnvo = new GeneralVO();
//		psnvo.setAttributeValue("pk_psndoc", psnList[listSelectRow].getAttributeValue("pk_psndoc"));
//		psnvo.setAttributeValue("sealdate", userDate);
//		psnvo.setAttributeValue("pk_corp", Global.getCorpPK());
//		hmRelVO.put("bd_psndoc", psnvo);
//		// ���¼�¼��Ϣ
//		HIDelegator.getPsnInf().updateRelTable(hmRelVO);
//		// ˢ�µ�ǰ����
//		onRefresh();
		BatchCancelAffirmDlg cancelAffirm = new BatchCancelAffirmDlg(this,this,null);
		// ˢ�µ�ǰ����
		if (cancelAffirm.showModal() == UIDialog.ID_OK){
			onRefresh();
		}
	}
	/**
	 * �༭���¼�. ��������:(2001-3-23 2:02:27)
	 * 
	 * @param e
	 *            ufbill.BillEditEvent
	 */
	public void afterEdit(BillEditEvent e) {
		String key = e.getKey();
		if (e.getPos() == IBillItem.HEAD) {
			BillItem item = getCard().getHeadItem(key);
			getCard().execHeadTailEditFormulas(item);
			if (key.equalsIgnoreCase("pk_om_job")) {
				String pk_deptdoc = (String) ((UIRefPane) getCard()
						.getHeadItem(key).getComponent())
						.getRefValue("om_job.pk_deptdoc");
				if (pk_deptdoc != null)
					((UIRefPane) getCard().getHeadItem("pk_deptdoc")
							.getComponent()).setPK(pk_deptdoc);

			}
		}
	};


	/**
	 * �иı��¼��� �������ڣ�(2001-3-23 2:02:27)
	 * 
	 * @param e
	 *            ufbill.BillEditEvent
	 */
	public void bodyRowChange(nc.ui.pub.bill.BillEditEvent e) {
		//
		BillModel mymodel = getCurBillModel();
		int row = e.getRow();
		if (row == -1)
			return;
		BillItem[] bodyItem = (BillItem[]) mymodel.getBodyItems();		
		PersonEAVO eavo = getCurData();// ��ȡ��������
		CircularlyAccessibleValueObject[] records = eavo
		.getTableVO(getCurTableName());
		String tablecode = getCard().getCurrentBodyTableCode();
		for (int i = 0; i < bodyItem.length; i++) {
			if (bodyItem[i].getDataType() == BillItem.UFREF) {
				String pk = (String) records[row]
				                             .getAttributeValue(Util.PK_PREFIX
				                            		 + bodyItem[i].getKey());
				((UIRefPane) getCard().getBodyItem(bodyItem[i].getKey())
						.getComponent()).setPK(pk);
				// ������֮�����������ϵʱ����Ҫ�ı����
				if ("hi_psndoc_part".equals(tablecode)
						|| "hi_psndoc_deptchg".equals(tablecode)) {
					refreshRef(tablecode, bodyItem[i].getKey(), pk);
				}
			}
		}
		// ���õ�ǰ�༭��
		if (!isNotMultiEdit())
			setEditLineNo(row);
	}
	/**
	 * ���ı������
	 * 
	 * @param tablecode
	 * @param fieldcode
	 * @param pk
	 */
	public void refreshRef(String tablecode, String fieldcode, String pk) {
		// ��ְ����ְ��¼
		if ("hi_psndoc_part".equals(tablecode)
				|| "hi_psndoc_deptchg".equals(tablecode)) {
			// ��˾�ı�Ӱ�����
			if ("pk_corp".equals(fieldcode)) {
				// ���ŵ�������
				UIRefPane deptRef = (UIRefPane) getCard().getBodyItem(
				"pk_deptdoc").getComponent();
				deptRef.getRefModel().clearCacheData();
				deptRef.setPk_corp(pk);
				AbstractRefModel refmodel = deptRef.getRefModel();
				if (refmodel instanceof SpaDeptdocRefTreeModel) {
					((SpaDeptdocRefTreeModel) refmodel).setCorppk(pk);

				} else if (refmodel instanceof nc.ui.bd.ref.DefaultRefTreeModel) {
					((DefaultRefTreeModel) refmodel).setPk_corp(pk);
				} else {
					refmodel.setPk_corp(pk);
				}
			} else if ("pk_deptdoc".equals(fieldcode)) { // ���Ÿı�Ӱ�����
				// ��λ��������
				UIRefPane jobRef = (UIRefPane) getCard().getBodyItem(
				"pk_postdoc").getComponent();
				nc.ui.hi.hi_301.ref.JobRef jobRefModel = new nc.ui.hi.hi_301.ref.JobRef();
				jobRef.setRefModel(jobRefModel);
				jobRefModel.setPk_deptdoc(pk);
				jobRef.getRefModel().reloadData();
			}
		}

	}

	/**
	 * ��ǰѡ�񵥾�Model�� �������ڣ�(2003-7-24 10:55:28)
	 */
	private BillModel getCurBillModel() {
		try {
			return (BillModel) (getCurBillPanel().getTableModel());
		} catch (Exception e) {
			reportException(e);
		}
		return null;
	}

	/**
	 * ���ص�ǰѡ����ҳ�档 �������ڣ�(2003-6-17 9:50:49)
	 * 
	 * @return nc.ui.pub.bill.BillScrollPane
	 */
	private BillScrollPane getCurBillPanel() {
		return getCard().getBodyPanel(getCurTableName());
	}

	/**
	 * ���ص�ǰ�����ӱ������ �������ڣ�(2003-6-17 9:50:49)
	 * 
	 * @return nc.ui.pub.bill.BillScrollPane
	 */
	public String getCurTableName() {
		return getCard().getCurrentBodyTableCode();
	}
	/**
	 * ��ȡ��ǰ���ڱ༭�ı�tableCodes�����ݡ� 
	 * �������ڣ�(2004-5-17 11:30:33)	 * 
	 * @param tableCodes String[] ��ǰ���ڱ༭�ı�
	 * @return nc.vo.hi.hi_301.PersonEAVO ��Щ������ݣ������PersonEAVO��
	 */
	private PersonEAVO getCurData() {
		// ��ǰ��Ա����
		PersonEAVO eavo;
		// �����ǰ���������Ա���������༭״̬Ϊ�༭�������������״̬������������һ��PersonEAVO����
		if (getEditType() == EDIT_MAIN && isAdding()) {
			person = null;
			eavo = new PersonEAVO();
		} else if (getEditType() == EDIT_RETURN && isAdding()) {
			person = null;
			eavo = new PersonEAVO();
		} else {
			eavo = (PersonEAVO) person.clone();
		}
		// ��ȡ����
		getCard().getBillValueVOExtended(eavo);
		return eavo;
	}

	private boolean isPsnclPower(String corppk) throws Exception{
		return nc.ui.hr.global.GlobalTool.isUsedDataPower("bd_psncl",corppk);
	}

	/**
	 * �õ�������˾�Ƿ���������Ա���Ȩ�ޡ� �������ڣ�(2004-10-22 15:36:49)
	 * 
	 * @return java.lang.String
	 * @param pk_corp
	 *            java.lang.String
	 */
	public String getPsnclPowerByCorp(String corppk) throws Exception {
		String power = (String) psnclPowerlist.get(corppk);
		if (power != null) {
			return power;
		}
		if (!"0001".equals(Global.getCorpPK())) {
			//�������Ż�
//			boolean usePsnclPower = GlobalTool.isUsedDataPower("bd_psncl",
//					corppk);
//			if (usePsnclPower) {
				power = GlobalTool.getPowerSql("bd_psncl", Global.getUserID(),
						corppk);
				if (power==null){
					psnclPowerlist.put(corppk, " 0 = 0 ");
				}else{
					psnclPowerlist.put(corppk, power);
				}
				return power;
//			} else {
//				psnclPowerlist.put(corppk, " 0 = 0 ");
//				return " 0 = 0 ";
//			}
		} else {
			psnclPowerlist.put(corppk, " 0 = 0 ");
			return " 0 = 0 ";
		}
	}
	/**
	 * �Ƿ���Զ����Ӽ���¼ͬʱ�༭
	 * 
	 * @return boolean
	 */
	protected boolean isNotMultiEdit() {
		// ׷����Ϣ������ʾ������<��ʼ
		return isBodyTraceTable()
		|| getBodyTableCode().equalsIgnoreCase("hi_psndoc_ctrt");
	}

	public boolean isEditSub() {
		return isEditSub;
	}

	public void setEditSub(boolean isEditSub) {
		this.isEditSub = isEditSub;
	}
	/**
	 * ȡ��ϵͳ�����������Ƿ���Ҫȷ�ϡ�
	 * @return
	 */
	public boolean getIsNeedAFirm(){
		if (isNeedAFirm == null){
			try{
				//Ч���Ż�
				//isNeedAFirm = PubDelegator.getIParValue().getParaBoolean(Global.getCorpPK(), "HI_NEEDAFIRM");
				isNeedAFirm = UFBoolean.valueOf(getPara("HI_NEEDAFIRM"));
			}catch(Exception e){
				
				e.printStackTrace();
			}
		}
		return isNeedAFirm.booleanValue();
	}

	public JComponent createFieldValueEditor(FilterMeta filterMeta) {
		// ��ʼ����ж�
		if(filterMeta==null || StringUtils.isEmpty(filterMeta.getFieldCode())){
			return null;
		}//end if
		// ��¼��˾����
		 String pk_corp = Global.getCorpPK();

		// ��¼�û�ID
		 String userID = Global.getUserID();
		//String selcorppk = getUIRefCorpPnl().getRefPK();
		 String selcorppk = null;
		if (selcorppk == null) {
			selcorppk = Global.getCorpPK();
		}//end if
		
		
		
		// ���������
		if("bd_psndoc.pk_psncl".equals(filterMeta.getFieldCode())){
			// ��Ա������
			UIRefPane psnclsref = new UIRefPane();
			psnclsref.setRefType(1);
			psnclsref.setName("psnclsref");
			psnclsref.setRefInputType(1);
			psnclsref.setRefNodeName("��Ա���");
			psnclsref.getRefModel().setSealedDataShow(true);
			return psnclsref;
		}else if("bd_accpsndoc.dutyname".equals(filterMeta.getFieldCode())){
			// ְ�����
			UIRefPane dutyref = new UIRefPane();
			dutyref.setName("dutyref");
			DutyRef dutyModel = new DutyRef(selcorppk);
			dutyref.setRefType(1);
			dutyref.setRefInputType(1);
			dutyref.setRefModel(dutyModel);
			return dutyref;
		}else if("hi_psndoc_deptchg.isreturn".equals(filterMeta.getFieldCode())){
			
			//filterMeta.setDataType(IQueryConstants.BOOLEAN);
		} else if("hi_psndoc_deptchg.pk_psncl".equals(filterMeta.getFieldCode())){
			// ��Ա������
			UIRefPane psnclsref = new UIRefPane();
			psnclsref.setRefType(1);
			psnclsref.setName("psnclsref");
			psnclsref.setRefInputType(1);
			psnclsref.setRefNodeName("��Ա���");
			psnclsref.getRefModel().setSealedDataShow(true);
			String wherepart = psnclsref.getRefModel().getWherePart();
			if (wherepart == null || wherepart.trim().length() == 0) {
				wherepart = "( psnclscope = 0 or  psnclscope = 5 ) ";
			} else {
				String whereScope = " (psnclscope = 0 or  psnclscope = 5 ) and (";
				if (wherepart.indexOf(whereScope) < 0) {
					wherepart = whereScope + wherepart + ")";
				}
			}
			psnclsref.getRefModel().setWherePart(wherepart);
			return psnclsref;
		}
		
		
		
		
		// �ֵ�¼���
		if (Global.getCorpPK().equals(CommonValue.GROUPCODE)) { 
			// �����¼Ϊ����
			if("bd_psndoc.pk_psndoc".equals(filterMeta.getFieldCode())){


				// ��Ա���� wangkf add v31Sp1
				SpaDeptdocRefGridTreeModel psnrefmodel = new SpaDeptdocRefGridTreeModel();
				psnrefmodel.setIsLimit(0);
				String deptsql = " bd_deptdoc.pk_corp = '" + selcorppk + "'";
				psnrefmodel.setCorpPK(selcorppk);
				psnrefmodel.setClassWherePart(deptsql);
				psnrefmodel.setCacheEnabled(false);
				UIRefPane psnref = new UIRefPane();
				psnref.setRefType(1);
				psnref.setRefInputType(1);
				String psnwhere = psnrefmodel.getWherePart();
				psnwhere = psnwhere.substring(0, psnwhere.indexOf("and bd_psndoc.psnclscope"));
				psnwhere += ("and bd_psndoc.psnclscope =" + getCurPsnScope());
				psnrefmodel.setWherePart(psnwhere);
				psnref.setRefModel(psnrefmodel);

				return psnref;

			}else if("bd_psndoc.pk_deptdoc".equals(filterMeta.getFieldCode())){
				// ���Ų���
				DeptRefModel deptrefmodel = new DeptRefModel();
				UIRefPane deptref = new UIRefPane();
				deptref.setName("deptref");
				deptref.setRefType(1);
				deptref.setRefInputType(1);
				deptref.setRefModel(deptrefmodel);
				setWhereToModel(deptref.getRefModel(),"((bd_deptdoc.hrcanceled = 'N') or ( bd_deptdoc.hrcanceled is null) )");
				deptref.setIncludeSubShow(true);
				return deptref;
			}else if("bd_psndoc.pk_om_job".equals(filterMeta.getFieldCode())){
				// ��λ����
				JobRef jobrefmodel = new JobRef();
				jobrefmodel.setRefTitle("��λ����");
				UIRefPane jobref = new UIRefPane();
				jobref.setName("jobref");
				jobref.setRefType(1);
				jobref.setRefInputType(1);
				jobref.setRefModel(jobrefmodel);
				return jobref;
			}//end if
		}else{
			// �����¼Ϊ��˾

			if("bd_psndoc.pk_psndoc".equals(filterMeta.getFieldCode())){
				if(getModuleName().equals("600704")){
					try{
						//���Ӳ���Ȩ��
						String deptpower="";
						// ѡ��Ĺ�˾Ϊ��ǰ��¼��˾,���ҵ�¼��˾���Ǽ��ţ��ܲ���Ȩ�޿���
						boolean isUserDeptPower = GlobalTool.isUsedDataPower("bd_deptdoc",selcorppk);
						if (isUserDeptPower) {
							String powerSql = GlobalTool.getPowerSql("bd_deptdoc", userID, selcorppk);
							if (powerSql != null && powerSql.length() > 0) {
								deptpower = " " + powerSql + " ";
							}
						} else {
							deptpower = " 0=0 ";
						}
						String deptsql = " 0=0 ";
						if (deptpower.length() > 0 && deptpower.trim().startsWith("select")) {
							deptsql = " and bd_deptdoc.pk_deptdoc in (" + deptpower
							+ ")";
						} else {
							deptsql = " and 0 = 0 ";
						}
						String psnclsql="";
						boolean isUserPsnClPower = GlobalTool.isUsedDataPower("bd_psncl", selcorppk);
						if (isUserPsnClPower) {
							String powerPsnclSql = GlobalTool.getPowerSql("bd_psncl", userID, selcorppk);
							if (powerPsnclSql != null && powerPsnclSql.length() > 0) {
								psnclsql += " and bd_psndoc.pk_psncl in ("
									+ powerPsnclSql + ") ";
							}
						}
						
						PsndocDefaulRefModel model = new PsndocDefaulRefModel("��Ա����");
						model.setPk_corp(pk_corp);
						model.setClassWherePart(" (pk_corp='" + pk_corp
								+ "' or pk_corp= '" + "0001"
								+ "' or pk_corp is null) and canceled <>'Y' and hrcanceled <> 'Y' "+deptsql);
						model.setWherePart(" bd_psndoc.pk_corp = '"
								+ pk_corp
								+ "' and bd_psndoc.pk_corp = bd_psnbasdoc.pk_corp and  bd_psndoc.indocflag = 'N' "+psnclsql);
						//���ò�����Ա����Ȩ�޿���
						model.setUseDataPower(false);
						model.clearCacheData();
						UIRefPane psnref = new UIRefPane();
						psnref.setRefModel(model);
						return psnref;
					}catch(Exception e){
						e.printStackTrace();
						return null;
					}

//					PsndocDefaulRefModel model = new PsndocDefaulRefModel("��Ա����");
//					model.setPk_corp(pk_corp);
//					model.setClassWherePart(" (pk_corp='" + pk_corp
//							+ "' or pk_corp= '" + "0001"
//							+ "' or pk_corp is null) and canceled <>'Y' and hrcanceled <> 'Y'");
//					model.setWherePart(" bd_psndoc.pk_corp = '"
//							+ pk_corp
//							+ "' and bd_psndoc.pk_corp = bd_psnbasdoc.pk_corp and  bd_psndoc.indocflag = 'N' ");
//
//					//���ò�ѯ�����ܲ���Ȩ�޿���
//					model.setClassDataPower(false);
//					//���ò�����Ա����Ȩ�޿���
//					model.setUseDataPower(false);
//					model.clearCacheData();
//					UIRefPane psnref = new UIRefPane();
//					psnref.setRefModel(model);
//					return psnref;
				}else{
					try{
						String deptpower="";
						// ѡ��Ĺ�˾Ϊ��ǰ��¼��˾,���ҵ�¼��˾���Ǽ��ţ��ܲ���Ȩ�޿���
						//�������Ż�
//						boolean isUserDeptPower = GlobalTool.isUsedDataPower("bd_deptdoc",selcorppk);
//						if (isUserDeptPower) {
							String powerSql = GlobalTool.getPowerSql("bd_deptdoc", userID, selcorppk);
							if (powerSql != null && powerSql.length() > 0) {
								deptpower = " " + powerSql + " ";
							}else{
								deptpower = " 0=0 ";
							}
//						} else {
//							deptpower = " 0=0 ";
//						}
						// ��Ա����
						SpaDeptdocRefGridTreeModel psnrefmodel = new SpaDeptdocRefGridTreeModel();
						psnrefmodel.setIsLimit(0);
						String deptsql = " 0=0 ";
						if (deptpower.length() > 0
								&& deptpower.trim().startsWith("select")) {
							deptsql = " bd_deptdoc.pk_deptdoc in (" + deptpower
							+ ")";
						} else {
							deptsql = " 0 = 0 ";
						}
						psnrefmodel.setCorpPK(selcorppk);
						psnrefmodel.setClassWherePart(deptsql);

						psnrefmodel.setCacheEnabled(false);
						UIRefPane psnref = new UIRefPane();
						psnref.setName("psnref");
						psnref.setRefType(0);
						psnref.setRefInputType(1);
						String psnwhere = psnrefmodel.getWherePart();
						if(psnwhere==null) psnwhere = "";
						psnwhere = psnwhere.substring(0, psnwhere.indexOf("and bd_psndoc.psnclscope"));
						psnwhere += ("and bd_psndoc.psnclscope =" + getSelPsnclscope());
						//�������Ż�
//						boolean isUserPsnClPower = GlobalTool.isUsedDataPower("bd_psncl", selcorppk);
//						if (isUserPsnClPower) {
							String powerPsnclSql = GlobalTool.getPowerSql("bd_psncl", userID, selcorppk);
							if (powerPsnclSql != null && powerPsnclSql.length() > 0) {
								psnwhere += " and bd_psndoc.pk_psncl in ("
									+ powerPsnclSql + ") ";
							}
//						}
						psnrefmodel.setWherePart(psnwhere);
						psnref.setRefModel(psnrefmodel);
						return psnref;
					}catch(Exception e){
						e.printStackTrace();
						return null;
					}//end try
				}
			}else if("bd_psndoc.pk_deptdoc".equals(filterMeta.getFieldCode())){
				// ���Ų���
				UIRefPane deptref = new UIRefPane();
				deptref.setName("deptref");
				deptref.setRefType(1);
				deptref.setRefInputType(1);
				deptref.setRefNodeName("���ŵ���HR");
				//2009-10-15
				deptref.setIncludeSubShow(true);
				boolean aa = deptref.isMultiSelectedEnabled();

				deptref.setMultiSelectedEnabled(false);
				deptref.getRefModel().setPk_corp(selcorppk);
				deptref.getRefModel().setUseDataPower(true);
				setWhereToModel(deptref.getRefModel(),"((bd_deptdoc.hrcanceled = 'N') or ( bd_deptdoc.hrcanceled is null) )");

				return deptref;
			}else if("hi_psndoc_deptchg.pk_deptdoc".equals(filterMeta.getFieldCode())){
				// ���Ų���
				UIRefPane deptref2 = new UIRefPane();
				deptref2.setRefType(1);
				deptref2.setName("deptref2");
				deptref2.setRefInputType(1);
				deptref2.setRefNodeName("���ŵ���HR");
				deptref2.setIncludeSubShow(true);
				deptref2.getRefModel().setPk_corp(selcorppk);
				deptref2.getRefModel().setUseDataPower(true);
				setWhereToModel(deptref2.getRefModel(),"((bd_deptdoc.hrcanceled = 'N') or ( bd_deptdoc.hrcanceled is null) )");			
				return deptref2;

			}else if("bd_psndoc.pk_om_job".equals(filterMeta.getFieldCode())){
				// ��λ����
				JobRef jobmodel = new JobRef(selcorppk, null);

				String where = jobmodel.getWherePart();
				jobmodel.setWherePart(where);
				jobmodel.setRefTitle("��λ����");
				UIRefPane jobref = new UIRefPane();
				jobref.setName("jobref");
				jobref.setRefType(1);
				jobref.setRefInputType(1);
				jobref.setRefModel(jobmodel);
				return jobref;
			}//end if
		}//end if
		
		// ��������
		return null;
	}

	public String getRefPanelWherePart(FilterMeta filterMeta) {
		// TODO Auto-generated method stub
		return null;
	}
	/**
	 * �õ�ѡ�е���Ա��������Χ
	 * 
	 * @return
	 */
	private int getCurPsnScope() {
		int index = getCmbPsncl().getSelectedIndex();
		if (index < 0 || index > PSNCLSCOPE_GROUP.length - 1)
			return -1;// δ�����ĳ��ָ
		return PSNCLSCOPE_GROUP[index];
	}
	/**
	 * ����������������ѯ
	 * @since NCHRV5.5
	 */
	protected void onSetOrderAndSort(){
		refreshSortDialogField();
		if (getConfigDialog().showModal() != UIDialog.ID_OK) {
			return;
		}
		//ˢ��
		onRefresh();

	}
	/**
	 * �������Dialog
	 * @return SortCnfigDialog ���Ա�������������Dialog
	 * @since NCHRV5.5
	 */
//	public SortConfigDialog getSortDialog() {
//	BillModel billModel = getPsnList().getTableModel();
//	BillItem[] items = billModel.getBodyItems();
//	Vector vvv = new Vector();
//	for (int i = 0; i < items.length; i++){
//	if(items[i].isShow()){
//	nc.ui.hr.comp.sort.Pair fieldPair 
//	= new nc.ui.hr.comp.sort.Pair(items[i].getKey(), items[i].getName());
//	vvv.addElement(fieldPair);					
//	}				
//	}
//	nc.ui.hr.comp.sort.Pair[] listItems = new nc.ui.hr.comp.sort.Pair[vvv.size()];
//	vvv.copyInto(listItems);		
//	configDialog = new nc.ui.hr.comp.combinesort.SortConfigDialog(Util.getTopFrame(this), true);
//	configDialog.setFields(listItems);
//	configDialog.setLocationRelativeTo(this);
//	configDialog.setModuleCode(getModuleName());
//	configDialog.btnLoad_ActionPerformed(null);



//	nc.ui.hr.comp.sort.Pair[] listSortItems = null;

//	Vector<nc.ui.hr.comp.sort.Pair> vecPair = new Vector<nc.ui.hr.comp.sort.Pair>();

//	nc.ui.hr.comp.sort.Pair condVo = new nc.ui.hr.comp.sort.Pair("bd_psndoc.psncode",NCLangRes.getInstance().getStrByID("common","UC000-0000147")/*@res "��Ա����"*/);
//	vecPair.addElement(condVo);
//	condVo = new nc.ui.hr.comp.sort.Pair("bd_psndoc.psnname",NCLangRes.getInstance().getStrByID("common","UC000-0000135")/*@res "��Ա����"*/);
//	vecPair.addElement(condVo);


//	listSortItems = new nc.ui.hr.comp.sort.Pair[vecPair.size()];
//	vecPair.copyInto(listSortItems);

//	if (sortDialog == null) {
//	sortDialog = new SortConfigDialog(this, true);
//	}

//	String newClassPk = "";//getUIRefWaClass().getRefPK();

//	sortDialog.setFields(listSortItems);
//	sortDialog.setTableCode(newClassPk);
//	sortDialog.setModuleCode(getModuleCode());
//	sortDialog.setLocationRelativeTo(this);

//	//�л������������仯��������������б��������ϴ���������������л������򲻱���
//	if(!newClassPk.equalsIgnoreCase(oldClassPk)) {
//	sortDialog.setSortingFields(new Vector());//�����Ĭ������
//	}
//	sortDialog.btnLoad_ActionPerformed(null);

//	//����Ĭ����������
//	setDefaultOrderFields(sortDialog, listSortItems);

//	oldClassPk = newClassPk;

//	return sortDialog;
//	}
	/**
	 * ����Ĭ����������
	 * @param configDialog SortConfigDialog ����Dialog
	 * @param listSortItems Vector �ɹ�ѡ��������ֶ�
	 * @since NCHRV5.5
	 */
	private void setDefaultOrderFields(SortConfigDialog configDialog, nc.ui.hr.comp.sort.Pair[] listSortItems) {
		Vector<Attribute> sortingFields = configDialog.getSortingFields();
		//��һ��ʹ��û��Ĭ�������ֶΣ�������Ĭ��Ϊ"���ű���" asc ��"��Ա����" asc
		if (sortingFields == null || sortingFields.isEmpty()) {
			Vector<Attribute> defaultSortFields = new Vector<Attribute>();
			nc.ui.hr.comp.combinesort.Attribute deptcode = new nc.ui.hr.comp.combinesort.Attribute(new nc.ui.hr.comp.sort.Pair("bd_deptdoc.deptcode",NCLangRes.getInstance().getStrByID("common","UC000-0004073")/*@res "���ű���"*/), true);
			nc.ui.hr.comp.combinesort.Attribute psncode = new nc.ui.hr.comp.combinesort.Attribute(new nc.ui.hr.comp.sort.Pair("bd_psndoc.psncode",NCLangRes.getInstance().getStrByID("common","UC000-0000147")/*@res "��Ա����"*/), true);
			defaultSortFields.addElement(deptcode);
			defaultSortFields.addElement(psncode);
			configDialog.setSortingFields(defaultSortFields);
		}
		//����Ѿ���Ĭ�������ֶΣ���������ֶ��Ƿ��ڿɹ�ѡ��������ֶ�����У������������Ҫɾ��
		else {
			for (int i = 0; i < sortingFields.size(); i++) {
				boolean isExistInList = false;
				nc.ui.hr.comp.sort.Pair pair = (sortingFields.elementAt(i)).getAttribute();
				for (int j = 0; j < listSortItems.length; j++) {
					if (listSortItems[j].getCode().equals(pair.getCode())) {
						isExistInList = true;
						break;
					}
				}
				if (!isExistInList) {
					sortingFields.removeElementAt(i);
				}
			}
		}
	}
	protected String oldClassPk = null;
	public void doMaintainAction(ILinkMaintainData maintaindata) {
		try {
			String pk_psndoc = maintaindata.getBillID();
//			GeneralVO[] personVOs = HIDelegator.getPsnInf().queryMainPersonInfo(pk_psndoc, Global.getCorpPK(),"bd_psndoc", GlobalTool.getFuncParser());// ���ǻ�����Ϣ�����Ϣ
			ConditionVO[] conds = new ConditionVO[2];
			conds[0] = new ConditionVO();
			conds[0].setFieldCode("bd_psndoc.pk_psndoc");
			conds[0].setOperaCode("=");
			conds[0].setValue(pk_psndoc);	
			
			conds[1] = new ConditionVO();
			conds[1].setFieldCode("bd_psndoc.indocflag");
			conds[1].setOperaCode("=");
			conds[1].setValue("Y");			

			//���ٹ�����ְ��������Աû����ְ��¼�����������ܲ��������Ա��dusx 2009-3-13
//			conds[2] = new ConditionVO();
//			conds[2].setFieldCode("hi_psndoc_deptchg.jobtype");
//			conds[2].setOperaCode("=");
//			conds[2].setDataType(CommonValue.DATATYPE_INTEGER);
//			conds[2].setValue("0");

			//amend on 09.12.2
			GeneralVO[] personVOs = HIDelegator.getPsnInf().queryByCondition(
					Global.getCorpPK(), conds, "", getListFieldForOne(),
					" and (1=1) ");
			
			
			if(personVOs==null||personVOs.length==0){
				return;
			}
			psnList = personVOs;
			listSelectRow = 0;
			switchButtonGroup(CARD_GROUP);
			setButtonGroup(CARD_GROUP);
			loadPsnInfo(listSelectRow);// װ�� ��ǰ����Ա����
			setButtonsState(CARD_MAIN_BROWSE);
			setButtonsState(CARD_CHILD_BROWSE);
			getCard().setPosMaximized(-1);
			bnList.setVisible(false);//��������
			updateButton(bnList);
			bnDel.setEnabled(false);
			updateButtons();
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	//Ч���Ż�start
	private HashMap<String, String> allRelatedPara= null;
	public String getPara(String para_name){
		if (allRelatedPara==null||allRelatedPara.size()<0){
			try {
				allRelatedPara = PubDelegator.getHrPara().getStringValues(Global.getCorpPK(), null, null, new String[]{"HI_INDOC","PROBUNIT","TERMUNIT","HI_MAXLINE","HI_CODECRTTYPE","HI_CODEUNIQUE","HI_MESSAGE","HI_KEYPERSON","HI_NEEDAFIRM"});
				String hi_indoc = "0";
				if(!allRelatedPara.get("HI_INDOC").equals("0")){
					hi_indoc = "1";
				}
				allRelatedPara.put("HI_INDOC", hi_indoc);
			} catch (BusinessException e) {
//				Logger.error("ȡ��������", e);
				Logger.error(nc.ui.ml.NCLangRes.getInstance().getStrByID("600700", 
						"UPP600700-000357")/* @res "ȡ��������" */ , e);
				
				e.printStackTrace();
			}
		}
		return allRelatedPara.get(para_name);
	}
	//Ч���Ż�end
	private void initPara(){
			try {
				allRelatedPara = PubDelegator.getHrPara().getStringValues(Global.getCorpPK(), null, null, new String[]{"HI_INDOC","PROBUNIT","TERMUNIT","HI_MAXLINE","HI_CODECRTTYPE","HI_CODEUNIQUE","HI_MESSAGE","HI_KEYPERSON","HI_NEEDAFIRM"});
				String hi_indoc = "0";
				if(!allRelatedPara.get("HI_INDOC").equals("0")){
					hi_indoc = "1";
				}
				allRelatedPara.put("HI_INDOC", hi_indoc);
			} catch (BusinessException e) {
//				Logger.error("ȡ��������", e);
				Logger.error(nc.ui.ml.NCLangRes.getInstance().getStrByID("600700", 
						"UPP600700-000357")/* @res "ȡ��������" */ , e);
				e.printStackTrace();
			}
	}
	
	public boolean isNeedPrompt() throws BusinessException {
		return false;
	}

	public String quickSearch(String psnWherePart, SearchType searchType) throws BusinessException {
		try{
		//�ж��Ƿ����ִ�в�ѯ
		String powerStr =QsbUtil.checkSearchPower(this,bnQuery,true);
		if(powerStr != null){
			return powerStr;
		}
		isQuery = false;
		isQuickSearch = true;
		quickWherePart = psnWherePart;
		// ˢ�����л���
		psnList = null;
		queryResult = null;
		recordcount = 0;
		person = null;
		psnDeptCache.clear();
		psnCorpCache.clear();
		persons.clear();
		// ǿ�ƻ�������
		System.gc();
		hmSortCondition.clear();
		listSelectRow = -1;
		unloadPsn = null;
		// ���²�ѯ��ˢ����Ա�б�
		performQuery();		
		if(unloadPsn != null){
			return unloadPsn;
		}
		}catch(Exception e){
			reportException(e);
			showErrorMessage(e.getMessage());
		}
		return null;
	}
	/**
	 * ��ѯ��Ա�б�TODO ��Ա��Ϣ�ɼ���
	 * �������ڣ�(2010-02-03 11:02:41)
	 * @exception java.lang.Exception  �쳣˵����
	 */
	protected void queryResult(String psnWherePart) throws java.lang.Exception {
		
	}
}
