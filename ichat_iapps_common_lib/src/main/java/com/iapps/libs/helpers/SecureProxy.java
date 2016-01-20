package com.iapps.libs.helpers;

import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import android.util.Base64;

/**
 * Helper class to handle encryption and decryption
 * @author melvin
 *
 */
public class SecureProxy {
	
	public static final boolean DEBUGGABLE = true;
	
	private Cipher ecipher;
	private Cipher dcipher;
	private byte[] salt = new byte[]{-62, -54, 61, 104, -101, 88, -117, -3};

    private static final String PBE_ALGORITHM = "PBEWithSHA256And256BitAES-CBC-BC";
    private static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    private static final String PROVIDER = "BC";
    
	private int iterationCount = 200;

	 public SecureProxy (String sha1Fingerprint) {
	    try {

	        // Create the key
	        KeySpec keySpec = new PBEKeySpec(sha1Fingerprint.toCharArray(), salt, iterationCount, 256);
	        SecretKey key = SecretKeyFactory.getInstance(
	                PBE_ALGORITHM, PROVIDER).generateSecret(keySpec);
	        ecipher = Cipher.getInstance(CIPHER_ALGORITHM, PROVIDER);
	        dcipher = Cipher.getInstance(CIPHER_ALGORITHM, PROVIDER);

	        // Prepare the parameter to the ciphers
	        AlgorithmParameterSpec paramSpec = new PBEParameterSpec(salt, iterationCount);

	        // Create the ciphers
	        ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);
	        dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);
	    } catch (Exception e) { e.printStackTrace(); }
	}
	 
	public String encrypt(String str) {
	    try {
	        // Encode the string into bytes using utf-8
//	        byte[] utf8 = str.getBytes("UTF8");

	        // Encrypt
//	        byte[] enc = ecipher.doFinal(utf8);

	        // Encode bytes to base64 to get a string
//	        return Base64.encodeToString(enc, Base64.DEFAULT);
			return "";
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}

	public String decrypt(String str) {
	    try {
	        // Decode base64 to get bytes
	        byte[] dec = Base64.decode(str, Base64.DEFAULT);

	        // Decrypt
	        byte[] utf8 = dcipher.doFinal(dec);

	        // Decode using utf-8
	        return new String(utf8, "UTF8");
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}

	public int getIterationCount() {
	    return iterationCount;
	}

	public String getSalt() {
	    return Base64.encodeToString(salt, Base64.DEFAULT);
	}
	
}
