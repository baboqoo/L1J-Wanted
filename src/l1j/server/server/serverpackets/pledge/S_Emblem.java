package l1j.server.server.serverpackets.pledge;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class S_Emblem extends ServerBasePacket {
	private static final String S_EMBLEM = "[S] S_Emblem";
	private static final String PATH = "emblem/%d";
	
	public S_Emblem(int emblemId, int id) {
		BufferedInputStream bis = null;
		FileInputStream	fis = null;
		File readFile = null;
		try {
			readFile = new File(String.format(PATH, emblemId));
			if (readFile.exists()) {
				byte[] buff = null;
				fis = new FileInputStream(readFile);
				bis = new BufferedInputStream(fis);
				writeC(Opcodes.S_EMBLEM);
				writeC(1);
				writeD(id);
				writeD(emblemId);
				buff = new byte[(int)fis.getChannel().size()];
				bis.read(buff, 0, buff.length);
				writeByte(buff);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException ignore) {}
				bis = null;
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException ignore) {}
				fis = null;
			}
			readFile = null;
		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_EMBLEM;
	}
}

