package abe;

import abe.serialize.Serializable;
import abe.serialize.SimpleSerializable;
import it.unisa.dia.gas.jpbc.Element;

public class Ciphertext implements SimpleSerializable{
	@Serializable
	Policy p;
	@Serializable(group="GT")
	Element C; //GT
	@Serializable(group="G1")
	Element _C;  //G1
}
