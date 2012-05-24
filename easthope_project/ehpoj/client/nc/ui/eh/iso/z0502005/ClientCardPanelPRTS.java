/*
 * Created on 2006-10-8
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package nc.ui.eh.iso.z0502005;

import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIRefPane;
import nc.ui.pub.bill.BillCardPanel;
import nc.ui.pub.bill.BillItem;
import nc.ui.wfengine.PfWorkflowBO_Client;
import nc.vo.pub.msg.MessageVO;
import nc.vo.pub.workflownote.WorkflownoteVO;
import nc.vo.wfengine.pub.WFTask;

/**
 * ����:��ⱨ�浥��ҳǩ�Ĵ�ӡģ��
 *
 */
public class ClientCardPanelPRTS implements nc.ui.pub.print.IDataSource {
	private String m_sModuleName= "";
	private WorkflownoteVO[] m_noteVOs = null;
	
	public ClientCardPanelPRTS(String m_sModuleName, BillCardPanel billcardpanel,String pk_code) {
		super();
		this.m_billcardpanel = billcardpanel;
		this.m_sModuleName = m_sModuleName;
		if (pk_code!=null&&billcardpanel.getHeadItem(pk_code).getValueObject()!=null)
			try {
//				m_noteVOs = PfWorkflowBO_Client.queryCheckNote(billcardpanel.getHeadItem(pk_code).getValueObject().toString());
			} catch (Exception e) {
				System.out.println("��ѯ������ʷ��¼ʧ�ܣ�");
			}
	}
	/**
	 *
	 * �õ����е���������ʽ����
	 * Ҳ���Ƿ������ж����������ı��ʽ
	 *
	 */
	public String[] getAllDataItemExpress() {
		int headCount = 0;
		int bodyCount = 0;
		int tailCount = 0;
		if (m_billcardpanel.getHeadItems() != null){
			headCount = m_billcardpanel.getHeadItems().length;
		}
		if (m_billcardpanel.getBillModel()!=null && m_billcardpanel.getBodyItems() != null){
			bodyCount = m_billcardpanel.getBillModel().getBodyItems().length;
		}
		if (m_billcardpanel.getTailItems() != null){
			tailCount = m_billcardpanel.getTailItems().length;
		}
		int count= headCount + bodyCount + tailCount ;
		String[] expfields = new String[count];
		try{
			for (int i = 0; i < headCount; i++){
				expfields[i] = "h_"+m_billcardpanel.getHeadItems()[i].getKey();
			}
			for (int j = 0; j < bodyCount ; j++){
				expfields[j+headCount] = m_billcardpanel.getBillModel().getBodyItems()[j].getKey();
			}
			for (int k = 0; k < tailCount ; k++){
				expfields[k+headCount+bodyCount] = "t_" + m_billcardpanel.getTailItems()[k].getKey();
			}
		}catch (Throwable e) {
				e.printStackTrace();
				System.out.print("error at  getAllDataItemExpress()");
		}
		return expfields;
	}
	/**
	 *
	 * �õ����е���������ʽ����
	 * Ҳ���Ƿ������ж����������Ĵ�ӡ�ֶ���
	 *
	 */
	public String[] getAllDataItemNames() {
		int headCount = 0;
		int bodyCount = 0;
		int tailCount = 0;
		if (m_billcardpanel.getHeadItems() != null){
			headCount = m_billcardpanel.getHeadItems().length;
		}
		if (m_billcardpanel.getBillModel()!=null&&m_billcardpanel.getBodyItems() != null){
			bodyCount = m_billcardpanel.getBillModel().getBodyItems().length;
		}
		if (m_billcardpanel.getTailItems() != null){
			tailCount = m_billcardpanel.getTailItems().length;
		}
		int count = headCount + bodyCount + tailCount;
		String[] namefields = new String[count];
		try{
			for (int i = 0; i < headCount; i++){
				namefields[i] = m_billcardpanel.getHeadItems()[i].getName();
			}
			for (int j = 0; j < bodyCount ; j++){
				namefields[j + headCount] = m_billcardpanel.getBillModel().getBodyItems()[j].getName();
			}
			for (int k = 0 ; k < tailCount ; k++){
				namefields[k + headCount+bodyCount] = m_billcardpanel.getTailItems()[k].getName();
			}
		}catch (Throwable e) {
			e.printStackTrace();
			System.out.print("error at  getAllDataItemNames()");
		}
		return namefields;
	}
	/**
	 *
	 * ������������������飬���������ֻ��Ϊ 1 ���� 2
	 * ���� null : 		û������
	 * ���� 1 :			��������
	 * ���� 2 :			˫������
	 *
	 */
	public String[] getDependentItemExpressByExpress(String itemName) {
		return null;
	}
	/*
	 * �������е��������Ӧ������
	 * ������ �����������
	 * ���أ� �������Ӧ�����ݣ�ֻ��Ϊ String[]��

	 */
	public String[] getItemValuesByExpress(String itemExpress) {

        
		int headCount = 0;
		int bodyCount = 0;
		int tailCount = 0;
		if (m_billcardpanel.getHeadItems() != null){
			headCount = m_billcardpanel.getHeadItems().length;
		}
		if (m_billcardpanel.getBillModel()!=null&&m_billcardpanel.getBillModel().getBodyItems() != null){
			bodyCount = m_billcardpanel.getBillModel().getBodyItems().length;
		}
		if (m_billcardpanel.getTailItems() != null){
			tailCount = m_billcardpanel.getTailItems().length;
		}
		int rowCount  = m_billcardpanel.getBillModel("eh_stock_checkreport_c").getRowCount()+m_billcardpanel.getBillModel("eh_stock_checkreport").getRowCount();
		try {
			//��ͷ
			if(itemExpress.startsWith("h_")){
				BillItem item = m_billcardpanel.getHeadItem(itemExpress.substring(2));
				if(item==null) return null;
				if (item.getKey().equals(itemExpress.substring(2))) {
					//UICheckbox
					if(item.getDataType()==4){
						if(item.getValueObject()==null){
							return new String[] {nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000165")/*@res "��"*/};
						}else{
							if(item.getValueObject().equals("false")){
								return new String[] {nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000165")/*@res "��"*/};
							}else{
								return new String[] {nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000164")/*@res "��"*/};
							}
						}
					}
					//UICombox
					else if(item.getDataType()==6){
						String sc = ((UIComboBox)item.getComponent()).getSelectedItem().toString();
						return new String[] { sc };
					}
					//UIRefPane
					else if (item.getDataType() == 5){
	  				    String sr = ((UIRefPane)item.getComponent()).isReturnCode() ? ((UIRefPane)item.getComponent()).getRefCode() : ((UIRefPane)item.getComponent()).getRefName();
		  				return new String[] { sr };
					}
					else if (item.getDataType()==9){
	  				    String sr = item.getValueObject().toString();
		  				return new String[] { sr };
					}
					//�����ı�
					else{
						String wb = ((UIRefPane)item.getComponent()).getText();
					  	//cf add �������ֲ�������С��λ��format
					   	try{
						   	if(item.getDataType() == 2){
								UIRefPane item_h=(UIRefPane)item.getComponent();
								nc.vo.pub.lang.UFDouble value=new nc.vo.pub.lang.UFDouble(wb);
								value=value.setScale(item_h.getNumPoint(),4);
								wb=value.toString();
							}
						}catch(Exception e){
							System.out.println("�������ֲ�������С��λ��format����:"+e);
					  	}
					   	//cf add
					 	return new String[] { wb };
					}
				}

			}
			//��β
			else if(itemExpress.startsWith("t_")){
				BillItem item = m_billcardpanel.getTailItem(itemExpress.substring(2));
				if(item==null) return null;
				if (item.getKey().equals(itemExpress.substring(2))) {
					//UICheckbox
					if(item.getDataType()==4){
						if(item.getValueObject()==null){
							return new String[] {nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000165")/*@res "��"*/};
						}else{
							if(item.getValueObject().equals("false")){
								return new String[] {nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000165")/*@res "��"*/};
							}else{
								return new String[] {nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000164")/*@res "��"*/};
							}
						}
					}
					//UICombox
					else if(item.getDataType()==6){
						String sc = ((UIComboBox)item.getComponent()).getSelectedItem().toString();
						return new String[] { sc };
					}
					//UIRefPane
					else if (item.getDataType() == 5){
	  				    String sr = ((UIRefPane)item.getComponent()).isReturnCode() ? ((UIRefPane)item.getComponent()).getRefCode() : ((UIRefPane)item.getComponent()).getRefName();
		  				return new String[] { sr };
					}
					//�����ı�
					else{
						String wb = ((UIRefPane)item.getComponent()).getText();
					    return new String[] { wb };
					}
				}
			}
//			�������
//			else if(itemExpress.startsWith("audit_")){
//			    if (m_noteVOs == null || m_noteVOs.length < 1)
//				      return null;
//			    rowCount=m_noteVOs.length;
//				if (rowCount>0){
//					String[] rslt=new String[rowCount];
//					for (int i=0;i<rowCount;i++){
//						if (itemExpress.equals("audit_senderName"))
//							rslt[i]=m_noteVOs[i].getSenderName();
//						else if (itemExpress.equals("audit_checkName"))
//							rslt[i]=m_noteVOs[i].getCheckName();
//						else if (itemExpress.equals("audit_dealdate"))
//							rslt[i]=m_noteVOs[i].getDealdate().toString();
//						else if (itemExpress.equals("audit_checknote"))
//							rslt[i]=m_noteVOs[i].getChecknote();
//						else if (itemExpress.equals("audit_approvestatus")){
//							int status = m_noteVOs[i].getApprovestatus().intValue();
//					          if (status == WFTask.STATUS_FINISHED) {
//					          	rslt[i] = NCLangRes.getInstance().getStrByID("102220", "UPP102220-000152")/*@res "������"*/;
//					          } else if (status == WFTask.STATUS_Inefficient) {
//					          	rslt[i] = NCLangRes.getInstance().getStrByID("common", "UC001-0000005")/*@res "����"*/;
//					          } else {
//					          	rslt[i] = "";
//					          }
//						}else if (itemExpress.equals("audit_approveresult")){
//							Object result = m_noteVOs[i].getApproveresult();
//							rslt[i] = WFTask.resolveApproveResult(result);
//						}else if (itemExpress.equals("audit_messagenote"))
//							rslt[i] = MessageVO.getMessageNoteAfterI18N(m_noteVOs[i].getMessagenote());
//				    }
//					return rslt;
//				}
//			}
			//����
			//eh_stock_checkreportΪ��ⱨ�浥�����һ��ҳǩ
			else{
                bodyCount = m_billcardpanel.getBillModel("eh_stock_checkreport").getBodyItems().length;
				for (int i = 0; i < bodyCount; i++){
                    BillItem item = m_billcardpanel.getBillModel("eh_stock_checkreport").getBodyItems()[i];
                    if(item==null) return null;
                    String[] rslt = new String[rowCount];
                    if (item.getKey().equals(itemExpress)) {
                        //UICheckbox
                        if(item.getDataType()==4){
                            for (int j = 0; j < rowCount; j++) {
                                if(m_billcardpanel.getBillModel("eh_stock_checkreport").getValueAt(j,  item.getKey())==null){
                                    rslt[j] = nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000165")/*@res "��"*/;
                                }else{
                                    if(m_billcardpanel.getBillModel("eh_stock_checkreport").getValueAt(j,  item.getKey()).toString().equals("false")){
                                        rslt[j] = nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000165");
                                    }else{
                                        rslt[j] = nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000164")/*@res "��"*/;
                                    }
                                }
                            }
                        }
                        //UIRefPane or UICombox
                        else{
                            for (int j = 0; j < rowCount; j++) {
                                rslt[j] = m_billcardpanel.getBillModel("eh_stock_checkreport").getValueAt(j,  item.getKey())==null?" ":m_billcardpanel.getBillModel("eh_stock_checkreport").getValueAt(j,  item.getKey()).toString();
                                //rslt[j] = m_billcardpanel.getBodyValueAt(j, item.getKey()) == null ? "" : m_billcardpanel.getBodyValueAt(j, item.getKey()).toString();
                            }
                        }
                        return rslt;
                    }
                }
                
                //XXXXXXXNewyear 
                //eh_stock_checkreport_cΪ��ⱨ�浥����ڶ���ҳǩ
                bodyCount = m_billcardpanel.getBillModel("eh_stock_checkreport_c").getBodyItems().length;
                for (int i = 0; i < bodyCount; i++){
                    BillItem item = m_billcardpanel.getBillModel("eh_stock_checkreport_c").getBodyItems()[i];
                    if(item==null) return null;
                    String[] rslt = new String[rowCount];
                    if (item.getKey().equals(itemExpress)) {
                        //UICheckbox
                        if(item.getDataType()==4){
                            for (int j = 0; j < rowCount; j++) {
                                if(m_billcardpanel.getBillModel("eh_stock_checkreport_c").getValueAt(j,  item.getKey())==null){
                                   rslt[j] = nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000165")/*@res "��"*/;
                                }else{
                                    if(m_billcardpanel.getBillModel("eh_stock_checkreport_c").getValueAt(j,  item.getKey()).toString().equals("false")){
                                        rslt[j] = nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000165")/*@res "��"*/;
                                    }else{
                                        rslt[j] = nc.ui.ml.NCLangRes.getInstance().getStrByID("uifactory","UPPuifactory-000164")/*@res "��"*/;
                                    }
                                }
                            }
                        }
                        //UIRefPane or UICombox
                        else{
                            for (int j = 0; j < rowCount; j++) {
                                rslt[j] = m_billcardpanel.getBillModel("eh_stock_checkreport_c").getValueAt(j,  item.getKey())==null?
                                        " ":m_billcardpanel.getBillModel("eh_stock_checkreport_c").getValueAt(j,  item.getKey()).toString();
                                //rslt[j] = m_billcardpanel.getBodyValueAt(j, item.getKey()) == null ? "" : m_billcardpanel.getBodyValueAt(j, item.getKey()).toString();
                            }
                        }
                        return rslt;
                    }
                }
			}

		} catch (Throwable e) {
			e.printStackTrace();
		    System.out.print("error at getItemValueByExpress()");
		    return null;
		}
		return null;
	}

	/*
	 *  ���ظ�����Դ��Ӧ�Ľڵ����
	 */
	public String getModuleName() {
		return m_sModuleName;
	}
	/*
	 * ���ظ��������Ƿ�Ϊ������
	 * ������ɲ������㣻��������ֻ��Ϊ�ַ�������
	 * �硰������Ϊ�������������롱Ϊ��������
	 */
	public boolean isNumber(String itemExpress) {
		/**�������� */
		//int headCount = 0;
		//int bodyCount = 0;
		//int tailCount = 0;
		//if (m_billcardpanel.getHeadItems() != null){
			//headCount = m_billcardpanel.getHeadItems().length;
		//}
		//if (m_billcardpanel.getBodyItems() != null){
			//bodyCount =m_billcardpanel.getBillModel().getBodyItems().length;
		//}
		//if (m_billcardpanel.getTailItems() != null){
			//tailCount = m_billcardpanel.getTailItems().length;
		//}

		try {
			if (itemExpress.startsWith("h_")){
				BillItem item = m_billcardpanel.getHeadItem(itemExpress.substring(2));
				if (item==null) return false;
				if (item.getDataType() == 1 || item.getDataType() == 2){
					return true;
				}
			}
			else if (itemExpress.startsWith("t_")){
				BillItem item = m_billcardpanel.getTailItem(itemExpress.substring(2));
				if (item==null) return false;
				if (item.getDataType() == 1 || item.getDataType() == 2){
					return true;
				}
			}
			else {
				//��Ҫ��֤billModel��Ϊ��
				if(m_billcardpanel.getBillModel()==null)
					return false;
				BillItem[] items=m_billcardpanel.getBillModel().getBodyItems();
				BillItem item=null;
				for(int i=0;i<items.length;i++){
					if (items[i].getKey().equals(itemExpress)){
						item=items[i];
						break;
					}
				}
				if (item==null) return false;
				if (item ==null){
					return false;
				}
				else if (item.getDataType() == 1 || item.getDataType() == 2){
					return true;
				}
			}
		} catch (Throwable e) {
			e.printStackTrace();
			System.out.print("error at  isNumber()");
			return false;
		}
		return false;
	}


		private BillCardPanel m_billcardpanel = null;
	}