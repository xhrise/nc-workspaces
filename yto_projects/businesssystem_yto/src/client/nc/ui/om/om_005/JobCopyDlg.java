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
 * ��λ��Ϣ�ڵ�
 * ��λ���ƶԻ���
 * @author wangxing
 *
 */
public class JobCopyDlg extends BorderDialog implements ActionListener {

	/**
	 * Ĭ�����л�ID
	 */
	private static final long serialVersionUID = 1L;

	// ��ǰ��ʾ��״̬����
	// ��״̬
	protected static final int JOBCOPY_STATUS_NONE = 0;
	// ѡ���λ״̬
	protected static final int JOBCOPY_STATUS_CHOOSEJOB = 100;
	// ��������λ״̬
	protected static final int JOBCOPY_STATUS_RENAMEJOB = 200;
	// �ر����λ״̬
	protected static final int JOBCOPY_STATUS_RECODEJOB = 300;
	
	// �������
	private JobInfoUI parentUI = null;
	
	// ��λѡ��Panel
	private JobChoosePanel panel_choosejob = null;
	
	// ��λ������Panel
	private JobRenamePanel panel_renamejob = null;
	
	// ��λ�ر���Panel
	private JobRecodePanel panel_recodejob = null;
	
	// ��ǰ��ʾ���л�����
	private int currentSwitchType = JOBCOPY_STATUS_NONE;
	
	// ��������Panel
	private UIPanel centertPanel = null;
	
	// ���빦���л�Panel
	private UIPanel switchPanel = null;
	
	// ���빦���л�Panel�Ĳ��ֹ�����
	private CardLayout switchPanelCardLayout = null;
	
	// �ײ���ʾ��ť��Panel
	private UIPanel buttonsPanel = null;
	
	// ��ť��ȷ��
	private UIButton button_ok = null;
	
	// ��ť��ȡ��
	private UIButton button_cancel = null;
	
	// ��ť����һ��
	private UIButton button_next = null;
	
	// ��ť����һ��
	private UIButton button_prev = null;
	
	/**
	 * ���캯��
	 * @param parent
	 */
	public JobCopyDlg(JobInfoUI parent) {
		super(parent);
		
		parentUI = parent;
		
		init();
	}
	
	/**
	 * ��ʼ������
	 */
	private void initListeners(){
		getButton_ok().addActionListener(this);
		getButton_cancel().addActionListener(this);
		getButton_next().addActionListener(this);
		getButton_prev().addActionListener(this);
		
	}
	
	/**
	 * �õ���ǰ�Ի������ʾ����
	 * @return
	 */
	public void setCurrentSwitchStatus(int showType){
		// �Ƿ��Զ�����
		boolean isAutoCode = parentUI.isAutoJobCode();
//		isAutoCode = false;
		
		// ��������ð�ť��״̬
		switch(showType){
		
			// ��λѡ��״̬
			case JOBCOPY_STATUS_CHOOSEJOB:{
				getSwitchPanelCardLayout().show(getSwitchPanel(), getPanel_choosejob().getName());
				currentSwitchType = JOBCOPY_STATUS_CHOOSEJOB;
				
				// ���ð�ť��ʾ
				getButton_ok().setVisible(false);
				getButton_cancel().setVisible(true);
				getButton_prev().setVisible(false);
				getButton_next().setVisible(true);
				break;
			}//end case
			
			// ��λ����״̬
			case JOBCOPY_STATUS_RENAMEJOB:{
				getSwitchPanelCardLayout().show(getSwitchPanel(), getPanel_renamejob().getName());
				currentSwitchType = JOBCOPY_STATUS_RENAMEJOB;
				
				// ���ð�ť��ʾ
				getButton_ok().setVisible(isAutoCode);
				getButton_cancel().setVisible(true);
				getButton_prev().setVisible(true);
				getButton_next().setVisible(!isAutoCode);
				break;
			}//end case
			
			// ��λ�ı���״̬
			case JOBCOPY_STATUS_RECODEJOB:{
				getSwitchPanelCardLayout().show(getSwitchPanel(), getPanel_recodejob().getName());
				currentSwitchType = JOBCOPY_STATUS_RECODEJOB;
				
				// ���ð�ť��ʾ
				getButton_ok().setVisible(true);
				getButton_cancel().setVisible(true);
				getButton_prev().setVisible(true);
				getButton_next().setVisible(false);
				
				break;
			}//end case
			
			// Ĭ�ϴ���
			default:{
//				currentSwitchType = JOBCOPY_STATUS_NONE;
			}//end default
		
		}//end switch
	}
	
	/**
	 * �õ���ǰ�Ի������ʾ����
	 * @return
	 */
	public int getCurrentSwitchStatus(){
		return currentSwitchType;
	}
	
	/**
	 * ��ʼ������
	 */
	private void init(){
		this.setName("JobCopyDlg");
//		UPT60050704-000217=��λ����
		this.setTitle(NCLangRes.getInstance().getStrByID("60050704", "UPT60050704-000217")/* @res "��λ����" */);
		this.setModal(true);
		
		this.setContentPane(getCentertPanel());
		this.setSize(650, 550);
		this.setResizable(false);
		// ������ʾ����
		setCurrentSwitchStatus(JOBCOPY_STATUS_CHOOSEJOB);
		
		// ��ʼ������
		initListeners();
	}
	
	/**
	 * �õ��������������
	 * @return
	 */
	protected JobInfoUI getParentUI() {
		return parentUI;
	}

	/**
	 * �õ���λѡ��Panel
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
	 * �õ���λ������Panel
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
	 * �õ���λ�ر���Panel
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
	 * �õ��в�����Panel
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
	 * �õ������л�Panel
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
	 * �õ������л�Panel�Ĳ��ֹ�����
	 * @return
	 */
	private CardLayout getSwitchPanelCardLayout() {
		if(switchPanelCardLayout==null){
			switchPanelCardLayout = new CardLayout();
		}
		return switchPanelCardLayout;
	}

	/**
	 * �õ���ť����Panel
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
			
			// ��Ӱ�ť
			buttonsPanel.add(getButton_prev(), getButton_prev().getName());
			buttonsPanel.add(getButton_next(), getButton_next().getName());
			buttonsPanel.add(getButton_ok(), getButton_ok().getName());
			buttonsPanel.add(getButton_cancel(), getButton_cancel().getName());
		}
		return buttonsPanel;
	}

	/**
	 * �õ�ȷ����ť
	 * @return
	 */
	private UIButton getButton_ok() {
		if(button_ok==null){
			button_ok = new UIButton();
			button_ok.setName("button_ok");
//			UPT60050704-000218=ȷ��
			button_ok.setText(NCLangRes.getInstance().getStrByID("60050704", "UPT60050704-000218")/* @res "ȷ��" */);
			
			addBottonCtrl(button_ok);
		}
		return button_ok;
	}

	/**
	 * �õ�ȡ����ť
	 * @return
	 */
	private UIButton getButton_cancel() {
		if(button_cancel==null){
			button_cancel = new UIButton();
			button_cancel.setName("button_cancel");
//			UPT60050704-000219=ȡ��
			button_cancel.setText(NCLangRes.getInstance().getStrByID("60050704", "UPT60050704-000219")/* @res "ȡ��" */);
			
			addBottonCtrl(button_cancel);
		}
		return button_cancel;
	}

	/**
	 * �õ���һ����ť
	 * @return
	 */
	private UIButton getButton_next() {
		if(button_next==null){
			button_next = new UIButton();
			button_next.setName("button_next");
//			UPT60050704-000220=��һ��
			button_next.setText(NCLangRes.getInstance().getStrByID("60050704", "UPT60050704-000220")/* @res "��һ��" */);
			
			addBottonCtrl(button_next);
		}
		return button_next;
	}

	/**
	 * �õ���һ����ť
	 * @return
	 */
	private UIButton getButton_prev() {
		if(button_prev==null){
			button_prev = new UIButton();
			button_prev.setName("button_prev");
//			UPT60050704-000221=��һ��
			button_prev.setText(NCLangRes.getInstance().getStrByID("60050704", "UPT60050704-000221")/* @res "��һ��" */);
			
			addBottonCtrl(button_prev);
		}
		return button_prev;
	}
	
	/**
	 * ȷ����ť ����¼�
	 */
	protected void buttonClicked_ok(){
		// �����
		switch(currentSwitchType){
		// �����������������һ������
		case JOBCOPY_STATUS_RENAMEJOB:{
			btnclicked_ok_renamejob();
			break;
		}//end case
		
		// �޸ĸ�λ���������һ������
		case JOBCOPY_STATUS_RECODEJOB:{
			btnclicked_ok_recodejob();
			break;
		}//end case
		
		}//end switch
	}
	
	/**
	 * ȡ����ť ����¼�
	 */
	protected void buttonClicked_cancel(){
		this.closeCancel();
	}
	
	/**
	 * ��һ����ť ����¼�
	 */
	protected void buttonClicked_prev(){
		// �����
		switch(currentSwitchType){
		// �����������������һ������
		case JOBCOPY_STATUS_RENAMEJOB:{
			btnclicked_prev_renamejob();
			break;
		}//end case
		
		// �޸ĸ�λ���������һ������
		case JOBCOPY_STATUS_RECODEJOB:{
			btnclicked_prev_recodejob();
			break;
		}//end case
		
		}//end switch
	}
	
	/**
	 * ��ʾ������Ϣ
	 * @param msg
	 */
	private void showHintMessage(String msg){
//		UPT60050704-000222=��ʾ�Ի���
		MessageDialog.showHintDlg(this, NCLangRes.getInstance().getStrByID("60050704", "UPT60050704-000222")/* @res "��ʾ�Ի���" */, msg);
	}
	
	/**
	 * ��ʾ������Ϣ
	 * @param msg
	 */
	private void showErrorMessage(String msg){
		MessageDialog.showErrorDlg(this, NCLangRes.getInstance().getStrByID("60050704", "UPT60050704-000223")/* @res "�쳣�Ի���" */, msg);
	}
	
	/**
	 * ֹͣ���еı��Panel�༭
	 */
	private void stopAllEditing(){
		
		// ֹͣ����������Panel�ı༭
		if (getPanel_renamejob().getListPanel().getTable().getCellEditor()!=null){
			getPanel_renamejob().getListPanel().getTable().getCellEditor().stopCellEditing();
		}//end if
		
		// ֹͣ�ر���Panel�ı༭
		if (getPanel_recodejob().getListPanel().getTable().getCellEditor()!=null){
			getPanel_recodejob().getListPanel().getTable().getCellEditor().stopCellEditing();
		}//end if
	}
	
	/**
	 * ��ѡ���λ�����е��[��һ��]���¼�
	 */
	protected void btnclicked_next_choosejob(){
		// �����ж��Ƿ������ѡ��
		OrgNodeVO srcDeptVO = getPanel_choosejob().getSelectedSourceDeptVO();
		JobVO[] srcJobVOs = getPanel_choosejob().getSelectedSourceJobVOs();
		OrgNodeVO[] tgtDeptVOs = getPanel_choosejob().getSelectedTargetDeptVOs();
		
		// �жϳ�ʼ����
		if(srcDeptVO==null || srcJobVOs==null || srcJobVOs.length<=0 || 
				tgtDeptVOs==null || tgtDeptVOs.length<=0 ){
//			UPT60050704-000224=
			showHintMessage(NCLangRes.getInstance().getStrByID("60050704", "UPT60050704-000224")/* @res "�������Դ��λ��Ŀ�겿�ŵ�ѡ��" */);
			return;
		}//end if
		
		// ����
		getPanel_renamejob().loadInitList();
		
		setCurrentSwitchStatus(JOBCOPY_STATUS_RENAMEJOB);
	}
	
	/**
	 * �ڸ�λ�����������е��[��һ��]���¼�
	 */
	protected void btnclicked_next_renamejob(){
		// ֹͣ�༭
		stopAllEditing();
		
		// �õ�����������VO����
		JobCopyDeptRenameVO[] renameVOs = getPanel_renamejob().getCurrentJobCoptDeptRenameVOs();
		
		// �ж�
		if(renameVOs==null || renameVOs.length<=0){
			return;
		}//end if
		
		for(JobCopyDeptRenameVO aRenameVO : renameVOs){
			// ��������ַ�����Ϊ�գ����Ҳ����ַ������滻�ַ�����ͬ
			if(StringUtils.hasText(aRenameVO.getFindString()) && aRenameVO.getFindString().equals(aRenameVO.getReplaceString())){
//				UPT60050704-000225=Ŀ�겿��[{0}]�Ĳ����ַ������滻�ַ�����ͬ�����޸ģ�
				showHintMessage(NCLangRes.getInstance().getStrByID("60050704", "UPT60050704-000225",null,new String[]{aRenameVO.getTgtDeptName()})/* @res "Ŀ�겿��[{0}]�Ĳ����ַ������滻�ַ�����ͬ�����޸ģ�" */);
				return;
			}//end if			
		}//end for
		
		// ����
		getPanel_recodejob().loadInitList();
		setCurrentSwitchStatus(JOBCOPY_STATUS_RECODEJOB);
	}
	
	/**
	 * �ڸ�λ�����������е��[ȷ��]���¼�
	 */
	protected void btnclicked_ok_renamejob(){
		// ֹͣ�༭
		stopAllEditing();
		
		// �õ�����������VO����
		JobCopyDeptRenameVO[] renameVOs = getPanel_renamejob().getCurrentJobCoptDeptRenameVOs();
		
		// �ж�
		if(renameVOs==null || renameVOs.length<=0){
			return;
		}//end if
		
		for(JobCopyDeptRenameVO aRenameVO : renameVOs){
			// ��������ַ�����Ϊ�գ����Ҳ����ַ������滻�ַ�����ͬ
			if(StringUtils.hasText(aRenameVO.getFindString()) && aRenameVO.getFindString().equals(aRenameVO.getReplaceString())){
				
				showHintMessage(NCLangRes.getInstance().getStrByID("60050704", "UPT60050704-000225",null,new String[]{aRenameVO.getTgtDeptName()})/* @res "Ŀ�겿��[{0}]�Ĳ����ַ������滻�ַ�����ͬ�����޸ģ�" */);
				return;
			}//end if
		}//end for
		
		/**
		 * ���Ƹ�λ�������ӱ���Ϣ�����и�λ�����Ϣ�����и���
		 * @param String pk_corp ��˾����
		 * @param BillCodeObjValueVO billCodeObjValueVO�������ɵ��ݺŵ�VO
		 * @param JobVO[] srcJobVOs Դ��λVO����
		 * @param JobCoptDeptRenameVO[] jobRenameVOs ��λ����VO���Ͳ���VOһһ��Ӧ
		 * @param String[] pk_deptdoc_tgts Ŀ�겿��VO���飬�͸�λ����VO����һһ��Ӧ
		 * @param String[] tgtJobCodes Ŀ���λ�±��룬����Ϊ�գ�Ϊ�մ���ϵͳ�Զ����ɣ������Ϊ�գ����ȱ���ΪsrcJobVOs.length * pk_deptdoc_tgts.length
		 * @throws BusinessException
		 */
		
		JobVO[] jobVOs = getPanel_choosejob().getSelectedSourceJobVOs();
		OrgNodeVO[] depts = getPanel_choosejob().getSelectedTargetDeptVOs();
		String[] deptPks = new String[depts.length];
		for(int i=0; i<deptPks.length; i++){
			deptPks[i] = depts[i].getPk_node();
		}//end for
		
		// ִ��ǰȷ��
//		UPT60050704-000226=ȷ�϶Ի���
//		UPT60050704-000227=�Ƿ�Ҫִ�и�λ���Ʋ�����
		int res = MessageDialog.showYesNoDlg(this, 
				NCLangRes.getInstance().getStrByID("60050704", "UPT60050704-000226")/* @res "ȷ�϶Ի���" */, 
				NCLangRes.getInstance().getStrByID("60050704", "UPT60050704-000227")/* @res "�Ƿ�Ҫִ�и�λ���Ʋ�����" */);
		if(res!=MessageDialog.ID_YES){
			return;
		}//end if
		
		
		try{
			// ִ�и�λ���Ʋ���
			ORGDelegator.getIJob().copyJobs(
					parentUI.getLogin_Pk_corp(),
					parentUI.getLogin_Uesrid(),
					jobVOs, 
					renameVOs, 
					deptPks, 
					null);
			
			// �رնԻ���
			this.closeOK();
			
		}catch(Exception e){
			parentUI.handleException(e);
		}//end try
		
	}
	
	/**
	 * �ڸ�λ�ر�������е��[ȷ��]���¼�
	 */
	protected void btnclicked_ok_recodejob(){
		// ֹͣ�༭
		stopAllEditing();
		
		// �õ�Ҫ���Ƶĸ�λ����VO
		JobLiteVO[] jobLiteVOs = getPanel_recodejob().getCurrentJobLiteVOs();
		if(jobLiteVOs==null || jobLiteVOs.length<=0){
//			UPT60050704-000228=û����Ҫ���Ƶĸ�λ��
			showHintMessage(NCLangRes.getInstance().getStrByID("60050704", "UPT60050704-000228")/* @res "û����Ҫ���Ƶĸ�λ��" */);
			return;
		}//end if
		
		// �õ�
		String[] jobCodes = new String[jobLiteVOs.length];
		for(int i=0; i<jobCodes.length; i++){
			// ��֤��λ����
			if(!StringUtils.hasText(jobLiteVOs[i].getJobcode())){
//				UPT60050704-000229=��ָ����λ[{0}]�ĸ�λ�¸�λ���룡
				showHintMessage(NCLangRes.getInstance().getStrByID("60050704", "UPT60050704-000229",null,new String[]{jobLiteVOs[i].getJobname()})/* @res "��ָ����λ[{0}]�ĸ�λ�¸�λ���룡" */);
				return;
			}//end if
			
			jobCodes[i] = jobLiteVOs[i].getJobcode();
		}//end for
		
		/**
		 * ���Ƹ�λ�������ӱ���Ϣ�����и�λ�����Ϣ�����и���
		 * @param String pk_corp ��˾����
		 * @param BillCodeObjValueVO billCodeObjValueVO�������ɵ��ݺŵ�VO
		 * @param JobVO[] srcJobVOs Դ��λVO����
		 * @param JobCoptDeptRenameVO[] jobRenameVOs ��λ����VO���Ͳ���VOһһ��Ӧ
		 * @param String[] pk_deptdoc_tgts Ŀ�겿��VO���飬�͸�λ����VO����һһ��Ӧ
		 * @param String[] tgtJobCodes Ŀ���λ�±��룬����Ϊ�գ�Ϊ�մ���ϵͳ�Զ����ɣ������Ϊ�գ����ȱ���ΪsrcJobVOs.length * pk_deptdoc_tgts.length
		 * @throws BusinessException
		 */
		
		JobVO[] jobVOs = getPanel_choosejob().getSelectedSourceJobVOs();
		JobCopyDeptRenameVO[] renameVOs = getPanel_renamejob().getCurrentJobCoptDeptRenameVOs();
		OrgNodeVO[] depts = getPanel_choosejob().getSelectedTargetDeptVOs();
		String[] deptPks = new String[depts.length];
		for(int i=0; i<deptPks.length; i++){
			deptPks[i] = depts[i].getPk_node();
		}//end for
		
		
		
		// ִ��ǰȷ��
		int res = MessageDialog.showYesNoDlg(this, 
				NCLangRes.getInstance().getStrByID("60050704", "UPT60050704-000226")/* @res "ȷ�϶Ի���" */, 
				NCLangRes.getInstance().getStrByID("60050704", "UPT60050704-000227")/* @res "�Ƿ�Ҫִ�и�λ���Ʋ�����" */
				);
		if(res!=MessageDialog.ID_YES){
			return;
		}//end if
		
		
		try{
			// ִ�и�λ���Ʋ���
			ORGDelegator.getIJob().copyJobs(
					parentUI.getLogin_Pk_corp(),
					parentUI.getLogin_Uesrid(),
					jobVOs, 
					renameVOs, 
					deptPks, 
					jobCodes);
			
			// �رնԻ���
			this.closeOK();
		}catch(Exception e){
			parentUI.handleException(e);
		}//end try
		
		
	}
	
	/**
	 * �ڸ�λ�����������е��[��һ��]���¼�
	 */
	protected void btnclicked_prev_renamejob(){
		// ֹͣ�༭
		stopAllEditing();
		
		setCurrentSwitchStatus(JOBCOPY_STATUS_CHOOSEJOB);
	}
	
	/**
	 * �ڸ�λ�ر�������е��[��һ��]���¼�
	 */
	protected void btnclicked_prev_recodejob(){
		// ֹͣ�༭
		stopAllEditing();
		
		setCurrentSwitchStatus(JOBCOPY_STATUS_RENAMEJOB);
	}
	
	/**
	 * ��һ����ť ����¼�
	 */
	protected void buttonClicked_next(){
		// �����
		switch(currentSwitchType){
		
		// ѡ���λ�������һ������
		case JOBCOPY_STATUS_CHOOSEJOB:{
			btnclicked_next_choosejob();
			break;
		}//end case
		
		// �����������������һ������
		case JOBCOPY_STATUS_RENAMEJOB:{
			btnclicked_next_renamejob();
			break;
		}//end case
		
		}//end switch
	}

	/**
	 * ��ť��Ӧ�¼�
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
	 * ���෽����д
	 */
	public int showModal() {
		// ������ʼѡ��״̬
		setCurrentSwitchStatus(JOBCOPY_STATUS_CHOOSEJOB);
		// 
		return super.showModal();
	}

}
