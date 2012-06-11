package com.ufsoft.iufo.fmtplugin.rounddigitarea;

import com.ufsoft.report.command.UfoCommand;

public class RoundDigitAreaCmd1 extends UfoCommand {

	private RoundDigitAreaExt1 _ext;

	public RoundDigitAreaCmd1(RoundDigitAreaExt1 ext) {
		_ext = ext;
	}

	public void execute(Object[] params) {
		if(Boolean.FALSE.equals(_ext.isUnRoundDigitArea())){
			_ext.setUnRoundDigitArea(true);
		}
	}

}
