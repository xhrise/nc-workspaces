package com.ufsoft.table;

import java.util.EventListener;

/**
 * <p>Title: ҳǩ�¼��������ӿ�</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UFSOFT</p>
 * @author wupeng
 * @version 1.0.0.1
 */

public interface PagemarkListener extends EventListener {
	/**
	 * ѡ�����µ�ҳǩ
	 * @param pageNum ��ҳǩ��
	 */
	void selectNewPage(int pageNum);
}