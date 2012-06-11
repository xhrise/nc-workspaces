/*
 * �������� 2005-6-22
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
 * ��ѯ�ֶ��Ϸ�����
 */
public class SelectFldTransferable implements Transferable {

	private SelectFldVO[] m_sfs = null;

	/**
	 * ������
	 */
	public SelectFldTransferable(SelectFldVO[] sfs) {
		m_sfs = sfs;
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
	 */
	public DataFlavor[] getTransferDataFlavors() {
		return null;
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.datatransfer.DataFlavor)
	 */
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return false;
	}

	/*
	 * ���� Javadoc��
	 * 
	 * @see java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer.DataFlavor)
	 */
	public Object getTransferData(DataFlavor flavor)
			throws UnsupportedFlavorException, IOException {
		return m_sfs;
	}

}
