package com.yale.test.thread.chapter.two;

public class EnergyTransferTask implements Runnable {

	private EnergySystem energySystem;
	private int fromBox;
	private double maxAmount;
	private int DELAY = 0;
	
	public EnergyTransferTask (EnergySystem energySystem,int form,double max) {
		this.energySystem = energySystem;
		this.fromBox = form;
		this.maxAmount = max;
	}
	@Override
	public void run() {
		while (true) {
			int toBox = (int)(energySystem.getBoxAmount()*Math.random());
			double amount = maxAmount * Math.random();
			energySystem.transfer(this.fromBox, toBox, amount);
			try {
				Thread.sleep((int)(this.DELAY*Math.random()));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
