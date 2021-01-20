package login;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import manager.dao.MemberDAO;
import manager.dto.MemberDTO;

public class FindPW extends JFrame implements ActionListener {
	private JFrame frame;
	private JTextField idT;
	private JTextField emailT;
	JButton findB, cancelB;
	JLabel title = new JLabel("");
	private MemberDAO memberDAO = new MemberDAO();
	public List<MemberDTO> list = new ArrayList<MemberDTO>();

	FindPW() {

		JPanel totalP = new JPanel() {
			public void paintComponent(Graphics g) {
				Dimension d = getSize();
				ImageIcon img = new ImageIcon("images/findPW.jpg");
				g.drawImage(img.getImage(), 0, 0, d.width, d.height, null);
			}
		};

		setContentPane(totalP);
		setBounds(700, 200, 300, 250);
		getContentPane().setLayout(null);
		setVisible(true);
		setResizable(false);

		totalP.setLayout(null);

		idT = new JTextField();
		idT.setBounds(127, 60, 116, 21);
		getContentPane().add(idT);
		idT.setColumns(10);

		emailT = new JTextField();
		emailT.setColumns(10);
		emailT.setBounds(127, 96, 116, 21);
		totalP.add(emailT);

		findB = new JButton("찾기");
		findB.setBounds(37, 140, 97, 39);
		totalP.add(findB);

		cancelB = new JButton("취소");
		cancelB.setBounds(146, 140, 97, 39);
		totalP.add(cancelB);
		findB.setBackground(Color.white);
		cancelB.setBackground(Color.white);
		
		findB.addActionListener(this);
		cancelB.addActionListener(this);

		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				setVisible(false);
			}
		});

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		list = memberDAO.getMemberList();
		if (e.getSource() == findB) {

			if (list.size() != 0) {
				for (int i = 0; i < list.size(); i++) {
					if (idT.getText().equals(list.get(i).getId()) && emailT.getText().equals(list.get(i).getEmail())) {

						new LoginSendMail().findPWMail(list.get(i).getId(), list.get(i).getEmail(),
								list.get(i).getPassword());
						JOptionPane.showMessageDialog(this, "비밀번호을 해당 이메일로 발송하였습니다.");
						dispose();
						return;

					}
				}
				JOptionPane.showMessageDialog(this, "등록된 회원정보가 없습니다.");
			} else {
				JOptionPane.showMessageDialog(this, "등록된 회원정보가 없습니다.");
			} // 같은 비밀번호 찾기 if
		} else if (e.getSource() == cancelB) {
			dispose();
		} // 버튼 선택 if

	}

}
