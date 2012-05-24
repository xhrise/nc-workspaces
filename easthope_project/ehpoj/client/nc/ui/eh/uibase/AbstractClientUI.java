/**
 * Module AbstractClientUI.java
 * @author chen cp (tom)
 * @date 2007-7-19
 */
package nc.ui.eh.uibase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import javax.swing.event.ListSelectionEvent;

import nc.bs.framework.common.NCLocator;
import nc.itf.uap.IUAPQueryBS;
import nc.jdbc.framework.processor.MapListProcessor;
import nc.jdbc.framework.processor.VectorProcessor;
import nc.ui.eh.button.ButtonFactory;
import nc.ui.eh.button.IEHButton;
import nc.ui.eh.pub.PubTools;
import nc.ui.pub.bill.BillEditEvent;
import nc.ui.pub.bill.BillItem;
import nc.ui.trade.base.IBillOperate;
import nc.ui.trade.manage.BillManageUI;
import nc.vo.pub.BusinessException;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.lang.UFDouble;
import nc.vo.trade.pub.IBillStatus;

/**
 * ˵��:����������,�����ͽ���Ӧ�̳д���
 * @author LiuYuan 2007-9-27 ����01:57:47
 */
abstract public class AbstractClientUI extends BillManageUI  {

    
    /**
     * ���ڼ�¼��pk�Ƿ�����
     */
    private Vector m_vhasOrdered=new Vector();
    
    public AbstractClientUI() {
        super();
        
    }

    public AbstractClientUI(Boolean bool) {
        super(bool);
    }

    public AbstractClientUI(String arg1, String arg2, String arg3, String arg4, String arg5) {
        super(arg1, arg2, arg3, arg4, arg5);
    }

    @Override
	public nc.ui.trade.manage.ManageEventHandler createEventHandler() {
        return new AbstractEventHandler(this, getUIControl());
    }

    public String getBilltype() {
        return getUIControl().getBillType();
    }

    /**
     * ����: ��ʼ��UI���������
     * @author LiuYuan 2007-9-27 ����02:10:19
     */
    @Override
	protected void initSelfData() {

        getBillCardWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);
        getBillListWrapper().initHeadComboBox("vbillstatus",IBillStatus.strStateRemark, true);
//        // ��ʾ���ݿ��е�0.
  //      getBillCardPanel().getBodyPanel().getRendererVO().setShowZeroLikeNull(false);
//        // ����ǧ��λ
        getBillCardWrapper().getBillCardPanel().getBodyPanel().setShowThMark(true);
        getBillListPanel().getParentListPanel().setShowThMark(true);
//        getBillCardPanel().setShowThMark(tableCode, showThMark)
        getBillCardPanel().setShowThMark(true);
        nc.vo.pub.bill.BillRendererVO voCell = new nc.vo.pub.bill.BillRendererVO();
		voCell.setShowThMark(true);
		voCell.setShowRed(true);//add by houcq 2011-02-12 ���ø�����ʾΪ��ɫ
		voCell.setShowZeroLikeNull(false);//add by houcq 2011-02-14 ����0����ʾ
		getBillCardPanel().setBodyShowFlags(voCell);
        
        
    }

    @Override
    protected void initPrivateButton() {
    	 nc.vo.trade.button.ButtonVO btnPrev = ButtonFactory.createButtonVO(IEHButton.Prev,"��һҳ","��һҳ");
    	 btnPrev.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
         addPrivateButton(btnPrev);
         nc.vo.trade.button.ButtonVO btnNext = ButtonFactory.createButtonVO(IEHButton.Next,"��һҳ","��һҳ");
         btnNext.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
         addPrivateButton(btnNext);
         nc.vo.trade.button.ButtonVO btnBus = ButtonFactory.createButtonVO(IEHButton.BusinesBtn,"ҵ�����","ҵ�����");
         btnBus.setOperateStatus(new int[]{IBillOperate.OP_NOTEDIT});
         addPrivateButton(btnBus);
    	super.initPrivateButton();
    }
    /**
     * ����:���û����ֶ�ֵ,pk_corp�ڱ����¼�ʱ����
     * ��ͷ:����״̬(����̬),���ݱ��� ��β:�Ƶ���,�Ƶ�����
     * @throws Exception
     * @author LiuYuan 2007-9-27 ����02:20:44
     */
    @Override
	public void setDefaultData() throws Exception {
    	setDefaultData_withNObillno();
    }

    /***
     * ��Ĭ��ֵʱ����ȡ���ݺţ��ڱ���ʱ��ȡ
     * wb 2008-12-22 14:28:49
     * @throws Exception
     */
    public void setDefaultData_withNObillno() throws Exception {
        //��ͷ���õ�
        String pk_corp = _getCorp().getPrimaryKey();
        BillItem oper = getBillCardPanel().getTailItem("coperatorid");
        if (oper != null)
            oper.setValue(_getOperator());
        else
            getBillCardPanel().getHeadItem("coperatorid").setValue(_getOperator());
        BillItem date = getBillCardPanel().getTailItem("dmakedate");
        if (date != null)
            date.setValue(_getDate());
        else
            getBillCardPanel().getHeadItem("dmakedate").setValue(_getDate());
        BillItem busitype = getBillCardPanel().getHeadItem("pk_busitype");
        if (busitype != null)
            getBillCardPanel().setHeadItem("pk_busitype", this.getBusinessType());
       
        getBillCardPanel().getHeadItem("pk_corp").setValue(pk_corp);
        BillItem vbilltype = getBillCardPanel().getHeadItem("vbilltype");			//��������
        if(vbilltype!=null){
        	getBillCardPanel().setHeadItem("vbilltype", this.getUIControl().getBillType());
        }
        BillItem vbillstatus = getBillCardPanel().getHeadItem("vbillstatus");
        if (vbillstatus!= null)
        	getBillCardPanel().getHeadItem("vbillstatus").setValue(Integer.toString(IBillStatus.FREE));
    }
    
    
    public void valueChanged(ListSelectionEvent arg0) {
    }

    @Override
	public void setBodySpecialData(CircularlyAccessibleValueObject[] vos) throws Exception {
    }

    @Override
	protected void setHeadSpecialData(CircularlyAccessibleValueObject vo, int intRow)
            throws Exception {
    }

    @Override
	protected void setTotalHeadSpecialData(CircularlyAccessibleValueObject[] vos) throws Exception {
    }
    

    protected void setButtonState(int iButton, boolean bEnable) {
        nc.ui.pub.ButtonObject bo = getButtonManager().getButton(iButton);
        if (bo == null)
            return;
        bo.setEnabled(bEnable);
    }
 
    /**
     * ����: ���ܱ��嵽��ͷ
     * @param bodyedit Ҫ���ܵı�����
     * @param headedit ���ܵı�ͷ��
     * @return:void
     * @author ������
     * 2007-10-24 ����08:06:17
     */
    protected void edit(String strCol,String bodyedit,String headedit){
        
        int rowsNum=getBillCardPanel().getBillModel().getRowCount();
        double count = 0;
        for(int i=0;i<rowsNum;i++){
            if(getBillCardPanel().getBodyValueAt(i, bodyedit)!=null){
                count=count+ Double.parseDouble(getBillCardPanel().getBillModel().getValueAt(i, bodyedit).toString());    
            }
       }
       BillItem bi = getBillCardPanel().getHeadItem(headedit);
       if(bi!=null){
           getBillCardPanel().getHeadItem(headedit).setValue(String.valueOf(count));
       }
    }
    
    /**
     * �жϵ�ǰ�������Ƿ��Ѵ��ڸ�key, ����Ѵ��ڣ���ȥ��.
     * @param key
     */
    public void removeSortKey(String key) {
        if (m_vhasOrdered.contains(key)) {
            m_vhasOrdered.removeElement(key);
        }
    }
    
    /**
     * �жϵ�ǰ�������Ƿ��Ѵ��ڸ�key, ����Ѵ��ڣ���ȥ��.
     * @param key
     */
    public void removeAllSortKey() {
        if (m_vhasOrdered.size() > 0) {
            m_vhasOrdered.removeAllElements();
        }
    }
    


    @Override
	public void afterEdit(BillEditEvent e) {
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
         
        super.afterEdit(e);
    }
    
    /**
     * ˵������������ж�ѡ���ݴ�������Ĺ��÷���
     *      �� afterEdit() ��д
     * @param refpks ѡ��Ĳ�������
     * @param refcode ���յ��ֶ�  (��Ҫִ�����еı༭��ʽ)
     * @param refpkfield �����������ֶ�
     */
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
            String[] invbasdocformual =getBillCardPanel().getBodyItem(refpkfield).getLoadFormula();//��ȡ��ʾ��ʽ
            getBillCardPanel().execBodyFormulas(i,invbasdocformual);
            String[] codeformual =getBillCardPanel().getBodyItem(refcode).getEditFormulas();       //��ȡ���յı༭��ʽ
            getBillCardPanel().execBodyFormulas(i,codeformual);
        }
    }
    /**
     * ˵������������ж�ѡ���ݴ�������Ĺ��÷���
     *      �� afterEdit() ��д
     * @param refpks ѡ��Ĳ�������
     * @param refcode ���յ��ֶ�  (��Ҫִ�����еı༭��ʽ)
     * @param refpkfield �����������ֶ�
     */
    public void getBodyDataByRef2(String[] refpks,String refpkfield,String refcode){
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
            String[] invbasdocformual =getBillCardPanel().getBodyItem(refpkfield).getLoadFormula();//��ȡ��ʾ��ʽ
            getBillCardPanel().execBodyFormulas(i,invbasdocformual);
            String[] codeformual =getBillCardPanel().getBodyItem(refcode).getEditFormulas();       //��ȡ���յı༭��ʽ
            getBillCardPanel().execBodyFormulas(i,codeformual);
            UFDouble oldprice = new PubTools().getInvPrice(pk_ref,_getDate());
 			getBillCardPanel().setBodyValueAt(oldprice, i, "oldprice");
        }
    }
    
    /**
     * ˵������λ����Ĺ��÷��� 
     * ע�⣺�ڱ��嵥λ����Ҫִ����Ӧ�ı༭��ʽ������������
     * @param oldpk_unit ��λ�޸�ǰ�ĵ�λ �� ������ͨ�� beforeEdit()�õ�
     * @param strpk_unit   �����еĵ�λ�ֶ� (���ӱ��� ��λ���� �ֶβ�һ��)  
     * @param amount  �����еĲ�Ʒ�����ֶ�
     * @param price   �����еĵ����ֶ�
     * @param totalprice  ����ֶ�
     * @author ����  �ɲμ� nc.ui.eh.trade.z0206005.ClientUI.java ��ʹ�÷���
     * 2008-5-6 14:56:17
     * ���� 2009-11-4 11:54:39
     */
//    public void changeDW222(String oldpk_unit,String strpk_unit,String stramount,String strprice,String strtotalprice){
//    	int row=getBillCardPanel().getBillTable().getSelectedRow();
//    	String pk_unit = getBillCardPanel().getBodyValueAt(row,strpk_unit)==null?"":
//                    getBillCardPanel().getBodyValueAt(row,strpk_unit).toString();            //��λ
//    	String pk_invbasdoc=getBillCardPanel().getBodyValueAt(row,"pk_invbasdoc")==null?"":
//            getBillCardPanel().getBodyValueAt(row,"pk_invbasdoc").toString();          //��Ʒ
//    	UFDouble amount = new UFDouble(getBillCardPanel().getBodyValueAt(row,stramount)==null?"0":
//            getBillCardPanel().getBodyValueAt(row,stramount).toString());              //����
//    	UFDouble price = new UFDouble(getBillCardPanel().getBodyValueAt(row,strprice)==null?"0":
//            getBillCardPanel().getBodyValueAt(row,strprice).toString());               //����
//    	
////    	StringBuffer sql = new StringBuffer()
////        .append(" select a.price from eh_invbasdoc a")
////        .append(" where a.pk_invbasdoc = '"+pk_invbasdoc+"' and isnull(a.dr,0)=0 ");
//    	//�°�SQL
//    	String sql = " select a.def3 price from bd_invbasdoc a,bd_invmandoc aa " +
//    			" where a.pk_invbasdoc=aa.pk_invbasdoc and nvl(aa.dr,0)=0 and aa.pk_invmandoc='"+pk_invbasdoc+"' ";
//        IUAPQueryBS  iUAPQueryBS =    (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
//        try {
//		    HashMap hm = new HashMap();
//        	Vector vc = (Vector)iUAPQueryBS.executeQuery(sql.toString(), new VectorProcessor());
//		    if(vc!=null&&vc.size()>0){
//		    	Vector vcc = (Vector)vc.get(0);
//	    		UFDouble oldprice = new UFDouble(vcc.get(0)==null?"0":vcc.get(0).toString());
//	    		hm.put(pk_invbasdoc, oldprice);
//		    }
//		    UFDouble rate = new PubTools().getInvMeasRate(pk_invbasdoc, oldpk_unit, pk_unit);
//		    if(rate.equals(new UFDouble(-1))){
//                this.showErrorMessage("��"+(row+1)+"������û��ά��������λ��ת���ʣ�����!");
//                getBillCardPanel().setBodyValueAt(oldpk_unit, row, strpk_unit);
//                String[] formual=getBillCardPanel().getBodyItem("dw").getLoadFormula();//��ȡ��ʾ��ʽ
//                getBillCardPanel().execBodyFormulas(row,formual); 
//                return;
//		    }else{
//		    	UFDouble price2 = price.div(rate);
//		    	getBillCardPanel().setBodyValueAt(price2, row, strprice);
//		    	if(strtotalprice!=null){
//		    	   getBillCardPanel().setBodyValueAt(price2.multiply(amount),row,strtotalprice);
//		    	}
//		    	String[] formual=getBillCardPanel().getBodyItem(stramount).getEditFormulas();//��ȡ�༭��ʽ
//	            getBillCardPanel().execBodyFormulas(row,formual);
//		    }
//        } catch (BusinessException e1) {
//			e1.printStackTrace();
//		}
//    }
    
  
    /**
     * �õ�һ�����ϵ����е�λת����
     * @param pk_invbasdoc
     * @return
     */
    public HashMap getInvRate(String pk_invbasdoc){
    	HashMap hm = new HashMap();
    	String sql = " select c.pk_measdoc,a.def3,b.mainmeasrate changerate  "+
    				 " from bd_invbasdoc a,bd_convert b,bd_measdoc c "+
    				 " where a.pk_invbasdoc = b.pk_invbasdoc "+
    				 " and b.pk_measdoc = c.pk_measdoc "+
    				 " and a.pk_invbasdoc = (select pk_invbasdoc from bd_invmandoc where pk_invmandoc='"+pk_invbasdoc+"' and nvl(dr,0)=0 ) "+
    				 " and nvl(b.dr,0)=0 and nvl(c.dr,0)=0 and nvl(a.dr,0)=0 "+
    				 " union all  "+
    				 " select c.pk_measdoc,a.def3,1 changerate "+
    				 " from  bd_invbasdoc a,bd_measdoc c "+
    				 " where a.pk_measdoc = c.pk_measdoc "+
    				 " and a.pk_invbasdoc = (select pk_invbasdoc from bd_invmandoc where pk_invmandoc='"+pk_invbasdoc+"' and nvl(dr,0)=0 ) "+
    				 " and nvl(a.dr,0)=0 and nvl(c.dr,0)=0 "; 
        IUAPQueryBS  iUAPQueryBS =    (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
        try {
        	Vector vc = (Vector)iUAPQueryBS.executeQuery(sql.toString(), new VectorProcessor());
		    if(vc!=null&&vc.size()>0){
		    	for(int i=0; i<vc.size(); i++){
		    		Vector vcc = (Vector)vc.get(i);
		    		String pk_measdoc = vcc.get(0)==null?"":vcc.get(0).toString();
		    		UFDouble changerate = new UFDouble(vcc.get(2)==null?"0":vcc.get(2).toString());
		    		hm.put(pk_measdoc, changerate);
		    	}
		    }
         }catch (BusinessException e1) {
 			e1.printStackTrace();
 		}
        return hm;
    }
    
  //�������ϵĻ����ϵ�Ӹ�����������ʱ�䣺2010-01-22 ���ߣ���־Զ
	public void setUA(String field,String pk_invbasdoc,UFDouble number,int row) throws BusinessException{
    	String sql="select mainmeasrate changerate from bd_convert " +
    			" where pk_invbasdoc=(select pk_invbasdoc from bd_invmandoc " +
    			" where pk_invmandoc='"+pk_invbasdoc+"') " +
    			" and nvl(dr,0)=0";
    	IUAPQueryBS  iUAPQueryBS =    (IUAPQueryBS)NCLocator.getInstance().lookup(IUAPQueryBS.class.getName());
    	ArrayList al=(ArrayList) iUAPQueryBS.executeQuery(sql, new MapListProcessor());
    	UFDouble changerate=new UFDouble(-1000000);
    	if(al!=null&&al.size()>0){
	    	for(int i=0;i<al.size();i++){
	    		HashMap hm=(HashMap) al.get(i);
	    		changerate=new UFDouble(hm.get("changerate")==null?"-10000":hm.get("changerate").toString());
	    	}
    	}else{
    		this.showErrorMessage("������û�����û���ϵ�����������á�");
    		return;
    	}
    	/**ԭ��ϵ���� 1�� = 25����1��=1/25�� 
    	 * ������ϵ����ά��Ϊ 1�� = 0.04��  �� ���� = ��������*ϵ�� 
    	 */
    	UFDouble fzamount=number.multiply(changerate);
    	getBillCardPanel().setBodyValueAt(fzamount, row, field);
    	
    }
}
