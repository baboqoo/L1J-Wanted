package l1j.server;

import java.util.logging.Filter;
import java.util.logging.LogRecord;

public class ErrorFilter implements Filter {
	public ErrorFilter() {
	}

	public boolean isLoggable(LogRecord record) {
		return record.getThrown() != null;
	}

}

