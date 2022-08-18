package abe;

import abe.serialize.Serializable;
import abe.serialize.SimpleSerializable;
import it.unisa.dia.gas.jpbc.Element;

public class SecretKey implements SimpleSerializable{
	@Serializable(group = "G1")
	Element D;

	@Serializable(group = "G2")
	Element _D;
	
	@Serializable
	SKComponent[] comps;

	@Serializable
	TimeKey time;

	@Serializable
	LocationKey location;
	
	public static class SKComponent implements SimpleSerializable{
		@Serializable
		String attr;
		@Serializable(group="G1")
		Element Dj;
		@Serializable(group="G2")
		Element _Dj;
	}
}
