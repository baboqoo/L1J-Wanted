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

public class PromotionUpdateDefine extends HttpJsonModel {
	private FullHttpMessage msg;
	private HttpPostRequestDecoder decoder;
	
	public PromotionUpdateDefine() {}
	private PromotionUpdateDefine(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) {
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
		String parent		= uploadPath.toString();
		try {
			if (request.is_multipart()) {
				FileUpload fileUpload		= null;
				Attribute attribute			= null;
				
				FileUpload mainFileUpload	= null;
				FileUpload subFileUpload	= null;
				
				decoder = new HttpPostRequestDecoder(FILE_DATA_FACTORY, request);
				decoder.setDiscardThreshold(0);
				decoder.offer(msg);
				
				String promoId			= null;
				String dividPath		= null;
				String promoTitle		= null;
				String promoContent		= null;
				String promoDate		= null;
				String promoLink		= null;
				String promoImg			= null;
				String promoSubImg		= null;
				String promoImgStr		= null;
				String promoSubImgStr	= null;
				while (decoder.hasNext()) {
					InterfaceHttpData data = decoder.next();
					if (data.getHttpDataType() == HttpDataType.Attribute) {
						attribute = (Attribute) data;
						switch(attribute.getName()) {
						case "promoId":
							promoId = attribute.getValue();
							break;
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
						case "promoImgStr":
							promoImgStr = attribute.getValue();
							break;
						case "promoSubImgStr":
							promoSubImgStr = attribute.getValue();
							break;
						}
					} else {
						if (data.getHttpDataType() == HttpDataType.FileUpload) {
							fileUpload = (FileUpload) data;
							if (fileUpload.isCompleted()) {
								switch(fileUpload.getName()) {
								case "promoImg":
									mainFileUpload	= fileUpload;
									break;
								case "promoSubImg":
									subFileUpload	= fileUpload;
									break;
								}
							}
						}
					}	
				}
				
				PromotionDAO dao		= PromotionDAO.getInstance();
				PromotionVO vo			= PromotionDAO.getPromotion(Integer.parseInt(promoId));
				
				if (mainFileUpload != null && mainFileUpload.getFile().length() > 0) {
					File file = new File(parent + StringUtil.SlushString + dividPath + promoId + StringUtil.UnderbarString + StringUtil.createUUID() + mainFileUpload.getFilename());
					try (FileChannel inputChannel		= new FileInputStream(mainFileUpload.getFile()).getChannel();
						FileChannel outputChannel		= new FileOutputStream(file).getChannel()) {
						outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
						inputChannel.close();
						outputChannel.close();
						promoImg			= FileExcutor.UPLOAD_STRING + StringUtil.SlushString + folderName + StringUtil.SlushString + file.getName();
						promoImg			= promoImg.replaceAll("\\\\", StringUtil.SlushString);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (subFileUpload != null && subFileUpload.getFile().length() > 0) {
					File file = new File(parent + StringUtil.SlushString + dividPath + promoId + StringUtil.UnderbarString + StringUtil.createUUID() + subFileUpload.getFilename());
					try (FileChannel inputChannel		= new FileInputStream(subFileUpload.getFile()).getChannel();
						FileChannel outputChannel		= new FileOutputStream(file).getChannel()) {
						outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
						inputChannel.close();
						outputChannel.close();
						promoSubImg			= FileExcutor.UPLOAD_STRING + StringUtil.SlushString + folderName + StringUtil.SlushString + file.getName();
						promoSubImg			= promoSubImg.replaceAll("\\\\", StringUtil.SlushString);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (promoImgStr == null || promoImg != null) {
					deleteFile(vo.getPromotionImg());
				}
				if (promoImg != null) {
					promoImg		= renameFile(parent, dividPath, folderName, vo.getId(), promoImg);
					vo.setPromotionImg(promoImg);
				}
				if (promoSubImgStr == null || promoSubImg != null) {
					deleteFile(vo.getListallImg());
				}
				if (promoSubImg != null) {
					promoSubImg	= renameFile(parent, dividPath, folderName, vo.getId(), promoSubImg);
					vo.setListallImg(promoSubImg);
				}
				
				vo.setTitle(promoTitle);
				vo.setSubText(promoContent);
				vo.setPromotionDate(promoDate);
				vo.setTargetLink(promoLink);
				result = dao.updatePromotion(vo);
				decoder.cleanFiles();
				return create_response_json(HttpResponseStatus.OK, result ? TRUE_JSON : FALSE_JSON);
			}
		} catch (Exception e) {
			e.printStackTrace();
			return create_response_json(HttpResponseStatus.OK, FALSE_JSON);
		}
		
		return create_response_json(HttpResponseStatus.OK, FALSE_JSON);
	}
	
	private void deleteFile(String str) {
		String img			= str;
		img					= img.substring(img.indexOf(FileExcutor.UPLOAD_STRING)).replace(FileExcutor.UPLOAD_STRING, FileExcutor.UPLOAD_PATH_STRING);
		File file			= new File(img);
		if(file.exists())file.delete();
	}
	
	private String renameFile(String parent, String dividPath, String folderName, int id, String str) {
		File oriFile		= new File(parent, str);
		File newFile		= new File(parent, dividPath + id + StringUtil.UnderbarString + StringUtil.createUUID() + str);
		oriFile.renameTo(newFile);
		str					= FileExcutor.UPLOAD_STRING + StringUtil.SlushString + folderName + StringUtil.SlushString + newFile.getName();
		return str.replaceAll("\\\\", StringUtil.SlushString);
	}

	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, DispatcherModel model) throws Exception {
		return null;
	}
	
	@Override
	public HttpJsonModel copyInstance(HttpRequestModel request, FullHttpMessage msg, DispatcherModel model) throws Exception {
		return new PromotionUpdateDefine(request, msg, model);
	}
}

