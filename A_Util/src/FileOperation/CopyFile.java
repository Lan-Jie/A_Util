package FileOperation;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.Date;

/**
 * @author lan 2016年3月26日
 */
public class CopyFile {

	public static void main(String[] args) throws Exception {
		File f1 = new File("F:\\照片\\素材\\1.png");
		File f2 = new File("F:/testcopy_left/19.png");
//		 System.out.println("第一种方法：");
//		 CopyFile(f1,f2);
//		 System.out.println("**************");

//		System.out.println("第二种方法：");
//		fileChannelCopy(f1, f2);
//		System.out.println("**************");

//		System.out.println("/第三种方法：");
//		System.out.println(forTransfer(f1, f2));
//		System.out.println("**************");
//
//		System.out.println("第四种方法：");
//		System.out.println(forImage(f1, f2));
//		System.out.println("**************");
//
		System.out.println("第五种方法：");
		System.out.println(forChannel(f1, f2));
		f2.setLastModified(f1.lastModified());
		System.out.println("**************");
	}

	// 方法一**************************************
	public static void CopyFile(File f1, File f2) { // 将 f1的复制到f2上
		long m = 0;
		m = System.currentTimeMillis();
		int b = 0;
		System.out.println(f1.getName() + "是否存在 " + f1.exists());
		try {

			FileInputStream in = new FileInputStream(f1);
			FileOutputStream out = new FileOutputStream(f2);
			byte[] buffer = new byte[2000];
			while ((b = in.read(buffer)) != -1) {
				out.write(buffer, 0, b);
				// System.currentTimeMillis();

			}
			in.close();
			out.close();
			// System.out.println("复制成功");
		} catch (FileNotFoundException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		} catch (IOException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
		}
		System.out.println(System.currentTimeMillis() - m);
	}

	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf.txt
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf.txt 使用的是FileChannel
	 */
	// 方法二**************************************
	public static void fileChannelCopy(File s, File t) {
		long m = 0;
		m = System.currentTimeMillis();
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		try {

			fi = new FileInputStream(s);
			fo = new FileOutputStream(t);
			in = fi.getChannel();// 得到对应的文件通道
			out = fo.getChannel();// 得到对应的文件通道
			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
			System.out.println(s.getName()+"大小为"+in.size());
			System.out.println(t.getName()+"大小为"+in.size());
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {

				fi.close();
				in.close();
				fo.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println(t.length());
		System.out.println(System.currentTimeMillis() - m);
	}
	// /**
	// * 复制单个文件
	// * @param oldPath String 原文件路径 如：c:/fqf.txt
	// * @param newPath String 复制后路径 如：f:/fqf.txt
	// * @return boolean
	// */
	// public static void copyFile(String oldPath, String newPath) {
	// try {
	// int bytesum = 0;
	// int byteread = 0;
	// File oldfile = new File(oldPath);
	// if (oldfile.exists()) { //文件存在时
	// InputStream inStream = new FileInputStream(oldPath); //读入原文件
	// FileOutputStream fs = new FileOutputStream(newPath);
	// byte[] buffer = new byte[1444];
	// int length;
	// while ( (byteread = inStream.read(buffer)) != -1) {
	// bytesum += byteread; //字节数 文件大小
	// System.out.println(bytesum);
	// fs.write(buffer, 0, byteread);
	// }
	// inStream.close();
	// }
	// }
	// catch (Exception e) {
	// System.out.println("复制单个文件操作出错");
	// e.printStackTrace();
	//
	// }

	// }

	/*
	 * 方法的3参数分别是原始文件,和拷贝的目的文件.这里不做过多介绍.
	 * 
	 * 实现方法很简单,分别对2个文件构建输入输出流,并且使用一个字节数组作为我们内存的缓存器, 然后使用流从f1
	 * 中读出数据到缓存里,在将缓存数据写到f2里面去.这里的缓存是2MB的字节数组
	 * 
	 * 第3种方法:使用NIO中的管道到管道传输实现方法:在第一种实现方法基础上对输入输出流获得其管道,
	 * 然后分批次的从f1的管道中像f2的管道中输入数据每次输入的数据最大为2MB
	 */
	// 方法三**************************************
	public static long forTransfer(File f1, File f2) throws Exception {
		long time = new Date().getTime();
		int length = 2097152;
		FileInputStream in = new FileInputStream(f1);
		FileOutputStream out = new FileOutputStream(f2);
		FileChannel inC = in.getChannel();
		FileChannel outC = out.getChannel();
		int i = 0;
		while (true) {
			if (inC.position() == inC.size()) {
				inC.close();
				outC.close();
				return new Date().getTime() - time;
			}
			if ((inC.size() - inC.position()) < 20971520)
				length = (int) (inC.size() - inC.position());
			else
				length = 20971520;
			inC.transferTo(inC.position(), length, outC);
			inC.position(inC.position() + length);
			i++;
		}
	}

	// 方法四**************************************
	/*
	 * 方法4:内存文件景象写(读文件没有使用文件景象,有兴趣的可以回去试试,,我就不试了,估计会更快)
	 * 实现方法:跟伤2个例子不一样,这里写文件流没有使用管道而是使用内存文件映射(假设文件f2在内存中).
	 * 在循环中从f1的管道中读取数据到字节数组里,然后在像内存映射的f2文件中写数据.
	 */
	public static long forImage(File f1, File f2) throws Exception {
		long time = new Date().getTime();
		int length = 2097152;
		FileInputStream in = new FileInputStream(f1);
		RandomAccessFile out = new RandomAccessFile(f2, "rw");
		FileChannel inC = in.getChannel();
		MappedByteBuffer outC = null;
		MappedByteBuffer inbuffer = null;
		byte[] b = new byte[length];
		while (true) {
			if (inC.position() == inC.size()) {
				inC.close();
				outC.force();
				out.close();
				return new Date().getTime() - time;
			}
			if ((inC.size() - inC.position()) < length) {
				length = (int) (inC.size() - inC.position());
			} else {
				length = 2000;
			}
			b = new byte[length];
			inbuffer = inC.map(MapMode.READ_ONLY, inC.position(), length);
			inbuffer.load();
			inbuffer.get(b);
			outC = out.getChannel().map(MapMode.READ_WRITE, inC.position(), length);
			inC.position(b.length + inC.position());
			outC.put(b);
			outC.force();
		}
	}

	// 方法五**************************************
	// 第5种方法:管道对管道，这里实现方式与第3种实现方式很类似,不过没有使用内存影射
	public static long forChannel(File f1, File f2) throws Exception {
		long time = new Date().getTime();
		int length = 2097152;
		FileInputStream in = new FileInputStream(f1);
		FileOutputStream out = new FileOutputStream(f2);
		FileChannel inC = in.getChannel();
		FileChannel outC = out.getChannel();
		ByteBuffer b = null;
		while (true) {
			if (inC.position() == inC.size()) {
				inC.close();
				outC.close();
				return new Date().getTime() - time;
			}
			if ((inC.size() - inC.position()) < length) {
				length = (int) (inC.size() - inC.position());
			} else
				length = 2097152;
			b = ByteBuffer.allocateDirect(length);
			inC.read(b);
			b.flip();
			outC.write(b);
			outC.force(false);
		}
	}

	/**
	 * 复制整个文件夹内容
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf/ff
	 * @return boolean
	 */
	public void copyFolder(String oldPath, String newPath) {

		try {
			(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}

				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath + "/" + (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {// 如果是子文件夹
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		} catch (Exception e) {
			System.out.println("复制整个文件夹内容操作出错");
			e.printStackTrace();

		}

	}

}
