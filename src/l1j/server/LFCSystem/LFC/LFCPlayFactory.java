package l1j.server.LFCSystem.LFC;

import l1j.server.LFCSystem.LFC.Template.LFCObject;
import l1j.server.server.utils.StringUtil;

public class LFCPlayFactory {
	public static LFCObject create(String s){
		LFCObject obj = null;
		try {
			Class<?> cls = Class.forName(complementClassName(s));
			obj = (LFCObject)cls.newInstance();
		} catch(Exception e){
			e.printStackTrace();
		}	
		return obj;
	}
	
	private static String complementClassName(String className){
		if (className.contains(StringUtil.PeriodString)) {
			return className;
		}
		if (className.contains(StringUtil.CommaString)) {
			return className;
		}
		StringBuilder sb = new StringBuilder().append("l1j.server.LFCSystem.LFC.Template.").append(className);
		return sb.toString();
	}
}

