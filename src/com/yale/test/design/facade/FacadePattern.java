package com.yale.test.design.facade;

import com.yale.test.design.facade.vo.Amplifier;
import com.yale.test.design.facade.vo.CdPlayer;
import com.yale.test.design.facade.vo.DvdPlayer;
import com.yale.test.design.facade.vo.PopcornPopper;
import com.yale.test.design.facade.vo.Projector;
import com.yale.test.design.facade.vo.Screen;
import com.yale.test.design.facade.vo.TheaterLights;
import com.yale.test.design.facade.vo.Tuner;

/**
 * 外观模式
 */
public class FacadePattern {
	Amplifier amp;
	Tuner tuner;
	DvdPlayer dvdPlayer;
	CdPlayer cd;
	Projector projector;
	TheaterLights lights;
	Screen screen;
	PopcornPopper popper;

	public FacadePattern(Amplifier amp, Tuner tuner, CdPlayer cd,DvdPlayer dvdPlayer,
			Projector projector, TheaterLights lights, Screen screen,
			PopcornPopper popper) {
		super();
		this.amp = amp;
		this.tuner = tuner;
		this.cd = cd;
		this.projector = projector;
		this.lights = lights;
		this.screen = screen;
		this.popper = popper;
		this.dvdPlayer = dvdPlayer;
	}
	
	public void watchMovie(String movie){
		System.out.println("外观模式其实最简单,就是一次性把需要的东西准备好。然后,提供出去");
	}
	public void endMovie(){
		System.out.println("外观模式其实最简单,就是一次性把需要的东西准备好。然后,提供出去");
	}
	
	public static void  main(String [] args){
		/**
		 * 这就是外观模式
		 */
		Amplifier amp = new Amplifier();
		Tuner tuner = new Tuner();
		DvdPlayer dvdPlayer = new DvdPlayer();
		CdPlayer cd = new CdPlayer();
		Projector projector = new Projector();
		TheaterLights lights = new TheaterLights();
		Screen screen = new Screen();
		PopcornPopper popper = new PopcornPopper();
		
		FacadePattern facadePattern = new FacadePattern(amp, tuner, cd, dvdPlayer, projector, lights, screen, popper);
		facadePattern.watchMovie("");
		facadePattern.endMovie();
	}
}
