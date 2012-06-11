package com.ufsoft.table.print.multidoc.win32;

import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.MultiDocPrintJob;
import javax.print.MultiDocPrintService;
import javax.print.PrintService;
import javax.print.ServiceUIFactory;
import javax.print.attribute.Attribute;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.PrintServiceAttribute;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.event.PrintServiceAttributeListener;

import sun.print.Win32PrintJob;
import sun.print.Win32PrintService;

/**
 * IUFO支持批量打印的打印服务，其依赖于具体环境的sun.print.Win32PrintService
 * @author guogang
 * @see javax.print.MultiDocPrintService
 */
public class IUFOPrintService implements MultiDocPrintService {

    private PrintService printService;
    private IUFOMultiDocPrintJob multiDocPrintJob;
	
	public IUFOPrintService(PrintService printservice){
		this.printService=printservice;
		
	}
	
	public IUFOMultiDocPrintJob createMultiDocPrintJob() {
		if(multiDocPrintJob==null){
			multiDocPrintJob=new IUFOMultiDocPrintJob(printService);
			return multiDocPrintJob;
		}
		return multiDocPrintJob;
	}

	public void addPrintServiceAttributeListener(
			PrintServiceAttributeListener listener) {
		printService.addPrintServiceAttributeListener(listener);

	}
    /**
     * 重载
     */
	public DocPrintJob createPrintJob() {
		return printService.createPrintJob();
	}

	public <T extends PrintServiceAttribute> T getAttribute(Class<T> category) {
		return printService.getAttribute(category);
	}

	public PrintServiceAttributeSet getAttributes() {
		return printService.getAttributes();
	}

	public Object getDefaultAttributeValue(Class<? extends Attribute> category) {
		return printService.getDefaultAttributeValue(category);
	}

	public String getName() {
		return printService.getName();
	}

	public ServiceUIFactory getServiceUIFactory() {
		return printService.getServiceUIFactory();
	}

	public Class<?>[] getSupportedAttributeCategories() {
		return printService.getSupportedAttributeCategories();
	}

	public Object getSupportedAttributeValues(
			Class<? extends Attribute> category, DocFlavor flavor,
			AttributeSet attributes) {
		return printService.getSupportedAttributeValues(category, flavor, attributes);
	}

	public DocFlavor[] getSupportedDocFlavors() {
		return printService.getSupportedDocFlavors();
	}

	public AttributeSet getUnsupportedAttributes(DocFlavor flavor,
			AttributeSet attributes) {
		return printService.getUnsupportedAttributes(flavor, attributes);
	}

	public boolean isAttributeCategorySupported(
			Class<? extends Attribute> category) {
		return printService.isAttributeCategorySupported(category);
	}

	public boolean isAttributeValueSupported(Attribute attrval,
			DocFlavor flavor, AttributeSet attributes) {
		return printService.isAttributeValueSupported(attrval, flavor, attributes);
	}

	public boolean isDocFlavorSupported(DocFlavor flavor) {
		return printService.isDocFlavorSupported(flavor);
	}

	public void removePrintServiceAttributeListener(
			PrintServiceAttributeListener listener) {
		printService.removePrintServiceAttributeListener(listener);
	}

}
