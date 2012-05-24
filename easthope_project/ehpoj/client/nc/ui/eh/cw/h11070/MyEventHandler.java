
package nc.ui.eh.cw.h11070;

import java.util.ArrayList;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IVOPersistence;
import nc.ui.eh.button.IEHButton;
import nc.ui.pub.query.QueryConditionClient;
import nc.ui.trade.bill.IListController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.list.BillListUI;
import nc.ui.trade.list.ListEventHandler;
import nc.vo.eh.cw.h1103005.ArapSkVO;
import nc.vo.eh.cw.h11060.SkhxVO;
import nc.vo.eh.trade.z0207501.InvoiceVO;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFBoolean;
import nc.vo.pub.lang.UFDouble;
import nc.vo.pub.query.ConditionVO;

public class MyEventHandler extends ListEventHandler {
  
    QueryConditionClient dlg = null;
    private SuperVO[] bVos=null;
    private SuperVO[] skVOs=null;
    private String CustCode = null;
    public MyEventHandler(BillListUI billUI, IListController control) {
        super(billUI, control);
    }

    @Override
	protected void onBoElse(int intBtn) throws Exception {
        switch (intBtn)
        {
            case IEHButton.Next:
                onBoHX();
                break;
        }   
    }
    protected void onBoHX() throws Exception {
    	ArrayList Inall=new ArrayList();
    	//1����ȡ����ͷ���������
    	if(skVOs==null || skVOs.length<=0){
    		getBillUI().showErrorMessage("û�к����տ�޷�����");
    		return;
    	}
     	if(bVos==null || bVos.length<=0){
    		getBillUI().showErrorMessage("û�к��ʷ�Ʊ�޷�����");
    		return;
    	}
     	//2��������������
     	for(int i=0;i<skVOs.length;i++){
     		ArapSkVO skvo=(ArapSkVO) skVOs[i];
     		UFDouble whxje=skvo.getHxje();
     		for(int j=0;j<bVos.length;j++){
     			SkhxVO cvo=new SkhxVO();
     			InvoiceVO bvo=(InvoiceVO) bVos[j];
     			UFDouble bwhxje=bvo.getHxje();
     			whxje=whxje.sub(bwhxje);
 				if(whxje.toDouble()>0){    //�տ�����ڷ�Ʊ���
     				bvo.setHxje(new UFDouble(0));
     				bvo.setHx_flag(new UFBoolean(true));
     				skvo.setHxje(whxje);
     				cvo.setPk_sk(skvo.getPk_sk());//�տ��PK
     				cvo.setPk_invoice(bvo.getPk_invoice());//��ƱPK
     				cvo.setHxje(bwhxje);//�����Ľ��
     				cvo.setHxrq(_getDate());//��������
     				Inall.add(cvo);
     			
				}else if(whxje.toDouble()==0){  //�տ�����ڷ�Ʊ���
					bvo.setHxje(new UFDouble(0));
					skvo.setHxje(new UFDouble(0));
					bvo.setHx_flag(new UFBoolean(true));
					skvo.setHx_flag(new UFBoolean(true));
					cvo.setPk_sk(skvo.getPk_sk());//�տ��PK
     				cvo.setPk_invoice(bvo.getPk_invoice());//��ƱPK
     				cvo.setHxje(bwhxje);//�����Ľ��   
     				cvo.setHxrq(_getDate());//��������
     				Inall.add(cvo);
					break;

				}else if(whxje.toDouble()<0){   //�տ���С�ڷ�Ʊ���
					bvo.setHxje(new UFDouble(0).sub(whxje));
					skvo.setHxje(new UFDouble(0));
					skvo.setHx_flag(new UFBoolean(true));
					cvo.setPk_sk(skvo.getPk_sk());//�տ��PK
     				cvo.setPk_invoice(bvo.getPk_invoice());//��ƱPK
     				cvo.setHxje(bwhxje);//�����Ľ��
     				cvo.setHxrq(_getDate());//��������
     				Inall.add(cvo);
					break;
				}
     		}
     	}
     	
     	 //��ʼ����ͷ����
        ((MyListUI)this.getBillUI()).initBillData("2=1");

        /***************************1.�����ͷ���ݣ������������*************************/
        ((MyListUI)getBillUI()).setHeadData(skVOs);
        
        /*******************************2.�ٲ�ѯ��������******************************/
        if(bVos!=null && bVos.length>0){
            setInvData(bVos);
        } 
        
    	//3.��������������
        SkhxVO [] Invo=(SkhxVO[]) Inall.toArray(new SkhxVO[Inall.size()]);
     	ArrayList list=new ArrayList();
     	list.add(Invo);
     	list.add(bVos);
     	list.add(skVOs);
     
		IVOPersistence IVOPersistence =(IVOPersistence)NCLocator.getInstance().lookup(IVOPersistence.class.getName()); 
		IVOPersistence.insertVOArray(Invo);    //���ӵ�������
		IVOPersistence.updateVOArray(bVos);    //�޸����۷�Ʊ
		IVOPersistence.updateVOArray(skVOs);   //�޸��տ
    }
    /*
     * ���ܣ���ʼ����ѯ�Ի���
     */
    protected QueryConditionClient getQueryDLG()
    {  
        
        if(dlg == null){
            dlg = new QueryConditionClient();
            dlg.setTempletID(_getCorp().getPrimaryKey(), "H11070", null, null);
            dlg.setNormalShow(false);
        }
        return dlg;
    }
    
    
    /**
     * ���ڵ��ӱ�Ľ��洦���������ش˷���������ݣ�ͬʱ���л������� ���� getBufferData().clear();
     * getBufferData().adddVOToBuffer(aVo); //����б�setListHeadData(queryVos);
     * ���õ��ݲ���״̬ getBillUI().setBillOperate(IBillOperate.OP_NOTEDIT);
     */
    @Override
	protected void onBoQuery() throws Exception {
        int iRetType = getQueryDLG().showModal();
        if(iRetType == 1 ){
            ConditionVO[] ConCustcode = getQueryDLG().getConditionVOsByFieldCode("custcode");
            if (ConCustcode.length>0)
                CustCode = ConCustcode[0].getValue()==null?"%":ConCustcode[0].getValue();
            else
                CustCode = "%";
            
            //��ʼ����ͷ����
            ((MyListUI)this.getBillUI()).initBillData("2=1");

            /***************************1.�����ͷ���ݣ������������*************************/     
            skVOs = HYPubBO_Client.queryByCondition(nc.vo.eh.cw.h1103005.ArapSkVO.class, " pk_cubasdoc='"+CustCode+"' and isnull(hx_flag,'N')='N' and isnull(hxje,0)<>0 and vbillstatus=1 " +
                    "  and isnull(dr,0)=0 order by billno");
            ((MyListUI)getBillUI()).setHeadData(skVOs);
            /*******************************2.�ٲ�ѯ��������******************************/
            bVos = HYPubBO_Client.queryByCondition(nc.vo.eh.trade.z0207501.InvoiceVO.class, " pk_cubasdoc='"+CustCode+"' and isnull(hx_flag,'N')='N' and isnull(hxje,0)<>0 and vbillstatus=1 " +
                    "  and isnull(dr,0)=0 order by billno");
            if(bVos!=null && bVos.length>0)
                setInvData(bVos);
        }            
    }
    
   

    protected void setInvData(SuperVO[] bodyVos){
    	//��ձ�������
    	int rowcount=this.getBillListPanelWrapper().getBillListPanel().getBodyBillModel().getRowCount();
    	int[] rows=new int[rowcount];
    	for(int i=rowcount - 1;i>=0;i--){
	          rows[i]=i;
	     }
    	this.getBillListPanelWrapper().getBillListPanel().getBodyBillModel().delLine(rows);
    	getBillUI().updateUI();
        this.getBillListPanelWrapper().getBillListPanel().setBodyValueVO(bodyVos);
    }
    
}

