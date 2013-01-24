package Stan.rsa;


import java.io.Serializable;
import java.security.PublicKey;

public class KeyTrust implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	long id;
	String name;
	String CAName;
	PublicKey pubKey;
	byte[] signature;
	
	
	
	public KeyTrust() {
		super();
	}

	public KeyTrust(long id, String name, String cAName, PublicKey pubKey, byte[] signature) {
		super();
		this.id = id;
		this.name = name;
		CAName = cAName;
		this.pubKey = pubKey;
		this.signature = signature;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCAName() {
		return CAName;
	}
	public void setCAName(String cAName) {
		CAName = cAName;
	}
	public PublicKey getPubKey() {
		return pubKey;
	}
	public void setPubKey(PublicKey pubKey) {
		this.pubKey = pubKey;
	}
	
	public byte[] getSignature() {
		return signature;
	}

	public void setSignature(byte[] signature) {
		this.signature = signature;
	}
	
	private String getHexString(byte[] b) {
		String result = "";
		for (int i = 0; i < b.length; i++) {
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		}
		return result;
	}

	@Override
	public String toString() {
		return "KeyTrust [id=" + id + ", name=" + name + ", CAName=" + CAName
				+ ", pubKey=" + pubKey + ", signature="
				+ getHexString(signature) + "]";
	}
}
