package com.ufsoft.table.demo;
/**
   @version 1.01 2004-08-24
   @author Cay Horstmann
*/

import java.awt.event.*;

import javax.print.*;
import javax.print.attribute.standard.JobName;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashDocAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.event.PrintJobEvent;
import javax.print.event.PrintJobListener;
import javax.swing.*;

import com.ufsoft.table.print.PrintSet;
import com.ufsoft.table.print.multidoc.win32.IUFOPrintServiceLookup;
import com.ufsoft.table.print.multidoc.*;

/**
   This program demonstrates the use of print services. The program lets you print a GIF image 
   to any of the print services that support the GIF document flavor.
*/
public class PrintServiceTest
{
   public static void main(String[] args)
   {
      JFrame frame = new PrintServiceFrame();
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      frame.setVisible(true);
   }
}

/**
   This frame displays the image to be printed. It contains
   menus for opening an image file, printing, and selecting
   a print service.
*/
class PrintServiceFrame extends JFrame
{
   public PrintServiceFrame()
   {
      setTitle("PrintServiceTest");
      setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);

      // set up menu bar
      JMenuBar menuBar = new JMenuBar();
      setJMenuBar(menuBar);

      JMenu menu = new JMenu("File");
      menuBar.add(menu);

      JMenuItem printItem = new JMenuItem("Print");
      menu.add(printItem);
      printItem.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               printFile();
            }
         });

      JMenuItem exitItem = new JMenuItem("Exit");
      menu.add(exitItem);
      exitItem.addActionListener(new
         ActionListener()
         {
            public void actionPerformed(ActionEvent event)
            {
               System.exit(0);
            }
         });

      // use a label to display the images
   }
  
   /**
      Print the current file using the current print service.
   */
   public void printFile()
   {
      try
      {
    	  PrintServiceLookup.registerServiceProvider(new IUFOPrintServiceLookup());
    	  MultiDocPrintService services[]=PrintServiceLookup.lookupMultiDocPrintServices(new DocFlavor[]{flavor}, null);
    	  PrintRequestAttributeSet attributes = new HashPrintRequestAttributeSet();
    	  DocAttributeSet das = new HashDocAttributeSet();
          PrintRequestAttributeSet pras=new HashPrintRequestAttributeSet();
          Doc[] printSrc={new SimpleDoc(new PrintPanel("你好","世界"), flavor, das),new SimpleDoc(new PrintPanel("Hello","World"), flavor, das)};
          DocPrintJob job;
          if (services != null&&services.length>0)
          {
    	 PrintService service=IUFOServiceUI.printDialog(attributes);
         if(service!=null){
        	 for(int i=0;i<printSrc.length;i++){
                 job = service.createPrintJob();//创建打印作业  
                 
                 System.out.println(job.getClass().getName());
                  job.addPrintJobListener(new PrintJobListener(){

         			public void printDataTransferCompleted(PrintJobEvent pje) {
         				System.out.println("**printDataTransferCompleted**"+pje.getPrintEventType());
         				
         			}

         			public void printJobCanceled(PrintJobEvent pje) {
         				System.out.println("**printJobCanceled**"+pje.getPrintEventType());
         				
         			}

         			public void printJobCompleted(PrintJobEvent pje) {
         				System.out.println("**printJobCompleted**"+pje.getPrintEventType());
         			}

         			public void printJobFailed(PrintJobEvent pje) {
         				System.out.println("**printJobFailed**"+pje.getPrintEventType());
         				
         			}

         			public void printJobNoMoreEvents(PrintJobEvent pje) {
         				System.out.println("**printJobNoMoreEvents**"+pje.getPrintEventType());
         				
         			}

         			public void printJobRequiresAttention(PrintJobEvent pje) {
         				System.out.println("**printJobRequiresAttention**"+pje.getPrintEventType());
         				
         			}
                 	 
                  });
                Doc doc;//建立打印文件格式    
                doc=printSrc[i];
                JobName jobName=new JobName("testjob"+i,getLocale());
                pras.add(jobName);
                job.print(doc, pras);//进行文件的打印
                } 
         }
        }
      }
      catch (PrintException e)
      {  
         JOptionPane.showMessageDialog(this, e);
      }
   }

   DocFlavor flavor=DocFlavor.SERVICE_FORMATTED.PRINTABLE;
   private static final int DEFAULT_WIDTH = 300;
   private static final int DEFAULT_HEIGHT = 400;
}

