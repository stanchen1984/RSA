package Stan.rsa;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;

public class KeyStore implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	long id;
	String name;
	String CAName;
	PublicKey pubKey;
	PrivateKey priKey;
	
	
	
	public KeyStore() {
		super();
	}

	public KeyStore(long id, String name, String cAName, PublicKey pubKey,
			PrivateKey priKey) {
		super();
		this.id = id;
		this.name = name;
		CAName = cAName;
		this.pubKey = pubKey;
		this.priKey = priKey;
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
	public PrivateKey getPriKey() {
		return priKey;
	}
	public void setPriKey(PrivateKey priKey) {
		this.priKey = priKey;
	}

	@Override
	public String toString() {
		return "KeyStore [id=" + id + ", name=" + name + ", CAName=" + CAName
				+ ", pubKey=" + pubKey + ", priKey=" + priKey + "]";
	}
}
