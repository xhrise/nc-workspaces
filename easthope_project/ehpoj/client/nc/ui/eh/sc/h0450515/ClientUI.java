
package nc.ui.eh.sc.h0450515;

import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.ColumnProcessor;
import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.ICombobox;
import nc.ui.eh.pub.PubTools;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.BillManageUI;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.eh.sc.h0450515.BomBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.scm.constant.ct.OperationState;

/**
 * 说明：BOM档案 
 * @author 张起源
 * 时间：2008-5-07
 */
public class ClientUI extends BillManageUI {
    UIRefPane ref = null;
    static String pk_bom_bs = null;
    static int flag = 0;
    
    public ClientUI() {
        super();
        initvar();
    }
    
    protected AbstractManageController createController() {
		return new ClientCtrl();
	}
    
    private void initvar(){
        try {
         ref=(UIRefPane)getBillCardPanel().getBillModel().getItemByKey("vinvcode").getComponent();
         ref.setMultiSelectedEnabled(true);
         ref.setTreeGridNodeMultiSelected(true);
     } catch (Exception e) {
         e.printStackTrace();
     }
    }

	public ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this,this.getUIControl());
	}
	
	protected void initSelfData() {
		 getBillCardWrapper().initBodyComboBox("invptype",ICombobox.BOM_INVPTYPE, true);
	     getBillListWrapper().initBodyComboBox("invptype",ICombobox.BOM_INVPTYPE, true);
	    
	}

	public void setDefaultData() throws Exception {
		getBillCardPanel().setHeadItem("ver", 1);	
		//getBillCardPanel().setHeadItem("new_flag","Y");
		getBillCardPanel().setTailItem("coperatorid", _getOperator());
		getBillCardPanel().setTailItem("dmakedate", _getDate());
		getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPk_corp());
		
	}
    
	/*
	 * 功能说明：自定义按钮（版本变更）
	 */
	protected void initPrivateButton() {
		nc.vo.trade.button.ButtonVO btn = ButtonFactory.createButtonVO(IEHButton.EditionChange, "版本变更", "版本变更");		
		btn.setOperateStatus(new int[]{OperationState.EDIT});
             nc.vo.trade.button.ButtonVO btnPrev = ButtonFactory.createButtonVO(IEHButton.Prev,"上一页","上一页");
             btnPrev.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
             addPrivateButton(btnPrev);
             nc.vo.trade.button.ButtonVO btnNext = ButtonFactory.createButtonVO(IEHButton.Next,"下一页","下一页");
             btnNext.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
             nc.vo.trade.button.ButtonVO btn1 = ButtonFactory.createButtonVO(IEHButton.LOCKBILL,"关闭","关闭");
            btn1.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
            addPrivateButton(btn1);
             addPrivateButton(btnNext);
             addPrivateButton(btn);
             //新物料BOM复制
             nc.vo.trade.button.ButtonVO btn3 = ButtonFactory.createButtonVO(IEHButton.CONFIRMBUG,"版本变更","版本变更");
             btn3.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
             addPrivateButton(btn3);
             nc.vo.trade.button.ButtonVO btn4 = ButtonFactory.createButtonVO(IEHButton.prevedition,"上一版本","上一版本");
             btn4.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
             addPrivateButton(btn4);
             nc.vo.trade.button.ButtonVO btn5 = ButtonFactory.createButtonVO(IEHButton.nextedition,"下一版本","下一版本");
             btn5.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
             addPrivateButton(btn5);
             nc.vo.trade.button.ButtonVO btnBus = ButtonFactory.createButtonVO(IEHButton.BusinesBtn,"业务操作","业务操作");
             btnBus.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
             addPrivateButton(btnBus);
             
             //确定生产此配方  add by wb 2009-2-11 18:27:04
             nc.vo.trade.button.ButtonVO confirm = ButtonFactory.createButtonVO(IEHButton.ConfirmSC,"确定此配方生产","确定此配方生产");
             confirm.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
             addPrivateButton(confirm);
             nc.vo.trade.button.ButtonVO btn2 = ButtonFactory.createButtonVO(IEHButton.UnCheck,"取消当前生产","取消当前生产");
             btn2.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
             addPrivateButton(btn2);
	}
	
	//王明
	@Override
	public void afterEdit(BillEditEvent e) {
			IUAPQueryBS  iUAPQueryBS=(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
			String str=e.getKey();
    		 if(e.getPos()==HEAD){
                 String[] formual=getBillCardPanel().getHeadItem(str).getEditFormulas();//获取编辑公式
                 getBillCardPanel().execHeadFormulas(formual);
             }else if (e.getPos()==BODY){
                 String[] formual=getBillCardPanel().getBodyItem(str).getEditFormulas();//获取编辑公式
                 getBillCardPanel().execBodyFormulas(e.getRow(),formual);
             }else{
                 getBillCardPanel().execTailEditFormulas();
             }	 
		
			if (str.equals("pk_invbasdoc") && e.getPos() == HEAD) {
            int rows = getBillCardPanel().getBillTable().getRowCount();
            String Headinvbasdoc = (String) (getBillCardPanel().getHeadItem("pk_invbasdoc") == null ? "head" : getBillCardPanel()
                    .getHeadItem("pk_invbasdoc").getValueObject());
        	String sql="select max(ver) ver from eh_bom  where pk_invbasdoc='"+Headinvbasdoc+"'  and isnull(dr,0)=0 ";
			try {
				Object verobj = iUAPQueryBS.executeQuery(sql, new ColumnProcessor());
				if(verobj!=null){
	            	int ver = Integer.parseInt(verobj.toString())+1;
	            	getBillCardPanel().setHeadItem("ver", ver);
	            	updateUI();
	            }
			} catch (BusinessException e1) {
				e1.printStackTrace();
			}
            
            for (int i = 0; i < rows; i++) {
                String Bodyinvbasdoc = (String) (getBillCardPanel().getBodyValueAt(i, "pk_invbasdoc") == null ? "0"
                        : getBillCardPanel().getBodyValueAt(i, "pk_invbasdoc").toString());
                String altBodyinvbasdoc = (String) (getBillCardPanel().getBodyValueAt(i, "pk_altinvbasdoc") == null ? "1"
                        : getBillCardPanel().getBodyValueAt(i,"pk_altinvbasdoc").toString());
                if (Headinvbasdoc.equals(Bodyinvbasdoc)|| Headinvbasdoc.equals(altBodyinvbasdoc)) {
                    showErrorMessage("子项物料与物料有重复，不允许操作！");
                    getBillCardPanel().setHeadItem("pk_invbasdoc", "");
                    getBillCardPanel().setHeadItem("vinvname", "");
                    getBillCardPanel().setHeadItem("vgg", "");
                    getBillCardPanel().setHeadItem("vxh", "");
                    getBillCardPanel().setHeadItem("vcolor", "");
                    getBillCardPanel().setHeadItem("vbrand", "");
                    getBillCardPanel().setHeadItem("pk_unit", "");
                    getBillCardPanel().setHeadItem("vunit", "");

                    return;
                    }
                }
			}     
            
			//选择替代物料的时候，对替代物料的编码和单位的可编辑性做判断 add by zqy 2008-6-14 11:35:47
             if(str.equals("altflag")&&e.getPos()==BODY){ 
                 String altflag = e.getValue()==null?"":e.getValue().toString();
                 if("true".equalsIgnoreCase(altflag)){
                     getBillCardPanel().getBodyItem("vtdwlcode").setEnabled(true);
                     getBillCardPanel().getBodyItem("valtunit").setEnabled(true);
                     getBillCardPanel().getBodyItem("altrate").setEnabled(true);
                 }else{
                     getBillCardPanel().getBodyItem("vtdwlcode").setEnabled(false);
                     getBillCardPanel().getBodyItem("valtunit").setEnabled(false);
                     getBillCardPanel().getBodyItem("altrate").setEnabled(false);
                 }
                   
             } 
             
            
            //子项物料
			if(str.equals("vinvcode")&&e.getPos()==BODY){
				int currrow=getBillCardPanel().getBillTable().getSelectedRow();
				String currbodyinvbasdoc=(String) (getBillCardPanel().getBodyValueAt(currrow, "pk_invbasdoc")==null?"0"
						:getBillCardPanel().getBodyValueAt(currrow, "pk_invbasdoc").toString());
				String Headinvbasdoc=(String) (getBillCardPanel().getHeadItem("pk_invbasdoc")==null?
						"head":getBillCardPanel().getHeadItem("pk_invbasdoc").getValueObject());
				String altBodyinvbasdoc=(String) (getBillCardPanel().getBodyValueAt(currrow, "pk_altinvbasdoc")==null?"1"
						:getBillCardPanel().getBodyValueAt(currrow, "pk_altinvbasdoc").toString());	
				if(currbodyinvbasdoc.equals(Headinvbasdoc)||currbodyinvbasdoc.equals(altBodyinvbasdoc)){
					showErrorMessage("子项物料与表头物料有重复，不允许操作！");
					getBillCardPanel().setBodyValueAt("", currrow, "pk_invbasdoc");
					getBillCardPanel().setBodyValueAt("", currrow, "vinvcode");
					getBillCardPanel().setBodyValueAt("", currrow, "vinvname");
					getBillCardPanel().setBodyValueAt("", currrow, "pk_unit");
					getBillCardPanel().setBodyValueAt("", currrow, "vunit");
					return;
				}
                //把子项用量统一全部转换成主计量单位
				//afterEdit_measRate(e); 
                    String[] refpks=ref.getRefPKs();
                    getBodyDataByRef(refpks, "pk_invbasdoc","vinvcode");
			}
            
            //编辑计量单位
            if(str.equalsIgnoreCase("vunit") && e.getPos()==BODY){
                //把子项用量统一全部转换成主计量单位
                afterEdit_measRate(e);
            }
            if(str.equalsIgnoreCase("amount") && e.getPos()==BODY){
                //把子项用量统一全部转换成主计量单位
                afterEdit_measRate(e);
            }
            if(str.equalsIgnoreCase("lossrate") && e.getPos()==BODY){
                //把子项用量统一全部转换成主计量单位
                afterEdit_measRate(e);
            }
            
			if(str.equals("vtdwlcode")&&e.getPos()==BODY){
				int currrow=getBillCardPanel().getBillTable().getSelectedRow();
				String currbodyinvbasdoc=(String) (getBillCardPanel().getBodyValueAt(currrow, "pk_invbasdoc")==null?"0"
						:getBillCardPanel().getBodyValueAt(currrow, "pk_invbasdoc").toString());
				String Headinvbasdoc=(String) (getBillCardPanel().getHeadItem("pk_invbasdoc")==null?
						"head":getBillCardPanel().getHeadItem("pk_invbasdoc").getValueObject());
				String altBodyinvbasdoc=(String) (getBillCardPanel().getBodyValueAt(currrow, "pk_altinvbasdoc")==null?"1"
						:getBillCardPanel().getBodyValueAt(currrow, "pk_altinvbasdoc").toString());	
				if(altBodyinvbasdoc.equals(Headinvbasdoc)){
					showErrorMessage("替代物料与表头物料有重复，不允许操作！");
					getBillCardPanel().setBodyValueAt("", currrow, "pk_altinvbasdoc");
					getBillCardPanel().setBodyValueAt("", currrow, "valtunit");
					getBillCardPanel().setBodyValueAt("", currrow, "vtdwlcode");
					getBillCardPanel().setBodyValueAt("", currrow, "vtdwlname");
					getBillCardPanel().setBodyValueAt("", currrow, "pk_altunit");
					return;
				}
                if(altBodyinvbasdoc.equals(currbodyinvbasdoc)){
                    showErrorMessage("替代物料与子项物料有重复，不允许操作！");
                    getBillCardPanel().setBodyValueAt("", currrow, "pk_altinvbasdoc");
                    getBillCardPanel().setBodyValueAt("", currrow, "valtunit");
                    getBillCardPanel().setBodyValueAt("", currrow, "vtdwlcode");
                    getBillCardPanel().setBodyValueAt("", currrow, "vtdwlname");
                    getBillCardPanel().setBodyValueAt("", currrow, "pk_altunit");
                    return;
                }
			}
			
			if(str.equalsIgnoreCase("pprate") && e.getPos()==HEAD){
				UFDouble pprate = new UFDouble(getBillCardPanel().getHeadItem("pprate").getValueObject()==null?"0":getBillCardPanel().getHeadItem("pprate").getValueObject().toString());
				int row = getBillCardPanel().getBillTable().getRowCount();
                for(int i=0;i<row;i++){
                	UFDouble zxamount = new UFDouble(getBillCardPanel().getBodyValueAt(i, "amount")==null?"0":getBillCardPanel().getBodyValueAt(i, "amount").toString());
                    getBillCardPanel().setBodyValueAt(pprate.multiply(zxamount), i, "ppamount");
                }
            }
			
			if(str.equalsIgnoreCase("amount") && e.getPos()==BODY){
				UFDouble pprate = new UFDouble(getBillCardPanel().getHeadItem("pprate").getValueObject()==null?"0":getBillCardPanel().getHeadItem("pprate").getValueObject().toString());
				int row = e.getRow();
                	UFDouble zxamount = new UFDouble(getBillCardPanel().getBodyValueAt(row, "amount")==null?"0":getBillCardPanel().getBodyValueAt(row, "amount").toString());
                    getBillCardPanel().setBodyValueAt(pprate.multiply(zxamount), row, "ppamount");
            }
			super.afterEdit(e);
		}
    
		//把源计量单位转换成目标计量单位
        public void afterEdit_measRate(BillEditEvent e){
            int row = e.getRow();
            //物料PK
            String pk_invbasdoc = getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc")==null?"0"
                    :getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc").toString();
            //计量单位
            String pk_unit = getBillCardPanel().getBodyValueAt(row, "pk_unit")==null?"0"
                    :getBillCardPanel().getBodyValueAt(row, "pk_unit").toString();
            //找出此物料的主计量单位
            String pk_measdoc = getBillCardPanel().getBodyValueAt(row, "pk_measdoc")==null?"0"
                    :getBillCardPanel().getBodyValueAt(row, "pk_measdoc").toString();
            
            //子项用量
            UFDouble amount = new UFDouble(getBillCardPanel().getBodyValueAt(row, "amount")==null?"0"
                    :getBillCardPanel().getBodyValueAt(row, "amount").toString());
            //损耗系数
            UFDouble lossrate = new UFDouble(getBillCardPanel().getBodyValueAt(row, "lossrate")==null?"0"
                    :getBillCardPanel().getBodyValueAt(row, "lossrate").toString());
            //总使用量为二者相加
            UFDouble sum = amount.add(lossrate);
            
            //如果二者相等，则不需要转换
            if (pk_unit.equalsIgnoreCase(pk_measdoc)){
                getBillCardPanel().setBodyValueAt(sum, row, "zamount");
                return;
            }
            
            nc.ui.eh.pub.PubTools tools = new nc.ui.eh.pub.PubTools();
            UFDouble rate = tools.getMeasRate(pk_unit, pk_measdoc);
            if (rate.equals(new UFDouble(-1))){
                rate = tools.getInvRate(pk_invbasdoc, pk_unit);
                if(rate.equals(new UFDouble(-1))){		//增加物料单位之间的转换提示  add by zqy 2010年11月16日14:35:59
                    this.showErrorMessage("物料没有维护计量单位的转换率，请检查!");
                    getBillCardPanel().setBodyValueAt(null, row, "pk_unit");
                    getBillCardPanel().setBodyValueAt(null, row, "vunit");
                    return;
                }else{
                    getBillCardPanel().setBodyValueAt(sum.div(rate), row, "zamount");
                }
            }else
                getBillCardPanel().setBodyValueAt(sum.multiply(rate), row, "zamount");
            
            
        }
        

		public void setBodySpecialData(CircularlyAccessibleValueObject[] arg0) throws Exception {
		}

		protected void setHeadSpecialData(CircularlyAccessibleValueObject arg0, int arg1) throws Exception {
		}

		protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] arg0) throws Exception {
		}
        
        
        public void getBodyDataByRef(String[] refpks,String refpkfield,String refcode){
            int selectedRow=getBillCardPanel().getBillTable().getSelectedRow();
            int rows=getBillCardPanel().getRowCount();
            BomBVO selectBombVO = null; 
            ArrayList arr=new ArrayList();
            BomBVO[] bombVOs;
			try {
				bombVOs = (BomBVO[])getVOFromUI().getChildrenVO();
				String[] pk_boms = new String[bombVOs.length];
		     for(int i=0;i<bombVOs.length;i++){
                if(i!=selectedRow){
              	  BomBVO bombVO = bombVOs[i];
              	  String pk_bom_b = bombVO.getPk_bom_b();
              	  pk_boms[i] = pk_bom_b;
              	  arr.add(bombVO);
                }
                if(i==selectedRow){
                   selectBombVO = bombVOs[i];
                   String pk_bom_b = selectBombVO.getPk_bom_b();
               	   pk_boms[i] = pk_bom_b;
                }
            }
		    pk_bom_bs = PubTools.combinArrayToString(pk_boms);
		    String pk_unit = getBillCardPanel().getBodyItem("pk_unit").getDefaultValue();
            for(int i=0;i<refpks.length;i++){
              BomBVO bombVO = null;
              String pk_invbasdoc=refpks[i];
          	  if(i==0){
          		bombVO = selectBombVO;
          	  }else{
          		bombVO = new BomBVO();
          	  }
              bombVO.setPk_invbasdoc(pk_invbasdoc);
          	  bombVO.setPk_unit(pk_unit);
              arr.add(bombVO);
            }
            int rowcount=getBillCardPanel().getRowCount();
            int[] rowss=new int[rowcount];
            for(int i=rowcount - 1;i>=0;i--){
                rowss[i]=i;
            }
            getBillCardPanel().getBillModel().delLine(rowss);
            flag = 1; // 在保存前先将原数据删除
            for(int i=0;i<arr.size();i++){
            	BomBVO bombVO = (BomBVO)arr.get(i);
                getBillCardPanel().getBillModel().addLine();
                getBillCardPanel().getBillModel().setBodyRowVO(bombVO, i);
                
                String[] invbasdocformual1 =getBillCardPanel().getBodyItem("vinvcode").getEditFormulas();//获取显示公式
                String[] invbasdocformual2 =getBillCardPanel().getBodyItem("pk_unit").getEditFormulas();//获取显示公式
                getBillCardPanel().execBodyFormulas(i,invbasdocformual1);
                getBillCardPanel().execBodyFormulas(i,invbasdocformual2);
                
                /**按花召滨要求，当表体物料编码为07开头的则计量单位可以修改，否则一律不允许修改 add by zqy 2010年11月29日14:47:49**/
            	String vinvcode = getBillCardWrapper().getBillCardPanel().getBodyValueAt(i, "vinvcode")==null?"":
            		getBillCardWrapper().getBillCardPanel().getBodyValueAt(i,"vinvcode").toString();
            	//截取物料编码前2位，以此来判断编码大类是否是07包装类物料
            	String subcode = vinvcode.substring(0, 2);
            	if(!vinvcode.equals("") && subcode.equals("07")){
            		getBillCardPanel().getBillModel("eh_bom_b").setCellEditable(i,"vunit", true);
            	}else{
            		getBillCardPanel().getBillModel("eh_bom_b").setCellEditable(i,"vunit", false);
            	}
                
            }
		 } catch (Exception e) {
				e.printStackTrace();
	     }
        }
}