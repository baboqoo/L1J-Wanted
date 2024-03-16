package l1j.server;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class FileLogFormatter extends Formatter {
	private static final String CRLF = "\r\n";

	private static final String a = "\t";

	@Override
	public String format(LogRecord record) {
		StringBuffer output = new StringBuffer();
		output.append(record.getMillis());
		output.append(a);
		output.append(record.getLevel().getName());
		output.append(a);
		output.append(record.getThreadID());
		output.append(a);
		output.append(record.getLoggerName());
		output.append(a);
		output.append(record.getMessage());
		output.append(CRLF);
		return output.toString();
	}
}

