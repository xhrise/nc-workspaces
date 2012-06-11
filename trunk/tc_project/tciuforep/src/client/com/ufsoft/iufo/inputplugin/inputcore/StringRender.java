package com.ufsoft.iufo.inputplugin.inputcore;

import java.awt.Component;
import java.lang.reflect.Method;

import javax.swing.JComponent;

import com.ufsoft.iufo.inputplugin.dynarea.DynAreaInputPlugin;
import com.ufsoft.iufo.inputplugin.key.KeyFmt;
import com.ufsoft.report.UfoReport;
import com.ufsoft.table.Cell;
import com.ufsoft.table.CellPosition;
import com.ufsoft.table.CellsPane;
import com.ufsoft.table.re.DefaultSheetCellRenderer;

public class StringRender extends DefaultSheetCellRenderer {
 
	private static final long serialVersionUID = -7952524960174260889L;

	/**当是主表关键字时,前面加上关键字名字**/
	public Component getCellRendererComponent(CellsPane table, Object obj,
			boolean isSelected, boolean hasFocus, int row, int column, Cell cell) {
		JComponent editorComp = (JComponent) super.getCellRendererComponent(
				table, obj, isSelected, hasFocus, row, column, cell);
		CellPosition pos = CellPosition.getInstance(row, column);
		KeyFmt fmt = (KeyFmt) table.getDataModel().getBsFormat(pos,
				KeyFmt.EXT_FMT_KEYINPUT);
		if (fmt != null) {
			String addText = fmt.toString();
			if (addText != null && (!fmt.isInDynArea())) {
				Method getText;
				try {
					getText = editorComp.getClass().getMethod("getText", null);
					String text = (String) getText.invoke(editorComp, null);
					if (text == null || text.length() == 0)
						text = (obj == null) ? "" : obj.toString();
					Method setText = editorComp.getClass().getMethod("setText",
							new Class[] { String.class });
					setText.invoke(editorComp, new Object[] { addText + ":"
							+ text });
				} catch (Exception e) {
					throw new RuntimeException("");
				}
			}
		}
		return editorComp;
	}

	public static boolean isInMainTable(Component comp, int row, int col) {
		UfoReport report = null;
		while (comp != null) {
			if (comp instanceof UfoReport) {
				report = (UfoReport) comp;
				break;
			}
			comp = comp.getParent();
		}
		DynAreaInputPlugin dynPI = (DynAreaInputPlugin) report
				.getPluginManager().getPlugin(
						DynAreaInputPlugin.class.getName());
		if (dynPI.isInDynArea(row, col)) {
			return false;
		} else {
			return true;
		}
	}
	//    /**参照KeyVO,不直接引用避免下载代码*/
	//    public final static String CORP_PK = "000000000000";
	//    public final static String YEAR_PK = "000000000001";
	//    public final static String HALF_YEAR_PK = "000000000002";
	//    public final static String QUARTER_PK = "000000000003";
	//    public final static String MONTH_PK = "000000000004";
	//    public final static String TENDAYS_PK = "000000000005";
	//    public final static String WEEK_PK = "000000000006";
	//    public final static String DAY_PK = "000000000007";
	//    public final static String DIC_CORP_PK = "000000000008";
	//    public final static String COIN_PK = "000000000009";
}
