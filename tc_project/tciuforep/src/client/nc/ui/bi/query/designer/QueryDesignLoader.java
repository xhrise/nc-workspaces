package nc.ui.bi.query.designer;


/**
 * 客户端加载查询引擎界面的包装类。 创建日期：(2002-5-19 14:51:54)
 * 
 * @author：xd
 * 
 * 修改人：朱俊彬
 */
public class QueryDesignLoader {
	/**
	 * Loader 构造子注解。
	 */
	public QueryDesignLoader(javax.swing.JApplet applet,
			javax.swing.JPanel container, String fileBase, String servletURL) {
		super();
		//initLocalVIDCache(fileBase, servletURL);
		//com.ufsoft.iuforeport.reporttool.table.UfoRepToolApplet l2=new
		// com.ufsoft.iuforeport.reporttool.table.UfoRepToolApplet();
		QueryDesignApplet l2 = new QueryDesignApplet();

		//TODO
		/*
		 * l2.setStub(new nc.ui.iuforeport.rep.MyAppletStub(applet));
		 */

		container.removeAll();
		container.add(l2);
		l2.init();
		l2.start();
//		Dimension mSize = applet.getSize();
//		Dimension mChildSize = l2.getSize();
//		Dimension leftSize = new Dimension(mSize.width
//				- mChildSize.width, mSize.height - mChildSize.height);
		l2.setLocation(0, 0);
		container.setBackground(l2.getBackground());
		container.setForeground(l2.getForeground());
		container.revalidate();
	}
//
//	/**
//	 * 此处插入方法说明。 创建日期：(2002-6-27 20:58:52)
//	 * 
//	 * @param fileBase
//	 *            java.lang.String
//	 * @param servletURL
//	 *            java.lang.String
//	 */
//	private synchronized void initLocalVIDCache(String fileBase,
//			String servletURL) {
//
//		//TODO
//		/*
//		 * nc.ui.sm.synchronize1.LocalPackageVIDCache.getDefLocalPVIDCache()
//		 * .setFileBase(fileBase);
//		 * nc.ui.sm.synchronize1LocalPackageVIDCache.getDefLocalPVIDCache()
//		 * .initPackageVIDCache(servletURL);
//		 */
//
//	}
}
