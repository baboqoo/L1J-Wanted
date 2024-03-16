package l1j.server.server.construct.message;

import l1j.server.server.serverpackets.message.S_NotificationMessage;
import l1j.server.server.serverpackets.message.S_NotificationMessageNoti;
import l1j.server.server.serverpackets.message.S_NotificationMessage.display_position;

public class L1NotificationMessege {
	public static final S_NotificationMessage NIGHT_CANNOT_TELEPORT			= new S_NotificationMessage(display_position.screen_top, "$30095", "00 ff 00", 10);// 밤시간 동안 무작위 텔레토프를 할 수 없는 지역입니다.
	public static final S_NotificationMessage AURAKIA_DARK_MENT_1			= new S_NotificationMessage(display_position.screen_top, "$35878", "00 ff 00", 10);// 누가 우리의 의식을 방해하느냐
	public static final S_NotificationMessage AURAKIA_DARK_MENT_2			= new S_NotificationMessage(display_position.screen_top, "$35879", "00 ff 00", 10);// 우리를 방해할 자격을 갖췄는지 확인해보지
	public static final S_NotificationMessage AURAKIA_DARK_MENT_3			= new S_NotificationMessage(display_position.screen_top, "$35880", "00 ff 00", 10);// 어둠감시관이여 저녀석들을 처리하거라
	public static final S_NotificationMessage AURAKIA_DARK_MENT_4			= new S_NotificationMessage(display_position.screen_top, "$35881", "00 ff 00", 10);// 부질없는 짓이라는걸 직접 알려주지
	public static final S_NotificationMessage AURAKIA_DARK_MENT_5			= new S_NotificationMessage(display_position.screen_top, "$35882", "00 ff 00", 10);// 어둠감시관의 본모습을 보여주마
	public static final S_NotificationMessage AURAKIA_DARK_MENT_6			= new S_NotificationMessage(display_position.screen_top, "$35883", "00 ff 00", 10);// 내 본모습을 보이게 될줄은 몰랐군
	public static final S_NotificationMessage AURAKIA_CLEAR_MENT			= new S_NotificationMessage(display_position.screen_top, "$35902", "00 ff 00", 10);// 안돼... 아우라키아의 구속이 풀리다니...
	public static final S_NotificationMessage AURAKIA_FAIL_MENT				= new S_NotificationMessage(display_position.screen_top, "$35903", "00 ff 00", 10);// 역시 너희들은 아직 나의 상대가 되지 못하는구나
	public static final S_NotificationMessageNoti OCCUPY_HEINE_START		= new S_NotificationMessageNoti(0, "$35485", "2d e6 83", 20);// 하이네 선착장 앞에 균열의 소용돌이가 생성되었습니다. 점령전이 시작되었습니다.
	public static final S_NotificationMessageNoti OCCUPY_HEINE_END			= new S_NotificationMessageNoti(0, "$35486", "2d e6 83", 20);// 하이네 점령전이 종료되었습니다. 균열이 파도 속으로 모습을 감추었습니다.
	public static final S_NotificationMessageNoti OCCUPY_WINDAWOOD_START	= new S_NotificationMessageNoti(0, "$36531", "2d e6 83", 20);// 윈다우드 성을 삼겨버린 절벽 아래에 균열의 소용돌이가 생성되었습니다. 점령전이 시작되었습니다.
	public static final S_NotificationMessageNoti OCCUPY_WINDAWOOD_END		= new S_NotificationMessageNoti(0, "$36532", "2d e6 83", 20);// 윈다우드 점령전이 종료되었습니다. 균열이 절벽 아래로 모습을 감추었습니다.
	public static final S_NotificationMessageNoti OCCUPY_WINDAWOOD_ENTER	= new S_NotificationMessageNoti(0, "$36533", "2d e6 83", 20);// 윈다우드 성이 가라앉은 절벽 아래로 발생한 균열을 통해 윈다우드 점령전에 참여할 수 있습니다.
}

