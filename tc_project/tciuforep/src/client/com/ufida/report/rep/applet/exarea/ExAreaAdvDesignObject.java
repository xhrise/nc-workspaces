package com.ufida.report.rep.applet.exarea;

import com.ufsoft.table.exarea.ExAreaCell;
import com.ufsoft.table.exarea.ExAreaModel;

import nc.vo.pub.dsmanager.DataSetDesignObject;

public class ExAreaAdvDesignObject extends DataSetDesignObject {

	private ExAreaModel model;
	private ExAreaCell cell;
	
	public ExAreaModel getExAreaModel() {
		return model;
	}
	public void setExAreaModel(ExAreaModel model) {
		this.model = model;
	}
	public ExAreaCell getExAreaCell() {
		return cell;
	}
	public void setExAreaCell(ExAreaCell cell) {
		this.cell = cell;
	}
	
	
	
}
