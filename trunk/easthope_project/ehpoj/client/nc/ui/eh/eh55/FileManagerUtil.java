package nc.ui.eh.eh55;


import nc.bs.framework.common.NCLocator;
import nc.bs.logging.Logger;
import nc.itf.uap.busibean.IFileManager;
import nc.ui.pub.filemanager.FileUtil;
import nc.ui.sm.login.ClientAssistant;

/**
 * This is a file manager utility class. This class is intended to upload
 * client_file to server,and download server file to client. Server location
 * root and and client location root are pre_defined_directory. The file tree
 * struction of server files uploaded and client files downloaded are the same.
 * used to manage files.
 * 
 * ʱ�䣺2009��4��23��10:14:39
 * ������55�в�����
 * @author������
 */

public class FileManagerUtil {
	//
	private static String m_ncFileManageBase = null;

	//private java.applet.Applet applet = null;
	private static String serverDefaultDir = null;

	/**
	 * FileManager ������ע�⡣
	 */
	public FileManagerUtil() {
		super();
	}

	/**
	 * ɾ�������ļ��� �������ڣ�(2003-4-9 14:41:58)
	 * 
	 * @return java.lang.String @@ if delete success,return null;else return
	 *         error message
	 * @param filePathAbsolute
	 *            java.lang.String
	 */
	public static String deleteFileLocal(String filePathAbsolute) {
		return FileUtil.deleteFile(filePathAbsolute);
	}

	/**
	 * ɾ�������ļ��� �������ڣ�(2003-4-9 14:41:58)
	 * 
	 * @return java.lang.String @@ if delete success,return null;else return
	 *         error message
	 * @param dirLocal
	 *            java.lang.String
	 * @param fileName
	 *            java.lang.String
	 */
	public static String deleteFileLocal(String dirLocal, String fileName) {
		return deleteFileLocal(getDefaultDirLocal()
				+ FileUtil.convertFilePath(dirLocal)
				+ FileUtil.convertFilePath(fileName));
	}

	/**
	 * ɾ���������ļ��� �������ڣ�(2003-4-9 14:41:58)
	 * 
	 * @return java.lang.String @@ if delete success,return null;else return
	 *         error message
	 * @param dir
	 *            java.lang.String
	 * @param fileName
	 *            java.lang.String
	 */
	public static String deleteFileServer(String dir, String fileName) {

		try {
			IFileManager iIFileManager = (IFileManager) NCLocator.getInstance()
					.lookup(IFileManager.class.getName());
			return iIFileManager.deleteFile(FileUtil.convertFilePath(dir)
					+ FileUtil.convertFilePath(fileName));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nc.ui.ml.NCLangRes.getInstance().getStrByID("_beans",
				"UPP_uapcom0-000014")/* @res "ɾ����������" */;
	}

	/**
	 * return destop applet�� �������ڣ�(2003-4-15 9:39:09)
	 * 
	 * @return java.applet.Applet
	 */
	public static java.applet.Applet getApplet() {
		//if (applet == null)
		//applet =
	    return ClientAssistant.getApplet();
//		return nc.ui.sm.login.AppletContainer.getThis(); //nc.ui.pub.ClientEnvironment.getInstance().getDesktopApplet();

		//return applet;
	}

	/**
	 * return client filemanager defaultDir�� �������ڣ�(2003-4-10 12:45:40)
	 * 
	 * @return java.lang.String
	 */
	public static String getDefaultDirLocal() {
		if (m_ncFileManageBase == null) {
			//java.applet.Applet applet = getApplet();
			StringBuffer sb = new StringBuffer();
			sb.append(System.getProperty("user.home"));
			sb.append("\\NC_CODE\\FileManager");
			m_ncFileManageBase = sb.toString().replace('\\', '/');
			Logger.debug("��ȡ���ػ��������·���� " + m_ncFileManageBase);
		}
		return m_ncFileManageBase;
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-3-4 15:41:42)
	 * 
	 * @return java.lang.String like: /ncupload
	 */
	private static java.lang.String getDefaultDirServer() {
		try {
			if (serverDefaultDir == null) {
				IFileManager iIFileManager = (IFileManager) NCLocator
						.getInstance().lookup(IFileManager.class.getName());
				serverDefaultDir = iIFileManager.getDefaultDir();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return serverDefaultDir;
	}

	/**
	 * return client file names of directory given�� �������ڣ�(2003-4-9 14:41:58)
	 * 
	 * @return java.lang.String
	 * @param dir
	 *            java.lang.String
	 */
	public static String[] getDirFileNamesLocal(String dir) {
		return FileUtil.getAllFile(getDefaultDirLocal()
				+ FileUtil.convertFilePath(dir), FileUtil.FILE_FILE);
	}

	/**
	 * �õ����������ݺ�Ŀ¼�µ��ļ��б� �������ڣ�(2003-4-9 14:41:58)
	 * 
	 * @return java.lang.String[], not contains directorys
	 * @param dir
	 *            java.lang.String
	 */
	public static String[] getDirFileNamesServer(String dir) {

		try {
			IFileManager iIFileManager = (IFileManager) NCLocator.getInstance()
					.lookup(IFileManager.class.getName());
			return iIFileManager.getAllFile(FileUtil.convertFilePath(dir),
					FileUtil.FILE_FILE);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * get client file absolute path�� �������ڣ�(2003-4-9 14:41:58)
	 * 
	 * @return java.lang.String
	 * @param dir
	 *            java.lang.String
	 */
	public static String getFilePathAbsoluteLocal(String dir) {
		return getDefaultDirLocal() + FileUtil.convertFilePath(dir);
	}

	/**
	 * return client file absolute path�� �������ڣ�(2003-4-9 14:41:58)
	 * 
	 * @return java.lang.String
	 * @param dir
	 *            java.lang.String
	 * @param fileName
	 *            java.lang.String
	 */
	public static String getFilePathAbsoluteLocal(String dir, String fileName) {
		return getFilePathAbsoluteLocal(FileUtil.convertFilePath(dir)
				+ FileUtil.convertFilePath(fileName));
	}

	/**
	 * ȡ�ñ����ļ���URL�� �������ڣ�(2003-4-9 14:41:58)
	 * 
	 * @return java.net.URL, like "file:D:/filename.ext"
	 * @param fileAbsolutePath
	 *            java.lang.String
	 */
	public static java.net.URL getFileURLLocal(String fileAbsolutePath) {
		return FileUtil.getFileURL(fileAbsolutePath);
	}

	/**
	 * ȡ�ñ����ļ���URL�� �������ڣ�(2003-4-9 14:41:58)
	 * 
	 * @return java.lang.String
	 * @param dirLocal
	 *            java.lang.String
	 * @param fileName
	 *            java.lang.String
	 */
	public static java.net.URL getFileURLLocal(String dirLocal, String fileName) {
		return getFileURLLocal(getDefaultDirLocal()
				+ FileUtil.convertFilePath(dirLocal)
				+ FileUtil.convertFilePath(fileName));
	}

	/**
	 * �˴����뷽��˵���� �������ڣ�(2003-3-4 16:28:37)
	 * 
	 * @return java.lang.String
	 */
	private static java.net.URL getFileURLServer(String dir, String fileName) {

		dir = FileUtil.convertFilePath(dir);
		fileName = FileUtil.convertFilePath(fileName);

		fileName = dir + fileName;
		if (fileName == null)
			return null;

		fileName = getDefaultDirServer().substring(
				getDefaultDirServer().lastIndexOf("/"))
				+ fileName;

		java.applet.Applet desktop = getApplet();
		if (desktop == null)
			return null;

		//nc.ui.sm.cmenu.Desktop desktop = getApplet();
		// //nc.ui.sm.cmenu.Desktop.getApplet();

		//if (desktop == null)
		//return null;

		StringBuffer sb = new StringBuffer();

		sb.append(desktop.getParameter("SCHEME"));
		sb.append("://");
		sb.append(desktop.getParameter("SERVER_IP"));
		sb.append(":");
		sb.append(desktop.getParameter("SERVER_PORT"));
		sb.append(desktop.getParameter("CONTEXT_PATH"));
		//sb.append("/");
		sb.append(fileName);

		String path = sb.toString();
		Logger.debug(path);

		try {
			return new java.net.URL(path);
		} catch (Exception e) 
		{
			Logger.error("error",e);
		}

		return null;
	}

	/**
	 * �жϱ����Ƿ�����ļ��� �������ڣ�(2003-4-9 14:41:58)
	 * 
	 * @return java.lang.String
	 * @param dir
	 *            java.lang.String
	 * @param fileName
	 *            java.lang.String
	 */
	public static boolean isFileExistedLocal(String dir, String fileName) {
		return new java.io.File(getFilePathAbsoluteLocal(dir, fileName))
				.exists();
	}

	/**
	 * �жϷ������Ƿ�����ļ��� �������ڣ�(2003-4-9 14:41:58)
	 * 
	 * @return java.lang.String
	 * @param dir
	 *            java.lang.String
	 * @param fileName
	 *            java.lang.String
	 */
	public static boolean isFileExistedServer(String dir, String fileName) {

		try {
			IFileManager iIFileManager = (IFileManager) NCLocator.getInstance()
					.lookup(IFileManager.class.getName());
			return iIFileManager.isFileExists(dir + fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * ���ط������ļ������ء� �������ڣ�(2003-4-9 14:41:58)
	 * 
	 * @param dir
	 *            java.lang.String, server directory
	 * @param fileName
	 *            java.lang.String
	 */
	public static void saveFileToLocal(String dir, String fileName) {

		try {
			IFileManager iIFileManager = (IFileManager) NCLocator.getInstance()
					.lookup(IFileManager.class.getName());
			FileUtil.writeFile(getDefaultDirLocal()
					+ (dir = FileUtil.convertFilePath(dir))
					+ (fileName = FileUtil.convertFilePath(fileName)),
					iIFileManager.readFile(dir + fileName));

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * �ϴ������ļ����������� �������ڣ�(2003-4-9 14:41:58)
	 * 
	 * @param dirID
	 *            java.lang.String, server directory
	 * @param localFilePath
	 *            java.lang.String, file absolute path
	 */
	public static void saveFileToServer(String dir, String localFilePath) {
		try {
			IFileManager iIFileManager = (IFileManager) NCLocator.getInstance()
					.lookup(IFileManager.class.getName());
			iIFileManager.upLoadFile((dir = FileUtil.convertFilePath(dir))
					+ (localFilePath = FileUtil.convertFilePath(localFilePath))
							.substring(localFilePath.lastIndexOf("/")),
					FileUtil.readFile(localFilePath));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��ʾ�����ļ��� �������ڣ�(2003-4-9 14:41:58)
	 * 
	 * @return java.lang.String, if success return null, else return error
	 *         message
	 * @param dir
	 *            java.lang.String, client directory
	 * @param fileName
	 *            java.lang.String
	 */
	public static String showFileLocal(String dir, String fileName) {
		return showFileLocal(getFileURLLocal(dir, fileName));
	}

	/**
	 * ��ʾ�����ļ��� �������ڣ�(2003-4-9 14:41:58)
	 * 
	 * @return java.lang.String, if success return null, else return error
	 *         message
	 * @param url
	 *            java.net.URL, client file url
	 */
	public static String showFileLocal(java.net.URL url) {

		//URL
		try {
			getApplet().getAppletContext().showDocument(url, "_blank");
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Show FileLocal error.";
	}

	/**
	 * ��ʾ�����ļ��� �������ڣ�(2003-4-9 14:41:58)
	 * 
	 * @return java.lang.String, if success return null, else return error
	 *         message
	 * @param dir
	 *            java.lang.String, client directory
	 * @param fileName
	 *            java.lang.String
	 */
	public static void showFileServer(String dir, String fileName) {

		java.net.URL url = getFileURLServer(dir, fileName);
		if (url == null)
			return;

		Logger.debug("url:" + url.toString());
		//URL
		try {
			nc.ui.pub.ClientEnvironment.getInstance().getDesktopApplet()
					.getAppletContext().showDocument(url, "_blank");
		} catch (Exception e) {
			Logger.error("error",e);
		}
	}
}