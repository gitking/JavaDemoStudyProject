package com.yale.test.java.swing;

import java.util.Random;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;


public class MiniMusicCmdLine {
	
	public void play(int instrument, int note) {
		try {
			Sequencer player = MidiSystem.getSequencer();
			player.open();
			
			Sequence seq = new Sequence(Sequence.PPQ, 4);
			Track track = seq.createTrack();
			
			ShortMessage first = new ShortMessage();
			first.setMessage(192, 1, instrument, 0);
			MidiEvent changeInstrument = new MidiEvent(first, 1);
			track.add(changeInstrument);
			
			ShortMessage a = new ShortMessage();
			a.setMessage(144, 1, note, 100);
			MidiEvent noteOn = new MidiEvent(a, 1);
			track.add(noteOn);
			
			ShortMessage b = new ShortMessage();
			b.setMessage(128, 1, note, 100);
			MidiEvent noteOff = new MidiEvent(b, 16);
			track.add(noteOff);
			
			player.setSequence(seq);
			player.start();
			
		} catch (MidiUnavailableException e) {
			e.printStackTrace();
		} catch (InvalidMidiDataException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		MiniMusicCmdLine mini = new MiniMusicCmdLine();
		Random rd = new Random();
		//传入来个[0,128)0-127之间的整数随机数
		int instrument = rd.nextInt(128);
		int note = rd.nextInt(128);
		System.out.println("instrument:" + instrument + ",note:" + note);
		mini.play(instrument, note);
	}
}
