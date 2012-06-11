package com.ufsoft.report.sysplugin.keyassist;
import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JDialog;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import nc.ui.pub.beans.UIScrollPane;
import nc.ui.pub.beans.UITable;
import com.ufsoft.report.util.MultiLang;
import com.ufsoft.report.util.UfoPublic;

/**
 * �ؼ��ְ����Ի��򴰿�
 * 
 * @author xulm
 * 
 */
public class KeyAssistDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	//���
	private JTable table;
	//������
	private Container container;
	//������ݼ���ݼ���������
	private String[][] keyAssistContent;
    //���ѡ�е�������
	private int selectRow = -1;
	//�����յİ����ļ���
	private int inputKeycode=-1;
	// ����Ĭ�ϵĿ��
	private int dialogWidth = 280;
	// ����Ĭ�ϵĸ߶�
	private int dialogHeight = 360;

	/**
	 * ���캯��
	 * 
	 * @param container
	 *            ������
	 * @param keyAssistContent
	 *            �ؼ��ְ��������磺{ "����ȫ��(L)", "Ctrl+Shift+S" }
	 */
	public KeyAssistDialog(Container container, String[][] keyAssistContent) {
		super(javax.swing.JOptionPane.getFrameForComponent(container));
		this.container = javax.swing.JOptionPane.getFrameForComponent(container);
		this.keyAssistContent = keyAssistContent;
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

		this.setUndecorated(true);
		this.setModal(false);
				
		this.setLayout(new java.awt.BorderLayout());
		JRootPane jRootPane = new JRootPane();
		jRootPane.setBorder(new javax.swing.border.EtchedBorder());
		jRootPane.setLayout(new java.awt.BorderLayout());
		final JScrollPane jScrollPane = new UIScrollPane();
		table = new UITable();
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //��ʱ����UIͳһ��ʽ
//		table.setBackground(new Color(255, 248, 200));
//		table.setSelectionBackground(Color.BLUE);
//		table.setSelectionForeground(Color.WHITE);
		
		jScrollPane.setViewportView(table);
		jRootPane.add(jScrollPane, BorderLayout.CENTER);
		this.setRootPane(jRootPane);

		final FocusAdapter focusListener = new FocusAdapter() {
			public void focusLost(FocusEvent arg0) {
				doClose();
				return;
			}
		};
		table.addFocusListener(focusListener);
		
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() < 2) {
					return;
				}
				table.removeFocusListener(focusListener);
				selectRow = table.getSelectedRow();
				doClose();
				return;
			}
		});

		table.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent arg0) {
				// TODO Auto-generated method stub
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER && arg0.getModifiers()==0) {
					selectRow = table.getSelectedRow();
					if (selectRow!=-1)
					{
						table.removeFocusListener(focusListener);
						doClose();
						return;
					}else
					{
						table.setRowSelectionInterval(0, 0);
					}
				} else if (arg0.getKeyCode() == KeyEvent.VK_LEFT
						|| arg0.getKeyCode() == KeyEvent.VK_UP
						|| arg0.getKeyCode() == KeyEvent.VK_RIGHT
						|| arg0.getKeyCode() == KeyEvent.VK_DOWN) {

				} else {
					inputKeycode=arg0.getKeyCode() ;
					//�û�������֯���Ͽ�ݼ������Բ�Ӧ�ùرմ���
					if (inputKeycode==KeyEvent.VK_CONTROL||inputKeycode==KeyEvent.VK_ALT||inputKeycode==KeyEvent.VK_SHIFT)
					{
						inputKeycode=-1;
						return;
					}
					table.removeFocusListener(focusListener);
					doClose();
					return;
				}
			}
		});
		init(keyAssistContent);
		this.setBounds();
	}

	/**
	 * ��ʼ�����ģ��
	 * 
	 * @param keyAssistContent
	 */
	private void init(String[][] keyAssistContent) {
		DefaultTableModel tm = new DefaultTableModel(keyAssistContent,
				//miufo1000551=����	miufo1000552=��ݼ�
				new Object[] { MultiLang.getString("miufo1000551"), MultiLang.getString("miufo1000552")}) {

			private static final long serialVersionUID = 1L;

			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		table.setModel(tm);
	}

	/**
	 * �رշ���
	 */
	public void doClose() {
		this.dispose();
	}

	/**
	 * ����Ӧ��ģ��ļ��̵ļ���
	 * 
	 * @return
	 */
	public int[] getKeycodes() {
		int[] keycodes=null;
		//����Ǽ��̽��յļ���
		if(inputKeycode!=-1)
		{
			keycodes=new int[1];
			keycodes[0]=inputKeycode;
			return keycodes;
		}
		//�����˫��ѡ����
		else if (selectRow != -1 && selectRow < keyAssistContent.length) {
			String shortcuts= this.keyAssistContent[selectRow][1];
            if (shortcuts!=null && shortcuts.equals("")==false)
            {
            	String[] keys=shortcuts.split("\\+");
				if (keys.length>0)
				{
                    keycodes=new int [keys.length];
                    for(int i=0; i<keys.length;i++)
                    {
                    	keycodes[i]=KeyMappingKeyCode.getKeycode(keys[i].trim());
                    	if (keycodes[i]==-1)
                    	{
                    		//miufo1000553=�޷�ʶ��İ���
                    		UfoPublic.sendMessage(MultiLang.getString("miufo1000553"), container);
                    		return null;
                    	}
                    }
				}
            }
            return keycodes;
		} else
		{
			return null;
		}
	}

	/**
	 * ���ƴ�����ʾ��λ�úʹ�С
	 */
	public void setBounds() {

		int containerWidth = container.getSize().width;
		int containerHeight = container.getSize().height;

		while ((containerWidth) % 3 != 0) {
			containerWidth += 1;
		}
		if ((containerHeight) % 2 != 0) {
			containerHeight += 1;
		}
		if (containerWidth / 3 < dialogWidth) {
			dialogWidth = containerWidth / 3;
		}
		if (containerHeight / 2 < dialogHeight) {
			dialogHeight = containerHeight / 2;
		}
	    this.setSize(dialogWidth, dialogHeight);
	    setLocationRelativeTo(container);
	}
	
	
	/**
	 * ��������ڸ�������λ��
	 */
	public void setLocationRelativeTo(Component c) {

        Container root=null;

        if (c != null) {
            if (c instanceof Window || c instanceof Applet) {
               root = (Container)c;
            } else {
                Container parent;
                for(parent = c.getParent() ; parent != null ; parent = parent.getParent()) {
                    if (parent instanceof Window || parent instanceof Applet) {
                        root = parent;
                        break;
                    }
                }
            }
        }

        if((c != null && !c.isShowing()) || root == null ||
           !root.isShowing()) {
            Dimension         paneSize = getSize();
            Dimension         screenSize = getToolkit().getScreenSize();

            setLocation(screenSize.width - paneSize.width-5,
                        screenSize.height - paneSize.height-5);
        } else {
            Dimension invokerSize = c.getSize();
            Point invokerScreenLocation = c.getLocationOnScreen();

            Rectangle  windowBounds = getBounds();
            int        dx = invokerScreenLocation.x+(invokerSize.width-windowBounds.width-5);
            int        dy = invokerScreenLocation.y+(invokerSize.height - windowBounds.height-5);
            
            Rectangle ss = root.getGraphicsConfiguration().getBounds();

            // Adjust for bottom edge being offscreen
            if (dy+windowBounds.height>ss.height) {
                dy = ss.height-windowBounds.height;
            }

            // Avoid being placed off the edge of the screen
            if (dx+windowBounds.width > ss.x + ss.width) {
                dx = ss.x + ss.width - windowBounds.width;
            }
            if (dx < ss.x) dx = 0;
            if (dy < ss.y) dy = 0;

            setLocation(dx, dy);
        }
    }
}
