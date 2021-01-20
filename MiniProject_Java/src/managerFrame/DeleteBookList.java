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
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowSorter;
import javax.swing.ScrollPaneConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import manager.dao.DeleteDAO;
import manager.dto.DeleteDTO;

public class DeleteBookList extends JPanel implements ActionListener, KeyListener {

	private JButton searchBtn, outputBtn;

	private JTextField searchT, codeT, titleT, authorT, genreT, publisherT , deleteDateT;
	private JLabel labelL, codeL, titleL, authorL, genreL, publisherL, deleteDateL;
	private JComboBox<String> searchCombo;

	private DefaultTableModel model;
	private JTable table;

	private JPanel p, p1, p2, p3, pp12, p4, p5, pp45;

	private DeleteDTO deleteDTO = new DeleteDTO();
	private DeleteDAO deleteDAO = new DeleteDAO();
	static List<DeleteDTO> deleteList = new ArrayList<DeleteDTO>();

	public void paintComponent(Graphics g) {
		Dimension d = getSize();
		ImageIcon img = new ImageIcon("Images/DeleteBook.jpg");
		g.drawImage(img.getImage(), 0, 0, d.width, d.height, null);
	}

	public DeleteBookList() {

		// 라벨 생성
		labelL = new JLabel("DeleteBookList");
		labelL.setFont(new Font("고딕체", Font.BOLD, 100));
		labelL.setForeground(new Color(0, 0, 0,0));

		codeL = new JLabel("도서코드");
		titleL = new JLabel("도서명");
		authorL = new JLabel("저자");
		publisherL = new JLabel("출판사");
		genreL = new JLabel("장르");
		
		//0522 추가
		deleteDateL = new JLabel("삭제일");
		
		// 버튼 생성
		searchBtn = new JButton("검색");
		outputBtn = new JButton("전체조회");

		// 텍스트필드 생성
		searchT = new JTextField("도서명을 검색해주세요", 30);

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
		deleteDateT = new JTextField(10);
		deleteDateT.setEditable(false);

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
		v.add("삭제일");
		
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
		table.getColumnModel().getColumn(1).setPreferredWidth(420);
		table.getColumnModel().getColumn(2).setPreferredWidth(100);
		table.getColumnModel().getColumn(3).setPreferredWidth(100);
		table.getColumnModel().getColumn(4).setPreferredWidth(100);

		// table sorter
		RowSorter<TableModel> sorter1 = new TableRowSorter<TableModel>(model);
		table.setRowSorter(sorter1);
				
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
		p4.add(deleteDateL);
		p4.add(deleteDateT);

		// 추가,삭제,수정 버튼
		p5 = new JPanel();
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
		outputBtn.addActionListener(this);
		searchT.addKeyListener(this);

		// 마우스 이벤트
		table.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				
				int i = table.getSelectedRow();
				//int i = table.convertRowIndexToModel(table.getSelectedRow());

				titleT.setText((table.getValueAt(i, 1)+""));
				codeT.setText((table.getValueAt(i, 0)+""));
				authorT.setText((table.getValueAt(i, 2)+""));
				publisherT.setText((table.getValueAt(i, 3)+""));
				genreT.setText((table.getValueAt(i, 4)+""));				
				// 추가
				deleteDateT.setText((table.getValueAt(i, 5)+""));
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
		} else if (e.getSource() == outputBtn) {
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

		List<DeleteDTO> list = deleteDAO.getDeleteList();
		for (DeleteDTO deleteDTO : list) {
			Vector<String> v = new Vector<String>();
			v.add(deleteDTO.getCode() + ""); 
			v.add(deleteDTO.getTitle());
			v.add(deleteDTO.getAuthor());
			v.add(deleteDTO.getPublisher());
			v.add(deleteDTO.getGenre());	
			//추가
			v.add(deleteDTO.getDeleteDate()+"");
			
			model.addRow(v);
		}
	}// output()

	// 테이블 검색
	public void search() {
		String search = searchT.getText();
		String comboBox = (String) searchCombo.getSelectedItem();
		deleteList = deleteDAO.getDeleteList();

		if (comboBox.equals("전체 도서")) {
			for (int i = 0; i < deleteList.size(); i++) {
				if (search.equals(deleteList.get(i).getTitle()) || deleteList.get(i).getTitle().contains(search)
						|| search.equals(""))
					outputRecord(i);
			}
		} else if (comboBox.equals("소설")) {
			searchTitle();
		} else if (comboBox.equals("시/에세이")) {
			searchTitle();
		} else if (comboBox.equals("자기계발")) {
			searchTitle();
		} else if (comboBox.equals("경제/경영")) {
			searchTitle();
		} else if (comboBox.equals("역사")) {
			searchTitle();
		} else if (comboBox.equals("인문")) {
			searchTitle();
		} else if (comboBox.equals("역사")) {
			searchTitle();
		} else if (comboBox.equals("사회")) {
			searchTitle();
		} else if (comboBox.equals("과학")) {
			searchTitle();
		}

	}// search()

	public void searchTitle() {

		String search = searchT.getText();
		String comboBox = (String) searchCombo.getSelectedItem();
		deleteList = deleteDAO.getDeleteList();

		for (int i = 0; i < deleteList.size(); i++) {
			if (comboBox.equals(deleteList.get(i).getGenre()) && search.equals(""))
				outputRecord(i);
			else if (comboBox.equals(deleteList.get(i).getGenre()) && search.equals(deleteList.get(i).getTitle()))
				outputRecord(i);
			else if (comboBox.equals(deleteList.get(i).getGenre()) && deleteList.get(i).getTitle().contains(search))
				outputRecord(i);
		}
	}

	// 테이블 검색 output
	public void outputRecord(int i) {
		Vector<String> v = new Vector<String>();
		v.add(deleteList.get(i).getCode() + "");
		v.add(deleteList.get(i).getTitle());
		v.add(deleteList.get(i).getAuthor());
		v.add(deleteList.get(i).getPublisher());
		v.add(deleteList.get(i).getGenre());		
		// 추가
		v.add(deleteList.get(i).getDeleteDate()+"");
		
		model.addRow(v);
	}// outputRecord()

}
