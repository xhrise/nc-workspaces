package com.ufsoft.table.print.multidoc.win32;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Locale;

import javax.print.CancelablePrintJob;
import javax.print.Doc;
import javax.print.DocPrintJob;
import javax.print.MultiDoc;
import javax.print.MultiDocPrintJob;
import javax.print.PrintException;
import javax.print.PrintService;
import javax.print.attribute.AttributeSetUtilities;
import javax.print.attribute.DocAttributeSet;
import javax.print.attribute.HashPrintJobAttributeSet;
import javax.print.attribute.PrintJobAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.DocumentName;
import javax.print.attribute.standard.JobName;
import javax.print.event.PrintJobAttributeListener;
import javax.print.event.PrintJobEvent;
import javax.print.event.PrintJobListener;
import javax.swing.JLabel;

import com.ufida.iufo.pub.tools.AppDebug;
import com.ufsoft.report.util.MultiLang;

/**
 * IUFO中的批量打印任务，其依赖于具体环境的sun.print.Win32PrintJob(不是Java的标准类)
 * @author guogang
 * @see javax.print.MultiDocPrintJob
 */
public class IUFOMultiDocPrintJob implements MultiDocPrintJob,CancelablePrintJob{
    
    private PrintService service;
    private PrintRequestAttributeSet reqAttrSet;
    private PrintJobAttributeSet jobAttrSet;
    private MultiDoc multidoc;
    private DocPrintJob curJob;
    private boolean isPrinting = false;
    private MessageFormat statusFormat =new MessageFormat("{0},{1}");
    private JLabel printStatus;
    
    public IUFOMultiDocPrintJob(PrintService printService){
        service = printService;
    }
    public void setPrintStatus(JLabel status){
    	printStatus=status;
    }
    public void cancel() throws PrintException{
		if(curJob!=null&&curJob instanceof CancelablePrintJob){
			((CancelablePrintJob)curJob).cancel();
		}
	}
	public void print(MultiDoc doc, PrintRequestAttributeSet attributes)
			throws PrintException {
		reqAttrSet = attributes;
		multidoc=doc;
		try {
			while(multidoc.next()!=null){
			  print(multidoc.getDoc(),reqAttrSet);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			AppDebug.debug(e);
		}
	}
	/* (non-Javadoc)
	 * @see javax.print.DocPrintJob#addPrintJobAttributeListener(javax.print.event.PrintJobAttributeListener, javax.print.attribute.PrintJobAttributeSet)
	 */
	public void addPrintJobAttributeListener(
			PrintJobAttributeListener listener, PrintJobAttributeSet attributes) {
	if(curJob!=null)
		curJob.addPrintJobAttributeListener(listener, attributes);
		
	}
	/* (non-Javadoc)
	 * @see javax.print.DocPrintJob#addPrintJobListener(javax.print.event.PrintJobListener)
	 */
	public void addPrintJobListener(PrintJobListener listener) {
		if(curJob!=null)
			curJob.addPrintJobListener(listener);	
		
	}
	/* 
	 * 需要考虑
	 * @see javax.print.DocPrintJob#getAttributes()
	 */
	/**
	 * @i18n report00068=IUFO打印
	 */
	public PrintJobAttributeSet getAttributes() {
		if(jobAttrSet == null)
        {
			jobAttrSet = new HashPrintJobAttributeSet();
			jobAttrSet.add(new JobName(MultiLang.getString("report00068"), Locale.getDefault()));
            return AttributeSetUtilities.unmodifiableView(jobAttrSet);
        }
//		jobAttrSet.add(new NumberOfDocuments(jobQueue.size()));
		return jobAttrSet;
	}
	/* (non-Javadoc)
	 * @see javax.print.DocPrintJob#getPrintService()
	 */
	public PrintService getPrintService() {
		return service;
	}
	/* (non-Javadoc)
	 * @see javax.print.DocPrintJob#print(javax.print.Doc, javax.print.attribute.PrintRequestAttributeSet)
	 */
	public void print(Doc doc,PrintRequestAttributeSet attributes)
			throws PrintException {
		
		curJob=service.createPrintJob();
		curJob=new ThreadSafeDocPrintJob(curJob);
		ThreadSafeDocPrintJob wrappedPrintJob =(ThreadSafeDocPrintJob)curJob;
		wrappedPrintJob.startUpdatingStatus(statusFormat, printStatus);
		curJob.print(doc, attributes);
		wrappedPrintJob.stopUpdatingStatus();	
	}
	/* (non-Javadoc)
	 * @see javax.print.DocPrintJob#removePrintJobAttributeListener(javax.print.event.PrintJobAttributeListener)
	 */
	public void removePrintJobAttributeListener(
			PrintJobAttributeListener listener) {
		if(curJob!=null)
			curJob.removePrintJobAttributeListener(listener);
		
	}
	/* (non-Javadoc)
	 * @see javax.print.DocPrintJob#removePrintJobListener(javax.print.event.PrintJobListener)
	 */
	public void removePrintJobListener(PrintJobListener listener) {
		if(curJob!=null)
			curJob.removePrintJobListener(listener);
		
	}
	private class ThreadSafeDocPrintJob implements DocPrintJob{
        private DocPrintJob printDelegate;
        private MessageFormat statusFormat;
        private JLabel statusTitle;
        private Throwable retThrowable;
        private String name;
        
        public ThreadSafeDocPrintJob(DocPrintJob printDelegate) {
			this.printDelegate = printDelegate;
		}

		public void startUpdatingStatus(MessageFormat statusFormat,
				JLabel statusTitle) {
			this.statusFormat = statusFormat;
			this.statusTitle = statusTitle;
		}
		public void stopUpdatingStatus() {
            statusFormat = null;
            statusTitle = null;
        }
		
		public void addPrintJobAttributeListener(
				PrintJobAttributeListener listener,
				PrintJobAttributeSet attributes) {
			// TODO Auto-generated method stub
			
		}

		public void addPrintJobListener(PrintJobListener listener) {
			
			
		}

		public PrintJobAttributeSet getAttributes() {
			// TODO Auto-generated method stub
			return null;
		}

		public PrintService getPrintService() {
			// TODO Auto-generated method stub
			return null;
		}

		public void print(final Doc doc, final PrintRequestAttributeSet attributes)
				throws PrintException {
			Runnable runnable = new Runnable() {
				/**
				 * @i18n report00069=开始打印..
				 * @i18n report00076=打印出错..
				 */
				public synchronized void run() {
					// set a flag to hide the selection and focused cell
					isPrinting = true;

					try {
						if (doc != null&&statusTitle!=null) {
							// set the status message on the JOptionPane with
							// the current page number
							DocAttributeSet docSet = doc.getAttributes();
							if (docSet != null
									&& docSet.get(DocumentName.class) != null) {
								DocumentName docName = (DocumentName) docSet
										.get(DocumentName.class);
								name=docName.getValue();
								statusTitle.setText(statusFormat
										.format(new Object[] { name,MultiLang.getString("report00069") }));
							}

						}
						printDelegate.addPrintJobListener(new PrintJobListener(){

							/**
							 * @i18n report00070=数据已传送给打印机..
							 */
							public void printDataTransferCompleted(
									PrintJobEvent pje) {
								statusTitle.setText(statusFormat
										.format(new Object[] { name,MultiLang.getString("report00070") }));
								
							}

							/**
							 * @i18n report00071=打印已取消..
							 */
							public void printJobCanceled(PrintJobEvent pje) {
								statusTitle.setText(statusFormat
										.format(new Object[] { name,MultiLang.getString("report00071") }));
								
							}

							/**
							 * @i18n report00072=打印完成..
							 */
							public void printJobCompleted(PrintJobEvent pje) {
								statusTitle.setText(statusFormat
										.format(new Object[] { name,MultiLang.getString("report00072") }));
								
							}

							/**
							 * @i18n report00073=打印失败..
							 */
							public void printJobFailed(PrintJobEvent pje) {
								statusTitle.setText(statusFormat
										.format(new Object[] { name,MultiLang.getString("report00073") }));
							}

							public void printJobNoMoreEvents(PrintJobEvent pje) {
								
			
							}

							/**
							 * @i18n report00074=纸张可能已用完，请检查打印机..
							 */
							public void printJobRequiresAttention(
									PrintJobEvent pje) {
								statusTitle.setText(statusFormat
										.format(new Object[] { name,MultiLang.getString("report00074") }));
							}
							
						});
						// call into the delegate and save the return value
						printDelegate.print(doc, attributes);
					} catch (Throwable throwable) {
						// save any Throwable to be rethrown
						retThrowable = throwable;
						if(statusTitle!=null)
						statusTitle.setText(statusFormat.format(new Object[] { name,MultiLang.getString("report00076") }));
					} finally {
						// restore the flag
						isPrinting = false;
						// notify the caller that we're done
						notifyAll();
					}
				}
			};
			synchronized (runnable) {
				// make sure these are initialized
				retThrowable = null;
				runnable.run();

				// if the delegate threw a throwable, rethrow it here
				if (retThrowable != null) {
					if (retThrowable instanceof PrintException) {
						throw (PrintException) retThrowable;
					} else if (retThrowable instanceof RuntimeException) {
						throw (RuntimeException) retThrowable;
					} else if (retThrowable instanceof Error) {
						throw (Error) retThrowable;
					}

					// can not happen
					throw new AssertionError(retThrowable);
				}
			}

		}

		public void removePrintJobAttributeListener(
				PrintJobAttributeListener listener) {
			// TODO Auto-generated method stub
			
		}

		public void removePrintJobListener(PrintJobListener listener) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
 