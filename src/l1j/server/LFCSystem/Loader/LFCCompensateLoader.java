package l1j.server.LFCSystem.Loader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import l1j.server.L1DatabaseFactory;
import l1j.server.LFCSystem.LFC.LFCType;
import l1j.server.LFCSystem.LFC.Compensate.LFCCompensate;
import l1j.server.LFCSystem.LFC.Compensate.LFCExpCompensate;
import l1j.server.LFCSystem.LFC.Compensate.LFCItemCompensate;
import l1j.server.server.utils.SQLUtil;

public class LFCCompensateLoader {
	private static LFCCompensateLoader _instance;
	public static LFCCompensateLoader getInstance(){
		if (_instance == null)
			_instance = new LFCCompensateLoader();
		return _instance;
	}
	
	public static void reload(){
		LFCCompensateLoader tmp = _instance;
		_instance = new LFCCompensateLoader();
		if (tmp != null){
			tmp.clear();
			tmp = null;
		}
	}
	
	public static void release(){
		if (_instance != null){
			_instance.clear();
			_instance = null;
		}
	}
	
	private LFCCompensateLoader(){
		Connection 			con 		= null;
		PreparedStatement 	pstm 		= null;
		ResultSet 			rs 			= null;
		LFCType			type		= null;
		LFCCompensate		compensate 	= null;
		String				field		= null;
		String				sType		= null;
		int					id			= 0;
		try {
			con 	= L1DatabaseFactory.getInstance().getConnection();
			pstm 	= con.prepareStatement("select * from TB_LFCCOMPENSATE");
			rs 		= pstm.executeQuery();
			while(rs.next()){
				field = "LFCID";
				id = rs.getInt(field);
				type = LFCTypeLoader.getInstance().get(id);
				if (type == null)
					continue;
				
				field = "TYPE";
				sType = rs.getString(field);
				if (sType == null)
					continue;
				else if (sType.equalsIgnoreCase("exp"))
					compensate = new LFCExpCompensate();
				else if (sType.equalsIgnoreCase("item"))
					compensate = new LFCItemCompensate();
				else continue;
				
				field = "PARTITION";
				compensate.setPartition(rs.getInt(field));
				field = "IDENTITY";
				compensate.setIdentity(rs.getInt(field));
				field = "QUANTITY";
				compensate.setQuantity(rs.getInt(field));
				field = "LEVEL";
				compensate.setLevel(rs.getInt(field));
				
				type.addCompensates(compensate);
			}
			
		} catch(Exception e){
			StringBuilder sb = new StringBuilder();
			if (type.getName() == null)	sb.append("null");
			else						sb.append(type.getName());
			sb.append("(");
			sb.append(type.getId());
			sb.append(", field : ");
			if (field == null)			sb.append("null");
			else						sb.append(field);
			//sb.append(")을(를) 불러들이는데 실패했습니다.");
			sb.append("Failed to load)");
			System.out.println(sb.toString());
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public void clear(){
		
	}
}

