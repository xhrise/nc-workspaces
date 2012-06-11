package com.ufsoft.table.re;

import java.awt.*;

import com.ufsoft.table.*;

/**
 * <p>
 * Title:������ҳ�е�Ԫ����Ⱦ��
 * </p>
 * <p>
 * Description: ���ؼ�
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company: UFSOFT
 * </p>
 * 
 * @author wupeng
 * @version 1.0.0.1
 */

public interface SheetCellRenderer {

	/**
	 * �õ�����һ����Ԫ����������Ը��ݵ�Ԫ��λ�������ò�ͬ����Ⱦ����
	 * 
	 * @param cellsPane
	 *            CellsPane ������������Sheet
	 * @param value
	 *            Object ���Ƶ�����(�����ǿؼ�������������ҵ���������ʵ�����Ͷ�Ϊ��Ӧ�����ݡ�
	 *            �ڿؼ�����������Ҫ��ʽ���󣬿����ɲ���cellsPane��row��column��ӵõ���
	 *            ��Cell��չ��ʽ�����ֵ��Ӧ��չ��ʽ����
	 *            )
	 * @param isSelected
	 *            boolean ��ǰ�Ƿ�ѡ��
	 * @param hasFocus
	 *            boolean �Ƿ��ǻ�ý���ĵ�Ԫ
	 * @param row
	 *            int ������λ�á�
	 * @param column
	 *            int ������λ��.
	 * @return Component ���ڻ��Ƶ�����������ֻ�����ڻ��ƣ����ƺ�Ӹ������ɾ�������Ǹ���������ػ档
	 * ���ԣ���������Թ��á�
	 */
	Component getCellRendererComponent(CellsPane cellsPane, Object value,
			boolean isSelected, boolean hasFocus, int row, int column, Cell cell);

}