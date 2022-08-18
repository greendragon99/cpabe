package ui;

import java.io.IOException;

import abe.api.Client;
import abe.api.Server;



public class USERS {
	static Server server = new Server();
	static Client Manager = new Client(new String[]{"管理员","01"});
	static Client Courier1 = new Client(new String[]{"站点1", "站点3","快递员","04"});
	static Client Courier2 = new Client(new String[]{"站点2", "快递员", "05"});
	static Client Courier3 = new Client(new String[]{"站点1", "快递员", "06"});
	
	public static void main(String[] args) throws IOException {
	//client从server处获取公钥字符串
//	String PKJSONString = server.getPublicKeyInString();
//	Courier1.setPK(PKJSONString);
//	Courier2.setPK(PKJSONString);
//	Courier3.setPK(PKJSONString);
//	Manager.setPK(PKJSONString);
//
//	//client将自己的属性信息发送给server,并获取私钥字符串
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
//	System.out.println("系统初始化成功");
	}
}