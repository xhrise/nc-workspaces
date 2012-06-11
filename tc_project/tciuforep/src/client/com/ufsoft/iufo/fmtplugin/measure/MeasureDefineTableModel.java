package com.ufsoft.iufo.fmtplugin.measure;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

import nc.pub.iufo.cache.MeasureCache;
import nc.pub.iufo.cache.ReportCache;
import nc.pub.iufo.exception.CommonException;
import nc.ui.iufo.measure.MeasCompParser;
import nc.vo.iufo.code.CodeVO;
import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.iufo.measure.HBBBMeasParser;
import nc.vo.iufo.measure.MeasureVO;
import nc.vo.iuforeport.rep.ReportVO;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.datastate.CellsModelOperator;
import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.fmtplugin.formatcore.CacheProxy;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaVO;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.format.Format;
import com.ufsoft.table.format.TableConstant;
import com.ufsoft.report.util.MultiLang;

/**
 * 指标提取时的操作数据
 * 
 * @author：王海涛
 */
public class MeasureDefineTableModel extends AbstractTableModel {
    /**
     * <code>serialVersionUID</code> 的注释
     */
    private static final long serialVersionUID = -8246854249111088685L;

    public static final int FLAG_COLUMN = 0; //提取标志

    public static final int REFERENCE_COLUMN = 1; //引用（所属报表不是本表，则是被引用状态）

    public static final int REPORT_CODE = 2; //所属报表

    public static final int ACT_CELL_COLUMN = 3; //实际单元

    public static final int NAME_COLUMN = 4; //指标名称

    public static final int TYPE_COLUMN = 5; //指标类型

    public static final int LENGTH_COLUMN = 6; //指标长度

//    public static final int DECIMAL_DIGITS = 7; //小数位数

    public static final int CODEREFERENCE_COLUMN = 7; //编码引用

    public static final int NOTE_COLUMN = 8; //说 明

    public static final int HEBING_COLUMN = 9; //合并指标

    public static final int DIRECTION_COLUMN = 10; //方向

    public static final int DXTYPE_COLUMN = 11; //抵消类型

    //以下两个参数是为指标提取的参照按钮而设置,用来控制参照按钮的可用禁用
    //	public static final int KEY_REF_COLUMN = 15;
    public static final int MEASURE_REF_COLUMN = 16;

//    private UfoReport m_ufoReport = null; //判断当前报表中是否含有可变指标时用

    private Vector m_oMVector = new Vector();//MeasurePosVO
    //	private EntityList m_oTableMeasList = null;
    // //报表中已有的指标，如果选中的区域是可变区，则为该可变区中的指标

    public static final MeasureColumnModel columnNames[] = {
            new MeasureColumnModel(StringResource.getStringResource("miufopublic285")+"/"+
            		StringResource.getStringResource("miufo1000160"), 60, JLabel.CENTER), //"指
                                                                            // 标"
            new MeasureColumnModel(StringResource
                    .getStringResource("miufo1001490"), 50, JLabel.CENTER), //"引
                                                                            // 用"
            new MeasureColumnModel(StringResource
                    .getStringResource("miufo1001530"), 84, JLabel.CENTER), //"所属报表"
            //		   new
            // MeasureColumnModel(StringResource.getStringResource("miufo1001491"),50,
            // JLabel.CENTER), //"关 键 字"
            new MeasureColumnModel(StringResource
                    .getStringResource("miufo1001492"), 84, JLabel.CENTER), //"实际单元"
            //		   new
            // MeasureColumnModel(StringResource.getStringResource("miufo1001493"),84,
            // JLabel.CENTER), //"相对单元"
            new MeasureColumnModel(StringResource
                    .getStringResource("miufo1001051"), 84, JLabel.CENTER), //"名
                                                                            // 称"
            new MeasureColumnModel(StringResource
                    .getStringResource("miufo1001494"), 84, JLabel.CENTER), //"类
                                                                            // 型"
            //		   new
            // MeasureColumnModel(StringResource.getStringResource("miufo1001495"),84,
            // JLabel.CENTER), //"指标单位"
            new MeasureColumnModel(StringResource
                    .getStringResource("miufo1001496"), 84, JLabel.CENTER), //"长
                                                                            // 度"
            new MeasureColumnModel(StringResource
                    .getStringResource("miufo1001497"), 84, JLabel.CENTER), //"代码选择"
            new MeasureColumnModel(StringResource
                    .getStringResource("miufo1001052"), 84, JLabel.CENTER), //"说
                                                                            // 明"
            new MeasureColumnModel(StringResource
                    .getStringResource("miufo1001498"), 84, JLabel.CENTER), //"合
                                                                            // 并"
            new MeasureColumnModel(StringResource
                    .getStringResource("miufo1001499"), 84, JLabel.CENTER), //"方
                                                                            // 向"
            new MeasureColumnModel(StringResource
                    .getStringResource("miufo1001500"), 84, JLabel.CENTER) }; //"期初合并"

    public static final String[] TYPES = {
            StringResource.getStringResource("miufopublic265"),
            StringResource.getStringResource("miufopublic266"),
            StringResource.getStringResource("miufopublic283"),
            StringResource.getStringResource("miufopublic315")}; //@i18n miufopublic315=日期

    public static final int[] TYPESLEN = { 0, 64, 0, 10};//指标类型对应的缺省长度

    public static final int[] TYPESUNIT = { 2, -1, -1, -1};//指标类型对应的units.

    public static final String[] UNITS = {
            StringResource.getStringResource("miufo1001501"),
            StringResource.getStringResource("miufo1001502"),
            StringResource.getStringResource("miufo1001503") };

    public static final String[] HEBING = {
            StringResource.getStringResource("miufo1001504"),
            StringResource.getStringResource("miufopublic267") };

    public static final String[] DIRECTION = { "",
            StringResource.getStringResource("miufopublic287"),
            StringResource.getStringResource("miufopublic288") };

    public static final String[] DXTYPE = {
            StringResource.getStringResource("miufo1001454"),
            StringResource.getStringResource("miufo1001453") };

    private Hashtable m_codes;//当前指标列中的所有编码

    private String UnitID = null;

    private java.lang.String m_strReportId;

    private MeasureDefineDialog parentDialog = null;

    public KeyVO m_rowIndexKey = null;//行号关键字

    /**
	 * @i18n uiuforep00127=行号
	 */
    public static final String KEY_NAME_ROW_INDEX = MultiLang.getString("uiuforep00127");// StringResource.getStringResource("miufo1001508");
                                                           // //"行号"

    private DynAreaVO m_oDynArea = null;//本次指标提取如果是在动态区域内提取，则记录当前动态区域vo

    //动态区域所对应的关键字组合
    private KeyGroupVO m_oKeyGroupVO;

    private Hashtable m_oCreateKey;

    //记录初始时，指标提取操作在动态区域中选择的范围内所包含的初始关键字
    private Vector m_oOriUfKeyword;

    //记录关键字应用后的关键字组合，如果应用过，则在点击确定时，就不再保存关键字了
    private KeyGroupVO m_boKeyGroupIsApply = null;

    /**
     * 指标命名空间数组，分别表示行可用名称和列可用名称
     */
    private String[] m_strsRowNames = null;

    private String[] m_strsColNames = null;

    //命名空间对应的起始行列
    private int m_iStartRow = -1;

    private int m_iStartCol = -1;

    //指标提取中已经命名的指标名称集合，对于大批量的指标提取，使用Hashcode进行查找比较快
    private HashSet m_nameHashset = null;

    //报表中已定义的指标名成集合
    private HashSet m_HasAllNameHashset = null;

    private boolean m_bIsAnaRep = false;

    private boolean m_bNowEditing = false;

    //指标参照后的判断是否重复的结果状态值
    public static int MEASREF_NO_ERR = 0;//没有重复

    public static int MEASREF_NAME_REPETITION_ERR = 1;//名称重复

    public static int MEASREF_MEAS_REPETITION_ERR = 2;//参照的指标重复

    private ReportCache m_ReportCache = CacheProxy.getSingleton()
            .getReportCache();

    private CellsModel m_cellsModel = null;
    
    private CellPosition[] m_cells = null;

    /**
     * TargerData 构造子注解。
     */
    public MeasureDefineTableModel(String reportId, CellsModel cellsModel, CellPosition[] selPoss, boolean isAnaRep) {
        super();
        this.m_bIsAnaRep = isAnaRep;
        if (m_oMVector != null)
            m_oMVector.removeAllElements();
        //    this.m_ufoReport = ufoReport;
        m_strReportId = reportId;
        m_cellsModel = cellsModel;
        m_cells = selPoss;
        //  初始化 m_oMVector
        for (int i = 0; i < selPoss.length; i++) {
            MeasureVO mvo = getMeasureModel().getMeasureVOByPos(selPoss[i]);
            MeasurePosVO mtvo = new MeasurePosVO();
            mtvo.setActPos(selPoss[i].toString());
            //        mtvo.setRelPos(getRelativePos(dynAreaVO, selPoss[i]));
            if (mvo == null) {
            	mtvo.setState(MeasurePosVO.MEASURE_TABLE_S_CREATE);
                mvo = new MeasureVO();
                mtvo.setMeasureVO(mvo);
                mvo.setReportPK(m_strReportId);
         
                String code = MeasureCache.getNewMeasurePK(getMeasruePackPK(selPoss[i]));
                mvo.setCode(code);
                createMeasureName(selPoss[i], mvo,false);
              //add by ll, 2007-11-06，按照单元属性设置指标属性
                Format tmp = cellsModel.getRealFormat(selPoss[i]);
                if(tmp == null || tmp.getCellType() != TableConstant.CELLTYPE_STRING)
                	mvo.setType(MeasureVO.TYPE_NUMBER);
                else{
                	mvo.setType(MeasureVO.TYPE_CHAR);
                	//add by wangyga 2008-7-9 如果单元属性是字符，提取指标时长度应该是64
                    mvo.setLen(TableConstant.CELLTYPE_STRING_LENGTH);
                }               	
                mvo.setAttribute(MeasureVO.ATTR_COIN_YUAN);

            } else {
            	//update by guogang 2007-8-15 克隆指标对象，避免对象指针引用的时候对话框的任何操作都会更改缓存内的对象
            	MeasureVO measureVo = (MeasureVO)mvo.clone();
                mtvo.setMeasureVO(measureVo);
                mtvo.setRefedMeasure(new Boolean(!m_strReportId.equals(mvo.getReportPK())));
//                if(!mtvo.getRefedMeasure()){//modify by wangyga 2008-7-18 防止添加了引用指标，取消后指标的名称是原来的引用指标的编码
//                	createMeasureName(selPoss[i], measureVo);
//                }
                if(mtvo.getRefedMeasure()){//提取指标界面出现之后(和上边的情况不一样)，再取消引用，防止取消引用指标后，原来的指标名称没有保存，导致名称还是显示的引用指标的编码
                	MeasureVO msVo = (MeasureVO)measureVo.clone();//防止修改掉原指标对象里的名称              	
                	createMeasureName(selPoss[i], msVo,false);//再构建一个自动生成的指标名称，保存在mtvo里，弹出指标提取对话框之后，用于取消引用，还原名称
                	mtvo.setOriginalMeasureName(msVo.getName());
                }
            }
            m_oMVector.add(mtvo);
        }

        //    if (dynarea != null) {
        //        //动态区域指标提取
        //        this.m_oDynArea = dynarea;
        //        this.m_oTableMeasList = dynarea.getMeasureList();
        //      	if(m_oTableMeasList == null )
        //      	{
        //	      	m_oTableMeasList = new EntityList();
        //	      	
        //      	}
        //        //系统自动生成一个“行号”关键字,如果该关键字已经在该动态区域内存在，则直接引用
        //        Vector keys = m_oDynArea.getKeyVO();
        //        //该关键字组合只记录动态区域内定义的关键字信息,并不记录主表中的关键字信息
        //		this.m_oKeyGroupVO = new KeyGroupVO();
        //		Vector dynkeys = m_oDynArea.getKeyVO();
        //		if( dynkeys != null && dynkeys.size() > 0 )
        //		{
        //            KeyVO[] vos = new KeyVO[dynkeys.size()];
        //            dynkeys.copyInto(vos);
        //            m_oKeyGroupVO.addKeyToGroup(vos);
        //		}
        //如果动态区内没有定义过指标和关键字，则自动生成行号关键字
        //        if(m_oTableMeasList.getEntityCount()==0&&(keys ==
        // null||keys.size()==0))
        //        {
        //	        CellProperty cp;
        //			//如果是表扬单元，直接返回
        //			UfoArea area = new UfoArea(dynarea.getPos());
        //
        //			cp = ufotable.getFormatModel().getValueAt(area.getStartCell());
        //			if((cp == null||cp.getDataType() !=
        // CellDataType.CELL_SAMPLE)&&(!IsAnaRep()) )//表样单元不提取
        //			{
        //	            m_rowIndexKey = new KeyVO();
        //	            m_rowIndexKey.setName(KEY_NAME_ROW_INDEX);
        //	            m_rowIndexKey.setType(KeyVO.TYPE_CHAR);
        //	            m_rowIndexKey.setLen(64);
        //	            m_rowIndexKey.setIsPrivate(true);
        //	            m_rowIndexKey.setKeywordPK(com.ufsoft.iuforeport.reporttool.toolkit.IDMaker.makeID(20));
        //	            m_rowIndexKey.setIsStop(new nc.vo.pub.lang.UFBoolean(false));
        //	            m_rowIndexKey.setIsBuiltIn(new nc.vo.pub.lang.UFBoolean(false));
        //	            m_rowIndexKey.setRepIdOfPrivate(m_strReportId);
        //
        //	            UfoKeyWord ufokey = new UfoKeyWord();
        //				ufokey.setCell(new UfoCell("A1"));
        //				ufokey.setKeyVO(m_rowIndexKey);
        //				if( m_oCreateKey == null )
        //					m_oCreateKey = new Hashtable();
        //				m_oCreateKey.put( Integer.toString(0) ,ufokey );
        //				addToKeyGroup(m_rowIndexKey);
        //
        //				MeasurePosVO mvo = new MeasurePosVO();
        //				MeasureVO vo = new MeasureVO();
        //				vo.setName(m_rowIndexKey.getName());
        //				
        //				//liuyy. 2005-1-13
        //				//根据指标组PK生成指标PK
        //				String strMeasurePK = null;
        //				 String strMeasPackPK = dynarea.getMeasPackPK();
        //				 try {
        //				     MeasureCache mCache = null;
        //					 mCache = UICacheManager.getSingleton().getMeasureCache();
        //                    strMeasurePK = mCache.getNewMeasurePK(strMeasPackPK);
        //                } catch (Exception e) {
        //                    AppDebug.debug(e);
        //                    strMeasurePK = strMeasPackPK +
        // com.ufsoft.iuforeport.reporttool.toolkit.IDMaker.makeID(MeasurePackVO.MEASURE_ID_LENGTH);
        //                }
        //				vo.setCode(strMeasurePK);
        //				
        //				vo.setLen(m_rowIndexKey.getLen());
        //				vo.setNote(m_rowIndexKey.getNote());
        //				vo.setType(m_rowIndexKey.getType());
        //				vo.setAttribute(-1);
        //				mvo.setMeasureVO(vo);
        //				mvo.setFlag(Boolean.FALSE);
        //				mvo.setKeyFlag(Boolean.TRUE);
        //				mvo.setPos(ufokey.getCell().getName());
        //				mvo.setActPos(new UfoArea(dynarea.getPos()).Start.getName());
        //				mvo.setState(MeasurePosVO.MEASURE_TABLE_S_CREATE);
        //				mvo.setState(MeasurePosVO.MEAS_S_DELTA_TO_KEY);
        //	            m_oMVector.addElement(mvo);
        //			}
        //        }
        //    } else {
        //        this.m_oTableMeasList =
        // m_ufoReport.getFormatModel().getEntityListFormat(TableVOTypes.MEASURE);
        //        if(m_oTableMeasList == null)
        //        {
        //	        m_oTableMeasList = new EntityList();
        //	        m_ufoReport.getFormatModel().setEntityListFormat(TableVOTypes.MEASURE,m_oTableMeasList);
        //        }
        //    }
    }
    
    public void updateAllMeasureName(boolean isCellPos){
    	if(m_oMVector!=null){
    		MeasurePosVO mtvo=null;
    		for(int i=0;i<m_oMVector.size();i++){
    			mtvo=(MeasurePosVO)m_oMVector.get(i);
    			createMeasureName(CellPosition.getInstance(mtvo.getActPos()), mtvo.getMeasureVO(),isCellPos);
    		}
    	}
    	
    }
    
    private String getMeasruePackPK(CellPosition cellPos){
        DynAreaCell dynAreaCell = getDynAreaModel().getDynAreaCellByPos(cellPos);
        String measurePackPK = null;
        if(dynAreaCell!=null){
            m_oDynArea = getDynAreaModel().getDynAreaCellByPos(cellPos).getDynAreaVO();
            String dynAreaPK = m_oDynArea.getDynamicAreaPK();
             measurePackPK = CellsModelOperator.getMeasureModel(m_cellsModel).getDynAreaMeasurePackPK(dynAreaPK);
        }else{
        	measurePackPK = CellsModelOperator.getMeasureModel(m_cellsModel).getMainMeasurePackPK();
        }    
        return measurePackPK;
    }
    private DynAreaModel getDynAreaModel() {
		return DynAreaModel.getInstance(m_cellsModel);
	}
	/**
     * 根据区域area，以该区域为基点，寻找该区域左边、上边单元的第一个表样的值，构成指标名称 如果area是一个单元，则直接可以查找
     * 如果area是一个区域，则按照area的左上角单元所在行列进行寻找，直到找到，
     * 由于在同一报表下指标名称不允许重复，当出现此种情况时，新提取的重名指标名称命名为系统自动生成一个名称
     * 如果该区域在查找后，在行列上都没有表样单元或名称为空字符串，则系统自动生成一个名称 创建日期：(2003-8-14 14:21:19)
     */
    private void createMeasureName(CellPosition pos, MeasureVO vo,boolean isCellPos) {
        StringBuffer msrName = new StringBuffer();
        if(!isCellPos){
        	 msrName.append(getFirstSample(pos, 0, -1));
             msrName.append(getFirstSample(pos, -1, 0));
        }
       
        String strNewName=msrName.toString();
        if(strNewName.length()==0)
        	strNewName=pos.toString();
        while(hasDefedMeasureName(vo,strNewName)){
        	strNewName=strNewName+pos.toString();
        }
        vo.setName(strNewName);
//        vo.setName(msrName.toString());
//        if (vo.getName().length() == 0 || hasDefedMeasureName(vo,vo.getName())) {
//            vo.setName(vo.getName() + pos.toString());
//        }
    }
    private String getFirstSample(CellPosition cellPos,int dRow,int dCol){
        //查找行左边的第一个表样单元.
    	int row = cellPos.getRow()+dRow;
    	int col = cellPos.getColumn()+dCol;
    	while(row>=0 && col>=0){
    		Cell cell = m_cellsModel.getCell(row,col);
    		if(cell!=null && cell.getValue() != null && cell.getValue() instanceof String){
    			String sample = cell.getValue().toString();
//    			sample = eraseIlleagelChar(sample);
    			if(sample.length() != 0) {
//    				if(!isAllNumber(sample)){
    					return sample;
//    				}
    			}
    		}    		
    		row+=dRow;
    		col+=dCol;
    	}
    	return "";
    }
    private boolean isAllNumber(String sample) {
    	for(int i=0;i<sample.length();i++){
    		char ch = sample.charAt(i);
    		if(Character.isDigit(ch) || isChineseNum(ch)){
    			continue;
    		}
    		return false;
    	}
		return true;
	}
	private boolean isChineseNum(char ch) {
    	char[] chineseNums = new char[]{'一','二','三','四','五','六','七','八','九','零','十'};
    	for(char chineseNum : chineseNums){
    		if(chineseNum == ch){
    			return true;
    		}
    	}
		return false;
	}
	/**
     * 返回本表是否有重复名称的指标,除自身外.
     * 
     * @param voParam
     * @return boolean
     */
    private boolean hasDefedMeasureName(MeasureVO voParam,String newMeasureName) {
        Collection list = this.getMeasureModel().getMeasureVOPosByAll().values();
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            MeasureVO mvo = (MeasureVO) iter.next();
            if (newMeasureName.equalsIgnoreCase(mvo.getName())
                    && !voParam.getCode().equalsIgnoreCase(mvo.getCode())) {
                return true;
            }
        }

        for (Iterator iter = m_oMVector.iterator(); iter.hasNext();) {
            MeasurePosVO mtvo = (MeasurePosVO) iter.next();
            MeasureVO mvo = mtvo.getMeasureVO();
            if (newMeasureName.equalsIgnoreCase(mvo.getName())
                    && !voParam.getCode().equalsIgnoreCase(mvo.getCode())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 去除非法字符,只保留字符、数字、汉字。
     * 
     * @param name
     * @return String
     */
    private String eraseIlleagelChar(String name) {
        if (name == null) {
            return "";
        }
        name = name.trim();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < name.length(); i++) {
            char c = name.charAt(i);
            if (Character.isLetterOrDigit(c)) {
                sb.append(c);
            } else {
                byte[] bytes = (String.valueOf(c)).getBytes();
                if (bytes.length > 1) {//汉语
                	if(!isChineseBD(c)){
                		sb.append(c);
                	}
                }
            }
        }
        return sb.toString();
    }

    private boolean isChineseBD(char c) {
    	char[] bds = new char[]{'，','。','；','、'};
    	for(char bd : bds){
    		if(c == bd){
    			return true;
    		}
    	}
		return false;
	}
	//	public void addEmlment(MeasurePosVO vo)
    //	{
    //		if(vo != null)
    //		{
    //			if(m_oMVector.size() > 0)
    //			{
    //				MeasurePosVO mvo = (MeasurePosVO)m_oMVector.get(0);
    //				if(
    // KEY_NAME_ROW_INDEX.equals(mvo.getMeasureVO().getName())&&!mvo.isRefMeasure())
    //				{
    //					if(vo.getPos().equals(mvo.getPos()))
    //					{
    //						String repPk = vo.getMeasureVO().getReportPK();
    //						vo.setMeasureVOAsRefKeyVO(this.getDynAreaVO(),
    // mvo.generateKeyVO(repPk).getKeyVO(),repPk);
    //						vo.setState(MeasurePosVO.MEASURE_TABLE_S_CREATE);
    //						vo.setState(MeasurePosVO.MEAS_S_DELTA_TO_KEY);
    //						m_oMVector.set(0,vo);
    //						return;
    //					}
    //				}
    //			}
    //			m_oMVector.addElement(vo);
    //		}
    //	}
    /**
     * 取消全部提取标志 创建日期：(2002-6-3 16:24:47)
     * 
     * @param table
     *            javax.swing.JTable
     */
    public void addMouseListener(final JTable ta) {

        ta.getTableHeader().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                int col = ta.columnAtPoint(event.getPoint());
            	Object toSetValue = null; 
                for(int row =0;row< m_oMVector.size();row++){
                	if(!isCellEditable(row, col)){
                		continue;
                	}
                	 MeasurePosVO vo = (MeasurePosVO) m_oMVector.get(row);
                    if (col == FLAG_COLUMN) {
                    	if(toSetValue == null){
                    		toSetValue = !vo.getFlag();
                    	}
                    	if((Boolean)toSetValue){
                    		if (vo.getKeyFlag().booleanValue())
                                continue;
                            vo.setFlag(Boolean.TRUE);
                    	}else{
                    		  if (vo.isRefMeasure()){
                    			  //modify by guogang 2007-8-14 全选取消的时候取消指标的引用
                    			  try {
									vo.cancelRefMeasure(m_strReportId, getMeasruePackPK(CellPosition.getInstance(vo.getActPos())));
								} catch (Exception e) {
									AppDebug.debug(e);
								}
                    		  }
                                  
                              vo.setFlag(Boolean.FALSE);
                    	}
                    }else if(col==TYPE_COLUMN){//指标类型
                    	if(toSetValue == null){
                    		toSetValue = vo.getMeasureVO().getType();
                    		if(((Integer)toSetValue).intValue() == MeasureVO.TYPE_NUMBER){
                    			toSetValue = TYPES[1];
                    		}else{
                    			toSetValue = TYPES[0];
                    		}
                    	}
                    	setValueAt(toSetValue, row, col);
                    }else if(col == HEBING_COLUMN){//是否合并指标
                    	if(toSetValue == null){
                    		toSetValue = (Integer)vo.getMeasureVO().getExttype();
                    		if(((Integer)toSetValue).intValue() == MeasureVO.TYPE_EXT_HEBING){
                    			toSetValue = HEBING[0];
                    		}else{
                    			toSetValue = HEBING[1];
                    		}
                    	}
                    	setValueAt(toSetValue, row, col);
                    }else if(col == DIRECTION_COLUMN){//合并方向
                    	if(toSetValue == null){
                    		toSetValue = HBBBMeasParser.getDirection(vo.getMeasureVO().getProps());
                    		if(((Integer)toSetValue).intValue() == HBBBMeasParser.DIRECTIONG_J){
                    			toSetValue = DIRECTION[2];
                    		}else{
                    			toSetValue = DIRECTION[1];
                    		}
                    	}
                    	setValueAt(toSetValue, row, col);
                    }else if(col == DXTYPE_COLUMN){//是否期初合并指标
                    	if(toSetValue == null){
                    		if(HBBBMeasParser.isDxMeas(vo.getMeasureVO().getProps())){
                    			toSetValue = DXTYPE[0];
                    		}else{
                    			toSetValue = DXTYPE[1];
                    		}
                    	}
                    	setValueAt(toSetValue, row, col);
                    }
                }
                fireTableDataChanged();
            }
        });
    }

    //    public void addNameToHasDefed(String name)
    //    {
    //	    m_nameHashset.add(name);
    //    }
    /**
     * 此处插入方法描述。 创建日期：(2003-8-29 10:09:38)
     * 
     * @return java.util.Hashtable
     */
    //public void addToCreatedUfoKeyword(int row,UfoKeyWord ufKey) {
    //	if(ufKey == null)
    //		return;
    //	if(m_oCreateKey == null)
    //	{
    //		m_oCreateKey = new Hashtable();
    //		m_oOriUfKeyword = new Vector();
    //	}
    //	m_oCreateKey.put(Integer.toString(row),ufKey);
    //	m_oOriUfKeyword.addElement(ufKey);
    //}
    /**
     * 向关键字组合中增加关键字 创建日期：(2003-9-25 11:56:20)
     * 
     * @param key
     *            nc.vo.iufo.keydef.KeyVO
     */
    //private void addToKeyGroup(KeyVO key)
    //{
    //	m_oKeyGroupVO.addKeyToGroup(new KeyVO[]{key});
    //}
    /**
     * 检查动态区域时的关键字合法性 1。动态区域内关键字或者全部为公有，或者全部私有 2。关键字个数不超过3个 创建日期：("2003-9-10"
     * 20:07:01)
     */
    //public void checkDynAreaKeys() throws CommonException
    //{
    //	if(m_oDynArea == null )
    //		return;
    //	//以动态区域内的关键字所在位置为Key,构造全部动态区域内所有关键字的Hashtabl，包括已经设置的和新设置的
    //	Hashtable keyTable = new Hashtable();
    //
    //	////得到动态区域内已经定义的关键字
    //	Vector hasVo = m_oDynArea.getKeyword();
    //
    //	//取到主表中定义的关键字个数,如果主表中还没有定义关键字,则为0
    //	int mainDefKeysNum = 0;
    //	if( m_ufoReport.getVecKeyVO() != null )
    //		mainDefKeysNum = m_ufoReport.getVecKeyVO().size();
    //
    //
    //	//检查是否私有公有关键字在一起定义
    //	Boolean isPrivate = null;
    //	Vector allKeyVec = m_oKeyGroupVO.getVecKeys();
    //	boolean isDefedKey = false;
    //	for(int i = 0 ; i < allKeyVec.size() ; i++ )
    //	{
    //		String strError = null;
    //		KeyVO key = (KeyVO)allKeyVec.get(i);
    //		if(isPrivate == null)
    //		{
    //			isPrivate = new Boolean(key.isPrivate());
    //		}else
    //		{
    //			if(isPrivate.booleanValue() != key.isPrivate()) {
    //				strError = StringResource.getStringResource("miufo1001509");
    // //"在动态区域内定义的关键字必须同为公有或私有关键字！"
    //				throw new CommonException(strError);
    //			}
    //		}
    //        if(IsAnaRep())
    //        {
    //            if(isPrivate.booleanValue()){
    //            	strError = StringResource.getStringResource("miufo1001510");
    // //"分析表中的动态区域内不能定义私有关键字！"
    //                throw new CommonException(strError);
    //            }
    //        }
    //
    //		if(key.getType() == KeyVO.TYPE_TIME )
    //		{
    //			if(isDefedKey){
    //				strError = StringResource.getStringResource("miufo1001511");
    // //"在动态区域内不能同时定义两个时间关键字！"
    //				throw new CommonException(strError);
    //			}
    //			isDefedKey = true;
    //		}
    //	}
    //	StringBuffer errBuff = new StringBuffer();
    //	//判断主子表相加之后的累积是否超过了关键字组合的最大限制
    //	if ( mainDefKeysNum + allKeyVec.size() >
    // nc.vo.iufo.keydef.KeyGroupVO.MaxCount )
    //	{
    //		String strError = StringResource.getStringResource("miufo1001512");
    // //"定义的关键字数量超过限制数{0},现在主表已经设置{1}个，本动态区中已经设置{2}个！"
    //		String[] params =
    // {nc.vo.iufo.keydef.KeyGroupVO.MaxCount+"",mainDefKeysNum+"",allKeyVec.size()+""};
    //		throw new CommonException(strError,params);
    //	}
    //
    //}
    //	public boolean checkKeyName(String name) throws CommonException
    //	{
    //		String commonMessage = "";
    //		if(name.trim().equals(""))
    //		{
    //			commonMessage = StringResource.getStringResource("miufo1001513");
    // //"请为该私有关键字指定名称！"
    //			throw new CommonException(commonMessage);
    //		}
    //		if(name.length() > KeyVO.NAME_MAX_LEN)
    //		{
    //			commonMessage =StringResource.getStringResource("miufo1001514");
    // //"该私有关键字名称超长，请重新定义！"
    //			throw new CommonException(commonMessage);
    //		}
    //		if( UICacheManager.getSingleton().getKeywordCache().getByName(name,null)
    // != null )
    //		{
    //			commonMessage = StringResource.getStringResource("miufo1001515");
    // //"该私有关键字和公有关键字重名，请重新定义！"
    //			throw new CommonException(commonMessage);
    //		}
    //		return true;
    //	}
    public boolean checkMeasureName(String name, MeasurePosVO vo)
            throws CommonException {
        String commonMessage = "";
        if (name.trim().equals("")) {
            commonMessage = StringResource.getStringResource("miufo1001516"); //"请为该指标指定指标名称！"
            throw new CommonException(commonMessage);
        }
        //新指标名称是一般指标时
//        char[] ch = name.toCharArray();
//        for (int i = 0; i < ch.length; i++) //判断指标名称中是否含有非法字符
        if(name.indexOf("->") >= 0 || name.indexOf('\"') >= 0 || name.indexOf('\'') >= 0)
        {
//            if (!(Character.isLetterOrDigit(ch[i]))) {
                commonMessage = StringResource
                        .getStringResource("miufo1001517"); //"指标名称'{0}'不符合规范（包含非法字符），请重新定义！"
                String[] params = { name };
                throw new CommonException(commonMessage, params);
//            }
        }
//        MeasureVO cachevo = CacheProxy.getSingleton()
//                .getMeasureCache().loadMeasuresByName(m_strReportId,
//                        name.toUpperCase());
//        if (cachevo != null
//                && (!cachevo.getCode().equalsIgnoreCase(
//                        vo.getMeasureVO().getCode())))//如果指标名称已存在，则弹出对话框
        if(hasDefedMeasureName(vo.getMeasureVO(),name))
        {
            String sHint = getBadNameHint(m_strReportId, name.toUpperCase());
            String message = messageProcess(sHint, 32);
            UfoPublic.sendErrorMessage(message,parentDialog,null);
//            int j = JOptionPane.showConfirmDialog(parentDialog, message,
//                    StringResource.getStringResource("miufo1000718"), //"用友集团"
//                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
//            if (j == JOptionPane.NO_OPTION) {
                return false;
//            }
//            MeasureVO mm = CacheProxy.getSingleton()
//                    .getMeasureCache().loadMeasuresByName(m_strReportId,
//                            name.toUpperCase());
//            if (isReferenced(mm, vo) != MEASREF_NO_ERR
//                    || (m_bIsAnaRep && !mm.getCode().equals(
//                            vo.getMeasureVO().getCode())))//只有在该报表中未被引用过得指标才能被引用
//            {
//                vo.setRefMeasure(mm);
//            } else {
//                commonMessage = StringResource
//                        .getStringResource("miufo1001518"); //"已经引用（定义）过该指标，不能再次引用（定义），可以通过单元公式实现！"
//                throw new CommonException(commonMessage);
//            }
        } else //如果修改得指标名称不存在，将创建新指标
        {
            MeasureVO measVo = new MeasureVO();
            measVo.setName(name);
            measVo.setCode(vo.getMeasureVO().getCode());
            if (isReferenced(measVo, vo) != MEASREF_NO_ERR) {
                commonMessage = StringResource
                        .getStringResource("miufo1001518"); //"已经引用（定义）过该指标，不能再次引用（定义），可以通过单元公式实现！"
                throw new CommonException(commonMessage);
            }
        }
        return true;
    }

    /**
     * 在动态区域内修改关键字时清除本次提取中的指标的引用 创建日期：(2003-10-21 15:46:05)
     */
    //private void clearMeasureRef()
    //{
    //	for(int i = 0 ; i < m_oMVector.size() ; i++ )
    //	{
    //		MeasurePosVO measvo = (MeasurePosVO)m_oMVector.get(i);
    //		if( measvo.isRefMeasure()&& measvo.getFlag().booleanValue() )
    //			setValueAt(Boolean.FALSE,i,FLAG_COLUMN);
    //	}
    //}
    /**
     * 清空指标命名空间， 注意：进行指标提取操作完成后，必须执行此操作！ 创建日期：(2003-11-5 16:05:57)
     */
    public void clearRowAndColNames() {
        m_strsRowNames = null;
        m_strsColNames = null;
        m_iStartRow = -1;
        m_iStartCol = -1;
        m_nameHashset = null;
        m_HasAllNameHashset = null;

    }

    /**
     * 从关键字组合中删除关键字 创建日期：(2003-9-25 11:56:20)
     * 
     * @param key
     *            nc.vo.iufo.keydef.KeyVO
     */
    //private void deleteFromKeyGroup(KeyVO key)
    //{
    //	Vector allKeyVec = m_oKeyGroupVO.getVecKeys();
    //	allKeyVec.remove(key);
    //	KeyVO[] vos = new KeyVO[allKeyVec.size()];
    //	allKeyVec.copyInto(vos);
    //	m_oKeyGroupVO.resetKeyVOs(vos);
    //}
    /**
     * 如果名称重复提示具体的冲突位置。
     */
    private String getBadNameHint(String sReportPK, String sName) {
        StringBuffer sHint = new StringBuffer(StringResource
                .getStringResource("miufo1001519")); //"指标名称和已经存在的指标名称冲突，位置在：报表->"
        //前期已经保证该指标存在
        nc.vo.iuforeport.rep.ReportVO reportvo = (nc.vo.iuforeport.rep.ReportVO) CacheProxy
                .getSingleton().getReportCache().get(sReportPK);
        sHint.append(reportvo.getName());
        sHint.append(StringResource.getStringResource("miufo1001520")); //"
                                                                        // 指标->"
        sHint.append(sName);
//        sHint.append(StringResource.getStringResource("miufo1001521")); //".如果继续，则该指标将被存在的指标替换,是否继续？"
        return sHint.toString();
    }

    public Hashtable getCode() {
        if (m_codes != null && m_codes.size() > 0) {
            return m_codes;
        } else {
            return initCodes();
        }
    }

    public String getColName(int actCol) {
        return m_strsColNames[actCol - m_iStartCol];
    }

    public int getColumnCount() {
        return columnNames.length;
    }

    public String getColumnName(int c) {
        return columnNames[c].title;
    }

    /**
     * 此处插入方法描述。 创建日期：(2003-8-29 10:09:38)
     * 
     * @return java.util.Hashtable
     */
    public Hashtable getCreatedUfoKeyword() {
        if (m_oCreateKey == null)
            m_oCreateKey = new Hashtable();
        return m_oCreateKey;
    }

    /**
     * 此处插入方法描述。 创建日期：(2003-8-29 10:08:49)
     * 
     * @return com.ufsoft.iuforeport.reporttool.temp.DynamicAreaVO
     */
    public DynAreaVO getDynAreaVO() {
        return m_oDynArea;
    }

    /**
     * 此处插入方法描述。 创建日期：(2003-8-17 15:58:41)
     * 
     * @return nc.vo.iufo.keydef.KeyGroupVO
     */
    public KeyGroupVO getKeyGroupVO() {
        return m_oKeyGroupVO;
    }

    /**
     * 取得目前所选指标中行列位置最大的一个 创建日期：(2003-8-27 23:27:42)
     * 
     * @return com.ufsoft.iuforeport.reporttool.pub.UfoCell
     */
    //public CellPosition getMaxCell(int maxcol,int maxrow)
    //{
    //	AreaPosition area;
    //
    //	for( int i = 0 ; i < m_oMVector.size() ; i++ )
    //	{
    //		MeasurePosVO vo = (MeasurePosVO)m_oMVector.get(i);
    //		if(vo.getFlag().booleanValue())
    //		{
    //			area = new UfoArea(vo.getPos());
    //			if(area.End.Col > maxcol )
    //				maxcol = area.End.Col;
    //			if(area.End.Row > maxrow )
    //				maxrow = area.End.Row;
    //		}
    //	}
    //	return new UfoCell(maxrow,maxcol);
    //}
    public String getPrivateRepUnitID() {
        return UnitID;
    }

    /**
     * 此处插入方法描述。 创建日期：(2002-6-11 10:37:01)
     * 
     * @return java.lang.String
     */
    public String getReportID() {
        return m_strReportId;
    }

    public int getRowCount() {
        return m_oMVector.size();
    }

    public String getRowName(int actRow) {
        return m_strsRowNames[actRow - m_iStartRow];
    }

    public MeasurePosVO getSelectMeasurePosVO(int row) {
        return (MeasurePosVO) m_oMVector.get(row);
    }

    /**
     * 此处插入方法描述。 创建日期：(2003-9-4 14:23:50)
     * 
     * @return com.ufsoft.iuforeport.reporttool.temp.EntityList
     * @i18n uiiufofmt00012=本表
     */
    //public com.ufsoft.iuforeport.reporttool.temp.EntityList
    // getTableMeasList() {
    //	return m_oTableMeasList;
    //}
//    /**
//     * 此处插入方法描述。 创建日期：(2002-5-15 16:42:23)
//     */
//    public UfoReport getUfoReport() {
//        return this.m_ufoReport;
//    }

    public Object getValueAt(int r, int c) {
        MeasurePosVO vo = (MeasurePosVO) m_oMVector.elementAt(r);
        int type = 0;
        Hashtable props = null;
        //		UfoKeyWord ufKeyvo = null;

        if (vo != null && vo.getMeasureVO() != null) {
            type = vo.getMeasureVO().getType();
            props = vo.getMeasureVO().getProps();
            //            if(m_oCreateKey != null)
            //            {
            //                ufKeyvo = (UfoKeyWord)m_oCreateKey.get(Integer.toString(r));
            //                if(ufKeyvo != null)
            //                {
            //                    type = ufKeyvo.getKeyVO().getType();
            //                    props = null;
            //                }
            //            }

            if (props == null)
                props = new Hashtable();
            switch (c) {
            case ACT_CELL_COLUMN:
                return vo.getActPos();
//            case VIR_CELL_COLUMN:
//                return vo.getPos();
            case NAME_COLUMN:
                return vo.getMeasureVO().getName();
            case REPORT_CODE:
                String repid = vo.getMeasureVO().getReportPK();
                if (repid != null && repid.length() > 0) {
                	if(repid.equals(m_strReportId))
                		return StringResource.getStringResource("uiiufofmt00012");
                    ReportVO repvo = (ReportVO) m_ReportCache.get(repid);
                    if (repvo != null)
                        return "(" + repvo.getCode() + ")" + repvo.getName();
                }
            case TYPE_COLUMN:
                if (type == TYPES.length)
                    return StringResource.getStringResource("miufopublic328"); //"时间"
                return TYPES[type];

//            case ATTRIBUTE_COLUMN:
//                //没有单位,则返回""
//                if (ufKeyvo != null) {
//                    return "";
//                } else {
//                    if (vo.getMeasureVO().getType() != MeasureVO.TYPE_NUMBER)
//                        return "";
//                    return UNITS[vo.getMeasureVO().getAttribute()];
//                }
            case LENGTH_COLUMN:
//                if (ufKeyvo != null) {
//                    if (ufKeyvo.getKeyVO().getLen() < 0)
//                        return new Integer(0);
//                    return new Integer(ufKeyvo.getKeyVO().getLen());
//                } else {
                    if (vo.getMeasureVO().getLen() == -1)
                        return new Integer(0);
                    return new Integer(vo.getMeasureVO().getLen());
//                }
            case CODEREFERENCE_COLUMN:
//                if (ufKeyvo != null) {
//                    if (ufKeyvo.getKeyVO().getRef() != null
//                            && ufKeyvo.getKeyVO().getRef().length() != 0)
//                        return ((nc.vo.iufo.code.CodeVO) m_codes.get(ufKeyvo
//                                .getKeyVO().getRef())).getName();
//                } else {
                    if (vo.getMeasureVO().getRefPK() != null
                            && vo.getMeasureVO().getRefPK().length() != 0) {
                        return ((nc.vo.iufo.code.CodeVO) m_codes.get(vo
                                .getMeasureVO().getRefPK())).getName();
                    }
//                }
                return "";
            case NOTE_COLUMN:

                return vo.getMeasureVO().getNote();

            case FLAG_COLUMN:
                return vo.getFlag();
//            case KEY_COLUMN:
//                Boolean keyFlag = vo.getKeyFlag();
//                if (keyFlag == null)
//                    keyFlag = Boolean.FALSE;
//                return keyFlag;

            case REFERENCE_COLUMN:
                return new Boolean(vo.isRefMeasure());
            case HEBING_COLUMN:
//                if (ufKeyvo != null)
//                    return "";
                if (vo.getMeasureVO().getExttype() == MeasureVO.TYPE_EXT_HEBING) {
                    return HEBING[1];
                } else {
                    return HEBING[0];
                }
            case DIRECTION_COLUMN:
//                if (ufKeyvo != null)
//                    return "";
                if (vo.getMeasureVO().getExttype() == MeasureVO.TYPE_EXT_HEBING) {
                	return DIRECTION[HBBBMeasParser.getDirection(props)];
                }
                return null;
            case DXTYPE_COLUMN:
//                if (ufKeyvo != null)
//                    return "";
                if (vo.getMeasureVO().getExttype() == MeasureVO.TYPE_EXT_HEBING) {
                    if (HBBBMeasParser.isDxMeas(props))
                        return DXTYPE[1];
                    else
                        return DXTYPE[0];
                }
                return "";
            }
            return "";
        } else
            return null;
    }

    /**
     * 首先要遍历r_vector,防止某些指标状态的错误标示, 例如对于某些新提取的指标,状态为isadded，
     * 但是又参照了指标，这时，新参照的指标就不是新提取的指标了， 但此时的指标时一般指标，并且不允许编辑，所以此时指标状态为-1，
     * 表示默认值，即没有进行过操作
     */
    public Vector getVector() {
        return this.m_oMVector;
    }

    /**
     * 首先要遍历r_vector,过滤掉关键字位置指标，因为关键字不改变单元属性
     */
    public Vector getVectorForCellProp() {
        Vector reVec = new Vector();
        if (m_oMVector != null) {
            MeasurePosVO mvo;
            for (int i = 0; i < m_oMVector.size(); i++) {
                mvo = (MeasurePosVO) m_oMVector.get(i);
                if (m_oCreateKey == null
                        || m_oCreateKey.get(Integer.toString(i)) == null) {
                    //非关键字行
                    if (mvo.getFlag().booleanValue()) {
                        reVec.addElement(m_oMVector.get(i));
                    }
                } else {//关键字行
                    mvo.getMeasureVO().setType(MeasureVO.TYPE_CHAR);
                    reVec.addElement(mvo);
                }
            }
        }
        return reVec;
    }

//    public boolean hasDefedMeasureName(String name, boolean includeNewName) {
//        if (includeNewName) {
//            if (m_HasAllNameHashset != null
//                    && m_HasAllNameHashset.contains(name)
//                    || m_nameHashset != null && m_nameHashset.contains(name))
//                return true;
//        } else {
//            if (m_HasAllNameHashset != null
//                    && m_HasAllNameHashset.contains(name))
//                return true;
//        }
//        return false;
//    }

    private Hashtable initCodes() {
        try {
            CodeVO[] code = CacheProxy.getSingleton().getCodeCache()
                    .getAllCode();
            m_codes = new Hashtable();
            for (int i = 0; i < code.length; i++) {
                CodeVO cvo = code[i];
                if (cvo.getType() != nc.vo.iufo.code.CodeVO.SYSTEM_CODE) {
                    m_codes.put(cvo.getId(), cvo);
                }
            }
            return m_codes;
        } catch (Exception e) {
AppDebug.debug(e);//@devTools             AppDebug.debug(e);
        }
        return null;
    }

    /**
     * 根据传入的区域设定指标命名空间中的行列数组的长度d 创建日期：(2003-11-5 16:11:06)
     * 
     * @param area
     *            com.ufsoft.iuforeport.reporttool.pub.UfoArea
     */
//    public void initRowAndColNameLen(AreaPosition area) {
//        int nRow = area.End.Row - area.Start.Row + 1;
//        int nCol = area.End.Col - area.Start.Col + 1;
//        m_strsRowNames = new String[nRow];
//        m_strsColNames = new String[nCol];
//        m_iStartRow = area.Start.Row;
//        m_iStartCol = area.Start.Col;
//        m_nameHashset = new HashSet();
//        MeasureVO[] vos = m_ufoReport.getFormatModel().getAllMeausreVO();
//        m_HasAllNameHashset = new HashSet();
//        if (vos != null && vos.length > 0) {
//            for (int i = 0; i < vos.length; i++) {
//                if (vos[i] != null)
//                    m_HasAllNameHashset.add(vos[i].getName());
//            }
//        }
//    }

    public boolean isCellEditable(int r, int c) {
        MeasurePosVO vo = (MeasurePosVO) (m_oMVector.elementAt(r));
//        UfoKeyWord key = null;
        //如果是关键字，则不允许编辑
//        if (m_oCreateKey != null) {
//            key = (UfoKeyWord) m_oCreateKey.get(Integer.toString(r));
//            //系统预置不允许修改
//            if (key != null
//                    && key.getKeyVO().isBuiltIn().booleanValue()
//                    && (c != FLAG_COLUMN && c != REFERENCE_COLUMN
//                            && c != KEY_COLUMN && c != KEY_REF_COLUMN))
//                return false;
//
//            if (m_oCreateKey.get(Integer.toString(r)) != null) {
//                if (vo.getState() == MeasurePosVO.MEASURE_TABLE_S_ORIGINAL
//                        + MeasurePosVO.MEAS_S_DELTA_TO_KEY) {
//                    if (c == FLAG_COLUMN || c == REFERENCE_COLUMN
//                            || c == KEY_COLUMN)
//                        return true;
//                    else
//                        return false;
//                }
//                if (c == ATTRIBUTE_COLUMN || c == HEBING_COLUMN
//                        || c == DIRECTION_COLUMN || c == DXTYPE_COLUMN
//                        || c == REFERENCE_COLUMN || c == MEASURE_REF_COLUMN) {
//                    return false;
//                }
//            }
//        }
        //引用的指标,不允许编辑
        if (vo.getMeasureVO() != null
                && vo.getMeasureVO().getReportPK() != null
                && !vo.getMeasureVO().getReportPK().equals(m_strReportId))
            if (c == MEASURE_REF_COLUMN || c == FLAG_COLUMN) {
                return true;
            } else {
                return false;
            }

//        if (vo.isRefMeasure()) {
//            if (c == NOTE_COLUMN || c == DXTYPE_COLUMN || c == DIRECTION_COLUMN
//                    || c == CODEREFERENCE_COLUMN || c == ATTRIBUTE_COLUMN
//                    || c == TYPE_COLUMN || c == HEBING_COLUMN
//                    || c == NAME_COLUMN || c == LENGTH_COLUMN)
//                return false;
//        }
        switch (c) {
        case ACT_CELL_COLUMN:
            return false;
//        case VIR_CELL_COLUMN:
//            return false;
        case FLAG_COLUMN:

            return true;
//        case KEY_COLUMN:
//            //非动态区域不允许提取成关键字
//            if (m_oDynArea == null)//|| vo.getFlag().booleanValue())
//            {
//                return false;
//            }
//            return true;
        case REFERENCE_COLUMN:
            return true;
        case TYPE_COLUMN:
//            if (key == null) {
                if (vo.getMeasureVO().getDbcolumn() != null
                        && vo.getMeasureVO().getDbtable() != null)
                    return false;
                //如果是合并指标，则不允许编辑
                if (vo.getMeasureVO().getExttype() == MeasureVO.TYPE_EXT_HEBING)
                    return false;

//            }
            return true;
//        case ATTRIBUTE_COLUMN:
//
//            //指标只有当类型为数值时允许编辑
//            if (vo.getMeasureVO().getType() == MeasureVO.TYPE_NUMBER)
//                return true;
//            return false;
        case LENGTH_COLUMN:
            if (vo.getMeasureVO().getType() == MeasureVO.TYPE_CHAR)
                return true;
            return false;
        case CODEREFERENCE_COLUMN:
            //指标只有当类型为编码时允许编辑
            if (vo.getMeasureVO().getType() == MeasureVO.TYPE_CODE)
                return true;
            return false;
        case HEBING_COLUMN:
            break;
        case DIRECTION_COLUMN:
        	if(vo.getMeasureVO().getExttype()!=MeasureVO.TYPE_EXT_HEBING)
        		return false;
            break;
        case DXTYPE_COLUMN:
            break;
        case NOTE_COLUMN:
            return true;
        case REPORT_CODE://所属报表不允许编辑
            return false;
//        case KEY_REF_COLUMN:
//            if (m_oDynArea == null || vo.getFlag().booleanValue()) {
//                return false;
//            }
//            return true;
        case MEASURE_REF_COLUMN:
//            if (m_oDynArea == null) {
//                //未设置关键字，不能参照指标
//                Vector keyvec = getUfoReport().getFormatModel().getKeyVO();
//                if (keyvec == null || keyvec.size() == 0)
//                    return false;
//            }
            return true;

        }
        return true;
    }

    /**
     * 判断是否是合并指标 创建日期：(2002-6-21 16:11:23)
     * 
     * @return boolean
     * @param meaCode
     *            java.lang.String
     */
    public boolean isHBBBMeasure(String meaCode) {

        MeasureVO pmvo = CacheProxy.getSingleton().getMeasureCache()
                .getMeasure(meaCode);
        if (pmvo != null) {
            if (pmvo.getProps() != null)//父指标是合并指标
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 此处插入方法描述。 创建日期：(2003-8-17 16:53:28)
     * 
     * @return boolean
     * @param row
     *            int
     */
//    public boolean isKeywordRow(int row) {
//        if (m_oCreateKey != null) {
//            if (m_oCreateKey.get(Integer.toString(row)) != null)
//                return true;
//        }
//        return false;
//    }

    /**
     * 判断是否已经参照过该指标 创建日期：(2002-6-15 13:09:41)
     * 
     * @return boolean
     * @param vo
     *            nc.vo.iufo.measure.MeasureVO
     */
    public int isReferenced(MeasureVO refvo, MeasurePosVO vo) {
        String cell = vo.getActPos();
        String name = refvo.getName();
        if (name != null && name.length() > 0 && cell != null
                && cell.length() > 0) {
            MeasurePosVO mvo;
            //全表检查指标是否重名
            //重名且PK重复，说明重复
            if (hasDefedMeasureName(vo.getMeasureVO(),name)) {
                return MEASREF_NAME_REPETITION_ERR;
            }
            for (int m = 0; m < m_oMVector.size(); m++) {
                mvo = (MeasurePosVO) m_oMVector.get(m);
                if (mvo.getMeasureVO() != null) {
                    if (mvo.getMeasureVO().getName().equalsIgnoreCase(name)
                            && (!mvo.getActPos().equalsIgnoreCase(cell))) {
                        if (refvo.getCode()
                                .equals(mvo.getMeasureVO().getCode())
                                && refvo.getReportPK().equals(
                                        mvo.getMeasureVO().getReportPK())) {
                            return MEASREF_MEAS_REPETITION_ERR;
                        }
                    }
                }
            }
        }
        return MEASREF_NO_ERR;
    }

    /**
     * 由于显示的时候，字符串的长度会影响提示框的长度， 或者由于提示框的长度有限，字符串会显示不全，所以在显示之前
     * 先进行字符串处理，在需要折行的时候，折行显示 message 要进行处理的字符串 wordNum 每行显示的字符串长度,当前默认为32的长度
     */
    public static String messageProcess(String message, int wordNum) {
        StringBuffer returnMessage = new StringBuffer();
        while (message.length() > 0) {
            if (message.length() > wordNum) {
                returnMessage.append(message.substring(0, wordNum));
                returnMessage.append("\n");
                message = message.substring(wordNum);
            } else {
                returnMessage.append(message);
                message = "";
            }
        }
        return returnMessage.toString();
    }

    /**
     * 响应指标参照 创建日期：(2003-8-29 10:12:03)
     */
    private void onMeasureRef(int r) {

    }

    /**
     * 保存动态区域内的关键字 创建日期：(2003-9-25 11:24:42)
     * 
     * @return boolean 表识是否保存完成
     */
    //public boolean saveKeyWord()
    //{
    //	try {
    //		//如果已经应用过，且没有在改变过关键字，则不用再次保存
    //		if(m_boKeyGroupIsApply != null &&
    // m_boKeyGroupIsApply.equals(m_oKeyGroupVO))
    //			return true;
    //		//如果是主表提取，则不用进行保存
    //		if( m_oDynArea == null )
    //			return true;
    //		//如果没有指标需要保存，则直接返回
    //	    if (m_oMVector == null || m_oMVector.size() == 0) {
    //	        return true;
    //	    }
    //
    //		Hashtable ufoKeyTable = new Hashtable();
    //	    //首先和本次提取包含的初始关键字作比较，看是否有更改
    //	    Vector dynkeys = m_oKeyGroupVO.getVecKeys();
    //	    //将要保存的关键字列表
    //	    Vector dynKeyword = new Vector();
    //		//将当前指标提取操作中的关键字做一个索引列表
    //	    for( int l = 0 ; l < dynkeys.size() ; l++ )
    //	    {
    //		    KeyVO vo = (KeyVO)dynkeys.get(l);
    //		    ufoKeyTable.put(vo.getKeywordPK(),vo);
    //	    }
    //	    //标记是否修改过关键字
    //		boolean isEdited = false;
    //		//标记是否进行了增删关键字
    //		boolean isCreateOrDelete = false;
    //		UfoKeyWord keyword = null;
    //		MeasurePosVO mvo = null;
    //		KeyVO key = null;
    //		//动态区域内的全部关键字
    //		Vector allDynKws = m_oDynArea.getKeyword();
    //		Hashtable editedKeyTable = new Hashtable();
    //
    //		//循环判断,将本次提取操作中包含的关键字找出,并构造成UfoKeyWord。当发现该关键字是新提取的或是修改过位置的话，则将修改标记置为true
    //		for (int i = 0; i < m_oMVector.size(); i++)
    //		{
    //	        mvo = (MeasurePosVO) m_oMVector.get(i);
    //	        UfoKeyWord ufKeyvo = null;
    //	        if(m_oCreateKey != null)
    //				ufKeyvo = (UfoKeyWord)m_oCreateKey.get(Integer.toString(i));
    //			if( ufKeyvo == null )
    //				continue;
    //            key = (KeyVO) ufoKeyTable.get(mvo.getMeasureVO().getCode());
    //            if( key == null )
    //            {
    //	            isCreateOrDelete = true;
    //	            dynKeyword.addElement(ufKeyvo);
    //            }else
    //            {
    //	            if( !ufKeyvo.getKeyVO().equals(key) )
    //	            {
    //		            //判断统一关键字是否属性发生改变
    //					for(int j = 0 ; j < allDynKws.size() ; j++ )
    //					{
    //						KeyVO okey = ((UfoKeyWord)allDynKws.get(j)).getKeyVO();
    //						//okey此时!= null
    //						if( key != null && key.getKeywordPK().equals(okey.getKeywordPK()) )
    //						{
    //							editedKeyTable.put(ufKeyvo.getKeyPK(),ufKeyvo.getKeyVO());
    //							isEdited = true;
    //							break;
    //						}
    //					}
    //					dynKeyword.addElement(ufKeyvo);
    //					continue;
    //	            }
    //	            if(key != null)
    //	            {
    //		            keyword = new UfoKeyWord();
    //					keyword.setKeyVO(key);
    //					keyword.setCell(new UfoCell(mvo.getPos()));
    //					if( !isCreateOrDelete)
    //					{
    //						//如果原关键字组合中不包含该关键字，则说明该关键字为新增
    //						if( m_oOriUfKeyword == null ||(!m_oOriUfKeyword.contains(keyword) ))
    //						{
    //							isCreateOrDelete = true;
    //						}
    //
    //					}
    //					dynKeyword.addElement(keyword);
    //	            }
    //            }
    //	    }
    //		//如果新增了关键字,或者在应用前后,dynKeyword.size() != m_oOriUfKeyword.size()则表明修改了关键字
    //		//此处增加这个判断主要是为了判断将选中的关键字全部除去的情况
    //		if( (m_oOriUfKeyword == null && dynKeyword.size() != 0)
    //			||(m_oOriUfKeyword != null) && dynKeyword.size() !=
    // m_oOriUfKeyword.size())
    //			isCreateOrDelete = true;
    //
    //		//只有修改过，才进行关键字的保存
    //		//如果动态区域内原本已经有关键字，则提示用户，修改关键字会清除指标引用
    //
    //		if(isCreateOrDelete || isEdited)
    //		{
    //			if( isCreateOrDelete )
    //			{
    //
    //				if( allDynKws.size() > 0)
    //				{
    //					String sHint = StringResource.getStringResource("miufo1001522");
    // //"关键字修改会导致本动态区域内数据被删除，同时会清除该动态区域内所有指标引用！"
    //					int j = JOptionPane.showConfirmDialog(parentDialog,
    //						sHint,StringResource.getStringResource("miufo1000718"), //"用友集团"
    //						JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
    //					if( j== JOptionPane.NO_OPTION )
    //					{
    //                        return false;
    //
    //					}else//清除指标引用,报表保存的时候再进行报表数据清除
    //					{
    //						m_ufoReport.clearDynRefMeasures(m_oDynArea.getDynamicAreaPK());
    //						clearMeasureRef();
    //					}
    //				}
    //			}
    //				//将动态区域内未被选择的关键字装入保存列表中
    //				loopa:
    //				for(int m = 0 ; m < allDynKws.size() ; m++ )
    //				{
    //					keyword = ( UfoKeyWord)allDynKws.get(m);
    //					if( m_oOriUfKeyword == null ||(!m_oOriUfKeyword.contains(keyword) ))
    //					{
    //						//此处要对原有指标进行判断，防止在原本动态区域内没有定义关键字的时候，多次应用关键字时出现关键字设置异常
    //						//问题描述：动态区域内原本没有关键字，设置关键字且应用后，在修改某一关键字，再次应用的时候就发现工作表格中的关键字显示和指标提取的关键字不符
    //						for( int i = 0 ; i < dynKeyword.size() ; i++ )
    //						{
    //							UfoKeyWord kw = ( UfoKeyWord)dynKeyword.get(i);
    //							if(kw.getCell().equals(keyword.getCell()))
    //								continue loopa;
    //						}
    //						dynKeyword.addElement(keyword);
    //					}
    //					
    //
    //			}
    //
    //				//清空动态区域关键字列表
    //				m_oDynArea.setKeyword(new Vector());
    //				m_oDynArea.setKeyVO(new Vector());
    //				//循环将保存列表中的关键字设置到动态区域中去
    //				for(int n = 0 ; n < dynKeyword.size() ; n++ )
    //				{
    //					keyword = ( UfoKeyWord)dynKeyword.get(n);
    //					m_oDynArea.addKeyWord(keyword);
    //					m_oDynArea.addKeyDef(keyword.getKeyVO());
    //				}
    //
    //
    //			
    //			//此处是为了处理在应用关键字后在点击确定就可以不用再次去调用本方法保存关键字
    //			if(m_boKeyGroupIsApply == null )
    //				m_boKeyGroupIsApply = new KeyGroupVO();
    //			m_boKeyGroupIsApply.addKeyToGroup(m_oKeyGroupVO.getKeys());
    //		}
    //    }catch (Exception e) {
    //        //其他异常
    //		AppDebug.debug(e);
    //    }
    //    return true;
    //}
    public void setColName(int actCol, String name) {
        m_strsColNames[actCol - m_iStartCol] = name;
    }

    /**
     * 此处插入方法描述。 创建日期：(2003-9-1 20:56:17)
     * 
     * @param newDialog
     *            com.ufsoft.iuforeport.reporttool.table.CreateMeasureDialog
     */
    public void setDialog(MeasureDefineDialog newDialog) {
        parentDialog = newDialog;
    }

    //设置编码引用，如果设置了编码引用，则返回true，表示指标的属性已经被更改了，false表示未修改指标属性
//    private boolean setKeyCodeRefence(String codeReference, UfoKeyWord vo) {
//        boolean isedited = false;
//        try {
//            if (codeReference != null) {
//                if (vo.getKeyVO().getRef() != null) {
//                    CodeVO code = UICacheManager.getSingleton().getCodeCache()
//                            .findCodeByID(vo.getKeyVO().getRef());
//                    UICacheManager.getSingleton().getCodeCache().delCodeRef(
//                            code);
//                }
//                for (Enumeration em = getCode().elements(); em
//                        .hasMoreElements();) {
//                    nc.vo.iufo.code.CodeVO cvo = (nc.vo.iufo.code.CodeVO) em
//                            .nextElement();
//                    if (codeReference.trim().length() == 0) {
//                        vo.getKeyVO().setRef(cvo.getId());
//                        UICacheManager.getSingleton().getCodeCache()
//                                .addCodeRef(cvo);
//                        isedited = true;
//                        break;
//                    }
//                    if (cvo.getName().equals(codeReference.trim())) {
//                        vo.getKeyVO().setRef(cvo.getId());
//                        UICacheManager.getSingleton().getCodeCache()
//                                .addCodeRef(cvo);
//                        isedited = true;
//                        break;
//                    }
//                }
//            } else {
//                vo.getKeyVO().setRef(null);
//            }
//        } catch (Exception e) {
//            AppDebug.debug(e);
//        }
//        return isedited;
//    }

    /**
     * 设置关键字类型，关键字没有数值类型，如果选择数值类型，则默认为字符
     *  
     */
    //public boolean setKeyType(String type,UfoKeyWord vo)
    //{
    //	for(int i = 0;i < TYPES.length;i++)
    //	{
    //		if(type.equals(TYPES[i]))
    //		{
    //			if(i == 0)
    //				i = 1;
    //			vo.getKeyVO().setType(i);
    //			vo.getKeyVO().setLen(TYPESLEN[i]);//按照规定的长度赋值，每个值对应一个指标类型
    //			
    //			break;
    //		}
    //
    //	}
    //	if(vo.getKeyVO().getType() != KeyVO.TYPE_REF)
    //	{
    //		setKeyCodeRefence(null,vo);
    //	}else
    //	{
    //		//为编码参照类型选择一个默认编码
    //		setKeyCodeRefence("",vo);
    //	}
    //	return true;
    //}
    //设置编码引用，如果设置了编码引用，则返回true，表示指标的属性已经被更改了，false表示未修改指标属性
    private boolean setMeasureCodeRefence(String codeReference, MeasurePosVO vo) {
        boolean isedited = false;
        try {
            if (codeReference != null) {
                if (vo.getMeasureVO().getType() == MeasureVO.TYPE_CODE) {
                    CodeVO code = CacheProxy.getSingleton().getCodeCache()
                            .findCodeByID(vo.getMeasureVO().getRefPK());
                    CacheProxy.getSingleton().getCodeCache().delCodeRef(code);
                }
                for (Enumeration em = getCode().elements(); em.hasMoreElements();) {
                    nc.vo.iufo.code.CodeVO cvo = (nc.vo.iufo.code.CodeVO) em.nextElement();
                    if (codeReference.trim().length() == 0) {
                    	if(vo.getMeasureVO().getRefPK() != null)
                    		continue;
                        vo.getMeasureVO().setRefPK(cvo.getId());
                        CacheProxy.getSingleton().getCodeCache()
                                .addCodeRef(cvo);
                        isedited = true;
                        break;
                    }
                    if (cvo.getName().equals(codeReference.trim())) {
                        vo.getMeasureVO().setRefPK(cvo.getId());
                        CacheProxy.getSingleton().getCodeCache()
                                .addCodeRef(cvo);
                        isedited = true;
                        break;
                    }
                }
            } else {
                vo.getMeasureVO().setRefPK(null);
            }
        } catch (Exception e) {
AppDebug.debug(e);//@devTools             AppDebug.debug(e);
        }
        return isedited;
    }

    /**
     * 设置指标类型 如果指标类型是数值或浮动数值，则单位可选，编码引用不可选，指标长度不可定义
     * 如果指标类型是字符或浮动字符，则单位不可选，编码引用不可选，指标长度可定义
     * 如果指标类型是编码或浮动编码，则单位不可选，编码引用可选，指标长度不可定义
     *  
     */
    public boolean setMeasureType(String type, MeasurePosVO vo) {

        for (int i = 0; i < TYPES.length; i++) {
            if (type.equals(TYPES[i])) {
                vo.getMeasureVO().setType(i);
                vo.getMeasureVO().setLen(TYPESLEN[i]);//按照规定的长度赋值，每个值对应一个指标类型
                vo.getMeasureVO().setAttribute(TYPESUNIT[i]);
                break;
            }

        }
        if (vo.getMeasureVO().getType() != MeasureVO.TYPE_CODE) {
            setMeasureCodeRefence(null, vo);
        } else {
            //为编码参照类型选择一个默认编码
            setMeasureCodeRefence("", vo);
        }
        return true;
    }

//    public void setPrivateRepUnitID(String unitid) {
//        if (unitid != null && !unitid.equals(""))
//            UnitID = unitid;
//        else
//            UnitID = null;
//    }

    /**
     * 设置参照的关键字, 创建日期：(2003-8-17 16:33:39)
     * 
     * @param vo
     *            com.ufsoft.iuforeport.reporttool.temp.MeasurePosVO
     */
    //public void setRefkey(int row,KeyVO keyvo) throws CommonException
    //{
    //	if( m_oKeyGroupVO.getSize() + 1 <= KeyGroupVO.MaxCount )
    //	{
    //		MeasurePosVO vo = (MeasurePosVO)m_oMVector.get(row);
    //		//指标不能参照为关键字
    //		if(vo.getFlag().booleanValue()){
    //			String strError = StringResource.getStringResource("miufo1001523");
    // //"指标不能参照为关键字！"
    //			throw new CommonException(strError);
    //		}
    //
    //		if( m_oCreateKey == null )
    //			m_oCreateKey = new Hashtable();
    //
    //
    //		UfoKeyWord orikey = (UfoKeyWord)m_oCreateKey.get( Integer.toString(row));
    //
    //
    //		Vector allKeyVec = m_oKeyGroupVO.getVecKeys();
    //		if(orikey != null)
    //			allKeyVec.remove(orikey.getKeyVO());
    //		allKeyVec.addElement(keyvo);
    //		boolean isDefedkey = false;
    //		for(int i = 0 ; i < allKeyVec.size() ; i++ )
    //		{
    //			KeyVO key = (KeyVO)allKeyVec.get(i);
    //
    //
    //			if(key.getType() == KeyVO.TYPE_TIME )
    //			{
    //				if(isDefedkey)
    //				{
    //					String strError = StringResource.getStringResource("miufo1001524");
    // //"在动态区域内不能同时定义两个时间关键字！"
    //					throw new CommonException(strError);
    //				}
    //				isDefedkey = true;
    //			}
    //		}
    //
    //		UfoKeyWord ufokey = vo.setMeasureVOAsRefKeyVO(this.getDynAreaVO(),
    // keyvo,m_strReportId);
    //		Enumeration em = m_oCreateKey.elements();
    //		UfoKeyWord ufok;
    //		for(;em.hasMoreElements();)
    //		{
    //			ufok = (UfoKeyWord)em.nextElement();
    //			if(ufok.getKeyPK().equals(ufokey.getKeyPK())){
    //				String strError = StringResource.getStringResource("miufo1001525");
    // //"在动态区域内已经定义该关键字，不能重复定义！"
    //				throw new CommonException(strError);
    //			}
    //		}
    //		KeyVO[] vos = new KeyVO[allKeyVec.size()];
    //		allKeyVec.copyInto(vos);
    //		m_oKeyGroupVO.resetKeyVOs(vos);
    //
    //		m_oCreateKey.put( Integer.toString(row) ,ufokey );
    //	}else
    //	{
    //		String strError = StringResource.getStringResource("miufo1001526");
    // //"本报表的关键字组合中关键字的数目已经达到最高上限，不允许再参照！"
    //		throw new CommonException(strError);
    //	}
    //	fireTableDataChanged();//刷新显示
    //}
    /**
     * 此处插入方法描述。 创建日期：(2002-6-11 10:34:21)
     * 
     * @param id
     *            java.lang.String
     */
//    public void setReportID(String id) {
//        this.m_strReportId = id;
//    }
//
//    public void setRowName(int actRow, String name) {
//        m_strsRowNames[actRow - m_iStartRow] = name;
//    }

    /**
     * 此处插入方法描述。 创建日期：(2002-5-9 16:37:39)
     * 
     * @param Aera
     *            java.lang.String
     */
    public void setValueAt(Object obj, int r, int c) {
        m_bNowEditing = true;

        if (obj == null)
            return;
        CacheProxy.getSingleton().getKeyGroupCache().getAllGroupVOs();
        MeasurePosVO vo = (MeasurePosVO) m_oMVector.get(r);

//        UfoKeyWord ufKeyvo = null;
//        if (m_oCreateKey != null)
//            ufKeyvo = (UfoKeyWord) m_oCreateKey.get(Integer.toString(r));

        //取出vo的扩展属性，
        Hashtable props = vo.getMeasureVO().getProps();
        if (props == null)
            props = new Hashtable();
        try {
            //liuyy 2005-2-6
//            String strMeasPackPK = null;
//            DynamicAreaVO dynVO = this.getDynAreaVO();
//            if (dynVO != null) {
//                strMeasPackPK = dynVO.getMeasPackPK();
//            } else {
//                strMeasPackPK = this.getUfoReport().getContextEO()
//                        .getM_strMainMeasurePackPK();
//            }

            switch (c) {
            case FLAG_COLUMN:
                //如果指标为引用指标，则在取消指标时取消引用（因为取消的指标不会有指标引用的标志，所以，此处执行时，仅在取消指标时有用）
                if (vo.isRefMeasure()) {
                    vo.cancelRefMeasure(m_strReportId, getMeasruePackPK(CellPosition.getInstance(vo.getActPos())));
                    vo.getMeasureVO().setReportPK(m_strReportId);
                }
                vo.setFlag((Boolean) obj);
                if (((Boolean) obj).booleanValue()) {

//                    if (ufKeyvo != null) {
//                        deleteFromKeyGroup(ufKeyvo.getKeyVO());
//                        m_oCreateKey.remove(Integer.toString(r));
//                    }
                    vo.setKeyFlag(Boolean.FALSE);
                    vo.stateRollBackFromToKey();
                }
                break;
            case REFERENCE_COLUMN:
                if (!((Boolean) obj).booleanValue()) {//分析表不允许取消参照。
                    if (IsAnaRep()) {
                        String message = StringResource
                                .getStringResource("uiuforep000223");//"分析表中不允许去除指标参照"
                        UfoPublic.sendWarningMessage(message, parentDialog);
                        break;
                    }

                }
                vo.cancelRefMeasure(m_strReportId, getMeasruePackPK(CellPosition.getInstance(vo.getActPos())));
                break;

            case NAME_COLUMN:
                String name = ((String) obj).trim();
//                if (ufKeyvo != null) {
//                    if (checkKeyName(name))
//                        ufKeyvo.getKeyVO().setName(name);
//                }
                //指标名称要检查是否合法，是否与当前的指标缓存中的指标重名，有错要提示用户
                if (!name.equals(vo.getMeasureVO().getName())) {
                    if (checkMeasureName(name, vo)) {
                        //指标名称不再自动转换成大写 modify by chxw 2008-03-02 
                    	//vo.getMeasureVO().setName(name.toUpperCase());
                    	vo.getMeasureVO().setName(name);
                    }
                }
                break;
            case LENGTH_COLUMN:
                //指标长度要验证合法性，有错要提示用户
                String length = (String) obj;
                String checkLength = null;
                int len;
//                if (ufKeyvo != null) {
//                    len = Integer.parseInt(length);
//                    if (len <= 0 || len > 64) {
//                        checkLength = StringResource
//                                .getStringResource("miufo1001527"); //"字符类型关键字的允许长度在0-64之间！"
//                    }
//                } else {
                    checkLength = new MeasCompParser().checkCharLen(length
                            .trim());
//                }
                if (checkLength != null) {
                    throw new CommonException(checkLength);
                } else {
//                    if (ufKeyvo != null) {
//                        ufKeyvo.getKeyVO().setLen(Integer.parseInt(length));
//                    }
                    vo.getMeasureVO().setLen(Integer.parseInt(length));
                }
                break;
            case NOTE_COLUMN:
                //指标说明的设置
                String note = (String) obj;
//                if (ufKeyvo != null) {
//                    note = note.trim();
//                    if (note.length() > KeyVO.NOTE_MAX_LEN) {
//                        String strError = StringResource
//                                .getStringResource("miufo1001528"); //"关键字说明超长！"
//                        throw new CommonException(strError);
//                    }
//                    ufKeyvo.getKeyVO().setNote(note);
//                }
                vo.getMeasureVO().setNote(note.trim());
                break;
            case CODEREFERENCE_COLUMN:
                String codeReference = ((String) obj).trim();
//                if (ufKeyvo != null) {
//                    setKeyCodeRefence(codeReference, ufKeyvo);
//                }
                //如果编码引用复选框的选择改变，则根据显示的编码引用名称找到对应的编码引用ID进行保存
                setMeasureCodeRefence(codeReference, vo);
                break;
//            case ATTRIBUTE_COLUMN:
//                for (int i = 0; i < UNITS.length; i++) {
//                    if (((String) obj).equals(UNITS[i])) {
//                        vo.getMeasureVO().setAttribute(i);
//                        break;
//                    }
//                }
            case TYPE_COLUMN:
                if (vo.isRefMeasure())
                    break;
                setMeasureType((String) obj, vo);
//                if (ufKeyvo != null) {
//                    setKeyType((String) obj, ufKeyvo);
//                }

                break;
            case DIRECTION_COLUMN:
                //当指标为合并指标时，方向为必选项，
                if (props == null) {
                    props = new Hashtable();
                }
                if (DIRECTION[0].equals(obj)) {
                    //如果当前指标是合并指标，但是选择了该方向，则默认为借方向
                    if (vo.getMeasureVO().getExttype() == MeasureVO.TYPE_EXT_HEBING) {
                        HBBBMeasParser.setDirection(
                                HBBBMeasParser.DIRECTIONG_J, props);
                        vo.getMeasureVO().setProps(props);
                        vo.getMeasureVO().setExttype(MeasureVO.TYPE_EXT_HEBING);
                    }
                } else if (DIRECTION[1].equals(obj)) {
                    //如果当前指标不是合并指标，但是选择了该方向，那么，该指标自动改为合并指标
                    HBBBMeasParser.setDirection(HBBBMeasParser.DIRECTIONG_J,
                            props);
                    vo.getMeasureVO().setProps(props);
                    vo.getMeasureVO().setExttype(MeasureVO.TYPE_EXT_HEBING);
                } else if (DIRECTION[2].equals(obj)) {
                    //如果当前指标不是合并指标，但是选择了该方向，那么，该指标自动改为合并指标
                    HBBBMeasParser.setDirection(HBBBMeasParser.DIRECTIONG_D,
                            props);
                    vo.getMeasureVO().setProps(props);
                    vo.getMeasureVO().setExttype(MeasureVO.TYPE_EXT_HEBING);
                }
                setMeasureType(TYPES[0], vo);
                break;
            case DXTYPE_COLUMN:

                if (props == null) {
                    props = new Hashtable();
                }
                if (DXTYPE[1].equals(obj)) {
                    //如果当前指标不是合并指标，但是选择了该类型，那么，该指标自动改为合并指标，
                    //此时，默认方向为借方向，并设置扩展属性为抵消类型

                    HBBBMeasParser.setDxMeas(true, props);
                    Object object = props.get(HBBBMeasParser.PROP_DIRECT);
                    if (object == null) {
                        HBBBMeasParser.setDirection(
                                HBBBMeasParser.DIRECTIONG_J, props);
                    }
                    vo.getMeasureVO().setProps(props);
                    vo.getMeasureVO().setExttype(MeasureVO.TYPE_EXT_HEBING);
                } else {
                    //如果当前指标是合并指标，选择了该类型，设置扩展属性为非抵消类型
                    if (vo.getMeasureVO().getExttype() == MeasureVO.TYPE_EXT_HEBING)
                        HBBBMeasParser.setDxMeas(false, props);
                }
                setMeasureType(TYPES[0], vo);
                break;
            case HEBING_COLUMN:
                if (HEBING[1].equals(obj)) {
                    //如果指标被设为合并指标，则指标的类型默认为数值类型（类型复选框不允许编辑），
                    if (vo.getMeasureVO().getExttype() == MeasureVO.TYPE_EXT_HEBING)
                        return;
                    if (vo.getMeasureVO().getAttribute() == 1) {
                        HBBBMeasParser.setPercent(true, props);
                    }
                    HBBBMeasParser.setDirection(HBBBMeasParser.DIRECTIONG_J,
                            props);
                    HBBBMeasParser.setDXType(HBBBMeasParser.DXITEM_N, props);
                    vo.getMeasureVO().setProps(props);
                    vo.getMeasureVO().setExttype(MeasureVO.TYPE_EXT_HEBING);
                    setMeasureType(TYPES[0], vo);
                } else {
                    //如果指标被设为非合并指标，则指标类型复选框允许编辑，默认为无方向，非抵消类型
                    vo.getMeasureVO().setProps(null);
                    vo.getMeasureVO().setExttype(0);
                }

                break;

            default:
                break;
            }
            if (vo.getState() == MeasurePosVO.MEASURE_TABLE_S_ORIGINAL) {
                vo.setState(MeasurePosVO.MEASURE_TABLE_S_UPDATE);
            }

        } catch (CommonException ce) {
            parentDialog.setStateMessage(ce.getMessage(), true);
            fireTableDataChanged();//刷新显示
            throw ce;
            //throw ce;

        } catch (Exception e) {
            parentDialog.setStateMessage(e.getMessage(), true);
            fireTableDataChanged();//刷新显示
            throw new CommonException(e.getMessage());
            //throw ce;
        }

        fireTableDataChanged();//刷新显示
        parentDialog.fireCompement(r);
        m_bNowEditing = false;
    }

    /**
     * setIsAnaRep
     * 设置是否是分析表
     * @param bIsAnaRep boolean
     */
    public void setIsAnaRep(boolean bIsAnaRep) {
        m_bIsAnaRep = bIsAnaRep;
    }

    /**
     * 返回是否是分析表
     * @return boolean
     */
    public boolean IsAnaRep() {
        return m_bIsAnaRep;
    }

    /**
     * 当当前报表是分析表时，校验是否所提取的指标全部已作参照处理
     * @throws CommonException
     */
    public void checkAnaRepMeasure() throws CommonException {
        if (IsAnaRep()) {
            //如果没有指标需要保存，则直接返回
            if (m_oMVector == null || m_oMVector.size() == 0) {
                return;
            }

            //通过循环的方法将指标全部保存到指标实体链中
            MeasurePosVO mvo;
            for (int i = 0; i < m_oMVector.size(); i++) {
                mvo = (MeasurePosVO) m_oMVector.get(i);
                switch (mvo.getState()) {
                case MeasurePosVO.MEASURE_TABLE_S_CREATE:
                    if (mvo.getFlag().booleanValue() && (!mvo.isRefMeasure())) {
                        String message = StringResource.getStringResource(
                                "uiuforep000025", new Object[] { mvo
                                        .getMeasureVO().getName() });
                        throw new CommonException(message);
                    }
                    break;
                case MeasurePosVO.MEASURE_TABLE_S_DELETE:

                    break;
                case MeasurePosVO.MEASURE_TABLE_S_UPDATE:
                    break;
                case MeasurePosVO.MEASURE_TABLE_S_ORIGINAL:
                    break;
                default:
                    break;
                }
            }

        }
    }

    public void setNowEditingState(boolean isEditing) {
        m_bNowEditing = isEditing;
    }

    public boolean isEditing() {
        return m_bNowEditing;
    }
    protected MeasureModel getMeasureModel(){
    	return CellsModelOperator.getMeasureModel(m_cellsModel);
    }
    public CellPosition[] getCells() {
        return m_cells;
    }
    public CellsModel getCellsModel() {
        return m_cellsModel;
    }
}
  