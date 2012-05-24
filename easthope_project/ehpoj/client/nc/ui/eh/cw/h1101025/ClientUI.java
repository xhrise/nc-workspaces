package nc.ui.eh.cw.h1101025;

import java.text.DecimalFormat;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillModel;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

/**
 * ����˵�����˷�֧����
 * @author wb
 * 2009-4-15 14:21:34
 */

@SuppressWarnings("serial")
public class ClientUI extends AbstractClientUI {

	@Override
	protected AbstractManageController createController() {
		return new ClientCtrl();
	}
    
	@Override
	public ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this, this.getUIControl());
	}
	

	@Override
	public void setDefaultData() throws Exception {
		getBillCardPanel().setHeadItem("fkrq", _getDate());     //��������
		super.setDefaultData_withNObillno();
	}
    
	
  
	@Override
	public void afterEdit(BillEditEvent e) {
        String strkey = e.getKey();
        /*
         * ѡ��ֿ�ʱ����������add by houcq 2011-07-01
         */
        if(strkey.equals("storename"))
        {
        	String pk_store =getBillCardPanel().getBodyValueAt(e.getRow(), "pk_in")==null?"":
        		getBillCardPanel().getBodyValueAt(e.getRow(), "pk_in").toString();
        	String pk_invbasdoc =getBillCardPanel().getBodyValueAt(e.getRow(), "pk_invbasdoc")==null?"":
        		getBillCardPanel().getBodyValueAt(e.getRow(), "pk_invbasdoc").toString();
        	UFDouble amount = getAmount(_getCorp().getPk_corp(),pk_store,pk_invbasdoc,_getDate());
        	getBillCardPanel().setBodyValueAt(amount, e.getRow(),"amount");
        }
        if(strkey.equals("bcfkje")||strkey.equals("rkmount")){				//����
            int row = getBillCardPanel().getBillTable().getRowCount();		//����������
            UFDouble sumamount = new UFDouble();							//�ϼƱ��幺������
            UFDouble sumbcfkje = new UFDouble();							//�ϼƱ���������
            for(int i=0;i<row;i++){
            	UFDouble amount = new UFDouble(getBillCardPanel().getBodyValueAt(i, "rkmount")==null?"0":
                    getBillCardPanel().getBodyValueAt(i, "rkmount").toString());
                UFDouble bcfkje = new UFDouble(getBillCardPanel().getBodyValueAt(i, "bcfkje")==null?"0":
                    getBillCardPanel().getBodyValueAt(i, "bcfkje").toString());
                sumamount = sumamount.add(amount);
                sumbcfkje = sumbcfkje.add(bcfkje);
            }
            getBillCardPanel().setHeadItem("fkje", sumbcfkje);    
            getBillCardPanel().setHeadItem("wfje", sumamount);
        }
        if(strkey.equals("fkje")||strkey.equals("rkmount")){					//��ͷ	���븶����ʱ���ݱ�������������ط�̯�˽��
        	UFDouble sumfkje = new UFDouble(getBillCardPanel().getHeadItem("fkje").getValueObject()==null?"0":
                getBillCardPanel().getHeadItem("fkje").getValueObject().toString());     		//�ϼ�������
        	UFDouble sumamount = new UFDouble(getBillCardPanel().getHeadItem("wfje").getValueObject()==null?"0":
                getBillCardPanel().getHeadItem("wfje").getValueObject().toString()); 			//�ϼ��������
            int row = getBillCardPanel().getBillTable().getRowCount();
            DecimalFormat df = new DecimalFormat("#.00"); 
            UFDouble ze = new UFDouble(0); // �����̯�Ľ��ϼ�
            for(int i=0;i<row;i++){
            	getBillCardPanel().getBillModel().setRowState(i, BillModel.MODIFICATION);
            	UFDouble amount = new UFDouble(getBillCardPanel().getBodyValueAt(i, "rkmount")==null?"0":
                    getBillCardPanel().getBodyValueAt(i, "rkmount").toString());;
         		UFDouble bl = amount.div(sumamount);  				// ��ռ����
        		UFDouble bcfkje = new UFDouble(df.format(sumfkje.multiply(bl).toDouble()));
        		getBillCardPanel().setBodyValueAt(bcfkje, i,"bcfkje");
        		ze = ze.add(bcfkje);
        	}
            //����ķ�̯���ϼ�������̯�ܶ�Ƚ�,���ڱ�������������,�轫������ۿ۷�̯�����һ�б�����
        	UFDouble chae = sumfkje.sub(ze);
    		UFDouble lastbcfkje = new UFDouble(getBillCardPanel().getBodyValueAt(row-1,"bcfkje")==null?"0":
                					getBillCardPanel().getBodyValueAt(row-1,"bcfkje").toString());
    		getBillCardPanel().setBodyValueAt(lastbcfkje.add(chae), row-1, "bcfkje");
        }

	    super.afterEdit(e);
	}
	/*
	 * �������ϲֿ�ȡ�ÿ�����ݣ��ֿ���ڳ�����+��¼�����·�1�ŵ���¼���ڳ�Ʒ���Ͳɹ������������֮�͡�
	 */
	private UFDouble getAmount(String pk_corp,String pk_store,String pk_invbasdoc,UFDate calcdate)
	{
		UFDouble amount = new UFDouble(0);
		int nmonth = calcdate.getMonth();
		int nyear = calcdate.getYear();
		int pyear = nyear;
		int pmonth =0;
        if(nmonth==1){
            pmonth=12;
            pyear = nyear - 1;
        }else
            pmonth = nmonth - 1;
        String beginDate = calcdate.toString().substring(0,8)+"01";
		StringBuilder sb = new StringBuilder()
		//.append(" select sum(t.qcamount)+sum(t.cgrkamount)+sum(t.cprkamount) amount from (")
		.append(" select sum(t.qcamount)+sum(t.cgrkamount)+sum(t.cprkamount)+sum(t.dramount) amount from (")
		//�ڳ����ȡ���µ���ĩ����
		//.append(" select b.pk_store,b.pk_invbasdoc pk_invbasdoc,sum(nvl(qmsl,0)) qcamount,0 cgrkamount, 0 cprkamount")
		.append(" select b.pk_store,b.pk_invbasdoc pk_invbasdoc,sum(nvl(qmsl,0)) qcamount,0 cgrkamount, 0 cprkamount,0 dramount")
		.append(" from eh_calc_kcybb a,eh_calc_kcybb_b b")	
		.append(" where a.pk_kcybb = b.pk_kcybb and nvl(a.dr, 0) = 0 and nvl(b.dr, 0) = 0")
		.append(" and a.pk_corp = '"+pk_corp+"'")		
		.append(" and a.nyear="+pyear)
		.append(" and a.nmonth="+pmonth)
		.append(" and b.pk_invbasdoc='"+pk_invbasdoc+"'")
		.append(" and b.pk_store='"+pk_store+"'")
		.append(" group by b.pk_store,b.pk_invbasdoc")
		//--��Ӫ�ɹ����
		.append(" union all")
		//.append(" select a.pk_stock pk_store,b.pk_invbasdoc pk_invbasdoc,0 qcamount,sum(nvl(b.inamount,0)) cgrkamount,0 cprkamount")
		.append(" select a.pk_stock pk_store,b.pk_invbasdoc pk_invbasdoc,0 qcamount,sum(nvl(b.inamount,0)) cgrkamount,0 cprkamount,0 dramount")
		.append(" from eh_stock_in a, eh_stock_in_b b")
		.append(" where a.pk_in = b.pk_in")
		.append(" and a.dmakedate between '"+beginDate+"' and '"+calcdate+"'")
		.append(" and nvl(a.dr, 0) = 0 and nvl(b.dr, 0) = 0 and a.vbillstatus = 1")
		.append(" and a.pk_corp = '"+pk_corp+"'")
		.append(" and b.pk_invbasdoc='"+pk_invbasdoc+"'")
		.append(" and a.pk_stock='"+pk_store+"'")
		.append(" group by a.pk_stock,b.pk_invbasdoc")		
		.append(" union all")	
		//--��Ʒ���
		//.append(" select a.pk_store,b.pk_invbasdoc pk_invbasdoc,0 qcamount,0 cgrkamount,sum(nvl(b.rkmount,0)) cprkamount")
		.append(" select a.pk_store,b.pk_invbasdoc pk_invbasdoc,0 qcamount,0 cgrkamount,sum(nvl(b.rkmount,0)) cprkamount,0 dramount")
		.append(" from eh_sc_cprkd a, eh_sc_cprkd_b b")        
		.append(" where a.pk_rkd = b.pk_rkd")
		.append(" and a.dmakedate between '"+beginDate+"' and '"+calcdate+"'")
		.append(" and nvl(a.dr, 0) = 0 and nvl(b.dr, 0) = 0 and a.vbillstatus = 1")
		.append(" and a.pk_corp = '"+pk_corp+"'")
		.append(" and b.pk_invbasdoc='"+pk_invbasdoc+"'")
		.append(" and a.pk_store='"+pk_store+"'")
		.append(" group by a.pk_store,b.pk_invbasdoc")
		//add by houcq 2011-08-16 begin
		//--�������
		.append(" union all")			
		.append("  select a.pk_dr_store pk_store,b.pk_dr_inv pk_invbasdoc,0 qcamount,0 cgrkamount,0 cprkamount,sum(nvl(b.drrmount,0)) dramount")
		.append(" from eh_sc_dbd a, eh_sc_dbd_b b")        
		.append(" where a.pk_dbd = b.pk_dbd")
		.append(" and a.dmakedate between '"+beginDate+"' and '"+calcdate+"'")
		.append(" and nvl(a.dr, 0) = 0 and nvl(b.dr, 0) = 0 and a.vbillstatus = 1")
		.append(" and a.pk_corp = '"+pk_corp+"'")
		.append(" and b.pk_dr_inv='"+pk_invbasdoc+"'")
		.append(" and a.pk_dr_store='"+pk_store+"'")
		.append(" group by a.pk_dr_store,b.pk_dr_inv")
		//add by houcq 2011-08-16 end
		.append(" ) t group by t.pk_store,t.pk_invbasdoc");
		IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
	
		try {
			Object o = iUAPQueryBS.executeQuery(sb.toString(), new ColumnProcessor());
			if (o!=null)
			{
				amount = new UFDouble(o.toString());
			}
			
		} catch (BusinessException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}		
		return amount;
	}

}
