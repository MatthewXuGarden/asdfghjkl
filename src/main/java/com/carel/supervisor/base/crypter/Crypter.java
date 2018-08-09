package com.carel.supervisor.base.crypter;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;


public class Crypter
{
    private static final String PUBLIC_KEY = "8de7066f67be16fcacd05d319b6729cd85fe698c07cec504776146eb7a041d9e3cacbf0fcd86441981c0083eed1f8f1b18393f0b186e47ce1b7b4981417b491";
    private static final String PRIVATE_KEY = "59fed719f8959a468de367f77a33a7536d53b8e4d25ed49ccc89a94cd6899da90415623fb73386e9635034fb65ad5f248445a1c66703f760d64a8271ad342b1";
    private static final int HEXADECIMAL = 16;

    private Crypter()
    {	
    }
    
    public static String encryptMD(String clearPassword, String cryptingMethod)
        throws NoSuchAlgorithmException
    {
        MessageDigest messageDigest = null;
        messageDigest = MessageDigest.getInstance(cryptingMethod);

        // SHA = Secure Hash Algorithm, standard del NIST
        BASE64Encoder enc = new BASE64Encoder();
        messageDigest.update(clearPassword.getBytes());

        byte[] impronta = messageDigest.digest();

        return enc.encode(impronta);
    } //MDCrypter

    public static BigInteger decryptRSA(BigInteger message)
    {
        BigInteger n = new BigInteger(PUBLIC_KEY, HEXADECIMAL);
        BigInteger d = new BigInteger(PRIVATE_KEY, HEXADECIMAL);

        return message.modPow(d, n);
    } //KKCrypter

    public static BigInteger encryptRSA(BigInteger message)
    {
        BigInteger n = new BigInteger(PUBLIC_KEY, HEXADECIMAL);
        BigInteger e = new BigInteger("10001", HEXADECIMAL);

        return message.modPow(e, n);
    } //if
    
    public static String encryptRSA4DB(String clear)
    {
    	byte[] b = clear.getBytes();
    	BigInteger  biClear = encryptRSA(new BigInteger(b));
    	BASE64Encoder enc = new BASE64Encoder();
    	clear = enc.encode(biClear.toByteArray());
    	return clear;
    }
    
    public static String decryptRAS4DB(String encode)
    {
    	String ret = "";
    	try
    	{
    		byte[] b = encode.getBytes();
    		BASE64Decoder denc = new BASE64Decoder();
    		BigInteger biCrypted = Crypter.decryptRSA(new BigInteger(denc.decodeBuffer(encode)));
    		ret = new String(biCrypted.toByteArray());
    	}
    	catch(Exception e){
    		ret = ""; 
    	}
    	return ret;
    }
    
    public static void main(String[] args) {
		System.out.println(Crypter.encryptRSA4DB("30"));
	}
}
