package com.yale.test.java.swing;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiEvent;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.sound.midi.ShortMessage;
import javax.sound.midi.Track;

/*
 * JavaSound API
 * 这是在Java1.3之后所加入的一组类和接口。他们是放在J2SE的类函数库中。JavaSound被分为俩个部分:MIDI和取样(sampled).
 * 这本书《Head First Java》只会讨论MIDI,它代表MusicalInstrument Digital Interface,也是不同电子发送装置沟通的标准协议。
 * 但在我们的程序中,你可以把MIDI想象成某种乐谱,它可以输入到某种“高级多功能电子魔音琴”中。换句话说,MIDI本身不带有声音,它带的是有MIDI播放功能装置的指令。
 * MIDI数据表示执行的动作(播放中央C,以及音量大小和长度等),但没有实际的声音。它并不知道怎么产生钢琴,吉他,鼓的音效,实际的声音是靠装置发出的。
 * 这样的很像是个编制完整的交响乐团,它可能是个高级键盘乐器,也有可能是计算机中的纯软件音源。对BeatBox来说,只会使用内建,纯软件的乐器音效。
 * 这通常被称为synthesizer(有些人把它叫做software synth)。
 * 
 * MIDI文件带有音乐的信息,但不具备声音本身。它有点像是乐谱。
 * MIDI装置知道要如何读取MIDI文件并加以播放。通常MIDI装置具有许多乐器的音效并且可以同时发声,因此能够模拟乐团所有的乐器。
 */
public class MiniMiniMusicApp {
	/*
	 * 首先我们需要一个Sequencer
	 * 在我们要能够发出任何声音之前,必须先取得Sequencer对象。此对象会将所有的MIDI数据送到正确的装置上,由装置来产生音乐。Sequencer实际上可以做
	 * 很多事情,但在我们的章节中所指的是播放的装置,就好像是你家中音响的CD唱盘。Sequencer这个类位与javax.sound.midi这个包中
	 * JavaSound的工作原理:
	 * 1,发声的装置Sequencer,把Sequencer想象成CD播放机
	 * 2,要演奏的乐曲,Sequence,Sequence就好像是单曲CD
	 * 3,带有乐曲的信息记录,Track可比喻是单曲CD上唯一歌曲的信息
	 * 4,乐曲的音符等信息,MidiEvent可以被唱盘理解的信息数据,MIDIEVENT就好像乐谱上的某一个音符记号,也可以用来表示更换乐器的指令等。
	 * 音符等指令组成一条歌,歌是放在CD上,CD需要播放机才能转成实际的声音。
	 * 另外还需5个步骤:
	 * 1,取的Sequencer并将它打开
	 * Sequencer sequencer = MidiSystem.getSequencer();
	 * sequencer.open();
	 * 2,创建新的Sequence
	 * Sequence seq = new Sequence(timing, 4);
	 * 3,从Sequence中创建新的Track
	 * Track t = seq.createTrack();
	 * 4,填入MidiEvent并让Sequencer播放。
	 * t.add(myMidiEvent1);
	 * player.setSequence(seq);
	 * player.start();
	 */
	public void play() throws MidiUnavailableException, InvalidMidiDataException{
		Sequencer player = MidiSystem.getSequencer();
		System.out.println("We got a sequencer:player");
		player.open();
		Sequence seq = new Sequence(Sequence.PPQ, 4);
		Track track = seq.createTrack();
		
		/**
		 * MIDI的Message是MidiEvent的关键
		 * MIDI的Message带有事件中要执行什么操作的部分,也就是要sequencer实际执行的指令。指令的第一个参数是信息的类型,后面的3个参数要看信息的类型
		 * 来决定他们的意义。例如144类型的信息代表"NOTE ON".为了要带出NOTE ON指令,sequencer还需要知道几件事。你可以想象sequencer会问到:
		 * "好啊,我可以发这个音,但是要用哪个频道？是鼓声的还是钢琴的频道？音量要多大？"
		 * 要创建的MIDI的Message,用ShortMessage的实例调用setMessage(),传入该信息的4个参数。但要记住,你还需要把信息加上执行时机装入事件中。
		 * Message是执行的内容,MidiEvent是执行的时机。
		 */
		ShortMessage a = new ShortMessage();
		/**
		 * 144是打开类型,1是频道,44是音符,100是音道
		 * 这是一个NOTE ON信息,因此后面的三个参数用来描述所要发出的声响
		 * 频道:每个频道代表不同的演奏者,例如一号频道是吉他,二号频道是Bass;或者可以向Iron Maiden用3把不同音色的吉他编制。
		 * 音符:从0-127代表不同音高。
		 * 音道:用多大的音道按下？0几乎听不到,100算差不多。
		 */
		a.setMessage(144, 1, 44, 100);//这代表发出44音符
		
		/*
		 * 制作MidiEvent(乐曲信息)
		 * MidiEvent是组合乐曲的指令。一连串的MidiEvent就好像是乐谱一样。我们会在乎的MidiEvent大部分都与描述要做的事情以及时机有关。
		 * 因为MidiEvent是非常琐碎的描述,所以你必须指定何时开始播放某个音符(NOTE ON事件)以及何时停止(NOTE OFF事件),因此你可以想象在
		 * “在开始发出G音”之前发出"停止播放G音"是没有作用的。
		 * Midi指令实际上会放在Message对象中,MidiEvent是由Message加上发音时机所组成的。也就是说Message会带有“开始播放C”指令,并伴随着
		 * “于第四拍执行指令”的信息。因此我们会同时需要MidiEvent与Message.Message描述做什么,而MidiEvent指定何时做。
		 * MidiEvent用来指示在何时执行什么操作。每个指令都必须包括该指令的执行时机。也就是说，乐声应该在哪一拍发响。
		 */
		MidiEvent noteOn = new MidiEvent(a, 1);//在第一拍启动a这个Message
		
		/*
		 * Track带有全部的MidiEvent对象,Sequence会根据事件的时间组织他们,然后Sequencer会根据此顺序来播放
		 * 同一时间可以执行多个操作,例如和弦声音或不同乐器的声音。
		 */
		track.add(noteOn);
		
		ShortMessage b = new ShortMessage();
		/**
		 * 128是关闭类型,1是频道,44是音符,100是音道
		 * 这是一个NOTE OFF信息,因此后面的三个参数用来描述所要发出的声响
		 */
		b.setMessage(128, 1, 44, 100);
		MidiEvent noteOff = new MidiEvent(b, 16);
		track.add(noteOff);
		
		player.setSequence(seq);
		
		player.start();
	}
	
	public static void main(String[] args) throws MidiUnavailableException, InvalidMidiDataException {
		MiniMiniMusicApp mini = new MiniMiniMusicApp();
		mini.play();
	}

}
