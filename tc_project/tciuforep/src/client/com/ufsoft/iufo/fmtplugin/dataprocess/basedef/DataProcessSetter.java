package com.ufsoft.iufo.fmtplugin.dataprocess.basedef;

/**
 @update
   2003-11-19 11:19 liulp
 ������ݴ����ֶε�����������Ϣ����
 @end
 * ���ݴ������������
 * �������ڣ�(2003-4-17 11:04:10)
 * @author���쿡��
 */
import java.awt.Container;
import java.util.Hashtable;
import java.util.Vector;

import nc.vo.iufo.keydef.KeyVO;

import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.report.dialog.UfoDialog;

public class DataProcessSetter{
    private static final long serialVersionUID = 1111L;
    //�����ѯVO
    //private ReportQueryVO m_rq = null;
    //���
    //private UfoMainFrame m_frame = null;
    //�����ֶ����ÿ�ʵ��
    //private GroupFldDefDlg m_gfdDlg = null;
    //�ֲ��ֶ����ÿ�ʵ��
    //private GroupFldDefDlg m_lfdDlg = null;
    //����������ÿ�ʵ��
    //private CodeRuleDlg m_crDlg = null;
    //�����ֶ����ÿ�ʵ��
    //private CrossFldDefDlg m_cfdDlg = null;

    // Suppresses default constructor, ensuring non-instantiability.
    private DataProcessSetter(){
    }

    /**
     * ���ܣ�
       1���������ݴ����塣
       2���������ݴ��������ʽ
        1) ��������Ƕ�ױ������ʽ��
        2) ͬʱ���ú���Ҫ�ڶ�̬��������ʾ�����ݴ����ֶε����λ��
         ��Ϊ���顢���涨�壬��ʾ�ֶ���Ŀ����ʾλ�ö����иı�
        3)����ͷֲ�����ݴ���ʽ���й�ʽ������
     *
     * �������ڣ�(2003-8-14 13:30:48)
     * @author������Ƽ
     * @param nDirection int -��չ����
     * @param nType int - ���ݴ�������
     * @param areaDataProcess com.ufsoft.iuforeport.reporttool.process.basedef.AreaDataProcess - �������ݴ�����
     * @param vecAllDynAreaDPFld java.util.Vector<DataProcessFld> -  ��̬�����ڵ��������ݴ����ֶ�,Ԫ��ΪDataProcessFld
     * @param hashDataTypes java.util.Hashtable - ��̬�������������ݴ����ֶζ�Ӧ��ָ�꣩����������hash(mapName,Integer(measType))
     * @param parent java.awt.Container
     * @return boolean
     */
    public static boolean generateDefAndFormat(DynAreaCell dynArea, int nType, AreaDataProcess areaDataProcess,
                                               Vector<DataProcessFld> vecAllDynAreaDPFld, Hashtable<String, Integer> hashDataTypes, Container parent){
        boolean bDirty = false;
        //������е����ݴ�����
        DataProcessDef dataProcessDef = null;
        if(areaDataProcess != null){
            dataProcessDef = areaDataProcess.getDataProcessDef();
        }
        //dataProcessDef = null;
        //�������ݴ�����
        dataProcessDef = getDataProcessDef(parent, vecAllDynAreaDPFld, null, dataProcessDef, hashDataTypes, nType);

        //����û����������ݴ����壬�������Ӧ�����ݴ��������ʽ����
        bDirty = setDataProcessFormat(areaDataProcess, dataProcessDef, dynArea, nType);

        return bDirty;

    }

    /**
     * ��÷���ͳ�ƺ������������ݴ����塣
     *
     * �������ڣ�(2003-8-15 10:20:14)
     * @author������Ƽ
     * @return com.ufsoft.iuforeport.reporttool.process.basedef.GroupFormulaVO
     * @param parent java.awt.Container
     * @param vecAllDynAreaDPFld java.util.Vector<DataProcessFld> - ��̬������ָ��/�ؼ��ֵ����ݴ����ֶ�ʸ����Ԫ��ΪDataProcessFld
     * @param hashDPFldTypes - ָ��/�ؼ��ֵĶ�Ӧָ���������͵��������ͣ�measPK,new Integer(meastype)��
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
     * ��ý��������ݴ����塣
     *
     * �������ڣ�(2003-8-13 11:55:37)
     * @author������Ƽ
     * @return com.ufsoft.iuforeport.reporttool.process.basedef.CrossTabDef
     * @param parent java.awt.Container
     * @param vecAllDynAreaDPFld java.util.Vector - Ԫ��ΪDtaProcessFld
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
     * ����û����������ݴ����壬�������Ӧ�����ݴ��������ʽ����
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

            //�������ݴ��������ʽ
            //�������ݴ��������ʽ��ͬʱ���ú���Ҫ�ڶ�̬��������ʾ�����ݴ����ֶε����λ��
            //	��Ϊ����򽻲���壬�����ֶ���ʾλ�ý����иı�)
            AreaFormatCreator.createFormat(dynArea, nType, areaDataProcess);

            //���������ȱʡ�Ͻ��У����㶨������ݴ���ʽ
            //ɸѡ������ȱʡ�Ͻ��У����㶨������ݴ���ʽ.added by liulp,2004-03-30 22:12
            if(nType != IDataProcessType.PROCESS_SORT && nType != IDataProcessType.PROCESS_FILTER){
                areaDataProcess.setUserDefined(true);
            }

            //��������ֵΪtrue
            bDirty = true;
        }
        return bDirty;
    }

    /**
     * ������ݴ����塣
     *
     * �������ڣ�(2003-8-12 14:33:26)
     * @author������Ƽ
     * @return com.ufsoft.iuforeport.reporttool.process.basedef.DataProcessDef
     * @param parent java.awt.Container
     * @param vecAllDynAreaDPFld Vector<DataProcessFld>
     * @param vecDPFilterData Vector
     * @param dataProcessDef com.ufsoft.iuforeport.reporttool.process.basedef.DataProcessDef
     * @param hashDataTypes java.util.Hashtable<String, Integer> - ��̬�������������ݴ����ֶζ�Ӧ��ָ�꣩����������hash(mapName,Integer(measType))
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
     * ��÷�������ݴ����塣
     *
     * �������ڣ�(2003-8-12 16:54:45)
     * @author������Ƽ
     * @return com.ufsoft.iuforeport.reporttool.process.basedef.GroupLayingDef
     * @param parent java.awt.Container
     * @param vecAllDynAreaDPFld java.util.Vector - Ԫ��ΪDtaProcessFld
     * @param hashDataTypes java.util.Hashtable - ��̬�������������ݴ����ֶζ�Ӧ��ָ�꣩����������hash(mapName,Integer(measType))
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
        //������е����ݴ�����
        DataProcessDef dataProcessDef = areaDataProcess.getDataProcessDef();
        //debug
//        dataProcessDef = null;
        //end
        //�������ݴ�����
        dataProcessDef = getFilterDef(parent, dataProcessDef, vecDPFilterData, vecRefDPFld, dynAreaKeyVOs);

        //����û����������ݴ����壬�������Ӧ�����ݴ��������ʽ����
        bDirty = setDataProcessFormat(areaDataProcess, dataProcessDef, dynArea, IDataProcessType.PROCESS_FILTER);

        return bDirty;
    }

    /**
     * �õ�ɸѡ�����ݴ�����
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
            //ֻ�ܶ��������ݴ��������ɸѡ��
            //��������ݴ����壬��ȱʡ���ݴ����壬��������ݴ�����,�򽻲�����ݴ�����
//            if(!DataProcessUtil.canBeSortedDef(dataProcessDef)){
//                //������ʾ
//                return null;
//            } else{
            returnDef = (DataProcessDef)dataProcessDef;
//            }
        } else{
            //��û�ж�����������ݴ���,����ȱʡ���ݴ�����
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
     * �����������ݴ����塣
     *
     * �������ڣ�(2003-8-12 16:04:42)
     * @author������Ƽ
     * @return com.ufsoft.iuforeport.reporttool.process.basedef.DefaultDataProcessDef
     * @param parent java.awt.Container
     * @param vecAllDynAreaDPFld java.util.Vector - Ԫ��ΪDtaProcessFld
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
            //ֻ�ܶ��������ݴ������������
            //������ݴ����壬��ȱʡ���ݴ����壬��������ݴ����壬
            if(!DataProcessUtil.canBeSortedDef(dataProcessDef)){
                //������ʾ
                return null;
            } else{
                if(dataProcessDef instanceof DefaultDataProcessDef){
                    returnDef = (DefaultDataProcessDef)dataProcessDef;
                } else{
                    returnDef = new DefaultDataProcessDef( (DataProcessDef)dataProcessDef);
                }
            }
        } else{
            //��û�ж�����������ݴ���,����ȱʡ���ݴ�����
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


