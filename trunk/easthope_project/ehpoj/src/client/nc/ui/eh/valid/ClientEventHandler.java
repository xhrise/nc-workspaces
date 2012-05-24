package nc.ui.eh.valid;

import java.text.SimpleDateFormat;
import java.util.Date;

import nc.ui.bd.pub.DefaultBDBillCardEventHandle;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.trade.bill.ICardController;
import nc.ui.trade.business.HYPubBO_Client;
import nc.ui.trade.card.BillCardUI;
import nc.vo.eh.valid.EhValidoperateVO;
import nc.vo.eh.valid.MyBillVO;
import nc.vo.ml.NCLangRes4VoTransl;
import nc.vo.pub.SuperVO;
import nc.vo.pub.lang.UFDateTime;

/**
 * 
 * 该类是AbstractMyEventHandler抽象类的实现类， 主要是重载了按钮的执行动作，用户可以对这些动作根据需要进行修改
 * 
 * @author author
 * @version tempProject version
 */

public class ClientEventHandler extends DefaultBDBillCardEventHandle {

	private EhValidoperateVO ehvalid = null;
	
	public ClientEventHandler(BillCardUI billUI, ICardController control) {
		super(billUI, control);
	}
	
	@Override
	protected void onBoLineAdd() throws Exception {
		super.onBoLineAdd();
	}

	@Override
	protected void onBoRefresh() throws Exception {
		try {

			Class c = Class.forName(getUIController().getBillVoName()[1]);
			SuperVO[] vos = getBusiDelegator().queryByCondition(c,
					" 1 = 1 order by createdate asc");
			// 需要先清空
			getBufferData().clear();

			if (vos != null) {
				MyBillVO billVO = new MyBillVO();
				// 加载数据到单据
				billVO.setChildrenVO(vos);
				// 加载数据到缓冲
				if (getBufferData().isVOBufferEmpty()) {
					getBufferData().addVOToBuffer(billVO);
				} else {
					getBufferData().setCurrentVO(billVO);
				}

				// 设置当前行
				getBufferData().setCurrentRow(0);
			} else {
				getBufferData().setCurrentRow(-1);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	protected void onBoEdit() throws Exception {
		super.onBoEdit();
		
		ehvalid = (EhValidoperateVO)getBillCardPanelWrapper().getSelectedBodyVOs()[0];
	}
	
	@Override
	protected void onBoSave() throws Exception {
		
		EhValidoperateVO ehvalidVO = (EhValidoperateVO)getBillCardPanelWrapper().getSelectedBodyVOs()[0];
		
		if(ehvalidVO.getPk_operate() != null) {
		
			if(ehvalid != null) {
				ehvalid.setPk_operate(null);
				ehvalid.setDr(1);
				ehvalid.setPk_lastoperate(getBillUI().getEnvironment().getUser().getPrimaryKey());
				HYPubBO_Client.insert(ehvalid);
				ehvalid = null;
			}
			
			ehvalidVO.setPk_lastoperate(getBillUI().getEnvironment().getUser().getPrimaryKey());
			
			HYPubBO_Client.update(ehvalidVO);
		} else {
			
			ehvalidVO.setCreatedate(new UFDateTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
			ehvalidVO.setPk_lastoperate(getBillUI().getEnvironment().getUser().getPrimaryKey());
			
			HYPubBO_Client.insert(ehvalidVO);
		}
		// 设置保存后状态
		setSaveOperateState();
		
		this.onBoRefresh();
	}
	
	
	@Override
	protected void onBoDelete() throws Exception {
		
		EhValidoperateVO[] ehvalidVO = (EhValidoperateVO[])getBillCardPanelWrapper().getSelectedBodyVOs();
		
		if(ehvalidVO == null || ehvalidVO.length == 0)
			throw new Exception("请选择一条记录！");
		
		if(ehvalidVO[0].getOpeatefield() != null && ehvalidVO[0].getOpeatefield().booleanValue())
			throw new Exception ("该按钮已经被启用，无法被删除");
		
		if(ehvalidVO[0].getIsenable() != null && ehvalidVO[0].getIsenable().booleanValue())
			throw new Exception ("该按钮已经被启用，无法被删除");
		
		if(MessageDialog.showOkCancelDlg(getBillUI(), NCLangRes4VoTransl.getNCLangRes().getStrByID("10082202", "UPT10082202-000007"), NCLangRes4VoTransl.getNCLangRes().getStrByID("10082202", "UPT10082202-000008"), 2) != 1)
            return;
		
		ehvalidVO[0].setDr(1);
		HYPubBO_Client.update(ehvalidVO[0]);
		
		
		onBoRefresh();
	}
	
	

}