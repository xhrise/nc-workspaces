package com.ufsoft.iufo.fmtplugin.key;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

import nc.pub.iufo.cache.base.ICacheObject;
import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIComboBox;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIRadioButton;
import nc.ui.pub.beans.UITextField;
import nc.util.iufo.pub.IDMaker;
import nc.vo.iufo.code.CodeVO;
import nc.vo.iufo.keydef.KeyVO;

import com.ufida.dataset.IContext;
import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.dynarea.DynAreaModel;
import com.ufsoft.iufo.fmtplugin.formatcore.CacheProxy;
import com.ufsoft.iufo.fmtplugin.formatcore.IUfoContextKey;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaCell;
import com.ufsoft.iufo.inputplugin.dynarea.DynAreaVO;
import com.ufsoft.iufo.inputplugin.inputcore.MultiLangInput;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;
import com.ufsoft.report.util.UfoPublic;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;

public class DynAreaKeyMngDlg extends UfoDialog implements IUfoContextKey{
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -4278651693003406390L;
	protected static final int ID_DELETE = 3;
    private JPanel jContentPane = null;
    private JButton keyRefButton = null;
    private JTextField keyNameTextField = null;
    private JLabel jLabel = null;
    private JButton okButton = null;
    private JButton cancleButton = null;
    private JButton delButton = null;
    private DynAreaVO dynAreaVO;
    private KeyVO keyVO;
    private CellPosition cellPos;
	private JRadioButton jRadioButtonPublic = null;
	private JRadioButton jRadioButtonPrivate = null;
	private JCheckBox jCheckBoxIsRef = null;
	private JComboBox jComboBox = null;
	private CellPosition  timeCell=null;//_hasTimeKeyVO;
	private CellsModel cellsModel = null;
	private IContext context = null;
    /**
     * This is the default constructor
     * @param cellPos 
     */
    public DynAreaKeyMngDlg(Container parent,CellsModel cellsModel,IContext context, DynAreaVO dynAreaVO,CellPosition cellPos, KeyVO keyVO) {
        super(parent);
        this.dynAreaVO = dynAreaVO;
        this.cellPos = cellPos;
        this.cellsModel = cellsModel;
        this.context = context;
        initialize();
        KeywordModel keywordModel = KeywordModel.getInstance(cellsModel);
        Hashtable allDynKeyVOs = keywordModel.getDynKeyVOPos(dynAreaVO.getDynamicAreaPK());
        if(allDynKeyVOs.size() > 1 ||
        		(allDynKeyVOs.size() == 1 && !allDynKeyVOs.keySet().toArray()[0].equals(cellPos))){
        	getJRadioButtonPublic().setEnabled(false);
        	getJRadioButtonPrivate().setEnabled(false);
        	KeyVO hasDefKeyVO = (KeyVO) allDynKeyVOs.values().toArray()[0];
        	if(hasDefKeyVO.isPrivate()){
            	setKeyState(false);
        	}else{
        		setKeyState(true);
        	}
        }else{
        	setKeyState(false);
        }
        setKeyVO(keyVO);
//        _hasTimeKeyVO = false;
        for(Iterator<CellPosition> iter = allDynKeyVOs.keySet().iterator();iter.hasNext();){
        	CellPosition cellTemp=iter.next();
        	KeyVO eachKeyVO =(KeyVO)  allDynKeyVOs.get(cellTemp) ;
        	if(eachKeyVO.isTTimeKeyVO()){
        		timeCell= cellTemp;
        		break;
        	}
        }
    }

    private void setKeyState(boolean isPublic) {
    	getJRadioButtonPublic().setSelected(!isPublic);
		getJRadioButtonPublic().setSelected(isPublic);
		getJRadioButtonPrivate().setSelected(!isPublic);
	}

	/**
     * This method initializes this
     * 
     * @return void
	 * @i18n uiiufofmt00015=动态区关键字定义
     */
    private void initialize() {
        this.setSize(404, 258);
        this.setTitle(StringResource.getStringResource("uiiufofmt00015"));
        this.setContentPane(getJContentPane());
        getJCheckBoxIsRef().setSelected(true);
        getJCheckBoxIsRef().setSelected(false);
    }

    /**
     * This method initializes jContentPane
     * 
     * @return javax.swing.JPanel
     * @i18n uiiufofmt00016=关键字名称：
     */
    private JPanel getJContentPane() {
        if (jContentPane == null) {
            jLabel = new nc.ui.pub.beans.UILabel();
            jLabel.setBounds(25, 62, 96, 31);
            jLabel.setText(StringResource.getStringResource("uiiufofmt00016"));
            jContentPane = new UIPanel();
            jContentPane.setLayout(null);
            jContentPane.add(getKeyRefButton(), null);
            jContentPane.add(getKeyNameTextField(), null);
            jContentPane.add(jLabel, null);
            jContentPane.add(getOkButton(), null);
            jContentPane.add(getCancleButton(), null);
            jContentPane.add(getDelButton(), null);
            jContentPane.add(getJRadioButtonPublic(), null);
            jContentPane.add(getJRadioButtonPrivate(), null);
            ButtonGroup buttonGroup = new ButtonGroup();
            buttonGroup.add(getJRadioButtonPublic());
            buttonGroup.add(getJRadioButtonPrivate());
            jContentPane.add(getJCheckBoxIsRef(), null);
            jContentPane.add(getJComboBox(), null);
        }
        return jContentPane;
    }

    /**
     * This method initializes jButton	
     * 	
     * @return javax.swing.JButton	
     * @i18n miufo1000766=参照
     */    
    private JButton getKeyRefButton() {
    	if (keyRefButton == null) {
    		keyRefButton = new nc.ui.pub.beans.UIButton();
    		keyRefButton.setBounds(295, 62, 75, 22);
    		keyRefButton.addActionListener(new java.awt.event.ActionListener() { 
    			public void actionPerformed(java.awt.event.ActionEvent e) {    
    				onKeyRef();
    			}
    		});
    		keyRefButton.setText(StringResource.getStringResource("miufo1000766"));
    	}
    	return keyRefButton;
    }

    /**
     * This method initializes jTextField	
     * 	
     * @return javax.swing.JTextField	
     */    
    private JTextField getKeyNameTextField() {
    	if (keyNameTextField == null) {
    		keyNameTextField = new UITextField();
    		keyNameTextField.setBounds(121, 62, 169, 31);
    	}
    	return keyNameTextField;
    }

    /**
     * 检查同一动态区中是否有重复私有关键字名称
     * @param strName
     * @param strKeyPK
     * @param strDynPK
     * @return
     */
    private boolean isDuplicateKeyName(String strName, String strKeyPK, String strDynPK){
    	if(strName==null || strDynPK==null )
    		return false;
    	
    	KeyVO[] dynKeys = getDynAreaModel().getKeywordModel().getDynKeyVOs(strDynPK);
    	KeyVO  toDelKey = getDynAreaModel().getKeywordModel().getKeyVOByPos(cellPos);
    	boolean bDuplicate = false;
    	if(dynKeys != null){
    		for(int i=0,size=dynKeys.length;i<size;i++){
    			if(dynKeys[i]==null || dynKeys[i].isPrivate() == false)
    				continue;
    			if((strKeyPK == null || !strKeyPK.equals(dynKeys[i].getKeywordPK())) 
    					&& (toDelKey == null || !toDelKey.getKeywordPK().equals(dynKeys[i].getKeywordPK()))
    					&& strName.equals(dynKeys[i].getName())){
    					bDuplicate=true;
    					break;
    			}
    		}
    	}
    	return bDuplicate;
    }
    
    /**
     * 检查多动态区是否是重复的关键字组合
     * add by guogang
     * @return
     */
    private boolean isDuplicatePubKeyGroup(KeyVO addKeyVo,String strDynPk){
    	boolean bDuplicate = false;
    	KeywordModel keyModle=getDynAreaModel().getKeywordModel();
    	Hashtable<String, Hashtable<CellPosition, String>> allKeyGroup=keyModle.getSubTableKeys();
    	Collection<String> addingKeyGroup=allKeyGroup.get(strDynPk).values();
    	if(allKeyGroup.size()<2){
    		return bDuplicate;
    	}
    	Collection<String> otherKeyGroup=null;
    	String otherDynPk=null;
    	boolean isEqualAll=false;
    	Iterator<String> it=null;
    	String key=null;
    	for(Enumeration<String> enumeration =allKeyGroup.keys();enumeration.hasMoreElements();){
    		otherDynPk=enumeration.nextElement();
    		isEqualAll=true;
    		if(!strDynPk.equals(otherDynPk)){
    		//获取别的动态区关键字集合
    		otherKeyGroup=allKeyGroup.get(otherDynPk).values();
    		//别的动态区包含要定义的关键字
    		if(otherKeyGroup!=null&&otherKeyGroup.size()-addingKeyGroup.size()==1&&otherKeyGroup.contains(addKeyVo.getKeywordPK())){
    			it=otherKeyGroup.iterator();
    			while(it.hasNext()){
    				key=it.next();
    				if(!key.equals(addKeyVo.getKeywordPK())&&!addingKeyGroup.contains(key)){
    					isEqualAll=false;
    				}
    			}
    			if(isEqualAll){
    				return true;
    			}
    		}
    		
    		}
    	
    	}
    	return bDuplicate;
    }
    /**
     * This method initializes jButton1	
     * 	
     * @return javax.swing.JButton	
     * @i18n miufo1003314=确定
     */    
    private JButton getOkButton() {
    	if (okButton == null) {
    		okButton = new nc.ui.pub.beans.UIButton();
    		okButton.setBounds(51, 165, 75, 22);    		
    		okButton.setText(StringResource.getStringResource("miufo1003314"));
    		okButton.addActionListener(new java.awt.event.ActionListener() { 
    			public void actionPerformed(java.awt.event.ActionEvent e) {
					if (isProcessedAndNotSortFilter()) {
						UfoPublic.sendWarningMessage(StringResource
								.getStringResource("miufo1004042"),
								DynAreaKeyMngDlg.this);
						return;
					} else if (getKeyNameTextField().getText() == null
							|| getKeyNameTextField().getText().trim().length() == 0) {
						UfoPublic.sendWarningMessage(StringResource
								.getStringResource("miufo73"),
								DynAreaKeyMngDlg.this);
					} else if (isMultiTimeKey()) {
						UfoPublic.sendWarningMessage(StringResource
								.getStringResource("miufo1001511"),
								DynAreaKeyMngDlg.this);
					} else {
						KeyVO keyTemp = getKeyVO();
						if (getDynAreaModel().getKeywordModel()
								.isDuplicateKeyName(keyTemp.getName(), true)
//								|| getDynAreaModel().getKeywordModel()
//										.isDuplicateKeyName(keyTemp.getName(),
//												false)
								||isDuplicateDynKeyName(keyTemp.getName()) // @edit by wangyga at 2008-12-26,下午04:05:01,关键字动态区不能重复
								|| isDuplicateKeyName(keyTemp.getName(),
										keyTemp.getKeywordPK(), dynAreaVO
												.getDynamicAreaPK())) {
							// TODO 处理字符串
							UfoPublic.sendWarningMessage(StringResource
									.getStringResource("miuforepkey0001"),
									DynAreaKeyMngDlg.this);// 私有关键字名称不能重复
						} else if (!keyTemp.isPrivate()
								&& isDuplicatePubKeyGroup(keyTemp, dynAreaVO
										.getDynamicAreaPK())) {
							UfoPublic
									.sendWarningMessage(
											MultiLangInput
													.getString("verify_dynarea_keycomb_notduplicate"),
											DynAreaKeyMngDlg.this);
						} else {
							String strRepId = context.getAttribute(REPORT_PK) == null ? null : (String)context.getAttribute(REPORT_PK);
							
							getDynAreaModel().getKeywordModel()
									.setDynAreaKeyVO(
											dynAreaVO.getDynamicAreaPK(),
											cellPos, keyTemp, strRepId);
							close();
						}
					}
				}
    			/**
    			 * 判断一个动态区只能有一个时间关键字。
    			 * @return
    			 */
				private boolean isMultiTimeKey() {
					KeyVO keyTemp=getKeyVO();
					return (keyTemp.isTTimeKeyVO()&& (timeCell!=null && timeCell.equals(cellPos)==false));
//					return _hasTimeKeyVO && keyVOgetKeyVO().isTimeKeyVO();
				}               
    		});
    	}
    	return okButton;
    }

    /**
     * This method initializes jButton2	
     * 	
     * @return javax.swing.JButton	
     * @i18n miufo1003315=取消
     */    
    private JButton getCancleButton() {
    	if (cancleButton == null) {
    		cancleButton = new nc.ui.pub.beans.UIButton();
    		cancleButton.setBounds(172, 165, 75, 22);
    		cancleButton.addActionListener(new java.awt.event.ActionListener() { 
    			public void actionPerformed(java.awt.event.ActionEvent e) {    
    			    close();
    			}
    		});
    		cancleButton.setText(StringResource.getStringResource("miufo1003315"));
    	}
    	return cancleButton;
    }

    /**
     * This method initializes jButton3	
     * 	
     * @return javax.swing.JButton	
     * @i18n ubiquery0110=删除
     */    
    private JButton getDelButton() {
    	if (delButton == null) {
    		delButton = new nc.ui.pub.beans.UIButton();
    		delButton.setBounds(293, 165, 75, 22);
    		delButton.addActionListener(new java.awt.event.ActionListener() { 
    			public void actionPerformed(java.awt.event.ActionEvent e) {
    				if(isProcessedAndNotSortFilter()){
    					UfoPublic.sendWarningMessage(StringResource.getStringResource("miufo1004042"),DynAreaKeyMngDlg.this);
    					return;
    				} 
    				Object repIdObj = context.getAttribute(REPORT_PK);
                    String strRepId = repIdObj != null && (repIdObj instanceof String)? (String)repIdObj:null; 
                    
//    				String reportPK = report.getContextVo().getContextId();
    				getDynAreaModel().getKeywordModel().removeDynAreaKeyVOByPos(dynAreaVO.getDynamicAreaPK(), cellPos,strRepId);
                    close();
    			}
    		});
    		delButton.setText(StringResource.getStringResource("ubiquery0110"));
    	}
    	return delButton;
    }

    private void onKeyRef() {  
        Vector hasDefVec = new Vector();
        KeyVO[] mainKeyVOs = getDynAreaModel().getKeywordModel().getMainKeyVOs();
        hasDefVec.addAll(Arrays.asList(mainKeyVOs));

        //对时间关键字处理，如果主表定义了时间关键字，则子表只能参照KeyVO.getTimeKeyIndex()比当前关键字值大的关键字
        KeyVO timeVO = null;
        for (int i = 0; i < hasDefVec.size(); i++) {
        	timeVO = (KeyVO) hasDefVec.get(i);
        	if(timeVO.getType() == KeyVO.TYPE_TIME){
        		int timeIndex = timeVO.getTimeKeyIndex();//自然时间               
                if (timeIndex > 0) {
                    KeyVO buildInVO = CacheProxy.getSingleton()
                            .getKeywordCache().getByPK(KeyVO.YEAR_PK);
                    if (timeIndex > buildInVO.getTimeKeyIndex()) {
                        hasDefVec.addElement(buildInVO);
                    }                  
                    buildInVO = CacheProxy.getSingleton().getKeywordCache()
                            .getByPK(KeyVO.HALF_YEAR_PK);
                    if (timeIndex > buildInVO.getTimeKeyIndex()) {
                        hasDefVec.addElement(buildInVO);
                    }
                    buildInVO = CacheProxy.getSingleton().getKeywordCache()
                            .getByPK(KeyVO.QUARTER_PK);
                    if (timeIndex > buildInVO.getTimeKeyIndex()) {
                        hasDefVec.addElement(buildInVO);
                    }                  
                    buildInVO = CacheProxy.getSingleton().getKeywordCache()
                            .getByPK(KeyVO.MONTH_PK);
                    if (timeIndex > buildInVO.getTimeKeyIndex()) {
                        hasDefVec.addElement(buildInVO);
                    }                                  
                    buildInVO = CacheProxy.getSingleton().getKeywordCache()
                            .getByPK(KeyVO.TENDAYS_PK);
                    if (timeIndex > buildInVO.getTimeKeyIndex()) {
                        hasDefVec.addElement(buildInVO);
                    }
                    buildInVO = CacheProxy.getSingleton().getKeywordCache()
                            .getByPK(KeyVO.WEEK_PK);
                    if (timeIndex > buildInVO.getTimeKeyIndex()) {
                        hasDefVec.addElement(buildInVO);
                    }
                    buildInVO = CacheProxy.getSingleton().getKeywordCache()
                            .getByPK(KeyVO.DAY_PK);
                    if (timeIndex > buildInVO.getTimeKeyIndex()) {
                        hasDefVec.addElement(buildInVO);
                    }
        	}           
                break;
            }else if(timeVO.getType() == KeyVO.TYPE_ACC){
        		int timeIndex = timeVO.getAccPeriodKeyIndex();//会计期间
                if (timeIndex > 0) {                    
                    KeyVO buildInVO = CacheProxy.getSingleton().getKeywordCache()
    						.getByPK(KeyVO.ACC_YEAR_PK);
    				if (timeIndex > buildInVO.getAccPeriodKeyIndex()) {
    					hasDefVec.addElement(buildInVO);
    				}                  
                    buildInVO = CacheProxy.getSingleton().getKeywordCache()
    						.getByPK(KeyVO.ACC_SEASON_PK);
    				if (timeIndex > buildInVO.getAccPeriodKeyIndex()) {
    					hasDefVec.addElement(buildInVO);
    				}                  
                    buildInVO = CacheProxy.getSingleton().getKeywordCache()
    						.getByPK(KeyVO.ACC_MONTH_PK);
    				if (timeIndex > buildInVO.getAccPeriodKeyIndex()) {
    					hasDefVec.addElement(buildInVO);    				}                
                   
        	}           
                break;
            
            }
        }
        if(dynAreaVO != null){
            Collection dynKeyVOs = getDynAreaModel().getKeywordModel().getKeyVOByArea(dynAreaVO.getOriArea()).values();
            hasDefVec.addAll(dynKeyVOs);
        }
        
        KeyVO[] vos = new KeyVO[hasDefVec.size()];
        hasDefVec.copyInto(vos);
        //modify by 王宇光 2008-6-10 此参照会过滤事件类型，只显示一种类型：自然时间或者会计期间
        ICacheObject[] cacheVos = CacheProxy.getSingleton().getKeywordCache().getAll();   
        if(isAnaPeport()){//分析表过滤掉会计类关键字
        	cacheVos = filterKeyVo(cacheVos);
        }
        DynKeywordRefDlg dlgKR = new DynKeywordRefDlg(DynAreaKeyMngDlg.this, vos,getTimeVo(),cacheVos);
        if (dlgKR.isInitOver) {
            dlgKR.setLocationRelativeTo(DynAreaKeyMngDlg.this);
            dlgKR.setModal(true);
            dlgKR.show();
            if(dlgKR.getRefVO() != null){
                setKeyVO(dlgKR.getRefVO());                
            }
        }
    }
       
    private void setKeyVO(KeyVO refVO) {
        this.keyVO =refVO==null?null: (KeyVO) refVO.clone();   
        if(keyVO != null){
            if(!keyVO.isPrivate()){
            	getJRadioButtonPublic().setSelected(false);
            	getJRadioButtonPublic().setSelected(true);
            }else{      
            	getJRadioButtonPrivate().setSelected(false);
                getJRadioButtonPrivate().setSelected(true);
                if(keyVO.getRef() != null){
                	getJCheckBoxIsRef().setSelected(true);
                	try {
    					CodeVO codeVO = CacheProxy.getSingleton().getCodeCache()
    							.findCodeByID(keyVO.getRef());
    					getJComboBox().setSelectedItem(codeVO); 					
    				} catch (Exception e) {
    					AppDebug.debug(e);
    				}            	
                }
            }        	
            getKeyNameTextField().setText(keyVO.getName());
        }
    }
        
    /**
	 * add by 王宇光 2008-6-11 获得主表或者字表的时间关键字
	 * 
	 * @param
	 * @return KeyVO
	 */
    private KeyVO getTimeVo(){
    	KeywordModel keyModel = getDynAreaModel().getKeywordModel();
    	DynAreaCell[] dynAreaCells = getDynAreaModel().getDynAreaCells();
    	HashMap mainTableKeyVOs = keyModel.getMainKeyVOPos();
    	if(mainTableKeyVOs != null && mainTableKeyVOs.size() >0){
    		for (Iterator iter = mainTableKeyVOs.keySet().iterator(); iter.hasNext();) {
                KeyVO each = (KeyVO) iter.next();
                if(each.getType() == KeyVO.TYPE_TIME || each.getType() == KeyVO.TYPE_ACC ){
                	return each;	                	
                }
            }
    	}
        
        if(dynAreaCells !=null && dynAreaCells.length > 0){
        	int iLength = dynAreaCells.length;
			for (int i = 0; i < iLength; i++) {
				Collection keys = keyModel.getKeyVOByArea(dynAreaCells[i].getArea()).values();
	            for (Iterator iter = keys.iterator();iter.hasNext();) {
	                KeyVO each = (KeyVO) iter.next();
	                if(each.getType() == KeyVO.TYPE_TIME || each.getType() == KeyVO.TYPE_ACC ){
	                	return each;	                	
	                }
			}
          }
        }   
        return null;
    }   
    
    /**
     * add by wangyga 2008-7-10 过滤会计期间关键字
     * @param vec
     * @return
     */
    private ICacheObject[] filterKeyVo(ICacheObject[] cacheVos){
    	Vector<KeyVO> newVector = new Vector<KeyVO>();
    	if(cacheVos != null && cacheVos.length > 0){
    		int iSize = cacheVos.length;
    		for(int i = 0;i < iSize;i++){
    			KeyVO keyVo = (KeyVO)cacheVos[i];
    			if(!keyVo.isAccPeriodKey()){
    				newVector.add(keyVo);
    			}
    		}
    	}    	
        return newVector.toArray(new KeyVO[0]);
    }
    
    
    /**
     * add by wangyga 判断报表类型
     * @see nc.vo.iuforeport.rep.ReportDirVO
     * @return
     */
    private boolean isAnaPeport(){
    	boolean isAnaRep = context.getAttribute(ANA_REP) == null ? false : Boolean.parseBoolean(context.getAttribute(ANA_REP).toString());       	  	
    	return isAnaRep;   	
    }
    
    /*
     * modify by guogang 2007-12-20 增加对私有关键字有参照改为取消参照的处理
     */
    private KeyVO getKeyVO() {
        if(getJRadioButtonPrivate().isSelected()){
            if(getKeyNameTextField().getText().trim().equals("")){
                keyVO = null;
            }else{
            	if(keyVO==null){
            		 keyVO = new KeyVO();  
            		 keyVO.setType(KeyVO.TYPE_CHAR);
                     keyVO.setLen(64);
                     keyVO.setIsPrivate(true);
                     keyVO.setKeywordPK(IDMaker.makeID(20));
                     keyVO.setIsStop(new nc.vo.pub.lang.UFBoolean(false));
                     keyVO.setIsBuiltIn(new nc.vo.pub.lang.UFBoolean(false));
                     Object repIdObj = context.getAttribute(REPORT_PK);
                     String strRepId = repIdObj != null && (repIdObj instanceof String)? (String)repIdObj:null; 
                     keyVO.setRepIdOfPrivate(strRepId);  
            	}
               
                keyVO.setName(getKeyNameTextField().getText().trim());  
                if(getJCheckBoxIsRef().isSelected()){
                	keyVO.setRef(((CodeVO)getJComboBox().getSelectedItem()).getId());
                	if (keyVO.getRef()!=null && keyVO.getRef().trim().length()>0)
                		keyVO.setType(KeyVO.TYPE_REF);
                }else{
                	if (keyVO.isPrivate()&&keyVO.getRef()!=null && keyVO.getRef().trim().length()>0){
                		keyVO.setRef(null);
                		keyVO.setType(KeyVO.TYPE_CHAR);
                	}
                }
            }
        }
        return keyVO;
    }
    private DynAreaModel getDynAreaModel(){   	
//        DynAreaDefPlugIn pi = (DynAreaDefPlugIn) report.getPluginManager().getPlugin(DynAreaDefPlugIn.class.getName());
//        return pi.getDynAreaModel();
    	return DynAreaModel.getInstance(cellsModel);
    }
	/**
	 * This method initializes RadioButtonPublic	
	 * 	
	 * @return javax.swing.JRadioButton	
	 * @i18n miufo1003455=公有
	 */    
	private JRadioButton getJRadioButtonPublic() {
		if (jRadioButtonPublic == null) {
			jRadioButtonPublic = new UIRadioButton();
			jRadioButtonPublic.setBounds(29, 24, 72, 21);
			jRadioButtonPublic.setText(StringResource.getStringResource("miufo1003455"));
			jRadioButtonPublic.addItemListener(new ItemListener(){
				public void itemStateChanged(ItemEvent e) {
					if(e.getStateChange() == ItemEvent.SELECTED){
						//私有改为公有时
						getKeyNameTextField().setEditable(false);
						getKeyRefButton().setEnabled(true);
						getJCheckBoxIsRef().setEnabled(false);
						getJCheckBoxIsRef().setSelected(false);
					}else{
						//公有改为私有时
						getKeyNameTextField().setEditable(true);
						getKeyRefButton().setEnabled(false);
						getJCheckBoxIsRef().setEnabled(true);
					}
				}				
			});
			jRadioButtonPublic.addMouseListener(new MouseAdapter(){
				@Override
				public void mouseClicked(MouseEvent e) {
					getKeyNameTextField().setText("");
					keyVO = null;
				}				
			});
		}
		return jRadioButtonPublic;
	}
	/**
	 * This method initializes jRadioButtonPrivate	
	 * 	
	 * @return javax.swing.JRadioButton	
	 * @i18n miufo1003456=私有
	 */    
	private JRadioButton getJRadioButtonPrivate() {
		if (jRadioButtonPrivate == null) {
			jRadioButtonPrivate = new UIRadioButton();
			jRadioButtonPrivate.setBounds(103, 24, 82, 21);
			jRadioButtonPrivate.setText(StringResource.getStringResource("miufo1003456"));
			jRadioButtonPrivate.addMouseListener(new MouseAdapter(){
				@Override
				public void mouseClicked(MouseEvent e) {
					getKeyNameTextField().setText("");
					keyVO = null;
				}				
			});
		}
		return jRadioButtonPrivate;
	}
	/**
	 * This method initializes jCheckBoxIsRef	
	 * 	
	 * @return javax.swing.JCheckBox	
	 * @i18n uiiufofmt00017=是否参照
	 */    
	private JCheckBox getJCheckBoxIsRef() {
		if (jCheckBoxIsRef == null) {
			jCheckBoxIsRef = new UICheckBox();
			jCheckBoxIsRef.setBounds(25, 108, 96, 31);
			jCheckBoxIsRef.setText(StringResource.getStringResource("uiiufofmt00017"));
			jCheckBoxIsRef.addItemListener(new ItemListener(){
				public void itemStateChanged(ItemEvent e) {
					getJComboBox().setEnabled(e.getStateChange() == ItemEvent.SELECTED);
				}				
			});
		}
		return jCheckBoxIsRef;
	}
	/**
	 * This method initializes jComboBox	
	 * 	
	 * @return javax.swing.JComboBox	
	 */    
	private JComboBox getJComboBox() {
		if (jComboBox == null) {
			jComboBox = new UIComboBox();
			jComboBox.setBounds(123, 108, 169, 31);
			
			CodeVO[] codeVOs = CacheProxy.getSingleton().getCodeCache().getAllCode();
	    	Arrays.sort(codeVOs);
	    	
			for (int i = 0; i < codeVOs.length; i++) {
				CodeVO eachCodeVO = codeVOs[i];
				 if (eachCodeVO.getId().equals(CodeVO.UNIT_CODE_ID)){
					 continue;
				 }
				jComboBox.addItem(eachCodeVO);
		
				jComboBox.setRenderer(new ListCellRenderer() {
					public Component getListCellRendererComponent(JList list,
							Object value, int index, boolean isSelected,
							boolean cellHasFocus) {
						
						
						String codeName="";
						if(value!=null&&value instanceof CodeVO){
							codeName=((CodeVO) value).getName();
						}
						JLabel lb = new nc.ui.pub.beans.UILabel(codeName);
						lb.setOpaque(true);
//						if (isSelected) {
//							lb.setBackground(list.getSelectionBackground());
//							lb.setForeground(list.getSelectionForeground());
//						} else {
//							lb.setBackground(list.getBackground());
//							lb.setForeground(list.getForeground());
//						}
						return lb;
					}
				});
			}
		}
		return jComboBox;
	}
	
	/**
	 * 除去当前选中动态区单元的关键字,交验动态区之间关键字不能重复
	 * @create by wangyga at 2008-12-26,下午03:48:37
	 *
	 * @param strName
	 * @return
	 */
	public boolean isDuplicateDynKeyName(String strName){
		if(strName==null){
			return false;
		}
		boolean bDuplicate = false;
		ArrayList<KeyVO> allKeys = getSubTableKeyVO();
		KeyVO tmpKey;
		for(int i=0;i<allKeys.size();i++){
			tmpKey=allKeys.get(i);
			if(strName.equals(tmpKey.getName())){
				bDuplicate=true;
				break;
			}
		}
		return bDuplicate;
	}
	
	public ArrayList<KeyVO> getSubTableKeyVO() {
		ArrayList<KeyVO> result = new ArrayList<KeyVO>();
		ArrayList<String> allPKs = new ArrayList<String>();
		KeywordModel keyWordModel = getDynAreaModel().getKeywordModel();
		CellPosition curPos = getAnchorCell();
		Hashtable<String, Hashtable<CellPosition, String>> subTableKeys = keyWordModel.getSubTableKeys();
		for (Iterator<Hashtable<CellPosition, String>> iterator = subTableKeys
				.values().iterator(); iterator.hasNext();) {
			Hashtable<?, String> dynKeyPos = iterator.next();
			if(!dynKeyPos.containsKey(curPos)){
				allPKs.addAll(dynKeyPos.values());
			}			
		}
		for (Iterator<String> iterator = allPKs.iterator(); iterator.hasNext();) {
			result.add(keyWordModel.getKeyVOByPK(iterator.next()));
		}
		return result;
	}
	
	private CellPosition getAnchorCell(){
		return cellsModel.getSelectModel().getAnchorCell();
	}
	
	/**
	 * 检查当前动态区是否定义除排序或过滤外的数据处理
	 * @return
	 */
	private boolean isProcessedAndNotSortFilter(){
//		CellsModel cellsModel = report.getCellsModel();
        DynAreaModel dynAreaModel = DynAreaModel.getInstance(cellsModel); 
        return dynAreaModel.isProcessedAndNotSortFilter(dynAreaVO.getDynamicAreaPK());
	}
}  //  @jve:decl-index=0:visual-constraint="65,64"
 