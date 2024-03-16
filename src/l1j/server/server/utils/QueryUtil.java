package l1j.server.server.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;

public class QueryUtil {
	private static void setupPrepareStatement(PreparedStatement pstm, Object[] args) throws SQLException {
        for (int i = 0; i < args.length; ++i) {
            pstm.setObject(i + 1, args[i]);
        }
    }
    
    public static <T> T selectFirst(EntityFactory<T> factory, String sql, Object... args) {
        List<T> result = selectAll(factory, sql, args);
        return result.isEmpty() ? null : result.get(0);
    }
    
    public static <T> List<T> selectAll(EntityFactory<T> factory, String sql, final Object... args) {
        List<T> result = new FastTable<T>();
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
        	con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement(sql);
            setupPrepareStatement(pstm, args);
        	rs = pstm.executeQuery();
            while (rs.next()) {
                T entity = factory.fromResultSet(rs);
                if (entity == null) {
                	new NullPointerException(factory.getClass().getSimpleName() + " returned null.");
                }
                result.add(entity);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
        	SQLUtil.close(rs, pstm, con);
        }
        return result;
    }
    
    public static boolean execute(Connection con, String sql, Object... args) {
    	PreparedStatement pstm = null;
        try {
        	pstm = con.prepareStatement(sql);
            setupPrepareStatement(pstm, args);
            pstm.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
        	SQLUtil.close(pstm);
        }
    }
    
    public static boolean execute(String sql, Object... args) {
    	Connection con = null;
        try {
        	con = L1DatabaseFactory.getInstance().getConnection();
            return execute(con, sql, args);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
        	SQLUtil.close(con);
        }
    }
    
    public interface EntityFactory<T> {
        T fromResultSet(ResultSet p0) throws SQLException;
    }
}

