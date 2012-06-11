/**
 * 创建日期 2005-7-11
 *
 */
package com.ufsoft.iufo.inputplugin.biz;
import com.ufida.iufo.pub.tools.AppDebug;

import junit.framework.TestCase;

import com.ufsoft.iuforeport.tableinput.applet.ITableInputMenuType;
import com.ufsoft.iuforeport.tableinput.applet.TableInputApplet;

/**
 * @author liulp
 *
 */
public class InputMenuOperTester extends TestCase {
	private IInputBizOper inputBizMenuOper = null;
	protected void setUp() throws Exception{
        TableInputApplet tableInputApplet = new TableInputApplet();
		tableInputApplet.init();
        inputBizMenuOper = new InputBizOper(tableInputApplet.getUfoReport());
	}
	protected void tearDown() throws Exception{
        inputBizMenuOper = null;
	}
	public void testPorformTask(){
		Object returnObj = inputBizMenuOper.performBizTask(ITableInputMenuType.MENU_TYPE_SAVE);
		assertNotNull(returnObj);
		AppDebug.debug(returnObj.toString());//@devTools System.out.println(returnObj.toString());
		if(returnObj instanceof Boolean){
			assertTrue(returnObj.toString(),true);
		}else{
			throw new AssertionError();
		}
		
		
	}
}
