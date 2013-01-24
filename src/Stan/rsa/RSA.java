package Stan.rsa;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.SignatureException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class RSA {
	String path = "./";
	public KeyStore keyStore;
	KeyTrust keyTrust;
	int bitlength = 2048;
	
	
	public void generateNewKeyPair (long id, String name, String caname){
		KeyPair kp = generateNewKeyPair(bitlength);
		keyStore = new KeyStore (id, name, caname, kp.getPublic(), kp.getPrivate());
		keyTrust = new KeyTrust (id, name, caname, kp.getPublic(), null);
	}
	
	private KeyPair generateNewKeyPair(int bitLength){
		KeyPairGenerator kpg;
		try {
			kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(bitLength);
		    KeyPair kp = kpg.generateKeyPair();
		    dumpKeyPair(kp);
		    return kp;		    
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	private void dumpKeyPair(KeyPair keyPair) {
		PublicKey pub = keyPair.getPublic();
		System.out.println("Public Key: " + getHexString(pub.getEncoded()));
 
		PrivateKey priv = keyPair.getPrivate();
		System.out.println("Private Key: " + getHexString(priv.getEncoded()));
	}
	
	private String getHexString(byte[] b) {
		String result = "";
		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}
	
	public void SaveKeyStore(String path) throws IOException {		
		try{
			 
			FileOutputStream fout = new FileOutputStream(path + keyStore.name + ".KeyStore");
			ObjectOutputStream oos = new ObjectOutputStream(fout);   
			oos.writeObject(keyStore);
			oos.close();
			System.out.println("Key Store Saved");
	 	   }catch(Exception ex){
			   ex.printStackTrace();
		   }
	}
	
	public void LoadKeyStore(String path, String myName){
		
		try{			 
			   FileInputStream fin = new FileInputStream(path + myName + ".KeyStore");
			   ObjectInputStream ois = new ObjectInputStream(fin);
			   keyStore = (KeyStore) ois.readObject();
			   ois.close();	 
			   System.out.println("Key store load.");
		   }catch(Exception ex){
			   ex.printStackTrace();
		   } 
	}
	
	public void SaveKeyTrust (String path, long id){
		try{			 
			FileOutputStream fout = new FileOutputStream(path + id + ".KeyTrust");
			ObjectOutputStream oos = new ObjectOutputStream(fout);   
			oos.writeObject(keyTrust);
			oos.close();
			System.out.println("Key Strust Saved for id: " + id);
	 	   }catch(Exception ex){
			   ex.printStackTrace();
		   }
	}
	
	public void LoadKeyTrust (String path, long id){
		try{			 
			   FileInputStream fin = new FileInputStream(path + id + ".KeyTrust");
			   ObjectInputStream ois = new ObjectInputStream(fin);
			   keyTrust = (KeyTrust) ois.readObject();
			   ois.close();	 
			   System.out.println("Key trust load");
		   }catch(Exception ex){
			   ex.printStackTrace();
		   } 
	}
	
	
	public byte[] Encryption (String msg) {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			SecureRandom random = new SecureRandom();
			try {
				cipher.init(Cipher.ENCRYPT_MODE, keyTrust.getPubKey(), random);
				try {
					byte[] cipherText = cipher.doFinal(msg.getBytes());
					System.out.println("cipher: " + new String(cipherText));
					return cipherText;
				} catch (IllegalBlockSizeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (BadPaddingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	public String Decryption (byte[] cipherText){
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			try {
				cipher.init(Cipher.DECRYPT_MODE, keyStore.getPriKey());
				try {
					byte[] plainText = cipher.doFinal(cipherText);
					return new String (plainText);
				} catch (IllegalBlockSizeException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (BadPaddingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public byte[] Sign (byte[] data) throws InvalidKeyException, SignatureException{
		try {
			Signature sig = Signature.getInstance("MD5WithRSA");
			sig.initSign(keyStore.getPriKey());			
			sig.update(data);
		    byte[] signatureBytes = sig.sign();
		    return signatureBytes;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public boolean VerifySign (byte[] data, byte[] sign) throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
		Signature sig = Signature.getInstance("MD5WithRSA");
		sig.initVerify(keyTrust.getPubKey());	
		sig.update(data);
		return sig.verify(sign);
	}
	
  public static void main(String[] args) throws Exception {
   
    RSA rsa = new RSA();
    long id = 1;
    String name = "client1";
    String caname = "server";
    rsa.generateNewKeyPair(id, name, caname);
    String path = "./";
    rsa.SaveKeyStore(path);
    rsa.SaveKeyTrust(path, id);
    String msg = "This is a testing message for this demo";
    rsa.LoadKeyStore(path, name);
    rsa.LoadKeyTrust(path, id);
    byte cipher[] = rsa.Encryption(msg);
    System.out.println(rsa.Decryption(cipher));
    byte[] sig = rsa.Sign(cipher);
    System.out.println("Signature: " + rsa.getHexString(sig));
    System.out.println("verify siganture: " + rsa.VerifySign(cipher, sig));
    
  }
}