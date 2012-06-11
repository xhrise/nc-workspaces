package com.ufsoft.report.component;

import java.awt.Container;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * 当需要在报表状态栏显示进度条时，需要在新的线程里执行操作。
 * indeterminate Mode时根据线程是否还是活动的来判断结束标记。
 * determinate Mode时根据setValue的值大于最大值来判断结束标志。
 * @author zzl
 */
public class ProcessController{
	
	private boolean _indeterminateMode = false;
	private JProgressBar _bar;
	private int _minimum = 0;
	private int _maximum = 100;
	private Timer _timer;
	private Thread _thread;
	private String _string;
	
	public ProcessController(boolean indeterminateMode) {
		_indeterminateMode = indeterminateMode;
	}
	public void setRunnable(Runnable runnable){
		_thread = new Thread(runnable);
	}
	
	public void setString(String string){
		_string = string;
	}
	/**
	 * 在线程执行过程中，对于determinate mode的进度条，可以随时修改进度值！
	 * @param value
	 */
	public void setValue(int value){
		if(!_indeterminateMode){
			_bar.setValue(value);
			if(value >= _maximum){
				stopProcessBar();
			}
		}
	}
	
	public void setMinimum(int minimum){
		_minimum = minimum;
	}
	public void setMaximum(int maximum){
		_maximum = maximum;
	}
	
	public void showProcessBar(JProgressBar bar){
		if (bar!=null){
			//需要保证bar已经放到父组件中。
			_bar = bar;
			_bar.setIndeterminate(_indeterminateMode);
			_bar.setString(_string);
			if(!_indeterminateMode){
				_bar.setMinimum(_minimum);
				_bar.setMaximum(_maximum);
			}
			//禁止窗口接收用户事件。
			SwingUtilities.getWindowAncestor(_bar).setEnabled(false);
			_bar.setEnabled(true);
		}
		
		//监听线程停止后，自动中止滚动条显示。
		_timer = new Timer(300, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
            	if(!_thread.isAlive()){
            		_timer.stop();
            		stopProcessBar();
            	}
            }
        });
		_thread.start();
		_timer.start();
	}
	private void stopProcessBar(){
		if(_bar != null){
			Window win = SwingUtilities.getWindowAncestor(_bar);
			if(win != null){
				win.setEnabled(true);
				Container container = _bar.getParent();
				_bar.getParent().remove(_bar);	
				container.repaint();
			}
		}
	}
}
