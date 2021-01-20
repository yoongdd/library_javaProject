package login;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import MemberFrame.BasicFrameMember;
import manager.chat.ChatManager;
import manager.dao.MemberDAO;
import manager.dto.MemberDTO;
import managerFrame.BasicFrameManager;

public class Login extends JPanel implements ActionListener, KeyListener {
	public JPanel bigP, p1, p2, p3, p4;
	private JFrame findF1, findF2;
	private JTextField idT;
	private JPasswordField pwT;
	private JLabel loginL, findL, verifyL, signL;
	private JButton loginB, findID, findPW, signB;
	private int key;

	public void paintComponent(Graphics g) {
		Dimension d = getSize();
		ImageIcon img = new ImageIcon("Images/login.jpg");
		g.drawImage(img.getImage(), 0, 0, d.width, d.height, null);
	}

	public Login() {
		loginL = new JLabel("회원 로그인");

		p1 = new JPanel(new GridLayout(5, 1, 5, 10));
		p2 = new JPanel(new GridLayout(1, 2, 5, 10));
		p3 = new JPanel();
		p4 = new JPanel(new GridLayout(4, 1, 5, 10));

		p1.setBackground(Color.white);
		p2.setBackground(Color.white);
		p4.setBackground(Color.white);

		idT = new JTextField(20);
		pwT = new JPasswordField(20);

		findL = new JLabel("아이디 혹은 비밀번호를 잃어버렸나요?");
		signL = new JLabel("If you're not member");
		verifyL = new JLabel("");
		JLabel[] blankL = new JLabel[5];
		for (int i = 0; i < blankL.length; i++) {
			blankL[i] = new JLabel("");
		}

		loginB = new JButton("Login");
		findID = new JButton("Find ID");
		findPW = new JButton("Find PW");
		signB = new JButton("Join us");
		findID.setBackground(Color.white);
		findPW.setBackground(Color.white);
		loginB.setBackground(Color.white);
		signB.setBackground(Color.white);

		idT.setText("ID");
		pwT.setText("Password");
		pwT.setEchoChar((char) 0);

		p1.add(loginL);
		p1.add(idT);
		p1.add(pwT);
		p1.add(loginB);
		p1.add(findL);

		p2.add(findID);
		p2.add(findPW);

		p3.setLayout(null);

		p4.add(blankL[1]);
		p4.add(verifyL);
		p4.add(signL);
		p4.add(signB);

		add(p1);
		add(p2);
		add(p3);
		add(p4);

		idT.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				idT.setText("");
			}
		});
		pwT.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				pwT.setText("");
				pwT.setEchoChar('●');
			}
		});

		findID.addActionListener(this);
		findPW.addActionListener(this);
		signB.addActionListener(this);
		loginB.addActionListener(this);
		pwT.addKeyListener(this);
	}

	public void managerSet() {

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == findID) {
			new FindID();
		} else if (e.getSource() == findPW) {
			new FindPW();
		} else if (e.getSource() == signB) {
			new SignUp();
		} else if (e.getSource() == loginB) {

			List<MemberDTO> list = new ArrayList<MemberDTO>();
			list = new MemberDAO().getMemberList();

			String password = "";
			char[] pwd = pwT.getPassword();
			for (int i = 0; i < pwd.length; i++) {
				password += pwd[i];
			}

			if (list.size() != 0) {
				int i;
				for (i = 0; i < list.size(); i++) {

					if ((idT.getText().equals(list.get(i).getId()) && password.equals(list.get(i).getPassword()))) {
						JOptionPane.showMessageDialog(this, "로그인 성공");
						key = list.get(i).getSeq();

						if (list.get(i).getStatus() == 0)
							new BasicFrameMember(key);

						if (list.get(i).getStatus() == 1) {
							new BasicFrameManager();
							if (list.get(i).getId().equals("manager1")) {
								new ChatManager();
							}
						}
						break;
					}

				}
				if (i == list.size()) {
					JOptionPane.showMessageDialog(this, "아이디 혹은 비밀번호가 틀립니다.");
				}

			} else {
				JOptionPane.showMessageDialog(this, "먼저 회원가입을 해주세요.");
			}

		}
	}

	// 키 이벤트(엔터)
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			loginB.doClick();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	public static void main(String[] args) {
		new Login();
	}

}
