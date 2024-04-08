package com.hh.yupao;

import com.hh.yupao.utils.AlgorithmUtils;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

/**
 * 算法工具类测试
 */
public class AlgorithmUtilsTest {


    @Test
    void test() {
        String str1 = "鱼皮是狗";
        String str2 = "鱼皮不是狗";
        String str3 = "鱼皮是鱼不是狗";
//        String str4 = "鱼皮是猫";
        // 1
        int score1 = AlgorithmUtils.minDistance(str1, str2);
        // 3
        int score2 = AlgorithmUtils.minDistance(str1, str3);
        System.out.println(score1);
        System.out.println(score2);
    }

    @Test
    void testCompareTags() {
        List<String> tagList1 = Arrays.asList("c++","原神","男");
        List<String> tagList2 = Arrays.asList("aaa","原神","男");
        List<String> tagList3 = Arrays.asList("java","c++");
        // 1
        int score1 = AlgorithmUtils.minDistance(tagList1, tagList2);
        // 3
        int score2 = AlgorithmUtils.minDistance(tagList1, tagList3);
        System.out.println(score1);
        System.out.println(score2);
    }

}
