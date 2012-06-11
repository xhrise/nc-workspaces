package com.ufsoft.iufo.fmtplugin.formula;
import com.ufida.iufo.pub.tools.AppDebug;

import nc.vo.iufo.pub.InputvalueCheck;

import com.ufsoft.iufo.resource.StringResource;
/**
 * ������˹�ʽ�����
 * @author ljhua 2005-8-15
 */
public class RepCheckFormulaChecker {

    
    private static final int MAX_NAME_LENGHT = 64;

    private BaseFmlExecutor m_fmlExecutor=null;
    
    /**
     * 
     */
    public RepCheckFormulaChecker(BaseFmlExecutor fmlExecutor) {
        super();
        m_fmlExecutor = fmlExecutor;

    }

    public static void main(String[] args) {
    }
    
    /**
     * ��鹫ʽ 
     * @param formula ��˹�ʽ
     * @param strBufErr ������Ϣ
     * @return �ϸ�������ݿ��ʽ�Ĺ�ʽ����
     */
    public  String checkCheckFormula(String formula,StringBuffer strBufErr) {
        String strErr=null;
        
        if (formula.trim().equals("")) {
            strErr= StringResource.getStringResource("miufo1000916"); //"��˹�ʽ����Ϊ��"
        } else if (formula.getBytes().length > 3000) {
            strErr= StringResource.getStringResource("miufo1000917"); //"��˹�ʽ���ݲ��ܳ���3000�ֽ�"
        }
        else if (formula.trim().indexOf("Table.check", 1) > 1) {
            //һ����ʽ�в�������������˹�ʽ����
            strErr=StringResource.getStringResource("miufo1000918"); //"checkֻ������һ��"
        }
        else if (m_fmlExecutor == null  )
            strErr= StringResource.getStringResource("miuforep013");//ȱ����������޷���˹�ʽ

        if(strErr!=null){
            strBufErr.append(strErr);
            return null;
        }
        try {
            //��������������﷨���       
            return  m_fmlExecutor.parseRepCheckFormula(formula, true);

        } catch (Exception e) {
AppDebug.debug(e);//@devTools             AppDebug.debug(e);
            strBufErr.append(e.getMessage());

        }
        return null;
    }


    /**
     * �����������Ƿ�Ϸ�,������Ϸ������ش�����Ϣ
     */
    public  String checkName(String strName) {
//        //���Ʋ���Ϊ��
//        if (strName == null || (strName = strName.trim()).length() == 0) {
//            return StringResource.getStringResource("miufochk001");
//        }
        //����Ƿ񳬳�
        if (strName.getBytes().length > MAX_NAME_LENGHT) {
            return StringResource.getStringResource("miufochk002");
        }

        //��������Ƿ�Ϸ�,
        if (InputvalueCheck.isValidName(strName) == false) {
            //�����а����Ƿ��ַ�
            return StringResource.getStringResource("miufochk004");//�������{0}����
        }
        return null;
    }

}
