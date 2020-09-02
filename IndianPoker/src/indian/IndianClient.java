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

	private JPanel contentPane;			//메인패널
	private JTextField nameBox;			//이름 입력 필드
	private JTextField roomBox;			//방 번호 입력 필드
	private JTextField sendBox;			//채팅 입력 필드
	private JTextArea msgView;			//채팅 view 에이리어
	private JLabel infoView;			//게임에 대한 정보를 보여주는 라벨
	private JLabel myId_lbl;			//(인게임) 자신의 이름 라벨
	private JLabel yourId_lbl;			//(인게임) 상대의 이름 라벨
	private JLabel myCoin_lbl;			//(인게임) 자신의 총 코인 라벨
	private JLabel yourCoin_lbl;		//(인게임) 상대의 총 코인 라벨
	private JLabel result_lbl;			//(인게임) 승/무/패 라벨
	private JButton startButton;		// 시작 버튼
	private JButton dieButton;			// (인게임) 다이 버튼
	private JButton batt5Button;		// (인게임) 5배팅 버튼
	private JButton callButton;			// (인게임) 콜 버튼
	private JButton ruleButton;			// 게임룰 버튼
	private JButton rankButton;			// 랭킹 버튼
	private JLabel myCard_lbl;			// (인게임) 자신의 라운드에 따른 카드 라벨
	private JLabel yourCard_lbl;		// (인게임) 상대의 라운드에 따른 카드 라벨
	private List pList;					// 대기방 & 게임방의 접속자 목록
	private JLabel name_lbl;			// nameBox를 알려주는 라벨
	private JLabel roomNum_lbl;			// roomBox를 알려주는 라벨
	private JButton batt10Button;		// (인게임) 10배팅 버튼
	private JButton batt1Button;		// (인게임) 1배팅 버튼
	private JButton enterButton;		// 입장하기 버튼
	private JButton exitButton;			// 대기실로 버튼
	private JLabel pInfo;				// 대기방 & 게임방의 접속자 수를 알려주는 라벨
	private JLabel myBatt_lbl;			// (인게임) 자신의 배팅 코인
	private JLabel yourBatt_lbl;		// (인게임) 상대의 배팅 코인
	
	private BufferedReader reader;      // 입력 스트림
	private PrintWriter writer;         // 출력 스트림
	private Socket socket;              // 소켓
	private int roomNumber=-1;          // 방 번호
	private int round = 1;				// 라운드
	private int yourcard=0;				// 라운드 별 상대 카드의 수
	private int endcoin=0;				// 라운드 및 게임이 끝날때 상대의 코인 수
	private int roundnine_sw =0;		// //쓰레드의 문제 때문에 강제 추가, 게임의 종료시 승패여부 비교에 사용하는 변수
	private String[] coinInfo;			// 상대의 배팅 정보를 저장하는 변수   coinInfo[0] : 상대의 현재코인
										//						   coinInfo[1] : 상대의 배팅코인
	private User myuser = new User();	// 플레이어 정보 ( User.java 참고)
	ImageIcon icon;						
	
	private boolean enable=false;
	 
	private boolean running=false;       // 게임이 진행 중인가를 나타내는 변수
	private JLabel meicon;			
	private JLabel usericonu;
	private JLabel mCoinC;
	private JLabel coin_01;
	private JLabel yCoinC;
	private JLabel coin_02;
	private JButton btnAllin;			// (인게임) 올인 버튼
	private JButton stopButton;			// (인게임) 기권 버튼 
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
	                setOpaque(false); //그림을 표시하게 설정,투명하게 조절
	                
	                super.paintComponent(g);
			 }
		};
			
		setResizable(false);
		
		
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		myId_lbl = new JLabel("");
		myId_lbl.setForeground(Color.BLACK);
		myId_lbl.setFont(new Font("나눔손글씨 펜", Font.BOLD, 20));
		myId_lbl.setBounds(195, 111, 80, 40);
		contentPane.add(myId_lbl);
		
		yourId_lbl = new JLabel("");
		yourId_lbl.setForeground(Color.BLACK);
		yourId_lbl.setFont(new Font("나눔손글씨 펜", Font.BOLD, 20));
		yourId_lbl.setBounds(470, 111, 80, 40);
		contentPane.add(yourId_lbl);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(40, 430, 580, 130);
		contentPane.add(scrollPane);
		
		msgView = new JTextArea();
		scrollPane.setViewportView(msgView);
		
		myCoin_lbl = new JLabel("");
		myCoin_lbl.setFont(new Font("나눔손글씨 붓", Font.BOLD, 13));
		myCoin_lbl.setBounds(162, 385, 40, 40);
		contentPane.add(myCoin_lbl);
		
		yourCoin_lbl = new JLabel("");
		yourCoin_lbl.setForeground(Color.RED);
		yourCoin_lbl.setFont(new Font("나눔손글씨 붓", Font.BOLD, 13));
		yourCoin_lbl.setBounds(479, 385, 40, 40);
		contentPane.add(yourCoin_lbl);
		
		result_lbl = new JLabel("");
		result_lbl.setFont(new Font("나눔손글씨 펜", Font.BOLD, 13));
		result_lbl.setHorizontalAlignment(SwingConstants.CENTER);
		result_lbl.setBounds(165, 150, 80, 30);
		contentPane.add(result_lbl);
		
		startButton = new JButton("\uC2DC\uC791");
		startButton.setFont(new Font("나눔손글씨 붓", Font.PLAIN, 13));
		startButton.setBounds(650, 252, 100, 40);
		contentPane.add(startButton);
		startButton.setEnabled(false);
		
		dieButton = new JButton("\uB2E4\uC774");
		dieButton.setFont(new Font("나눔손글씨 붓", Font.PLAIN, 13));
		dieButton.setBounds(773, 430, 100, 40);
		contentPane.add(dieButton);
		dieButton.setEnabled(false);
		
		batt5Button = new JButton("");
		batt5Button.setIcon(new ImageIcon(IndianClient.class.getResource("/img/fchip.png")));
		batt5Button.setBounds(650, 430, 100, 40);
		contentPane.add(batt5Button);
		batt5Button.setEnabled(false);
		
		callButton = new JButton("\uCF5C");
		callButton.setFont(new Font("나눔손글씨 붓", Font.PLAIN, 13));
		callButton.setBounds(773, 380, 100, 40);
		contentPane.add(callButton);
		callButton.setEnabled(false);
		
		ruleButton = new JButton("\uAC8C\uC784\uB8F0");
		ruleButton.setFont(new Font("나눔손글씨 붓", Font.PLAIN, 13));
		ruleButton.addActionListener(new ActionListener() {		//게임룰 버튼을 누르면 Rule 자바의 정보를 Frame창에 띄워줌
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
		infoView.setFont(new Font("나눔손글씨 펜", Font.PLAIN, 13));
		infoView.setHorizontalAlignment(SwingConstants.CENTER);
		infoView.setBounds(65, 20, 485, 40);
		contentPane.add(infoView);
		
		myBatt_lbl = new JLabel("");
		myBatt_lbl.setFont(new Font("나눔손글씨 붓", Font.BOLD, 13));
		myBatt_lbl.setHorizontalAlignment(SwingConstants.CENTER);
		myBatt_lbl.setBounds(250, 227, 65, 50);
		myBatt_lbl.setForeground(Color.YELLOW);
		myBatt_lbl.setBackground(Color.BLACK);
		myBatt_lbl.setOpaque(true); 
		contentPane.add(myBatt_lbl);
		
		yourBatt_lbl = new JLabel("");
		yourBatt_lbl.setFont(new Font("나눔손글씨 붓", Font.BOLD, 13));
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
		mCoinC.setFont(new Font("나눔손글씨 붓", Font.BOLD, 13));
		mCoinC.setBounds(90, 385, 60, 40);
		contentPane.add(mCoinC);
		
		coin_01 = new JLabel("");
		coin_01.setIcon(new ImageIcon(IndianClient.class.getResource("/img/coins.png")));
		coin_01.setBounds(209, 390, 40, 30);
		contentPane.add(coin_01);
		
		yCoinC = new JLabel("\uC0C1\uB300 \uCF54\uC778");
		yCoinC.setForeground(Color.RED);
		yCoinC.setFont(new Font("나눔손글씨 붓", Font.BOLD, 13));
		yCoinC.setBounds(398, 385, 80, 40);
		contentPane.add(yCoinC);
		
		coin_02 = new JLabel("");
		coin_02.setIcon(new ImageIcon(IndianClient.class.getResource("/img/coins.png")));
		coin_02.setBounds(520, 390, 40, 30);
		contentPane.add(coin_02);
		
		rankButton = new JButton("Rank");
		rankButton.setFont(new Font("나눔손글씨 붓", Font.PLAIN, 13));
		rankButton.setEnabled(true);
		rankButton.setBounds(773, 302, 100, 40);
		contentPane.add(rankButton);
		rankButton.addActionListener(new ActionListener() {		//랭킹 버튼을 누르면 IndianDAO 의 랭킹 정보를 Frame창에 띄워줌
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
		btnAllin.setFont(new Font("나눔손글씨 붓", Font.PLAIN, 13));
		btnAllin.setEnabled(false);
		btnAllin.setBounds(773, 480, 100, 40);
		contentPane.add(btnAllin);
		
		stopButton = new JButton("\uAE30\uAD8C");
		stopButton.setFont(new Font("나눔손글씨 붓", Font.PLAIN, 13));
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
		betLabel.setFont(new Font("나눔손글씨 펜", Font.BOLD, 10));
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

	public boolean isRunning(){           // 게임의 진행 상태를 반환한다.
	    return running; 
	}
	
	public void enabled(boolean b) {		// 버튼 활성화 함수 자신의 턴이면 true , 상대턴 false 
		
		dieButton.setEnabled(b);
		batt1Button.setEnabled(b);
		batt5Button.setEnabled(b);
		batt10Button.setEnabled(b);
		callButton.setEnabled(b);
		btnAllin.setEnabled(b);
		
	}
	
	public int[] deck_create(int[] deck) {			// 1~10까지 랜덤의 카드넘버를 설정하는 함수 
		
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
	
	void addLog(String log)						//채팅View에 메세지를 추가하고, 포인터를 가장 마지막으로 둬서 스크롤바를 내려주는 함수
	{
	  msgView.append(log + "\n");  
	  msgView.setCaretPosition(msgView.getDocument().getLength()); 
	}
	
	public void batting() {						//초기배팅(1)개를 담당하는 함수
		
		int bsBatt = 1; // 기본배팅
		
		myuser.setMyCoin(myuser.getMyCoin()-bsBatt);						//초기배팅을 하고 자신의 코인과 배팅코인의 정보와 텍스트를 바꿔준다.
		myuser.setBatCoin(bsBatt);
		myCoin_lbl.setText(Integer.toString(myuser.getMyCoin()));
		myBatt_lbl.setText(Integer.toString(myuser.getBatCoin()));
		
		writer.println("[bsBATTING]"+myuser.getMyCoin()+","+myuser.getBatCoin()); //서버에 자신의 현재코인과 배팅코인 정보를 보내준다.
		
		
	}
	
	public void batting(int battCoin) {			//버튼을 통한 배팅을 담당하는 함수
		
		myuser.setMyCoin(myuser.getMyCoin()-battCoin);						//배팅을 하고 자신의 코인과 배팅코인의 정보와 텍스트를 바꿔준다.
		myuser.setBatCoin(myuser.getBatCoin()+battCoin);
		myCoin_lbl.setText(Integer.toString(myuser.getMyCoin()));
		myBatt_lbl.setText(Integer.toString(myuser.getBatCoin()));
		
		writer.println("[BATTING]"+myuser.getMyCoin()+","+myuser.getBatCoin()); //서버에 자신의 현재코인과 배팅코인 정보를 보내준다.
		enabled(false);
		 
		
	}
	
	public void cardCompare(int round) {						//배팅 종료후 플레이어의 카드를 비교하는 함수
		
		infoView.setText("["+(round+1)+"ROUND] 를 종료합니다. ");	
		addLog("["+(round+1)+"ROUND] 를 종료합니다. ");
		
		try{ Thread.sleep(1000); }catch(Exception e){}
		
		infoView.setText("["+(round+1)+"ROUND] 카드를 비교합니다. ");
		addLog("["+(round+1)+"ROUND] 카드를 비교합니다. ");
		try{ Thread.sleep(1000); }catch(Exception e){}
		
		myCard_lbl.setIcon(new ImageIcon(IndianClient.class.getResource("/img/"+myuser.getMyCardNum()[round]+".png")));
		
		
		
		
		if(myuser.getMyCardNum()[round] > yourcard) {					//라운드 시작시 yourcard에 상대의 이번 라운드 카드의 수 정보가 들어감.
			infoView.setText("["+(round+1)+"ROUND] 당신의 승리입니다. ");	// myuser.getMyCardNum()[round] : 이번 라운드 자신의 카드의 수
			addLog("["+(round+1)+"ROUND] 당신의 승리입니다. ");
			ledLabel.setIcon(new ImageIcon(IndianClient.class.getResource("/img/win.jpg")));
			try{ Thread.sleep(1000); }catch(Exception e){}
			coinToss(1);												
		}
		else if(myuser.getMyCardNum()[round] < yourcard) {
			infoView.setText("["+(round+1)+"ROUND] 당신의 패배입니다. ");
			addLog("["+(round+1)+"ROUND] 당신의 패배입니다. ");
			ledLabel.setIcon(new ImageIcon(IndianClient.class.getResource("/img/lose.jpg")));
			try{ Thread.sleep(1000); }catch(Exception e){}
			coinToss(2);
		}
		else {
			infoView.setText("["+(round+1)+"ROUND] 무승부입니다. ");
			addLog("["+(round+1)+"ROUND] 무승부입니다. ");
			ledLabel.setIcon(new ImageIcon(IndianClient.class.getResource("/img/draw.jpg")));
			try{ Thread.sleep(1000); }catch(Exception e){}
			coinToss(3);
		}
		
	}
	
	public void coinToss(int status) {				//게임의 진행상황에 따른 코인 및 선 후공 설정 함수
		
		String attack="";	// 선 후공 제어 변수			
		
		this.round++;	    // coinToss는 카드 비교 후 라운드 종료시에 들어오므로 여기서 round를 증가 시킨다.
		
		if(status ==1) {	// 승리시 
			myuser.setWin(myuser.getWin()+1);
			myuser.setMyCoin(myuser.getMyCoin()+myuser.getBatCoin()+Integer.parseInt(coinInfo[1])); //현재코인 + 자신의 배팅 코인 + 상대의 배팅 코인
			attack = "SECOND";	//이겼으므로 다음 라운드 후공
			roundnine_sw = 1;	//쓰레드의 문제 때문에 강제 추가, 게임의 종료시 승패여부 비교에 사용
		}
		else if(status == 2) {	// 패배시
			myuser.setLose(myuser.getLose()+1);
			myuser.setMyCoin(myuser.getMyCoin());
			attack = "FIRST";	// 졌으므로 다음 라운드 선공
			roundnine_sw = 2;	
		}
		else if(status ==3 ) {	// 무승부시
			Random r = new Random();
			
			myuser.setDraw(myuser.getDraw()+1);
			myuser.setMyCoin(myuser.getMyCoin()+myuser.getBatCoin()); // 현재 코인 + 자신의 배팅 코인
			try{ Thread.sleep(r.nextInt(100)+1); }catch(Exception e){}
			writer.println("[DRAW]");	//서버에 비겼음을 알린다. 
		}
		
		if(status==1 || status==2)			//승리 혹은 패배시에 다시 게임에 돌입한다. 무승부는 따로 제어한다.
			startGame(attack, this.round);
	}
	
	public void reset() {			// 라운드 종료시마다 인게임의 텍스트및 상태를 초기화한다.
		
		myCard_lbl.setIcon(new ImageIcon(IndianClient.class.getResource("/img/0.png")));
	    yourCard_lbl.setIcon(new ImageIcon(IndianClient.class.getResource("/img/0.png")));
	    myCoin_lbl.setText(Integer.toString(myuser.getMyCoin()));	
	    writer.println("[ENDCOIN]"+myuser.getMyCoin());	//상대의 총 코인 갯수
	    myBatt_lbl.setText("0");
	    yourBatt_lbl.setText("0");
	    result_lbl.setText(myuser.getWin()+"승/"+myuser.getDraw()+"무/"+myuser.getLose()+"패");
	    myuser.setBatCoin(0);
	    writer.println("[IS_RESET]");	//서버의 batting,draw 를 false로 초기화한다.
	    
		
	}
	
	public void gameReset() {	//게임 종료시 인게임의 텍스트 및 상태를 초기화 한다.
		
		myCard_lbl.setIcon(new ImageIcon(IndianClient.class.getResource("/img/0.png")));
	    yourCard_lbl.setIcon(new ImageIcon(IndianClient.class.getResource("/img/0.png")));
	    myCoin_lbl.setText(Integer.toString(myuser.getMyCoin()));
	    writer.println("[ENDCOIN]"+myuser.getMyCoin());
	    myBatt_lbl.setText("0");
	    yourBatt_lbl.setText("0");
	    writer.println("[IS_RESET]");
	    enabled(false);		//게임이 종료되었으므로 인게임의 버튼을 비활성화한다.
	    this.round =1;		// 라운드를 초기화한다. 
		
	}
	
	public void startGame(String col, int round){     // 게임을 시작한다.
	    running=true;		//게임의 진행여부를 알리는 변수
	    
	    if(round==1) {		//1라운드일때 플레이어 값을 초기설정한다.
	    	
	    	myuser.setMyCardNum(deck_create(myuser.getMyCardNum()));	//덱 생성
	    	myuser.setMyCoin(50);										// 총 코인 생성
	    	myCoin_lbl.setText(Integer.toString(myuser.getMyCoin()));	
	    	yourCoin_lbl.setText("50");
	    	infoView.setText("잠시 후 게임을 시작합니다.");
	    	result_lbl.setText("0승/0무/0패");
	    	myuser.setWin(0); myuser.setDraw(0); myuser.setLose(0);		//라운드 승, 무, 패 초기화
	    	stopButton.setEnabled(true);								//기권 버튼 활성화
	    	endcoin=0;													//상대의 총 코인 정보 초기화
	    	ledLabel.setIcon(new ImageIcon(IndianClient.class.getResource("/img/vs.jpg")));
	    	
	    }
	    //라운드 초기화 함수		
	    reset();												
	    
	    //9라운드가 끝날 경우
	    if(round>9) {	
	    	running =false;
	    	endGame("게임이 종료되었습니다.");
	    	ledLabel.setIcon(new ImageIcon(IndianClient.class.getResource("/img/ledb.jpg")));
	    }
	    
	    //9라운드 이전에 자신의 코인이 다 떨어진 경우
	    if(myuser.getMyCoin() <=0 && round <=9) {
	    	running = false;
	    	endGame("코인이 다 떨어졌습니다.");
	    	ledLabel.setIcon(new ImageIcon(IndianClient.class.getResource("/img/lose.jpg")));
	    }
	    
	    //9라운드 이전에 상대의 코인이 다 떨어진 경우
	    if(myuser.getMyCoin() >= 100 && round <=9) {
	    	running = false;
	    	endGame("상대의 코인이 다 떨어졌습니다.");
	    	ledLabel.setIcon(new ImageIcon(IndianClient.class.getResource("/img/win.jpg")));
    	}
	    
	    try{ Thread.sleep(1000); }catch(Exception e){}
	    
	    //if를 다 통과하여 정상적인 게임의 진행 경우 
	    if(running == true) {
	    
	    	if(col.equals("FIRST")) {
            	infoView.setText("["+round+"ROUND] 시작합니다.  선공입니다..");
            	 ledLabel.setIcon(new ImageIcon(IndianClient.class.getResource("/img/vs.jpg")));
	    	}
          	else
        	  infoView.setText("["+round+"ROUND] 시작합니다.  후공입니다..");
	    		ledLabel.setIcon(new ImageIcon(IndianClient.class.getResource("/img/vs.jpg")));
        
        
	    	writer.println("[YOURCARD]"+myuser.getMyCardNum()[round-1]); //상대방의 이번 라운드 카드 정보를 가져온다.
	    
	    
	    	if(col.equals("FIRST")){              		// 선공일 경우
	    		enabled(true);							// 배팅 버튼 활성화
	    		
	    		
	    		if(myuser.getMyCoin() <2) {				//코인이 1개 이하 일 때 버튼 제어
	    			batt10Button.setEnabled(false);
	    			batt5Button.setEnabled(false);
	    			batt1Button.setEnabled(false);
	    			callButton.setEnabled(true);
	    		}
	    		else if(myuser.getMyCoin() <6) {		//코인이 5개 이하 일 때 버튼 제어
	    			batt10Button.setEnabled(false);
	    			batt5Button.setEnabled(false);
	    			callButton.setEnabled(false);
	    		}
	    		else if(myuser.getMyCoin() <11) {		//코인이 10개 이하 일 떄 버튼 제어
	    			batt10Button.setEnabled(false);
	    			callButton.setEnabled(false);
	    		}
	    		else {									// 그 외의 상황
	    			callButton.setEnabled(false);
	    		}
	    		
	    	}   
	    	else{                                // 후공일 경우
	    		enabled(false); 				 // 버튼 비활성화 
	    	}
	    
	    	
	    	batting(); // 기본배팅 (1개 배팅)
	    
	    	//1. 버튼을 눌러서 배팅을 진행중	(Action)   
	    	//2. 두 명이 1번씩 배팅하면 배팅제어 (Action + batting)
	    	//3. 승패여부([CHECK])
	    	//round 증가후 승패 검증 -> coinToss 에서 startgame 재호출 
	    }
	    
	    
	  }
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==sendBox){             // 메시지 입력 상자이면
		      String msg=sendBox.getText();
		      if(msg.length()==0)return;
		      //if(msg.length()>=30)msg=msg.substring(0,30);
		      try{  
		        writer.println("[MSG]"+msg);	//서버에 메세지 전달 
		        sendBox.setText("");
		      }catch(Exception ie){}
		    }
		 
		    else if(e.getSource()==enterButton){         // 입장하기 버튼이면
		      try{
		        
		        if(Integer.parseInt(roomBox.getText())<1){
		          infoView.setText("방번호가 잘못되었습니다. 1이상");
		          return; 
		        }
		        if(myuser.getUsername().equals("")){  // 아직 이름이 입력이 안되었다면
		  	      String name=nameBox.getText().trim();
		  	      if(name.length()<=2 || name.length()>10){
		  	        infoView.setText("이름이 잘못되었습니다. 3~10자");
		  	        nameBox.requestFocus();
		  	        return;
		  	      }
		  	      myuser.setUsername(name);				//이름을 담고 서버에 전달 
			      writer.println("[NAME]"+myuser.getUsername());    
			      nameBox.setText(myuser.getUsername());
			      nameBox.setEditable(false);
		        }
		          writer.println("[ROOM]"+Integer.parseInt(roomBox.getText()));	//방 번호를 서버에 보냄 
		          msgView.setText("");
		        }catch(Exception ie){
		          infoView.setText("입력하신 사항에 오류가 았습니다."); 
		        }
		    }
		 
		    else if(e.getSource()==exitButton){           // 대기실로 버튼이면
		      try{
		        goToWaitRoom();
		        startButton.setEnabled(false);
		        stopButton.setEnabled(false);
		        meicon.setEnabled(false);
		        usericonu.setEnabled(false);
		      }catch(Exception ie){ infoView.setText("입력하신 사항에 오류가 있습니다.1"); }
		    }
		 
		    else if(e.getSource()==startButton){          //  시작 버튼이면
		      try{
		        writer.println("[START]");
		        infoView.setText("상대의 결정을 기다립니다.");
		        startButton.setEnabled(false);
		      }catch(Exception ie){}
		    }
		
		    else if(e.getSource()==stopButton){          //  기권 버튼이면
			      try{
			       writer.println("[DROPGAME]");	  
			       endGame("게임을 포기하였습니다."); 
			        
			      }catch(Exception ie){}
			}
		
		 
		    else if(e.getSource()==dieButton){          // 다이 버튼이면
		    	try{
			    	  
			    	if( (myuser.getMyCardNum()[round-1] == 10) && (myuser.getMyCoin() > 10) ) {	//자신의 카드가 10이면서, 코인이 10개 이상일 경우
			    		addLog("자신의 카드가 10일때 다이를 하여 자동으로 10개가 배팅됩니다.");
			    		batting(10);
			    	}
			    	else if(( (myuser.getMyCardNum()[round-1] == 10) && (myuser.getMyCoin() < 10) )) { //자신의 카드가 10이면서 코인이 10개 미만일 경우
			    		addLog("자신의 카드가 10인데 다이를 하여 10개의 패널티를 부여합니다. (10보다 코인수가 적으므로 올인됩니다.)");
			    		batting(myuser.getMyCoin());
			    		
			    	}
			    	
			        writer.println("[DIE]");
			      }catch(Exception ie){}
		    }
		
		    else if(e.getSource() == batt1Button) {			//1개배팅 버튼이면
		    	try{
			        batting(1);
			        addLog("["+round+"ROUND] 1개를 배팅!! ");
			        writer.println("[CHECK]");
			      }catch(Exception ie){}
		    }
		
		    else if(e.getSource() == batt5Button) {			//5개배팅 버튼이면
		    	try{
			        batting(5);
			        addLog("["+round+"ROUND] 5개를 배팅!! ");
			        writer.println("[CHECK]");
			      }catch(Exception ie){}
		    }
		
		    else if(e.getSource() == batt10Button) {		//10개배팅 버튼이면
		    	try{
			        batting(10);
			        addLog("["+round+"ROUND] 10개를 배팅!! ");
			        writer.println("[CHECK]");
			      }catch(Exception ie){}
		    }
		
		    else if(e.getSource()==callButton) {			//call 버튼이면
		    	
		    	int battCoin = Integer.parseInt(coinInfo[1]); // 이번라운드 상대의 배팅 코인 ( 배팅 시 마다 받아온다.)
		    	
		    	addLog("["+(round)+"ROUND] 콜하였습니다. ");
		    	
		    	if(battCoin >= myuser.getMyCoin() && battCoin >11) 		//상대의 올인을 콜할때 배팅 코인이 현재 나의 코인보다 많다면..
		    		batting( myuser.getMyCoin() );
		    	else if(battCoin < myuser.getMyCoin() && battCoin >11)  //상대의 올인을 콜할때 배팅 코인이 현재 나의 코인보다 적다면..
		    		batting(battCoin);
		    	else 
		    		batting( (battCoin-myuser.getBatCoin()) );			//이외의 경우 
		    	
		    	writer.println("[CHECK]");
		    	
		    }
			
		    else if(e.getSource()==btnAllin) {				//올인 버튼 이면
		    	
		    	addLog("["+(round)+"ROUND] 올인하였습니다. ");
		    	
		    	if(myuser.getMyCoin() >= Integer.parseInt(coinInfo[0]))	//나의 코인이 상대의 코인보다 많다면...
		    		batting(Integer.parseInt(coinInfo[0]));
		    	else 													//나의 코인이 상대의 코인보다 적다면...
		    		batting(myuser.getMyCoin());
	        	
	        	
		    	writer.println("[CHECK]");								//배팅 여부 체크
		    }
		
		
		
		
	}
	
	void goToWaitRoom(){                   // 대기실로 버튼을 누르면 호출된다.
	    if(myuser.getUsername().equals("")){
	      String name=nameBox.getText().trim();
	      if(name.length()<=2 || name.length()>10){
	        infoView.setText("이름이 잘못되었습니다. 3~10자");
	        nameBox.requestFocus();
	        return;
	      }
	      myuser.setUsername(name);
	      writer.println("[NAME]"+myuser.getUsername());    
	      nameBox.setText(myuser.getUsername());
	      nameBox.setEditable(false);
	    }  
	    addLog("");
	    writer.println("[ROOM]0");			//대기실의 방번호는  0번이므로.
	    infoView.setText("대기실에 입장하셨습니다.");
	    myId_lbl.setText("     ");
	    yourId_lbl.setText("     ");
	    roomBox.setText("0");
	    enterButton.setEnabled(true);
	    exitButton.setEnabled(false);
	  }

	@Override
	public void run() {
		String msg;                             // 서버로부터의 메시지
	    try{
	    while((msg=reader.readLine())!=null){
	 
	 
	        if(msg.startsWith("[ROOM]")){    // 방에 입장
	          if(!msg.equals("[ROOM]0")){          // 대기실이 아닌 방이면
	            enterButton.setEnabled(false);
	            exitButton.setEnabled(true);
	            infoView.setText(msg.substring(6)+"번 방에 입장하셨습니다.");
	            myId_lbl.setText(myuser.getUsername());
	            meicon.setEnabled(true);
	            
	          }
	          else infoView.setText("대기실에 입장하셨습니다.");
	 
	          roomNumber=Integer.parseInt(msg.substring(6));     // 방 번호 지정
	 
	        }
	 
	        else if(msg.startsWith("[FULL]")){       // 방이 찬 상태이면
	          infoView.setText("방이 차서 입장할 수 없습니다.");
	        }
	 
	        else if(msg.startsWith("[PLAYERS]")){      // 방에 있는 사용자 명단
	          nameList(msg.substring(9));
	          String[] yourId = msg.substring(9).split("\t");
	          if(roomNumber > 0 && yourId.length >1) {
	        	  yourId_lbl.setText(yourId[0]);
	        	  usericonu.setEnabled(true);
	          }
	        }
	 
	        else if(msg.startsWith("[ENTER]")){        // 손님 입장
	          pList.add(msg.substring(7));
	          playersInfo();
	          addLog("["+msg.substring(6)+"]님이 다른 방으로 입장하였습니다.");
	          usericonu.setEnabled(true);
	          if(roomNumber != 0) {
	        	  yourId_lbl.setText(msg.substring(7));
	          }
	        }
	        
	        else if(msg.startsWith("[EXIT]")){          // 손님 퇴장
	          pList.remove(msg.substring(6));            // 리스트에서 제거
	          playersInfo();                        // 인원수를 다시 계산하여 보여준다.
	          addLog("["+msg.substring(6)+"]님이 다른 방으로 입장하였습니다.");
	          if(roomNumber!=0) {
	            endGame("상대가 나갔습니다.");		//게임중이 아닐때 나갈 경우 Null 에러가 뜸 
	          	usericonu.setEnabled(false);
	          }
	          yourId_lbl.setText("     ");
	            
	        }
	        
	        else if(msg.startsWith("[DISCONNECT]")){     // 손님 접속 종료
	          pList.remove(msg.substring(12));
	          playersInfo();
	          addLog("["+msg.substring(12)+"]님이 접속을 끊었습니다.");
	          usericonu.setEnabled(false);
	          if(roomNumber!=0)
	            endGame("상대가 나갔습니다.");		//게임중이 아닐때 나갈 경우 Null 에러가 뜸 
	            yourId_lbl.setText("     ");
	        }
	 
	        // 선 후순위 부여 
	        else if(msg.startsWith("[ATTACK]")){          // 선 후공을 부여 받는다. [START] , [DRAW] 일 경우에 호출 된다.
	          String attack=msg.substring(8);
	          this.startGame(attack,round);                      // 게임을 시작한다.
	        }
	        
	        else if(msg.startsWith("[ENDCOIN]")){  			//자신의 현재 코인 갯수를 상대에게 보낸다.
		         String coin = msg.substring(9);
		         endcoin = Integer.parseInt(coin);
		         yourCoin_lbl.setText(Integer.toString(endcoin));

		    }
	        
	        else if(msg.startsWith("[YOURCARD]")) {			//자신의 카드 정보를 상대에게 보낸다.
	        	
	        	yourcard = Integer.parseInt(msg.substring(10));
	        	yourCard_lbl.setIcon(new ImageIcon(IndianClient.class.getResource("/img/"+yourcard+".png")));
	        	
	        }
	        
	        
	        
	        else if(msg.startsWith("[bsBATTING]")) {			//초기배팅
	        	coinInfo = msg.substring(11).split(",");
	        	int totCoin = Integer.parseInt(coinInfo[0]);  // 상대의 전체코인
	        	int battCoin = Integer.parseInt(coinInfo[1]); // 상대의 배팅코인
	        	
	        	yourCoin_lbl.setText(Integer.toString(totCoin));
	        	yourBatt_lbl.setText(Integer.toString(battCoin));
	        	callButton.setText("콜 : "+Integer.toString(battCoin));
	        }
	        
	        else if(msg.startsWith("[MYDIE]")) {				//자기가 다이버튼을 누른 경우
		        infoView.setText("["+round+"ROUND] 다이하셨습니다.");
		        addLog("["+round+"ROUND] 다이하셨습니다.");
		        ledLabel.setIcon(new ImageIcon(IndianClient.class.getResource("/img/lose.jpg")));
		        enabled(false);
		        coinToss(2);  //패배      
	    	}
	        else if(msg.startsWith("[DIE]")) {					//상대가 다이버튼을 누른 경우 
	        	infoView.setText("["+round+"ROUND] 상대방이 다이하였습니다. 승리입니다.");
	        	addLog("["+round+"ROUND] 상대방이 다이하였습니다. 승리입니다.");
	        	ledLabel.setIcon(new ImageIcon(IndianClient.class.getResource("/img/win.jpg")));
	        	if(yourcard==10) {
	        		addLog("상대가 카드의 수가 10일때 다이를 해서 10개의 추가 코인을 얻습니다.");
	        	}
		        coinToss(1);	// 승리
		        enabled(false);
		        
	        }
	        
	        
	        else if(msg.startsWith("[BATTING]")) {				//상대가 배팅을 한 경우  
	        	coinInfo = msg.substring(9).split(",");
	        	int totCoin = Integer.parseInt(coinInfo[0]);  // 상대의 전체코인
	        	int battCoin = Integer.parseInt(coinInfo[1]); // 상대의 배팅코인
	        	
	        	yourCoin_lbl.setText(Integer.toString(totCoin));
	        	yourBatt_lbl.setText(Integer.toString(battCoin));
	        	callButton.setText("콜 : "+Integer.toString(battCoin));
	        	//enabled(true);
	        	addLog("["+round+"ROUND] 당신의 차례입니다. 콜 혹은 다이를 선택해주세요.");
	        	callButton.setEnabled(true);
	        	dieButton.setEnabled(true);
	        }
	        
	        else if(msg.startsWith("[CHECK]")) {	// 양 측 모두 배팅을 했다면, 카드를 비교한다.
	        	enabled(false);
	        	cardCompare(round-1);
	        	
	        }
	        
	        else if(msg.startsWith("[GAMEWIN]")) {	//게임의 최종 결과가 승리시
	        	rankUpdate(myuser.getUsername(),myuser.getWin(),myuser.getDraw(),myuser.getLose(),myuser.getMyCoin());
	        	infoView.setText("[최종 결과]  게임이 당신의 승리로 끝났습니다.");
		    	addLog("[최종 결과]  게임이 당신의 승리로 끝났습니다.");
		    	ledLabel.setIcon(new ImageIcon(IndianClient.class.getResource("/img/win.jpg")));
	        	
	        }
	        
	        else if(msg.startsWith("[GAMELOSE]")) { // 게임의 최종 결과가 패배시
	        	infoView.setText("[최종 결과]  게임이 당신의 패배로 끝났습니다.");
		    	addLog("[최종 결과]  게임이 당신의 패배로 끝났습니다.");
		    	ledLabel.setIcon(new ImageIcon(IndianClient.class.getResource("/img/lose.jpg")));
	        	
	        }
	        
	        
	        else if(msg.startsWith("[DROPGAME]")) {      // 상대가 기권하면
	          endGame("상대가 기권하였습니다.");
	        ledLabel.setIcon(new ImageIcon(IndianClient.class.getResource("/img/win.jpg")));
	        	}
	        // 약속된 메시지가 아니면 메시지 영역에 보여준다.
	        else addLog(msg);
	    }
	    }catch(IOException ie){
	    	addLog(ie+"\n");
	    }
	    addLog("접속이 끊겼습니다.");
	  }
	 
	  public void rankUpdate(String pname, int pwin, int pdraw, int plose,int pcoin) { //랭킹 업데이트
			
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
					JOptionPane.showMessageDialog(null, "클래스 error");
				} catch(SQLException e){
					JOptionPane.showMessageDialog(null, "error");
				}
		  }

	private void endGame(String msg){                // 게임의 종료시 호출 되는 메소드 
	   
		int ninecoin = Integer.parseInt(coinInfo[0]); //꼬인 부분 ...
		  
		infoView.setText(msg);
	    addLog(msg);
	    startButton.setEnabled(false);
	    stopButton.setEnabled(false);
	    writer.println("[STOPGAME]");	//서버의 ready를 false 로 바꿈 
	    gameReset(); // 게임정보 초기화
	    
	    if((msg.indexOf("기권")>=0) ) {	//상대가 기권을 한 경우 
	    	myuser.setMyCoin(myuser.getMyCoin()+myuser.getBatCoin()+Integer.parseInt(coinInfo[1]));
	    	myCoin_lbl.setText(Integer.toString(myuser.getMyCoin()));
	    	writer.println("[GAMEWIN]");
	    }
	    
	    else if((msg.indexOf("코인")==0)) {	//자신의 코인이 다 떨어진 경우 
	    	myuser.setMyCoin(0);
	    	myCoin_lbl.setText(Integer.toString(myuser.getMyCoin()));
	    	writer.println("[GAMELOSE]");
	    }
	    
	    
	    
	    else if((msg.indexOf("포기")>=0)) {	// 자신이 기권을 한 경우 
	    	int dropCoin = myuser.getBatCoin()+Integer.parseInt(coinInfo[1])+Integer.parseInt(coinInfo[0]);
	    	//addLog(Integer.toString(dropCoin));
	    	yourCoin_lbl.setText(Integer.toString(dropCoin));
	    }
	    else {
	    	// 단일 쓰레드 사용의 문제로 endcoin이 제기능을 안해서 마지막 라운드의 코인 비교가 꼬였습니다.
	    	
	    	if( (roundnine_sw == 2) && (myuser.getMyCoin() >= Integer.parseInt(coinInfo[0])) ) //마지막 라운드에 상대가 코인이 더 적고, 자신이 진 경우
	    		ninecoin = ninecoin + (Integer.parseInt(coinInfo[1])*2); 
	    	
	    	if(myuser.getMyCoin() > ninecoin) {		//자신의 코인이 상대보다 많다면..
	    		writer.println("[GAMEWIN]");
	    	}
	    	else if(myuser.getMyCoin() == ninecoin) {		//자신의 코인과 상대의 코인이 같다면..
	    	
	    		infoView.setText("[최종 결과]  게임이 무승부로 끝났습니다.");
	    		addLog("[최종 결과]  게임이 무승부로 끝났습니다.");
	    	}
	    }
	    
	    
	    try{ Thread.sleep(2000); }catch(Exception e){}    // 2초간 대기
	 
	    //if(board.isRunning())board.stopGame();
	    if(pList.getItemCount()==2)startButton.setEnabled(true);	// 게임 종료 후 시작 버튼 활성화
	  }
	 
	  private void playersInfo(){                 // 방에 있는 접속자의 수를 보여준다.
	    int count=pList.getItemCount();
	    if(roomNumber==0)
	      pInfo.setText("대기실: "+count+"명");
	    else pInfo.setText(roomNumber+" 번 방: "+count+"명"); 
	 
	    // 게임시작 버튼의 활성화 상태를 점검한다.
	    if(count==2 && roomNumber!=0)
	      startButton.setEnabled(true);
	    else startButton.setEnabled(false); 
	  }
	 
	  // 사용자 리스트에서 사용자들을 추출하여 pList에 추가한다.
	  private void nameList(String msg){
	    pList.removeAll();
	    StringTokenizer st=new StringTokenizer(msg, "\t");
	    while(st.hasMoreElements())
	      pList.add(st.nextToken());
	    playersInfo();
	  }
	 
	  private void connect(){                    // 연결
	    try{
	      msgView.append("서버에 연결을 요청합니다.\n");
	      socket=new Socket("127.0.0.1", 7222);
	      msgView.append("---연결 성공--.\n");
	      msgView.append("이름을 입력하고 대기실로 입장하세요.\n");
	      reader=new BufferedReader(
	                           new InputStreamReader(socket.getInputStream()));
	      writer=new PrintWriter(socket.getOutputStream(), true);
	      new Thread(this).start();
	      //board.setWriter(writer);
	    }catch(Exception e){
	      msgView.append(e+"\n\n연결 실패..\n");  
	    }
	  }
}
