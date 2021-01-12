package com.yale.test.java.swing;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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
import javax.swing.JPanel;

public class BeatBox {
	JPanel mainPanel;
	ArrayList<JCheckBox> checkboxList;
	Sequencer sequencer;
	Sequence sequence;
	Track track;
	JFrame theFrame;
	//乐器的名字
	String[] instrumentNames = {"Bass Drum", "Closed Hi-Hat", "Open Hi-Hat", "Acoustic Snare", "Crash Cymbal", 
			"Hand Clap", "High Tom", "Hi Bongo", "Maracas", "Whistle", "Low Conga", "Cowbell", "Vibrasslap",
			"Low-mid Tom", "High Agogo", "Open Hi Conga"};
	//实际的乐器关键字,例如说35是Bass,42是Closed Hi-Hat
	int[] instruments = {35, 42, 46, 38, 49, 39, 50, 60, 70, 72, 64, 56, 58, 47, 67, 63};
	
	public void buildGUI() throws MidiUnavailableException, InvalidMidiDataException {
		theFrame = new JFrame("Cyber BeatBox");
		theFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		BorderLayout layout = new BorderLayout();
		JPanel background = new JPanel(layout);
		//设定面板panel上摆设组件时的空白边缘
		background.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		
		checkboxList = new ArrayList<JCheckBox>();
		Box buttonBox = new Box(BoxLayout.Y_AXIS);
		
		JButton start = new JButton("Start");
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
		
		Box nameBox = new Box(BoxLayout.Y_AXIS);
		for(int i=0; i<16; i++) {
			nameBox.add(new Label(instrumentNames[i]));
		}
		
		background.add(BorderLayout.EAST, buttonBox);
		background.add(BorderLayout.WEST, nameBox);
		
		theFrame.getContentPane().add(background);
		
		GridLayout grid = new GridLayout(16, 16);
		grid.setVgap(1);
		grid.setHgap(1);
		mainPanel = new JPanel(grid);
		background.add(BorderLayout.CENTER, mainPanel);
		
		for (int i=0; i<256; i++) {
			JCheckBox c = new JCheckBox();
			c.setSelected(false);
			checkboxList.add(c);
			mainPanel.add(c);
		}
		
		setUpMidi();
		
		theFrame.setBounds(50, 50, 300, 300);
		theFrame.pack();//pack方法会使window的大小符合内含组件的大小。
		theFrame.setVisible(true);
	}
	
	public void setUpMidi() throws MidiUnavailableException, InvalidMidiDataException {//一般的MIDI设置程序代码
		sequencer = MidiSystem.getSequencer();
		sequencer.open();
		sequence = new Sequence(Sequence.PPQ, 4);
		track = sequence.createTrack();
		sequencer.setTempoInBPM(120);
	}
	
	/**
	 * 重点在这里,此处会将复选框状态转换为MIDI事件并加到track上
	 * @throws InvalidMidiDataException 
	 */
	public void buildTrackAndStart() throws InvalidMidiDataException {
		int[] trackList = null;//创建出16个元素的数组来存储一项乐器的值。如果该节应该要演奏,其值会是关键字值,否则值为零。
		sequence.deleteTrack(track);//清除掉旧的track
		track = sequence.createTrack();//创建一个新的track
		for (int i=0; i<16; i++) {//对每个乐器都执行一次
			trackList = new int[16];
			int key = instruments[i];//设定代表乐器的关键字
			for(int j=0; j<16; j++) {//对每一拍执行一次
				JCheckBox jc = (JCheckBox)checkboxList.get(j + (16*i));
				if (jc.isSelected()) {
					//如果有勾选,将关键字值放到数组的该位置上,不然的话就补0
					trackList[j] = key;
				} else {
					trackList[j] = 0;
				}
			}
			makeTracks(trackList);//创建此乐器的事件并加到track上
			track.add(makeEvent(176, 1, 127, 0, 16));
		}
		track.add(makeEvent(192, 9, 1, 0, 15));//确保第16拍有事件,否则beatbox不会重复播放
		sequencer.setSequence(sequence);
		sequencer.setLoopCount(sequencer.LOOP_CONTINUOUSLY);//指定无穷的重复次数
		sequencer.start();
		sequencer.setTempoInBPM(120);//开始播放
	}
	
	public class MyStartListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent a) {
			try {
				buildTrackAndStart();
			} catch (InvalidMidiDataException e) {
				e.printStackTrace();
			}
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
		public void actionPerformed(ActionEvent e) {//其实就是调整播放速度,越大播放速度越快
			float tempoFactor = sequencer.getTempoFactor();
			System.out.println("向上调整:" + tempoFactor);
			sequencer.setTempoFactor((float)(tempoFactor * 1.03));//节奏因子,预设为1.0,每次调整3%
		}
	}
	
	public class MyDownTempoListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			float tempoFactor = sequencer.getTempoFactor();
			System.out.println("向下down调整:" +tempoFactor);
			sequencer.setTempoFactor((float)(tempoFactor * .97));
		}
	}
	
	public void makeTracks(int[] list) throws InvalidMidiDataException {
		for (int i=0; i<16; i++) {
			int key = list[i];
			if (key != 0) {
				track.add(makeEvent(144, 9, key, 100, i));//创建NOTE ON和NOTE OFF事件并加入到track上
				track.add(makeEvent(128, 9, key, 100, i+1));
			}
		}
	}
	
	public MidiEvent makeEvent(int comd, int chan, int one, int two, int tick) throws InvalidMidiDataException {
		ShortMessage a = new ShortMessage();
		a.setMessage(comd, chan, one, two);
		MidiEvent event = new MidiEvent(a, tick);
		return event;
	}
	
	public static void main(String[] args) {
		try {
			new BeatBox().buildGUI();
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}
}
