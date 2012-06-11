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
 * ָ����ȡʱ�Ĳ�������
 * 
 * @author��������
 */
public class MeasureDefineTableModel extends AbstractTableModel {
    /**
     * <code>serialVersionUID</code> ��ע��
     */
    private static final long serialVersionUID = -8246854249111088685L;

    public static final int FLAG_COLUMN = 0; //��ȡ��־

    public static final int REFERENCE_COLUMN = 1; //���ã����������Ǳ������Ǳ�����״̬��

    public static final int REPORT_CODE = 2; //��������

    public static final int ACT_CELL_COLUMN = 3; //ʵ�ʵ�Ԫ

    public static final int NAME_COLUMN = 4; //ָ������

    public static final int TYPE_COLUMN = 5; //ָ������

    public static final int LENGTH_COLUMN = 6; //ָ�곤��

//    public static final int DECIMAL_DIGITS = 7; //С��λ��

    public static final int CODEREFERENCE_COLUMN = 7; //��������

    public static final int NOTE_COLUMN = 8; //˵ ��

    public static final int HEBING_COLUMN = 9; //�ϲ�ָ��

    public static final int DIRECTION_COLUMN = 10; //����

    public static final int DXTYPE_COLUMN = 11; //��������

    //��������������Ϊָ����ȡ�Ĳ��հ�ť������,�������Ʋ��հ�ť�Ŀ��ý���
    //	public static final int KEY_REF_COLUMN = 15;
    public static final int MEASURE_REF_COLUMN = 16;

//    private UfoReport m_ufoReport = null; //�жϵ�ǰ�������Ƿ��пɱ�ָ��ʱ��

    private Vector m_oMVector = new Vector();//MeasurePosVO
    //	private EntityList m_oTableMeasList = null;
    // //���������е�ָ�꣬���ѡ�е������ǿɱ�������Ϊ�ÿɱ����е�ָ��

    public static final MeasureColumnModel columnNames[] = {
            new MeasureColumnModel(StringResource.getStringResource("miufopublic285")+"/"+
            		StringResource.getStringResource("miufo1000160"), 60, JLabel.CENTER), //"ָ
                                                                            // ��"
            new MeasureColumnModel(StringResource
                    .getStringResource("miufo1001490"), 50, JLabel.CENTER), //"��
                                                                            // ��"
            new MeasureColumnModel(StringResource
                    .getStringResource("miufo1001530"), 84, JLabel.CENTER), //"��������"
            //		   new
            // MeasureColumnModel(StringResource.getStringResource("miufo1001491"),50,
            // JLabel.CENTER), //"�� �� ��"
            new MeasureColumnModel(StringResource
                    .getStringResource("miufo1001492"), 84, JLabel.CENTER), //"ʵ�ʵ�Ԫ"
            //		   new
            // MeasureColumnModel(StringResource.getStringResource("miufo1001493"),84,
            // JLabel.CENTER), //"��Ե�Ԫ"
            new MeasureColumnModel(StringResource
                    .getStringResource("miufo1001051"), 84, JLabel.CENTER), //"��
                                                                            // ��"
            new MeasureColumnModel(StringResource
                    .getStringResource("miufo1001494"), 84, JLabel.CENTER), //"��
                                                                            // ��"
            //		   new
            // MeasureColumnModel(StringResource.getStringResource("miufo1001495"),84,
            // JLabel.CENTER), //"ָ�굥λ"
            new MeasureColumnModel(StringResource
                    .getStringResource("miufo1001496"), 84, JLabel.CENTER), //"��
                                                                            // ��"
            new MeasureColumnModel(StringResource
                    .getStringResource("miufo1001497"), 84, JLabel.CENTER), //"����ѡ��"
            new MeasureColumnModel(StringResource
                    .getStringResource("miufo1001052"), 84, JLabel.CENTER), //"˵
                                                                            // ��"
            new MeasureColumnModel(StringResource
                    .getStringResource("miufo1001498"), 84, JLabel.CENTER), //"��
                                                                            // ��"
            new MeasureColumnModel(StringResource
                    .getStringResource("miufo1001499"), 84, JLabel.CENTER), //"��
                                                                            // ��"
            new MeasureColumnModel(StringResource
                    .getStringResource("miufo1001500"), 84, JLabel.CENTER) }; //"�ڳ��ϲ�"

    public static final String[] TYPES = {
            StringResource.getStringResource("miufopublic265"),
            StringResource.getStringResource("miufopublic266"),
            StringResource.getStringResource("miufopublic283"),
            StringResource.getStringResource("miufopublic315")}; //@i18n miufopublic315=����

    public static final int[] TYPESLEN = { 0, 64, 0, 10};//ָ�����Ͷ�Ӧ��ȱʡ����

    public static final int[] TYPESUNIT = { 2, -1, -1, -1};//ָ�����Ͷ�Ӧ��units.

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

    private Hashtable m_codes;//��ǰָ�����е����б���

    private String UnitID = null;

    private java.lang.String m_strReportId;

    private MeasureDefineDialog parentDialog = null;

    public KeyVO m_rowIndexKey = null;//�кŹؼ���

    /**
	 * @i18n uiuforep00127=�к�
	 */
    public static final String KEY_NAME_ROW_INDEX = MultiLang.getString("uiuforep00127");// StringResource.getStringResource("miufo1001508");
                                                           // //"�к�"

    private DynAreaVO m_oDynArea = null;//����ָ����ȡ������ڶ�̬��������ȡ�����¼��ǰ��̬����vo

    //��̬��������Ӧ�Ĺؼ������
    private KeyGroupVO m_oKeyGroupVO;

    private Hashtable m_oCreateKey;

    //��¼��ʼʱ��ָ����ȡ�����ڶ�̬������ѡ��ķ�Χ���������ĳ�ʼ�ؼ���
    private Vector m_oOriUfKeyword;

    //��¼�ؼ���Ӧ�ú�Ĺؼ�����ϣ����Ӧ�ù������ڵ��ȷ��ʱ���Ͳ��ٱ���ؼ�����
    private KeyGroupVO m_boKeyGroupIsApply = null;

    /**
     * ָ�������ռ����飬�ֱ��ʾ�п������ƺ��п�������
     */
    private String[] m_strsRowNames = null;

    private String[] m_strsColNames = null;

    //�����ռ��Ӧ����ʼ����
    private int m_iStartRow = -1;

    private int m_iStartCol = -1;

    //ָ����ȡ���Ѿ�������ָ�����Ƽ��ϣ����ڴ�������ָ����ȡ��ʹ��Hashcode���в��ұȽϿ�
    private HashSet m_nameHashset = null;

    //�������Ѷ����ָ�����ɼ���
    private HashSet m_HasAllNameHashset = null;

    private boolean m_bIsAnaRep = false;

    private boolean m_bNowEditing = false;

    //ָ����պ���ж��Ƿ��ظ��Ľ��״ֵ̬
    public static int MEASREF_NO_ERR = 0;//û���ظ�

    public static int MEASREF_NAME_REPETITION_ERR = 1;//�����ظ�

    public static int MEASREF_MEAS_REPETITION_ERR = 2;//���յ�ָ���ظ�

    private ReportCache m_ReportCache = CacheProxy.getSingleton()
            .getReportCache();

    private CellsModel m_cellsModel = null;
    
    private CellPosition[] m_cells = null;

    /**
     * TargerData ������ע�⡣
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
        //  ��ʼ�� m_oMVector
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
              //add by ll, 2007-11-06�����յ�Ԫ��������ָ������
                Format tmp = cellsModel.getRealFormat(selPoss[i]);
                if(tmp == null || tmp.getCellType() != TableConstant.CELLTYPE_STRING)
                	mvo.setType(MeasureVO.TYPE_NUMBER);
                else{
                	mvo.setType(MeasureVO.TYPE_CHAR);
                	//add by wangyga 2008-7-9 �����Ԫ�������ַ�����ȡָ��ʱ����Ӧ����64
                    mvo.setLen(TableConstant.CELLTYPE_STRING_LENGTH);
                }               	
                mvo.setAttribute(MeasureVO.ATTR_COIN_YUAN);

            } else {
            	//update by guogang 2007-8-15 ��¡ָ����󣬱������ָ�����õ�ʱ��Ի�����κβ���������Ļ����ڵĶ���
            	MeasureVO measureVo = (MeasureVO)mvo.clone();
                mtvo.setMeasureVO(measureVo);
                mtvo.setRefedMeasure(new Boolean(!m_strReportId.equals(mvo.getReportPK())));
//                if(!mtvo.getRefedMeasure()){//modify by wangyga 2008-7-18 ��ֹ���������ָ�꣬ȡ����ָ���������ԭ��������ָ��ı���
//                	createMeasureName(selPoss[i], measureVo);
//                }
                if(mtvo.getRefedMeasure()){//��ȡָ��������֮��(���ϱߵ������һ��)����ȡ�����ã���ֹȡ������ָ���ԭ����ָ������û�б��棬�������ƻ�����ʾ������ָ��ı���
                	MeasureVO msVo = (MeasureVO)measureVo.clone();//��ֹ�޸ĵ�ԭָ������������              	
                	createMeasureName(selPoss[i], msVo,false);//�ٹ���һ���Զ����ɵ�ָ�����ƣ�������mtvo�����ָ����ȡ�Ի���֮������ȡ�����ã���ԭ����
                	mtvo.setOriginalMeasureName(msVo.getName());
                }
            }
            m_oMVector.add(mtvo);
        }

        //    if (dynarea != null) {
        //        //��̬����ָ����ȡ
        //        this.m_oDynArea = dynarea;
        //        this.m_oTableMeasList = dynarea.getMeasureList();
        //      	if(m_oTableMeasList == null )
        //      	{
        //	      	m_oTableMeasList = new EntityList();
        //	      	
        //      	}
        //        //ϵͳ�Զ�����һ�����кš��ؼ���,����ùؼ����Ѿ��ڸö�̬�����ڴ��ڣ���ֱ������
        //        Vector keys = m_oDynArea.getKeyVO();
        //        //�ùؼ������ֻ��¼��̬�����ڶ���Ĺؼ�����Ϣ,������¼�����еĹؼ�����Ϣ
        //		this.m_oKeyGroupVO = new KeyGroupVO();
        //		Vector dynkeys = m_oDynArea.getKeyVO();
        //		if( dynkeys != null && dynkeys.size() > 0 )
        //		{
        //            KeyVO[] vos = new KeyVO[dynkeys.size()];
        //            dynkeys.copyInto(vos);
        //            m_oKeyGroupVO.addKeyToGroup(vos);
        //		}
        //�����̬����û�ж����ָ��͹ؼ��֣����Զ������кŹؼ���
        //        if(m_oTableMeasList.getEntityCount()==0&&(keys ==
        // null||keys.size()==0))
        //        {
        //	        CellProperty cp;
        //			//����Ǳ��ﵥԪ��ֱ�ӷ���
        //			UfoArea area = new UfoArea(dynarea.getPos());
        //
        //			cp = ufotable.getFormatModel().getValueAt(area.getStartCell());
        //			if((cp == null||cp.getDataType() !=
        // CellDataType.CELL_SAMPLE)&&(!IsAnaRep()) )//������Ԫ����ȡ
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
        //				//����ָ����PK����ָ��PK
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
     * ��������area���Ը�����Ϊ���㣬Ѱ�Ҹ�������ߡ��ϱߵ�Ԫ�ĵ�һ��������ֵ������ָ������ ���area��һ����Ԫ����ֱ�ӿ��Բ���
     * ���area��һ����������area�����Ͻǵ�Ԫ�������н���Ѱ�ң�ֱ���ҵ���
     * ������ͬһ������ָ�����Ʋ������ظ��������ִ������ʱ������ȡ������ָ����������Ϊϵͳ�Զ�����һ������
     * ����������ڲ��Һ��������϶�û�б�����Ԫ������Ϊ���ַ�������ϵͳ�Զ�����һ������ �������ڣ�(2003-8-14 14:21:19)
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
        //��������ߵĵ�һ��������Ԫ.
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
    	char[] chineseNums = new char[]{'һ','��','��','��','��','��','��','��','��','��','ʮ'};
    	for(char chineseNum : chineseNums){
    		if(chineseNum == ch){
    			return true;
    		}
    	}
		return false;
	}
	/**
     * ���ر����Ƿ����ظ����Ƶ�ָ��,��������.
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
     * ȥ���Ƿ��ַ�,ֻ�����ַ������֡����֡�
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
                if (bytes.length > 1) {//����
                	if(!isChineseBD(c)){
                		sb.append(c);
                	}
                }
            }
        }
        return sb.toString();
    }

    private boolean isChineseBD(char c) {
    	char[] bds = new char[]{'��','��','��','��'};
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
     * ȡ��ȫ����ȡ��־ �������ڣ�(2002-6-3 16:24:47)
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
                    			  //modify by guogang 2007-8-14 ȫѡȡ����ʱ��ȡ��ָ�������
                    			  try {
									vo.cancelRefMeasure(m_strReportId, getMeasruePackPK(CellPosition.getInstance(vo.getActPos())));
								} catch (Exception e) {
									AppDebug.debug(e);
								}
                    		  }
                                  
                              vo.setFlag(Boolean.FALSE);
                    	}
                    }else if(col==TYPE_COLUMN){//ָ������
                    	if(toSetValue == null){
                    		toSetValue = vo.getMeasureVO().getType();
                    		if(((Integer)toSetValue).intValue() == MeasureVO.TYPE_NUMBER){
                    			toSetValue = TYPES[1];
                    		}else{
                    			toSetValue = TYPES[0];
                    		}
                    	}
                    	setValueAt(toSetValue, row, col);
                    }else if(col == HEBING_COLUMN){//�Ƿ�ϲ�ָ��
                    	if(toSetValue == null){
                    		toSetValue = (Integer)vo.getMeasureVO().getExttype();
                    		if(((Integer)toSetValue).intValue() == MeasureVO.TYPE_EXT_HEBING){
                    			toSetValue = HEBING[0];
                    		}else{
                    			toSetValue = HEBING[1];
                    		}
                    	}
                    	setValueAt(toSetValue, row, col);
                    }else if(col == DIRECTION_COLUMN){//�ϲ�����
                    	if(toSetValue == null){
                    		toSetValue = HBBBMeasParser.getDirection(vo.getMeasureVO().getProps());
                    		if(((Integer)toSetValue).intValue() == HBBBMeasParser.DIRECTIONG_J){
                    			toSetValue = DIRECTION[2];
                    		}else{
                    			toSetValue = DIRECTION[1];
                    		}
                    	}
                    	setValueAt(toSetValue, row, col);
                    }else if(col == DXTYPE_COLUMN){//�Ƿ��ڳ��ϲ�ָ��
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
     * �˴����뷽�������� �������ڣ�(2003-8-29 10:09:38)
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
     * ��ؼ�����������ӹؼ��� �������ڣ�(2003-9-25 11:56:20)
     * 
     * @param key
     *            nc.vo.iufo.keydef.KeyVO
     */
    //private void addToKeyGroup(KeyVO key)
    //{
    //	m_oKeyGroupVO.addKeyToGroup(new KeyVO[]{key});
    //}
    /**
     * ��鶯̬����ʱ�Ĺؼ��ֺϷ��� 1����̬�����ڹؼ��ֻ���ȫ��Ϊ���У�����ȫ��˽�� 2���ؼ��ָ���������3�� �������ڣ�("2003-9-10"
     * 20:07:01)
     */
    //public void checkDynAreaKeys() throws CommonException
    //{
    //	if(m_oDynArea == null )
    //		return;
    //	//�Զ�̬�����ڵĹؼ�������λ��ΪKey,����ȫ����̬���������йؼ��ֵ�Hashtabl�������Ѿ����õĺ������õ�
    //	Hashtable keyTable = new Hashtable();
    //
    //	////�õ���̬�������Ѿ�����Ĺؼ���
    //	Vector hasVo = m_oDynArea.getKeyword();
    //
    //	//ȡ�������ж���Ĺؼ��ָ���,��������л�û�ж���ؼ���,��Ϊ0
    //	int mainDefKeysNum = 0;
    //	if( m_ufoReport.getVecKeyVO() != null )
    //		mainDefKeysNum = m_ufoReport.getVecKeyVO().size();
    //
    //
    //	//����Ƿ�˽�й��йؼ�����һ����
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
    // //"�ڶ�̬�����ڶ���Ĺؼ��ֱ���ͬΪ���л�˽�йؼ��֣�"
    //				throw new CommonException(strError);
    //			}
    //		}
    //        if(IsAnaRep())
    //        {
    //            if(isPrivate.booleanValue()){
    //            	strError = StringResource.getStringResource("miufo1001510");
    // //"�������еĶ�̬�����ڲ��ܶ���˽�йؼ��֣�"
    //                throw new CommonException(strError);
    //            }
    //        }
    //
    //		if(key.getType() == KeyVO.TYPE_TIME )
    //		{
    //			if(isDefedKey){
    //				strError = StringResource.getStringResource("miufo1001511");
    // //"�ڶ�̬�����ڲ���ͬʱ��������ʱ��ؼ��֣�"
    //				throw new CommonException(strError);
    //			}
    //			isDefedKey = true;
    //		}
    //	}
    //	StringBuffer errBuff = new StringBuffer();
    //	//�ж����ӱ����֮����ۻ��Ƿ񳬹��˹ؼ�����ϵ��������
    //	if ( mainDefKeysNum + allKeyVec.size() >
    // nc.vo.iufo.keydef.KeyGroupVO.MaxCount )
    //	{
    //		String strError = StringResource.getStringResource("miufo1001512");
    // //"����Ĺؼ�����������������{0},���������Ѿ�����{1}��������̬�����Ѿ�����{2}����"
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
    // //"��Ϊ��˽�йؼ���ָ�����ƣ�"
    //			throw new CommonException(commonMessage);
    //		}
    //		if(name.length() > KeyVO.NAME_MAX_LEN)
    //		{
    //			commonMessage =StringResource.getStringResource("miufo1001514");
    // //"��˽�йؼ������Ƴ����������¶��壡"
    //			throw new CommonException(commonMessage);
    //		}
    //		if( UICacheManager.getSingleton().getKeywordCache().getByName(name,null)
    // != null )
    //		{
    //			commonMessage = StringResource.getStringResource("miufo1001515");
    // //"��˽�йؼ��ֺ͹��йؼ��������������¶��壡"
    //			throw new CommonException(commonMessage);
    //		}
    //		return true;
    //	}
    public boolean checkMeasureName(String name, MeasurePosVO vo)
            throws CommonException {
        String commonMessage = "";
        if (name.trim().equals("")) {
            commonMessage = StringResource.getStringResource("miufo1001516"); //"��Ϊ��ָ��ָ��ָ�����ƣ�"
            throw new CommonException(commonMessage);
        }
        //��ָ��������һ��ָ��ʱ
//        char[] ch = name.toCharArray();
//        for (int i = 0; i < ch.length; i++) //�ж�ָ���������Ƿ��зǷ��ַ�
        if(name.indexOf("->") >= 0 || name.indexOf('\"') >= 0 || name.indexOf('\'') >= 0)
        {
//            if (!(Character.isLetterOrDigit(ch[i]))) {
                commonMessage = StringResource
                        .getStringResource("miufo1001517"); //"ָ������'{0}'�����Ϲ淶�������Ƿ��ַ����������¶��壡"
                String[] params = { name };
                throw new CommonException(commonMessage, params);
//            }
        }
//        MeasureVO cachevo = CacheProxy.getSingleton()
//                .getMeasureCache().loadMeasuresByName(m_strReportId,
//                        name.toUpperCase());
//        if (cachevo != null
//                && (!cachevo.getCode().equalsIgnoreCase(
//                        vo.getMeasureVO().getCode())))//���ָ�������Ѵ��ڣ��򵯳��Ի���
        if(hasDefedMeasureName(vo.getMeasureVO(),name))
        {
            String sHint = getBadNameHint(m_strReportId, name.toUpperCase());
            String message = messageProcess(sHint, 32);
            UfoPublic.sendErrorMessage(message,parentDialog,null);
//            int j = JOptionPane.showConfirmDialog(parentDialog, message,
//                    StringResource.getStringResource("miufo1000718"), //"���Ѽ���"
//                    JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
//            if (j == JOptionPane.NO_OPTION) {
                return false;
//            }
//            MeasureVO mm = CacheProxy.getSingleton()
//                    .getMeasureCache().loadMeasuresByName(m_strReportId,
//                            name.toUpperCase());
//            if (isReferenced(mm, vo) != MEASREF_NO_ERR
//                    || (m_bIsAnaRep && !mm.getCode().equals(
//                            vo.getMeasureVO().getCode())))//ֻ���ڸñ�����δ�����ù���ָ����ܱ�����
//            {
//                vo.setRefMeasure(mm);
//            } else {
//                commonMessage = StringResource
//                        .getStringResource("miufo1001518"); //"�Ѿ����ã����壩����ָ�꣬�����ٴ����ã����壩������ͨ����Ԫ��ʽʵ�֣�"
//                throw new CommonException(commonMessage);
//            }
        } else //����޸ĵ�ָ�����Ʋ����ڣ���������ָ��
        {
            MeasureVO measVo = new MeasureVO();
            measVo.setName(name);
            measVo.setCode(vo.getMeasureVO().getCode());
            if (isReferenced(measVo, vo) != MEASREF_NO_ERR) {
                commonMessage = StringResource
                        .getStringResource("miufo1001518"); //"�Ѿ����ã����壩����ָ�꣬�����ٴ����ã����壩������ͨ����Ԫ��ʽʵ�֣�"
                throw new CommonException(commonMessage);
            }
        }
        return true;
    }

    /**
     * �ڶ�̬�������޸Ĺؼ���ʱ���������ȡ�е�ָ������� �������ڣ�(2003-10-21 15:46:05)
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
     * ���ָ�������ռ䣬 ע�⣺����ָ����ȡ������ɺ󣬱���ִ�д˲����� �������ڣ�(2003-11-5 16:05:57)
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
     * �ӹؼ��������ɾ���ؼ��� �������ڣ�(2003-9-25 11:56:20)
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
     * ��������ظ���ʾ����ĳ�ͻλ�á�
     */
    private String getBadNameHint(String sReportPK, String sName) {
        StringBuffer sHint = new StringBuffer(StringResource
                .getStringResource("miufo1001519")); //"ָ�����ƺ��Ѿ����ڵ�ָ�����Ƴ�ͻ��λ���ڣ�����->"
        //ǰ���Ѿ���֤��ָ�����
        nc.vo.iuforeport.rep.ReportVO reportvo = (nc.vo.iuforeport.rep.ReportVO) CacheProxy
                .getSingleton().getReportCache().get(sReportPK);
        sHint.append(reportvo.getName());
        sHint.append(StringResource.getStringResource("miufo1001520")); //"
                                                                        // ָ��->"
        sHint.append(sName);
//        sHint.append(StringResource.getStringResource("miufo1001521")); //".������������ָ�꽫�����ڵ�ָ���滻,�Ƿ������"
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
     * �˴����뷽�������� �������ڣ�(2003-8-29 10:09:38)
     * 
     * @return java.util.Hashtable
     */
    public Hashtable getCreatedUfoKeyword() {
        if (m_oCreateKey == null)
            m_oCreateKey = new Hashtable();
        return m_oCreateKey;
    }

    /**
     * �˴����뷽�������� �������ڣ�(2003-8-29 10:08:49)
     * 
     * @return com.ufsoft.iuforeport.reporttool.temp.DynamicAreaVO
     */
    public DynAreaVO getDynAreaVO() {
        return m_oDynArea;
    }

    /**
     * �˴����뷽�������� �������ڣ�(2003-8-17 15:58:41)
     * 
     * @return nc.vo.iufo.keydef.KeyGroupVO
     */
    public KeyGroupVO getKeyGroupVO() {
        return m_oKeyGroupVO;
    }

    /**
     * ȡ��Ŀǰ��ѡָ��������λ������һ�� �������ڣ�(2003-8-27 23:27:42)
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
     * �˴����뷽�������� �������ڣ�(2002-6-11 10:37:01)
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
     * �˴����뷽�������� �������ڣ�(2003-9-4 14:23:50)
     * 
     * @return com.ufsoft.iuforeport.reporttool.temp.EntityList
     * @i18n uiiufofmt00012=����
     */
    //public com.ufsoft.iuforeport.reporttool.temp.EntityList
    // getTableMeasList() {
    //	return m_oTableMeasList;
    //}
//    /**
//     * �˴����뷽�������� �������ڣ�(2002-5-15 16:42:23)
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
                    return StringResource.getStringResource("miufopublic328"); //"ʱ��"
                return TYPES[type];

//            case ATTRIBUTE_COLUMN:
//                //û�е�λ,�򷵻�""
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
     * ����Ҫ����r_vector,��ֹĳЩָ��״̬�Ĵ����ʾ, �������ĳЩ����ȡ��ָ��,״̬Ϊisadded��
     * �����ֲ�����ָ�꣬��ʱ���²��յ�ָ��Ͳ�������ȡ��ָ���ˣ� ����ʱ��ָ��ʱһ��ָ�꣬���Ҳ�����༭�����Դ�ʱָ��״̬Ϊ-1��
     * ��ʾĬ��ֵ����û�н��й�����
     */
    public Vector getVector() {
        return this.m_oMVector;
    }

    /**
     * ����Ҫ����r_vector,���˵��ؼ���λ��ָ�꣬��Ϊ�ؼ��ֲ��ı䵥Ԫ����
     */
    public Vector getVectorForCellProp() {
        Vector reVec = new Vector();
        if (m_oMVector != null) {
            MeasurePosVO mvo;
            for (int i = 0; i < m_oMVector.size(); i++) {
                mvo = (MeasurePosVO) m_oMVector.get(i);
                if (m_oCreateKey == null
                        || m_oCreateKey.get(Integer.toString(i)) == null) {
                    //�ǹؼ�����
                    if (mvo.getFlag().booleanValue()) {
                        reVec.addElement(m_oMVector.get(i));
                    }
                } else {//�ؼ�����
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
     * ���ݴ���������趨ָ�������ռ��е���������ĳ���d �������ڣ�(2003-11-5 16:11:06)
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
        //����ǹؼ��֣�������༭
//        if (m_oCreateKey != null) {
//            key = (UfoKeyWord) m_oCreateKey.get(Integer.toString(r));
//            //ϵͳԤ�ò������޸�
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
        //���õ�ָ��,������༭
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
//            //�Ƕ�̬����������ȡ�ɹؼ���
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
                //����Ǻϲ�ָ�꣬������༭
                if (vo.getMeasureVO().getExttype() == MeasureVO.TYPE_EXT_HEBING)
                    return false;

//            }
            return true;
//        case ATTRIBUTE_COLUMN:
//
//            //ָ��ֻ�е�����Ϊ��ֵʱ����༭
//            if (vo.getMeasureVO().getType() == MeasureVO.TYPE_NUMBER)
//                return true;
//            return false;
        case LENGTH_COLUMN:
            if (vo.getMeasureVO().getType() == MeasureVO.TYPE_CHAR)
                return true;
            return false;
        case CODEREFERENCE_COLUMN:
            //ָ��ֻ�е�����Ϊ����ʱ����༭
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
        case REPORT_CODE://������������༭
            return false;
//        case KEY_REF_COLUMN:
//            if (m_oDynArea == null || vo.getFlag().booleanValue()) {
//                return false;
//            }
//            return true;
        case MEASURE_REF_COLUMN:
//            if (m_oDynArea == null) {
//                //δ���ùؼ��֣����ܲ���ָ��
//                Vector keyvec = getUfoReport().getFormatModel().getKeyVO();
//                if (keyvec == null || keyvec.size() == 0)
//                    return false;
//            }
            return true;

        }
        return true;
    }

    /**
     * �ж��Ƿ��Ǻϲ�ָ�� �������ڣ�(2002-6-21 16:11:23)
     * 
     * @return boolean
     * @param meaCode
     *            java.lang.String
     */
    public boolean isHBBBMeasure(String meaCode) {

        MeasureVO pmvo = CacheProxy.getSingleton().getMeasureCache()
                .getMeasure(meaCode);
        if (pmvo != null) {
            if (pmvo.getProps() != null)//��ָ���Ǻϲ�ָ��
            {
                return true;
            }
        }
        return false;
    }

    /**
     * �˴����뷽�������� �������ڣ�(2003-8-17 16:53:28)
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
     * �ж��Ƿ��Ѿ����չ���ָ�� �������ڣ�(2002-6-15 13:09:41)
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
            //ȫ����ָ���Ƿ�����
            //������PK�ظ���˵���ظ�
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
     * ������ʾ��ʱ���ַ����ĳ��Ȼ�Ӱ����ʾ��ĳ��ȣ� ����������ʾ��ĳ������ޣ��ַ�������ʾ��ȫ����������ʾ֮ǰ
     * �Ƚ����ַ�����������Ҫ���е�ʱ��������ʾ message Ҫ���д�����ַ��� wordNum ÿ����ʾ���ַ�������,��ǰĬ��Ϊ32�ĳ���
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
     * ��Ӧָ����� �������ڣ�(2003-8-29 10:12:03)
     */
    private void onMeasureRef(int r) {

    }

    /**
     * ���涯̬�����ڵĹؼ��� �������ڣ�(2003-9-25 11:24:42)
     * 
     * @return boolean ��ʶ�Ƿ񱣴����
     */
    //public boolean saveKeyWord()
    //{
    //	try {
    //		//����Ѿ�Ӧ�ù�����û���ڸı���ؼ��֣������ٴα���
    //		if(m_boKeyGroupIsApply != null &&
    // m_boKeyGroupIsApply.equals(m_oKeyGroupVO))
    //			return true;
    //		//�����������ȡ�����ý��б���
    //		if( m_oDynArea == null )
    //			return true;
    //		//���û��ָ����Ҫ���棬��ֱ�ӷ���
    //	    if (m_oMVector == null || m_oMVector.size() == 0) {
    //	        return true;
    //	    }
    //
    //		Hashtable ufoKeyTable = new Hashtable();
    //	    //���Ⱥͱ�����ȡ�����ĳ�ʼ�ؼ������Ƚϣ����Ƿ��и���
    //	    Vector dynkeys = m_oKeyGroupVO.getVecKeys();
    //	    //��Ҫ����Ĺؼ����б�
    //	    Vector dynKeyword = new Vector();
    //		//����ǰָ����ȡ�����еĹؼ�����һ�������б�
    //	    for( int l = 0 ; l < dynkeys.size() ; l++ )
    //	    {
    //		    KeyVO vo = (KeyVO)dynkeys.get(l);
    //		    ufoKeyTable.put(vo.getKeywordPK(),vo);
    //	    }
    //	    //����Ƿ��޸Ĺ��ؼ���
    //		boolean isEdited = false;
    //		//����Ƿ��������ɾ�ؼ���
    //		boolean isCreateOrDelete = false;
    //		UfoKeyWord keyword = null;
    //		MeasurePosVO mvo = null;
    //		KeyVO key = null;
    //		//��̬�����ڵ�ȫ���ؼ���
    //		Vector allDynKws = m_oDynArea.getKeyword();
    //		Hashtable editedKeyTable = new Hashtable();
    //
    //		//ѭ���ж�,��������ȡ�����а����Ĺؼ����ҳ�,�������UfoKeyWord�������ָùؼ���������ȡ�Ļ����޸Ĺ�λ�õĻ������޸ı����Ϊtrue
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
    //		            //�ж�ͳһ�ؼ����Ƿ����Է����ı�
    //					for(int j = 0 ; j < allDynKws.size() ; j++ )
    //					{
    //						KeyVO okey = ((UfoKeyWord)allDynKws.get(j)).getKeyVO();
    //						//okey��ʱ!= null
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
    //						//���ԭ�ؼ�������в������ùؼ��֣���˵���ùؼ���Ϊ����
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
    //		//��������˹ؼ���,������Ӧ��ǰ��,dynKeyword.size() != m_oOriUfKeyword.size()������޸��˹ؼ���
    //		//�˴���������ж���Ҫ��Ϊ���жϽ�ѡ�еĹؼ���ȫ����ȥ�����
    //		if( (m_oOriUfKeyword == null && dynKeyword.size() != 0)
    //			||(m_oOriUfKeyword != null) && dynKeyword.size() !=
    // m_oOriUfKeyword.size())
    //			isCreateOrDelete = true;
    //
    //		//ֻ���޸Ĺ����Ž��йؼ��ֵı���
    //		//�����̬������ԭ���Ѿ��йؼ��֣�����ʾ�û����޸Ĺؼ��ֻ����ָ������
    //
    //		if(isCreateOrDelete || isEdited)
    //		{
    //			if( isCreateOrDelete )
    //			{
    //
    //				if( allDynKws.size() > 0)
    //				{
    //					String sHint = StringResource.getStringResource("miufo1001522");
    // //"�ؼ����޸Ļᵼ�±���̬���������ݱ�ɾ����ͬʱ������ö�̬����������ָ�����ã�"
    //					int j = JOptionPane.showConfirmDialog(parentDialog,
    //						sHint,StringResource.getStringResource("miufo1000718"), //"���Ѽ���"
    //						JOptionPane.YES_NO_OPTION,JOptionPane.WARNING_MESSAGE);
    //					if( j== JOptionPane.NO_OPTION )
    //					{
    //                        return false;
    //
    //					}else//���ָ������,�������ʱ���ٽ��б����������
    //					{
    //						m_ufoReport.clearDynRefMeasures(m_oDynArea.getDynamicAreaPK());
    //						clearMeasureRef();
    //					}
    //				}
    //			}
    //				//����̬������δ��ѡ��Ĺؼ���װ�뱣���б���
    //				loopa:
    //				for(int m = 0 ; m < allDynKws.size() ; m++ )
    //				{
    //					keyword = ( UfoKeyWord)allDynKws.get(m);
    //					if( m_oOriUfKeyword == null ||(!m_oOriUfKeyword.contains(keyword) ))
    //					{
    //						//�˴�Ҫ��ԭ��ָ������жϣ���ֹ��ԭ����̬������û�ж���ؼ��ֵ�ʱ�򣬶��Ӧ�ùؼ���ʱ���ֹؼ��������쳣
    //						//������������̬������ԭ��û�йؼ��֣����ùؼ�����Ӧ�ú����޸�ĳһ�ؼ��֣��ٴ�Ӧ�õ�ʱ��ͷ��ֹ�������еĹؼ�����ʾ��ָ����ȡ�Ĺؼ��ֲ���
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
    //				//��ն�̬����ؼ����б�
    //				m_oDynArea.setKeyword(new Vector());
    //				m_oDynArea.setKeyVO(new Vector());
    //				//ѭ���������б��еĹؼ������õ���̬������ȥ
    //				for(int n = 0 ; n < dynKeyword.size() ; n++ )
    //				{
    //					keyword = ( UfoKeyWord)dynKeyword.get(n);
    //					m_oDynArea.addKeyWord(keyword);
    //					m_oDynArea.addKeyDef(keyword.getKeyVO());
    //				}
    //
    //
    //			
    //			//�˴���Ϊ�˴�����Ӧ�ùؼ��ֺ��ڵ��ȷ���Ϳ��Բ����ٴ�ȥ���ñ���������ؼ���
    //			if(m_boKeyGroupIsApply == null )
    //				m_boKeyGroupIsApply = new KeyGroupVO();
    //			m_boKeyGroupIsApply.addKeyToGroup(m_oKeyGroupVO.getKeys());
    //		}
    //    }catch (Exception e) {
    //        //�����쳣
    //		AppDebug.debug(e);
    //    }
    //    return true;
    //}
    public void setColName(int actCol, String name) {
        m_strsColNames[actCol - m_iStartCol] = name;
    }

    /**
     * �˴����뷽�������� �������ڣ�(2003-9-1 20:56:17)
     * 
     * @param newDialog
     *            com.ufsoft.iuforeport.reporttool.table.CreateMeasureDialog
     */
    public void setDialog(MeasureDefineDialog newDialog) {
        parentDialog = newDialog;
    }

    //���ñ������ã���������˱������ã��򷵻�true����ʾָ��������Ѿ��������ˣ�false��ʾδ�޸�ָ������
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
     * ���ùؼ������ͣ��ؼ���û����ֵ���ͣ����ѡ����ֵ���ͣ���Ĭ��Ϊ�ַ�
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
    //			vo.getKeyVO().setLen(TYPESLEN[i]);//���չ涨�ĳ��ȸ�ֵ��ÿ��ֵ��Ӧһ��ָ������
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
    //		//Ϊ�����������ѡ��һ��Ĭ�ϱ���
    //		setKeyCodeRefence("",vo);
    //	}
    //	return true;
    //}
    //���ñ������ã���������˱������ã��򷵻�true����ʾָ��������Ѿ��������ˣ�false��ʾδ�޸�ָ������
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
     * ����ָ������ ���ָ����������ֵ�򸡶���ֵ����λ��ѡ���������ò���ѡ��ָ�곤�Ȳ��ɶ���
     * ���ָ���������ַ��򸡶��ַ�����λ����ѡ���������ò���ѡ��ָ�곤�ȿɶ���
     * ���ָ�������Ǳ���򸡶����룬��λ����ѡ���������ÿ�ѡ��ָ�곤�Ȳ��ɶ���
     *  
     */
    public boolean setMeasureType(String type, MeasurePosVO vo) {

        for (int i = 0; i < TYPES.length; i++) {
            if (type.equals(TYPES[i])) {
                vo.getMeasureVO().setType(i);
                vo.getMeasureVO().setLen(TYPESLEN[i]);//���չ涨�ĳ��ȸ�ֵ��ÿ��ֵ��Ӧһ��ָ������
                vo.getMeasureVO().setAttribute(TYPESUNIT[i]);
                break;
            }

        }
        if (vo.getMeasureVO().getType() != MeasureVO.TYPE_CODE) {
            setMeasureCodeRefence(null, vo);
        } else {
            //Ϊ�����������ѡ��һ��Ĭ�ϱ���
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
     * ���ò��յĹؼ���, �������ڣ�(2003-8-17 16:33:39)
     * 
     * @param vo
     *            com.ufsoft.iuforeport.reporttool.temp.MeasurePosVO
     */
    //public void setRefkey(int row,KeyVO keyvo) throws CommonException
    //{
    //	if( m_oKeyGroupVO.getSize() + 1 <= KeyGroupVO.MaxCount )
    //	{
    //		MeasurePosVO vo = (MeasurePosVO)m_oMVector.get(row);
    //		//ָ�겻�ܲ���Ϊ�ؼ���
    //		if(vo.getFlag().booleanValue()){
    //			String strError = StringResource.getStringResource("miufo1001523");
    // //"ָ�겻�ܲ���Ϊ�ؼ��֣�"
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
    // //"�ڶ�̬�����ڲ���ͬʱ��������ʱ��ؼ��֣�"
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
    // //"�ڶ�̬�������Ѿ�����ùؼ��֣������ظ����壡"
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
    // //"������Ĺؼ�������йؼ��ֵ���Ŀ�Ѿ��ﵽ������ޣ��������ٲ��գ�"
    //		throw new CommonException(strError);
    //	}
    //	fireTableDataChanged();//ˢ����ʾ
    //}
    /**
     * �˴����뷽�������� �������ڣ�(2002-6-11 10:34:21)
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
     * �˴����뷽�������� �������ڣ�(2002-5-9 16:37:39)
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

        //ȡ��vo����չ���ԣ�
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
                //���ָ��Ϊ����ָ�꣬����ȡ��ָ��ʱȡ�����ã���Ϊȡ����ָ�겻����ָ�����õı�־�����ԣ��˴�ִ��ʱ������ȡ��ָ��ʱ���ã�
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
                if (!((Boolean) obj).booleanValue()) {//����������ȡ�����ա�
                    if (IsAnaRep()) {
                        String message = StringResource
                                .getStringResource("uiuforep000223");//"�������в�����ȥ��ָ�����"
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
                //ָ������Ҫ����Ƿ�Ϸ����Ƿ��뵱ǰ��ָ�껺���е�ָ���������д�Ҫ��ʾ�û�
                if (!name.equals(vo.getMeasureVO().getName())) {
                    if (checkMeasureName(name, vo)) {
                        //ָ�����Ʋ����Զ�ת���ɴ�д modify by chxw 2008-03-02 
                    	//vo.getMeasureVO().setName(name.toUpperCase());
                    	vo.getMeasureVO().setName(name);
                    }
                }
                break;
            case LENGTH_COLUMN:
                //ָ�곤��Ҫ��֤�Ϸ��ԣ��д�Ҫ��ʾ�û�
                String length = (String) obj;
                String checkLength = null;
                int len;
//                if (ufKeyvo != null) {
//                    len = Integer.parseInt(length);
//                    if (len <= 0 || len > 64) {
//                        checkLength = StringResource
//                                .getStringResource("miufo1001527"); //"�ַ����͹ؼ��ֵ���������0-64֮�䣡"
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
                //ָ��˵��������
                String note = (String) obj;
//                if (ufKeyvo != null) {
//                    note = note.trim();
//                    if (note.length() > KeyVO.NOTE_MAX_LEN) {
//                        String strError = StringResource
//                                .getStringResource("miufo1001528"); //"�ؼ���˵��������"
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
                //����������ø�ѡ���ѡ��ı䣬�������ʾ�ı������������ҵ���Ӧ�ı�������ID���б���
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
                //��ָ��Ϊ�ϲ�ָ��ʱ������Ϊ��ѡ�
                if (props == null) {
                    props = new Hashtable();
                }
                if (DIRECTION[0].equals(obj)) {
                    //�����ǰָ���Ǻϲ�ָ�꣬����ѡ���˸÷�����Ĭ��Ϊ�跽��
                    if (vo.getMeasureVO().getExttype() == MeasureVO.TYPE_EXT_HEBING) {
                        HBBBMeasParser.setDirection(
                                HBBBMeasParser.DIRECTIONG_J, props);
                        vo.getMeasureVO().setProps(props);
                        vo.getMeasureVO().setExttype(MeasureVO.TYPE_EXT_HEBING);
                    }
                } else if (DIRECTION[1].equals(obj)) {
                    //�����ǰָ�겻�Ǻϲ�ָ�꣬����ѡ���˸÷�����ô����ָ���Զ���Ϊ�ϲ�ָ��
                    HBBBMeasParser.setDirection(HBBBMeasParser.DIRECTIONG_J,
                            props);
                    vo.getMeasureVO().setProps(props);
                    vo.getMeasureVO().setExttype(MeasureVO.TYPE_EXT_HEBING);
                } else if (DIRECTION[2].equals(obj)) {
                    //�����ǰָ�겻�Ǻϲ�ָ�꣬����ѡ���˸÷�����ô����ָ���Զ���Ϊ�ϲ�ָ��
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
                    //�����ǰָ�겻�Ǻϲ�ָ�꣬����ѡ���˸����ͣ���ô����ָ���Զ���Ϊ�ϲ�ָ�꣬
                    //��ʱ��Ĭ�Ϸ���Ϊ�跽�򣬲�������չ����Ϊ��������

                    HBBBMeasParser.setDxMeas(true, props);
                    Object object = props.get(HBBBMeasParser.PROP_DIRECT);
                    if (object == null) {
                        HBBBMeasParser.setDirection(
                                HBBBMeasParser.DIRECTIONG_J, props);
                    }
                    vo.getMeasureVO().setProps(props);
                    vo.getMeasureVO().setExttype(MeasureVO.TYPE_EXT_HEBING);
                } else {
                    //�����ǰָ���Ǻϲ�ָ�꣬ѡ���˸����ͣ�������չ����Ϊ�ǵ�������
                    if (vo.getMeasureVO().getExttype() == MeasureVO.TYPE_EXT_HEBING)
                        HBBBMeasParser.setDxMeas(false, props);
                }
                setMeasureType(TYPES[0], vo);
                break;
            case HEBING_COLUMN:
                if (HEBING[1].equals(obj)) {
                    //���ָ�걻��Ϊ�ϲ�ָ�꣬��ָ�������Ĭ��Ϊ��ֵ���ͣ����͸�ѡ������༭����
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
                    //���ָ�걻��Ϊ�Ǻϲ�ָ�꣬��ָ�����͸�ѡ������༭��Ĭ��Ϊ�޷��򣬷ǵ�������
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
            fireTableDataChanged();//ˢ����ʾ
            throw ce;
            //throw ce;

        } catch (Exception e) {
            parentDialog.setStateMessage(e.getMessage(), true);
            fireTableDataChanged();//ˢ����ʾ
            throw new CommonException(e.getMessage());
            //throw ce;
        }

        fireTableDataChanged();//ˢ����ʾ
        parentDialog.fireCompement(r);
        m_bNowEditing = false;
    }

    /**
     * setIsAnaRep
     * �����Ƿ��Ƿ�����
     * @param bIsAnaRep boolean
     */
    public void setIsAnaRep(boolean bIsAnaRep) {
        m_bIsAnaRep = bIsAnaRep;
    }

    /**
     * �����Ƿ��Ƿ�����
     * @return boolean
     */
    public boolean IsAnaRep() {
        return m_bIsAnaRep;
    }

    /**
     * ����ǰ�����Ƿ�����ʱ��У���Ƿ�����ȡ��ָ��ȫ���������մ���
     * @throws CommonException
     */
    public void checkAnaRepMeasure() throws CommonException {
        if (IsAnaRep()) {
            //���û��ָ����Ҫ���棬��ֱ�ӷ���
            if (m_oMVector == null || m_oMVector.size() == 0) {
                return;
            }

            //ͨ��ѭ���ķ�����ָ��ȫ�����浽ָ��ʵ������
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
  