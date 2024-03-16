package l1j.server.server.encryptions.lin380;

import l1j.server.server.encryptions.lin380.types.UChar8;
import l1j.server.server.encryptions.lin380.types.ULong32;

public class LineageEncryption {
	private LineageBlowfish _LineageBlowfish = new LineageBlowfish();
	private LineageKeys keys = new LineageKeys();

	public byte[] server_HashKey = new byte[256];
	public byte[] client_HashKey = new byte[256];

	public void initKeys(long seed) {
		long[] key = { seed, 2467289058L };
		this._LineageBlowfish.getSeeds(key);
		long tmp69_68 = key[0];
		keys.decodeKey[0] = tmp69_68;
		keys.encodeKey[0] = tmp69_68;
		long tmp88_87 = key[1];
		keys.decodeKey[1] = tmp88_87;
		keys.encodeKey[1] = tmp88_87;

		byte[] hashkey = new byte[256];
		char[] tk = UChar8.fromArray(keys.decodeKey);

		init_enc_hashkey(hashkey, tk, 8);
		System.arraycopy(hashkey, 0, this.client_HashKey, 0, 256);
		System.arraycopy(hashkey, 0, this.server_HashKey, 0, 256);
	}

	public void init_enc_hashkey(byte[] hashkey, char[] currentkey, int const_num) {
		int k = 0;
		for (int i = 0; i < 256; i++) {
			hashkey[i] = ((byte) i);
		}
		for (int j = 0; j < 256; j++) {
			k = hashkey[j] + k + currentkey[(j & 7)] & 0xFF;

			byte tmp = hashkey[k];
			hashkey[k] = hashkey[j];
			hashkey[j] = tmp;
		}
	}

	public char[] encrypt_S(char[] buf) {
		int k = 0;
		int j = 0;
		int m = 0;
		int o = 0;
		try {
			for (int i = 0; i < buf.length; ++i) {
				m = i + 1;
				o = m >> 0x00000008;
				m -= o << 0x00000008;

				j = m & 0xFF;
				k += this.client_HashKey[m];
				k &= 255;

				byte tk = this.client_HashKey[j];
				this.client_HashKey[j] = this.client_HashKey[k];
				this.client_HashKey[k] = tk;

				int b3 = this.client_HashKey[j];
				int b4 = this.client_HashKey[k];

				b3 += b4;
				b3 &= 255;

				byte b5 = this.client_HashKey[b3];
				byte b6 = (byte) buf[i];
				b6 = (byte) (b6 ^ b5);
				buf[i] = (char) b6;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return buf;
	}

	public char[] fromArray(byte[] buff) {
		char[] charBuff = new char[buff.length];
		for (int i = 0; i < buff.length; i++) {
			charBuff[i] = (char) (buff[i] & 0xFF);
		}
		return charBuff;
	}

	public byte[] fromArray(char[] buff) {
		byte[] byteBuff = new byte[buff.length];
		for (int i = 0; i < buff.length; ++i) {
			byteBuff[i] = (byte) (buff[i] & 0xFF);
		}
		return byteBuff;
	}

	public byte[] decrypt_C(byte[] buf) throws Exception {
		int k = 0;
		int j = 0;
		for (int i = 0; i < buf.length; i++) {
			j = i + 1 & 0xFF;

			k += this.client_HashKey[j];
			k &= 255;

			int tk = this.client_HashKey[j];
			this.client_HashKey[j] = this.client_HashKey[k];
			this.client_HashKey[k] = ((byte) (tk & 0xFF));

			int b3 = this.client_HashKey[j];
			int b4 = this.client_HashKey[k];
			b3 += b4;
			b3 &= 255;

			byte b5 = this.client_HashKey[b3];
			byte b6 = buf[i];
			b6 = (byte) (b6 ^ b5);
			buf[i] = b6;
		}
		return buf;
	}

	public char[] decrypt_C(char[] buf) throws Exception {
		int k = 0;
		int j = 0;
		for (int i = 0; i < buf.length; i++) {
			j = i + 1 & 0xFF;

			k += this.client_HashKey[j];
			k &= 255;

			byte tk = this.client_HashKey[j];
			this.client_HashKey[j] = this.client_HashKey[k];
			this.client_HashKey[k] = tk;

			int b3 = this.client_HashKey[j];
			int b4 = this.client_HashKey[k];
			b3 += b4;
			b3 &= 255;

			byte b5 = this.client_HashKey[b3];
			byte b6 = (byte) buf[i];
			b6 = (byte) (b6 ^ b5);
			buf[i] = ((char) b6);
		}
		return buf;
	}

	public char[] encrypt_C(char[] buf) throws Exception {
		if (keys == null)
			throw new Exception();
		long mask = ULong32.fromArray(buf);
		_encrypt(buf);
		keys.encodeKey[0] ^= mask;
		keys.encodeKey[1] = ULong32.add(keys.encodeKey[1], 679411651L);
		return buf;
	}

	public byte[] encrypt_C(byte[] buf) throws Exception {
		long mask = ULong32.fromArray(buf);
		_encrypt(buf);
		keys.encodeKey[0] ^= mask;
		keys.encodeKey[1] = ULong32.add(keys.encodeKey[1], 679411651L);
		return buf;
	}

	public char[] decrypt_S(char[] buf) throws Exception {
		_decrypt(buf);
		char[] buf1 = new char[buf.length + ~0x00000003];
		System.arraycopy(buf, 4, buf1, 0, buf.length + ~0x00000003);
		long mask = ULong32.fromArray(buf1);
		keys.decodeKey[0] ^= mask;
		keys.decodeKey[1] = ULong32.add(keys.decodeKey[1], 679411651L);
		return buf1;
	}

	public byte[] decrypt_S(byte[] buf) throws Exception {
		_decrypt(buf, buf.length);
		byte[] buf1 = new byte[buf.length + ~0x00000003];
		System.arraycopy(buf, 4, buf1, 0, buf.length + ~0x00000003);
		long mask = ULong32.fromArray(buf1);
		keys.decodeKey[0] ^= mask;
		keys.decodeKey[1] = ULong32.add(keys.decodeKey[1], 679411651L);
		return buf1;
	}

	private char[] _encrypt(char[] buf) {
		int size = buf.length;
		char[] ek = UChar8.fromArray(keys.encodeKey);
		int tmp14_13 = 0;
		char[] tmp14_12 = buf;
		tmp14_12[tmp14_13] = ((char) (tmp14_12[tmp14_13] ^ ek[0]));
		for (int i = 1; i < size; i++) {
			int tmp32_30 = i;
			char[] tmp32_29 = buf;
			tmp32_29[tmp32_30] = ((char) (tmp32_29[tmp32_30] ^ (buf[(i - 1)] ^ ek[(i & 0x7)])));
		}
		buf[3] = ((char) (buf[3] ^ ek[2]));
		buf[2] = ((char) (buf[2] ^ buf[3] ^ ek[3]));
		buf[1] = ((char) (buf[1] ^ buf[2] ^ ek[4]));
		buf[0] = ((char) (buf[0] ^ buf[1] ^ ek[5]));
		return buf;
	}

	private byte[] _encrypt(byte[] buf) {
		int size = buf.length;
		char[] ek = UChar8.fromArray(keys.encodeKey);
		int tmp14_13 = 0;
		byte[] tmp14_12 = buf;
		tmp14_12[tmp14_13] = ((byte) (tmp14_12[tmp14_13] ^ ek[0]));
		for (int i = 1; i < size; i++) {
			int tmp32_30 = i;
			byte[] tmp32_29 = buf;
			tmp32_29[tmp32_30] = ((byte) (tmp32_29[tmp32_30] ^ (buf[(i - 1)] ^ ek[(i & 0x7)])));
		}
		buf[3] = ((byte) (buf[3] ^ ek[2]));
		buf[2] = ((byte) (buf[2] ^ buf[3] ^ ek[3]));
		buf[1] = ((byte) (buf[1] ^ buf[2] ^ ek[4]));
		buf[0] = ((byte) (buf[0] ^ buf[1] ^ ek[5]));
		return buf;
	}

	private char[] _decrypt(char[] buf) {
		int size = buf.length;
		char[] dk = UChar8.fromArray(keys.decodeKey);

		char b3 = buf[3];
		int tmp19_18 = 3;
		char[] tmp19_17 = buf;
		tmp19_17[tmp19_18] = ((char) (tmp19_17[tmp19_18] ^ dk[2]));

		char b2 = buf[2];
		int tmp35_34 = 2;
		char[] tmp35_33 = buf;
		tmp35_33[tmp35_34] = ((char) (tmp35_33[tmp35_34] ^ (b3 ^ dk[3])));

		char b1 = buf[1];
		int tmp54_53 = 1;
		char[] tmp54_52 = buf;
		tmp54_52[tmp54_53] = ((char) (tmp54_52[tmp54_53] ^ (b2 ^ dk[4])));

		char k = (char) (buf[0] ^ b1 ^ dk[5]);
		buf[0] = ((char) (k ^ dk[0]));

		for (int i = 1; i < size; i++) {
			char t = buf[i];
			int tmp106_104 = i;
			char[] tmp106_103 = buf;
			tmp106_103[tmp106_104] = ((char) (tmp106_103[tmp106_104] ^ (dk[(i & 0x7)] ^ k)));
			k = t;
		}
		return buf;
	}

	private byte[] _decrypt(byte[] buf, int size) {
		char[] dk = UChar8.fromArray(keys.decodeKey);

		byte b3 = buf[3];
		int tmp16_15 = 3;
		byte[] tmp16_14 = buf;
		tmp16_14[tmp16_15] = ((byte) (tmp16_14[tmp16_15] ^ dk[2]));

		byte b2 = buf[2];
		int tmp32_31 = 2;
		byte[] tmp32_30 = buf;
		tmp32_30[tmp32_31] = ((byte) (tmp32_30[tmp32_31] ^ (b3 ^ dk[3])));

		byte b1 = buf[1];
		int tmp51_50 = 1;
		byte[] tmp51_49 = buf;
		tmp51_49[tmp51_50] = ((byte) (tmp51_49[tmp51_50] ^ (b2 ^ dk[4])));

		byte k = (byte) (buf[0] ^ b1 ^ dk[5]);
		buf[0] = ((byte) (k ^ dk[0]));

		for (int i = 1; i < size; i++) {
			byte t = buf[i];
			int tmp103_101 = i;
			byte[] tmp103_100 = buf;
			tmp103_100[tmp103_101] = ((byte) (tmp103_100[tmp103_101] ^ (dk[(i & 0x7)] ^ k)));
			k = t;
		}
		return buf;
	}
}
