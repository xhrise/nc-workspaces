
package nc.ui.eh.trade.z0205502;


import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.ArrayListProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.IEHButton;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIDialog;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.controller.IControllerBase;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.eh.kc.h0250210.PeriodVO;
import nc.vo.eh.stock.z0150502.PerioddiscountHVO;
import nc.vo.eh.stock.z0150502.PerioddiscountVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.BusinessException;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDate;

/**
 * ˵�����ڼ��ۿۼ���
 * ���ͣ�ZA66
 * ���ߣ�wb
 * ʱ�䣺2008-6-10 16:01:45
 */
public class ClientEventHandler extends ManageEventHandler {
    nc.ui.pub.ClientEnvironment ce= nc.ui.pub.ClientEnvironment.getInstance();
    
    public ClientEventHandler(BillManageUI billUI, IControllerBase control) {
        super(billUI, control);
    }
    
    @Override
	protected void onBoElse(int intBtn) throws Exception {
        switch (intBtn)
        {
            case IEHButton.CALCKCYBB:    // ����
                onBoCalcZK();
                break;
            case IEHButton.GenNextData:  // ������������
                onBoNextMonZK();
                break;    
            case IEHButton.ConfirmSC:    // ����
                onBoConfirmZK();
                break;        
            case IEHButton.FirDayOfMonCut:    // �����ۿ��ڳ�����
                onBoFirDayOfMonCut();
                break;            
                
        }
    }
    
    

   /**�������л�ʱ�����ð�ť״̬*/
	protected void onBoCard() throws Exception {
		super.onBoCard();
    	this.getBoEnable();
	}

	/***
     * ����
     * edit by wb 2009-11-19 16:10:38
     */
	private void onBoCalcZK() {
		int year = Integer.parseInt(this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vyear").getValueObject().toString());
		int month = Integer.parseInt(this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vmonth").getValueObject().toString());		
		int ret = getBillUI().showYesNoMessage("��ȷ��Ҫ����"+year+"��"+month+"�¿ͻ��ۿ���?");
        if (ret ==UIDialog.ID_YES){
         try{
        	PerioddiscountVO disVO = new PerioddiscountVO();
        	disVO.setNyear(year);
        	disVO.setNmonth(month);
        	disVO.setPk_corp(_getCorp().getPk_corp());
        	disVO.setPk_cubasdoc(_getOperator());
        	disVO.setDef_1(_getDate().toString());
        	PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
        	pubItf.calcZK(disVO);
        	
        	AggregatedValueObject aggvo = getBillUI().getVOFromUI();
        	PerioddiscountHVO hvo = (PerioddiscountHVO) aggvo.getParentVO();
        	if(hvo!=null){		//���Ƶ��ˣ��Ƶ����ڽ��и��� add by zqy 2010��11��17��10:20:20
        		String coperatorid = _getOperator();
        		String pk_corp = _getCorp().getPk_corp();
        		String dmakedate = _getDate().toString();
        		String pk_perioddiscount_h = hvo.getPk_perioddiscount_h()==null?"":hvo.getPk_perioddiscount_h().toString();
        		
        		String sql = " update eh_perioddiscount_h set coperatorid = '"+coperatorid+"',calcdate = '"+dmakedate+"'" +
        				" where pk_perioddiscount_h = '"+pk_perioddiscount_h+"' and pk_corp = '"+pk_corp+"' ";
        		pubItf.updateSQL(sql);
        	}
			
        	//��������ʾ������
        	String whereSql = " nyear = "+year+" and nmonth= "+month+" and pk_corp = '"+_getCorp().getPk_corp()+"'";
        	nc.ui.trade.bsdelegate.BusinessDelegator business = new nc.ui.trade.bsdelegate.BusinessDelegator();
            SuperVO[] supervo=business.queryByCondition(PerioddiscountHVO.class, whereSql);
            getBufferData().clear();
            if (supervo != null && supervo.length != 0)
            {
                int len=supervo.length;
                for(int i=0;i<len;i++){
                    AggregatedValueObject aVo =
                        (AggregatedValueObject) Class
                            .forName(getUIController().getBillVoName()[0])
                            .newInstance();
                    aVo.setParentVO(supervo[i]);
                    aVo.setChildrenVO(null);
                    getBufferData().addVOToBuffer(aVo);
                }
                getBillUI().setListHeadData(supervo);
                getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
                getBufferData().setCurrentRow(0);       
            }
            else
            {
                getBillUI().setListHeadData(null);
                getBillUI().setBillOperate(IBillOperate.OP_INIT);
                getBufferData().setCurrentRow(-1);
                getBillUI().showWarningMessage("û�д�������!");
            }
         } catch (Exception e) {
				e.printStackTrace();
			}
             
       }
       this.getBoEnable(); 
  } 
	
	@SuppressWarnings("unchecked")
	private void onBoNextMonZK(){
//		//����ձ���
//		int rows= getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
//        if(rows>0){
		//add by houcq 2011-03-14 begin 
		int year = Integer.parseInt(this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vyear").getValueObject().toString());
        int month = Integer.parseInt(this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vmonth").getValueObject().toString());
        UFDate date = _getDate();
        if (date.getMonth()!=month)
        {
        	getBillUI().showErrorMessage("�����·ݲ���ͬһ�ڼ�,��ֹ����!");
	        return;
        }
        int nextyear =0;
		int nextmonth=0;
		if (month==12)
		{   
			nextyear=year+1;
			nextmonth=1;
		}
		else
		{
			nextyear=year;
			nextmonth=month+1;
		}
		  try {
			  PeriodVO[] perVOs = (PeriodVO[]) getBusiDelegator().queryByCondition(PeriodVO.class,"pk_corp = '"+_getCorp().getPk_corp()+"' and nyear = "+nextyear+" and nmonth = "+nextmonth+"" );
			    if(perVOs!=null&&perVOs.length==0){
			    	getBillUI().showErrorMessage("�ڼ䵵����δά��������ά��!");
			        return;
			    }
			    PeriodVO[] perVOsnew = (PeriodVO[]) getBusiDelegator().queryByCondition(PeriodVO.class,"pk_corp = '"+_getCorp().getPk_corp()+"' and nyear = "+year+" and nmonth = "+month+"" );
			    if(perVOsnew!=null&&perVOsnew.length>0){
			    	PeriodVO perVO = perVOsnew[0];
	  				boolean jz_flag =  perVO.getJz_flag()==null?false:perVO.getJz_flag().booleanValue();
	  			    if(!jz_flag){
	  			    	getBillUI().showWarningMessage("������δ���ʣ��޷������������ݣ�����ʺ��ٲ�����");
	  			        return;
	  			    }
			    }
			    else
			    {
			    	getBillUI().showErrorMessage("�ڼ䵵����δά��������ά��!");
			        return;
			    }
		  }catch (Exception e1) {
				  e1.printStackTrace();
			    }
	       //add by houcq 2011-05-13 begin
		  StringBuilder tip = new StringBuilder("");
		  StringBuilder sb = new StringBuilder();
          sb.append("select * from eh_trade_lsdisc ")
          .append("  where pk_corp='")
          .append(_getCorp().getPk_corp())
          .append("' and nvl(dr,0)=0 and vbillstatus<>1")
          .append(" and substr(dmakedate,0,4) = '")
          .append(year)
          .append("' and substr(dmakedate,6,2) = '");
          if (month<10)
          {
        	   sb.append("0"+month); 
          }
          else
          {
        	  sb.append(month);
          }
          sb.append("'");
          IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
          try {
				ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sb.toString(), new ArrayListProcessor());
				if (arr!=null && arr.size()>0)
				{
					tip.append("��ʱ�ۿ�,");
				}
			} catch (BusinessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	       StringBuilder zktzsql = new StringBuilder();
	       zktzsql.append("select * from eh_discount_adjust ")
          .append("  where pk_corp='")
          .append(_getCorp().getPk_corp())
          .append("' and nvl(dr,0)=0 and vbillstatus<>1")
          .append(" and substr(dmakedate,0,4) = '")
          .append(year)
          .append("' and substr(dmakedate,6,2) = '");
          if (month<10)
          {
        	  zktzsql.append("0"+month); 
          }
          else
          {
        	  zktzsql.append(month);
          }
	       zktzsql.append("'");
          try {
				ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(zktzsql.toString(), new ArrayListProcessor());
				if (arr!=null && arr.size()>0)
				{
					tip.append("�ۿ۵���,");
				}
			} catch (BusinessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
      	 //add by houcq end
			/*
			 * �����¼�뵥��״̬�ļ�顣
	      	 * add by houcq 2011-07-06
			 */
		       StringBuilder thsql = new StringBuilder();
		       thsql.append("select * from eh_ladingbill ")
	          .append("  where pk_corp='")
	          .append(_getCorp().getPk_corp())
	          .append("' and nvl(dr,0)=0 and vbillstatus<>1")
	          .append(" and substr(dmakedate,0,4) = '")
	          .append(year)
	          .append("' and substr(dmakedate,6,2) = '");
	          if (month<10)
	          {
	        	  thsql.append("0"+month); 
	          }
	          else
	          {
	        	  thsql.append(month);
	          }
	          thsql.append("'");
	          try {
					ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(thsql.toString(), new ArrayListProcessor());
					if (arr!=null && arr.size()>0)
					{
						tip.append("���֪ͨ,");
					}
				} catch (BusinessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}	
				if (!"".equals(tip.toString()))
				{
					String tmp =tip.toString();
					getBillUI().showWarningMessage("����"+tip.toString().substring(0,tmp.length()-1)+"����δ����ͨ�������������ٲ���!");
					return;
				}
		      	//add by houcq end
				int iRet = getBillUI().showYesNoCancelMessage("        �����������ɺ󣬽��޷��ٿ��ߵ��ڵĵ����������ʱ�ۿۡ��ۿ۵������Ƿ�ȷ������?");
	        	if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
	        		
	                    String pk_period = this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_perioddiscount_h").getValueObject().toString();
	            		PerioddiscountVO disVO = new PerioddiscountVO();
	                	disVO.setNyear(year);
	                	disVO.setNmonth(month);
	                	disVO.setPk_corp(_getCorp().getPk_corp());
	                	disVO.setPk_cubasdoc(_getOperator());
	                	disVO.setDef_1(_getDate().toString());
	                	disVO.setPk_perioddiscount_h(pk_period);
	                	PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
	                	try {
	    					pubItf.getNextMonZK(disVO);
	    				} catch (Exception e) {
	    					e.printStackTrace();
	    				}
	            this.getBoEnable();
	            this.getBillUI().showWarningMessage("�¸�������������ɣ�");
	    		}else
	    		{
	    			return;
	    		}
        	
	}
	
	/**����*/
	@SuppressWarnings("unchecked")
	private void onBoConfirmZK(){

		//����ձ���
		int rows= getBillCardPanelWrapper().getBillCardPanel().getBillTable().getRowCount();
        if(rows>0){
        	//add by houcq 2011-03-14 begin 
        	int year = Integer.parseInt(this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vyear").getValueObject().toString());
    		int month = Integer.parseInt(this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vmonth").getValueObject().toString());
    		UFDate date = _getDate();
            if (date.getMonth()!=month)
            {
            	getBillUI().showErrorMessage("�����·ݲ���ͬһ�ڼ�,��ֹ����!");
    	        return;
            }
    		int nextyear =0;
    		int nextmonth=0;
    		if (month==12)
    		{   
    			nextyear=year+1;
    			nextmonth=1;
    		}
    		else
    		{
    			nextyear=year;
    			nextmonth=month+1;
    		}
    		try {
  			  PeriodVO[] perVOs = (PeriodVO[]) getBusiDelegator().queryByCondition(PeriodVO.class,"pk_corp = '"+_getCorp().getPk_corp()+"' and nyear = "+nextyear+" and nmonth = "+nextmonth+"" );
  			    if(perVOs!=null&&perVOs.length==0){
  			    	getBillUI().showErrorMessage("�ڼ䵵����δά��������ά��!");
  			        return;
  			    }
  			    PeriodVO[] perVOsnew = (PeriodVO[]) getBusiDelegator().queryByCondition(PeriodVO.class,"pk_corp = '"+_getCorp().getPk_corp()+"' and nyear = "+year+" and nmonth = "+month+"" );
  			    if(perVOsnew!=null&&perVOsnew.length>0){
  			    	PeriodVO perVO = perVOsnew[0];
  	  				boolean jz_flag =  perVO.getJz_flag()==null?false:perVO.getJz_flag().booleanValue();
  	  			    if(!jz_flag){
  	  			    	getBillUI().showWarningMessage("������δ���ʣ��޷������������ݣ�����ʺ��ٲ�����");
  	  			        return;
  	  			    }
  			    }
  			    else
  			    {
  			    	getBillUI().showErrorMessage("�ڼ䵵����δά��������ά��!");
  			        return;
  			    }
  			    
  	         }catch (Exception e1) {
  				  e1.printStackTrace();
  			    }
		    //add by houcq 2011-03-14 end
        	//add by houcq begin 2011-02-21 �ж��Ƿ���δ������ʱ�ۿ۵���
        	String newyear = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vyear").getValueObject().toString();
            String newmonth =getBillCardPanelWrapper().getBillCardPanel().getHeadItem("vmonth").getValueObject().toString();
            if (newmonth.length()==1)
            {
            	newmonth=0+newmonth;
            }
            StringBuilder tip = new StringBuilder("");
            StringBuilder sb = new StringBuilder();
            sb.append("select * from eh_trade_lsdisc ")
            .append("  where pk_corp='")
            .append(_getCorp().getPk_corp())
            .append("' and nvl(dr,0)=0 and vbillstatus<>1")
            .append(" and substr(dmakedate,0,4) = '")
            .append(year)
            .append("' and substr(dmakedate,6,2) = '");
            if (month<10)
            {
        	   sb.append("0"+month); 
            }
            else
            {
        	  sb.append(month);
            }
            sb.append("'");
            IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
            try {
				ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sb.toString(), new ArrayListProcessor());
				if (arr!=null && arr.size()>0)
				{
					tip.append("��ʱ�ۿ�,");
				}
			} catch (BusinessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	//add by houcq end
			//add by houcq 2011-05-13
	        StringBuilder zktzsql = new StringBuilder();
	        zktzsql.append("select * from eh_discount_adjust ")
            .append("  where pk_corp='")
            .append(_getCorp().getPk_corp())
            .append("' and nvl(dr,0)=0 and vbillstatus<>1")
            .append(" and substr(dmakedate,0,4) = '")
            .append(year)
            .append("' and substr(dmakedate,6,2) = '");
             if (month<10)
             {
            	 zktzsql.append("0"+month); 
             }
             else
             {
            	zktzsql.append(month);
             }
             zktzsql.append("'");
            try {
				ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(zktzsql.toString(), new ArrayListProcessor());
				if (arr!=null && arr.size()>0)
				{
					tip.append("�ۿ۵���,");
				}
			} catch (BusinessException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	//add by houcq end
			/*
			 * ������������ۿ�¼�뵥��״̬�ļ�顣
	      	 * add by houcq 2011-07-06
			 */
		       StringBuilder thsql = new StringBuilder();
		       thsql.append("select * from eh_ladingbill ")
	          .append("  where pk_corp='")
	          .append(_getCorp().getPk_corp())
	          .append("' and nvl(dr,0)=0 and vbillstatus<>1")
	          .append(" and substr(dmakedate,0,4) = '")
	          .append(year)
	          .append("' and substr(dmakedate,6,2) = '");
	          if (month<10)
	          {
	        	  thsql.append("0"+month); 
	          }
	          else
	          {
	        	  thsql.append(month);
	          }
	          thsql.append("'");
	          try {
					ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(thsql.toString(), new ArrayListProcessor());
					if (arr!=null && arr.size()>0)
					{
						tip.append("���֪ͨ��,");
					}
				} catch (BusinessException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			       StringBuilder seconddiscountsql = new StringBuilder();
			       seconddiscountsql.append("select * from eh_discount_adjust ")
		          .append("  where pk_corp='")
		          .append(_getCorp().getPk_corp())
		          .append("' and nvl(dr,0)=0 and vbillstatus<>1")
		          .append(" and substr(dmakedate,0,4) = '")
		          .append(year)
		          .append("' and substr(dmakedate,6,2) = '");
		          if (month<10)
		          {
		        	  seconddiscountsql.append("0"+month); 
		          }
		          else
		          {
		        	  seconddiscountsql.append(month);
		          }
		          seconddiscountsql.append("'");
		          try {
						ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(seconddiscountsql.toString(), new ArrayListProcessor());
						if (arr!=null && arr.size()>0)
						{
							tip.append("�����ۿ�,");
						}
					} catch (BusinessException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					if (!"".equals(tip.toString()))
					{
						String tmp =tip.toString();
						getBillUI().showWarningMessage("����"+tip.toString().substring(0,tmp.length()-1)+"����δ����ͨ�������������ٲ���!");
						return;
					}
		      	//add by houcq end
			int iRet = getBillUI().showYesNoCancelMessage("        ���ú󣬽��޷��ٿ��ߵ��ڵĵ����������ʱ�ۿۡ��ۿ۵����������ۿ۵ȵ��ݣ��Ƿ�ȷ������?");
		    if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION)
		    {
		    	String pk_period = this.getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_perioddiscount_h").getValueObject().toString();
        		PerioddiscountVO disVO = new PerioddiscountVO();
            	disVO.setNyear(year);
            	disVO.setNmonth(month);
            	disVO.setPk_corp(_getCorp().getPk_corp());
            	disVO.setPk_cubasdoc(_getOperator());
            	disVO.setDef_1(_getDate().toString());
            	disVO.setPk_perioddiscount_h(pk_period);
            	PubItf pubItf = (PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
            	try {
					pubItf.getUseZK(disVO);					
				} catch (Exception e) {
					e.printStackTrace();
				}
				this.getBoEnable();
		        this.getBillUI().showWarningMessage("����������ɣ�");
		    }
        	
        }
        
	}
	
	public void getBoEnable(){
		AggregatedValueObject aggvo = null;
		try {
			super.onBoRefresh();
			aggvo = getBillUI().getVOFromUI();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(aggvo.getParentVO().getAttributeValue("pk_perioddiscount_h")!=null){
			//�����ڳ��ۿ۲�����
			getButtonManager().getButton(IEHButton.FirDayOfMonCut).setEnabled(false);
			
	        String iflag=aggvo.getParentVO().getAttributeValue("scxy_flag").toString();
	        String nflag=aggvo.getParentVO().getAttributeValue("qy_flag").toString();
	        if (nflag.equals("N")){
	        	if(iflag.equals("Y")){
	        		getButtonManager().getButton(IEHButton.GenNextData).setEnabled(false);
	        	}else{
	        		getButtonManager().getButton(IEHButton.GenNextData).setEnabled(true);
	        	}
	        	getButtonManager().getButton(IEHButton.CALCKCYBB).setEnabled(true);
	    		getButtonManager().getButton(IEHButton.ConfirmSC).setEnabled(true);
	    	}else if(nflag.equals("Y")){
	    		getButtonManager().getButton(IEHButton.GenNextData).setEnabled(false);
	    		getButtonManager().getButton(IEHButton.CALCKCYBB).setEnabled(false);
	    		getButtonManager().getButton(IEHButton.ConfirmSC).setEnabled(false);
	    	}
		}else{
			//�����ڳ��ۿۿ���
			getButtonManager().getButton(IEHButton.FirDayOfMonCut).setEnabled(true);
		}
		
	        try {
				getBillUI().updateButtonUI();
			} catch (Exception e) {
				e.printStackTrace();
			}
	
	}

	protected void onBoRefresh() throws Exception {
		
		this.getBoEnable();
		
		super.onBoRefresh();
	}
	
	//���û���ڼ��ۿۼ���
	public void onBoFirDayOfMonCut(){
		
		if(!this.getDiscount()){
			PerioddiscountHVO pdvo = new PerioddiscountHVO();
			pdvo.setNyear(_getDate().getYear());
			pdvo.setNmonth(_getDate().getMonth());
			pdvo.setVyear(_getDate().getYear());
			pdvo.setVmonth(_getDate().getMonth());
			pdvo.setPk_corp(_getCorp().getPrimaryKey());
			pdvo.setCoperatorid(_getOperator());
			pdvo.setCalcdate(_getDate());
			IVOPersistence ip = (IVOPersistence) NCLocator.getInstance().lookup(IVOPersistence.class.getName());
			try {
				ip.insertVO(pdvo);
				this.getBillUI().showWarningMessage("�����ۿ��ڳ�������ɣ�");
			} catch (BusinessException e) {
				e.printStackTrace();
			}
		}else{
			this.getBillUI().showWarningMessage("�������������ѯ�������ݣ����ɱ����ۿۣ�");
			return;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public boolean getDiscount(){
		boolean flag = false;
		StringBuffer str = new StringBuffer()
		.append(" SELECT a.pk_perioddiscount_h FROM eh_perioddiscount_h a WHERE  a.pk_corp = '"+_getCorp().getPrimaryKey()+"' AND NVL(a.dr,0)=0 ");
		IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		try {
			ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(str.toString(), new MapListProcessor());
			if(arr!=null && arr.size()>0){
	        	flag = true;
	        }
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		
		return flag;
	}
	
	
	
}

