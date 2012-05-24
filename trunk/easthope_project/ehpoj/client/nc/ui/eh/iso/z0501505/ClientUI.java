
package nc.ui.eh.iso.z0501505;

import java.util.ArrayList;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.ICombobox;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.BusinessException;

/**
 * 说明：抽样单 
 * @author 张起源
 * 时间：2008-4-14
 */
public class ClientUI extends AbstractClientUI {
    
	public ClientUI() {
	     super();
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
	protected void initSelfData() {
		super.initSelfData();
		//表头的抽样单类型下拉菜单
		getBillCardWrapper().initHeadComboBox("type_flag", ICombobox.STR_TYPEFLAG,true);
		getBillListWrapper().initHeadComboBox("type_flag", ICombobox.STR_TYPEFLAG,true);
        getBillCardPanel().getBodyPanel().getRendererVO().setShowZeroLikeNull(false);
	}

	@Override
	public void setDefaultData() throws Exception {
		//获得抽样日期
		IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
		//String sql="select pk_psndoc  from sm_userandclerk  where userid = '"+_getOperator()+"'";
		StringBuffer sql = new StringBuffer()
		.append(" SELECT a.pk_psndoc ")
		.append(" FROM BD_PSNDOC a, BD_PSNBASDOC b, SM_USER c, SM_USERANDCLERK d ")
		.append(" WHERE d.pk_psndoc = b.pk_psnbasdoc AND d.userid = c.cuserid AND a.pk_psnbasdoc = b.pk_psnbasdoc AND d.userid = '"+_getOperator()+"'  ");

		ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
		String person=new String();
		if(al!=null && al.size()>0){
			HashMap hm=(HashMap) al.get(0);
			person=hm.get("pk_psndoc")==null?"":hm.get("pk_psndoc").toString();
		}
			
		
		getBillCardPanel().setHeadItem("sampledate", _getDate());
		getBillCardPanel().setHeadItem("shdate", _getDate());
		getBillCardPanel().setHeadItem("cyperson", person);
		super.setDefaultData();
	}
	
	@Override
	public void afterEdit(BillEditEvent e) {
		String strKey = e.getKey();
		if(strKey.equals("pk_invbasdoc")){
			getBillCardPanel().getBillModel().clearBodyData();   //清空表体数据
			String pk_invbasdoc = getBillCardPanel().getHeadItem("pk_invbasdoc").getValueObject()==null?"":
								getBillCardPanel().getHeadItem("pk_invbasdoc").getValueObject().toString();
			IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
			StringBuffer sql = new StringBuffer()
			.append(" select b.pk_project,b.ll_ceil,b.ll_limit,b.rece_ceil,b.rece_limit,b.anaimethod from eh_iso a ,eh_iso_b b ")
			.append(" where a.pk_iso = b.pk_iso and a.pk_invbasdoc = '"+pk_invbasdoc+"'")
			.append(" and vbilltype = 'ZA25' and NVL(lock_flag,'N')='N' and NVL(a.dr,0)=0 and NVL(b.dr,0)=0" );
			   
			try { 
				ArrayList al = (ArrayList) iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
				for(int i=0;i<al.size();i++){
					HashMap hm=(HashMap) al.get(i);
					String pk_project = hm.get("pk_project")==null?"":hm.get("pk_project").toString();
					String ll_ceil = hm.get("ll_ceil")==null?"":hm.get("ll_ceil").toString();
					String ll_limit = hm.get("ll_limit")==null?"":hm.get("ll_limit").toString();
					String rece_ceil =hm.get("rece_ceil")==null?"":hm.get("rece_ceil").toString();
					String rece_limit = hm.get("rece_limit")==null?"":hm.get("rece_limit").toString();
					String anaimethod = hm.get("anaimethod")==null?"":hm.get("anaimethod").toString();
					getBillCardPanel().addLine();
					getBillCardPanel().setBodyValueAt(pk_project, i, "pk_project");
					getBillCardPanel().setBodyValueAt(ll_ceil, i, "ll_ceil");
					getBillCardPanel().setBodyValueAt(ll_limit, i, "ll_limit");
					getBillCardPanel().setBodyValueAt(rece_ceil, i, "rece_ceil");
					getBillCardPanel().setBodyValueAt(rece_limit, i, "rece_limit");	
					getBillCardPanel().setBodyValueAt(anaimethod, i, "def_2");	
				    String[] formual=getBillCardPanel().getBodyItem("pk_project").getLoadFormula();//获取编辑公式
	                getBillCardPanel().execBodyFormulas(i,formual);
					
				}
			} catch (BusinessException e1) {
				e1.printStackTrace();
			}			
		}
        
		int row = getBillCardWrapper().getBillCardPanel().getRowCount();
		if(strKey.equals("pk_invbasdoc")){
			for(int i=0;i<row;i++){
				getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "pk_project", false);
				getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "ll_ceil", false);
				getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "ll_limit", false);
				getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "rece_ceil", false);
				getBillCardWrapper().getBillCardPanel().getBillModel().setCellEditable(i, "rece_limit", false);
			}
		}
		super.afterEdit(e);
	}
    
    @Override
	protected void initPrivateButton() {
        nc.vo.trade.button.ButtonVO btn1 = ButtonFactory.createButtonVO(IEHButton.LOCKBILL,"关闭","关闭");
        btn1.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
        addPrivateButton(btn1);
        super.initPrivateButton();
    }
	
}