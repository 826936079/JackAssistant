package com.jack.jackassistant.util;

import org.junit.Test;

/**
 * Created by xiaofeng on 2017/3/12.
 */
public class HttpUtilsTest {
    @Test
    public void doGet() throws Exception {

        String result = HttpUtils.doGet("hi");
        System.out.println("hi->result=" + result);

        result = HttpUtils.doGet("说个笑话");
        System.out.println("说个笑话->result=" + result);

        result = HttpUtils.doGet("天气");
        System.out.println("天气->result=" + result);
    }

}