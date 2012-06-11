package com.ufsoft.table.print.multidoc;

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GraphicsEnvironment;
import java.awt.HeadlessException;
import java.awt.KeyboardFocusManager;
import java.awt.Window;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.attribute.Attribute;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Destination;
import javax.print.attribute.standard.Fidelity;

import com.ufsoft.table.print.PrintSet;
import com.ufsoft.table.print.multidoc.win32.IUFOPrintService;
import com.ufsoft.table.print.multidoc.win32.IUFOPrintServiceLookup;

import sun.print.SunAlternateMedia;


public class IUFOServiceUI {


    /**
     * Presents a dialog to the user for selecting a print service (printer).
     * It is displayed at the location specified by the application and
     * is modal.
     * If the specification is invalid or would make the dialog not visible it
     * will be displayed at a location determined by the implementation.
     * The dialog blocks its calling thread and is application modal.
     * <p>
     * The dialog may include a tab panel with custom UI lazily obtained from
     * the PrintService's ServiceUIFactory when the PrintService is browsed.
     * The dialog will attempt to locate a MAIN_UIROLE first as a JComponent,
     * then as a Panel. If there is no ServiceUIFactory or no matching role
     * the custom tab will be empty or not visible.
     * <p>
     * The dialog returns the print service selected by the user if the user
     * OK's the dialog and null if the user cancels the dialog.
     * <p>
     * An application must pass in an array of print services to browse.
     * The array must be non-null and non-empty.
     * Typically an application will pass in only PrintServices capable
     * of printing a particular document flavor.
     * <p>
     * An application may pass in a PrintService to be initially displayed.
     * A non-null parameter must be included in the array of browsable
     * services.
     * If this parameter is null a service is chosen by the implementation.
     * <p>
     * An application may optionally pass in the flavor to be printed.
     * If this is non-null choices presented to the user can be better
     * validated against those supported by the services.
     * An application must pass in a PrintRequestAttributeSet for returning
     * user choices.
     * On calling the PrintRequestAttributeSet may be empty, or may contain
     * application-specified values.
     * <p>
     * These are used to set the initial settings for the initially
     * displayed print service. Values which are not supported by the print
     * service are ignored. As the user browses print services, attributes
     * and values are copied to the new display. If a user browses a
     * print service which does not support a particular attribute-value, the
     * default for that service is used as the new value to be copied.
     * <p>
     * If the user cancels the dialog, the returned attributes will not reflect
     * any changes made by the user.
     *
     * A typical basic usage of this method may be : 
     * <pre>
     * PrintService[] services = PrintServiceLookup.lookupPrintServices(
     *                            DocFlavor.INPUT_STREAM.JPEG, null);
     * PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
     * if (services.length > 0) {
     *    PrintService service =  ServiceUI.printDialog(null, 50, 50,
     *                                               services, services[0],
     *                                               null,
     *                                               attributes);
     *    if (service != null) {
     *     ... print ...
     *    }
     * }
     * </pre>
     * <p>

     * @param gc used to select screen. null means primary or default screen.
     * @param x location of dialog including border in screen coordinates
     * @param y location of dialog including border in screen coordinates
     * @param services to be browsable, must be non-null.
     * @param defaultService - initial PrintService to display.
     * @param flavor - the flavor to be printed, or null.
     * @param attributes on input is the initial application supplied
     * preferences. This cannot be null but may be empty.
     * On output the attributes reflect changes made by the user. 
     * @return print service selected by the user, or null if the user
     * cancelled the dialog.
     * @throws HeadlessException if GraphicsEnvironment.isHeadless()
     * returns true.
     * @throws IllegalArgumentException if services is null or empty,
     * or attributes is null, or the initial PrintService is not in the
     * list of browsable services.
     */
    public static IUFOPrintService printDialog(PrintRequestAttributeSet attributes) 
	throws HeadlessException
    {
        int defaultIndex = 0;
        DocFlavor flavor=DocFlavor.SERVICE_FORMATTED.PAGEABLE;
//        PrintServiceLookup.registerServiceProvider(new IUFOPrintServiceLookup());
//        MultiDocPrintService services[]=PrintServiceLookup.lookupMultiDocPrintServices(new DocFlavor[]{flavor}, null);
        IUFOPrintServiceLookup lookup=new IUFOPrintServiceLookup();
        IUFOPrintService services[]=lookup.getMultiDocPrintServices(new DocFlavor[]{flavor}, null);
       
        int x=200;
        int y=100;
        if (GraphicsEnvironment.isHeadless()) {
            throw new HeadlessException();
        } else if ((services == null) || (services.length == 0)) {
            throw new IllegalArgumentException("services must be non-null " +
                                               "and non-empty");
        } else if (attributes == null) {
            throw new IllegalArgumentException("attributes must be non-null");
        } 

	boolean newFrame = false;
	Window owner = 
	    KeyboardFocusManager.getCurrentKeyboardFocusManager().
	    getActiveWindow();

	if (!(owner instanceof Dialog || owner instanceof Frame)) {
	    owner = new Frame();
	    newFrame = true;
	}
	
	IUFOServiceDialog dialog;
	if (owner instanceof Frame) {
	    dialog = new IUFOServiceDialog(null, x, y, 
				       services, defaultIndex,
				       flavor, attributes, 
				       (Frame)owner,null);
	} else {
	    dialog = new IUFOServiceDialog(null, x, y, 
				       services, defaultIndex,
				       flavor, attributes, 
				       (Dialog)owner,null);
	}
        dialog.show();

        if (dialog.getStatus() == IUFOServiceDialog.APPROVE) {
            PrintRequestAttributeSet newas = dialog.getAttributes();
            Class dstCategory = Destination.class;
            Class amCategory = SunAlternateMedia.class;
            Class fdCategory = Fidelity.class;

            if (attributes.containsKey(dstCategory) &&
                !newas.containsKey(dstCategory))
            {
                attributes.remove(dstCategory);
            }

            if (attributes.containsKey(amCategory) &&
                !newas.containsKey(amCategory))
            {
                attributes.remove(amCategory);
            }

            attributes.addAll(newas);

            Fidelity fd = (Fidelity)attributes.get(fdCategory);
            if (fd != null) {
                if (fd == Fidelity.FIDELITY_TRUE) {
                    removeUnsupportedAttributes(dialog.getPrintService(),
                                                flavor, attributes);
                }
            }
        }
	      
	if (newFrame) {
	    owner.dispose();
	}
	return dialog.getPrintService();
    }
    public static PrintSet printDialog(PrintRequestAttributeSet attributes,PrintSet printSet) 
	throws HeadlessException
    {
        int defaultIndex = 0;
        DocFlavor flavor=DocFlavor.SERVICE_FORMATTED.PAGEABLE;
//        PrintServiceLookup.registerServiceProvider(new IUFOPrintServiceLookup());
//        MultiDocPrintService services[]=PrintServiceLookup.lookupMultiDocPrintServices(new DocFlavor[]{flavor}, null);
        IUFOPrintServiceLookup lookup=new IUFOPrintServiceLookup();
        IUFOPrintService services[]=lookup.getMultiDocPrintServices(new DocFlavor[]{flavor}, null);
        int x=200;
        int y=100;
        if (GraphicsEnvironment.isHeadless()) {
            throw new HeadlessException();
        } else if ((services == null) || (services.length == 0)) {
            throw new IllegalArgumentException("services must be non-null " +
                                               "and non-empty");
        } else if (attributes == null) {
            throw new IllegalArgumentException("attributes must be non-null");
        } 

	boolean newFrame = false;
	Window owner = 
	    KeyboardFocusManager.getCurrentKeyboardFocusManager().
	    getActiveWindow();

	if (!(owner instanceof Dialog || owner instanceof Frame)) {
	    owner = new Frame();
	    newFrame = true;
	}
	
	IUFOServiceDialog dialog;
	if (owner instanceof Frame) {
	    dialog = new IUFOServiceDialog(null, x, y, 
				       services, defaultIndex,
				       flavor, attributes, 
				       (Frame)owner,printSet);
	} else {
	    dialog = new IUFOServiceDialog(null, x, y, 
				       services, defaultIndex,
				       flavor, attributes, 
				       (Dialog)owner,printSet);
	}
        dialog.show();
        
	if (newFrame) {
	    owner.dispose();
	}
	 if (dialog.getStatus() == IUFOServiceDialog.APPROVE) {
		 return dialog.getPrintSet();
	 }
	 else return null;
    }
    /**
     * Removes any attributes from the given AttributeSet that are 
     * unsupported by the given PrintService/DocFlavor combination.
     */
    private static void removeUnsupportedAttributes(PrintService ps,
                                                    DocFlavor flavor,
                                                    AttributeSet aset)
    {
        AttributeSet asUnsupported = ps.getUnsupportedAttributes(flavor,
                                                                 aset);

        if (asUnsupported != null) {
	    Attribute[] usAttrs = asUnsupported.toArray();

            for (int i=0; i<usAttrs.length; i++) {
                Class category = usAttrs[i].getCategory();

                if (ps.isAttributeCategorySupported(category)) {
                    Attribute attr = 
                        (Attribute)ps.getDefaultAttributeValue(category);

                    if (attr != null) {
                        aset.add(attr);
                    } else {
                        aset.remove(category);
                    }
                } else {
                    aset.remove(category);
                }
            }
        }
    }
}
