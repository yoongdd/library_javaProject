package managerFrame;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;

import manager.dao.LendDAO;
import manager.dto.LendJoinDTO;
import manager.dto.MemberDTO;

public class UserPage extends JFrame implements ActionListener {
	
	String driver = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@192.168.0.61:1521:xe";
	String username = "c##member";
	String passwordDB = "1234";
	Connection conn;
	PreparedStatement pstmt;
	ResultSet rs;

	final static String PASSWORD = "1234"; // 임시비번
	private JButton correctBtn;

	private DefaultTableModel model;
	private JTextField[] textField;
	private boolean fieldCheck = false;
	private boolean correctCheck = true;
	private LendDAO lendDAO = new LendDAO();// 추가
	public List<LendJoinDTO> list = new ArrayList<LendJoinDTO>();// 추가
	private final String code = "1234"; //관리인 코드

	private MemberDTO memberDTO;
	
	
	public void resizeColumnWidth(JTable table) {
		final TableColumnModel columnModel = table.getColumnModel();
		for (int column = 0; column < table.getColumnCount(); column++) {
			int width = 10; // 최소 가로길이
			for (int row = 0; row < table.getRowCount(); row++) {
				TableCellRenderer renderer = table.getCellRenderer(row, column);
				Component comp = table.prepareRenderer(renderer, row, column);
				width = Math.max(comp.getPreferredSize().width + 1, width);
			}
			columnModel.getColumn(column).setPreferredWidth(width);
		}
	}

	public UserPage(MemberDTO memberDTO) {
		
		this.memberDTO = memberDTO;
		
		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		getData();

		JPanel totalP = new JPanel() {
			public void paintComponent(Graphics g) {
				Dimension d = getSize();
				ImageIcon img = new ImageIcon("images/FrameMyPage.jpg");
				g.drawImage(img.getImage(), 0, 0, d.width, d.height, null);
			}
		};

		totalP.setLayout(null);

		textField = new JTextField[7];

		for (int i = 0; i < 7; i++) {
			textField[i] = new JTextField(12);
		}

		JLabel nameL = new JLabel("이름 :"); //이름 
		nameL.setBounds(118, 140, 40, 40);
		totalP.add(nameL);

		textField[0].setText(memberDTO.getName());
		textField[0].setBounds(170, 145, 144, 25);
		textField[0].setEditable(fieldCheck);
		totalP.add(textField[0]); // 이름 필드

		JLabel birthL = new JLabel("생년월일 :");
		birthL.setBounds(435, 140, 90, 40);
		totalP.add(birthL);

		textField[1].setText(memberDTO.getBirth());
		textField[1].setBounds(500, 145, 144, 25);
		textField[1].setColumns(12);
		textField[1].setEditable(fieldCheck);
		totalP.add(textField[1]);

		JLabel idL = new JLabel("아이디:");
		idL.setBounds(110, 190, 50, 40);
		totalP.add(idL);

		textField[2].setText(memberDTO.getId());
		textField[2].setBounds(170, 200, 144, 25);
		textField[2].setColumns(12);
		textField[2].setEditable(fieldCheck);
		totalP.add(textField[2]);

		JLabel emailL = new JLabel("이메일 :");
		emailL.setBounds(435, 190, 78, 25);
		totalP.add(emailL);

		textField[3].setText(memberDTO.getEmail());
		textField[3].setColumns(12);
		textField[3].setBounds(500, 190, 144, 25);
		textField[3].setEditable(fieldCheck);
		totalP.add(textField[3]);

		JLabel phoneL = new JLabel("핸드폰 번호 :"); //핸드폰 번호,
		phoneL.setBounds(86, 250, 92, 40);
		totalP.add(phoneL);

		textField[4].setText(memberDTO.getPhone());
		textField[4].setBounds(170, 255, 166, 25);
		textField[4].setColumns(12);
		textField[4].setEditable(fieldCheck);
		totalP.add(textField[4]);

		JLabel addressL = new JLabel("주소 : ");
		addressL.setBounds(123, 310, 57, 15);
		totalP.add(addressL);
		
		textField[5].setText(memberDTO.getAddress1());
		textField[5].setBounds(170, 310, 179, 21);
		textField[5].setColumns(15);
		textField[5].setEditable(fieldCheck);
		totalP.add(textField[5]);
		
		textField[6].setText(memberDTO.getAddress2());
		textField[6].setBounds(170, 340, 335, 21);
		textField[6].setColumns(10);
		textField[6].setEditable(fieldCheck);
		totalP.add(textField[6]);

		// ----------------------------------------------------- 내가 빌린책들 리스트 테이블

		Vector<String> v = new Vector<String>();
		v.add("도서코드");
		v.add("도서명");
		v.add("저자");
		v.add("출판사");
		v.add("장르");
		model = new DefaultTableModel(v, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		JTable table = new JTable(model);
		int code = 0;
		String anthor, publisher, genre;

		list = lendDAO.getLendJoin(memberDTO.getId());

		for (LendJoinDTO dto : list) { // 0518 수정
			Vector<String> v1 = new Vector<String>();
			v1.add(dto.getCode() + "");
			v1.add(dto.getTitle() + "");
			v1.add(dto.getAuthor() + "");
			v1.add(dto.getPublisher());
			v1.add(dto.getGenre());
			model.addRow(v1);

		}

		// JScrollPanedp table 추가
		JScrollPane scroll = new JScrollPane(table);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setBounds(100, 400, 600, 280);
		totalP.add(scroll);
		// ---------------------------------------------------- 하단- 수정, 문의 버튼
		correctBtn = new JButton("수정"); // 수정버튼
		correctBtn.setBounds(330, 700, 109, 39);
		totalP.add(correctBtn);

		// ----------------------------------------------

		setBounds(700, 100, 800, 800);
		setVisible(true);
		setResizable(false);
		setContentPane(totalP);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				dispose();
			}
		});
		resizeColumnWidth(table);

		// --------이벤트
		correctBtn.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (correctBtn == e.getSource()) {
			if (correctCheck == true) {
				String pwd = JOptionPane.showInputDialog(this, "직원 코드를 입력해주세요.", "회원 정보 수정",
						JOptionPane.INFORMATION_MESSAGE);
				if (pwd != null) {

					if (pwd.equals(code)) {

						for (int i = 0; i < 7; i++) {
							textField[i].setEditable(true);

						}
						textField[2].setEditable(false);
						correctBtn.setText("수정완료");
						correctCheck = false;
			

					} else if (!pwd.equals(PASSWORD)) {
						JOptionPane.showMessageDialog(this, "직원 코드가 틀렸습니다.", "회원 정보 수정",
								JOptionPane.INFORMATION_MESSAGE);
						correctCheck = true;

					}
				}

			} else {

				for (int i = 0; i < 7; i++) {
					textField[i].setEditable(false);
				}
				correctBtn.setText("수정");
				correctCheck = true;
				JOptionPane.showMessageDialog(this, "회원정보가 수정 되었습니다.", "회원 정보 수정", JOptionPane.INFORMATION_MESSAGE);// ===========================>>>>>0515

				update1();
				update2();
				update3();
				
				memberDTO.setName(textField[0].getText());
				memberDTO.setBirth(textField[1].getText());
				memberDTO.setEmail(textField[3].getText());
				memberDTO.setPhone(textField[4].getText());
				memberDTO.setAddress1(textField[5].getText());
				memberDTO.setAddress2(textField[6].getText());						
			}

		}
	}
	public void getConnection() {
		try {
			conn = DriverManager.getConnection(url, username, passwordDB);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}// getConnection()
	
	public void update1() { //이동0520
		System.out.println("update1");
		getConnection();
		String sql = "update member_table set name= ?,birth= ?,id = ?"
				+ ",email= ? where no_seq= ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, textField[0].getText());
			pstmt.setString(2, textField[1].getText());
			pstmt.setString(3, textField[2].getText());
			pstmt.setString(4, textField[3].getText());
			pstmt.setInt(5, memberDTO.getSeq()); 

			pstmt.executeUpdate();

		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e1) {

				e1.printStackTrace();
			}

		}
		
	} //update1()
	public void update2() { 
		getConnection();
		System.out.println("update2");

		String sql = "update memberextra_table set adress1= ?,adress2= ?,phone = ? where id= ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, textField[4].getText());
			pstmt.setString(2, textField[5].getText());
			pstmt.setString(3, textField[6].getText());
			pstmt.setString(4, memberDTO.getId());
		
			pstmt.executeUpdate();

		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e1) {

				e1.printStackTrace();
			}

		}
		
	}
	
	public void update3() {//lend table update
		getConnection();
		System.out.println("update3");

		String sql = "update lend_table set name= ?,email= ? where id= ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, textField[0].getText());
			pstmt.setString(2, textField[3].getText());
			pstmt.setString(3, memberDTO.getId());
		
			pstmt.executeUpdate();

		} catch (SQLException e1) {
			e1.printStackTrace();
		} finally {
			try {
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e1) {

				e1.printStackTrace();
			}

		}
		
	}
	
	public void getData() {
		
		getConnection();
		String sql = "select * from memberextra_table where id =?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, memberDTO.getId());

			rs = pstmt.executeQuery();

			while (rs.next()) {
				memberDTO.setAddress1(rs.getString("adress1"));
				memberDTO.setAddress2(rs.getString("adress2"));
				memberDTO.setPhone(rs.getString("phone"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {

			try {
				if (rs != null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (conn != null)
					conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		
		
	}

}
