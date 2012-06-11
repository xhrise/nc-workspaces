package com.ufida.report.rep.applet;

import java.awt.Component;
import java.util.Hashtable;

import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableCellEditor;


import com.ufida.report.adhoc.model.AdhocPageDimField;
import com.ufsoft.iufo.data.IMetaData;

public class PageDimRelativeCellEditor extends AbstractCellEditor implements TableCellEditor,TableModelListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private AdhocPageDimField[] pageDims;
	private int relativeColumn;
	/** Integer:relativeComboDataµÄÐÐºÅ*/
	protected Hashtable<Integer,PageDimRefTextField> refs=new Hashtable<Integer,PageDimRefTextField>();
	protected JComponent editorComponent;
	private PageDimRefTextField delegate;
	private JTextField defaultTextField=new JTextField();
	
	public PageDimRelativeCellEditor(int relaColumn,AdhocPageDimField[] pageDims){
		this.relativeColumn=relaColumn;
		this.pageDims=pageDims;
	}

	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected, int row, int column) {
		    Object relativeValue=table.getModel().getValueAt(row, relativeColumn);
		   
		    if(relativeValue instanceof IMetaData){
				delegate=getDelegate(row,(IMetaData)relativeValue);
		    }
			editorComponent=delegate;
            if(editorComponent==null){
            	editorComponent=defaultTextField;
            }
		return delegate;
	}
    
	private PageDimRefTextField getDelegate(int row,IMetaData key){
		PageDimRefTextField ref=null;
		if(refs.get(row)==null||refs.get(row).getPageDimField().getField()!=key){
			AdhocPageDimField pageDim=null;
			for(int i=0;i<pageDims.length;i++){
				if(pageDims[i].getField()==key){
					pageDim=pageDims[i];
					break;
				}
			}
			ref=new PageDimRefTextField(pageDim);
			refs.put(row, ref);
		}
		if(ref==null){
			ref=refs.get(row);
		}
		
		return ref;
	}
	
	public Object getCellEditorValue() {
		return delegate.getText();
	}
   
	public void removeRowEditor(int row){
		
		int allRow=refs.size();
		refs.remove(row);
		row=row+1;
		PageDimRefTextField ref=null;
		for(int i=row;i<allRow;i++){
			ref=refs.get(i);
			refs.remove(i);
			refs.put(i-1, ref);
		}
	}

	public void tableChanged(TableModelEvent e) {
		if(e.getType()==TableModelEvent.DELETE){
			int firstRow=e.getFirstRow();
			int lastRow=e.getLastRow();
			for(int i=firstRow;i<=lastRow;i++){
				removeRowEditor(i);
			}
		}
		
	}
}
