package com.ufsoft.report.sysplugin.dnd;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;


/**
 * 
 * @author zzl 2005-4-19
 */
public class DndObjectTransferable implements Transferable{
    public final static DataFlavor DND_OBJ_FLAVOR = new DataFlavor(Object.class,"DndObject");
    
    private Object m_obj = null;
    
    public DndObjectTransferable(Object obj){
        m_obj = obj;
    }
    /*
     * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
     */
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{DND_OBJ_FLAVOR};
    }

    /*
     * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.datatransfer.DataFlavor)
     */
    public boolean isDataFlavorSupported(DataFlavor flavor) {
		if (flavor == null) {
			return false;
		}
		DataFlavor[] flavors = getTransferDataFlavors();
		if (flavors != null) {
			for (int i = 0; i < flavors.length; i++) {
				if (flavors[i].equals(flavor)) {
					return true;
				}
			}
		}
		return false;
    }

    /*
     * @see java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer.DataFlavor)
     */
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if(flavor.equals(DND_OBJ_FLAVOR)){
            return m_obj;
        }else{
            return null;
        }       
    }    
}
