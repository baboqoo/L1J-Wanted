package l1j.server.server.utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.sql.Date;
import java.text.SimpleDateFormat;

public class FileUtil {
	private static final String DATE_PATTERN	= "yyyy-MM-dd";
	
	/**
	 * 날짜별 폴더명 취득
	 * @return String
	 */
	public static String getFoler() {
		return new SimpleDateFormat(DATE_PATTERN).format(new Date(System.currentTimeMillis())).replace(StringUtil.MinusString, File.separator);
	}
	
	/**
	 * 경로 문자열에서 확장자를 찾아 반환한다.
	 * @param path 경로 문자열
	 * @return 찾아낸 확장자(.은 제거된다.)
	 **/
	public static String getExtension(File file) {
		return getExtension(file.getName());
	}
	
	public static String getNameWithoutExtension(File file) {
		String fileName = file.getName();
		int indexof = fileName.lastIndexOf(StringUtil.DirectoryExtensionChar);
		return indexof == -1 ? StringUtil.EmptyString : fileName.substring(0, indexof);
	}
	
	public static String getExtension(String path){
		if (StringUtil.isNullOrEmpty(path)) {
			return StringUtil.EmptyString;
		}
		int indexof = path.lastIndexOf(StringUtil.DirectoryExtensionChar);
		return indexof == -1 ? StringUtil.EmptyString : path.substring(indexof + 1, path.length());
	}
	
	/**
	 * 디렉터리가 없다면 생성한다.
	 * @param path 경로 문자열
	 **/
	public static void createDirectory(String path){
		File f = new File(path);
		if (!f.exists()) {
			f.mkdir();
		}
	}
	
	/**
	 * 디렉터리 목록을 반환한다.
	 * @param path 경로 문자열
	 * @return 디렉터리이름 배열을 반환한다. 만약 없다면 빈 문자열 배열을 반환한다.
	 **/
	public static String[] getDirectoriesItems(String path){
		File f = new File(path);
		return !f.exists() || !f.isDirectory() ? new String[]{} : f.list(); 
	}
	
	/**
	 * 경로에 해당하는 파일을 읽어서 byte배열로 반환한다.
	 * @param path
	 * @return byte[]
	 */
	public static byte[] readFileBytes(String path){
		byte[] buff				= null;
		BufferedInputStream bis	= null;
		FileInputStream fis		= null;
		File file				= null;
    	try {
			file		= new File(path);
			if (file.exists()) {
				fis		= new FileInputStream(file);
				bis		= new BufferedInputStream(fis);
				buff	= new byte[(int)fis.getChannel().size()];
				bis.read(buff, 0, buff.length);
			} else {
				System.out.println(String.format("[FileUtil] NOT_FOUND_FILE : PATH(%s)", path));
			}
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bis != null) {
					bis.close();
					bis = null;
				}
				if (fis != null) {
					fis.close();
					fis = null;
				}
				file = null;
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
    	return buff;
    }
	
	/**
	 * 파일 데이터를 읽는다.(동기방식.)
	 * 간편한 파일 리딩 위주로 사용할 것을 권장(200mb이내)
	 * 소규모 파일에 적합하게 튜닝된 메서드이므로 힙버퍼를 사용한다.
	 * 큰 파일에서 예외가 발생할 수 있다.
	 * 대용량에는 java.nio.file.Files 사용할 것.
	 * @param path 경로 문자열
	 * @return 읽어들인 바이트 배열
	 **/
	public static byte[] readAllBytes(String path){
		byte[] buff = null;
		File f = new File(path);
		if (!f.exists()) {
			return null;
		}
		int length = (int)f.length();
		try (RandomAccessFile raf = new RandomAccessFile(path, "r");
				FileChannel channel = raf.getChannel()) {
			
			ByteBuffer buffer = ByteBuffer.allocate(length);
			buffer.clear();
			raf.seek(0);
			channel.read(buffer);
			buff = buffer.array();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return buff;
	}
	
	/**
	 * 파일 데이터를 문자열(UTF8)로 읽는다.(동기방식.)
	 * 간편한 파일 리딩 위주로 사용할 것을 권장(200mb이내)
	 * 소규모 파일에 적합하게 튜닝된 메서드이므로 힙버퍼를 사용한다.
	 * 큰 파일에서 예외가 발생할 수 있다.
	 * 대용량에는 java.nio.file.Files 사용할 것.
	 * @param path 경로 문자열
	 * @return 읽어들인 문자열
	 **/
	public static String readAllText(String path){
		return readAllText(path, CharsetUtil.UTF_8);
	}
	
	/**
	 * 파일 데이터를 문자열로 읽는다.(동기방식.)
	 * 간편한 파일 리딩 위주로 사용할 것을 권장(200mb이내)
	 * 소규모 파일에 적합하게 튜닝된 메서드이므로 힙버퍼를 사용한다.
	 * 큰 파일에서 예외가 발생할 수 있다.
	 * 대용량에는 java.nio.file.Files 사용할 것.
	 * @param path 경로 문자열
	 * @param charSet 파일 인코딩
	 * @return 읽어들인 문자열
	 **/
	public static String readAllText(String path, Charset charSet){
		byte[] buff = readAllBytes(path);
		if (buff == null) {
			return StringUtil.EmptyString;
		}
		return new String(buff, charSet);
	}
	
	/**
	 * {@link WriteType#OVERWRITE}
	 * {@link WriteType#APPEND}
	 **/
	public enum WriteType{
		/**
		 * 파일을 처음부터 새로 쓴다.
		 **/
		OVERWRITE,
		
		/**
		 * 파일을 이어쓴다.
		 **/
		APPEND
	};
	
	/**
	 * 파일에 데이터를 쓴다.(동기방식.)
	 * 간편한 파일 쓰기 위주로 사용할 것을 권장(200mb이내)
	 * 소규모 파일에 적합하게 튜닝된 메서드
	 * 큰 파일에서 예외가 발생할 수 있다.
	 * 대용량에는 java.nio.file.Files 사용할 것.
	 * @param path 경로 문자열
	 * @param buff 파일에 쓸 버퍼
	 * @param writeType {@link WriteType}
	 **/
	public static void writeAllBytes(String path, byte[] buff, WriteType writeType){
		ByteBuffer buffer = ByteBuffer.wrap(buff);
		writeAllBytes(path, buffer, writeType);
	}
	
	/**
	 * 파일에 데이터를 쓴다.(동기방식.)
	 * 간편한 파일 쓰기 위주로 사용할 것을 권장(200mb이내)
	 * 소규모 파일에 적합하게 튜닝된 메서드
	 * 큰 파일에서 예외가 발생할 수 있다.
	 * 대용량에는 java.nio.file.Files 사용할 것.
	 * ByteBuffer 재사용 용도
	 * @param path 경로 문자열
	 * @param buffer 파일에 쓸 버퍼
	 * @param writeType {@link WriteType}
	 **/
	public static void writeAllBytes(String path, ByteBuffer buffer, WriteType writeType){
		try (RandomAccessFile raf = new RandomAccessFile(path, "rw");
				FileChannel channel = raf.getChannel()) {
			
			long rafLength = raf.length();
			if (rafLength > 0) {
				if (writeType == WriteType.OVERWRITE) {
					raf.seek(0);
					raf.setLength(0);
				} else {
					raf.seek(rafLength);
				}
			}
			buffer.flip();
			channel.write(buffer);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 파일에 텍스트를 쓴다.(동기방식.)
	 * 간편한 파일 쓰기 위주로 사용할 것을 권장(200mb이내)
	 * 소규모 파일에 적합하게 튜닝된 메서드
	 * 큰 파일에서 예외가 발생할 수 있다.
	 * 대용량에는 java.nio.file.Files 사용할 것.
	 * @param path 경로 문자열
	 * @param data 파일에 쓸 텍스트
	 * @param MJEncoding 저장 인코딩
	 * @param writeType {@link WriteType}
	 **/
	public static void writeAllText(String path, String data, Charset MJEncoding, WriteType writeType){
		byte[] buff = data.getBytes(MJEncoding);
		writeAllBytes(path, buff, writeType);
	}
	
	/**
	 * 파일에 텍스트를 쓴다.(동기방식.)
	 * UTF8
	 * 간편한 파일 쓰기 위주로 사용할 것을 권장(200mb이내)
	 * 소규모 파일에 적합하게 튜닝된 메서드
	 * 큰 파일에서 예외가 발생할 수 있다.
	 * 대용량에는 java.nio.file.Files 사용할 것.
	 * @param path 경로 문자열
	 * @param data 파일에 쓸 텍스트
	 * @param writeType {@link WriteType}
	 **/
	public static void writeAllText(String path, String data, WriteType writeType){
		writeAllText(path, data, CharsetUtil.UTF_8, writeType);
	}
	
	/**
	 * 파일을 생성한다.
	 * @param buff
	 * @param fileName
	 */
	public static void createFile(byte[] buff, String fileName){
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(fileName);
			fos.write(buff, 0, buff.length);// 파일 생성
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			fos = null;
		}
	}
	
	/**
	 * 파일을 HexPacket 형태로 생성한다.
	 * @param buff
	 * @param fileName
	 */
	public static void createHexPacketFile(byte[] buff, String fileName){
		String str = HexHelper.DataToPacket(buff, buff.length);
		createFile(str.getBytes(), fileName);
	}
}

