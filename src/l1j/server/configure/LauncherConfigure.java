package l1j.server.configure;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.utils.CharsetUtil;
import l1j.server.server.utils.StringUtil;

public class LauncherConfigure {
	private static final Logger _log = Logger.getLogger(LauncherConfigure.class.getName());
	private static final String CONNECTOR_CONFIG_FILE	= "./config/connector.properties";

	public String CONNECTOR_ENCRYPT_KEY;
	public String CONNECTOR_SESSION_KEY;
	public int CONNECTOR_CLIENT_SIDE_KEY;
	public int CONNECTOR_DLL_PASSWORD;
	
	
	public boolean CONNECTOR_CHROM_BROWSER_USE;
	public boolean CONNECTOR_CHROM_CACHE_USE;
	public int CONNECTOR_CHROM_DEBUG_PORT;
	public String CONNECTOR_BROWSER_URL;
	public String CONNECTOR_HOMPAGE_URL;
	
	public String CONNECTOR_LINBIN_PATH;
	public int CONNECTOR_LINBIN_SIZE;
	public String CONNECTOR_PATCH_PATH;
	public String CONNECTOR_MSDLL_PATH;
	public int CONNECTOR_MSDLL_SIZE;
	public String CONNECTOR_LIBCOCOS_PATH;
	public int CONNECTOR_LIBCOCOS_SIZE;
	public String CONNECTOR_BOXDLL_PATH;
	public String CONNECTOR_LOADER_PATH;
	public String CONNECTOR_CRAFT_PATH;
	public String CONNECTOR_ENGINE_NAMES;
	public int CONNECTOR_CLIENT_MAX_COUNT;
	public boolean CONNECTOR_LOG;
	public boolean CONNECTOR_PATCH_MERGE;
	public boolean CONNECTOR_PROCESS_MERGE;
	public String CONNECTOR_CREATE_URI;
	public String CONNECTOR_LOGIN_URI;
	public String CONNECTOR_LOGIN_MERGE_URI;
	public String CONNECTOR_ENGINE_URI;
	public String CONNECTOR_PROCESS_URI;
	public int CONNECTOR_PATCH_VERSION;
	public int CONNECTOR_PROCESS_INTERVAL;
	
	public void load(){
		try {
			Properties 	settings 		= new Properties();
			InputStream is				= new FileInputStream(new File(CONNECTOR_CONFIG_FILE));
			settings.load(is);
			is.close();
			
			CONNECTOR_ENCRYPT_KEY		= new String(settings.getProperty("CONNECTOR_ENCRYPT_KEY", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1_STR), CharsetUtil.EUC_KR_STR);
			CONNECTOR_SESSION_KEY		= new String(settings.getProperty("CONNECTOR_SESSION_KEY", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1_STR), CharsetUtil.EUC_KR_STR);
			CONNECTOR_CLIENT_SIDE_KEY	= Integer.parseInt(settings.getProperty("CONNECTOR_CLIENT_SIDE_KEY", StringUtil.ZeroString));
			CONNECTOR_DLL_PASSWORD		= Integer.parseInt(settings.getProperty("CONNECTOR_DLL_PASSWORD", StringUtil.ZeroString));
			CONNECTOR_CHROM_BROWSER_USE	= Boolean.parseBoolean(settings.getProperty("CONNECTOR_CHROM_BROWSER_USE", StringUtil.FalseString));
			CONNECTOR_CHROM_CACHE_USE	= Boolean.parseBoolean(settings.getProperty("CONNECTOR_CHROM_CACHE_USE", StringUtil.FalseString));
			CONNECTOR_CHROM_DEBUG_PORT	= Integer.parseInt(settings.getProperty("CONNECTOR_CHROM_DEBUG_PORT", StringUtil.ZeroString));
			CONNECTOR_BROWSER_URL		= new String(settings.getProperty("CONNECTOR_BROWSER_URL", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1), CharsetUtil.EUC_KR);
			CONNECTOR_HOMPAGE_URL		= new String(settings.getProperty("CONNECTOR_HOMPAGE_URL", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1), CharsetUtil.EUC_KR);
			CONNECTOR_LINBIN_PATH		= new String(settings.getProperty("CONNECTOR_LINBIN_PATH", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1), CharsetUtil.EUC_KR);
			CONNECTOR_LINBIN_SIZE		= Integer.parseInt(settings.getProperty("CONNECTOR_LINBIN_SIZE", StringUtil.ZeroString));
			CONNECTOR_PATCH_PATH		= new String(settings.getProperty("CONNECTOR_PATCH_PATH", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1), CharsetUtil.EUC_KR);
			CONNECTOR_MSDLL_PATH		= new String(settings.getProperty("CONNECTOR_MSDLL_PATH", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1), CharsetUtil.EUC_KR);
			CONNECTOR_MSDLL_SIZE		= Integer.parseInt(settings.getProperty("CONNECTOR_MSDLL_SIZE", StringUtil.ZeroString));
			CONNECTOR_LIBCOCOS_PATH		= new String(settings.getProperty("CONNECTOR_LIBCOCOS_PATH", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1), CharsetUtil.EUC_KR);
			CONNECTOR_LIBCOCOS_SIZE		= Integer.parseInt(settings.getProperty("CONNECTOR_LIBCOCOS_SIZE", StringUtil.ZeroString));
			CONNECTOR_BOXDLL_PATH		= new String(settings.getProperty("CONNECTOR_BOXDLL_PATH", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1), CharsetUtil.EUC_KR);
			CONNECTOR_LOADER_PATH		= new String(settings.getProperty("CONNECTOR_LOADER_PATH", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1), CharsetUtil.EUC_KR);
			CONNECTOR_CRAFT_PATH		= new String(settings.getProperty("CONNECTOR_CRAFT_PATH", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1), CharsetUtil.EUC_KR);
			CONNECTOR_ENGINE_NAMES		= new String(settings.getProperty("CONNECTOR_ENGINE_NAMES", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1), CharsetUtil.EUC_KR);
			CONNECTOR_CLIENT_MAX_COUNT	= Integer.parseInt(settings.getProperty("CONNECTOR_CLIENT_MAX_COUNT", StringUtil.ZeroString));
			CONNECTOR_LOG				= Boolean.parseBoolean(settings.getProperty("CONNECTOR_LOG", StringUtil.FalseString));
			CONNECTOR_PATCH_MERGE		= Boolean.parseBoolean(settings.getProperty("CONNECTOR_PATCH_MERGE", StringUtil.FalseString));
			CONNECTOR_PROCESS_MERGE		= Boolean.parseBoolean(settings.getProperty("CONNECTOR_PROCESS_MERGE", StringUtil.FalseString));
			CONNECTOR_PROCESS_INTERVAL	= Integer.parseInt(settings.getProperty("CONNECTOR_PROCESS_INTERVAL", "10"));
			CONNECTOR_CREATE_URI		= new String(settings.getProperty("CONNECTOR_CREATE_URI", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1), CharsetUtil.EUC_KR);
			CONNECTOR_LOGIN_URI			= new String(settings.getProperty("CONNECTOR_LOGIN_URI", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1), CharsetUtil.EUC_KR);
			CONNECTOR_LOGIN_MERGE_URI	= new String(settings.getProperty("CONNECTOR_LOGIN_MERGE_URI", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1), CharsetUtil.EUC_KR);
			CONNECTOR_ENGINE_URI		= new String(settings.getProperty("CONNECTOR_ENGINE_URI", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1), CharsetUtil.EUC_KR);
			CONNECTOR_PROCESS_URI		= new String(settings.getProperty("CONNECTOR_PROCESS_URI", StringUtil.EmptyString).getBytes(CharsetUtil.ISO_8859_1), CharsetUtil.EUC_KR);
			CONNECTOR_PATCH_VERSION		= Integer.parseInt(settings.getProperty("CONNECTOR_PATCH_VERSION", StringUtil.ZeroString));
		} catch(Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new Error("Failed to Load " + CONNECTOR_CONFIG_FILE + " File.");
		}
	}
}

