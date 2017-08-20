package com.yale.test.lambda;

@FunctionalInterface
public interface IUtilFour<R,PS,PI> {
	R createPer(PS ps, PI pi);
}
