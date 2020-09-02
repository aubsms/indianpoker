package indian;

public class User {
	
	private String username;
	private int myCoin;
	private int[] myCardNum;
	private int batCoin;
	private int win;
	private int draw;
	private int lose;
	
	
	public User() {
		this.username = "";
		this.myCoin = 0;
		this.myCardNum = new int[10];
		this.batCoin = 0;
		this.win = 0;
		this.draw = 0;
		this.lose =0;
		
	}
	
	public int getWin() {
		return win;
	}

	public void setWin(int win) {
		this.win = win;
	}


	public int getDraw() {
		return draw;
	}


	public void setDraw(int draw) {
		this.draw = draw;
	}

	public int getLose() {
		return lose;
	}

	public void setLose(int lose) {
		this.lose = lose;
	}

	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public int getMyCoin() {
		return myCoin;
	}
	public void setMyCoin(int myCoin) {
		this.myCoin = myCoin;
	}
	public int[] getMyCardNum() {
		return myCardNum;
	}
	public void setMyCardNum(int[] myCardNum) {
		this.myCardNum = myCardNum;
	}
	public int getBatCoin() {
		return batCoin;
	}
	public void setBatCoin(int batCoin) {
		this.batCoin = batCoin;
	}
	
	
}
