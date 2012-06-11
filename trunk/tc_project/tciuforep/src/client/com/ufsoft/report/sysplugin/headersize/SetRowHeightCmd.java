package com.ufsoft.report.sysplugin.headersize;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.header.HeaderModel;

/**
 * 设置行高
 * @author caijie 
 * @since 3.1
 */
public class SetRowHeightCmd extends UfoCommand {    
	private UfoReport m_RepTool = null;//  报表工具

	/**	
	 * @param  rep UfoReport - 报表工具
	 */
	public SetRowHeightCmd(UfoReport rep) {
		super();
		this.m_RepTool = rep;
	}

	/**
     * 得到三个参数 params[0]: a / p a表示要设置所有行(包括将来产生的行) o表示只设置部分行 params[1]: Integer
     * /int[] 如果设置全部行,以Integer列为代表,否则用int[]表示要设置的行 params[3]: Integer 要设置的行高
     */
	public void execute(Object[] params) {

        int height = -1;

        boolean changeAllRows = false;//设置方式

        if ((params == null) || (params.length < 3)) {
//            IUFOLogger.getLogger(this).fatal(MultiLang.getString(this,"uiuforep0000840"));//执行设置行高命令时,参数为空或者参数不全
            return ;
        }
        
        try {

            //获取设置类型
            try {
                char type = ((Character) params[0]).charValue();
                if (type == 'a')
                    changeAllRows = true;
            } catch (Exception e) {
                throw new IllegalArgumentException(MultiLang.getString("uiuforep0000841"));//参数错误:非法的行参数
            }

            if (changeAllRows) {//设置所有行的行高
                int col = 0;
                try {
                    col = ((Integer) params[1]).intValue();//以该列为基准来设置行高

                } catch (Exception e) {
                    throw new IllegalArgumentException(MultiLang.getString("uiuforep0000841"));//参数错误:非法的行参数
                }

                if ((col < 0)
                        || (col > this.getRepTool().getCellsModel().getMaxCol()))
                    throw new IllegalArgumentException(MultiLang.getString("uiuforep0000841"));//参数错误:非法的行参数

                //  获取设定的行高值
                try {
                    height = ((Integer) params[2]).intValue();
                } catch (Exception e) {
                    throw new IllegalArgumentException(MultiLang.getString("uiuforep0000844"));//参数错误:非法的行高值
                }
                if (height < 0)
                    throw new IllegalArgumentException(MultiLang.getString("uiuforep0000844"));//参数错误:非法的行高值

                //设置行高
                HeaderModel headModel = getRepTool().getCellsModel().getRowHeaderModel();
                int realCounts = headModel.getTempCount();
                for (int i = 0; i < realCounts; i++) { //设置界面上已有行的行高
                    headModel.setSize(i, height);
                }
                //将指定值设置为默认行高
                headModel.setPreferredSize(height);
            } else { //设置指定区域的行高
                int[] rows = null;
                try {
                    rows = (int[]) params[1];

                } catch (Exception e) {
                    throw new IllegalArgumentException(MultiLang.getString("uiuforep0000841"));//参数错误:非法的行参数
                }

                if ((rows == null) || (rows.length < 1))
                    throw new IllegalArgumentException(MultiLang.getString("uiuforep0000841"));//参数错误:非法的行参数
                //  获取设定的列宽值
                try {
                    height = ((Integer) params[2]).intValue();
                } catch (Exception e) {
                    throw new IllegalArgumentException(MultiLang.getString("uiuforep0000848"));//参数错误:非法的行高值
                }
                if (height < 0)
                    throw new IllegalArgumentException(MultiLang.getString("uiuforep0000849"));//参数错误:非法的行高值
                // 设置列宽
                HeaderModel headerModel = this.getRepTool().getCellsModel().getRowHeaderModel();
                for (int i = 0; i < rows.length; i++) {
                    headerModel.setSize(rows[i], height);
                }

            }
        } catch (Exception e) {
            UfoPublic.sendErrorMessage(MultiLang.getString("uiuforep0000850"), getRepTool(), null);//设置行高失败
        }
    
	}
	/**
     * @return 返回 UfoRepTool。
     */
    public UfoReport getRepTool() {
        return m_RepTool;
    }
}
