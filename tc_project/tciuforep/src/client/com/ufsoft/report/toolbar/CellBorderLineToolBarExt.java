package com.ufsoft.report.toolbar;

import java.util.Hashtable;

import javax.swing.JComboBox;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.report.UfoReport;
import com.ufsoft.report.constant.PropertyType;
import com.ufsoft.report.toolbar.dropdown.ImagePanel;
import com.ufsoft.report.toolbar.dropdown.ImagePanelVo;
import com.ufsoft.report.toolbar.dropdown.SwatchPanel;
import com.ufsoft.table.format.TableConstant;

/**
 * JComboBox型的下拉panel，设置单元边框线
 * 
 * @author:wangyuguang
 * 
 */
public class CellBorderLineToolBarExt extends CellAttrComboBoxExt {

	/** <code>OLType</code> 边框操作类型：外边框 */
	private final static int OLType = 45;
	/** <code>ALType</code> 边框操作类型：所有边框 */
	private final static int ALType = 46;
	/** <code>NOType</code> 边框操作类型：无边框 */
	private final static int NOType = 48;
	/** <code>ALType</code> 边框操作类型：粗底边框 */
	private final static int WBType = 49;
	/** <code>WOType</code> 边框操作类型：粗外边框 */
	private final static int WOType = 50;
	/** <code>TBBType</code> 边框操作类型：上边框和双下边框 */
	private final static int TBBType = 51;
	/** <code>TBType</code> 边框操作类型：上边框和下边框 */
	private final static int TBType = 52;
	/** <code>BBType</code> 边框操作类型：双下边框 */
	private final static int BBType = 53;
	/** <code>BottomType</code> 边框操作类型：下边框 */
	private final static int BottomType = 54;
	/** <code>RightType</code> 边框操作类型：右边框 */
	private final static int RightType = 55;

	/** <code>AllLine</code> 单元边框图片名称：所有边框 */
	private final static String AllLine = "reportcore/all_line.png";
	/** <code>OutLine</code> 单元边框图片名称：外部边框 */
	private final static String OutLine = "reportcore/out_line.png";
	/** <code>NoLine</code> 单元边框图片名称：无边框 */
	private final static String NoLine = "reportcore/no_line.png";
	/** <code>WBLine</code> 单元边框图片名称：粗底边框 */
	private final static String WBLine = "reportcore/wide_bottom_line.png";
	/** <code>WOLine</code> 单元边框图片名称：粗外边框 */
	private final static String WOLine = "reportcore/wide_out_line.png";
	/** <code>TBBLine</code> 单元边框图片名称：上边框和双下边框 */
	private final static String TBBLine = "reportcore/top_bothbottom_line.png";
	/** <code>TBLine</code> 单元边框图片名称：上边框和下边框 */
	private final static String TBLine = "reportcore/top_bottom_line.png";
	/** <code>BBLine</code> 单元边框图片名称：双下边框 */
	private final static String BBLine = "reportcore/both_bottom_line.png";
	/** <code>BottomLine</code> 单元边框图片名称：下边框 */
	private final static String BottomLine = "reportcore/bottom_line.png";
	/** <code>RightLine</code> 单元边框图片名称：右边框 */
	private final static String RightLine = "reportcore/right_line.png";
	// 默认边框线类型
	private final int DFType = 1;
	// 粗边框线类型
	private final int WFType = 5;
	// 双边框类型
	private final int DoFType = 8;
	// 默认边框颜色
	private final int DFColor = -16777216;

	public CellBorderLineToolBarExt(int propertyname, SwatchPanel panel,
			String imageFile, UfoReport rep) {
		super(propertyname, panel, imageFile, rep);
	}

	/**
	 * override
	 * @see AbsActionExt.getParams();
	 * @see MenuUtil.createComboxListener() 该方法中会向params[]中设置操作的属性值
	 */
	public Object[] getParams(UfoReport container) {
		Object[] params = new Object[8 + 1];
		params = getCellParams(params);
		int nIndex = 0;
		JComboBox item = getItem();

		Object selected = item.getSelectedItem();
		if (selected instanceof String) {
			try {
				nIndex = Integer.parseInt((String) selected);
			} catch (NumberFormatException e) {
				AppDebug.debug(e);
			}
		}
		if (selected instanceof Integer) {
			nIndex = ((Integer) (selected)).intValue();
		}

		Hashtable<Integer, Integer> m_propertyCache = new Hashtable<Integer, Integer>();
		m_propertyCache = getSelectImageValue(nIndex);
		params[0] = m_propertyCache;
		return params;

	}

	/**
	 * 
	 * 
	 * @see AbsActionExt.getSwatchPanel();返回imagePanel构造时需要的数组
	 */
	public static ImagePanel getSwatchPanel() {
		ImagePanelVo allLineVo = new ImagePanelVo();
		allLineVo.setImagePath(AllLine);
		allLineVo.setOperate(ALType);
		ImagePanelVo outLineVo = new ImagePanelVo();
		outLineVo.setImagePath(OutLine);
		outLineVo.setOperate(OLType);
		ImagePanelVo noLineVo = new ImagePanelVo();
		noLineVo.setImagePath(NoLine);
		noLineVo.setOperate(NOType);
		ImagePanelVo wbLineVo = new ImagePanelVo();
		wbLineVo.setImagePath(WBLine);
		wbLineVo.setOperate(WBType);
		ImagePanelVo woLineVo = new ImagePanelVo();
		woLineVo.setImagePath(WOLine);
		woLineVo.setOperate(WOType);
		ImagePanelVo tbLineVo = new ImagePanelVo();
		tbLineVo.setImagePath(TBLine);
		tbLineVo.setOperate(TBType);
		ImagePanelVo bottomLineVo = new ImagePanelVo();
		bottomLineVo.setImagePath(BottomLine);
		bottomLineVo.setOperate(BottomType);
		ImagePanelVo rightLineVo = new ImagePanelVo();
		rightLineVo.setImagePath(RightLine);
		rightLineVo.setOperate(RightType);
		Object[] imageValue = { allLineVo, outLineVo, noLineVo, wbLineVo,
				woLineVo, tbLineVo, bottomLineVo, rightLineVo };

		return new ImagePanel(imageValue);
	}

	/**
	 * 
	 * 
	 * @see AbsActionExt.getSelectImageValue();根据边框的类型返回相对应的Hashtable
	 */
	private Hashtable<Integer, Integer> getSelectImageValue(int nIndex) {
		Hashtable<Integer, Integer> image_propertyCache = new Hashtable<Integer, Integer>();
		if (nIndex == ALType) {
			image_propertyCache.put(PropertyType.TLType, DFType);
			image_propertyCache.put(PropertyType.TLColor, DFColor);
			image_propertyCache.put(PropertyType.BLType, DFType);
			image_propertyCache.put(PropertyType.BLColor, DFColor);
			image_propertyCache.put(PropertyType.LLType, DFType);
			image_propertyCache.put(PropertyType.LLColor, DFColor);
			image_propertyCache.put(PropertyType.RLType, DFType);
			image_propertyCache.put(PropertyType.RLColor, DFColor);
			image_propertyCache.put(PropertyType.HLType, DFType);
			image_propertyCache.put(PropertyType.HLColor, DFColor);
			image_propertyCache.put(PropertyType.VLType, DFType);
			image_propertyCache.put(PropertyType.VLColor, DFColor);
		} else if (nIndex == OLType) {
			image_propertyCache.put(PropertyType.TLType, DFType);
			image_propertyCache.put(PropertyType.TLColor, DFColor);
			image_propertyCache.put(PropertyType.BLType, DFType);
			image_propertyCache.put(PropertyType.BLColor, DFColor);
			image_propertyCache.put(PropertyType.LLType, DFType);
			image_propertyCache.put(PropertyType.LLColor, DFColor);
			image_propertyCache.put(PropertyType.RLType, DFType);
			image_propertyCache.put(PropertyType.RLColor, DFColor);

		} else if (nIndex == NOType) {
			image_propertyCache.put(PropertyType.TLType,
					TableConstant.UNDEFINED);
			image_propertyCache.put(PropertyType.TLColor,
					TableConstant.UNDEFINED);
			image_propertyCache.put(PropertyType.BLType,
					TableConstant.UNDEFINED);
			image_propertyCache.put(PropertyType.BLColor,
					TableConstant.UNDEFINED);
			image_propertyCache.put(PropertyType.LLType,
					TableConstant.UNDEFINED);
			image_propertyCache.put(PropertyType.LLColor,
					TableConstant.UNDEFINED);
			image_propertyCache.put(PropertyType.RLType,
					TableConstant.UNDEFINED);
			image_propertyCache.put(PropertyType.RLColor,
					TableConstant.UNDEFINED);
			image_propertyCache.put(PropertyType.HLType,
					TableConstant.UNDEFINED);
			image_propertyCache.put(PropertyType.HLColor,
					TableConstant.UNDEFINED);
			image_propertyCache.put(PropertyType.VLType,
					TableConstant.UNDEFINED);
			image_propertyCache.put(PropertyType.VLColor,
					TableConstant.UNDEFINED);
			image_propertyCache.put(PropertyType.DLType,
					TableConstant.UNDEFINED);
			image_propertyCache.put(PropertyType.DLColor,
					TableConstant.UNDEFINED);
			image_propertyCache.put(PropertyType.D2LType,
					TableConstant.UNDEFINED);
			image_propertyCache.put(PropertyType.D2LColor,
					TableConstant.UNDEFINED);
		} else if (nIndex == RightType) {
			image_propertyCache.put(PropertyType.RLType, DFType);
			image_propertyCache.put(PropertyType.RLColor, DFColor);
		} else if (nIndex == BottomType) {
			image_propertyCache.put(PropertyType.BLType, DFType);
			image_propertyCache.put(PropertyType.BLColor, DFColor);
		} else if (nIndex == TBType) {
			image_propertyCache.put(PropertyType.TLType, DFType);
			image_propertyCache.put(PropertyType.TLColor, DFColor);
			image_propertyCache.put(PropertyType.BLType, DFType);
			image_propertyCache.put(PropertyType.BLColor, DFColor);
		} else if (nIndex == WBType) {
			image_propertyCache.put(PropertyType.BLType, WFType);
			image_propertyCache.put(PropertyType.BLColor, DFColor);
		} else if (nIndex == WOType) {
			image_propertyCache.put(PropertyType.TLType, WFType);
			image_propertyCache.put(PropertyType.TLColor, DFColor);
			image_propertyCache.put(PropertyType.BLType, WFType);
			image_propertyCache.put(PropertyType.BLColor, DFColor);
			image_propertyCache.put(PropertyType.LLType, WFType);
			image_propertyCache.put(PropertyType.LLColor, DFColor);
			image_propertyCache.put(PropertyType.RLType, WFType);
			image_propertyCache.put(PropertyType.RLColor, DFColor);
		} else if (nIndex == BBType) {
			image_propertyCache.put(PropertyType.BLType, DoFType);
			image_propertyCache.put(PropertyType.BLColor, DFColor);
		} else if (nIndex == TBBType) {
			image_propertyCache.put(PropertyType.TLType, DFType);
			image_propertyCache.put(PropertyType.TLColor, DFColor);
			image_propertyCache.put(PropertyType.BLType, DoFType);
			image_propertyCache.put(PropertyType.BLColor, DFColor);
		}
		return image_propertyCache;
	}
}
