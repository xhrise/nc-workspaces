package nc.ui.eh.trade.z0200301;

import java.util.ArrayList;

import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDouble;

/**
 * ����˵�����Ƽ۵�����
 * @author ����
 * @date 2008-04-08
 */
@SuppressWarnings("serial")
public class ClientUI extends AbstractClientUI {
    
	UIRefPane ref = null;
	public ClientUI() {
		super();
        initvar();
	}
	
	private void initvar(){
        try {
         ref=(UIRefPane)getBillCardPanel().getBillModel("eh_price_b").getItemByKey("vcode").getComponent();
         ref.setMultiSelectedEnabled(true);
         ref.setTreeGridNodeMultiSelected(true);
     } catch (Exception e) {
         e.printStackTrace();
     }
    }
	
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
		getBillCardPanel().setHeadItem("zxdate", _getDate());
		getBillCardPanel().setHeadItem("yxdate", PubTools.getDateAfter(_getDate(), 91));
		super.setDefaultData();

	}


	/*
	 * (non-Javadoc) @����˵�����Զ��尴ť
	 */
	@Override
	protected void initPrivateButton() {
		nc.vo.trade.button.ButtonVO btn = ButtonFactory.createButtonVO(
				IEHButton.ChooseInv, "ѡ���Ʒ", "ѡ���Ʒ");
		btn.setOperateStatus(new int[]{IBillOperate.OP_ADD,IBillOperate.OP_EDIT});
		addPrivateButton(btn);
		super.initPrivateButton();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void afterEdit(BillEditEvent e) {
		String strKey = e.getKey();
		// ���޸ĵ�������
		if (strKey.equals("modifypercent")) {
			UFDouble modifypercent = getBillCardPanel().getHeadItem("modifypercent").getValueObject()==null?
					new UFDouble():new UFDouble(getBillCardPanel().getHeadItem("modifypercent").getValueObject().toString());
			double a=modifypercent.doubleValue();
			if(a<0||a>100){
				showErrorMessage("������0-100����");
				return;
			}
			getBillCardPanel().setHeadItem("modifyprice", "");
			UFDouble modify=modifypercent.div(new UFDouble(100));
			int rows = getBillCardPanel().getBillTable().getRowCount();
			if (rows != 0) {
				for (int i = 0; i < rows; i++) {
					String ze = null;
					UFDouble oldprice = new UFDouble(getBillCardPanel().getBodyValueAt(i, "oldprice") == null ? "0"
											: getBillCardPanel().getBodyValueAt(i, "oldprice").toString());
					ze = (oldprice.multiply(modify)).add(oldprice) == null ? ""
							: (oldprice.multiply(modify)).add(oldprice).toString();
					getBillCardPanel().setBodyValueAt(ze, i, "newprice");
					//add by houcq begin
					UFDouble tonPrice = new UFDouble(this.getBillCardPanel().getBodyValueAt(i, "newprice").toString());//���Ƽ�
					String pk_invbasdoc = this.getBillCardPanel().getBodyValueAt(i, "pk_invbasdoc").toString();
					try {
						if(pk_invbasdoc!=null&&pk_invbasdoc.length()>0){
							this.setUA("bagprice",pk_invbasdoc,tonPrice,i);
						}
					} catch (BusinessException e1) {
						e1.printStackTrace();
					}
					//add by houcq end 
				}
				super.afterEdit(e);
			} else {
				getBillCardPanel().setHeadItem("modifypercent", "");
				showErrorMessage("����û������");
			}
		}
		// ���޸ĵ������
		if (strKey.equals("modifyprice")) {
			getBillCardPanel().setHeadItem("modifypercent", "");
			String modifyprice = getBillCardPanel().getHeadItem("modifyprice").getValue();
			int rows = getBillCardPanel().getBillTable().getRowCount();
			if (rows != 0) {
				for (int i = 0; i < rows; i++) {
					UFDouble oldprice = new UFDouble(getBillCardPanel().getBodyValueAt(i, "oldprice") == null ? "0"
							: getBillCardPanel().getBodyValueAt(i, "oldprice").toString());
					if(oldprice.toString().equals("")){
						showErrorMessage("����������û�е������Ƽ�!!!");
					}
					UFDouble mofdifyprice=new UFDouble(modifyprice);
					String price =mofdifyprice.add(oldprice)==null?"":mofdifyprice.add(oldprice).toString();
					getBillCardPanel().setBodyValueAt(price, i,"newprice");
					//add by houcq begin 2010-10-11
					UFDouble tonPrice = new UFDouble(this.getBillCardPanel().getBodyValueAt(i, "newprice").toString());//���Ƽ�
					String pk_invbasdoc = this.getBillCardPanel().getBodyValueAt(i, "pk_invbasdoc").toString();
					try {
						if(pk_invbasdoc!=null&&pk_invbasdoc.length()>0){
							this.setUA("bagprice",pk_invbasdoc,tonPrice,i);
						}
					} catch (BusinessException e1) {
						e1.printStackTrace();
					}
					//add by houcq end 
				}
			} else {
				getBillCardPanel().setHeadItem("modifyprice", "");
				showErrorMessage("����û������");
			}
		}
		//��Ʒ���ն�ѡʱ�Ĵ��� add by wb at 2008��5��5��14:11:20
		if(e.getKey().equals("vcode")){		
            String[] refpks=ref.getRefPKs();
            super.getBodyDataByRef2(refpks, "pk_invbasdoc","vcode"); 
	    }
		//���ܣ��������Ƽۻ�������Ƽۡ�ʱ�䣺2010-01-22.���ߣ���־Զ
		if(e.getKey().equals("newprice")){
			int row = this.getBillCardPanel().getBillTable().getSelectedRow();//��ǰ��
			UFDouble tonPrice = new UFDouble(this.getBillCardPanel().getBodyValueAt(row, "newprice").toString());//���Ƽ�
			String pk_invbasdoc = this.getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc").toString();
			try {
				if(pk_invbasdoc!=null&&pk_invbasdoc.length()>0){
					this.setUA("bagprice",pk_invbasdoc,tonPrice,row);
				}
			} catch (BusinessException e1) {
				e1.printStackTrace();
			}
		}
		super.afterEdit(e);
	}
	
	@SuppressWarnings("unchecked")
	public void getBodyDataByRef(String[] refpks,String refpkfield,String refcode){
    	int selectedRow=getBillCardPanel().getBillTable().getSelectedRow();
        int rows=getBillCardPanel().getRowCount();
        
        ArrayList arr=new ArrayList();
        for(int i=0;i<rows;i++){   
            if(i!=selectedRow){
                String pk_ref=getBillCardPanel().getBodyValueAt(i,refpkfield)==null?"":
                    getBillCardPanel().getBodyValueAt(i,refpkfield).toString();
                arr.add(pk_ref);          // �Ѿ�ѡ�������
            }
        }
        for(int i=0;i<refpks.length;i++){
            String pk_ref=refpks[i];
            arr.add(selectedRow++,pk_ref); // ֮��ѡ������
        }
        //��ձ�������
        int[] rowcount=new int[rows];
        for(int i=rows - 1;i>=0;i--){
        	rowcount[i]=i;
        }
        getBillCardPanel().getBillModel().delLine(rowcount);
        this.updateUI();
        for(int i=0;i<arr.size();i++){
            String pk_ref=arr.get(i).toString();
            getBillCardPanel().getBillModel().addLine();
            getBillCardPanel().setBodyValueAt(pk_ref, i, refpkfield);
            UFDouble oldprice = new PubTools().getInvPrice(pk_ref,_getDate());
 			getBillCardPanel().setBodyValueAt(oldprice, i, "oldprice");
            String[] invbasdocformual =getBillCardPanel().getBodyItem(refpkfield).getLoadFormula();//��ȡ��ʾ��ʽ
            getBillCardPanel().execBodyFormulas(i,invbasdocformual);
            String[] codeformual =getBillCardPanel().getBodyItem(refcode).getEditFormulas();       //��ȡ���յı༭��ʽ
            getBillCardPanel().execBodyFormulas(i,codeformual);
        }
    }

}
