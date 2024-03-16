package l1j.server.web.dispatcher.response.define;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import l1j.server.server.utils.FileUtil;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.editor.FileExcutor;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

import com.google.gson.Gson;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;

public class FileUploadDefine extends HttpJsonModel {
	private FullHttpMessage msg;
	
	private HttpPostRequestDecoder decoder;
	
	public FileUploadDefine() {}
	private FileUploadDefine(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) {
		super(request, model);
		this.msg = msg;
	}

	@SuppressWarnings("resource")
	@Override
	public HttpResponse get_response() throws Exception {
		String folerName	= FileUtil.getFoler();
		File uploadPath		= new File(FileExcutor.UPLOAD_PATH_STRING, folerName);
		if (!uploadPath.exists()) {
			uploadPath.mkdirs();// 없으면 폴더 생성
		}
		try {
			if (request.is_multipart()) {
				FileUpload fileUpload = null;
				
				decoder = new HttpPostRequestDecoder(FILE_DATA_FACTORY, request);
				decoder.setDiscardThreshold(0);
				decoder.offer(msg);
				
				String dividPath = null;
				File oriFile = null;
				while (decoder.hasNext()) {
					InterfaceHttpData data = decoder.next();
					if (data.getHttpDataType() == HttpDataType.Attribute) {
						Attribute attribute = (Attribute) data;
						if (dividPath == null) {
							dividPath = attribute.getValue();
						}
					} else {
						if (data.getHttpDataType() == HttpDataType.FileUpload) {
							fileUpload = (FileUpload) data;
							if (fileUpload.isCompleted()) {
								if (oriFile == null) {
									oriFile = fileUpload.getFile();
								}
							}
						}
					}	
				}
				File file = new File(uploadPath.toString() + StringUtil.SlushString + dividPath + StringUtil.createUUID() + fileUpload.getFilename());
				try (FileChannel inputChannel		= new FileInputStream(fileUpload.getFile()).getChannel();
					FileChannel outputChannel		= new FileOutputStream(file).getChannel()) {
					outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
					inputChannel.close();
					outputChannel.close();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					decoder.cleanFiles();
				}
				String jsonString = new Gson().toJson(FileExcutor.UPLOAD_STRING + StringUtil.SlushString + folerName + StringUtil.SlushString + file.getName());
				return create_response_json(HttpResponseStatus.OK, jsonString);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return create_response_json(HttpResponseStatus.OK, FALSE_JSON);
		}
		
		return create_response_json(HttpResponseStatus.OK, FALSE_JSON);
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return null;
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return new FileUploadDefine(request, msg, model);
	}
}

