package l1j.server.server.controller.action;

import l1j.server.IndunSystem.ruun.Ruun;

/**
 * 루운성 초기화 컨트롤러
 * @author LinOffice
 */
public class RuunReset implements ControllerInterface {
	private static class newInstance {
		public static final RuunReset INSTANCE = new RuunReset();
	}
	public static RuunReset getInstance() {
		return newInstance.INSTANCE;
	}
	private RuunReset(){}

	@Override
	public void execute() {
		Ruun ruun = Ruun.getInstance();
		ruun.returnTeleport();
		ruun.end();
	}

	@Override
	public void execute(l1j.server.server.model.Instance.L1PcInstance pc) {
	}

}

