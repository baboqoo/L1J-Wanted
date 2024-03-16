package l1j.server.web.dispatcher.response.define;

import io.netty.handler.codec.http.FullHttpMessage;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.FileUpload;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.InterfaceHttpData.HttpDataType;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;

import l1j.server.server.utils.FileUtil;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.editor.FileExcutor;
import l1j.server.web.dispatcher.response.powerbook.L1Info;
import l1j.server.web.dispatcher.response.powerbook.L1InfoDAO;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

public class PowerbookUpdateDefine extends HttpJsonModel {
	private FullHttpMessage msg;
	private HttpPostRequestDecoder decoder;
	
	public PowerbookUpdateDefine() {}
	private PowerbookUpdateDefine(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) {
		super(request, model);
		this.msg = msg;
	}

	@Override
	public HttpResponse get_response() throws Exception {
		boolean result		= false;
		String folderName	= FileUtil.getFoler();
		File uploadPath		= new File(FileExcutor.UPLOAD_PATH_STRING, folderName);
		if (!uploadPath.exists()) {
			uploadPath.mkdirs();
		}
		try {
			if (request.is_multipart()) {
				FileUpload fileUpload		= null;
				Attribute attribute			= null;
				
				FileUpload mainFileUpload	= null;
				
				decoder = new HttpPostRequestDecoder(FILE_DATA_FACTORY, request);
				decoder.setDiscardThreshold(0);
				decoder.offer(msg);
				
				String id				= null;
				String title			= null;
				String content			= null;
				String oriFile			= null;
				String deleteFile		= null;
				while (decoder.hasNext()) {
					InterfaceHttpData data = decoder.next();
					if (data.getHttpDataType() == HttpDataType.Attribute) {
						attribute = (Attribute) data;
						switch(attribute.getName()) {
						case "id":
							id = attribute.getValue();
							break;
						case "title":
							title = attribute.getValue();
							break;
						case "content":
							content = attribute.getValue();
							break;
						case "oriFile":
							oriFile = attribute.getValue();
							break;
						case "deleteFile":
							deleteFile = attribute.getValue();
							break;
						}
					} else {
						if (data.getHttpDataType() == HttpDataType.FileUpload) {
							fileUpload = (FileUpload) data;
							if (fileUpload.isCompleted()) {
								if (fileUpload.getName().equals("uploadFile")) {
									mainFileUpload	= fileUpload;
								}
							}
						}
					}	
				}
				
				L1InfoDAO dao 			= L1InfoDAO.getInstance();
				L1Info info				= dao.getInfoGuide(4, Integer.parseInt(id));
				if (info == null) {
					return create_response_json(HttpResponseStatus.OK, FALSE_JSON);
				}
				
				if (mainFileUpload != null && mainFileUpload.getFile() != null && mainFileUpload.getFile().length() > 0) {
					String mainFileName = mainFileUpload.getFilename();
					File file = new File(uploadPath.toString() + StringUtil.SlushString + "POWERBOOK_" + id + StringUtil.UnderbarString + StringUtil.createUUID() + mainFileName);
					try (FileChannel inputChannel		= new FileInputStream(mainFileUpload.getFile()).getChannel();
						FileChannel outputChannel		= new FileOutputStream(file).getChannel()) {
						outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
						inputChannel.close();
						outputChannel.close();
						mainFileName		= FileExcutor.UPLOAD_STRING + StringUtil.SlushString + folderName + StringUtil.SlushString + file.getName();
						mainFileName		= mainFileName.replaceAll("\\\\", StringUtil.SlushString);
						info.getInfo().put("mainImg", mainFileName);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (!StringUtil.isNullOrEmpty(deleteFile) && StringUtil.isNullOrEmpty(oriFile)) {
					deleteFile		= deleteFile.substring(deleteFile.indexOf(FileExcutor.UPLOAD_STRING)).replace(FileExcutor.UPLOAD_STRING, FileExcutor.UPLOAD_PATH_STRING);
					File delfile	= new File(deleteFile);
					if (delfile.exists()) {
						delfile.delete();
					}
				}
				
				info.getInfo().put("title", title);
				info.getInfo().put("content", content);
				info.setName(title);
				info.setInfoText(content);
				result = dao.guideUpdate(info);
				decoder.cleanFiles();
				return create_response_json(HttpResponseStatus.OK, result ? TRUE_JSON : FALSE_JSON);
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
		return new PowerbookUpdateDefine(request, msg, model);
	}
}

