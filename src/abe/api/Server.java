package abe.api;

import abe.CPABEImplWithoutSerialize;
import abe.CPABEImplWithoutSerialize.KeyPair;
import abe.SecretKey;


public class Server {
	private static KeyPair pair;
	
	public Server(){
		this.pair = CPABEImplWithoutSerialize.setup();
	}
	
	public String getPublicKeyInString(){
		return pair.getPKJSONString();
	}
	
	public SecretKey generateSecretKey(String id, String[] attrs){
		return CPABEImplWithoutSerialize.keygen(id, attrs, pair.getPK(), pair.getMK());
	}
	
	public String genTLKey(String time, String loc) {
		return CPABEImplWithoutSerialize.TLkeygen(time, loc, pair.getPK(), pair.getMK());
	}
}
