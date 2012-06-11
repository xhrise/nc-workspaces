package nc.ui.iufo.query.datasetmanager;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

/**
 * 字符串的拖拽数据传输类
 * @author yaoza
 *
 */
public abstract class StringTransferHandler extends TransferHandler {

	protected abstract String exportStirng(JComponent control);
	protected abstract void importString(JComponent control,String str);
	protected abstract void cleanup(JComponent control,boolean remove);
	
	@Override
	protected Transferable createTransferable(JComponent control)
	{
		return new StringSelection(exportStirng(control));
	}
	
	@Override
	public int getSourceActions(JComponent control)
	{
		return COPY_OR_MOVE;
	}
	
	@Override
	public boolean canImport(JComponent c, DataFlavor[] flavors)
	{
		for(int i=0;i<flavors.length;i++)
		{
			if(DataFlavor.stringFlavor.equals(flavors[i]))
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	protected void exportDone(JComponent source, Transferable data, int action)
	{
		cleanup(source, action == MOVE);
	}
	
	@Override
	public boolean importData(JComponent c, Transferable t)
	{
		if (canImport(c, t.getTransferDataFlavors())) {
			try {
				String str = (String) t
						.getTransferData(DataFlavor.stringFlavor);
				importString(c, str);
				return true;
			} catch (UnsupportedFlavorException ufe) {
			} catch (IOException ioe) {
			}
		}
		return false;
	}
}
