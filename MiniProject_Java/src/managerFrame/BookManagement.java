package managerFrame;

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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
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
import manager.dao.DeleteDAO;
import manager.dao.LendDAO;
import manager.dao.ReportDAO;
import manager.dto.BookDTO;
import manager.dto.DeleteDTO;
import manager.dto.LendDTO;
import manager.dto.ReportDTO;

public class BookManagement extends JPanel implements ActionListener, KeyListener {

	private JButton searchBtn, updateBtn, insertBtn, deleteBtn, outputBtn;
	private JTextField searchT, codeT, titleT, authorT, genreT, publisherT, lendT;
	private JLabel labelL, codeL, titleL, authorL, genreL, publisherL, lendL;
	private JComboBox<String> searchCombo, genreCombo;

	private JPanel p, p1, p2, p3, pp12, p4, p5, pp45;

	private DefaultTableModel model;
	private JTable table;

	private BookDTO bookDTO = new BookDTO();
	private BookDAO bookDAO = new BookDAO();
	static List<BookDTO> bookList = new ArrayList<BookDTO>();

	private LendDTO lendDTO = new LendDTO();
	private LendDAO lendDAO = new LendDAO();
	static List<LendDTO> lendList = new ArrayList<LendDTO>();

	private DeleteDTO deleteDTO = new DeleteDTO();
	private DeleteDAO deleteDAO = new DeleteDAO();
	static List<DeleteDTO> deleteList = new ArrayList<DeleteDTO>();

	private ReportDAO reportDAO = new ReportDAO();
	private int seq;

	public static int addCount, deleteCount;
	
	int rentableArray[];
	int bookQtyArray[];

	// 배경추가
	public void paintComponent(Graphics g) {
		Dimension d = getSize();
		ImageIcon img = new ImageIcon("Images/BookManagement.jpg");
		g.drawImage(img.getImage(), 0, 0, d.width, d.height, null);
	}

	public BookManagement() {

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
		insertBtn = new JButton("등록");
		deleteBtn = new JButton("삭제");
		updateBtn = new JButton("수정");
		outputBtn = new JButton("전체 조회");

		// 텍스트필드 생성
		searchT = new JTextField("안녕하세요 - 도서명을 검색하세요", 30);
		codeT = new JTextField(10);
		codeT.setEditable(false);
		titleT = new JTextField(10);
		authorT = new JTextField(10);
		publisherT = new JTextField(10);
		String[] genre = { "소설", "시/에세이", "자기계발", "경제/경영", "인문", "역사", "사회", "과학" };
		genreCombo = new JComboBox<String>(genre);
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
		table.getColumnModel().getColumn(0).setPreferredWidth(60);
		table.getColumnModel().getColumn(1).setPreferredWidth(340);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		table.getColumnModel().getColumn(4).setPreferredWidth(100);
		table.getColumnModel().getColumn(5).setPreferredWidth(60);
		table.getColumnModel().getColumn(6).setPreferredWidth(40);

		// JScrollPane에 table 추가
		JScrollPane scroll = new JScrollPane(table);
		scroll.setPreferredSize(new Dimension(1000, 430));
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		// Layout 배치
		p1 = new JPanel();
		p1.add(labelL); // 도서관리 title
		p1.setBackground(new Color(255, 0, 0, 0));

		// 검색창
		p2 = new JPanel();
		p2.add(searchCombo);
		p2.add(searchT);
		p2.add(searchBtn);
		p2.setBackground(new Color(255, 0, 0, 0));

		pp12 = new JPanel();
		pp12.setLayout(new BorderLayout());
		pp12.setBackground(new Color(255, 0, 0, 0));
		pp12.add("North", p1);
		pp12.add("South", p2);

		// 테이블
		p3 = new JPanel();
		p3.add(scroll);
		p3.setBackground(new Color(255, 0, 0, 0));

		// 수정창
		p4 = new JPanel();
		p4.add(codeL);
		p4.add(codeT);
		p4.add(titleL);
		p4.add(titleT);
		p4.add(authorL);
		p4.add(authorT);
		p4.add(genreL);
		p4.add(genreCombo);
		p4.add(publisherL);
		p4.add(publisherT);
		p4.add(lendL);
		p4.add(lendT);
		p4.setBackground(new Color(255, 0, 0, 0));

		// 추가,삭제,수정, 조회 버튼
		p5 = new JPanel();
		p5.add(insertBtn);
		p5.add(deleteBtn);
		p5.add(updateBtn);
		p5.add(outputBtn);
		p5.setBackground(new Color(255, 0, 0, 0));

		pp45 = new JPanel();
		pp45.setLayout(new BorderLayout());
		pp45.add("North", p4);
		pp45.add("South", p5);
		pp45.setBackground(new Color(255, 0, 0, 0));

		// big Panel
		p = new JPanel();
		p.setLayout(new BorderLayout());
		p.add("North", pp12);
		p.add("Center", p3);
		p.add("South", pp45);
		p.setBackground(new Color(255, 0, 0, 0));

		add(p);

		// 버튼 이벤트
		searchBtn.addActionListener(this);
		insertBtn.addActionListener(this);
		deleteBtn.addActionListener(this);
		updateBtn.addActionListener(this);
		outputBtn.addActionListener(this);
		searchT.addKeyListener(this);

		// 마우스 이벤트
		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				int i = table.getSelectedRow();
	

				titleT.setText((table.getValueAt(i, 1) + ""));
				codeT.setText((table.getValueAt(i, 0) + ""));
				authorT.setText((table.getValueAt(i, 2) + ""));
				publisherT.setText((table.getValueAt(i, 3) + ""));
				genreCombo.setSelectedItem((table.getValueAt(i, 4) + ""));
				lendT.setText((table.getValueAt(i, 5) + ""));
			}
		});

		searchT.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				searchT.setText("");
			}
		});

		output();
	}

	// 버튼 이벤트 구현
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == insertBtn) {
			new InsertBook();
		} else if (e.getSource() == deleteBtn) {
			int result = JOptionPane.showConfirmDialog(this, "선택된 도서를 삭제하시겠습니까?", "삭제", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION)
				delete();
			output();
		} else if (e.getSource() == updateBtn) {
			int result = JOptionPane.showConfirmDialog(this, "도서정보를 수정하시겠습니까?", "수정", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION)
				update();
			output();
		} else if (e.getSource() == outputBtn) {
			output();
		} else if (e.getSource() == searchBtn) {
			model.setRowCount(0);
			search();
		}
	}// actionPerformed()

	// 키 이벤트(엔터)
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

	// 테이블 삭제
	public void delete() {

		
		int index = table.convertRowIndexToModel(table.getSelectedRow());

		if (index != -1) {

			// DTO선택
			bookList = bookDAO.getBookList();
			bookDTO = bookList.get(index);

			lendList = lendDAO.getLendList();
			for (int i = 0; i < lendList.size(); i++) {
				if (lendList.get(i).getCode() == bookDTO.getCode()) {
					JOptionPane.showMessageDialog(this, "대여 중인 도서입니다", "도서 삭제 불가", JOptionPane.WARNING_MESSAGE);
					return;
				}
			}

			// DTO삭제
			bookList.remove(index);
			// DB삭제
			bookDAO.bookDelete(bookDTO);

			deleteList = deleteDAO.getDeleteList();
			DeleteDTO deleteDTO = new DeleteDTO();
			deleteDTO.setCode(Integer.parseInt(codeT.getText()));
			deleteDTO.setTitle(titleT.getText());
			deleteDTO.setAuthor(authorT.getText());
			deleteDTO.setPublisher(publisherT.getText());
			deleteDTO.setGenre((String) genreCombo.getSelectedItem());

			// list에 dto추가
			deleteList.add(deleteDTO);
			deleteDAO.discardBook(deleteDTO);

			JOptionPane.showMessageDialog(this, "도서가 삭제되었습니다", "대여 삭제", JOptionPane.WARNING_MESSAGE);

			List<ReportDTO> list = new ArrayList<ReportDTO>();
			list = new ReportDAO().showReport();
			deleteCount = list.get(BookReport.nowMon-1).getDeleteCount();
			deleteCount++;
			reportDAO.updateDelete(BookReport.nowMon);

		} else
			return;
	}// delete()

	// 테이블 수정
	public void update() {

		
		int i = table.convertRowIndexToModel(table.getSelectedRow());

		if (i != -1) {
			// DTO선택
			bookList = bookDAO.getBookList();
			bookDTO = bookList.get(i);
			// DTO수정
			bookDTO.setTitle(titleT.getText());
			bookDTO.setAuthor(authorT.getText());
			bookDTO.setPublisher(publisherT.getText());
			bookDTO.setGenre(genreCombo.getSelectedItem());
			// DB수정
			bookDAO.bookUpdate(bookDTO);

			JOptionPane.showMessageDialog(this, "도서 정보가 수정되었습니다", "대여 수정", JOptionPane.INFORMATION_MESSAGE);
		} else
			return;
	}// update()

	// 테이블 등록, inner class
	public class InsertBook extends JFrame implements ActionListener {
		private JButton insertBtn, cancleBtn;
		private JTextField titleT, authorT, publisherT, qtyT;
		private JLabel labelL;
		JComboBox<String> genreCombo;

		private DefaultTableModel model;
		private BookManagement bookManagement;

		private BookDTO dto = new BookDTO();

		// frame창
		public InsertBook() {

			super("도서 추가");
			
			JPanel totalP = new JPanel() {
				public void paintComponent(Graphics g) {
					Dimension d = getSize();
					ImageIcon img = new ImageIcon("images/insertBook.jpg");
					g.drawImage(img.getImage(), 0, 0, d.width, d.height, null);
				}
			};

			totalP.setLayout(null);
			
			setLayout(null);

			// 텍스트필드 생성
			titleT = new JTextField("도서명을 입력하세요");
			titleT.setForeground(new Color(150, 150, 150));
			titleT.setBounds(75, 93, 200, 35);
			authorT = new JTextField("저자를 입력하세요");
			authorT.setBounds(75, 133, 200, 35);
			authorT.setForeground(new Color(150, 150, 150));
			publisherT = new JTextField("출판사를 입력하세요");
			publisherT.setBounds(75, 173, 200, 35);
			publisherT.setForeground(new Color(150, 150, 150));
			qtyT = new JTextField("수량을 입력하세요");
			qtyT.setBounds(75, 213, 200, 35);
			qtyT.setForeground(new Color(150, 150, 150));

			// 버튼 생성
			insertBtn = new JButton("등록");
			insertBtn.setBounds(100, 310, 70, 30);
			cancleBtn = new JButton("취소");
			cancleBtn.setBounds(175, 310, 70, 30);

			// 콤보박스 생성
			String[] genre = { "소설", "시/에세이", "자기계발", "경제/경영", "인문", "역사", "사회", "과학" };
			genreCombo = new JComboBox<String>(genre);
			genreCombo.setBounds(75, 253, 200, 30);

			// 배치
			totalP.add(titleT);
			totalP.add(authorT);
			totalP.add(publisherT);
			totalP.add(qtyT);
			totalP.add(genreCombo);
			totalP.add(insertBtn);
			totalP.add(cancleBtn);
			
			// frame 설정
			setContentPane(totalP);
			setBounds(1080, 250, 350, 420);
			setVisible(true);
			setResizable(false);
			setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

			addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					int result = JOptionPane.showConfirmDialog(null, "종료하시겠습니까?");
					if (result == JOptionPane.YES_OPTION)
						setVisible(false);
				}
			});

			// 버튼 이벤트
			insertBtn.addActionListener(this);
			cancleBtn.addActionListener(this);

			// 텍스트필드 클릭 시 라벨 초기화
			titleT.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					titleT.setText("");
				}
			});
			authorT.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					authorT.setText("");
				}
			});
			publisherT.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					publisherT.setText("");
				}
			});
			qtyT.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					qtyT.setText("");
				}
			});

		}

		// 입력창 버튼 이벤트
		public void actionPerformed(ActionEvent e) {

			if (e.getSource() == insertBtn) {

				if (titleT.getText().equals("") || authorT.getText().equals("") 
						|| publisherT.getText().equals("") || qtyT.getText().equals("")
						|| titleT.getText().equals("도서명을 입력하세요") || authorT.getText().equals("저자를 입력하세요")
						|| publisherT.getText().equals("출판사를 입력하세요") || qtyT.getText().equals("수량을 입력하세요")) {
					JOptionPane.showMessageDialog(this, "도서 정보를 입력해주세요", "도서 등록 불가", JOptionPane.WARNING_MESSAGE);
				} else {

					int qty = Integer.parseInt(qtyT.getText());

					int firstCode = 0;
					int lastCode = 0;
					for (int i = 1; i <= qty; i++) {
						seq = bookDAO.getSeqBook();
						System.out.println(seq);

						List<BookDTO> list = bookDAO.getBookList();
						bookDTO = new BookDTO();
						bookDTO.setCode(seq);
						bookDTO.setTitle(titleT.getText());
						bookDTO.setAuthor(authorT.getText());
						bookDTO.setPublisher(publisherT.getText());
						bookDTO.setLendCheck(0); // 처음 등록시 대여 가능
						bookDTO.setGenre((String) genreCombo.getSelectedItem());

						// list에 dto추가
						bookList.add(bookDTO);
						// DB에  추가
						bookDAO.bookInsert(bookDTO);

						if (i == 1)
							firstCode = bookDTO.getCode();
						else if (i == qty)
							lastCode = bookDTO.getCode();
						
						List<ReportDTO> newlist = new ArrayList<ReportDTO>();
						newlist = new ReportDAO().showReport();
						addCount = newlist.get(BookReport.nowMon-1).getAddCount();
						addCount++;
						reportDAO.updateAdd(BookReport.nowMon);

					}

					String message = "\n 도서가 등록되었습니다" 
									+ "\n-------------------------------------" 
									+ "\n도서코드 :   " + firstCode + " ~ " + lastCode 
									+ "\n도서명 :   " + titleT.getText() 
									+ "\n저자 :   " + authorT.getText() 
									+ "\n출판사 :   " + publisherT.getText() 
									+ "\n도서수량 :   " + qty + " 권"
									+ "\n장르 :   " + bookDTO.getGenre() 
									+ "\n-------------------------------------" 
									+ "\n";

					JOptionPane.showMessageDialog(this, message, "도서 등록 완료", JOptionPane.INFORMATION_MESSAGE);

					output();

					setVisible(false);
				}

			} else if (e.getSource() == cancleBtn) {
				setVisible(false);
			}
		} // actionPerformed()
	}
}