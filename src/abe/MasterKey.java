package abe;

import abe.serialize.Serializable;
import abe.serialize.SimpleSerializable;
import it.unisa.dia.gas.jpbc.Element;

public class MasterKey implements SimpleSerializable{
	@Serializable(group = "Zr")
	Element alpha;

	@Serializable(group = "Zr")
	Element beta;
	
	@Serializable(group="G1")
	Element g_alpha;
}
