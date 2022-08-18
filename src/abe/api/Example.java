package abe.api;

import java.io.File;
import java.io.IOException;

public class Example {
	public static void Initialize(String[] usersList, String[][] attrsList) {
		Server server = new Server();
		String i;
		//for (i in usersList) {
			
		//}
	}
	public static void Ini() {
//		Server server = new Server();
//		Client Courier1 = new Client(new String[]{"路线1", "快递员",  "起点", "包裹P"});
//		Client Courier2 = new Client(new String[]{"路线2", "快递员",  "起点"});
//		Client Courier3 = new Client(new String[]{"路线2", "快递员",  "起点"});
//		Client Manager = new Client(new String[]{"管理员", "9"});
//		//client从server处获取公钥字符串
//		String PKJSONString = server.getPublicKeyInString();
//		Courier1.setPK(PKJSONString);
//		Courier2.setPK(PKJSONString);
//		Courier3.setPK(PKJSONString);
//		Manager.setPK(PKJSONString);
//
//		//client将自己的属性信息发送给server,并获取私钥字符串
//		String SKJSONString = server.generateSecretKey(Courier1.getId(), Courier1.getAttrs());
//		Courier1.setSK(SKJSONString);
//
//		SKJSONString = server.generateSecretKey(Courier2.getId(), Courier2.getAttrs());
//		Courier2.setSK(SKJSONString);
//
//		SKJSONString = server.generateSecretKey(Courier3.getId(), Courier3.getAttrs());
//		Courier3.setSK(SKJSONString);
//
//		SKJSONString = server.generateSecretKey(Manager.getId(), Manager.getAttrs());
//		Manager.setSK(SKJSONString);
//
//
//		System.out.println("系统初始化成功");
	}

	public static void encode(String[] args) throws IOException {
	
	}
	
	public static void main(String[] args) throws IOException {
//		Server server = new Server();
//		Client Courier1 = new Client(new String[]{"路线1", "快递员", "9", "起点", "包裹1"});
//		Client Courier2 = new Client(new String[]{"路线2", "快递员", "10", "起点"});
//		Client Manager = new Client(new String[]{"管理员", "9"});
//		//client从server处获取公钥字符串
//		String PKJSONString = server.getPublicKeyInString();
//		Courier1.setPK(PKJSONString);
//		Courier2.setPK(PKJSONString);
//		Manager.setPK(PKJSONString);
//
//		//client将自己的属性信息发送给server,并获取私钥字符串
//		String SKJSONString = server.generateSecretKey(Courier1.getId(), Courier1.getAttrs());
//		Courier1.setSK(SKJSONString);
//
//		SKJSONString = server.generateSecretKey(Courier2.getId(), Courier2.getAttrs());
//		Courier2.setSK(SKJSONString);
//
//		SKJSONString = server.generateSecretKey(Manager.getId(), Manager.getAttrs());
//		Manager.setSK(SKJSONString);
//
//
//		System.out.println("系统初始化成功");
//
//		File directory = new File("");//参数为空
//		String dir = directory.getCanonicalPath();
//
//		String outputFileName;
//		String policy;
//		File in;
///*		//加密
//		outputFileName = "test.cpabe";
//		in = new File("test.txt");
//		policy = "courier OR manager";
//		System.out.print("当前用户：Manager  ");
//		Manager.enc(in, policy, outputFileName, dir + "\\data\\cipher\\");
//		System.out.println("密文保存至："+dir +"\\data\\cipher\\" + outputFileName);
//
//		//解密
//		in = new File(dir + "\\data\\cipher\\" + outputFileName);
//		System.out.print("当前用户：Courier1  ");
//		Courier1.dec(in,  dir + "\\data\\user\\Courier1\\");
////		Manager.dec(in,  dir + "\\data\\user\\Manager\\");
//		System.out.print("当前用户：Courier2  ");
//		Courier2.dec(in,  dir + "\\data\\user\\Courier2\\");
//*/
//
//		//加密
//		outputFileName = "next_station_p1.cpabe";
//		File in0 = new File("next_station_p1.txt");
//		policy = "(快递员 AND 路线1 AND 起点 AND 包裹1) OR 管理员";
//		System.out.print("当前用户：Manager  ");
//		//Manager.enc(in0, policy, outputFileName, dir + "\\data\\cipher\\");
//		System.out.println("密文保存至："+dir +"\\data\\cipher\\" + outputFileName);
//
//		//解密
//		in = new File(dir + "\\data\\cipher\\" + outputFileName);
//		System.out.print("当前用户：Courier1  ");
//		Courier2.dec(in,  dir + "\\data\\user\\Courier1\\");
////		Manager.dec(in,  dir + "\\data\\user\\Manager\\");
//		System.out.print("当前用户：Courier2  ");
//		Courier1.dec(in,  dir + "\\data\\user\\Courier2\\");
//		System.out.println("解密后明文保存至："+ "D:\\__Study\\4\\cpabe\\data\\user\\Courier2\\next_station_p1");
//
//		//策略更新
////		System.out.println("celvegengxin");
//		File in1 = new File("next_station_r1.txt");
//		outputFileName = "next_station_r1.cpabe";
//		String new_policy = "(deliver AND route1 AND working AND start) OR manager";
//		//Manager.renew_policy(in1, new_policy, outputFileName, dir + "\\data\\cipher\\");
//
//		//解密
//		in = new File(dir + "\\data\\cipher\\" + outputFileName);
//		Courier1.dec(in,  dir + "\\data\\user\\Courier1\\");
//		Manager.dec(in,  dir + "\\data\\user\\Manager\\");
//		Courier2.dec(in,  dir + "\\data\\user\\Courier2\\");
//
//
//		//属性撤销
////		System.out.println("shuxingchexiao");
//		String[] attrs1 ={"route1", "deliver", "working", "start"};
//		Courier1.setAttrs(attrs1);
//		System.out.println("修改后Deliver1属性：" + "{\"route1\", \"deliver\", \"working\", \"start\"}");
//
//		String[] attrs2 ={"route2", "deliver", "working", "start", "package1"};
//		Courier2.setAttrs(attrs2);
//		System.out.println("修改后Deliver2属性：" + "{\"route2\", \"deliver\", \"working\", \"start\", \"package1\"}");
//		//client从server处获取公钥字符串
//		String PKJSONString1 = server.getPublicKeyInString();
//		Courier1.setPK(PKJSONString1);
//		Courier2.setPK(PKJSONString1);
//		Manager.setPK(PKJSONString1);
//
//		//client将自己的属性信息发送给server,并获取私钥字符串
//		String SKJSONString1 = server.generateSecretKey(Courier1.getId(), Courier1.getAttrs());
//		Courier1.setSK(SKJSONString1);
//
//		SKJSONString1 = server.generateSecretKey(Courier2.getId(), Courier2.getAttrs());
//		Courier2.setSK(SKJSONString1);
//
//		SKJSONString1 = server.generateSecretKey(Manager.getId(), Manager.getAttrs());
//		Manager.setSK(SKJSONString1);
//
//		//加密
//		File in2 = new File("next_station_p1.txt");
//		outputFileName = "next_station_p1.cpabe";
//		System.out.print("当前用户：Manager  ");
//		policy = "(deliver AND route2 AND working AND start AND package1) OR manager";
//		//Manager.enc(in2, policy, outputFileName, dir + "\\data\\cipher\\");
//
//		//解密
//		in = new File(dir + "\\data\\cipher\\" + outputFileName);
//		System.out.print("当前用户：Deliver1  ");
//		Courier1.dec(in,  dir + "\\data\\user\\Courier1\\");
////		Manager.dec(in,  dir + "\\data\\user\\Manager\\");
//		System.out.print("当前用户：Deliver2  ");
//		Courier2.dec(in,  dir + "\\data\\user\\Courier2\\");
//		System.out.println("解密后明文保存至："+ "D:\\__Study\\4\\cpabe\\data\\user\\Courier2\\next_station_p1");
//
//		new_policy = "(deliver AND route2 AND working AND start AND package1) OR manager";
//		System.out.print("当前用户：Manager  ");
//		//Manager.enc(in2, new_policy, outputFileName, dir + "\\data\\cipher\\");
//		System.out.println("密文保存至："+dir +"\\data\\cipher\\" + outputFileName);
//
//		in = new File(dir + "\\data\\cipher\\" + outputFileName);
//		System.out.print("当前用户：Deliver1  ");
//		Courier1.dec(in,  dir + "\\data\\user\\Courier1\\");
////		Manager.dec(in,  dir + "\\data\\user\\Manager\\");
//		System.out.print("当前用户：Deliver2  ");
//		Courier2.dec(in,  dir + "\\data\\user\\Courier2\\");
//		System.out.println("解密后明文保存至："+ "D:\\__Study\\4\\cpabe\\data\\user\\Courier2\\next_station_p1");
	}
}
