/*
 * 创建日期 2005-6-22
 *
 */
package nc.ui.bi.query.designer;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import nc.vo.iuforeport.businessquery.SelectFldVO;

/**
 * @author zjb
 * 
 * 查询字段拖放载体
 */
public class SelectFldTransferable implements Transferable {

	private SelectFldVO[] m_sfs = null;

	/**
	 * 构造子
	 */
	public SelectFldTransferable(SelectFldVO[] sfs) {
		m_sfs = sfs;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
	 */
	public DataFlavor[] getTransferDataFlavors() {
		return null;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.datatransfer.DataFlavor)
	 */
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return false;
	}

	/*
	 * （非 Javadoc）
	 * 
	 * @see java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer.DataFlavor)
	 */
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		return m_sfs;
	}

}
