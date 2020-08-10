package com.yale.test.leetcode;

/**
 * 移动零
 * 给定一个数组nums,编写一个函数将所有0移动到数组的末尾,同时保持非零元素的相对顺序
 * 示例:输入[0,1,0,3,12] 输出:[1,3,12,0,0]
 * 要求：1、必须在原数组上进行操作，不能拷贝额外的数组2,尽量减少操作次数
 * @author dell
 */
public class MoveZeroes283 {
	public static void main(String[] args) {

	}
	
	/*
	 * https://leetcode-cn.com/problems/move-zeroes/solution/yi-dong-ling-by-leetcode/
	 * 把上面的-cn去掉就可以去国际站看了
	 * https://leetcode.com/problems/move-zeroes/solution/yi-dong-ling-by-leetcode/?utm_source=LCUS&utm_medium=ip_redirect_q_uns&utm_campaign=transfer2china
	 */
	public static void moveZeroes(int[] nums) {
		int j=0;
		for (int i=0; i< nums.length; i++) {
			if(nums[i] != 0) {
				nums[j] = nums[i];
				if (i != j) {
					nums[i] = 0;
				}
				j++;
			}
		}
	}
	
	/*
	 * 进行交换swap
	 */
	public static void moveZeroes2(int[] nums) {
		int j=0;
		for (int i=0; i< nums.length; i++) {
			if(nums[i] != 0) {
				int temp = nums[j];
				nums[j] = nums[i];
				nums[i] = temp;
				j++;
			}
		}
	}
}
