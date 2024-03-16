package l1j.server.server.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Cmd {
	private Process process;
	private BufferedReader bufferedReader;
	private StringBuffer readBuffer;
	private InputStreamReader inputStreamReader;
	
	public String execCommand(String query){
		try {
			process				= Runtime.getRuntime().exec(query);
			inputStreamReader	= new InputStreamReader(process.getInputStream(), CharsetUtil.EUC_KR_STR);
			bufferedReader		= new BufferedReader(inputStreamReader);
			
			String line			= null;
			readBuffer			= new StringBuffer();
			while ((line = bufferedReader.readLine()) != null) {
				readBuffer.append(line);
				readBuffer.append(StringUtil.LineString);
			}
			return readBuffer.toString();
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(1);
		} finally {
			try {
				if (bufferedReader != null) {
					bufferedReader.close();
				}
				if (inputStreamReader != null) {
					inputStreamReader.close();
				}
				if (process != null) {
					process.destroy();
				}
			} catch(Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
}

