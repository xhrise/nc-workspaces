package com.ufsoft.table;

import java.util.EventListener;

/**
 * <p>Title: 页签事件监听器接口</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UFSOFT</p>
 * @author wupeng
 * @version 1.0.0.1
 */

public interface PagemarkListener extends EventListener {
	/**
	 * 选择了新的页签
	 * @param pageNum 新页签号
	 */
	void selectNewPage(int pageNum);
}