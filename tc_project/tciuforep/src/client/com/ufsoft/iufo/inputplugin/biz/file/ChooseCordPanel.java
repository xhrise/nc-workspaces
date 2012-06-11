package com.ufsoft.iufo.inputplugin.biz.file;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import nc.pub.iufo.accperiod.IUFODefaultNCAccSchemeUtil;
import nc.ui.iufo.input.InputUtil;
import nc.ui.pub.beans.UIButton;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.vo.iufo.unit.UnitInfoVO;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.inputplugin.biz.AbsIufoBizCmd;
import com.ufsoft.iufo.inputplugin.biz.IInputBizOper;
import com.ufsoft.iufo.inputplugin.biz.RepSelectionPlugIn;
import com.ufsoft.iufo.inputplugin.biz.WindowNavUtil;
import com.ufsoft.iufo.inputplugin.inputcore.AbsUfoContextVO;
import com.ufsoft.iufo.inputplugin.inputcore.AccTimeRefTextField;
import com.ufsoft.iufo.inputplugin.inputcore.CodeRefTextField;
import com.ufsoft.iufo.inputplugin.inputcore.TimeRefTextField;
import com.ufsoft.iufo.inputplugin.inputcore.UnitRefTextField;
import com.ufsoft.iufo.inputplugin.querynavigation.QueryNaviMenu;
import com.ufsoft.iufo.inputplugin.querynavigation.QueryNavigation;
import com.ufsoft.iuforeport.tableinput.TableSearchCondVO;
import com.ufsoft.iuforeport.tableinput.applet.ITableInputMenuType;
import com.ufsoft.iuforeport.tableinput.applet.TableInputContextVO;
import com.ufsoft.iuforeport.tableinput.applet.TableInputTransObj;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.re.RefTextField;
import com.ufsoft.iufo.resource.StringResource;

/**
 * �ۺϲ�ѯ������ѯ���
 * @author chxw
 * 2007-09-07
 */
public class ChooseCordPanel extends UIPanel implements IUfoContextKey{
	private static final int KEYWORDS_MAX_COUNT = 10;
	
    /**
     * ������
     */
    private UfoReport m_ufoReport;
    
    /**
     * ��¼��ؼ���ֵ����
     */
    private String[] m_strInputKeyValues = null;
    
    /**
     * �л��ؼ���֮ǰ��ֵ
     */
    private String[] m_strOldInputKeyValues = null;
    
    /**
     * �л��汾֮ǰ��ֵ
     */
    private Object[] m_strOldVersionValues = null;
    
    /**
     * ��ѯ��������
     */
    private TableSearchCondVO m_objSearchCondVO = null;
    
    /**
     * Ҫ¼��Ĺؼ�����Ϣ
     */
    private ChangeKeywordsData[] m_changeKeywordsDatas = null;
    
    /**
     * �Ӱ汾���棬Ϊ��������������ô���
     */
    private Hashtable m_mapDataVersAndSubVers = new Hashtable();
    
    /**
     * ��ʶ�ؼ�������Ƿ��ʼ�����
     */
    private boolean isInitKeyTextField = false;
    
    /**
     * �Ƿ���Ҫ���¼��ؾɵĹؼ���ֵ
     */
    private boolean isLoadOldKeyValue = true;
    
    /**
     * �������ݲ�ѯ�İ汾�ż��Ӱ汾��
     */
    private JLabel m_jLabelUnitKey = null;
    private JLabel m_jLabelVersionNum = null;
    private JComboBox m_jCombVersionNum = null;
    private JLabel m_jLabelSubVersionNum = null;
    private JComboBox m_jCombSubVersionNum = null;
    private JTextField m_jTextFieldUnitKey = null;
    
    /**
     * �������ݲ�ѯ�ı����б�
     */
    private JLabel    m_jLabelReportList = null;
    private JComboBox m_jCombRepList  = null;
    
    /**
     * �ؼ����б���ʾJPanel
     */
    private JPanel m_jKeywordPanel = null;
    private JScrollPane m_jScrollPane = null;
    
    private JTextField[] m_jTextFieldKeywords = new JTextField[KEYWORDS_MAX_COUNT];
    private JLabel[]     m_jLabelKeywords     = new nc.ui.pub.beans.UILabel[KEYWORDS_MAX_COUNT];
    private JButton      m_btnViewnReport     = null;
    
    private RepActionListener m_objRepActionListener = null;
    private KeyDocumentListener m_objKeyDocumentListener = null;
    private ItemListener m_switchVerListener = null;
    
    /**
     * ���ĵ�λ������
     */
    private ChooseRepPanel m_pnlChooseRepPanel = null;
    
    /**
     * �ؼ����Ƿ��޸�
     */
    private boolean m_bKeyModified = false;
    /**
     * �Ƿ���ָ��׷��
     */
    private boolean isMeasureTrace=false;
    
    /**
	 * ChooseCordP1anel ������
	 * @param ufoReport
	 */
	public ChooseCordPanel(UfoReport ufoReport, ChangeKeywordsData[] changeKeywordsDatas){
		m_ufoReport = ufoReport;
		m_changeKeywordsDatas = changeKeywordsDatas;
		initialize();
	}
	
	/**
     * This method initializes this
     * 
     * @return void
     */
    private void initialize() {
    	this.setLayout(new BorderLayout());
    	this.setBorder(null);
    	this.add(getJScrollPane(), BorderLayout.CENTER);
    }

    /**
     * This method initializes jScrollPane  
     *  
     * @return javax.swing.JScrollPane  
     */    
    private JScrollPane getJScrollPane() {
        if (m_jScrollPane == null) {
        	m_jScrollPane = new UIScrollPane();
        	m_jScrollPane.setViewportView(getKeywordPanel());
        	Dimension preSize=m_jScrollPane.getPreferredSize();
            this.setPreferredSize(preSize);
        }
        return m_jScrollPane;
    }
    
    /**
     * ��ʼ����ѯ������壬���������б�����ؼ��ּ�����(��汾)����
     *  
     * @return javax.swing.JPanel   
     */    
    private JPanel getKeywordPanel() {
    	if(m_jKeywordPanel == null) {
    		m_jKeywordPanel = new UIPanel();

        	//��ѯ�������������
        	FlowLayout flowLayout = new FlowLayout();
        	flowLayout.setAlignment(FlowLayout.LEFT);
        	m_jKeywordPanel.setLayout(flowLayout);
        	
        	//��ఴ��λ��ģʽ��ʾʱ������뱨���б���ѯ�������
        	addReportListRef(m_jKeywordPanel);
        	
        	//���ݱ���ؼ������ô�����ѯ���
        	int nColCount = getKeyColCount();
            for(int nColIndex = 0; nColIndex < nColCount ; nColIndex++){                
                addKeywordRow(m_jKeywordPanel, nColIndex);
            }
            isInitKeyTextField = true;
            addExtQueryCord(m_jKeywordPanel);
            
        }
        return m_jKeywordPanel;
    }
    
    /**
     * �����б��������ǵ�λ��������ʾ�����б��������Ǳ�����������ʾ��λ����
     * @param parentPanel
     * @i18n report=����
     */
    private void addReportListRef(JPanel parentPanel){
    	if(m_jLabelReportList == null){
    		m_jLabelReportList = new JLabel(MultiLang.getString("report"));
        	m_jLabelReportList.setPreferredSize(new Dimension(35, 20));
        	m_jLabelReportList.setHorizontalAlignment(SwingConstants.RIGHT);
        	
        	m_jCombRepList = new JComboBox();
        	m_jCombRepList.setPreferredSize(new Dimension(200, 20));
        	m_jCombRepList.addActionListener(getRepActionListener());
    	}
    	
    	parentPanel.add(m_jLabelReportList, null);
    	parentPanel.add(m_jCombRepList, null);
    	
    	if(GeneralQueryUtil.isShowRepTree(m_ufoReport)){
    		m_jLabelReportList.setVisible(false);
    		m_jCombRepList.setVisible(false);
    	}
    }
    
    /**
     * ����һ��ؼ��ֵ��ؼ������
     * @param parentPanel
     * @param nColIndex
     */
    private void addKeywordRow(JPanel parentPanel, int nColIndex) {
        if(parentPanel == null){
            return;
        }       
        ChangeKeywordsData changeKeywordsData = getChangeKeywordsData(nColIndex);
        if(changeKeywordsData == null){
            return;
        }
        
        m_jLabelKeywords[nColIndex] = new nc.ui.pub.beans.UILabel();
        m_jLabelKeywords[nColIndex].setPreferredSize(new Dimension(55, 20));
        m_jLabelKeywords[nColIndex].setText(changeKeywordsData.getKeyName());
        m_jLabelKeywords[nColIndex].setHorizontalAlignment(SwingConstants.RIGHT);
        
        m_jTextFieldKeywords[nColIndex] = null;
        if(changeKeywordsData.isTimeType()){
        	try {
        		Date defaultDate = null;
        		if(changeKeywordsData.getKeyValue() != null){
        			defaultDate = new SimpleDateFormat("yyyy-MM-dd").parse(changeKeywordsData.getKeyValue());//"2006-12-10")	
        		}       		
        		m_jTextFieldKeywords[nColIndex] = new TimeRefTextField(changeKeywordsData.getKeywordPK(),defaultDate);	
			} catch (ParseException e) {
				AppDebug.debug(e);
			}
        } else if(changeKeywordsData.isAccPeriodTimeType()){//add by ����� 2008-6-13 ��ӻ���ڼ����
        	String strKeyValue = changeKeywordsData.getKeyValue();
        	String strAccPeriodPk = getAccPeriodPk();
        	if(strAccPeriodPk == null || strAccPeriodPk.trim().length() == 0){
        		strAccPeriodPk = IUFODefaultNCAccSchemeUtil.getInstance().getIUFODefaultNCAccScheme();
        	}
			String strKeyWordPk = changeKeywordsData.getKeywordPK();
			m_jTextFieldKeywords[nColIndex] = new AccTimeRefTextField(strKeyValue,strAccPeriodPk,strKeyWordPk);
        }else if(changeKeywordsData.isCorpKey() || changeKeywordsData.isDicCorpKey()){
        	String curUnitCode = (String)((AbsUfoContextVO)m_ufoReport.getContextVo()).getAttribute(CUR_UNIT_CODE);
        	String strOrgPK = ((AbsUfoContextVO)m_ufoReport.getContextVo()).getAttribute(ORG_PK) == null ? null : (String)((AbsUfoContextVO)m_ufoReport.getContextVo()).getAttribute(ORG_PK);

        	if(m_ufoReport.getContextVo() instanceof TableInputContextVO){
        		TableInputContextVO inputContextVO = (TableInputContextVO)m_ufoReport.getContextVo();
//        		String strOperUnitPK = inputContextVO.getInputTransObj().getRepDataParam().getOperUnitPK();
//        		if(inputContextVO.getPubDataVO() != null && inputContextVO.getPubDataVO().getUnitPK() != null){
//        			strOperUnitPK = inputContextVO.getPubDataVO().getUnitPK();
//        		}
        		String strLoginUnitPK = inputContextVO.getAttribute(LOGIN_UNIT_ID) == null ? null : (String)inputContextVO.getAttribute(LOGIN_UNIT_ID);

        		UnitInfoVO unitInfo = GeneralQueryUtil.findUnitInfoByPK(strLoginUnitPK);
        		m_jTextFieldKeywords[nColIndex] = new UnitRefTextField(unitInfo.getCode(), strOrgPK);
        	} else {
        		m_jTextFieldKeywords[nColIndex] = new UnitRefTextField(curUnitCode, strOrgPK);
        	}

        } else if(changeKeywordsData.getKeyRef() != null){//��ͨ��������
        	m_jTextFieldKeywords[nColIndex] = new CodeRefTextField(changeKeywordsData.getKeyRef());
        } else {
        	m_jTextFieldKeywords[nColIndex] = new nc.ui.pub.beans.UITextField();
        }

        //�ڱ�������״̬�£�����ʾ�汾���Ӱ汾��ѯ���������Ҳ������ѯ�汾���¼�����
        m_jTextFieldKeywords[nColIndex].setPreferredSize(new Dimension(120, 20));
        m_jTextFieldKeywords[nColIndex].setDocument(new KeyWordDoc());
        m_jTextFieldKeywords[nColIndex].setText(changeKeywordsData.getKeyValue());
        // @edit by wangyga at 2009-1-13,����01:03:36 �ؼ���ֵ�����仯ʱ����Ҫ��ʾ�Ƿ����¼�������(����¼����ۺϲ�ѯ)
//        if(GeneralQueryUtil.isGeneralQuery(m_ufoReport)){        	
        	m_jTextFieldKeywords[nColIndex].getDocument().addDocumentListener(getDocumentListener());
        	m_jTextFieldKeywords[nColIndex].addKeyListener(new KeyInputListener());
        	m_jTextFieldKeywords[nColIndex].addFocusListener(new KeyFocusAdapter());
//        }
        
        //��ఴ��λ��ģʽ��ʾʱ������뱨���б���ѯ�������
        parentPanel.add(m_jLabelKeywords[nColIndex], null);
    	parentPanel.add(m_jTextFieldKeywords[nColIndex], null);
        if(changeKeywordsData.isCorpKey()){
    		m_jLabelUnitKey = m_jLabelKeywords[nColIndex];
    		m_jTextFieldUnitKey = m_jTextFieldKeywords[nColIndex];
    	} 
        if(!GeneralQueryUtil.isShowRepTree(m_ufoReport) && changeKeywordsData.isCorpKey()){
        	m_jLabelUnitKey.setVisible(false);
        	m_jTextFieldUnitKey.setVisible(false);
        }    
    }
    
    /**
     * add by  ����� 2008-6-13 �������Ļ���ڼ䷽������
     * @param
     * @return String
     */
    private String getAccPeriodPk(){
    	String strAccPreiodPk = null;//����ڼ�PK
    	TableInputContextVO contextVo = (TableInputContextVO)m_ufoReport.getContextVo();
    	if(contextVo != null){
    		Object tableInputTransObj = contextVo.getAttribute(TABLE_INPUT_TRANS_OBJ);
    		TableInputTransObj inputTransObj = tableInputTransObj != null &&(tableInputTransObj instanceof TableInputTransObj) ? (TableInputTransObj)tableInputTransObj : null;
		
//    		TableInputTransObj inputTransObj = contextVo.getInputTransObj();
    		String strTaskPK =  inputTransObj.getRepDataParam().getTaskPK();
    		strAccPreiodPk = InputUtil.getAccSechemePK(strTaskPK);
    	}
    	return strAccPreiodPk;
    }
    
    /**
     * �����ۺϲ�ѯ������ؼ�����������ѯ����
     * 
     * @param parentPanel
     * @i18n uiuforep00138=�汾
     * @i18n uiuforep00139=�Ӱ汾
     * @i18n uiuforep00140=��ѯ
     */
    private void addExtQueryCord(JPanel parentPanel){
    	if(GeneralQueryUtil.isGeneralQuery(m_ufoReport.getContext())){
    		//���ݰ汾ѡ���б�
    		m_strOldVersionValues = new Object[2];
    		m_jLabelVersionNum = new JLabel(MultiLang.getString("uiuforep00138"));
    		m_jLabelVersionNum.setPreferredSize(new Dimension(35, 20));
    		m_jLabelVersionNum.setHorizontalAlignment(SwingConstants.RIGHT);
    		m_jCombVersionNum = new JComboBox();
    		m_jCombVersionNum.addFocusListener(new KeyFocusAdapter());
    		m_jCombVersionNum.setPreferredSize(new Dimension(120, 20));

    		parentPanel.add(m_jLabelVersionNum, null);
    		parentPanel.add(m_jCombVersionNum, null);

    		m_jLabelSubVersionNum = new JLabel(MultiLang.getString("uiuforep00139"));
    		m_jLabelSubVersionNum.setPreferredSize(new Dimension(45, 20));
    		m_jLabelSubVersionNum.setHorizontalAlignment(SwingConstants.RIGHT);
    		m_jCombSubVersionNum = new JComboBox();
    		m_jCombSubVersionNum.addFocusListener(new KeyFocusAdapter());
    		m_jCombSubVersionNum.setPreferredSize(new Dimension(120, 20));

    		parentPanel.add(m_jLabelSubVersionNum, null);
    		parentPanel.add(m_jCombSubVersionNum, null);

    		//�汾�л�
    		m_jCombVersionNum.addItemListener(getSwitchVerListener());
    		// @edit by wangyga at 2009-2-26,����09:59:09
    		m_jCombSubVersionNum.addItemListener(new ItemListener(){
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange()==ItemEvent.DESELECTED){
    					m_bKeyModified = true;
    					m_strOldVersionValues[1] = e.getItem();
					}					
				}
    			
    		});

    	}
    	
    	//��ѯ��ť
    	if(!GeneralQueryUtil.isShowRepTree(m_ufoReport) || 
    			(GeneralQueryUtil.isShowRepTree(m_ufoReport) && m_changeKeywordsDatas != null && m_changeKeywordsDatas.length >0)){
    		m_btnViewnReport = new UIButton(MultiLang.getString("uiuforep00140"));
//        	m_btnViewnReport.setPreferredSize(new Dimension(50, 20));
        	parentPanel.add(m_btnViewnReport, null);
        	m_btnViewnReport.addMouseListener(new MouseAdapter(){
    			public void mouseClicked(MouseEvent e) {
    				doOpenReportByCond(true);
    			}
        		
        	});
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
     * ���ĵ�λ������
     * @return ChooseRepPanel
     */
    private ChooseRepPanel getLeftChooseRepPanel(){
    	if(m_pnlChooseRepPanel == null){
    		RepSelectionPlugIn selectionPlugIn = (RepSelectionPlugIn)
    		m_ufoReport.getPluginManager().getPlugin(RepSelectionPlugIn.class.getName());
    		ChooseRepExt2  chooseRepExt2  = (ChooseRepExt2)(selectionPlugIn.getDescriptor().getExtensions()[0]);
    		m_pnlChooseRepPanel = chooseRepExt2.getChooseRepNavPanel();	
    	}

    	return m_pnlChooseRepPanel;
    }
    
    /**
     * �����ۺϲ�ѯ�ؼ������ã����ݲ�ѯ��ʽ���ùؼ���ֵ
     * @return
     */
    public String[] getInputKeyValues(){
        return getInputKeyValues(null);
    }
    
    /**
     * �����ۺϲ�ѯ�ؼ������ã����ݲ�ѯ��ʽ���ùؼ���ֵ
     * @param strOperUnitCode �µĲ�ѯ��λ
     * @return
     */
    private String[] getInputKeyValues(String strOperUnitCode){
    	m_strInputKeyValues = new String[getKeyColCount()];
    	for(int i =0;i < getKeyColCount();i++){
    		if(!GeneralQueryUtil.isShowRepTree(m_ufoReport) && strOperUnitCode != null && m_changeKeywordsDatas[i].isCorpKey()){
    			m_strInputKeyValues[i] = strOperUnitCode;
    			continue;
    		}
    		m_strInputKeyValues[i] = m_jTextFieldKeywords[i].getText();
    	}

    	return m_strInputKeyValues;
    }

    /**
     * ����ѡ���ı�����ѯ�ñ�����¼������ݰ汾
     * @param strRepPK
     * @i18n uiuforep00141=���ڵ�λ����ѡ���ѯ��λ��
     * @i18n uiuforep00142=���ڱ�������ѡ���ѯ����
     */
    private void getRepDataVers(String strRepPK){
    	if(strRepPK == null)
    		return;
    	
    	//���ò�ѯ��λ����ѯ����
    	boolean isShowRepTree = GeneralQueryUtil.isShowRepTree(m_ufoReport);
    	boolean isSelectionNode = getLeftChooseRepPanel().isSelectionNode();
    	String strNewUnitPK = null;
    	String strNewRepPK = null;
    	if(!isShowRepTree){
    		if(!isSelectionNode){
    			javax.swing.JOptionPane.showMessageDialog(m_ufoReport, MultiLang.getString("uiuforep00141"));
    			return;
    		}
    		strNewUnitPK = getLeftChooseRepPanel().getOperUnit();
    	}else{
    		if(!isSelectionNode){
    			javax.swing.JOptionPane.showMessageDialog(m_ufoReport, MultiLang.getString("uiuforep00142"));
    			return;
    		}
    		strNewRepPK = getLeftChooseRepPanel().getOperRep();
    	}
    	TableSearchCondVO searchCondVO = getTableSearchCondObj(strNewUnitPK);
    	if(isShowRepTree){
    		searchCondVO.setStrOperRepPK(strNewRepPK);
    	}
    	searchCondVO.setStrOperRepPK(strRepPK);
    	IInputBizOper inputMenuOper = new TableInputSearchOper(m_ufoReport, new Object[]{searchCondVO});
        inputMenuOper.performBizTask(ITableInputMenuType.BIZ_TYPE_SEARCH_VERS);
    }
    
    /**
     * ����ѡ���ı�����ѯ�ñ�����¼������ݰ汾
     * @param verItem
     * @i18n uiuforep00141=���ڵ�λ����ѡ���ѯ��λ��
     * @i18n uiuforep00142=���ڱ�������ѡ���ѯ����
     */
    private void getRepDataSubVers(VerItem verItem){
    	if(verItem == null)
    		return;
    	
    	//���ò�ѯ��λ����ѯ����
    	boolean isShowRepTree = GeneralQueryUtil.isShowRepTree(m_ufoReport);
    	boolean isSelectionNode = getLeftChooseRepPanel().isSelectionNode();
    	String strNewUnitPK = null;
    	String strNewRepPK = null;
    	if(!isShowRepTree){
    		if(!isSelectionNode){
    			javax.swing.JOptionPane.showMessageDialog(m_ufoReport, MultiLang.getString("uiuforep00141"));
    			return;
    		}
    		strNewUnitPK = getLeftChooseRepPanel().getOperUnit();
    	}else{
    		if(!isSelectionNode){
    			javax.swing.JOptionPane.showMessageDialog(m_ufoReport, MultiLang.getString("uiuforep00142"));
    			return;
    		}
    		strNewRepPK = getLeftChooseRepPanel().getOperRep();
    	}
    	TableSearchCondVO searchCondVO = getTableSearchCondObj(strNewUnitPK);
    	if(isShowRepTree){
    		searchCondVO.setStrOperRepPK(strNewRepPK);
    	}
    	searchCondVO.setDataVerType(verItem.getVerPK());
    	IInputBizOper inputMenuOper = new TableInputSearchOper(m_ufoReport, new Object[]{searchCondVO});
        inputMenuOper.performBizTask(ITableInputMenuType.BIZ_TYPE_SEARCH_SUBVERS);
    }
    
	/**
     * ����ѡ�������ؼ�������ִ���ۺϲ��򿪱���
	 * @i18n uiuforep00143=����ѡ����ѯ������δ��ѯ���������ݣ���ȷ���ڴ˹ؼ����������Ƿ���¼������!
	 * @i18n uiuforep00141=���ڵ�λ����ѡ���ѯ��λ��
	 * @i18n uiuforep00142=���ڱ�������ѡ���ѯ����
	 * @i18n uiuforep00129=��ѯ�ؼ��ֲ���Ϊ��
     *
     */
    private void doOpenReportByCond(boolean isCheckVersion){
    	//��ʾ���汨��
		TableInputContextVO tableInputContextVO = (TableInputContextVO) m_ufoReport.getContextVo();
		Object tableInputTransObj = tableInputContextVO.getAttribute(TABLE_INPUT_TRANS_OBJ);
		TableInputTransObj inputTransObj = tableInputTransObj != null &&(tableInputTransObj instanceof TableInputTransObj) ? (TableInputTransObj)tableInputTransObj : null;
		
		String strOldRepPK = inputTransObj.getRepDataParam().getReportPK();
		if(strOldRepPK != null && !GeneralQueryUtil.isCommit(m_ufoReport))
			AbsIufoBizCmd.doComfirmSave(m_ufoReport, false);
		
    	//����Ƿ�ѡ����ѯ�����Ӱ汾
		// @edit by wangyga at 2009-1-9,����04:20:33		
		if(GeneralQueryUtil.isGeneralQuery(m_ufoReport.getContext()) && 
	    		(m_jCombSubVersionNum.getItemCount() == 0 || m_jCombSubVersionNum.getSelectedIndex() < 0)){
			if(isCheckVersion){
				javax.swing.JOptionPane.showMessageDialog(m_ufoReport, MultiLang.getString("uiuforep00143"));
			}	    		
	    	TableInputSearchOper.createEmptyTable(m_ufoReport);
	    	return;
		}
    	  	
    	//���ؼ���¼���Ƿ�Ϸ�
    	String strNewUnitPK = null;
    	String strNewRepPK = null;
    	boolean isShowRepTree = GeneralQueryUtil.isShowRepTree(m_ufoReport);
    	boolean isSelectionNode = getLeftChooseRepPanel().isSelectionNode();
    	if(!isShowRepTree){
    		if(!isSelectionNode){
    			javax.swing.JOptionPane.showMessageDialog(m_ufoReport, MultiLang.getString("uiuforep00141"));
    			return;
    		}
    		strNewUnitPK = getLeftChooseRepPanel().getOperUnit();
    	}else{
    		if(!isSelectionNode){
    			javax.swing.JOptionPane.showMessageDialog(m_ufoReport, MultiLang.getString("uiuforep00142"));
    			return;
    		}
    		strNewRepPK = getLeftChooseRepPanel().getOperRep();
    	}
    	
    	
    	//���ھ����Ƿ�δ¼��
    	TableSearchCondVO searchCondVO = getTableSearchCondObj(strNewUnitPK);
    	if(isShowRepTree){
    		searchCondVO.setStrOperRepPK(strNewRepPK);
    	}
    	boolean hasInput = GeneralQueryUtil.checkKeyInput(searchCondVO.getStrKeyValues());
		if(!hasInput){
			javax.swing.JOptionPane.showMessageDialog(m_ufoReport, MultiLang.getString("uiuforep00129"));
			return;
		}
    	
    	Object objReturn = null;
    	Boolean isGenralQuery = new Boolean(GeneralQueryUtil.isGeneralQuery(m_ufoReport.getContext()));
		IInputBizOper inputMenuOper = new TableInputSearchOper(m_ufoReport, new Object[]{searchCondVO, isGenralQuery});
    	if(GeneralQueryUtil.isGeneralQuery(m_ufoReport.getContext())){
    		objReturn = inputMenuOper.performBizTask(ITableInputMenuType.BIZ_TYPE_SEARCH_REPDATASUBMIT);	
    	} else{
    		objReturn = inputMenuOper.performBizTask(ITableInputMenuType.BIZ_TYPE_SEARCH_REPDATA);	
    	}

    	if(objReturn != null && objReturn.toString().length() > 0 && !objReturn.toString().equals("true")){
    		javax.swing.JOptionPane.showMessageDialog(m_ufoReport, objReturn.toString());
    		return;
    	}
    	
    	//add by ����� 2008-5-6 �л��ؼ���ʱ������������ڱ༭�ĵ�Ԫ����Ҫɾ����
    	Component com = m_ufoReport.getTable().getEditorComp();
        if(com instanceof RefTextField){
        	m_ufoReport.getTable().getCells().removeEditor();
        }
        //end
        
    	//����anchor_Change�¼�,��������ʽ׷�ٹ���
		WindowNavUtil.startFormulaTrace(m_ufoReport,null,true);
		//�����˽����������
		ChooseRepPanel repsPanel=GeneralQueryUtil.getChooseRepPanel(m_ufoReport);
		repsPanel.preChangeRep();
    	regMenu();
    }
    
    private void regMenu() {
		QueryNaviMenu menu = QueryNavigation.getSingleton().getCurWindow(this);
//		menu.clear();
    	menu.add(m_ufoReport, "");
    	QueryNavigation.refreshMenu(m_ufoReport);
	}
    
    /**
     * �����л���λ��ʱ��ѯ�����ĸ���
     * @param strDefOperUnit
     * @param strDefOperRep
     * @return
     */
    public void switchTreeView(String strDefOperUnit, String strDefOperRep){
    	m_jScrollPane.setViewportView(getKeywordPanel());
    	if(GeneralQueryUtil.isShowRepTree(m_ufoReport)){
    		m_jCombRepList.setVisible(false);
    		m_jLabelReportList.setVisible(false);
    		//modify by guogang 2008-2-26 �����Ŀ���û�е�λ�ؼ��֣���ǰ���ǰ���λ��֯��ʱ�򱨴�����⣬���ʱ����޸�Ϊ��������֯
    		if(m_jTextFieldUnitKey!=null)
    		m_jTextFieldUnitKey.setVisible(true);
    		if(m_jLabelUnitKey!=null)
    		m_jLabelUnitKey.setVisible(true);
    	} else{
    		m_jCombRepList.setVisible(true);
    		m_jLabelReportList.setVisible(true);
    		m_jTextFieldUnitKey.setVisible(false);
    		m_jLabelUnitKey.setVisible(false);
    	}
    	setSelItemOfCordPanel(strDefOperUnit, strDefOperRep);
    	
    }
    
    /**
     * �ۺϲ�ѯ����ѡ�����
     * @return
     */
    public TableSearchCondVO getTableSearchCondObj(){
    	return getTableSearchCondObj(null);
    	
    }
    
    /**
     * �ۺϲ�ѯ����ѡ�����
     * @param newOperUnitCode �µĲ�ѯ��λ
     * @return
     */
    public TableSearchCondVO getTableSearchCondObj(String newOperUnitCode){
    	String strOperUnitCode = (newOperUnitCode != null) ? newOperUnitCode : getOperUnitPK();
    	if(m_objSearchCondVO == null){
    		m_objSearchCondVO = new TableSearchCondVO();
    	}
    	//ȡ��λ��ѡ����λ
    	if(strOperUnitCode != null && strOperUnitCode.length() == 0){
    		strOperUnitCode = getLeftChooseRepPanel().getOperUnit();
    	}
    	
    	m_objSearchCondVO.setStrKeyValues(getInputKeyValues(strOperUnitCode));
    	m_objSearchCondVO.setStrOperRepPK(getOperRepPK());
    	m_objSearchCondVO.setStrOperUnitPK(strOperUnitCode);
    	m_objSearchCondVO.setShowRepTree(GeneralQueryUtil.isShowRepTree(m_ufoReport));
    	m_objSearchCondVO.setDataVerType(getDataVerType());
    	m_objSearchCondVO.setSubDataVerType(getDataSubVerType());
    	m_objSearchCondVO.setAloneID(getDataSubVerType());
    	return m_objSearchCondVO;
    	
    }
    
    /**
     * �����ѡ���������б���
     * @param reps
     */
    public void addRepItemsToList(String[][] reps){
    	if(m_jCombRepList == null){
    		return;
    	}
    	
    	m_jCombRepList.removeActionListener(getRepActionListener());
    	m_jCombRepList.removeAllItems();
    	if(reps == null){
    		return;
    	}
    	
    	for(String[] rep : reps){
    		m_jCombRepList.addItem(new RepItem(rep[0], rep[1]));
    	}
    	m_jCombRepList.addActionListener(getRepActionListener());
    }
    
    /**
     * �����ѡ���ݰ汾���汾�б���
     * @param vers
     */
    public void addDataVerItemsToList(String[][] vers){
    	if(m_jCombVersionNum == null){
    		return;
    	}
    	
    	m_jCombVersionNum.removeAllItems();
    	removeAllSubVerItemsOfList();
    	if(vers == null || vers.length == 0){
    		return;
    	}

    	ItemListener m_switchVerListener = getSwitchVerListener();
    	m_jCombVersionNum.removeItemListener(m_switchVerListener);
    	for(String[] ver : vers){
    		m_jCombVersionNum.addItem(new VerItem(ver[0], ver[1]));
    	}
    	m_jCombVersionNum.addItemListener(m_switchVerListener);
    	
    }
    
    /**
     * �����ѡ���ݰ汾���Ӱ汾�б���
     * @param vers
     */
    public void addDataSubVerItemsToList(String[][] vers){
    	if(m_jCombSubVersionNum == null){
    		return;
    	}
    	
    	m_jCombSubVersionNum.removeAllItems();
    	if(vers == null || vers.length == 0){
    		return;
    	}
    	
    	for(String[] ver : vers){
    		m_jCombSubVersionNum.addItem(new VerItem(ver[0], ver[1]));
    	}
    	
    	VerItem verItem = (VerItem)m_jCombVersionNum.getSelectedItem();
    	if(verItem != null){
    		m_mapDataVersAndSubVers.put(verItem, vers);
    	}
    }
    
    /**
     * �������ݰ汾ȱʡѡ����
     * @param ver
     */
    public void setDefaultDataVer(String ver){
    	if(m_jCombVersionNum == null || ver == null){
    		return;
    	}

    	for(int index = 0; index < m_jCombVersionNum.getItemCount(); index++){
    		VerItem verObj = (VerItem)m_jCombVersionNum.getItemAt(index);
    		if(verObj.getVerPK().equals(ver)){
    			m_jCombVersionNum.setSelectedIndex(index);
    			return;
    		}
    	}
    }
    
    public JComboBox getVersionComb() {
		return m_jCombVersionNum;
	}

	/**
     * ���������Ӱ汾ȱʡѡ����
     * @param ver
     */
    public void setDefaultDataSubVer(String ver){
    	if(m_jCombSubVersionNum == null || ver == null){
    		return;
    	}

    	for(int index = 0; index < m_jCombSubVersionNum.getItemCount(); index++){
    		VerItem verObj = (VerItem)m_jCombSubVersionNum.getItemAt(index);
    		if(verObj.getVerPK().equals(ver)){
    			m_jCombSubVersionNum.setSelectedIndex(index);
    			return;
    		}
    	}
    }
    
    public void removeAllSubVerItemsOfList(){
    	m_jCombSubVersionNum.removeAllItems();
    }
    
    /**
     * ���ر�ѡ��ı���PK���������δѡ����ִ�в�ѯʱȱʡ��һ�ű���
     * @return
     */
    private String getOperRepPK(){
    	if(m_jCombRepList != null && m_jCombRepList.getItemCount() > 0){
    		if(m_jCombRepList.getSelectedIndex() == -1 ){
    			m_jCombRepList.removeActionListener(getRepActionListener());
    			m_jCombRepList.setSelectedIndex(0);
    			m_jCombRepList.addActionListener(getRepActionListener());
    		}
    		RepItem repItem = (RepItem)m_jCombRepList.getSelectedItem();
    		return repItem.getRepPK();
    	}
    	return null;
    	
    }
    
    /**
     * ���ر�ѡ��ĵ�λPK���������δѡ����ִ�в�ѯʱȱʡʹ�õ�ǰ��¼��λ
     * @return
     */
    private String getOperUnitPK(){
    	if(m_jTextFieldUnitKey != null){
    		return m_jTextFieldUnitKey.getText();
    	}
    	return null;
    }

    /**
     * �������ݰ汾��δѡ��ʱ��ȱʡ�������
     * @return
     */
    public String getDataVerType(){
    	if(m_jCombVersionNum != null && m_jCombVersionNum.getItemCount() > 0){
    		if(m_jCombVersionNum.getSelectedIndex() == -1 )
    			m_jCombVersionNum.setSelectedIndex(0);
    		
    		VerItem verItem = (VerItem)m_jCombVersionNum.getSelectedItem();
    		return verItem.getVerPK();
    	}
    	return null;
    }
    
    /**
     * ���������Ӱ汾��Ϊѡ��ʱ��ȱʡ�������
     * @return
     */
    public String getDataSubVerType(){
    	if(m_jCombSubVersionNum != null && m_jCombSubVersionNum.getItemCount() > 0){
    		if(m_jCombSubVersionNum.getSelectedIndex() == -1 )
    			m_jCombSubVersionNum.setSelectedIndex(0);
    		
    		VerItem verItem = (VerItem)m_jCombSubVersionNum.getSelectedItem();
    		return verItem.getVerPK();
    	}
    	return null;
    }
    
    public JComboBox getSubVersionComb() {
		return m_jCombSubVersionNum;
	}
    
	public boolean isMeasureTrace() {
		return isMeasureTrace;
	}

	public void setMeasureTrace(boolean isMeasureTrace) {
		this.isMeasureTrace = isMeasureTrace;
	}

	/**
     * ���ò�ѯ����ȱʡѡ��
     * @param strDefOperUnit
     * @param strDefOperRep
     * @return
     */
    public void setSelItemOfCordPanel(String strDefOperUnit, String strDefOperRep){
    	if(!GeneralQueryUtil.isShowRepTree(m_ufoReport) && strDefOperRep != null){
    		for(int i = 0; i < m_jCombRepList.getItemCount(); i ++){
    			RepItem objRepItem = (RepItem)m_jCombRepList.getItemAt(i);
    			if(objRepItem.getRepPK().equals(strDefOperRep)){
    				m_jCombRepList.removeActionListener(getRepActionListener());
    				m_jCombRepList.setSelectedIndex(i);
    				m_jCombRepList.addActionListener(getRepActionListener());
    				break;
    			}
    		}
    	} else{
    		if(m_jTextFieldUnitKey != null && strDefOperUnit != null){
    			m_jTextFieldUnitKey.setText(strDefOperUnit);	
    		}
    	}
    	
    }
    
    public void clearVersCache(){
    	m_mapDataVersAndSubVers.clear();
    }
    
    private ActionListener getRepActionListener(){
    	if(m_objRepActionListener == null){
    		m_objRepActionListener = new RepActionListener();
    	}
    	return m_objRepActionListener;
    }
    
    private KeyDocumentListener getDocumentListener(){
    	if(m_objKeyDocumentListener == null){
    		m_objKeyDocumentListener = new KeyDocumentListener();
    	}
    	return m_objKeyDocumentListener;
    }
    
    /**
     * �л��汾�¼�������
     * @return
     */
    private ItemListener getSwitchVerListener(){
    	if(m_switchVerListener == null){
    		m_switchVerListener = new ItemListener(){
    			public void itemStateChanged(ItemEvent e) {
    				if(e.getStateChange()==ItemEvent.SELECTED){
    					if(m_jCombVersionNum == null)
    						return;
    					VerItem verItem = (VerItem)m_jCombVersionNum.getSelectedItem();
    					if(verItem == null){
    						return;
    					}
    					//ָ��׷�ٵ�ʱ�������Ӱ汾��Ϣ��ȡ�İ汾��Ϣ������Ҫ�ٲ�ѯһ��
    					if(!isMeasureTrace()){
    					getRepDataSubVers(verItem);
    					}
    					// @edit by wangyga at 2009-2-26,����09:56:16
    					m_bKeyModified = true;   					
    				} else if(e.getStateChange() == ItemEvent.DESELECTED){
    					m_strOldVersionValues[0] = e.getItem();
    				}
    			}	
    		};;
    	}
    	return m_switchVerListener;
    }
    
    /**
     * �ؼ���ֵ�����ı�ʱ���Ƿ����¼�������
     * @create by wangyga at 2009-1-9,����02:30:51
     *
     * @param e
     * @i18n miufohbbb00102=�ؼ���ֵ�����ı��Ƿ����¼�������?
     */
    private void doQueryWhenKeyValueChange(FocusEvent e){
    	if(!m_bKeyModified || e == null){
    		return;
    	}   	
    	boolean isSelectionNode = getLeftChooseRepPanel().isSelectionNode();
    	if(!isSelectionNode){
    		return;
    	}
    	Object eventSource = e.getOppositeComponent();	
    	if(!(eventSource instanceof CellsPane)){
    		return;
    	}  	
    	if(UfoPublic.showConfirmDialog(m_ufoReport, StringResource.getStringResource("miufohbbb00102"), "",
				JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
    		doOpenReportByCond(false);
    	} else {
    		if(e.getSource() instanceof JTextField){
    			for (int i = 0; i < getKeyColCount(); i++) {
        			if(isLoadOldKeyValue){
        				isLoadOldKeyValue = false;
        			}
        			m_jTextFieldKeywords[i].setText(m_strOldInputKeyValues[i]);
    			}
        		isLoadOldKeyValue = true;
    		} else if(e.getSource() instanceof JComboBox){
    			m_jCombVersionNum.setSelectedItem(m_strOldVersionValues[0]);
    			m_jCombSubVersionNum.setSelectedItem(m_strOldVersionValues[1]);
    		}  		
    	}
    	m_bKeyModified = false;
    }
    
    ///////////////////////////////////////////////////////////////////////////////////
    class RepItem {
    	String strRepPK = null;
    	String strRepDisName = null;
    	
    	public RepItem(String repPK, String repDisName){
    		this.strRepPK = repPK;
    		this.strRepDisName = repDisName;
    	}

    	public String getRepDisName() {
			return strRepDisName;
		}

		public String getRepPK() {
			return strRepPK;
		}

		@Override
		public String toString() {
			return strRepDisName;
		}
    }
    
    public class VerItem {
    	String strVerPK = null;
    	String strVerDisName = null;

    	public VerItem(String verPK, String verDisName){
    		this.strVerPK = verPK;
    		this.strVerDisName = verDisName;
    	}

    	public String getVerDisName() {
			return strVerDisName;
		}

		public String getVerPK() {
			return strVerPK;
		}

		@Override
    	public String toString() {
    		return strVerDisName;
    	}
    }

    class RepActionListener implements ActionListener{
    	public void actionPerformed(ActionEvent e) {
    		RepItem repItem = (RepItem)m_jCombRepList.getSelectedItem();
    		getRepDataVers(repItem.getRepPK());
    	}

    };
    
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
    
    class KeyInputListener implements KeyListener{
    
		public void keyPressed(KeyEvent e) {
			m_bKeyModified = true;
		}

		public void keyReleased(KeyEvent e) {
		}

		public void keyTyped(KeyEvent e) {
		}
    	
    };
    
    /**
     * �ؼ��ֱ༭
     * @author wangyga
     * @created at 2009-1-13,����11:18:42
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
			//�ѹؼ����л�֮ǰ��ֵ��������
			if(isLoadOldKeyValue){
				m_strOldInputKeyValues = getInputKeyValues();
			}
						
			super.replace(offset, length, text, attrs);
		}
		
		
	}
    
    class KeyFocusAdapter extends FocusAdapter{
    	/**
         * Invoked when a component loses the keyboard focus.
         */
        public void focusLost(FocusEvent e) {
        	if(GeneralQueryUtil.isGeneralQuery(m_ufoReport.getContext())){
        		if(m_bKeyModified && e.getSource() instanceof JTextField){
            		String strRepPK = null;
            		if(GeneralQueryUtil.isShowRepTree(m_ufoReport)){
            			strRepPK = getLeftChooseRepPanel().getOperRep();
            		} else{
            			RepItem repItem = (RepItem)m_jCombRepList.getSelectedItem();
            			strRepPK = repItem.getRepPK();
            		}
            		getRepDataVers(strRepPK);       		
            	}
        	}        	
        	// @edit by wangyga at 2009-1-9,����02:44:00
        	doQueryWhenKeyValueChange(e);
        	
//        	m_bKeyModified = false;
        }
    };
       
    ///////////////////////////////////////////////////////////////////////////////////
    
}
  