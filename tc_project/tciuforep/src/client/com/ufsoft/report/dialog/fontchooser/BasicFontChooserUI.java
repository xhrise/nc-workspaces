package com.ufsoft.report.dialog.fontchooser;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.TextAttribute;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.plaf.ComponentUI;

import nc.ui.pub.beans.UICheckBox;
import nc.ui.pub.beans.UIList;
import nc.ui.pub.beans.UIPanel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITextArea;
import nc.ui.pub.beans.UITextField;

import com.ufsoft.report.util.MultiLang;

/**
 * BasicFontChooserUI. <br>
 * <BR>修改人：雷军 2004-10-9 修改为统一从"PropertySheetRB.properties"获取资源
 */
public class BasicFontChooserUI extends FontChooserUI {

  public static ComponentUI createUI(JComponent component) {
    return new BasicFontChooserUI();
  }

  private JFontChooser chooser;

  private JPanel fontPanel;
  private JTextField fontField;
  private JList fontList;

  private JPanel fontSizePanel;
  private JTextField fontSizeField;
  private JList fontSizeList;

  private JCheckBox boldCheck;
  private JCheckBox italicCheck;

  private JTextArea previewPanel;

  private PropertyChangeListener propertyListener;

  static String[] DEFAULT_FONT_SIZES = { "6", "8", "10", "11", "12", "14", "16", "18", "20", "22", "24", "26", "28",
      "32", "40", "48", "56", "64", "72" };

  public void installUI(JComponent c) {
    super.installUI(c);

    chooser = (JFontChooser) c;

    installComponents();
    installListeners();
  }

  protected void installComponents() {
    JLabel label;

    fontPanel = new UIPanel(new PercentLayout(PercentLayout.VERTICAL, 2));
    fontPanel.add(label = new nc.ui.pub.beans.UILabel(MultiLang.getString("font") /*@res "字体："*/));
    fontPanel.add(fontField = new UITextField(25));
    fontField.setEditable(false);
    fontPanel.add(new UIScrollPane(fontList = new UIList()), "*");
    label.setLabelFor(fontList);
    fontList.setVisibleRowCount(7);
    fontList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    String[] fontFamilies = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
    Arrays.sort(fontFamilies);
    fontList.setListData(fontFamilies);

    fontSizePanel = new UIPanel(new PercentLayout(PercentLayout.VERTICAL, 2));

    fontSizePanel.add(label = new nc.ui.pub.beans.UILabel(MultiLang.getString("style")/*@res "样式"*/));
    fontSizePanel.add(boldCheck = new UICheckBox(MultiLang.getString("bold")/*@res "粗体"*/));
    fontSizePanel.add(italicCheck = new UICheckBox(MultiLang.getString("italic")/*@res "斜体"*/));
    fontSizePanel.add(label = new nc.ui.pub.beans.UILabel(MultiLang.getString("size")/*@res "大小"*/));

    fontSizePanel.add(fontSizeField = new UITextField());
    label.setLabelFor(fontSizeField);
    fontSizePanel.add(new UIScrollPane(fontSizeList = new UIList()), "*");
    fontSizeList.setListData(DEFAULT_FONT_SIZES);
    fontSizeList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    fontSizeList.setVisibleRowCount(2);

    chooser.setLayout(LookAndFeelTweaks.createBorderLayout());
    JPanel panel = new UIPanel();
    panel.setLayout(LookAndFeelTweaks.createHorizontalPercentLayout());
    panel.add(fontPanel, "*");
    panel.add(fontSizePanel);
    chooser.add("Center", panel);

    previewPanel = new UITextArea();
    previewPanel.setPreferredSize(new Dimension(100, 40));
    previewPanel.setText("abcABC123");
    JScrollPane scroll = new UIScrollPane(previewPanel);
    chooser.add("South", scroll);
  }

  protected void installListeners() {
    SelectedFontUpdater listener = new SelectedFontUpdater();
    if (listener != null) {
      fontList.addListSelectionListener(listener);
      fontSizeList.addListSelectionListener(listener);
      fontSizeField.getDocument().addDocumentListener(listener);
      boldCheck.addActionListener(listener);
      italicCheck.addActionListener(listener);
    }

    propertyListener = createPropertyChangeListener();
    chooser.addPropertyChangeListener(JFontChooser.SELECTED_FONT_CHANGED_KEY, propertyListener);
  }

  public void uninstallUI(JComponent c) {
    chooser.remove(fontPanel);
    chooser.remove(fontSizePanel);

    super.uninstallUI(c);
  }

  public void uninstallListeners() {
    chooser.removePropertyChangeListener(propertyListener);
  }

  protected PropertyChangeListener createPropertyChangeListener() {
    return new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent evt) {
        updateDisplay();
      }
    };
  }

  private void updateDisplay() {
    Font selected = chooser.getSelectedFont();
    if (selected != null) {
      previewPanel.setFont(selected);
      fontList.setSelectedValue(selected.getName(), true);
      fontSizeField.setText(String.valueOf(selected.getSize()));
      fontSizeList.setSelectedValue(String.valueOf(selected.getSize()), true);
      boldCheck.setSelected(selected.isBold());
      italicCheck.setSelected(selected.isItalic());
    }
  }

  private void updateSelectedFont() {
    Font currentFont = chooser.getSelectedFont();
    String fontFamily = currentFont == null ? "SansSerif" : currentFont.getName();
    int fontStyle = currentFont == null ? Font.PLAIN : currentFont.getStyle();
    int fontSize = currentFont == null ? 11 : currentFont.getSize();

    if (fontList.getSelectedIndex() >= 0) {
      fontFamily = (String) fontList.getSelectedValue();
    }

    if (fontSizeField.getText().trim().length() > 0) {
      try {
        fontSize = Integer.parseInt(fontSizeField.getText().trim());
      } catch (Exception e) {
        // ignore the NumberFormatException
      }
    }

    Map attributes = new HashMap();
    attributes.put(TextAttribute.SIZE, new Float(fontSize));
    attributes.put(TextAttribute.FAMILY, fontFamily);
    if (boldCheck.isSelected()) {
      attributes.put(TextAttribute.WEIGHT, TextAttribute.WEIGHT_BOLD);
    }
    if (italicCheck.isSelected()) {
      attributes.put(TextAttribute.POSTURE, TextAttribute.POSTURE_OBLIQUE);
    }

    Font font = Font.getFont(attributes);
    if (!font.equals(currentFont)) {
      chooser.setSelectedFont(font);
      previewPanel.setFont(font);
    }
  }

  private class SelectedFontUpdater implements ListSelectionListener, DocumentListener, ActionListener {
    public void valueChanged(ListSelectionEvent e) {
      if (fontList == e.getSource() && fontList.getSelectedValue() != null) {
        fontField.setText((String) fontList.getSelectedValue());
      }
      if (fontSizeList == e.getSource() && fontSizeList.getSelectedValue() != null) {
        fontSizeField.setText((String) fontSizeList.getSelectedValue());
      }
      updateSelectedFont();
    }

    public void changedUpdate(DocumentEvent e) {
      updateLater();
    }

    public void insertUpdate(DocumentEvent e) {
      updateLater();
    }

    public void removeUpdate(DocumentEvent e) {
      updateLater();
    }

    public void actionPerformed(ActionEvent e) {
      updateLater();
    }

    void updateLater() {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          updateSelectedFont();
        }
      });
    }
  }

}