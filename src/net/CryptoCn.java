package net;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.crypto.*;
import javax.crypto.spec.*;

import org.apache.commons.codec.binary.*;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.*;

import start.Info;

@SuppressWarnings("unused")
public class CryptoCn
{
	  
  
		enum E_CODE
		{
			RSA ,
			RSA_EP,
			AES
		}
		
	  private static String rsa_key = "";	//a
	  private static String rsa_ep_key = ""; 	//b
	  private static String aes_key = "";		//c
	  
	  public static List<String> game_rsa_key = new ArrayList<String>();	  
	  public static boolean ON = false;
	  
	  public static String BaseSecretKey2 = "A1dPUcrvur2CRQyl";  //cn?tw? key1
	  public static String BaseSecretKeyCN = "rBwj1MIAivVN222b";  //cn?tw? key2
	  
	  public static byte[] BaseSecretKey = BaseSecretKeyCN.getBytes();  //cn?tw? key2
	  
	  public static void reset()
	  {
		  BaseSecretKey = BaseSecretKeyCN.getBytes();
	  }
	  
	  public CryptoCn()
	  {
		  
	
		  game_rsa_key.add(0,"MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBANV2ohKiVs/2cOiGN7TICmQ/NbkuellbTtcKbuDbIlBMocH+Eu0n2nBYZQ2xQbAv+E9na8K2FyMyVY4+RIYEJ+0CAwEAAQ==");
		  game_rsa_key.add(1,"MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAOLtTe70uQZ2BAneeTyNezMH/yn/uDu6qabQ3XHhmqqW8C4ZLxG7uW6bNmUdZQSUk8dO2+7ZTbN5lQw/u70Av2ECAwEAAQ==");
		  game_rsa_key.add(2,"MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAM5U06JAbYWdRBrnMdE2bEuDmWgUav7xNKm7i8s1Uy/fvpvfxLeoWowLGIBKz0kDLIvhuLV8Lv4XV0+aXdl2j4kCAwEAAQ==");
		  game_rsa_key.add(3,"MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAL1mnz5vCQEa1xPeyIUQ2WHIzKIsZp9PKAzJ6etXV2NpyxoGheqlDZ5rXQVLEY2JSY2nBlt/QBdo9xkp8XcFgUsCAwEAAQ==");
		  game_rsa_key.add(4,"MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAKFTx5sYAmW9kFineXZj6NwGPGA6QSgui+nwb9ru30oeoYviC6e5iHD/Qk7Gc8JPpIA335YHo6K1/U8U9gM3BncCAwEAAQ==");
		  game_rsa_key.add(5,"MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAL3EP/qaJ9XGmpEia4KqoJkCYFvgpJtWK3zPZ7d/qCF1GbQSSzPI+FUnuJjAXSZ43vvYYmQNHNYg791C9SrSOT0CAwEAAQ==");
		  game_rsa_key.add(6,"MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBANWNwx1kRSlR5sl3dHkPtW//F5KlRQMPWbrLG3ZyCI1q3NUMOC+xdC87gGiINs4WC3S28j0/DrrocIXS7zf2MdECAwEAAQ==");
		  game_rsa_key.add(7,"MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBANzMvdAQ/lmyAQQ3S0B1BkzlwvR8mYrCiATLRV0t/HeudLvhUgbkWm2UNr4M84f0wA52XqFPABMyp+o59D4DEwUCAwEAAQ==");
		  game_rsa_key.add(8,"MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBANr/4m+Z7qKlr2kmyZmgNjf49LSgm6QP5JZwk+Wi2m8E68sUMyfKkhr1t2OXlvNAEfQrSYHu6rlXqpSf2o1zvSkCAwEAAQ==");
		  game_rsa_key.add(9,"MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBANqJlJznVfrsXd/Nnn4L7E7Kcz2u1YwIExrJC3uyxsEk+HiCnNJ8ZUFtkc7XeZKEyw2UFfiQ73SOFAzhVfkCCS0CAwEAAQ==");
		  

		  this.set_rsa_key(game_rsa_key.get(Info.key));
	  }
	  
  public static byte[] decode(byte[] paramArrayOfByte)
  {
    return decode(paramArrayOfByte, BaseSecretKey);
  }

  public static byte[] decode(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2)
  {
    SecretKeySpec localSecretKeySpec = new SecretKeySpec(paramArrayOfByte2, "AES");
    try
    {
      Cipher localCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      localCipher.init(Cipher.DECRYPT_MODE, localSecretKeySpec);
      byte[] arrayOfByte = localCipher.doFinal(paramArrayOfByte1);
      return arrayOfByte;
    }
    catch (Exception localException)
    {
    	localException.printStackTrace();
    }
    return null;
  }

  public static byte[] decode64(byte[] paramArrayOfByte)
  {
    SecretKeySpec localSecretKeySpec = new SecretKeySpec(BaseSecretKey, "AES");
	try
    {
      byte[] arrayOfByte1 = Base64.decodeBase64(paramArrayOfByte);
      
      Cipher localCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
      localCipher.init(Cipher.DECRYPT_MODE, localSecretKeySpec);
      byte[] arrayOfByte2 = localCipher.doFinal(arrayOfByte1);
      return arrayOfByte2;
    }
    catch (Exception localException)
    {
    	localException.printStackTrace();
    }
    return null;
  }
  

  public static byte[] encode(String paramString)
  {
    return encode(paramString, BaseSecretKey);
  }

  public static byte[] encode(String paramString, byte[] paramArrayOfByte)
  {
    SecretKeySpec localSecretKeySpec = new SecretKeySpec(paramArrayOfByte, "AES");
    try
    {
      byte[] arrayOfByte1 = paramString.getBytes();
      Cipher localCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      localCipher.init(Cipher.ENCRYPT_MODE, localSecretKeySpec);
      byte[] arrayOfByte2 = localCipher.doFinal(arrayOfByte1);
      return arrayOfByte2;
    }
    catch (Exception localException)
    {
    }
    return null;
  }

  public static String encodeWithBase64(String paramString)
  {
    SecretKeySpec localSecretKeySpec = new SecretKeySpec(BaseSecretKey, "AES");
    try
    {
      byte[] arrayOfByte = paramString.getBytes();
      Cipher localCipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
      localCipher.init(Cipher.ENCRYPT_MODE, localSecretKeySpec);
      String str = Base64.encodeBase64String(localCipher.doFinal(arrayOfByte));
      return str;
    }
    catch (Exception localException)
    {
    }
    return null;
  }

  public static String getCryptUrl(String paramString)
  {
    StringBuffer localStringBuffer = new StringBuffer(paramString);
    if (paramString.indexOf("?") == -1)
     {
    	localStringBuffer.append("?");
     }else{
    	 localStringBuffer.append("&"); 
     }
      localStringBuffer.append("cyt=1");
      return localStringBuffer.toString();
  }	

  public static void setKey(String paramString)
  {
    System.out.println(String.format("> set K C:%s", new Object[] { paramString }));
    BaseSecretKey = paramString.getBytes();
  }
    
/* -- 12.17 */
public static String BaseSecretKeyNew = "011218525486l6u1"; // cn ios key  

  
public List<NameValuePair> addnew_crypt_K_param(List<NameValuePair> param, String url)
{
	ArrayList<NameValuePair> localArrayList = new ArrayList<NameValuePair>();

	generate_new_aes_key();

	localArrayList.add(new BasicNameValuePair("K", get_K()));

	Iterator<NameValuePair> i  = param.iterator();
	boolean is_login = false;
	
	if(url.indexOf("login?") != -1 || url.indexOf("regist?") != -1)
	{
		is_login = true;
		ON=true;
	}
	while(i.hasNext()) {

		NameValuePair n = i.next();
		String val = Base64.encodeBase64String(new_encrypt_cn(E_CODE.AES,n.getValue()));
		if(is_login == true){
			localArrayList.add(new BasicNameValuePair(n.getName(), Base64.encodeBase64String(new_encrypt_cn(E_CODE.RSA_EP, val))));
		}else{
			localArrayList.add(new BasicNameValuePair(n.getName(), val));
		}
		
	}
	return localArrayList;
}

public String get_K()
{
    if (rsa_key.length() < 2)
    	return null;
    Cipher localCipher;
    byte[] arrayOfByte = null; 

    PublicKey localPublicKey;
	try {
		byte[] b = Base64.decodeBase64(rsa_key);
		X509EncodedKeySpec sp = new X509EncodedKeySpec(b);
		localPublicKey = KeyFactory.getInstance("RSA").generatePublic(sp);
		localCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
	    localCipher.init(Cipher.ENCRYPT_MODE, localPublicKey);
	    arrayOfByte = localCipher.doFinal(aes_key.getBytes("utf-8"));
	} catch (Exception e) {
		e.printStackTrace();
		return null;
	} 
    return Base64.encodeBase64String(arrayOfByte);
}
  
public void set_rsa_key(String key)
{
	rsa_key = key;
}

public void set_rsa_ep_key(String key)
{
	rsa_ep_key = key;
}

public void generate_new_aes_key() {
	
	try
    {
        KeyGenerator localKeyGenerator = KeyGenerator.getInstance("AES");
        localKeyGenerator.init(128, SecureRandom.getInstance("SHA1PRNG"));
        aes_key = Base64.encodeBase64String(localKeyGenerator.generateKey().getEncoded());
      }
      catch (NoSuchAlgorithmException localNoSuchAlgorithmException)
      {

      }
    return;
}

public  byte[] new_encrypt_cn(E_CODE paramE_CODE, String val)
{
	byte[] localObject = null;
  try
  {
	if(paramE_CODE == E_CODE.RSA)
	{	
		if (rsa_key.length() > 2)
		{
		    Cipher localCipher;
		    byte[] arrayOfByte = null;
		    PublicKey localPublicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.decodeBase64(rsa_key)));
		    localCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		    localCipher.init(Cipher.ENCRYPT_MODE, localPublicKey);
		    arrayOfByte = localCipher.doFinal(val.getBytes());
		    return arrayOfByte;
		}
		return null;
	}
	else if (paramE_CODE == E_CODE.RSA_EP)
    {
      if (rsa_key.length() >= 2)
      {
    	    Cipher localCipher;
    	    PublicKey localPublicKey;
    	    
  		byte[] b = Base64.decodeBase64(rsa_key);
  		X509EncodedKeySpec sp = new X509EncodedKeySpec(b);
  		localPublicKey = KeyFactory.getInstance("RSA").generatePublic(sp);
  		localCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
  	    localCipher.init(Cipher.ENCRYPT_MODE, localPublicKey);
  	  localObject = localCipher.doFinal(val.getBytes("utf-8"));
      }
    }
    else if (paramE_CODE == E_CODE.AES)
    {
      SecretKeySpec localSecretKeySpec = new SecretKeySpec(Base64.decodeBase64(aes_key.getBytes()), "AES");
      Cipher localCipher1 = Cipher.getInstance("AES/ECB/PKCS5Padding");
      localCipher1.init(Cipher.ENCRYPT_MODE, localSecretKeySpec);
      byte[] arrayOfByte2 = localCipher1.doFinal(val.getBytes());
      localObject = arrayOfByte2;
    }
  }
  catch (Exception localException)
  {
	  return null;
  }
  return localObject;
}
  

public  byte[] new_decrypt_cn(E_CODE paramE_CODE, byte[] paramArrayOfByte)
{
	byte[] localObject = null;
  try
  {
	if(paramE_CODE == E_CODE.RSA)
	{	
		if (rsa_key.length() > 2)
		{
		    Cipher localCipher;
		    byte[] arrayOfByte = null;
		    PublicKey localPublicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(Base64.decodeBase64(rsa_key)));
		    localCipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
		    localCipher.init(Cipher.DECRYPT_MODE, localPublicKey);
		    arrayOfByte = localCipher.doFinal(paramArrayOfByte);
		    return arrayOfByte;
		}
	}
	else if (paramE_CODE == E_CODE.RSA_EP)
    {
      if (rsa_ep_key.length() >= 2)
      {
        PrivateKey localPrivateKey = KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(Base64.decodeBase64(rsa_ep_key)));
        Cipher localCipher2 = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        localCipher2.init(2, localPrivateKey);
        localObject = localCipher2.doFinal(paramArrayOfByte);
      }
    }
    else if (paramE_CODE == E_CODE.AES)
    {
      SecretKeySpec localSecretKeySpec = new SecretKeySpec(Base64.decodeBase64(aes_key.getBytes()), "AES");
      byte[] arrayOfByte1 = Base64.decodeBase64(paramArrayOfByte);
      Cipher localCipher1 = Cipher.getInstance("AES/ECB/PKCS5Padding");
      localCipher1.init(2, localSecretKeySpec);
      byte[] arrayOfByte2 = localCipher1.doFinal(arrayOfByte1);
      localObject = arrayOfByte2;
    }
  }
  catch (Exception localException)
  {
	  return null;
  }
  return localObject;
}

public static void set_dynamic_aes_key_json(String ret) {
	BaseSecretKey = BaseSecretKeyNew.getBytes();
	
    
    try
    {

      JSONObject jo = new JSONObject(ret);
      JSONArray words = jo.getJSONArray("base_keyword");
      String hex = jo.getString("base_hex");
      String number = jo.getString("base_number");
      JSONArray pattern = jo.getJSONArray("base_pattern");
      
      String str2 = words.getString(0);


    }
    catch (Exception localException)
    {
    }
	
}


}