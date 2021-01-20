package managerFrame;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.RowSorter;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import java.util.Date;

import manager.dao.LendDAO;
import manager.dao.ReportDAO;
import manager.dto.LendDTO;
import manager.dto.MemberDTO;
import manager.dto.ReportDTO;

public class BlackList extends JPanel implements ActionListener{
	private JButton sendBtn, sendAllBtn, updateBtn;

	private JLabel titleL;

	private JComboBox<String> combo;
	private DefaultTableModel model;
	private JTable table;
	private Date date;
	private JPanel p, p1, p2, p3, p4, labelP, btnP;
	private LendDAO lendDAO = new LendDAO();//추가
	public List<LendDTO> list = new ArrayList<LendDTO>();//추가
	public static int overCount;
	private BlackSendMail blackSendMail = new BlackSendMail();
	public Date today;
	private ReportDAO reportDAO = new ReportDAO();

	public void paintComponent(Graphics g) {
		Dimension d = getSize();
		ImageIcon img = new ImageIcon("Images/BlackList.jpg");
		g.drawImage(img.getImage(), 0, 0, d.width, d.height, null);
	}

	public BlackList() {

		this.setLayout(null);

		titleL = new JLabel("BlackList");
		titleL.setFont(new Font("고딕체", Font.BOLD, 20));
		titleL.setOpaque(false);

		// 버튼 생성
		sendBtn = new JButton("메일 보내기",new ImageIcon("Images//frame.jpg"));
		sendBtn.setHorizontalTextPosition(JToggleButton.CENTER);
		sendBtn.setBorderPainted(false);
		sendBtn.setOpaque(false);
		
		sendAllBtn = new JButton("모두 보내기",new ImageIcon("Images//frame.jpg"));
		sendAllBtn.setHorizontalTextPosition(JToggleButton.CENTER);
		sendAllBtn.setBorderPainted(false);
		sendAllBtn.setOpaque(false);
		
		updateBtn = new JButton("새로 고침",new ImageIcon("Images//frame.jpg"));
		updateBtn.setHorizontalTextPosition(JToggleButton.CENTER);
		updateBtn.setBorderPainted(false);
		updateBtn.setOpaque(false);

		// Table 생성
		Vector<String> v = new Vector<String>();
		v.add("ID");
		v.add("이름");
		v.add("이메일");
		v.add("도서코드");
		v.add("도서이름");
		v.add("연체일");
		model = new DefaultTableModel(v, 0){
	         @Override
	           public boolean isCellEditable(int row, int column) { 
	            return false;
	         }
	       };
	       
		table = new JTable(model);

		today = new Date();
		list = lendDAO.getLendList();

		for (LendDTO dto : list) {
			if (today.after(dto.getReturnDate())) {
				Vector<String> v1 = new Vector<String>();
				long diffDay = (dto.getReturnDate().getTime() - today.getTime()) / (24 * 60 * 60 * 1000); 
				if (diffDay != 0) {
					v1.add(dto.getId());
					v1.add(dto.getName());
					v1.add(dto.getEmail());
					v1.add(dto.getCode() + "");
					v1.add(dto.getTitle());
					v1.add("반납일 " + (1+(diffDay*-1)) +"일 초과");
					model.addRow(v1);
					
				}
			}																										
		}

		// JScrollPanedp table 추가
		JScrollPane scroll = new JScrollPane(table);
		scroll.setPreferredSize(new Dimension(1000, 500));
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		
		// 배치
		labelP = new JPanel();
		labelP.add(titleL); // 블랙리스트 title

		// 테이블
		scroll.setBounds(50, 200, 1000, 500);
		add(scroll);

		// 추가,삭제,수정 버튼
		btnP = new JPanel(new GridLayout(1, 3, 5, 0));
		btnP.setOpaque(false);
		btnP.add(updateBtn);
		btnP.add(sendBtn);
		btnP.add(sendAllBtn);

		btnP.setBounds(650, 150, 400, 40);
		add(btnP);

		// 이벤트
		updateBtn.addActionListener(this);
		sendBtn.addActionListener(this);
		sendAllBtn.addActionListener(this);

	       
	}

	public void actionPerformed(ActionEvent e) {
		String name,email,bookName =null;
		
		if (e.getSource() == updateBtn) {
			model.setRowCount(0);
			list = lendDAO.getLendList();
			for (LendDTO dto : list) {
				if (today.after(dto.getReturnDate())) {
					Vector<String> v1 = new Vector<String>();
					v1.add(dto.getId());
					v1.add(dto.getName());
					v1.add(dto.getEmail());
					v1.add(dto.getCode() + "");
					v1.add(dto.getTitle());
					long diffDay = (dto.getReturnDate().getTime() - today.getTime()) / (24 * 60 * 60 * 1000); 
					v1.add("반납일 " + diffDay*-1 + "초과");
					v1.add(diffDay + "");
					model.addRow(v1);
				}
			}
			JOptionPane.showMessageDialog(this, "새로고침을 완료하였습니다.", "대여 완료", JOptionPane.INFORMATION_MESSAGE);
			
		} else if (e.getSource() == sendBtn) {
			
			int row = table.getSelectedRow();
			
			if (row == -1) {
				JOptionPane.showMessageDialog(this, "선택된  회원이 없습니다");
				return;
			}
			int result = JOptionPane.showConfirmDialog(this, "연체 회원에게 메일을 보내시겠습니까?", "메일 보내기", JOptionPane.YES_NO_OPTION);
			
			if (result == 1) return;
			else if(result ==0) {

				name =(String) table.getValueAt(row, 1);
				email = (String) table.getValueAt(row, 2);
				bookName = (String) table.getValueAt(row, 4);
				blackSendMail.sendMail(name,email,bookName);
			}
			
		} else if (e.getSource() == sendAllBtn) {
			int result = JOptionPane.showConfirmDialog(this, "연체 회원들에게 단체 메일을 보내시겠습니까?", "메일 보내기",
					JOptionPane.YES_NO_OPTION);
			
			if (result == 1) return;
			else if (result == 0) {
				for (int i = 0; i < list.size(); i++) {
					if (today.after(list.get(i).getReturnDate())) {
						name = list.get(i).getName();
						email = list.get(i).getEmail();
						bookName = list.get(i).getTitle();
						blackSendMail.sendMail(name, email, bookName);
					}

				} // for
			}
		}

	}// actionPerformed()



}
