package abe.api;

import it.unisa.dia.gas.jpbc.Element;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.crypto.Cipher;

import abe.CPABEImpl;
import abe.CPABEImplWithoutSerialize;
import abe.Ciphertext;
import abe.Parser;
import abe.Policy;
import abe.PublicKey;
import abe.SecretKey;
import abe.serialize.SerializeUtils;
import aes.AES;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class Client {
	private PublicKey PK;
	private SecretKey SK;
	private String id;
	private String[] attrs;
	
	public Client(){}
	
	public Client(String[] attrs){
		this.attrs = attrs;
		id = new String(String.valueOf(hashCode()));
	}
	public Client(String id, String[] attrs){
		this.id = id;
		this.attrs = attrs;
	}

	public String getId() {
		return id;
	}

	public String[] getAttrs(){
		return attrs;
	}
	
	public void setAttrs(String[] attrs){
		this.attrs = attrs;
	}
	
	public PublicKey getPK() {
		return PK;
	}

	public void setPK(String PKJSONString) {
		JSONObject json = JSON.parseObject(PKJSONString);
		byte[] b = json.getBytes("PK");
		this.PK = SerializeUtils.constructFromByteArray(PublicKey.class, b);
	}

	public SecretKey getSK() {
		return SK;
	}

	public void setSK(String SKJSONString) {
		JSONObject json = JSON.parseObject(SKJSONString);
		byte[] b = json.getBytes("SK");
		this.SK = SerializeUtils.constructFromByteArray(SecretKey.class, b);
	}

	public void setSK(SecretKey key) {
		this.SK = key;
	}

	public long deal_time(String time_str) {
		long t = 0;
		
		return t;
	}
	
	public long deal_loc(String[] loc) {
		long p = 0;
		
		return p;
	}
	
	public void enc(File in, String policy, String outputFileName, String dir,
					String time_begin, String time_end, String location_begin, String location_end){
		Parser parser = new Parser();
		Policy p = parser.parse(policy);
		CPABEImplWithoutSerialize.enc(in, p, this.PK, outputFileName, dir,
				time_begin, time_end, location_begin, location_end);
//		System.out.println("加密成功" + dir + outputFileName);
		
	}
	
	public void dec(File in, String dir, String time, String loc){
		String ciphertextFileName = null; 
		DataInputStream dis = null;
		try {
			ciphertextFileName = in.getCanonicalPath();
			dis = new DataInputStream(new FileInputStream(in));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		Ciphertext ciphertext = SerializeUtils._unserialize(Ciphertext.class, dis);
		
		String output = null;
		if(ciphertextFileName.endsWith(".cpabe")){
			int st = ciphertextFileName.lastIndexOf("\\");
			int end = ciphertextFileName.indexOf(".cpabe");
			output = ciphertextFileName.substring(st, end);
		}
		else{
			output = ciphertextFileName + ".out";
		}
		File f = new File(output);
		File fileParent = f.getParentFile();
        //判断是否存在，如果不存在则创建
        if (!fileParent.exists()) {
            fileParent.mkdirs();
        }
		
		File outputFile = CPABEImpl.createNewFile(output);
		OutputStream os = null;
		try {
			os =  new FileOutputStream(outputFile);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Element m = CPABEImpl.dec(ciphertext, SK, PK, time, loc);
		AES.crypto(Cipher.DECRYPT_MODE, dis, os, m);
		System.out.println("解密后明文保存至："+ dir + output.substring(1));
	}
	
	public void renew_policy(File in, String new_policy, String outputFileName, String dir,
							 String time_begin, String time_end, String location_begin, String location_end) {
		Parser parser = new Parser();
		Policy p = parser.parse(new_policy);
		CPABEImplWithoutSerialize.enc(in, p, this.PK, outputFileName, dir,
				time_begin, time_end, location_begin, location_end);
//		System.out.println("重加密成功");
	}
	
//	private String getVName(Client this) {
//		// TODO Auto-generated method stub
//		Field field = this.getClass().getDeclaredFields();
//		return field.getName();
//	}

	
	public void serializePK(File f){
		SerializeUtils.serialize(this.PK, f);
	}
	
	public void serializeSK(File f){
		SerializeUtils.serialize(this.SK, f);
	}

	public String printAttrs() {
		// TODO Auto-generated method stub
		String[] tmp = this.getAttrs();
		String res="";
		for (int i =0;i<tmp.length;i++) {
			res.concat(tmp[i]);
		}
		System.out.print(res);
		return res;
	}
}
