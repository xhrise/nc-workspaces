
package nc.ui.eh.cw.h13006;

import java.util.ArrayList;
import java.util.Vector;

import nc.bs.eh.cw.h13006.ClientUICheckRuleGetter;
import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.ui.eh.pub.ICombobox;
import nc.ui.eh.pub.PubTools;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.beans.UITree;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.base.IChildMenuController;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.bsdelegate.BusinessDelegator;
import nc.ui.trade.manage.ManageEventHandler;
import nc.ui.trade.pub.BillTableCreateTreeTableTool;
import nc.ui.trade.pub.IVOTreeData;
import nc.ui.trade.treemanage.BillTreeManageUI;
import nc.vo.eh.cw.h13006.FtstandardBVO;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;

/*
 * 功能：成本费用分摊
 * 作者：zqy
 * 时间：2008-9-10 10:00:00
 */

public class ClientUI extends BillTreeManageUI{

	private static final long serialVersionUID = 1L;
    UIRefPane ref = null;
    static String pk_ftstandard_b = null;
    static int flag = 0;
    static  IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
    
    public ClientUI() {
        super();
        initvar();
    }
    
    //对参照的处理
	private void initvar() {
        try {
             ref=(UIRefPane)getBillCardPanel().getBillModel().getItemByKey("invcode").getComponent();
             ref.setMultiSelectedEnabled(true);
             ref.setTreeGridNodeMultiSelected(true);
         } catch (Exception e) {
             e.printStackTrace();
         }        
    }

    @Override
	protected IVOTreeData createTableTreeData() {
		return null;
	}

	@Override
	protected IVOTreeData createTreeData() {
		return new ClientManageData();
	}

	@Override
	public void afterInit() throws java.lang.Exception{
		modifyRootNodeShowName("成本费用类别");
	}

	@Override
	protected AbstractManageController createController() {
		return new ClientCtrl();
	}

	@Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo,
			int intRow) throws Exception {
	}
	
	@Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos)
			throws Exception {
	}
    
	@Override
	public void setDefaultData() throws Exception {
		//在表头设置公司
		getBillCardPanel().setHeadItem("pk_corp", _getCorp().getPk_corp());		
		//取得制单日期
        getBillCardPanel().setTailItem("dmakedate",_getDate());        
        //取得制单人名称
        getBillCardPanel().setTailItem("coperatorid",_getOperator());
        nc.ui.trade.pub.VOTreeNode selectNode = this.getBillTreeSelectNode();
        if (selectNode != null) {
            nc.vo.pub.SuperVO vo = (nc.vo.pub.SuperVO) selectNode.getData();           
            String pk_fytype = vo.getAttributeValue("pk_fytype").toString();
            getBillCardPanel().setHeadItem("pk_fytype", pk_fytype);
            
            StringBuffer sql = new StringBuffer()
            .append(" select typename from eh_arap_fytype ")
            .append(" where pk_fytype='"+pk_fytype+"' and isnull(dr,0)=0 ");
            String typename = null;
            try {
               Vector vector =(Vector)iUAPQueryBS.executeQuery(sql.toString(), new VectorProcessor());
               if(vector!=null && vector.size()>0){
                   Vector ve = (Vector) vector.get(0);
                   typename = ve.get(0)==null?"":ve.get(0).toString();
               }
           } catch (BusinessException e1) {
               e1.printStackTrace();
           }
           if("工资".equals(typename) || "制造费用".equals(typename)){
               getBillCardPanel().getHeadItem("invtype").setEnabled(false);
           }
        }
    
     }   

    @Override
	public void afterEdit(BillEditEvent e) {
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
        //触发参照
         if(str.equals("invcode") && e.getPos()==BODY ){
             String[] refpks=ref.getRefPKs();
             getBodyDataByRef(refpks, "pk_invbasdoc","invcode"); 
         }      
         
        super.afterEdit(e);
    }

    //表体物料参照可以多选 add by zqy 
	private void getBodyDataByRef(String[] refpks, String string, String string2) {
        int selectedRow=getBillCardPanel().getBillTable().getSelectedRow();
        FtstandardBVO selectfbvo = null;
        ArrayList arr=new ArrayList();
        FtstandardBVO[] Fbvo;
        try {
            Fbvo = (FtstandardBVO[]) getVOFromUI().getChildrenVO();
            String[] pk_fbvo = new String[Fbvo.length];
            for(int i=0;i<Fbvo.length;i++){
                if(i!=selectedRow){
                    FtstandardBVO fbvo = Fbvo[i];
                    String pk_ftstandard_b = fbvo.getPk_ftstandard_b();
                    pk_fbvo[i]=pk_ftstandard_b;
                    arr.add(fbvo);
                }
                if(i==selectedRow){
                    selectfbvo = Fbvo[i];
                    String pk_ftstandard_b = selectfbvo.getPk_ftstandard_b();
                    pk_fbvo[i]=pk_ftstandard_b;
                }
            }
            pk_ftstandard_b = PubTools.combinArrayToString(pk_fbvo);
            for(int i=0;i<refpks.length;i++){
                FtstandardBVO Ftbvo = null;
                String pk_invbasdoc = refpks[i];
                if(i==0){
                    Ftbvo = selectfbvo;
                }else{
                    Ftbvo = new FtstandardBVO();
                }
                Ftbvo.setPk_invbasdoc(pk_invbasdoc);
                arr.add(Ftbvo);
            }
            
            int rowcount=getBillCardPanel().getBillTable().getRowCount();
            int[] rowss=new int[rowcount];
            for(int i=rowcount - 1;i>=0;i--){
                rowss[i]=i;
            }           
            getBillCardPanel().getBillModel().delLine(rowss);
            
            flag = 1;// 在保存前先将原数据删除
            for(int i=0;i<arr.size();i++){
                FtstandardBVO ftstandbvo = (FtstandardBVO) arr.get(i);
                getBillCardPanel().getBillModel().addLine();
                getBillCardPanel().getBillModel().setBodyRowVO(ftstandbvo, i);
                
                String[] invbasdocformual1 =getBillCardPanel().getBodyItem("invcode").getEditFormulas();//获取显示公式
                getBillCardPanel().execBodyFormulas(i,invbasdocformual1);
            }            
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
	public String getRefBillType() {
		return null;
	}
    
	@Override
	protected ManageEventHandler createEventHandler() {
		return new ClientEventHandler(this,getUIControl());
	}

    @Override
	protected IChildMenuController createChildMenuController() {
        return super.createChildMenuController();
    }
    
	@Override
	protected BusinessDelegator createBusinessDelegator() {
		return super.createBusinessDelegator();
	}
	
	@Override
	protected UITree getBillTree() {
		return super.getBillTree();
	}

	@Override
	protected BillTableCreateTreeTableTool getBillTableTreeData() {
		return super.getBillTableTreeData();
	}
	
	@Override
	protected void initSelfData() {
         getBillCardWrapper().initHeadComboBox("invtype", ICombobox.STR_INVTYPE,true);
         getBillListWrapper().initHeadComboBox("invtype", ICombobox.STR_INVTYPE,true);
	}
   
   @Override
public Object getUserObject() {
        return new ClientUICheckRuleGetter();
    }
   
   
}
