package com.hh.yupao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author 黄昊
 * @version 1.0
 **/
public class suanfa {
   public static int[][] merge(int[][] intervals) {
        if(intervals == null || intervals.length == 0) return new int[][]{};
        // 按区间的 start 升序排列
        Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));
        List<int[]> res = new ArrayList<>();
        res.add(intervals[0]);

        for(int i=1; i<intervals.length; i++){
            int[] curr = intervals[i];
            // res 中最后一个元素的引用
            int[] last = res.get(res.size()-1);
            if(curr[0] <= last[1]){
                // 找到最大的 end
                last[1] = Math.max(last[1], curr[1]);
            } else{
                // 处理下一个待合并区间
                res.add(curr);
            }
        }
        return res.toArray(new int[res.size()][]);
    }

    public static void main(String[] args) {
//        int [][]inter={{1,3},{2,6},{8,10},{15,18}};
//        int[][] merge = merge(inter);
        int[] nums={1,2,3,4};
        int[] ints = productExceptSelf(nums);
    }
    public static int[] productExceptSelf(int[] nums) {
        int n=nums.length;
        int[] ans=new int[n];
        //计算索引0左边的乘积，由于没有数，所以令他为1
        ans[0]=1;
        //计算左下角的乘积
        for(int i=1;i<n;i++){
            ans[i]=nums[i-1]*ans[i-1];
        }
        //设置一个中间值
        int temp=1;
        //为什么是n-2呢，因为矩阵最右下角的值为1不用计算
        for(int i=n-1;i>=0;i--){
            ans[i]*=temp;
            temp*=nums[i];
        }
        return ans;
    }
}
