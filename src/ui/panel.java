package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import abe.MyPairUtils;
import abe.SecretKey;
import abe.api.Client;
import abe.api.Server;



public class panel extends JPanel {
	
	private JPanel contentPane;
	private JTextField TF_username;
	private JTextField TF_filename;
	private JTextField TF_policy;
	private JTextField TF_year1;
	private JTextField TF_year2;
	private JTextField TF_month1;
	private JTextField TF_month2;
	private JTextField TF_day1;
	private JTextField TF_day2;
	private JTextField TF_hour1;
	private JTextField TF_hour2;
	private JTextField TF_min1;
	private JTextField TF_min2;
	private JTextField time;
	SimpleDateFormat myfmt = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	public static final String min_time = "946656000000";
	public static final String max_time = "2524579200000";
	public static final String min_loc = "000000";
	public static final String max_loc = "132334";
	static Server server = new Server();
	static Client Manager = new Client("0", new String[]{"管理员","01"});
	static Client Courier1 = new Client("1", new String[]{"站点1", "站点3","快递员","P", "time", "location"});
	static Client Courier2 = new Client("2", new String[]{"站点2", "快递员", "05"});
	static Client Courier3 = new Client("3", new String[]{"站点1", "快递员", "06"});
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		//系统初始化
		JFrame frame=new JFrame("物流隐私保护系统");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(new panel(),BorderLayout.WEST);
        //frame.getContentPane().add(new panel());
        frame.pack();
        frame.setVisible(true);
        
	}
	private static void Ini() {
		//client从server处获取公钥字符串
		MyPairUtils.setUp();
		String PKJSONString = server.getPublicKeyInString();
		SecretKey key = null;
		Courier1.setPK(PKJSONString);
		Courier2.setPK(PKJSONString);
		Courier3.setPK(PKJSONString);
		Manager.setPK(PKJSONString);

		//client将自己的属性信息发送给server,并获取私钥字符串
		key  = server.generateSecretKey(Courier1.getId(), Courier1.getAttrs());
		Courier1.setSK(key);
		
		key = server.generateSecretKey(Courier2.getId(), Courier2.getAttrs());
		Courier2.setSK(key);
		
		key = server.generateSecretKey(Courier3.getId(), Courier3.getAttrs());
		Courier3.setSK(key);
		
		key = server.generateSecretKey(Manager.getId(), Manager.getAttrs());
		Manager.setSK(key);
		
		System.out.println("系统初始化成功");
	}
	/**
	 * Create the panel.
	 */
	public panel() {
		Ini();
		//super(new GridLayout(1,1));
        JTabbedPane tabbedPane = new JTabbedPane();
        //ImageIcon icon = createImageIcon("tab.jpg");
        JComponent panel1 = makeEncodePanel();
        //JComponent panel1 = makeTextPanel("加密");
        //tabbedPane.addTab("加密",icon, panel1,"Does nothing");
        tabbedPane.addTab("加密", panel1);
        tabbedPane.setMnemonicAt(0,KeyEvent.VK_1);
        
        JComponent panel2 = makeDecodePanel();
        //tabbedPane.addTab("解密",icon,panel2,"Does twice as much nothing");
        tabbedPane.addTab("解密", panel2);
        tabbedPane.setMnemonicAt(1,KeyEvent.VK_2);
        
        JComponent panel3 = makeRevocPanel();
        //tabbedPane.addTab("属性撤销",icon,panel3,"Still does nothing");
        tabbedPane.addTab("属性撤销", panel3);
        tabbedPane.setMnemonicAt(2,KeyEvent.VK_3);
        
        JComponent panel4 = makeRenewPanel();
        panel4.setPreferredSize(new Dimension(500,500));
        //tabbedPane.addTab("策略更新",icon,panel4,"Does nothing at all");
        tabbedPane.addTab("策略更新", panel4);
        tabbedPane.setMnemonicAt(3,KeyEvent.VK_4);
        add(tabbedPane);
	}
	

    protected JComponent makeEncodePanel() {
    	
    	JPanel contentPane = new JPanel(false);
    	contentPane.setLayout(null);
    	
    	JLabel lblNewLabel0 = new JLabel("用户名：");//用户名（输入）
		lblNewLabel0.setBounds(10, 10, 90, 30);//x,y,width,height
		contentPane.add(lblNewLabel0);
		
		final JTextField TF_username = new JTextField();
		TF_username.setBounds(80, 10, 150, 30);
		contentPane.add(TF_username);
		TF_username.setColumns(10);
		
		
    	JLabel lblNewLabel1 = new JLabel("当前用户类型：");//当前用户类型（选择）
		lblNewLabel1.setBounds(250, 10, 90, 30);//x,y,width,height
		contentPane.add(lblNewLabel1);
		
		JComboBox user_type = new JComboBox();
		user_type.setBounds(340, 10, 150, 30);
		contentPane.add(user_type);
		user_type.addItem("--请选择--");    //向下拉列表中添加一项
		user_type.addItem("用户");
		user_type.addItem("管理员");
		user_type.addItem("快递员");
		//comboBox.addItem("快递员1");
		//comboBox.addItem("快递员2");
		
		JLabel lblNewLabel2 = new JLabel("请选择要加密的文件：");//选择文件 filechooser
		lblNewLabel2.setBounds(10, 50, 160, 30);
		contentPane.add(lblNewLabel2);
		
		final JTextField TF_filename = new JTextField();
		TF_filename.setBounds(130, 50, 270, 30);
		contentPane.add(TF_filename);
		TF_filename.setColumns(10);
		
		JButton choose_file = new JButton("请选择...");
		choose_file.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFileChooser jfc=new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );
				jfc.showDialog(new JLabel(), "选择");
				File file=jfc.getSelectedFile();
				if(file.isDirectory()){
					System.out.println("文件夹:"+file.getAbsolutePath());
				}else if(file.isFile()){
					System.out.println("文件:"+file.getAbsolutePath());
				}
				TF_filename.setText(file.getAbsolutePath());
				String filename = jfc.getSelectedFile().getName();
				int len = file.getAbsolutePath().length();
				int len1 = filename.length();
				String pathname = file.getAbsolutePath().substring(0, len-len1);
				System.out.println(filename);
				System.out.println(pathname);
			}
		});
		choose_file.setBounds(410, 50, 80, 30);
		contentPane.add(choose_file);
		
		JLabel lblNewLabel3 = new JLabel("请输入访问策略：");//
		lblNewLabel3.setBounds(10, 90, 160, 30);
		contentPane.add(lblNewLabel3);
		
		final JTextField TF_policy = new JTextField();
		TF_policy.setBounds(130, 90, 270, 30);
		contentPane.add(TF_policy);
		TF_policy.setColumns(10);
		
		
		
		JLabel lblNewLabel4 = new JLabel("访问时间约束：");//
		lblNewLabel4.setBounds(10, 130, 160, 30);
		contentPane.add(lblNewLabel4);
		JLabel dao = new JLabel("到");//
		dao.setBounds(282, 165, 20, 30);
		contentPane.add(dao);
		
		//year
		final JTextField TF_year1 = new JTextField();
		TF_year1.setBounds(100, 130, 60, 30);
		contentPane.add(TF_year1);
		TF_year1.setColumns(10);
		final JTextField TF_year2 = new JTextField();
		TF_year2.setBounds(100, 200, 60, 30);
		contentPane.add(TF_year2);
		TF_year2.setColumns(10);
		JLabel year = new JLabel("年");//
		year.setBounds(162, 130, 20, 30);
		contentPane.add(year);
		JLabel year2 = new JLabel("年");//
		year2.setBounds(162, 200, 20, 30);
		contentPane.add(year2);
		
		//month
		final JTextField TF_month1 = new JTextField();
		TF_month1.setBounds(180, 130, 60, 30);
		contentPane.add(TF_month1);
		TF_month1.setColumns(10);
		final JTextField TF_month2 = new JTextField();
		TF_month2.setBounds(180, 200, 60, 30);
		contentPane.add(TF_month2);
		TF_month2.setColumns(10);
		JLabel mon = new JLabel("月");//
		mon.setBounds(242, 130, 20, 30);
		contentPane.add(mon);
		JLabel mon2 = new JLabel("月");//
		mon2.setBounds(242, 200, 20, 30);
		contentPane.add(mon2);
		
		//day
		final JTextField TF_day1 = new JTextField();
		TF_day1.setBounds(260, 130, 60, 30);
		contentPane.add(TF_day1);
		TF_day1.setColumns(10);
		final JTextField TF_day2 = new JTextField();
		TF_day2.setBounds(260, 200, 60, 30);
		contentPane.add(TF_day2);
		TF_day2.setColumns(10);
		JLabel day = new JLabel("日");//
		day.setBounds(322, 130, 20, 30);
		contentPane.add(day);
		JLabel day2 = new JLabel("日");//
		day2.setBounds(322, 200, 20, 30);
		contentPane.add(day2);
		
		//hour
		final JTextField TF_hour1 = new JTextField();
		TF_hour1.setBounds(340, 130, 60, 30);
		contentPane.add(TF_hour1);
		TF_hour1.setColumns(10);
		final JTextField TF_hour2 = new JTextField();
		TF_hour2.setBounds(340, 200, 60, 30);
		contentPane.add(TF_hour2);
		TF_hour2.setColumns(10);
		JLabel hour = new JLabel("时");//
		hour.setBounds(402, 130, 20, 30);
		contentPane.add(hour);
		JLabel hour2 = new JLabel("时");//
		hour2.setBounds(402, 200, 20, 30);
		contentPane.add(hour2);
		
		//minute
		final JTextField TF_min1 = new JTextField();
		TF_min1.setBounds(420, 130, 60, 30);
		contentPane.add(TF_min1);
		TF_min1.setColumns(10);
		final JTextField TF_min2 = new JTextField();
		TF_min2.setBounds(420, 200, 60, 30);
		contentPane.add(TF_min2);
		TF_min2.setColumns(10);
		JLabel min = new JLabel("分");//
		min.setBounds(482, 130, 20, 30);
		contentPane.add(min);
		JLabel min2 = new JLabel("分");//
		min2.setBounds(482, 200, 20, 30);
		contentPane.add(min2);
		
		
		
		JLabel lblNewLabel5 = new JLabel("访问空间约束：");//
		lblNewLabel5.setBounds(10, 250, 160, 30);
		contentPane.add(lblNewLabel5);
		
		String[] loc = {"0"};
		final JComboBox loc_1 = new JComboBox();
		loc_1.setBounds(100, 250, 50, 30);
		contentPane.add(loc_1);
		loc_1.addItem("A");
		loc_1.addItem("B");
		loc_1.addItem("C");
		JLabel loc1 = new JLabel("市");//
		loc1.setBounds(155, 250, 40, 30);
		contentPane.add(loc1);
		
		final JComboBox loc_2 = new JComboBox();
		loc_2.setBounds(200, 250, 50, 30);
		contentPane.add(loc_2);
		loc_2.addItem("a");
		loc_2.addItem("b");
		loc_2.addItem("c");
		loc_2.addItem("d");
		loc_2.addItem("e");
		JLabel loc2 = new JLabel("区/县");//
		loc2.setBounds(255, 250, 50, 30);
		contentPane.add(loc2);
		
		final JComboBox loc_3 = new JComboBox();
		loc_3.setBounds(300, 250, 50, 30);
		contentPane.add(loc_3);
		loc_3.addItem("1");
		loc_3.addItem("2");
		loc_3.addItem("3");
		loc_3.addItem("4");
		loc_3.addItem("5");
		JLabel loc3 = new JLabel("街道");//
		loc3.setBounds(355, 250, 50, 30);
		contentPane.add(loc3);
		
		JLabel Linfo = new JLabel("输出信息:");
        Linfo.setBounds(10, 340, 100, 30);
        contentPane.add(Linfo);
        final JTextArea info = new JTextArea();
        info.setBounds(10, 370, 480, 120);
        info.setEditable(false);
        contentPane.add(info);
		
		JButton btn = new JButton("开始加密");
		btn.setBounds(155, 305, 160, 30);
		contentPane.add(btn);
		btn.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	String username = TF_username.getText();
		    	System.out.println("username:" + username);
		    	if (username.length() == 0) {
		    		info.setText(info.getText()+"请输入当前用户名！\n");
		    	}
		    	
		    	String filename = TF_filename.getText();
		    	System.out.println("filename:" + filename);
		    	String fname = filename.substring(filename.lastIndexOf("\\")+1);
		    	System.out.println("fname:" + fname);
		    	if (filename.length() == 0) {
		    		info.setText(info.getText()+"请选择要加密的文件！\n");
		    	}
		    	
				String policy = TF_policy.getText();
				System.out.println("policy:" + policy);
				if (policy.length() == 0) {
		    		info.setText(info.getText()+"请输入访问策略！\n");
		    	}
				//获取开始时间
				String start_time = new String();
				try {
					start_time = dateToStamp(TF_year1.getText(), 
										     TF_month1.getText(), 
										     TF_day1.getText(), 
										     TF_hour1.getText(), 
										     TF_min1.getText());
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
					info.setText(info.getText()+"请输入正确的时间范围约束！\n");
				}
				System.out.println(start_time);
				//获取结束时间
				String end_time = new String();
				try {
					end_time = dateToStamp(TF_year2.getText(), 
										     TF_month2.getText(), 
										     TF_day2.getText(), 
										     TF_hour2.getText(), 
										     TF_min2.getText());
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
					info.setText(info.getText()+"请输入正确的时间范围约束！\n");
				}
				System.out.println(end_time);
		    	//获取空间限制（下拉框）
				String loc1 = loc_1.getSelectedItem().toString();
				String loc2 = loc_2.getSelectedItem().toString();
				String loc3 = loc_3.getSelectedItem().toString();
				String[] addr = {loc1, loc2, loc3};
				String loc = deal_loc(addr);
				System.out.println(loc);
				
				String outfile = fname.substring(0,fname.lastIndexOf(".")) + "_enc.txt";
				int len = filename.length();
				String pathname = filename.substring(0, len-fname.length())+"cipher\\";
				
				//encode(username, filename, policy, outfile, pathname);
				File in = new File(filename);
				System.out.println(username);
				if (username.equals("Manager")) {
					Manager.enc(in, policy, outfile, pathname, start_time, end_time, loc, loc);
				}else if (username.equals("Courier1")) {
					Courier1.enc(in, policy, outfile, pathname, start_time, end_time, loc, loc);
				}else if (username.equals("Courier2")) {
					Courier2.enc(in, policy, outfile, pathname, start_time, end_time, loc, loc);
				}else if (username.equals("Courier3")) {
					Courier3.enc(in, policy, outfile, pathname, start_time, end_time, loc, loc);
				}
				info.setText(info.getText()+"加密成功！\n");
				info.setText(info.getText()+"密文保存至"+ pathname + outfile + '\n');
				
			}
		});
		
    	return contentPane;
    }
    
    
    protected JComponent makeDecodePanel() {
    	JPanel contentPane = new JPanel(false);
    	contentPane.setLayout(null);
    	
    	JLabel Linfo = new JLabel("输出信息:");
        Linfo.setBounds(10, 340, 100, 30);
        contentPane.add(Linfo);
        final JTextArea info = new JTextArea();
        info.setBounds(10, 370, 480, 120);
        contentPane.add(info);
        info.setEditable(false);
    	
    	
    	JLabel lblNewLabel0 = new JLabel("用户名：");//用户名（输入）
		lblNewLabel0.setBounds(10, 10, 90, 30);//x,y,width,height
		contentPane.add(lblNewLabel0);
		
		final JTextField TF_username = new JTextField();
		TF_username.setBounds(80, 10, 150, 30);
		contentPane.add(TF_username);
		TF_username.setColumns(10);
		
		
    	JLabel lblNewLabel1 = new JLabel("当前用户类型：");//当前用户类型（选择）
		lblNewLabel1.setBounds(250, 10, 90, 30);//x,y,width,height
		contentPane.add(lblNewLabel1);
		
		JComboBox user_type = new JComboBox();
		user_type.setBounds(340, 10, 150, 30);
		contentPane.add(user_type);
		user_type.addItem("--请选择--");    //向下拉列表中添加一项
		user_type.addItem("用户");
		user_type.addItem("管理员");
		user_type.addItem("快递员");
		//comboBox.addItem("快递员1");
		//comboBox.addItem("快递员2");
		
		JLabel lblNewLabel2 = new JLabel("请输入当前包裹名：");//选择文件 filechooser
		lblNewLabel2.setBounds(10, 50, 180, 30);
		contentPane.add(lblNewLabel2);
		
		final JTextField TF_pname = new JTextField();
		TF_pname.setBounds(130, 50, 270, 30);
		contentPane.add(TF_pname);
		TF_pname.setColumns(10);
		
		
		
		JLabel lblNewLabel3 = new JLabel("当前访问时间：");//
		lblNewLabel3.setBounds(10, 90, 160, 30);
		contentPane.add(lblNewLabel3);
		
		time = new JTextField();
		contentPane.add(time);
		time.setBounds(130, 90, 270, 30);
		//time.setText("2022-05-25 15:33:29");
        time.addActionListener(new TimeActionListener());
        setVisible(true);
		
		
		JLabel lblNewLabel5 = new JLabel("当前访问位置：");//
		lblNewLabel5.setBounds(10, 240, 160, 30);
		contentPane.add(lblNewLabel5);
		
		String[] loc = {"0"};
		final JComboBox loc_1 = new JComboBox();
		loc_1.setBounds(100, 240, 50, 30);
		contentPane.add(loc_1);
		loc_1.addItem("A");
		loc_1.addItem("B");
		loc_1.addItem("C");
		JLabel loc1 = new JLabel("市");//
		loc1.setBounds(155, 240, 40, 30);
		contentPane.add(loc1);
		
		final JComboBox loc_2 = new JComboBox();
		loc_2.setBounds(200, 240, 50, 30);
		contentPane.add(loc_2);
		loc_2.addItem("a");
		loc_2.addItem("b");
		loc_2.addItem("c");
		loc_2.addItem("d");
		loc_2.addItem("e");
		JLabel loc2 = new JLabel("区/县");//
		loc2.setBounds(255, 240, 50, 30);
		contentPane.add(loc2);
		
		final JComboBox loc_3 = new JComboBox();
		loc_3.setBounds(300, 240, 50, 30);
		contentPane.add(loc_3);
		loc_3.addItem("1");
		loc_3.addItem("2");
		loc_3.addItem("3");
		loc_3.addItem("4");
		loc_3.addItem("5");
		JLabel loc3 = new JLabel("街道");//
		loc3.setBounds(355, 240, 50, 30);
		contentPane.add(loc3);
		
		
		JButton btn = new JButton("开始解密");
		btn.setBounds(155, 305, 160, 30);
		contentPane.add(btn);
		btn.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	String pname = TF_pname.getText();
		    	String username = TF_username.getText();
		    	System.out.println("pname:" + pname);
				//获取访问时间
		    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		    	Date date = null;
				try {
					date = simpleDateFormat.parse(time.getText());
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
		        long ts = date.getTime();
		        String t = String.valueOf(ts);
		        System.out.println(date);
				System.out.println(t);
		    	//获取访问位置（下拉框）
				String loc1 = loc_1.getSelectedItem().toString();
				String loc2 = loc_2.getSelectedItem().toString();
				String loc3 = loc_3.getSelectedItem().toString();
				String[] addr = {loc1, loc2, loc3};
				String loc = deal_loc(addr);
				//System.out.println(addr);
				System.out.println("loc_code:" + loc);
				
				String pathname = "D:\\encode\\";
			    String[] list=new File(pathname).list();
			    String fname = null;
				for (int i = 0; i < list.length; i++) {
					int index = list[i].indexOf(pname);
					if (index != -1) {
						fname = list[i];
						break;
					}
				}
				String rename = fname.substring(0,fname.lastIndexOf(".")) + "_re.txt";
				int len = pname.length();
				
				info.setText(info.getText()+"重加密成功！\n");
				//info.setText(info.getText()+"重加密密文保存至"+pathname + rename + "\n");
				
				String basename = "D:\\re-encode\\";
			    String[] lis=new File(basename).list();
			    fname = null;
				for (int i = 0; i < lis.length; i++) {
					int index = lis[i].indexOf(pname);
					if (index != -1) {
						fname = lis[i];
						break;
					}
				}
				String decname = fname.substring(0,fname.lastIndexOf(".")) + "_dec.txt";
				
				String filename = pathname + fname;
				File in = new File(filename);
				System.out.println(username);
				int flag = 0;
				if (username.equals("Manager")) {
					pathname = "D:\\decode\\Manager\\";
					Manager.dec(in, pathname, t, loc);
				}else if (username.equals("Courier1")) {
					pathname = "D:\\decode\\c1";
					Courier1.dec(in, pathname, t, loc);
				}else if (username.equals("Courier2")) {
					pathname = "D:\\decode\\c2";
					Courier2.dec(in, pathname, t, loc);
				}else if (username.equals("Courier3")) {
					pathname = "D:\\decode\\c3";
					Courier3.dec(in, pathname, t, loc);
				}
				if (flag == 0){
				info.setText(info.getText()+"解密成功！\n");
				info.setText(info.getText()+"解密后的物流信息保存至"+ pathname + decname +"\n");
				}
				else {
				info.setText(info.getText()+"解密失败！不满足访问策略！\n");}
				
			}
		});
    	return contentPane;
    }
    
    
    protected JComponent makeRevocPanel() {
    	JPanel contentPane = new JPanel(false);
    	contentPane.setLayout(null);
    	
    	JLabel Linfo = new JLabel("输出信息:");
        Linfo.setBounds(10, 340, 100, 30);
        contentPane.add(Linfo);
        final JTextArea info = new JTextArea();
        info.setBounds(10, 370, 480, 120);
        contentPane.add(info);
        info.setEditable(false);
    	
    	
    	JLabel lblNewLabel0 = new JLabel("用户名：");//用户名（输入）
		lblNewLabel0.setBounds(10, 10, 90, 30);//x,y,width,height
		contentPane.add(lblNewLabel0);
		
		final JTextField TF_username = new JTextField();
		TF_username.setBounds(80, 10, 150, 30);
		contentPane.add(TF_username);
		TF_username.setColumns(10);
		
		
    	JLabel lblNewLabel1 = new JLabel("当前用户类型：");//当前用户类型（选择）
		lblNewLabel1.setBounds(250, 10, 90, 30);//x,y,width,height
		contentPane.add(lblNewLabel1);
		
		final JComboBox user_type = new JComboBox();
		user_type.setBounds(340, 10, 150, 30);
		contentPane.add(user_type);
		user_type.addItem("--请选择--");    //向下拉列表中添加一项
		user_type.addItem("用户");
		user_type.addItem("管理员");
		user_type.addItem("快递员");
		//comboBox.addItem("快递员1");
		//comboBox.addItem("快递员2");
		
		JLabel lblNewLabel2 = new JLabel("请输入要撤销属性的用户：");//选择yonghu
		lblNewLabel2.setBounds(10, 50, 180, 30);
		contentPane.add(lblNewLabel2);
		
		final JTextField TF_uname = new JTextField();
		TF_uname.setBounds(190, 50, 210, 30);
		contentPane.add(TF_uname);
		TF_uname.setColumns(10);
		
		JLabel lblNewLabel3 = new JLabel("请输入要撤销该用户的属性：");//选择属性
		lblNewLabel3.setBounds(10, 90, 180, 30);
		contentPane.add(lblNewLabel3);
		
		final JTextField TF_uattr = new JTextField();
		TF_uattr.setBounds(190, 90, 210, 30);
		contentPane.add(TF_uattr);
		TF_uattr.setColumns(10);
		
		JButton btn = new JButton("确认撤销属性");
		btn.setBounds(155, 305, 160, 30);
		contentPane.add(btn);
		btn.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	String username = TF_username.getText();
		    	System.out.println("username" + username);
		    	if (username.length() == 0) {
		    		info.setText(info.getText()+"请输入当前用户名！\n");
		    	}
		    	
		    	if (username.equals("Manager") & user_type.getSelectedItem().equals("管理员")) {
		    		revoke_attr(TF_uname.getText(), TF_uattr.getText());
		    		//client从server处获取公钥字符串
		    		String PKJSONString1 = server.getPublicKeyInString();
					SecretKey key = null;
		    		Courier1.setPK(PKJSONString1);
		    		Courier2.setPK(PKJSONString1);
		    		Manager.setPK(PKJSONString1);

		    		//client将自己的属性信息发送给server,并获取私钥字符串
					key = server.generateSecretKey(Courier1.getId(), Courier1.getAttrs());
		    		Courier1.setSK(key);
		    				
		    		key = server.generateSecretKey(Courier2.getId(), Courier2.getAttrs());
		    		Courier2.setSK(key);
		    				
		    		key = server.generateSecretKey(Manager.getId(), Manager.getAttrs());
		    		Manager.setSK(key);
					info.setText(info.getText()+"属性撤销成功！相关密钥、密文已更新。\n");
				}else {
					info.setText(info.getText()+"策略更新失败！没有权限！！\n");
				}
				
			}
		});
		
		return contentPane;
    }
    
    
    protected void revoke_attr(String uname, String attr) {
		// TODO Auto-generated method stub
    	if (uname.equals("Manager")) {
    		String[] attrs = Manager.getAttrs();
    		String[] new_attrs = remove(attrs, attr);
			Manager.setAttrs(new_attrs);
		}else if (uname.equals("Courier1")) {
			String[] attrs = Courier1.getAttrs();
    		String[] new_attrs = remove(attrs, attr);
			Courier1.setAttrs(new_attrs);
		}else if (uname.equals("Courier2")) {
			String[] attrs = Courier2.getAttrs();
    		String[] new_attrs = remove(attrs, attr);
			Courier2.setAttrs(new_attrs);
		}else if (uname.equals("Courier3")) {
			String[] attrs = Courier3.getAttrs();
    		String[] new_attrs = remove(attrs, attr);
			Courier3.setAttrs(new_attrs);
		}
		
	}
    
	private String[] remove(String[] attrs, String attr) {
		// TODO Auto-generated method stub
		String[] tmp = new String[attrs.length - 1];
		int len = attrs.length;  
		int j = 0;
		for (int i = 0; i < len; i++) {
			if(attrs[i].equals(attr)) {
				
			}else {
				tmp[j] = attrs[i];
				j++;
			}
		}
		return tmp;
	}
	
	
	protected JComponent makeRenewPanel() {
    	JPanel contentPane = new JPanel(false);
    	contentPane.setLayout(null);
    	
    	JLabel Linfo = new JLabel("输出信息:");
        Linfo.setBounds(10, 340, 100, 30);
        contentPane.add(Linfo);
        final JTextArea info = new JTextArea();
        info.setBounds(10, 370, 480, 120);
        contentPane.add(info);
        info.setEditable(false);
    	
    	
    	JLabel lblNewLabel0 = new JLabel("用户名：");//用户名（输入）
		lblNewLabel0.setBounds(10, 10, 90, 30);//x,y,width,height
		contentPane.add(lblNewLabel0);
		
		final JTextField TF_username = new JTextField();
		TF_username.setBounds(80, 10, 150, 30);
		contentPane.add(TF_username);
		TF_username.setColumns(10);
		
		
    	JLabel lblNewLabel1 = new JLabel("当前用户类型：");//当前用户类型（选择）
		lblNewLabel1.setBounds(250, 10, 90, 30);//x,y,width,height
		contentPane.add(lblNewLabel1);
		
		final JComboBox user_type = new JComboBox();
		user_type.setBounds(340, 10, 150, 30);
		contentPane.add(user_type);
		user_type.addItem("--请选择--");    //向下拉列表中添加一项
		user_type.addItem("用户");
		user_type.addItem("管理员");
		user_type.addItem("快递员");
		//comboBox.addItem("快递员1");
		//comboBox.addItem("快递员2");
		
		JLabel lblNewLabel2 = new JLabel("请选择要更新访问策略的文件：");//选择文件 filechooser
		lblNewLabel2.setBounds(10, 50, 200, 30);
		contentPane.add(lblNewLabel2);
		
		final JTextField TF_filename = new JTextField();
		TF_filename.setBounds(180, 50, 220, 30);
		contentPane.add(TF_filename);
		TF_filename.setColumns(10);
		
		JButton choose_file = new JButton("请选择...");
		choose_file.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				JFileChooser jfc=new JFileChooser();
				jfc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES );
				jfc.showDialog(new JLabel(), "选择");
				File file=jfc.getSelectedFile();
				if(file.isDirectory()){
					System.out.println("文件夹:"+file.getAbsolutePath());
				}else if(file.isFile()){
					System.out.println("文件:"+file.getAbsolutePath());
				}
				TF_filename.setText(file.getAbsolutePath());
				System.out.println(jfc.getSelectedFile().getName());
			}
		});
		choose_file.setBounds(410, 50, 80, 30);
		contentPane.add(choose_file);
		
		JLabel lblNewLabel3 = new JLabel("请输入新的访问策略：");//
		lblNewLabel3.setBounds(10, 90, 160, 30);
		contentPane.add(lblNewLabel3);
		
		final JTextField TF_policy = new JTextField();
		TF_policy.setBounds(130, 90, 270, 30);
		contentPane.add(TF_policy);
		TF_policy.setColumns(10);
		
		
		
		JLabel lblNewLabel4 = new JLabel("访问时间约束：");//
		lblNewLabel4.setBounds(10, 130, 160, 30);
		contentPane.add(lblNewLabel4);
		JLabel dao = new JLabel("到");//
		dao.setBounds(282, 165, 20, 30);
		contentPane.add(dao);
		
		//year
		final JTextField TF_year1 = new JTextField();
		TF_year1.setBounds(100, 130, 60, 30);
		contentPane.add(TF_year1);
		TF_year1.setColumns(10);
		final JTextField TF_year2 = new JTextField();
		TF_year2.setBounds(100, 200, 60, 30);
		contentPane.add(TF_year2);
		TF_year2.setColumns(10);
		JLabel year = new JLabel("年");//
		year.setBounds(162, 130, 20, 30);
		contentPane.add(year);
		JLabel year2 = new JLabel("年");//
		year2.setBounds(162, 200, 20, 30);
		contentPane.add(year2);
		
		//month
		final JTextField TF_month1 = new JTextField();
		TF_month1.setBounds(180, 130, 60, 30);
		contentPane.add(TF_month1);
		TF_month1.setColumns(10);
		final JTextField TF_month2 = new JTextField();
		TF_month2.setBounds(180, 200, 60, 30);
		contentPane.add(TF_month2);
		TF_month2.setColumns(10);
		JLabel mon = new JLabel("月");//
		mon.setBounds(242, 130, 20, 30);
		contentPane.add(mon);
		JLabel mon2 = new JLabel("月");//
		mon2.setBounds(242, 200, 20, 30);
		contentPane.add(mon2);
		
		//day
		final JTextField TF_day1 = new JTextField();
		TF_day1.setBounds(260, 130, 60, 30);
		contentPane.add(TF_day1);
		TF_day1.setColumns(10);
		final JTextField TF_day2 = new JTextField();
		TF_day2.setBounds(260, 200, 60, 30);
		contentPane.add(TF_day2);
		TF_day2.setColumns(10);
		JLabel day = new JLabel("日");//
		day.setBounds(322, 130, 20, 30);
		contentPane.add(day);
		JLabel day2 = new JLabel("日");//
		day2.setBounds(322, 200, 20, 30);
		contentPane.add(day2);
		
		//hour
		final JTextField TF_hour1 = new JTextField();
		TF_hour1.setBounds(340, 130, 60, 30);
		contentPane.add(TF_hour1);
		TF_hour1.setColumns(10);
		final JTextField TF_hour2 = new JTextField();
		TF_hour2.setBounds(340, 200, 60, 30);
		contentPane.add(TF_hour2);
		TF_hour2.setColumns(10);
		JLabel hour = new JLabel("时");//
		hour.setBounds(402, 130, 20, 30);
		contentPane.add(hour);
		JLabel hour2 = new JLabel("时");//
		hour2.setBounds(402, 200, 20, 30);
		contentPane.add(hour2);
		
		//minute
		final JTextField TF_min1 = new JTextField();
		TF_min1.setBounds(420, 130, 60, 30);
		contentPane.add(TF_min1);
		TF_min1.setColumns(10);
		final JTextField TF_min2 = new JTextField();
		TF_min2.setBounds(420, 200, 60, 30);
		contentPane.add(TF_min2);
		TF_min2.setColumns(10);
		JLabel min = new JLabel("分");//
		min.setBounds(482, 130, 20, 30);
		contentPane.add(min);
		JLabel min2 = new JLabel("分");//
		min2.setBounds(482, 200, 20, 30);
		contentPane.add(min2);
		
		
		
		JLabel lblNewLabel5 = new JLabel("访问空间约束：");//
		lblNewLabel5.setBounds(10, 250, 160, 30);
		contentPane.add(lblNewLabel5);
		
		String[] loc = {"0"};
		final JComboBox loc_1 = new JComboBox();
		loc_1.setBounds(100, 250, 50, 30);
		contentPane.add(loc_1);
		loc_1.addItem("A");
		loc_1.addItem("B");
		loc_1.addItem("C");
		
		JLabel loc1 = new JLabel("市");//
		loc1.setBounds(155, 250, 40, 30);
		contentPane.add(loc1);
		
		final JComboBox loc_2 = new JComboBox();
		loc_2.setBounds(200, 250, 50, 30);
		contentPane.add(loc_2);
		loc_2.addItem("a");
		loc_2.addItem("b");
		loc_2.addItem("c");
		loc_2.addItem("d");
		loc_2.addItem("e");
		JLabel loc2 = new JLabel("区/县");//
		loc2.setBounds(255, 250, 50, 30);
		contentPane.add(loc2);
		
		final JComboBox loc_3 = new JComboBox();
		loc_3.setBounds(300, 250, 50, 30);
		contentPane.add(loc_3);
		loc_3.addItem("1");
		loc_3.addItem("2");
		loc_3.addItem("3");
		loc_3.addItem("4");
		loc_3.addItem("5");
		JLabel loc3 = new JLabel("街道");//
		loc3.setBounds(355, 250, 50, 30);
		contentPane.add(loc3);
		
		
		JButton btn = new JButton("确认更新策略");
		btn.setBounds(155, 305, 160, 30);
		contentPane.add(btn);
		btn.addActionListener(new ActionListener() {
		    public void actionPerformed(ActionEvent e) {
		    	String username = TF_username.getText();
		    	System.out.println("username" + username);
		    	if (username.length() == 0) {
		    		info.setText(info.getText()+"请输入当前用户名！\n");
		    	}
		    	
		    	String filename = TF_filename.getText();
		    	System.out.println("filename" + filename);
		    	if (filename.length() == 0) {
		    		info.setText(info.getText()+"请选择要更改访问策略的文件！\n");
		    	}
		    	
				String policy = TF_policy.getText();
				System.out.println("policy:" + policy);
				if (policy.length() == 0) {
		    		info.setText(info.getText()+"请输入新的访问策略！\n");
		    	}
				//获取开始时间
				String start_time = new String();
				try {
					start_time = dateToStamp(TF_year1.getText(), 
										     TF_month1.getText(), 
										     TF_day1.getText(), 
										     TF_hour1.getText(), 
										     TF_min1.getText());
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
					info.setText(info.getText()+"请输入正确的时间范围约束！\n");
				}
				System.out.println(start_time);
				//获取结束时间
				String end_time = new String();
				try {
					end_time = dateToStamp(TF_year2.getText(), 
										     TF_month2.getText(), 
										     TF_day2.getText(), 
										     TF_hour2.getText(), 
										     TF_min2.getText());
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					//e1.printStackTrace();
					info.setText(info.getText()+"请输入正确的时间范围约束！\n");
				}
				System.out.println(end_time);
		    	//获取空间限制（下拉框）
				String loc1 = loc_1.getSelectedItem().toString();
				String loc2 = loc_2.getSelectedItem().toString();
				String loc3 = loc_3.getSelectedItem().toString();
				String[] addr = {loc1, loc2, loc3};
				String loc = deal_loc(addr);
				System.out.println(loc);
				String fname = filename.substring(filename.lastIndexOf("\\")+1);
				String outfile = fname.substring(0,fname.lastIndexOf(".")) + "_enc.txt";
				int len = filename.length();
				String pathname = filename.substring(0, len-fname.length())+"cipher\\";
				File in = new File(filename);
				if (username.equals("Manager") & user_type.getSelectedItem().equals("管理员")) {
					Manager.renew_policy(in, policy, outfile, pathname, start_time, end_time, loc, loc);
					info.setText(info.getText()+"策略更新成功！相关密文已更新。\n");
				}else {
					info.setText(info.getText()+"策略更新失败！没有权限！！\n");
				}
				
			}
		});
		
		
		return contentPane;
    }
    
    
    class TimeActionListener implements ActionListener{
        public TimeActionListener(){
            Timer t=new Timer(1000,this);
            t.start();
        }

        @Override
        public void actionPerformed(ActionEvent ae){
            time.setText(myfmt.format(new Date()).toString());
        }
    }

    public static String dateToStamp(String y,String M, String d, String h, String m) throws ParseException {
//		dateToStamp s:2000-01-01 00:00
//		946656000000
//		dateToStamp s:2050-01-01 00:00
//		2524579200000
        String res;
        if (y.length() != 4) {
        	res = "时间格式错误";
        }
        if (M.length() != 2) {
        	M = '0' + M;
        }
        if (d.length() != 2) {
        	d = '0' + d;
        }
        if (h.length() != 2) {
        	h = '0' + h;
        }
        if (m.length() != 2) {
        	m = '0' + m;
        }
        if (Integer.valueOf(M) > 12) {
        	res = "时间格式错误";
        }
        int[] day_count = {31,28,31,30,31,30,31,31,30,31,30,31};
        if (Integer.valueOf(d) > day_count[Integer.valueOf(M)-1]) {
        	res = "时间格式错误";
        }
        if (Integer.valueOf(h) > 24) {
        	res = "时间格式错误";
        }
        if (Integer.valueOf(m) > 60) {
        	res = "时间格式错误";
        }
        String s = y + '-' + M + '-' + d + ' ' + h + ':' + m;
        System.out.println("dateToStamp s:" + s);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm"); 
        Date date = simpleDateFormat.parse(s);
        long ts = date.getTime();
        res = String.valueOf(ts);
        return res;
    }

    public static String deal_loc(String[] addr) {
//		00000
//		132334
    	String act_list[][] = {{"A", "B", "C", "D"},{"a", "b", "c", "d"},{"1", "2", "3", "4"}};
		String vir_list[][] = {{"10", "11", "12", "13"},{"20", "21", "22", "23"}, {"30", "31", "32", "33"}};
		int n = 6;
		int len = addr.length;
		String ret = "";
		for (int i = 0; i < len; i++) {
			int index = find(act_list[i], addr[i]);
			//System.out.println(index);
			if (index != -1){
				ret += vir_list[i][index];
			}else {
				ret += "00";
			}
			//System.out.println(vir_list[i][index]);
			//System.out.println(ret);
		}
		while(ret.length() != n) {
			ret += "0";
		}
		return ret;
    }
    
    public static int find(String[] list, String target) {
		int index = -1;
		int len = list.length;
		for (int i = 0; i < len; i++) {
			if (target == list[i]) {
				index = i;
			}
		}
		return index;
	}
    
    
}
