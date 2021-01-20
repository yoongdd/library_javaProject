package managerFrame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.ScrollPaneConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import login.SignUp;
import manager.dao.LendDAO;
import manager.dao.MemberDAO;
import manager.dto.LendDTO;
import manager.dto.LendJoinDTO;
import manager.dto.MemberDTO;

public class MemberManagement extends JPanel implements ActionListener, ListSelectionListener {

	private JButton searchBtn, inquiryBtn, withdrawBtn, allSearchBtn;

	private JTextField searchT;
	private JLabel labelL;

	private JComboBox<String> combo;
	private DefaultTableModel model;
	private JTable table;

	private JPanel p, p1, p2, p3, pp12, p4;
	private MemberDAO memberDAO = new MemberDAO();
	private LendDAO lendDAO = new LendDAO();
	public List<MemberDTO> list = new ArrayList<MemberDTO>();
	public List<LendDTO> lendList = new ArrayList<LendDTO>();

	public void paintComponent(Graphics g) {
		Dimension d = getSize();
		ImageIcon img = new ImageIcon("Images/MemberManagement.jpg");
		g.drawImage(img.getImage(), 0, 0, d.width, d.height, null);
	}

	public MemberManagement() {

		// 라벨 생성
		labelL = new JLabel("Member Management");
		labelL.setFont(new Font("고딕체", Font.BOLD, 100));
		labelL.setForeground(new Color(255, 0, 0, 0));

		// 버튼 생성
		searchBtn = new JButton("검색");
		inquiryBtn = new JButton("조회");
		withdrawBtn = new JButton("탈퇴");
		allSearchBtn = new JButton("전체조회");

		// 텍스트필드 생성
		searchT = new JTextField("안녕하세요", 30);

		// 콤보박스 생성
		String[] comboItem = { "ID", "이름" };
		combo = new JComboBox<String>(comboItem);

		// Table 생성
		Vector<String> v = new Vector<String>();
		v.add("ID");
		v.add("이름");
		v.add("생년월일");
		v.add("나이");
		v.add("email");
		v.add("성별");
		v.add("연체 여부");
		model = new DefaultTableModel(v, 0) {
			@Override
			public boolean isCellEditable(int row, int column) { // 0514 수정, 입력 불가
				return false;
			}
		};
		table = new JTable(model);
		
		lendList = lendDAO.getLendList();
		Date today = new Date();

		list = memberDAO.getMemberList();
		if (list != null) {
			for (MemberDTO dto : list) {
				if (dto.getStatus() == 0) {
					Vector<String> v1 = new Vector<String>();
					v1.add(dto.getId());
					v1.add(dto.getName());
					v1.add(dto.getBirth());
					v1.add(dto.getAge() + "");
					v1.add(dto.getEmail());
					if (dto.getSex() == 0)
						v1.add("남성");
					else if (dto.getSex() == 1)
						v1.add("여성");
					for (LendDTO dto1 : lendList) {
						if (dto1.getName().equals(dto.getName()) && today.after(dto1.getReturnDate())) {
							v1.add("연체자");
						}
					}
					model.addRow(v1);
				}
			}
		}
		
		// JScrollPanedp table 추가
		JScrollPane scroll = new JScrollPane(table);
		scroll.setPreferredSize(new Dimension(1000, 430));
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		// 배치
		p1 = new JPanel();
		p1.add(labelL); // 도서관리 title
		p1.setBackground(new Color(255, 0, 0, 0));

		// 검색창
		p2 = new JPanel();
		p2.add(combo);
		p2.add(searchT);
		p2.add(searchBtn);
		p2.add(allSearchBtn);
		p2.setBackground(new Color(255, 0, 0, 0));

		pp12 = new JPanel();
		pp12.setLayout(new BorderLayout());
		pp12.add("North", p1);
		pp12.add("South", p2);
		pp12.setBackground(new Color(255, 0, 0, 0));

		// 테이블
		p3 = new JPanel();
		p3.add(scroll);
		p3.setBackground(new Color(255, 0, 0, 0));

		// 조회, 탈퇴 버튼
		p4 = new JPanel();
		p4.add(inquiryBtn);
		p4.add(withdrawBtn);
		p4.setBackground(new Color(255, 0, 0, 0));

		// big Panel
		p = new JPanel();
		p.setLayout(new BorderLayout());
		p.add("North", pp12);
		p.add("Center", p3);
		p.add("South", p4);
		p.setBackground(new Color(255, 0, 0, 0));

		add(p);

		// 이벤트
		searchBtn.addActionListener(this);
		inquiryBtn.addActionListener(this);
		withdrawBtn.addActionListener(this);
		searchT.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				searchT.setText("");
			}
		});
		allSearchBtn.addActionListener(this);

	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == searchBtn) {
			// 회원 검색
			if (combo.getSelectedItem() == "ID") {
				for (MemberDTO dto : list) {
					if (searchT.getText().equals(dto.getId())) {
						model.setRowCount(0);
						Vector<String> v1 = new Vector<String>();
						v1.add(dto.getId());
						v1.add(dto.getName());
						v1.add(dto.getBirth());
						v1.add(dto.getAge() + "");
						v1.add(dto.getEmail());
						if (dto.getSex() == 0)
							v1.add("남성");
						if (dto.getSex() == 1)
							v1.add("여성");
						model.addRow(v1);
					}
				}
			} else if (combo.getSelectedItem() == "이름") {
				for (MemberDTO dto : list) {
					if (searchT.getText().equals(dto.getName())) {
						model.setRowCount(0);
						Vector<String> v1 = new Vector<String>();
						v1.add(dto.getId());
						v1.add(dto.getName());
						v1.add(dto.getBirth());
						v1.add(dto.getAge() + "");
						v1.add(dto.getEmail());
						if (dto.getSex() == 0)
							v1.add("남성");
						if (dto.getSex() == 1)
							v1.add("여성");
						model.addRow(v1);
					}
				}
			}
		} else if (e.getSource() == inquiryBtn) {
			// 조회 버튼 , 마이페이지
			if (table.getSelectedRow() == -1)
				return;
			int index = table.getSelectedRow();
			String selectedID = (String) table.getValueAt(index, 0);
			for (MemberDTO memberDTO : list) {
				if (selectedID == memberDTO.getId()) {
					new UserPage(memberDTO);
				}
			}

		} else if (e.getSource() == withdrawBtn) {
			// 회원리스트 삭제
			int result = JOptionPane.showConfirmDialog(null, "회원정보를 삭제하시겠습니까?", "회원정보 삭제", JOptionPane.OK_CANCEL_OPTION,
					JOptionPane.WARNING_MESSAGE, null);
			if (result == JOptionPane.YES_OPTION) {
				int index = table.getSelectedRow();
				MemberDTO memberDTO = list.get(index);
				memberDAO.deleteMember(memberDTO);
				model.removeRow(index);
			}
		} else if(e.getSource() == allSearchBtn) {
			System.out.println("dd");
			lendList = lendDAO.getLendList();
			Date today = new Date();

			list = memberDAO.getMemberList();
			model.setRowCount(0);
			if (list != null) {
				for (MemberDTO dto : list) {
					if (dto.getStatus() == 0) {
						Vector<String> v1 = new Vector<String>();
						v1.add(dto.getId());
						v1.add(dto.getName());
						v1.add(dto.getBirth());
						v1.add(dto.getAge() + "");
						v1.add(dto.getEmail());
						if (dto.getSex() == 0)
							v1.add("남성");
						else if (dto.getSex() == 1)
							v1.add("여성");
						for (LendDTO dto1 : lendList) {
							if (dto1.getName().equals(dto.getName()) && today.after(dto1.getReturnDate())) {
								v1.add("연체자");
							}
						}
						model.addRow(v1);
					}
				}
			}
			
		}
	}// actionPerformed()

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
	public void valueChanged(ListSelectionEvent e) {
		if (table.getSelectedRow() == -1)
			return;

	}

}
