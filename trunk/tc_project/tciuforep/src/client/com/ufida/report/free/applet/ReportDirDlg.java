package com.ufida.report.free.applet;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;

import nc.ui.iufo.cache.IUFOUICacheManager;
import nc.ui.iufo.resmng.common.AuthUIBizHelper;
import nc.ui.iufo.resmng.common.UISrvException;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITree;
import nc.us.bi.report.manager.BIFreeQueryReportSrv;
import nc.util.bi.resmng.IBIResMngConstants;
import nc.util.iufo.pub.UFOString;
import nc.util.iufo.resmng.IResMngConsants;
import nc.util.iufo.resmng.ResMngBizHelper;
import nc.util.iufo.resmng.ResMngToolKit;
import nc.util.iufo.resmng.loader.IModuleLoaderParam;
import nc.util.iufo.resmng.loader.ITreeLoader;
import nc.util.iufo.resmng.loader.LoaderParam;
import nc.vo.bi.report.manager.ReportDirVO;
import nc.vo.iufo.authorization.IAuthorizeTypes;
import nc.vo.iufo.resmng.uitemplate.IResTreeObject;
import nc.vo.iufo.resmng.uitemplate.ResOperException;
import nc.vo.iufo.resmng.uitemplate.ResTreeObject;
import nc.vo.iufo.user.UserInfoVO;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.iufo.resource.StringResource;
import com.ufsoft.report.dialog.UfoDialog;

public class ReportDirDlg extends UfoDialog {
	private static final long serialVersionUID = 1L;

	private String m_strUserPK = null;
	private String m_strUnitPK = null;

	private JScrollPane TreeScrollPane = null;

	private JTree reportDirTree = null;

	private JPanel jContentPane = null;

	private JButton OKBtn = null;

	private JButton cancelBtn = null;

	public ReportDirDlg(Container parent, String strUserPK, String strUnitPK) {
		super(parent);
		m_strUserPK = strUserPK;
		m_strUnitPK = strUnitPK;
		initialize();
	}

	/**
	 * @i18n mbicomplex00019=报表目录
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setTitle(StringResource.getStringResource("mbicomplex00019"));
		this.setContentPane(getJContentPane());
	}

	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new UIPanel();
			jContentPane.setLayout(new BorderLayout());
			jContentPane.add(getTreeScrollPane(), BorderLayout.CENTER);
			JPanel panel = new UIPanel();
			panel.add(getOKBtn());
			panel.add(getCancelBtn());
			jContentPane.add(panel, BorderLayout.SOUTH);
		}
		return jContentPane;
	}

	private JScrollPane getTreeScrollPane() {
		if (TreeScrollPane == null) {
			TreeScrollPane = new UIScrollPane();
			TreeScrollPane.setViewportView(getReportDirTree());
		}
		return TreeScrollPane;
	}

	/**
	 * This method initializes queryDirTree
	 * 
	 * @return javax.swing.JTree
	 */
	protected JTree getReportDirTree() {
		if (reportDirTree == null) {
			reportDirTree = new UITree();
			reportDirTree.setModel(new DefaultTreeModel(createTreeNode(m_strUnitPK, m_strUserPK)));
			initTreeSelectionModel();
			reportDirTree.addMouseListener(new MouseAdapter() {
				public void mouseClicked(MouseEvent e) {
					processMouseClick(e);
				}
			});

		}
		return reportDirTree;
	}
	protected void processMouseClick(MouseEvent e){
		if (e.getClickCount() >= 2) {
			closeDlgWithResult(true);
		}
	}
	@SuppressWarnings("unchecked")
	public DefaultMutableTreeNode createTreeNode(String unitPK, String userPK) {
		IResTreeObject rootUserObj = createVitualRootTreeObj(userPK);
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootUserObj);
		IResTreeObject[] tos = null;
		try {
			String strModuleID = IBIResMngConstants.MODULE_FREEQUERY;
			ITreeLoader treeLoader = ResMngBizHelper.getTreeLoader(strModuleID);
	   		boolean bIncludeFile = false;
    		IModuleLoaderParam moduleLoaderParam = null;
    		UserInfoVO loginUserInfoVO = IUFOUICacheManager.getSingleton().getUserCache().getUserById(userPK);
    		boolean bHaveResMngRight  = AuthUIBizHelper.haveResMngRight(strModuleID, loginUserInfoVO);
    		// @edit by wangyga at 2009-1-6,下午03:01:57 权限类型的过滤：最小权限应该为"完全控制"
    		int nNeedAuType =  IAuthorizeTypes.AU_TYPE_MODIFY;
            String strLoaderResOwnerPK = unitPK;
    		LoaderParam loaderParam = new LoaderParam(
    				strLoaderResOwnerPK, bHaveResMngRight,nNeedAuType,false,true,true,strLoaderResOwnerPK);
    		
			tos = treeLoader.loadResTreeObjs(userPK,
					bIncludeFile, loaderParam,moduleLoaderParam);
			tos = getOrderData(tos);
		} catch (ResOperException e) {
			AppDebug.debug(e);// @devTools AppDebug.debug(e);
		}
		if (tos == null || tos.length == 0)
			return root;

		Hashtable map = new Hashtable();
		DefaultMutableTreeNode node = null;
		DefaultMutableTreeNode parentNode = null;

		for (int i = 0; i < tos.length; i++) {
			node = new DefaultMutableTreeNode(tos[i]);
			map.put(tos[i].getID(), node);
		}
		map.put(rootUserObj.getID(), root);

		for (int i = 0; i < tos.length; i++) {
			node = (DefaultMutableTreeNode) map.get(tos[i].getID());
			parentNode = (DefaultMutableTreeNode) map.get(tos[i].getParentID());
			if ( parentNode == null || rootUserObj.getID().equalsIgnoreCase(tos[i].getParentID()) ) {
				if(!tos[i].getID().equals(rootUserObj.getID()))
				root.add(node);
			} else {
				parentNode.add(node);
			}
			processNode(node);
		}
		return root;
	}
	private IResTreeObject[] getOrderData(IResTreeObject[] fileResTreeObjs){
		if(fileResTreeObjs == null || fileResTreeObjs.length == 0)
			return null;
		Arrays.sort(fileResTreeObjs, new Comparator<IResTreeObject>() {
			public int compare(IResTreeObject o1, IResTreeObject o2) {
				if(o1 == null)
					return 1;
				if(o2 == null)
					return -1;
				return UFOString.compareHZString(o1.getLabel(), o2.getLabel());
			}
		});
		return fileResTreeObjs;
	}
	protected void processNode(DefaultMutableTreeNode node){
		
	}

	/**
	 * 得到虚根目录
	 * 
	 * @param strResOwnerPK
	 * @return
	 * @throws ResOperException
	 */
	private static IResTreeObject createVitualRootTreeObj(String userPK) {
		IResTreeObject resTreeObject = new ResTreeObject();
		int nObjectType = IResTreeObject.OBJECT_TYPE_DIR;
		String strTreeObjID = ResMngToolKit.getHashCodeID(IResMngConsants.VIRTUAL_ROOT_ID, nObjectType);
		resTreeObject.setID(strTreeObjID);
		resTreeObject.setParentID("");
		String strRootDirName = "";
		try {
			strRootDirName = BIFreeQueryReportSrv.getInstance().getRootDirDisName(userPK, true);
		} catch (UISrvException e) {
			AppDebug.debug(e);// @devTools AppDebug.debug(e);
		}
		resTreeObject.setLabel(strRootDirName);
		resTreeObject.setType(nObjectType);
		resTreeObject.setSrcVO(null);// 虚根无实际VO
		resTreeObject.setNote(strRootDirName);
		return resTreeObject;
	}

	protected void initTreeSelectionModel() {
		TreeSelectionModel model = getReportDirTree().getSelectionModel();
		model.setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	}

	public ReportDirVO getReportDirVO() {
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) getReportDirTree().getSelectionPath()
				.getLastPathComponent();
		if (node == null)
			return null;
		IResTreeObject obj = (IResTreeObject) node.getUserObject();
		String dirID = (obj == null) ? null : ResMngToolKit.getVOIDByTreeObjectID(obj.getID());
		if (dirID == null)// TODO 根目录不允许保存报表
			return null;
		ReportDirVO dirVO = new ReportDirVO();
		dirVO.setDirID(dirID);
		dirVO.setDirName(obj.getName());

		return dirVO;

	}

	private JButton getOKBtn() {
		if (OKBtn == null) {
			OKBtn = new nc.ui.pub.beans.UIButton();
			OKBtn.setText(StringResource.getStringResource("mbiadhoc00021"));
			OKBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					closeDlgWithResult(true);
				}
			});
		}
		return OKBtn;
	}

	protected void closeDlgWithResult(boolean isOK) {
		if (isOK) {
			/**
			 * @i18n uibiadhoc00008=您还没有选择查询对象
			 * @i18n uibiadhoc00009=选择查询对象错误
			 */
			if (getReportDirVO() != null) {
				setResult(UfoDialog.ID_OK);
				close();
			} else {
				JOptionPane.showMessageDialog(ReportDirDlg.this, StringResource.getStringResource("uibiadhoc00008"),
						StringResource.getStringResource("uibiadhoc00009"), JOptionPane.ERROR_MESSAGE);
			}
		} else
			setResult(UfoDialog.ID_CANCEL);
		close();
	}

	/**
	 * This method initializes jButton1
	 * 
	 * @return javax.swing.JButton
	 */
	private JButton getCancelBtn() {
		if (cancelBtn == null) {
			cancelBtn = new nc.ui.pub.beans.UIButton();
			cancelBtn.setText(StringResource.getStringResource("mbiadhoc00022"));
			cancelBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					closeDlgWithResult(false);
				}
			});
		}
		return cancelBtn;
	}
}
 