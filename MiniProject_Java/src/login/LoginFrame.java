package login;

import java.awt.Container;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

public class LoginFrame extends JFrame {
	static LoginFrame login;
	public Login member;

	LoginFrame() {
		super("Login");

		Container c = this.getContentPane();

		member = new Login();

		JTabbedPane tab = new JTabbedPane(JTabbedPane.NORTH);
		tab.add(member, "로그인");

		c.add(tab);

		setBounds(700, 100, 300, 450);
		setVisible(true);
		setResizable(false);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				int result = JOptionPane.showConfirmDialog(null, "종료하시겠습니까?", "창 종료", JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION)
					System.exit(0);
				else if (result == JOptionPane.NO_OPTION)
					;
				setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
			}

		});
	}

	public static void main(String[] args) {
		new LoginFrame();
	}

}
