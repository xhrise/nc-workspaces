package nc.ui.uap.bd.corp;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import nc.bs.framework.common.NCLocator;
import nc.bs.util.SleepTime;
import nc.bs.util.Uriread;
import nc.itf.yto.util.IFilePost;
import nc.itf.yto.util.IGener;
import nc.itf.yto.util.IReadmsg;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.uif2.actions.DeleteAction;
import nc.vo.bd.CorpVO;
import nc.vo.bd.MultiLangTrans;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.SuperVO;

public class CorpDeleteAction extends DeleteAction {

	private List<nc.vo.yto.business.CorpVO> CorpDel = new ArrayList<nc.vo.yto.business.CorpVO>();
	private List<nc.vo.yto.business.CorpVO> CorpDel2 = new ArrayList<nc.vo.yto.business.CorpVO>();
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public void doAction(ActionEvent e) throws Exception {
		if (MessageDialog.showOkCancelDlg(getToftPanel(), NCLangRes4VoTransl
				.getNCLangRes()
				.getStrByID("10082202", "UPT10082202-000007"/* 确定删除 */),
				NCLangRes4VoTransl.getNCLangRes().getStrByID("10082202",
						"UPT10082202-000008")/* 您确定要删除所选数据吗 */,
				MessageDialog.ID_CANCEL) != MessageDialog.ID_OK)
			return;
		
//		 删除公司目录信息时 同步信息至中间表 add by river for 2011-09-13
		CorpVO value = (CorpVO) this.getModel().getSelectedData();
		
		super.doAction(e);

		getToftPanel().showHintMessage(MultiLangTrans.getTransStr("MD6", new String[]{""}));
		
		CorpVO corpVO = (CorpVO) HYPubBO_Client.queryByPrimaryKey(CorpVO.class, value.getPk_corp());
		if(corpVO == null || Integer.valueOf(corpVO.getAttributeValue("dr").toString()) == 1) {
			IReadmsg msg = (IReadmsg) NCLocator.getInstance().lookup(IReadmsg.class.getName());
			IGener gener = (IGener) NCLocator.getInstance().lookup(IGener.class.getName());
			IFilePost filepost = (IFilePost) NCLocator.getInstance().lookup(IFilePost.class.getName());
		
			nc.vo.yto.business.CorpVO corpvo = ((nc.vo.yto.business.CorpVO[])msg.getGeneralVOs(nc.vo.yto.business.CorpVO.class, " pk_corp = '"+((CorpVO)value).getAttributeValue("pk_corp")+"'"))[0];
			
			String retStr = filepost.postFile(Uriread.uriPath() , 
					gener.generateXml2(corpvo, "RequestCorp", "corp", "del"));
			
			
			
			String[] strs = retStr.split("<success>");
			String retMsg = "";
			if (strs.length > 1)
				retMsg = strs[1].substring(0, strs[1].indexOf("<"));
			
			if (retMsg.equals("false") || strs.length <= 1) {
				CorpDel.add(corpvo);
				new Thread() {
					public void run() {
						IGener gener = (IGener) NCLocator.getInstance().lookup(IGener.class.getName());
						IFilePost filepost = (IFilePost) NCLocator.getInstance().lookup(IFilePost.class.getName());
					
						try {
							if(true) {
								this.sleep(SleepTime.Time);
							
								for(nc.vo.yto.business.CorpVO corp : CorpDel) {
									String retStr = filepost.postFile(Uriread.uriPath() , 
											gener.generateXml2(corp, "RequestCorp", "corp", "del"));
									
									String[] strs = retStr.split("<success>");
									String retMsg = "";
									if (strs.length > 1)
										retMsg = strs[1].substring(0, strs[1].indexOf("<"));
								
									if(retMsg.equals("false") || strs.length <= 1)
										CorpDel2.add(corp);
								}
								
								CorpDel = CorpDel2;
								
								CorpDel2 = new ArrayList<nc.vo.yto.business.CorpVO>();
								
//								if(CorpDel.size() == 0)
//									break;
								
								
							}
							System.out.println("<<<<<<  公司档案删除线程停止！ >>>>>>");
							System.out.println("<<<<<<  线程号: " + this.getId() + " >>>>>>");
							this.stop();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
				}.start();
			}
		}
		
	}

	@Override
	protected boolean isActionEnable() {
		 return getModel().getContext().getPk_org().equals("0001")&&super.isActionEnable();
	}

	
}
