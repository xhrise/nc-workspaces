package nc.ui.eh.trade.z00120;

import java.text.DecimalFormat;
import java.util.HashMap;

import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.itf.uap.IVOPersistence;
import nc.jdbc.framework.processor.MapProcessor;
import nc.ui.eh.button.IEHButton;
import nc.ui.pub.ButtonObject;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.pub.VOTreeNode;
import nc.ui.trade.treemanage.BillTreeManageUI;
import nc.ui.trade.treemanage.TreeManageEventHandler;
import nc.vo.eh.trade.z00120.InvbasdocBVO;
import nc.vo.eh.trade.z00120.InvbasdocVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;

/**
 * 功能：物料档案
 * @author 张起源
 * 日期：2008-3-25
 */
public class ClientEventHandler extends TreeManageEventHandler {
	
	nc.ui.trade.pub.VOTreeNode snode=null;

	public ClientEventHandler(BillTreeManageUI billUI, ICardController control) {
		super(billUI, control);
	}
	
	@Override
	public boolean isAllowDelNode(nc.ui.trade.pub.TableTreeNode node) {
		return true;
	}

	@Override
	protected void onBoSave() throws Exception {
       
        int row = getBillCardPanelWrapper().getBillCardPanel().getBillModel().getRowCount();
        for (int i = 0; i < row; i++) {
        //设置公司编码
        getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getCorp().getPk_corp(), i, "pk_corp");
   
        }
        //得到修改人的名称和修改的日期
        getBillCardPanelWrapper().getBillCardPanel().setTailItem("editcoperid", _getOperator());
        getBillCardPanelWrapper().getBillCardPanel().setTailItem("editdate", _getDate());
        //非空判断
        getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();
        
       //对物料单位的表体表头的校验 by add wm 
        InvbasdocVO vo=(InvbasdocVO) getBillCardPanelWrapper().getBillVOFromUI().getParentVO();
        InvbasdocBVO[] bvos=(InvbasdocBVO[]) getBillCardPanelWrapper().getBillVOFromUI().getChildrenVO();
        String pk_main=vo.getPk_measdoc()==null?"":vo.getPk_measdoc().toString();
        for(int i=0;i<bvos.length;i++){
        	String pk_measdoc=bvos[i].getPk_measdoc();
        	if(pk_main.equals(pk_measdoc)){
        		getBillUI().showErrorMessage("你的主计量单位不能再作为辅助计量单位!!");
        		return;
        	}
        }
        super.onBoSave();
        // 把物料保存到U8中去 by add wm  time:2009年2月4日19:59:24
        PubItf pubitf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
        String info=pubitf.Insertfitemss(vo);
        if(!info.equals("")){
        	getBillUI().showErrorMessage(info);
        }
}

	@Override
	protected void onBoEdit() throws Exception {
		super.onBoEdit();
	}
	/**
	 * 树选中时执行的操作 重载了基类的方法,因为基类的实现存在效率问题 没有发挥缓存的作用
	 */
	@Override
	public void onTreeSelected(nc.ui.trade.pub.VOTreeNode node) {

		if (node.isRoot())
			return;
		nc.ui.trade.buffer.BillUIBuffer buffer = (nc.ui.trade.buffer.BillUIBuffer) 
												((nc.ui.trade.treemanage.BillTreeManageUI) getBillUI())
												.getTreeToBuffer().get(node.getNodeID());
		if (buffer == null || buffer.isVOBufferEmpty()) {
			try {
				onQueryHeadData(node);
				snode=node;
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				((nc.ui.trade.treemanage.BillTreeManageUI) getBillUI())
						.setListHeadData(getBufferData()
								.getAllHeadVOsFromBuffer());
				((nc.ui.trade.treemanage.BillTreeManageUI) getBillUI())
						.setBillOperate(IBillOperate.OP_NOTEDIT);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 此处插入方法说明。 创建日期：(2008-3-25)
	 * 
	 * @param node
	 * nc.ui.trade.pub.VOTreeNode
	 */
	public void onQueryHeadData(nc.ui.trade.pub.VOTreeNode selectnode)
			throws Exception {
		//清空缓冲数据
		getBufferData().clear();

		nc.vo.pub.SuperVO vo = (nc.vo.pub.SuperVO) selectnode.getData();
		StringBuffer sqlWhere = new StringBuffer();
		sqlWhere.append(" pk_invcl = '").append(vo.getAttributeValue("pk_invcl")).append("' and pk_corp = '"+_getCorp().getPk_corp()+"' order by ts ");
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
	public void onBoAdd(ButtonObject arg0) throws Exception {
        VOTreeNode node=((ClientUI)getBillUI()).getBillTreeSelectNode();
        if (node != null) {
            nc.vo.pub.SuperVO vo = (nc.vo.pub.SuperVO) node.getData();
            String endflag = vo.getAttributeValue("endflag").toString();
            if(endflag.equals("N")){
            	getBillUI().showErrorMessage("该级不是末级，不允许进行增加操作！");
            	return;
            }
            super.onBoAdd(arg0);
            
            String invclasscode = vo.getAttributeValue("invclasscode").toString();
            int length = invclasscode.length();
            String pk_corp = _getCorp().getPk_corp();
            StringBuffer sql=new StringBuffer("")
            .append("  SELECT CASE WHEN a.zcinvcode IS NULL THEN b.oldinvcode ")
            .append("              ELSE a.zcinvcode ")
            .append("         END invcode")
            .append("   FROM ")
            .append("  (SELECT max(invcode) zcinvcode FROM eh_invbasdoc WHERE invcode like '"+invclasscode+"%' and pk_corp = '"+pk_corp+"' AND LEN(invcode) = "+length+"+4 and isnull(dr,0)=0) a,")
            .append("  (SELECT max(invcode) oldinvcode FROM eh_invbasdoc WHERE invcode like '"+invclasscode+"%' and pk_corp = '"+pk_corp+"' AND LEN(invcode) = "+length+"+3 and isnull(dr,0)=0) b");
            
            
            IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
            HashMap hm=(HashMap)iUAPQueryBS.executeQuery(sql.toString(),new MapProcessor());
            String code="";
            DecimalFormat df = new DecimalFormat("0000");

            if(hm.get("invcode")!=null){
                String invcode=hm.get("invcode").toString().trim();
                String addcode = df.format((Integer.parseInt(invcode.substring(invclasscode.length()))+1));
                code=invclasscode+addcode;
            }else{
                code=invclasscode+"0001";
            }
            
            
            getBillCardPanelWrapper().getBillCardPanel().setHeadItem("invcode",code);       
            getBillUI().updateUI();
        }      
		
	}
    
    @Override
	protected void onBoElse(int intBtn) throws Exception {
        switch (intBtn)
        {
            case IEHButton.Prev:    //上一页 下一页
                onBoBrows(intBtn);
                break;
            case IEHButton.Next:    //上一页 下一页
                onBoBrows(intBtn);
                break;
            case IEHButton.LOCKBILL: //停用按钮 
                onbolockflag();
                break;
        }   
    }
    
    //增加对单个物料关闭的按钮 add by zqy 2008-6-26 14:59:21
    private void onbolockflag() throws Exception {
        AggregatedValueObject aggvo = getBillUI().getVOFromUI();
        InvbasdocVO ivo = (InvbasdocVO) aggvo.getParentVO();
        String pk = ivo.getPrimaryKey();
        String lock_flag = ivo.getLock_flag()==null?"N":ivo.getLock_flag().toString();
        if(lock_flag.equals("Y")){
            getBillUI().showErrorMessage("该物料已经停用！");
            return;
        }else if(!pk.equals("")){
            int iRet = getBillUI().showYesNoMessage("是否确定进行停用此物料操作?");
            if(iRet == MessageDialog.YES_YESTOALL_NO_CANCEL_OPTION){
                IVOPersistence ivoPersistence = (IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName());
                ivo.setAttributeValue("lock_flag", new UFBoolean(true));
                ivoPersistence.updateVO(ivo);
                getBillUI().showWarningMessage("已经停用成功");
                onBoRefresh();
            }
            else{
                return;
            }
        }
        setBoEnabled();
    }


    private void onBoBrows(int intBtn) throws java.lang.Exception {
        // 动作执行前处理
        buttonActionBefore(getBillUI(), intBtn);
        switch (intBtn) {
        case IEHButton.Prev: {
            getBufferData().prev();
            break;
        }
        case IEHButton.Next: {
            getBufferData().next();
            break;
        }
        }
        // 动作执行后处理
        buttonActionAfter(getBillUI(), intBtn);
        getBillUI().showHintMessage(
                nc.ui.ml.NCLangRes.getInstance()
                        .getStrByID(
                                "uifactory",
                                "UPPuifactory-000503",
                                null,
                                new String[] { nc.vo.format.Format
                                        .indexFormat(getBufferData()
                                                .getCurrentRow()+1) })/*
                                                                     * @res
                                                                     * "转换第:" +
                                                                     * getBufferData().getCurrentRow() +
                                                                     * "页完成)"
                                                                     */
                        );
         setBoEnabled();
    }

    private void setBoEnabled() throws Exception {
        AggregatedValueObject aggvo = getBillUI().getVOFromUI();
        if(getButtonManager().getButton(IEHButton.Prev)!=null){
            if(!getBufferData().hasPrev()){
                getButtonManager().getButton(IEHButton.Prev).setEnabled(false);
            }
            else{
                getButtonManager().getButton(IEHButton.Prev).setEnabled(true);
            }
            if(!getBufferData().hasNext()){
                getButtonManager().getButton(IEHButton.Next).setEnabled(false);
            }
            else{
                getButtonManager().getButton(IEHButton.Next).setEnabled(true);
            }
        }
        //在有关闭按钮时对关闭按钮的控制 
        String[] keys = aggvo.getParentVO().getAttributeNames();
        if(keys!=null && keys.length>0){
            for(int i=0;i<keys.length;i++){
                if(keys[i].endsWith("lock_flag")){ 
                    String lock_flag=getBillCardPanelWrapper().getBillCardPanel().getHeadItem("lock_flag").getValueObject()==null?"N":
                        getBillCardPanelWrapper().getBillCardPanel().getHeadItem("lock_flag").getValueObject().toString();
                    if(lock_flag.equals("false")){
                        getButtonManager().getButton(IEHButton.LOCKBILL).setEnabled(true);
                    }else{
                        getButtonManager().getButton(IEHButton.LOCKBILL).setEnabled(false);
                    }
                    break;
                }
                   
            }
        }
        getBillUI().updateButtonUI();
    }

    @Override
	protected void onBoQuery() throws Exception {
        StringBuffer sbWhere = new StringBuffer();
        if(askForQueryCondition(sbWhere)==false) 
            return;

        String sqlWhere = sbWhere.toString()+" order by invcode";
        SuperVO[] queryVos = queryHeadVOs(sqlWhere);
        
        getBufferData().clear();
        // 增加数据到Buffer
        addDataToBuffer(queryVos);

        updateBuffer();	
    }
    

}
