/*
 * �������� 2006-4-10
 *
 */
package com.ufsoft.iufo.inputplugin.biz.file;

import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.inputplugin.biz.AbsIufoBizCmd;
import com.ufsoft.iuforeport.tableinput.applet.TableInputContextVO;
import com.ufsoft.iuforeport.tableinput.applet.TableInputTransObj;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.command.UfoCommand;

public class ChooseRepCmd extends AbsIufoBizCmd implements IUfoContextKey{

    protected ChooseRepCmd(UfoReport ufoReport) {
        super(ufoReport);
    }
    
    protected boolean isValidParams(Object[] params) {
        //#���ݵĲ������±���PK
        if(params == null || params.length <= 0 || params[0] == null){
            return false;
        }
        return true;
    }

    protected boolean isNeedCheckParams() {
        return true;
    }
    
    protected void executeIUFOBizCmd(UfoReport ufoReport, Object[] params) {
        

        String strNewReportPK = (String)params[0];
        TableInputContextVO tableInputContextVO = (TableInputContextVO)ufoReport.getContextVo();

        //if marked begin, it's for debuging...
        //���ѡ����±������Ѵ򿪵ı�����ͬ���������´򿪱���
        Object tableInputTransObj = tableInputContextVO.getAttribute(TABLE_INPUT_TRANS_OBJ);
		TableInputTransObj tableInputTrans = tableInputTransObj != null &&(tableInputTransObj instanceof TableInputTransObj) ? (TableInputTransObj)tableInputTransObj : null;
		
        if(strNewReportPK!=null && strNewReportPK.equals(tableInputTrans.getRepDataParam().getReportPK())){
            return;
        }
//        marked end
        
        //�����±���PK�����ݲ�����
        tableInputTrans.getRepDataParam().setReportPK(strNewReportPK);
        //����ѡ��ı���
        UfoCommand openRepCmd = new OpenRepCmd(ufoReport);
        openRepCmd.execute(null);  
    }

}
