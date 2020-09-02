package indian;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;


import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import java.util.StringTokenizer;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import java.awt.List;
import javax.swing.SwingConstants;
import javax.swing.ImageIcon;
import java.awt.Font;
import java.awt.Color;
//import com.jgoodies.forms.factories.DefaultComponentFactory;

public class IndianClient extends JFrame implements Runnable, ActionListener {

	private JPanel contentPane;			//�����г�
	private JTextField nameBox;			//�̸� �Է� �ʵ�
	private JTextField roomBox;			//�� ��ȣ �Է� �ʵ�
	private JTextField sendBox;			//ä�� �Է� �ʵ�
	private JTextArea msgView;			//ä�� view ���̸���
	private JLabel infoView;			//���ӿ� ���� ������ �����ִ� ��
	private JLabel myId_lbl;			//(�ΰ���) �ڽ��� �̸� ��
	private JLabel yourId_lbl;			//(�ΰ���) ����� �̸� ��
	private JLabel myCoin_lbl;			//(�ΰ���) �ڽ��� �� ���� ��
	private JLabel yourCoin_lbl;		//(�ΰ���) ����� �� ���� ��
	private JLabel result_lbl;			//(�ΰ���) ��/��/�� ��
	private JButton startButton;		// ���� ��ư
	private JButton dieButton;			// (�ΰ���) ���� ��ư
	private JButton batt5Button;		// (�ΰ���) 5���� ��ư
	private JButton callButton;			// (�ΰ���) �� ��ư
	private JButton ruleButton;			// ���ӷ� ��ư
	private JButton rankButton;			// ��ŷ ��ư
	private JLabel myCard_lbl;			// (�ΰ���) �ڽ��� ���忡 ���� ī�� ��
	private JLabel yourCard_lbl;		// (�ΰ���) ����� ���忡 ���� ī�� ��
	private List pList;					// ���� & ���ӹ��� ������ ���
	private JLabel name_lbl;			// nameBox�� �˷��ִ� ��
	private JLabel roomNum_lbl;			// roomBox�� �˷��ִ� ��
	private JButton batt10Button;		// (�ΰ���) 10���� ��ư
	private JButton batt1Button;		// (�ΰ���) 1���� ��ư
	private JButton enterButton;		// �����ϱ� ��ư
	private JButton exitButton;			// ���Ƿ� ��ư
	private JLabel pInfo;				// ���� & ���ӹ��� ������ ���� �˷��ִ� ��
	private JLabel myBatt_lbl;			// (�ΰ���) �ڽ��� ���� ����
	private JLabel yourBatt_lbl;		// (�ΰ���) ����� ���� ����
	
	private BufferedReader reader;      // �Է� ��Ʈ��
	private PrintWriter writer;         // ��� ��Ʈ��
	private Socket socket;              // ����
	private int roomNumber=-1;          // �� ��ȣ
	private int round = 1;				// ����
	private int yourcard=0;				// ���� �� ��� ī���� ��
	private int endcoin=0;				// ���� �� ������ ������ ����� ���� ��
	private int roundnine_sw =0;		// //�������� ���� ������ ���� �߰�, ������ ����� ���п��� �񱳿� ����ϴ� ����
	private String[] coinInfo;			// ����� ���� ������ �����ϴ� ����   coinInfo[0] : ����� ��������
										//						   coinInfo[1] : ����� ��������
	private User myuser = new User();	// �÷��̾� ���� ( User.java ����)
	ImageIcon icon;						
	
	private boolean enable=false;
	 
	private boolean running=false;       // ������ ���� ���ΰ��� ��Ÿ���� ����
	private JLabel meicon;			
	private JLabel usericonu;
	private JLabel mCoinC;
	private JLabel coin_01;
	private JLabel yCoinC;
	private JLabel coin_02;
	private JButton btnAllin;			// (�ΰ���) ���� ��ư
	private JButton stopButton;			// (�ΰ���) ��� ��ư 
	private JLabel ledLabel;
	private JLabel betLabel;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {			
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IndianClient frame = new IndianClient();
					frame.setVisible(true);
					frame.connect();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public IndianClient() {

		icon = new ImageIcon(IndianClient.class.getResource("/img/mainb.jpg"));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 900, 640);
		contentPane = new JPanel() {
			public void paintComponent(Graphics g) {
				 g.drawImage(icon.getImage(), 0, 0, this.getWidth(), this.getHeight(), this);
	                // Approach 2: Scale image to size of component
	                // Dimension d = getSize();
	                // g.drawImage(icon.getImage(), 0, 0, d.width, d.height, null);
	                // Approach 3: Fix the image position in the scroll pane
	                // Point p = scrollPane.getViewport().getViewPosition();
	                // g.drawImage(icon.getImage(), p.x, p.y, null);
	                setOpaque(false); //�׸��� ǥ���ϰ� ����,�����ϰ� ����
	                
	                super.paintComponent(g);
			 }
		};
			
		setResizable(false);
		
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		myId_lbl = new JLabel("");
		myId_lbl.setForeground(Color.BLACK);
		myId_lbl.setFont(new Font("�����ձ۾� ��", Font.BOLD, 20));
		myId_lbl.setBounds(195, 111, 80, 40);
		contentPane.add(myId_lbl);
		
		yourId_lbl = new JLabel("");
		yourId_lbl.setForeground(Color.BLACK);
		yourId_lbl.setFont(new Font("�����ձ۾� ��", Font.BOLD, 20));
		yourId_lbl.setBounds(470, 111, 80, 40);
		contentPane.add(yourId_lbl);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(40, 430, 580, 130);
		contentPane.add(scrollPane);
		
		msgView = new JTextArea();
		scrollPane.setViewportView(msgView);
		
		myCoin_lbl = new JLabel("");
		myCoin_lbl.setFont(new Font("�����ձ۾� ��", Font.BOLD, 13));
		myCoin_lbl.setBounds(162, 385, 40, 40);
		contentPane.add(myCoin_lbl);
		
		yourCoin_lbl = new JLabel("");
		yourCoin_lbl.setForeground(Color.RED);
		yourCoin_lbl.setFont(new Font("�����ձ۾� ��", Font.BOLD, 13));
		yourCoin_lbl.setBounds(479, 385, 40, 40);
		contentPane.add(yourCoin_lbl);
		
		result_lbl = new JLabel("");
		result_lbl.setFont(new Font("�����ձ۾� ��", Font.BOLD, 13));
		result_lbl.setHorizontalAlignment(SwingConstants.CENTER);
		result_lbl.setBounds(165, 150, 80, 30);
		contentPane.add(result_lbl);
		
		startButton = new JButton("\uC2DC\uC791");
		startButton.setFont(new Font("�����ձ۾� ��", Font.PLAIN, 13));
		startButton.setBounds(650, 252, 100, 40);
		contentPane.add(startButton);
		startButton.setEnabled(false);
		
		dieButton = new JButton("\uB2E4\uC774");
		dieButton.setFont(new Font("�����ձ۾� ��", Font.PLAIN, 13));
		dieButton.setBounds(773, 430, 100, 40);
		contentPane.add(dieButton);
		dieButton.setEnabled(false);
		
		batt5Button = new JButton("");
		batt5Button.setIcon(new ImageIcon(IndianClient.class.getResource("/img/fchip.png")));
		batt5Button.setBounds(650, 430, 100, 40);
		contentPane.add(batt5Button);
		batt5Button.setEnabled(false);
		
		callButton = new JButton("\uCF5C");
		callButton.setFont(new Font("�����ձ۾� ��", Font.PLAIN, 13));
		callButton.setBounds(773, 380, 100, 40);
		contentPane.add(callButton);
		callButton.setEnabled(false);
		
		ruleButton = new JButton("\uAC8C\uC784\uB8F0");
		ruleButton.setFont(new Font("�����ձ۾� ��", Font.PLAIN, 13));
		ruleButton.addActionListener(new ActionListener() {		//���ӷ� ��ư�� ������ Rule �ڹ��� ������ Frameâ�� �����
			public void actionPerformed(ActionEvent e) {
				Rule rz = new Rule();
				rz.setVisible(true);
			}
		});
		ruleButton.setBounds(773, 252, 100, 40);
		contentPane.add(ruleButton);
		
		pInfo = new JLabel("\uB300\uAE30\uC2E4: \uBA85");
		pInfo.setBounds(650, 120, 100, 25);
		contentPane.add(pInfo);
		
		myCard_lbl = new JLabel("New label");
		myCard_lbl.setIcon(new ImageIcon(IndianClient.class.getResource("/img/0.png")));
		myCard_lbl.setBounds(90, 175, 140, 210);
		contentPane.add(myCard_lbl);
		
		yourCard_lbl = new JLabel("New label");
		yourCard_lbl.setIcon(new ImageIcon(IndianClient.class.getResource("/img/0.png")));
		yourCard_lbl.setBounds(400, 175, 140, 210);
		contentPane.add(yourCard_lbl);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(40, 560, 580, 30);
		contentPane.add(scrollPane_1);
		
		sendBox = new JTextField();
		scrollPane_1.setViewportView(sendBox);
		sendBox.setColumns(10);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(650, 150, 220, 100);
		contentPane.add(scrollPane_2);
		
		pList = new List();
		scrollPane_2.setViewportView(pList);
		
		name_lbl = new JLabel("\uC774\uB984");
		name_lbl.setBounds(650, 30, 90, 25);
		contentPane.add(name_lbl);
		
		roomNum_lbl = new JLabel("\uBC29\uBC88\uD638");
		roomNum_lbl.setBounds(650, 60, 90, 25);
		contentPane.add(roomNum_lbl);
		
		nameBox = new JTextField();
		nameBox.setBounds(750, 30, 120, 25);
		contentPane.add(nameBox);
		nameBox.setColumns(10);
		
		roomBox = new JTextField();
		roomBox.setBounds(750, 60, 120, 25);
		contentPane.add(roomBox);
		roomBox.setColumns(10);
		
		batt10Button = new JButton("");
		batt10Button.setIcon(new ImageIcon(IndianClient.class.getResource("/img/tchip.png")));
		batt10Button.setBounds(650, 480, 100, 40);
		contentPane.add(batt10Button);
		batt10Button.setEnabled(false);
		
		batt1Button = new JButton("");
		batt1Button.setIcon(new ImageIcon(IndianClient.class.getResource("/img/ochip.png")));
		batt1Button.setBounds(650, 380, 100, 40);
		contentPane.add(batt1Button);
		batt1Button.setEnabled(false);
		
		enterButton = new JButton("\uC785\uC7A5\uD558\uAE30");
		enterButton.setBounds(650, 91, 100, 25);
		contentPane.add(enterButton);
		
		exitButton = new JButton("\uB300\uAE30\uC2E4\uB85C");
		exitButton.setBounds(770, 90, 100, 25);
		contentPane.add(exitButton);
		
		infoView = new JLabel("\uB300\uAE30\uC911....");
		infoView.setFont(new Font("�����ձ۾� ��", Font.PLAIN, 13));
		infoView.setHorizontalAlignment(SwingConstants.CENTER);
		infoView.setBounds(65, 20, 485, 40);
		contentPane.add(infoView);
		
		myBatt_lbl = new JLabel("");
		myBatt_lbl.setFont(new Font("�����ձ۾� ��", Font.BOLD, 13));
		myBatt_lbl.setHorizontalAlignment(SwingConstants.CENTER);
		myBatt_lbl.setBounds(250, 227, 65, 50);
		myBatt_lbl.setForeground(Color.YELLOW);
		myBatt_lbl.setBackground(Color.BLACK);
		myBatt_lbl.setOpaque(true); 
		contentPane.add(myBatt_lbl);
		
		yourBatt_lbl = new JLabel("");
		yourBatt_lbl.setFont(new Font("�����ձ۾� ��", Font.BOLD, 13));
		yourBatt_lbl.setHorizontalAlignment(SwingConstants.CENTER);
		yourBatt_lbl.setBounds(315, 227, 65, 50);
		yourBatt_lbl.setForeground(Color.YELLOW);
		yourBatt_lbl.setBackground(Color.BLACK);
		yourBatt_lbl.setOpaque(true); 
		contentPane.add(yourBatt_lbl);
		
		meicon = new JLabel("");
		meicon.setIcon(new ImageIcon(IndianClient.class.getResource("/img/angel.png")));
		meicon.setBounds(100, 65, 80, 100);
		contentPane.add(meicon);
		meicon.setEnabled(false);
		
		usericonu = new JLabel("");
		usericonu.setIcon(new ImageIcon(IndianClient.class.getResource("/img/devil.png")));
		usericonu.setBounds(388, 65, 80, 100);
		contentPane.add(usericonu);
		usericonu.setEnabled(false);
		
		mCoinC = new JLabel("\uB0B4 \uCF54\uC778");
		mCoinC.setForeground(Color.BLACK);
		mCoinC.setFont(new Font("�����ձ۾� ��", Font.BOLD, 13));
		mCoinC.setBounds(90, 385, 60, 40);
		contentPane.add(mCoinC);
		
		coin_01 = new JLabel("");
		coin_01.setIcon(new ImageIcon(IndianClient.class.getResource("/img/coins.png")));
		coin_01.setBounds(209, 390, 40, 30);
		contentPane.add(coin_01);
		
		yCoinC = new JLabel("\uC0C1\uB300 \uCF54\uC778");
		yCoinC.setForeground(Color.RED);
		yCoinC.setFont(new Font("�����ձ۾� ��", Font.BOLD, 13));
		yCoinC.setBounds(398, 385, 80, 40);
		contentPane.add(yCoinC);
		
		coin_02 = new JLabel("");
		coin_02.setIcon(new ImageIcon(IndianClient.class.getResource("/img/coins.png")));
		coin_02.setBounds(520, 390, 40, 30);
		contentPane.add(coin_02);
		
		rankButton = new JButton("Rank");
		rankButton.setFont(new Font("�����ձ۾� ��", Font.PLAIN, 13));
		rankButton.setEnabled(true);
		rankButton.setBounds(773, 302, 100, 40);
		contentPane.add(rankButton);
		rankButton.addActionListener(new ActionListener() {		//��ŷ ��ư�� ������ IndianDAO �� ��ŷ ������ Frameâ�� �����
			public void actionPerformed(ActionEvent e) {
				IndianDAO id;
				try {
					id = new IndianDAO();
					id.setVisible(true);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});
		
		btnAllin = new JButton("All-In");
		btnAllin.setFont(new Font("�����ձ۾� ��", Font.PLAIN, 13));
		btnAllin.setEnabled(false);
		btnAllin.setBounds(773, 480, 100, 40);
		contentPane.add(btnAllin);
		
		stopButton = new JButton("\uAE30\uAD8C");
		stopButton.setFont(new Font("�����ձ۾� ��", Font.PLAIN, 13));
		stopButton.setEnabled(false);
		stopButton.setBounds(650, 302, 100, 40);
		contentPane.add(stopButton);
		
		ledLabel = new JLabel("");
		ledLabel.setIcon(new ImageIcon(IndianClient.class.getResource("/img/ledb.jpg")));
		ledLabel.setBounds(250, 270, 130, 80);
		ledLabel.setBackground(Color.BLACK);
		ledLabel.setOpaque(true); 
		contentPane.add(ledLabel);
		
		betLabel = new JLabel(" \uB098\uC758 BET  | \uC801\uC758 BET");
		betLabel.setForeground(Color.YELLOW);
		betLabel.setBackground(Color.BLACK);
		betLabel.setOpaque(true); 
		betLabel.setFont(new Font("�����ձ۾� ��", Font.BOLD, 10));
		betLabel.setBounds(250, 192, 130, 40);
		contentPane.add(betLabel);
		
		sendBox.addActionListener(this);
	    enterButton.addActionListener(this);
	    exitButton.addActionListener(this);
	    startButton.addActionListener(this);
	    dieButton.addActionListener(this);
	    callButton.addActionListener(this);
	    batt1Button.addActionListener(this);
	    batt5Button.addActionListener(this);
	    batt10Button.addActionListener(this);
	    btnAllin.addActionListener(this);
	    stopButton.addActionListener(this);
		
	}

	public boolean isRunning(){           // ������ ���� ���¸� ��ȯ�Ѵ�.
	    return running; 
	}
	
	public void enabled(boolean b) {		// ��ư Ȱ��ȭ �Լ� �ڽ��� ���̸� true , ����� false 
		
		dieButton.setEnabled(b);
		batt1Button.setEnabled(b);
		batt5Button.setEnabled(b);
		batt10Button.setEnabled(b);
		callButton.setEnabled(b);
		btnAllin.setEnabled(b);
		
	}
	
	public int[] deck_create(int[] deck) {			// 1~10���� ������ ī��ѹ��� �����ϴ� �Լ� 
		
		int[] rndDeck = new int[deck.length];
		int ran;
		boolean cheak;
		Random r = new Random();
		
		for(int i=0;i<deck.length;i++) {
			ran= r.nextInt(10)+1;
			cheak = true;
			for(int j=0;j<i;j++) {
				if(rndDeck[j] == ran) {
					i--;
					cheak=false;
				}
			}
			if(cheak)
				rndDeck[i] = ran;
		}
		return rndDeck;	
	}
	
	void addLog(String log)						//ä��View�� �޼����� �߰��ϰ�, �����͸� ���� ���������� �ּ� ��ũ�ѹٸ� �����ִ� �Լ�
	{
	  msgView.append(log + "\n");  
	  msgView.setCaretPosition(msgView.getDocument().getLength()); 
	}
	
	public void batting() {						//�ʱ����(1)���� ����ϴ� �Լ�
		
		int bsBatt = 1; // �⺻����
		
		myuser.setMyCoin(myuser.getMyCoin()-bsBatt);						//�ʱ������ �ϰ� �ڽ��� ���ΰ� ���������� ������ �ؽ�Ʈ�� �ٲ��ش�.
		myuser.setBatCoin(bsBatt);
		myCoin_lbl.setText(Integer.toString(myuser.getMyCoin()));
		myBatt_lbl.setText(Integer.toString(myuser.getBatCoin()));
		
		writer.println("[bsBATTING]"+myuser.getMyCoin()+","+myuser.getBatCoin()); //������ �ڽ��� �������ΰ� �������� ������ �����ش�.
		
		
	}
	
	public void batting(int battCoin) {			//��ư�� ���� ������ ����ϴ� �Լ�
		
		myuser.setMyCoin(myuser.getMyCoin()-battCoin);						//������ �ϰ� �ڽ��� ���ΰ� ���������� ������ �ؽ�Ʈ�� �ٲ��ش�.
		myuser.setBatCoin(myuser.getBatCoin()+battCoin);
		myCoin_lbl.setText(Integer.toString(myuser.getMyCoin()));
		myBatt_lbl.setText(Integer.toString(myuser.getBatCoin()));
		
		writer.println("[BATTING]"+myuser.getMyCoin()+","+myuser.getBatCoin()); //������ �ڽ��� �������ΰ� �������� ������ �����ش�.
		enabled(false);
		 
		
	}
	
	public void cardCompare(int round) {						//���� ������ �÷��̾��� ī�带 ���ϴ� �Լ�
		
		infoView.setText("["+(round+1)+"ROUND] �� �����մϴ�. ");	
		addLog("["+(round+1)+"ROUND] �� �����մϴ�. ");
		
		try{ Thread.sleep(1000); }catch(Exception e){}
		
		infoView.setText("["+(round+1)+"ROUND] ī�带 ���մϴ�. ");
		addLog("["+(round+1)+"ROUND] ī�带 ���մϴ�. ");
		try{ Thread.sleep(1000); }catch(Exception e){}
		
		myCard_lbl.setIcon(new ImageIcon(IndianClient.class.getResource("/img/"+myuser.getMyCardNum()[round]+".png")));
		
		
		
		
		if(myuser.getMyCardNum()[round] > yourcard) {					//���� ���۽� yourcard�� ����� �̹� ���� ī���� �� ������ ��.
			infoView.setText("["+(round+1)+"ROUND] ����� �¸��Դϴ�. ");	// myuser.getMyCardNum()[round] : �̹� ���� �ڽ��� ī���� ��
			addLog("["+(round+1)+"ROUND] ����� �¸��Դϴ�. ");
			ledLabel.setIcon(new ImageIcon(IndianClient.class.getResource("/img/win.jpg")));
			try{ Thread.sleep(1000); }catch(Exception e){}
			coinToss(1);												
		}
		else if(myuser.getMyCardNum()[round] < yourcard) {
			infoView.setText("["+(round+1)+"ROUND] ����� �й��Դϴ�. ");
			addLog("["+(round+1)+"ROUND] ����� �й��Դϴ�. ");
			ledLabel.setIcon(new ImageIcon(IndianClient.class.getResource("/img/lose.jpg")));
			try{ Thread.sleep(1000); }catch(Exception e){}
			coinToss(2);
		}
		else {
			infoView.setText("["+(round+1)+"ROUND] ���º��Դϴ�. ");
			addLog("["+(round+1)+"ROUND] ���º��Դϴ�. ");
			ledLabel.setIcon(new ImageIcon(IndianClient.class.getResource("/img/draw.jpg")));
			try{ Thread.sleep(1000); }catch(Exception e){}
			coinToss(3);
		}
		
	}
	
	public void coinToss(int status) {				//������ �����Ȳ�� ���� ���� �� �� �İ� ���� �Լ�
		
		String attack="";	// �� �İ� ���� ����			
		
		this.round++;	    // coinToss�� ī�� �� �� ���� ����ÿ� �����Ƿ� ���⼭ round�� ���� ��Ų��.
		
		if(status ==1) {	// �¸��� 
			myuser.setWin(myuser.getWin()+1);
			myuser.setMyCoin(myuser.getMyCoin()+myuser.getBatCoin()+Integer.parseInt(coinInfo[1])); //�������� + �ڽ��� ���� ���� + ����� ���� ����
			attack = "SECOND";	//�̰����Ƿ� ���� ���� �İ�
			roundnine_sw = 1;	//�������� ���� ������ ���� �߰�, ������ ����� ���п��� �񱳿� ���
		}
		else if(status == 2) {	// �й��
			myuser.setLose(myuser.getLose()+1);
			myuser.setMyCoin(myuser.getMyCoin());
			attack = "FIRST";	// �����Ƿ� ���� ���� ����
			roundnine_sw = 2;	
		}
		else if(status ==3 ) {	// ���ºν�
			Random r = new Random();
			
			myuser.setDraw(myuser.getDraw()+1);
			myuser.setMyCoin(myuser.getMyCoin()+myuser.getBatCoin()); // ���� ���� + �ڽ��� ���� ����
			try{ Thread.sleep(r.nextInt(100)+1); }catch(Exception e){}
			writer.println("[DRAW]");	//������ ������� �˸���. 
		}
		
		if(status==1 || status==2)			//�¸� Ȥ�� �й�ÿ� �ٽ� ���ӿ� �����Ѵ�. ���ºδ� ���� �����Ѵ�.
			startGame(attack, this.round);
	}
	
	public void reset() {			// ���� ����ø��� �ΰ����� �ؽ�Ʈ�� ���¸� �ʱ�ȭ�Ѵ�.
		
		myCard_lbl.setIcon(new ImageIcon(IndianClient.class.getResource("/img/0.png")));
	    yourCard_lbl.setIcon(new ImageIcon(IndianClient.class.getResource("/img/0.png")));
	    myCoin_lbl.setText(Integer.toString(myuser.getMyCoin()));	
	    writer.println("[ENDCOIN]"+myuser.getMyCoin());	//����� �� ���� ����
	    myBatt_lbl.setText("0");
	    yourBatt_lbl.setText("0");
	    result_lbl.setText(myuser.getWin()+"��/"+myuser.getDraw()+"��/"+myuser.getLose()+"��");
	    myuser.setBatCoin(0);
	    writer.println("[IS_RESET]");	//������ batting,draw �� false�� �ʱ�ȭ�Ѵ�.
	    
		
	}
	
	public void gameReset() {	//���� ����� �ΰ����� �ؽ�Ʈ �� ���¸� �ʱ�ȭ �Ѵ�.
		
		myCard_lbl.setIcon(new ImageIcon(IndianClient.class.getResource("/img/0.png")));
	    yourCard_lbl.setIcon(new ImageIcon(IndianClient.class.getResource("/img/0.png")));
	    myCoin_lbl.setText(Integer.toString(myuser.getMyCoin()));
	    writer.println("[ENDCOIN]"+myuser.getMyCoin());
	    myBatt_lbl.setText("0");
	    yourBatt_lbl.setText("0");
	    writer.println("[IS_RESET]");
	    enabled(false);		//������ ����Ǿ����Ƿ� �ΰ����� ��ư�� ��Ȱ��ȭ�Ѵ�.
	    this.round =1;		// ���带 �ʱ�ȭ�Ѵ�. 
		
	}
	
	public void startGame(String col, int round){     // ������ �����Ѵ�.
	    running=true;		//������ ���࿩�θ� �˸��� ����
	    
	    if(round==1) {		//1�����϶� �÷��̾� ���� �ʱ⼳���Ѵ�.
	    	
	    	myuser.setMyCardNum(deck_create(myuser.getMyCardNum()));	//�� ����
	    	myuser.setMyCoin(50);										// �� ���� ����
	    	myCoin_lbl.setText(Integer.toString(myuser.getMyCoin()));	
	    	yourCoin_lbl.setText("50");
	    	infoView.setText("��� �� ������ �����մϴ�.");
	    	result_lbl.setText("0��/0��/0��");
	    	myuser.setWin(0); myuser.setDraw(0); myuser.setLose(0);		//���� ��, ��, �� �ʱ�ȭ
	    	stopButton.setEnabled(true);								//��� ��ư Ȱ��ȭ
	    	endcoin=0;													//����� �� ���� ���� �ʱ�ȭ
	    	ledLabel.setIcon(new ImageIcon(IndianClient.class.getResource("/img/vs.jpg")));
	    	
	    }
	    //���� �ʱ�ȭ �Լ�		
	    reset();												
	    
	    //9���尡 ���� ���
	    if(round>9) {	
	    	running =false;
	    	endGame("������ ����Ǿ����ϴ�.");
	    	ledLabel.setIcon(new ImageIcon(IndianClient.class.getResource("/img/ledb.jpg")));
	    }
	    
	    //9���� ������ �ڽ��� ������ �� ������ ���
	    if(myuser.getMyCoin() <=0 && round <=9) {
	    	running = false;
	    	endGame("������ �� ���������ϴ�.");
	    	ledLabel.setIcon(new ImageIcon(IndianClient.class.getResource("/img/lose.jpg")));
	    }
	    
	    //9���� ������ ����� ������ �� ������ ���
	    if(myuser.getMyCoin() >= 100 && round <=9) {
	    	running = false;
	    	endGame("����� ������ �� ���������ϴ�.");
	    	ledLabel.setIcon(new ImageIcon(IndianClient.class.getResource("/img/win.jpg")));
    	}
	    
	    try{ Thread.sleep(1000); }catch(Exception e){}
	    
	    //if�� �� ����Ͽ� �������� ������ ���� ��� 
	    if(running == true) {
	    
	    	if(col.equals("FIRST")) {
            	infoView.setText("["+round+"ROUND] �����մϴ�.  �����Դϴ�..");
            	 ledLabel.setIcon(new ImageIcon(IndianClient.class.getResource("/img/vs.jpg")));
	    	}
          	else
        	  infoView.setText("["+round+"ROUND] �����մϴ�.  �İ��Դϴ�..");
	    		ledLabel.setIcon(new ImageIcon(IndianClient.class.getResource("/img/vs.jpg")));
        
        
	    	writer.println("[YOURCARD]"+myuser.getMyCardNum()[round-1]); //������ �̹� ���� ī�� ������ �����´�.
	    
	    
	    	if(col.equals("FIRST")){              		// ������ ���
	    		enabled(true);							// ���� ��ư Ȱ��ȭ
	    		
	    		
	    		if(myuser.getMyCoin() <2) {				//������ 1�� ���� �� �� ��ư ����
	    			batt10Button.setEnabled(false);
	    			batt5Button.setEnabled(false);
	    			batt1Button.setEnabled(false);
	    			callButton.setEnabled(true);
	    		}
	    		else if(myuser.getMyCoin() <6) {		//������ 5�� ���� �� �� ��ư ����
	    			batt10Button.setEnabled(false);
	    			batt5Button.setEnabled(false);
	    			callButton.setEnabled(false);
	    		}
	    		else if(myuser.getMyCoin() <11) {		//������ 10�� ���� �� �� ��ư ����
	    			batt10Button.setEnabled(false);
	    			callButton.setEnabled(false);
	    		}
	    		else {									// �� ���� ��Ȳ
	    			callButton.setEnabled(false);
	    		}
	    		
	    	}   
	    	else{                                // �İ��� ���
	    		enabled(false); 				 // ��ư ��Ȱ��ȭ 
	    	}
	    
	    	
	    	batting(); // �⺻���� (1�� ����)
	    
	    	//1. ��ư�� ������ ������ ������	(Action)   
	    	//2. �� ���� 1���� �����ϸ� �������� (Action + batting)
	    	//3. ���п���([CHECK])
	    	//round ������ ���� ���� -> coinToss ���� startgame ��ȣ�� 
	    }
	    
	    
	  }
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==sendBox){             // �޽��� �Է� �����̸�
		      String msg=sendBox.getText();
		      if(msg.length()==0)return;
		      //if(msg.length()>=30)msg=msg.substring(0,30);
		      try{  
		        writer.println("[MSG]"+msg);	//������ �޼��� ���� 
		        sendBox.setText("");
		      }catch(Exception ie){}
		    }
		 
		    else if(e.getSource()==enterButton){         // �����ϱ� ��ư�̸�
		      try{
		        
		        if(Integer.parseInt(roomBox.getText())<1){
		          infoView.setText("���ȣ�� �߸��Ǿ����ϴ�. 1�̻�");
		          return; 
		        }
		        if(myuser.getUsername().equals("")){  // ���� �̸��� �Է��� �ȵǾ��ٸ�
		  	      String name=nameBox.getText().trim();
		  	      if(name.length()<=2 || name.length()>10){
		  	        infoView.setText("�̸��� �߸��Ǿ����ϴ�. 3~10��");
		  	        nameBox.requestFocus();
		  	        return;
		  	      }
		  	      myuser.setUsername(name);				//�̸��� ��� ������ ���� 
			      writer.println("[NAME]"+myuser.getUsername());    
			      nameBox.setText(myuser.getUsername());
			      nameBox.setEditable(false);
		        }
		          writer.println("[ROOM]"+Integer.parseInt(roomBox.getText()));	//�� ��ȣ�� ������ ���� 
		          msgView.setText("");
		        }catch(Exception ie){
		          infoView.setText("�Է��Ͻ� ���׿� ������ �ҽ��ϴ�."); 
		        }
		    }
		 
		    else if(e.getSource()==exitButton){           // ���Ƿ� ��ư�̸�
		      try{
		        goToWaitRoom();
		        startButton.setEnabled(false);
		        stopButton.setEnabled(false);
		        meicon.setEnabled(false);
		        usericonu.setEnabled(false);
		      }catch(Exception ie){ infoView.setText("�Է��Ͻ� ���׿� ������ �ֽ��ϴ�.1"); }
		    }
		 
		    else if(e.getSource()==startButton){          //  ���� ��ư�̸�
		      try{
		        writer.println("[START]");
		        infoView.setText("����� ������ ��ٸ��ϴ�.");
		        startButton.setEnabled(false);
		      }catch(Exception ie){}
		    }
		
		    else if(e.getSource()==stopButton){          //  ��� ��ư�̸�
			      try{
			       writer.println("[DROPGAME]");	  
			       endGame("������ �����Ͽ����ϴ�."); 
			        
			      }catch(Exception ie){}
			}
		
		 
		    else if(e.getSource()==dieButton){          // ���� ��ư�̸�
		    	try{
			    	  
			    	if( (myuser.getMyCardNum()[round-1] == 10) && (myuser.getMyCoin() > 10) ) {	//�ڽ��� ī�尡 10�̸鼭, ������ 10�� �̻��� ���
			    		addLog("�ڽ��� ī�尡 10�϶� ���̸� �Ͽ� �ڵ����� 10���� ���õ˴ϴ�.");
			    		batting(10);
			    	}
			    	else if(( (myuser.getMyCardNum()[round-1] == 10) && (myuser.getMyCoin() < 10) )) { //�ڽ��� ī�尡 10�̸鼭 ������ 10�� �̸��� ���
			    		addLog("�ڽ��� ī�尡 10�ε� ���̸� �Ͽ� 10���� �г�Ƽ�� �ο��մϴ�. (10���� ���μ��� �����Ƿ� ���ε˴ϴ�.)");
			    		batting(myuser.getMyCoin());
			    		
			    	}
			    	
			        writer.println("[DIE]");
			      }catch(Exception ie){}
		    }
		
		    else if(e.getSource() == batt1Button) {			//1������ ��ư�̸�
		    	try{
			        batting(1);
			        addLog("["+round+"ROUND] 1���� ����!! ");
			        writer.println("[CHECK]");
			      }catch(Exception ie){}
		    }
		
		    else if(e.getSource() == batt5Button) {			//5������ ��ư�̸�
		    	try{
			        batting(5);
			        addLog("["+round+"ROUND] 5���� ����!! ");
			        writer.println("[CHECK]");
			      }catch(Exception ie){}
		    }
		
		    else if(e.getSource() == batt10Button) {		//10������ ��ư�̸�
		    	try{
			        batting(10);
			        addLog("["+round+"ROUND] 10���� ����!! ");
			        writer.println("[CHECK]");
			      }catch(Exception ie){}
		    }
		
		    else if(e.getSource()==callButton) {			//call ��ư�̸�
		    	
		    	int battCoin = Integer.parseInt(coinInfo[1]); // �̹����� ����� ���� ���� ( ���� �� ���� �޾ƿ´�.)
		    	
		    	addLog("["+(round)+"ROUND] ���Ͽ����ϴ�. ");
		    	
		    	if(battCoin >= myuser.getMyCoin() && battCoin >11) 		//����� ������ ���Ҷ� ���� ������ ���� ���� ���κ��� ���ٸ�..
		    		batting( myuser.getMyCoin() );
		    	else if(battCoin < myuser.getMyCoin() && battCoin >11)  //����� ������ ���Ҷ� ���� ������ ���� ���� ���κ��� ���ٸ�..
		    		batting(battCoin);
		    	else 
		    		batting( (battCoin-myuser.getBatCoin()) );			//�̿��� ��� 
		    	
		    	writer.println("[CHECK]");
		    	
		    }
			
		    else if(e.getSource()==btnAllin) {				//���� ��ư �̸�
		    	
		    	addLog("["+(round)+"ROUND] �����Ͽ����ϴ�. ");
		    	
		    	if(myuser.getMyCoin() >= Integer.parseInt(coinInfo[0]))	//���� ������ ����� ���κ��� ���ٸ�...
		    		batting(Integer.parseInt(coinInfo[0]));
		    	else 													//���� ������ ����� ���κ��� ���ٸ�...
		    		batting(myuser.getMyCoin());
	        	
	        	
		    	writer.println("[CHECK]");								//���� ���� üũ
		    }
		
		
		
		
	}
	
	void goToWaitRoom(){                   // ���Ƿ� ��ư�� ������ ȣ��ȴ�.
	    if(myuser.getUsername().equals("")){
	      String name=nameBox.getText().trim();
	      if(name.length()<=2 || name.length()>10){
	        infoView.setText("�̸��� �߸��Ǿ����ϴ�. 3~10��");
	        nameBox.requestFocus();
	        return;
	      }
	      myuser.setUsername(name);
	      writer.println("[NAME]"+myuser.getUsername());    
	      nameBox.setText(myuser.getUsername());
	      nameBox.setEditable(false);
	    }  
	    addLog("");
	    writer.println("[ROOM]0");			//������ ���ȣ��  0���̹Ƿ�.
	    infoView.setText("���ǿ� �����ϼ̽��ϴ�.");
	    myId_lbl.setText("     ");
	    yourId_lbl.setText("     ");
	    roomBox.setText("0");
	    enterButton.setEnabled(true);
	    exitButton.setEnabled(false);
	  }

	@Override
	public void run() {
		String msg;                             // �����κ����� �޽���
	    try{
	    while((msg=reader.readLine())!=null){
	 
	 
	        if(msg.startsWith("[ROOM]")){    // �濡 ����
	          if(!msg.equals("[ROOM]0")){          // ������ �ƴ� ���̸�
	            enterButton.setEnabled(false);
	            exitButton.setEnabled(true);
	            infoView.setText(msg.substring(6)+"�� �濡 �����ϼ̽��ϴ�.");
	            myId_lbl.setText(myuser.getUsername());
	            meicon.setEnabled(true);
	            
	          }
	          else infoView.setText("���ǿ� �����ϼ̽��ϴ�.");
	 
	          roomNumber=Integer.parseInt(msg.substring(6));     // �� ��ȣ ����
	 
	        }
	 
	        else if(msg.startsWith("[FULL]")){       // ���� �� �����̸�
	          infoView.setText("���� ���� ������ �� �����ϴ�.");
	        }
	 
	        else if(msg.startsWith("[PLAYERS]")){      // �濡 �ִ� ����� ���
	          nameList(msg.substring(9));
	          String[] yourId = msg.substring(9).split("\t");
	          if(roomNumber > 0 && yourId.length >1) {
	        	  yourId_lbl.setText(yourId[0]);
	        	  usericonu.setEnabled(true);
	          }
	        }
	 
	        else if(msg.startsWith("[ENTER]")){        // �մ� ����
	          pList.add(msg.substring(7));
	          playersInfo();
	          addLog("["+msg.substring(6)+"]���� �ٸ� ������ �����Ͽ����ϴ�.");
	          usericonu.setEnabled(true);
	          if(roomNumber != 0) {
	        	  yourId_lbl.setText(msg.substring(7));
	          }
	        }
	        
	        else if(msg.startsWith("[EXIT]")){          // �մ� ����
	          pList.remove(msg.substring(6));            // ����Ʈ���� ����
	          playersInfo();                        // �ο����� �ٽ� ����Ͽ� �����ش�.
	          addLog("["+msg.substring(6)+"]���� �ٸ� ������ �����Ͽ����ϴ�.");
	          if(roomNumber!=0) {
	            endGame("��밡 �������ϴ�.");		//�������� �ƴҶ� ���� ��� Null ������ �� 
	          	usericonu.setEnabled(false);
	          }
	          yourId_lbl.setText("     ");
	            
	        }
	        
	        else if(msg.startsWith("[DISCONNECT]")){     // �մ� ���� ����
	          pList.remove(msg.substring(12));
	          playersInfo();
	          addLog("["+msg.substring(12)+"]���� ������ �������ϴ�.");
	          usericonu.setEnabled(false);
	          if(roomNumber!=0)
	            endGame("��밡 �������ϴ�.");		//�������� �ƴҶ� ���� ��� Null ������ �� 
	            yourId_lbl.setText("     ");
	        }
	 
	        // �� �ļ��� �ο� 
	        else if(msg.startsWith("[ATTACK]")){          // �� �İ��� �ο� �޴´�. [START] , [DRAW] �� ��쿡 ȣ�� �ȴ�.
	          String attack=msg.substring(8);
	          this.startGame(attack,round);                      // ������ �����Ѵ�.
	        }
	        
	        else if(msg.startsWith("[ENDCOIN]")){  			//�ڽ��� ���� ���� ������ ��뿡�� ������.
		         String coin = msg.substring(9);
		         endcoin = Integer.parseInt(coin);
		         yourCoin_lbl.setText(Integer.toString(endcoin));

		    }
	        
	        else if(msg.startsWith("[YOURCARD]")) {			//�ڽ��� ī�� ������ ��뿡�� ������.
	        	
	        	yourcard = Integer.parseInt(msg.substring(10));
	        	yourCard_lbl.setIcon(new ImageIcon(IndianClient.class.getResource("/img/"+yourcard+".png")));
	        	
	        }
	        
	        
	        
	        else if(msg.startsWith("[bsBATTING]")) {			//�ʱ����
	        	coinInfo = msg.substring(11).split(",");
	        	int totCoin = Integer.parseInt(coinInfo[0]);  // ����� ��ü����
	        	int battCoin = Integer.parseInt(coinInfo[1]); // ����� ��������
	        	
	        	yourCoin_lbl.setText(Integer.toString(totCoin));
	        	yourBatt_lbl.setText(Integer.toString(battCoin));
	        	callButton.setText("�� : "+Integer.toString(battCoin));
	        }
	        
	        else if(msg.startsWith("[MYDIE]")) {				//�ڱⰡ ���̹�ư�� ���� ���
		        infoView.setText("["+round+"ROUND] �����ϼ̽��ϴ�.");
		        addLog("["+round+"ROUND] �����ϼ̽��ϴ�.");
		        ledLabel.setIcon(new ImageIcon(IndianClient.class.getResource("/img/lose.jpg")));
		        enabled(false);
		        coinToss(2);  //�й�      
	    	}
	        else if(msg.startsWith("[DIE]")) {					//��밡 ���̹�ư�� ���� ��� 
	        	infoView.setText("["+round+"ROUND] ������ �����Ͽ����ϴ�. �¸��Դϴ�.");
	        	addLog("["+round+"ROUND] ������ �����Ͽ����ϴ�. �¸��Դϴ�.");
	        	ledLabel.setIcon(new ImageIcon(IndianClient.class.getResource("/img/win.jpg")));
	        	if(yourcard==10) {
	        		addLog("��밡 ī���� ���� 10�϶� ���̸� �ؼ� 10���� �߰� ������ ����ϴ�.");
	        	}
		        coinToss(1);	// �¸�
		        enabled(false);
		        
	        }
	        
	        
	        else if(msg.startsWith("[BATTING]")) {				//��밡 ������ �� ���  
	        	coinInfo = msg.substring(9).split(",");
	        	int totCoin = Integer.parseInt(coinInfo[0]);  // ����� ��ü����
	        	int battCoin = Integer.parseInt(coinInfo[1]); // ����� ��������
	        	
	        	yourCoin_lbl.setText(Integer.toString(totCoin));
	        	yourBatt_lbl.setText(Integer.toString(battCoin));
	        	callButton.setText("�� : "+Integer.toString(battCoin));
	        	//enabled(true);
	        	addLog("["+round+"ROUND] ����� �����Դϴ�. �� Ȥ�� ���̸� �������ּ���.");
	        	callButton.setEnabled(true);
	        	dieButton.setEnabled(true);
	        }
	        
	        else if(msg.startsWith("[CHECK]")) {	// �� �� ��� ������ �ߴٸ�, ī�带 ���Ѵ�.
	        	enabled(false);
	        	cardCompare(round-1);
	        	
	        }
	        
	        else if(msg.startsWith("[GAMEWIN]")) {	//������ ���� ����� �¸���
	        	rankUpdate(myuser.getUsername(),myuser.getWin(),myuser.getDraw(),myuser.getLose(),myuser.getMyCoin());
	        	infoView.setText("[���� ���]  ������ ����� �¸��� �������ϴ�.");
		    	addLog("[���� ���]  ������ ����� �¸��� �������ϴ�.");
		    	ledLabel.setIcon(new ImageIcon(IndianClient.class.getResource("/img/win.jpg")));
	        	
	        }
	        
	        else if(msg.startsWith("[GAMELOSE]")) { // ������ ���� ����� �й��
	        	infoView.setText("[���� ���]  ������ ����� �й�� �������ϴ�.");
		    	addLog("[���� ���]  ������ ����� �й�� �������ϴ�.");
		    	ledLabel.setIcon(new ImageIcon(IndianClient.class.getResource("/img/lose.jpg")));
	        	
	        }
	        
	        
	        else if(msg.startsWith("[DROPGAME]")) {      // ��밡 ����ϸ�
	          endGame("��밡 ����Ͽ����ϴ�.");
	        ledLabel.setIcon(new ImageIcon(IndianClient.class.getResource("/img/win.jpg")));
	        	}
	        // ��ӵ� �޽����� �ƴϸ� �޽��� ������ �����ش�.
	        else addLog(msg);
	    }
	    }catch(IOException ie){
	    	addLog(ie+"\n");
	    }
	    addLog("������ ������ϴ�.");
	  }
	 
	  public void rankUpdate(String pname, int pwin, int pdraw, int plose,int pcoin) { //��ŷ ������Ʈ
			
			  try {
				    Connection con = null;
					con = new IndianDBConn().getConnection();
					
					String sql = "insert into IndianPoker values(?,?,?,?,?,?)"; 
					PreparedStatement st = con.prepareStatement(sql);
					st.setString(1,pname);
					st.setInt(2,pwin);
					st.setInt(3,pdraw);
					st.setInt(4,plose);
					st.setInt(5,pcoin);
					st.setInt(6,(pwin*3*pcoin)+(pdraw*1*pcoin));
					st.executeUpdate();
					con.close();	
				} catch(ClassNotFoundException e) {
					JOptionPane.showMessageDialog(null, "Ŭ���� error");
				} catch(SQLException e){
					JOptionPane.showMessageDialog(null, "error");
				}
		  }

	private void endGame(String msg){                // ������ ����� ȣ�� �Ǵ� �޼ҵ� 
	   
		int ninecoin = Integer.parseInt(coinInfo[0]); //���� �κ� ...
		  
		infoView.setText(msg);
	    addLog(msg);
	    startButton.setEnabled(false);
	    stopButton.setEnabled(false);
	    writer.println("[STOPGAME]");	//������ ready�� false �� �ٲ� 
	    gameReset(); // �������� �ʱ�ȭ
	    
	    if((msg.indexOf("���")>=0) ) {	//��밡 ����� �� ��� 
	    	myuser.setMyCoin(myuser.getMyCoin()+myuser.getBatCoin()+Integer.parseInt(coinInfo[1]));
	    	myCoin_lbl.setText(Integer.toString(myuser.getMyCoin()));
	    	writer.println("[GAMEWIN]");
	    }
	    
	    else if((msg.indexOf("����")==0)) {	//�ڽ��� ������ �� ������ ��� 
	    	myuser.setMyCoin(0);
	    	myCoin_lbl.setText(Integer.toString(myuser.getMyCoin()));
	    	writer.println("[GAMELOSE]");
	    }
	    
	    
	    
	    else if((msg.indexOf("����")>=0)) {	// �ڽ��� ����� �� ��� 
	    	int dropCoin = myuser.getBatCoin()+Integer.parseInt(coinInfo[1])+Integer.parseInt(coinInfo[0]);
	    	//addLog(Integer.toString(dropCoin));
	    	yourCoin_lbl.setText(Integer.toString(dropCoin));
	    }
	    else {
	    	// ���� ������ ����� ������ endcoin�� ������� ���ؼ� ������ ������ ���� �񱳰� �������ϴ�.
	    	
	    	if( (roundnine_sw == 2) && (myuser.getMyCoin() >= Integer.parseInt(coinInfo[0])) ) //������ ���忡 ��밡 ������ �� ����, �ڽ��� �� ���
	    		ninecoin = ninecoin + (Integer.parseInt(coinInfo[1])*2); 
	    	
	    	if(myuser.getMyCoin() > ninecoin) {		//�ڽ��� ������ ��뺸�� ���ٸ�..
	    		writer.println("[GAMEWIN]");
	    	}
	    	else if(myuser.getMyCoin() == ninecoin) {		//�ڽ��� ���ΰ� ����� ������ ���ٸ�..
	    	
	    		infoView.setText("[���� ���]  ������ ���ºη� �������ϴ�.");
	    		addLog("[���� ���]  ������ ���ºη� �������ϴ�.");
	    	}
	    }
	    
	    
	    try{ Thread.sleep(2000); }catch(Exception e){}    // 2�ʰ� ���
	 
	    //if(board.isRunning())board.stopGame();
	    if(pList.getItemCount()==2)startButton.setEnabled(true);	// ���� ���� �� ���� ��ư Ȱ��ȭ
	  }
	 
	  private void playersInfo(){                 // �濡 �ִ� �������� ���� �����ش�.
	    int count=pList.getItemCount();
	    if(roomNumber==0)
	      pInfo.setText("����: "+count+"��");
	    else pInfo.setText(roomNumber+" �� ��: "+count+"��"); 
	 
	    // ���ӽ��� ��ư�� Ȱ��ȭ ���¸� �����Ѵ�.
	    if(count==2 && roomNumber!=0)
	      startButton.setEnabled(true);
	    else startButton.setEnabled(false); 
	  }
	 
	  // ����� ����Ʈ���� ����ڵ��� �����Ͽ� pList�� �߰��Ѵ�.
	  private void nameList(String msg){
	    pList.removeAll();
	    StringTokenizer st=new StringTokenizer(msg, "\t");
	    while(st.hasMoreElements())
	      pList.add(st.nextToken());
	    playersInfo();
	  }
	 
	  private void connect(){                    // ����
	    try{
	      msgView.append("������ ������ ��û�մϴ�.\n");
	      socket=new Socket("127.0.0.1", 7222);
	      msgView.append("---���� ����--.\n");
	      msgView.append("�̸��� �Է��ϰ� ���Ƿ� �����ϼ���.\n");
	      reader=new BufferedReader(
	                           new InputStreamReader(socket.getInputStream()));
	      writer=new PrintWriter(socket.getOutputStream(), true);
	      new Thread(this).start();
	      //board.setWriter(writer);
	    }catch(Exception e){
	      msgView.append(e+"\n\n���� ����..\n");  
	    }
	  }
}
