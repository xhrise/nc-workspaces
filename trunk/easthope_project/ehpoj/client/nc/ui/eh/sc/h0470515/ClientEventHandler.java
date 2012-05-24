package nc.ui.eh.sc.h0470515;

import nc.ui.eh.trade.z00102.ClientUI;
import nc.ui.pub.ButtonObject;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.treemanage.TreeManageEventHandler;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;

/**
 * 功能：设备档案
 * ZB27
 * @author 王兵
 * 2009-1-9 15:28:11
 */
public class ClientEventHandler extends TreeManageEventHandler {
	
	public ClientEventHandler(BillManageUI billUI, ICardController control) {
		super(billUI, control);
	}

	@Override
	public boolean isAllowDelNode(nc.ui.trade.pub.TableTreeNode node) {
		return true;
	}
		
	@Override
	protected void onBoSave() throws Exception {
        //非空的有效性判断
        getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
//        PubItf pubitf=(PubItf)NCLocator.getInstance().lookup(PubItf.class.getName());
        AggregatedValueObject aggvo = getBillCardPanelWrapper().getBillVOFromUI();
//        try{
//        	ScSbbasdocVO vo = (ScSbbasdocVO)aggvo.getParentVO();
//        	String pk_sb = vo.getPrimaryKey()==null?"":vo.getPrimaryKey().toString();
//            String sql=" code='"+vo.getCode()+"' and pk_sb!='"+pk_sb+"' and isnull(dr,0)=0 ";
//            int i=pubitf.BackCheck(vo.getTableName(),sql);
//            if(i==1){
//                getBillUI().showErrorMessage("设备编码已经存在，不允许重复！");
//                return;
//            }
//        }catch (Exception e) {
//        	e.printStackTrace();
//        }
        super.onBoSave();
        onBoRefresh();
        getBillTreeManageUI().setCardUIData(aggvo);
	}

	@Override
	protected void onBoEdit() throws Exception {
		
		super.onBoEdit();
	}
	
	@Override
	public void onBoAdd(ButtonObject arg0) throws Exception {
		super.onBoAdd(arg0);
//        VOTreeNode node= getBillTreeManageUI().getBillTreeSelectNode();
//        if(node!=null){
//        	 String supercode=node.getData().getAttributeValue("code").toString();
//             StringBuffer sql=new StringBuffer("")
//             .append(" select max(code) code from eh_sc_sbbasdoc where pk_corp='"+_getCorp().getPrimaryKey()+"'")
//             .append(" and code like '"+supercode+"%' and code != '"+supercode+"' and isnull(dr,0)=0 ");
//             IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
//             HashMap hm=(HashMap)iUAPQueryBS.executeQuery(sql.toString(),new MapProcessor());
//             String code="";
//             DecimalFormat df = new DecimalFormat("000");
//             if(hm.get("code")!=null){
//                 String maxcode=hm.get("code").toString().trim();
//                 String addcode = df.format((Integer.parseInt(maxcode.substring(supercode.length()))+1));
//                 code=supercode+addcode;
//             }else{
//                 code=supercode+"001";
//             }
//             getBillCardPanelWrapper().getBillCardPanel().setHeadItem("code",code);       
//             getBillUI().updateUI();
//        }
	}
    
    @Override
    protected void onBoDelete() throws Exception {
        // TODO Auto-generated method stub
        super.onBoDelete();
        ((ClientUI)getBillUI()).getBillTreeSelectNode();
        ((ClientUI)getBillUI()).getBillTree().setSelectionRow(0);
        onBoRefresh();
        
    }
    
    @Override
	public void onTreeSelected(nc.ui.trade.pub.VOTreeNode node) {

        if (node.isRoot())
            return;
        node = getBillTreeManageUI().getBillTreeSelectNode();
        try {
            onQueryHeadData(node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void onQueryHeadData(nc.ui.trade.pub.VOTreeNode selectnode)
			throws Exception {
		//清空缓冲数据
		getBufferData().clear();
		nc.vo.pub.SuperVO vo = (nc.vo.pub.SuperVO) selectnode.getData();
		String pk_sb = vo.getAttributeValue("pk_sb")==null?"":vo.getAttributeValue("pk_sb").toString();
		StringBuffer sqlWhere = new StringBuffer();
		sqlWhere.append(" pk_father = '"+pk_sb+"' or pk_sb = '"+pk_sb+"' order by code");
		SuperVO[] queryVos = queryHeadVOs(sqlWhere.toString());
		if (queryVos != null && queryVos.length != 0) {
			for (int i = 0; i < queryVos.length; i++) {
				AggregatedValueObject aVo = (AggregatedValueObject) Class
						.forName(getUIController().getBillVoName()[0])
						.newInstance();
				aVo.setParentVO(queryVos[i]);
				getBufferData().addVOToBuffer(aVo);
			}
			getBillUI().setListHeadData(queryVos);
			getBufferData().setCurrentRow(0);
			getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
		} else {
			getBillUI().setListHeadData(queryVos);
			getBufferData().setCurrentRow(-1);
			getBillUI().setBillOperate(IBillOperate.OP_INIT);
		}
   }
    
   @Override
	protected void onBoRefresh() throws Exception {
	   getBillTreeManageUI().createBillTree(getBillTreeManageUI().getCreateTreeData());
	   getBillTreeManageUI().afterInit();
//	   getBillTreeManageUI().setBillOperate(nc.ui.trade.base.IBillOperate.OP_INIT);
	   super.onBoRefresh();
	}
    
	
}
