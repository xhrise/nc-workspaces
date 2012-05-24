/*
 * �������� 2006-6-7
 *
 * TODO Ҫ���Ĵ����ɵ��ļ���ģ�壬��ת��
 * ���� �� ��ѡ�� �� Java �� ������ʽ �� ����ģ��
 */
package nc.ui.eh.trade.z0205505;

import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.PubTools;
import nc.ui.eh.refpub.InvdocByCusdocRefModel;
import nc.ui.eh.uibase.AbstractClientUI;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillMouseEnent;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.bill.AbstractManageController;
import nc.ui.trade.manage.ManageEventHandler;
import nc.vo.pub.BusinessException;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.lang.UFDouble;

/**
 * ���� һ���ۿ۹���
 * @author �麣
 * 2008-04-08
 */
@SuppressWarnings("serial")
public class ClientUI extends AbstractClientUI{

    public static String pk_cubasdoc = null;
	public ClientUI() {
        super();
    }

    /**
     * @param arg0
     */
    public ClientUI(Boolean arg0) {
        super(arg0);
    }

    /**
     * @param arg0
     * @param arg1
     * @param arg2
     * @param arg3
     * @param arg4
     */
    public ClientUI(String pk_corp, String pk_billType, String pk_busitype, String operater, String billId)
    {
        super(pk_corp, pk_billType, pk_busitype, operater, billId);
    }

    /* ���� Javadoc��
     * @see nc.ui.trade.manage.BillManageUI#createController()
     */
    @Override
	protected AbstractManageController createController() {
        // TODO �Զ����ɷ������
        return new ClientCtrl();
    }

    /*
     * (non-Javadoc)
     * @see nc.ui.trade.manage.BillManageUI#createEventHandler()
     */
    @Override
	public ManageEventHandler createEventHandler() {
        // TODO Auto-generated method stub
        return new ClientEventHandler(this,this.getUIControl());
    }
    
    @Override
	public void setDefaultData() throws Exception {
        getBillCardPanel().setHeadItem("zxdate", _getDate());
        getBillCardPanel().setHeadItem("yxdate", PubTools.getDateAfter(_getDate(), 91));
        getBillCardPanel().setHeadItem("def_1", "Y");             // ���±��
        getBillCardPanel().setHeadItem("def_2", "1");             // �汾
        super.setDefaultData();
    }
    
    @Override
    public boolean beforeEdit(BillEditEvent e) {
    	String strKey=e.getKey();
        if (e.getPos()==BODY){
            if("vinvbascode".equalsIgnoreCase(strKey)){
            	pk_cubasdoc=getBillCardPanel().getHeadItem("custom").getValueObject()==null?"":
                    getBillCardPanel().getHeadItem("custom").getValueObject().toString();
            	String pk_areacl = getBillCardPanel().getHeadItem("pk_areacl").getValueObject()==null?null:
                    getBillCardPanel().getHeadItem("pk_areacl").getValueObject().toString();
            	if(pk_areacl!=null&&pk_areacl.length()>0){
            		InvdocByCusdocRefModel.pk_cubasdoc = "%";
            	}else{
	            	if(pk_cubasdoc==null||pk_cubasdoc.length()<=0){
	            		showErrorMessage("�ͻ�����Ϊ��,����ѡ��ͻ�!");
	            	}
	            	InvdocByCusdocRefModel.pk_cubasdoc = pk_cubasdoc;  //�����̴���������
            	}
            }
        }
    	return super.beforeEdit(e);
    }
    @Override
    public void afterEdit(BillEditEvent e) {
        // TODO Auto-generated method stub
        String strKey=e.getKey();
        if(e.getPos()==HEAD){
            String[] formual=getBillCardPanel().getHeadItem(strKey).getEditFormulas();//��ȡ�༭��ʽ
            getBillCardPanel().execHeadFormulas(formual);
        }else if (e.getPos()==BODY){
            String[] formual=getBillCardPanel().getBodyItem(strKey).getEditFormulas();//��ȡ�༭��ʽ
            getBillCardPanel().execBodyFormulas(e.getRow(),formual);
        }else{
            getBillCardPanel().execTailEditFormulas();
        }
        Object zxdate=getBillCardPanel().getHeadItem("zxdate").getValueObject();
        Object yxdate=getBillCardPanel().getHeadItem("yxdate").getValueObject();
        
        //��ѡ��Ƭ����ʱ�򣬽��ͻ��������ÿ�
        if(strKey.equals("pk_areacl")){
        	//add by houcq 2011-03-15 begin
        	int rows=getBillCardPanel().getBillTable().getRowCount();
            int[] rowcount=new int[rows];
            for(int i=rows - 1;i>=0;i--){
            	rowcount[i]=i;
            }
            getBillCardPanel().getBillModel().delLine(rowcount);
            getBillCardPanel().setHeadItem("pk_invbasdoc", null);
          //add by houcq 2011-03-15 end
            getBillCardPanel().setHeadItem("custom", null);
            
        }
        //��ѡ��ͻ���ʱ�򣬽�Ƭ���ÿ�
        if(strKey.equals("custom")){
            getBillCardPanel().setHeadItem("pk_areacl", null);
            int rows=getBillCardPanel().getBillTable().getRowCount();
            int[] rowcount=new int[rows];
            for(int i=rows - 1;i>=0;i--){
            	rowcount[i]=i;
            }
            getBillCardPanel().getBillModel().delLine(rowcount);
            getBillCardPanel().setHeadItem("pk_invbasdoc", null);//add by houcq 2011-03-15
            this.updateUI();
        }
      //add by houcq 2011-03-15 begin
      //��ѡ������ʱ����Ƭ���Ϳͻ��ÿ�
        if(strKey.equals("pk_invbasdoc")){
            getBillCardPanel().setHeadItem("pk_areacl", null);
            int rows=getBillCardPanel().getBillTable().getRowCount();
            int[] rowcount=new int[rows];
            for(int i=rows - 1;i>=0;i--){
            	rowcount[i]=i;
            }
            getBillCardPanel().getBillModel().delLine(rowcount);
            getBillCardPanel().setHeadItem("custom", null);
            this.updateUI();
        }
      //add by houcq 2011-03-15 end
        if(strKey.equals("modifyprice")){
           UFDouble price =new UFDouble(getBillCardPanel().getHeadItem("modifyprice").getValueObject()==null?"0":
                               getBillCardPanel().getHeadItem("modifyprice").getValueObject().toString());
           int row=getBillCardPanel().getBillTable().getRowCount();
           for(int i=0;i<row;i++){
               getBillCardPanel().setBodyValueAt(price, i,"newdiscount");
               String[] formual=getBillCardPanel().getBodyItem("newdiscount").getEditFormulas();//��ȡ�༭��ʽ
               getBillCardPanel().execBodyFormulas(i,formual);
           }
        }   
        //���Ƚ�ִ�����ں���Ч����
        if(strKey.equals("zxdate")&&!yxdate.equals("")){
          if(new UFDate(yxdate.toString()).before(new UFDate(zxdate.toString()))){
              showErrorMessage("��Ч���ڲ�������ִ������");
              getBillCardPanel().setHeadItem("zxdate","");
          }
        }
        //���Ƚ�ִ�����ں���Ч����
        if(strKey.equals("yxdate")&&!zxdate.equals("")){
            if(new UFDate(yxdate.toString()).before(new UFDate(zxdate.toString()))){
                showErrorMessage("��Ч���ڲ�������ִ������");
                getBillCardPanel().setHeadItem("yxdate","");
            }
        }
        //ѡ������ʱ����ԭ�ۿ�
        if(strKey.equals("vinvbascode")){
        	int row=getBillCardPanel().getBillTable().getSelectedRow();
        	getBillCardPanel().setBodyValueAt(pk_cubasdoc, row, "pk_cubasdoc");
        	String[] formual=getBillCardPanel().getBodyItem("pk_cubasdoc").getEditFormulas();//��ȡ�༭��ʽ
        	getBillCardPanel().execBodyFormulas(row,formual);
        	//add by houcq 2011-04-18 �Ƽ���Դ����Ϊȡ�Ƽ۵�
            String pk_invbasdoc=getBillCardPanel().getBodyValueAt(row,"pk_invbasdoc")==null?"":
                                 getBillCardPanel().getBodyValueAt(row,"pk_invbasdoc").toString();       
            UFDouble price = new PubTools().getInvPrice(pk_invbasdoc,_getDate());
			getBillCardPanel().setBodyValueAt(price, row, "oldprice");
        }
        
        // ��ִ������
        if(strKey.equals("zxdate")&&e.getPos()==HEAD){
       	 UFDate zxdate2 = new UFDate(getBillCardPanel().getHeadItem("zxdate").getValueObject()==null?"":
       		 getBillCardPanel().getHeadItem("zxdate").getValueObject().toString());
       	 int rows=getBillCardPanel().getBillTable().getRowCount();
       	 for(int i=0;i<rows;i++){
       		 getBillCardPanel().setBodyValueAt(zxdate2, i, "zxdate");
       		 
       	 }
        }
        // ����Ч����
        if(strKey.equals("yxdate")&&e.getPos()==HEAD){
       	 UFDate yxdate2 = new UFDate(getBillCardPanel().getHeadItem("yxdate").getValueObject()==null?"":
       		 getBillCardPanel().getHeadItem("yxdate").getValueObject().toString());
       	 int rows=getBillCardPanel().getBillTable().getRowCount();
       	 for(int i=0;i<rows;i++){
       		 getBillCardPanel().setBodyValueAt(yxdate2, i, "yxdate");
       	 }
        }
        //�����ۡ�ʱ�䣺2010-02-02
        if(strKey.equals("tzdiscount")){
        	//��ǰ��
        	int row=getBillCardPanel().getBillTable().getSelectedRow();
        	//�Ƽ�
        	UFDouble oldprice = new UFDouble(this.getBillCardPanel().getBodyValueAt(row, "oldprice")==null?"0":this.getBillCardPanel().getBodyValueAt(row, "oldprice").toString());
        	//�����ۿ�
        	UFDouble tzdiscount = new UFDouble(this.getBillCardPanel().getBodyValueAt(row, "tzdiscount")==null?"0":this.getBillCardPanel().getBodyValueAt(row, "tzdiscount").toString());
        	//���� 
        	UFDouble newprice = oldprice.sub(tzdiscount);
        	String pk_invbasdoc = this.getBillCardPanel().getBodyValueAt(row, "pk_invbasdoc").toString();
			try {
				if(pk_invbasdoc!=null&&pk_invbasdoc.length()>0){
					this.setUA("packageprice",pk_invbasdoc,newprice,row);
				}
			} catch (BusinessException e1) {
				e1.printStackTrace();
			}
        }
        //�������ʱͬʱ���������ۡ�ʱ�䣺2011-03-31 add by houcq
        if(strKey.equals("modifyprice")){
        	//��ǰ��
        	int row=getBillCardPanel().getBillTable().getRowCount();
        	UFDouble modifyprice = new UFDouble(getBillCardPanel().getHeadItem("modifyprice").getValueObject()==null?"0":getBillCardPanel().getHeadItem("modifyprice").getValueObject().toString());
        	for (int i=0;i<row;i++)
        	{
        		//�Ƽ�
            	UFDouble oldprice = new UFDouble(getBillCardPanel().getBodyValueAt(i, "oldprice")==null?"0":this.getBillCardPanel().getBodyValueAt(i, "oldprice").toString());
            	//�����ۿ�
            	
            	//���� 
            	UFDouble newprice = oldprice.sub(modifyprice);
            	String pk_invbasdoc = this.getBillCardPanel().getBodyValueAt(i, "pk_invbasdoc").toString();
    			try {
    				if(pk_invbasdoc!=null&&pk_invbasdoc.length()>0){
    					this.setUA("packageprice",pk_invbasdoc,newprice,i);
    				}
    			} catch (BusinessException e1) {
    				e1.printStackTrace();
    			}
        	}
        	
        }
        super.afterEdit(e);
        
    }
 
    /*
     * ע���Զ��尴ť
     * 2008-04-02
     */
    @Override
	protected void initPrivateButton() {
        nc.vo.trade.button.ButtonVO btn = ButtonFactory.createButtonVO(IEHButton.GENRENDETAIL,"������ϸ","������ϸ");
        nc.vo.trade.button.ButtonVO btn1 = ButtonFactory.createButtonVO(IEHButton.CARDAPPROVE,"������","������");
        nc.vo.trade.button.ButtonVO btn2 = ButtonFactory.createButtonVO(IEHButton.STOCKCHANGE,"�ۿ۱��","�ۿ۱��");
        nc.vo.trade.button.ButtonVO btn3 = ButtonFactory.createButtonVO(IEHButton.LOCKBILL,"�ر�","�ر�");
        btn.setOperateStatus(new int[]{IBillOperate.OP_EDIT,IBillOperate.OP_ADD});
        btn1.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
        btn2.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
        btn3.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
        addPrivateButton(btn);
        addPrivateButton(btn3);
        addPrivateButton(btn1);
        addPrivateButton(btn2);
        super.initPrivateButton();
    }

//   @Override
    @Override
	public void mouse_doubleclick(BillMouseEnent arg0) {
        super.mouse_doubleclick(arg0);
        getButtonManager().getButton(IEHButton.GENRENDETAIL).setEnabled(false);
        updateButtons();
    }
   
    
}

   
    

