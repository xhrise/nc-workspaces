/*
 * �������� 2006-4-7
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.ui.bi.query.designer;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;

import nc.ui.bd.manage.UIRefCellEditor;
import nc.ui.iuforeport.businessquery.RowNoCellRenderer;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITablePane;
import nc.vo.bi.integration.dimension.DimRescource;
import nc.vo.bi.integration.dimension.DimensionVO;
import nc.vo.bi.query.manager.BIQueryModelDef;
import nc.vo.bi.query.manager.DimMaterialDef;
import nc.vo.bi.query.manager.MaterialInfoVO;
import nc.vo.bi.query.manager.MetaDataVO;
import nc.vo.pub.querymodel.QueryConst;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;

/**
 * @author zyjun
 *
 * TODO Ҫ���Ĵ����ɵ�����ע�͵�ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
public class SetMaterPanel extends AbstractQueryDesignSetPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String TAB_TITLE = "ubiquery0111";//"�ﻯ����";

	//����
	public final static int COL_FLDNAME = 1;

	public final static int COL_FLDDIM = 2;

	public final static int COL_FLDLEVEL = 3;



	//����
	public final static String FLD_NAME = "ubiquery0101";//"��ʾ��";

	//����
	public final static String FLD_DIMENSION = "ubiquery0107";//"ά��";

	public final static String FLD_LEVEL = "ubiquery0112";//"���";

	//����ά�ȶ���
//	private DimensionVO[] m_dims = null;

	private UITablePane ivjTablePn = null;
	
	//	private boolean		m_bMaterEnabled = false;
	private Hashtable<String, Object>		m_hashMater = new Hashtable<String, Object>();
	
	private boolean		m_bHashMaterInited = false;

	/**
	 *  
	 */
	public SetMaterPanel() {
		super();
		initialize();
	}

	/**
	 * ��ñ�� �������ڣ�(2005-5-17 20:02:35)
	 * 
	 * @return UITable
	 */
	public UITable getTable() {
		return getTablePn().getTable();
	}

	/**
	 * ���� TablePn ����ֵ��
	 * 
	 * @return UITablePane
	 */
	/* ���棺�˷������������ɡ� */
	private UITablePane getTablePn() {
		if (ivjTablePn == null) {
			try {
				ivjTablePn = new UITablePane();
				ivjTablePn.setName("TablePn");
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}
		}
		return ivjTablePn;
	}

	/**
	 * ��ñ��ģ�� �������ڣ�(2005-5-17 20:03:25)
	 * 
	 * @return javax.swing.table.DefaultTableModel
	 */
	private DefaultTableModel getTM() {
		return (DefaultTableModel) getTable().getModel();
	}

	/**
	 * ÿ�������׳��쳣ʱ������
	 * 
	 * @param exception
	 *            java.lang.Throwable
	 */
	private void handleException(java.lang.Throwable exception) {

		/* ��ȥ���и��е�ע�ͣ��Խ�δ��׽�����쳣��ӡ�� stdout�� */
		AppDebug.debug("--------- δ��׽�����쳣 ---------");//@devTools System.out.println("--------- δ��׽�����쳣 ---------");
		AppDebug.debug(exception);//@devTools exception.printStackTrace(System.out);
	}

	/**
	 * �����б༭�� �������ڣ�(2003-9-17 14:50:41)
	 */
	public void initEditorValue() {
		Object[] objsAttr = new Integer[DimRescource.INT_MAX_FLDPRE_NUMBER +1];
		objsAttr[0] = null;
		for( int i=1; i<objsAttr.length; i++ ){
			objsAttr[i] = new Integer(i);
		}
		//�������б༭��
		JComboBox cbbAttr = new UIComboBox(objsAttr);
		TableColumn tc = getTable().getColumnModel().getColumn(COL_FLDLEVEL);
		tc.setCellEditor(new UIRefCellEditor(cbbAttr));

	}

	/**
	 * ��ʼ���ࡣ
	 */
	/* ���棺�˷������������ɡ� */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("SetMaterPanel");
			setSize(480, 320);
			//add(getCheckBox(), "North");
			add(getTablePn(), "Center");
		} catch (java.lang.Throwable ivjExc) {
			handleException(ivjExc);
		}
		// user code begin {2}
		initTable();
		setTableCell();
		initEditorValue();
		// user code end
	}

	/**
	 * ��ʼ����� �������ڣ�(2005-5-17 20:04:22)
	 */
	@SuppressWarnings("serial")
	private void initTable() {
		DefaultTableModel tm = new DefaultTableModel(new Object[] { "",
				StringResource.getStringResource(FLD_NAME), 
				StringResource.getStringResource(FLD_DIMENSION), 
				StringResource.getStringResource(FLD_LEVEL) }, 0) {
			public int getColumnCount() {
				return 4;
			}

			public boolean isCellEditable(int row, int col) {
				return (col == COL_FLDLEVEL);
			}
		};
		getTable().setModel(tm);
		//���ñ�����
		getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getTable().getTableHeader().setBackground(QueryConst.HEADER_BACK_COLOR);
		getTable().getTableHeader().setForeground(QueryConst.HEADER_FORE_COLOR);
		getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		getTable().setColumnWidth(new int[] { 20, 160, 160, 160 });
	}

	/**
	 * ���þ��ж��� �������ڣ�(01-5-14 13:17:27)
	 */
	public void setTableCell() {
		DefaultTableCellRenderer renderer = null;
		for (int i = 0; i < getTable().getColumnCount(); i++) {
			TableColumn tc = getTable().getColumnModel().getColumn(i);
			if (i == 0)
				renderer = new RowNoCellRenderer();
			else
				renderer = new DefaultTableCellRenderer();
			renderer.setHorizontalAlignment(SwingConstants.CENTER);
			tc.setCellRenderer(renderer);
		}
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#getPanelTitle()
	 */
	public String getPanelTitle() {
		return TAB_TITLE;
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#getResultFromPanel(nc.vo.bi.query.manager.BIQueryModelDef)
	 */
	public void getResultFromPanel(BIQueryModelDef qmd) {
		//��ֹ�༭̬
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();
		
		MaterialInfoVO		materInfoVO = null;
		
		int 		iRowCount = getTable().getRowCount();
		ArrayList<DimMaterialDef>	aryDefs = new ArrayList<DimMaterialDef>();
		for (int i = 0; i < iRowCount; i++) {
			//���
			Integer		nLevel = (Integer)getTM().getValueAt(i, COL_FLDLEVEL);
			if( nLevel!= null ){
				//ά��PK��
				DimensionVO dimVO = (DimensionVO) getTM().getValueAt(i, COL_FLDDIM);
				aryDefs.add( new DimMaterialDef(dimVO.getDimID(), nLevel.intValue()) );
			}
		}
		if( aryDefs.size() >0 ){
			DimMaterialDef[]	defs = new DimMaterialDef[aryDefs.size()];
			aryDefs.toArray(defs);
			materInfoVO = new MaterialInfoVO(defs);
		}
		//����
		qmd.getAdvModel().setMaterialDef(materInfoVO);
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#setResultToPanel(nc.vo.bi.query.manager.BIQueryModelDef)
	 */
	public void setResultToPanel(BIQueryModelDef qmd) {

		if( m_bHashMaterInited == false){
			//����qmd�е���Ϣ����hashMater
			MaterialInfoVO	 materInfoVO  = qmd.getMaterialDef();
			if( materInfoVO != null ){
				DimMaterialDef[]	defs = materInfoVO.getMaterialDefs();
				for( int i=0; i<defs.length; i++ ){
					if( defs[i] != null ){
						m_hashMater.put(defs[i].getDimPK(), new Integer(defs[i].getMaterialLevel()));
					}
				}
			}			
			m_bHashMaterInited = true;
		}else{
			m_hashMater = getMaterInfoHash();
		}
		//���ģ���е����й����Ĺ���ά��
		MetaDataVO[]	mdataVOs = qmd.getMetadatas();
//		m_bMaterEnabled = true;
		if( mdataVOs != null ){
			ArrayList<MetaDataVO>	aryList = new ArrayList<MetaDataVO>(); 
			ArrayList<DimensionVO>	aryDims = new ArrayList<DimensionVO>();
			for( int i=0; i<mdataVOs.length; i++ ){
				if( mdataVOs[i].getPk_dimdef()!= null && mdataVOs[i].getPk_dimdef().length() >0){
					DimensionVO		dimVO = getDimVO(mdataVOs[i]);
					if( dimVO != null ){
						aryList.add(mdataVOs[i]);
						aryDims.add(dimVO);
					}
				}
			}
			
			getTM().setNumRows(aryList.size());
			for( int i=0; i<aryList.size(); i++ ){
				//����ģ��
				MetaDataVO	mDataVO = (MetaDataVO)aryList.get(i);
				getTM().setValueAt(mDataVO.getFldalias(), i, COL_FLDNAME);
				DimensionVO		dimVO = (DimensionVO)aryDims.get(i);
				getTM().setValueAt(dimVO, i, COL_FLDDIM);
//				if( m_hashMater.get(mDataVO.getPk_dimdef()) != null ){
					getTM().setValueAt( m_hashMater.get(mDataVO.getPk_dimdef()), i, COL_FLDLEVEL);

			}
			

		}
		//m_checkBoxMater.set
	}

	public Hashtable<String, Object>		getMaterInfoHash(){
		int iRowCount = getTable().getRowCount();
		for (int i = 0; i < iRowCount; i++){
			DimensionVO		dimVO = (DimensionVO)getTM().getValueAt(i, COL_FLDDIM);
			if(getTM().getValueAt(i, COL_FLDLEVEL) != null){
				m_hashMater.put(dimVO.getDimID(), getTM()
					.getValueAt(i, COL_FLDLEVEL));
			}else{
				m_hashMater.remove(dimVO.getDimID());
			}
		}
		return m_hashMater;
	}
	
	private DimensionVO  	getDimVO(MetaDataVO mDataVO){
		Hashtable hashFldDims = getTabPn().getMetaDataPanel().getHashFldDim();
		if( hashFldDims != null ){
			Object[]	objs = (Object[])hashFldDims.get(mDataVO.getFldalias());
			if( objs != null ){
				return (DimensionVO)objs[1];
			}
		}
		return null;
	}
	/**
	 * �Ϸ��Լ�� �������ڣ�(2003-4-4 14:00:39)
	 * 
	 * @return java.lang.String
	 */
	public String check() {

		return null;
	}
}
 