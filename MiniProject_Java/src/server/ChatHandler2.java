package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

class ChatHandler2 extends Thread {
	private BufferedReader reader;
	private PrintWriter writer;
	private Socket socket;
	private List<ChatHandler2> list;
	private String nickName;
	private String sendTo;
	private boolean first = false;

	public ChatHandler2(Socket socket, List<ChatHandler2> list) throws IOException {
		this.socket = socket;
		this.list = list;
		System.out.println("핸들러 입장");
		reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		writer = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
	}

	public void run() {
		try {
			// 클라이언트로부터 받기
			// 닉네임
			nickName = reader.readLine();
			if (!nickName.equals("Manager")) {
				broadcast(nickName + "님이 문의하십니다.");
			}

			String line;
			while (true) {
				// 메세지
				first = true;
				line = reader.readLine();

				String ar[] = line.split(":");
				if (ar.length == 1) {
					sendTo = null;
					line = ar[0];
					if (line == null || line.equals("exit")) {
						// 나가려고 exit를 보낸 클라이언트에게 답변 보내기
						writer.println("exit");
						writer.flush();

						reader.close();
						writer.close();
						socket.close();

						// 남아있는 클라이언트에게 퇴장메세지 보내기
						broadcast(nickName + "퇴장합니다.");
						list.remove(this);

						break;
					} // if
				} else {// 보낸이가 매니저
					sendTo = ar[0];
					line = ar[1];
				}

				broadcast("[" + nickName + "] " + line);
			} // while

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getNickName() {
		return nickName;
	}

	public void broadcast(String msg) {

		list.get(0).writer.println(msg);
		list.get(0).writer.flush();
		// System.out.println("매니저한테= " + sendTo + ":" + msg);

		if (nickName.equals("Manager")) {
			if (sendTo == null) {
				for (int i = 1; i < list.size(); i++) {
					System.out.println(msg);
					list.get(i).writer.println("[전체공지]"+msg);
					list.get(i).writer.flush();
				}
			}
			for (int i = 1; i < list.size(); i++) {
				if (list.get(i).getNickName().equals(sendTo)) {
					list.get(i).writer.println(msg);
					list.get(i).writer.flush();
					// System.out.println("손님한테= " + msg);
				}
			}

		} else {// 회원일때,
			if (first == false) {
				for (int i = 1; i < list.size(); i++) {
					if (list.get(i).getNickName().equals(nickName)) {
						list.get(i).writer.println("관리자에게 문의를 요청했습니다.");
						list.get(i).writer.flush();
					}
				}

			} else {
				for (int i = 1; i < list.size(); i++) {
					if (list.get(i).getNickName().equals(nickName)) {
						list.get(i).writer.println(msg);
						list.get(i).writer.flush();
						// System.out.println("손님한테= " + msg);
					}
				}
			}

		}

	}
}
