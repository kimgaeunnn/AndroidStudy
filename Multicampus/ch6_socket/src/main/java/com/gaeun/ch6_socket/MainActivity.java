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
	BufferedInputStream bIn;
	BufferedOutputStream bOut;

	SocketThread st; // 연결관리 thread


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


	class SocketThread extends Thread {

		public void run() {

			//add~~~~~~~~~~~~~~~~~


		}
	}

	class WriteThread extends Thread {

		@Override
		public void run() {
			//add~~~~~~~~~~

		}
	}

	class ReadThread extends Thread {

		@Override
		public void run() {
			//add~~~~~~~~~~~~~~~~~~~


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

