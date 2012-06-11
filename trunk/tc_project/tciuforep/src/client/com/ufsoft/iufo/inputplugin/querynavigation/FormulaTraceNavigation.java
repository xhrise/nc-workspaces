package com.ufsoft.iufo.inputplugin.querynavigation;

import java.util.Vector;

import com.ufida.dataset.IContext;
import com.ufida.zior.view.Mainboard;
import com.ufsoft.table.CellPosition;

import nc.ui.iufo.input.edit.RepDataEditor;


public class FormulaTraceNavigation implements Cloneable {

//	private static FormulaTraceNavigation singleton = null;
	
	transient private  Vector<FormulaTraceNavView> vecHistory = new Vector<FormulaTraceNavView>();
	//µ±Η°΄°Με
	transient private FormulaTraceNavView curView;
	
	private static final  String FMLNAVIGATION_KEY = "fml_navigation_key";
	

	public Object clone(){
		return this;
	}
	
	public synchronized FormulaTraceNavView getPreView(){
		int iIndex = vecHistory.indexOf(curView);
		FormulaTraceNavView view = null;
		if(iIndex > 0){
			view = vecHistory.get(iIndex-1);
			setCurView(view);
		}
		return view;
	}
	
	public synchronized FormulaTraceNavView getNextView(){
		int iIndex = vecHistory.indexOf(curView);
		FormulaTraceNavView view = null;
		if(iIndex < vecHistory.size()-1){
			view = vecHistory.get(iIndex + 1);
			setCurView(view);
		}
		return view;
	}
	
	public synchronized void addView(FormulaTraceNavView param){
		vecHistory.add(param);
	}
	
	public synchronized void addView(RepDataEditor editor ,CellPosition cellPos){
		FormulaTraceNavView view = new FormulaTraceNavView(editor,cellPos);
		if(vecHistory.indexOf(view) < 0)
			vecHistory.add(view);
		setCurView(view);
	}
	
	public synchronized boolean hasPre(){
		if(curView == null)
			return false;
		else{
			int iIndex = vecHistory.indexOf(curView);
			if(iIndex>0)
				return true;
			else
				return false;
		}
		
	}
	
	public synchronized boolean hasNext(){
		if(curView == null)
			return false;
		else{
			int iIndex = vecHistory.indexOf(curView);
			if(iIndex < vecHistory.size() - 1)
				return true;
			else
				return false;
		}
		
	}
	public synchronized void remove(RepDataEditor editor){
		int iSize = vecHistory.size();
		for(int i= iSize - 1;i > -1; i--){
			FormulaTraceNavView traceNavView = vecHistory.get(i);
			if(traceNavView.getEditor() == editor){
				vecHistory.remove(traceNavView);
				if (curView==traceNavView){
					curView=null;
				}
			}
		}
		
	}
	
	
	public synchronized FormulaTraceNavView getCurView() {
		return curView;
	}

	public synchronized void setCurView(FormulaTraceNavView curView) {
		this.curView = curView;
	}
	
	public synchronized void setCurView(RepDataEditor editor ,CellPosition cellPos){
		this.curView = new FormulaTraceNavView(editor,cellPos);
	}
	
	public synchronized void remove(FormulaTraceNavView view){
		vecHistory.remove(view);
	}
	
//	public synchronized void destroy(){
//		singleton = null;
//	}
	public synchronized static void uninitializeNavigation(Mainboard mainBoard){
		mainBoard.getContext().removeAttribute(FMLNAVIGATION_KEY);
	}
	
	
	public synchronized static FormulaTraceNavigation getInstance(Mainboard mainboard){
		FormulaTraceNavigation navigaion = (FormulaTraceNavigation)mainboard.getContext().getAttribute(FMLNAVIGATION_KEY);
		if(navigaion == null){
			navigaion = new FormulaTraceNavigation();
			mainboard.getContext().setAttribute(FMLNAVIGATION_KEY, navigaion);
		}
		return navigaion;
	}
	
	public static void removeFromContext(IContext context){
		if (context!=null)
			context.removeAttribute(FMLNAVIGATION_KEY);
	}
}
