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


public class FindID extends JFrame implements ActionListener {
	JFrame frame;
	JTextField nameT;
	JLabel nameL;
	JLabel emailL;
	JTextField emailT;
	JButton findB, cancelB;
	JLabel title = new JLabel("");
	private MemberDAO memberDAO = new MemberDAO();
	public  List<MemberDTO> list = new ArrayList<MemberDTO>();


	FindID() {
		
		JPanel totalP = new JPanel() {
			public void paintComponent(Graphics g) {
				Dimension d = getSize();
				ImageIcon img = new ImageIcon("images/findID.jpg");
				g.drawImage(img.getImage(), 0, 0, d.width, d.height, null);
			}
		};
		setContentPane(totalP);
		setBounds(700, 200, 300, 250);
		setVisible(true);
		setResizable(false);
		setBackground(Color.white);

		totalP.setLayout(null);
		nameT = new JTextField();
		nameT.setBounds(127, 60, 116, 21);
		getContentPane().add(nameT);
		nameT.setColumns(10);


		emailT = new JTextField();
		emailT.setColumns(10);
		emailT.setBounds(127, 96, 116, 21);
		totalP.add(emailT);

		findB = new JButton("찾기");
		findB.setBounds(37, 140, 97, 39);
		findB.setBackground(Color.white);
		totalP.add(findB);

		cancelB = new JButton("취소");
		cancelB.setBounds(146, 140, 97, 39);
		cancelB.setBackground(Color.white);
		totalP.add(cancelB);

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
					if (nameT.getText().equals(list.get(i).getName()) && emailT.getText().equals(list.get(i).getEmail())) {
						JOptionPane.showMessageDialog(this, "회원님의 아이디는 [" + list.get(i).getId() + "] 입니다");
						return;
					}
				}//for
				JOptionPane.showMessageDialog(this, "등록된 회원정보가 없습니다.");	
			} else {
				JOptionPane.showMessageDialog(this, "등록된 회원정보가 없습니다.");
			}//size 체크(null아닐때)
		} else if (e.getSource() == cancelB) {
			dispose();
		}//버튼 선택 if

	}

}
