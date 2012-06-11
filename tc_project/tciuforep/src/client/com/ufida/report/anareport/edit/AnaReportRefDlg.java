package com.ufida.report.anareport.edit;

import java.awt.Container;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import nc.ui.bi.report.manager.ReportBO_Client;
import nc.util.iufo.resmng.ResMngToolKit;
import nc.vo.bi.report.manager.ReportDirVO;
import nc.vo.bi.report.manager.ReportSrv;
import nc.vo.bi.report.manager.ReportVO;
import nc.vo.iufo.resmng.ResMngDirVO;
import nc.vo.iufo.resmng.uitemplate.IResTreeObject;
import nc.vo.pub.ValueObject;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufida.report.free.applet.ReportDirDlg;
import com.ufsoft.report.dialog.UfoDialog;

public class AnaReportRefDlg extends ReportDirDlg {

	private static final long serialVersionUID = 1L;
	HashMap<String, ArrayList<ReportVO>> m_map = null;

	public AnaReportRefDlg(Container parent, String strUserPK, String strUnitPK) {
		super(parent, strUserPK, strUnitPK);
	}

	public ReportDirVO getReportDirVO() {
		return null;
	}

	private void doLoadReportVOs() {
		m_map = new HashMap<String, ArrayList<ReportVO>>();
		try {
			ValueObject[] vos = ReportBO_Client.queryNotBlob("(type = 5 or type = 6)", null);
			if (vos == null || vos.length == 0)
				return;
			for (int i = 0; i < vos.length; i++) {
				if (!(vos[i] instanceof ReportVO))
					continue;
				String dirPK = ((ReportVO) vos[i]).getPk_folderID();
				if (!m_map.containsKey(dirPK))
					m_map.put(dirPK, new ArrayList<ReportVO>());
				ArrayList<ReportVO> list = m_map.get(dirPK);
				list.add(((ReportVO) vos[i]));
			}
		} catch (Exception ex) {
			AppDebug.error(ex);
		}
	}

	protected void processNode(DefaultMutableTreeNode node) {
		if (m_map == null)// 第一次加载ReportVO
			doLoadReportVOs();

		Object selNode = node.getUserObject();
		if (selNode == null)
			return;
		Object srcVO = null;
		if (selNode instanceof IResTreeObject) {
			String resID = ResMngToolKit.getVOIDByTreeObjectID(((IResTreeObject) selNode).getID());
			if (resID == null)
				return;
			srcVO = ((IResTreeObject) selNode).getSrcVO();
		} else
			srcVO = selNode;

		String dirPK = null;
		if (srcVO instanceof ResMngDirVO)
			dirPK = ((ResMngDirVO) srcVO).getDirPK();
		else if (srcVO instanceof ReportDirVO)
			dirPK = ((ReportDirVO) srcVO).getDirID();
		if (dirPK == null)
			return;
		if (m_map.containsKey(dirPK)) {
			ArrayList<ReportVO> list = m_map.get(dirPK);
			for (ReportVO vo : list) {
				DefaultMutableTreeNode subNode = new DefaultMutableTreeNode(vo);
				node.add(subNode);
			}
		}
	}

	ReportVO getReportVO() {
		TreePath path = getReportDirTree().getSelectionPath();
		if (path == null)
			return null;
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
		if (node == null)
			return null;
		Object selNode = node.getUserObject();
		if (selNode instanceof ReportVO) {
			return (ReportVO) selNode;
		}
		return null;
	}

	public Object getValidateValue(String text) {
		ReportSrv srv = new ReportSrv();
		ValueObject[] vos = srv.getByIDs(new String[] { text });
		if (vos != null && vos.length > 0)
			return vos[0];
		return null;
	}

	public void setDefaultValue(Object obj) {
		DefaultMutableTreeNode repNode = null;
		if (obj instanceof ReportVO) {
			repNode = new DefaultMutableTreeNode(obj);
			TreePath repPath = new TreePath(((DefaultTreeModel) getReportDirTree().getModel()).getPathToRoot(repNode));
			getReportDirTree().setSelectionPath(repPath);
			getReportDirTree().fireTreeExpanded(repPath);
		}
	}

	protected void processMouseClick(MouseEvent e) {
		if (getReportDirTree().getSelectionPath() == null)
			return;
		DefaultMutableTreeNode node = (DefaultMutableTreeNode) getReportDirTree().getSelectionPath()
				.getLastPathComponent();
		if (node == null)
			return;
		Object selNode = node.getUserObject();
		if (selNode instanceof ReportVO) {
			if (e.getClickCount() >= 2) {
				closeDlgWithResult(true);
			}
			return;
		}
	}

	protected void doCloseOK() {
		setResult(UfoDialog.ID_OK);
		close();
	}
	protected void doCloseCancel() {
		setResult(UfoDialog.ID_CANCEL);
		close();
	}
	protected void closeDlgWithResult(boolean isOK) {
		if(isOK)
			doCloseOK();
		else
			doCloseCancel();
	}
	
}
