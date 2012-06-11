package com.ufsoft.report.sysplugin.postil;

import java.awt.Color;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.plaf.basic.BasicInternalFrameUI;

import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITextArea;

import com.ufsoft.report.util.StringDocument;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsModel;
import com.ufsoft.table.CellsPane;

/**
 * @author zzl Create on 2005-3-7
 */
public class PostilInternalFrame extends JInternalFrame {
	private static final long serialVersionUID = -6947199138955792445L;

	private JTextArea textArea;

	private CellsPane frameParent;

	private CellPosition cellPos;

	@SuppressWarnings("serial")
	public PostilInternalFrame(boolean bEditable) {
		super();
		setUI(new PostilInternalFrameUI(this));
		textArea = new UITextArea() {
			protected boolean processKeyBinding(KeyStroke ks, KeyEvent e,
					int condition, boolean pressed) {
				super.processKeyBinding(ks, e, condition, pressed);
				return true;
			}
		};
		// @edit by zhaopq at 2008-12-31,下午03:51:05
		textArea.setDocument(new StringDocument(2000));

		textArea.setLineWrap(true);
		setEditable(bEditable);
		JScrollPane scroller = new UIScrollPane(textArea);
		getContentPane().add(scroller);
		init();
	}

	private void init() {
		setSize(100, 80);
		textArea.setBackground(Color.YELLOW);
		textArea.setForeground(Color.RED); 
	}

	public void setContent(String content) {
		textArea.setText(content);
	}

	public String getContent() {
		return textArea.getText();
	}

	public void setLocation(CellsPane cellsPane, CellPosition cellPos,PostilVO pvo) {
		Rectangle rect = cellsPane.getCellRect(cellPos, true);
		int x = 0;
		int y = 0;
		if(cellPos.getColumn()==cellsPane.getDataModel().getColNum()-1){
			if(cellPos.getRow()==0){
				//单元格在右上角，此时批注显示在左边
				x = rect.x-pvo.getSize().width<0 ? 0 : rect.x-pvo.getSize().width;
				y = rect.y-pvo.getSize().height<0 ? 0 : rect.y-pvo.getSize().height;
			}else{
				//单元格显示在上边
				x = rect.x+rect.width-pvo.getSize().width;
				y = rect.y-pvo.getSize().height<0 ? 0 : rect.y-pvo.getSize().height;
			}
		}
		else if(cellPos.getRow()==cellsPane.getDataModel().getRowNum()-1){////单元格显示在上边
			x = rect.x+rect.width-pvo.getSize().width;
			y = rect.y-pvo.getSize().height<0 ? 0 : rect.y-pvo.getSize().height;
			
		}else{
			x = rect.x + rect.width;
			y = rect.y;
		}
		setLocation(new Point(x,y));
	}
	
	public void setLocation(CellsPane cellsPane, CellPosition cellPos) {
        Rectangle rect = cellsPane.getCellRect(cellPos, true);
        Point location = new Point(rect.x + rect.width, rect.y);
        setLocation(location);        
    }

	private void setEditable(boolean isEditable) {
		setResizable(isEditable);
		textArea.setEditable(isEditable);
	}

	public JTextArea getTextArea() {
		return textArea;
	}

	public Insets getInsets() {
		return new Insets(1, 1, 1, 1);
	}

	public CellPosition getCellPos() {
		return cellPos;
	}

	public void setCellPos(CellPosition cellPos) {
		this.cellPos = cellPos;
	}

	public CellsPane getFrameParent() {
		return frameParent;
	}

	public void setFrameParent(CellsPane frameParent) {
		this.frameParent = frameParent;
	}
}

class PostilInternalFrameUI extends BasicInternalFrameUI {

	public PostilInternalFrameUI(JInternalFrame b) {
		super(b);
	}

	public void setNorthPane(JComponent c) {
		// 不绘制标题栏
	}
}