package com.ufsoft.iufo.fmtplugin.formula;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.AbstractCellEditor;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import nc.bs.framework.common.InvocationInfoProxy;
import nc.bs.logging.Logger;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITablePane;
import nc.ui.pub.beans.constenum.DefaultConstEnum;
import nc.ui.pub.dsmanager.ref.AbsCG;
import nc.ui.pub.dsmanager.ref.CGFactory;
import nc.ui.pub.querymodel.UIUtil;
import nc.vo.iuforeport.businessquery.QueryUtil;
import nc.vo.pub.dsmanager.IQEContextKey;
import nc.vo.pub.dsmanager.exception.DSNotFoundException;
import nc.vo.pub.querymodel.QueryConst;

import com.ufida.dataset.Context;
import com.ufida.dataset.DataSet;
import com.ufida.dataset.Parameter;
import com.ufida.dataset.ParameterAnalyzer;
import com.ufida.dataset.descriptor.FilterItem;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.formatcore.UfoContextVO;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iufo.util.parser.UfoSimpleObject;
import com.ufsoft.report.fmtplugin.formula.FmlRefTextField;
import com.ufsoft.script.function.FuncListInst;
import com.ufsoft.table.CellsPane;

/*
 * ���ݼ����������������˶������.
 * Creation date: (2008-07-09 10:52:08)
 * @author: chxw
 * @edit: yza �ع�,��δ�����
 * @edit:wangyga �ϱߵ��ع��ĳ���һ�����⡣��������ѽ������һֱ�����ڲŷ���
 */
public class ParamCordFilterPanel extends AbsDataSetCordPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * @i18n uiufo20043=����
	 * @i18n miufo00914=��������
	 * @i18n miufo00915=��������
	 * @i18n miufo00612=ֵ����
	 */
	private final static String[] COLUMN_NAMES = {
		StringResource.getStringResource("uiufo20043"), StringResource.getStringResource("miufo00914"), StringResource.getStringResource("miufo00612"), StringResource.getStringResource("miufo1000751"),// 4
	};

	private final static DefaultConstEnum[] VALUE_TYPES = new DefaultConstEnum[] {
		new DefaultConstEnum(FilterItem.TYPE_CONST, FilterItem
				.getValueTypeCaption(FilterItem.TYPE_CONST)),
		new DefaultConstEnum(FilterItem.TYPE_EXP, FilterItem
				.getValueTypeCaption(FilterItem.TYPE_EXP))};

	private UITablePane ivjTablePn = null;
	
	private CellsPane cellsPane = null;
	
	private UfoFmlExecutor rowFilterExprFmlExecutor = null;
	
	private Hashtable<String, String> paramCondFilters = null;
	
	private UfoFmlExecutor paramFilterExprFmlExecutor = null;
	
	//�õ��Ĳ����б�
	private Parameter[] usedParams = null;
	
	//������Ӧ�ؼ��������б�
	private AbsCG[] cgs = null;

	
	/**
	 * ��ñ��
	 */
	public UITable getTable() {
		return getTablePn().getTable();
	}

	/**
	 * ��ñ��ģ��
	 */
	public DefaultTableModel getTM() {
		return (DefaultTableModel) getTable().getModel();
	}

	/**
	 * ���� TablePn ����ֵ��
	 * 
	 * @return UITablePane
	 */
	private UITablePane getTablePn() {
		if (ivjTablePn == null) {
			try {
				ivjTablePn = new UITablePane();
				ivjTablePn.setName("TablePn");
		} catch (java.lang.Throwable ivjExc) {
				handleException(ivjExc);
			}
		}
		return ivjTablePn;
	}

	/**
	 * ÿ�������׳��쳣ʱ������
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {
		exception.printStackTrace(System.out);
	}

	/**
	 * ��ʼ���ࡣ
	 */
	private void initialize() {
		try {
			setName("ParamCordFilterPanel");
			setLayout(new BorderLayout());
			setSize(560, 350);
			add(getTablePn(), "Center");
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
	}

	public ParamCordFilterPanel(CellsPane pane) {
		super(pane.getDataModel(),pane.getContext());
		this.cellsPane = pane;
		initialize();
		initModel();
	}

	private void initModel() {
		UITable table = getTable();
		table.setModel(new ParamFilterTableModel());
		// ���ñ�����
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setBackground(QueryConst.HEADER_BACK_COLOR);
		table.getTableHeader().setForeground(QueryConst.HEADER_FORE_COLOR);
		table.getColumn(COLUMN_NAMES[2]).setCellEditor(
				new DefaultCellEditor(new UIComboBox(VALUE_TYPES)));
		table.getColumn(COLUMN_NAMES[3]).setCellEditor(new ParamValueEditor());

	}

	private void initData(){
		DataSet dataset = super.getDataSet();
		//���ݼ��õ��Ĳ���
		Parameter[] params = dataset.getUsedParameters();
		//�������͵Ĳ����õ����������ʵ����
		usedParams = filterParameters(params);
		((ParamFilterTableModel)getTable().getModel()).removeParameter();
		if(usedParams == null || usedParams.length == 0){
			return;
		}
		for (Parameter p : usedParams) {
			if (paramCondFilters != null) {
				if (paramCondFilters.containsKey(p.getName())) {
					String value = toDelQuotes(paramCondFilters
							.get(p.getName()));
					p.setValue(toDelQuotes(value));
					p.setOperaCode("" + getValueType(value));

				}
			} else {
				p.setOperaCode("" + FilterItem.TYPE_CONST);
			}
		}
		
		//��ʼ���ؼ��������б�
		//��ʼ��Managers��Editors
		cgs = new AbsCG[this.usedParams.length];
		for(int i=0;i<usedParams.length;i++){
			AbsCG cg = CGFactory.getFactory(usedParams[i], dataset).createDefaultValueCG();
			cgs[i] = cg;
		}
		
		//��ʼ�������б�
		if(this.usedParams!=null &&
				this.usedParams.length > 0){
			for(int i=0;i<usedParams.length;i++){
				Object[] objs = new Object[]{
						usedParams[i].getName(),
						usedParams[i].getCaption(),
						usedParams[i].getOperaCode(),
						getDisplayName(usedParams[i])
				};
				getTM().addRow(objs);
			}
		}
	}
	
	public void stopTableEdit() {
		if (getTable().getCellEditor() != null) {
			getTable().getCellEditor().stopCellEditing();
		}
	}
	
	/**
	 * ��ģ��
	 */
	private class ParamFilterTableModel extends DefaultTableModel {
		private static final long serialVersionUID = 1L;

		public ParamFilterTableModel() {
			super(COLUMN_NAMES, 0);
		}
		
		public void removeParameter() {
			while (getRowCount()>0) {
				removeRow(0);
			}
		}
		
		public Object getValueAt(final int row, final int column) {
			//Parameter param = (Parameter) super.getValueAt(row, 0);
			Parameter param = getParam(row);
			if(param == null){
				return null;
			}
			switch (column) {
			case 0:
				return param.getName();
			case 1:
				return param.getCaption();
//			case 2:
//				return param.getHint();
			case 2:
				if(param.getOperaCode() == null){
					return VALUE_TYPES[0];
				}
				return (param.getOperaCode().equals(""+FilterItem.TYPE_EXP))?
						VALUE_TYPES[1]:VALUE_TYPES[0];
			case 3:
				return getDisplayName(param);
			default:
				break;
			}
			return null;
		}

		public void setValueAt(final Object aValue, final int row,
				final int column) {
			DefaultConstEnum obj = null;
			//Parameter param = (Parameter) super.getValueAt(row, 0);			
			Parameter pp = getParam(row);
			if(pp == null){
				return;
			}
			switch (column) {
			case 2:
				obj = ((DefaultConstEnum) aValue);
				pp.setOperaCode(obj.getValue() + "");
				break;
			case 3:
				//param.setValue(aValue);
				//NC���ս������Ѿ�����
//				if (!ParameterAnalyzer.isNCRef(pp)) {
					pp.setValue(aValue);
//				}
//				if (aValue == null || aValue.toString().trim().length() == 0) {
//					pp.setValue(aValue);
//					// ��ղ���ֵ,Ĭ��Ϊȫ��
//					super.setValueAt("", row, column);
//				} else {
//					if (pp.getDataType() == ParameterAnalyzer.REF_ID) {
//						super.setValueAt(getDisplayName(pp), row, column);
//					}
//				}
				break;
			default:
				break;
			}
			fireTableCellUpdated(row, column);
		}

		public void refreshData(Object[] rows){
			removeParameter();
			addRow(rows);
		}
		
		public boolean isCellEditable(final int rowIndex, final int columnIndex) {
			return (columnIndex == 2 || columnIndex == 3)?true:false;
		}
		
	}

	public Parameter getParam(final int row){
		if(row < 0 || row + 1 > usedParams.length){
			return null;
		}
		return usedParams[row];
	}
	
	public Hashtable<String, String> getParamCondFilters() {
		return paramCondFilters;
	}

	public void setParamCondFilters(Hashtable<String, String> paramCondFilters) {
		this.paramCondFilters = paramCondFilters;
	}

	@Override
	public void setDataSet(DataSet dataSet) {
		super.setDataSet(dataSet);
		initData();
	}

	public DataSet getDataSet() {
		return super.getDataSet();
	}
	
	private class ParamValueEditor extends AbstractCellEditor implements TableCellEditor {
		private static final long serialVersionUID = 1L;
		Component comp = null;

		AbsCG cg = null;
		int r = 0;
		
		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			ParamFilterTableModel dataModel = (ParamFilterTableModel)ParamCordFilterPanel.this.getTM();
			Parameter param = getParam(row);
			DefaultConstEnum obj = ((DefaultConstEnum) dataModel.getValueAt(row, 2));
			if(param == null){
				return null;
			}
			
			String strText = value != null?value + "" : "";
			int valuetype = new Integer(obj.getValue() + "").intValue();
			switch (valuetype) {
			case FilterItem.TYPE_CONST:
				r = row;
				cg = ParamCordFilterPanel.this.cgs[row];
				cg.setValue(strText);
				comp = cg.getControl();
				
				if(comp instanceof JTextField){
					if(param.getDataType() == ParameterAnalyzer.DECIMAL)
						((JTextField)comp).setDocument(new FloatDoc());
					((JTextField)comp).setText(strText);					
				}else if(comp instanceof JComboBox)
					((JComboBox)comp).setSelectedItem(strText);
				break;
			case FilterItem.TYPE_EXP:
				comp = new FmlRefTextField(cellsPane,getRowFilterExprFmlExecutor());
				if(comp instanceof JTextField){
					((JTextField)comp).setText(strText);	
				}
				break;
			default:
				return null;
			}
			return comp;
		}

		public Object getCellEditorValue() {
			if(!(comp instanceof JTextField) && cg != null && cg.getControl() instanceof UIRefPane){
				UIRefPane pane = (UIRefPane) cg.getControl();
				Parameter p = cg.getParam();
				String result = null;
				try{
					result = UIUtil.getRefResult(p.getOperaCode(), pane,p.getDataType().intValue());
				}catch(Exception ex){
					Logger.error(StringResource.getStringResource("miufohbbb00093"));
					result = pane.getUITextField().getText();
				}
				return result;
			}else if(comp instanceof JComboBox){
				return ((JComboBox) comp).getSelectedItem();
			} else if(comp instanceof JTextField){
				return ((JTextField)comp).getText();
			} else {
				return null;
			}
		}
		
		/**
		 * @i18n miufohbbb00093=���շ���ʧ��,��������Ϊ�û�����ķǷ��Ĳ���ֵ...
		 */
		@Override
		public boolean stopCellEditing(){
			return super.stopCellEditing();
		}
		
//		private ICustomRef initCustomRef(DataSet dataset) {
//			ICustomRef customRef = null;
//			if (dataset != null
//					&& dataset.getProvider() != null
//					&& dataset.getProvider().getRefModelClassName() != null
//					&& dataset.getProvider().getRefModelClassName().trim().length() > 0) {
//				String className = dataset.getProvider().getRefModelClassName();
//				try {
//					Class classObj = Class.forName(className);
//					Object obj = classObj.newInstance();
//					if (obj instanceof ICustomRef) {
//						customRef = (ICustomRef) obj;
//						customRef.setProvider(dataset.getProvider());
//					}
//				} catch (Exception e) {
//
//				}
//			}
//			return customRef;
//		}
	}

	/**
	 * @i18n miufo00916=��������
	 * @i18n miufo00617=δ��ȷ����ֵ
	 */
	public String check(){
//		ParamFilterTableModel dataModel = (ParamFilterTableModel)this.getTM();
//		int nRow = dataModel.getRowCount();
//		StringBuffer bufParamCord = new StringBuffer();
//		for(int row = 0; row < nRow; row++){
//			Parameter param = (Parameter) dataModel.getParam(row);
//			String strParamName = param.getCaption();
//			String strParamValue = (String)dataModel.getValueAt(row, 4);
//			if(strParamValue == null || strParamValue.trim().length() == 0){
//				if(bufParamCord.length() > 0) bufParamCord.append(",");
//				bufParamCord.append(strParamName);
//			}
//		}
//		if(bufParamCord.length() > 0){
//			bufParamCord.insert(0, StringResource.getStringResource("miufo00916"));
//			bufParamCord.append(StringResource.getStringResource("miufo00617"));
//		}
//		return bufParamCord.toString();
		return null;
	}
	
	/**
	 * ������������
	 * @return
	 */
	public String getParamCordFilter(){
		ParamFilterTableModel dataModel = (ParamFilterTableModel)this.getTM();
		int nRow = dataModel.getRowCount();
		StringBuffer bufParamCord = new StringBuffer();
		for(int row = 0; row < nRow; row++){
			//Parameter param = (Parameter) dataModel.getParam(row);
			Parameter param = usedParams[row];
			String strParamName = param.getCaption();
			String strParamValue = (String)dataModel.getValueAt(row, 3);
			if(strParamValue == null || strParamValue.trim().length() < 1){
				continue;//modify by wangya 2008-11-19 ����ֵΪ�գ�����ӹ�������
			}				
			bufParamCord.append("'");
			bufParamCord.append(strParamName);
			bufParamCord.append("'=");
			
			//@edit by wangyga at 2008-12-25 ����11:20:44 ���ʽ�������''
			boolean isExpr = param.getOperaCode().equals(""+FilterItem.TYPE_EXP);
			boolean isAddQuot = isAddQuotes(strParamName) && !isExpr;
			
			if(isAddQuot) bufParamCord.append("'");
			bufParamCord.append(strParamValue);
			if(isAddQuot) bufParamCord.append("'");
			if(row < nRow-1) bufParamCord.append(" AND ");
		}
		return bufParamCord.toString();
		
	}
	
	/**
	 * ����ÿ�ζ�ȥʵ������
	 * @return
	 */
	private UfoFmlExecutor getParamFilterExprFmlExecutor(){
		if(paramFilterExprFmlExecutor == null){
			paramFilterExprFmlExecutor = new ParamFilterExprFmlExecutor(
					getContext(), getCellsModel());
		}
		return paramFilterExprFmlExecutor;
	}
	
	/**
	 * ȥ���������
	 * @param str
	 * @return
	 */
	private String toDelQuotes(String str){
		if(str == null || str.length() == 0 || str.equals("''")){
			return "";
		}
		if(str.charAt(0) == '\'' && str.charAt(str.length()-1) == '\''){
			return str.substring(1, str.length()-1);
		} else{
			return str;
		}
	}
	
	private boolean isHasQuotes(String str){
		if(str == null || str.length() == 0 || str.equals("''")){
			return false;
		}
		if(str.charAt(0) == '\'' && str.charAt(str.length()-1) == '\''){
			return true;
		}
		return false;
	}
	
	/**
	 * ������ʾ�������ж��ǳ������Ǳ��ʽ:���ʽ������ϵͳ������Ч�Ĺ�ʽ,��������ֻ����ַ�����������
	 * 
	 * @param strDisplay
	 * @return Object
	 */
	private int getValueType(String strDisplay){
		if(strDisplay == null || strDisplay.trim().length() ==0)
			return FilterItem.TYPE_CONST;
		int startPos = strDisplay.indexOf("(");
		int endPos = strDisplay.indexOf(")");
		if(startPos < 0 || endPos <0)
			return FilterItem.TYPE_CONST;
		String strExpName = strDisplay.substring(0, startPos);
		UfoFmlExecutor rowFilterFmlExecutor = getParamFilterExprFmlExecutor();
		FuncListInst funcList = rowFilterFmlExecutor.getFuncListInst();
		if(funcList == null)
			return FilterItem.TYPE_CONST;
		UfoSimpleObject[] catList = funcList.getCatList();

		if (catList == null || catList.length == 0)
			return FilterItem.TYPE_CONST;

		for (UfoSimpleObject module : catList) {
			UfoSimpleObject[] m_FuncNameList = funcList.getFuncList(module
					.getID());
			if (m_FuncNameList == null || m_FuncNameList.length == 0)
				return FilterItem.TYPE_CONST;
			for (UfoSimpleObject funcNameObj : m_FuncNameList) {
				if (funcNameObj.getName().equalsIgnoreCase(strExpName))
					return FilterItem.TYPE_EXP;
			}
		}

		return FilterItem.TYPE_CONST;
	}
	
	/**
	 * ����ÿ�ζ�ȥʵ������
	 * @return
	 */
	private UfoFmlExecutor getRowFilterExprFmlExecutor(){
		if(rowFilterExprFmlExecutor == null){
             rowFilterExprFmlExecutor = new RowFilterExprFmlExecutor(
					(Context)getContext(), cellsPane.getDataModel());
		}
		return rowFilterExprFmlExecutor;
	}
	
	/**
	 * ����ֵ�Ƿ��ַ�����
	 * @param strParamName
	 * @return
	 */
	private boolean isAddQuotes(String strParamName){
		DataSet ds = super.getDataSet();
		if(ds == null || ds.getParameterByCapture(strParamName) == null){
			return false;
		}
		Parameter param = ds.getParameterByCapture(strParamName);
		if(param.getDataType() == ParameterAnalyzer.STRING ||
				param.getDataType() == ParameterAnalyzer.COMBO_STRING ||
				param.getDataType() == ParameterAnalyzer.REF_ID ||
				param.getDataType() == ParameterAnalyzer.REF_CODE ||
				param.getDataType() == ParameterAnalyzer.REF_NAME ||
				param.getDataType() == ParameterAnalyzer.REF_PARAM ||
				param.getDataType() == ParameterAnalyzer.REF_IUFO ||
				param.getDataType() == ParameterAnalyzer.REPLACE_PARAM){
			return true;
		}
		return false;
	}
		
	public class FloatDoc extends PlainDocument {
		private static final long serialVersionUID = 1L;
		public void insertString(int offs, String str, AttributeSet a)
		throws BadLocationException {
			String name = getText(0, offs) + str + getText(offs, getLength() - offs);
			 Pattern pa = Pattern.compile("(^(\\-){0,1})(\\d*)((\\.){0,1})(\\d*)$"); 
		        Matcher ma = pa.matcher(name); 
		        if(!ma.find())
		        	return;		   
			super.insertString(offs, str, a);
		}
	}
	
	private Parameter[] filterParameters(Parameter[] params){
		ArrayList<Parameter> filted = new ArrayList<Parameter>();
		if(params == null || params.length == 0)
			return params;
		for(Parameter p : params){
			if(p.getDataType() != ParameterAnalyzer.REF_PARAM){
				filted.add(p);
			}
		}
		return filted.toArray(new Parameter[]{});
	}
	
	/**
	 * @i18n miufohbbb00094=û������NC����Դ,�޷�����NC��������Դ
	 */
	private void initNCClient(DataSet dataset){
		if(dataset!=null && dataset.getProvider() != null){
			//��ʼ��NC�ͻ���,��Ҫ������Դ��Ϣ
			Context context = dataset.getProvider().getContext();
			if(context != null){
				Object ds = context.getAttribute(IQEContextKey.NC_DATA_SOURCE);
				if(ds!=null){
					InvocationInfoProxy.getInstance().setDefaultDataSource(ds.toString());
				}else{
					AppDebug.error(StringResource.getStringResource("miufohbbb00094"));
					throw new DSNotFoundException(NCLangRes.getInstance()
							.getStrByID("20081106", "upp08110601089"));
				}
			}else{
				AppDebug.error(StringResource.getStringResource("miufohbbb00094"));
				throw new DSNotFoundException(NCLangRes.getInstance()
						.getStrByID("20081106", "upp08110601089"));
			}
		}
	}
	
	private Object getDisplayName(Parameter param) {
		String disPlayName = null;
		if (param != null) {
			if (param.getDataType() != ParameterAnalyzer.REF_ID) {
				//@edit by yza at 2009-2-8 ����Ĭ��ֵ
				return param.getValue()==null?param.getDefaultValue():param.getValue();
			} else {
				//��֤��Ϊnull
				//
				disPlayName=param.getValue()==null?" ":param.getValue().toString();
				AbsCG cg = CGFactory.getFactory(param, getDataSet())
						.createDefaultValueCG();
				if (ParameterAnalyzer.isNCRef(param) && !param.getOperaCode().equals(String.valueOf(FilterItem.TYPE_EXP))) {
					UIRefPane pane = (UIRefPane) cg.getControl();
					String[] pks;
					if (QueryUtil.isInType(param.getOperaCode())) {
						pks = nc.vo.jcom.lang.StringUtil.split(
								(String) param.getDefaultValue(), ",");

						pane.setPKs(pks);
						String[] names = pane.getRefNames();
						int iLen = names == null ? 0 : names.length;
						for (int i = 0; i < iLen; i++) {
							disPlayName = disPlayName == null ? names[i]
									: disPlayName + "," + names[i];
						}
					} else {
						pane.setPK(param.getValue()==null?param.getDefaultValue():param.getValue());
						disPlayName = pane.getRefName();
					}
				} else {
					return param.getValue()==null?param.getDefaultValue():param.getValue();
				}
			}
		}
		return disPlayName;
	}
}
 