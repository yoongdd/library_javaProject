package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ChatServer {
	private ServerSocket serverSocket;
	private List<ChatHandler> list;
	
	public ChatServer(){
		try{
			serverSocket = new ServerSocket(9500);
			System.out.println("서버준비완료..");

			list = new ArrayList<ChatHandler>();

			while(true){
				Socket socket = serverSocket.accept();//낚아챈다

				ChatHandler handler = new ChatHandler(socket, list);//스레드 생성
				handler.start();//스레드 시작-스레드 실행(run())

				list.add(handler);
				System.out.println(list.size());
			}//while
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		new ChatServer();
	}

}
