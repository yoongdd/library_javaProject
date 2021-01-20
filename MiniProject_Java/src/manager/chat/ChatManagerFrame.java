package manager.chat;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;

public class ChatManagerFrame extends JFrame {
	private JTextArea output;
	private JTextField input;
	private JButton sendBtn;

	private String id;

	public ChatManagerFrame(String id) {
		// TODO Auto-generated constructor stub
		this.setTitle("관리자 채팅" + id);
		this.id = id;

		output = new JTextArea();
		output.setFont(new Font("돋움체", Font.BOLD, 16));
		output.setEditable(false);
		JScrollPane scroll = new JScrollPane(output);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

		input = new JTextField();
		sendBtn = new JButton("보내기");

		JPanel p = new JPanel();
		p.setLayout(new BorderLayout());
		p.add("Center", input);
		p.add("East", sendBtn);

		Container c = this.getContentPane();
		c.add("Center", scroll);
		c.add("South", p);

		setBounds(400, 100, 300, 500);
		setVisible(true);
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		input.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String data = input.getText();
				ChatManager.sendTo(id + ":" + data);
				input.setText("");

			}

		});

		sendBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				String data = input.getText();
				ChatManager.sendTo(id + ":" + data);
				input.setText("");

			}

		});

	}

	public String getId() {
		return id;
	}

	public void setMsg(String msg) {
		output.append(msg + "\n");
		int pos = output.getText().length();
		output.setCaretPosition(pos);
	}

}
