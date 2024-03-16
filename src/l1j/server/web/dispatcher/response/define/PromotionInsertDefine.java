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
import l1j.server.web.dispatcher.response.promotion.PromotionDAO;
import l1j.server.web.dispatcher.response.promotion.PromotionVO;
import l1j.server.web.http.HttpJsonModel;
import l1j.server.web.http.HttpRequestModel;

public class PromotionInsertDefine extends HttpJsonModel {
	private FullHttpMessage msg;
	private HttpPostRequestDecoder decoder;
	
	public PromotionInsertDefine() {}
	private PromotionInsertDefine(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) {
		super(request, model);
		this.msg = msg;
	}

	@Override
	public HttpResponse get_response() throws Exception {
		boolean result		= false;
		String folerName	= FileUtil.getFoler();
		File uploadPath		= new File(FileExcutor.UPLOAD_PATH_STRING, folerName);
		if (!uploadPath.exists()) {
			uploadPath.mkdirs();
		}
		try {
			if (request.is_multipart()) {
				FileUpload fileUpload		= null;
				Attribute attribute			= null;
				
				FileUpload mainFileUpload	= null;
				FileUpload subFileUpload	= null;
				
				decoder = new HttpPostRequestDecoder(FILE_DATA_FACTORY, request);
				decoder.setDiscardThreshold(0);
				decoder.offer(msg);
				
				String dividPath		= null;
				String promoTitle		= null;
				String promoContent		= null;
				String promoDate		= null;
				String promoLink		= null;
				String promoImg			= null;
				String promoSubImg		= null;
				while (decoder.hasNext()) {
					InterfaceHttpData data = decoder.next();
					if (data.getHttpDataType() == HttpDataType.Attribute) {
						attribute = (Attribute) data;
						switch(attribute.getName()) {
						case "dividPath":
							dividPath = attribute.getValue();
							break;
						case "promoTitle":
							promoTitle = attribute.getValue();
							break;
						case "promoContent":
							promoContent = attribute.getValue();
							break;
						case "promoDate":
							promoDate = attribute.getValue();
							break;
						case "promoLink":
							promoLink = attribute.getValue();
							break;
						}
					} else {
						if (data.getHttpDataType() == HttpDataType.FileUpload) {
							fileUpload = (FileUpload) data;
							if (fileUpload.isCompleted()) {
								if (mainFileUpload == null) {
									mainFileUpload	= fileUpload;
								} else if(subFileUpload == null) {
									subFileUpload	= fileUpload;
								}
							}
						}
					}	
				}
				
				PromotionDAO dao		= PromotionDAO.getInstance();
				int nextId = dao.nextId();
				
				if (mainFileUpload != null) {
					File file = new File(uploadPath.toString() + StringUtil.SlushString + dividPath + nextId + StringUtil.UnderbarString + StringUtil.createUUID() + mainFileUpload.getFilename());
					try (FileChannel inputChannel		= new FileInputStream(mainFileUpload.getFile()).getChannel();
						FileChannel outputChannel		= new FileOutputStream(file).getChannel()) {
						outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
						inputChannel.close();
						outputChannel.close();
						promoImg			= FileExcutor.UPLOAD_STRING + StringUtil.SlushString + folerName + StringUtil.SlushString + file.getName();
						promoImg			= promoImg.replaceAll("\\\\", StringUtil.SlushString);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (subFileUpload != null) {
					File file = new File(uploadPath.toString() + StringUtil.SlushString + dividPath + nextId + StringUtil.UnderbarString + StringUtil.createUUID() + subFileUpload.getFilename());
					try (FileChannel inputChannel		= new FileInputStream(subFileUpload.getFile()).getChannel();
						FileChannel outputChannel		= new FileOutputStream(file).getChannel()) {
						outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
						inputChannel.close();
						outputChannel.close();
						promoSubImg			= FileExcutor.UPLOAD_STRING + StringUtil.SlushString + folerName + StringUtil.SlushString + file.getName();
						promoSubImg			= promoSubImg.replaceAll("\\\\", StringUtil.SlushString);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				PromotionVO vo			= new PromotionVO(nextId, promoTitle, promoContent, promoDate, promoLink, promoImg, promoSubImg);
				result					= dao.insertPromotion(vo);
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
		return new PromotionInsertDefine(request, msg, model);
	}
}

