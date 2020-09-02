package indian;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextPane;
import java.awt.Color;

public class Rule extends JFrame {

	private JPanel contentPane;
	ImageIcon icon;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Rule frame = new Rule();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Rule() {
		icon = new ImageIcon(IndianClient.class.getResource("/img/subb.jpg"));
		
		setBounds(100, 100, 345, 600);
		contentPane = new JPanel() {
			 public void paintComponent(Graphics g) {
				 g.drawImage(icon.getImage(), 0, 0,  this.getWidth(), this.getHeight(), this);
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
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("\u2606\u2605 \uAC8C\uC784 \uB8F0 \u2606\u2605");
		lblNewLabel.setForeground(Color.WHITE);
		lblNewLabel.setFont(new Font("나눔손글씨 펜", Font.PLAIN, 20));
		lblNewLabel.setBounds(12, 10, 160, 40);
		contentPane.add(lblNewLabel);
		setResizable(false);
		
		JTextPane txtpnAd = new JTextPane() {
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
		txtpnAd.setForeground(Color.WHITE);
		txtpnAd.setFont(new Font("나눔손글씨 펜", Font.PLAIN, 18));
		
		txtpnAd.setText("1. \uAC01 \uD50C\uB808\uC774\uC5B4\uB294 \uB79C\uB364\uD558\uAC8C \uC11E\uC778 1\uBD80\uD130 10\uAE4C\uC9C0 \uC788\uB294 \uCE74\uB4DC \uB354\uBBF8\uB97C \uBC1B\uC2B5\uB2C8\uB2E4.\r\n\r\n2. \uCE74\uB4DC \uB354\uBBF8 \uC911 \uD55C \uC7A5\uC744 \uC624\uD508, \uC790\uC2E0\uC740 \uC624\uD508 \uB41C \uCE74\uB4DC\uC758 \uC218\uB97C \uC54C \uC218 \uC5C6\uC9C0\uB9CC \uC0C1\uB300\uBC29\uC758 \uC218\uB294 \uC54C \uC218 \uC788\uC2B5\uB2C8\uB2E4.\r\n\r\n3. \uC790\uC2E0\uC758 \uCE74\uB4DC\uAC00 \uBB34\uC5C7\uC778\uC9C0 \uC54C \uC218 \uC5C6\uAE30 \uB54C\uBB38\uC5D0 \uCC44\uD305\uC744 \uD1B5\uD55C \uC2E0\uACBD\uC804\uACFC \uCF54\uC778\uC744 \uC774\uC6A9\uD55C \uBE14\uB7EC\uD551\uC73C\uB85C \uACBD\uAE30\uAC00 \uC9C4\uD589\uB429\uB2C8\uB2E4.\r\n\r\n4. \uAC8C\uC784\uC740 9\uB77C\uC6B4\uB4DC\uB85C \uC9C4\uD589\uB418\uBA70 \uB9C8\uC9C0\uB9C9 \uB77C\uC6B4\uB4DC\uAC00 \uC885\uB8CC \uB41C \uB4A4 \uC2B9\uB9AC\uB97C \uB354 \uB9CE\uC774 \uCC59\uAE34 \uD50C\uB808\uC774\uC5B4\uAC00 \uC2B9\uB9AC\uD569\uB2C8\uB2E4.");
		txtpnAd.setBounds(12, 213, 325, 338);

		contentPane.add(txtpnAd);
		txtpnAd.setEditable(false);
		
	}
}
