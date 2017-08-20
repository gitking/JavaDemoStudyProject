package com.yale.test.lambda;
@FunctionalInterface
public interface IUtil<P,R> {
	R changeType(P p);
}
