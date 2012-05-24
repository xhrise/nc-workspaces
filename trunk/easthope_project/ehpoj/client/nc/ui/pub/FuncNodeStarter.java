package nc.ui.pub;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.JOptionPane;

import nc.bs.uap.sf.facility.SFServiceFacility;
import nc.ui.ml.NCLangRes;
import nc.ui.pub.beans.ExtTabbedPane;
import nc.ui.pub.beans.MessageDialog;
import nc.ui.pub.linkoperate.ILinkAdd;
import nc.ui.pub.linkoperate.ILinkAddData;
import nc.ui.pub.linkoperate.ILinkApprove;
import nc.ui.pub.linkoperate.ILinkApproveData;
import nc.ui.pub.linkoperate.ILinkMaintain;
import nc.ui.pub.linkoperate.ILinkMaintainData;
import nc.ui.pub.linkoperate.ILinkQuery;
import nc.ui.pub.linkoperate.ILinkQueryData;
import nc.ui.pub.linkoperate.ILinkType;
import nc.ui.sm.cmenu.Desktop;
import nc.ui.sm.funcreg.FuncRegTree;
import nc.ui.sm.login.ClientAssistant;
import nc.vo.bd.CorpVO;
import nc.vo.logging.Debug;
import nc.vo.pub.BusinessException;
import nc.vo.pub.ValueObject;
import nc.vo.pub.lang.UFDate;
import nc.vo.pub.uniteremotecall.OpenMainFrameParaVO;
import nc.vo.pub.uniteremotecall.OpenMainFrameReturnedVO;
import nc.vo.sm.config.Account;
import nc.vo.sm.funcreg.FuncRegisterVO;
import nc.vo.sm.login.NCEnv;
import nc.vo.sm.user.UserPowerVO;

/**
 * 功能节点启动器,拥有打开一个节点的功能 创建日期:2006-2-13
 * 
 * @author licp
 * @since 5.0
 */
public class FuncNodeStarter {
    private static class Bag {
        public Bag(int intbusiOperate, Object openMsgVO, Component parent, boolean synOpen, int frameType, boolean modal) {
            this.busiOperate = intbusiOperate;
            this.openMsgVO = openMsgVO;
            this.parent = parent;
            this.synOpen = synOpen;
            this.frameType = frameType;
            this.modal = modal;
        }
        public Bag(int intbusiOperate, Object openMsgVO, Component parent, boolean synOpen, int frameType, boolean modal, LinkListener linkListener) {
            this.busiOperate = intbusiOperate;
            this.openMsgVO = openMsgVO;
            this.parent = parent;
            this.synOpen = synOpen;
            this.frameType = frameType;
            this.modal = modal;
            this.linkListener = linkListener;
        }
        // 打开节点类型(新增、维护、查询、审批)
        public int busiOperate = ILinkType.NONLINK_TYPE;

        // 打开节点传入的对象
        public Object openMsgVO = null;

        public Component parent = null;

        public boolean synOpen = false;

        public int frameType = TYPE_FRAME;

        public boolean modal = false;
        
        public LinkListener linkListener = null;

    }

    private FuncRegisterVO frVO = null;

    private OpenMainFrameReturnedVO retrVO = null;

    private static final int TYPE_FRAME = 0;

    private static final int TYPE_DIALOG = 1;

    private static final int TYPE_PANEL = 2;

    private static ArrayList al_opening = new ArrayList();

    /**
     * 
     */
    private FuncNodeStarter(FuncRegisterVO frVO) {
        super();
        this.frVO = frVO;
    }


    /**
     * 以Frame的方式打开一个功能节点
     * 
     * @param frVO
     *            功能节点注册vo
     * @param msg
     *            MessageVO
     * @param parent
     *            父组件
     * @param synOpen
     *            是否同步
     */
    public static void openFrame(FuncRegisterVO frVO, int intbusiOperate, Object msg, Component parent, boolean synOpen) {
        Bag bag = new Bag(intbusiOperate, msg, parent, synOpen, TYPE_FRAME, false);
        new FuncNodeStarter(frVO).openFuncNode(bag);
    }
    
    public static void openLinkedFrame(FuncRegisterVO frVO, int intbusiOperate, Object msg, Component parent, boolean synOpen, LinkListener linkListener) {
        Bag bag = new Bag(intbusiOperate, msg, parent, synOpen, TYPE_FRAME, false, linkListener);
        new FuncNodeStarter(frVO).openFuncNode(bag);
    }
    /**
     * 以Dialog的形式打开一个功能节点
     * 
     * @param frVO
     *            功能节点注册vo
     * @param msg
     *            MessageVO
     * @param parent
     *            父组件
     * @param synOpen
     *            是否同步
     */
    public static void openDialog(FuncRegisterVO frVO, int intbusiOperate, Object msg, Component parent, boolean synOpen, boolean modal) {
        Bag bag = new Bag(intbusiOperate, msg, parent, synOpen, TYPE_DIALOG, modal);
        new FuncNodeStarter(frVO).openFuncNode(bag);
    }
    public static void openLinkedDialog(FuncRegisterVO frVO, int intbusiOperate, Object msg, Component parent, boolean synOpen, boolean modal, LinkListener linkListener) {
        Bag bag = new Bag(intbusiOperate, msg, parent, synOpen, TYPE_DIALOG, modal, linkListener);
        new FuncNodeStarter(frVO).openFuncNode(bag);
    }

    
    public static void openNodeInTabbedPane(FuncRegisterVO frVO, Component waitComp, boolean synOpen) {
        // Container parent = Desktop.getApplet().getDesktopTabbedPane();
        Bag bag = new Bag(ILinkType.NONLINK_TYPE, null, waitComp, synOpen, TYPE_PANEL, false);
        new FuncNodeStarter(frVO).openFuncNode(bag);
    }
    public static void openNodeInTabbedPane(FuncRegisterVO frVO, Component waitComp, boolean synOpen, LinkListener linkListener) {
        // Container parent = Desktop.getApplet().getDesktopTabbedPane();
        Bag bag = new Bag(ILinkType.NONLINK_TYPE, null, waitComp, synOpen, TYPE_PANEL, false, linkListener);
        new FuncNodeStarter(frVO).openFuncNode(bag);
    }
   
    public static void openLinkedTabbedPane(FuncRegisterVO frVO,int intbusiOperate,Object msg, Component waitComp, boolean synOpen) {
        Bag bag = new Bag(intbusiOperate, msg, waitComp, synOpen, TYPE_PANEL, false);
        new FuncNodeStarter(frVO).openFuncNode(bag);
    }
    
    public static void openLinkedTabbedPane(FuncRegisterVO frVO,int intbusiOperate,Object msg,  Component waitComp, boolean synOpen, LinkListener linkListener) {
        // Container parent = Desktop.getApplet().getDesktopTabbedPane();
        Bag bag = new Bag(intbusiOperate, msg, waitComp, synOpen, TYPE_PANEL, false, linkListener);
        new FuncNodeStarter(frVO).openFuncNode(bag);
    }
    /**
     * 打开功能节点
     */
    private void openFuncNode(final Bag bag) {
        int intbusiOperate = bag.busiOperate;
        final Component compWait = bag.parent;
        boolean synOpen = bag.synOpen;
        String funCode = frVO.getFunCode();
        nc.ui.sm.cmenu.Desktop.getApplet().setBusy(true);
        if (compWait != null)
            compWait.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        //

        Integer funType = frVO.getFunProperty();
        if (funType != null && funType.intValue() == 5) {
            try {
                //
                String className = frVO.getClassName();
                Class moduleClass = Class.forName(className);
                java.lang.reflect.Method m = moduleClass.getMethod("main", new Class[] { String[].class });
                m.invoke(moduleClass, new Object[] { new String[] {} });
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (compWait != null)
                    compWait.setCursor(java.awt.Cursor.getDefaultCursor());
                nc.ui.sm.cmenu.Desktop.getApplet().setBusy(false);
            }
            return;
        }
        //

        switch (intbusiOperate) {
        case ILinkType.LINK_TYPE_APPROVE:
        case ILinkType.LINK_TYPE_QUERY: {
            bag.frameType = TYPE_DIALOG;
            bag.modal = true;
            try {
                getOpenMainFrameReturnedVO();
                openNode(bag, compWait, synOpen, true);
            } catch (Exception e) {
                e.printStackTrace();
                if (compWait != null)
                    compWait.setCursor(java.awt.Cursor.getDefaultCursor());
                nc.ui.sm.cmenu.Desktop.getApplet().setBusy(false);
            }

            break;
        }
        default: {
            boolean hasOpen = hasOpen();
            if (!hasOpen) {
                if (canOpen()) {
                    synchronized (FuncNodeStarter.class) {
                        if (al_opening.contains(funCode)) {
                            return;
                        } else {
                            al_opening.add(funCode);
                        }
                    }
                    // 在一个新的线程中显示预警文件
                    if (ClientEnvironment.getInstance().getUserType() == ClientEnvironment.USER) {
                        new Thread() {
                            @Override
							public void run() {
                                showAlterFiles();
                            }
                        }.start();
                    }
                    // 开始打开节点
                    openNode(bag, compWait, synOpen, false);
                } else {
                    if (compWait != null)
                        compWait.setCursor(java.awt.Cursor.getDefaultCursor());
                    nc.ui.sm.cmenu.Desktop.getApplet().setBusy(false);

                }
            } else {
                if (compWait != null)
                    compWait.setCursor(java.awt.Cursor.getDefaultCursor());
                nc.ui.sm.cmenu.Desktop.getApplet().setBusy(false);

            }
        }
        }

    }

    private void openNode(final Bag bag, final Component compWait, boolean synOpen, final boolean isOpenApproveType) {
        Runnable run = new Runnable() {
            public void run() {
                try {
                    openImpl(bag);
                } catch (Throwable th) {
                    th.printStackTrace();
                } finally {
                    if (!isOpenApproveType)
                        al_opening.remove(frVO.getFunCode());
                    if (compWait != null)
                        compWait.setCursor(java.awt.Cursor.getDefaultCursor());
                    nc.ui.sm.cmenu.Desktop.getApplet().setBusy(false);
                }
            }
        };
        if (synOpen) {
            run.run();
        } else {
            new Thread(run).start();
        }
    }

    private FramePanel openImpl(Bag bag) {
        int intbusiOperate = bag.busiOperate;
        Object msg = bag.openMsgVO;
        int frameType = bag.frameType;
        FramePanel fp = new FramePanel();
        fp.setLinkType(bag.busiOperate);
        fp.setHotKeyVOs(retrVO.getHotKeyRegisterVos());
        fp.getButtonBar().setAllUnionfunc(retrVO.getAllUnionfunc());
        fp.setVoPowerButton(retrVO.getVoPowerButton());
        fp.setFuncRegisterVO(frVO);
       
        try {
            ToftPanel tp = constructToftPanel(fp);
            if (tp != null) {
                try {
                    //注册toftpanel联动监听
                    if(bag.linkListener != null){
                        tp.addLinkListener(bag.linkListener);
                    }
                        
                    // 检查打开该节点的前提条件是否满足：
                    String retrMsg = tp.checkPrerequisite();
                    // 返回值不为null时，不再继续打开该节点
                    if (retrMsg != null) {
                        if (retrMsg.trim().equals("")) {
                            retrMsg = nc.ui.ml.NCLangRes.getInstance().getStrByID("smcomm", "UPP1005-000078")/*
                                                                                                              * @res "未知问题"
                                                                                                              */;
                        }
                        throw new PrerequisiteUnsatisfiedException(retrMsg);
                    }
                } catch (PrerequisiteUnsatisfiedException e) {
                    if (!e.getMessage().equalsIgnoreCase(IFuncWindow.OPEN_BPM_FRAME))
                        fp.clearServerEnv();
                    if (!e.getMessage().equalsIgnoreCase(IFuncWindow.DONTSHOWFRAME) && !e.getMessage().equalsIgnoreCase(IFuncWindow.OPEN_BPM_FRAME))
                        nc.ui.pub.beans.MessageDialog.showErrorDlg(nc.ui.sm.cmenu.Desktop.getApplet(), nc.ui.ml.NCLangRes.getInstance().getStrByID("smcomm", "UPP1005-000081")/*
                                                                                                                                                                               * @res "条件检验错误"
                                                                                                                                                                               */, e.getMessage());
                    return fp;
                }
                //
                //               将toftpanel加入到framepanel中
                fp.addClientPanel(tp);

                // 根据不同的打开方式进行节点打开
                String hint = null;
                switch (intbusiOperate) {
                case ILinkType.LINK_TYPE_ADD:
                    if (tp instanceof ILinkAdd) {
                        ((ILinkAdd) tp).doAddAction((ILinkAddData) msg);
                    } else {
                        hint = "This funnode:" + frVO.getFunCode() + " must implements ILinkAdd interface.";

                    }
                    break;
                case ILinkType.LINK_TYPE_APPROVE:
                    if (tp instanceof ILinkApprove) {
                        ((ILinkApprove) tp).doApproveAction((ILinkApproveData) msg);
                    } else {
                        hint = "This funnode:" + frVO.getFunCode() + " must implements ILinkApprove interface.";

                    }
                    break;
                case ILinkType.LINK_TYPE_MAINTAIN:
                    if (tp instanceof ILinkMaintain) {
                        ((ILinkMaintain) tp).doMaintainAction((ILinkMaintainData) msg);
                    } else {
                        hint = "This funnode:" + frVO.getFunCode() + " must implements ILinkMaintain interface.";

                    }
                    break;
                case ILinkType.LINK_TYPE_QUERY:
                    if (tp instanceof ILinkQuery) {
                        ((ILinkQuery) tp).doQueryAction((ILinkQueryData) msg);
                    } else {
                        hint = "This funnode:" + frVO.getFunCode() + " must implements ILinkQuery interface.";

                    }
                    break;
                default:
                    break;
                }
                if (hint != null) {
                    Debug.debug(hint);
                }
            }
        } catch (Exception e) {
            fp.clearServerEnv();
            e.printStackTrace();
        }

        String windowTitle = FuncRegTree.getFunTransStr(frVO.getFunCode(), frVO.getFunName());
        IFuncWindow window = null;
        if (frameType == TYPE_FRAME) {
            window = new FuncNodeFrame(windowTitle, fp);
        } else if (frameType == TYPE_DIALOG) {
            boolean modal = bag.modal;
            Frame owner = null;
            Component comp = bag.parent;
            if (comp != null && modal)
                owner = JOptionPane.getFrameForComponent(comp);
            window = new FuncNodeDialog(owner, windowTitle, modal, fp);
        } else if (frameType == TYPE_PANEL) {
            ExtTabbedPane tabbedPane = Desktop.getApplet().getDesktopTabbedPane();
            Component comp = bag.parent;
            if(comp instanceof ExtTabbedPane)
                tabbedPane = (ExtTabbedPane)comp;
            window = new FuncNodePanel(tabbedPane, fp);
        }
        fp.setParentFuncWindow(window);

        // 设置客户端环境：
        switch (intbusiOperate) {
        case ILinkType.LINK_TYPE_APPROVE:
        case ILinkType.LINK_TYPE_QUERY:
            break;
        default:
            ClientEnvironment.getInstance().getOpenModules().add(window);
        }

        window.showWindow();
        fp.checkButtonBar();
        fp.revalidate();
        return fp;
    }

    private ToftPanel constructToftPanel(FramePanel fp) throws Exception {
        ToftPanel tp = null;
        // 装入参数指定的模块界面
        String module = frVO.getClassName();
        // // 将参数指定的模块界面装入到界面主框架中
        Class moduleClass = null;
        try {
            moduleClass = Class.forName(module);
            java.lang.reflect.Constructor cs = null;
            // 带参构造方法：
            cs = moduleClass.getConstructor(new Class[] { FramePanel.class });
            tp = (ToftPanel) cs.newInstance(new Object[] { fp });
        } catch (NoSuchMethodException e) {
            tp = (ToftPanel) moduleClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (tp != null){
            tp.setFrame(fp);
        }
        return tp;
    }

    private void showAlterFiles() {
        String[] files = retrVO.getFiles();
        if (files != null) {
            System.out.println("==== Funcnode Alter Files ====");
            for (int i = 0; i < files.length; i++) {
                System.out.println(files[i]);
                try {
                    String str = "";
                    files[i] = files[i].replace('\\', '/');
                    StringTokenizer st = new StringTokenizer(files[i], "/");
                    while (st.hasMoreTokens()) {
                        str += "/" + java.net.URLEncoder.encode(st.nextToken(), "UTF-8");
                    }
                    ClientAssistant.getInstance();
					java.net.URL url = new java.net.URL(ClientAssistant.getServerURL() +"NCFindWeb?service=prealert&filename="+ str.substring(1));
                    ClientAssistant.showDocument(url, "_blank");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            System.out.println("======== End =========");
        } else {
            System.out.println("========No alert files!============");
        }
        //

    }

    /**
     * 检查该节点是否已经被打开了,如果已经打开了就将其显示并返回true,否则返回false;
     * 
     * @param funcCode
     * @return
     */
    private boolean hasOpen() {
        String funcCode = frVO.getFunCode();
        List openModules = ClientEnvironment.getInstance().getOpenModules();
        Iterator it = openModules.iterator();
        boolean isOpen = false;
        while (it.hasNext()) {
            // MainFrame openModule = (MainFrame) it.next();
            IFuncWindow window = (IFuncWindow) it.next();
            if (window.getFuncPanel().getModuleCode().equals(funcCode)) {
                window.showWindow();
                isOpen = true;
            }
        }
        return isOpen;
    }

    /**
     * 检查该节点是否允许被打开,返回true表示可以打开,返回false表示不允许打开
     * 
     * @param retrVO
     * @return
     */
    private boolean canOpen() {
        boolean canOpen = true;
        try {
            getOpenMainFrameReturnedVO();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        // 检查节点的互斥关系
        UserPowerVO voMutex = retrVO.getVOMutex();
        if (voMutex != null) {
            canOpen = false;
            MessageDialog.showHintDlg(Desktop.getApplet(), NCLangRes.getInstance().getStrByID("smcomm", "UPP1005-000083")/* "功能互斥" */, NCLangRes.getInstance().getStrByID("smcomm", "UPP1005-000084", null,
                    new String[] { FuncRegTree.getFunTransStr(frVO.getFunCode(), frVO.getFunName()), FuncRegTree.getFunTransStr(voMutex.getFunCode(), voMutex.getFunName()), voMutex.getUserId() }));
            // /*@res"您正在打开的功能\"{0}\"\n被功能\"{1}\"互斥\n(占用人:{2}"+
            // userPower.getFunName()+"\"\n被功能\""+voMutex.getFunName()+"\"互斥\n(占用人:"+voMutex.getUserId()+")");
        }
        // 检查产品授权数：
        if (NCEnv.isToControlProductLicense()) {
            /**
             * 写回0,表示可以打开 写回1,表示产品使用达到最大授权数 写回2，表示登录帐套为demo版，已经过了使用期限
             */
            int retr = 0;//retrVO.getCheckLienceResult();
            if (retr == 1) {
                MessageDialog.showErrorDlg(Desktop.getApplet(), NCLangRes.getInstance().getStrByID("smcomm", "UPP1005-000019")/*
                                                                                                                               * @res "错误"
                                                                                                                               */, NCLangRes.getInstance().getStrByID("smcomm", "UPP1005-000085")/* @res"该产品的用户数已达到产品授权数！" */);
                canOpen = false;
            } else if (retr == 2) {
                MessageDialog.showErrorDlg(Desktop.getApplet(), NCLangRes.getInstance().getStrByID("smcomm", "UPP1005-000019")/*
                                                                                                                               * @res "错误"
                                                                                                                               */, NCLangRes.getInstance().getStrByID("smcomm", "UPP1005-000086")/*
                                                                                    * @res "登录帐套的该产品为DEMO版并且已经过期！"
                                                                                    */);
                canOpen = false;
            }
        }
        return canOpen;
    }

    private void getOpenMainFrameReturnedVO() throws Exception {
        if (retrVO == null) {
            CorpVO corp = ClientEnvironment.getInstance().getCorporation();
            String pk_corp = null;
            if (corp != null) {
                pk_corp = corp.getPk_corp();
                frVO.setPkCorp(pk_corp);// 存储corpid为了服务端能够缓存公司
            }
            String userId = ClientEnvironment.getInstance().getUser().getPrimaryKey();
            String funcCode = frVO.getFunCode();
            String dsName = "";
            Account account = ClientEnvironment.getInstance().getConfigAccount();
            if (account != null) {
                dsName = account.getDataSourceName();
            }
            UFDate date = ClientEnvironment.getInstance().getBusinessDate();

            OpenMainFrameParaVO para = new OpenMainFrameParaVO();
            para.setUserId(userId);
            para.setDsName(dsName);
            para.setModuleCode(funcCode);
            para.setFuncRegisterVO(frVO);
            para.setIsNewFrame(true);
            para.setLog(Desktop.getApplet().getOperateLog(frVO));
            para.setPKCorp(pk_corp);
            para.setBusinessDate(date);
            //
            ValueObject vo = (ValueObject) ClientEnvironment.getInstance().getValue(ClientEnvironment.GLORGBOOKPK);
            String pkGlOrgBook = null;
            if (vo != null)
                try {
                    pkGlOrgBook = vo.getPrimaryKey();
                } catch (BusinessException e1) {
                    e1.printStackTrace();
                }
            para.setPkglorgbook(pkGlOrgBook);
            //
            try {
                retrVO = SFServiceFacility.getUnitRC().openMainFrame(para);
            } catch (Exception e) {
                e.printStackTrace();
                nc.ui.pub.beans.MessageDialog.showErrorDlg(nc.ui.sm.cmenu.Desktop.getApplet(), nc.ui.ml.NCLangRes.getInstance().getStrByID("smcomm", "UPP1005-000019")/*
                                                                                                                                                                       * @res "错误"
                                                                                                                                                                       */, nc.ui.ml.NCLangRes.getInstance().getStrByID("smcomm", "UPP1005-000082")/*
                                                                                             * @res "在打开节点调用UnitRCBO_Client.openMainFrame方法时发生错误！"
                                                                                             */);
                throw e;
            }
        }

    }
   
}