package manager.chat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.plaf.synth.SynthSeparatorUI;

import member.dto.PersonDTO;

public class ChatManager extends Thread {
	private Socket socket;
	private static BufferedReader reader;
	private static PrintWriter writer;
	private List<ChatManagerFrame> frames;
	private List<ChatDTO> list;
	private ChatDAO chatDAO;

	public ChatManager() {
		// TODO Auto-generated constructor stub
		// 서버IP
		chatDAO = new ChatDAO();
		frames = new ArrayList<ChatManagerFrame>();
		String serverIP = "192.168.0.65";
		if (serverIP == null || serverIP.length() == 0) {
			System.out.println("서버IP가 입력되지 않았습니다");
			System.exit(0);
		}

		// 닉네임
		String nickName = "관리자";

		try {
			// 소켓 생성
			socket = new Socket(serverIP, 9500);

			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

		} catch (UnknownHostException e) {
			System.out.println("서버를 찾을 수 없습니다");
			e.printStackTrace();
			System.exit(0);
		} catch (IOException e) {
			System.out.println("서버와 연결이 안되었습니다");
			e.printStackTrace();
			System.exit(0);
		}
		this.start();// 스레드 시작 - 스레드 실행

		// 서버로 닉네임 보내기
		writer.println(nickName);
		writer.flush();
		client();

	}

	public void client() {
		
		int sleepSec = 5;

		ScheduledThreadPoolExecutor stpe = new ScheduledThreadPoolExecutor(1);
		stpe.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {

				list = chatDAO.getChatList();
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getCheck() == 0) {
						ChatManagerFrame cf = new ChatManagerFrame(list.get(i).getId());
						list.get(i).setCheck(1);
						chatDAO.chatUpdate(list.get(i));
						frames.add(cf);
					} else if (list.get(i).getComplited() == 1) {

						for (int j = 0; j < frames.size(); j++) {
							if (frames.get(j).getId().equals(list.get(i).getId())) {
								frames.get(j).dispose();
								frames.remove(frames.get(j));
								chatDAO.chatDelete(list.get(i).getId());
							}
						}

					}
				}

			}

		}, 0, sleepSec, TimeUnit.SECONDS);
	}

	@Override
	public void run() {
		// 서버로부터 받기
		String line = null;
		while (true) {

			try {
				line = reader.readLine();
				System.out.println("서버로부터 받은 내려받은 내용" + line);

				String ar[] = line.split(":");
				if (ar.length >= 2) { // 회원으로부터 메시지가 왔을때,
					String id = ar[0];
					line = ar[1]; // 메시지 내용넣어준다.

					if (ar[1].equals("exit")) {
						for (int i = 0; i < frames.size(); i++) {
							if (frames.get(i).getId().equals(ar[0])) {
								line = ar[0] + "님이 퇴장하셨습니다.";
							}
						}

					}

					for (int i = 0; i < frames.size(); i++) {

						if (frames.get(i).getId().equals(ar[0])) {
							frames.get(i).setMsg(line);
						}
					}

				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		} // while

	}

	public static void sendTo(String msg) {
		writer.println(msg);
		writer.flush();

	}

}
