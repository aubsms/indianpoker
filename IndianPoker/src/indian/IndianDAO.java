package indian;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class IndianDAO extends JFrame {

	private JPanel contentPane;
	private JTextArea textArea;
	private JButton btnNewButton;
	
	private Connection conn;
	PreparedStatement st = null;
	ResultSet rs = null;
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IndianDAO frame = new IndianDAO();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 * @throws SQLException 
	 */
	public IndianDAO() throws SQLException {
		
		ImageIcon icon = new ImageIcon(IndianClient.class.getResource("/img/rank.png"));
		
		setBounds(100, 100, 555, 489);
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
		this.setTitle("인디언 포커 랭킹");
		
		JLabel lblNewLabel = new JLabel("Ranking");
		lblNewLabel.setFont(new Font("나눔고딕", Font.BOLD, 30));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(169, 10, 177, 47);
		contentPane.add(lblNewLabel);
		
		JScrollPane scrollPane = new JScrollPane(){
			public void paintComponent(Graphics g) {
				 g.drawImage(null, 0, 0, null);
	                // Approach 2: Scale image to size of component
	                // Dimension d = getSize();
	                // g.drawImage(icon.getImage(), 0, 0, d.width, d.height, null);
	                // Approach 3: Fix the image position in the scroll pane
	                // Point p = scrollPane.getViewport().getViewPosition();
	                // g.drawImage(icon.getImage(), p.x, p.y, null);
	                setOpaque(false); //그림을 표시하게 설정,투명하게 조절
			 }
		};
		scrollPane.setBounds(12, 62, 515, 333);
		contentPane.add(scrollPane);
		
		textArea = new JTextArea() {
			public void paintComponent(Graphics g) {
				 g.drawImage(null, 0, 0, null);
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
		scrollPane.setViewportView(textArea);
		textArea.setEditable(true);
		
		btnNewButton = new JButton("\uB2EB\uAE30");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnNewButton.setBounds(220, 405, 97, 35);
		contentPane.add(btnNewButton);
		
		textArea.setText("");
		
		try {
			
			conn = new IndianDBConn().getConnection();
			
			ArrayList<User> aru = new ArrayList<User>();
			
			String sql = "select * from INDIANPOKER order by score desc"; 
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			String str1 = String.format("%20s%21s%21s%21s%21s%21s\n\n", "[ID]","[승]","[무]","[패]","[코인]","[점수]");
			str1 +=("   ======================================================================\n\n");
			while(rs.next()) {
				String id = rs.getString("ID");
				int win = rs.getInt("WIN");
				int draw = rs.getInt("DRAW");
				int lose = rs.getInt("LOSE");
				int coin = rs.getInt("COIN");
				int score = rs.getInt("SCORE");
				User rv1 = new User();
				aru.add(rv1);
				str1 += String.format("%20s%20s%20s%20s%20s%20s\n",id,win+"승",draw+"무",lose+"패",coin+"개",score+"점");
				
				textArea.setText(str1);
			}
			conn.close();	
		} catch(ClassNotFoundException e) {
			JOptionPane.showMessageDialog(null, "클래스 error");
		} catch(SQLException e){
			JOptionPane.showMessageDialog(null, "error");
		}
	}
	
	
	
	
}

