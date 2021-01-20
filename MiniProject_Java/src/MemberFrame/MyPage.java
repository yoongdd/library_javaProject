package MemberFrame;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import manager.dao.LendDAO;
import manager.dao.MemberDAO;
import manager.dto.LendDTO;
import manager.dto.LendJoinDTO;
import manager.dto.MemberDTO;
import member.chat.ChatClient;
import member.dto.PersonDTO;

public class MyPage extends JPanel implements ActionListener {
	String driver = "oracle.jdbc.driver.OracleDriver";
	String url = "jdbc:oracle:thin:@192.168.0.61:1521:xe";
	String username = "c##member";
	String passwordDB = "1234";
	Connection conn;
	PreparedStatement pstmt;
	ResultSet rs;

	private JButton correctBtn;
	private JButton askBtn;
	private JButton withdrawalBtn;
	private DefaultTableModel model;
	private JTextField[] textField;
	private JPasswordField passwordF;
	private boolean fieldCheck = false;
	private boolean correctCheck = true;
	private BasicFrameMember bm;
	private PersonDTO personDTO;
	private String password;
	private MemberDAO memberDAO = new MemberDAO();
	private LendDAO lendDAO = new LendDAO();
	public List<LendJoinDTO> list = new ArrayList<LendJoinDTO>();

	public void paintComponent(Graphics g) {
		Dimension d = getSize();
		ImageIcon img = new ImageIcon("Images/MyPage.jpg"); // 0518 수정
		g.drawImage(img.getImage(), 0, 0, d.width, d.height, null);
	} // paintComponent() ,바탕화면 이미지 설정

	public MyPage() {

		try {
			Class.forName(driver);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		this.setLayout(null);

		JPanel totalP = new JPanel();
		totalP.setBounds(120, 150, 700, 140);
		add(totalP);
		totalP.setLayout(new GridLayout(3, 2, 0, 0));

		JPanel nameP = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		totalP.add(nameP);

		textField = new JTextField[8];

		for (int i = 0; i < 7; i++) {
			textField[i] = new JTextField(12);
		}

		JLabel nameL = new JLabel("이름 :");
		nameP.add(nameL);

		nameP.add(textField[0]); // 이름 필드
		textField[0].setSize(144, 34);
		textField[0].setEditable(fieldCheck);

		JPanel birthP = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		totalP.add(birthP);

		JLabel birthL = new JLabel("생년월일 :");
		birthP.add(birthL);
		birthP.add(textField[1]);
		textField[1].setColumns(12);
		textField[1].setEditable(fieldCheck);

		JPanel idP = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		totalP.add(idP);

		JLabel idL = new JLabel("아이디 :");
		idP.add(idL);
		idP.add(textField[2]);
		textField[2].setColumns(12);
		textField[2].setEditable(false);

		JPanel emaliP = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		totalP.add(emaliP);

		JLabel emailL = new JLabel("이메일 :");
		emaliP.add(emailL);
		textField[3].setColumns(12);
		emaliP.add(textField[3]);
		textField[3].setEditable(fieldCheck);

		JPanel phoneP = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		totalP.add(phoneP);

		JLabel phoneL = new JLabel("핸드폰 번호 :");
		phoneP.add(phoneL);
		phoneP.add(textField[4]);
		textField[4].setColumns(12);
		textField[4].setEditable(fieldCheck);

		JPanel passwordP = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		passwordP.setBackground(Color.white);
		totalP.add(passwordP);

		JLabel passwordL = new JLabel("비밀번호 :");
		passwordP.add(passwordL);
		passwordF = new JPasswordField(12);
		passwordP.add(passwordF);
		passwordF.setEditable(fieldCheck);

		JPanel addressP = new JPanel(new FlowLayout(FlowLayout.LEFT));
		addressP.setBounds(253, 280, 600, 70);
		add(addressP);
		addressP.setLayout(null);

		JLabel addressL = new JLabel("주소 :");
		addressL.setBounds(10, 10, 60, 20);
		addressL.setHorizontalAlignment(JLabel.RIGHT);
		addressP.add(addressL);

		textField[5].setBounds(75, 10, 180, 20);
		addressP.add(textField[5]);
		textField[5].setColumns(15);
		textField[5].setEditable(fieldCheck);

		textField[6].setBounds(75, 50, 335, 20);
		addressP.add(textField[6]);
		textField[6].setColumns(10);
		textField[6].setEditable(fieldCheck);

		totalP.setOpaque(false);
		emaliP.setOpaque(false);
		birthP.setOpaque(false);
		idP.setOpaque(false);
		phoneP.setOpaque(false);
		nameP.setOpaque(false);
		addressP.setOpaque(false);
		// ----------------------------------------------------- 내가 빌린책들 리스트 테이블

		Vector<String> v = new Vector<String>();
		v.add("도서코드");
		v.add("도서명");
		v.add("저자");
		v.add("출판사");
		v.add("장르");
		v.add("반납일");
		model = new DefaultTableModel(v, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};
		JTable table = new JTable(model);
		Date today = new Date();

		// table sorter
		RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
		table.setRowSorter(sorter);
		for (LendJoinDTO dto : list) { // 0519수정\
			Vector<String> v1 = new Vector<String>();
			long diffDay = (dto.getReturnDate().getTime() - today.getTime()) / (24 * 60 * 60 * 1000); // 0515날짜 계산안됨
			v1.add(dto.getCode() + "");
			v1.add(dto.getTitle() + "");
			v1.add(dto.getAuthor() + "");
			v1.add(dto.getPublisher());
			v1.add(dto.getGenre());
			if (today.after(dto.getReturnDate())) {
				JOptionPane.showMessageDialog(null, dto.getTitle() + "반납일 " + (diffDay * -1) + "일 초과");
			} else {
				v1.add(dto.getReturnDate() + "");
			}
			model.addRow(v1);
		}
		int code = 0;
		String anthor, publisher, genre;

		// JScrollPanedp table 추가
		JScrollPane scroll = new JScrollPane(table);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		scroll.setBounds(200, 380, 700, 280);
		add(scroll);

		// ---------------------------------------------------- 하단- 수정, 문의 버튼
		correctBtn = new JButton("수정"); // 수정버튼
		correctBtn.setBounds(755, 336, 65, 30);
		correctBtn.setBackground(new Color(255, 255, 255));
		add(correctBtn);

		withdrawalBtn = new JButton("탈퇴"); // 수정버튼
		withdrawalBtn.setBounds(840, 336, 65, 30);
		withdrawalBtn.setBackground(new Color(255, 255, 255));
		add(withdrawalBtn);
		withdrawalBtn.setVisible(false);
		askBtn = new JButton("문의"); // 문의버튼
		askBtn.setBounds(495, 670, 109, 39);
		add(askBtn);

		// ----------------------------------------------
		getData();
		getExtraData(); 
		getData2();
		setData();

		password = personDTO.getPassword();

		withdrawalBtn.addActionListener(this);
		correctBtn.addActionListener(this);
		askBtn.addActionListener(this);

		int sleepSec = 1;

		ScheduledThreadPoolExecutor stpe = new ScheduledThreadPoolExecutor(1);
		stpe.scheduleAtFixedRate(new Runnable() {
			public void run() {
				list = lendDAO.getLendJoin(textField[2].getText());
				model.setRowCount(0);
				for (LendJoinDTO dto : list) { // 0519수정
					Vector<String> v1 = new Vector<String>();
					long diffDay = (dto.getReturnDate().getTime() - today.getTime()) / (24 * 60 * 60 * 1000); // 0515날짜
																											
					v1.add(dto.getCode() + "");
					v1.add(dto.getTitle() + "");
					v1.add(dto.getAuthor() + "");
					v1.add(dto.getPublisher());
					v1.add(dto.getGenre());
					if (today.after(dto.getReturnDate())) {
						v1.add("반납일 " + (diffDay * -1) + "일 초과");
					} else {
						v1.add(dto.getReturnDate() + "");
					}
					model.addRow(v1);
				}
				resizeColumnWidth(table);
			}
		}, 0, sleepSec, TimeUnit.SECONDS);

	}// MyPage()

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

	@Override
	public void actionPerformed(ActionEvent e) {
		if (correctBtn == e.getSource()) {
			if (correctCheck == true) {
				String pwd = JOptionPane.showInputDialog(this, "비밀번호를 입력해주세요.", "회원 정보 수정",
						JOptionPane.INFORMATION_MESSAGE);
				if (pwd != null) {

					if (pwd.equals(password)) {
						fieldCheck = true;

						for (int i = 0; i < 7; i++) {
							textField[i].setEditable(fieldCheck);
						}
						textField[2].setEditable(false);
						passwordF.setEditable(true);
						passwordF.setEchoChar((char) 0);

						withdrawalBtn.setVisible(true);
						correctBtn.setText("완료");
						correctCheck = false;

					} else if (!pwd.equals(password)) {
						JOptionPane.showMessageDialog(this, "비밀번호가 틀렸습니다.", "회원 정보 수정",
								JOptionPane.INFORMATION_MESSAGE);
						correctCheck = true;
					}
				}

			} else {

				fieldCheck = false;

				for (int i = 0; i < 7; i++) {
					textField[i].setEditable(fieldCheck);

				}
				passwordF.setEditable(fieldCheck);
				passwordF.setEchoChar('●');
				JOptionPane.showMessageDialog(this, "회원정보가 수정 되었습니다.", "회원 정보 수정", JOptionPane.INFORMATION_MESSAGE);// ===========================>>>>>0515

				withdrawalBtn.setVisible(false); // 메세지추가
				correctBtn.setText("수정");
				correctCheck = true;

				char[] pwdAr = passwordF.getPassword();
				password = "";
				for (int i = 0; i < pwdAr.length; i++) {
					password += pwdAr[i];
				}
				personDTO.setPassword(password);
				update1();
				update2();

			}

		} else if (askBtn == e.getSource()) {
			getConnection();
			String sql = "insert into ask_table(id, check_id,complited) values (?,?,?)";

			try {
				pstmt = conn.prepareStatement(sql);
				pstmt.setString(1, textField[2].getText());
				pstmt.setInt(2, 0);
				pstmt.setInt(3, 0);

				pstmt.executeUpdate();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} finally {

				try {

					if (pstmt != null)
						pstmt.close();
					if (conn != null)
						conn.close();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
			}

			ChatClient chat = new ChatClient();
			chat.service();

		} else if (e.getSource() == withdrawalBtn) {
			list = lendDAO.getLendJoin(textField[2].getText());
			System.out.println(list.size());

			if (list.size() != 0) {
				JOptionPane.showMessageDialog(null, "반납하지 않은 책이 남아있습니다.");
				return;
			}
			
			// 회원리스트 삭제
			int result = JOptionPane.showConfirmDialog(null, "탈퇴하시겠습니까?", "회원정보 삭제", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.WARNING_MESSAGE, null);
			if (result == JOptionPane.YES_OPTION) {
				List<MemberDTO> list = new ArrayList<MemberDTO>();
				list = memberDAO.getMemberList();
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getName().equals(textField[0].getText())) {
						MemberDTO memberDTO = list.get(i);
						MemberDAO memberDAO = new MemberDAO();
						memberDAO.deleteMember(memberDTO);
						System.exit(0);
					}
				}

			}
		}

	}// ActionPerformed()

	public void getConnection() {
		try {
			conn = DriverManager.getConnection(url, username, passwordDB);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}// getConnection()

	public void getData() {
		bm = new BasicFrameMember();

		getConnection();
		String sql = "select * from member_table where no_seq =?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, bm.getKey());

			rs = pstmt.executeQuery();

			while (rs.next()) {

				personDTO = new PersonDTO(rs.getString("id"), rs.getString("password"), rs.getString("name"),
						rs.getString("birth"), rs.getString("email"), rs.getInt("sex"));
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
	} // getData()

	public void getExtraData() { 
		getConnection();
		String sql = "select * from memberextra_table where id =?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, personDTO.getId());

			rs = pstmt.executeQuery();

			while (rs.next()) {
				personDTO.setAddress1(rs.getString("adress1"));
				personDTO.setAddress2(rs.getString("adress2"));
				personDTO.setPhoneNum(rs.getString("phone"));
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

	} // getExtraData()

	public void getData2() {// ----추가 0521
		List<LendDTO> lendList = lendDAO.getLendList();

		for (LendDTO lendDTO : lendList) {
			if (personDTO.getId().equals(lendDTO.getId())) {
				personDTO.setState(lendDTO.getState());
			}

		}
	}

	public void setData() { 
		textField[0].setText(personDTO.getName());
		textField[1].setText(personDTO.getBirth());
		textField[2].setText(personDTO.getId());
		textField[3].setText(personDTO.getEmail());
		passwordF.setText(personDTO.getPassword());
		textField[4].setText(personDTO.getPhoneNum());
		textField[5].setText(personDTO.getAddress1());
		textField[6].setText(personDTO.getAddress2());

	} // setData()

	public void update1() { // 이동0520
		getConnection();
		String sql = "update member_table set name= ?,birth= ?,id = ?" + ",email= ?,password= ? where no_seq= ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, textField[0].getText());
			pstmt.setString(2, textField[1].getText());
			pstmt.setString(3, textField[2].getText());
			pstmt.setString(4, textField[3].getText());
			pstmt.setString(5, password);
			pstmt.setInt(6, bm.getKey());

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

	} // update1()

	public void update2() { 
		getConnection();
		String sql = "update memberextra_table set adress1= ?,adress2= ?,phone = ? where id= ?";
		try {
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, textField[5].getText());
			pstmt.setString(2, textField[6].getText());
			pstmt.setString(3, textField[4].getText());
			pstmt.setString(4, personDTO.getId());

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

	public PersonDTO getPersonDTO() {
		return personDTO;
	}// getPersonDTO()

}
