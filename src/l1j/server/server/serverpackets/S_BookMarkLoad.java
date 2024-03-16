package l1j.server.server.serverpackets;

import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_BookMarkLoad extends ServerBasePacket {
    private static final String S_BOOK_MARK_LOAD = "[S] S_BookmarkLoad";
    private byte[] _byte = null;
    private static Logger _log = Logger.getLogger(S_BookMarkLoad.class.getName());

    public S_BookMarkLoad(L1PcInstance pc) {
        try {
            int size		= pc.getBookMarkSize();
            int fastsize	= pc._speedbookmarks.size();
            int booksize	= pc.getBookMarkCount() + 6;
            int tempsize	= booksize - 1 - size - fastsize;
            writeC(Opcodes.S_VOICE_CHAT);
            writeC(0x2a);
            writeC(booksize);
            writeC(0x00);
            writeC(0x02);
            if (size > 0) {
                for (int i = 0; i < size; i++) {
                	writeC(i);
                }
            }

            if (fastsize > 0) {
                for (int i = 0; i < fastsize; i++) {
                	writeC(pc._speedbookmarks.get(i).getNumId());
                }
            }

            if (tempsize > 0) {
                for (int i = 0; i < tempsize; i++) {
                	writeC(0xff);
                }
            }

            writeH(pc.getBookMarkCount());
            writeH(size);
            for (int i = 0; i < size; i++) {
                writeD(pc.getBookMark().get(i).getNumId());
                writeS(pc.getBookMark().get(i).getName()); 
                writeH(pc.getBookMark().get(i).getMapId());
                writeH(pc.getBookMark().get(i).getLocX());
                writeH(pc.getBookMark().get(i).getLocY());
            }
        } catch (Exception e) {
            //_log.log(Level.WARNING, "S_BookMarkLoad 예외 발생.", e);
            _log.log(Level.WARNING, "S_BookMarkLoad exception occurred.", e);
        }
    }

    @Override
    public byte[] getContent() {
        if (_byte == null) {
        	_byte = getBytes();
        }
        return _byte;
    }

    public String getType() {
        return S_BOOK_MARK_LOAD;

    }

}
