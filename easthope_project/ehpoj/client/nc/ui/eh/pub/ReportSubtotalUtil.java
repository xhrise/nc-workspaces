package nc.ui.eh.pub;

/*
 * 创建日期 2005-7-25
 *
 * TODO 要更改此生成的文件的模板，请转至
 * 窗口 － 首选项 － Java － 代码样式 － 代码模板
 */

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;

import nc.bs.logging.Logger;
import nc.ui.pub.bill.BillItem;
import nc.ui.pub.bill.IBillItem;
import nc.ui.pub.report.ReportBaseClass;
import nc.ui.pub.report.ReportItem;
import nc.ui.pub.report.ReportTotalSubParent;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.report.SubtotalContext;
import nc.vo.pub.rs.IResultSetConst;
import nc.vo.pub.rs.InitLineMode;
import nc.vo.pub.rs.LineMode;
import nc.vo.pub.rs.MemoryResultSet;
import nc.vo.pub.rs.MemoryResultSetMetaData;
import nc.vo.pub.rs.ResultSetCalute;

/**
 * @author dengjt
 * 小计工具类
 */
public class ReportSubtotalUtil extends ReportTotalSubParent
{
	private SubtotalContext m_stctx = null;

    //小计指定列(按该列小计)
    private String m_strSubtotalCol = null;
    //小计运算列数组
    private String[] m_strSubtotalSumCols = null;
    
    
    
	public ReportSubtotalUtil(ReportBaseClass baseClass)
	{
		super(baseClass);
	}
	  /**
     * 此处插入方法说明.
     * 创建日期:(2002-11-19 16:23:13)
     * @param strGrpkeys java.lang.String[]
     * @param istotalAll boolean
     */
    public void subtotal() {
        subtotal(false);
    }

    /**
     * 此处插入方法说明.
     * 创建日期:(2002-11-21 9:02:16)
     * @param strGrps java.lang.String[]
     * @param strSumKeys java.lang.String[]
     */
    public void subtotal(String[] strGrps, String[] strSumKeys) {
        subtotal(strGrps, strSumKeys, false);
    }

    /**
     * 此处插入方法说明.
     * 创建日期:(2003-2-28 10:42:05)
     * @param strGrps java.lang.String[]
     * @param strSumKeys java.lang.String[]
     * @param sSumAssisUnitKeys java.lang.String[]
     * @param sAssisUnitFieldkey java.lang.String
     * @param sInventoryfieldName java.lang.String
     * @param htInvAssisUnitOrder java.util.Hashtable
     * @param isShowTotalOnly boolean
     */
    public void subtotal(String[] strGrps, String[] strSumKeys,
            String[] sSumAssisUnitKeys, String sAssisUnitFieldkey,
            String sInventoryfieldName, Hashtable htInvAssisUnitOrder,
            boolean isShowTotalOnly) {
        subtotal(strGrps, strSumKeys, sSumAssisUnitKeys, sAssisUnitFieldkey, sInventoryfieldName, htInvAssisUnitOrder, isShowTotalOnly, true);
    }
    
    public void subtotal(String[] strGrps, String[] strSumKeys,
            String[] sSumAssisUnitKeys, String[] sortCols, String sAssisUnitFieldkey,
            String sInventoryfieldName, Hashtable htInvAssisUnitOrder,
            boolean isShowTotalOnly) {
        subtotal(strGrps, strSumKeys, sSumAssisUnitKeys, sortCols,sAssisUnitFieldkey, sInventoryfieldName, htInvAssisUnitOrder, isShowTotalOnly, true);
    }
    
    public void subtotal(String[] strGrps, String[] strSumKeys,
            String[] sSumAssisUnitKeys,String sAssisUnitFieldkey,
            String sInventoryfieldName, Hashtable htInvAssisUnitOrder,
            boolean isShowTotalOnly, boolean isNested) {
    	subtotal(strGrps,strSumKeys,sSumAssisUnitKeys,null,sAssisUnitFieldkey,
                sInventoryfieldName,htInvAssisUnitOrder,
                isShowTotalOnly,isNested);
    }
    /**
     * 此处插入方法说明.
     * 创建日期:(2003-2-28 10:42:05)
     * @param strGrps java.lang.String[]
     * @param strSumKeys java.lang.String[]
     * @param sSumAssisUnitKeys java.lang.String[]
     * @param sAssisUnitFieldkey java.lang.String
     * @param sInventoryfieldName java.lang.String
     * @param htInvAssisUnitOrder java.util.Hashtable
     * @param isShowTotalOnly boolean
     */
    public void subtotal(String[] strGrps, String[] strSumKeys,
            String[] sSumAssisUnitKeys, String[] sortCols,String sAssisUnitFieldkey,
            String sInventoryfieldName, Hashtable htInvAssisUnitOrder,
            boolean isShowTotalOnly, boolean isNested) {
        SubtotalContext stctx = new SubtotalContext();
        stctx.setGrpKeys(strGrps);
        stctx.setSubtotalCols(strSumKeys);
        stctx.setSortCols(sortCols);
        stctx.setSubtotalName("--"
                + nc.ui.ml.NCLangRes.getInstance().getStrByID("_Template",
                        "UPP_Template-000348")/*@res "小计"*/+ "--");
        stctx.setSumtotalName("--"
                + nc.ui.ml.NCLangRes.getInstance().getStrByID("_Template",
                        "UPP_Template-000349")/*@res "合计"*/+ "--");

        boolean hasAssistSum = true;
        if (sSumAssisUnitKeys == null || sSumAssisUnitKeys.length == 0) {
            hasAssistSum = false;
        }
        if (hasAssistSum
                && (htInvAssisUnitOrder == null || htInvAssisUnitOrder
                        .isEmpty())) {
            hasAssistSum = false;
        }
        if (hasAssistSum
                && (sAssisUnitFieldkey == null || sInventoryfieldName == null)) {
            hasAssistSum = false;
        }
        //辅助量小计
        if (hasAssistSum) {
            stctx.setAssisUnitOrders(htInvAssisUnitOrder);
            stctx.setAssisUnitFieldkey(sAssisUnitFieldkey);
            stctx.setSumAssisUnitKeys(sSumAssisUnitKeys);
            stctx.setInventoryFldName(sInventoryfieldName);
            stctx.setIsSumtotal(false);
        }

        stctx.setisShowTotalOnly(isShowTotalOnly);
        ReportItem[] ris = getBaseClass().getBody_Items();
        for (int i = 0; i < ris.length; i++) {
            if (!ris[i].isShow())
                continue;
            if (ris[i].getDataType() == IBillItem.STRING) {
                stctx.setTotalNameColKeys(ris[i].getKey());
                break;
            }
        }
        setSubtotalContext(stctx);
        subtotal(isNested);
    }

    public void subtotal(String[] strGrps, String[] strSumKeys,String[] sortCols,
            boolean isShowTotalOnly) {
        subtotal(strGrps, strSumKeys,sortCols, isShowTotalOnly, false);
    }
    
    public void subtotal(String[] strGrps, String[] strSumKeys,
            boolean isShowTotalOnly) {
        subtotal(strGrps, strSumKeys, null,isShowTotalOnly, false);
    }
    
    public void subtotal(String[] strGrps, String[] strSumKeys,
            boolean isShowTotalOnly, boolean isNested) {
    	subtotal(strGrps,strSumKeys,null,isShowTotalOnly,isNested);
    }

    /**
     * 此处插入方法说明.
     * 创建日期:(2003-2-28 10:42:05)
     * @param strGrps java.lang.String[]
     * @param strSumKeys java.lang.String[]
     * @param isShowTotalOnly boolean
     */
    public void subtotal(String[] strGrps, String[] strSumKeys,String[] sortCols,
            boolean isShowTotalOnly, boolean isNested) {
        SubtotalContext stctx = new SubtotalContext();
        stctx.setGrpKeys(strGrps);
        stctx.setSubtotalCols(strSumKeys);
        stctx.setSortCols(sortCols);
        stctx.setSubtotalName("--"
                + nc.ui.ml.NCLangRes.getInstance().getStrByID("_Template",
                        "UPP_Template-000348")/*@res "小计"*/+ "--");
        stctx.setSumtotalName("--"
                + nc.ui.ml.NCLangRes.getInstance().getStrByID("_Template",
                        "UPP_Template-000349")/*@res "合计"*/+ "--");
        stctx.setisShowTotalOnly(isShowTotalOnly);
        ReportItem[] ris = getBaseClass().getBody_Items();
        for (int i = 0; i < ris.length; i++) {
            if (!ris[i].isShow())
                continue;
            if (ris[i].getDataType() == IBillItem.STRING) {
                stctx.setTotalNameColKeys(ris[i].getKey());
                break;
            }
        }
        setSubtotalContext(stctx);
        subtotal(isNested);
    }

    /**
     * 
     * @param strGrpkeys java.lang.String[]
     * @param istotalAll boolean
     */
    public void subtotal(boolean isNested) {
        //构造内存结果集元数据
        MemoryResultSetMetaData mrsmd = getReportGeneralUtil().createMeteData();
        //构造内存结果集
        Vector dataVec = getBaseClass().getBillModel().getDataVector();

        initM_mrs(dataVec, mrsmd, isNested);

        LineMode[] lms = getsubtotalLineMode(getSubtotalContext()
                .isLevelCompute());
        //转换内存结果集
        MemoryResultSet mrs = changeMrs_total(getReportInfoCtrl().getMrs(), lms);
        //回转为向量
        dataVec = getReportGeneralUtil().mrs2Vector(mrs);
        dataVec = parseCustomFormat(dataVec);

        //回设表体向量
        getBaseClass().getBillModel().setDataVector(dataVec);

    }
    /**
     * 此处插入方法说明.
     * 创建日期:(2002-11-20 10:50:43)
     * @param stctx nc.vo.pub.rs.SubtotalContext
     */
    public void setSubtotalContext(SubtotalContext stctx) {
        m_stctx = stctx;
        if (stctx.isShowTotalOnly())
            m_stctx.setReserveKeys(stctx.getGrpKeys());
    }
    
    /**
     * 此处插入方法说明.
     * 创建日期:(2002-11-20 10:51:47)
     * @return nc.vo.pub.rs.SubtotalContext
     */
    public SubtotalContext getSubtotalContext() {
        return m_stctx;
    }
    
    /**
     * 此处插入方法说明.
     * 创建日期:(2003-1-15 14:28:38)
     * @return nc.vo.pub.rs.MemoryResultSet
     * @param vecData java.util.Vector
     * @param mrsmd nc.vo.pub.rs.MemoryResultSetMetaData
     */
    private void initM_mrs(Vector vecData, MemoryResultSetMetaData mrsmd,
            boolean isNested) {
        if (isNested) {
        	getReportInfoCtrl().setMrs(getReportGeneralUtil().vector2Mrs(vecData, mrsmd));
        }
        else {
            if (getReportInfoCtrl().getMrs() == null)
            	getReportInfoCtrl().setMrs(getReportGeneralUtil().vector2Mrs(vecData, mrsmd));
        }
        MemoryResultSetMetaData mrsmdOrg = getReportInfoCtrl().getMrs().getMetaData0();
        int index = -1;
        try {
            index = mrsmdOrg.getNameIndex("&&");
            if (index != -1)
            	getReportInfoCtrl().getMrs().setAllClumnvalue("&&", "&&");
        }
        catch (SQLException e) {
            try {
            	getReportInfoCtrl().getMrs().appendClumnByDefaultValue("&&", "&&");
            }
            catch (SQLException eAppend) {
                Logger.error(e.getMessage(),e);
            }
        }

    }

    /**
     * 
     * @return nc.vo.pub.rs.MemoryResultSet
     * @param m_mrs nc.vo.pub.rs.MemoryResultSet
     * @param lms nc.vo.pub.rs.LineMode[]
     */
    public MemoryResultSet changeMrs_total(MemoryResultSet mrs, LineMode[] lms) {
        MemoryResultSet mrsNew = null;
        ResultSetCalute rsc = new ResultSetCalute();
        String strSort = "";
        if (getSubtotalContext().getSortCols() != null)
            strSort = getSubtotalSortOrder(getSubtotalContext().getSortCols());
        else  if (getSubtotalContext().getGrpKeys() != null)
        	strSort = getSubtotalSortOrder(getSubtotalContext().getGrpKeys());
        rsc.setSortOrder(strSort);
        rsc.setAppendInitValue(true);
        rsc.setLineMode(lms);
        try {
            rsc.addResultSet(mrs);
            mrsNew = rsc.execute();
        }
        catch (SQLException e) {
            Logger.error(e.getMessage(),e);
        }

        if (getSubtotalContext().isShowTotalOnly()) {
            java.util.List list = mrsNew.getResultArrayList();
            int index = 0;
            try {
                index = mrsNew.getMetaData0().getNameIndex("&&");
            }
            catch (Exception e) {
                Logger.error(e.getMessage(),e);
            }
            for (int i = list.size() - 1; i >= 0; i--) {
                if (((java.util.List) list.get(i)).get(index).equals("&&"))
                    list.remove(i);
            }
        }
        return mrsNew;

    }
    /**
     * 根据小计运算列构造行模式数组
     * @return java.lang.String
     * @param strSumCols java.lang.String[]
     */
    private LineMode getSubtotalLinemode(String[] strGroupCols, int iOrder,
            String strFormula) {
        //构造标志
        String strFlag = strGroupCols[iOrder - 1]
                + "=["
                + iOrder
                + nc.ui.ml.NCLangRes.getInstance().getStrByID("_Template",
                        "UPP_Template-000353")/*@res "级小计"*/+ "]";
        //构造key值
        String strKey = "";
        for (int i = 0; i < iOrder; i++) {
            strKey += strGroupCols[i];
            if (i < iOrder - 1)
                strKey += ",";
        }
        //构造返回值
        LineMode lm = new LineMode(IResultSetConst.APPEND_LINE, strFlag,
                strKey, strFormula, false);
        return lm;
    }

    /**
     * 此处插入方法说明.
     * @return nc.vo.pub.rs.LineMode[]
     */
    private LineMode[] getsubtotalLineMode(boolean isLevelCompute) {
        SubtotalContext stctx = getSubtotalContext();
        String[] strGrp = stctx.getGrpKeys();
        //如果分组列为空，则不计算小计
        if (strGrp == null || strGrp.length == 0) {
            stctx.setIsSubtotal(false);
            strGrp = new String[0];
        }

        // 构成进行运算的LineMode
        String strFomulaAll = getsumtotalFormula();
        
        boolean isSubtotal = stctx.isSubtotal();
        boolean isSumtotal = stctx.isSumtotal();

        if (stctx.getSubtotalCols() == null
                && stctx.getSumAssisUnitKeys() == null) {
            isSubtotal = false;
            isSumtotal = false;
        }
        Vector v = new Vector();
        v.add(new LineMode(IResultSetConst.LOOP_LINE, "&&=&&", "",
                getLoopFomula(), false));

        if (isSubtotal) {
            if (isLevelCompute) {
                Stack s = new Stack();
                StringBuffer grpBuy = new StringBuffer();
                for (int i = 0, gn = strGrp.length; i < gn; i++) {
                    grpBuy.append(strGrp[i]);
                    s
                            .push(new LineMode(IResultSetConst.APPEND_LINE,
                                    "&&=sum", grpBuy.toString(),
                                    getsubtotalFormula(i + 1), false));
                    grpBuy.append(",");
                }
                while (!s.empty()) {
                    v.add(s.pop());
                }
            }
            else {
                int reserveCount = stctx.getReserveKeys() == null ? 0 : stctx
                        .getReserveKeys().length;
                v.add(new LineMode(IResultSetConst.APPEND_LINE, "&&=sum",
                        getsubtotalGrp(strGrp),
                        getsubtotalFormula(reserveCount), false));
            }
        }
        if (isSumtotal)
            v.add(new LineMode(IResultSetConst.APPEND_LINE, "&&=sumAll", "",
                    strFomulaAll, false));

        LineMode[] lms = null;
        lms = (LineMode[]) v.toArray(new LineMode[0]);

        if (stctx.getHtAssisUnitOrders() != null) {
            for (int i = 0, ln = lms.length; i < ln; i++) {
                lms[i].setHtInvAssisUnitOrder(stctx.getHtAssisUnitOrders());
            }
        }
        return lms;
    }
    
    /**
     * 此处插入方法说明.
     * 创建日期:(2003-5-29 11:10:36)
     */
    private String getLoopFomula() {
        SubtotalContext stctx = getSubtotalContext();
        String[] strsubtotalFormulas = stctx.getSubtotalFormulas();
        if (strsubtotalFormulas == null)
            return "";
        else {
            StringBuffer sb = new StringBuffer();
            if (strsubtotalFormulas != null)
                for (int i = 0; i < strsubtotalFormulas.length; i++) {
                    if (i != 0)
                        sb.append(";");
                    sb.append(strsubtotalFormulas[i]);
                }
            String strFormula = sb.toString();
            return strFormula;
        }
    }
    
    /**
     * 此处插入方法说明.
     * 创建日期:(2002-11-20 9:00:59)
     * @return java.lang.String
     * @param strGrpkeys java.lang.String[]
     */
    private String getsubtotalGrp(String[] strGrpkeys) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < strGrpkeys.length; i++)
            if (i == 0)
                sb.append(strGrpkeys[i]);
            else {
                sb.append(",");
                sb.append(strGrpkeys[i]);
            }
        String strGrp = sb.toString();
        return strGrp;
    }
    
    /**
     * 此处插入方法说明.
     * 创建日期:(2002-11-20 14:43:09)
     * @return java.lang.String
     */
    private String getsumtotalFormula() {
        SubtotalContext stctx = getSubtotalContext();
        String[] strsubtotalCols = stctx.getSubtotalCols();
        String[] strsubtotalFormulas = stctx.getSubtotalFormulas();

        String strAssisUnitFieldkey = stctx.getAssisUnitFieldkey();
        String[] strSumAssisUnitKeys = stctx.getSumAssisUnitKeys();
        String strInventoryFldKey = stctx.getInventoryFldName();

        //判断是否含有辅助小计合计列
        boolean hasAssistSubtotal = strSumAssisUnitKeys != null
                && strSumAssisUnitKeys.length > 0;

        if (strsubtotalCols == null && strsubtotalFormulas == null
                && (!hasAssistSubtotal))
            return null;
        else {
            //处理汇总列
            StringBuffer sb = new StringBuffer();
            if (strsubtotalCols != null)
                for (int i = 0; i < strsubtotalCols.length; i++) {
                    if (i != 0)
                        sb.append(";");
                    sb.append(strsubtotalCols[i]);
                    sb.append("->SUMLINE_");
                    sb.append(strsubtotalCols[i]);
                }
            if (strsubtotalFormulas != null)
                for (int i = 0; i < strsubtotalFormulas.length; i++) {
                    if (sb.length() != 0)
                        sb.append(";");
                    sb.append(strsubtotalFormulas[i]);
                }
            //处理辅助小计合计列
            if (hasAssistSubtotal) {
                StringBuffer sbUnitBuf = new StringBuffer("-");
                sbUnitBuf.append(strAssisUnitFieldkey).append("-");
                sbUnitBuf.append(strInventoryFldKey);
                String sbUnit = sbUnitBuf.toString();

                for (int i = 0, an = strSumAssisUnitKeys.length; i < an; i++) {
                    if (i > 0 || sb.length() > 0) {
                        sb.append(";");
                    }
                    sb.append(strSumAssisUnitKeys[i]);
                    sb.append("->ASSISTSUM_");
                    sb.append(strSumAssisUnitKeys[i]);
                    sb.append(sbUnit);
                }
            }

            String strFormula = sb.toString();
            return strFormula;
        }
    }
    
    /**
     * 此处插入方法说明.
     * 创建日期:(2002-11-20 11:30:39)
     * @param dataVec java.util.Vector
     */
    private Vector parseCustomFormat(Vector dataVec) {
        if (dataVec == null)
            return null;
        SubtotalContext stctx = getSubtotalContext();
        String strTotalNameColKey = stctx.getTotalNameColKeys();
        String strSubtotalName = stctx.getSubtotalName();
        String strSumtotalName = stctx.getSumtotalName();

        if (!stctx.isShowTotalOnly()) {
            //放置总计和小计标志
            if (strTotalNameColKey != null) {
                int index = getBaseClass().getBodyColByKey(strTotalNameColKey);
                for (int i = 0; i < dataVec.size(); i++) {
                    Vector v = (Vector) dataVec.get(i);
//                    String srtkeyname = get
                    if (((String) v.get(v.size() - 1)).equals("sum")) {
                        if (strSubtotalName != null)
                            v.set(index, strSubtotalName);
                    }
                    else if (((String) v.get(v.size() - 1)).equals("sumAll"))
                        if (strSumtotalName != null)
                            v.set(index, strSumtotalName);
                    dataVec.set(i, v);
                }
            }
        }
        else {
            for (int i = 0; i < dataVec.size(); i++) {
                Vector v = (Vector) dataVec.get(i);
                if (((String) v.get(v.size() - 1)).equals("sumAll")) {
                    if (strSumtotalName == null)
                        strSumtotalName = "--"
                                + nc.ui.ml.NCLangRes.getInstance().getStrByID(
                                        "_Template", "UPP_Template-000349")/*@res "合计"*/
                                + "--";
                    int index = 0;
                    if (strTotalNameColKey != null)
                        index = getBaseClass().getBodyColByKey(strTotalNameColKey);
                    else {
                        ReportItem[] ris = getBaseClass().getBody_Items();
                        for (int k = 0; k < ris.length; k++)
                            if (ris[k].getDataType() != IBillItem.DECIMAL
                                    && ris[k].getDataType() != IBillItem.INTEGER) {
                                index = k;
                                break;
                            }
                    }
                    v.set(index, strSumtotalName);
                }
            }
        }
        //去掉辅助列“&&”
        int iLen = ((Vector) dataVec.get(0)).size();
        for (int i = 0; i < dataVec.size(); i++)
            ((Vector) dataVec.get(i)).remove(iLen - 1);
        return dataVec;
    }
    /**
     * 根据小计运算列构造公式
     * 创建日期:(02-6-4 9:30:06)
     * @return java.lang.String
     * @param strSumCols java.lang.String[]
     */
    private String getSubtotalFormula(String[] strSumCols, String strOperator) {
        String strFormula = "";
        if (strSumCols != null) {
            int iLen = strSumCols.length;
            for (int i = 0; i < iLen; i++) {
                strFormula += (strSumCols[i] + "->" + strOperator + strSumCols[i]);
                if (i < iLen - 1)
                    strFormula += ";";
            }
        }
        return strFormula;
    }

    /**
     * 此处插入方法说明.
     * 创建日期:(2002-11-20 9:02:24)
     * @return java.lang.String
     * @param level int
     */
    private String getsubtotalFormula(int level) {
        SubtotalContext stctx = getSubtotalContext();
        String[] strsubtotalCols = stctx.getSubtotalCols();
        String[] strsubtotalFormulas = stctx.getSubtotalFormulas();

        String strAssisUnitFieldkey = stctx.getAssisUnitFieldkey();
        String[] strSumAssisUnitKeys = stctx.getSumAssisUnitKeys();
        String strInventoryFldKey = stctx.getInventoryFldName();

        //判断是否含有辅助小计合计列
        boolean hasAssistSubtotal = strSumAssisUnitKeys != null
                && strSumAssisUnitKeys.length > 0;

        if (strsubtotalCols == null && strsubtotalFormulas == null
                && (!hasAssistSubtotal))
            return null;
        else {
            //处理汇总列
            StringBuffer sb = new StringBuffer();
            if (strsubtotalCols != null)
                for (int i = 0; i < strsubtotalCols.length; i++) {
                    if (i != 0)
                        sb.append(";");
                    sb.append(strsubtotalCols[i]);
                    sb.append("->SUMLINE_");
                    sb.append(strsubtotalCols[i]);
                }
            if (strsubtotalFormulas != null)
                for (int i = 0; i < strsubtotalFormulas.length; i++) {
                    if (sb.length() != 0)
                        sb.append(";");
                    sb.append(strsubtotalFormulas[i]);
                }
            //处理辅助小计合计列
            if (hasAssistSubtotal) {
                StringBuffer sbUnitBuf = new StringBuffer("-");
                sbUnitBuf.append(strAssisUnitFieldkey).append("-");
                sbUnitBuf.append(strInventoryFldKey);
                String sbUnit = sbUnitBuf.toString();

                for (int i = 0, an = strSumAssisUnitKeys.length; i < an; i++) {
                    if (i > 0 || sb.length() > 0) {
                        sb.append(";");
                    }
                    sb.append(strSumAssisUnitKeys[i]);
                    sb.append("->ASSISTSUM_");
                    sb.append(strSumAssisUnitKeys[i]);
                    sb.append(sbUnit);
                }
            }
            //处理保留列
            String[] strReserveKeys = stctx.getReserveKeys();
            if (strReserveKeys != null) {
                int length = strReserveKeys.length;
                length = Math.min(length, level);
                for (int i = 0; i < length; i++) {
                    if (sb.length() != 0)
                        sb.append(";");
                    sb.append(strReserveKeys[i]);
                    sb.append("->UPLINE_");
                    sb.append(strReserveKeys[i]);
                }
            }
            String strFormula = sb.toString();
            return strFormula;
        }
    }
    /**
     * 此处插入方法说明.
     * 创建日期:(2002-11-20 14:26:24)
     * @return java.lang.String
     * @param strSortOrder java.lang.String
     */
    private String getSubtotalSortOrder(String[] strSortOrders) {
        String str = null;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < strSortOrders.length; i++)
            if (i == 0)
                sb.append(strSortOrders[i]);
            else {
                sb.append(",");
                sb.append(strSortOrders[i]);
            }
        str = sb.toString();
        return str;
    }
    
    /**
     * 转换内存结果集(分级小计)
     * 创建日期:(02-5-28 15:25:46)
     * @return nc.vo.pub.rs.MemoryResultSet
     * @param mrs nc.vo.pub.rs.MemoryResultSet
     */
    public MemoryResultSet changeMrs_subtotal(MemoryResultSet mrs,
            String[] strGroupCols, String[] strSumCols) {
    	return changeMrs_subtotal(mrs,strGroupCols,strSumCols);
    }
    

    public MemoryResultSet changeMrs_subtotal(MemoryResultSet mrs,
            String[] strGroupCols, String[] strSumCols,String[] sortCols) {

        MemoryResultSet newMrs = null;
        try {
            MemoryResultSetMetaData mrsmd = mrs.getMetaData0();
            //获得全部字段名
            int iFldCount = mrsmd.getColumnCount();
            String strAllFld = "";
            for (int i = 0; i < iFldCount; i++) {
                strAllFld += mrsmd.getColumnName(i + 1);
                if (i < iFldCount - 1)
                    strAllFld += ",";
            }

            //设置行模式
            int iGroupLen = strGroupCols.length;
            LineMode[] lmA = new LineMode[iGroupLen + 1];
            lmA[0] = new InitLineMode(IResultSetConst.INIT_LINE, "_TEMP=0",
                    strAllFld, "", "");
            String strFormula = getSubtotalFormula(strSumCols,
                    IResultSetConst.STR_SUMLINE_PREFIX);
            for (int i = 1; i < lmA.length; i++)
                lmA[lmA.length - i] = getSubtotalLinemode(strGroupCols, i,
                        strFormula);
            //标志列
            mrs.appendClumnByDefaultValue("_TEMP", "1");
            //构造排序字段
            String strKey = "";
            if( sortCols != null ){
                for (int i = 0; i < sortCols.length; i++) {
                    strKey += sortCols[i];
                    if (i < sortCols.length - 1)
                        strKey += ",";
                }
            }
            else{
            	  for (int i = 0; i < iGroupLen; i++) {
                      strKey += strGroupCols[i];
                      if (i < iGroupLen - 1)
                          strKey += ",";
                  }
            }


            //小计合计处理
            ResultSetCalute rsc = new ResultSetCalute();
            rsc.setSortOrder(strKey);
            rsc.setAppendInitValue(true);
            rsc.setLineMode(lmA);
            rsc.addResultSet(mrs);
            newMrs = rsc.execute();
            //设置小计状态标志
            //m_bSubtotal = true;

            //删除临时列
            mrs.removeColumn("_TEMP");
        }
        catch (Exception e) {
            Logger.error(e.getMessage(),e);
        }
        return newMrs;
    }
    
    /**
     * 添加小计行
     * 创建日期:(01-8-17 11:38:22)
     * @param dataVO nc.vo.pub.CircularlyAccessibleValueObject[]
     */
    public CircularlyAccessibleValueObject[] subtotalColumn(
            CircularlyAccessibleValueObject[] dataVO) {
        if (dataVO.length == 0)
            return dataVO;
        int iFlag = (m_strAggregatedCols == null) ? 0 : 1;
        //排序
        int[] iIndices = null;
        Object objTemp = dataVO[0].getAttributeValue(m_strSubtotalCol);
        if (objTemp == null) {
            Logger.debug("该小计列不存在——" + m_strSubtotalCol);
            return null;
        }
        String strSubTotal = SUB_TOTAL;
        //
        if (objTemp.getClass().getSuperclass().equals(Number.class)) {
            //小计指定列为整型
            int[] iNumbers = new int[dataVO.length - iFlag];
            for (int i = 0; i < iNumbers.length; i++)
                iNumbers[i] = new Integer(dataVO[i].getAttributeValue(
                        m_strSubtotalCol).toString()).intValue();
            iIndices = getReportGeneralUtil().sortBubble(iNumbers);
        }
        else {
            //认为小计指定列为字符串型
            String[] strNumbers = new String[dataVO.length - iFlag];
            for (int i = 0; i < strNumbers.length; i++)
                strNumbers[i] = dataVO[i].getAttributeValue(m_strSubtotalCol)
                        .toString();
            iIndices = getReportGeneralUtil().sortBubble(strNumbers);
        }
        //按序整理数据
        CircularlyAccessibleValueObject[] caVOs = new CircularlyAccessibleValueObject[dataVO.length
                - iFlag];
        try {
            for (int i = 0; i < dataVO.length - iFlag; i++)
                caVOs[i] = dataVO[iIndices[i]];
            //构造实例
            CircularlyAccessibleValueObject appVO = null;
            appVO = caVOs[0].getClass()
                    .newInstance();
            //插入小计行数据
            Vector vecData = new Vector();
            Vector vecTotalFlag = new Vector();
            vecData.addElement(caVOs[0]);
            vecTotalFlag.addElement(new Boolean(false));
            //执行小计
            String[] keys = dataVO[0].getAttributeNames();
            for (int i = 1; i < dataVO.length - iFlag; i++) {
                if (!caVOs[i].getAttributeValue(m_strSubtotalCol).equals(
                        caVOs[i - 1].getAttributeValue(m_strSubtotalCol))) {
                    if (i == 1 && keys != null) {
                        //清空appVO
                        for (int j = 0; j < keys.length; j++)
                            if (!getReportGeneralUtil().isElement(keys[j], m_strSubtotalSumCols)) {
                                appVO.setAttributeValue(keys[j], null);
                                continue;
                            }
                    }
                    appVO.setAttributeValue(m_strSubtotalCol, strSubTotal);
                    //appVO.setDirty(true);
                    vecData.addElement(appVO);
                    vecTotalFlag.addElement(new Boolean(true));
                    //重新实例化
                    appVO = dataVO[0]
                            .getClass().newInstance();
                }
                vecData.addElement(caVOs[i]);
                vecTotalFlag.addElement(new Boolean(false));
                keys = caVOs[i].getAttributeNames();
                if (keys == null)
                    continue;
                for (int j = 0; j < keys.length; j++) {
                    if (!getReportGeneralUtil().isElement(keys[j], m_strSubtotalSumCols)) { //清空
                        appVO.setAttributeValue(keys[j], null);
                        continue;
                    }
                    Object objSum = appVO.getAttributeValue(keys[j]);
                    Object obj = caVOs[i].getAttributeValue(keys[j]);
                    if (obj.getClass().getSuperclass().equals(Number.class)
                            || obj.getClass().equals(UFDouble.class)) {
                        String strSum = (objSum == null) ? "0" : objSum
                                .toString().trim();
                        String str = (obj == null) ? "0" : obj.toString()
                                .trim();
                        //Logger.debug(strSum);
                        if (obj.getClass().equals(UFDouble.class)) {
                            UFDouble dSum = new UFDouble(strSum)
                                    .add(new UFDouble(str));
                            appVO.setAttributeValue(keys[j], dSum);
                        }
                        else {
                            int iSum = new Integer(strSum).intValue()
                                    + new Integer(str).intValue();
                            appVO.setAttributeValue(keys[j], new Integer(iSum));
                        }
                    }
                }
            }
            //最后一组的小计
            appVO.setAttributeValue(m_strSubtotalCol, strSubTotal);
            //appVO.setDirty(true);
            vecData.addElement(appVO);
            vecTotalFlag.addElement(new Boolean(true));
            //添加合计行(如果有)
            if (iFlag == 1) {
                //dataVO[dataVO.length - 1].setAttributeValue(m_strSubtotalCol, TOTAL); //可注释
                vecData.addElement(dataVO[dataVO.length - 1]);
                vecTotalFlag.addElement(new Boolean(true));
            }
            caVOs = new CircularlyAccessibleValueObject[vecData.size()];
            vecData.copyInto(caVOs);
            //设置小计合计行标志
            m_bTotals = new Boolean[vecTotalFlag.size()];
            vecTotalFlag.copyInto(m_bTotals);
        }
        catch (Exception e) {
            Logger.error(e.getMessage(),e);
        }
        return caVOs;
    }
    
    /**
     * 设置小计列(strSubtotalCol:小计指定列的key,strSubtotalSumCols:小计运算列的key数组.即按指定列小计运算列)
     * 创建日期:(01-8-17 9:38:56)
     * @param keys java.lang.String[]
     */
    public void setSubtotal(String strSubtotalCol, String[] strSubtotalSumCols) {
        if (strSubtotalCol != null) {
            if (getBaseClass().getBodyColByKey(strSubtotalCol) == -1) {
                Logger.debug("['小计']查无此列——" + strSubtotalCol);
                return;
            }
            //判断数据类型
            BillItem billItem = getBaseClass().getBodyItem(strSubtotalCol);
            if (billItem != null) {
                int iType = billItem.getDataType();
                if (iType != IBillItem.STRING && iType != IBillItem.DATE) {
                    Logger.debug("指定列不能显示文字'—小计—'!");
                    return;
                }
            }
            m_strSubtotalCol = strSubtotalCol;
            m_strSubtotalSumCols = strSubtotalSumCols;
        }
    }

	/**
	 * @return 返回 m_strSubtotalSumCols。
	 */
	public String[] getSubtotalSumCols()
	{
		return m_strSubtotalSumCols;
	}
	
	
	/**
     * 分级小计
     */
    public void multiSubtotal(String[] strGroupCols0, String[] strSumCols0,String[] sortCols) {
        try {
            //合法性检查
            if (strGroupCols0 == null || strGroupCols0.length == 0
                    || strSumCols0 == null || strSumCols0.length == 0) {
                Logger.debug("分级小计传入参数为空");
                if (getReportInfoCtrl().getMrs() != null) {
                	getReportInfoCtrl().getMrs().beforeFirst();
                    //回转为向量
                    Vector dataVec = getReportGeneralUtil().mrs2Vector(getReportInfoCtrl().getMrs());
                    //回设表体向量
                    getBaseClass().getBillModel().setDataVector(dataVec);
                }
                return;
            }

            if (getReportInfoCtrl().getMrs() == null) {
                //构造内存结果集元数据
                MemoryResultSetMetaData mrsmd = getReportGeneralUtil().createMeteData();
                //获得表体向量
                Vector dataVec = getBaseClass().getBillModel().getDataVector();
                //构造内存结果集
                getReportInfoCtrl().setMrs(getReportGeneralUtil().vector2Mrs(dataVec, mrsmd));
            }

            //转换内存结果集
            MemoryResultSet mrs = changeMrs_subtotal(getReportInfoCtrl().getMrs(), strGroupCols0,
                    strSumCols0,sortCols);
            //回转为向量
            Vector dataVec = getReportGeneralUtil().mrs2Vector(mrs);
            //回设表体向量
            getBaseClass().getBillModel().setDataVector(dataVec);
        }
        catch (Exception e) {
            Logger.error(e.getMessage(),e);
        }
    }
	/**
	 * @return 返回 m_strSubtotalCol。
	 */
	public String getSubtotalCol()
	{
		return m_strSubtotalCol;
	}
	/**
	 * @param subtotalCol 要设置的 m_strSubtotalCol。
	 */
	public void setSubtotalCol(String subtotalCol)
	{
		m_strSubtotalCol = subtotalCol;
	}
}

