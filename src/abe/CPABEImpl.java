package abe;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import javax.crypto.Cipher;

import abe.serialize.SerializeUtils;
import aes.AES;
import ui.panel;

public class CPABEImpl {
	private static Pairing pairing = MyPairUtils.pairing;
	public static boolean debug = false;
	
	public static void setup(String PKFileName, String MKFileName){
		File PKFile = new File(PKFileName);
		File MKFile = new File(MKFileName);
		if(!PKFile.exists()){
			try {
				PKFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if(!MKFile.exists()){
			try {
				MKFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		PublicKey PK = new PublicKey();
		MasterKey MK = new MasterKey();
		
		Element alpha  = pairing.getZr().newElement().setToRandom();
		PK.g           = pairing.getG1().newElement().setToRandom();
		PK.y = pairing.getG2().newElement().setToRandom();
		MK.beta        = pairing.getZr().newElement().setToRandom();
		MK.g_alpha     = PK.y.duplicate().powZn(alpha);
		PK.h           = PK.g.duplicate().powZn(MK.beta);
		PK.egy_alpha = pairing.pairing(PK.g, MK.g_alpha);
		
		SerializeUtils.serialize(PK, PKFile);
		SerializeUtils.serialize(MK, MKFile);
	}
	
	public static void setup(){
		String pk = "PubKey";
		String mk = "MasterKey";
		setup(pk, mk);
	}

	public static void keygen(String[] attrs, PublicKey PK, MasterKey MK, String SKFileName){
		if(SKFileName == null || SKFileName.trim().equals("")){
			SKFileName = "SecretKey";
		}
		File SKFile = createNewFile(SKFileName);
		SecretKey SK = new SecretKey();
		Element r = pairing.getZr().newElement().setToRandom();
		Element g_r = PK.y.duplicate().powZn(r);
		SK.D = MK.g_alpha.duplicate().mul(g_r);
		Element beta_inv = MK.beta.duplicate().invert();
		SK.D.powZn(beta_inv);
		SK.comps = new SecretKey.SKComponent[attrs.length];
		
		for(int i=0; i<attrs.length; i++){
			Element rj = pairing.getZr().newElement().setToRandom();
			Element hash = pairing.getG2().newElementFromBytes(attrs[i].getBytes()).powZn(rj);
			SK.comps[i] = new SecretKey.SKComponent();
			SK.comps[i].attr = attrs[i];
			SK.comps[i].Dj = g_r.mul(hash);
			SK.comps[i]._Dj = PK.y.duplicate().powZn(rj);
		}
		
		SerializeUtils.serialize(SK, SKFile);
	}
	
	public static File createNewFile(String fileName){
//		System.out.println("sign"+fileName);
		File f = new File(fileName);
		
		if(!f.exists()){
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else{
			try {
				String path = f.getCanonicalPath();
				f.delete();
				f = new File(path);
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return f;
	}
	
	public static void enc(File file, Policy p, PublicKey PK, String outputFileName){
		File ciphertextFile = createNewFile(outputFileName);
		Element m = PairingManager.defaultPairing.getGT().newRandomElement();
		if(debug){
			System.out.println("in function enc() m:" + m);
		}
		Element s = pairing.getZr().newElement().setToRandom();
		fill_policy(p, s, PK);
		Ciphertext ciphertext = new Ciphertext();
		ciphertext.p = p;
		//此处m.duplicate()是为了后面AES加密中还需要用到m
		ciphertext.C = m.duplicate().mul(PK.egy_alpha.duplicate().powZn(s));
		ciphertext._C = PK.h.duplicate().powZn(s);
		
		SerializeUtils.serialize(ciphertext, ciphertextFile);
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(file);
			fos = new FileOutputStream(ciphertextFile, true);
			AES.crypto(Cipher.ENCRYPT_MODE, fis, fos, m);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}finally{
			try {
				fis.close();
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static Element dec(Ciphertext ciphertext, SecretKey SK, PublicKey PK, String time, String location){
		check_sat(SK, ciphertext.p, time, location);
		if(ciphertext.p.satisfiable != 1){
			System.err.println("权限不足！属性私钥不满足访问策略!");
			return null;
		}
		pick_sat_min_leaves(ciphertext.p, SK);
		Element r = dec_flatten(ciphertext.p, SK, PK, time, location);
		Element m = ciphertext.C.duplicate().mul(r);
		r = pairing.pairing(SK.D, ciphertext._C);
		r.invert();
		m.mul(r);
		
		if(debug){
			System.out.println("In function dec() m:" + m);
		}
		System.out.println("dec.m = " + m);
		return m;
	}
	
	private static Element dec_flatten(Policy p, SecretKey SK, PublicKey PK, String time, String location){
		Element r = pairing.getGT().newElement().setToOne();
		Element one = pairing.getZr().newElement().setToOne();
		dec_node_flatten(r, one, p, SK, PK, time, location);
		return r;
	}
	
	private static void dec_node_flatten(Element r, Element exp, 
			Policy p, SecretKey SK, PublicKey PK, String time, String location){
		assert(p.satisfiable == 1);
		if(p.children == null || p.children.length == 0){
			dec_leaf_flatten(r, exp, p, SK, PK, time, location);
		}
		else{
			dec_internal_flatten(r, exp, p, SK, PK, time, location);
		}
	}
	
	private static void dec_leaf_flatten(Element r, Element exp, 
			Policy p, SecretKey SK, PublicKey PK, String time, String location){
		SecretKey.SKComponent comp = SK.comps[p.attri];
		Element s = MyPairUtils.pairing(comp.Dj, p.Cy);
		Element t = MyPairUtils.pairing(comp._Dj, p._Cy);
		t.invert();
		s.mul(t);
		if (p.attr.equals("time")) {
			Element __Dt = MyPairUtils.MRDFNarrow(SK.time.__Dj, PK.lambda, PK.mu, SK.time.begin, SK.time.end, time);
			Element __Ct = MyPairUtils.MRDFNarrow(p.time.__Dj, PK.lambda, PK.mu, p.time.begin, p.time.end, time);
			Element u = MyPairUtils.pairing(SK.time.Dj, __Ct.duplicate().mul(p.time.Dj));
			Element d = MyPairUtils.pairing(SK.time._Dj.duplicate().mul(__Dt), p.time._Dj);
			d.invert();
			u.mul(d);
			s.mul(u);
		}
		if (p.attr.equals("location")) {
			Element lambda_l = PK.lambda.duplicate().pow(new BigInteger(location).subtract(new BigInteger(SK.location.begin)));
			Element mu_l = PK.mu.duplicate().pow(new BigInteger(SK.location.end).subtract(new BigInteger(location)));
			Element __Dl = SK.location.__Dj.duplicate().powZn(lambda_l.duplicate().mul(mu_l));
			lambda_l = PK.lambda.duplicate().pow(new BigInteger(location).subtract(new BigInteger(p.location.begin)));
			mu_l = PK.mu.duplicate().pow(new BigInteger(p.location.end).subtract(new BigInteger(location)));
			Element __Cl = p.location.__Dj.duplicate().powZn(lambda_l.duplicate().mul(mu_l));
			Element u = MyPairUtils.pairing(SK.location.Dj, __Cl.mul(p.location.Dj));
			Element d = MyPairUtils.pairing(SK.location._Dj.duplicate().mul(__Dl), p.location._Dj);
			d.invert();
			u.mul(d);
			s.mul(u);
		}
		s.powZn(exp);
		r.mul(s);
	}
	
	private static void dec_internal_flatten(Element r, Element exp,
			 Policy p, SecretKey SK, PublicKey PK, String time, String location){
		int i;
		Element t;
		Element expnew;
		Element zero = MyPairUtils.pairing.getZr().newElement().setToZero();
		
		for(i=0; i<p.satl.size(); i++){
			t = lagrange_coef(p.satl, p.satl.get(i), zero);
			expnew = exp.duplicate().mul(t);    //注意这里的duplicate
			dec_node_flatten(r, expnew, p.children[p.satl.get(i)-1], SK, PK, time, location);
		}
	}

	
	private static void pick_sat_min_leaves(Policy p, SecretKey SK){
		int i,k,l;
		int size = p.children == null ? 0 : p.children.length;
		Integer[] c;
		assert(p.satisfiable == 1);
		if(p.children == null || p.children.length == 0){
			p.min_leaves = 1;
		}
		else{
			for(i=0; i<p.children.length; i++){
				if(p.children[i].satisfiable == 1){
					pick_sat_min_leaves(p.children[i], SK);
				}
			}
			
			c = new Integer[p.children.length];
			for(i=0; i<size; i++){
				c[i] = i;
			}
			
			Arrays.sort(c, new PolicyInnerComparator(p));
			p.satl = new ArrayList<Integer>();
			p.min_leaves = 0;
			l = 0;
			for(i=0; i<size && l<p.k; i++){
				if(p.children[i].satisfiable == 1){
					l++;
					p.min_leaves += p.children[i].min_leaves;
					k = c[i] + 1;
					p.satl.add(k);
				}
			}
			assert(l == p.k);
		}
	}
	
	private static class PolicyInnerComparator implements Comparator<Integer>{
		Policy p;
		public PolicyInnerComparator(Policy p){
			this.p = p;
		}
		
		@Override
		public int compare(Integer o1, Integer o2) {
			int k, l;
			k = p.children[o1].min_leaves;
			l = p.children[o2].min_leaves;
			return k < l ? -1 : k == l ? 0 : 1;
		}
		
	}
	
	private static void check_sat(SecretKey SK, Policy p, String time, String location){
		int i,l;
		int size = p.children == null ? 0 : p.children.length;
		p.satisfiable = 0;
		if(p.children == null || size == 0){
			if (p.attr.equals("time")) {
				BigInteger time_int = new BigInteger(time);
				if (time_int.compareTo(new BigInteger(p.time.begin)) < 0 ||
				time_int.compareTo(new BigInteger(p.time.end)) > 0) {
					p.satisfiable = 0;
					return;
				}
			}
			if (p.attr.equals("location")) {
				BigInteger loc_int = new BigInteger(location);
				if (loc_int.compareTo(new BigInteger(p.location.begin)) < 0 ||
						loc_int.compareTo(new BigInteger(p.location.end)) > 0) {
					p.satisfiable = 0;
					return;
				}
			}
			for (i = 0; i < SK.comps.length; i++) {
				if (SK.comps[i].attr.equals(p.attr)) {
					p.satisfiable = 1;
					p.attri = i;
					break;
				}
			}
		}
		else{
			for(i=0; i<size; i++){
				check_sat(SK, p.children[i], time, location);
			}
			
			l = 0;
			for(i=0; i<size; i++){
				if(p.children[i].satisfiable == 1){
					l++;
				}
			}
			if(l >= p.k){
				p.satisfiable = 1;
			}
		}
	}
	
	
	public static void fill_policy(Policy p, Element e, PublicKey PK){
		int i;
		int size = p.children == null ? 0 : p.children.length;
		Element r, t;
		p.q = rand_poly(p.k - 1, e);
		if(p.children == null || size == 0){
			p.Cy = PK.g.duplicate().powZn(p.q.coef.get(0));
			p._Cy = MyPairUtils.getG1FromByte(p.attr.getBytes()).powZn(p.q.coef.get(0));
		}
		else{
			for(i=0; i<size; i++){
				r = pairing.getZr().newElement().set(i+1);
				t = Polynomial.eval_poly(p.q, r);
				fill_policy(p.children[i], t, PK);
			}
		}
	}
	
	public static Polynomial rand_poly(int deg, Element zero_val){
		int i;
		Polynomial q = new Polynomial();
		q.deg = deg;
		q.coef = new ArrayList<Element>();

		q.coef.add(zero_val.duplicate());
		for(i=1; i<q.deg+1; i++){
			q.coef.add(pairing.getZr().newElement().setToRandom());
		}
		
		return q;
	}
	
	public static Element lagrange_coef(List<Integer> S, int i, Element x){
		int j,k;
		Element r = pairing.getZr().newElement().setToOne();
		Element t;
		for(k=0; k<S.size(); k++){
			j = S.get(k);
			if(j == i){
				continue;
			}
			t = x.duplicate().sub(pairing.getZr().newElement().set(j));   //注意这里的duplicate
			r.mul(t);
			t.set(i-j).invert();
			r.mul(t);
		}
		return r;
	}
}
