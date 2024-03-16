package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.L1Racing;

public class S_GameList2 extends ServerBasePacket {
	private static final String S_GAME_LIST_2 = "[S] S_GameList2";

	private byte[] _byte = null;

	/*
	0000 : 7e 42 0a 00 04 00 [b8 c5 b4 cf c1 ae] 00 [c5 a9 b7    ~B..............
	0010 : af bf ec] 00 [bb e7 c4 ab c0 cc] 00 [b0 dd c6 c4] 00    ................
	0020 : [be c6 bf f4 bc d2 bd cc] 00 [4f 6f 6f 30 30 30] 00    .........Ooo000.
	0030 : [b3 c7 76 76] 00 [c8 a3 b9 da 30 30 32] 00 [b3 eb ba    ..vv.....002....
	0040 : a7] 00 [b3 fa b7 fa c1 f6 c1 b8] 00 [92 12] 00 ca 77    ...............w

	=======================================================================

	0000 : 7e 42 0a 00 04 00 [b0 dd c6 c4] 00 b8 c5 b4 cf c1    ~B..............
	0010 : ae 00 c5 a9 b7 af bf ec 00 bb e7 c4 ab c0 cc 00    ................
	0020 : be c6 bf f4 bc d2 bd cc 00 4f 6f 6f 30 30 30 00    .........Ooo000.
	0030 : b3 c7 76 76 00 c8 a3 b9 da 30 30 32 00 b3 eb ba    ..vv.....002....
	0040 : a7 00 b3 fa b7 fa c1 f6 c1 b8 00 26 c7 7b b8 34    ...........&.{.4

	=======================================================================

	0000 : 7e 42 0a 00 05 00 [b0 dd c6 c4]  00 b3 fa b7 fa c1    ~B..............
	0010 : f6 c1 b8 00 b8 c5 b4 cf c1 ae 00 c5 a9 b7 af bf    ................
	0020 : ec 00 bb e7 c4 ab c0 cc 00 be c6 bf f4 bc d2 bd    ................
	0030 : cc 00 4f 6f 6f 30 30 30 00 b3 c7 76 76 00 c8 a3    ..Ooo000...vv...
	0040 : b9 da 30 30 32 00 b3 eb ba a7 00 80 0d 00 3d b1    ..002.........=.

	=======================================================================

	0000 : 7e 42 0a 00 03 00 [b0 dd c6 c4] 00 b3 fa b7 fa c1    ~B..............
	0010 : f6 c1 b8 00 b8 c5 b4 cf c1 ae 00 be c6 bf f4 bc    ................
	0020 : d2 bd cc 00 c5 a9 b7 af bf ec 00 bb e7 c4 ab c0    ................
	0030 : cc 00 4f 6f 6f 30 30 30 00 b3 c7 76 76 00 c8 a3    ..Ooo000...vv...
	0040 : b9 da 30 30 32 00 b3 eb ba a7 00 da e6 58 66 52    ..002........XfR

	=======================================================================

	0000 : 7e 42 0a 00 03 00 [b0 dd c6 c4] 00 [b3 fa b7 fa c1    ~B..............
	0010 : f6 c1 b8] 00 [b8 c5 b4 cf c1 ae] 00 [be c6 bf f4 bc    ................
	0020 : d2 bd cc] 00 [bb e7 c4 ab c0 cc] 00 [c5 a9 b7 af bf    ................
	0030 : ec] 00 [4f 6f 6f 30 30 30] 00 [b3 c7 76 76] 00 [c8 a3    ..Ooo000...vv...
	0040 : b9 da 30 30 32] 00 [b3 eb ba a7] 00 [80 0d] 00 3d 78    ..002.........=x


	0000 : 7e 42 0a 00 03 00 [b0 dd c6 c4] 00 [b3 fa b7 fa c1    ~B..............
	0010 : f6 c1 b8] 00 [b8 c5 b4 cf c1 ae] 00 [be c6 bf f4 bc    ................
	0020 : d2 bd cc] 00 [bb e7 c4 ab c0 cc] 00 [4f 6f 6f 30 30    ...........Ooo00
	0030 : 30] 00 [c5 a9 b7 af bf ec] 00 [b3 c7 76 76] 00 [c8 a3    0..........vv...
	0040 : b9 da 30 30 32] 00 [b3 eb ba a7] 00 [7e 0d] 00 3d 90    ..002......~..=.

	0000 : 7e 42 0a 00 08 00 [c8 a3 b9 da 30 30 32] 00 [bb e7    ~B........002...
	0010 : c4 ab c0 cc] 00 [b8 c5 b4 cf c1 ae] 00 [b3 fa b7 fa    ................
	0020 : c1 f6 c1 b8] 00 [b3 c7 76 76] 00 [b3 eb ba a7] 00 [c5    .......vv.......
	0030 : a9 b7 af bf ec] 00 [b0 dd c6 c4] 00 [be c6 bf f4 bc    ................
	0040 : d2 bd cc] 00 [4f 6f 6f 30 30 30] 00 [80 0d] 00 3d 3f    ....Ooo000....=?

*/

	public S_GameList2(L1PcInstance pc, int i){
		buildPacket(pc, i);
	}

    /*스타트*/
	private void buildPacket(L1PcInstance pc, int i) {
		writeC(Opcodes.S_EVENT);
		writeC(0x42);
        writeH(L1Racing.getInstance().size(0)); // 참여자수
        writeH(i); // 등수
		for(int j = 0; j < L1Racing.getInstance().size(0) ; j++){
			writeS(L1Racing.getInstance().arrayList(0).get(j).getName());
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
		return S_GAME_LIST_2;
	}
}

