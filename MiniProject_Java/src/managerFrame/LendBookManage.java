package managerFrame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import manager.dao.BookDAO;
import manager.dao.LendDAO;
import manager.dao.MemberDAO;
import manager.dao.ReportDAO;
import manager.dto.BookDTO;
import manager.dto.LendDTO;
import manager.dto.MemberDTO;
import manager.dto.ReportDTO;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.awt.event.ActionEvent;

public class LendBookManage extends JPanel implements ActionListener {
	private JLabel idL, nameL, birthL, emailL, codeL, titleL, authorL, publisherL;
	private JTextField idT, nameT, birthT, emailT, codeT, titleT, authorT, publisherT;
	private JButton mSearchBtn, bSearchBtn, lendBtn, returnBtn, delBtn, outputBtn;
	private JTable table;
	private JScrollPane scroll;
	private Vector<String> vector;
	private DefaultTableModel model;

	private BookDTO bookDTO = new BookDTO();
	private BookDAO bookDAO = new BookDAO();
	static List<BookDTO> bookList = new ArrayList<BookDTO>();

	private MemberDTO memberDTO = new MemberDTO();
	private MemberDAO memberDAO = new MemberDAO();
	static List<MemberDTO> memberList = new ArrayList<MemberDTO>();

	private LendDTO lendDTO = new LendDTO();
	private LendDAO lendDAO = new LendDAO();
	static List<LendDTO> lendList = new ArrayList<LendDTO>();

	private ReportDAO reportDAO = new ReportDAO();
	public static int lendCount;

	// 배경
	public void paintComponent(Graphics g) {
		Dimension d = getSize();
		ImageIcon img = new ImageIcon("Images//Lendbook.jpg");
		g.drawImage(img.getImage(), 0, 0, d.width, d.height, null);
	}

	// frame 설정
	public LendBookManage() {
		setLayout(null);

		idL = new JLabel("회원아이디");
		idL.setHorizontalAlignment(JLabel.RIGHT);
		idT = new JTextField(20);
		// idL.setBounds(100, 166, 105, 37);
		idL.setBounds(60, 205, 105, 35);
		idT.setBounds(177, 205, 200, 35);
		nameL = new JLabel("이름");
		nameL.setHorizontalAlignment(JLabel.RIGHT);
		nameT = new JTextField(20);
		nameT.setEditable(false);
		nameL.setBounds(60, 255, 105, 35);
		nameT.setBounds(177, 255, 200, 35);
		birthL = new JLabel("생년월일");
		birthL.setHorizontalAlignment(JLabel.RIGHT);
		birthT = new JTextField(20);
		birthT.setEditable(false);
		birthL.setBounds(419, 255, 57, 35);
		birthT.setBounds(488, 255, 200, 35);
		emailL = new JLabel("email");
		emailL.setHorizontalAlignment(JLabel.RIGHT);
		emailT = new JTextField(20);
		emailT.setEditable(false);
		emailL.setBounds(703, 255, 84, 35);
		emailT.setBounds(799, 255, 200, 35);

		codeL = new JLabel("도서코드");
		codeL.setHorizontalAlignment(JLabel.RIGHT);
		codeT = new JTextField(20);
		codeL.setBounds(60, 380, 105, 35);
		codeT.setBounds(177, 380, 200, 35);
		titleL = new JLabel("도서명");
		titleL.setHorizontalAlignment(JLabel.RIGHT);
		titleT = new JTextField(20);
		titleT.setEditable(false);
		titleL.setBounds(60, 430, 105, 35);
		titleT.setBounds(177, 430, 200, 35);
		authorL = new JLabel("저자");
		authorL.setHorizontalAlignment(JLabel.RIGHT);
		authorT = new JTextField(20);
		authorT.setEditable(false);
		authorL.setBounds(419, 430, 57, 35);
		authorT.setBounds(488, 430, 200, 35);
		publisherL = new JLabel("출판사");
		publisherL.setHorizontalAlignment(JLabel.RIGHT);
		publisherT = new JTextField(20);
		publisherT.setEditable(false);
		publisherL.setBounds(703, 430, 84, 35);
		publisherT.setBounds(799, 430, 200, 35);

		mSearchBtn = new JButton(new ImageIcon("Images//SearchBtn.jpg"));
		mSearchBtn.setBorderPainted(false);
		mSearchBtn.setLocation(392, 205);
		mSearchBtn.setSize(80, 35);
		bSearchBtn = new JButton(new ImageIcon("Images//SearchBtn.jpg"));
		bSearchBtn.setBorderPainted(false);
		bSearchBtn.setLocation(392, 380);
		bSearchBtn.setSize(80, 35);
		lendBtn = new JButton(new ImageIcon("Images//lendBtn.jpg"));
		lendBtn.setBorderPainted(false);
		lendBtn.setBounds(719, 380, 80, 35);
		returnBtn = new JButton(new ImageIcon("Images//returnBtn.jpg"));
		returnBtn.setBorderPainted(false);
		returnBtn.setBounds(819, 380, 80, 35);
		delBtn = new JButton(new ImageIcon("Images//delBtn.jpg"));
		delBtn.setBorderPainted(false);
		delBtn.setBounds(919, 380, 80, 35);
		outputBtn = new JButton(new ImageIcon("Images//outputBtn.jpg"));
		outputBtn.setBorderPainted(false);
		outputBtn.setBounds(919, 205, 80, 35);

		add(idL);
		add(idT);
		add(nameL);
		add(nameT);
		add(birthL);
		add(birthT);
		add(emailL);
		add(emailT);
		add(codeL);
		add(codeT);
		add(titleL);
		add(titleT);
		add(authorL);
		add(authorT);
		add(publisherL);
		add(publisherT);

		add(codeL);
		add(codeT);
		add(titleL);
		add(titleT);
		add(authorL);
		add(authorT);
		add(publisherL);
		add(publisherT);

		add(mSearchBtn);
		add(bSearchBtn);
		add(lendBtn);
		add(returnBtn);
		add(delBtn);
		add(outputBtn);

		vector = new Vector<String>();
		vector.addElement("회원ID");
		vector.addElement("이름");
		vector.add("책코드");
		vector.add("책제목");
		vector.add("대여일");
		vector.add("반납일");

		model = new DefaultTableModel(vector, 0) {
			@Override
			public boolean isCellEditable(int row, int column) { // 수정, 입력 불가
				return false;
			}
		};

		table = new JTable(model);

		JScrollPane scroll = new JScrollPane(table);
		scroll.setBounds(90, 510, 920, 180);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		add(scroll);

		table.getColumnModel().getColumn(0).setPreferredWidth(90);
		table.getColumnModel().getColumn(1).setPreferredWidth(90);
		table.getColumnModel().getColumn(2).setPreferredWidth(90);
		table.getColumnModel().getColumn(3).setPreferredWidth(410);
		table.getColumnModel().getColumn(4).setPreferredWidth(130);
		table.getColumnModel().getColumn(5).setPreferredWidth(130);

		lendBtn.addActionListener(this);
		returnBtn.addActionListener(this);
		bSearchBtn.addActionListener(this);
		mSearchBtn.addActionListener(this);
		delBtn.addActionListener(this);
		outputBtn.addActionListener(this);

		// 마우스 이벤트
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {

				lendList = lendDAO.getLendList();

				int index = table.getSelectedRow();

				memberList = memberDAO.getMemberList();
				for (int i = 0; i < memberList.size(); i++) {
					if (table.getValueAt(index, 0).equals(memberList.get(i).getId())) {
						idT.setText(memberList.get(i).getId());
						nameT.setText(memberList.get(i).getName());
						birthT.setText(memberList.get(i).getBirth());
						emailT.setText(memberList.get(i).getEmail());
					}
				}

				bookList = bookDAO.getBookList();
				for (int i = 0; i < bookList.size(); i++) {
					if ((table.getValueAt(index, 2).equals(bookList.get(i).getCode() + ""))) {
						codeT.setText(bookList.get(i).getCode() + "");
						titleT.setText(bookList.get(i).getTitle());
						authorT.setText(bookList.get(i).getAuthor());
						publisherT.setText(bookList.get(i).getPublisher());

						System.out.println(table.getValueAt(index, 2));
					}
				}
			}
		});

		output();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == lendBtn) {
			int result = JOptionPane.showConfirmDialog(this, "선택된 책을 대여하시겠습니까?", "대여", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION)
				lendBook();
		} else if (e.getSource() == returnBtn) {
			int result = JOptionPane.showConfirmDialog(this, "선택된 책을 반납하시겠습니까?", "반납", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION)
				returnBook();
		} else if (e.getSource() == bSearchBtn) {
			bSearch();
		} else if (e.getSource() == mSearchBtn) {
			mSearch();
		} else if (e.getSource() == delBtn) {
			deleteText();
		} else if (e.getSource() == outputBtn) {
			output();
		}

	}

	// 대여
	private void lendBook() {

		if (codeT.getText().equals("") || idT.getText().equals("") || titleT.getText().equals("")
				|| nameT.getText().equals("")) {
			JOptionPane.showMessageDialog(this, "정보를 입력해주세요", "대여불가", JOptionPane.WARNING_MESSAGE);
		} else {
			int count = 0;
			List<LendDTO> lendList = lendDAO.getLendList();

			for (LendDTO lendDTO : lendList) {
				if (idT.getText().equals(lendDTO.getId())) {
					count++;
					if (lendDTO.getState() == 1) {
						JOptionPane.showMessageDialog(this, "[연체자] 도서를 대여할 수 없습니다	    \n - 책을 반납하여 주세요", "대여불가",
								JOptionPane.ERROR_MESSAGE);
						return;
					} else if (count >= 3) {
						JOptionPane.showMessageDialog(this, "도서 대여는 3권만  가능합니다", "대여불가", JOptionPane.WARNING_MESSAGE);
						return;
					}
				} else if (codeT.getText().equals(lendDTO.getCode() + "")) {
					JOptionPane.showMessageDialog(this, "이미 대여중인 책입니다", "대여불가", JOptionPane.WARNING_MESSAGE);
					return;
				}

			}

			lendDTO = new LendDTO();
			lendDTO.setId(idT.getText());
			lendDTO.setName(nameT.getText());
			lendDTO.setEmail(emailT.getText());
			lendDTO.setCode(Integer.parseInt(codeT.getText()));
			lendDTO.setTitle(titleT.getText());
			lendDTO.setLendCheck(1); // 대여
			lendDTO.setState(0); // 처음 대여할때는 비연체자

			// list에 dto추가
			lendList.add(lendDTO);

			// DB에 데이터 추가
			lendDAO.lendInsert(lendDTO);

			JOptionPane.showMessageDialog(this, "대여 완료되었습니다", "대여완료", JOptionPane.INFORMATION_MESSAGE);

			List<ReportDTO> list = new ArrayList<ReportDTO>();
			list = new ReportDAO().showReport();
			lendCount = list.get(BookReport.nowMon - 1).getLendCount();
			lendCount++;
			reportDAO.updateLend(BookReport.nowMon);

			bookList = bookDAO.getBookList();
			for (BookDTO bookDTO : bookList) {
				if (codeT.getText().equals(bookDTO.getCode() + "")) {
					bookDTO.setLendCheck(1);
					lendDAO.lendCheckUpdate(bookDTO);
					System.out.println(bookDTO.getLendCheck());
				}
			}
			output();
			System.out.println(lendList.size());
		}

	}

	// 반납
	private void returnBook() {
		if (codeT.getText().equals("") || idT.getText().equals("")) {
			JOptionPane.showMessageDialog(this, "정보를 입력해주세요", "반납불가", JOptionPane.WARNING_MESSAGE);
		} else {

			int index = table.getSelectedRow();

			if (index != -1) {
				lendList = lendDAO.getLendList();
				for (int i = 0; i < lendList.size(); i++) {
					if ((table.getValueAt(index, 2).equals(lendList.get(i).getCode() + ""))) {
						lendDTO = lendList.get(i);
						// DTO삭제
						lendList.remove(i);
						// DB삭제
						lendDAO.lendDelete(lendDTO);
					}
				}

				bookList = bookDAO.getBookList();
				for (BookDTO bookDTO : bookList) {
					if (codeT.getText().equals(bookDTO.getCode() + "")) {
						bookDTO.setLendCheck(0);
						lendDAO.returnCheckUpdate(bookDTO);
					}
				}

				JOptionPane.showMessageDialog(this, "반납 완료되었습니다", "반납완료", JOptionPane.INFORMATION_MESSAGE);

				output();

				reportDAO.updateReturn(BookReport.nowMon);
			} else
				return;
		}
	}

	// 출력
	public void output() {

		model.setRowCount(0);

		List<LendDTO> list = lendDAO.getLendList();
		for (LendDTO lendDTO : list) {
			Vector<String> v = new Vector<String>();
			v.add(lendDTO.getId());
			v.add(lendDTO.getName());
			v.add(lendDTO.getCode() + "");
			v.add(lendDTO.getTitle());
			v.add(lendDTO.getLendDate() + "");
			v.add(lendDTO.getReturnDate() + "");
			model.addRow(v);
		}

	}// output()

	// 회원 id 검색
	private void mSearch() {
		memberList = memberDAO.getMemberList();

		nameT.setText("");
		birthT.setText("");
		emailT.setText("");

		for (int i = 0; i < memberList.size(); i++) {
			if (idT.getText().equals(memberList.get(i).getId())) {
				nameT.setText(memberList.get(i).getName());
				birthT.setText(memberList.get(i).getBirth());
				emailT.setText(memberList.get(i).getEmail());
			}
		}

		model.setRowCount(0);

		lendList = lendDAO.getLendList();
		for (int i = 0; i < lendList.size(); i++) {
			if (idT.getText().equals(lendList.get(i).getId())) {
				Vector<String> v = new Vector<String>();
				v.add(lendList.get(i).getId());
				v.add(lendList.get(i).getName());
				v.add(lendList.get(i).getCode() + "");
				v.add(lendList.get(i).getTitle());
				v.add(lendList.get(i).getLendDate() + "");
				v.add(lendList.get(i).getReturnDate() + "");
				model.addRow(v);
			}
		}

	}// mSearch()

	// 도서 code 검색
	public void bSearch() {
		bookList = bookDAO.getBookList();

		titleT.setText("");
		authorT.setText("");
		publisherT.setText("");

		for (int i = 0; i < bookList.size(); i++) {
			if (codeT.getText().equals(bookList.get(i).getCode() + "")) {
				titleT.setText(bookList.get(i).getTitle());
				authorT.setText(bookList.get(i).getAuthor());
				publisherT.setText(bookList.get(i).getPublisher());
			}
		}

		model.setRowCount(0);

		lendList = lendDAO.getLendList();
		for (int i = 0; i < lendList.size(); i++) {
			if (codeT.getText().equals(lendList.get(i).getCode() + "")) {
				Vector<String> v = new Vector<String>();
				v.add(lendList.get(i).getId());
				v.add(lendList.get(i).getName());
				v.add(lendList.get(i).getCode() + "");
				v.add(lendList.get(i).getTitle());
				v.add(lendList.get(i).getLendDate() + "");
				v.add(lendList.get(i).getReturnDate() + "");
				model.addRow(v);
			}
		}
	}// bSearch()

	// 입력창 지우기
	private void deleteText() {
		idT.setText("");
		nameT.setText("");
		birthT.setText("");
		emailT.setText("");
		codeT.setText("");
		titleT.setText("");
		authorT.setText("");
		publisherT.setText("");
	}
}
