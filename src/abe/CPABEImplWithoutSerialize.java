package abe;

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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import ui.panel;

public class CPABEImplWithoutSerialize {
	static Pairing pairing = MyPairUtils.pairing;
	public static class KeyPair{
		PublicKey PK;
		MasterKey MK;
		public PublicKey getPK() {
			return PK;
		}

		public MasterKey getMK() {
			return MK;
		}

		public String getPKJSONString(){
			JSONObject json = new JSONObject();
			byte[] b = SerializeUtils.convertToByteArray(this.PK);
			json.put("PK", b);
			return JSON.toJSONString(json);
		}
	}
	
	public static KeyPair setup(){
		KeyPair pair = new KeyPair();
		PublicKey PK = new PublicKey();
		MasterKey MK = new MasterKey();
		
		Element alpha  = MyPairUtils.getZrGenerator();
		MK.alpha = alpha;
		PK.g = MyPairUtils.getG1SubgroupGenerator(1).getImmutable();
		PK.y = MyPairUtils.getG2Generator().getImmutable();
		MK.beta = MyPairUtils.getZrGenerator();
		MK.g_alpha = PK.g.duplicate().powZn(alpha);
		PK.h = PK.y.duplicate().powZn(MK.beta);
		PK.egy_alpha = MyPairUtils.pairing(PK.g, PK.y).duplicate().powZn(alpha);
		PK.lambda = MyPairUtils.getZrGenerator();
		PK.mu = MyPairUtils.getZrGenerator();

		pair.PK = PK;
		pair.MK = MK;
		
		return pair;
	}
	
	public static SecretKey keygen(String id, String[] attrs, PublicKey PK, MasterKey MK){
		SecretKey SK = new SecretKey();
		//D & D'
		Element r = MyPairUtils.getZrGenerator();
		Element g_r = PK.g.duplicate().powZn(r);
		Element g_alpha = PK.g.duplicate().powZn(MK.alpha);
		SK.D = g_alpha.duplicate().mul(g_r);
		Element idHash = MyPairUtils.getG1FromByte(id.getBytes());
		idHash = idHash.duplicate().powZn(r);
		Element beta_inv = MK.beta.duplicate().invert();
		SK.D = SK.D.duplicate().powZn(beta_inv);
		idHash = idHash.duplicate().powZn(beta_inv);
		SK.D = SK.D.duplicate().mul(idHash);
		SK._D = PK.y.duplicate().powZn(r);
		SK.comps = new SecretKey.SKComponent[attrs.length];

		//Dj & Dj'
		//(gh(uid))^r
		Element gh = MyPairUtils.getG1FromByte(id.getBytes());
		gh = gh.duplicate().mul(PK.g);
		gh = gh.duplicate().powZn(r);
		for(int i=0; i<attrs.length; i++){
			Element rj = MyPairUtils.getZrGenerator();
			Element hash = MyPairUtils.getG1FromByte(attrs[i].getBytes()).powZn(rj);
			SK.comps[i] = new SecretKey.SKComponent();
			SK.comps[i].attr = attrs[i];
			SK.comps[i].Dj = gh.duplicate().mul(hash);
			SK.comps[i]._Dj = PK.y.duplicate().powZn(rj);
		}
		//time
		Element rt = MyPairUtils.getZrGenerator();

		SK.time = new TimeKey();
		Element Dt0 = gh.duplicate().mul(MyPairUtils.getG1FromByte(panel.min_time.getBytes()).powZn(rt));
		Element Dt1 = PK.y.duplicate().powZn(rt);
		Element Dt2 = MyPairUtils.MRDFFunction(rt, PK.lambda, PK.mu, panel.max_time, panel.min_time, panel.max_time);
		SK.time.begin = panel.min_time;
		SK.time.end = panel.max_time;
		SK.time.Dj = Dt0;
		SK.time._Dj = Dt1;
		SK.time.__Dj = Dt2;
		//location
		Element rl = MyPairUtils.getZrGenerator();
		SK.location = new LocationKey();
		Element Dl0 = gh.duplicate().mul(MyPairUtils.getG1FromByte(panel.min_loc.getBytes()).powZn(rl));
		Element Dl1 = PK.y.duplicate().powZn(rl);
		Element Dl2 = MyPairUtils.MRDFFunction(rl, PK.lambda, PK.mu, panel.max_loc, panel.min_loc, panel.max_loc);
		SK.location.begin = panel.min_loc;
		SK.location.end = panel.max_loc;
		SK.location.Dj = Dl0;
		SK.location._Dj = Dl1;
		SK.location.__Dj = Dl2;


		byte[] b = SerializeUtils.convertToByteArray(SK);
		JSONObject json = new JSONObject();
		json.put("SK", b);
		return SK;
	}
	
	public static String TLkeygen(String time, String loc, PublicKey PK, MasterKey MK){
		SecretKey SK = new SecretKey();
		Element r = pairing.getZr().newElement().setToRandom();
		Element g_r = PK.y.duplicate().powZn(r);
		SK.D = MK.g_alpha.duplicate().mul(g_r);
		Element beta_inv = MK.beta.duplicate().invert();
		SK.D.powZn(beta_inv);
		SK.comps = new SecretKey.SKComponent[2];
		
		
		Element rj = pairing.getZr().newElement().setToRandom();
		Element hash = pairing.getG2().newElementFromBytes(time.getBytes()).powZn(rj);
		SK.comps[0] = new SecretKey.SKComponent();
		SK.comps[0].attr = time;
		SK.comps[0].Dj = g_r.mul(hash);
		SK.comps[0]._Dj = PK.y.duplicate().powZn(rj);
		
		rj = pairing.getZr().newElement().setToRandom();
		hash = pairing.getG2().newElementFromBytes(loc.getBytes()).powZn(rj);
		SK.comps[1] = new SecretKey.SKComponent();
		SK.comps[1].attr = loc;
		SK.comps[1].Dj = g_r.mul(hash);
		SK.comps[1]._Dj = PK.y.duplicate().powZn(rj);
		
		
		byte[] b = SerializeUtils.convertToByteArray(SK);
		JSONObject json = new JSONObject();
		json.put("SK", b);
		return JSON.toJSONString(json);
	}
	
	
	public static void enc(File file, Policy p, PublicKey PK, String outputFileName, String dir,
						   String time_begin, String time_end, String location_begin, String location_end){
		//加密算法
		//文件处理
		//确保存在可以写入的文件
		File f = new File(dir + outputFileName);
		File fileParent = f.getParentFile();
        //判断是否存在，如果不存在则创建
        if (!fileParent.exists()) {
            fileParent.mkdirs();
        }
        
		File ciphertextFile = createNewFile(dir + outputFileName);
		Element m = MyPairUtils.getGTGenerator();
		Element s = MyPairUtils.getZrGenerator();
		//生成决策树并进行加密
		fill_policy(p, s, PK, time_begin, time_end, location_begin, location_end);
		Ciphertext ciphertext = new Ciphertext();
		ciphertext.p = p;
		//此处m.duplicate()是为了后面AES加密中还需要用到m
		ciphertext.C = m.duplicate().mul(PK.egy_alpha.duplicate().powZn(s));
		ciphertext._C = PK.h.duplicate().powZn(s);
		
		SerializeUtils.serialize(ciphertext, ciphertextFile);
		FileInputStream fis = null;
		FileOutputStream fos = null;
		System.out.println("enc.m = " + m);
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
		System.out.println("加密成功");
	}
	
	public static void REenc(File enc_file, PublicKey PK, String time, String loc, String outputFileName, String dir) {
		
		File f = new File(dir + outputFileName);
		File fileParent = f.getParentFile();
        //判断是否存在，如果不存在则创建
        if (!fileParent.exists()) {
            fileParent.mkdirs();
        }
        
		File ciphertextFile = createNewFile(dir + outputFileName);
		Element m = PairingManager.defaultPairing.getGT().newRandomElement();
		Element s = pairing.getZr().newElement().setToRandom();
		//fill_policy(p, s, PK);
		Ciphertext ciphertext = new Ciphertext();
		//ciphertext.p = p;
		//此处m.duplicate()是为了后面AES加密中还需要用到m
		ciphertext.C = m.duplicate().mul(PK.egy_alpha.duplicate().powZn(s));
		ciphertext._C = PK.h.duplicate().powZn(s);
		
		SerializeUtils.serialize(ciphertext, ciphertextFile);
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try {
			fis = new FileInputStream(enc_file);
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
		System.out.println("重加密成功");
		
	}
	
	
	public static Element dec(Ciphertext ciphertext, SecretKey SK, PublicKey PK){
		check_sat(SK, ciphertext.p);
		if(ciphertext.p.satisfiable != 1){
			System.err.println("权限不足！属性私钥不满足访问策略!");
			return null;
		}
		pick_sat_min_leaves(ciphertext.p, SK);
		Element r = dec_flatten(ciphertext.p, SK);
		Element m = ciphertext.C.mul(r);
		r = pairing.pairing(ciphertext._C, SK.D);
		r.invert();
		m.mul(r);
		
		return m;
	}
	
	public static File createNewFile(String fileName){
		File file = new File(fileName);
		if(!file.exists()){
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else{
			try {
				String path = file.getCanonicalPath();
				file.delete();
				file = new File(path);
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file;
	}
	
	private static Element dec_flatten(Policy p, SecretKey SK){
		Element r = pairing.getGT().newElement().setToOne();
		Element one = pairing.getZr().newElement().setToOne();
		dec_node_flatten(r, one, p, SK);
		return r;
	}
	
	private static void dec_node_flatten(Element r, Element exp, 
			Policy p, SecretKey SK){
		assert(p.satisfiable == 1);
		if(p.children == null || p.children.length == 0){
			dec_leaf_flatten(r, exp, p, SK);
		}
		else{
			dec_internal_flatten(r, exp, p, SK);
		}
	}
	
	private static void dec_leaf_flatten(Element r, Element exp, 
			Policy p, SecretKey SK){
		SecretKey.SKComponent comp = SK.comps[p.attri];
		Element s = pairing.pairing(p.Cy, comp.Dj);
		Element t = pairing.pairing(p._Cy, comp._Dj);
		t.invert();
		s.mul(t);
		s.powZn(exp);
		r.mul(s);
	}
	
	private static void dec_internal_flatten(Element r, Element exp,
			 Policy p, SecretKey SK){
		int i;
		Element t;
		Element expnew;
		Element zero = pairing.getZr().newElement().setToZero();
		
		for(i=0; i<p.satl.size(); i++){
			t = lagrange_coef(p.satl, p.satl.get(i), zero);
			expnew = exp.duplicate().mul(t);    //注意这里的duplicate
			dec_node_flatten(r, expnew, p.children[p.satl.get(i)-1], SK);
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
	
	private static void check_sat(SecretKey SK, Policy p){
		int i,l;
		int size = p.children == null ? 0 : p.children.length;
		p.satisfiable = 0;
		if(p.children == null || size == 0){
			for(i=0; i<SK.comps.length; i++){
				if(SK.comps[i].attr.equals(p.attr)){
					p.satisfiable = 1;
					p.attri = i;
					break;
				}
			}
		}
		else{
			for(i=0; i<size; i++){
				check_sat(SK, p.children[i]);
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
	
	
	public static void fill_policy(Policy p, Element zero_value, PublicKey PK, String begin_time,
								   String end_time, String start_loc, String end_loc){
		//为策略数生成多项式并进行加密
		int i;
		int size = p.children == null ? 0 : p.children.length;
		Element r, t;
		//对所有叶子节点都需要生成一个多项式
		p.q = rand_poly(p.k - 1, zero_value);
		p.time = null;
		p.location = null;
		if(p.children == null || size == 0){
			//加密节点的属性n
			Element qn = p.q.coef.get(0).duplicate();
			Debug.qn = qn.duplicate();
			if (p.attr.equals("time")) {
				Element ta = MyPairUtils.getZrGenerator();
				qn = qn.duplicate().sub(ta);
				p.time = new TimeKey();
				p.time.begin = begin_time;
				p.time.end = end_time;
				p.time.Dj = PK.y.duplicate().powZn(ta);
				p.time._Dj = MyPairUtils.getG2FromByte(panel.min_time.getBytes()).duplicate().powZn(ta);
				p.time.__Dj = MyPairUtils.MRDFFunction(ta, PK.lambda, PK.mu,
						panel.max_time, begin_time, end_time);
			}
			if (p.attr.equals("location")) {
				Element lb = MyPairUtils.getZrGenerator();
				qn = qn.duplicate().sub(lb);
				p.location = new LocationKey();
				p.location.begin = start_loc;
				p.location.end = end_loc;
				p.location.Dj = PK.y.duplicate().powZn(lb);
				p.location._Dj = MyPairUtils.getG2FromByte(panel.min_loc.getBytes()).duplicate().powZn(lb);
				p.location.__Dj = MyPairUtils.MRDFFunction(lb, PK.lambda, PK.mu,
						panel.max_loc, start_loc, end_loc);
			}
			p.Cy = PK.y.duplicate().powZn(qn);
			p._Cy = MyPairUtils.getG1FromByte(p.attr.getBytes()).powZn(qn);
		}
		else{
			for(i=0; i<size; i++){
				r = pairing.getZr().newElement().set(i+1);
				t = Polynomial.eval_poly(p.q, r);
				fill_policy(p.children[i], t, PK, begin_time, end_time, start_loc, end_loc);
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
			q.coef.add(MyPairUtils.getZrGenerator());
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
