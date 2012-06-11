package com.ufsoft.report.sysplugin.headersize;

import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.header.HeaderModel;

/**
 * �����и�
 * @author caijie 
 * @since 3.1
 */
public class SetRowHeightCmd extends UfoCommand {    
	private UfoReport m_RepTool = null;//  ������

	/**	
	 * @param  rep UfoReport - ������
	 */
	public SetRowHeightCmd(UfoReport rep) {
		super();
		this.m_RepTool = rep;
	}

	/**
     * �õ��������� params[0]: a / p a��ʾҪ����������(����������������) o��ʾֻ���ò����� params[1]: Integer
     * /int[] �������ȫ����,��Integer��Ϊ����,������int[]��ʾҪ���õ��� params[3]: Integer Ҫ���õ��и�
     */
	public void execute(Object[] params) {

        int height = -1;

        boolean changeAllRows = false;//���÷�ʽ

        if ((params == null) || (params.length < 3)) {
//            IUFOLogger.getLogger(this).fatal(MultiLang.getString(this,"uiuforep0000840"));//ִ�������и�����ʱ,����Ϊ�ջ��߲�����ȫ
            return ;
        }
        
        try {

            //��ȡ��������
            try {
                char type = ((Character) params[0]).charValue();
                if (type == 'a')
                    changeAllRows = true;
            } catch (Exception e) {
                throw new IllegalArgumentException(MultiLang.getString("uiuforep0000841"));//��������:�Ƿ����в���
            }

            if (changeAllRows) {//���������е��и�
                int col = 0;
                try {
                    col = ((Integer) params[1]).intValue();//�Ը���Ϊ��׼�������и�

                } catch (Exception e) {
                    throw new IllegalArgumentException(MultiLang.getString("uiuforep0000841"));//��������:�Ƿ����в���
                }

                if ((col < 0)
                        || (col > this.getRepTool().getCellsModel().getMaxCol()))
                    throw new IllegalArgumentException(MultiLang.getString("uiuforep0000841"));//��������:�Ƿ����в���

                //  ��ȡ�趨���и�ֵ
                try {
                    height = ((Integer) params[2]).intValue();
                } catch (Exception e) {
                    throw new IllegalArgumentException(MultiLang.getString("uiuforep0000844"));//��������:�Ƿ����и�ֵ
                }
                if (height < 0)
                    throw new IllegalArgumentException(MultiLang.getString("uiuforep0000844"));//��������:�Ƿ����и�ֵ

                //�����и�
                HeaderModel headModel = getRepTool().getCellsModel().getRowHeaderModel();
                int realCounts = headModel.getTempCount();
                for (int i = 0; i < realCounts; i++) { //���ý����������е��и�
                    headModel.setSize(i, height);
                }
                //��ָ��ֵ����ΪĬ���и�
                headModel.setPreferredSize(height);
            } else { //����ָ��������и�
                int[] rows = null;
                try {
                    rows = (int[]) params[1];

                } catch (Exception e) {
                    throw new IllegalArgumentException(MultiLang.getString("uiuforep0000841"));//��������:�Ƿ����в���
                }

                if ((rows == null) || (rows.length < 1))
                    throw new IllegalArgumentException(MultiLang.getString("uiuforep0000841"));//��������:�Ƿ����в���
                //  ��ȡ�趨���п�ֵ
                try {
                    height = ((Integer) params[2]).intValue();
                } catch (Exception e) {
                    throw new IllegalArgumentException(MultiLang.getString("uiuforep0000848"));//��������:�Ƿ����и�ֵ
                }
                if (height < 0)
                    throw new IllegalArgumentException(MultiLang.getString("uiuforep0000849"));//��������:�Ƿ����и�ֵ
                // �����п�
                HeaderModel headerModel = this.getRepTool().getCellsModel().getRowHeaderModel();
                for (int i = 0; i < rows.length; i++) {
                    headerModel.setSize(rows[i], height);
                }

            }
        } catch (Exception e) {
            UfoPublic.sendErrorMessage(MultiLang.getString("uiuforep0000850"), getRepTool(), null);//�����и�ʧ��
        }
    
	}
	/**
     * @return ���� UfoRepTool��
     */
    public UfoReport getRepTool() {
        return m_RepTool;
    }
}
