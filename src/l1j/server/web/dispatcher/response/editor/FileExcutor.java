package l1j.server.web.dispatcher.response.editor;

import java.io.File;

import l1j.server.server.utils.StringUtil;

/**
 * 첨부파일
 * @author LinOffice
 */
public class FileExcutor {
	public static enum TempStatus {
		INSERT, UPDATE, DELETE, INSERT_CANCEL, UPDATE_CANCEL
	}
	
	public static final String UPLOAD_STRING		= "/upload";
	public static final String UPLOAD_PATH_STRING	= "./appcenter/upload";
	public static final String TEMP_STRING			= "_TEMP";
	
	public static void excute(String tempList, String content, TempStatus status) {
		String[] tempArray = tempList.split(StringUtil.CommaString);
		File tempfile		= null;
		String checkTemp	= null;
		for (String temp : tempArray) {
			if (StringUtil.isNullOrEmpty(temp)) {
				continue;
			}
			checkTemp		= temp.substring(temp.indexOf(UPLOAD_STRING)).replace(UPLOAD_STRING, UPLOAD_PATH_STRING);
			tempfile		= new File(checkTemp + TEMP_STRING);
			if (tempfile.exists()) {// 템프 파일 존재 여부
				if (status == TempStatus.INSERT || status == TempStatus.UPDATE) {
					tempfile.delete();// temp 삭제
				} else if (status == TempStatus.UPDATE_CANCEL) {
					if (content != null && content.contains(temp)) {
						String tempPath		= tempfile.getPath();
						String tempPrefix	= tempPath.substring(0, tempPath.indexOf(tempfile.getName()));
						File changeFile		= new File(tempPrefix, tempfile.getName().replaceAll(TEMP_STRING, StringUtil.EmptyString));
						tempfile.renameTo(changeFile);// 이름 변경
					} else {
						tempfile.delete();// temp 삭제
					}
				} else if (status == TempStatus.INSERT_CANCEL) {
					tempfile.delete();// temp 삭제
				}
			}
		}
	}
	
	public static void delete(String imgList) {
		imgList				= imgList.replaceAll(UPLOAD_STRING, UPLOAD_PATH_STRING);
		String[] imgArray 	= imgList.split(StringUtil.CommaString);
		File file			= null;
		for (String img : imgArray) {
			if (StringUtil.isNullOrEmpty(img)) {
				continue;
			}
			file			= new File(img);
			if (file.exists()) {
				file.delete();
			}
		}
	}
}

