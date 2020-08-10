package com.yale.test.leetcode;

import java.util.Arrays;

public class RemoveDuplicates26 {
	public static void main(String[] args) {
		int[] arr = new int[]{1,1,2};
		System.out.println("数组的新长度为:" + removeDuplicates(arr));
		int[] nums = new int[]{0,0,1,1,1,2,2,3,3,4};
		System.out.println("数组的新长度为:" + removeDuplicates(nums));
		
		int[] nums3 = new int[]{0,0,1,1,1,2,2,3,3,4};
		System.out.println("###########优化的方法,数组的新长度为:" + removeDuplicates3(nums3));
		
		int[] numss = new int[]{0,1,2,3,4,5};
		System.out.println("###########优化的方法,数组的新长度为:" + removeDuplicates3(numss));
	}
	
	/*
	 * 给定一个排序数组，你需要在 原地 删除重复出现的元素，使得每个元素只出现一次，返回移除后数组的新长度。
	 * 不要使用额外的数组空间，你必须在 原地 修改输入数组 并在使用 O(1) 额外空间的条件下完成。
	 * 示例 1:给定数组 nums = [1,1,2], 函数应该返回新的长度 2, 并且原数组 nums 的前两个元素被修改为 1, 2。 你不需要考虑数组中超出新长度后面的元素。
	 * 示例 2:给定 nums = [0,0,1,1,1,2,2,3,3,4],函数应该返回新的长度 5, 并且原数组 nums 的前五个元素被修改为 0, 1, 2, 3, 4。你不需要考虑数组中超出新长度后面的元素。
	 * 来源：力扣（LeetCode）
	 * 链接：https://leetcode-cn.com/problems/remove-duplicates-from-sorted-array
	 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
	 */
	public static int removeDuplicates(int[] arr){
		/*
		 * 方法：双指针法
		 * 复杂度分析
		 * 时间复杂度：O(n)O(n)O(n)，假设数组的长度是 nnn，那么 iii 和 jjj 分别最多遍历 nnn 步。
		 * 空间复杂度：O(1)O(1)O(1)。
		 * 首先注意数组是有序的，那么重复的元素一定会相邻。
		 * 要求删除重复元素，实际上就是将不重复的元素移到数组的左侧。
		 * 考虑用 2 个指针，一个在前记作 p，一个在后记作 q，算法流程如下：
		 * 1.比较 p 和 q 位置的元素是否相等。
		 * 如果相等，q 后移 1 位
		 * 如果不相等，将 q 位置的元素复制到 p+1 位置上，p 后移一位，q 后移 1 位
		 * 重复上述过程，直到 q 等于数组长度。
		 * 返回 p + 1，即为新数组长度。
		 */
		if (arr.length == 0) return 0;
	    int i = 0;
	    for (int j = 1; j < arr.length; j++) {
	        if (arr[j] != arr[i]) {
	            i++;
	            arr[i] = arr[j];
	        }
	    }
	    System.out.println("数组的长度" + arr.length);
	    System.out.println("数组内容变了没?" + Arrays.toString(arr));
	    return i + 1;
	}
	
	 public int removeDuplicates2(int[] nums) {
	    if(nums == null || nums.length == 0) return 0;
	    int p = 0;
	    int q = 1;
	    while(q < nums.length){
	        if(nums[p] != nums[q]){
	            nums[p + 1] = nums[q];
	            p++;
	        }
	        q++;
	    }
	    return p + 1;
	}
	 
	 /*
	  * 优化：
	  * 这是大部分题解都没有提出的，在这里提一下。
	  * 考虑如下数组：0 1 2 3 4 5
	  * 此时数组中没有重复元素，按照上面的方法removeDuplicates2，每次比较时 nums[p] 都不等于 nums[q]，因此就会将 q 指向的元素原地复制一遍，这个操作其实是不必要的。
	  * 因此我们可以添加一个小判断，当 q - p > 1 时，才进行复制。
	  * 复杂度分析：
	  * 时间复杂度：O(n)O(n)O(n)。
	  * 空间复杂度：O(1)O(1)O(1)。
	  */
	 public static int removeDuplicates3(int[] nums) {
	    if(nums == null || nums.length == 0) return 0;
	    int p = 0;
	    int q = 1;
	    while(q < nums.length){
	        if(nums[p] != nums[q]){
	            if(q - p > 1){
	                nums[p + 1] = nums[q];
	            }
	            p++;
	        }
	        q++;
	    }
	    return p + 1;
	}

}
