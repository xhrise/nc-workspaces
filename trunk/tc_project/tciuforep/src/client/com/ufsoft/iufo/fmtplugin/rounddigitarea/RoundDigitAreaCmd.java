package com.ufsoft.iufo.fmtplugin.rounddigitarea;

import com.ufsoft.report.command.UfoCommand;

public class RoundDigitAreaCmd extends UfoCommand {

	private RoundDigitAreaExt _ext;

	public RoundDigitAreaCmd(RoundDigitAreaExt ext) {
		_ext = ext;
	}

	public void execute(Object[] params) {
		if(Boolean.TRUE.equals(_ext.isUnRoundDigitArea())){
			_ext.setUnRoundDigitArea(false);
		}
	}

}
