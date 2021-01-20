package MemberFrame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import managerFrame.BookManagement.InsertBook;
import managerFrame.BookReport;
import managerFrame.LendBookManage;

public class BookSearch extends JPanel implements ActionListener, KeyListener {

	private JButton searchBtn, lendBtn, outputBtn;

	private JTextField searchT, codeT, titleT, authorT, genreT, publisherT, lendT;
	private JLabel labelL, codeL, titleL, authorL, genreL, publisherL, lendL;
	private JComboBox<String> searchCombo;

	private DefaultTableModel model;
	private JTable table;

	private JPanel p, p1, p2, p3, pp12, p4, p5, pp45;

	private BookDTO bookDTO = new BookDTO();
	private BookDAO bookDAO = new BookDAO();
	static List<BookDTO> bookList = new ArrayList<BookDTO>();

	private int seq;

	private MemberDTO memberDTO = new MemberDTO();
	private MemberDAO memberDAO = new MemberDAO();
	static List<MemberDTO> memberList = new ArrayList<MemberDTO>();

	private LendDTO lendDTO = new LendDTO();
	private LendDAO lendDAO = new LendDAO();
	static List<LendDTO> lendList = new ArrayList<LendDTO>();
	
	private ReportDTO reportDTO = new ReportDTO();
	private ReportDAO reportDAO = new ReportDAO();

	public void paintComponent(Graphics g) {
		Dimension d = getSize();
		ImageIcon img = new ImageIcon("Images/BookSearch.jpg");
		g.drawImage(img.getImage(), 0, 0, d.width, d.height, null);
	}

	public BookSearch() {

		// 라벨 생성
		labelL = new JLabel("Book Management");
		labelL.setFont(new Font("고딕체", Font.BOLD, 100));
		labelL.setForeground(new Color(255, 0, 0, 0));

		codeL = new JLabel("도서코드");
		titleL = new JLabel("도서명");
		authorL = new JLabel("저자");
		publisherL = new JLabel("출판사");
		genreL = new JLabel("장르");
		lendL = new JLabel("대여");

		// 버튼 생성
		searchBtn = new JButton("검색");
		lendBtn = new JButton("대여");
		outputBtn = new JButton("전체조회");

		// 텍스트필드 생성
		searchT = new JTextField("안녕하세요", 30);

		codeT = new JTextField(10);
		codeT.setEditable(false);
		titleT = new JTextField(10);
		titleT.setEditable(false);
		authorT = new JTextField(10);
		authorT.setEditable(false);
		publisherT = new JTextField(10);
		publisherT.setEditable(false);
		genreT = new JTextField(10);
		genreT.setEditable(false);
		lendT = new JTextField(10);
		lendT.setEditable(false);

		// 콤보박스 생성
		String[] comboItem = { "전체 도서", "소설", "시/에세이", "자기계발", "경제/경영", "인문", "역사", "사회", "과학" };
		searchCombo = new JComboBox<String>(comboItem);

		// Table 생성
		Vector<String> v = new Vector<String>();
		v.add("도서코드");
		v.add("도서명");
		v.add("저자");
		v.add("출판사");
		v.add("장르");
		v.add("대여여부");
		v.add("대여현황");
		model = new DefaultTableModel(v, 0) {
			@Override
			public boolean isCellEditable(int row, int column) { // 수정, 입력 불가
				return false;
			}
		};

		table = new JTable(model);
		
		// table sorter
		RowSorter<TableModel> sorter = new TableRowSorter<TableModel>(model);
		table.setRowSorter(sorter);
		
		// 테이블 컬럼 크기 설정
		table.getColumnModel().getColumn(0).setPreferredWidth(80);
		table.getColumnModel().getColumn(1).setPreferredWidth(320);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		table.getColumnModel().getColumn(4).setPreferredWidth(100);
		table.getColumnModel().getColumn(5).setPreferredWidth(100);

		// JScrollPanedp table 추가
		JScrollPane scroll = new JScrollPane(table);
		scroll.setPreferredSize(new Dimension(1000, 430));
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		// 배치
		p1 = new JPanel();
		p1.add(labelL); // 도서관리 title

		// 검색창
		p2 = new JPanel();
		p2.add(searchCombo);
		p2.add(searchT);
		p2.add(searchBtn);

		pp12 = new JPanel();
		pp12.setLayout(new BorderLayout());
		pp12.add("North", p1);
		pp12.add("South", p2);

		// 테이블
		p3 = new JPanel();
		p3.add(scroll);

		// 수정창
		p4 = new JPanel();
		p4.add(codeL);
		p4.add(codeT);
		p4.add(titleL);
		p4.add(titleT);
		p4.add(authorL);
		p4.add(authorT);
		p4.add(genreL);
		p4.add(genreT);
		p4.add(publisherL);
		p4.add(publisherT);
		p4.add(lendL);
		p4.add(lendT);

		// 추가,삭제,수정 버튼
		p5 = new JPanel();
		p5.add(lendBtn);
		p5.add(outputBtn);

		pp45 = new JPanel();
		pp45.setLayout(new BorderLayout());
		pp45.add("North", p4);
		pp45.add("South", p5);

		// big Panel
		p = new JPanel();
		p.setLayout(new BorderLayout());
		p.add("North", pp12);
		p.add("Center", p3);
		p.add("South", pp45);

		add(p);

		p1.setOpaque(false);
		p2.setOpaque(false);
		p3.setOpaque(false);
		p4.setOpaque(false);
		p5.setOpaque(false);
		pp12.setOpaque(false);
		pp45.setOpaque(false);
		p.setOpaque(false);

		// 이벤트
		searchBtn.addActionListener(this);
		lendBtn.addActionListener(this);
		outputBtn.addActionListener(this);
		searchT.addKeyListener(this);

		// 마우스 이벤트
		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				
				int i = table.getSelectedRow();				

				titleT.setText((table.getValueAt(i, 1)+""));
				codeT.setText((table.getValueAt(i, 0)+""));
				authorT.setText((table.getValueAt(i, 2)+""));
				publisherT.setText((table.getValueAt(i, 3)+""));
				genreT.setText((table.getValueAt(i, 4)+""));
				lendT.setText((table.getValueAt(i, 5)+""));
			}
		});	

		searchT.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				searchT.setText("");
			}
		});
		
		output();
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == searchBtn) {
			model.setRowCount(0);
			search();
		} else if (e.getSource() == lendBtn) {
			lendBook();
			output();
		} else if (e.getSource() == outputBtn) {
			System.out.println(LendBookManage.lendCount+"대여 횟수");
			output();
		}
	}// actionPerformed()

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_ENTER) {
			searchBtn.doClick();
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
	}

	@Override
	public void keyTyped(KeyEvent e) {
	}

	// 테이블 출력
	public void output() {
		
		model.setRowCount(0);

		List<BookDTO> bookList = bookDAO.getBookList();

		int rentableArray[] = new int[bookList.size()];
		int bookQtyArray[] = new int[bookList.size()];

		for (int i = 0; i < bookList.size(); i++) {
			for (int j = 0; j < bookList.size(); j++) {
				if (bookList.get(i).getTitle().equals(bookList.get(j).getTitle())) {
					bookQtyArray[i] += 1;
				}

			}
		}
		for (int i = 0; i < bookList.size(); i++) {
			if (bookList.get(i).getLendCheck() == 1) {
				rentableArray[i] += 1;

				for (int j = 0; j < bookList.size(); j++) {
					if (bookList.get(i).getTitle().equals(bookList.get(j).getTitle())) {
						rentableArray[j] = rentableArray[i];
					}
				}
			}
		}

		List<BookDTO> list = bookDAO.getBookList();
		for (int i = 0; i < bookList.size(); i++) {
			bookDTO = bookList.get(i);
			Vector<String> v = new Vector<String>();
			v.add(bookDTO.getCode() + ""); // ----------------------------------------0515수정, code로 변수명 수정
			v.add(bookDTO.getTitle());
			v.add(bookDTO.getAuthor());
			v.add(bookDTO.getPublisher());
			v.add(bookDTO.getGenre());
			v.add(bookDTO.getStringLendCheck());
			v.add(rentableArray[i] + "/" + bookQtyArray[i]);
			model.addRow(v);
		}
	}// output()

	// 테이블 검색
	public void search() {
		// model.setRowCount(0); ??
		String search = searchT.getText();
		String comboBox = (String) searchCombo.getSelectedItem();
		bookList = bookDAO.getBookList();
		
		int rentableArray[] = new int[bookList.size()];
		int bookQtyArray[] = new int[bookList.size()];

		for (int i = 0; i < bookList.size(); i++) {
			for (int j = 0; j < bookList.size(); j++) {
				if (bookList.get(i).getTitle().equals(bookList.get(j).getTitle())) {
					bookQtyArray[i] += 1;
				}

			}
		}
		for (int i = 0; i < bookList.size(); i++) {
			if (bookList.get(i).getLendCheck() == 1) {
				rentableArray[i] += 1;

				for (int j = 0; j < bookList.size(); j++) {
					if (bookList.get(i).getTitle().equals(bookList.get(j).getTitle())) {
						rentableArray[j] = rentableArray[i];
					}
				}
			}
		}
		
		if (comboBox.equals("전체 도서")) {
			for (int i = 0; i < bookList.size(); i++) {
				if (search.equals(bookList.get(i).getTitle()) || bookList.get(i).getTitle().contains(search)
						|| search.equals("")) {
					Vector<String> v = new Vector<String>();
					v.add(bookList.get(i).getCode() + "");
					v.add(bookList.get(i).getTitle());
					v.add(bookList.get(i).getAuthor());
					v.add(bookList.get(i).getPublisher());
					v.add(bookList.get(i).getGenre());
					v.add(bookList.get(i).getStringLendCheck());
					v.add(rentableArray[i] + "/" + bookQtyArray[i]);
					model.addRow(v);
				}
			}
		} else if (comboBox.equals("소설")) {
			for (int i = 0; i < bookList.size(); i++) {
				if (comboBox.equals(bookList.get(i).getGenre()) && search.equals("")) {
					Vector<String> v = new Vector<String>();
					v.add(bookList.get(i).getCode() + "");
					v.add(bookList.get(i).getTitle());
					v.add(bookList.get(i).getAuthor());
					v.add(bookList.get(i).getPublisher());
					v.add(bookList.get(i).getGenre());
					v.add(bookList.get(i).getStringLendCheck());
					v.add(rentableArray[i] + "/" + bookQtyArray[i]);
					model.addRow(v);
				}
				else if (comboBox.equals(bookList.get(i).getGenre()) && search.equals(bookList.get(i).getTitle())){
					Vector<String> v = new Vector<String>();
					v.add(bookList.get(i).getCode() + "");
					v.add(bookList.get(i).getTitle());
					v.add(bookList.get(i).getAuthor());
					v.add(bookList.get(i).getPublisher());
					v.add(bookList.get(i).getGenre());
					v.add(bookList.get(i).getStringLendCheck());
					v.add(rentableArray[i] + "/" + bookQtyArray[i]);
					model.addRow(v);
				}
				else if (comboBox.equals(bookList.get(i).getGenre()) && bookList.get(i).getTitle().contains(search)){
					Vector<String> v = new Vector<String>();
					v.add(bookList.get(i).getCode() + "");
					v.add(bookList.get(i).getTitle());
					v.add(bookList.get(i).getAuthor());
					v.add(bookList.get(i).getPublisher());
					v.add(bookList.get(i).getGenre());
					v.add(bookList.get(i).getStringLendCheck());
					v.add(rentableArray[i] + "/" + bookQtyArray[i]);
					model.addRow(v);
				}
			}
		} else if (comboBox.equals("시/에세이")) {
			for (int i = 0; i < bookList.size(); i++) {
				if (comboBox.equals(bookList.get(i).getGenre()) && search.equals("")) {
					Vector<String> v = new Vector<String>();
					v.add(bookList.get(i).getCode() + "");
					v.add(bookList.get(i).getTitle());
					v.add(bookList.get(i).getAuthor());
					v.add(bookList.get(i).getPublisher());
					v.add(bookList.get(i).getGenre());
					v.add(bookList.get(i).getStringLendCheck());
					v.add(rentableArray[i] + "/" + bookQtyArray[i]);
					model.addRow(v);
				}
				else if (comboBox.equals(bookList.get(i).getGenre()) && search.equals(bookList.get(i).getTitle())){
					Vector<String> v = new Vector<String>();
					v.add(bookList.get(i).getCode() + "");
					v.add(bookList.get(i).getTitle());
					v.add(bookList.get(i).getAuthor());
					v.add(bookList.get(i).getPublisher());
					v.add(bookList.get(i).getGenre());
					v.add(bookList.get(i).getStringLendCheck());
					v.add(rentableArray[i] + "/" + bookQtyArray[i]);
					model.addRow(v);
				}
				else if (comboBox.equals(bookList.get(i).getGenre()) && bookList.get(i).getTitle().contains(search)){
					Vector<String> v = new Vector<String>();
					v.add(bookList.get(i).getCode() + "");
					v.add(bookList.get(i).getTitle());
					v.add(bookList.get(i).getAuthor());
					v.add(bookList.get(i).getPublisher());
					v.add(bookList.get(i).getGenre());
					v.add(bookList.get(i).getStringLendCheck());
					v.add(rentableArray[i] + "/" + bookQtyArray[i]);
					model.addRow(v);
				}
			}
		} else if (comboBox.equals("자기계발")) {
			for (int i = 0; i < bookList.size(); i++) {
				if (comboBox.equals(bookList.get(i).getGenre()) && search.equals("")) {
					Vector<String> v = new Vector<String>();
					v.add(bookList.get(i).getCode() + "");
					v.add(bookList.get(i).getTitle());
					v.add(bookList.get(i).getAuthor());
					v.add(bookList.get(i).getPublisher());
					v.add(bookList.get(i).getGenre());
					v.add(bookList.get(i).getStringLendCheck());
					v.add(rentableArray[i] + "/" + bookQtyArray[i]);
					model.addRow(v);
				}
				else if (comboBox.equals(bookList.get(i).getGenre()) && search.equals(bookList.get(i).getTitle())){
					Vector<String> v = new Vector<String>();
					v.add(bookList.get(i).getCode() + "");
					v.add(bookList.get(i).getTitle());
					v.add(bookList.get(i).getAuthor());
					v.add(bookList.get(i).getPublisher());
					v.add(bookList.get(i).getGenre());
					v.add(bookList.get(i).getStringLendCheck());
					v.add(rentableArray[i] + "/" + bookQtyArray[i]);
					model.addRow(v);
				}
				else if (comboBox.equals(bookList.get(i).getGenre()) && bookList.get(i).getTitle().contains(search)){
					Vector<String> v = new Vector<String>();
					v.add(bookList.get(i).getCode() + "");
					v.add(bookList.get(i).getTitle());
					v.add(bookList.get(i).getAuthor());
					v.add(bookList.get(i).getPublisher());
					v.add(bookList.get(i).getGenre());
					v.add(bookList.get(i).getStringLendCheck());
					v.add(rentableArray[i] + "/" + bookQtyArray[i]);
					model.addRow(v);
				}
			}
		} else if (comboBox.equals("경제/경영")) {
			for (int i = 0; i < bookList.size(); i++) {
				if (comboBox.equals(bookList.get(i).getGenre()) && search.equals("")) {
					Vector<String> v = new Vector<String>();
					v.add(bookList.get(i).getCode() + "");
					v.add(bookList.get(i).getTitle());
					v.add(bookList.get(i).getAuthor());
					v.add(bookList.get(i).getPublisher());
					v.add(bookList.get(i).getGenre());
					v.add(bookList.get(i).getStringLendCheck());
					v.add(rentableArray[i] + "/" + bookQtyArray[i]);
					model.addRow(v);
				}
				else if (comboBox.equals(bookList.get(i).getGenre()) && search.equals(bookList.get(i).getTitle())){
					Vector<String> v = new Vector<String>();
					v.add(bookList.get(i).getCode() + "");
					v.add(bookList.get(i).getTitle());
					v.add(bookList.get(i).getAuthor());
					v.add(bookList.get(i).getPublisher());
					v.add(bookList.get(i).getGenre());
					v.add(bookList.get(i).getStringLendCheck());
					v.add(rentableArray[i] + "/" + bookQtyArray[i]);
					model.addRow(v);
				}
				else if (comboBox.equals(bookList.get(i).getGenre()) && bookList.get(i).getTitle().contains(search)){
					Vector<String> v = new Vector<String>();
					v.add(bookList.get(i).getCode() + "");
					v.add(bookList.get(i).getTitle());
					v.add(bookList.get(i).getAuthor());
					v.add(bookList.get(i).getPublisher());
					v.add(bookList.get(i).getGenre());
					v.add(bookList.get(i).getStringLendCheck());
					v.add(rentableArray[i] + "/" + bookQtyArray[i]);
					model.addRow(v);
				}
			}
		} else if (comboBox.equals("역사")) {
			for (int i = 0; i < bookList.size(); i++) {
				if (comboBox.equals(bookList.get(i).getGenre()) && search.equals("")) {
					Vector<String> v = new Vector<String>();
					v.add(bookList.get(i).getCode() + "");
					v.add(bookList.get(i).getTitle());
					v.add(bookList.get(i).getAuthor());
					v.add(bookList.get(i).getPublisher());
					v.add(bookList.get(i).getGenre());
					v.add(bookList.get(i).getStringLendCheck());
					v.add(rentableArray[i] + "/" + bookQtyArray[i]);
					model.addRow(v);
				}
				else if (comboBox.equals(bookList.get(i).getGenre()) && search.equals(bookList.get(i).getTitle())){
					Vector<String> v = new Vector<String>();
					v.add(bookList.get(i).getCode() + "");
					v.add(bookList.get(i).getTitle());
					v.add(bookList.get(i).getAuthor());
					v.add(bookList.get(i).getPublisher());
					v.add(bookList.get(i).getGenre());
					v.add(bookList.get(i).getStringLendCheck());
					v.add(rentableArray[i] + "/" + bookQtyArray[i]);
					model.addRow(v);
				}
				else if (comboBox.equals(bookList.get(i).getGenre()) && bookList.get(i).getTitle().contains(search)){
					Vector<String> v = new Vector<String>();
					v.add(bookList.get(i).getCode() + "");
					v.add(bookList.get(i).getTitle());
					v.add(bookList.get(i).getAuthor());
					v.add(bookList.get(i).getPublisher());
					v.add(bookList.get(i).getGenre());
					v.add(bookList.get(i).getStringLendCheck());
					v.add(rentableArray[i] + "/" + bookQtyArray[i]);
					model.addRow(v);
				}
			}
		} else if (comboBox.equals("인문")) {
			for (int i = 0; i < bookList.size(); i++) {
				if (comboBox.equals(bookList.get(i).getGenre()) && search.equals("")) {
					Vector<String> v = new Vector<String>();
					v.add(bookList.get(i).getCode() + "");
					v.add(bookList.get(i).getTitle());
					v.add(bookList.get(i).getAuthor());
					v.add(bookList.get(i).getPublisher());
					v.add(bookList.get(i).getGenre());
					v.add(bookList.get(i).getStringLendCheck());
					v.add(rentableArray[i] + "/" + bookQtyArray[i]);
					model.addRow(v);
				}
				else if (comboBox.equals(bookList.get(i).getGenre()) && search.equals(bookList.get(i).getTitle())){
					Vector<String> v = new Vector<String>();
					v.add(bookList.get(i).getCode() + "");
					v.add(bookList.get(i).getTitle());
					v.add(bookList.get(i).getAuthor());
					v.add(bookList.get(i).getPublisher());
					v.add(bookList.get(i).getGenre());
					v.add(bookList.get(i).getStringLendCheck());
					v.add(rentableArray[i] + "/" + bookQtyArray[i]);
					model.addRow(v);
				}
				else if (comboBox.equals(bookList.get(i).getGenre()) && bookList.get(i).getTitle().contains(search)){
					Vector<String> v = new Vector<String>();
					v.add(bookList.get(i).getCode() + "");
					v.add(bookList.get(i).getTitle());
					v.add(bookList.get(i).getAuthor());
					v.add(bookList.get(i).getPublisher());
					v.add(bookList.get(i).getGenre());
					v.add(bookList.get(i).getStringLendCheck());
					v.add(rentableArray[i] + "/" + bookQtyArray[i]);
					model.addRow(v);
				}
			}
		} else if (comboBox.equals("역사")) {
			for (int i = 0; i < bookList.size(); i++) {
				if (comboBox.equals(bookList.get(i).getGenre()) && search.equals("")) {
					Vector<String> v = new Vector<String>();
					v.add(bookList.get(i).getCode() + "");
					v.add(bookList.get(i).getTitle());
					v.add(bookList.get(i).getAuthor());
					v.add(bookList.get(i).getPublisher());
					v.add(bookList.get(i).getGenre());
					v.add(bookList.get(i).getStringLendCheck());
					v.add(rentableArray[i] + "/" + bookQtyArray[i]);
					model.addRow(v);
				}
				else if (comboBox.equals(bookList.get(i).getGenre()) && search.equals(bookList.get(i).getTitle())){
					Vector<String> v = new Vector<String>();
					v.add(bookList.get(i).getCode() + "");
					v.add(bookList.get(i).getTitle());
					v.add(bookList.get(i).getAuthor());
					v.add(bookList.get(i).getPublisher());
					v.add(bookList.get(i).getGenre());
					v.add(bookList.get(i).getStringLendCheck());
					v.add(rentableArray[i] + "/" + bookQtyArray[i]);
					model.addRow(v);
				}
				else if (comboBox.equals(bookList.get(i).getGenre()) && bookList.get(i).getTitle().contains(search)){
					Vector<String> v = new Vector<String>();
					v.add(bookList.get(i).getCode() + "");
					v.add(bookList.get(i).getTitle());
					v.add(bookList.get(i).getAuthor());
					v.add(bookList.get(i).getPublisher());
					v.add(bookList.get(i).getGenre());
					v.add(bookList.get(i).getStringLendCheck());
					v.add(rentableArray[i] + "/" + bookQtyArray[i]);
					model.addRow(v);
				}
			}
		} else if (comboBox.equals("사회")) {
			for (int i = 0; i < bookList.size(); i++) {
				if (comboBox.equals(bookList.get(i).getGenre()) && search.equals("")) {
					Vector<String> v = new Vector<String>();
					v.add(bookList.get(i).getCode() + "");
					v.add(bookList.get(i).getTitle());
					v.add(bookList.get(i).getAuthor());
					v.add(bookList.get(i).getPublisher());
					v.add(bookList.get(i).getGenre());
					v.add(bookList.get(i).getStringLendCheck());
					v.add(rentableArray[i] + "/" + bookQtyArray[i]);
					model.addRow(v);
				}
				else if (comboBox.equals(bookList.get(i).getGenre()) && search.equals(bookList.get(i).getTitle())){
					Vector<String> v = new Vector<String>();
					v.add(bookList.get(i).getCode() + "");
					v.add(bookList.get(i).getTitle());
					v.add(bookList.get(i).getAuthor());
					v.add(bookList.get(i).getPublisher());
					v.add(bookList.get(i).getGenre());
					v.add(bookList.get(i).getStringLendCheck());
					v.add(rentableArray[i] + "/" + bookQtyArray[i]);
					model.addRow(v);
				}
				else if (comboBox.equals(bookList.get(i).getGenre()) && bookList.get(i).getTitle().contains(search)){
					Vector<String> v = new Vector<String>();
					v.add(bookList.get(i).getCode() + "");
					v.add(bookList.get(i).getTitle());
					v.add(bookList.get(i).getAuthor());
					v.add(bookList.get(i).getPublisher());
					v.add(bookList.get(i).getGenre());
					v.add(bookList.get(i).getStringLendCheck());
					v.add(rentableArray[i] + "/" + bookQtyArray[i]);
					model.addRow(v);
				}
			}
		} else if (comboBox.equals("과학")) {
			for (int i = 0; i < bookList.size(); i++) {
				if (comboBox.equals(bookList.get(i).getGenre()) && search.equals("")) {
					Vector<String> v = new Vector<String>();
					v.add(bookList.get(i).getCode() + "");
					v.add(bookList.get(i).getTitle());
					v.add(bookList.get(i).getAuthor());
					v.add(bookList.get(i).getPublisher());
					v.add(bookList.get(i).getGenre());
					v.add(bookList.get(i).getStringLendCheck());
					v.add(rentableArray[i] + "/" + bookQtyArray[i]);
					model.addRow(v);
				}
				else if (comboBox.equals(bookList.get(i).getGenre()) && search.equals(bookList.get(i).getTitle())){
					Vector<String> v = new Vector<String>();
					v.add(bookList.get(i).getCode() + "");
					v.add(bookList.get(i).getTitle());
					v.add(bookList.get(i).getAuthor());
					v.add(bookList.get(i).getPublisher());
					v.add(bookList.get(i).getGenre());
					v.add(bookList.get(i).getStringLendCheck());
					v.add(rentableArray[i] + "/" + bookQtyArray[i]);
					model.addRow(v);
				}
				else if (comboBox.equals(bookList.get(i).getGenre()) && bookList.get(i).getTitle().contains(search)){
					Vector<String> v = new Vector<String>();
					v.add(bookList.get(i).getCode() + "");
					v.add(bookList.get(i).getTitle());
					v.add(bookList.get(i).getAuthor());
					v.add(bookList.get(i).getPublisher());
					v.add(bookList.get(i).getGenre());
					v.add(bookList.get(i).getStringLendCheck());
					v.add(rentableArray[i] + "/" + bookQtyArray[i]);
					model.addRow(v);
				}
			}
		}

	}// search()

	// 대여
	private void lendBook() {
		
		// 로그인한 회원 정보 가져오기
		BasicFrameMember bm = new BasicFrameMember();
		
		List<MemberDTO> memberList = memberDAO.getMemberList();
		
		MemberDTO memberDTO =new MemberDTO();
		for (int i = 0; i < memberList.size(); i++) {
			if (bm.getKey() == memberList.get(i).getSeq()) {
		
				memberDTO = memberList.get(i);				
			}
		}		

		int count = 0;
		List<LendDTO> lendList = lendDAO.getLendList();

		for (LendDTO lendDTO : lendList) {
			if (memberDTO.getId().equals(lendDTO.getId())) {
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
		lendDTO.setId(memberDTO.getId());
		lendDTO.setName(memberDTO.getName());
		lendDTO.setEmail(memberDTO.getEmail());
		lendDTO.setCode(Integer.parseInt(codeT.getText()));
		lendDTO.setTitle(titleT.getText());
		lendDTO.setLendCheck(1); // 대여
		lendDTO.setState(0); // 처음 대여할때는 비연체자

		// list에 dto추가
		lendList.add(lendDTO);

		// DB에 데이터 추가
		lendDAO.lendInsert(lendDTO);

		JOptionPane.showMessageDialog(this, "대여 완료되었습니다", "대여완료", JOptionPane.INFORMATION_MESSAGE);
		
		//==================================================
		
		SimpleDateFormat sdf = new SimpleDateFormat("M");
		int nowMon =Integer.parseInt(sdf.format(new Date()));
		LendBookManage lb = new LendBookManage();

		reportDAO.memberUpdate(nowMon);
		
		bookList = bookDAO.getBookList();
		for (BookDTO bookDTO : bookList) {
			if (codeT.getText().equals(bookDTO.getCode() + "")) {
				bookDTO.setLendCheck(1);
				lendDAO.lendCheckUpdate(bookDTO);
				System.out.println(bookDTO.getLendCheck());
			}
		}

		output();
	}

}
