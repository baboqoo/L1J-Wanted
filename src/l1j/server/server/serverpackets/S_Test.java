package l1j.server.server.serverpackets;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.utils.StringUtil;

public class S_Test extends ServerBasePacket {
	public S_Test(int OpCodeID, int TestLevel, L1PcInstance Player) {
		_opcodeid	= OpCodeID;
		_testlevel	= TestLevel;
		_gm			= Player;
	}
	
	public S_Test(byte[] data) {
		writeByte(data);
	}

	@Override
	public byte[] getContent() {
		writeC(_opcode[_testlevel][_opcodeid]);
		// ■■■■ jump고등어등으로 조사한 테스트용 데이터 ■■■■
		int objid = 0;
		Object[] petList = _gm.getPetList().values().toArray();
		L1SummonInstance summon = null;
		for (Object pet : petList) {
			if (pet instanceof L1SummonInstance) {
				summon = (L1SummonInstance) pet;
				objid = summon.getId();
				break;
			}
		}
		writeD(objid);
		writeC(25);
		// ■■■■■■■■■■■■■■■■■■■■■■■■
		return getBytes();
	}

	public String getInfo() {
		StringBuilder info = new StringBuilder();
		//info.append(".opc 오퍼레이션 코드 ID 라고 입력해 주세요.\n");
		info.append("Please enter the operation code ID as .opc.\n");
		info.append("[Ver] ").append(_version);
		info.append(" [Level] ").append(_testlevel);
		info.append(" [IdRange] 0 - ").append(_opcode[_testlevel].length - 1)
				.append("\n");
		//info.append("[직전에 있는 행동] ").append(_action).append("\n");	
		//info.append("[예상되는 상태] ").append(_status).append("\n");
		info.append("[Previous Action] ").append(_action).append("\n");
		info.append("[Expected State] ").append(_status).append("\n");
		return info.toString();
	}

	public String getCode() {
		StringBuilder info = new StringBuilder();
		info.append("[OpCodeId] ").append(_opcodeid).append(" [OpCode] ")
				.append(_opcode[_testlevel][_opcodeid]);
		return info.toString();
	}

	public String getCodeList() {
		StringBuilder info = new StringBuilder();
		//info.append(".opcid 오퍼레이션 코드 ID 라고 입력해 주세요.\n");
		info.append("Please enter the operation code ID as .opcid.\n");
		info.append("Lv").append(_testlevel).append(
				" 0  1  2  3  4  5  6  7  8  9\n");
		int t = 0;
		int tc = 10;
		for (int i = 0; i < _opcode[_testlevel].length; i++) {
			if (tc == 10) {
				if (t > 0) {
					info.append("\n");
				}
				info.append(padt(t));
				t++;
				tc = 0;
			}
			info.append(pad(_opcode[_testlevel][i]));
			tc++;
		}
		return info.toString();
	}

	private String pad(int i) {
		if (i < 10) {
			return (new StringBuilder()).append(" 00").append(i).toString();
		} else if (i < 100) {
			return (new StringBuilder()).append(" 0").append(i).toString();
		}
		return (new StringBuilder()).append(StringUtil.EmptyOneString).append(i).toString();
	}

	private String padt(int i) {
		if (i < 10) {
			return (new StringBuilder()).append(StringUtil.ZeroString).append(i).append(StringUtil.EmptyOneString).toString();
		}
		return (new StringBuilder()).append(i).append(StringUtil.EmptyOneString).toString();
	}
	@Override
	public String getType() {
		return "[S]  S_Test";
	}

	private int _opcodeid;
	private int _testlevel;
	private L1PcInstance _gm;

	// ■■■■ 보고가 감색 와르르 없게 하기 때문에(위해)의 버젼 ■■■■
	private final String _version = "S_HPMeter1.0";

	// ■■■■ 직전에 있어 두는 행동 ■■■■
	//private final String _action = "사몬을 1마리만 내 둔다";
	private final String _action = "Leave only one Samon.";

	// ■■■■ 올바른 오퍼레이션 코드를 보낼 수 있었을 때에 예상되는 상태 ■■■■
	//private final String _status = "사몬의 HP미터가25%에 변동한다";
	private final String _status = "Samon's HP meter fluctuates at 25%.";

	// 이미 해명 끝난 오퍼레이션 코드는 하단으로 설정
	// 상단(Level0) 현재 137고등어로 전혀 사용되지 않은 코드 .opc .opcid 용
	// 중단(Level1) 현재 137고등어로 정의는 되고 있지만 정말로 그것용의 코드인가 확인하고 있지 않는 코드와
	// "etc/작동코드 참고 자료.txt"에 써 있데 와 없고 밖에 알지 않은 것과
	// 130~139(0~129의 사이가 가능성 높다고 생각하므로)의 area .opc2 .opcid2 용
	// 하단(Level2) 현재 137고등어로 이용되고 있어 동작도 하고 있는 것(만일을 위해에 준비) .opc3 .opcid3 용
	// 상단에서 안되었던 경우는 중단, 중단에서도 안되었던 경우는 하단이라는 느낌으로
	// Already explained operation codes are placed at the bottom.
	// Top (Level 0) is for codes not used in 137 High Tuna at all, such as .opc and .opcid.
	// Middle (Level 1) is for codes defined in 137 High Tuna but not confirmed if they are really for that purpose, or if they are mentioned in "etc/operating code reference.txt" and are known only outside.
	// 130-139 (between 0-129 seems more likely) are used for area .opc2 and .opcid2.
	// Bottom (Level 2) is for codes currently used and working in 137 High Tuna and ready for emergencies, like .opc3 and .opcid3.
	// If it doesn't work at the top, move to the middle; if it doesn't work in the middle, move to the bottom.	
	int[][] _opcode = {
			{ 2, 3, 4, 6, 8, 16, 17, 18, 19, 22, 24, 27, 31, 33, 34, 35, 37,
					38, 40, 43, 47, 48, 49, 52, 54, 62, 65, 70, 72, 73, 74, 75,
					76, 78, 80, 83, 84, 86, 87, 88, 89, 90, 91, 92, 93, 95, 98,
					99, 101, 102, 104, 105, 107, 110, 112, 113, 114, 116, 117,
					118, 119, 120, 121, 122, 124, 127, 128 },

			{ 0, 5, 9, 13,

			42, 44, 50, 53, 55, 58, 60, 64, 77,

			111, 129, 130, 131, 132, 133, 134, 135, 136, 137, 138, 139 },

			{ 1, 7, 10, 11, 12, 15, 20, 21, 23, 25, 26, 28, 29, 30, 32, 36, 39,
					41, 45, 46, 51, 56, 57, 59, 61, 63, 66, 67, 68, 69, 71, 79,
					81, 82, 85, 94, 96, 97, 100, 103, 106, 108, 109, 115, 123,
					125, 126 } };
}

