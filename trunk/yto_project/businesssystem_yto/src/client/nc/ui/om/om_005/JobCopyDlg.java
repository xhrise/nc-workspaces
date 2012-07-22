package nc.ui.om.om_005;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import nc.itf.hr.jf.ORGDelegator;
import nc.ui.hi.pub.BorderDialog;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIPanel;
import nc.vo.hr.tools.pub.StringUtils;
import nc.vo.om.om_005.JobCopyDeptRenameVO;
import nc.vo.om.om_005.JobLiteVO;
import nc.vo.om.om_005.JobVO;
import nc.vo.om.om_011.OrgNodeVO;

/**
 * 岗位信息节点
 * 岗位复制对话框
 * @author wangxing
 *
 */
public class JobCopyDlg extends BorderDialog implements ActionListener {

	/**
	 * 默认序列化ID
	 */
	private static final long serialVersionUID = 1L;

	// 当前显示的状态常量
	// 无状态
	protected static final int JOBCOPY_STATUS_NONE = 0;
	// 选择岗位状态
	protected static final int JOBCOPY_STATUS_CHOOSEJOB = 100;
	// 重命名岗位状态
	protected static final int JOBCOPY_STATUS_RENAMEJOB = 200;
	// 重编码岗位状态
	protected static final int JOBCOPY_STATUS_RECODEJOB = 300;
	
	// 父入口类
	private JobInfoUI parentUI = null;
	
	// 岗位选择Panel
	private JobChoosePanel panel_choosejob = null;
	
	// 岗位重命名Panel
	private JobRenamePanel panel_renamejob = null;
	
	// 岗位重编码Panel
	private JobRecodePanel panel_recodejob = null;
	
	// 当前显示的切换类型
	private int currentSwitchType = JOBCOPY_STATUS_NONE;
	
	// 中央主体Panel
	private UIPanel centertPanel = null;
	
	// 中央功能切换Panel
	private UIPanel switchPanel = null;
	
	// 中央功能切换Panel的布局管理器
	private CardLayout switchPanelCardLayout = null;
	
	// 底部显示按钮的Panel
	private UIPanel buttonsPanel = null;
	
	// 按钮，确定
	private UIButton button_ok = null;
	
	// 按钮，取消
	private UIButton button_cancel = null;
	
	// 按钮，下一步
	private UIButton button_next = null;
	
	// 按钮，上一步
	private UIButton button_prev = null;
	
	/**
	 * 构造函数
	 * @param parent
	 */
	public JobCopyDlg(JobInfoUI parent) {
		super(parent);
		
		parentUI = parent;
		
		init();
	}
	
	/**
	 * 初始化监听
	 */
	private void initListeners(){
		getButton_ok().addActionListener(this);
		getButton_cancel().addActionListener(this);
		getButton_next().addActionListener(this);
		getButton_prev().addActionListener(this);
		
	}
	
	/**
	 * 得到当前对话框的显示类型
	 * @return
	 */
	public void setCurrentSwitchStatus(int showType){
		// 是否自动编码
		boolean isAutoCode = parentUI.isAutoJobCode();
//		isAutoCode = false;
		
		// 分情况设置按钮的状态
		switch(showType){
		
			// 岗位选择状态
			case JOBCOPY_STATUS_CHOOSEJOB:{
				getSwitchPanelCardLayout().show(getSwitchPanel(), getPanel_choosejob().getName());
				currentSwitchType = JOBCOPY_STATUS_CHOOSEJOB;
				
				// 设置按钮显示
				getButton_ok().setVisible(false);
				getButton_cancel().setVisible(true);
				getButton_prev().setVisible(false);
				getButton_next().setVisible(true);
				break;
			}//end case
			
			// 岗位改名状态
			case JOBCOPY_STATUS_RENAMEJOB:{
				getSwitchPanelCardLayout().show(getSwitchPanel(), getPanel_renamejob().getName());
				currentSwitchType = JOBCOPY_STATUS_RENAMEJOB;
				
				// 设置按钮显示
				getButton_ok().setVisible(isAutoCode);
				getButton_cancel().setVisible(true);
				getButton_prev().setVisible(true);
				getButton_next().setVisible(!isAutoCode);
				break;
			}//end case
			
			// 岗位改编码状态
			case JOBCOPY_STATUS_RECODEJOB:{
				getSwitchPanelCardLayout().show(getSwitchPanel(), getPanel_recodejob().getName());
				currentSwitchType = JOBCOPY_STATUS_RECODEJOB;
				
				// 设置按钮显示
				getButton_ok().setVisible(true);
				getButton_cancel().setVisible(true);
				getButton_prev().setVisible(true);
				getButton_next().setVisible(false);
				
				break;
			}//end case
			
			// 默认处理
			default:{
//				currentSwitchType = JOBCOPY_STATUS_NONE;
			}//end default
		
		}//end switch
	}
	
	/**
	 * 得到当前对话框的显示类型
	 * @return
	 */
	public int getCurrentSwitchStatus(){
		return currentSwitchType;
	}
	
	/**
	 * 初始化方法
	 */
	private void init(){
		this.setName("JobCopyDlg");
//		UPT60050704-000217=岗位复制
		this.setTitle(NCLangRes.getInstance().getStrByID("60050704", "UPT60050704-000217")/* @res "岗位复制" */);
		this.setModal(true);
		
		this.setContentPane(getCentertPanel());
		this.setSize(650, 550);
		this.setResizable(false);
		// 设置显示类型
		setCurrentSwitchStatus(JOBCOPY_STATUS_CHOOSEJOB);
		
		// 初始化监听
		initListeners();
	}
	
	/**
	 * 得到父主程序入口类
	 * @return
	 */
	protected JobInfoUI getParentUI() {
		return parentUI;
	}

	/**
	 * 得到岗位选择Panel
	 * @return
	 */
	protected JobChoosePanel getPanel_choosejob() {
		if(panel_choosejob==null){
			panel_choosejob = new JobChoosePanel(this);
			panel_choosejob.setName("panel_choosejob");
		}
		return panel_choosejob;
	}

	/**
	 * 得到岗位重命名Panel
	 * @return
	 */
	protected JobRenamePanel getPanel_renamejob() {
		if(panel_renamejob==null){
			panel_renamejob = new JobRenamePanel(this);
			panel_renamejob.setName("panel_renamejob");
		}
		return panel_renamejob;
	}

	/**
	 * 得到岗位重编码Panel
	 * @return
	 */
	protected JobRecodePanel getPanel_recodejob() {
		if(panel_recodejob==null){
			panel_recodejob = new JobRecodePanel(this);
			panel_recodejob.setName("panel_recodejob");
		}
		return panel_recodejob;
	}

	/**
	 * 得到中部主体Panel
	 * @return
	 */
	private UIPanel getCentertPanel() {
		if(centertPanel==null){
			centertPanel = new UIPanel();
			centertPanel.setName("centertPanel");
			
			centertPanel.setLayout(new BorderLayout());
			centertPanel.add(getSwitchPanel(), BorderLayout.CENTER);
			centertPanel.add(getButtonsPanel(), BorderLayout.SOUTH);
		}
		return centertPanel;
	}

	/**
	 * 得到主体切换Panel
	 * @return
	 */
	private UIPanel getSwitchPanel() {
		if(switchPanel==null){
			switchPanel = new UIPanel();
			switchPanel.setName("switchPanel");
			
			switchPanel.setLayout(getSwitchPanelCardLayout());
			
			switchPanel.add(getPanel_choosejob(), getPanel_choosejob().getName());
			switchPanel.add(getPanel_renamejob(), getPanel_renamejob().getName());
			switchPanel.add(getPanel_recodejob(), getPanel_recodejob().getName());
		}
		return switchPanel;
	}

	/**
	 * 得到主体切换Panel的布局管理器
	 * @return
	 */
	private CardLayout getSwitchPanelCardLayout() {
		if(switchPanelCardLayout==null){
			switchPanelCardLayout = new CardLayout();
		}
		return switchPanelCardLayout;
	}

	/**
	 * 得到按钮管理Panel
	 * @return
	 */
	private UIPanel getButtonsPanel() {
		if(buttonsPanel==null){
			buttonsPanel = new UIPanel();
			buttonsPanel.setName("buttonsPanel");
			
			FlowLayout fl = new FlowLayout();
			fl.setAlignment(FlowLayout.RIGHT);
			fl.setVgap(10);
			fl.setHgap(15);
			buttonsPanel.setLayout(fl);
			buttonsPanel.setPreferredSize(new Dimension(100,40));
			
			// 添加按钮
			buttonsPanel.add(getButton_prev(), getButton_prev().getName());
			buttonsPanel.add(getButton_next(), getButton_next().getName());
			buttonsPanel.add(getButton_ok(), getButton_ok().getName());
			buttonsPanel.add(getButton_cancel(), getButton_cancel().getName());
		}
		return buttonsPanel;
	}

	/**
	 * 得到确定按钮
	 * @return
	 */
	private UIButton getButton_ok() {
		if(button_ok==null){
			button_ok = new UIButton();
			button_ok.setName("button_ok");
//			UPT60050704-000218=确定
			button_ok.setText(NCLangRes.getInstance().getStrByID("60050704", "UPT60050704-000218")/* @res "确定" */);
			
			addBottonCtrl(button_ok);
		}
		return button_ok;
	}

	/**
	 * 得到取消按钮
	 * @return
	 */
	private UIButton getButton_cancel() {
		if(button_cancel==null){
			button_cancel = new UIButton();
			button_cancel.setName("button_cancel");
//			UPT60050704-000219=取消
			button_cancel.setText(NCLangRes.getInstance().getStrByID("60050704", "UPT60050704-000219")/* @res "取消" */);
			
			addBottonCtrl(button_cancel);
		}
		return button_cancel;
	}

	/**
	 * 得到下一步按钮
	 * @return
	 */
	private UIButton getButton_next() {
		if(button_next==null){
			button_next = new UIButton();
			button_next.setName("button_next");
//			UPT60050704-000220=下一步
			button_next.setText(NCLangRes.getInstance().getStrByID("60050704", "UPT60050704-000220")/* @res "下一步" */);
			
			addBottonCtrl(button_next);
		}
		return button_next;
	}

	/**
	 * 得到上一步按钮
	 * @return
	 */
	private UIButton getButton_prev() {
		if(button_prev==null){
			button_prev = new UIButton();
			button_prev.setName("button_prev");
//			UPT60050704-000221=上一步
			button_prev.setText(NCLangRes.getInstance().getStrByID("60050704", "UPT60050704-000221")/* @res "上一步" */);
			
			addBottonCtrl(button_prev);
		}
		return button_prev;
	}
	
	/**
	 * 确定按钮 点击事件
	 */
	protected void buttonClicked_ok(){
		// 分情况
		switch(currentSwitchType){
		// 设置命名规则界面下一步操作
		case JOBCOPY_STATUS_RENAMEJOB:{
			btnclicked_ok_renamejob();
			break;
		}//end case
		
		// 修改岗位编码界面下一步操作
		case JOBCOPY_STATUS_RECODEJOB:{
			btnclicked_ok_recodejob();
			break;
		}//end case
		
		}//end switch
	}
	
	/**
	 * 取消按钮 点击事件
	 */
	protected void buttonClicked_cancel(){
		this.closeCancel();
	}
	
	/**
	 * 上一步按钮 点击事件
	 */
	protected void buttonClicked_prev(){
		// 分情况
		switch(currentSwitchType){
		// 设置命名规则界面下一步操作
		case JOBCOPY_STATUS_RENAMEJOB:{
			btnclicked_prev_renamejob();
			break;
		}//end case
		
		// 修改岗位编码界面下一步操作
		case JOBCOPY_STATUS_RECODEJOB:{
			btnclicked_prev_recodejob();
			break;
		}//end case
		
		}//end switch
	}
	
	/**
	 * 显示警告信息
	 * @param msg
	 */
	private void showHintMessage(String msg){
//		UPT60050704-000222=提示对话框
		MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("60050704", "UPT60050704-000222")/* @res "提示对话框" */, msg);
	}
	
	/**
	 * 显示错误信息
	 * @param msg
	 */
	private void showErrorMessage(String msg){
		MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("60050704", "UPT60050704-000223")/* @res "异常对话框" */, msg);
	}
	
	/**
	 * 停止所有的表格Panel编辑
	 */
	private void stopAllEditing(){
		
		// 停止重命名规则Panel的编辑
		if (getPanel_renamejob().getListPanel().getTable().getCellEditor()!=null){
			getPanel_renamejob().getListPanel().getTable().getCellEditor().stopCellEditing();
		}//end if
		
		// 停止重编码Panel的编辑
		if (getPanel_recodejob().getListPanel().getTable().getCellEditor()!=null){
			getPanel_recodejob().getListPanel().getTable().getCellEditor().stopCellEditing();
		}//end if
	}
	
	/**
	 * 在选择岗位界面中点击[下一步]的事件
	 */
	protected void btnclicked_next_choosejob(){
		// 首先判断是否完成了选择
		OrgNodeVO srcDeptVO = getPanel_choosejob().getSelectedSourceDeptVO();
		JobVO[] srcJobVOs = getPanel_choosejob().getSelectedSourceJobVOs();
		OrgNodeVO[] tgtDeptVOs = getPanel_choosejob().getSelectedTargetDeptVOs();
		
		// 判断初始条件
		if(srcDeptVO==null || srcJobVOs==null || srcJobVOs.length<=0 || 
				tgtDeptVOs==null || tgtDeptVOs.length<=0 ){
//			UPT60050704-000224=
			showHintMessage(NCLangRes.getInstance().getStrByID("60050704", "UPT60050704-000224")/* @res "请先完成源岗位和目标部门的选择！" */);
			return;
		}//end if
		
		// 加载
		getPanel_renamejob().loadInitList();
		
		setCurrentSwitchStatus(JOBCOPY_STATUS_RENAMEJOB);
	}
	
	/**
	 * 在岗位重命名界面中点击[下一步]的事件
	 */
	protected void btnclicked_next_renamejob(){
		// 停止编辑
		stopAllEditing();
		
		// 得到部门重命名VO数组
		JobCopyDeptRenameVO[] renameVOs = getPanel_renamejob().getCurrentJobCoptDeptRenameVOs();
		
		// 判断
		if(renameVOs==null || renameVOs.length<=0){
			return;
		}//end if
		
		for(JobCopyDeptRenameVO aRenameVO : renameVOs){
			// 如果查找字符串不为空，而且查找字符串和替换字符串相同
			if(StringUtils.hasText(aRenameVO.getFindString()) && aRenameVO.getFindString().equals(aRenameVO.getReplaceString())){
//				UPT60050704-000225=目标部门[{0}]的查找字符串和替换字符串相同，请修改！
				showHintMessage(NCLangRes.getInstance().getStrByID("60050704", "UPT60050704-000225",null,new String[]{aRenameVO.getTgtDeptName()})/* @res "目标部门[{0}]的查找字符串和替换字符串相同，请修改！" */);
				return;
			}//end if			
		}//end for
		
		// 加载
		getPanel_recodejob().loadInitList();
		setCurrentSwitchStatus(JOBCOPY_STATUS_RECODEJOB);
	}
	
	/**
	 * 在岗位重命名界面中点击[确定]的事件
	 */
	protected void btnclicked_ok_renamejob(){
		// 停止编辑
		stopAllEditing();
		
		// 得到部门重命名VO数组
		JobCopyDeptRenameVO[] renameVOs = getPanel_renamejob().getCurrentJobCoptDeptRenameVOs();
		
		// 判断
		if(renameVOs==null || renameVOs.length<=0){
			return;
		}//end if
		
		for(JobCopyDeptRenameVO aRenameVO : renameVOs){
			// 如果查找字符串不为空，而且查找字符串和替换字符串相同
			if(StringUtils.hasText(aRenameVO.getFindString()) && aRenameVO.getFindString().equals(aRenameVO.getReplaceString())){
				
				showHintMessage(NCLangRes.getInstance().getStrByID("60050704", "UPT60050704-000225",null,new String[]{aRenameVO.getTgtDeptName()})/* @res "目标部门[{0}]的查找字符串和替换字符串相同，请修改！" */);
				return;
			}//end if
		}//end for
		
		/**
		 * 复制岗位，包含子表信息等所有岗位相关信息都进行复制
		 * @param String pk_corp 公司主键
		 * @param BillCodeObjValueVO billCodeObjValueVO用于生成单据号的VO
		 * @param JobVO[] srcJobVOs 源岗位VO数组
		 * @param JobCoptDeptRenameVO[] jobRenameVOs 岗位改名VO，和部门VO一一对应
		 * @param String[] pk_deptdoc_tgts 目标部门VO数组，和岗位改名VO数组一一对应
		 * @param String[] tgtJobCodes 目标岗位新编码，可以为空，为空代表系统自动生成，如果不为空，长度必须为srcJobVOs.length * pk_deptdoc_tgts.length
		 * @throws BusinessException
		 */
		
		JobVO[] jobVOs = getPanel_choosejob().getSelectedSourceJobVOs();
		OrgNodeVO[] depts = getPanel_choosejob().getSelectedTargetDeptVOs();
		String[] deptPks = new String[depts.length];
		for(int i=0; i<deptPks.length; i++){
			deptPks[i] = depts[i].getPk_node();
		}//end for
		
		// 执行前确认
//		UPT60050704-000226=确认对话框
//		UPT60050704-000227=是否要执行岗位复制操作？
		int res = MessageDialog.showYesNoDlg(this, 
				NCLangRes.getInstance().getStrByID("60050704", "UPT60050704-000226")/* @res "确认对话框" */, 
				NCLangRes.getInstance().getStrByID("60050704", "UPT60050704-000227")/* @res "是否要执行岗位复制操作？" */);
		if(res!=MessageDialog.ID_YES){
			return;
		}//end if
		
		
		try{
			// 执行岗位复制操作
			ORGDelegator.getIJob().copyJobs(
					parentUI.getLogin_Pk_corp(),
					parentUI.getLogin_Uesrid(),
					jobVOs, 
					renameVOs, 
					deptPks, 
					null);
			
			// 关闭对话框
			this.closeOK();
			
		}catch(Exception e){
			parentUI.handleException(e);
		}//end try
		
	}
	
	/**
	 * 在岗位重编码界面中点击[确定]的事件
	 */
	protected void btnclicked_ok_recodejob(){
		// 停止编辑
		stopAllEditing();
		
		// 得到要复制的岗位精简VO
		JobLiteVO[] jobLiteVOs = getPanel_recodejob().getCurrentJobLiteVOs();
		if(jobLiteVOs==null || jobLiteVOs.length<=0){
//			UPT60050704-000228=没有需要复制的岗位！
			showHintMessage(NCLangRes.getInstance().getStrByID("60050704", "UPT60050704-000228")/* @res "没有需要复制的岗位！" */);
			return;
		}//end if
		
		// 得到
		String[] jobCodes = new String[jobLiteVOs.length];
		for(int i=0; i<jobCodes.length; i++){
			// 验证岗位编码
			if(!StringUtils.hasText(jobLiteVOs[i].getJobcode())){
//				UPT60050704-000229=请指明岗位[{0}]的岗位新岗位编码！
				showHintMessage(NCLangRes.getInstance().getStrByID("60050704", "UPT60050704-000229",null,new String[]{jobLiteVOs[i].getJobname()})/* @res "请指明岗位[{0}]的岗位新岗位编码！" */);
				return;
			}//end if
			
			jobCodes[i] = jobLiteVOs[i].getJobcode();
		}//end for
		
		/**
		 * 复制岗位，包含子表信息等所有岗位相关信息都进行复制
		 * @param String pk_corp 公司主键
		 * @param BillCodeObjValueVO billCodeObjValueVO用于生成单据号的VO
		 * @param JobVO[] srcJobVOs 源岗位VO数组
		 * @param JobCoptDeptRenameVO[] jobRenameVOs 岗位改名VO，和部门VO一一对应
		 * @param String[] pk_deptdoc_tgts 目标部门VO数组，和岗位改名VO数组一一对应
		 * @param String[] tgtJobCodes 目标岗位新编码，可以为空，为空代表系统自动生成，如果不为空，长度必须为srcJobVOs.length * pk_deptdoc_tgts.length
		 * @throws BusinessException
		 */
		
		JobVO[] jobVOs = getPanel_choosejob().getSelectedSourceJobVOs();
		JobCopyDeptRenameVO[] renameVOs = getPanel_renamejob().getCurrentJobCoptDeptRenameVOs();
		OrgNodeVO[] depts = getPanel_choosejob().getSelectedTargetDeptVOs();
		String[] deptPks = new String[depts.length];
		for(int i=0; i<deptPks.length; i++){
			deptPks[i] = depts[i].getPk_node();
		}//end for
		
		
		
		// 执行前确认
		int res = MessageDialog.showYesNoDlg(this, 
				NCLangRes.getInstance().getStrByID("60050704", "UPT60050704-000226")/* @res "确认对话框" */, 
				NCLangRes.getInstance().getStrByID("60050704", "UPT60050704-000227")/* @res "是否要执行岗位复制操作？" */
				);
		if(res!=MessageDialog.ID_YES){
			return;
		}//end if
		
		
		try{
			// 执行岗位复制操作
			ORGDelegator.getIJob().copyJobs(
					parentUI.getLogin_Pk_corp(),
					parentUI.getLogin_Uesrid(),
					jobVOs, 
					renameVOs, 
					deptPks, 
					jobCodes);
			
			// 关闭对话框
			this.closeOK();
		}catch(Exception e){
			parentUI.handleException(e);
		}//end try
		
		
	}
	
	/**
	 * 在岗位重命名界面中点击[上一步]的事件
	 */
	protected void btnclicked_prev_renamejob(){
		// 停止编辑
		stopAllEditing();
		
		setCurrentSwitchStatus(JOBCOPY_STATUS_CHOOSEJOB);
	}
	
	/**
	 * 在岗位重编码界面中点击[上一步]的事件
	 */
	protected void btnclicked_prev_recodejob(){
		// 停止编辑
		stopAllEditing();
		
		setCurrentSwitchStatus(JOBCOPY_STATUS_RENAMEJOB);
	}
	
	/**
	 * 下一步按钮 点击事件
	 */
	protected void buttonClicked_next(){
		// 分情况
		switch(currentSwitchType){
		
		// 选择岗位界面的下一步操作
		case JOBCOPY_STATUS_CHOOSEJOB:{
			btnclicked_next_choosejob();
			break;
		}//end case
		
		// 设置命名规则界面下一步操作
		case JOBCOPY_STATUS_RENAMEJOB:{
			btnclicked_next_renamejob();
			break;
		}//end case
		
		}//end switch
	}

	/**
	 * 按钮响应事件
	 */
	public void actionPerformed(ActionEvent e) {
		if(e==null || e.getSource()==null){
			return;
		}//end if
		
		if(getButton_ok().equals(e.getSource())){
			buttonClicked_ok();
		}else if(getButton_cancel().equals(e.getSource())){
			buttonClicked_cancel();
		}else if(getButton_prev().equals(e.getSource())){
			buttonClicked_prev();
		}else if(getButton_next().equals(e.getSource())){
			buttonClicked_next();
		}//end if
	}

	/**
	 * 父类方法重写
	 */
	public int showModal() {
		// 设置起始选择状态
		setCurrentSwitchStatus(JOBCOPY_STATUS_CHOOSEJOB);
		// 
		return super.showModal();
	}

}
