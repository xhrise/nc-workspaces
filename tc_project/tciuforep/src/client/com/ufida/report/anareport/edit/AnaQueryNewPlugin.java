package com.ufida.report.anareport.edit;

import javax.swing.JApplet;
import javax.swing.SwingUtilities;

import nc.vo.iufo.measure.MeasureQueryModelDef;

import com.ufida.zior.plugin.AbstractPlugin;
import com.ufida.zior.plugin.IPluginAction;
import com.ufsoft.iufo.inputplugin.biz.FreeQueryMainBoardListner;

public class AnaQueryNewPlugin extends AbstractPlugin{

	@Override
	protected IPluginAction[] createActions() {
		return new IPluginAction[] {
				new AnaQueryAction(MeasureQueryModelDef.QUERYBY_CORP),
				new AnaQueryAction(MeasureQueryModelDef.QEURY_NEXTCORP),
				new AnaQueryAction(MeasureQueryModelDef.QUERYBY_YEAR),
				new AnaQueryAction(MeasureQueryModelDef.QUERY_ANAREOIRT) };
	}

	@Override
	public void shutdown() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void startup() {
		  FreeQueryMainBoardListner.setMainBoard(getMainboard(),(JApplet)SwingUtilities.getAncestorOfClass(JApplet.class,getMainboard()));
	}

}
