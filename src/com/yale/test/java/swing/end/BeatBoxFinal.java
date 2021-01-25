package com.yale.test.java.swing.end;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


/*
 * BeatBox客户端最终版本
 */
public class BeatBoxFinal {
	JFrame theFrame;
	JPanel mainPanel;
	JList incomingList;
	JTextField userMessage;
	ArrayList<JCheckBox> checkboxList;
	int nextNum;
	Vector<String> listVector = new Vector<String>();
	String userName;
	ObjectOutputStream out;
	ObjectInputStream in;
	HashMap<String, boolean[]> otherSeqsMap = new HashMap<String, boolean[]>();
	Sequencer sequencer;
	Sequence sequence;
	Sequence mySequence = null;
	Track track;
	
	String[] instrumentNames = {"Bass Drum", "Closed Hi-Hat", "Open Hi-Hat", "Acoustic Snare", "Crash Cymbal",
			"Hand Clap", "High Tom", "Hi Bongo", "Maracas", "Whistle", "Low Conga", "Cowbell", "Vibraslap",
			"Low-mid Tom", "High Agogo", "Open Hi Conga"};
	int[] instruments = {35, 42, 46, 38, 49, 39, 50, 60, 70, 72, 64, 56, 58, 47, 67, 63};
	
	public void startUp(String name) {
		userName = name;
		try {
			Socket sock = new Socket("127.0.0.1", 4242);
			out = new ObjectOutputStream(sock.getOutputStream());
			in = new ObjectInputStream(sock.getInputStream());
			Thread remote = new Thread(new RemoteReader());
			remote.start();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		setUpMidi();
		buildGUI();
	}
	public void buildGUI() {
		theFrame = new JFrame("Cyber BeatBox");
		BorderLayout layout = new BorderLayout();
		JPanel background = new JPanel(layout);
		background.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		checkboxList = new ArrayList<JCheckBox>();
		
		Box buttonBox = new Box(BoxLayout.Y_AXIS);
		JButton start = new JButton("start");
		start.addActionListener(new MyStartListener());
		buttonBox.add(start);
		
		JButton stop = new JButton("Stop");
		stop.addActionListener(new MyStopListener());
		buttonBox.add(stop);
		
		JButton upTempo = new JButton("Tempo Up");
		upTempo.addActionListener(new MyUpTempoListener());
		buttonBox.add(upTempo);
		
		JButton downTempo = new JButton("Tempo Down");
		downTempo.addActionListener(new MyDownTempoListener());
		buttonBox.add(downTempo);
		
		JButton sendIt = new JButton("sendIt");
		sendIt.addActionListener(new MySendListener());
		buttonBox.add(sendIt);
		
		userMessage = new JTextField();
		buttonBox.add(userMessage);
		
		incomingList = new JList();
		incomingList.addListSelectionListener(new MyListSelectionListener());
		incomingList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);//单选
		JScrollPane theList = new JScrollPane(incomingList);
		buttonBox.add(theList);
		incomingList.setListData(listVector);
		
		Box nameBox = new Box(BoxLayout.Y_AXIS);
		for (int i=0; i<16; i++) {
			nameBox.add(new Label(instrumentNames[i]));
		}
		
		background.add(BorderLayout.EAST, buttonBox);
		background.add(BorderLayout.WEST, nameBox);
		
		theFrame.getContentPane().add(background);
		GridLayout grid = new GridLayout(16, 16);
		grid.setVgap(1);//间隙
		grid.setHgap(2);//间隙
		mainPanel = new JPanel(grid);
		background.add(BorderLayout.CENTER, mainPanel);
		
		for (int i=0; i<256; i++) {
			JCheckBox c = new JCheckBox();
			c.setSelected(false);
			checkboxList.add(c);
			mainPanel.add(c);
		}
		
		theFrame.setBounds(50, 50, 300, 300);
		theFrame.pack();//pack()方法会使window的大小符合内含组件的大小。
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		theFrame.setVisible(true);
	}
	
	public void setUpMidi() {
		try {
			sequencer = MidiSystem.getSequencer();
			sequencer.open();
			sequence = new Sequence(Sequence.PPQ, 4);
			track = sequence.createTrack();
			sequencer.setTempoInBPM(120);
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}
	
	public void buildTrackAndStart() {
		ArrayList<Integer> trackList = null;
		sequence.deleteTrack(track);
		track = sequence.createTrack();
		for (int i=0; i<16; i++) {
			trackList = new ArrayList<Integer>();
			for (int j=0; j<16; j++) {
				JCheckBox jc = (JCheckBox)checkboxList.get(j + (16*i));
				if (jc.isSelected()) {
					int key = instruments[i];
					trackList.add(new Integer(key));
				} else {
					trackList.add(null);
				}
			}
			makeTracks(trackList);
		}
		try {
			track.add(makeEvent(192, 9, 1, 0, 15));
			sequencer.setSequence(sequence);
			sequencer.setLoopCount(sequencer.LOOP_CONTINUOUSLY);
			sequencer.start();
			sequencer.setTempoInBPM(120);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}
	
	public class MyStartListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			buildTrackAndStart();
		}
	}
	
	public class MyStopListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			sequencer.stop();
		}
	}
	
	public class MyUpTempoListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent a) {
			float tempoFactor = sequencer.getTempoFactor();
			sequencer.setTempoFactor((float)(tempoFactor * 1.03));
		}
	}
	
	public class MyDownTempoListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			float tempoFactor = sequencer.getTempoFactor();
			sequencer.setTempoFactor((float)(tempoFactor * .97));
		}
	}
	
	public class MySendListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			boolean[] checkboxState = new boolean[256];
			for(int i=0; i<256; i++) {
				JCheckBox check = (JCheckBox)checkboxList.get(i);
				if (check.isSelected()) {
					checkboxState[i] = true;
				}
			}
			String messageToSend = null;
			try {
				out.writeObject(userName + nextNum++ + ":" + userMessage.getText());
				out.writeObject(checkboxState);
			} catch (IOException e1) {
				e1.printStackTrace();
				System.err.println("Sorry dude. Could not send it to the server.");
			}
			userMessage.setText("");
		}
	}
	public class MyListSelectionListener implements ListSelectionListener {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			if (!e.getValueIsAdjusting()) {
				String selected = (String)incomingList.getSelectedValue();
				if (selected != null) {
					boolean[] selectedState = (boolean[])otherSeqsMap.get(selected);
					changeSequence(selectedState);
					sequencer.stop();
					buildTrackAndStart();
				}
			}
		}
	}
	public class RemoteReader implements Runnable {
		boolean[] checkboxState = null;
		String nameToShow = null;
		Object obj = null;
		
		@Override
		public void run() {
			try {
				while ((obj = in.readObject()) != null) {
					System.out.println("got an object from server");
					System.out.println(obj.getClass());
					String nameToShow = (String)obj;
					checkboxState = (boolean[])in.readObject();
					otherSeqsMap.put(nameToShow, checkboxState);
					listVector.add(nameToShow);
					incomingList.setListData(listVector);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public class MyPlayMineListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			sequence = mySequence;
		}
	}
	
	public void changeSequence(boolean[] checkboxState) {
		for (int i=0; i<256; i++) {
			JCheckBox check = (JCheckBox)checkboxList.get(i);
			if (checkboxState[i]) {
				check.setSelected(true);
			} else {
				check.setSelected(false);
			}
		}
	}
	
	public void makeTracks(ArrayList list) {
		Iterator it = list.iterator();
		for (int i=0; i<16; i++) {
			Integer num = (Integer)it.next();
			if (num != null) {
				int numKey = num.intValue();
				track.add(makeEvent(144, 9, numKey, 100, i));
				track.add(makeEvent(128, 9, numKey, 100, i+1));
			}
		}
	}
	
	public MidiEvent makeEvent(int comd, int chan, int one, int two, int tick) {
		MidiEvent event = null;
		try {
			ShortMessage a = new ShortMessage();
			a.setMessage(comd, chan, one, two);
			event = new MidiEvent(a, tick);
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
		return event;
	}
	
	/*
	 * 有哪些方法可以改善这个程序？
	 * 你可以从以下的方向考虑
	 * (1)加载新的样式时,目前的样式就会消失.如果正在设计新样式,则辛苦都白费了。你也许会希望有个提示对话框确认是否要先存盘。
	 * (2)输入不正确命令栏参数会导致异常,把程序改成在这种情况下能够使用默认值或给使用者一个提示,尽可能地让程序优雅的运行而不会粗鲁的直接挂掉。
	 * (3)随机产生节奏样式(译者注:这是个烂注意,相信我,计算机只能随机产生噪音)。
	 * 这个是BeatBox的最终版本,它会连接到MisicServer来让你传送并接收其他用户的信息以及节奏样式
	 *  功能:输入信息并按下sendIt按钮就会把你的信息和你的节奏样式发送出去,然后对方点击你的信息就会加载你的节奏样式
	 * 来自《Head First Java》第316页,第418页,第430页,第462也,第472也,第523页,第649也。中国电力出版社
	 * 第347页：
	 * 第15章:目的地,完成之后我们就会有一个BeatBox程序,它同时也是Drum,Chat的客户端程序。我们需要学习GUI,IO,网络和线程等。
	 * 第12章:MIDI事件: 这一章会创建出一个小小的"MTV",它会根据MIDI音乐的节奏来画出随机的字符。这一章的内容能让我们学习到MIDI事件的处理。
	 * 第13章:独立的BeatBox:我们确实创建出BeatBox,GUI等功能。但这还是很受限的版本。没有存储和还原的功能,也无法通过网络通信。
	 * 第14章:存储和还原:现在可以存盘与加载还原以再度播放了。这让我们可以准备好完成最终版本,把存储发到网络的另一端的chat服务器。
	 *
	 */
	public static void main (String[] args) {
		/*
		 * CMD命令行main方法传参数: java BeatBoxFinal 参数
		 * 启动这个类之前必须先启动MisicServer.java这个类
		 * 然后这个类BeatBoxFinal.java可以启动多个,模拟多个用户
		 * 每次启动的时候可以将startUp的参数(用户名)改一下
		 */
		new BeatBoxFinal().startUp("HeadFistJava77");//args[0] si your user ID/screen name
	}
}
