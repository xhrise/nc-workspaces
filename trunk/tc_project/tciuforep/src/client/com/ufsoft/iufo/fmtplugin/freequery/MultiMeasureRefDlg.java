package com.ufsoft.iufo.fmtplugin.freequery;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellEditor;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import nc.itf.iufo.exproperty.IExPropConstants;
import nc.pub.iufo.cache.ReportDirCache;
import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iufo.exproperty.ExPropOperator;
import nc.ui.iufo.exproperty.IExPropOperator;
import nc.ui.pub.beans.UIScrollPane;
import nc.vo.iufo.exproperty.ExPropertyVO;
import nc.vo.iufo.keydef.KeyGroupVO;
import nc.vo.iufo.measure.MeasureVO;
import nc.vo.iufo.measure.UnitExInfoVO;
import nc.vo.iuforeport.rep.ReportDirVO;
import nc.vo.iuforeport.rep.ReportVO;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.fmtplugin.measure.MeasRefTreeNode;
import com.ufsoft.iufo.fmtplugin.measure.MeasureRefDlg;
import com.ufsoft.iufo.fmtplugin.measure.MeasureRefRightPanel;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.util.UfoPublic;

/**
 * 指标参照界面，增加多选功能
 * 
 * @author ll
 * 
 */
public class MultiMeasureRefDlg extends MeasureRefDlg {

	private static final long serialVersionUID = 1L;
	private static String TITLE_UNITINFO = "miufoiufoddc012";
	
	private MeasureVO[] m_SelMeasures = null;
	private String m_selKeyCombPk=null;

	private JPanel m_measListPanel = null;
	private JScrollPane jScrollPane1 = null;
	private JScrollPane jScrollPaneUnit = null;
	private UniqueList m_measList = null;
	private DefaultListModel m_measListModel = null;
	private JPanel m_measOprPanel = null;
	private JButton m_btnListAdd = null;
	private JButton m_btnListRemove = null;
	private JButton m_btnListUp = null;
	private JButton m_btnListDown = null;

	private UnitExInfoPanel m_unitPanel = null;

	/**
	 * 
	 * @param parent
	 * @param currentRepVO
	 * @param currentKeyGroupVO
	 * @param strUserPK
	 * @param isContains
	 *            是否包含本表指标
	 * @param bRepMgr
	 * @param excludeMeasures
	 */
	public MultiMeasureRefDlg(JDialog parent, ReportVO currentRepVO, KeyGroupVO currentKeyGroupVO, String strUserPK,
			boolean isContains, boolean bRepMgr, boolean bIncludeRefMeas, MeasureVO[] selMeasureVOs) {
		// 不送关键字组合信息，而是按照第一个选择的指标进行过滤
		super(parent, currentRepVO, null, strUserPK, isContains, bRepMgr, bIncludeRefMeas, null);

		m_SelMeasures = selMeasureVOs;
		getRightPanelList().setSelMeasureVOs(selMeasureVOs);
		getRightPanelSample().setSelMeasureVOs(selMeasureVOs);

		refButton.setText(StringResource.getStringResource("miufo1000758"));// 确定
		closeButton.setText(StringResource.getStringResource("miufo1000757"));// 取消

		initMeasureListPanel();
		addUnitInfo();
	}

	private void initMeasureListPanel() {
		setSize(getSize().width + 200, getSize().height + 50);
		getJContentPane().add(getMeasListPanel(), BorderLayout.EAST);
	}

	/**
	 * This method initializes rightPanel
	 * 
	 * @return javax.swing.JPanel
	 */
	protected MeasureRefRightPanel getRightPanelList() {
		if (rightMeasureListPanel == null) {
			rightMeasureListPanel = new MultiSelMeasureRefRightPanelList(this, isContainsCurrentReport,
					m_oCurrentKeyGroupVO, excludeMeasuresList, isIncludeRefMeasures()){
				@Override
				protected void initSelectedAction(ChangeEvent e) {
					TableCellEditor editor = getCheckBoxEditor();
			        if (editor != null) {
			            Object value = editor.getCellEditorValue();
			            if(value instanceof Boolean){
			            	if(((Boolean) value).booleanValue()){
			            	     addObjToSelList(getSelectedMeasureVO());
			            	}else{
			            		getMeasureListModel().removeElement(getSelectedMeasureVO());
			            	}
			            }
			        }
				}
		
	};
		}
		return rightMeasureListPanel;
	}

	protected MeasureRefRightPanel getRightPanelSample() {
		if (rightMeasureSamplePanel == null) {
			rightMeasureSamplePanel = new MultiSelMeasureRefRightPanelSample(this, isContainsCurrentReport,
					m_oCurrentKeyGroupVO, excludeMeasuresList, isIncludeRefMeasures());
		}
		return rightMeasureSamplePanel;
	}

	public MeasureVO[] getSelMeasureVOs() {
		return (MeasureVO[]) getSelVOsFromList(true);
		// if (m_SelMeasures != null && m_SelMeasures.length == 0)
		// m_SelMeasures = null;
		// return m_SelMeasures;
	}

	public UnitExInfoVO[] getSelUnitexVOs() {
		return (UnitExInfoVO[]) getSelVOsFromList(false);
	}

	public void actionPerformed(java.awt.event.ActionEvent event) {
		if (event.getSource() == refButton) {// 确定
			if (isCurrShowList())
				m_SelMeasures = getRightPanelList().getSelMeasureVOs();
			else {
				MeasureVO[] selVOs = getRightPanelSample().getSelMeasureVOs();
				addMeasureVOToResult(selVOs);// 将选中指标加入结果列表中
			}
			setResult(ID_OK);
			if (getSelMeasureVOs() == null||getSelMeasureVOs().length==0){
				JOptionPane.showMessageDialog(this, StringResource.getStringResource("miufo00113"));
			}else{
				this.close();
			}
		} else if (event.getSource() == closeButton) {
			setResult(ID_CANCEL);
			this.close();
		}
	
	}

	private void addMeasureVOToResult(MeasureVO[] selVOs) {
		if (selVOs == null || selVOs.length == 0)
			return;
		if (m_SelMeasures == null || m_SelMeasures.length == 0) {
			m_SelMeasures = selVOs;
			return;
		}
		String oldKeyCombPk = m_SelMeasures[0].getKeyCombPK();
		ArrayList<MeasureVO> als = new ArrayList<MeasureVO>();
		for (MeasureVO newVO : selVOs) {
			if (newVO.getKeyCombPK().equals(oldKeyCombPk))
				als.add(newVO);

		}
		if (als.size() == 0)
			return;

		int mLen = m_SelMeasures.length;
		MeasureVO[] results = new MeasureVO[mLen + als.size()];
		System.arraycopy(m_SelMeasures, 0, results, 0, mLen);
		System.arraycopy(als.toArray(new MeasureVO[0]), 0, results, mLen, als.size());
		m_SelMeasures = results;
	}

	private JPanel getMeasListPanel() {
		if (m_measListPanel == null) {
			m_measListPanel = new JPanel();
			m_measListPanel.setPreferredSize(new Dimension(200, 400));
			m_measListPanel.setLayout(new BorderLayout());
			JLabel lblList = new JLabel();
			lblList.setText(StringResource.getStringResource("uiufomquery0006"));// uiufomquery0006=已选指标:
			m_measListPanel.add(getMeasOperPanel(), BorderLayout.WEST);
			m_measListPanel.add(lblList, BorderLayout.NORTH);
			JPanel centerPanel = new JPanel();
			centerPanel.setLayout(new BorderLayout());
			centerPanel.add(lblList, BorderLayout.NORTH);
			centerPanel.add(getJScrollPane1(), BorderLayout.CENTER);

			m_measListPanel.add(centerPanel, BorderLayout.CENTER);
		}
		return m_measListPanel;
	}

	private JScrollPane getJScrollPane1() {
		if (jScrollPane1 == null) {
			jScrollPane1 = new UIScrollPane();
			jScrollPane1.setViewportView(getMeasureList());
		}
		return jScrollPane1;
	}
	private JScrollPane getJScrollPaneUnit() {
		if (jScrollPaneUnit == null) {
			jScrollPaneUnit = new UIScrollPane();
			jScrollPaneUnit.setViewportView(getUnitInfoPanel());
		}
		return jScrollPaneUnit;
	}

	private UniqueList getMeasureList() {
		if (m_measList == null) {
			m_measList = new UniqueList() {
				private static final long serialVersionUID = 1L;

				@Override
				public int getObjIndex(Object obj) {
					DefaultListModel model = (DefaultListModel) getModel();
					for (int i = 0; i < model.size(); i++) {
						Object elem = model.get(i);
						if (elem.equals(obj))
							return i;

					}
					// if (obj instanceof MeasureVO) {
					// return ((DefaultListModel) getModel()).indexOf(obj);
					// }
					return -1;
				}
			};
			m_measList.setModel(getMeasureListModel());
			if (m_SelMeasures != null&&m_SelMeasures[0]!=null) {
				if (m_selKeyCombPk == null) {
					m_selKeyCombPk = m_SelMeasures[0].getKeyCombPK();
				}
				for (MeasureVO meas : m_SelMeasures){
					addObjToSelList(meas);
				}
					
			}
			
			m_measList.addListSelectionListener(new ListSelectionListener() {

				public void valueChanged(ListSelectionEvent e) {
					Object obj = m_measList.getSelectedValue();
					if (obj instanceof UnitExInfoVO) {
						setIsSampleEnabled(false);// 控制表样形式不可选
						changeUnitInfoPanel(true);
						UnitExInfoVO selUnits =(UnitExInfoVO) obj ;
						getUnitInfoPanel().setRowSelectFromVO(selUnits);
						getUnitInfoPanel().setSelUnitInfo(getSelUnitexVOs());
					} else if (obj instanceof MeasureVO) {
						
						if(_selReportVO==null||_selReportVO.getReportPK()==null||!(_selReportVO.getReportPK().equals(((MeasureVO)obj).getReportPK()))){
							_selReportVO= (ReportVO) reportCache.get(((MeasureVO)obj).getReportPK());
						    setIsSampleEnabled(true);
							changeUnitInfoPanel(false);
							
						}
						if(!isSampleEnabled()){
							setIsSampleEnabled(true);
							changeUnitInfoPanel(false);
						}
							
						rightListToLeftTree(_selReportVO);
						
						getRightPanelList().setSelMeasureVOs(getSelMeasureVOs());
						getRightPanelList().repaint();
					}
				}

			});

		}
		return m_measList;
	}
	
	private void rightListToLeftTree(ReportVO reportVo){

		ArrayList<MeasRefTreeNode> dirnodes=new ArrayList<MeasRefTreeNode>();
		MeasRefTreeNode dirnode=null;
		MeasRefTreeNode repnode=null;
		String dirId=reportVo.getRepDir();
		if(dirId!=null){
			ReportDirCache  dirCache=IUFOUICacheManager.getSingleton().getReportDirCache();
			ReportDirVO dirVO=dirCache.getReportDir(dirId);
			boolean rootbeging=false;
            while (dirVO != null) {
				dirnode = new MeasRefTreeNode(MeasureRefDlg.ICON_REPORTDIR);
				if(dirVO.getDirName().indexOf("_")>0){
					dirnode.setName(StringResource.getStringResource("uiuforep000102"));
					rootbeging=true;
				}else{
					dirnode.setName(dirVO.getDirName());
				}
				if(!rootbeging){
				   dirnode.setPk(dirVO.getDirId());
				}
				dirnodes.add(0, dirnode);
				if (dirVO.getParentDirPK() != null) {
					dirVO = dirCache.getReportDir(dirVO.getParentDirPK());
				} else {
					dirVO = null;
				}

			}
            
			repnode= new MeasRefTreeNode(
					MeasureRefDlg.ICON_REPORT);
					repnode.setName(reportVo.getName());
					repnode.setPk(reportVo.getReportPK());
					repnode.setReportCode(reportVo.getCode());

			if (dirnodes.size() > 0) {
				dirnode = dirnodes.get(0);
				for (int i = 1; i < dirnodes.size(); i++) {
					dirnode.addSubNode(dirnodes.get(i));
					dirnode=dirnodes.get(i);
				}
				dirnode.addSubNode(repnode);
			}

	        TreePath path=new TreePath(((DefaultTreeModel)getLeftTree().getModel()).getPathToRoot(repnode));						       
			getLeftTree().setSelectionPath(path);
		}

	
	}

	private DefaultListModel getMeasureListModel() {
		if (m_measListModel == null) {
			m_measListModel = new DefaultListModel();
		}
		return m_measListModel;
	}

	private JPanel getMeasOperPanel() {
		if (m_measOprPanel == null) {
			m_measOprPanel = new JPanel();
			// m_measOprPanel.setLayout(new BoxLayout(m_measOprPanel,
			// BoxLayout.Y_AXIS));
			m_measOprPanel.setLayout(null);
			m_measOprPanel.setSize(40, 200);
			m_measOprPanel.setPreferredSize(new Dimension(40, 200));
			m_measOprPanel.add(getBtnAdd());
			m_measOprPanel.add(getBtnRemove());
			m_measOprPanel.add(getBtnUp());
			m_measOprPanel.add(getBtnDown());

		}
		return m_measOprPanel;
	}

	private JButton getBtnAdd() {
		if (m_btnListAdd == null) {
			m_btnListAdd = new JButton();
			m_btnListAdd.setBounds(5, 40, 30, 22);
			m_btnListAdd.setText(StringResource.getStringResource("miufo1000950"));// miufo1000950=添加
			m_btnListAdd.addActionListener(new ListOperListener());
		}
		return m_btnListAdd;
	}

	private JButton getBtnRemove() {
		if (m_btnListRemove == null) {
			m_btnListRemove = new JButton();
			m_btnListRemove.setBounds(5, 80, 30, 22);
			m_btnListRemove.setText(StringResource.getStringResource("miufo1001641")); // miufo1001641=删除
			m_btnListRemove.addActionListener(new ListOperListener());
		}
		return m_btnListRemove;
	}

	private JButton getBtnUp() {
		if (m_btnListUp == null) {
			m_btnListUp = new JButton();
			m_btnListUp.setBounds(5, 120, 30, 22);
			m_btnListUp.setText(StringResource.getStringResource("miufo1001650"));// miufo1001650=向上
			m_btnListUp.addActionListener(new ListOperListener());
		}
		return m_btnListUp;
	}

	private JButton getBtnDown() {
		if (m_btnListDown == null) {
			m_btnListDown = new JButton();
			m_btnListDown.setBounds(5, 160, 30, 22);
			m_btnListDown.setText(StringResource.getStringResource("miufo1001648"));// miufo1001648=向下
			m_btnListDown.addActionListener(new ListOperListener());
		}
		return m_btnListDown;
	}

	private class ListOperListener implements ActionListener {

		/**
		 * @i18n miufo00401=不允许新加指标与已加指标关键字组合不同
		 */
		public void actionPerformed(ActionEvent e) {
			UniqueList list = getMeasureList();
			DefaultListModel model = (DefaultListModel) list.getModel();
			if (e.getSource() == getBtnAdd()) {
				boolean isUnitInfo = !isSampleEnabled();
				if (isUnitInfo) {
					UnitExInfoVO[] selUnitInfo = getUnitInfoPanel().getSelPropVOs();
					if (selUnitInfo != null) {
						for (UnitExInfoVO vo : selUnitInfo) {
							addObjToSelList(vo);
						}
					}
				} else {
					MeasureVO[] selMeasures = null;
					if (isCurrShowList()) {// 当前是列表选择
						selMeasures = getRightPanelList().getSelMeasureVOs();
					} else {// 表样选择
						selMeasures = getRightPanelSample().getSelMeasureVOs();
					}
					if (selMeasures != null&&selMeasures[0]!=null) {
						if (m_selKeyCombPk == null) {
							m_selKeyCombPk = selMeasures[0].getKeyCombPK();
						}
						for (MeasureVO meas : selMeasures) {
							if (!m_selKeyCombPk
									.equals(meas.getKeyCombPK())) {
								UfoPublic.sendWarningMessage(
										StringResource
												.getStringResource("miufo00401"), MultiMeasureRefDlg.this);
								return;
							}

							addObjToSelList(meas);
							if(!isCurrShowList()){
								((MultiSelMeasureRefRightPanelList)getRightPanelList()).addSelectedMeasure(meas);
							}

						}
					}
				}
			} else if (e.getSource() == getBtnRemove()) {
				int[] index = list.getSelectedIndices();

				if (index != null) {
					for (int i = index.length - 1; i >= 0; i--) {
						model.remove(index[i]);
					}
					if (index[0] < model.size())
						list.setSelectedIndex(index[0]);
					else
						list.setSelectedIndex(model.size() - 1);
					
					if(model.size()==0){
						m_selKeyCombPk=null;
					}
					
				}
			} else if (e.getSource() == getBtnUp()) {
				int index = list.getSelectedIndex();
				if (index > 0) {
					Object obj = model.get(index);
					model.remove(index);
					model.insertElementAt(obj, index - 1);
					list.setSelectedIndex(index - 1);
				}
			} else if (e.getSource() == getBtnDown()) {
				int index = list.getSelectedIndex();
				if (index >= 0 && index < model.size() - 1) {
					Object obj = model.get(index);
					model.remove(index);
					model.insertElementAt(obj, index + 1);
					list.setSelectedIndex(index + 1);
				}
			}
		}
	}

	/**
	 * 为左边的树增加单位信息，并增加相应处理
	 */
	private void addUnitInfo() {
		DefaultTreeModel treeModel = (DefaultTreeModel) getLeftTree().getModel();
		DefaultMutableTreeNode root = (DefaultMutableTreeNode) treeModel.getRoot();
		DefaultMutableTreeNode unitInfoNode = new DefaultMutableTreeNode();
		unitInfoNode.setUserObject(StringResource.getStringResource(TITLE_UNITINFO));// miufoiufoddc012=单位信息
		root.insert(unitInfoNode, 0);
		treeModel.setRoot(root);
		// DefaultMutableTreeNode newRoot = new DefaultMutableTreeNode("");//
		// newRoot.add(unitInfoNode);
		// newRoot.add(root);
		// treeModel.setRoot(newRoot);

	}

	protected void doTreeValueChanged() {
		TreePath path = getLeftTree().getSelectionPath();
		if (path == null)
			return;
		DefaultMutableTreeNode node = getTreeNode(path);
		boolean isNowReport = (isSampleEnabled());
		if (node.getUserObject() instanceof String) {// 选择单位信息节点后，更新右边的列表显示，并且控制表样形式不可选
			if (isNowReport) {// 从报表切换到单位信息
				if (!isCurrShowList())
					setCurrSelList();// 先修改成列表显示
				setIsSampleEnabled(false);// 控制表样形式不可选
				changeUnitInfoPanel(true);
			}
		} else {
			if (!isNowReport) {// 从单位信息切换到报表
				setIsSampleEnabled(true);
				changeUnitInfoPanel(false);
			}
			super.doTreeValueChanged();
		}
	}

	private void changeUnitInfoPanel(boolean bUnit) {
		if (bUnit) {
			setTabComponent(0, getJScrollPaneUnit(), StringResource.getStringResource(TITLE_UNITINFO));
		} else {
			setTabComponent(0, getRightPanelList(), StringResource.getStringResource(TITLE_LIST));
		}
	}

	private UnitExInfoPanel getUnitInfoPanel() {
		if (m_unitPanel == null) {
			m_unitPanel = new UnitExInfoPanel(){

				@Override
				protected void initSelectedAction(ChangeEvent e) {
					TableCellEditor editor = getCheckBoxEditor();
			        if (editor != null) {
			            Object value = editor.getCellEditorValue();
			            if(value instanceof Boolean){
			            	if(((Boolean) value).booleanValue()){
			            	     addObjToSelList(getSelectedVO());
			            	}else{
			            		getMeasureListModel().removeElement(getSelectedVO());
			            	}
			            }
			        }
				
				}
				
			};

			// 得到所有的自定义属性
			try {
				IExPropOperator exPropOper = ExPropOperator.getExPropOper(IExPropConstants.EXPROP_MODULE_UNIT);
				ExPropertyVO[] vos = exPropOper.loadAllExProp("");
				if (vos != null && vos.length > 0) {
					ArrayList<UnitExInfoVO> al_unit = new ArrayList<UnitExInfoVO>();

					for (int i = 0; i < vos.length; i++) {
						if (vos[i].getUsedTag() == ExPropertyVO.USED_TAG_START&&!"level_code".equals(vos[i].getDBColumnName()))
							al_unit.add(new UnitExInfoVO(vos[i]));
					}
					if (al_unit.size() > 0) {
						m_unitPanel.setUnitInfo(al_unit.toArray(new UnitExInfoVO[0]));
					}
				}
				// List<ExPropertyVO> list = ExPropListAction.getUserExPropList(
				// IExPropConstants.EXPROP_MODULE_UNIT, m_userPk,
				// UnitPropVO.BASEORGPK);
				// if (list.size() > 0) {
				// UnitExInfoVO[] unitVOs = new UnitExInfoVO[list.size()];
				// for (int i = 0; i < list.size(); i++) {
				// unitVOs[i] = new UnitExInfoVO(list.get(i));
				// }
				// m_unitPanel.setUnitInfo(unitVOs);
				// }
			} catch (Exception ex) {
				AppDebug.debug(ex);
			}
		}
		return m_unitPanel;
	}

	private Object[] getSelVOsFromList(boolean isMeasure) {
		DefaultListModel model = (DefaultListModel) getMeasureList().getModel();
		int size = model.getSize();
		if (size == 0)
			return null;
		if (isMeasure) {
			ArrayList<MeasureVO> al_obj = new ArrayList<MeasureVO>();
			for (int i = 0; i < size; i++) {
				Object obj = model.get(i);
				if (obj instanceof MeasureVO)
					al_obj.add((MeasureVO) obj);
			}
			return al_obj.toArray(new MeasureVO[0]);
		} else {
			ArrayList<UnitExInfoVO> al_obj = new ArrayList<UnitExInfoVO>();
			for (int i = 0; i < size; i++) {
				Object obj = model.get(i);
				if (obj instanceof UnitExInfoVO)
					al_obj.add((UnitExInfoVO) obj);
			}
			return al_obj.toArray(new UnitExInfoVO[0]);
		}
	}

	public void setSelUnitexVOs(UnitExInfoVO[] vos) {
		if (vos != null) {
			for (UnitExInfoVO vo : vos)
				addObjToSelList(vo);
		}
		getUnitInfoPanel().setSelUnitInfo(vos);
	}

	private void addObjToSelList(Object obj) {

		if (getMeasureList().getObjIndex(obj) < 0)
			getMeasureListModel().addElement(obj);
	}
}
