package com.gaeun.ch6_socket;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

	ArrayList<ChatMessage> list;
	MyAdapter ap;

	ListView listView;
	ImageView sendBtn;
	EditText msgEdit;

	// thread 제어용
	boolean flagConnection = true;
	boolean isConnected = false;
	boolean flagRead = true;

	Handler writeHandler;

	Socket socket;
	BufferedInputStream bin;
	BufferedOutputStream bout;

	SocketThread st; // 연결관리 thread
	ReadThread rt;
	WriteThread wt;

	// 깡샘 서버
	String serverIp = "10.5.6.100";
	int serverPort = 7070;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		listView = findViewById(R.id.lab1_list);
		sendBtn = findViewById(R.id.lab1_send_btn);
		msgEdit = findViewById(R.id.lab1_send_text);

		sendBtn.setOnClickListener(this);

		list = new ArrayList<ChatMessage>();
		ap = new MyAdapter(this, R.layout.chat_item, list);
		listView.setAdapter(ap);

		sendBtn.setEnabled(false);
		msgEdit.setEnabled(false);

	}

	private void addMessage(String who, String msg) {
		ChatMessage vo = new ChatMessage();
		vo.who = who;
		vo.msg = msg;
		list.add(vo);
		ap.notifyDataSetChanged();
		listView.setSelection(list.size() - 1);
	}


	@Override
	public void onClick(View v) {
		if (!msgEdit.getText().toString().trim().equals("")) {
			Message msg = new Message();
			msg.obj = msgEdit.getText().toString();
			writeHandler.sendMessage(msg);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		st = new SocketThread();
		st.start();
	}

	@Override
	protected void onStop() {
		super.onStop();

		flagConnection = false;
		isConnected = false;

		if (socket != null) {
			flagRead = false;
			writeHandler.getLooper().quit();
			try {
				bout.close();
				bin.close();
				socket.close();
			} catch (IOException e) {
			}
		}
	}

	private void showToast(String message) {
		Toast toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
		toast.show();
	}

	Handler mainHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 10) {
				//connection ok~~
				showToast("connection ok~~");
				sendBtn.setEnabled(true);
				msgEdit.setEnabled(true);
			} else if (msg.what == 20) {
				//connection fail~~~
				showToast("connection fail~~");
				sendBtn.setEnabled(false);
				msgEdit.setEnabled(false);
			} else if (msg.what == 100) {
				//message read....
				addMessage("you", (String) msg.obj);
			} else if (msg.what == 200) {
				//message write...
				addMessage("me", (String) msg.obj);
			}
		}
	};


	// Connection 관리 thread
	class SocketThread extends Thread {

		public void run() {

			while (flagConnection) {
				try {
					// 연결상태를 지속적으로 체크, 연결이 되지 않은 상황이면 다시 연결을 시도
					if (!isConnected) {
						socket = new Socket();
						SocketAddress remoteAddr = new InetSocketAddress(serverIp, serverPort);
						socket.connect(remoteAddr, 10000);

						bout = new BufferedOutputStream(socket.getOutputStream());
						bin = new BufferedInputStream(socket.getInputStream());

						//새로운 연결이 되었음.
						// read, write thread가 구동중이라면, 이전 연결정보로 동작중인 것이기 때문에
						// 종료시키고 다시 Start 해야함.
						if (rt != null) {
							flagRead = false;
						}
						if (wt != null) {
							writeHandler.getLooper().quit();
						}

						// Start
						wt = new WriteThread();
						wt.start();

						rt = new ReadThread();
						rt.start();

						isConnected = true;

						// 연결 성공, 화면 조정 필요
						// Thread 다르니까 mainThread에 view 조정 요청
						Message msg = new Message();    // 의뢰 내용
						msg.what = 10;                  // 의뢰 구분자, 개발자 임의숫자임
						mainHandler.sendMessage(msg);
					} else {
						SystemClock.sleep(10000);
					}

				} catch (Exception e) {
					e.printStackTrace();
					SystemClock.sleep(10000);
				}

			}
		}
	}

	// API level 25부터는 socket write가 꼭 thread에 의해 처리되어야 함.
	class WriteThread extends Thread {

		// Thread 내에서 looper를 구동시키면, 자동으로 looper가 감지해야하는 message queue가 만들어짐
		// 아래처럼 작성하면, Thread 내부에서 Loop문 돌리지 않아도 looper가 동료되지 않는한
		// thread는 죽지 않음
		// (write thread를 죽일때 flag변경이 아니라 looper.quit으로 한게 이 방법으로 했기 때문임 ^^)
		@Override
		public void run() {
			Looper.prepare();
			// looper 가 감지하는 message queue에 queue가 하나 담겼을때 실행될 handler 정의
			writeHandler = new Handler() {
				@Override
				public void handleMessage(Message msg) {
					try {
						bout.write(((String) msg.obj).getBytes());
						bout.flush();

						Message message = new Message();
						message.what = 200;
						message.obj = msg.obj;
						mainHandler.sendMessage(message);
					} catch (Exception e) {
						// 플래그 바꿔주면 Connection Thread에서 다시 알아서 맺을것
						isConnected = false;
					}
				}
			};
			Looper.loop();
		}
	}

	class ReadThread extends Thread {

		@Override
		public void run() {
			byte[] buffer = null;
			while (flagRead) {
				buffer = new byte[1024];
				try {
					String message = null;

					// read() 함수 만나면 서버로부터 데이터 들어올때까지 대기 상태
					// 넘어온 데이터를 buffer에 담아주고, 몇바이트 읽었는지 리턴
					int size = bin.read(buffer);

					if (size > 0) {
						message = new String(buffer, 0, size, "utf-8");
						if (message != null && !message.equals("")) {
							Message msg = new Message();
							msg.what = 100;
							msg.obj = message;
							mainHandler.sendMessage(msg);
						}
					} else {
						isConnected = false;
					}
				} catch (Exception e) {
					isConnected = false;
				}
			}

			Message msg=  new Message();
			msg.what = 20;
			mainHandler.sendMessage(msg);
		}

	}


}

class ChatMessage {
	String who;
	String msg;
}

class MyAdapter extends ArrayAdapter<ChatMessage> {
	ArrayList<ChatMessage> list;
	int resId;
	Context context;

	public MyAdapter(Context context, int resId, ArrayList<ChatMessage> list) {
		super(context, resId, list);
		this.context = context;
		this.resId = resId;
		this.list = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		convertView = inflater.inflate(resId, null);


		TextView msgView = (TextView) convertView.findViewById(R.id.lab1_item_msg);
		RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) msgView
				.getLayoutParams();

		ChatMessage msg = list.get(position);
		if (msg.who.equals("me")) {
			params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,
					RelativeLayout.TRUE);
			msgView.setTextColor(Color.WHITE);
			msgView.setBackgroundResource(R.drawable.chat_right);
		} else if (msg.who.equals("you")) {
			params.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
					RelativeLayout.TRUE);
			msgView.setBackgroundResource(R.drawable.chat_left);
		}
		msgView.setText(msg.msg);

		return convertView;

	}
}

