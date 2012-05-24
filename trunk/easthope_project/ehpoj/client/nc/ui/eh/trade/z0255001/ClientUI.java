
package nc.ui.eh.trade.z0255001;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.eh.trade.z0255001.ClientUICheckRuleGetter;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.businessref.PCRefModel;
import nc.ui.eh.businessref.YsContractRefModel;
import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.IBillType;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.refpub.InvdocByCusdocRefModel;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.eh.trade.z0255001.IcoutBVO;
import nc.vo.eh.trade.z0255001.IcoutVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

/**
 * ˵�������ⵥ 
 * �������ͣ�ZA11
 * @author ���� 
 * ʱ�䣺2008-4-8 19:43:27
 */
@SuppressWarnings("serial")
public class ClientUI extends AbstractClientUI {
	UIRefPane ref=null;
	UIRefPane refpc=null;
	
	public ClientUI() {
	    super();
	    ref=(UIRefPane) getBillCardPanel().getHeadItem("pk_yscontracts").getComponent();
		ref.setMultiSelectedEnabled(true);
		ref.setProcessFocusLost(false);
		ref.setButtonFireEvent(true);
		ref.setTreeGridNodeMultiSelected(true);
		
		refpc=(UIRefPane) getBillCardPanel().getBodyItem("pc").getComponent();
		refpc.setMultiSelectedEnabled(true);
		refpc.setProcessFocusLost(false);
		refpc.setButtonFireEvent(true);
		refpc.setTreeGridNodeMultiSelected(true);
		
	 }
   
	@Override
	protected AbstractManageController createController() {
		return new ClientCtrl();
	}

	@Override
	public ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this,this.getUIControl());
	}
	
	@Override
	public void setDefaultData() throws Exception {
		super.setDefaultData();
        getBillCardPanel().setHeadItem("outdate", _getDate());
        IcoutBVO[] bvos = ZA30TOZA11DLG.bvos;
        if(bvos!=null&&bvos.length>0){
        	for(int i=0;i<bvos.length;i++){
        		getBillCardPanel().getBillModel().addLine();
        		getBillCardPanel().getBillModel().setBodyRowVO(bvos[i], i);
        		String[] formual=getBillCardPanel().getBodyItem("vcode").getEditFormulas();//��ȡ�༭��ʽ
                getBillCardPanel().execBodyFormulas(i,formual);
        	}
        	ZA30TOZA11DLG.bvos = null;
        }

	}
	 @Override
	    protected void initPrivateButton() {
		 	nc.vo.trade.button.ButtonVO btn1 = ButtonFactory.createButtonVO(IEHButton.LOCKBILL,"�ر�","�ر�");
	        btn1.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
	        addPrivateButton(btn1);
	    	super.initPrivateButton();
	    }

	@Override
	public Object getUserObject() {
		return new ClientUICheckRuleGetter();
	}

	
	@SuppressWarnings("unchecked")
	@Override
	public void afterEdit(BillEditEvent e) {
		String strKey=e.getKey();
		int row=e.getRow();
		String vsourcebilltype = null;
		try {
			//<�޸�>��ӿ��жϡ�ʱ�䣺2009-09-07
			vsourcebilltype = ((IcoutVO)getBillCardWrapper().getBillVOFromUI().getParentVO()).getVsourcebilltype() == null?"":((IcoutVO)getBillCardWrapper().getBillVOFromUI().getParentVO()).getVsourcebilltype();	//��Դ��������
		} catch (Exception e1) {
			e1.printStackTrace();
		}		
        if(e.getPos()==HEAD){
            String[] formual=getBillCardPanel().getHeadItem(strKey).getEditFormulas();//��ȡ�༭��ʽ
            getBillCardPanel().execHeadFormulas(formual);
        }else if (e.getPos()==BODY){
            String[] formual=getBillCardPanel().getBodyItem(strKey).getEditFormulas();//��ȡ�༭��ʽ
            getBillCardPanel().execBodyFormulas(e.getRow(),formual);
        }else{
            getBillCardPanel().execTailEditFormulas();
        }
        if(strKey.equals("outamount")&&vsourcebilltype.equals(IBillType.eh_z0255001)){           // ���ݳ��������һ�ζ����ۿ�
        	int rows=e.getRow();
        	UFDouble fist=new UFDouble(getBillCardPanel().getBodyValueAt(rows,"vfirst")==null?"0":
                getBillCardPanel().getBodyValueAt(rows,"vfirst").toString());
        	UFDouble sec=new UFDouble(getBillCardPanel().getBodyValueAt(rows,"vsecond")==null?"0":
                getBillCardPanel().getBodyValueAt(rows,"vsecond").toString());
        	UFDouble outamout =new UFDouble(getBillCardPanel().getBodyValueAt(rows,"outamount")==null?"0":
                getBillCardPanel().getBodyValueAt(rows,"outamount").toString());
        	UFDouble ladingamount =new UFDouble(getBillCardPanel().getBodyValueAt(rows,"ladingamount")==null?"0":
                getBillCardPanel().getBodyValueAt(rows,"ladingamount").toString());
        	UFDouble rate = outamout.div(ladingamount);
        	fist = fist.multiply(rate);
        	sec = sec.multiply(rate);
        	getBillCardPanel().setBodyValueAt(fist, rows, "firstdiscount");
        	getBillCardPanel().setBodyValueAt(sec, rows, "seconddiscount");
        	
        }
        /**����ʱ���ݿͻ����ϼ���һ���ۿ� add by wb at 2008-10-23 9:44:13*/
        if(strKey.equals("outamount")&&!vsourcebilltype.equals(IBillType.eh_z0255001)){
        	String pk_cubasdoc=getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject()==null?"":
                getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject().toString(); 	//�ͻ�
        	String pk_invbasdoc=getBillCardPanel().getBodyValueAt(row,"pk_invbasdoc")==null?"":
                getBillCardPanel().getBodyValueAt(row,"pk_invbasdoc").toString();           //����
        	ArrayList arr= new nc.ui.eh.trade.z0206005.ClientUI().getDiscount(pk_cubasdoc,pk_invbasdoc);
        	HashMap hm=(HashMap)arr.get(0);                                                                       	//һ���ۿ�
        	UFDouble outamount=new UFDouble(getBillCardPanel().getBodyValueAt(row,"outamount")==null?"0":
                 getBillCardPanel().getBodyValueAt(row,"outamount").toString()); 								  	//��������
        	UFDouble firstdiscount=new UFDouble(hm.get(pk_invbasdoc+pk_cubasdoc)==null?"0":hm.get(pk_invbasdoc+pk_cubasdoc).toString());//һ���ۿ�
        	getBillCardPanel().setBodyValueAt(outamount.multiply(firstdiscount), row, "firstdiscount");  
        }
        /************************ end ***********************************/
        
        if(strKey.equals("pk_cubasdoc")){
        	String pk_cubasdoc=getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject()==null?"":
                getBillCardPanel().getHeadItem("pk_cubasdoc").getValueObject().toString();
        	YsContractRefModel.pk_cubasdoc = pk_cubasdoc;  		//�����̴��������ͬ������   add by wb at 2008-10-14 10:18:52
        	InvdocByCusdocRefModel.pk_cubasdoc = pk_cubasdoc;  //�����̴���������
        }
        if("pk_yscontracts".equalsIgnoreCase(strKey)){				//ѡ�������ͬ��������� add by wb at 2008-10-14 10:21:00
        	String[] billnos = ref.getRefNames();
        	String billno = PubTools.combinArrayToString3(billnos);
          	getBillCardPanel().setHeadItem("ysbillnos", billno);
          }
        //�����޸Ĳ���
        if("vcode".equals(strKey)){
    		String pk_invbasdoc=getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc")==null?"":
        		getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc").toString();
    		PCRefModel.pk_invbasdoc=pk_invbasdoc; 
    		getBillCardPanel().setBodyValueAt("", row, "pc");
    		getBillCardPanel().setBodyValueAt("", row, "pk_cprk_b");
    		//add by houcq 2011-07-02 begin
    		String pk_store=getBillCardPanel().getHeadItem("pk_stock").getValueObject()==null?"":
        		getBillCardPanel().getHeadItem("pk_stock").getValueObject().toString();
    		if ("".equals(pk_store))
    		{
    			getBillCardPanel().getBillModel().delLine(new int[]{e.getRow()});
    			showWarningMessage("����ѡ��ֿ⣬��ѡ������!");
    			return;
    		}
    		HashMap hm = new PubTools().getInvKcAmountByStore(_getCorp().getPk_corp(),_getDate(),pk_store);
    		UFDouble[] unckrk = getUnckUnrk(pk_invbasdoc,_getDate());
    		UFDouble amountkc = new UFDouble(hm.get(pk_invbasdoc)==null?"0":hm.get(pk_invbasdoc).toString());
			if(unckrk!=null&&unckrk.length==2){
				//modify by  by houcq 2011-07-11 ��������=���-����δ����+���´�δ���+�����-�ѳ�������
				UFDouble thsl=new UFDouble(getBillCardPanel().getBodyValueAt(row, "ladingamount")==null?"0":
            		getBillCardPanel().getBodyValueAt(row, "ladingamount").toString());
    			UFDouble ycksl=new UFDouble(getBillCardPanel().getBodyValueAt(row, "def_6")==null?"0":
            		getBillCardPanel().getBodyValueAt(row, "def_6").toString());
				UFDouble ytwcamount = unckrk[0];		//�ѿ������δ������
				UFDouble ypgwrkamount = unckrk[1];		//�������ɹ�δ�������
				//UFDouble maxthamount = amountkc.sub(ytwcamount).add(ypgwrkamount);	
				UFDouble maxthamount = amountkc.sub(ytwcamount).add(ypgwrkamount).add(thsl).sub(ycksl);	
				getBillCardPanel().setBodyValueAt(maxthamount, row, "def_8");
			}
    		//end
        }
      //����Դ���������֪ͨ��ʱ��ѡ��ֿ�ʱ������������add by houcq 2011-07-02 begin
        if("pk_stock".equals(strKey)){
        	String pk_store=getBillCardPanel().getHeadItem("pk_stock").getValueObject()==null?"":
        		getBillCardPanel().getHeadItem("pk_stock").getValueObject().toString();
        	HashMap hm = new PubTools().getInvKcAmountByStore(_getCorp().getPk_corp(),_getDate(),pk_store);
        	int rows =getBillCardPanel().getBillModel().getRowCount();
    		for (int i=0;i<rows;i++)
    		{
    			String pk_invbasdoc=getBillCardPanel().getBodyValueAt(i, "pk_invbasdoc")==null?"":
            		getBillCardPanel().getBodyValueAt(i, "pk_invbasdoc").toString();
    			//modify by  by houcq 2011-07-11 ��������=���-����δ����+���´�δ���+�����-�ѳ�������
    			UFDouble thsl=new UFDouble(getBillCardPanel().getBodyValueAt(i, "ladingamount")==null?"0":
            		getBillCardPanel().getBodyValueAt(i, "ladingamount").toString());
    			UFDouble ycksl=new UFDouble(getBillCardPanel().getBodyValueAt(i, "def_6")==null?"0":
            		getBillCardPanel().getBodyValueAt(i, "def_6").toString());
    			UFDouble[] unckrk = getUnckUnrk(pk_invbasdoc,_getDate());
        		UFDouble amountkc = new UFDouble(hm.get(pk_invbasdoc)==null?"0":hm.get(pk_invbasdoc).toString());
    			if(unckrk!=null&&unckrk.length==2){
    				UFDouble ytwcamount = unckrk[0];
    				UFDouble ypgwrkamount = unckrk[1];
    				UFDouble maxthamount = amountkc.sub(ytwcamount).add(ypgwrkamount).add(thsl).sub(ycksl);
    				getBillCardPanel().setBodyValueAt(maxthamount, i, "def_8");
    			}
    		}
        }
        if("pc".equals(strKey)){
        	String pk_cprk_b=refpc.getRefPK();
        	String[] pc=refpc.getRefCodes();
        	String sql= "select sum(isnull(rkmount,0))-sum(isnull(ckamount,0)) wckamount from eh_sc_cprkd_b where  pk_rkd_b in ("+pk_cprk_b+")";
			UFDouble wckamount = new UFDouble(0);
			IUAPQueryBS  iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
			try {
				Object wcamountstr = iUAPQueryBS.executeQuery(sql, new ColumnProcessor());
				wckamount = wcamountstr==null?new UFDouble(0):new UFDouble(wcamountstr.toString());			//��ѡ����δ��������
			} catch (Exception e1) {
				e1.printStackTrace();
			}
        	String  c ="";
        	if (pc != null)
      		  c = PubTools.combinArrayToString3(pc);
        	getBillCardPanel().setBodyValueAt(pk_cprk_b, row, "pk_cprk_b");
        	getBillCardPanel().setBodyValueAt(c, row, "pc");
        	getBillCardPanel().setBodyValueAt(wckamount, row, "wckamount");
        }
        
        /**ѡ��ֿ���ж��Ƿ���ĩ���ֿ�(���ٱ�Ҫ��) add by zqy 2010��11��23��10:38:19**/
        if(strKey.equals("pk_outtype") && e.getPos()==HEAD){
        	String pk_outtype = getBillCardWrapper().getBillCardPanel().getHeadItem("pk_outtype").getValueObject()==null?"":
        		getBillCardWrapper().getBillCardPanel().getHeadItem("pk_outtype").getValueObject().toString();
        	IUAPQueryBS iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        	String sql = " select * from bd_rdcl where pk_frdcl = '"+pk_outtype+"' and nvl(dr,0)=0 ";
        	try {
				ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql, new MapListProcessor());
				if(arr!=null && arr.size()>0){
					showErrorMessage("��ѡ��ĩ����������!");
					getBillCardWrapper().getBillCardPanel().setHeadItem("pk_outtype", null);
					return;
				}
			} catch (BusinessException e1) {
				e1.printStackTrace();
			}
        	
        }
        
		super.afterEdit(e);
	}
	
	@Override
	public boolean beforeEdit(BillEditEvent arg0) {
		if(arg0.getKey().equals("pc")){
			String pk_invbasdoc=getBillCardPanel().getBodyValueAt(arg0.getRow(), "pk_invbasdoc")==null?"":
        		getBillCardPanel().getBodyValueAt(arg0.getRow(), "pk_invbasdoc").toString();
    		PCRefModel.pk_invbasdoc=pk_invbasdoc;  	
    		refpc.getRef().getRefModel().reloadData();
		}
		return super.beforeEdit(arg0);
	}
	 /***
	 * �õ���¼����ǰ11��(������)�����ϵ��������ɹ�δ��������������ѿ������δ������
	 * @param pk_invbasdoc ���Ϲ�����PK
	 * @return
	 * wb 2008-10-22 18:25:34
	 */
	@SuppressWarnings("unchecked")
	public UFDouble[] getUnckUnrk(String pk_invbasdoc,UFDate date){
		UFDouble[] unrksc = new UFDouble[2];
		String pk_corp = _getCorp().getPk_corp();
		UFDate beforeDate = date.getDateBefore(10);
		StringBuffer sql = new StringBuffer()
		.append(" select sum(isnull(a.pgamount,0)) pgamount,sum(isnull(a.scamount,0)) scamount")
		.append(" from ")
//		.append(" ---��¼����ǰ11��(������)�������ɹ�δ�������")
		.append(" (select b.pk_invbasdoc,sum(isnull(b.pgamount,0)) pgamount,0 scamount")
		.append(" from eh_sc_pgd a,eh_sc_pgd_b b")
		.append(" where a.pk_pgd = b.pk_pgd")
		.append(" and isnull(a.lock_flag,'N') <> 'Y'")
		.append(" and isnull(a.rk_flag,'N')<>'Y'")
		.append(" and isnull(a.xdflag,'N')='Y'")
		.append(" and a.pk_corp = '"+pk_corp+"'")
		.append(" and b.pk_invbasdoc = '"+pk_invbasdoc+"' ")
		.append(" and isnull(a.dr,0) = 0")
		.append(" and isnull(b.dr,0) = 0")
		.append(" and a.dmakedate between '"+beforeDate+"' and '"+date+"'" )
		.append(" group by b.pk_invbasdoc")
		.append(" union all ")
//		.append(" ---�ѿ������δ������")
		.append(" select b.pk_invbasdoc,0 pgamount,sum(isnull(b.zamount,0)) scamount")
		.append(" from eh_ladingbill a,eh_ladingbill_b b")
		.append(" where a.pk_ladingbill = b.pk_ladingbill")
		.append(" and isnull(a.ck_flag,'N') <> 'Y'")
		.append(" and isnull(b.isfull,'N') <> 'Y'")
		.append(" and a.vbillstatus = 1")
		.append(" and a.pk_corp = '"+pk_corp+"'")
		.append(" and b.pk_invbasdoc = '"+pk_invbasdoc+"'  ")
		.append(" and isnull(a.dr,0) = 0")
		.append(" and isnull(b.dr,0) = 0")
		.append(" group by b.pk_invbasdoc")
		.append(" ) a");
		IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		try {
    		ArrayList  arr = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
    		if(arr!=null&&arr.size()>0){
				HashMap hmA = (HashMap)arr.get(0);
				unrksc[0] = new UFDouble(hmA.get("scamount")==null?"0":hmA.get("scamount").toString());			//�ѿ������δ������
				unrksc[1] = new UFDouble(hmA.get("pgamount")==null?"0":hmA.get("pgamount").toString());			//�������ɹ�δ�������
    		}
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return unrksc;
	}
}