package com.yale.test.thread.chapter.two;

public class EnergySystem {
	
	private final double[] energyBoxes;
	private final Object lockObj = new Object();
	
	public EnergySystem(int n,double initialEnergy) { 
		energyBoxes = new double[n];
		for (int i=0;i<energyBoxes.length; i++) {
			energyBoxes[i] = initialEnergy;
		}
	}
	
	public void transfer (int from,int to,double amount) {
		synchronized (lockObj) {
			if (energyBoxes[from] < amount) {
				try {
					lockObj.wait();//让当前线程进入阻塞状态
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println(Thread.currentThread().getName());
				energyBoxes[from] -= amount;
				System.out.printf("从%d转移%10.2f单位能量到%d",from, amount,to);
				energyBoxes[to] += amount;
				System.out.printf("能量总和:%10.2f%n",getTotalEnergies());
				lockObj.notifyAll();//唤醒所有处在阻塞状态的的线程
			}
		}
	}
	
	public double getTotalEnergies() {
		double sum = 0;
		for (double amount : energyBoxes) {
			sum += amount;
		}
		return sum;
	}
	
	public int getBoxAmount() {
		return energyBoxes.length;
	}
}
