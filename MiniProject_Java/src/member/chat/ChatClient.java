package member.chat;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import MemberFrame.MyPage;
import member.dto.PersonDTO;

import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ChatClient extends JFrame implements ActionListener, Runnable {
	private JTextArea output;
	private JTextField input;
	private JButton sendBtn;
	private Socket socket;
	private BufferedReader reader;
	private PrintWriter writer;
	
	String driver = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@192.168.0.61:1521:xe";
	String username = "c##member";
	String passwordDB = "1234";
	Connection conn;
	PreparedStatement pstmt;
	
	private PersonDTO personDTO;

	public ChatClient() {
		MyPage mypage = new MyPage();
		
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		
		this.setTitle("사용자 채팅");
		System.out.println("사용자채팅");
		personDTO = mypage.getPersonDTO();
		output = new JTextArea();
		output.setFont(new Font("돋움체", Font.BOLD, 16));
		output.setEditable(false);
		JScrollPane scroll = new JScrollPane(output);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		input = new JTextField();
		sendBtn = new JButton("보내기");

		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.add("Center", input);
		p.add("East", sendBtn);

		Container c = this.getContentPane();
		c.add("Center", scroll);
		c.add("South", p);

		setBounds(700, 200, 300, 300);
		setVisible(true);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				writer.println("exit");
				writer.flush();
			}
		});
	}
	public void getConnection() {
		try {
			conn = DriverManager.getConnection(url, username, passwordDB);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}// getConnection()

	public void service() {
		// 서버IP
		String serverIP = "192.168.0.65";
		if (serverIP == null || serverIP.length() == 0) {
			System.out.println("서버IP가 입력되지 않았습니다");
			System.exit(0);
		}

		// 닉네임
		String nickName = personDTO.getId();

		try {
			// 소켓 생성
			socket = new Socket(serverIP, 9500);

			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

		} catch (UnknownHostException e) {
			System.out.println("서버를 찾을 수 없습니다");
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			System.out.println("서버와 연결이 안되었습니다");
			e.printStackTrace();
			System.exit(0);
		}

		// 서버로 닉네임 보내기
		writer.println(nickName);
		writer.flush();

		Thread t = new Thread(this);// 스레드 생성
		t.start();// 스레드 시작 - 스레드 실행

		// 이벤트
		input.addActionListener(this);
		sendBtn.addActionListener(this);
	}

	@Override
	public void run() {
		// 서버로부터 받기
		String line = null;

		while (true) {
			try {
				line = reader.readLine();

				if (line == null || line.equals("exit")) {
					
					getConnection();
					String sql = "update ask_table set complited = ? where id = ?"; 

					try {
						pstmt = conn.prepareStatement(sql);
						pstmt.setInt(1, 1);
						pstmt.setString(2, personDTO.getId()); 

						pstmt.executeUpdate();

					} catch (SQLException e) {
						e.printStackTrace();
					} finally {
						try {
							if (pstmt != null)
								pstmt.close();
							if (conn != null)
								conn.close();
						} catch (SQLException e) {
							e.printStackTrace();
						}
					}
					
					dispose();
					break;

				}

				output.append(line + "\n");

				int pos = output.getText().length();
				output.setCaretPosition(pos);

			} catch (IOException e) {
				e.printStackTrace();
			}
		} // while
		
		try {
			reader.close();
			writer.close();
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// 서버로 보내기
		String data = input.getText();
		writer.println(data);
		writer.flush();
		input.setText("");
	}

}
