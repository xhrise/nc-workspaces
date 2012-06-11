
package com.ufsoft.report.checkbox;

import java.util.EventObject;

import javax.swing.tree.TreePath;


public class TreeCheckingEvent extends EventObject {
    /** Paths this event represents. */
    protected TreePath leadingPath;

    /**
         * Returns the paths that have been added or removed from the selection.
         */
    public TreePath getLeadingPath() {
	return this.leadingPath;
    }

    public TreeCheckingEvent(TreePath path) {
	super(path);
	this.leadingPath = path;
    }

}
