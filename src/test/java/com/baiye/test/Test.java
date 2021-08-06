package com.baiye.test;

import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: baiye
 * @Date: 2021/07/17/15:57
 * @Description:
 */
public class Test {
    public static void main(String[] args) {
        String o ="jj";
        String sql = "insert into user(username,birthday,sex,address) values(#{username},#{birthday},#{sex},#{address})";
        String replace = sql.replace("#{" + "username" + "}", "\"" + o + "" + "\"");
        System.out.println(replace);
        System.out.println( LocalDateTime.now());
    }
}
