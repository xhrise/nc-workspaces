package nc.ui.eh.sc.h0452005;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;
import nc.vo.bd.warehouseinfo.StordocVO;
/**
 * 功能说明：成品入库单
 * @author 王明
 * 2008-05-08 下午02:03:18
 */
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
		 //入库日期
		 getBillCardPanel().setHeadItem("rkdate", _getDate());
         //分解标记默认为is_fenj='0' by add zqy
         getBillCardPanel().setHeadItem("is_fenj", 'N');
         //入库批次的初始化
         getBillCardPanel().setHeadItem("pc", _getDate());
         //对入库员的默认值
         getBillCardPanel().setHeadItem("pk_llr", _getOperator());
         
         String pc = PubTools.getPC("eh_sc_cprkd", _getDate()); //获得批次
         getBillCardPanel().setHeadItem("pc", pc);
         //根据操作员初始入库员2009-12-08
         this.getBillCardPanel().setHeadItem("pk_llr", this.getPk_psndoc());
       
		super.setDefaultData();
	}
	
	
	 @Override
	protected void initSelfData() {    
	       //ButtonObject.setCode方法
	       getButtonManager().getButton(IEHButton.Prev).setCode("上一页");
	       getButtonManager().getButton(IEHButton.Next).setCode("下一页");
//	       getButtonManager().getButton(IEHButton.UnCheck).setCode("表体不显示");
//	       getButtonManager().getButton(IEHButton.BusinesBtn).setCode("业务操作");//modify by houcq取消成品入库单据终止单据按钮
	       super.initSelfData();
	    }
	@Override
    protected void initPrivateButton() {
	 	nc.vo.trade.button.ButtonVO btn1 = ButtonFactory.createButtonVO(IEHButton.LOCKBILL,"关闭","关闭");
        btn1.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
        addPrivateButton(btn1);
        nc.vo.trade.button.ButtonVO btn2 = ButtonFactory.createButtonVO(IEHButton.UnCheck,"表体不检测","表体不检测");
        btn2.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
        addPrivateButton(btn2);
        nc.vo.trade.button.ButtonVO btn3 = ButtonFactory.createButtonVO(IEHButton.UnCheck,"表体不显示","表体不显示");
        btn3.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
        addPrivateButton(btn3);
    	super.initPrivateButton();
    }

	@Override
	public void afterEdit(BillEditEvent e) {
		super.afterEdit(e);
		//取BOM的最新版本
		if(e.getKey().equals("vinvcode")){
			
			int row=getBillCardPanel().getBillTable().getSelectedRow();
			this.getBillCardWrapper().getBillCardPanel().setBodyValueAt(0, row, "rknumber");//考虑物料修改后不对数量进行修改，不能触发转换功能。时间：2010-01-06作者：张志远
			this.getBillCardWrapper().getBillCardPanel().setBodyValueAt(0, row, "rkmount");//考虑物料修改后不对数量进行修改，不能触发转换功能。时间：2010-01-06作者：张志远
			String pk_invbasdoc=getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc")==null?""
					:getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc").toString();
			IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
			if(pk_invbasdoc!=null&&pk_invbasdoc.length()>0){
				String sql="select ver from eh_bom where sc_flag ='Y'   and pk_invbasdoc='"+pk_invbasdoc+"'"+" and pk_corp='"+_getCorp().getPrimaryKey()+"' and isnull(dr,0)=0 ";
				try {
					ArrayList al= (ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
					Integer ver=null;
					if(al==null||al.size()==0){
						showErrorMessage("此物料没有在生产的配方,请维护BOM!");
					}else{
						HashMap hm=(HashMap) al.get(0);
						ver=hm.get("ver")==null?new Integer(0):new Integer(hm.get("ver").toString());
					}
					getBillCardPanel().setBodyValueAt(ver, row, "ver");
				} catch (BusinessException e1) {
					e1.printStackTrace();
				}
			}
		} 
        
        //当选择表头仓库后，同时会把选择好的仓库放到表体的仓库中
        if(e.getKey().equals("pk_store")){
            String pk_store = getBillCardWrapper().getBillCardPanel().getHeadItem("pk_store").getValueObject()==null?"":
                getBillCardWrapper().getBillCardPanel().getHeadItem("pk_store").getValueObject().toString();
            IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
            try {
                StordocVO stvo = (StordocVO) iUAPQueryBS.retrieveByPK(StordocVO.class, pk_store);
                String name = stvo==null?"":stvo.getStorname()==null?"":stvo.getStorname().toString();
                int row = getBillCardPanel().getBillTable().getRowCount();
                for(int i=0;i<row;i++){
                    getBillCardPanel().setBodyValueAt(pk_store, i, "pk_store");
                    getBillCardPanel().setBodyValueAt(name, i, "vck");
                }
            } catch (BusinessException e1) {
                e1.printStackTrace();
            }
        }
        
        //根据录入的物料个数算出吨数。时间：2010-01-06作者：张志远
        //入库个数
        if(e.getKey().equals("rknumber")){
        	int rows=e.getRow();
//        	String pk_measdoc=getBillCardPanel().getBodyValueAt(rows, "pk_measdoc")==null?"":
//        		getBillCardPanel().getBodyValueAt(rows, "pk_measdoc").toString();
        	String pk_invbasdoc = getBillCardPanel().getBodyValueAt(rows, "pk_invbasdoc")==null?"":
        		getBillCardPanel().getBodyValueAt(rows, "pk_invbasdoc").toString();
        	UFDouble amounts=new UFDouble(getBillCardPanel().getBodyValueAt(rows, "rknumber")==null?"-1000":
        		getBillCardPanel().getBodyValueAt(rows, "rknumber").toString());
        	try {
        		if(pk_invbasdoc!=null&&pk_invbasdoc.length()>0){
        			this.setUA("rkmount", pk_invbasdoc, amounts, rows);
        		}
			} catch (BusinessException e1) {
				e1.printStackTrace();
			}
        }
        
        /**选择仓库后，判断是否是末级仓库(花召滨要求) add by zqy 2010年11月23日10:38:19**/
        if(e.getKey().equals("pk_intype") && e.getPos()==HEAD){
        	String pk_intype = getBillCardWrapper().getBillCardPanel().getHeadItem("pk_intype").getValueObject()==null?"":
        		getBillCardWrapper().getBillCardPanel().getHeadItem("pk_intype").getValueObject().toString();
        	IUAPQueryBS iUAPQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        	String sql = " select * from bd_rdcl where pk_frdcl = '"+pk_intype+"' and nvl(dr,0)=0 ";
        	try {
				ArrayList arr = (ArrayList)iUAPQueryBS.executeQuery(sql, new MapListProcessor());
				if(arr!=null && arr.size()>0){
					showErrorMessage("请选择末级入库类型!");
					getBillCardWrapper().getBillCardPanel().setHeadItem("pk_intype", null);
					return;
				}
			} catch (BusinessException e1) {
				e1.printStackTrace();
			}
        }
        
	}
	
	@SuppressWarnings("unchecked")
	/**
	 * 功能：根据制单人对应的pk找到入库员的pk。该对应关系在 <用户管理> 中维护，维护项目为 <业务员>
	 * 时间：2009-12-08
	 * 作者：张志远
	 */
	public String getPk_psndoc(){
		String pk_psndoc = null;
//		String coperatorid = this.getBillCardPanel().getTailItem("coperatorid").getValueObject()==null?"":
//								this.getBillCardPanel().getTailItem("coperatorid").getValueObject().toString();
		StringBuffer str = new StringBuffer()
		.append(" SELECT a.pk_psndoc ")
		.append(" FROM BD_PSNDOC a, BD_PSNBASDOC b, SM_USER c, SM_USERANDCLERK d ")
		.append(" WHERE d.pk_psndoc = b.pk_psnbasdoc AND d.userid = c.cuserid AND a.pk_psnbasdoc = b.pk_psnbasdoc AND d.userid = '"+_getOperator()+"'  ");
		
		IUAPQueryBS iUPAQueryBS = (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
		try {
			ArrayList arr = (ArrayList)iUPAQueryBS.executeQuery(str.toString(), new MapListProcessor());
			if(arr!=null&&arr.size()>0){
				for(int i=0;i<arr.size();i++){
					HashMap hm = (HashMap) arr.get(i);
					pk_psndoc = hm.get("pk_psndoc")==null?"":hm.get("pk_psndoc").toString();
				}
				
			}
		} catch (BusinessException e) {
			e.printStackTrace();
		}
		return pk_psndoc;
	}
}
