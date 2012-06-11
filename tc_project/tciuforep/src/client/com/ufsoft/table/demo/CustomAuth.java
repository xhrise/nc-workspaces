package com.ufsoft.table.demo;

import com.ufsoft.table.*;

/**
 * <p>Title: 演示如何利用CellsAuthorization来控制单元的权限</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: UFSOFT</p>
 * @author wupeng
 * @version 1.0.0.1
 */

public class CustomAuth implements CellsAuthorization {
	private CellPosition[] unReadCells, unWriteCells;	
	/**
	 * @param unReadCells 不可读单元
	 * @param unWriteCells 不可写单元
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