package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

class ChatHandler extends Thread {
	private BufferedReader reader;
	private PrintWriter writer;
	private Socket socket;
	private List<ChatHandler> list;
	private String nickName;
	private String sendTo;

	public ChatHandler(Socket socket, List<ChatHandler> list) throws IOException {
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

			String line;
			while (true) {
				// 메세지
				line = reader.readLine();
				String[] ar = null;

				if (line != null) {
					ar = line.split(":");

					if (ar.length == 1) { // 보낸이가 회원
						line = ar[0];
						if (line == null || line.equals("exit")) {
							// 나가려고 exit를 보낸 클라이언트에게 답변 보내기
							writer.println("exit");
							writer.flush();

							reader.close();
							writer.close();
							socket.close();

							// 남아있는 클라이언트에게 퇴장메세지 보내기
							broadcast("exit");
							list.remove(this);

							break;
						} // if
					} else {// 보낸이가 매니저
						sendTo = ar[0];
						line = ar[1];
						if (line == null || line.equals("exit")) {
							// 나가려고 exit를 보낸 클라이언트에게 답변 보내기
							writer.println(sendTo + ":exit");
							writer.flush();

							// 남아있는 클라이언트에게 퇴장메세지 보내기
							broadcast(nickName + "퇴장합니다.");

							break;
						} // if
					}
					broadcast("[" + nickName + "] " + line);
				}

			} // while

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public String getNickName() {
		return nickName;
	}

	public void broadcast(String msg) {
		// System.out.println(msg);
		// System.out.println("매니저한테= " + sendTo + ":" + msg);

		// System.out.println("손님한테= " + msg);

		if (nickName.equals("관리자")) {
			System.out.println("매니저가 매니저로부터 받음 =" + msg);
			writer.println(sendTo + ":" + msg);
			writer.flush();
			for (int i = 1; i < list.size(); i++) {
				if (list.get(i).getNickName().equals(sendTo)) {
					list.get(i).writer.println(msg);
					list.get(i).writer.flush();
					System.out.println("매니저가 손님한테= " + msg);
				}
			}

		} else {// 회원일때,
//			for(int i=0; i<list.size(); i++) {
//				System.out.println("list이름="+list.get(i).getNickName());
//			}

			list.get(0).writer.println(nickName + ":" + msg);
			list.get(0).writer.flush();
			System.out.println("손님이 매니저한테= " + nickName + ":" + msg);

			writer.println(msg);
			writer.flush();
			System.out.println("손님이 손님한테= " + msg);

		}

	}
}
