package com.ufsoft.table.demo;

import com.ufsoft.table.*;

/**
 * <p>Title: ��ʾ�������CellsAuthorization�����Ƶ�Ԫ��Ȩ��</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UFSOFT</p>
 * @author wupeng
 * @version 1.0.0.1
 */

public class CustomAuth implements CellsAuthorization {
	private CellPosition[] unReadCells, unWriteCells;	
	/**
	 * @param unReadCells ���ɶ���Ԫ
	 * @param unWriteCells ����д��Ԫ
	 */
	public CustomAuth(CellPosition[] unReadCells, CellPosition[] unWriteCells) {
		this.unReadCells = unReadCells;
		this.unWriteCells = unWriteCells;
	}

	public boolean isReadable(int row, int col) {
		if (unReadCells != null) {
			for (int i = 0; i < unReadCells.length; i++) {
				if (unReadCells[i].equals(CellPosition.getInstance(row, col))) {
					return false;
				}
			}

		}
		return true;
	}

	public boolean isWritable(int row, int col) {
		if (unWriteCells != null) {
			for (int i = 0; i < unWriteCells.length; i++) {
				if (unWriteCells[i].equals(CellPosition.getInstance(row, col))) {
					return false;
				}
			}

		}
		return true;
	}
	public void authorize(int row, int col, int type) {

	}

}