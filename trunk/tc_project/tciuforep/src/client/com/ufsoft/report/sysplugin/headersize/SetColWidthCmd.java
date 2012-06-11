package com.ufsoft.report.sysplugin.headersize;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.util.IUFOLogger;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.header.HeaderModel;

/**
 * �����п�
 * 
 * @author caijie
 * @since 3.1
 * ERROR Ӧ�ú�setRowHeightCmdʹ��һ��
 */
public class SetColWidthCmd extends UfoCommand {

    //  ������
    private UfoReport m_RepTool = null;

    /**
     * @param rep
     *            UfoReport - ������
     */
    public SetColWidthCmd(UfoReport rep) {
        super();
        this.m_RepTool = rep;
    }

    /**
     * �õ��������� params[0]: a / p a��ʾҪ����������(����������������) o��ʾֻ���ò����� params[1]: Integer
     * /int[] �������ȫ����,��Integer��Ϊ����,������int[]��ʾҪ���õ��� params[3]: Integer Ҫ���õ��п�
     */
    public void execute(Object[] params) {
        int width = -1;

        boolean changeAllCols = false;//���÷�ʽ

        if ((params == null) || (params.length < 3)) {
            IUFOLogger.getLogger(this).fatal(MultiLang.getString("uiuforep0000829"));//ִ�������п�����ʱ,����Ϊ�ջ��߲�����ȫ
            return;            
        }
        
        try {

            //��ȡ��������
            try {
                char type = ((Character) params[0]).charValue();
                if (type == 'a')
                    changeAllCols = true;
            } catch (Exception e) {
                throw new IllegalArgumentException(MultiLang.getString("uiuforep0000830"));//��������:�Ƿ����в���
            }

            if (changeAllCols) {//���������е��п�
                int row = 0;
                try {
                    row = ((Integer) params[1]).intValue();

                } catch (Exception e) {
                    throw new IllegalArgumentException(MultiLang.getString("uiuforep0000830"));//��������:�Ƿ����в���
                }

                if ((row < 0)
                        || (row > getRepTool().getCellsModel().getMaxCol()))
                    throw new IllegalArgumentException(MultiLang.getString("uiuforep0000830"));//��������:�Ƿ����в���

                //  ��ȡ�趨���п�ֵ
                try {
                    width = ((Integer) params[2]).intValue();
                } catch (Exception e) {
                    throw new IllegalArgumentException(MultiLang.getString("uiuforep0000833"));//��������:�Ƿ����п�ֵ
                }
                if (width < 0)
                    throw new IllegalArgumentException(MultiLang.getString("uiuforep0000833"));//��������:�Ƿ����п�ֵ

                //�����п�

                HeaderModel headModel = getRepTool().getCellsModel().getColumnHeaderModel();
                int realCounts = headModel.getTempCount();
                for (int i = 0; i < realCounts; i++) { //���ý����������е��п�
                    headModel.setSize(i, width);
                }
                //��ָ��ֵ����ΪĬ���п�
                headModel.setPreferredSize(width);
            } else { //�����ƶ�������п�
                int[] cols = null;
                try {
                    cols = (int[]) params[1];

                } catch (Exception e) {
                    throw new IllegalArgumentException(MultiLang.getString("uiuforep0000830"));//��������:�Ƿ����в���
                }

                if ((cols == null) || (cols.length < 1))
                    throw new IllegalArgumentException(MultiLang.getString("uiuforep0000830"));//��������:�Ƿ����в���
                //  ��ȡ�趨���п�ֵ
                try {
                    width = ((Integer) params[2]).intValue();
                } catch (Exception e) {
                    throw new IllegalArgumentException(MultiLang.getString("uiuforep0000833"));//��������:�Ƿ����п�ֵ
                }
                if (width < 0)
                    throw new IllegalArgumentException(MultiLang.getString("uiuforep0000833"));//��������:�Ƿ����п�ֵ
                // �����п�

                HeaderModel headerModel = this.getRepTool().getCellsModel().getColumnHeaderModel();
                for (int i = 0; i < cols.length; i++) {
                    headerModel.setSize(cols[i], width);
                }

            }
        } catch (Exception e) {
            UfoPublic.sendErrorMessage(MultiLang.getString("uiuforep0000839"), getRepTool(), null);//�����п�ʧ��
        }
    }
//    /**
//     * ���п��Сʱ,
//     * caijie  2004-11-9
//     *
//     */
//    private void fillViewByExtendHeaders(){
//        HeaderModel headModel = getRepTool().getUfoTableModel().getCellsModel()
//                .getColumnHeaderModel();
//        if(headModel == null) return;
//        int realCounts = headModel.getRealCount();
//        long totalSize = 0;
//        for (int i = 0; i < realCounts; i++) { //��ȡ��ǰģ���������е����п�
//            totalSize = totalSize + headModel.getSize(i);
//        }
//    }
    /**
     * @return ���� UfoRepTool��
     */
    public UfoReport getRepTool() {
        return m_RepTool;
    }
}