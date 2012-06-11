package com.ufsoft.report.sysplugin.headersize;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.util.IUFOLogger;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.header.HeaderModel;

/**
 * 设置列宽
 * 
 * @author caijie
 * @since 3.1
 * ERROR 应该和setRowHeightCmd使用一个
 */
public class SetColWidthCmd extends UfoCommand {

    //  报表工具
    private UfoReport m_RepTool = null;

    /**
     * @param rep
     *            UfoReport - 报表工具
     */
    public SetColWidthCmd(UfoReport rep) {
        super();
        this.m_RepTool = rep;
    }

    /**
     * 得到三个参数 params[0]: a / p a表示要设置所有列(包括将来产生的列) o表示只设置部分列 params[1]: Integer
     * /int[] 如果设置全部列,以Integer行为代表,否则用int[]表示要设置的列 params[3]: Integer 要设置的列宽
     */
    public void execute(Object[] params) {
        int width = -1;

        boolean changeAllCols = false;//设置方式

        if ((params == null) || (params.length < 3)) {
            IUFOLogger.getLogger(this).fatal(MultiLang.getString("uiuforep0000829"));//执行设置列宽命令时,参数为空或者参数不全
            return;            
        }
        
        try {

            //获取设置类型
            try {
                char type = ((Character) params[0]).charValue();
                if (type == 'a')
                    changeAllCols = true;
            } catch (Exception e) {
                throw new IllegalArgumentException(MultiLang.getString("uiuforep0000830"));//参数错误:非法的列参数
            }

            if (changeAllCols) {//设置所有列的列宽
                int row = 0;
                try {
                    row = ((Integer) params[1]).intValue();

                } catch (Exception e) {
                    throw new IllegalArgumentException(MultiLang.getString("uiuforep0000830"));//参数错误:非法的列参数
                }

                if ((row < 0)
                        || (row > getRepTool().getCellsModel().getMaxCol()))
                    throw new IllegalArgumentException(MultiLang.getString("uiuforep0000830"));//参数错误:非法的列参数

                //  获取设定的列宽值
                try {
                    width = ((Integer) params[2]).intValue();
                } catch (Exception e) {
                    throw new IllegalArgumentException(MultiLang.getString("uiuforep0000833"));//参数错误:非法的列宽值
                }
                if (width < 0)
                    throw new IllegalArgumentException(MultiLang.getString("uiuforep0000833"));//参数错误:非法的列宽值

                //设置列宽

                HeaderModel headModel = getRepTool().getCellsModel().getColumnHeaderModel();
                int realCounts = headModel.getTempCount();
                for (int i = 0; i < realCounts; i++) { //设置界面上已有列的列宽
                    headModel.setSize(i, width);
                }
                //将指定值设置为默认列宽
                headModel.setPreferredSize(width);
            } else { //设置制定区域的列宽
                int[] cols = null;
                try {
                    cols = (int[]) params[1];

                } catch (Exception e) {
                    throw new IllegalArgumentException(MultiLang.getString("uiuforep0000830"));//参数错误:非法的列参数
                }

                if ((cols == null) || (cols.length < 1))
                    throw new IllegalArgumentException(MultiLang.getString("uiuforep0000830"));//参数错误:非法的列参数
                //  获取设定的列宽值
                try {
                    width = ((Integer) params[2]).intValue();
                } catch (Exception e) {
                    throw new IllegalArgumentException(MultiLang.getString("uiuforep0000833"));//参数错误:非法的列宽值
                }
                if (width < 0)
                    throw new IllegalArgumentException(MultiLang.getString("uiuforep0000833"));//参数错误:非法的列宽值
                // 设置列宽

                HeaderModel headerModel = this.getRepTool().getCellsModel().getColumnHeaderModel();
                for (int i = 0; i < cols.length; i++) {
                    headerModel.setSize(cols[i], width);
                }

            }
        } catch (Exception e) {
            UfoPublic.sendErrorMessage(MultiLang.getString("uiuforep0000839"), getRepTool(), null);//设置列宽失败
        }
    }
//    /**
//     * 当列宽变小时,
//     * caijie  2004-11-9
//     *
//     */
//    private void fillViewByExtendHeaders(){
//        HeaderModel headModel = getRepTool().getUfoTableModel().getCellsModel()
//                .getColumnHeaderModel();
//        if(headModel == null) return;
//        int realCounts = headModel.getRealCount();
//        long totalSize = 0;
//        for (int i = 0; i < realCounts; i++) { //获取当前模型中已有列的总列宽
//            totalSize = totalSize + headModel.getSize(i);
//        }
//    }
    /**
     * @return 返回 UfoRepTool。
     */
    public UfoReport getRepTool() {
        return m_RepTool;
    }
}