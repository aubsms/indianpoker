package moving;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import indian.Rule;
import indian.User;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
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

public class IndianClient2 extends JFrame implements Runnable, ActionListener {

	private JPanel contentPane;
	private JTextField nameBox;
	private JTextField roomBox;
	private JTextField sendBox;
	private JTextArea msgView;
	private JLabel infoView;
	
	private JLabel myId_lbl;
	private JLabel yourId_lbl;
	private JLabel myCoin_lbl;
	private JLabel yourCoin_lbl;
	private JLabel result_lbl;
	private JButton startButton;
	private JButton stopButton;
	private JButton batt5Button;
	private JButton callButton;
	private JButton ruleButton;
	private JLabel myCard_lbl;
	private JLabel yourCard_lbl;
	private List pList;
	private JLabel name_lbl;
	private JLabel roomNum_lbl;
	private JButton batt10Button;
	private JButton batt1Button;
	private JButton enterButton;
	private JButton exitButton;
	private JLabel pInfo;
	private JLabel myBatt_lbl;
	private JLabel yourBatt_lbl;
	
	private BufferedReader reader;                         // 입력 스트림
	private PrintWriter writer;                               // 출력 스트림
	private Socket socket;                                 // 소켓
	private int roomNumber=-1;                           // 방 번호
	private int round = 1;
	private int yourcard=0;
	private int endcoin=0;
	private String[] coinInfo;

	private User myuser = new User();
	ImageIcon icon;
	
	private boolean enable=false;
	 
	private boolean running=false;       // 게임이 진행 중인가를 나타내는 변수
	private JLabel meicon;
	private JLabel usericonu;
	private JLabel mCoinC;
	private JLabel coin_01;
	private JLabel yCoinC;
	private JLabel coin_02;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IndianClient2 frame = new IndianClient2();
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
	public IndianClient2() {
		icon = new ImageIcon("e:/images/mainb.jpg");
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
		myId_lbl.setFont(new Font("휴먼모음T", Font.BOLD, 18));
		myId_lbl.setBounds(195, 111, 80, 40);
		contentPane.add(myId_lbl);
		
		yourId_lbl = new JLabel("");
		yourId_lbl.setForeground(Color.BLACK);
		yourId_lbl.setFont(new Font("휴먼모음T", Font.BOLD, 18));
		yourId_lbl.setBounds(470, 111, 80, 40);
		contentPane.add(yourId_lbl);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(50, 430, 580, 130);
		contentPane.add(scrollPane);
		
		msgView = new JTextArea();
		scrollPane.setViewportView(msgView);
		
		myCoin_lbl = new JLabel("");
		myCoin_lbl.setFont(new Font("휴먼모음T", Font.PLAIN, 14));
		myCoin_lbl.setBounds(195, 390, 40, 30);
		contentPane.add(myCoin_lbl);
		
		yourCoin_lbl = new JLabel("");
		yourCoin_lbl.setFont(new Font("휴먼모음T", Font.PLAIN, 14));
		yourCoin_lbl.setBounds(479, 390, 40, 30);
		contentPane.add(yourCoin_lbl);
		
		result_lbl = new JLabel("승/무/패");
		result_lbl.setHorizontalAlignment(SwingConstants.CENTER);
		result_lbl.setBounds(280, 239, 100, 80);
		contentPane.add(result_lbl);
		
		startButton = new JButton("\uC2DC\uC791");
		startButton.setBounds(650, 252, 100, 25);
		contentPane.add(startButton);
		startButton.setEnabled(false);
		
		stopButton = new JButton("\uB2E4\uC774");
		stopButton.setBounds(773, 480, 100, 40);
		contentPane.add(stopButton);
		stopButton.setEnabled(false);
		
		batt5Button = new JButton("");
		batt5Button.setIcon(new ImageIcon("E:\\images\\fchip.png"));
		batt5Button.setBounds(650, 430, 100, 40);
		contentPane.add(batt5Button);
		batt5Button.setEnabled(false);
		
		callButton = new JButton("\uCF5C");
		callButton.setBounds(773, 430, 100, 40);
		contentPane.add(callButton);
		callButton.setEnabled(false);
		
		ruleButton = new JButton("\uAC8C\uC784\uB8F0");
		ruleButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Rule rz = new Rule();
				rz.setVisible(true);
			}
		});
		ruleButton.setBounds(773, 252, 100, 25);
		contentPane.add(ruleButton);
		
		pInfo = new JLabel("\uB300\uAE30\uC2E4: \uBA85");
		pInfo.setBounds(650, 120, 100, 25);
		contentPane.add(pInfo);
		
		myCard_lbl = new JLabel("New label");
		myCard_lbl.setIcon(new ImageIcon(IndianClient2.class.getResource("/img/0.png")));
		myCard_lbl.setBounds(115, 175, 140, 210);
		contentPane.add(myCard_lbl);
		
		yourCard_lbl = new JLabel("New label");
		yourCard_lbl.setIcon(new ImageIcon(IndianClient2.class.getResource("/img/0.png")));
		yourCard_lbl.setBounds(400, 175, 140, 210);
		contentPane.add(yourCard_lbl);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(50, 560, 580, 30);
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
		batt10Button.setIcon(new ImageIcon("E:\\images\\tchip.png"));
		batt10Button.setBounds(650, 480, 100, 40);
		contentPane.add(batt10Button);
		batt10Button.setEnabled(false);
		
		batt1Button = new JButton("");
		batt1Button.setIcon(new ImageIcon("E:\\images\\ochip.png"));
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
		infoView.setFont(new Font("나눔손글씨 펜", Font.PLAIN, 30));
		infoView.setHorizontalAlignment(SwingConstants.CENTER);
		infoView.setBounds(65, 20, 485, 40);
		contentPane.add(infoView);
		
		myBatt_lbl = new JLabel("Bet");
		myBatt_lbl.setFont(new Font("휴먼모음T", Font.PLAIN, 14));
		myBatt_lbl.setHorizontalAlignment(SwingConstants.CENTER);
		myBatt_lbl.setBounds(280, 190, 80, 30);
		contentPane.add(myBatt_lbl);
		
		yourBatt_lbl = new JLabel("your batt");
		yourBatt_lbl.setFont(new Font("휴먼모음T", Font.PLAIN, 14));
		yourBatt_lbl.setHorizontalAlignment(SwingConstants.CENTER);
		yourBatt_lbl.setBounds(280, 340, 80, 30);
		contentPane.add(yourBatt_lbl);
		
		meicon = new JLabel("");
		meicon.setIcon(new ImageIcon("E:\\images\\angel.png"));
		meicon.setBounds(100, 65, 80, 100);
		contentPane.add(meicon);
		meicon.setEnabled(false);
		
		usericonu = new JLabel("");
		usericonu.setIcon(new ImageIcon("E:\\images\\devil.png"));
		usericonu.setBounds(388, 65, 80, 100);
		contentPane.add(usericonu);
		usericonu.setEnabled(false);
		
		mCoinC = new JLabel("\uB0B4 \uCF54\uC778");
		mCoinC.setForeground(Color.YELLOW);
		mCoinC.setFont(new Font("휴먼모음T", Font.PLAIN, 14));
		mCoinC.setBounds(123, 390, 60, 30);
		contentPane.add(mCoinC);
		
		coin_01 = new JLabel("");
		coin_01.setIcon(new ImageIcon("E:\\images\\coins.png"));
		coin_01.setBounds(235, 390, 40, 30);
		contentPane.add(coin_01);
		
		yCoinC = new JLabel("\uC0C1\uB300 \uCF54\uC778");
		yCoinC.setForeground(Color.YELLOW);
		yCoinC.setFont(new Font("휴먼모음T", Font.PLAIN, 14));
		yCoinC.setBounds(398, 390, 60, 30);
		contentPane.add(yCoinC);
		
		coin_02 = new JLabel("");
		coin_02.setIcon(new ImageIcon("E:\\images\\coins.png"));
		coin_02.setBounds(520, 390, 40, 30);
		contentPane.add(coin_02);
		
		sendBox.addActionListener(this);
	    enterButton.addActionListener(this);
	    exitButton.addActionListener(this);
	    startButton.addActionListener(this);
	    stopButton.addActionListener(this);
	    callButton.addActionListener(this);
	    batt1Button.addActionListener(this);
	    batt5Button.addActionListener(this);
	    batt10Button.addActionListener(this);
		
	}

	public boolean isRunning(){           // 게임의 진행 상태를 반환한다.
	    return running; 
	}
	
	public void enabled(boolean b) {
		
		stopButton.setEnabled(b);
		batt1Button.setEnabled(b);
		batt5Button.setEnabled(b);
		batt10Button.setEnabled(b);
		callButton.setEnabled(b);
		
	}
	
	public int[] deck_create(int[] deck) {
		
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
	
	void addLog(String log)
	{
	  msgView.append(log + "\n");  
	  msgView.setCaretPosition(msgView.getDocument().getLength()); 
	}
	
	public void batting() {
		
		int bsBatt = 1;
		
		myuser.setMyCoin(myuser.getMyCoin()-bsBatt);
		myuser.setBatCoin(bsBatt);
		myCoin_lbl.setText(Integer.toString(myuser.getMyCoin()));
		myBatt_lbl.setText("배팅: "+Integer.toString(myuser.getBatCoin()));
		
		writer.println("[bsBATTING]"+myuser.getMyCoin()+","+myuser.getBatCoin()); //현재코인과 배팅코인을 보내준다.
		
		
	}
	
	public void batting(int battCoin) {
		
		myuser.setMyCoin(myuser.getMyCoin()-battCoin);
		myuser.setBatCoin(myuser.getBatCoin()+battCoin);
		myCoin_lbl.setText(Integer.toString(myuser.getMyCoin()));
		myBatt_lbl.setText("배팅: "+Integer.toString(myuser.getBatCoin()));
		
		writer.println("[BATTING]"+myuser.getMyCoin()+","+myuser.getBatCoin()); //현재코인과 배팅코인을 보내준다.
		enabled(false);
		 
		
	}
	
	public void cardCompare(int round) {
		
		infoView.setText("["+(round+1)+"ROUND] 를 종료합니다. ");
		addLog("["+(round+1)+"ROUND] 를 종료합니다. ");
		
		try{ Thread.sleep(1000); }catch(Exception e){}
		
		infoView.setText("["+(round+1)+"ROUND] 카드를 비교합니다. ");
		addLog("["+(round+1)+"ROUND] 카드를 비교합니다. ");
		try{ Thread.sleep(1000); }catch(Exception e){}
		
		myCard_lbl.setIcon(new ImageIcon(IndianClient2.class.getResource("/img/"+myuser.getMyCardNum()[round]+".png")));
		
		
		
		
		if(myuser.getMyCardNum()[round] > yourcard) {
			infoView.setText("["+(round+1)+"ROUND] 당신의 승리입니다. ");
			addLog("["+(round+1)+"ROUND] 당신의 승리입니다. ");
			try{ Thread.sleep(1000); }catch(Exception e){}
			coinToss(1);
		}
		else if(myuser.getMyCardNum()[round] < yourcard) {
			infoView.setText("["+(round+1)+"ROUND] 당신의 패배입니다. ");
			addLog("["+(round+1)+"ROUND] 당신의 패배입니다. ");
			try{ Thread.sleep(1000); }catch(Exception e){}
			coinToss(2);
		}
		else {
			infoView.setText("["+(round+1)+"ROUND] 무승부입니다. ");
			addLog("["+(round+1)+"ROUND] 무승부입니다. ");
			try{ Thread.sleep(1000); }catch(Exception e){}
			coinToss(3);
		}
		
	}
	
	public void coinToss(int status) {
		
		String attack="";
		
		this.round++;
		
		if(status ==1) {
			myuser.setWin(myuser.getWin()+1);
			myuser.setMyCoin(myuser.getMyCoin()+myuser.getBatCoin()+Integer.parseInt(coinInfo[1]));
			attack = "SECOND";
		}
		else if(status == 2) {
			myuser.setLose(myuser.getLose()+1);
			myuser.setMyCoin(myuser.getMyCoin());
			attack = "FIRST";
		}
		else if(status ==3 ) {
			Random r = new Random();
			
			myuser.setDraw(myuser.getDraw()+1);
			myuser.setMyCoin(myuser.getMyCoin()+myuser.getBatCoin());
			try{ Thread.sleep(r.nextInt(100)+1); }catch(Exception e){}
			writer.println("[DRAW]");
		}
		
		if(status==1 || status==2)
			startGame(attack, this.round);
	}
	
	public void reset() {
		
		myCard_lbl.setIcon(new ImageIcon(IndianClient2.class.getResource("/img/0.png")));
	    yourCard_lbl.setIcon(new ImageIcon(IndianClient2.class.getResource("/img/0.png")));
	    myCoin_lbl.setText(Integer.toString(myuser.getMyCoin()));
	    writer.println("[ENDCOIN]"+myuser.getMyCoin());
	    myBatt_lbl.setText("0");
	    yourBatt_lbl.setText("0");
	    result_lbl.setText(myuser.getWin()+"승/"+myuser.getDraw()+"무/"+myuser.getLose()+"패");
	    myuser.setBatCoin(0);
	    writer.println("[IS_RESET]");
		
	}
	
	public void gameReset() {
		
		myCard_lbl.setIcon(new ImageIcon(IndianClient2.class.getResource("/img/0.png")));
	    yourCard_lbl.setIcon(new ImageIcon(IndianClient2.class.getResource("/img/0.png")));
	    myCoin_lbl.setText(Integer.toString(myuser.getMyCoin()));
	    writer.println("[ENDCOIN]"+myuser.getMyCoin());
	    myBatt_lbl.setText("배팅: 개");
	    yourBatt_lbl.setText("배팅: 개");
	    writer.println("[IS_RESET]");
	    enabled(false);
	    this.round =1;
		
	}
	
	public void startGame(String col, int round){     // 게임을 시작한다.
	    running=true;
	    
	    if(round==1) {
	    	myuser.setMyCardNum(deck_create(myuser.getMyCardNum()));
	    	myuser.setMyCoin(50);
	    	myCoin_lbl.setText(Integer.toString(myuser.getMyCoin()));
	    	yourCoin_lbl.setText("50");
	    	infoView.setText("잠시 후 게임을 시작합니다.");
	    	result_lbl.setText("0승/0무/0패");
	    	myuser.setWin(0); myuser.setDraw(0); myuser.setLose(0);
	    	endcoin=0;
	    }
	    
	    reset();
	    
	    if(round>9) {
	    	running =false;
	    	endGame("게임이 종료되었습니다.");
	    }
	    
	    
	    
	    try{ Thread.sleep(1000); }catch(Exception e){}
	    
	    if(running == true) {
	    
        if(col.equals("FIRST"))
            infoView.setText("["+round+"ROUND] 시작합니다.  선공입니다..");
          else
        	infoView.setText("["+round+"ROUND] 시작합니다.  후공입니다..");
        
        
        
	    writer.println("[YOURCARD]"+myuser.getMyCardNum()[round-1]);
	    
	    
	    if(col.equals("FIRST")){              // 흑이 선택되었을 때
	    	enabled(true); 
	    }   
	    else{                                // 백이 선택되었을 때
	    	enabled(false); 
	    }
	    // 배팅 
	    batting(); // 기본배팅
	    
	    //1. 버튼을 눌러서 배팅을 진행중	(Action)   
	    //2. 두 명이 1번씩 배팅하면 배팅제어 (Action + batting)
	    //3. 승패여부([CHECK])
	    //round 증가후 승패 검증 -> win lose 에서 startgame 재호출 
	    }
	    
	    
	  }
	
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==sendBox){             // 메시지 입력 상자이면
		      String msg=sendBox.getText();
		      if(msg.length()==0)return;
		      if(msg.length()>=30)msg=msg.substring(0,30);
		      try{  
		        writer.println("[MSG]"+msg);
		        sendBox.setText("");
		      }catch(Exception ie){}
		    }
		 
		    else if(e.getSource()==enterButton){         // 입장하기 버튼이면
		      try{
		        
		        if(Integer.parseInt(roomBox.getText())<1){
		          infoView.setText("방번호가 잘못되었습니다. 1이상");
		          return; 
		        }
		        if(myuser.getUsername().equals("")){
		          myCard_lbl.setText("1");
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
		          writer.println("[ROOM]"+Integer.parseInt(roomBox.getText()));
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
		      }catch(Exception ie){ infoView.setText("입력하신 사항에 오류가 았습니다.1"); }
		    }
		 
		    else if(e.getSource()==startButton){          // 대국 시작 버튼이면
		      try{
		        writer.println("[START]");
		        infoView.setText("상대의 결정을 기다립니다.");
		        startButton.setEnabled(false);
		      }catch(Exception ie){}
		    }
		 
		    else if(e.getSource()==stopButton){          // 기권 버튼이면
		    	try{
			    	  
			    	if(myuser.getMyCardNum()[round-1] == 10) {
			    		addLog("자신의 카드가 10일때 다이를 하여 자동으로 10개가 배팅됩니다.");
			    		batting(10);
			    	}
			    	
			        writer.println("[DIE]");
			      }catch(Exception ie){}
		    }
		
		    else if(e.getSource() == batt1Button) {
		    	try{
			        batting(1);
			        addLog("["+round+"ROUND] 1개를 배팅!! ");
			        writer.println("[CHECK]");
			      }catch(Exception ie){}
		    }
		
		    else if(e.getSource() == batt5Button) {
		    	try{
			        batting(5);
			        addLog("["+round+"ROUND] 5개를 배팅!! ");
			        writer.println("[CHECK]");
			      }catch(Exception ie){}
		    }
		
		    else if(e.getSource() == batt10Button) {
		    	try{
			        batting(10);
			        addLog("["+round+"ROUND] 10개를 배팅!! ");
			        writer.println("[CHECK]");
			      }catch(Exception ie){}
		    }
		
		    else if(e.getSource()==callButton) {
		    	
		    	int battCoin = Integer.parseInt(coinInfo[1]); // 상대의 배팅 코인
	        	addLog("["+(round+1)+"ROUND] 콜하였습니다. ");
		    	batting( (battCoin-myuser.getBatCoin()) );
		    	writer.println("[CHECK]");
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
	    writer.println("[ROOM]0");
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
	            endGame("상대가 나갔습니다.");
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
	            endGame("상대가 나갔습니다.");
	            yourId_lbl.setText("     ");
	        }
	 
	        // 선 후순위 부여 
	        else if(msg.startsWith("[ATTACK]")){          // 돌의 색을 부여받는다.
	          String attack=msg.substring(8);
	          this.startGame(attack,round);                      // 게임을 시작한다.
	        }
	        
	        else if(msg.startsWith("[ENDCOIN]")){          
		         String coin = msg.substring(9);
		         endcoin = Integer.parseInt(coin);
		         yourCoin_lbl.setText(Integer.toString(endcoin));

		    }
	        
	        else if(msg.startsWith("[YOURCARD]")) {
	        	
	        	yourcard = Integer.parseInt(msg.substring(10));
	        	yourCard_lbl.setIcon(new ImageIcon(IndianClient2.class.getResource("/img/"+yourcard+".png")));
	        	
	        }
	        
	        
	        
	        else if(msg.startsWith("[bsBATTING]")) {
	        	coinInfo = msg.substring(11).split(",");
	        	int totCoin = Integer.parseInt(coinInfo[0]);  // 상대의 전체코인
	        	int battCoin = Integer.parseInt(coinInfo[1]); // 상대의 배팅코인
	        	
	        	yourCoin_lbl.setText(Integer.toString(totCoin));
	        	yourBatt_lbl.setText("배팅: "+Integer.toString(battCoin));
	        }
	        
	        else if(msg.startsWith("[MYDIE]")) {
		        infoView.setText("["+round+"ROUND] 다이하셨습니다.");
		        addLog("["+round+"ROUND] 다이하셨습니다.");
		        enabled(false);
		        coinToss(2);        
	    	}
	        else if(msg.startsWith("[DIE]")) {
	        	infoView.setText("["+round+"ROUND] 상대방이 다이하였습니다. 승리입니다.");
	        	addLog("["+round+"ROUND] 상대방이 다이하였습니다. 승리입니다.");
	        	if(yourcard==10) {
	        		addLog("상대가 카드의 수가 10일때 다이를 해서 10개의 추가 코인을 얻습니다.");
	        	}
		        coinToss(1);
		        enabled(false);
		        
	        }
	        
	        
	        else if(msg.startsWith("[BATTING]")) {
	        	coinInfo = msg.substring(9).split(",");
	        	int totCoin = Integer.parseInt(coinInfo[0]);  // 상대의 전체코인
	        	int battCoin = Integer.parseInt(coinInfo[1]); // 상대의 배팅코인
	        	
	        	yourCoin_lbl.setText(Integer.toString(totCoin));
	        	yourBatt_lbl.setText("배팅: "+Integer.toString(battCoin));
	        	//enabled(true);
	        	addLog("["+round+"ROUND] 당신의 차례입니다. 콜 혹은 다이를 선택해주세요.");
	        	callButton.setEnabled(true);
	        	stopButton.setEnabled(true);
	        }
	        
	        else if(msg.startsWith("[CHECK]")) {
	        	enabled(false);
	        	cardCompare(round-1);
	        	
	        }
	        
	        else if(msg.startsWith("[GAMEWIN]")) {
	        	infoView.setText("[최종 결과]  게임이 당신의 승리로 끝났습니다.");
		    	addLog("[최종 결과]  게임이 당신의 승리로 끝났습니다.");
	        	
	        }
	        
	        else if(msg.startsWith("[GAMELOSE]")) {
	        	infoView.setText("[최종 결과]  게임이 당신의 패배로 끝났습니다.");
		    	addLog("[최종 결과]  게임이 당신의 패배로 끝났습니다.");
	        	
	        }
	        
	        
	        else if(msg.startsWith("[DROPGAME]"))      // 상대가 기권하면
	          endGame("상대가 기권하였습니다.");
	        
	        // 약속된 메시지가 아니면 메시지 영역에 보여준다.
	        else addLog(msg);
	    }
	    }catch(IOException ie){
	    	addLog(ie+"\n");
	    }
	    addLog("접속이 끊겼습니다.");
	  }
	 
	  private void endGame(String msg){                // 게임의 종료시키는 메소드
	    infoView.setText(msg);
	    startButton.setEnabled(false);
	    stopButton.setEnabled(false);
	    writer.println("[STOPGAME]");
	    gameReset();
	    
	    if(myuser.getMyCoin() > endcoin) {
	    	writer.println("[GAMEWIN]");
	    }
	    else if(myuser.getMyCoin() == endcoin) {
	    	
	    	infoView.setText("[최종 결과]  게임이 무승부로 끝났습니다.");
	    	addLog("[최종 결과]  게임이 무승부로 끝났습니다.");
	    }
	    
	    try{ Thread.sleep(1000); }catch(Exception e){}    // 2초간 대기
	 
	    //if(board.isRunning())board.stopGame();
	    if(pList.getItemCount()==2)startButton.setEnabled(true);
	  }
	 
	  private void playersInfo(){                 // 방에 있는 접속자의 수를 보여준다.
	    int count=pList.getItemCount();
	    if(roomNumber==0)
	      pInfo.setText("대기실: "+count+"명");
	    else pInfo.setText(roomNumber+" 번 방: "+count+"명"); 
	 
	    // 대국 시작 버튼의 활성화 상태를 점검한다.
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
	      socket=new Socket("127.0.0.1", 7131);
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
