package l1j.server.GameSystem.astar.share;

public final class TimeLine {

	private static long time;

	public static void startNano() {
		time = System.nanoTime();
	}

	public static long endNano() {
		return System.nanoTime() - time;
	}

	public static void start(String msg) {
		time = System.currentTimeMillis();
		if (msg != null) {
			System.out.print(msg);
		}
	}

	public static void end() {
		System.out.printf("%dms\r\n", System.currentTimeMillis() - time);
	}

}

