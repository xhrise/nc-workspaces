
package com.ufsoft.report.checkbox;

import javax.swing.tree.TreePath;

/**
 * SimpleTreeCheckingMode defines a TreeCheckingMode without recursion. In this
 * simple mode the check state always changes only the current node: no
 * recursion.
 * 
 */
public class SimpleTreeCheckingMode extends TreeCheckingMode {

    SimpleTreeCheckingMode(DefaultTreeCheckingModel model) {
	super(model);
    }

    @Override
    public void checkPath(TreePath path) {
	this.model.addToCheckedPathsSet(path);
	this.model.updatePathGreyness(path);
	this.model.updateAncestorsGreyness(path);
    }

    @Override
    public void uncheckPath(TreePath path) {
	this.model.removeFromCheckedPathsSet(path);
	this.model.updatePathGreyness(path);
	this.model.updateAncestorsGreyness(path);
    }

    /*
         * (non-Javadoc)
         * 
         * @see it.cnr.imaa.essi.lablib.gui.checkboxtree.TreeCheckingMode#updateCheckAfterChildrenInserted(javax.swing.tree.TreePath)
         */
    @Override
    public void updateCheckAfterChildrenInserted(TreePath parent) {
	this.model.updatePathGreyness(parent);
	this.model.updateAncestorsGreyness(parent);
    }

    /*
         * (non-Javadoc)
         * 
         * @see it.cnr.imaa.essi.lablib.gui.checkboxtree.TreeCheckingMode#updateCheckAfterChildrenRemoved(javax.swing.tree.TreePath)
         */
    @Override
    public void updateCheckAfterChildrenRemoved(TreePath parent) {
	this.model.updatePathGreyness(parent);
	this.model.updateAncestorsGreyness(parent);
    }

    /*
         * (non-Javadoc)
         * 
         * @see it.cnr.imaa.essi.lablib.gui.checkboxtree.TreeCheckingMode#updateCheckAfterStructureChanged(javax.swing.tree.TreePath)
         */
    @Override
    public void updateCheckAfterStructureChanged(TreePath parent) {
	this.model.updatePathGreyness(parent);
	this.model.updateAncestorsGreyness(parent);
    }

}