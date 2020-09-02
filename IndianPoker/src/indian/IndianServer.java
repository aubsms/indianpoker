package indian;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.Vector;

import java.net.*;
import java.io.*;
import java.util.*;
public class IndianServer{
  private ServerSocket server;
  private BManager bMan=new BManager();   // 메시지 방송자
  private Random rnd= new Random();       // 선 순위, 후 순위를 정하는 변수
  public IndianServer(){}
  void startServer(){                     // 서버를 실행한다.
    try{
      server=new ServerSocket(7222);	  //서버 소켓 포트 지정
      System.out.println("서버소켓이 생성되었습니다.");
      while(true){	//무한루프를 돌면서 클라이언트의 정보를 받는다.
 
        // 클라이언트와 연결된 스레드를 얻는다.
        Socket socket=server.accept();
        
        // 스레드를 만들고 실행시킨다.
        Indian_Thread it=new Indian_Thread(socket);
        it.start();
 
        // bMan에 스레드를 추가한다.
        bMan.add(it);
        
        
        // 접속한 클라이언트의 수를 보여준다.
        System.out.println("접속자 수: "+bMan.size()); 
      }
    }catch(Exception e){
      System.out.println(e); 
    }
  }
  public static void main(String[] args){
	IndianServer server=new IndianServer();
    server.startServer();
  }
 
 // 클라이언트와 통신하는 스레드 클래스
  class Indian_Thread extends Thread{
    private int roomNumber=-1;        // 방 번호
    private String userName=null;       // 사용자 이름
    private Socket socket;              // 소켓
 
    
    private boolean ready=false;		// 게임 준비 여부, true이면 게임을 시작할 준비가 되었음을 의미한다.
    private boolean batting = false;	// 게임 배팅 여부 , true이면 배팅이 되었음을 의미한다.
    private boolean draw = false;		// 게임 무승부 여부, true이면 게임이 무승부로 끝났음을 의미한다.
    
    private BufferedReader reader;     // 입력 스트림
    private PrintWriter writer;           // 출력 스트림
    Indian_Thread(Socket socket){     // 생성자
      this.socket=socket;
    }
    Socket getSocket(){               // 소켓을 반환한다.
      return socket; 
    }
    int getRoomNumber(){             // 방 번호를 반환한다.
      return roomNumber;
    }
    String getUserName(){             // 사용자 이름을 반환한다.
      return userName;
    }
    boolean isReady(){                 // 준비 상태를 반환한다.
      return ready;
    }
    boolean isBatting() {				//배팅 상태를 반환한다.
    	return batting;
    }
    boolean isDraw() {					//무승부 상태를 반환한다.
    	return draw;
    }
    public void run(){
      try{
        reader=new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
        
        writer=new PrintWriter(socket.getOutputStream(), true);
 
        String msg;                     // 클라이언트의 메시지
 
        while((msg=reader.readLine())!=null){	//스레드로 클라이언트에서 오는 메세지를 계속 받는다.
 
          // msg가 "[NAME]"으로 시작되는 메시지이면		[NAME]aaa 의 형식으로 메세지가 온다. 클라이언트의 이름 칸의 데이터를 가져온다.
          if(msg.startsWith("[NAME]")){
            userName=msg.substring(6);          // userName을 정한다.
          }
 
          // msg가 "[ROOM]"으로 시작되면 방 번호를 정한다.		[ROOM]2의 형식으로 메세지가 온다. 클라이언트의 방 번호 칸의 데이터를 가져온다.
          else if(msg.startsWith("[ROOM]")){
            int roomNum=Integer.parseInt(msg.substring(6));
            if( !bMan.isFull(roomNum)){             // 방이 찬 상태가 아니면
 
              // (대기방이든 어느방이든 사용자가 다른방으로 이동하는 것이기에) 현재 방의 다른 사용에게 사용자의 퇴장을 알린다. 
              if(roomNumber!=-1)
                bMan.sendToOthers(this, "[EXIT]"+userName);
 
              // 사용자의 새 방 번호를 지정한다.
              roomNumber=roomNum;
 
              // 사용자에게 메시지를 그대로 전송하여 입장할 수 있음을 알린다.   
              writer.println(msg);
 
              // 사용자에게 새 방에 있는 사용자 이름 리스트를 전송한다.
              writer.println(bMan.getNamesInRoom(roomNumber));
 
              // 새 방에 있는 다른 사용자에게 사용자의 입장을 알린다.
              bMan.sendToOthers(this, "[ENTER]"+userName);
            }
            else writer.println("[FULL]");        // 사용자에 방이 찼음을 알린다.
          }
          
 
          // 대화 메시지를 방에 전송한다.
          else if(msg.startsWith("[MSG]"))
            bMan.sendToRoom(roomNumber,
                              "["+userName+"]: "+msg.substring(5));
 
          // "[START]" 메시지이면
          else if(msg.startsWith("[START]")){
            ready=true;   // 게임을 시작할 준비가 되었다.
 
            // 다른 사용자도 게임을 시작한 준비가 되었으면   isReady는 synchronized 함수이다.    
            if(bMan.isReady(roomNumber)){   
 
              // 선, 후순위를 정하고 사용자와 상대편에게 전송한다.
              int a=rnd.nextInt(2);   
              if(a==0){
                writer.println("[ATTACK]FIRST");			//자신이 선공이면 상대에겐 후공을 보낸다.
                bMan.sendToOthers(this, "[ATTACK]SECOND");
              }
              else{
                writer.println("[ATTACK]SECOND");			//자신이 후공이면 상대에겐 선공을 보낸다.
                bMan.sendToOthers(this, "[ATTACK]FIRST");
              }
            }
          }
          
          else if(msg.startsWith("[IS_RESET]")) {			//라운드 혹은 게임이 끝날 때 배팅과 드로우 여부를 초기화한다.
        	  batting = false;
        	  draw = false;
        	  
          }
          
          else if(msg.startsWith("[ENDCOIN]")) {			//라운드 혹은 게임이 끝날때의 자신의 총 코인 갯수를 상대에게 전송한다.
        	  bMan.sendToOthers(this, msg);
          }
          
          else if(msg.startsWith("[YOURCARD]")) {			//라운드마다 자신의 카드정보를 상대에게 넘겨준다.
        	  bMan.sendToOthers(this, msg);
          }
          
          
          
          else if(msg.startsWith("[bsBATTING]")) {			//초기배팅(1)에 대한 정보를 상대에게 넘겨준다.
        	  bMan.sendToOthers(this, msg);
          }

          
          else if(msg.startsWith("[BATTING]")) {			//배팅의 여부를 true로 바꾸고 상대방에게 자신의 배팅 정보(코인갯수, 배팅 코인)을 상대에게 넘겨준다.
        	  batting = true;
        	  bMan.sendToOthers(this, msg);
          }
          
          else if(msg.startsWith("[DIE]")) {				//사용자가 다이 버튼을 누르면 사용자에게 [MYDIE]를 보내어 자신이 죽었음을 알리고,
        	  writer.println("[MYDIE]");					// 상대에게는 [DIE]를 보내어 자신이 다이버튼을 눌렀음을 알린다.
        	  bMan.sendToOthers(this, msg);
          }
          
          else if(bMan.isBatting(roomNumber)) {				//isBatting은 synchronized 함수이다. 
        	  bMan.sendToRoom(roomNumber, "[CHECK]");		//두 사용자가 전부 배팅을 했다면, 사용자들에게 배팅이 끝났음을 [CHECK] 로 알린다.
        	  batting=false;								//batting을 안꺼주면 가끔 에러가 떠서 꺼둠.
          }
          
          
 
          // 사용자가 게임이 끝났다는 메시지를 보내면  
          else if(msg.startsWith("[STOPGAME]"))			
            ready=false;
 
          // 사용자가 게임을 기권하는 메시지를 보내면
          else if(msg.startsWith("[DROPGAME]")){
            ready=false;
            // 상대편에게 사용자의 기권을 알린다.
            bMan.sendToOthers(this, "[DROPGAME]");
          } 
          
          else if(msg.startsWith("[DRAW]")){		//라운드가 무승부를 하면,
              draw = true;
              
              if(bMan.isDraw(roomNumber)) {					//isBatting은 synchronized 함수이다. 선 후순위를 랜덤으로 다시 보내기위해 사용
            	  int a=rnd.nextInt(2);						//[START] 때와 같이 선후순위를 정해서 다시 게임을 시작한다.
            	  if(a==0){
            		  writer.println("[ATTACK]FIRST");
            		  bMan.sendToOthers(this, "[ATTACK]SECOND");
            	  }
            	  else{
            		  writer.println("[ATTACK]SECOND");
            		  bMan.sendToOthers(this, "[ATTACK]FIRST");
            	  }
              }  
           }
          
          
          else if(msg.startsWith("[GAMEWIN]")){				//게임이 끝났을때 승리를 했다면
        	  writer.println("[GAMEWIN]");					//자기 자신에게 이겼음을 알리고,
        	  bMan.sendToOthers(this, "[GAMELOSE]");		//상대방에게는 졌다고 알린다.
          }
          
          else if(msg.startsWith("[GAMELOSE]")){			//게임이 끝났을때 패배했다면,
        	  writer.println("[GAMELOSE]");					//자기 자신에게 졌음을 알린다.
        	  //bMan.sendToOthers(this, "[GAMEWIN]");		//상대에게 승리를 알릴 경우 이겼다는 정보가 2번들어가 랭킹이 2번 등록되서 주석 처리.
          }
          
          
          
        }
      }catch(Exception e){
      }finally{
        try{
          bMan.remove(this);					//사용자가 나가면 메세지 사용자를 제거하고, 입력,출력스트림과 소켓을 꺼준다. 
          if(reader!=null) reader.close();
          if(writer!=null) writer.close();
          if(socket!=null) socket.close();
          reader=null; writer=null; socket=null;
          System.out.println(userName+"님이 접속을 끊었습니다.");
          System.out.println("접속자 수: "+bMan.size());
          // 사용자가 접속을 끊었음을 같은 방에 알린다.
          bMan.sendToRoom(roomNumber,"[DISCONNECT]"+userName);
        }catch(Exception e){}
      }
    }
  }
  
  
  
  class BManager extends Vector{       // 메시지를 전달하는 클래스
    BManager(){}
    void add(Indian_Thread it){           // 스레드를 추가한다.
      super.add(it);
    }
    void remove(Indian_Thread it){        // 스레드를 제거한다.
       super.remove(it);
    }
    Indian_Thread getOT(int i){            // i번째 스레드를 반환한다.
      return (Indian_Thread)elementAt(i);
    }
    Socket getSocket(int i){              // i번째 스레드의 소켓을 반환한다.
      return getOT(i).getSocket();
    }
 
    // i번째 스레드와 연결된 클라이언트에게 메시지를 전송한다.
    void sendTo(int i, String msg){
      try{
        PrintWriter pw= new PrintWriter(getSocket(i).getOutputStream(), true);
        pw.println(msg);
      }catch(Exception e){}  
    }
    int getRoomNumber(int i){            // i번째 스레드의 방 번호를 반환한다.
      return getOT(i).getRoomNumber();
    }
    synchronized boolean isFull(int roomNum){    // 방이 찼는지 알아본다.
      if(roomNum==0)return false;                 // 대기실은 차지 않는다.
 
      // 다른 방은 2명 이상 입장할 수 없다.
      int count=0;
      for(int i=0;i<size();i++)
        if(roomNum==getRoomNumber(i))count++;
      if(count>=2)return true;
      return false;
    }
 
    // roomNum 방에 msg를 전송한다.
    void sendToRoom(int roomNum, String msg){
      for(int i=0;i<size();i++)
        if(roomNum==getRoomNumber(i))	// 같은방에 있으면서 자기자신을 포함한다.
          sendTo(i, msg);
    }
 
    // it와 같은 방에 있는 다른 사용자에게 msg를 전달한다.
    void sendToOthers(Indian_Thread it, String msg){
      for(int i=0;i<size();i++)
        if(getRoomNumber(i)==it.getRoomNumber() && getOT(i)!=it) // 같은방에 있으면서 자기자신은 제외한다.
          sendTo(i, msg);
    }
 
    // 게임을 시작할 준비가 되었는가를 반환한다.
    // 두 명의 사용자 모두 준비된 상태이면 true를 반환한다.
    synchronized boolean isReady(int roomNum){
      int count=0;
      for(int i=0;i<size();i++) {
        if(roomNum==getRoomNumber(i) && getOT(i).isReady())
          count++;
      }
      if(count==2)return true;
      return false;
    }
    
    //라운드마다 배팅이 끝났는가를 반환한다.
    //두 명의 사용자가 모두 배팅을 했다면 true를 반환한다.
    synchronized boolean isBatting(int roomNum) {
    	int count=0;
    	for(int i=0;i<size();i++) {
    		if(roomNum==getRoomNumber(i) && getOT(i).isBatting())
    			count++;
    	}
    	if(count==2) return true;
    	return false;
    }
    
    //라운드마다 무승부로 끝났는가를 반환한다.
    //두 명의 사용자가 무승부를 했다면 true를 반환한다.
    synchronized boolean isDraw(int roomNum) {
    	int count=0;
    	for(int i=0;i<size();i++) {
    		if(roomNum==getRoomNumber(i) && getOT(i).isDraw())
    			count++;
    	}
    	if(count==2) return true;
    	return false;
    }
 
    // roomNum방에 있는 사용자들의 이름을 반환한다.		
    String getNamesInRoom(int roomNum){
      StringBuffer sb=new StringBuffer("[PLAYERS]");
      for(int i=0;i<size();i++)
        if(roomNum==getRoomNumber(i))
          sb.append(getOT(i).getUserName()+"\t");
      return sb.toString();
    }
  }
}
