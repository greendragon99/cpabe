package ui;

import java.io.IOException;

import abe.api.Client;
import abe.api.Server;



public class USERS {
	static Server server = new Server();
	static Client Manager = new Client(new String[]{"����Ա","01"});
	static Client Courier1 = new Client(new String[]{"վ��1", "վ��3","���Ա","04"});
	static Client Courier2 = new Client(new String[]{"վ��2", "���Ա", "05"});
	static Client Courier3 = new Client(new String[]{"վ��1", "���Ա", "06"});
	
	public static void main(String[] args) throws IOException {
	//client��server����ȡ��Կ�ַ���
//	String PKJSONString = server.getPublicKeyInString();
//	Courier1.setPK(PKJSONString);
//	Courier2.setPK(PKJSONString);
//	Courier3.setPK(PKJSONString);
//	Manager.setPK(PKJSONString);
//
//	//client���Լ���������Ϣ���͸�server,����ȡ˽Կ�ַ���
//	String SKJSONString = server.generateSecretKey(Courier1.getId(), Courier1.getAttrs());
//	Courier1.setSK(SKJSONString);
//
//	SKJSONString = server.generateSecretKey(Courier2.getId(), Courier2.getAttrs());
//	Courier2.setSK(SKJSONString);
//
//	SKJSONString = server.generateSecretKey(Courier3.getId(), Courier3.getAttrs());
//	Courier3.setSK(SKJSONString);
//
//	SKJSONString = server.generateSecretKey(Manager.getId(), Manager.getAttrs());
//	Manager.setSK(SKJSONString);
//
//	System.out.println("ϵͳ��ʼ���ɹ�");
	}
}