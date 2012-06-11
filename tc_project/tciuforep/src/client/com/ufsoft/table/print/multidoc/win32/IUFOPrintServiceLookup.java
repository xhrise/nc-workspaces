package com.ufsoft.table.print.multidoc.win32;

import java.util.ArrayList;

import javax.print.DocFlavor;
import javax.print.MultiDocPrintService;
import javax.print.PrintService;
import javax.print.attribute.AttributeSet;

import sun.print.Win32PrintService;
import sun.print.Win32PrintServiceLookup;

/**
 * IUFO批量打印的打印查找服务，依赖于具体环境的sun.print.Win32PrintServiceLookup
 * @author guogang
 * @see javax.print.PrintServiceLookup
 */
public class IUFOPrintServiceLookup extends Win32PrintServiceLookup {
	public IUFOPrintService[] getMultiDocPrintServices(DocFlavor[] flavors,
			AttributeSet attributes) {
		if(flavors==null||flavors.length==0)
			return null;
		IUFOPrintService printservices[] = null;
		ArrayList arraylist = new ArrayList();
		DocFlavor flavor;
		for(int i=0;i<flavors.length;i++){
			flavor=flavors[i];
			PrintService tPrintServices[]=super.getPrintServices(flavor, attributes);
			if(tPrintServices==null||tPrintServices.length==0){
				continue;
			}else{
				for(int j=0;j<tPrintServices.length;j++){
					
					arraylist.add(new IUFOPrintService(tPrintServices[j]));
				}
			}
			
		}
		printservices = new IUFOPrintService[arraylist.size()];
        return (IUFOPrintService[])arraylist.toArray(printservices);
		
	}
}
