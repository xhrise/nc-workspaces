package com.ufsoft.report;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JPanel;
import javax.swing.plaf.PanelUI;

import com.ufsoft.report.component.RepToolBarPanelUI;

public class ReportToolBarPanel extends JPanel {
	private final static RepToolBarPanelUI instance = new RepToolBarPanelUI();
	
	public ReportToolBarPanel() {
		super(new ReportToolBarPanelLayout());
	}


	@Override
	public void setUI(PanelUI ui) {
		super.setUI(instance);
	}
    
	public static class ReportToolBarPanelLayout extends FlowLayout{
		private static final long serialVersionUID = 55026997145256226L;
		
		public ReportToolBarPanelLayout() {
			super(FlowLayout.LEFT,0,0);
		}
		public Dimension preferredLayoutSize(Container target) {
			Dimension containerSize = target.getSize();
			int containerWidth = containerSize.width;				
			int totalHeight = 0;
			int nmembers = target.getComponentCount();				
			int curRowWidth = 0;
			int curRowHeight = 0;
			int tmpRowWidth=0;
			int tmpRowHeight=0;
			for(int i=0;i<nmembers;i++){
				tmpRowWidth=target.getComponent(i).getPreferredSize().width;
				tmpRowHeight= target.getComponent(i).getPreferredSize().height;
				if(i == 0){
					curRowHeight =tmpRowHeight;
					curRowWidth = tmpRowWidth;
				}else{
					if(tmpRowWidth + curRowWidth > containerWidth){
						totalHeight += curRowHeight;
						//ÐÂÐÐ
						curRowHeight =tmpRowHeight;
						curRowWidth = tmpRowWidth;
					}else{
						curRowHeight = Math.max(curRowHeight,tmpRowHeight);
						curRowWidth += tmpRowWidth;
					}
				}
				if(i==nmembers-1){
					totalHeight += curRowHeight;
				}
			}
			return new Dimension(target.getSize().width,totalHeight);
		}
	}
}
