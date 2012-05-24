package nc.ui.eh.cw.h13006;

import java.util.ArrayList;
import java.util.Vector;
import nc.bs.framework.common.NCLocator;
import nc.itf.eh.trade.pub.PubItf;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.treemanage.BillTreeManageUI;
import nc.ui.trade.treemanage.TreeManageEventHandler;
import nc.vo.eh.cw.h13006.FtstandardBVO;
import nc.vo.eh.cw.h13006.FtstandardVO;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.pub.SuperVO;

/*
 * ���ܣ��ɱ����÷�̯
 * ���ߣ�zqy
 * ʱ�䣺2008-9-10 10:00:00
 */

public class ClientEventHandler extends TreeManageEventHandler {
	
	nc.ui.trade.pub.VOTreeNode snode=null;
    int flag = 0 ;
    
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
        //���ù�˾����
        getBillCardPanelWrapper().getBillCardPanel().setBodyValueAt(_getCorp().getPk_corp(), i, "pk_corp");   
        }
        //�ǿ��ж�
        getBillCardPanelWrapper().getBillCardPanel().getBillData().dataNotNullValidate();     
        
        
        //ͬһ�����ϲ�����ͬһ���ɱ����������ά�����  add by zqy 2008-9-22 
        IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
        AggregatedValueObject aggvo = getBillUI().getVOFromUI();
        FtstandardVO fvo = (FtstandardVO) aggvo.getParentVO();
        String pk_ftstandard = fvo.getPk_ftstandard()==null?"":fvo.getPk_ftstandard().toString();//��������pk
        FtstandardBVO[] fbvo = (FtstandardBVO[]) aggvo.getChildrenVO();
        String fytype = fvo.getPk_fytype()==null?"":fvo.getPk_fytype().toString();//�ɱ��������
        Integer invtype = new Integer(fvo.getInvtype()==null?"9":fvo.getInvtype().toString());//����
//        String inv = null;
//        if(invtype.compareTo(0)==0){
//            inv = "����";
//        }if(invtype.compareTo(1)==0){
//            inv = "����";
//        }else{
//            inv = "����";
//        }
  
        StringBuffer fysql = new StringBuffer()
        .append(" select typename from eh_arap_fytype ")
        .append(" where pk_fytype='"+fytype+"' and isnull(dr,0)=0 ");
        String typename = null;
        Vector vector = (Vector)iUAPQueryBS.executeQuery(fysql.toString(), new VectorProcessor());
        if(vector!=null && vector.size()>0){
            Vector ve = (Vector) vector.get(0);
            typename = ve.get(0)==null?"":ve.get(0).toString();//�ɱ������������
        }
        
//        HashMap hmname = new HashMap();
//        StringBuffer wlsql = new StringBuffer()
//        .append(" select pk_invbasdoc,invname from eh_invbasdoc where isnull(dr,0)=0 ");
//        ArrayList arr = (ArrayList) iUAPQueryBS.executeQuery(wlsql.toString(), new MapListProcessor());
//        if(arr!=null && arr.size()>0){
//            String pk_invbasdoc = null;
//            String invname = null;
//            for(int i=0;i<arr.size();i++){
//                HashMap hm = (HashMap)arr.get(i);
//                pk_invbasdoc = hm.get("pk_invbasdoc")==null?"":hm.get("pk_invbasdoc").toString();
//                invname = hm.get("invname")==null?"":hm.get("invname").toString();
//                hmname.put(pk_invbasdoc, invname);
//            }
//        }
        
        int length = fbvo.length;
        StringBuffer SQL = new StringBuffer();
        for(int i=0;i<length;i++){
            String pk_invbasdoc = fbvo[i].getPk_invbasdoc()==null?"":fbvo[i].getPk_invbasdoc().toString();//����PK
//            String invname = hmname.get(pk_invbasdoc)==null?"":hmname.get(pk_invbasdoc).toString();//��������
            StringBuffer sql = new StringBuffer()
            .append(" select * from eh_arap_ftstandard_b where pk_ftstandard in ")
            .append(" (select pk_ftstandard from eh_arap_ftstandard where pk_fytype='"+fytype+"' ) ")
            .append(" and pk_invbasdoc='"+pk_invbasdoc+"' and pk_ftstandard <>nvl('"+pk_ftstandard+"',' ') and isnull(dr,0)=0 ");
            
            ArrayList all = (ArrayList)iUAPQueryBS.executeQuery(sql.toString(), new MapListProcessor());
            if(all!=null && all.size()>0){
                SQL.append("��("+(i+1)+")�������Ѿ���("+typename+")��ά������,���ʵ!\r\n");
            }
            
//            //һ������ֻ��ά����һ����������(���������ȼ��������)
//            StringBuffer lxsql = new StringBuffer()
//            .append(" select * from eh_arap_ftstandard_b where pk_ftstandard in ")
//            .append(" (select pk_ftstandard from eh_arap_ftstandard where invtype='"+invtype+"' and isnull(dr,0)=0 )")
//            .append(" and pk_invbasdoc='"+pk_invbasdoc+"' and isnull(dr,0)=0 ");
//            
//            ArrayList lxarr = (ArrayList)iUAPQueryBS.executeQuery(lxsql.toString(), new MapListProcessor());
//            if(lxarr!=null && lxarr.size()>0){
//                getBillUI().showErrorMessage("����:("+invname+")�Ѿ���("+inv+")��ά������,���ʵ!");
//                return;
//            }
            
        } 
        if(SQL.length()>0){
            getBillUI().showErrorMessage(SQL.toString());
            return;
        }
        
        //���ɱ��������Ϊȼ����ʱ�򣬱�ͷ���Ͳ���Ϊ���� add by zqy 
        if(("ȼ��".equals(typename)) && (invtype.compareTo(0)==0)){
            getBillUI().showErrorMessage("�ɱ��������Ϊȼ��ʱ��ͷ���Ͳ���Ϊ������!,���ʵ!");
            return;
        }
        
        if(ClientUI.flag==1){
            String sql = "delete from eh_arap_ftstandard_b where pk_ftstandard_b in "+ClientUI.pk_ftstandard_b ;
            PubItf pubItf = (PubItf) NCLocator.getInstance().lookup(PubItf.class.getName());
            pubItf.updateSQL(sql);
        }
        ClientUI.pk_ftstandard_b="('')";
        
        super.onBoSave();
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onBoEdit() throws Exception {	
		super.onBoEdit();
		
		Object fytype = getBillCardPanelWrapper().getBillCardPanel().getHeadItem("pk_fytype").getValueObject();
		//1045A81000000000EZU6--������� 1073B810000000000ODI--����
		if (fytype!=null)
		{
			IUAPQueryBS iUAPQueryBS =(IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName()); 
			StringBuffer fysql = new StringBuffer()
	        .append(" select typename from eh_arap_fytype ")
	        .append(" where pk_fytype='"+fytype+"' and isnull(dr,0)=0 ");
	        String typename = null;
	        Vector vector = (Vector)iUAPQueryBS.executeQuery(fysql.toString(), new VectorProcessor());
	        if(vector!=null && vector.size()>0){
	            Vector ve = (Vector) vector.get(0);
	            typename = ve.get(0)==null?"":ve.get(0).toString();//�ɱ������������
	        }
	        if("����".equals(typename) || "�������".equals(typename)){
	        	getBillCardPanelWrapper().getBillCardPanel().getHeadItem("invtype").setEnabled(false);
	        }
		}
	}
	/**
	 * ��ѡ��ʱִ�еĲ��� �����˻���ķ���,��Ϊ�����ʵ�ִ���Ч������ û�з��ӻ��������
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
	 * �˴����뷽��˵���� �������ڣ�(2008-3-25)
	 * 
	 * @param node
	 * nc.ui.trade.pub.VOTreeNode
	 */
	public void onQueryHeadData(nc.ui.trade.pub.VOTreeNode selectnode)
			throws Exception {
		//��ջ�������
		getBufferData().clear();
		nc.vo.pub.SuperVO vo = (nc.vo.pub.SuperVO) selectnode.getData();
		StringBuffer sqlWhere = new StringBuffer();
		sqlWhere.append(" pk_fytype = '").append(vo.getAttributeValue("pk_fytype")).append("' and pk_corp = '"+_getCorp().getPk_corp()+"' order by ts ");
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

}
