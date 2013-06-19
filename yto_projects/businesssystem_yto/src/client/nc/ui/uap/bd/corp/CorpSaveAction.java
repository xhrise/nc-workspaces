package nc.ui.uap.bd.corp;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.tree.DefaultMutableTreeNode;

import nc.bs.framework.common.NCLocator;
import nc.bs.generate.Gener;
import nc.bs.logging.Logger;
import nc.bs.uif2.BusinessExceptionAdapter;
import nc.bs.util.SleepTime;
import nc.bs.util.Uriread;
import nc.itf.yto.util.IFilePost;
import nc.itf.yto.util.IGener;
import nc.itf.yto.util.IReadmsg;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.uap.bd.BdAppModelDataManager;
import nc.ui.uif2.UIState;
import nc.ui.uif2.actions.SaveAction;
import nc.ui.uif2.model.HierachicalDataAppModel;
import nc.vo.bd.CorpVO;
import nc.vo.bd.MultiLangTrans;
import nc.vo.hi.hi_301.GeneralVO;

public class CorpSaveAction extends SaveAction {
	
	private List<nc.vo.yto.business.CorpVO> CorpUpdate = new ArrayList<nc.vo.yto.business.CorpVO>();
	private List<nc.vo.yto.business.CorpVO> CorpUpdate2 = new ArrayList<nc.vo.yto.business.CorpVO>();
	
	private List<nc.vo.yto.business.CorpVO> CorpAdd = new ArrayList<nc.vo.yto.business.CorpVO>();
	private List<nc.vo.yto.business.CorpVO> CorpAdd2 = new ArrayList<nc.vo.yto.business.CorpVO>();

	/**
	 * @author yaonb
	 */
	private static final long serialVersionUID = 1L;
    private BdAppModelDataManager dataManager;
    
	@Override
	public void doAction(ActionEvent e) throws Exception {
		IGener gener = (IGener) NCLocator.getInstance().lookup(IGener.class.getName());
		IFilePost filepost = (IFilePost) NCLocator.getInstance().lookup(IFilePost.class.getName());
	
		Object value = getEditor().getValue();
		validate(value);
		if(getModel().getUiState()==UIState.ADD)
		{
			Object corpVo=getModel().add(value);
			DefaultMutableTreeNode newNode = ((HierachicalDataAppModel)getModel()).findNodeByBusinessObject(corpVo);//ʹ����ѡ�н���Ϊ��ǰ�������
			((HierachicalDataAppModel)getModel()).setSelectedNode(newNode);
			getToftPanel().showHintMessage(MultiLangTrans.getTransStr("MD1", new String[]{""}));
			
			// ��˾Ŀ¼ ���ӹ�˾��Ϣʱͬ����Ϣ���м�� add by river for 2011-09-13
			IReadmsg msg = (IReadmsg) NCLocator.getInstance().lookup(IReadmsg.class.getName());
			nc.vo.yto.business.CorpVO corpvo = ((nc.vo.yto.business.CorpVO[])msg.getGeneralVOs(nc.vo.yto.business.CorpVO.class, " unitcode = '"+((CorpVO)corpVo).getAttributeValue("unitcode")+"'"))[0];

			String retStr = filepost.postFile(Uriread.uriPath() , 
					gener.generateXml2(corpvo, "RequestCorp", "corp", "add"));
		
			String[] strs = retStr.split("<success>");
			String retMsg = "";
			if (strs.length > 1)
				retMsg = strs[1].substring(0, strs[1].indexOf("<"));
			
			if (retMsg.equals("false") || strs.length <= 1) {
				CorpAdd.add(corpvo);
				Thread th1 = new Thread() {
					public void run() {
						IGener gener = (IGener) NCLocator.getInstance().lookup(IGener.class.getName());
						IFilePost filepost = (IFilePost) NCLocator.getInstance().lookup(IFilePost.class.getName());
					
						try {
							if(true) {
								this.sleep(SleepTime.Time);
							
								for(nc.vo.yto.business.CorpVO corp : CorpAdd) {
									String retStr = filepost.postFile(Uriread.uriPath() , 
											gener.generateXml2(corp, "RequestCorp", "corp", "update"));
									
									String[] strs = retStr.split("<success>");
									String retMsg = "";
									if (strs.length > 1)
										retMsg = strs[1].substring(0, strs[1].indexOf("<"));
								
									if(retMsg.equals("false") || strs.length <= 1)
										CorpAdd2.add(corp);
								}
								
								CorpAdd = CorpAdd2;
								
								CorpAdd2 = new ArrayList<nc.vo.yto.business.CorpVO>();
								
//								if(CorpAdd.size() == 0)
//									break;
								
								
							}
							System.out.println("<<<<<<  ��˾���������߳�ֹͣ�� >>>>>>");
							System.out.println("<<<<<<  �̺߳�: " + this.getId() + " >>>>>>");
							this.stop();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				};
				
				th1.start();
			}
			
			//System.out.println(retStr);
			
		}
		if(getModel().getUiState()==UIState.EDIT){
			
			if (nc.ui.pub.beans.MessageDialog.showYesNoDlg(getToftPanel(), null,
					nc.vo.bd.MultiLangTrans.getTransStr("MT1", new String[] {
							nc.ui.ml.NCLangRes.getInstance().getStrByID("100406",
									"UPP100406-000035")/* @res "����" */,
							nc.ui.ml.NCLangRes.getInstance().getStrByID("100406",
									"UPP100406-000036") /* @res "�����¼" */})) // ȷ��Ҫ���ݱ����¼��
			== nc.ui.pub.beans.MessageDialog.ID_YES) {
				((CorpVO) value).setBackup(true);
			} else {
				((CorpVO) value).setBackup(false);
			}
			getModel().update(value);
			doWhenSealChange(value);
			
//			 ��˾Ŀ¼ �޸Ĺ�˾��Ϣʱͬ����Ϣ���м�� add by river for 2011-09-13
			IReadmsg msg = (IReadmsg) NCLocator.getInstance().lookup(IReadmsg.class.getName());
			nc.vo.yto.business.CorpVO corpvo = ((nc.vo.yto.business.CorpVO[])msg.getGeneralVOs(nc.vo.yto.business.CorpVO.class, " pk_corp = '"+((CorpVO)value).getAttributeValue("pk_corp")+"'"))[0];
			
			String retStr = filepost.postFile(Uriread.uriPath() , 
					gener.generateXml2(corpvo, "RequestCorp", "corp", "update"));
		
			String[] strs = retStr.split("<success>");
			String retMsg = "";
			if (strs.length > 1)
				retMsg = strs[1].substring(0, strs[1].indexOf("<"));
			
			if (retMsg.equals("false") || strs.length <= 1) {
				CorpUpdate.add(corpvo);
				Thread th2 = new Thread() {
					public void run() {
						IGener gener = (IGener) NCLocator.getInstance().lookup(IGener.class.getName());
						IFilePost filepost = (IFilePost) NCLocator.getInstance().lookup(IFilePost.class.getName());
					
						try {
							if(true) {
								this.sleep(SleepTime.Time);
							
								for(nc.vo.yto.business.CorpVO corp : CorpUpdate) {
									String retStr = filepost.postFile(Uriread.uriPath() , 
											gener.generateXml2(corp, "RequestCorp", "corp", "add"));
									
									String[] strs = retStr.split("<success>");
									String retMsg = "";
									if (strs.length > 1)
										retMsg = strs[1].substring(0, strs[1].indexOf("<"));
								
									if(retMsg.equals("false") || strs.length <= 1)
										CorpUpdate2.add(corp);
								}
								
								CorpUpdate = CorpUpdate2;
								
								CorpUpdate2 = new ArrayList<nc.vo.yto.business.CorpVO>();
								
//								if(CorpUpdate.size() == 0)
//									break;
								
								
							}
							System.out.println("<<<<<<  ��˾�����޸��߳�ֹͣ�� >>>>>>");
							System.out.println("<<<<<<  �̺߳�: " + this.getId() + " >>>>>>");
							this.stop();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				};
				
				th2.start();
			}
		}
		getModel().setUiState(UIState.NOT_EDIT);
		
	}

	/**
	 * ����޸ķ�����ԣ��ж��뵱ǰ��ʾ���״̬�Ƿ����Ӧ���������Ӧ��ӵ�ǰ����ɾ����㵫�������̨ɾ��
	 * @param value
	 */
	private void doWhenSealChange(Object value) {
		if(value instanceof CorpVO)
		{
			CorpVO vo = (CorpVO) value;
			if (!dataManager.isShowSealdata()&&vo.getIsseal().booleanValue() != dataManager.isShowSealdata()) {
				MessageDialog.showWarningDlg(getToftPanel(), "hint",
						MultiLangTrans.getTransStr("MD1", new String[] { "" })
								+ "��"+NCLangRes.getInstance().getStrByID("100406",
										"UPP100406-000073"));
				((CorpAppModel) getModel()).deleteNodeFromTree();
				return;
			}
			getToftPanel().showHintMessage(
					MultiLangTrans.getTransStr("MD1", new String[] { "" }));
		}
	}

	public void setDataManager(BdAppModelDataManager dataManager) {
		this.dataManager = dataManager;
	}

	public BdAppModelDataManager getDataManager() {
		return dataManager;
	}

	 protected void validate(Object value)
	    {
	        try {
				((CorpEditor)getEditor()).getBillCardPanel().dataNotNullValidate();
			} catch (nc.vo.pub.ValidationException e) {
				Logger.error(e.getMessage());
				throw new BusinessExceptionAdapter(e);
			}
	        super.validate(value);
	    }

}
