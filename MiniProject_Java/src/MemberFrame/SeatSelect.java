package MemberFrame;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JToggleButton;

import manager.dto.LendJoinDTO;
import member.dao.SeatDAO;
import member.dto.PersonDTO;
import member.dto.SeatDTO;

public class SeatSelect extends JPanel implements ActionListener {

	private JLabel infoL;
	private JToggleButton[] seatBtn;
	private ButtonGroup group;
	private int indexOfButton = -1;
	private boolean sameSeatChecked = true;
	private JButton enterBtn, exitBtn, reBtn;
	private JToggleButton selectedBtn;

	private SeatDAO seatDAO = new SeatDAO();


	private PersonDTO personDTO;
	
	public void paintComponent(Graphics g) {
		Dimension d = getSize();
		ImageIcon img = new ImageIcon("images/seatSelect.jpg");
		g.drawImage(img.getImage(), 0, 0, d.width, d.height, null);
	}

	public SeatSelect() {
		MyPage page = new MyPage();
		personDTO = page.getPersonDTO();
		infoL = new JLabel();

		JPanel seatP = new JPanel(new GridLayout(6, 4, 5, 40));
		seatP.setBackground(new Color(0, 0, 0, 0));
		seatP.setBounds(150, 150, 500, 500);

		seatBtn = new JToggleButton[24];
		group = new ButtonGroup();

		for (int i = 0; i < 24; i++) {

			seatBtn[i] = new JToggleButton(i + 1 + "", new ImageIcon("images/Btn1.png"), false);
			seatBtn[i].setHorizontalTextPosition(JToggleButton.CENTER);
			seatBtn[i].setFont(new Font("맑은고딕", Font.BOLD, 22));
			seatBtn[i].setForeground(new Color(203, 225, 152));
			seatBtn[i].setBorderPainted(false);
			seatP.add(seatBtn[i]);
			group.add(seatBtn[i]);
		}

		// 현재 입실 중인 사람들 나옴
		List<SeatDTO> arlist = seatDAO.getSeatList();

		for (int i = 0; i < arlist.size(); i++) {

			int a = arlist.get(i).getSeat_num();
			seatBtn[a].setForeground(Color.white);
			if (arlist.get(i).getId().equals(personDTO.getId())) {
				personDTO.setSeatNum(arlist.get(i).getSeat_num());

			} else {
				seatBtn[a].setEnabled(false);
				if (arlist.get(i).getSeat_check() == 0) { // 남자
					seatBtn[a].setDisabledIcon(new ImageIcon("images/btn2.png"));
				} else {
					seatBtn[a].setDisabledIcon(new ImageIcon("images/btn3.png"));
				}
			}

		}

		if (personDTO.getSeatNum() != -1) {
			seatBtn[personDTO.getSeatNum()].setIcon(new ImageIcon("images/btn4.png"));
			seatBtn[personDTO.getSeatNum()].setForeground(Color.white);
			seatBtn[personDTO.getSeatNum()].setEnabled(true);
		}
		// ----------------------------------------------------------
		JPanel panel01 = new JPanel();
		panel01.setLayout(null);
		panel01.setBounds(830, 120, 250, 600); // 수정
		panel01.setOpaque(false);

		infoL.setText(arlist.size() + " /24명 입실중 입니다.");

		infoL.setFont(new Font("맑은고딕", Font.PLAIN, 20));
		infoL.setBounds(20, 101, 221, 49);
		infoL.setOpaque(false);
		panel01.add(infoL);

		// --------------------------------입실 퇴실 버튼
		JPanel btnP = new JPanel(new GridLayout(1, 2, 0, 0));

		enterBtn = new JButton("입실", new ImageIcon("images/btn5.png"));
		enterBtn.setHorizontalTextPosition(JButton.CENTER); // 텍스트 가운데
		enterBtn.setBorderPainted(false);
		enterBtn.setFont(new Font("맑은고딕", Font.BOLD, 30));
		enterBtn.setForeground(new Color(181, 230, 29));
		enterBtn.setOpaque(false);
		btnP.add(enterBtn);

		exitBtn = new JButton("퇴실", new ImageIcon("images/btn5.png"));
		exitBtn.setHorizontalTextPosition(JButton.CENTER); // 텍스트 가운데
		exitBtn.setBorderPainted(false);
		exitBtn.setFont(new Font("맑은고딕", Font.BOLD, 30));
		exitBtn.setForeground(new Color(181, 230, 29));
		exitBtn.setOpaque(false);
		btnP.add(exitBtn);

		btnP.setBounds(0, 250, 235, 200);
		panel01.add(btnP);

		// ------------------------------새로고침 버튼

		reBtn = new JButton("새로고침", new ImageIcon("images/btn1.png"));
		reBtn.setHorizontalTextPosition(JButton.CENTER); // 텍스트 가운데
		reBtn.setBorderPainted(false);
		reBtn.setFont(new Font("맑은고딕", Font.BOLD, 20));
		reBtn.setForeground(new Color(181, 230, 29));
		reBtn.setBounds(110, 500, 120, 50);
		panel01.add(reBtn);
		reBtn.setVisible(false);
		// -------------------------------------------------

		setLayout(null);
		add("East", panel01);
		add("Center", seatP);
		

		for (int i = 0; i < seatBtn.length; i++) {
			seatBtn[i].addActionListener(this);
		}
		enterBtn.addActionListener(this);
		exitBtn.addActionListener(this);
		reBtn.addActionListener(this);
		int sleepSec = 5;

		ScheduledThreadPoolExecutor stpe = new ScheduledThreadPoolExecutor(1);
		stpe.scheduleAtFixedRate(new Runnable() {
			public void run() {
				refresh();
			}
		}, 0, sleepSec, TimeUnit.SECONDS);
	}

	@Override
	public void actionPerformed(ActionEvent e) { 
		JToggleButton btn = null;
		JButton btn2 = null;

		if (e.getActionCommand() == "입실" || e.getActionCommand() == "퇴실") {
			btn2 = (JButton) e.getSource();

			if (personDTO.getSeatNum() == -1) { // 만약 입실이 안되어있을때,
				if ("퇴실" == e.getActionCommand()) { // 퇴실을 누르면
					JOptionPane.showMessageDialog(this, "입실이 안되셨습니다. 좌석을 선택해주세요.", "좌석선택",
							JOptionPane.INFORMATION_MESSAGE);
				} else if ("입실" == e.getActionCommand()) { // 입실을 눌렀을때...

					if (selectedBtn == null || selectedBtn.isSelected() == false) {
						JOptionPane.showMessageDialog(this, "앉으실 좌석을 선택해주세요.", "좌석선택", JOptionPane.INFORMATION_MESSAGE);
					} else if (selectedBtn.isSelected() == true) { // 좌석을 선택했으면
						JToggleButton tempbtn = selectedBtn;
						if (checkSeat(Integer.parseInt(tempbtn.getText()) - 1)) {
							
							if(personDTO.getState() == 1) {
								JOptionPane.showMessageDialog(this, "연체자는 입실할 수 없습니다.", "좌석선택",
										JOptionPane.INFORMATION_MESSAGE);
							}else {
							
								personDTO.setSeatNum(Integer.parseInt(tempbtn.getText()) - 1);
								String id = personDTO.getId();
								int seatNum = personDTO.getSeatNum();
								int sex = personDTO.getSex();

								SeatDTO seatDTO = new SeatDTO();
								seatDTO.setId(id);
								seatDTO.setSeat_num(seatNum);
								seatDTO.setSeat_check(sex);

								int su = seatDAO.seatWrite(seatDTO);
								JOptionPane.showMessageDialog(this, "좌석이 예약 되셨습니다.", "좌석선택",
										JOptionPane.INFORMATION_MESSAGE);
								System.out.println(personDTO.getSeatNum());
							}
							
							
							
						} else {
							JOptionPane.showMessageDialog(this, "이미 선택된 좌석입니다.", "좌석선택",
									JOptionPane.INFORMATION_MESSAGE);
						}
						refresh();

					}
				}
			} else { // 입실이 되어있을때.....
				if ("입실" == e.getActionCommand()) { // 입실을 누르면

					JOptionPane.showMessageDialog(this, "이미 입실 되셨습니다.", "좌석선택", JOptionPane.INFORMATION_MESSAGE);

				} else if ("퇴실" == e.getActionCommand()) { // 퇴실을 눌렀을때...
					JOptionPane.showMessageDialog(this, "퇴실 되셨습니다.", "좌석선택", JOptionPane.INFORMATION_MESSAGE);
					seatBtn[personDTO.getSeatNum()].setIcon(new ImageIcon("images/btn1.png"));
					seatBtn[personDTO.getSeatNum()].setForeground(new Color(203, 225, 152));
					personDTO.setSeatNum(-1);
					
					int su = 0;
					String id = personDTO.getId();

					List<SeatDTO> arlist = seatDAO.getSeatList();
					System.out.println("list 사이즈 = " + arlist.size());

					for (int i = 0; i < arlist.size(); i++) {
						SeatDTO seatDTO = arlist.get(i);

						if (seatDTO.getId().equals(id)) {
							su = seatDAO.seatDelete(seatDTO);
						}
					}
					refresh();

				}

			}

		} else if (reBtn == e.getSource()) {// 새로고침
			refresh();
		} else { // 좌석버튼을 누른 경우

			if (personDTO.getSeatNum() != -1) { // 입실이 되어있는데 좌석버튼을 눌렀을 경우
				JOptionPane.showMessageDialog(this, "퇴실을 먼저해주세요.", "좌석선택", JOptionPane.INFORMATION_MESSAGE);

			} else {
				btn = (JToggleButton) e.getSource();
				selectedBtn = btn;

				if (btn.isSelected()) { // 버튼이 눌렸을때, true
					btn.setIcon(new ImageIcon("images/btn4.png"));
					btn.setForeground(Color.white);
				}

				if (indexOfButton != -1) {
					if (indexOfButton == Integer.parseInt(btn.getText()) - 1) {
						if (sameSeatChecked == true) {
							btn.setIcon(new ImageIcon("images/btn1.png"));
							btn.setForeground(new Color(203, 225, 152));
							selectedBtn = null;
							sameSeatChecked = false;
						} else {
							btn.setIcon(new ImageIcon("images/btn4.png"));
							btn.setForeground(Color.white);
							sameSeatChecked = true;

						}

					} else {
						sameSeatChecked = true;
						seatBtn[indexOfButton].setSelected(false);
						seatBtn[indexOfButton].setIcon(new ImageIcon("images/btn1.png"));
						seatBtn[indexOfButton].setForeground(new Color(203, 225, 152));
					}

				}

				indexOfButton = Integer.parseInt(btn.getText()) - 1;

			}

		}
		System.out.println("선택된 좌석 : " + indexOfButton);
		System.out.println("예약/취소 된 좌석 :" + personDTO.getSeatNum());
	} // actionPerformed()

	public void refresh() {

		for (int i = 0; i < 24; i++) {
			seatBtn[i].setEnabled(true);
			seatBtn[i].setIcon(new ImageIcon("images/btn1.png"));
			seatBtn[i].setForeground(new Color(203, 225, 152));
		}

		List<SeatDTO> arlist;
		arlist = seatDAO.getSeatList();

		for (int i = 0; i < arlist.size(); i++) {
			int a = arlist.get(i).getSeat_num();
			seatBtn[a].setForeground(Color.white);
			if (arlist.get(i).getId().equals(personDTO.getId())) {
				personDTO.setSeatNum(arlist.get(i).getSeat_num());

			} else {
				seatBtn[a].setEnabled(false);
				if (arlist.get(i).getSeat_check() == 0) { // 남자
					seatBtn[a].setDisabledIcon(new ImageIcon("images/btn2.png"));
				} else {
					seatBtn[a].setDisabledIcon(new ImageIcon("images/btn3.png"));
				}
			}

		}

		if (personDTO.getSeatNum() != -1) {
			seatBtn[personDTO.getSeatNum()].setIcon(new ImageIcon("images/btn4.png"));
			seatBtn[personDTO.getSeatNum()].setForeground(Color.white);
			seatBtn[personDTO.getSeatNum()].setEnabled(true);

		}
		infoL.setText(arlist.size() + " /24명 입실중 입니다.");
		infoL.setBackground(Color.WHITE);
		

		selectedBtn = null;
		sameSeatChecked = false;
		indexOfButton = -1;

	} //refresh()

	public Boolean checkSeat(int seatNum) {
		boolean selected = true;
		List<SeatDTO> arlist;
		arlist = seatDAO.getSeatList();

		for (int i = 0; i < arlist.size(); i++) {
			int a = arlist.get(i).getSeat_num();
			if (a == seatNum) {
				selected = false;
			}

		}
		return selected;

	} //checkSeat()

	

}
