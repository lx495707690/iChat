package com.iapps.libs.helpers;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.security.auth.x500.X500Principal;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;

/**
 * Helper class to manage Certificate in Android App
 * 
 * @author melvin
 * 
 */
public class CertHelper {

	private static final X500Principal DEBUG_DN = new X500Principal(
			"CN=Android Debug,O=Android,C=US");

	/**
	 * Get the SHA1 fingerprint for the cetificate
	 * 
	 * @param pm
	 *            {@link PackageManager} package Manager
	 * @param pn
	 *            {@link String} package name
	 * @return
	 */
	public static String getSHA1Fingerprint(PackageManager pm, String pn) {
		String fingerprintSHA1 = null;
		byte[] cert = getCert(pm, pn);

		try {
			MessageDigest md = MessageDigest.getInstance("SHA1");

			byte[] sha1digest = new byte[0];
			if (md != null) {
				sha1digest = md.digest(cert);
				StringBuilder sb = new StringBuilder();
				for (int i = 0; i < sha1digest.length; ++i) {
					sb.append((Integer
							.toHexString((sha1digest[i] & 0xFF) | 0x100))
							.substring(1, 3));
				}
				fingerprintSHA1 = sb.toString();
			}

		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}

		return fingerprintSHA1;
	}

	/**
	 * Check if the app is signed using debug certificate
	 * 
	 * @param pm
	 *            {@link PackageManager} Package Manager
	 * @param pn
	 *            {@link String} Package Name
	 * @return true if is signed using debug certificate, false otherwise.
	 */
	public static boolean isDebuggable(PackageManager pm, String pn) {
		boolean debuggable = false;
		byte[] cert = getCert(pm, pn);

		InputStream input = new ByteArrayInputStream(cert);

		CertificateFactory cf = null;
		try {
			cf = CertificateFactory.getInstance("X509");

		} catch (CertificateException e) {
			e.printStackTrace();
		}
		X509Certificate c = null;

		try {
			c = (X509Certificate) cf.generateCertificate(input);
			debuggable = c.getSubjectX500Principal().equals(DEBUG_DN);

		} catch (CertificateException e) {
			e.printStackTrace();
		}

		return debuggable;
	}

	/**
	 * Get certificate in bytes
	 * 
	 * @param pm
	 * @param pn
	 * @return
	 */
	private static byte[] getCert(PackageManager pm, String pn) {

		int flags = PackageManager.GET_SIGNATURES;

		PackageInfo packageInfo = null;
		byte[] cert = null;
		try {
			packageInfo = pm.getPackageInfo(pn, flags);
			Signature[] signatures = packageInfo.signatures;
			cert = signatures[0].toByteArray();

		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}

		return cert;
	}

}
