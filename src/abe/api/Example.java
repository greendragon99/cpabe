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
//		Client Courier1 = new Client(new String[]{"·��1", "���Ա",  "���", "����P"});
//		Client Courier2 = new Client(new String[]{"·��2", "���Ա",  "���"});
//		Client Courier3 = new Client(new String[]{"·��2", "���Ա",  "���"});
//		Client Manager = new Client(new String[]{"����Ա", "9"});
//		//client��server����ȡ��Կ�ַ���
//		String PKJSONString = server.getPublicKeyInString();
//		Courier1.setPK(PKJSONString);
//		Courier2.setPK(PKJSONString);
//		Courier3.setPK(PKJSONString);
//		Manager.setPK(PKJSONString);
//
//		//client���Լ���������Ϣ���͸�server,����ȡ˽Կ�ַ���
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
//		System.out.println("ϵͳ��ʼ���ɹ�");
	}

	public static void encode(String[] args) throws IOException {
	
	}
	
	public static void main(String[] args) throws IOException {
//		Server server = new Server();
//		Client Courier1 = new Client(new String[]{"·��1", "���Ա", "9", "���", "����1"});
//		Client Courier2 = new Client(new String[]{"·��2", "���Ա", "10", "���"});
//		Client Manager = new Client(new String[]{"����Ա", "9"});
//		//client��server����ȡ��Կ�ַ���
//		String PKJSONString = server.getPublicKeyInString();
//		Courier1.setPK(PKJSONString);
//		Courier2.setPK(PKJSONString);
//		Manager.setPK(PKJSONString);
//
//		//client���Լ���������Ϣ���͸�server,����ȡ˽Կ�ַ���
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
//		System.out.println("ϵͳ��ʼ���ɹ�");
//
//		File directory = new File("");//����Ϊ��
//		String dir = directory.getCanonicalPath();
//
//		String outputFileName;
//		String policy;
//		File in;
///*		//����
//		outputFileName = "test.cpabe";
//		in = new File("test.txt");
//		policy = "courier OR manager";
//		System.out.print("��ǰ�û���Manager  ");
//		Manager.enc(in, policy, outputFileName, dir + "\\data\\cipher\\");
//		System.out.println("���ı�������"+dir +"\\data\\cipher\\" + outputFileName);
//
//		//����
//		in = new File(dir + "\\data\\cipher\\" + outputFileName);
//		System.out.print("��ǰ�û���Courier1  ");
//		Courier1.dec(in,  dir + "\\data\\user\\Courier1\\");
////		Manager.dec(in,  dir + "\\data\\user\\Manager\\");
//		System.out.print("��ǰ�û���Courier2  ");
//		Courier2.dec(in,  dir + "\\data\\user\\Courier2\\");
//*/
//
//		//����
//		outputFileName = "next_station_p1.cpabe";
//		File in0 = new File("next_station_p1.txt");
//		policy = "(���Ա AND ·��1 AND ��� AND ����1) OR ����Ա";
//		System.out.print("��ǰ�û���Manager  ");
//		//Manager.enc(in0, policy, outputFileName, dir + "\\data\\cipher\\");
//		System.out.println("���ı�������"+dir +"\\data\\cipher\\" + outputFileName);
//
//		//����
//		in = new File(dir + "\\data\\cipher\\" + outputFileName);
//		System.out.print("��ǰ�û���Courier1  ");
//		Courier2.dec(in,  dir + "\\data\\user\\Courier1\\");
////		Manager.dec(in,  dir + "\\data\\user\\Manager\\");
//		System.out.print("��ǰ�û���Courier2  ");
//		Courier1.dec(in,  dir + "\\data\\user\\Courier2\\");
//		System.out.println("���ܺ����ı�������"+ "D:\\__Study\\4\\cpabe\\data\\user\\Courier2\\next_station_p1");
//
//		//���Ը���
////		System.out.println("celvegengxin");
//		File in1 = new File("next_station_r1.txt");
//		outputFileName = "next_station_r1.cpabe";
//		String new_policy = "(deliver AND route1 AND working AND start) OR manager";
//		//Manager.renew_policy(in1, new_policy, outputFileName, dir + "\\data\\cipher\\");
//
//		//����
//		in = new File(dir + "\\data\\cipher\\" + outputFileName);
//		Courier1.dec(in,  dir + "\\data\\user\\Courier1\\");
//		Manager.dec(in,  dir + "\\data\\user\\Manager\\");
//		Courier2.dec(in,  dir + "\\data\\user\\Courier2\\");
//
//
//		//���Գ���
////		System.out.println("shuxingchexiao");
//		String[] attrs1 ={"route1", "deliver", "working", "start"};
//		Courier1.setAttrs(attrs1);
//		System.out.println("�޸ĺ�Deliver1���ԣ�" + "{\"route1\", \"deliver\", \"working\", \"start\"}");
//
//		String[] attrs2 ={"route2", "deliver", "working", "start", "package1"};
//		Courier2.setAttrs(attrs2);
//		System.out.println("�޸ĺ�Deliver2���ԣ�" + "{\"route2\", \"deliver\", \"working\", \"start\", \"package1\"}");
//		//client��server����ȡ��Կ�ַ���
//		String PKJSONString1 = server.getPublicKeyInString();
//		Courier1.setPK(PKJSONString1);
//		Courier2.setPK(PKJSONString1);
//		Manager.setPK(PKJSONString1);
//
//		//client���Լ���������Ϣ���͸�server,����ȡ˽Կ�ַ���
//		String SKJSONString1 = server.generateSecretKey(Courier1.getId(), Courier1.getAttrs());
//		Courier1.setSK(SKJSONString1);
//
//		SKJSONString1 = server.generateSecretKey(Courier2.getId(), Courier2.getAttrs());
//		Courier2.setSK(SKJSONString1);
//
//		SKJSONString1 = server.generateSecretKey(Manager.getId(), Manager.getAttrs());
//		Manager.setSK(SKJSONString1);
//
//		//����
//		File in2 = new File("next_station_p1.txt");
//		outputFileName = "next_station_p1.cpabe";
//		System.out.print("��ǰ�û���Manager  ");
//		policy = "(deliver AND route2 AND working AND start AND package1) OR manager";
//		//Manager.enc(in2, policy, outputFileName, dir + "\\data\\cipher\\");
//
//		//����
//		in = new File(dir + "\\data\\cipher\\" + outputFileName);
//		System.out.print("��ǰ�û���Deliver1  ");
//		Courier1.dec(in,  dir + "\\data\\user\\Courier1\\");
////		Manager.dec(in,  dir + "\\data\\user\\Manager\\");
//		System.out.print("��ǰ�û���Deliver2  ");
//		Courier2.dec(in,  dir + "\\data\\user\\Courier2\\");
//		System.out.println("���ܺ����ı�������"+ "D:\\__Study\\4\\cpabe\\data\\user\\Courier2\\next_station_p1");
//
//		new_policy = "(deliver AND route2 AND working AND start AND package1) OR manager";
//		System.out.print("��ǰ�û���Manager  ");
//		//Manager.enc(in2, new_policy, outputFileName, dir + "\\data\\cipher\\");
//		System.out.println("���ı�������"+dir +"\\data\\cipher\\" + outputFileName);
//
//		in = new File(dir + "\\data\\cipher\\" + outputFileName);
//		System.out.print("��ǰ�û���Deliver1  ");
//		Courier1.dec(in,  dir + "\\data\\user\\Courier1\\");
////		Manager.dec(in,  dir + "\\data\\user\\Manager\\");
//		System.out.print("��ǰ�û���Deliver2  ");
//		Courier2.dec(in,  dir + "\\data\\user\\Courier2\\");
//		System.out.println("���ܺ����ı�������"+ "D:\\__Study\\4\\cpabe\\data\\user\\Courier2\\next_station_p1");
	}
}
