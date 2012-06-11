package nc.ui.iufo.input.view;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import nc.pub.iufo.accperiod.IUFODefaultNCAccSchemeUtil;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iufo.data.MeasurePubDataBO_Client;
import nc.ui.iufo.input.InputUtil;
import nc.ui.iufo.input.control.RepDataControler;
import nc.ui.iufo.unit.UnitMngBO_Client;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UILabel;
import nc.ui.pub.beans.UIPanel;
import nc.vo.iufo.data.MeasurePubDataVO;
import nc.vo.iufo.data.VerItem;
import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iufo.keydef.KeyVO;
import nc.vo.iufo.unit.UnitInfoVO;
import nc.vo.iuforeport.rep.ReportVO;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.iufo.pub.tools.DateUtil;
import com.ufida.zior.console.ActionHandler;
import com.ufida.zior.view.Editor;
import com.ufida.zior.view.Mainboard;
import com.ufsoft.iufo.check.ui.CheckResultBO_Client;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.inputplugin.biz.file.ChangeKeywordsData;
import com.ufsoft.iufo.inputplugin.biz.file.GeneralQueryUtil;
import com.ufsoft.iufo.inputplugin.inputcore.AccTimeRefTextField;
import com.ufsoft.iufo.inputplugin.inputcore.CodeRefTextField;
import com.ufsoft.iufo.inputplugin.inputcore.TimeRefTextField;
import com.ufsoft.iufo.inputplugin.inputcore.UnitRefTextField;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.report.util.UserListComponentPolicy;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.header.TableHeader;
import com.ufsoft.table.re.RefTextField;

/**
 * 综合查询条件查询面板
 * @author guogang
 * 2009-06-07
 */
public class KeyCondPanel extends UIPanel implements IUfoContextKey,Scrollable{
	private static final long serialVersionUID = 5975546266903807398L;

	private static final int KEYWORDS_MAX_COUNT = 10;
	private boolean isGeneQeury=false;
	private String m_strRepPK=null;
	private String m_strTaskPK=null;
    /**
     * 已录入关键字值数组
     */
    private String[] m_strInputKeyValues = null;
    /**
     * 切换关键字之前的值
     */
    private String[] m_strOldInputKeyValues = null;

    /**
     * 关键字是否被修改
     */
    private boolean m_bKeyModified = false;
    /**
     * 要录入的关键字信息
     */
    private ChangeKeywordsData[] m_changeKeywordsDatas = null;
    /**
     * 切换版本之前的值
     */
    private VerItem[] m_strOldVersionValues = new VerItem[2];
    
    /**
     * 关键字列表显示JPanel
     */
    private JPanel m_jKeywordPanel = null;
    
    private JTextField[] m_jTextFieldKeywords =null;
    private JLabel[]     m_jLabelKeywords     = null;
    private JButton      m_btnViewnReport     = null;
    //版本
    private JComboBox m_jCombVersionNum = null;
    //子版本
    private JComboBox m_jCombSubVersionNum = null;
    
    private KeyDocumentListener m_objKeyDocumentListener = null;
    private KeyInputListener m_objKeyInputListener = null;
    private KeyFocusAdapter m_objKeyFocusAdapter=null;
    /**
     * 标识关键字组件是否初始化完成
     */
    private boolean isInitKeyTextField = false;
    /**
	 * ChooseCordP1anel 构造子
	 * @param ufoReport
	 */
	public KeyCondPanel(ChangeKeywordsData[] changeKeywordsDatas,boolean isGeneQeury,String strTaskPK,String strRepPK,VerItem ver,VerItem subVer){
		m_changeKeywordsDatas = changeKeywordsDatas;
		this.isGeneQeury=isGeneQeury;
		m_strTaskPK=strTaskPK;
		m_strRepPK=strRepPK;
		m_strOldVersionValues[0]=ver;
		m_strOldVersionValues[1]=subVer;
		initUI();
	}
	
	public Dimension getPreferredSize() {
		Dimension prefSize=super.getPreferredSize();
		if (prefSize==null)
			return prefSize;
		
		Dimension size=getSize();
		if (size.width>0 && size.width>=prefSize.width)
			return new Dimension(size.width,43);
		else
			return new Dimension(prefSize.width,43);
	}	
	
	public Insets getInsets(){
		return new Insets(1,0,0,0);
	}
	
	/**
     * This method initializes this
     * 
     * @return void
     */
    private void initUI() {
    	this.setLayout(new BorderLayout());
    	this.setBorder(null);
    	this.add(getKeywordPanel(), BorderLayout.CENTER);
    }
    
    public void removeAllKeyCondComponent() {
		m_bKeyModified = false;
		setFocusTraversalPolicyProvider(false);
		setFocusTraversalPolicy(null);
		getKeywordPanel().removeAll();
		this.m_objKeyDocumentListener = null;
		this.m_objKeyFocusAdapter = null;
		this.m_objKeyInputListener = null;
		if (m_jTextFieldKeywords != null) {
			for (int i = 0; i < m_jTextFieldKeywords.length; i++) {
				if (m_jTextFieldKeywords[i] instanceof RefTextField) {
					((RefTextField) m_jTextFieldKeywords[i]).setRefComp(null,null);
				}
			}
		}

	}
    
    public void initKeyWordPanel() {
		isInitKeyTextField = false;

		m_jTextFieldKeywords = new JTextField[KEYWORDS_MAX_COUNT];
		m_jLabelKeywords = new nc.ui.pub.beans.UILabel[KEYWORDS_MAX_COUNT];
		removeAllKeyCondComponent();
		ArrayList<Component> comList = new ArrayList<Component>();
		// 根据报表关键字设置创建查询面板
		ChangeKeywordsData keyData = null;
		int nColCount = getKeyColCount();
		m_strOldInputKeyValues=new String[nColCount];
		for (int nColIndex = 0; nColIndex < nColCount; nColIndex++) {
			keyData = getChangeKeywordsData(nColIndex);
			if (keyData != null) {
				m_strOldInputKeyValues[nColIndex]=keyData.getKeyValue();
				m_jLabelKeywords[nColIndex] = getKeyWordsLabel(nColIndex==0,keyData
						.getKeyName());
				m_jTextFieldKeywords[nColIndex] = getKeywordTextField(keyData);
				getKeywordPanel().add(m_jLabelKeywords[nColIndex]);
				getKeywordPanel().add(m_jTextFieldKeywords[nColIndex]);
				comList.add(m_jTextFieldKeywords[nColIndex]);
			}
		}

		if (nColCount > 0 && this.isGeneQeury) {// 综合查询
			getKeywordPanel().add(
					getKeyWordsLabel(false,MultiLang.getString("uiuforep00138")));
			getKeywordPanel().add(getVersionComb());
			comList.add(getVersionComb());
			getKeywordPanel().add(
					getKeyWordsLabel(false,MultiLang.getString("uiuforep00139")));
			getKeywordPanel().add(getSubVersionComb());
			comList.add(getSubVersionComb());
		}
		getKeywordPanel().add(getQueryButton());
		comList.add(getQueryButton());
		setFocusTraversalPolicy(new UserListComponentPolicy(comList));
		setFocusCycleRoot(true);
		setFocusTraversalPolicyProvider(true);
//		Set<AWTKeyStroke> forwardKeySet = new HashSet<AWTKeyStroke>();
//		forwardKeySet.add(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0));
//		forwardKeySet.add(KeyStroke.getKeyStroke(KeyEvent.VK_TAB, 0));
//		Set<AWTKeyStroke> backwardKeySet = new HashSet<AWTKeyStroke>();
//		backwardKeySet.add(KeyStroke.getKeyStroke(KeyEvent.VK_TAB,
//				KeyEvent.SHIFT_MASK));
//		setFocusTraversalKeys(KeyboardFocusManager.FORWARD_TRAVERSAL_KEYS,
//				forwardKeySet);
//		setFocusTraversalKeys(KeyboardFocusManager.BACKWARD_TRAVERSAL_KEYS,
//				backwardKeySet);
		repaint();
		updateVerCompSet(true);
		isInitKeyTextField = true;
		m_bKeyModified=false;
	}
    
    private void updateVerCompSet(boolean isInit){
    	isInitKeyTextField=false;
    	if(this.isGeneQeury){
    		isInitKeyTextField=false;
	         VerItem[] newVers=this.getVersionByCond();
	         VerItem temp;
	         if (m_strOldVersionValues[0] != null && newVers != null) {
				for (int i = 0; i < newVers.length; i++) {
					if (newVers[i].getVerPK().equals(
							m_strOldVersionValues[0].getVerPK())) {
						temp = newVers[0];
						newVers[0] = newVers[i];
						newVers[i] = temp;
						break;
					}
				}

			}
	       this.addVerItemsToList(newVers);
	       newVers=this.getSubVersionByCond();
	        if (m_strOldVersionValues[1] != null && newVers != null) {
				for (int i = 0; i < newVers.length; i++) {
					if (KeyCondPaneUtil.getSWVer(newVers[i].getVerPK()) == KeyCondPaneUtil
							.getSWVer(m_strOldVersionValues[1].getVerPK())) {
						temp = newVers[0];
						newVers[0] = newVers[i];
						newVers[i] = temp;
						break;
					}
				}

			}
	       this.addSubVerItemsToList(newVers);
       }
    	isInitKeyTextField=true;
    }
    
    /**
     * 初始化查询条件面板，包括报表列表、报表关键字及其他(如版本)条件
     *  
     * @return javax.swing.JPanel   
     */    
    public JPanel getKeywordPanel() {
    	if(m_jKeywordPanel == null) {
    		m_jKeywordPanel = new UIPanel();
        	//查询条件左对齐排列
        	FlowLayout flowLayout = new FlowLayout(FlowLayout.LEFT);
        	m_jKeywordPanel.setLayout(flowLayout);
        }
        return m_jKeywordPanel;
    }
    
   private JLabel getKeyWordsLabel(boolean bFirst,String labelName){
	   UILabel label= new nc.ui.pub.beans.UILabel();
//	   label.setPreferredSize(new Dimension(55, 20));
	   label.setText((bFirst?" ":"    ")+labelName);
	   label.setHorizontalAlignment(SwingConstants.RIGHT);
	   return label;
   }
    /**
     * 加入一组关键字到关键字面板
     * @param parentPanel
     * @param nColIndex
     */
   private JTextField getKeywordTextField(ChangeKeywordsData changeKeywordsData){     
	   Mainboard mainBoard=getMainboard(this);
	   JTextField jTextFieldKeyword = null;
        if(changeKeywordsData.isTimeType()){
        	try {
        		Date defaultDate = null;
        		if(changeKeywordsData.getKeyValue() != null){
        			defaultDate = DateUtil.getDayFormat().parse(changeKeywordsData.getKeyValue());//"2006-12-10")	
        		}       		
        		jTextFieldKeyword = new TimeRefTextField(changeKeywordsData.getKeywordPK(),defaultDate);	
			} catch (ParseException e) {
				AppDebug.debug(e);
			}
        } else if(changeKeywordsData.isAccPeriodTimeType()){//add by 王宇光 2008-6-13 添加会计期间参照
        	String strKeyValue = changeKeywordsData.getKeyValue();
        	String strAccPeriodPk = getAccPeriodPk();
        	if(strAccPeriodPk == null || strAccPeriodPk.trim().length() == 0){
        		strAccPeriodPk = IUFODefaultNCAccSchemeUtil.getInstance().getIUFODefaultNCAccScheme();
        	}
			String strKeyWordPk = changeKeywordsData.getKeywordPK();
			jTextFieldKeyword= new AccTimeRefTextField(strKeyValue,strAccPeriodPk,strKeyWordPk);
        }else if(changeKeywordsData.isCorpKey() || changeKeywordsData.isDicCorpKey()){
        	UnitInfoVO unitInfo=null;
        	try{
        		if (changeKeywordsData.isDicCorpKey())
        			unitInfo=IUFOUICacheManager.getSingleton().getUnitCache().getRootUnitInfo();
        		else
        			unitInfo=UnitMngBO_Client.loadUnitInfoByIds(new String[]{RepDataControler.getInstance(mainBoard).getCurUserInfo(mainBoard).getUnitId()})[0];
        	}catch(Exception e){};
        	jTextFieldKeyword = new UnitRefTextField(unitInfo.getCode(), (String)mainBoard.getContext().getAttribute(IUfoContextKey.ORG_PK));
        } else if(changeKeywordsData.getKeyRef() != null){//普通参照类型
        	jTextFieldKeyword= new CodeRefTextField(changeKeywordsData.getKeyRef());
        } else {
        	jTextFieldKeyword= new nc.ui.pub.beans.UITextField();
        }

        //在报表数据状态下，不显示版本及子版本查询条件，并且不加入查询版本的事件处理
        jTextFieldKeyword.setPreferredSize(new Dimension(120, 20));
        jTextFieldKeyword.setDocument(new KeyWordDoc());
        jTextFieldKeyword.setText(changeKeywordsData.getKeyValue());
        //关键字值发生变化时，需要提示是否重新加载数据(数据录入和综合查询)
        jTextFieldKeyword.getDocument().addDocumentListener(getDocumentListener());
        jTextFieldKeyword.addKeyListener(getKeyInputListener());
        addKeyFoucusAdapter(jTextFieldKeyword);

        return jTextFieldKeyword;
    }
    private KeyDocumentListener getDocumentListener(){
    	if(m_objKeyDocumentListener == null){
    		m_objKeyDocumentListener = new KeyDocumentListener();
    	}
    	return m_objKeyDocumentListener;
    }
    
    private KeyInputListener getKeyInputListener(){
    	if(m_objKeyInputListener == null){
    		m_objKeyInputListener = new KeyInputListener();
    	}
    	return m_objKeyInputListener;
    }
    public void addKeyFoucusAdapter(Component componet){
    	if(componet==null)
    		return;
    	if(m_objKeyFocusAdapter==null){
    		m_objKeyFocusAdapter=new KeyFocusAdapter();
    	}
    	componet.addFocusListener(m_objKeyFocusAdapter);
    }
    /**
     * 获得任务的会计期间方案主键
     * @param
     * @return String
     */
    private String getAccPeriodPk(){
    	String strAccPreiodPk = InputUtil.getAccSechemePK(this.m_strTaskPK);//会计期间PK
    	return strAccPreiodPk;
    }
    
    
    private JButton getQueryButton() {
		if (m_btnViewnReport == null) {
			m_btnViewnReport = new UIButton(MultiLang
					.getString("uiuforep00140"));
			m_btnViewnReport.setPreferredSize(new Dimension(50, 20));
			m_btnViewnReport.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					EventQueue.invokeLater(new Runnable() {
						public void run() {
							doOpenReportByCond(isGeneQeury);
						}
					});
					
				}
			});
			m_btnViewnReport.addKeyListener(getKeyInputListener());
		}
		return m_btnViewnReport;
	}
    
    private JComboBox getVersionComb() {
		if (m_jCombVersionNum == null) {
			m_jCombVersionNum = new JComboBox();
			m_jCombVersionNum.setPreferredSize(new Dimension(120, 20));
			m_jCombVersionNum.addItemListener(new ItemListener(){
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange()==ItemEvent.SELECTED){
						VerItem verItem = (VerItem)m_jCombVersionNum.getSelectedItem();
    					if(verItem == null||!isInitKeyTextField){
    						return;
    					}
    					m_strOldVersionValues[0]=verItem;
    					addSubVerItemsToList(getSubVersionByCond());
    					m_bKeyModified=true;
					}
					
				}
				
			});
			addKeyFoucusAdapter(m_jCombVersionNum);
		}
		return m_jCombVersionNum;
	}
    
    private JComboBox getSubVersionComb() {
		if (m_jCombSubVersionNum == null) {
			m_jCombSubVersionNum = new JComboBox();
			m_jCombSubVersionNum.setPreferredSize(new Dimension(120, 20));
			m_jCombSubVersionNum.addItemListener(new ItemListener(){
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange()==ItemEvent.SELECTED){
						VerItem verItem = (VerItem)m_jCombSubVersionNum.getSelectedItem();
    					if(verItem == null||!isInitKeyTextField){
    						return;
    					}
    					m_strOldVersionValues[1]=verItem;
    					m_bKeyModified=true;
					}
					
				}
				
			});
			addKeyFoucusAdapter(m_jCombSubVersionNum);
		}
		return m_jCombSubVersionNum;
	}
    
    public VerItem getDataVerType(){
    	VerItem verItem =null;
    	if(getVersionComb().getItemCount() > 0){
    		if(getVersionComb().getSelectedIndex() == -1 )
    			getVersionComb().setSelectedIndex(0);
    		verItem = (VerItem)getVersionComb().getSelectedItem();
    	}
    	return verItem;
    }
    
    /**
     * 返回数据子版本，为选择时，缺省规则待定
     * @return
     */
    public VerItem getDataSubVerType() {
    	VerItem verItem =null;
		if (getSubVersionComb().getItemCount() > 0) {
			if (getSubVersionComb().getSelectedIndex() == -1)
				getSubVersionComb().setSelectedIndex(0);
			verItem= (VerItem) getSubVersionComb().getSelectedItem();
		}
		return verItem;
	}
    
    private void removeAllVerItemsOfList(){
    	getVersionComb().removeAllItems();
    	getSubVersionComb().removeAllItems();
    }
    private void removeAllSubVerItemsOfList(){
    	getSubVersionComb().removeAllItems();
    }
   
    /**
     * 加入可选数据版本到版本列表中
     * @param vers
     */
    public void addVerItemsToList(VerItem[] vers) {
		removeAllVerItemsOfList();
		if (vers == null || vers.length == 0) {
			return;
		}
		for (VerItem ver : vers) {
			getVersionComb().addItem(ver);
		}

	}
    
    /**
	 * 加入可选数据版本到子版本列表中
	 * 
	 * @param vers
	 */
    public void addSubVerItemsToList(VerItem[] vers) {
		removeAllSubVerItemsOfList();
		if (vers == null || vers.length == 0) {
			return;
		}
		for (VerItem ver : vers) {
			getSubVersionComb().addItem(ver);
		}
	}
    
    
	private ChangeKeywordsData getChangeKeywordsData(int nIndex){
        return m_changeKeywordsDatas[nIndex];
    }
    
    private int getKeyColCount(){
        if(m_changeKeywordsDatas != null){
            return m_changeKeywordsDatas.length;
        }
        return 0;
    }
    
    /**
     * 返回综合查询关键字设置，根据查询方式设置关键字值
     * @return
     */
    public String[] getInputKeyValues(){
    	m_strInputKeyValues = new String[getKeyColCount()];
    	for(int i =0;i < getKeyColCount();i++){
    		m_strInputKeyValues[i] = m_jTextFieldKeywords[i].getText();
    	}

    	return m_strInputKeyValues;
    }
    
	/**
     * 根据选定报表及关键字条件执行综合并打开报表
	 * @i18n uiuforep00143=根据选定查询条件，未查询到报表数据！请确认在此关键字条件下是否有录入数据!
	 * @i18n uiuforep00141=请在单位树上选择查询单位！
	 * @i18n uiuforep00142=请在报表树上选择查询报表！
	 * @i18n uiuforep00129=查询关键字不得为空
	 * @i18n miufohbbb00129=对不起，没有查看该报表数据的权限
     *
     */
    private void doOpenReportByCond(boolean isCheckVersion){  	
    	m_bKeyModified = false;
    	//检查冠军字是否未录入
    	String[] strInputKeyVals=getInputKeyValues();
    	boolean hasInput = GeneralQueryUtil.checkKeyInput(strInputKeyVals);
		if(!hasInput){
			javax.swing.JOptionPane.showMessageDialog(null, MultiLang.getString("uiuforep00129"));
			resetPanel();
			return;
		}
    	
    	try{
    		Mainboard mainBoard=getMainboard(this);
    		RepDataControler controler=RepDataControler.getInstance(mainBoard);
    		
    		MeasurePubDataVO pubData=null;
    		MeasurePubDataVO oldPubData=null;
    		if(controler.getLastActiveRepDataEditor()!=null){
    			oldPubData=controler.getLastActiveRepDataEditor().getPubData();
    		}
    		
    		VerItem verItem =getDataVerType();
    		if(verItem==null&&isCheckVersion){
    			addVerItemsToList(getVersionByCond());
//    			UfoPublic.sendWarningMessage(MultiLang.getString("uiuforep00104"), null);
    		}
    		int iDataVer=controler.getDataVer();
    		if(verItem!=null&&verItem.getVerPK()!=null){
    			String[] strDataVers = verItem.getVerPK().split("@");
    			iDataVer = Integer.parseInt(strDataVers[0]);
    		}
    		VerItem subVerItem=getDataSubVerType();
    		String aloneid=null;
    		if(subVerItem!=null&&subVerItem.getVerPK()!=null){
    			aloneid=subVerItem.getVerPK();
    			int iPos=aloneid.indexOf("@");
    			aloneid=aloneid.substring(iPos+1);
    		}
    		if(this.isGeneQeury){
    			if (aloneid!=null)
    				pubData=MeasurePubDataBO_Client.findByAloneID(aloneid);
    			else{
    				pubData=new MeasurePubDataVO();
    				ReportVO reportVO=IUFOUICacheManager.getSingleton().getReportCache().getByPK(getRepPK());
    				KeyGroupVO keyGroup=IUFOUICacheManager.getSingleton().getKeyGroupCache().getByPK(reportVO.getKeyCombPK());
    				pubData.setKType(reportVO.getKeyCombPK());
    				pubData.setKeyGroup(keyGroup);
    				pubData.setAccSchemePK(InputUtil.getAccSechemePK(m_strTaskPK));
    				pubData.setKeywords(strInputKeyVals);
    			}
    		}else{
    			pubData=(MeasurePubDataVO)ActionHandler.exec("nc.ui.iufo.input.RepDataActionHandler", "getNewMeasurePubDataVO",
    					new Object[]{m_strTaskPK,controler.getCurUserInfo(mainBoard),strInputKeyVals,iDataVer,m_strRepPK,(String) mainBoard.getContext().getAttribute(
								IUfoContextKey.ORG_PK) });	
    			
//    			if (pubData==null){
//	    			pubData=new MeasurePubDataVO();
//					ReportVO reportVO=IUFOUICacheManager.getSingleton().getReportCache().getByPK(getRepPK());
//					KeyGroupVO keyGroup=IUFOUICacheManager.getSingleton().getKeyGroupCache().getByPK(reportVO.getKeyCombPK());
//					pubData.setKType(reportVO.getKeyCombPK());
//					pubData.setKeyGroup(keyGroup);
//					pubData.setAccSchemePK(InputUtil.getAccSechemePK(m_strTaskPK));
//					pubData.setKeywords(strInputKeyVals);
//					pubData.setVer(iDataVer);
//    			}
    			
//    			if (pubData!=null && pubData.getAloneID()!=null && controler.getDataVer()>=1000){
//    				if (!CheckResultBO_Client.isAloneIDInput(pubData.getAloneID(), new String[]{m_strRepPK})){
//    					UfoPublic.sendErrorMessage(StringResource.getStringResource("miufohbbb00126"), controler.getMainBoard(), null);
//    					return;
//    				}
//    			}
//    			if (pubData==null){
//    				UfoPublic.sendErrorMessage(StringResource.getStringResource("miufohbbb00129"), controler.getMainBoard(), null);
//    				return;
//    			}
    		}
    		
    		
    		if(oldPubData!=null&&oldPubData.getAloneID()!=null&&!oldPubData.getAloneID().equals(pubData.getAloneID())){
    			resetPanel();
    		}
    		
    		
    		KeyVO[] keys=pubData.getKeyGroup().getKeys();
    		for (int i=0;i<keys.length;i++)
    			controler.setKeyVals(new String[]{keys[i].getKeywordPK()},new String[]{pubData.getKeywordByPK(keys[i].getKeywordPK())});
    		if(isGeneQeury){
    			controler.openEditWin(getMainboard(this),pubData.getAloneID(), pubData, m_strTaskPK, m_strRepPK,true,verItem==null?null:verItem.getVerPK(),subVerItem==null?null:subVerItem.getVerPK(),null,false);
    		}else{
    			controler.doOpenRepEditWinWithPubData(getMainboard(this),pubData.getAloneID(), pubData, m_strTaskPK, m_strRepPK,true,verItem==null?null:verItem.getVerPK(),subVerItem==null?null:subVerItem.getVerPK());
    		}
    		   		
    	}catch(Exception e){
    		AppDebug.debug(e);
    		javax.swing.JOptionPane.showMessageDialog(null, e.getMessage());
    	}
    }
    
    /**
     * 关键字值发生改变时，是否重新加载数据
     * @create by wangyga at 2009-1-9,下午02:30:51
     *
     * @param e
     * @i18n miufohbbb00102=关键字值发生改变是否重新加载数据?
     */
    public void doQueryWhenKeyValueChange(FocusEvent e){
    	if(!m_bKeyModified || e == null){
    		return;
    	} 
    	m_bKeyModified = false;//必须提前
    	if(UfoPublic.showConfirmDialog(this, StringResource.getStringResource("miufohbbb00102"), "",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
    		if(e.getSource() instanceof JTextField &&isGeneQeury){
    			addVerItemsToList(getVersionByCond());
    		}
    		doOpenReportByCond(isGeneQeury);
    	} else {
    		resetPanel();   		  		
    	}
    	
    	
    }
    
    private void resetPanel(){
    	RepDataControler controler=RepDataControler.getInstance(getMainboard(this));
		MeasurePubDataVO oldPubData=null;
		if(controler.getLastActiveRepDataEditor()!=null){
			oldPubData=controler.getLastActiveRepDataEditor().getPubData();
		}
		setOldVerInfo(KeyCondPaneUtil.getVerInfo(oldPubData), KeyCondPaneUtil.getSubVerInfo(oldPubData));
    	
		if(m_strOldInputKeyValues!=null){
			for (int i = 0; i < getKeyColCount(); i++) {
    			m_jTextFieldKeywords[i].setText(m_strOldInputKeyValues[i]);
			}
    		m_bKeyModified=false;
		}
		
		updateVerCompSet(false);
    }
    
    public void setOldVerInfo(VerItem verPk,VerItem subVerPk){
    	m_strOldVersionValues[0]=verPk;
    	m_strOldVersionValues[1]=subVerPk;
    }
    
    private VerItem[] getVersionByCond() {
		String[] strInputKeyVals = getInputKeyValues();
		VerItem[] vers = null;
		boolean hasInput = GeneralQueryUtil.checkKeyInput(strInputKeyVals);
		if (!hasInput) {
			return vers;
		}
		Mainboard mainBoard=getMainboard(this);
		RepDataControler controler = RepDataControler.getInstance(mainBoard);
		try {
			vers = (VerItem[]) ActionHandler.exec(
					"nc.ui.iufo.input.RepDataActionHandler",
					"loadDataVersByCond", new Object[] {
							controler.getCurUserInfo(mainBoard).getID(),
							this.m_strTaskPK,
							this.m_strRepPK,
							strInputKeyVals,
							(String) controler.getLastActiveRepDataEditor()
									.getContext().getAttribute(
											IUfoContextKey.ORG_PK) });
		} catch (Exception e) {
			javax.swing.JOptionPane.showMessageDialog(null, e.getMessage());
		}
		return vers;
	}
    private VerItem[] getSubVersionByCond() {
    	VerItem[] subVers = null;
    	VerItem verItem= getDataVerType();
		if (verItem != null) {
			String ver =verItem.getVerPK();
			String[] strInputKeyVals = getInputKeyValues();
			Mainboard mainBoard=getMainboard(this);
			RepDataControler controler = RepDataControler.getInstance(mainBoard);
			subVers = (VerItem[]) ActionHandler.exec(
					"nc.ui.iufo.input.RepDataActionHandler",
					"loadDataSubVersByCond", new Object[] {
							controler.getCurUserInfo(mainBoard).getID(),
							this.m_strTaskPK,
							this.m_strRepPK,
							strInputKeyVals,
							ver,
							(String) controler.getLastActiveRepDataEditor()
									.getContext().getAttribute(
											IUfoContextKey.ORG_PK) });
		}
		return subVers;
	}
    
    public static Mainboard getMainboard(Component comp){
    	while (comp!=null){
    		if (comp instanceof Editor)
    			return ((Editor)comp).getMainboard();
    		if (comp instanceof Mainboard)
    			return (Mainboard)comp;
    		comp=comp.getParent();
    	}
    	return null;
    }
       
    ///////////////////////////////////////////////////////////////////////////////////
	

	public boolean isGeneQeury() {
		return isGeneQeury;
	}

	public void setGeneQeury(boolean isGeneQeury) {
		this.isGeneQeury = isGeneQeury;
	}

	public String getRepPK() {
		return m_strRepPK;
	}

	public void setRepPK(String repPK) {
		m_strRepPK = repPK;
	}

	public String getTaskPK() {
		return m_strTaskPK;
	}

	public void setTaskPK(String taskPK) {
		m_strTaskPK = taskPK;
	}

	public ChangeKeywordsData[] getKeywordsDatas() {
		return m_changeKeywordsDatas;
	}

	public void setKeywordsDatas(ChangeKeywordsData[] keywordsDatas) {
		m_changeKeywordsDatas = keywordsDatas;
	}
    
    class KeyDocumentListener implements DocumentListener{
    	public void changedUpdate(DocumentEvent e) {
    		m_bKeyModified = true;
    	}
    	public void insertUpdate(DocumentEvent e) {    		
    		m_bKeyModified = true;
    	}
    	public void removeUpdate(DocumentEvent e) {
    		m_bKeyModified = true;
    	}

    };
    /**
     * 关键字编辑
     * @author wangyga
     * @created at 2009-1-13,上午11:18:42
     *
     */
    class KeyWordDoc extends PlainDocument {
		private static final long serialVersionUID = 1L;
		public void insertString(int offs, String str, AttributeSet a)
		throws BadLocationException {
			super.insertString(offs, str, a);
		}
		
		@Override
		public void replace(int offset, int length, String text,
				AttributeSet attrs) throws BadLocationException {
			if(!isInitKeyTextField){
				super.replace(offset, length, text, attrs);
				return;
			}						
			super.replace(offset, length, text, attrs);
			
		}
		
		
	}
    class KeyInputListener implements KeyListener{
    
		public void keyPressed(KeyEvent e) {
			// m_bKeyModified = true;
			if (e.getKeyCode() == KeyEvent.VK_ENTER) {
				if (e.getSource() == getQueryButton()) {
					doOpenReportByCond(isGeneQeury);
				} else {
					((Component) e.getSource()).nextFocus();
				}
			}
		}

		public void keyReleased(KeyEvent e) {
		}

		public void keyTyped(KeyEvent e) {
		}
    	
    };
    
    class KeyFocusAdapter extends FocusAdapter{
    	/**
         * Invoked when a component loses the keyboard focus.
         */
        public void focusLost(final FocusEvent e) {
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					// @edit by wangyga at 2009-1-9,下午02:44:00
//					System.out.println("----" + e.paramString() + "-----");
					if (e.getSource() instanceof RefTextField) {
						if (e.getOppositeComponent() != null
								&& e.getOppositeComponent().getParent() == getKeywordPanel()) {
							((RefTextField) e.getSource()).validateRefText(false);
							// 解决先更改版本再更改关键字记住最近的版本
							if (isGeneQeury) {
								m_strOldVersionValues[0] = getDataVerType();
								m_strOldVersionValues[1] = getDataSubVerType();
								updateVerCompSet(true);
							}

						}
					}
					if (e.getOppositeComponent() instanceof CellsPane||e.getOppositeComponent() instanceof TableHeader) {
						if (e.getSource() instanceof RefTextField) {
							((RefTextField) e.getSource())
									.validateRefText(false);
						}
						doQueryWhenKeyValueChange(e);
					} 
					}
			});
		}

		@Override
		public void focusGained(FocusEvent e) {
			// if(e.getSource() instanceof Viewer){
			// doQueryWhenKeyValueChange(e);
			// }
		}        	
        
        }

	public Dimension getPreferredScrollableViewportSize() {
		return this.getPreferredSize();
	}

	public int getScrollableBlockIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		return 0;
	}

	public boolean getScrollableTracksViewportHeight() {
		return false;
	}

	public boolean getScrollableTracksViewportWidth() {
		return false;
	}

	public int getScrollableUnitIncrement(Rectangle visibleRect,
			int orientation, int direction) {
		return 0;
	}
    
    

}
 
 