package l1j.server.configure;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.utils.CharsetUtil;
import l1j.server.server.utils.StringUtil;

public class MessageConfigure {
	private static final Logger _log = Logger.getLogger(MessageConfigure.class.getName());
	private static final String MESSAGE_CONFIG_FILE	= "./config/message.properties";
	
	public String GM_WISPER_MESSAGE;
	public String GM_EMPTY_MESSAGE;
	public int SYSTEM_MESSAGE_INTERVAL;
	public String SYSTEM_MESSAGE_1;
	public String SYSTEM_MESSAGE_2;
	public String SYSTEM_MESSAGE_3;
	public String SYSTEM_MESSAGE_4;
	public String SYSTEM_MESSAGE_5;
	public String SYSTEM_MESSAGE_6;
	public String SYSTEM_MESSAGE_7;
	public String SYSTEM_MESSAGE_8;
	public String SYSTEM_MESSAGE_9;
	public String SYSTEM_MESSAGE_10;
	public String SYSTEM_MESSAGE_11;
	public String SYSTEM_MESSAGE_12;
	public String SYSTEM_MESSAGE_13;
	public String SYSTEM_MESSAGE_14;
	public String SYSTEM_MESSAGE_15;
	public String SYSTEM_MESSAGE_16;
	
	public void load(){
		try {
			Properties 	message = new Properties();
			InputStream is		= new FileInputStream(new File(MESSAGE_CONFIG_FILE));
			message.load(is);
			is.close();
			
			GM_WISPER_MESSAGE		= new String(message.getProperty("GM_WISPER_MESSAGE", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1_STR), CharsetUtil.EUC_KR_STR);
			GM_EMPTY_MESSAGE		= new String(message.getProperty("GM_EMPTY_MESSAGE", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1_STR), CharsetUtil.EUC_KR_STR);
			SYSTEM_MESSAGE_INTERVAL	= Integer.parseInt(message.getProperty("SYSTEM_MESSAGE_INTERVAL", "30"));
			SYSTEM_MESSAGE_1		= new String(message.getProperty("SYSTEM_MESSAGE_1", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1_STR), CharsetUtil.EUC_KR_STR);
			SYSTEM_MESSAGE_2		= new String(message.getProperty("SYSTEM_MESSAGE_2", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1_STR), CharsetUtil.EUC_KR_STR);
			SYSTEM_MESSAGE_3		= new String(message.getProperty("SYSTEM_MESSAGE_3", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1_STR), CharsetUtil.EUC_KR_STR);
			SYSTEM_MESSAGE_4		= new String(message.getProperty("SYSTEM_MESSAGE_4", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1_STR), CharsetUtil.EUC_KR_STR);
			SYSTEM_MESSAGE_5		= new String(message.getProperty("SYSTEM_MESSAGE_5", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1_STR), CharsetUtil.EUC_KR_STR);
			SYSTEM_MESSAGE_6		= new String(message.getProperty("SYSTEM_MESSAGE_6", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1_STR), CharsetUtil.EUC_KR_STR);
			SYSTEM_MESSAGE_7		= new String(message.getProperty("SYSTEM_MESSAGE_7", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1_STR), CharsetUtil.EUC_KR_STR);
			SYSTEM_MESSAGE_8		= new String(message.getProperty("SYSTEM_MESSAGE_8", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1_STR), CharsetUtil.EUC_KR_STR);
			SYSTEM_MESSAGE_9		= new String(message.getProperty("SYSTEM_MESSAGE_9", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1_STR), CharsetUtil.EUC_KR_STR);
			SYSTEM_MESSAGE_10		= new String(message.getProperty("SYSTEM_MESSAGE_10", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1_STR), CharsetUtil.EUC_KR_STR);
			SYSTEM_MESSAGE_11		= new String(message.getProperty("SYSTEM_MESSAGE_11", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1_STR), CharsetUtil.EUC_KR_STR);
			SYSTEM_MESSAGE_12		= new String(message.getProperty("SYSTEM_MESSAGE_12", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1_STR), CharsetUtil.EUC_KR_STR);
			SYSTEM_MESSAGE_13		= new String(message.getProperty("SYSTEM_MESSAGE_13", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1_STR), CharsetUtil.EUC_KR_STR);
			SYSTEM_MESSAGE_14		= new String(message.getProperty("SYSTEM_MESSAGE_14", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1_STR), CharsetUtil.EUC_KR_STR);
			SYSTEM_MESSAGE_15		= new String(message.getProperty("SYSTEM_MESSAGE_15", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1_STR), CharsetUtil.EUC_KR_STR);
			SYSTEM_MESSAGE_16		= new String(message.getProperty("SYSTEM_MESSAGE_16", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1_STR), CharsetUtil.EUC_KR_STR);
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new Error("Failed to Load " + MESSAGE_CONFIG_FILE + " File.");
		}
	}
}

