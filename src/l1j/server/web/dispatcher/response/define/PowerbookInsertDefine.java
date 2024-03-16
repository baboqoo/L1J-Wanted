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
import java.util.HashMap;

import l1j.server.server.utils.FileUtil;
import l1j.server.server.utils.StringUtil;
import l1j.server.web.PowerbookType;
import l1j.server.web.dispatcher.DispatcherModel;
import l1j.server.web.dispatcher.response.editor.FileExcutor;
import l1j.server.web.dispatcher.response.powerbook.L1Info;
import l1j.server.web.dispatcher.response.powerbook.L1InfoDAO;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

public class PowerbookInsertDefine extends HttpJsonModel {
	private FullHttpMessage msg;
	private HttpPostRequestDecoder decoder;
	
	public PowerbookInsertDefine() {}
	private PowerbookInsertDefine(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) {
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
				
				String title		= null;
				String content		= null;
				String mainImgFile	= null;
				while (decoder.hasNext()) {
					InterfaceHttpData data = decoder.next();
					if (data.getHttpDataType() == HttpDataType.Attribute) {
						attribute = (Attribute) data;
						switch(attribute.getName()) {
						case "title":
							title = attribute.getValue();
							break;
						case "content":
							content = attribute.getValue();
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
				
				L1InfoDAO dao			= L1InfoDAO.getInstance();
				int nextId				= dao.guideNextId();
				
				if (mainFileUpload != null) {
					File file = new File(uploadPath.toString() + StringUtil.SlushString + "POWERBOOK_" + nextId + StringUtil.UnderbarString + StringUtil.createUUID() + mainFileUpload.getFilename());
					try (FileChannel inputChannel		= new FileInputStream(mainFileUpload.getFile()).getChannel();
						FileChannel outputChannel		= new FileOutputStream(file).getChannel()) {
						outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
						inputChannel.close();
						outputChannel.close();
						mainImgFile			= FileExcutor.UPLOAD_STRING + StringUtil.SlushString + folderName + StringUtil.SlushString + file.getName();
						mainImgFile			= mainImgFile.replaceAll("\\\\", StringUtil.SlushString);
					}catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				HashMap<String, Object> data = new HashMap<String, Object>();
				data.put("id", nextId);
				data.put("title", title);
				data.put("content", content);
				data.put("mainImg", mainImgFile);
				data.put("mainIndex", 0);
				L1Info info = new L1Info(title, PowerbookType.GUIDE.toInt(), data, content);
				result = dao.guideInsert(info);
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
		return new PowerbookInsertDefine(request, msg, model);
	}
}

