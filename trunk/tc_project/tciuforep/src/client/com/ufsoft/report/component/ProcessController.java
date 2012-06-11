package com.ufsoft.report.component;

import java.awt.Container;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

/**
 * ����Ҫ�ڱ���״̬����ʾ������ʱ����Ҫ���µ��߳���ִ�в�����
 * indeterminate Modeʱ�����߳��Ƿ��ǻ�����жϽ�����ǡ�
 * determinate Modeʱ����setValue��ֵ�������ֵ���жϽ�����־��
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
	 * ���߳�ִ�й����У�����determinate mode�Ľ�������������ʱ�޸Ľ���ֵ��
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
			//��Ҫ��֤bar�Ѿ��ŵ�������С�
			_bar = bar;
			_bar.setIndeterminate(_indeterminateMode);
			_bar.setString(_string);
			if(!_indeterminateMode){
				_bar.setMinimum(_minimum);
				_bar.setMaximum(_maximum);
			}
			//��ֹ���ڽ����û��¼���
			SwingUtilities.getWindowAncestor(_bar).setEnabled(false);
			_bar.setEnabled(true);
		}
		
		//�����߳�ֹͣ���Զ���ֹ��������ʾ��
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
