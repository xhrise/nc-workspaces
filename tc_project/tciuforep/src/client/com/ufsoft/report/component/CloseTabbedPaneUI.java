package com.ufsoft.report.component;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;

import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.text.StyleContext;
import javax.swing.text.View;

import com.ufsoft.report.lookandfeel.plaf.Office2003Utilities;

/**
 * 主要为了保证关闭按钮在标签的右部
 * @author guogang
 *
 */
public class CloseTabbedPaneUI extends BasicTabbedPaneUI{
	private boolean oldFocusable;

	private static final int TAB_HEIGHT		= 16;

	private static final int INSETS_LEFT		= 3+8;
	private static final int INSETS_RIGHT		= 3;
    
	private boolean hasTabTitle=true;
    
	public CloseTabbedPaneUI() {
		this(true);
	}
	/**
	 * 
	 * @param hasTitle 是否显示title
	 */
    public CloseTabbedPaneUI(boolean hasTitle){
    	super();
		hasTabTitle=hasTitle;
    }

	protected int calculateTabHeight(int tabPlacement, int tabIndex,
								int fontHeight) {
		if(hasTabTitle){
		   return TAB_HEIGHT;
		}else{
		   return 0;
		}
	} 


	protected int calculateTabWidth(int tabPlacement, int tabIndex,
								FontMetrics metrics) {
		// Ensure we use the font metrics for the bold font used for the
		// selected tab; this ensures that tabs don't "grow" when they are
		// selected to accomodate the bold font.
		if(hasTabTitle){
		metrics = tabPane.getFontMetrics(getBoldFont(tabPane.getFont()));
		int width = super.calculateTabWidth(tabPlacement, tabIndex, metrics);
		return width;
		}else{
			return 0;
		}
	}
	protected int calculateMaxTabHeight(int tabPlacement) {
		if(hasTabTitle){
		return super.calculateMaxTabHeight(tabPlacement);
		}else{
			return 0;
		}
	}

	protected int calculateMaxTabWidth(int tabPlacement) {
		if(hasTabTitle){
		return super.calculateMaxTabWidth(tabPlacement);
		}else{
			return 0;
		}
	}

	protected int calculateTabAreaHeight(int tabPlacement, int horizRunCount, int maxTabHeight) {
		if(hasTabTitle){
		    return super.calculateTabAreaHeight(tabPlacement, horizRunCount, maxTabHeight);
		}else{
			return 0;
		}
	}

	protected int calculateTabAreaWidth(int tabPlacement, int vertRunCount, int maxTabWidth) {
		if(hasTabTitle){
		return super.calculateTabAreaWidth(tabPlacement, vertRunCount, maxTabWidth);
		}else{
			return 0;
		}
	}

	/**
	 * Returns the UI to use for the specified tabbed pane.
	 *
	 * @param c The tabbed pane.
	 * @return The UI.
	 */
	public static ComponentUI createUI(JComponent c){
		return new CloseTabbedPaneUI();
	}


	/**
	 * WORKAROUND for Sun JRE bug 6282887: calling Font.deriveFont(style) to
	 * get a bold/italic Japanese (Asian?) font instead returns a font that
	 * prints squares for all (non-ASCII) characters.  This is fixed in
	 * 1.5.0-b45, but since we run on 1.4+, we'll keep this workaround.
	 *
	 * @param font The non-bold font.
	 * @return A bold version of the font.
	 */
	protected static Font getBoldFont(Font font) {
		//return font.deriveFont(Font.BOLD);
		StyleContext sc = StyleContext.getDefaultStyleContext();
		return sc.getFont(font.getFamily(), Font.BOLD, font.getSize());
	}


	/**
	 * This method is overridden because, as of 1.5, the layout manager used by
	 * <code>BasicTabbedPaneUI</code> for the wrapped tab layout uses a "fast"
	 * method of calculating the minimum width needed for the tabbed pane,
	 * which is evidently incompatible with this UI (probably since we
	 * change the font to be bold when calculating tab widths).  So we have to
	 * do this ourselves.
	 *
	 * @param c The tabbed pane.
	 * @return The minimum size of the tabbed pane.
	 */
	public Dimension getMinimumSize(JComponent c) {
		Dimension d = super.getMinimumSize(c);
		if (d==null && tabPane.
				getTabLayoutPolicy()==JTabbedPane.WRAP_TAB_LAYOUT)
		{
			int tabPlacement = tabPane.getTabPlacement();
			d = new Dimension(calculateMaxTabWidth(tabPlacement),
						   calculateMaxTabHeight(tabPlacement));
			d.width += tabAreaInsets.left + tabAreaInsets.right;
		}
		return d;
	}


	protected int getTabLabelShiftX(int tabPlacement, int tabIndex,
								boolean isSelected) {
		switch (tabPlacement) {
			case SwingConstants.TOP:
			case SwingConstants.BOTTOM:
				return 0;
			default: // LEFT or RIGHT
				return 8; // Just to get out of the curved part.
		}
	}


	protected int getTabLabelShiftY(int tabPlacement, int tabIndex,
								boolean isSelected) {
		// For some reason we need to shift the text up when the tabs are
		// at the bottom.
		return tabPlacement==SwingConstants.BOTTOM ? -1 : 0;
	}


	/**
	 * Overridden to ensure the selected tab does not get "padded" to appear
	 * larger.
	 */
	protected void installDefaults() {
		super.installDefaults();
		contentBorderInsets = new Insets(1,1,1,1);
		selectedTabPadInsets = new Insets(0,0,0,0);
		tabAreaInsets = new Insets(2,INSETS_LEFT,0,INSETS_RIGHT);
		oldFocusable = tabPane.isFocusable();
		tabPane.setFocusable(false);
	}
	protected void layoutLabel(int tabPlacement, FontMetrics metrics,
			int tabIndex, String title, Icon icon, Rectangle tabRect,
			Rectangle iconRect, Rectangle textRect, boolean isSelected) {
		textRect.x = textRect.y = iconRect.x = iconRect.y = 0;

		View v = getTextViewForTab(tabIndex);
		if (v != null) {
			tabPane.putClientProperty("html", v);
		}
		//修改了默认的水平方向的horizontalTextPosition对齐方式为SwingUtilities.LEADING,标签的标题在关闭图标的左边
		
		SwingUtilities.layoutCompoundLabel((JComponent) tabPane, metrics, title, icon,
				SwingUtilities.CENTER, SwingUtilities.CENTER,
				SwingUtilities.CENTER, SwingUtilities.LEADING, tabRect,
				iconRect, textRect, textIconGap);

		tabPane.putClientProperty("html", null);

		int xNudge = getTabLabelShiftX(tabPlacement, tabIndex, isSelected);
		int yNudge = getTabLabelShiftY(tabPlacement, tabIndex, isSelected);
		iconRect.x += xNudge;
		iconRect.y += yNudge;
		textRect.x += xNudge;
		textRect.y += yNudge;
	}
	/**
	 * Paints the bottom part of the border surrounding the content.
	 */
	protected void paintContentBorderBottomEdge(Graphics g, int tabPlacement,
						int selectedIndex, int x, int y, int w, int h) {
		g.setColor(Office2003Utilities.getColor("report.TabBorderColor"));
		y += h - 1;
		if (tabPlacement!=SwingConstants.BOTTOM || selectedIndex==-1) {
			g.drawLine(x+1,y, x+w-1,y);
		}
		else {
			// The selected tab is always in the "bottommost" run, so we
			// don't need to check what run the tab is in.
			Rectangle tabBounds = new Rectangle();
			tabBounds = getTabBounds(selectedIndex, tabBounds);
			// "Offset" the bounds since our tabs "overlap."
			tabBounds.x -= 9;
			tabBounds.width += 8;
			g.drawLine(x,y,tabBounds.x-1,y);
			g.drawLine(tabBounds.x+tabBounds.width,y, x+w-1,y);
			g.setColor(java.awt.Color.WHITE);
			g.drawLine(tabBounds.x,y, tabBounds.x+tabBounds.width-1,y);
		}
	}


	/**
	 * Paints the left part of the border surrounding the content.
	 */
	protected void paintContentBorderLeftEdge(Graphics g, int tabPlacement,
						int selectedIndex, int x, int y, int w, int h) {
		
	}


	/**
	 * Paints the right part of the border surrounding the content.
	 */
	protected void paintContentBorderRightEdge(Graphics g, int tabPlacement,
						int selectedIndex, int x, int y, int w, int h) {
		
	}


	/**
	 * Paints the top part of the border surrounding the content.
	 */
	protected void paintContentBorderTopEdge(Graphics g, int tabPlacement,
						int selectedIndex, int x, int y, int w, int h) {
//		g.setColor(Office2003Utilities.getColor("report.TabBorderColor"));
//		if (tabPlacement!=SwingConstants.TOP || selectedIndex==-1) {
//			g.drawLine(x+1,y, x+w-1,y);
//		}
//		else {
//			// The selected tab is always in the "bottommost" run, so we
//			// don't need to check what run the tab is in.
//			Rectangle tabBounds = new Rectangle();
//			tabBounds = getTabBounds(selectedIndex, tabBounds);
//			// "Offset" the bounds since our tabs "overlap."
//			tabBounds.x -= 9;
//			tabBounds.width += 8;
//			g.drawLine(x,y,tabBounds.x-1,y);
//			g.drawLine(tabBounds.x+tabBounds.width,y, x+w-1,y);
//			g.setColor(java.awt.Color.WHITE);
//			g.drawLine(tabBounds.x,y, tabBounds.x+tabBounds.width-1,y);
//		}
	}


	/**
	 * Paints the "tab area" and the tabs inside it.
	 */
	protected void paintTabArea(Graphics g, int tabPlacement, int selectedIndex) {

		int tabCount = tabPane.getTabCount();
		Rectangle iconRect = new Rectangle();
		Rectangle textRect = new Rectangle();
		Rectangle clipRect = g.getClipBounds();  

		Graphics2D g2d = (Graphics2D)g;
		AffineTransform oldTransform = g2d.getTransform();

		switch (tabPlacement) {

			case SwingConstants.BOTTOM:
				// Paint the pseudo-"tails" of the tabs.
				for (int i=0; i<runCount; i++) {
					int firstInRun = tabRuns[i];
					Rectangle r = rects[firstInRun];
					g.translate(r.x, r.y);
					g.translate(0,TAB_HEIGHT-1);
					g2d.scale(1, -1);
					paintTabOverlappingBackground(g, tabPlacement,
										-9, TAB_HEIGHT, firstInRun);
					paintTabOverlappingBorder(g, tabPlacement,
										-9, TAB_HEIGHT-1, firstInRun);
					g2d.setTransform(oldTransform);
				}
				break;

			case SwingConstants.TOP:
				// Paint the pseudo-"tails" of the tabs.
				for (int i=0; i<runCount; i++) {
					int firstInRun = tabRuns[i];
					Rectangle r = rects[firstInRun];
					g.translate(r.x, r.y);
					paintTabOverlappingBackground(g, tabPlacement,
										-9, TAB_HEIGHT, firstInRun);
					paintTabOverlappingBorder(g, tabPlacement,
										-9, TAB_HEIGHT-1, firstInRun);
					g2d.setTransform(oldTransform);
				}
				break;

			default:

		}

		g2d.setTransform(oldTransform);

		// Paint the runs of tabs in reverse order since runs overlap.
		for (int i=runCount-1; i>=0; i--) {
			int start = tabRuns[i];
			int next = tabRuns[(i==runCount-1) ? 0 : i+1];
			int end = (next!=0 ? next-1 : tabCount-1);
			for (int j=start; j<=end; j++) {
				if (rects[j].intersects(clipRect))
					paintTab(g, tabPlacement, rects, j, iconRect, textRect);
			}
		}

    }


	/**
	 * Fills in the "background" color of a tab.  Also colors the part of the
	 * tab that is "overlapped" by another, if necessary.
	 */
	protected void paintTabBackground(Graphics g, int tabPlacement,
								int tabIndex, int x, int y, int w,
								int h, boolean isSelected) {

		g.setColor(isSelected ? Office2003Utilities.getColor("report.TabSelectedColor") :
							UIManager.getColor("TabbedPane.background"));

		Graphics2D g2d = (Graphics2D)g;
		AffineTransform oldTransform = g2d.getTransform();
		g2d.translate(x,y);

		switch (tabPlacement) {

			case SwingConstants.BOTTOM:
				g.translate(0,h);
				g2d.scale(1, -1);
				// Fall through.

			case SwingConstants.TOP:
			case SwingConstants.LEFT:
			case SwingConstants.RIGHT:

				// Fill in this tab.
				// NOTE: The fillRect() call works with (0,7, w-1,h-7) for
				// SwingConstants.TOP, but not BOTTOM (there will be an
				// unapinted line between the rect and the lines painted
				// next).  Why is this?  It must have something to do with
				// the scale done above...
				g.fillRect(0,6, w-1,h-6);
				g.drawLine(1,6, w-2,6);
				g.drawLine(2,5, w-2,5);
				g.drawLine(3,4, w-2,4);
				g.drawLine(4,3, w-2,3);
				g.drawLine(6,2, w-2,2);
				g.drawLine(8,1, w-3,1);

				// (Possibly) paint the background of the overlapping tab.
				// Ensure that there is a tab in this run "after" this one.
				int nextTabIndexInRun = getNextTabIndexInRun(
									tabPane.getTabCount(), tabIndex);
				if (nextTabIndexInRun==tabIndex+1 && nextTabIndexInRun==tabPane.getSelectedIndex()) {
					x = w - 9;
					y = h;
					paintTabOverlappingBackground(g, tabPlacement, x,y,
											nextTabIndexInRun);
				}

				break;

			default:
				g2d.setTransform(oldTransform);
				super.paintTabBackground(g, tabPlacement, tabIndex, x,y,w,h, isSelected);

		}

		// Leave our Graphics object unperturbed.
		g2d.setTransform(oldTransform);

	}


	/**
	 * Paints the line border around a tab.  Also draws the part of the border
	 * of the "overlapping" tab, if any.
	 */
	protected void paintTabBorder(Graphics g, int tabPlacement,
							int tabIndex, int x, int y, int w,
							int h, boolean isSelected) {

		g.setColor(isSelected ? Office2003Utilities.getColor("report.TabBorderColor") :
			Office2003Utilities.getColor("report.BackgroundTabBorderColor"));

		Graphics2D g2d = (Graphics2D)g;
		AffineTransform oldTransform = g2d.getTransform();
		g2d.translate(x,y);

		int rhsYStop = (tabPlacement==SwingConstants.LEFT ||
					tabPlacement==SwingConstants.RIGHT) ? h-1 : 7;

		switch (tabPlacement) {

			case SwingConstants.BOTTOM:
				g.translate(0,h-1);
				g2d.scale(1, -1);
				// Fall through.

			case SwingConstants.TOP:
			case SwingConstants.LEFT:
			case SwingConstants.RIGHT:
				// Draw this tab's border.
				g.drawLine(0,6,   3,3);
				g.drawLine(4,2,   5,2);
				g.drawLine(6,1,   7,1);
				g.drawLine(8,0,   w-3,0);		// "Top" line.
				g.drawLine(w-2,1, w-2,1);		// "Right turn."
				g.drawLine(w-1,2, w-1,rhsYStop);	// Right-hand side.

				// (Possibly) paint the border of the overlapping tab.
				int nextTabIndexInRun = getNextTabIndexInRun(
									tabPane.getTabCount(), tabIndex);
				if (nextTabIndexInRun==tabIndex+1 && nextTabIndexInRun==tabPane.getSelectedIndex()) {
					x = w - 9;
					paintTabOverlappingBorder(g, tabPlacement, x,h-1, nextTabIndexInRun);
				}
				else {
					g.drawLine(w-1,8, w-1,h-1);
				}

				break;

			default:
				g2d.setTransform(oldTransform);
				super.paintTabBorder(g, tabPlacement, tabIndex, x,y,w,h, isSelected);

		}

		// Leave our Graphics object unperturbed.
		g2d.setTransform(oldTransform);

	}


	/**
	 * Paints the background of the tab overlapping the specified tab, if any.
	 *
	 * @param g The graphics context.
	 * @param tabPlacement The tab placement.
	 * @param x The x-coordinate of tab <code>tabIndex</code>.
	 * @param y The y-coordinate of tab <code>tabIndex</code>.
	 * @param overlappingTabIndex The tab that is BEING overlapped (possibly).
	 * @see #paintTabOverlappingBorder
	 */
	protected void paintTabOverlappingBackground(Graphics g, int tabPlacement,
										int x, int y,
										int overlappingTabIndex) {

		boolean overlappingSelected = overlappingTabIndex==tabPane.getSelectedIndex();
		g.setColor(overlappingSelected ?
			Office2003Utilities.getColor("report.TabSelectedColor") :
			UIManager.getColor("TabbedPane.background"));

		switch (tabPlacement) {

			case SwingConstants.BOTTOM: // Been mirrored, so fall through.
			case SwingConstants.TOP:
				int x2 = x + 9;
				g.fillPolygon(new int[] { x, x2, x2 },
								new int[] { y, y, y-9 }, 3);
				break;

			case SwingConstants.LEFT:
			case SwingConstants.RIGHT:
				// Do nothing.

		}

	}


	/**
	 * Paints the border of the tab overlapping the specified tab, or, if the
	 * tab is not being overlapped, the last part of the tab is painted.
	 *
	 * @param g The graphics context.
	 * @param tabPlacement The tab placement.
	 * @param x The x-coordinate of tab <code>tabIndex</code>.
	 * @param y The y-coordinate of tab <code>tabIndex</code>.
	 * @param overlappingTabIndex The tab that is BEING overlapped (possibly).
	 * @see #paintTabOverlappingBackground
	 */
	protected void paintTabOverlappingBorder(Graphics g, int tabPlacement,
									int x, int y,
									int overlappingTabIndex) {

		boolean overlappingSelected = overlappingTabIndex==tabPane.getSelectedIndex();
		g.setColor(overlappingSelected ?
				Office2003Utilities.getColor("report.TabBorderColor") :
				Office2003Utilities.getColor("report.BackgroundTabBorderColor"));

		switch (tabPlacement) {

			case SwingConstants.BOTTOM: // Been mirrored, so fall through.
			case SwingConstants.TOP:
				g.drawLine(x,y, x+9,y-9);
				break;

			case SwingConstants.LEFT:
			case SwingConstants.RIGHT:
				// Do nothing.

		}

	}


	/**
	 * Paints the titles on tabs.
	 */
	protected void paintText(Graphics g, int tabPlacement, Font font,
						FontMetrics metrics, int tabIndex, String title,
						Rectangle textRect, boolean isSelected) {
		if (isSelected) {
			font = getBoldFont(font);
			metrics = tabPane.getFontMetrics(font);
		}
		super.paintText(g, tabPlacement, font, metrics, tabIndex, title,
					textRect, isSelected);
	}


	public int tabForCoordinate(JTabbedPane pane, int x, int y) {
		// Returns the rectangular bounds for a tab.
		int tab = super.tabForCoordinate(pane, x,y);
		if (tab==-1)
			return tab;
		switch (tabPane.getTabPlacement()) {
			case SwingConstants.TOP:
			case SwingConstants.BOTTOM:
				// If this tab is not selected (as selected tabs never
				// have another tab overlapping them)...
				if (tab!=tabPane.getSelectedIndex()) {
					int nextTab = getNextTabIndexInRun(
							tabPane.getTabCount(), tab);
					// And we're not at the end of a tab run (as tabs at
					// the end of a tab run cannot have a tab overlapping
					// them)...
					if (nextTab==tab+1) {
						Rectangle tabBounds = getTabBounds(tabPane, tab);
						// And they clicked in the triangle area
						// representing the overlapping tab...
						// FIXME: Add me in when I can think.
					}
				}
				break;
			default: // LEFT or RIGHT.
				// Nothing special because the tabs don't overlap.
				break;
		}
		return tab;
	}


	protected void uninstallDefaults() {
		tabPane.setFocusable(oldFocusable);
		super.uninstallDefaults();
	}
}