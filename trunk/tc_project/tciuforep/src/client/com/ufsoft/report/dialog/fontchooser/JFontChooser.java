package com.ufsoft.report.dialog.fontchooser;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.HeadlessException;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.UIManager;

import nc.ui.pub.beans.UIPanel;

import com.ufsoft.report.dialog.UfoDialog;

/**
 * <code>JFontChooser</code> provides a pane of controls designed to allow a
 * user to manipulate and select a font.
 *
 * @beaninfo
 *  attribute: isContainer false
 *  description: A component that supports selecting a Font.
 */
public class JFontChooser extends JComponent {

  // ensure at least the default FontChooserUI is registered
  static {
    if (UIManager.get("FontChooserUI") == null) {
      UIManager.put("FontChooserUI", BasicFontChooserUI.class.getName());
    }
  }

  public static final String SELECTED_FONT_CHANGED_KEY = "selectedFont";

  protected Font selectedFont;

  /**
   * Creates a font chooser with an initial default font
   *
   */
  public JFontChooser() {
    super();
    selectedFont = getFont();
    updateUI();
  }

  /**
   * Notification from the <code>UIManager</code> that the L&F has changed.
   * Replaces the current UI object with the latest version from
   * the <code>UIManager</code>.
   *
   * @see javax.swing.JComponent#updateUI
   */
  public void updateUI() {
    setUI(((FontChooserUI)UIManager.getUI(this)));
  }

  /**
   * Sets the L&F object that renders this component.
   *
   * @param ui the <code>FontChooserUI</code> L&F object
   * @see javax.swing.UIDefaults#getUI
   *
   * @beaninfo
   *  bound: true
   *  hidden: true
   *  description: The UI object that implements the font chooser's LookAndFeel.
   */
  public void setUI(FontChooserUI ui) {
    super.setUI(ui);
  }

  /**
   * Returns the name of the L&F class that renders this component.
   *
   * @return the string "FontChooserUI"
   * @see javax.swing.JComponent#getUIClassID
   * @see javax.swing.UIDefaults#getUI
   */
  public String getUIClassID() {
    return "FontChooserUI";
  }

  /**
   * Sets the selected font of this JFontChooser. This will fire a <code>PropertyChangeEvent</code>
   * for the property named {@link #SELECTED_FONT_CHANGED_KEY}.
   *
   * @param f the font to select
   * @see javax.swing.JComponent#addPropertyChangeListener
   *
   * @beaninfo
   *  bound: true
   *  hidden: false
   *  description: The current font the chooser is to display
   */
  public void setSelectedFont(Font f) {
    Font oldFont = selectedFont;
    selectedFont = f;
    firePropertyChange(SELECTED_FONT_CHANGED_KEY, oldFont, selectedFont);
  }

  /**
   * Gets the current font value from the font chooser.
   *
   * @return the current font value of the font chooser
   */
  public Font getSelectedFont() {
    return selectedFont;
  }

  /**
   * Shows a modal font-chooser dialog and blocks until the dialog is hidden.
   * If the user presses the "OK" button, then this method hides/disposes the
   * dialog and returns the selected color. If the user presses the "Cancel"
   * button or closes the dialog without pressing "OK", then this method
   * hides/disposes the dialog and returns <code>null</code>.
   *
   * @param component the parent <code>Component</code> for the dialog
   * @param title the String containing the dialog's title
   * @param initialFont the initial Font set when the font-chooser is shown
   * @return the selected font or <code>null</code> if the user opted out
   * @exception HeadlessException if GraphicsEnvironment.isHeadless() returns
   *              true.
   * @see java.awt.GraphicsEnvironment#isHeadless
   */
  public static Font showDialog(
    Component parent,
    String title,
    Font initialFont) {
    UfoDialog dialog = new UfoDialog((Container)parent,title){
		private static final long serialVersionUID = 41886160996297576L;
		protected void initDialog() {  
			super.initDialog();
    	    getContentPane().setLayout(new BorderLayout());
    	    JPanel btnPanel = new UIPanel();
    	    btnPanel.add(createOkButton());
    	    btnPanel.add(createCancleButton());
    	    getContentPane().add(btnPanel,BorderLayout.SOUTH);
    	}
    };
    JFontChooser chooser = new JFontChooser();
    chooser.setSelectedFont(initialFont);
    dialog.getContentPane().add(chooser,BorderLayout.CENTER);
    dialog.pack();
    dialog.show();
    if (dialog.getResult() == UfoDialog.ID_OK) {
      return chooser.getSelectedFont();
    } else {
      return null;
    }
  }

}
