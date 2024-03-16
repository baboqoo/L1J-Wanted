package l1j.server.server.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.management.ManagementFactory;
import java.util.Calendar;
import l1j.server.server.model.gametime.RealTimeClock;

public class ProcessPlayer {
	public static String getPid() {
		String s = ManagementFactory.getRuntimeMXBean().getName();
		return s.substring(0, s.indexOf("@"));
	}

	public static void dumpLog() {
		Calendar cal = RealTimeClock.getInstance().getRealTimeCalendar();
		String[] command = { 
			"cmd", 
			"/C", 
			"C:\\Program Files\\Java\\jdk1.8.0_91\\bin\\jstack", 
			getPid(), 
			">", 
			String.format("dump\\[%02d-%02d-%02d]dump.txt", cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND)) 
		};

		ProcessPlayer mpp = new ProcessPlayer();
		try {
			mpp.byRuntime(command);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void byRuntime(String[] command) throws IOException, InterruptedException {
		Runtime runtime = Runtime.getRuntime();
		Process process = runtime.exec(command);
		printStream(process);
	}

	public void byProcessBuilder(String[] command) throws IOException, InterruptedException {
		ProcessBuilder builder = new ProcessBuilder(command);
		Process process = builder.start();
		printStream(process);
	}

	private void printStream(Process process) throws IOException, InterruptedException {
		process.waitFor();
		InputStream psout = process.getInputStream();
		Throwable localThrowable3 = null;
		try {
			copy(psout, System.out);
		} catch (Throwable localThrowable1) {
			localThrowable3 = localThrowable1;
			throw localThrowable1;
		} finally {
			if (psout != null) {
				if (localThrowable3 != null) {
					try {
						psout.close();
					} catch (Throwable localThrowable2) {
						localThrowable3.addSuppressed(localThrowable2);
					}
				} else {
					psout.close();
				}
			}
		}
	}

	public void copy(InputStream input, OutputStream output) throws IOException {
		byte[] buffer = new byte['?'];
		int n = 0;
		while ((n = input.read(buffer)) != -1) {
			output.write(buffer, 0, n);
		}
	}
}

