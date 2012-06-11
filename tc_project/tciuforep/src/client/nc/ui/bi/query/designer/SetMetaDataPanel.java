/*
 * �������� 2005-5-25
 *
 */
package nc.ui.bi.query.designer;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import nc.ui.bd.manage.UIRefCellEditor;
import nc.ui.iufo.resmng.common.UISrvException;
import nc.ui.iuforeport.businessquery.RowNoCellRenderer;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIButtonLayout;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UITable;
import nc.ui.pub.beans.UITablePane;
import nc.us.bi.integration.dimension.DimensionSrv;
import nc.vo.bi.integration.dimension.DimensionVO;
import nc.vo.bi.query.manager.BIModelUtil;
import nc.vo.bi.query.manager.BIQueryModelDef;
import nc.vo.bi.query.manager.MetaDataVO;
import nc.vo.pub.querymodel.QueryConst;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;

/**
 * @author zjb
 * 
 * Ԫ�������ý���
 */
public class SetMetaDataPanel extends AbstractQueryDesignSetPanel implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public final static String TAB_TITLE = "ubiquery0100";//"Ԫ����";

	// ����
	public final static int COL_FLDNAME = 1;

	public final static int COL_FLDALIAS = 2;

	public final static int COL_FLDTYPE = 3;
	
	public final static int COL_FLDPRECISION = 4;

	public final static int COL_FLDATTR = 5;

	public final static int COL_FLDDIM = 6;

	// ����
	public final static String FLD_NAME = "ubiquery0101";//"��ʾ��";

	public final static String FLD_ALIAS = "ubiquery0102";//"����";

	public final static String FLD_TYPE = "ubiquery0103";//"��������";

	public final static String FLD_LENGTH = "ubiquery0104";//"����";

	public final static String FLD_ATTR = "ubiquery0105";//"������";

	public final static String FLD_DIM = "ubiquery0106";//"����ά��";

	// ����
	public final static String DIMENSION = "ubiquery0107";//"ά��";

	public final static String MEASURE = "ubiquery0108";//"ָ��";
	
	

	// ����ά�ȶ���
	private DimensionVO[] m_dims = null;

	// �ֶ���ά��ӳ���ϵ
	private Hashtable<String, Object[]> m_hashFldDim = new Hashtable<String, Object[]>();

	private UITablePane ivjTablePn = null;

	private boolean		m_bEditable = false;
	private UIButton    m_btnAdd = null;
	private UIButton    m_btnDelete = null;
	private UIPanel  	m_btnPnl = null;
	private UIPanel     m_btnAddPnl = null;
	private UIPanel     m_btnDelPnl = null;
	
	/**
	 * 
	 */
	public SetMetaDataPanel(boolean bEditable) {
		super();
		m_bEditable = bEditable;
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
	
	private  UIPanel  getBtnPnl(){
		if( m_btnPnl == null ){
			try {
				GridLayout	layout = new GridLayout(2,1);
				m_btnPnl = new UIPanel(layout);
				m_btnPnl.setName("BtnPnl");
				m_btnPnl.setPreferredSize(new Dimension(100, 55));
				m_btnPnl.setMinimumSize(new Dimension(100, 55));
				// user code begin {1}
				m_btnPnl.add(getBtnAddPnl());
				m_btnPnl.add(getBtnDelPnl());
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}			
		}
		return m_btnPnl;
	}
	private  UIPanel  getBtnAddPnl(){
		if( m_btnAddPnl == null ){
			try {
				m_btnAddPnl = new UIPanel();
				m_btnAddPnl.setLayout(new UIButtonLayout());
				m_btnAddPnl.setName("BtnAddPnl");
				// user code begin {1}
				m_btnAddPnl.add(getBtnAdd(),getBtnAdd().getName());
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}			
		}
		return m_btnAddPnl;
	}
	private  UIPanel  getBtnDelPnl(){
		if( m_btnDelPnl == null ){
			try {
				m_btnDelPnl = new UIPanel();
				m_btnDelPnl.setLayout(new UIButtonLayout());
				m_btnDelPnl.setName("BtnDelPnl");
				// user code begin {1}
				m_btnDelPnl.add(getBtnDelete(),getBtnDelete().getName());
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}			
		}
		return m_btnDelPnl;
	}

	private  UIButton  getBtnAdd(){
		if( m_btnAdd == null ){
			try {
				m_btnAdd = new UIButton(StringResource.getStringResource("ubiquery0109"));//"����");
				m_btnAdd.addActionListener(this);
				m_btnAdd.setName("btnAdd");
				m_btnAdd.setPreferredSize(new Dimension(70, 22));
				m_btnAdd.setMinimumSize(new Dimension(88, 22));
				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}						
		}
		return m_btnAdd;
	}
	private  UIButton  getBtnDelete(){
		if( m_btnDelete == null ){
			try {
				m_btnDelete = new UIButton(StringResource.getStringResource("ubiquery0110"));//"ɾ��"
				m_btnDelete.setName("btnDel");
				m_btnDelete.addActionListener(this);
				m_btnDelete.setPreferredSize(new Dimension(70, 22));
				m_btnDelete.setMinimumSize(new Dimension(88, 22));

				// user code begin {1}
				// user code end
			} catch (java.lang.Throwable ivjExc) {
				// user code begin {2}
				// user code end
				handleException(ivjExc);
			}						
		}
		return m_btnDelete;
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
		TableColumnModel	tcModel = getTable().getColumnModel();
		Object[] objsAttr = new Object[] { 
								StringResource.getStringResource(DIMENSION), 
								StringResource.getStringResource(MEASURE) };
		// �������б༭��
		JComboBox cbbAttr = new UIComboBox(objsAttr);
		TableColumn tc = tcModel.getColumn(COL_FLDATTR);
		tc.setCellEditor(new UIRefCellEditor(cbbAttr));

		// ��ù���ά��
		DimensionVO[] dims = getDimDef();
		// ���ӿ���
		int iLen = (dims == null) ? 0 : dims.length;
		DimensionVO[] temps = new DimensionVO[iLen + 1];
		for (int i = 0; i < iLen; i++) {
			temps[i + 1] = dims[i];
		}
		temps[0] = getEmptyDimDef();
		dims = temps;
		// ����ά���б༭��
		JComboBox cbbDim = new UIComboBox(dims);
		tc = tcModel.getColumn(COL_FLDDIM);
		tc.setCellEditor(new UIRefCellEditor(cbbDim));
		
		//�������ͣ���ֵ���ַ�������
		Object[]	objTypes = new Object[]{"VARCHAR","DECIMAL"};
		JComboBox  ccbType = new UIComboBox(objTypes);
		tc = tcModel.getColumn(COL_FLDTYPE);
		tc.setCellEditor(new UIRefCellEditor(ccbType));
	}

	/**
	 * ��ʼ���ࡣ
	 */
	/* ���棺�˷������������ɡ� */
	private void initialize() {
		try {
			// user code begin {1}
			// user code end
			setName("SetMetaDataPanel");
			setSize(480, 320);
			add(getTablePn(), "Center");
			if( m_bEditable ){
				add(getBtnPnl(), "East");
			}
			//setMetaDataAddable(false);
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
		DefaultTableModel tm = new DefaultTableModel(
								new Object[] { "", 
										StringResource.getStringResource(FLD_NAME), 
										StringResource.getStringResource(FLD_ALIAS), 
										StringResource.getStringResource(FLD_TYPE), 
										StringResource.getStringResource(FLD_LENGTH), 
										StringResource.getStringResource(FLD_ATTR),
										StringResource.getStringResource(FLD_DIM) }, 0) {
			public int getColumnCount() {
				return 7;
			}

			public boolean isCellEditable(int row, int col) {
				if( m_bEditable ){
					return (col != 0);
				}else{
					return (col == COL_FLDNAME || col == COL_FLDATTR || col == COL_FLDDIM );
				}
			}
		};
		getTable().setModel(tm);
		// ���ñ�����
		getTable().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		getTable().getTableHeader().setBackground(QueryConst.HEADER_BACK_COLOR);
		getTable().getTableHeader().setForeground(QueryConst.HEADER_FORE_COLOR);
		getTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		getTable().setColumnWidth(new int[] { 20, 150, 150, 70, 70, 70,150 });
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
		// ��ֹ�༭̬
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();

		int iRowCount = getTable().getRowCount();
		MetaDataVO[] mds = new MetaDataVO[iRowCount];
		for (int i = 0; i < iRowCount; i++) {
			// ��������
			String colType = getTM().getValueAt(i, COL_FLDTYPE).toString();
			int iColType = Types.VARCHAR;
			if (!colType.trim().equals(""))
				iColType = BIModelUtil.convDB2DataType(colType);
			// ������
			boolean bDimflag = true;
			String colAttr = getTM().getValueAt(i, COL_FLDATTR).toString();
			if (colAttr.equals(StringResource.getStringResource(MEASURE)))
				bDimflag = false;
			// ����ά��
			DimensionVO dimdef = (DimensionVO) getTM().getValueAt(i, COL_FLDDIM);
			//
			mds[i] = new MetaDataVO();
			mds[i].setFldname(getTM().getValueAt(i, COL_FLDNAME).toString());
			mds[i].setFldalias(getTM().getValueAt(i, COL_FLDALIAS).toString());
			mds[i].setColtype(new Integer(iColType));
			mds[i].setDimflag(bDimflag);
			if( dimdef != null ){
				mds[i].setPk_dimdef(dimdef.getPrimaryKey());
				mds[i].setDimname(dimdef.getDimname());
			}else{
				mds[i].setPk_dimdef(null);
				mds[i].setDimname(null);
			}

			// �ֿ�
//			mds[i].setPrecision(m_metaDataVOs[i].getPrecision());
//			mds[i].setScale(m_metaDataVOs[i].getScale());
			//�����ɳ��Ⱥ;������
			String			strPrecision = getTM().getValueAt(i, COL_FLDPRECISION).toString();
			StringTokenizer	tokener = new StringTokenizer(strPrecision,",");
			int				nPrecision = 0;
			int				nScale = 0;
			nPrecision = Integer.parseInt(tokener.nextToken());
			if( tokener.hasMoreTokens() ){
				nScale = Integer.parseInt(tokener.nextToken());
			}
			mds[i].setPrecision(nPrecision);
			mds[i].setScale(nScale);

		}
		// ׷���������γ�� TODO
//		mds = appendAttrDims(mds);
		// ����
		qmd.setMetadatas(mds);
	}

	/**
	 * ΪԪ�����Զ��������γ��
	 * 
	 * @param mds
	 * @param iColType
	 * @return
	 */
	public static MetaDataVO[] appendAttrDims(MetaDataVO[] mds) {
		if (mds == null || mds.length == 0)
			return mds;
		ArrayList<String> al_allDims = new ArrayList<String>();
		ArrayList<MetaDataVO> al_exMetaVO = new ArrayList<MetaDataVO>();
		int realMdsIndex = 0;
		for (int i = 0; i < mds.length; i++) {
			if (mds[i].getPk_mainDimdef() != null && mds[i].getPk_mainDimdef().length() > 0) {
				if (realMdsIndex == 0)// ��¼��һ���Զ���ӵ�����γ�ȵ�λ��
					realMdsIndex = i;
			} else if (mds[i].getPk_dimdef() != null && mds[i].getPk_dimdef().length() > 0) {
				al_allDims.add(mds[i].getPk_dimdef());
			}
		}
		MetaDataVO[] realMds = null;// ȥ�������Զ���ӵ�����γ�ȣ����½��д���
		if (realMdsIndex == 0) {
			realMds = mds;
		} else {
			realMds = new MetaDataVO[realMdsIndex];
			System.arraycopy(mds, 0, realMds, 0, realMdsIndex);
		}

		int dimCount = al_allDims.size();
		if (dimCount == 0)// ���û��γ�ȣ�ֱ�ӷ���
			return realMds;

		for (int i = 0; i < realMds.length; i++) {
			if(al_allDims.contains(realMds[i].getPk_dimdef())){
			try {
				String[] attrDimIDs = DimensionSrv.getInstance().getPropDimIDs((String) al_allDims.get(i));
				if (attrDimIDs != null && attrDimIDs.length > 0) {
					for (int j = 0; j < attrDimIDs.length; j++) {
						if (!al_allDims.contains(attrDimIDs[j])) {
							DimensionVO attrDimVO = DimensionSrv.getInstance().getDimByID(attrDimIDs[j]);
							MetaDataVO meta = new MetaDataVO(attrDimVO.getDimcode(), attrDimVO.getDimname(),
									Types.VARCHAR);
							meta.setPk_dimdef(attrDimIDs[j]);
							meta.setDimname(attrDimVO.getDimname());
							meta.setPk_mainDimdef(realMds[i].getPk_dimdef());
							meta.setMainFldName(realMds[i].getFldalias());

							al_exMetaVO.add(meta);
							al_allDims.add(attrDimIDs[j]);
						}
					}
				}
			} catch (UISrvException ex) {
			} catch (Exception ex) {
			}
			}
		}
		if (al_exMetaVO.size() > 0) {
			MetaDataVO[] newMds = new MetaDataVO[realMds.length + al_exMetaVO.size()];
			System.arraycopy(realMds, 0, newMds, 0, realMds.length);
			for (int i = 0; i < al_exMetaVO.size(); i++) {
				newMds[realMds.length + i] = (MetaDataVO) al_exMetaVO.get(i);
			}
			return newMds;
		} else {
			return realMds;
		}
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see nc.ui.bi.query.designer.AbstractQueryDesignSetPanel#setResultToPanel(nc.vo.bi.query.manager.BIQueryModelDef)
	 */
	public void setResultToPanel(BIQueryModelDef qmd) {

		getTM().setNumRows(0);
		// ��ñ���
		MetaDataVO[] mds = qmd.getMetadatas();
		int iLen = (mds == null) ? 0 : mds.length;
		for (int i = 0; i < iLen; i++) {
			String fldName = mds[i].getFldname();
			String fldAlias = mds[i].getFldalias();
			// ��������
			int 	iFldType = (mds[i].getColtype() == null) ? Types.VARCHAR : mds[i].getColtype().intValue();
			String 	fldType = BIModelUtil.convDataType2DB(iFldType);
			String	strPrecision = null;
			if( mds[i].getScale() >0 ){
				strPrecision = mds[i].getPrecision()+","+mds[i].getScale();
			}else{
				strPrecision = ""+mds[i].getPrecision();
			}

			// ����ά����Ϣ
			String fldAttrID = null;
			DimensionVO dimdef = null;
			if (m_hashFldDim.containsKey(fldAlias)) {
				Object[] objs = (Object[]) m_hashFldDim.get(fldAlias);
				boolean bDimflag = ((Boolean) objs[0]).booleanValue();
				fldAttrID = bDimflag ? DIMENSION: MEASURE;
				dimdef = (DimensionVO) objs[1];
			} else {
				fldAttrID = DIMENSION;
				dimdef = getEmptyDimDef();
			}
			String fldAttr = StringResource.getStringResource(fldAttrID);
			//
			Object[] objs = new Object[] { "", fldName, fldAlias, fldType, strPrecision,fldAttr, dimdef };
			getTM().addRow(objs);
		}
	}

	/**
	 * @return ���� DimDefVO[]��
	 */
	public DimensionVO[] getDimDef() {
		if (m_dims == null)
			try {
				m_dims = DimensionSrv.getInstance().getAllDimVOs();
			} catch (Exception e) {
				AppDebug.debug(e);
			}
		return m_dims;
	}

	/**
	 * @return ���� DimDefVO[]��
	 */
	public DimensionVO getEmptyDimDef() {
		DimensionVO dimdef = new DimensionVO();
		dimdef.setPrimaryKey("");
		dimdef.setDimname("");
		return dimdef;
	}

	/**
	 * @return ���� hashFldDim��
	 */
	public Hashtable getHashFldDim() {
		return m_hashFldDim;
	}

	/**
	 * @param hashFldDim
	 *            Ҫ���õ� hashFldDim��
	 */
	public void setHashFldDim(Hashtable<String, Object[]> hashFldDim) {
		m_hashFldDim = hashFldDim;
	}

	/**
	 * @param hashFldDim
	 *            Ҫ���õ� hashFldDim��
	 */
	public void refreshHashFldDim() {
		m_hashFldDim.clear();
		int iLen = getTable().getRowCount();
		for (int i = 0; i < iLen; i++) {
			// �б���
			String fldAlias = getTM().getValueAt(i, COL_FLDALIAS).toString();
			// ������
			boolean bDimflag = true;
			String colAttr = getTM().getValueAt(i, COL_FLDATTR).toString();
			if (colAttr.equals(StringResource.getStringResource(MEASURE)) )
				bDimflag = false;
			// ����ά��
			DimensionVO dimdef = (DimensionVO) getTM().getValueAt(i, COL_FLDDIM);
			// ά���ֶ�-ά�ȹ�ϣ��
			m_hashFldDim.put(fldAlias, new Object[] { new Boolean(bDimflag), dimdef });
		}
	}

	/**
	 * �Ϸ��Լ�� �������ڣ�(2003-4-4 14:00:39)
	 * 
	 * @return java.lang.String
	 */
	public String check() {
		// ��ֹ�༭̬
		String  strErrMsg = checkOnSwitch();
		if( strErrMsg == null ){
			int iLen = getTM().getRowCount();
			if (iLen == 0)
				return StringResource.getStringResource("mbiquery0106");//"No metadata";
		}
		return strErrMsg;
	}

	public String checkOnSwitch(){
		if (getTable().getCellEditor() != null)
			getTable().getCellEditor().stopCellEditing();

		int iLen = getTM().getRowCount();
		for (int i = 0; i < iLen; i++) {	
			// ��ʾ�����
			String fldname = getTM().getValueAt(i, COL_FLDNAME).toString();
			if (fldname.trim().equals(""))
				return StringResource.getStringResource("mbiquery0107",
						new String[]{Integer.toString(i+1)});//"Display name can't be empty";
			// �������
			String fldalias = getTM().getValueAt(i, COL_FLDALIAS).toString();
			if (fldalias.trim().equals(""))
				return StringResource.getStringResource("mbiquery0108",
						new String[]{Integer.toString(i+1)});//"Alias name can't be empty";
			
			//�ж��Ƿ�������
			if( fldalias.length() <fldalias.getBytes().length){
				return StringResource.getStringResource("mbiquery0126",
						new String[]{Integer.toString(i+1)});//�������ܰ�������
			}
			
			//���ά���Ƿ����ظ�
			String	strDimPK = getDimPK(i);
			if( strDimPK != null && strDimPK.length()>0 ){
				for( int j=i+1; j<iLen; j++ ){
					if( strDimPK.equals(getDimPK(j))){
						return StringResource.getStringResource("mbiquery0133",
							new String[]{Integer.toString(i+1), Integer.toString(j+1)});
					}
				}
			}
			if( m_bEditable ){
				boolean  bDim = true;
				if( getTM().getValueAt(i, COL_FLDATTR).toString().equals(StringResource.getStringResource(MEASURE)) ){
					bDim = false;
				}
				//���ƺͱ��������ظ�
				for( int j=i+1; j<iLen; j++ ){
					if( fldalias.equals(getTM().getValueAt(j, COL_FLDALIAS).toString())){
						return StringResource.getStringResource("mbiquery0134",
								new String[]{Integer.toString(i+1), Integer.toString(j+1)});
					}
				}
				//������Ͳ���Ϊ�գ�
				String	strType = getTM().getValueAt(i, COL_FLDTYPE).toString();
				if( strType.trim().equals("")){
					return StringResource.getStringResource("mbiquery0100", 
							new String[]{Integer.toString(i+1)});//"type can't be empty";
				}else if( bDim && strType.equalsIgnoreCase("VARCHAR") == false){
					return StringResource.getStringResource("mbiquery0101", 
							new String[]{Integer.toString(i+1)});//"attr and type conflict";
				}else if( bDim == false && strType.equalsIgnoreCase("VARCHAR")){
					return StringResource.getStringResource("mbiquery0101", 
							new String[]{Integer.toString(i+1)});
				}
				//��鳤�Ȳ���Ϊ��,����Ϊ����
				String	strPrecision = getTM().getValueAt(i, COL_FLDPRECISION).toString();
				if( strPrecision.trim().equals("")){
					return StringResource.getStringResource("mbiquery0102", 
							new String[]{Integer.toString(i+1)});//"precision can't be empty";
				}else{
					try{
						StringTokenizer	tokener = new StringTokenizer(strPrecision,",");
						if( Integer.parseInt(tokener.nextToken()) <0 ){
							return StringResource.getStringResource("mbiquery0131",
									new String[]{Integer.toString(i+1)});
						}
						if( tokener.hasMoreTokens() ){
							if( Integer.parseInt(tokener.nextToken())<0 ){
								return StringResource.getStringResource("mbiquery0132",
										new String[]{Integer.toString(i+1)});
							}
						}
					}catch(NumberFormatException e){
						return StringResource.getStringResource("mbiquery0103", 
								new String[]{Integer.toString(i+1)});//"precision format error";
					}
				}

				//���ά�ȵ�ѡ����Ϊ��
				if( bDim && strDimPK == null){
					return StringResource.getStringResource("mbiquery0104", 
							new String[]{Integer.toString(i+1)});//"dimension can't be empty";
				}else if( bDim == false && (strDimPK != null && strDimPK.length()>0)){
					return StringResource.getStringResource("mbiquery0105", 
							new String[]{Integer.toString(i+1)});//"you shouldn't select dimension";
				}
			}
			
		}
		return null;
	}
	/**
	 * ����ֶα���-��ʾ����ϣ�� �������ڣ�(2003-4-3 16:16:01)
	 * 
	 * @return nc.vo.iuforeport.businessquery.FromTableVO[]
	 */
	public Hashtable getHashAliasDisp() {
		Hashtable<Object, Object> hashAliasDisp = new Hashtable<Object, Object>();
		int iRowCount = getTable().getRowCount();
		for (int i = 0; i < iRowCount; i++)
			hashAliasDisp.put(getTM().getValueAt(i, COL_FLDALIAS), getTM().getValueAt(i, COL_FLDNAME));
		return hashAliasDisp;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == getBtnAdd()){
			// ��ֹ�༭̬
			if (getTable().getCellEditor() != null)
				getTable().getCellEditor().stopCellEditing();
			//���ͱ�������
			Object[]	objs = new Object[]{"","","","", "",""};
			getTM().addRow(objs);
		}else if (e.getSource() == getBtnDelete()){
			//�õ���ǰѡ�е���
			// ��ֹ�༭̬
			if (getTable().getCellEditor() != null)
				getTable().getCellEditor().stopCellEditing();
			int		nRow = getTable().getSelectedRow();
			if( nRow >=0 ){
				getTM().removeRow(nRow);
			}
		}
	};
	
	private String getDimPK(int nIndex){
		DimensionVO		dimVO = (DimensionVO)getTM().getValueAt(nIndex, COL_FLDDIM);
		if( dimVO != null ){
			return dimVO.getDimID();
		}
		return null;
	}
} 