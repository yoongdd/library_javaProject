package MemberFrame;

import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

public class BasicFrameMember extends JFrame {
	public BookSearch p1;
	public MyPage p2;
	public SeatSelect p3;
	private static int key;

	public BasicFrameMember() {

	}

	public BasicFrameMember(int key) {
		super("Library");
		this.key = key;

		JTabbedPane t = new JTabbedPane(); // JTabbedPane생성

		p1 = new BookSearch();
		p2 = new MyPage();
		p3 = new SeatSelect();

		JTabbedPane tab = new JTabbedPane();
		tab.add(p1, "도서검색");
		tab.add(p2, "마이페이지");
		tab.add(p3, "좌석현황");

		add(tab);

		setBounds(700, 100, 1100, 800);
		setVisible(true);
		setResizable(false);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int result = JOptionPane.showConfirmDialog(null, "종료하시겠습니까?");
				if (result == JOptionPane.YES_OPTION)
					System.exit(0);
			}
		});
	}

	public int getKey() {
		return key;
	}

}
