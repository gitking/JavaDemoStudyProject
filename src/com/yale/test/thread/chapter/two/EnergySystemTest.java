package com.yale.test.thread.chapter.two;

public class EnergySystemTest {
	
	public static final int BOX_AMOUNT = 100;
	public static final double INITAL_ENERGY = 1000;
	
	public static void main(String[] args) {
		EnergySystem energySystem = new EnergySystem(BOX_AMOUNT,INITAL_ENERGY);
		for (int i =0; i<energySystem.getBoxAmount();i++) {
			EnergyTransferTask task = new EnergyTransferTask(energySystem, i, INITAL_ENERGY);
			Thread m1 = new Thread(task,"task"+01);
			m1.start();
		}
	}
}
