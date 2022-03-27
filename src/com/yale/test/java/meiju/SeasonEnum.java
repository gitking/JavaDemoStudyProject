package com.yale.test.java.meiju;

/**
 * 5. 【推荐】如果变量值仅在一个固定范围内变化用enum类型来定义。 说明：如果存在名称之外的延伸属性应使用enum类型，下面正例SeasonEnum中的数字就是延伸信息，表示一年中的第几个季节。
 * @author issuser
 */
public enum SeasonEnum {
	SPRING(1), SUMMER(2), AUTUMN(3), WINTER(4);
	private int seq;
	
	SeasonEnum(int seq) {
		this.seq = seq;
	}
	
	public int getSeq() {
		return this.seq;
	}
}
