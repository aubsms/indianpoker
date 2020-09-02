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
  private BManager bMan=new BManager();   // �޽��� �����
  private Random rnd= new Random();       // �� ����, �� ������ ���ϴ� ����
  public IndianServer(){}
  void startServer(){                     // ������ �����Ѵ�.
    try{
      server=new ServerSocket(7222);	  //���� ���� ��Ʈ ����
      System.out.println("���������� �����Ǿ����ϴ�.");
      while(true){	//���ѷ����� ���鼭 Ŭ���̾�Ʈ�� ������ �޴´�.
 
        // Ŭ���̾�Ʈ�� ����� �����带 ��´�.
        Socket socket=server.accept();
        
        // �����带 ����� �����Ų��.
        Indian_Thread it=new Indian_Thread(socket);
        it.start();
 
        // bMan�� �����带 �߰��Ѵ�.
        bMan.add(it);
        
        
        // ������ Ŭ���̾�Ʈ�� ���� �����ش�.
        System.out.println("������ ��: "+bMan.size()); 
      }
    }catch(Exception e){
      System.out.println(e); 
    }
  }
  public static void main(String[] args){
	IndianServer server=new IndianServer();
    server.startServer();
  }
 
 // Ŭ���̾�Ʈ�� ����ϴ� ������ Ŭ����
  class Indian_Thread extends Thread{
    private int roomNumber=-1;        // �� ��ȣ
    private String userName=null;       // ����� �̸�
    private Socket socket;              // ����
 
    
    private boolean ready=false;		// ���� �غ� ����, true�̸� ������ ������ �غ� �Ǿ����� �ǹ��Ѵ�.
    private boolean batting = false;	// ���� ���� ���� , true�̸� ������ �Ǿ����� �ǹ��Ѵ�.
    private boolean draw = false;		// ���� ���º� ����, true�̸� ������ ���ºη� �������� �ǹ��Ѵ�.
    
    private BufferedReader reader;     // �Է� ��Ʈ��
    private PrintWriter writer;           // ��� ��Ʈ��
    Indian_Thread(Socket socket){     // ������
      this.socket=socket;
    }
    Socket getSocket(){               // ������ ��ȯ�Ѵ�.
      return socket; 
    }
    int getRoomNumber(){             // �� ��ȣ�� ��ȯ�Ѵ�.
      return roomNumber;
    }
    String getUserName(){             // ����� �̸��� ��ȯ�Ѵ�.
      return userName;
    }
    boolean isReady(){                 // �غ� ���¸� ��ȯ�Ѵ�.
      return ready;
    }
    boolean isBatting() {				//���� ���¸� ��ȯ�Ѵ�.
    	return batting;
    }
    boolean isDraw() {					//���º� ���¸� ��ȯ�Ѵ�.
    	return draw;
    }
    public void run(){
      try{
        reader=new BufferedReader(
                            new InputStreamReader(socket.getInputStream()));
        
        writer=new PrintWriter(socket.getOutputStream(), true);
 
        String msg;                     // Ŭ���̾�Ʈ�� �޽���
 
        while((msg=reader.readLine())!=null){	//������� Ŭ���̾�Ʈ���� ���� �޼����� ��� �޴´�.
 
          // msg�� "[NAME]"���� ���۵Ǵ� �޽����̸�		[NAME]aaa �� �������� �޼����� �´�. Ŭ���̾�Ʈ�� �̸� ĭ�� �����͸� �����´�.
          if(msg.startsWith("[NAME]")){
            userName=msg.substring(6);          // userName�� ���Ѵ�.
          }
 
          // msg�� "[ROOM]"���� ���۵Ǹ� �� ��ȣ�� ���Ѵ�.		[ROOM]2�� �������� �޼����� �´�. Ŭ���̾�Ʈ�� �� ��ȣ ĭ�� �����͸� �����´�.
          else if(msg.startsWith("[ROOM]")){
            int roomNum=Integer.parseInt(msg.substring(6));
            if( !bMan.isFull(roomNum)){             // ���� �� ���°� �ƴϸ�
 
              // (�����̵� ������̵� ����ڰ� �ٸ������� �̵��ϴ� ���̱⿡) ���� ���� �ٸ� ��뿡�� ������� ������ �˸���. 
              if(roomNumber!=-1)
                bMan.sendToOthers(this, "[EXIT]"+userName);
 
              // ������� �� �� ��ȣ�� �����Ѵ�.
              roomNumber=roomNum;
 
              // ����ڿ��� �޽����� �״�� �����Ͽ� ������ �� ������ �˸���.   
              writer.println(msg);
 
              // ����ڿ��� �� �濡 �ִ� ����� �̸� ����Ʈ�� �����Ѵ�.
              writer.println(bMan.getNamesInRoom(roomNumber));
 
              // �� �濡 �ִ� �ٸ� ����ڿ��� ������� ������ �˸���.
              bMan.sendToOthers(this, "[ENTER]"+userName);
            }
            else writer.println("[FULL]");        // ����ڿ� ���� á���� �˸���.
          }
          
 
          // ��ȭ �޽����� �濡 �����Ѵ�.
          else if(msg.startsWith("[MSG]"))
            bMan.sendToRoom(roomNumber,
                              "["+userName+"]: "+msg.substring(5));
 
          // "[START]" �޽����̸�
          else if(msg.startsWith("[START]")){
            ready=true;   // ������ ������ �غ� �Ǿ���.
 
            // �ٸ� ����ڵ� ������ ������ �غ� �Ǿ�����   isReady�� synchronized �Լ��̴�.    
            if(bMan.isReady(roomNumber)){   
 
              // ��, �ļ����� ���ϰ� ����ڿ� ������� �����Ѵ�.
              int a=rnd.nextInt(2);   
              if(a==0){
                writer.println("[ATTACK]FIRST");			//�ڽ��� �����̸� ��뿡�� �İ��� ������.
                bMan.sendToOthers(this, "[ATTACK]SECOND");
              }
              else{
                writer.println("[ATTACK]SECOND");			//�ڽ��� �İ��̸� ��뿡�� ������ ������.
                bMan.sendToOthers(this, "[ATTACK]FIRST");
              }
            }
          }
          
          else if(msg.startsWith("[IS_RESET]")) {			//���� Ȥ�� ������ ���� �� ���ð� ��ο� ���θ� �ʱ�ȭ�Ѵ�.
        	  batting = false;
        	  draw = false;
        	  
          }
          
          else if(msg.startsWith("[ENDCOIN]")) {			//���� Ȥ�� ������ �������� �ڽ��� �� ���� ������ ��뿡�� �����Ѵ�.
        	  bMan.sendToOthers(this, msg);
          }
          
          else if(msg.startsWith("[YOURCARD]")) {			//���帶�� �ڽ��� ī�������� ��뿡�� �Ѱ��ش�.
        	  bMan.sendToOthers(this, msg);
          }
          
          
          
          else if(msg.startsWith("[bsBATTING]")) {			//�ʱ����(1)�� ���� ������ ��뿡�� �Ѱ��ش�.
        	  bMan.sendToOthers(this, msg);
          }

          
          else if(msg.startsWith("[BATTING]")) {			//������ ���θ� true�� �ٲٰ� ���濡�� �ڽ��� ���� ����(���ΰ���, ���� ����)�� ��뿡�� �Ѱ��ش�.
        	  batting = true;
        	  bMan.sendToOthers(this, msg);
          }
          
          else if(msg.startsWith("[DIE]")) {				//����ڰ� ���� ��ư�� ������ ����ڿ��� [MYDIE]�� ������ �ڽ��� �׾����� �˸���,
        	  writer.println("[MYDIE]");					// ��뿡�Դ� [DIE]�� ������ �ڽ��� ���̹�ư�� �������� �˸���.
        	  bMan.sendToOthers(this, msg);
          }
          
          else if(bMan.isBatting(roomNumber)) {				//isBatting�� synchronized �Լ��̴�. 
        	  bMan.sendToRoom(roomNumber, "[CHECK]");		//�� ����ڰ� ���� ������ �ߴٸ�, ����ڵ鿡�� ������ �������� [CHECK] �� �˸���.
        	  batting=false;								//batting�� �Ȳ��ָ� ���� ������ ���� ����.
          }
          
          
 
          // ����ڰ� ������ �����ٴ� �޽����� ������  
          else if(msg.startsWith("[STOPGAME]"))			
            ready=false;
 
          // ����ڰ� ������ ����ϴ� �޽����� ������
          else if(msg.startsWith("[DROPGAME]")){
            ready=false;
            // ������� ������� ����� �˸���.
            bMan.sendToOthers(this, "[DROPGAME]");
          } 
          
          else if(msg.startsWith("[DRAW]")){		//���尡 ���ºθ� �ϸ�,
              draw = true;
              
              if(bMan.isDraw(roomNumber)) {					//isBatting�� synchronized �Լ��̴�. �� �ļ����� �������� �ٽ� ���������� ���
            	  int a=rnd.nextInt(2);						//[START] ���� ���� ���ļ����� ���ؼ� �ٽ� ������ �����Ѵ�.
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
          
          
          else if(msg.startsWith("[GAMEWIN]")){				//������ �������� �¸��� �ߴٸ�
        	  writer.println("[GAMEWIN]");					//�ڱ� �ڽſ��� �̰����� �˸���,
        	  bMan.sendToOthers(this, "[GAMELOSE]");		//���濡�Դ� ���ٰ� �˸���.
          }
          
          else if(msg.startsWith("[GAMELOSE]")){			//������ �������� �й��ߴٸ�,
        	  writer.println("[GAMELOSE]");					//�ڱ� �ڽſ��� ������ �˸���.
        	  //bMan.sendToOthers(this, "[GAMEWIN]");		//��뿡�� �¸��� �˸� ��� �̰�ٴ� ������ 2���� ��ŷ�� 2�� ��ϵǼ� �ּ� ó��.
          }
          
          
          
        }
      }catch(Exception e){
      }finally{
        try{
          bMan.remove(this);					//����ڰ� ������ �޼��� ����ڸ� �����ϰ�, �Է�,��½�Ʈ���� ������ ���ش�. 
          if(reader!=null) reader.close();
          if(writer!=null) writer.close();
          if(socket!=null) socket.close();
          reader=null; writer=null; socket=null;
          System.out.println(userName+"���� ������ �������ϴ�.");
          System.out.println("������ ��: "+bMan.size());
          // ����ڰ� ������ �������� ���� �濡 �˸���.
          bMan.sendToRoom(roomNumber,"[DISCONNECT]"+userName);
        }catch(Exception e){}
      }
    }
  }
  
  
  
  class BManager extends Vector{       // �޽����� �����ϴ� Ŭ����
    BManager(){}
    void add(Indian_Thread it){           // �����带 �߰��Ѵ�.
      super.add(it);
    }
    void remove(Indian_Thread it){        // �����带 �����Ѵ�.
       super.remove(it);
    }
    Indian_Thread getOT(int i){            // i��° �����带 ��ȯ�Ѵ�.
      return (Indian_Thread)elementAt(i);
    }
    Socket getSocket(int i){              // i��° �������� ������ ��ȯ�Ѵ�.
      return getOT(i).getSocket();
    }
 
    // i��° ������� ����� Ŭ���̾�Ʈ���� �޽����� �����Ѵ�.
    void sendTo(int i, String msg){
      try{
        PrintWriter pw= new PrintWriter(getSocket(i).getOutputStream(), true);
        pw.println(msg);
      }catch(Exception e){}  
    }
    int getRoomNumber(int i){            // i��° �������� �� ��ȣ�� ��ȯ�Ѵ�.
      return getOT(i).getRoomNumber();
    }
    synchronized boolean isFull(int roomNum){    // ���� á���� �˾ƺ���.
      if(roomNum==0)return false;                 // ������ ���� �ʴ´�.
 
      // �ٸ� ���� 2�� �̻� ������ �� ����.
      int count=0;
      for(int i=0;i<size();i++)
        if(roomNum==getRoomNumber(i))count++;
      if(count>=2)return true;
      return false;
    }
 
    // roomNum �濡 msg�� �����Ѵ�.
    void sendToRoom(int roomNum, String msg){
      for(int i=0;i<size();i++)
        if(roomNum==getRoomNumber(i))	// �����濡 �����鼭 �ڱ��ڽ��� �����Ѵ�.
          sendTo(i, msg);
    }
 
    // it�� ���� �濡 �ִ� �ٸ� ����ڿ��� msg�� �����Ѵ�.
    void sendToOthers(Indian_Thread it, String msg){
      for(int i=0;i<size();i++)
        if(getRoomNumber(i)==it.getRoomNumber() && getOT(i)!=it) // �����濡 �����鼭 �ڱ��ڽ��� �����Ѵ�.
          sendTo(i, msg);
    }
 
    // ������ ������ �غ� �Ǿ��°��� ��ȯ�Ѵ�.
    // �� ���� ����� ��� �غ�� �����̸� true�� ��ȯ�Ѵ�.
    synchronized boolean isReady(int roomNum){
      int count=0;
      for(int i=0;i<size();i++) {
        if(roomNum==getRoomNumber(i) && getOT(i).isReady())
          count++;
      }
      if(count==2)return true;
      return false;
    }
    
    //���帶�� ������ �����°��� ��ȯ�Ѵ�.
    //�� ���� ����ڰ� ��� ������ �ߴٸ� true�� ��ȯ�Ѵ�.
    synchronized boolean isBatting(int roomNum) {
    	int count=0;
    	for(int i=0;i<size();i++) {
    		if(roomNum==getRoomNumber(i) && getOT(i).isBatting())
    			count++;
    	}
    	if(count==2) return true;
    	return false;
    }
    
    //���帶�� ���ºη� �����°��� ��ȯ�Ѵ�.
    //�� ���� ����ڰ� ���ºθ� �ߴٸ� true�� ��ȯ�Ѵ�.
    synchronized boolean isDraw(int roomNum) {
    	int count=0;
    	for(int i=0;i<size();i++) {
    		if(roomNum==getRoomNumber(i) && getOT(i).isDraw())
    			count++;
    	}
    	if(count==2) return true;
    	return false;
    }
 
    // roomNum�濡 �ִ� ����ڵ��� �̸��� ��ȯ�Ѵ�.		
    String getNamesInRoom(int roomNum){
      StringBuffer sb=new StringBuffer("[PLAYERS]");
      for(int i=0;i<size();i++)
        if(roomNum==getRoomNumber(i))
          sb.append(getOT(i).getUserName()+"\t");
      return sb.toString();
    }
  }
}
