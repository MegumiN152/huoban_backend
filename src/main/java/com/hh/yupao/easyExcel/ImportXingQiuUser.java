package com.hh.yupao.easyExcel;

/**
 * @author 黄昊
 * @version 1.0
 **/

import com.alibaba.excel.EasyExcel;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 导入星球用户到数据库
 */
public class ImportXingQiuUser {

    public static void main(String[] args) {
        //Excel数据文件放在自己电脑上，能够找到的路径
        String fileName = "E:\\IDEA_profession\\hh_springboot\\huoban_backend\\src\\main\\resources\\testExcel.xlsx";
        // 这里 需要指定读用哪个class去读，然后读取第一个sheet 同步读取会自动finish
         List<XingQIuUserInfo> userInfoList =
                EasyExcel.read(fileName).head(XingQIuUserInfo.class).sheet().doReadSync();
       System.out.println("总数 = " + userInfoList.size());
        Map<String, List<XingQIuUserInfo>> listMap =
                userInfoList.stream()
                        .filter(userInfo -> StringUtils.isNotEmpty(userInfo.getUsername()))
                        .collect(Collectors.groupingBy(XingQIuUserInfo::getUsername));
        for (Map.Entry<String, List<XingQIuUserInfo>> stringListEntry : listMap.entrySet()) {
            if (stringListEntry.getValue().size() > 1) {
                System.out.println("username = " + stringListEntry.getKey());
                System.out.println("1");
            }
        }
        System.out.println("不重复昵称数 = " + listMap.keySet().size());
    }
}

