package l1j.server.web;

import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

import java.io.File;
import java.security.cert.CertificateException;

import javax.net.ssl.SSLException;

import l1j.server.Config;
import l1j.server.server.utils.StringUtil;

public class SSLCertificator {
	
	/**
	 * Ssl 인증서
	 * 인증서 설정 파일로 생성이 불가능하면 자가서명으로 생성한다.
	 * @return SslContext
	 */
	public static SslContext getCertificate() {
		SslContext ssl = getCertificateFile();
		return ssl == null ? getSelfCertificate() : ssl;
	}

	/**
	 * Ssl 파일 인증서(기관 인증서)
	 * @return SslContext
	 */
	private static SslContext getCertificateFile() {
		try {
			if (StringUtil.isNullOrEmpty(Config.WEB.WEB_SSL_CERTIFICATE_FILE_PATH) || StringUtil.isNullOrEmpty(Config.WEB.WEB_SSL_KEY_FILE_PATH)) {
				return null;
			}
			File cert	= new File(Config.WEB.WEB_SSL_CERTIFICATE_FILE_PATH);
			File key	= new File(Config.WEB.WEB_SSL_KEY_FILE_PATH);
			if (!cert.exists() || !key.exists()) {
				return null;
			}
			return SslContextBuilder.forServer(cert, key).build();
		} catch(SSLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Ssl 자가 서명 인증서
	 * @return SslContext
	 */
	private static SslContext getSelfCertificate() {
		try {
			System.out.println("[WebServer] SSL_FILE_NULL_OR_EMPTY_TO_SELF_SSL_CREATE");
			SelfSignedCertificate ssc = new SelfSignedCertificate();// 자가 서명 인증서
			return SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
		} catch(SSLException e) {
			e.printStackTrace();
			return null;
		} catch(CertificateException e) {
			e.printStackTrace();
			return null;
		}
	}
}

