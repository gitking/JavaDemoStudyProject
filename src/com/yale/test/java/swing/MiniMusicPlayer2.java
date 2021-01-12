package com.yale.test.java.swing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.sound.midi.ControllerEventListener;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;
import javax.swing.JFrame;
import javax.swing.JPanel;

/*
 * 监听特定类型的MIDI事件,这个非GUI的事件会向事件源注册而由音乐本身来触发
 * 监听非GUI的事件,这个程序会监听音乐节奏并在每个拍子上画出随机的方块图形。
 * 注册并监听实际的MIDI事件,因此只要sequencer收到事件,我们的程序也会取得并绘制图形。但是有个问题，实际上有个bug会让我们无法监听
 * 我们自己制造的MIDI实际(NOTE ON)。所以我们得做一点点小小的修正。我们可以监听另外一种类型的MIDI事件,它被称为ControllerEvent。
 * 我们的解决方案是注册CotrollerEvent,然后确保每个NOTE ON事件都有对应的ControllerEvent事件会在同一拍上面触发。要怎么样确保这件事呢?如同其他事件一样把它加到track上。
 * 也就是说,我们的sequence会像下面这样:
 * BEAT1-NOTE ON,CONTROLLER EVENT
 * BEAT2-NOTE OFF
 * BEAT3-NOTE NO,CONTROLLER EVENT
 * BEAT4-NOTE OFF......
 */
public class MiniMusicPlayer2{
	
	static JFrame jframe = new JFrame("My First Music Video");
	static MyDrawPanelInner mdp ;
	
	public static MidiEvent makeEvent(int cmd, int chan, int one, int two, int tick) throws InvalidMidiDataException {
		ShortMessage a = new ShortMessage();
		a.setMessage(cmd, chan, one, two);
		MidiEvent event = new MidiEvent(a, tick);
		return event;
	}
	
	public void setUpGui() {
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mdp = new MyDrawPanelInner();
		jframe.setContentPane(mdp);
		jframe.setBounds(30, 30, 300, 300);
		jframe.setVisible(true);
	}
	
	public void go() throws MidiUnavailableException, InvalidMidiDataException {
		
		setUpGui();
		
		Sequencer sequencer = MidiSystem.getSequencer();
		sequencer.open();
		
		int[] eventsIWant = {127};
		sequencer.addControllerEventListener(mdp, eventsIWant);
		
		Sequence seq = new Sequence(Sequence.PPQ, 4);
		Track track = seq.createTrack();
		
		int r = 0;
		for(int i=0; i<60; i+=4) {//创建一堆连续的音符事件
			
			r = (int)((Math.random() * 50) + 1);
			track.add(makeEvent(144, 1, r, 100, i));//调用makeEvent来产生信息和事件然后把他们加到track上
			
			/*
			 * 插入事件编号为127的自定义ControllerEvent(176),它不会做任何事情,只是让我们知道有音符被播放,因为它的tick跟 NOTE ON是同时进行的。
			 */
			track.add(makeEvent(176, 1, 127, 0, i));
			
			track.add(makeEvent(128, 1, r, 100, i+2));
		}
		
		sequencer.setSequence(seq);
		//sequencer.setTempoInBPM(220);
		sequencer.setTempoInBPM(120);
		sequencer.start();
	}
	
	class MyDrawPanelInner extends JPanel implements ControllerEventListener {
		boolean msg = false;
		
		@Override
		public void controlChange(ShortMessage event) {
			msg = true;		//监听到事件时在打印一句话
			repaint();
		}
		
		@Override
		public void paintComponent(Graphics g) {
			if (msg) {
				Graphics2D g2 = (Graphics2D)g;
				int r = (int)(Math.random() * 250);
				int gr = (int)(Math.random() * 250);
				int b = (int)(Math.random() * 250);
				
				g2.setColor(new Color(r, gr, b));
				
				int ht = (int)((Math.random() * 120) + 10);
				int width = (int)((Math.random() * 120) + 10);
				int x = (int)((Math.random() * 40) + 10);
				int y = (int)((Math.random() * 40) + 10);
				g2.fillRect(x, y, ht, width);
				msg = false;
			}
		}
	}
	
	public static void main(String[] args) throws MidiUnavailableException, InvalidMidiDataException {
		MiniMusicPlayer2 mini = new MiniMusicPlayer2();
		mini.go();
	}
}
