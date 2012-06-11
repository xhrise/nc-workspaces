
package com.ufsoft.report.checkbox;

import java.util.EventListener;

public interface TreeCheckingListener extends EventListener {
    /**
         * Called whenever the value of the checking changes.
         * 
         * @param e the event that characterizes the change.
         */
    void valueChanged(TreeCheckingEvent e);
}