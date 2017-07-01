package clutter;

import java.io.File;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;


public class BeanUtil {

	// 公共部分
	private static final String RT_1 = "\r\n";
	private static final String RT_2 = RT_1 + RT_1;
	private static final String BLANK_1 = " ";
	private static final String BLANK_4 = "    ";
	private static final String BLANK_8 = BLANK_4 + BLANK_4;

	// 文件根目录字符串，如com.*
	// 文件项目名。如com.arrow.*
	private static final String ROOT = "com";
	private static final String PROJECT_NAME = "arrow";

	// 生成文件地址
	private static final String DAO_PATH = ROOT + "/" + PROJECT_NAME + "/dao";
	private static final String DAO_IMPL_PATH = ROOT + "/" + PROJECT_NAME + "/dao/impl";
	private static final String SERVICE_PATH = ROOT + "/" + PROJECT_NAME + "/service";
	private static final String SERVICE_IMPL_PATH = ROOT + "/" + PROJECT_NAME + "/service/impl";

	// 生成包名
	private static final String BEAN_URL = ROOT + "." + PROJECT_NAME + ".model";
	private static final String DAO_URL = ROOT + "." + PROJECT_NAME + ".dao";
	private static final String DAO_IMPL_URL = ROOT + "." + PROJECT_NAME + ".dao.impl";
	private static final String SERVICE_URL = ROOT + "." + PROJECT_NAME + ".service";
	private static final String SERVICE_IMPL_URL = ROOT + "." + PROJECT_NAME + ".service.impl";

	// 基本类文件夹PATH
	private static final String BASE_DAO_PATH = ROOT + "/" + PROJECT_NAME + "/base";
	private static final String BASE_DAO_IMPL_PATH = ROOT + "/" + PROJECT_NAME + "/base/impl";
	private static final String BASE_SERVICE_PATH = ROOT + "/" + PROJECT_NAME + "/base/service";
	private static final String BASE_SERVICE_IMPL_PATH = ROOT + "/" + PROJECT_NAME + "/base/service/impl";
	
	// 基本类文件夹URL               
	private static final String BASE_DAO_URL = ROOT + "." + PROJECT_NAME + ".base";
	private static final String BASE_DAO_IMPL_URL = ROOT + "." + PROJECT_NAME + ".base";
	private static final String BASE_SERVICE_URL = ROOT + "." + PROJECT_NAME + ".base.service";
	private static final String BASE_SERVICE_IMPL_URL = ROOT + "." + PROJECT_NAME + ".base.service.impl";
	
	
	// 基本类名称
	private static final String BASE_DAO_NAME = "IDaoSupport";
	private static final String ABSTRACT_BASE_DAO_IMPL_NAME = "DaoSupportImpl";
	private static final String BASE_SERVICE_NAME = "BaseService";
	private static final String ABSTRACT_BASE_SERVICE_IMPL_NAME = "BaseServiceImpl";
	// 基本类URL
	private static final String BASE_DAO_NAME_URL = BASE_DAO_URL + "." + BASE_DAO_NAME;
	private static final String ABSTRACT_BASE_DAO_IMPL_NAME_URL = BASE_DAO_IMPL_URL + "." + ABSTRACT_BASE_DAO_IMPL_NAME;
	private static final String BASE_SERVICE_NAME_URL = BASE_SERVICE_URL + "." + BASE_SERVICE_NAME;
	private static final String ABSTRACT_BASE_SERVICE_IMPL_NAME_URL = BASE_SERVICE_IMPL_URL + "." + ABSTRACT_BASE_SERVICE_IMPL_NAME;

	public static void main(String[] args) {
//		try {
//			BeanUtil bu = new BeanUtil(User.class);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	public BeanUtil(Class c) throws Exception {
		Initilize();
		createBeanDao(c);
		createBeanDaoImpl(c);
		createBeanService(c);
		createBeanServiceImpl(c);
		System.out.println(c.getName()+" 的dao/service模块 初始化完成");
	}

	public void Initilize() {
		// 判断文件夹是否存在，如果不存在则创建
		String projectPath = System.getProperty("user.dir");
		createFolder(projectPath + "\\" + "src\\" + DAO_PATH);
		createFolder(projectPath + "\\" + "src\\" + DAO_IMPL_PATH);
		createFolder(projectPath + "\\" + "src\\" + SERVICE_PATH);
		createFolder(projectPath + "\\" + "src\\" + SERVICE_IMPL_PATH);
		System.out.println("文件夹初始化完成");
	}

	// 创建文件夹
	public void createFolder(String path) {
		File fp = new File(path);
		// 创建目录
		if (!fp.exists()) {
			fp.mkdirs();// 目录不存在的情况下，创建目录。
			showInfo("创建了文件夹：" + path);
		}
	}

	/**
	 * 创建bean的Dao<br>
	 * 
	 * @param c
	 * @throws Exception
	 */
	public void createBeanDao(Class c) throws Exception {
		String cName = c.getName();
		String fileName = System.getProperty("user.dir") + "\\src\\" + DAO_PATH + "\\" + getLastChar(cName) + "Dao.java";
		File f = new File(fileName);
		System.out.println("文件名：" + fileName);
		f.createNewFile();
		FileWriter fw = new FileWriter(f);
		String packageStr = "package " + DAO_URL + ";";
		String importStr = "import "+BASE_DAO_NAME_URL+";"+RT_1+"import "+cName+";";
		fw.write(packageStr + RT_2 +importStr + RT_2+ "public interface " + getLastChar(cName) + "Dao extends " + BASE_DAO_NAME + " <"
				+ getLastChar(cName) + "> {" + RT_2 + "}");
		fw.flush();
		fw.close();
		showInfo(fileName);
	}

	/**
	 * 创建bean的Dao的实现类
	 * 
	 * @param c
	 * @throws Exception
	 */
	public void createBeanDaoImpl(Class c) throws Exception {
		String cName = c.getName();
		String fileName = System.getProperty("user.dir") + "/src/" + DAO_IMPL_PATH + "/" + getLastChar(cName) + "DaoImpl.java";
		File f = new File(fileName);
		FileWriter fw = new FileWriter(f);
		String packageStr = "package " + DAO_IMPL_URL + ";";
		String repositoryAnno = "@Repository";
		String importStr = "import org.springframework.stereotype.Repository; "+RT_1+
				"import "+ABSTRACT_BASE_DAO_IMPL_NAME_URL+";"+RT_1+"import "+DAO_URL+"."+getLastChar(cName) + "Dao;"+RT_1+"import "+BEAN_URL+"."+getLastChar(cName)+";";
		fw.write( packageStr+ RT_2 +importStr+RT_2+ repositoryAnno+RT_1+"public class " + getLastChar(cName) + "DaoImpl extends "
				+ ABSTRACT_BASE_DAO_IMPL_NAME + "<" + getLastChar(cName) + "> implements " + getLastChar(cName) + "Dao{" + RT_2
				+ "}");
		fw.flush();
		fw.close();
		showInfo(fileName);
	}

	/**
	 * 创建bean的service
	 * 
	 * @param c
	 * @throws Exception
	 */
	public void createBeanService(Class c) throws Exception {
		String cName = c.getName();
		String fileName = System.getProperty("user.dir") + "/src/" + SERVICE_PATH + "/" + getLastChar(cName) + "Service.java";
		File f = new File(fileName);
		FileWriter fw = new FileWriter(f);
		String packageStr ="package " + SERVICE_URL + ";" ;
		String importStr = "import "+DAO_URL+"."+getLastChar(cName)+"Dao;";
		fw.write(packageStr+ RT_2 + importStr+RT_2+"public interface " + getLastChar(cName) + "Service extends "
				+ DAO_URL+"."+getLastChar(cName)+"Dao"+ "{" + RT_2 + "}");
		fw.flush();
		fw.close();
		showInfo(fileName);
	}

	/**
	 * 创建bean的service的实现类
	 * 
	 * @param c
	 * @throws Exception
	 */
	public void createBeanServiceImpl(Class c) throws Exception {
		String cName = c.getName();
		String fileName = System.getProperty("user.dir") + "/src/" + SERVICE_IMPL_PATH + "/" + getLastChar(cName) + "ServiceImpl.java";
		File f = new File(fileName);
		FileWriter fw = new FileWriter(f);
		String packageStr ="package " + SERVICE_IMPL_URL + ";" ;
		String serviceAnno = "@Service(\""+getLowercaseChar(getLastChar(cName))+"Service\")";
		String importStr = "import org.springframework.stereotype.Service;"+RT_1+"import "+DAO_IMPL_URL+"."+getLastChar(cName) + "DaoImpl; "+RT_1+"import "+SERVICE_URL + "." + getLastChar(cName) + "Service;";
		fw.write(packageStr + RT_2 +importStr+RT_2+ serviceAnno+RT_1+"public class " + getLastChar(cName) + "ServiceImpl extends "
				 +getLastChar(cName) + "DaoImpl implements " +  getLastChar(cName) + "Service{"+RT_2+ "}");
		fw.flush();
		fw.close();
		showInfo(fileName);
	}

	/**
	 * 获取路径的最后面字符串<br>
	 * 
	 * @param str
	 * @return
	 */
	public String getLastChar(String str) {
		if ((str != null) && (str.length() > 0)) {
			int dot = str.lastIndexOf('.');
			if ((dot > -1) && (dot < (str.length() - 1))) {
				return str.substring(dot + 1);
			}
		}
		return str;
	}

	/**
	 * 把第一个字母变为小写<br>
	 * 如：<br>
	 * <code>str = "UserDao";</code><br>
	 * <code>return "userDao";</code>
	 * 
	 * @param str
	 * @return
	 */
	public String getLowercaseChar(String str) {
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}

	/**
	 * 显示信息
	 * 
	 * @param info
	 */
	public void showInfo(String info) {
		System.out.println("创建文件：" + info + "成功！");
	}

	/**
	 * 获取系统时间
	 * 
	 * @return
	 */
	public static String getDate() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		return simpleDateFormat.format(new Date());
	}
}
