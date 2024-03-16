package l1j.server.web.dispatcher.response.define;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.editor.FileExcutor;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

public class FileDeleteDefine extends HttpJsonModel {
	public FileDeleteDefine() {}
	private FileDeleteDefine(HttpRequestModel request, DispatcherModel model) {
		super(request, model);
	}

	@Override
	public HttpResponse get_response() throws Exception {
		boolean result = false;
		String src	= request.get_post_datas().get("src");
		src			= src.substring(src.indexOf(FileExcutor.UPLOAD_STRING)).replace(FileExcutor.UPLOAD_STRING, FileExcutor.UPLOAD_PATH_STRING);
		File file	= new File(src);
		if (file.exists()) {
			File copyFile			= new File(src + FileExcutor.TEMP_STRING);
			FileInputStream fis		= new FileInputStream(file);// 파일의 내용을 읽어오기위한 준비
			FileOutputStream fos	= new FileOutputStream(copyFile);// 파일의 내용을 쓰기 위한 준비
			int nRealByte = 0;
			while ((nRealByte = fis.read()) != -1) {
				fos.write(nRealByte);// temp 생성
			}
			fis.close();
			fos.close();
			result				= file.delete();// 기존 삭제
		}
		return create_response_json(HttpResponseStatus.OK, result ? TRUE_JSON : FALSE_JSON);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return new FileDeleteDefine(request, model);
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return null;
	}

}

