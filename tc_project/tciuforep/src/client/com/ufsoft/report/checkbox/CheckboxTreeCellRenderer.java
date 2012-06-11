
package com.ufsoft.report.checkbox;

import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.TreeCellRenderer;


public interface CheckboxTreeCellRenderer extends TreeCellRenderer {

    /**
         * This method is redeclared just to underline that the implementor has
         * to properly display the checking/graying state of <code>value</code>.
         * This should go in the method parameters, but would imply changes to
         * Swing classes that were considered unpractical. For example in
         * DefaultCheckboxTreeCellRenderer the following code is used to get the
         * checking/graying states:
         * 
         * <pre>
         * TreeCheckingModel checkingModel = ((CheckboxTree) tree).getCheckingModel();
         * 
         * TreePath path = tree.getPathForRow(row);
         * 
         * boolean enabled = checkingModel.isPathEnabled(path);
         * 
         * boolean checked = checkingModel.isPathChecked(path);
         * 
         * boolean greyed = checkingModel.isPathGreyed(path);
         * </pre>
         * 
         * You could use a QuadristateCheckbox to properly renderer the states
         * (as in DefaultCheckboxTreeCellRenderer).
         * 
         * @see TreeCellRenderer#getTreeCellRendererComponent
         */
    Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf, int row,
	    boolean hasFocus);

    /**
         * Returns whether the specified relative coordinates insist on the
         * intended checkbox control. May be used by a mouse listener to figure
         * out whether to toggle a node or not.
         */
    public boolean isOnHotspot(int x, int y);

}
