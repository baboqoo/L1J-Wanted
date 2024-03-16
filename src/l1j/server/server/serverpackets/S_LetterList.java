package l1j.server.server.serverpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;
import l1j.server.server.utils.StringUtil;

public class S_LetterList extends ServerBasePacket {
    private static Logger _log = Logger.getLogger(S_Letter.class.getName());
    private static final String S_LETTER_LIST = "[S] S_LetterList";
    private byte[] _byte = null;
    
    public static final int WRITE_TYPE_PRIVATE_MAIL		= 80;
    public static final int WRITE_TYPE_BLOODPLEDGE_MAIL	= 81;

    public static final int TYPE_RECEIVE				= 0;
    public static final int TYPE_SEND					= 1;
    
    public static final S_LetterList WRITE_PRIVATE_MAIL		= new S_LetterList(32);
    public static final S_LetterList WRITE_BLOODPLEDGE_MAIL	= new S_LetterList(33);
    
    public S_LetterList(int type) {
    	writeC(Opcodes. S_MAIL_INFO);
        writeC(type);
        writeC(0x01);
    }
    
    public S_LetterList(L1PcInstance pc, int type, int count) {
        buildPacket(pc, type, count);
    }

    private void buildPacket(L1PcInstance pc, int type, int count) {
        Connection con = null;
        PreparedStatement pstm = null;
        PreparedStatement pstm1 = null;

        ResultSet rs = null;
        ResultSet rs1 = null;
        int cnt = 0;
        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement("SELECT * FROM letter WHERE receiver=? AND template_id = ? ORDER BY date LIMIT ?");
            pstm.setString(1, pc.getName());
            pstm.setInt(2, type);
            pstm.setInt(3, count);
            rs = pstm.executeQuery();

            pstm1 = con.prepareStatement("SELECT COUNT(*) AS cnt FROM letter WHERE receiver=? AND template_id = ? ORDER BY date LIMIT ?");
            pstm1.setString(1, pc.getName());
            pstm1.setInt(2, type);
            pstm1.setInt(3, count);
            rs1 = pstm1.executeQuery();
            if(rs1.next())cnt = rs1.getInt(1);
            writeC(Opcodes. S_MAIL_INFO);
            writeC(type);
            writeH(cnt);

            while(rs.next()){
                writeD(rs.getInt(1));
                writeC(rs.getInt(9));
                writeD((int) (rs.getTimestamp(5).getTime() / 1000L));
                writeC(0);
                writeS(rs.getString(3));
                writeSS(rs.getString(7));
            }
        } catch (SQLException e) {
            _log.log(Level.SEVERE, e.getLocalizedMessage(), e);//이로그를 없애면 안나옴
        } finally {
            SQLUtil.close(rs);
            SQLUtil.close(rs1);
            SQLUtil.close(pstm);
            SQLUtil.close(pstm1);
            SQLUtil.close(con);
        }
    }

    public S_LetterList(int writeType, int id, int type, String name, String title) {
        writeC(Opcodes. S_MAIL_INFO);
        writeC(writeType);
        writeD(id);
        writeC(type); // 0:수신, 1:발신
        writeS(name);
        writeSS(title);
    }

    public S_LetterList(L1PcInstance pc, int type, int id, int read) {
        Connection con = null;
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            con = L1DatabaseFactory.getInstance().getConnection();
            pstm = con.prepareStatement("SELECT * FROM letter WHERE item_object_id=? ");
            pstm.setInt(1, id);
            rs = pstm.executeQuery();
            writeC(Opcodes. S_MAIL_INFO);
            writeC(type);
            if (rs.next()) {
                writeD(rs.getInt("item_object_id"));
                writeSS(rs.getString("content"));
            }
        } catch (Exception e) {
            _log.log(Level.WARNING, StringUtil.EmptyString, e);
        } finally {
            SQLUtil.close(rs, pstm, con);
        }
    }
  
    public S_LetterList(L1PcInstance pc, int type, int id, boolean value) {
        writeC(Opcodes. S_MAIL_INFO);
        writeC(type); 
        writeD(id); 
        writeC(value ? 1 : 0);
    }
    
    @Override
    public byte[] getContent() {
        if (_byte == null) {
        	_byte = getBytes();
        }
        return _byte;
    }

    public String getType() {
        return S_LETTER_LIST;
    }
}

