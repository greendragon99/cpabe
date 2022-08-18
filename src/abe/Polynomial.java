package abe;

import it.unisa.dia.gas.jpbc.Element;

import java.util.List;


public class Polynomial {
	//最高次项
	int deg;
	//系数
	List<Element> coef; /* coefficients from [0] x^0 to [deg] x^deg */
						/* G_T (of length deg + 1) */
	
	public static Element eval_poly(Polynomial q, Element x){
		//用来带入x的具体值进行计算
		int i;
		Element s, t, r;

		r = MyPairUtils.pairing.getZr().newElement().setToZero();
		t = MyPairUtils.pairing.getZr().newElement().setToOne();
		
		for(i=0; i<q.deg+1; i++){
			s = q.coef.get(i).duplicate().mul(t);
			r.add(s);
			t.mul(x);
		}
		return r;
	}
}
