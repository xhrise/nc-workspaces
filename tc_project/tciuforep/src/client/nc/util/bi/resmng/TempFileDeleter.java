package nc.util.bi.resmng;

import java.io.File;
import java.util.ArrayList;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

/**
 * @author zyjun
 */
public class TempFileDeleter implements HttpSessionBindingListener {

    /** The chart names. */
    private ArrayList<String>	m_alTempFiles = new ArrayList<String>();
    
    /**
     * Blank constructor.
     */
    public TempFileDeleter() {
        super();
    }
  

    /**
     * Add a chart to be deleted when the session expires
     *
     * @param filename  the name of the chart in the temporary directory to be deleted.
     */
    public void addFile(String strFullPathName) {
        m_alTempFiles.add(strFullPathName);
    }

    /**
     * Binding this object to the session has no additional effects.
     *
     * @param event  the session bind event.
     */
    public void valueBound(HttpSessionBindingEvent event) {
        return;
    }

    /**
     * When this object is unbound from the session (including upon session
     * expiry) the files that have been added to the ArrayList are iterated
     * and deleted.
     *
     * @param event  the session unbind event.
     */
    public void valueUnbound(HttpSessionBindingEvent event) {

    	int		nSize = m_alTempFiles.size();
        for( int i=0; i<nSize; i++ ){
            File file = new File((String)m_alTempFiles.get(i));
            if (file.exists()) {
                file.delete();
            }       	
        }
     
        return;

    }
}
