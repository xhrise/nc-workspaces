package nc.ui.iufo.input.edit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JViewport;
import javax.swing.KeyStroke;
import javax.swing.ScrollPaneLayout;
import javax.swing.Scrollable;
import javax.swing.border.Border;

import nc.pub.iufo.cache.base.UnitCache;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iufo.input.InputUtil;
import nc.ui.iufo.input.control.DataSourceConfig;
import nc.ui.iufo.input.control.RepDataCondVO;
import nc.ui.iufo.input.control.RepDataControler;
import nc.ui.iufo.input.table.TableInputParam;
import nc.ui.iufo.input.view.KeyCondPaneUtil;
import nc.ui.iufo.input.view.KeyCondPanel;
import nc.ui.iufo.pub.UfoPublic;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.UIScrollPane;
import nc.vo.iufo.data.MeasurePubDataVO;
import nc.vo.iufo.data.VerItem;
import nc.vo.iufo.datasource.DataSourceLoginVO;
import nc.vo.iufo.datasource.DataSourceVO;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.iufo.unit.UnitInfoVO;
import nc.vo.iuforeport.rep.ReportVO;

import com.ufida.dataset.IContext;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.zior.comp.KStatusBar;
import com.ufida.zior.console.ActionHandler;
import com.ufida.zior.docking.view.actions.TitleAction;
import com.ufida.zior.plugin.system.RefreshViewPlugin;
import com.ufida.zior.util.ResourceManager;
import com.ufida.zior.util.UIUtilities;
import com.ufida.zior.view.Mainboard;
import com.ufida.zior.view.Viewer;
import com.ufsoft.iufo.check.vo.CheckDetailVO;
import com.ufsoft.iufo.check.vo.CheckResultVO;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.fmtplugin.monitor.MonitorPlugin;
import com.ufsoft.iufo.fmtplugin.statusshow.RepInputStatusBarPlugin;
import com.ufsoft.iufo.inputplugin.biz.UfoFormulaTracePlugin;
import com.ufsoft.iufo.inputplugin.biz.file.MenuStateData;
import com.ufsoft.iufo.inputplugin.biz.formulatrace.FormulaTraceBizUtil;
import com.ufsoft.iufo.inputplugin.formula.FormulaInputPlugin;
import com.ufsoft.iufo.inputplugin.hbdraft.UfoHBDraftPlugin;
import com.ufsoft.iufo.inputplugin.inputcore.InputCorePlugin;
import com.ufsoft.iufo.inputplugin.key.KeyInputPlugin;
import com.ufsoft.iufo.inputplugin.measure.MeasureFmt;
import com.ufsoft.iufo.inputplugin.measure.MeasureInputPlugin;
import com.ufsoft.iufo.inputplugin.querynavigation.FormulaTraceNavigation;
import com.ufsoft.iufo.inputplugin.querynavigation.UfoFormulaQueryNextPlugin;
import com.ufsoft.iufo.inputplugin.ufobiz.EditInpuDirPlugin;
import com.ufsoft.iufo.inputplugin.ufobiz.InputKeyAssistPlugin;
import com.ufsoft.iufo.inputplugin.ufobiz.UfoInputDataPlugin;
import com.ufsoft.iufo.inputplugin.ufobiz.UfoInputFilePlugin;
import com.ufsoft.iufo.inputplugin.ufobiz.data.InputDirConstant;
import com.ufsoft.iufo.inputplugin.ufobiz.data.UfoSaveRepDataCmd;
import com.ufsoft.iufo.inputplugin.ufodynarea.UfoDynAreaInputPlugin;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.iuforeport.repdatainput.RepDataOperResultVO;
import com.ufsoft.iuforeport.tableinput.applet.DataSourceInfo;
import com.ufsoft.iuforeport.tableinput.applet.IRepDataParam;
import com.ufsoft.iuforeport.tableinput.applet.RepDataParam;
import com.ufsoft.iuforeport.tableinput.applet.TableDataInputAuth;
import com.ufsoft.report.ReportDesigner;
import com.ufsoft.report.sysplugin.cellpostil.CellPostilInputPlugin;
import com.ufsoft.report.sysplugin.editplugin.RepEditPlugin;
import com.ufsoft.report.sysplugin.findreplace.FindReplacePlugin;
import com.ufsoft.report.sysplugin.headersize.HeaderSizePlugin;
import com.ufsoft.report.sysplugin.help.HelpPlugin;
import com.ufsoft.report.sysplugin.location.LocationPlugin;
import com.ufsoft.report.sysplugin.print.PrintPlugin;
import com.ufsoft.report.sysplugin.repheaderlock.RepHeaderLockPlugin;
import com.ufsoft.report.sysplugin.repstyle.RepStylePlugin;
import com.ufsoft.report.sysplugin.viewmanager.ViewManagerPlugin;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsAuthorization;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.TablePane;
import com.ufsoft.table.undo.UndoPlugin;

public class RepDataEditor extends ReportDesigner {
	private static final long serialVersionUID = -6724497574244933880L;

	//��ǰ��ҳ��Ӧ�Ĺؼ����������ؼ�����������ȫ���ô˱������ɹؼ�����壬�κ������£���ֵ����Ϊ��
	//��aloneid��Ϊ�ɣ�aloneidΪ��ʱ����ʾ�������ݲ��ܴ�
	private MeasurePubDataVO m_pubData=null;
	
	//�˵�״̬��Ϣ�����ڿ��Ƹ��˵��Ŀ���״̬
	private MenuStateData m_menuState=null;
	
	//ͬ��̨�����Ĳ������󣬰�����AloneID������PK������PK������Դ������Ϣ����ֵ����Ϊ��
	private IRepDataParam m_repDataParam=new RepDataParam();
	
	//��̬�����ݱ���ʱ����¼���ݿ��ж�̬����Ӧ��AloneID����ֹɾ����ı���ָ��Ķ�̬������
	private Hashtable<String, Vector<String>> m_hashDynAloneID=null;
	
	/***��ǰ�������ݶ�Ӧ�ı�����˽������ͬ�������ݵı�����˽���Ƿֿ���ģ����Է��ڱ�����*/
	//�������������洢������˽�����û�ѡ���׷�ٽ���������ڴ��ڼ���ʱ��ˢ����˽���б���λ�б�ѡ����
	private CheckResultVO m_repCheckResult=null;
	private CheckDetailVO m_repCheckDetail=null;
	
	//�������������洢��λ���ĵ�Ԫ���б���Ԫ�񱳾�ɫ�����ڵ�Ԫ�����
	private Color m_repCheckColor=null;
	private List<CellPosition> m_repCheckCell=null;
	/***��ǰ�������ݶ�Ӧ�ı�����˽������ͬ�������ݵı�����˽���Ƿֿ���ģ����Է��ڱ�����*/
	//��ʽ׷�ٶ�λ���ĵ�Ԫ���б�ֻ��Ӧһ���������ݴ���
	private List<CellPosition> m_traceCells = null;
	
	//�ؼ�����壬Ϊ��֧�ֹ���������JScrollPane
	private KeyCondPanel m_keyCondPane=null;
	private JScrollPane m_keyScrollPane=null;
	
	//��ʽ��Ԫ���Ƿ����¼�룬ÿ����������ҳ���в�ͬ�����ã��ڴ򿪱�������ҳ��ʱȡ�ã��ڴ��ڱ�����ʱ������MeasureFmt��
	private boolean m_bCanFormInput=false;
	
	//���������޷��򿪵ĳ�����ʾ
	private String m_strErrMsg=null;
	
	//�Ƿ���Ҫ��λ����1������¼��ĵ�Ԫ��
	private boolean m_bNeedLocateFirstCell=false;
	
	private IPostRepDataEditorActive m_postActive=null;

	private int m_iInputDir=InputDirConstant.DIR_RIGHT;
	
	private boolean m_bShutDowned=false;

	public RepDataEditor(){
		super();
	}
	
	public void shutdown() {
		RepDataControler controler = RepDataControler.getInstance(getMainboard());
		controler.removeOneEditorID(getId());// ɾ����ǰ���ڼ�¼��������Ϣ
		controler.activeOtherRepDataEditor(getMainboard(),this);// ������һ���������ݴ��ڣ���ֹ������ܽ����Դ���������ڱ�����
		controler.clearTaskCheckResult(this);// ����ɾ����Ӧ�ñ������ݵı�������Ϣ
		if (controler.getReportCalUtilWithoutCreate()!=null 
				&& controler.getReportCalUtilWithoutCreate().getCellsModel()==getCellsModel())
			controler.reSetReportCalUtil();
		
		m_bShutDowned=true;
		FormulaTraceNavigation.getInstance(getMainboard()).remove(this);
		
		if (controler.isRepEditorAllRemoved()){
			getMainboard().getDockingManager().close(RepDataControler.FORMULA_TRACERESULT_ID);
			getMainboard().getDockingManager().close(RepDataControler.CHECK_RESULT_ID);
		}
		
		if (m_keyCondPane != null) {
			m_keyCondPane.removeAllKeyCondComponent();
		}
		removeAll();
	}
		
	public void startup() {
		super.startup();
		//��context�У���¼����״̬Ϊ¼��״̬����״̬��TableInputAuth���õ�
		getContext().setAttribute(IUfoContextKey.OPERATION_STATE, IUfoContextKey.OPERATION_INPUT);
		reInit();
		addTitleAction(new RefreshViewAction(),0);
		installKeyboardActions();
	}
	
	protected boolean save() {
		if (isDirty()==false)
			return true;
        
		return new UfoSaveRepDataCmd(this).save(this);
	}
	
	private void innerReInit(){
		Mainboard mainBoard=getMainboard();
		RepDataControler controler=RepDataControler.getInstance(mainBoard);
		
		//��¼�ô����ϴζ�Ӧ��AloneID��ReportPK
		String strPrevAloneID=m_repDataParam.getAloneID();
		String strPrevRepPK=m_repDataParam.getReportPK();
		MeasurePubDataVO prevPubData=m_repDataParam.getPubData();
		
		RepDataCondVO repDataCond=controler.getRepDataCond(getId());
		m_hashDynAloneID=null;
		m_pubData=null;
		m_repDataParam=new RepDataParam();
		m_postActive=repDataCond.getPostRepDataActive();
		
		//�ڱ���������屻Mainboard��ʼ��ʱ,RepDataCondΪ��
		if (repDataCond!=null){
			m_pubData=repDataCond.getPubData();
			m_repDataParam.setAloneID(repDataCond.getAloneID());
			m_repDataParam.setReportPK(repDataCond.getRepPK());
			m_repDataParam.setTaskPK(repDataCond.getTaskPK());
            if (m_pubData != null && m_pubData.getAccSchemePK() == null) {
				String strAccSchemePK = InputUtil.getAccSechemePK(repDataCond
						.getTaskPK());
				m_pubData.setAccSchemePK(strAccSchemePK);
			}
		}
		m_repDataParam.setOperType(TableInputParam.OPERTYPE_REPDATA_INPUT);
		m_repDataParam.setOperUserPK(controler.getCurUserInfo(mainBoard).getID());
		m_repDataParam.setOperUnitPK(controler.getCurUserInfo(mainBoard).getUnitId());
		m_repDataParam.setOrgPK((String)getMainboard().getContext().getAttribute(IUfoContextKey.ORG_PK));
		m_repDataParam.setPubData(m_pubData);
		
		//���ñ���
		ReportVO report=IUFOUICacheManager.getSingleton().getReportCache().getByPK(m_repDataParam.getReportPK());
		doSetTitle(report,m_repDataParam.getPubData());
		
		//���AloneID��ReportPKΪ�գ���ʾ�κβ˵���������
		if (m_repDataParam.getAloneID()==null || m_repDataParam.getReportPK()==null)
			m_menuState=null;
		
		//���AloneID��ReportPK���ϴβ�һ������ʾCellsModel��Ҫ���¼��أ�����������ǰ��CellsModel
		if (!UfoPublic.strIsEqual(m_repDataParam.getAloneID(),strPrevAloneID) || (m_repDataParam.getPubData()!=null && m_repDataParam.getAloneID()==null && m_repDataParam.getPubData().equals(prevPubData)==false) || !UfoPublic.strIsEqual(m_repDataParam.getReportPK(),strPrevRepPK)){
			CellsModel cellsModel=null;
			if (m_repDataParam.getAloneID()!=null && m_repDataParam.getReportPK()!=null){
				try{
					RepDataOperResultVO result=(RepDataOperResultVO)ActionHandler.execWithZip("com.ufsoft.iuforeport.repdatainput.TableInputActionHandler", "openRepData",
							new Object[]{getRepDataParam(),controler.getLoginEnv(mainBoard)});
					cellsModel=result.getCellsModel();
					cellsModel.setDirty(false);
					m_menuState=result.getMenuState();
					m_hashDynAloneID=result.getHashDynAloneID();
					m_bCanFormInput=result.isFmlCanInput();
					MeasureFmt.setCanInput(result.isFmlCanInput());
					m_bNeedLocateFirstCell=true;
					setInputDir(controler.getLastInputDir());
				}catch(Exception e){
					AppDebug.debug(e);
					com.ufsoft.report.util.UfoPublic.sendErrorMessage(StringResource.getStringResource("miufohbbb00125"), RepDataEditor.this,null);
					//����AloneIDΪ�գ���ʾ��������û����Ч��
					m_repDataParam.setAloneID(null);
				}
			}
			
			//���δ���س��������ݣ�����һ���յ�CellsModel
			if (cellsModel==null){
				cellsModel=CellsModel.getInstance(null, true);
			}
			setCellsModel(cellsModel);
			//�������ɴ˱������ݴ��ڶ�Ӧ�Ĺؼ������
			if (m_keyCondPane!=null){
				m_keyCondPane.removeAllKeyCondComponent();
				remove(m_keyScrollPane);
			}	
			RepDataControler.getInstance(getMainboard()).setLastActiveRepDataEditor(RepDataEditor.this);
			m_keyCondPane=KeyCondPaneUtil.geneKeyCondPane(RepDataEditor.this);
			m_keyScrollPane=new MySCrollPane(m_keyCondPane,JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			add(m_keyScrollPane,BorderLayout.NORTH);
			RepDataEditor.this.setFocusCycleRoot(false);
			RepDataEditor.this.setFocusTraversalPolicyProvider(false);
			RepDataEditor.this.setFocusTraversalKeysEnabled(false);
			m_keyCondPane.initKeyWordPanel();
		}else if (getCellsModel()==null){
			//��ֹCellsModelΪ��
			setCellsModel(CellsModel.getInstance(null, true));
		}
		
		//�ۺϲ�ѯ���ѯ��λ�����£�����ؼ���ֵ��Ч�ұ���ȷ��������û���ҵ���Ч��AloneIDʱ����ʾ�˹ؼ��������µı�������δ¼�룬����������ʾ
		if (RepDataControler.getInstance(getMainboard()).isRepHadOpened() && repDataCond!=null && repDataCond.isKeyValid()==true  && m_repDataParam.getAloneID()==null && m_repDataParam.getReportPK()!=null){
			m_strErrMsg=StringResource.getStringResource("miufohbbb00126");
		}
		
		//�ڱ���������屻Mainboard��ʼ��ʱ������PKΪ�գ��ڱ���PKΪ��ʱ������pubDataΪ�գ��ؼ����������Ϊ��
		if (m_repDataParam.getReportPK()==null){
			m_repDataParam.setAloneID(null);
			m_repDataParam.setPubData(null);
		}
		
		//���ñ����ڵ������Ļ��������ڹ�ʽ׷��
		doSetContext();
		
		revalidate();
		repaint();
	}
	
	/**
	 * ���³�ʼ������
	 * @i18n miufohbbb00125=�򿪱���ʧ��
	 * @i18n miufohbbb00126=����ѡ����ѯ������δ��ѯ���������ݣ���ȷ���ڴ˹ؼ����������Ƿ���¼�����ݻ��Ƿ�������в���Ȩ��
	 */
	public void reInit(){
		innerReInit();
	}
	
	private void refreshContent(){		
		if (m_repDataParam.getAloneID()==null || m_repDataParam.getReportPK()==null)
			return;
		
		if (getTable().getCellEditor()!=null && !getTable().getCellEditor().stopCellEditing())
			return;
		
		if (isDirty()){
    		int iResult = UIUtilities.showConfirmDialog(getMainboard(), NCLangRes.getInstance().getStrByID("20090618","upp09061800030"), NCLangRes.getInstance().getStrByID("20090618","upp09061800031"), JOptionPane.YES_NO_CANCEL_OPTION);
			if(iResult == JOptionPane.CANCEL_OPTION){
				return;
			}
    		if(iResult == JOptionPane.YES_OPTION){
				if (!save())
					return;
			}
		}
			
		getCellsPane().setEnabled(false);
		((KStatusBar)getMainboard().getStatusBar()).processDisplay(StringResource.getStringResource("miufoweb0014"),new Runnable(){
			public void run() {
				try{
					RepDataControler controler=RepDataControler.getInstance(getMainboard());
					
					RepDataOperResultVO result=(RepDataOperResultVO)ActionHandler.execWithZip("com.ufsoft.iuforeport.repdatainput.TableInputActionHandler", "openRepData",
							new Object[]{getRepDataParam(),controler.getLoginEnv(getMainboard())});
					CellsModel cellsModel=result.getCellsModel();
					cellsModel.setDirty(false);
					setCellsModel(cellsModel);
					m_menuState=result.getMenuState();
					m_hashDynAloneID=result.getHashDynAloneID();
					m_bCanFormInput=result.isFmlCanInput();
					MeasureFmt.setCanInput(result.isFmlCanInput());
					m_bNeedLocateFirstCell=true;
					setInputDir(controler.getLastInputDir());
					
					revalidate();
					repaint();
					
					afterEditorActive();
				}catch(Exception e){
					AppDebug.debug(e);
					com.ufsoft.report.util.UfoPublic.sendErrorMessage(StringResource.getStringResource("miufohbbb00125"), getMainboard(),null);
					//����AloneIDΪ�գ���ʾ��������û����Ч��
					m_repDataParam.setAloneID(null);
				}
			}
		});
		getCellsPane().setEnabled(true);
	}
	
	@Override
	public void refresh() {
		refreshContent();
	}

	/**
	 * ��ǰ���ڱ����ǰ�������õķ�������Ϊ��Щ�����ڴ��ڱ�����ǰ����ִ�У�����Щ�ŵ��÷�����ִ�У��÷�����RepDataAcitveListner����
	 */
	public void afterEditorActive(){
		//���������޷���ʱ�����û��ĳ�����ʾ
		if (m_strErrMsg!=null){
			com.ufsoft.report.util.UfoPublic.sendErrorMessage(m_strErrMsg, getMainboard(),null);
			//��ʾ��󣬽�������գ������ظ���ʾ
			m_strErrMsg=null;
		}
		
		//����¼����˱������ݣ���Ҫ��λ����1������¼��ĵ�Ԫ��
		if (m_bNeedLocateFirstCell){
			if (m_menuState!=null && m_menuState.isRepCanModify()){
				new Thread(new Runnable(){
					public void run(){
						while (m_bShutDowned==false){
							CellsPane cellsPane=getCellsPane();
							JViewport port=(JViewport)cellsPane.getParent();
							if (port.getExtentSize().height>0 && port.getExtentSize().width>0){
								List<CellPosition> vCell=getTraceCells();
								if (vCell==null || vCell.size()<=0){
									RepDataControler controler=RepDataControler.getInstance(getMainboard());
									vCell=controler.getTaskCheckCells(RepDataEditor.this);
								}
								
								if (vCell==null || vCell.size()<=0)
									vCell=getRepCheckCell();
								
								if (vCell==null || vCell.size()<=0){
									doSetFirstAnchorCell(null);
								}
								m_bNeedLocateFirstCell=false;
								return;
							}
							try{
								this.wait(100);
							}catch(Exception e){}
						}
					}
				}).start();
			}else{
				m_bNeedLocateFirstCell=false;
			}
		}
		
		if (m_postActive!=null){
			m_postActive.afterRepDataActive(this);
			m_postActive=null;
		}
	}
    
	public KeyCondPanel getKeyCondPane() {
		return m_keyCondPane;
	}

	public boolean isDirty() {
		if (m_repDataParam==null || m_repDataParam.getAloneID()==null)
			return false;
		if (getMenuState()!=null && !getMenuState().isRepCanModify())
			return false;
		
		return getCellsModel()!=null && getCellsModel().isDirty();
	}
	
	public String[] createPluginList() {
		List<String> vPluginName=new ArrayList<String>();
		vPluginName.add(RefreshViewPlugin.class.getName());
		vPluginName.add(UndoPlugin.class.getName());
		vPluginName.add(InputCorePlugin.class.getName());
		vPluginName.add(KeyInputPlugin.class.getName());     //�ؼ���¼��
		vPluginName.add(MeasureInputPlugin.class.getName()); //ָ��¼��	
		vPluginName.add(UfoInputFilePlugin.class.getName());    //����ѡ�񣬱��棬�˳�,�л��ؼ���
		vPluginName.add(PrintPlugin.class.getName());        //��ӡ��ز˵���ҳ�����ã���ӡԤ������ӡ
		vPluginName.add(RepEditPlugin.class.getName());         //�༭��ġ����С����ơ�ճ����ɾ���������
		vPluginName.add(EditInpuDirPlugin.class.getName());
		vPluginName.add(RepHeaderLockPlugin.class.getName());   //��//���ᴰ�ڣ�ȡ������	 
		vPluginName.add(UfoDynAreaInputPlugin.class.getName()); //��̬��¼�� add by ����� 2008-4-8 �����˴˲���ļ���˳��
		vPluginName.add(FindReplacePlugin.class.getName());//���ң��滻
		vPluginName.add(LocationPlugin.class.getName());     //��λ
		vPluginName.add(RepStylePlugin.class.getName());       //��ʾ���
		vPluginName.add(HeaderSizePlugin.class.getName());   //�иߣ��п�
		vPluginName.add(UfoInputDataPlugin.class.getName());
		vPluginName.add(UfoFormulaTracePlugin.class.getName());
		vPluginName.add(UfoFormulaQueryNextPlugin.class.getName());
//		vPluginName.add(UfoExcelExpPlugin.class.getName());
		vPluginName.add(HelpPlugin.class.getName());         //����������\
		//xulm 2009-09-03 ���ӿ�ݼ��������
		vPluginName.add(InputKeyAssistPlugin.class.getName());
		vPluginName.add(CellPostilInputPlugin.class.getName());
		vPluginName.add(FormulaInputPlugin.class.getName());
		vPluginName.add(ViewManagerPlugin.class.getName());
		vPluginName.add(RepInputStatusBarPlugin.class.getName());
		
		//@edit by guogang 2009-5-15
		if("true".equals(getContext().getAttribute(IUfoContextKey.GENRAL_QUERY))){
			vPluginName.add("com.ufida.report.anareport.edit.AnaQueryNewPlugin");	
			vPluginName.add(UfoHBDraftPlugin.class.getName());
		}

		vPluginName.add(MonitorPlugin.class.getName());
		
		return vPluginName.toArray(new String[0]);
	}

	public String getAloneID() {
		return m_repDataParam.getAloneID();
	}

	public MeasurePubDataVO getPubData() {
		return m_pubData;
	}

	public String getRepPK() {
		return m_repDataParam.getReportPK();
	}
	
	public String getTaskPK(){
		return m_repDataParam.getTaskPK();
	}

	public MenuStateData getMenuState() {
		return m_menuState;
	}

	public void setMenuState(MenuStateData state) {
		m_menuState = state;
	}

	public Hashtable<String, Vector<String>> getHashDynAloneID() {
		return m_hashDynAloneID;
	}

	public void setHashDynAloneID(Hashtable<String, Vector<String>> dynAloneID) {
		m_hashDynAloneID = dynAloneID;
	}

	/**
	 * �õ�ͬ��̨�����ı������ݲ������󣬴˴�Ҫ������Դ������Ϣ������ȡ�ö�Ӧ��ǰ��λ������Դ������Ϣ���ŵ�RepDataParam��
	 * @return
	 */
	public IRepDataParam getRepDataParam() {
		RepDataControler controler=RepDataControler.getInstance(getMainboard());
		DataSourceVO dsVO=(DataSourceVO)getMainboard().getContext().getAttribute(IUfoContextKey.DATA_SOURCE);
		if (dsVO!=null){
			DataSourceConfig config=DataSourceConfig.getInstance(getMainboard());
			DataSourceLoginVO login=config.getOneSourceConfig(getPubData().getUnitPK()!=null?getPubData().getUnitPK():controler.getSelectedUnitPK());
			if (login!=null){
				DataSourceInfo dsInfo=m_repDataParam.getDSInfo();
				if (dsInfo==null){
					dsInfo=new DataSourceInfo();
					m_repDataParam.setDSInfo(dsInfo);
				}
				dsInfo.setDSID(dsVO.getId());
				dsInfo.setDSUnitPK(login.getDSUnit());
				dsInfo.setDSUserPK(login.getDSUser());
				dsInfo.setDSPwd(login.getDSPass());
			}
		}
		return m_repDataParam;
	}

	/**
	 * ���ػ���ķ�����һ��Ҫ���������Զ����㹤�߶���һ������CellsAuth
	 */
	public void setCellsModel(CellsModel model) {
		CellsModel oldModel=getCellsModel();
		super.setCellsModel(model);
		
		RepDataControler controler=RepDataControler.getInstance(getMainboard());
		if (oldModel!=model && controler.getReportCalUtilWithoutCreate()!=null 
				&& controler.getReportCalUtilWithoutCreate().getCellsModel()==oldModel)
			controler.reSetReportCalUtil();

		model.setCellsAuth(new TableDataInputAuth(this));
	}
	
	/**
	 * �õ���ǰ���ڶԱ����˽���ļ�ֵ����TaskPK��AloneID�����ֵȷ��
	 * @return
	 */
	public String getTaskCheckKey(){
		return "#"+getTaskPK()+"$"+getAloneID();
	}

	public CheckResultVO getRepCheckResult() {
		return m_repCheckResult;
	}

	/**
	 * �ֹ���˻��Զ����ʱ������������˽������ԭ�����е���˽����Ϣ��գ�������˽�����
	 * @param checkResult
	 */
	public void setRepCheckResult(CheckResultVO checkResult) {
		m_repCheckResult = checkResult;
		m_repCheckDetail=null;
		m_repCheckColor=null;
		m_repCheckCell=null;
		Mainboard mainBoard=getMainboard();
		RepDataControler.getInstance(mainBoard).openCheckResultView(mainBoard,checkResult);
	}
	
	/**
	 * ���û�����˽�����ѡ���˶�λ�����˽��ʱ����Ҫ����λ������˽������Ϣ���
	 */
	public void clearCheckDetail(){
		m_repCheckDetail=null;
		m_repCheckColor=null;
		m_repCheckCell=null;
	}

	public CheckDetailVO getRepCheckDetail() {
		return m_repCheckDetail;
	}

	public void setRepCheckDetail(CheckDetailVO checkDetail) {
		m_repCheckDetail = checkDetail;
	}

	public Color getRepCheckColor() {
		return m_repCheckColor;
	}

	public void setRepCheckColor(Color checkColor) {
		m_repCheckColor = checkColor;
	}

	public List<CellPosition> getRepCheckCell() {
		return m_repCheckCell;
	}

	public void setRepCheckCell(List<CellPosition> checkCell) {
		m_repCheckCell = checkCell;
	}
	
	public VerItem getVerFromPanel(){
		if(this.m_keyCondPane!=null){
			return m_keyCondPane.getDataVerType();
		}
		return null;
	}
	
	public boolean isCanFormInput() {
		return m_bCanFormInput;
	}
	
	public int getInputDir() {
		return m_iInputDir;
	}

	public void setInputDir(int inputDir) {
		m_iInputDir = inputDir;
	}
	
	public void setTraceCells(final List<CellPosition> cells){
		m_traceCells = cells;
		if (cells!=null && cells.size()>0){
			new Thread(new Runnable(){
				public void run(){
					while (m_bShutDowned==false){
						if (m_bNeedLocateFirstCell==false){
							EventQueue.invokeLater(new Runnable(){
								public void run() {
									RepDataEditor.this.editCell(cells.get(0));
									m_traceCells=cells;
								}
							});
							return;
						}
						try{
							this.wait(100);
						}catch(Exception e){}
					}
				}
			}).start();
		}
	}
	
	public List<CellPosition> getTraceCells(){
		return m_traceCells;
	}
	
	protected void initContext(){
		IContext context=getContext();
		if (context!=null){
			RepDataControler.removeFromContext(context);
			FormulaTraceNavigation.removeFromContext(context);
			DataSourceConfig.removeFromContext(context);
		}
	}
	
	/**
	 * ���ñ��ر������ݴ��ڵ������Ļ���
	 */
	private void doSetContext(){
		getContext().setAttribute(IUfoContextKey.REPORT_PK,m_repDataParam.getReportPK());
		getContext().setAttribute(IUfoContextKey.TASK_PK, m_repDataParam.getTaskPK());
		getContext().setAttribute(IUfoContextKey.MEASURE_PUB_DATA_VO,m_repDataParam.getPubData() );
		getContext().setAttribute(IUfoContextKey.DATA_RIGHT, m_menuState==null?0:(m_menuState.isRepCanModify() && m_menuState.isCommited()==false?IUfoContextKey.RIGHT_DATA_WRITE:IUfoContextKey.RIGHT_DATA_READ));
		// @edit by wangyga at 2009-7-20,����02:47:54 û�е�λ�ؼ���
		if(m_repDataParam.getPubData() != null && m_repDataParam.getPubData().getUnitPK() != null){
			getContext().setAttribute(IUfoContextKey.CUR_UNIT_ID, m_repDataParam.getPubData().getUnitPK());
		}
		getContext().setAttribute(IUfoContextKey.DATA_VERSION, m_repDataParam.getPubData()==null?null:new Integer(m_repDataParam.getPubData().getVer()));
		getContext().setAttribute(IUfoContextKey.ALONE_ID, m_repDataParam.getPubData()==null?null:m_repDataParam.getPubData().getAloneID());
	}

	/**
	 * ��λ����1���ɱ༭��Ԫ��
	 */
	public void doSetFirstAnchorCell(final CellPosition anchorCell){
		EventQueue.invokeLater(new Runnable(){
			public void run() {
				if (m_menuState==null || !m_menuState.isRepCanModify())
					return;
				CellsModel cellsModel=getCellsModel();					
				CellsAuthorization auth=cellsModel.getCellsAuth();
				
				if (anchorCell!=null && auth.isWritable(anchorCell.getRow(), anchorCell.getColumn())){
					editCell(anchorCell);
					return;
				}
				int iMaxRow=cellsModel.getRowNum();
				int iMaxCol=cellsModel.getColNum();
				for (int iRow=0;iRow<iMaxRow;iRow++){
					for (int iCol=0;iCol<iMaxCol;iCol++){
						if (auth.isWritable(iRow,iCol)){
							CellPosition cellPos=CellPosition.getInstance(iRow,iCol);
							editCell(cellPos);
							return;
						}
					}
				}
				
			}
			
		});
		
	}
	
	private void editCell(CellPosition anchorCell){
		FormulaTraceBizUtil.setView2HighlightArea(getTable(),anchorCell,true);
		getCellsModel().getSelectModel().setAnchorCell(anchorCell);
		
		CellsPane cellsPane=getCellsPane();		
		TablePane tablePane=cellsPane.getTablePane();
		if (tablePane.isFreezing()){
			if (anchorCell.getRow()>=tablePane.getSeperateRow()){
				if (anchorCell.getColumn()>=tablePane.getSeperateCol()){
					cellsPane=(CellsPane)tablePane.getRightDownView().getView();
				}else{
					cellsPane=(CellsPane)tablePane.getDownView().getView();
				}
			}else{
				if (anchorCell.getColumn()>=tablePane.getSeperateCol()){
					cellsPane=(CellsPane)tablePane.getRightView().getView();
				}else{
					cellsPane=(CellsPane)tablePane.getMainView().getView();
				}
			}
		}
		
		cellsPane.requestFocus();

		MouseEvent event=new MouseEvent(cellsPane,0,0,0,0,0,2,false,0);
		cellsPane.editCellAt(anchorCell.getRow(),anchorCell.getColumn(),event);
	}
	
	public void processEnterAction(boolean bShift){
		int dx=0;
		int dy=0;
		if (getInputDir()==InputDirConstant.DIR_DOWN)
			dy=1;
		else if (getInputDir()==InputDirConstant.DIR_UP)
			dy=-1;
		else if (getInputDir()==InputDirConstant.DIR_RIGHT)
			dx=1;
		else
			dx=-1;
		if (bShift){
			dx=-dx;
			dy=-dy;
		}
		CellsPane table =getCellsPane();
		RepDataKeyActionHelper.doNavigate(table,dx,dy,true);
	}
	
	/**
	 * ���ô��ڱ���
	 * @param report
	 * @param pubData
	 */
	private void doSetTitle(ReportVO report,MeasurePubDataVO pubData){
		if (report==null){
			setTitle("");
			return;
		}
		
		UnitCache unitCache=IUFOUICacheManager.getSingleton().getUnitCache();
		
		String strTitle=report.getCode();
		KeyVO[] keys=null;
		if (pubData!=null)
			keys=pubData.getKeyGroup().getKeys();
		if (keys!=null){
			for (int i=0;i<keys.length;i++){
				String strKeyVal=pubData.getKeywordByIndex(i+1);
				if (strKeyVal==null || strKeyVal.equalsIgnoreCase("null"))
					strKeyVal="";
				
				if (keys[i].getKeywordPK().equals(KeyVO.CORP_PK) || keys[i].getKeywordPK().equals(KeyVO.DIC_CORP_PK)){
					UnitInfoVO unitInfo=unitCache.getUnitInfoByPK(strKeyVal);
					if (unitInfo!=null)
						strKeyVal=unitInfo.getCode();
				}
				
				strTitle+="_"+strKeyVal;
			}
		}
		setTitle(strTitle);
	}	
	
	protected void installKeyboardActions(){		
		RepDataKeyActionHelper.registerKeyAction(this);
		
		registerKeyboardAction(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				if(getCellsPane().hasFocus()||(getTable().getEditorComp()!=null&&getTable().getEditorComp().hasFocus())){
					getCellsPane().removeEditor();
					if(m_keyCondPane!=null){
						m_keyCondPane.requestDefaultFocus();
					}
					
				}else{
					CellPosition anchorCell=null;
					if (getCellsPane().getDataModel()!=null && getCellsPane().getDataModel().getSelectModel()!=null)
						anchorCell=getCellsPane().getDataModel().getSelectModel().getAnchorCell();
					doSetFirstAnchorCell(anchorCell);
				}
				
			}
		}, KeyStroke.getKeyStroke(KeyEvent.VK_F6,0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
	}
	
	private class RefreshViewAction extends TitleAction {

		@Override
		public void execute(Viewer view, ActionEvent e) {
			refreshContent();
		}

		@Override
		public KeyStroke getAccelerator() {
			return KeyStroke.getKeyStroke(KeyEvent.VK_F5,0);
		}

		@Override
		public Icon getIcon() {
			return ResourceManager.createIcon("/images/reportcore/refresh.gif");
		}

		/**
		 * @i18n miufohbbb00120=����
		 */
		@Override
		public String getName() {
			return StringResource.getStringResource("miufopublic251");
		}
	}
	
	private static class MySCrollPane extends UIScrollPane{
		public MySCrollPane(Component view, int vsbPolicy, int hsbPolicy){
			super(view,vsbPolicy,hsbPolicy);
			setLayout(new MyScrollPaneLayout());
			// @edit by wangyga at 2009-8-26,����03:46:44 
			setBorder(BorderFactory.createLineBorder(new Color(0x8AA3BD)));
		}
	}
	
	private static class MyScrollPaneLayout extends ScrollPaneLayout{
		MyScrollPaneLayout(){
		}
		 
	    public void layoutContainer(Container parent) {
			JScrollPane scrollPane = (JScrollPane)parent;
			vsbPolicy = scrollPane.getVerticalScrollBarPolicy();
			hsbPolicy = scrollPane.getHorizontalScrollBarPolicy();
	
			Rectangle availR = scrollPane.getBounds();
			availR.x = availR.y = 0;
			Dimension prefSize=this.getViewport().getView().getPreferredSize();
	
			Insets insets = parent.getInsets();
			availR.x = insets.left;
			availR.y = insets.top;
			availR.width -= insets.left + insets.right;
			availR.height -= insets.top + insets.bottom;
	
		    boolean leftToRight = scrollPane.getComponentOrientation().isLeftToRight();
	
			/* If there's a visible column header remove the space it 
			 * needs from the top of availR.  The column header is treated 
			 * as if it were fixed height, arbitrary width.
			 */
	
			Rectangle colHeadR = new Rectangle(0, availR.y, 0, 0);
	
			if ((colHead != null) && (colHead.isVisible())) {
			    int colHeadHeight = Math.min(availR.height,
		                                         colHead.getPreferredSize().height);
			    colHeadR.height = colHeadHeight; 
			    availR.y += colHeadHeight;
			    availR.height -= colHeadHeight;
			}
	
			/* If there's a visible row header remove the space it needs
			 * from the left or right of availR.  The row header is treated 
			 * as if it were fixed width, arbitrary height.
			 */
	
			Rectangle rowHeadR = new Rectangle(0, 0, 0, 0);
			
			if ((rowHead != null) && (rowHead.isVisible())) {
			    int rowHeadWidth = Math.min(availR.width,
		                                        rowHead.getPreferredSize().width);
			    rowHeadR.width = rowHeadWidth;
			    availR.width -= rowHeadWidth;
		            if ( leftToRight ) {
		                rowHeadR.x = availR.x;
		                availR.x += rowHeadWidth;
		            } else {
		                rowHeadR.x = availR.x + availR.width;
		            }
			}
	
			/* If there's a JScrollPane.viewportBorder, remove the
			 * space it occupies for availR.
			 */
	
			Border viewportBorder = scrollPane.getViewportBorder();
			Insets vpbInsets;
			if (viewportBorder != null) {
			    vpbInsets = viewportBorder.getBorderInsets(parent);
			    availR.x += vpbInsets.left;
			    availR.y += vpbInsets.top;
			    availR.width -= vpbInsets.left + vpbInsets.right;
			    availR.height -= vpbInsets.top + vpbInsets.bottom;
			}
			else {
			    vpbInsets = new Insets(0,0,0,0);
			}
	
			Component view = (viewport != null) ? viewport.getView() : null;
			Dimension viewPrefSize =  
			    (view != null) ? view.getPreferredSize() 
		                           : new Dimension(0,0);
	
			Dimension extentSize = (viewport != null) ? viewport.toViewCoordinates(availR.getSize()) 
			  : new Dimension(0,0);
	
			boolean viewTracksViewportWidth = false;
			boolean viewTracksViewportHeight = false;
		        boolean isEmpty = (availR.width < 0 || availR.height < 0);
			Scrollable sv;
			if (!isEmpty && view instanceof Scrollable) {
			    sv = (Scrollable)view;
			    viewTracksViewportWidth = sv.getScrollableTracksViewportWidth();
			    viewTracksViewportHeight = sv.getScrollableTracksViewportHeight();
			}
			else {
			    sv = null;
			}
	
			Rectangle vsbR = new Rectangle(0, availR.y - vpbInsets.top, 0, 0);
	
			boolean vsbNeeded;
		        if (isEmpty) {
		            vsbNeeded = false;
		        }
			else if (vsbPolicy == VERTICAL_SCROLLBAR_ALWAYS) {
			    vsbNeeded = true;
			}
			else if (vsbPolicy == VERTICAL_SCROLLBAR_NEVER) {
			    vsbNeeded = false;
			}
			else {  // vsbPolicy == VERTICAL_SCROLLBAR_AS_NEEDED
			    vsbNeeded = !viewTracksViewportHeight && (viewPrefSize.height > extentSize.height);
			}
	
	
			if ((vsb != null) && vsbNeeded) {
			    adjustForVSB(true, availR, vsbR, vpbInsets, leftToRight);
			    extentSize = viewport.toViewCoordinates(availR.getSize());
			}
			
			/* If there's a horizontal scrollbar and we need one, allocate
			 * space for it (we'll make it visible later). A horizontal 
			 * scrollbar is considered to be fixed height, arbitrary width.
			 */
	
			Rectangle hsbR = new Rectangle(availR.x - vpbInsets.left, 0, 0, 0);
			boolean hsbNeeded;
		        if (isEmpty) {
		            hsbNeeded = false;
		        }
			else if (hsbPolicy == HORIZONTAL_SCROLLBAR_ALWAYS) {
			    hsbNeeded = true;
			}
			else if (hsbPolicy == HORIZONTAL_SCROLLBAR_NEVER) {
			    hsbNeeded = false;
			}
			else {  // hsbPolicy == HORIZONTAL_SCROLLBAR_AS_NEEDED
			    hsbNeeded = !viewTracksViewportWidth && (viewPrefSize.width > extentSize.width);
			}
	
			if ((hsb != null) && hsbNeeded) {
			    adjustForHSB(true, availR, hsbR, vpbInsets);
	
			    /* If we added the horizontal scrollbar then we've implicitly 
			     * reduced  the vertical space available to the viewport. 
			     * As a consequence we may have to add the vertical scrollbar, 
			     * if that hasn't been done so already.  Of course we
			     * don't bother with any of this if the vsbPolicy is NEVER.
			     */
			    if ((vsb != null) && !vsbNeeded &&
				(vsbPolicy != VERTICAL_SCROLLBAR_NEVER)) {
	
				extentSize = viewport.toViewCoordinates(availR.getSize());
				vsbNeeded = viewPrefSize.height > extentSize.height;
	
				if (vsbNeeded) {
				    adjustForVSB(true, availR, vsbR, vpbInsets, leftToRight);
				}
			    }
			}
	
			/* Set the size of the viewport first, and then recheck the Scrollable
			 * methods. Some components base their return values for the Scrollable
			 * methods on the size of the Viewport, so that if we don't
			 * ask after resetting the bounds we may have gotten the wrong
			 * answer.
			 */
			
			if (viewport != null) {
				mySetViewSize(availR,viewport,prefSize);
			 //   viewport.setBounds(availR);
	
			    if (sv != null) {
				extentSize = viewport.toViewCoordinates(availR.getSize());
	
				boolean oldHSBNeeded = hsbNeeded;
				boolean oldVSBNeeded = vsbNeeded;
				viewTracksViewportWidth = sv.
				                          getScrollableTracksViewportWidth();
				viewTracksViewportHeight = sv.
				                          getScrollableTracksViewportHeight();
				if (vsb != null && vsbPolicy == VERTICAL_SCROLLBAR_AS_NEEDED) {
				    boolean newVSBNeeded = !viewTracksViewportHeight &&
				                     (viewPrefSize.height > extentSize.height);
				    if (newVSBNeeded != vsbNeeded) {
					vsbNeeded = newVSBNeeded;
					adjustForVSB(vsbNeeded, availR, vsbR, vpbInsets,
						     leftToRight);
					extentSize = viewport.toViewCoordinates
					                      (availR.getSize());
				    }
				}
				if (hsb != null && hsbPolicy ==HORIZONTAL_SCROLLBAR_AS_NEEDED){
				    boolean newHSBbNeeded = !viewTracksViewportWidth &&
					               (viewPrefSize.width > extentSize.width);
				    if (newHSBbNeeded != hsbNeeded) {
					hsbNeeded = newHSBbNeeded;
					adjustForHSB(hsbNeeded, availR, hsbR, vpbInsets);
					if ((vsb != null) && !vsbNeeded &&
					    (vsbPolicy != VERTICAL_SCROLLBAR_NEVER)) {
	
					    extentSize = viewport.toViewCoordinates
						         (availR.getSize());
					    vsbNeeded = viewPrefSize.height >
						        extentSize.height;
	
					    if (vsbNeeded) {
						adjustForVSB(true, availR, vsbR, vpbInsets,
							     leftToRight);
					    }
					}
				    }
				}
				if (oldHSBNeeded != hsbNeeded ||
				    oldVSBNeeded != vsbNeeded) {
				    //viewport.setBounds(availR);
					mySetViewSize(availR,viewport,prefSize);
				    // You could argue that we should recheck the
				    // Scrollable methods again until they stop changing,
				    // but they might never stop changing, so we stop here
				    // and don't do any additional checks.
				}
			    }
			}
	
			/* We now have the final size of the viewport: availR.
			 * Now fixup the header and scrollbar widths/heights.
			 */
			vsbR.height = availR.height + vpbInsets.top + vpbInsets.bottom;
			hsbR.width = availR.width + vpbInsets.left + vpbInsets.right;
			rowHeadR.height = availR.height + vpbInsets.top + vpbInsets.bottom;
		        rowHeadR.y = availR.y - vpbInsets.top;
			colHeadR.width = availR.width + vpbInsets.left + vpbInsets.right;
		        colHeadR.x = availR.x - vpbInsets.left;
	
			/* Set the bounds of the remaining components.  The scrollbars
			 * are made invisible if they're not needed.
			 */
			
			if (rowHead != null) {
			    rowHead.setBounds(rowHeadR);
			}
	
			if (colHead != null) {
			    colHead.setBounds(colHeadR);
			}
	
			if (vsb != null) {
			    if (vsbNeeded) {
				vsb.setVisible(true);
				vsb.setBounds(vsbR);
			    }
			    else {
				vsb.setVisible(false);
			    }
			}
	
			if (hsb != null) {
			    if (hsbNeeded) {
				hsb.setVisible(true);
				hsb.setBounds(hsbR);
			    }
			    else {
				hsb.setVisible(false);
			    }
			}
	
			if (lowerLeft != null) {
			    lowerLeft.setBounds(leftToRight ? rowHeadR.x : vsbR.x,
		                                hsbR.y,
		                                leftToRight ? rowHeadR.width : vsbR.width,
		                                hsbR.height);
			}
	
			if (lowerRight != null) {
			    lowerRight.setBounds(leftToRight ? vsbR.x : rowHeadR.x,
		                                 hsbR.y,
		                                 leftToRight ? vsbR.width : rowHeadR.width,
		                                 hsbR.height);
			}
	
			if (upperLeft != null) {
			    upperLeft.setBounds(leftToRight ? rowHeadR.x : vsbR.x,
		                                colHeadR.y,
		                                leftToRight ? rowHeadR.width : vsbR.width,
		                                colHeadR.height);
			}
	
			if (upperRight != null) {
			    upperRight.setBounds(leftToRight ? vsbR.x : rowHeadR.x,
		                                 colHeadR.y,
		                                 leftToRight ? vsbR.width : rowHeadR.width,
		                                 colHeadR.height);
			}
	    }
	    
	    private void mySetViewSize(Rectangle rec,Component comp,Dimension prefSize){
	    	int iHeight=28;
	    	int y=(int)rec.getHeight()/2-iHeight/2;
	    	rec.y=y;
	    	rec.height=iHeight;
	    	if (rec.width>prefSize.width)
	    		rec.width=prefSize.width;
	    	comp.setBounds(rec);
	    }
	    
	    /**
	     * Adjusts the <code>Rectangle</code> <code>available</code> based on if
	     * the vertical scrollbar is needed (<code>wantsVSB</code>).
	     * The location of the vsb is updated in <code>vsbR</code>, and
	     * the viewport border insets (<code>vpbInsets</code>) are used to offset
	     * the vsb. This is only called when <code>wantsVSB</code> has
	     * changed, eg you shouldn't invoke adjustForVSB(true) twice.
	     */
	    private void adjustForVSB(boolean wantsVSB, Rectangle available,
				      Rectangle vsbR, Insets vpbInsets, 
	                              boolean leftToRight) {
	        int oldWidth = vsbR.width;
		if (wantsVSB) {
	            int vsbWidth = Math.max(0, Math.min(vsb.getPreferredSize().width,
	                                                available.width));

		    available.width -= vsbWidth;
		    vsbR.width = vsbWidth;
	            
	            if( leftToRight ) {
	                vsbR.x = available.x + available.width + vpbInsets.right;
	            } else {
	                vsbR.x = available.x - vpbInsets.left;
	                available.x += vsbWidth;
	            }
		}
		else {
		    available.width += oldWidth;
		}
	    }

	    /**
	     * Adjusts the <code>Rectangle</code> <code>available</code> based on if
	     * the horizontal scrollbar is needed (<code>wantsHSB</code>).
	     * The location of the hsb is updated in <code>hsbR</code>, and
	     * the viewport border insets (<code>vpbInsets</code>) are used to offset
	     * the hsb.  This is only called when <code>wantsHSB</code> has
	     * changed, eg you shouldn't invoked adjustForHSB(true) twice.
	     */
	    private void adjustForHSB(boolean wantsHSB, Rectangle available,
				      Rectangle hsbR, Insets vpbInsets) {
	        int oldHeight = hsbR.height;
		if (wantsHSB) {
	            int hsbHeight = Math.max(0, Math.min(available.height,
	                                              hsb.getPreferredSize().height));

		    available.height -= hsbHeight;
		    hsbR.y = available.y + available.height + vpbInsets.bottom;
		    hsbR.height = hsbHeight;
		}
		else {
		    available.height += oldHeight;
		}
	    }
	}
}
 