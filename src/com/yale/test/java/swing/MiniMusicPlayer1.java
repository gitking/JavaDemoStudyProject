package com.yale.test.java.swing;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

public class MiniMusicPlayer1 {
	
	public static MidiEvent makeEvent(int cmd, int chan, int one, int two, int tick) throws InvalidMidiDataException {
		MidiEvent event = null;
		ShortMessage a = new ShortMessage();
		a.setMessage(cmd, chan, one, two);
		event = new MidiEvent(a, tick);
		return event;
	}
	
	public static void main(String[] args) throws MidiUnavailableException, InvalidMidiDataException {
		Sequencer sequencer = MidiSystem.getSequencer();
		sequencer.open();
		
		Sequence seq = new Sequence(Sequence.PPQ, 4);
		Track track = seq.createTrack();
		
		for(int i=5; i<61; i+=4) {//创建一堆连续的音符事件
			track.add(makeEvent(144, 1, i, 100, i));//调用makeEvent来产生信息和事件然后把他们加到track上
			track.add(makeEvent(128, 1, i, 100, i+2));
		}
		
		sequencer.setSequence(seq);
		sequencer.setTempoInBPM(220);
		sequencer.start();
	}
}
