package com.ufsoft.iufo.fmtplugin.formula;
import com.ufida.iufo.pub.tools.AppDebug;

import nc.vo.iufo.pub.InputvalueCheck;

import com.ufsoft.iufo.resource.StringResource;
/**
 * 表内审核公式检查器
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
     * 检查公式 
     * @param formula 审核公式
     * @param strBufErr 错误信息
     * @return 合格存入数据库格式的公式内容
     */
    public  String checkCheckFormula(String formula,StringBuffer strBufErr) {
        String strErr=null;
        
        if (formula.trim().equals("")) {
            strErr= StringResource.getStringResource("miufo1000916"); //"审核公式不能为空"
        } else if (formula.getBytes().length > 3000) {
            strErr= StringResource.getStringResource("miufo1000917"); //"审核公式内容不能超过3000字节"
        }
        else if (formula.trim().indexOf("Table.check", 1) > 1) {
            //一个公式中不允许定义两条审核公式内容
            strErr=StringResource.getStringResource("miufo1000918"); //"check只允许定义一次"
        }
        else if (m_fmlExecutor == null  )
            strErr= StringResource.getStringResource("miuforep013");//缺少审核器，无法审核公式

        if(strErr!=null){
            strBufErr.append(strErr);
            return null;
        }
        try {
            //调用批命令进行语法检查       
            return  m_fmlExecutor.parseRepCheckFormula(formula, true);

        } catch (Exception e) {
AppDebug.debug(e);//@devTools             AppDebug.debug(e);
            strBufErr.append(e.getMessage());

        }
        return null;
    }


    /**
     * 检查审核名称是否合法,如果不合法，返回错误信息
     */
    public  String checkName(String strName) {
//        //名称不能为空
//        if (strName == null || (strName = strName.trim()).length() == 0) {
//            return StringResource.getStringResource("miufochk001");
//        }
        //检查是否超长
        if (strName.getBytes().length > MAX_NAME_LENGHT) {
            return StringResource.getStringResource("miufochk002");
        }

        //检查名称是否合法,
        if (InputvalueCheck.isValidName(strName) == false) {
            //名称中包含非法字符
            return StringResource.getStringResource("miufochk004");//审核名称{0}有误
        }
        return null;
    }

}
