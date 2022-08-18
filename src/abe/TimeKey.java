package abe;

import abe.serialize.Serializable;
import abe.serialize.SimpleSerializable;
import it.unisa.dia.gas.jpbc.Element;

public class TimeKey implements SimpleSerializable{
	@Serializable
	String begin;

	@Serializable
	String end;

	@Serializable(group="G2")
	Element Dj;
	@Serializable(group="G2")
	Element _Dj;
	@Serializable(group = "G2")
	Element __Dj;
}
