package com.ufsoft.iufo.fmtplugin.dataprocess.basedef;

/**
 @update
   2003-11-19 11:19 liulp
 添加数据处理字段的数据类型信息参数
 @end
 * 数据处理界面设置类
 * 创建日期：(2003-4-17 11:04:10)
 * @author：朱俊彬
 */
import java.awt.Container;
import java.util.Hashtable;
import java.util.Vector;

import nc.vo.iufo.keydef.KeyVO;

import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.report.dialog.UfoDialog;

public class DataProcessSetter{
    private static final long serialVersionUID = 1111L;
    //报表查询VO
    //private ReportQueryVO m_rq = null;
    //表格
    //private UfoMainFrame m_frame = null;
    //分组字段设置框实例
    //private GroupFldDefDlg m_gfdDlg = null;
    //分层字段设置框实例
    //private GroupFldDefDlg m_lfdDlg = null;
    //编码规则设置框实例
    //private CodeRuleDlg m_crDlg = null;
    //交叉字段设置框实例
    //private CrossFldDefDlg m_cfdDlg = null;

    // Suppresses default constructor, ensuring non-instantiability.
    private DataProcessSetter(){
    }

    /**
     * 功能：
       1，设置数据处理定义。
       2，设置数据处理区域格式
        1) 计算分组的嵌套表区域格式，
        2) 同时设置好需要在动态区域里显示的数据处理字段的相对位置
         因为分组、交叉定义，显示字段数目及显示位置都会有改变
        3)分组和分层的数据处理方式还有公式的设置
     *
     * 创建日期：(2003-8-14 13:30:48)
     * @author：刘良萍
     * @param nDirection int -扩展方向
     * @param nType int - 数据处理类型
     * @param areaDataProcess com.ufsoft.iuforeport.reporttool.process.basedef.AreaDataProcess - 区域数据处理定义
     * @param vecAllDynAreaDPFld java.util.Vector<DataProcessFld> -  动态区域内的所有数据处理字段,元素为DataProcessFld
     * @param hashDataTypes java.util.Hashtable - 动态区域内所有数据处理字段对应（指标）的数据类型hash(mapName,Integer(measType))
     * @param parent java.awt.Container
     * @return boolean
     */
    public static boolean generateDefAndFormat(DynAreaCell dynArea, int nType, AreaDataProcess areaDataProcess,
                                               Vector<DataProcessFld> vecAllDynAreaDPFld, Hashtable<String, Integer> hashDataTypes, Container parent){
        boolean bDirty = false;
        //获得已有的数据处理定义
        DataProcessDef dataProcessDef = null;
        if(areaDataProcess != null){
            dataProcessDef = areaDataProcess.getDataProcessDef();
        }
        //dataProcessDef = null;
        //设置数据处理定义
        dataProcessDef = getDataProcessDef(parent, vecAllDynAreaDPFld, null, dataProcessDef, hashDataTypes, nType);

        //如果用户有设置数据处理定义，则进行相应的数据处理区域格式设置
        bDirty = setDataProcessFormat(areaDataProcess, dataProcessDef, dynArea, nType);

        return bDirty;

    }

    /**
     * 获得分组统计函数的区域数据处理定义。
     *
     * 创建日期：(2003-8-15 10:20:14)
     * @author：刘良萍
     * @return com.ufsoft.iuforeport.reporttool.process.basedef.GroupFormulaVO
     * @param parent java.awt.Container
     * @param vecAllDynAreaDPFld java.util.Vector<DataProcessFld> - 动态区域内指标/关键字的数据处理字段矢量，元素为DataProcessFld
     * @param hashDPFldTypes - 指标/关键字的对应指标数据类型的数据类型（measPK,new Integer(meastype)）
     * @param groupFormulaVO com.ufsoft.iuforeport.reporttool.process.basedef.GroupFormulaVO
     */
    public static GroupFormulaVO generateGroupAggregate(Container parent, Vector<DataProcessFld> vecAllDynAreaDPFld,
        Hashtable<String, Integer> hashDPFldTypes, GroupFormulaVO groupFormulaVO){
        if(parent == null || vecAllDynAreaDPFld == null || hashDPFldTypes == null || groupFormulaVO == null){
            return null;
        }

        GroupAggregateDefDlg dlg = new GroupAggregateDefDlg(parent, vecAllDynAreaDPFld, hashDPFldTypes, groupFormulaVO);
        dlg.showModal();

        if(dlg.getResult() == UfoDialog.ID_OK){
            groupFormulaVO = dlg.getGroupFormulaVO();
        } else{
            groupFormulaVO = null;
        }

        return groupFormulaVO;
    }

    /**
     * 获得交叉表的数据处理定义。
     *
     * 创建日期：(2003-8-13 11:55:37)
     * @author：刘良萍
     * @return com.ufsoft.iuforeport.reporttool.process.basedef.CrossTabDef
     * @param parent java.awt.Container
     * @param vecAllDynAreaDPFld java.util.Vector - 元素为DtaProcessFld
     * @param dataProcessDef com.ufsoft.iuforeport.reporttool.process.basedef.DataProcessDef
     */
    private static CrossTabDef getCrossTabDef(Container parent, Vector vecAllDynAreaDPFld,
                                              DataProcessDef dataProcessDef){
        if(parent == null){
            return null;
        }

        CrossTabDef returnDef = null;

        if(dataProcessDef != null){
            if(! (dataProcessDef instanceof CrossTabDef)){
                if(dataProcessDef instanceof DataProcessDef){
                    returnDef = new CrossTabDef( (DataProcessDef)dataProcessDef);
                } else{
                    returnDef = new CrossTabDef();
                }
            } else{
                returnDef = (CrossTabDef)dataProcessDef;
            }
        } else{
            returnDef = new CrossTabDef();
        }

        CrossTableDefDlg dlg = new CrossTableDefDlg(parent, vecAllDynAreaDPFld, returnDef);
        dlg.showModal();

        if(dlg.getResult() == UfoDialog.ID_OK){
            dataProcessDef = dlg.getDataProcessDef();
        } else{
            returnDef = null;
        }

        return returnDef;
    }

    /**
     * 如果用户有设置数据处理定义，则进行相应的数据处理区域格式设置
     *
     * @param areaDataProcess AreaDataProcess
     * @param dataProcessDef DataProcessDef
     * @param nDerection int
     * @param nType int
     * @return boolean
     */
    private static boolean setDataProcessFormat(AreaDataProcess areaDataProcess, DataProcessDef dataProcessDef,
                                                DynAreaCell dynArea, int nType){
        boolean bDirty = false;
        if(dataProcessDef != null){
            areaDataProcess.setDataProcessDef(dataProcessDef);

            //设置数据处理区域格式
            //计算数据处理区域格式，同时设置好需要在动态区域里显示的数据处理字段的相对位置
            //	因为分组或交叉表定义，各个字段显示位置将会有改变)
            AreaFormatCreator.createFormat(dynArea, nType, areaDataProcess);

            //排序可以在缺省上进行，不算定义过数据处理方式
            //筛选可以在缺省上进行，不算定义过数据处理方式.added by liulp,2004-03-30 22:12
            if(nType != IDataProcessType.PROCESS_SORT && nType != IDataProcessType.PROCESS_FILTER){
                areaDataProcess.setUserDefined(true);
            }

            //设置脏标记值为true
            bDirty = true;
        }
        return bDirty;
    }

    /**
     * 获得数据处理定义。
     *
     * 创建日期：(2003-8-12 14:33:26)
     * @author：刘良萍
     * @return com.ufsoft.iuforeport.reporttool.process.basedef.DataProcessDef
     * @param parent java.awt.Container
     * @param vecAllDynAreaDPFld Vector<DataProcessFld>
     * @param vecDPFilterData Vector
     * @param dataProcessDef com.ufsoft.iuforeport.reporttool.process.basedef.DataProcessDef
     * @param hashDataTypes java.util.Hashtable<String, Integer> - 动态区域内所有数据处理字段对应（指标）的数据类型hash(mapName,Integer(measType))
     * @param iProcessType int
     * @return DataProcessDef
     */
    private static DataProcessDef getDataProcessDef(Container parent, Vector<DataProcessFld> vecAllDynAreaDPFld, Vector vecDPFilterData,
        DataProcessDef dataProcessDef, Hashtable<String, Integer> hashDataTypes, int iProcessType){
        switch(iProcessType){
            case IDataProcessType.PROCESS_FILTER:
                break;
            case IDataProcessType.PROCESS_SORT:
                dataProcessDef = getSortDef(parent, vecAllDynAreaDPFld, dataProcessDef);
                break;
            case IDataProcessType.PROCESS_GROUP:
                dataProcessDef = getGroupLayingDef(parent, vecAllDynAreaDPFld, hashDataTypes, dataProcessDef);
                break;
            case IDataProcessType.PROCESS_GROUP_AGGREGEATE:
                break;
            case IDataProcessType.PROCESS_CROSSTABLE:
                dataProcessDef = getCrossTabDef(parent, vecAllDynAreaDPFld, dataProcessDef);
                break;
            case IDataProcessType.PROCESS_CROSS_CHART:
                dataProcessDef = null; //getChartDef(parent, (ChartDef) dataProcessDef);
                break;
            case IDataProcessType.PROCESS_DELETE:

                //dataProcessDef = new DefaultDataProcessDef();
                //getProcessDeleteDef(parent, (ChartDef) dataProcessDef);
                break;
        }
        return dataProcessDef;

    }

    /**
     * 获得分组的数据处理定义。
     *
     * 创建日期：(2003-8-12 16:54:45)
     * @author：刘良萍
     * @return com.ufsoft.iuforeport.reporttool.process.basedef.GroupLayingDef
     * @param parent java.awt.Container
     * @param vecAllDynAreaDPFld java.util.Vector - 元素为DtaProcessFld
     * @param hashDataTypes java.util.Hashtable - 动态区域内所有数据处理字段对应（指标）的数据类型hash(mapName,Integer(measType))
     * @param dataProcessDef com.ufsoft.iuforeport.reporttool.process.basedef.GroupLayingDef
     */
    private static GroupLayingDef getGroupLayingDef(Container parent, Vector<DataProcessFld> vecAllDynAreaDPFld,
        Hashtable hashDataTypes, DataProcessDef dataProcessDef){
        if(parent == null){
            return null;
        }

        GroupLayingDef returnDef = null;

        if(dataProcessDef != null){
            dataProcessDef = (DataProcessDef)dataProcessDef.clone();
            if(! (dataProcessDef instanceof GroupLayingDef)){
                if(dataProcessDef instanceof DefaultDataProcessDef){
                    returnDef = new GroupLayingDef( (DefaultDataProcessDef)dataProcessDef);
                } else if(dataProcessDef instanceof CrossTabDef || dataProcessDef instanceof DataProcessDef){
                    returnDef = new GroupLayingDef( (DataProcessDef)dataProcessDef);
                } else{
                    returnDef = new GroupLayingDef();
                }
            } else{
                returnDef = (GroupLayingDef)dataProcessDef;
            }

        } else{
            returnDef = new GroupLayingDef();
        }
        GroupLayingDefDlg dlg = new GroupLayingDefDlg(parent, vecAllDynAreaDPFld, hashDataTypes,
            (GroupLayingDef)returnDef);
        dlg.showModal();

        if(dlg.getResult() == UfoDialog.ID_OK){
            returnDef = dlg.getDataProcessDef();
        } else{
            returnDef = null;
        }

        return returnDef;
    }

    /**
     *
     * @param parent Container
     * @param vecDPFilterData Vector<DataProcessFilterData>
     * @param vecRefDPFld Vector<DataProcessFld>
     * @param areaDataProcess AreaDataProcess
     * @param dynAreaKeyVOs nc.vo.iufo.keydef.KeyVO[]
     * @return AreaDataProcess
     */
    public static boolean getFilterDefAndFormat(Container parent, Vector<DataProcessFilterData> vecDPFilterData, Vector<DataProcessFld> vecRefDPFld,
                                                AreaDataProcess areaDataProcess, KeyVO[] dynAreaKeyVOs,DynAreaCell dynArea){
        if(parent == null || areaDataProcess == null || vecDPFilterData == null || vecRefDPFld == null){
            return false;
        }

        boolean bDirty = false;
        //获得已有的数据处理定义
        DataProcessDef dataProcessDef = areaDataProcess.getDataProcessDef();
        //debug
//        dataProcessDef = null;
        //end
        //设置数据处理定义
        dataProcessDef = getFilterDef(parent, dataProcessDef, vecDPFilterData, vecRefDPFld, dynAreaKeyVOs);

        //如果用户有设置数据处理定义，则进行相应的数据处理区域格式设置
        bDirty = setDataProcessFormat(areaDataProcess, dataProcessDef, dynArea, IDataProcessType.PROCESS_FILTER);

        return bDirty;
    }

    /**
     * 得到筛选的数据处理定义
     *
     * @param parent Container
     * @param dataProcessDef DataProcessDef
     * @param vecDPFilterData Vector
     * @param vecRefDPFld Vector
     * @param dynAreaKeyVOs nc.vo.iufo.keydef.KeyVO[]
     * @return DataProcessDef
     */
    private static DataProcessDef getFilterDef(Container parent, DataProcessDef dataProcessDef, Vector vecDPFilterData,
                                               Vector vecRefDPFld, KeyVO[] dynAreaKeyVOs){

        DataProcessDef returnDef = null;

        if(dataProcessDef != null){
            dataProcessDef = (DataProcessDef)dataProcessDef.clone();
            //只能对以下数据处理定义进行筛选：
            //？或空数据处理定义，或缺省数据处理定义，或分组数据处理定义,或交叉表数据处理定义
//            if(!DataProcessUtil.canBeSortedDef(dataProcessDef)){
//                //或有提示
//                return null;
//            } else{
            returnDef = (DataProcessDef)dataProcessDef;
//            }
        } else{
            //还没有定义过区域数据处理,创建缺省数据处理定义
            returnDef = new DataProcessDef();
        }

        SetFilterDefDlg dlg = new SetFilterDefDlg(parent, vecDPFilterData, vecRefDPFld, returnDef, dynAreaKeyVOs);
        dlg.showModal();

        if(dlg.getResult() == UfoDialog.ID_OK){
            returnDef = dlg.getDataProcessDef();
        } else{
            returnDef = null;
        }
        return returnDef;
    }

    /**
     * 获得排序的数据处理定义。
     *
     * 创建日期：(2003-8-12 16:04:42)
     * @author：刘良萍
     * @return com.ufsoft.iuforeport.reporttool.process.basedef.DefaultDataProcessDef
     * @param parent java.awt.Container
     * @param vecAllDynAreaDPFld java.util.Vector - 元素为DtaProcessFld
     * @param dataProcessDef com.ufsoft.iuforeport.reporttool.process.basedef.DefaultDataProcessDef
     */
    private static DefaultDataProcessDef getSortDef(Container parent, Vector vecAllDynAreaDPFld,
        DataProcessDef dataProcessDef){
        if(parent == null){
            return null;
        }

        DefaultDataProcessDef returnDef = null;

        if(dataProcessDef != null){
            dataProcessDef = (DataProcessDef)dataProcessDef.clone();
            //只能对以下数据处理定义进行排序：
            //或空数据处理定义，或缺省数据处理定义，或分组数据处理定义，
            if(!DataProcessUtil.canBeSortedDef(dataProcessDef)){
                //或有提示
                return null;
            } else{
                if(dataProcessDef instanceof DefaultDataProcessDef){
                    returnDef = (DefaultDataProcessDef)dataProcessDef;
                } else{
                    returnDef = new DefaultDataProcessDef( (DataProcessDef)dataProcessDef);
                }
            }
        } else{
            //还没有定义过区域数据处理,创建缺省数据处理定义
            returnDef = new DefaultDataProcessDef();
        }

        SetSortDefDlg dlg = new SetSortDefDlg(parent, vecAllDynAreaDPFld, returnDef);
        dlg.showModal();

        if(dlg.getResult() == UfoDialog.ID_OK){
            returnDef = dlg.getDataProcessDef();
        } else{
            returnDef = null;
        }

        return returnDef;
    }
}


