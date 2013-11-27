package net;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;

public class Crypto {

	// key
	private static final String BaseSecretKey = "rBwj1MIAivVN222b";

	public static String Encrypt2Base64NoKey(String toEncrypt)
			throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException {
		return encrypt2Base64(toEncrypt);
	}

	private static String encrypt2Base64(String toEncrypt)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		SecretKeySpec keyspec = new SecretKeySpec(GetSecretKey().getBytes(),
				"AES");
		Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
		c.init(Cipher.ENCRYPT_MODE, keyspec);
		return Base64.encodeBase64String(c.doFinal(toEncrypt.getBytes()));
	}

	private static String GetSecretKey() {
		String pw = BaseSecretKey;
		return pw;
	}

	public static byte[] DecryptNoKey(byte[] Ciphertext)
			throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException {
		return decrypt2Bytes(Ciphertext);
	}

	private static byte[] decrypt2Bytes(byte[] ciphertext)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		SecretKeySpec keyspec = new SecretKeySpec(GetSecretKey().getBytes(),
				"AES");
		Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
		c.init(Cipher.DECRYPT_MODE, keyspec);
		return c.doFinal(ciphertext);
	}

	// 解密
	public static String DecryptBase64NoKey2Str(String cyphertext)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		SecretKeySpec keyspec = new SecretKeySpec(GetSecretKey().getBytes(),
				"AES");
		Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
		c.init(Cipher.DECRYPT_MODE, keyspec);
		return new String(c.doFinal(Base64.decodeBase64(cyphertext)));
	}

}
