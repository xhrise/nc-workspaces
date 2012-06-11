/*
 * 创建日期 2005-7-28
 *
 */
package nc.ui.bi.query.designer;

import java.awt.Color;
import java.awt.Component;
import java.sql.Types;

import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import nc.vo.bi.query.manager.BIModelUtil;
import nc.vo.iuforeport.businessquery.SelectFldVO;
import com.ufsoft.iufo.resource.StringResource;

/**
 * @author zjb
 * 
 * 树节点修饰（用于区分主外键字段和数值型字段）
 * 
 * @author：朱俊彬
 */
public class FieldCellRenderer extends DefaultTreeCellRenderer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * PowerCellRenderer 构造子注解。
	 */
	public FieldCellRenderer() {
		super();
	}

	/**
	 * @return Component that the renderer uses to draw the value.
	 * @i18n miufo00426=自由项
	 */
	public Component getTreeCellRendererComponent(JTree tree, Object value,
			boolean sel, boolean expanded, boolean leaf, int row,
			boolean hasFocus) {
		setComponentOrientation(tree.getComponentOrientation()); //ibm.597
		String stringValue = tree.convertValueToText(value, sel, expanded,
				leaf, row, hasFocus);
		setText(stringValue);
		if (sel)
			setForeground(getTextSelectionColor());
		else
			setForeground(getTextNonSelectionColor());
		if (!tree.isEnabled()) {
			setEnabled(false);
			if (leaf) {
				setDisabledIcon(getLeafIcon());
			} else if (expanded) {
				setDisabledIcon(getOpenIcon());
			} else {
				setDisabledIcon(getClosedIcon());
			}
		} else {
			setEnabled(true);
			if (leaf) {
				setIcon(getLeafIcon());
			} else if (expanded) {
				setIcon(getOpenIcon());
			} else {
				setIcon(getClosedIcon());
			}
		}

		DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
		//对主外键字段和数值型字段设置不同颜色
		if (node.getUserObject() instanceof SelectFldVO) {
			SelectFldVO sf = (SelectFldVO) node.getUserObject();
			if (sf != null && sf.getColtype() != null) {
				String exp = sf.getExpression();
				int iColType = sf.getColtype().intValue();
				if (iColType == Types.CHAR) {
					int iLength = sf.getScale();
					if (exp.equalsIgnoreCase("pk_corp") || iLength == 20) {
						if (exp.indexOf(StringResource.getStringResource("miufo00426")) == -1) {
							//主外键
							setForeground(Color.red);
						}
					}
				} else if (BIModelUtil.isNumberType(iColType)) {
					//数值型
					setForeground(Color.blue);
				}
			}
		}
		selected = sel;
		return this;
	}
}
 