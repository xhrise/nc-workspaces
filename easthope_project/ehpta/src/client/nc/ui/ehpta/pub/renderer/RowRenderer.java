package nc.ui.ehpta.pub.renderer;

import java.awt.Color;
import java.awt.Component;

import javax.swing.table.DefaultTableCellRenderer;

import nc.bs.logging.Logger;
import nc.ui.trade.buffer.BillUIBuffer;
import nc.vo.pub.AggregatedValueObject;
import nc.vo.trade.pub.IBillStatus;

public class RowRenderer extends DefaultTableCellRenderer{
	
	private static final long serialVersionUID = -5225680391528363280L;

	private BillUIBuffer buffer = null;
	
	public RowRenderer(){
		super();
	}
	
	public RowRenderer(BillUIBuffer _buffer){
		super();
		buffer = _buffer;
	}
	
	public Color colora = new Color(236, 244, 244);
	
	public Color colorb = Color.white;
	
	public Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row ,int column){
		try{
			AggregatedValueObject aggVO = buffer.getVOByRowNo(row);
			if(Integer.valueOf(aggVO.getParentVO().getAttributeValue("vbillstatus").toString()) == IBillStatus.CHECKPASS){
				setBackground(colora);
			}else{
				setBackground(colorb);
			}
		}catch(Exception e){
			Logger.debug(e);
		}
			
		return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	}

}
