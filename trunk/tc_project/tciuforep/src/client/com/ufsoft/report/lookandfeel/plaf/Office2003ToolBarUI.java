/* ====================================================================
 * 
 * Office Look and Feels License
 * http://sourceforge.net/projects/officelnfs
 *
 * Copyright (c) 2003-2005 Robert Futrell.  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer. 
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The names "Office Look And Feels" and "OfficeLnFs" must not
 *    be used to endorse or promote products derived from this software
 *    without prior written permission. For written permission, please
 *    contact robert_futrell@users.sourceforge.net.
 *
 * 4. Products derived from this software may not be called "OfficeLnFs"
 *    nor may "OfficeLnFs" appear in their names without prior written
 *    permission.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * ====================================================================
 */ 
package com.ufsoft.report.lookandfeel.plaf;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JToolBar;
import javax.swing.plaf.ComponentUI;


/**
 * The toolbar UI used in the Office2003 Look and Feel.
 *
 * @author Robert Futrell
 * @version 1.1
 */
public class Office2003ToolBarUI extends OfficeXPToolBarUI {


	public static ComponentUI createUI(JComponent c) {
		return new Office2003ToolBarUI();
	}


	/**
	 * Paints the toolbar.  This is actually rather expensive (for a toolbar
	 * paint method at least), as we gradient-fill a rounded-rectangle.
	 *
	 * @param g The graphics context in which to paint.
	 * @param c The toolbar to paint.
	 */
	public void paint(Graphics g, JComponent c) {

		// Don't paint it this way if this toolbar is in a file chooser.
		Component parent = c.getParent();
		if (parent!=null && (parent instanceof JFileChooser)) {
			super.paint(g, c);
			return;
		}

		// Colors for the rounded-edge, gradient toolbar.
		Color beginColor = Office2003Utilities.getColor("report.ToolBarBeginGradientColor");
		Color endColor   = Office2003Utilities.getColor("report.ToolBarEndGradientColor");
        
		Graphics2D g2d = (Graphics2D) g;
		Rectangle bounds = c.getBounds();
		int orientation = toolBar.getOrientation();
		
		Color backgroundColor = Office2003Utilities.getColor("report.ToolBarBackgroundColor");
	    Color borderColor = Office2003Utilities.getColor("report.ToolBarBottomBorderColor");
		if (orientation==JToolBar.HORIZONTAL) {

			ComponentOrientation co = toolBar.getComponentOrientation();
			boolean rtl = !co.isLeftToRight();

			int width = getContainedComponentsWidth(c);

				// Don't use Toolbar.background as that would also change
				// the color of toolbars in JFileChoosers.
				g.setColor(backgroundColor);
				g.fillRect(0,0, bounds.width,bounds.height);

			// Paint the rounded edge toolbar.
			
			GradientPaint paint = new GradientPaint(0,0,		beginColor,
									0,bounds.height,	endColor);
			g2d.setPaint(paint);
			int x = 1;
			int y = 0;
			int w = width + 1;
			int h = bounds.height - 1;
			if (rtl) {
				x = bounds.width-width-2;
			}
			g2d.fillRoundRect(x,y, w,h, 8,8);
			
			// @edit by wangyga at 2009-8-27,����07:57:19
//			g2d.setColor(borderColor);
//			g2d.drawLine(x+4,h, (x+4)+5+width-9,h);

		}

		else { // Vertical.

			int height = getContainedComponentsHeight(c);
			// Paint the (solid color) background.
			g.setColor(backgroundColor);
			g.fillRect(0,0, bounds.width,bounds.height);

			// Paint the rounded edge toolbar.
			GradientPaint paint = new GradientPaint(0,0,	beginColor,
										bounds.width, 0, endColor);
			g2d.setPaint(paint);
			g2d.fillRoundRect(0,1, bounds.width,height+1, 8,8);

		}

	}


}