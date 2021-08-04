package com.baiye.www.mybaits.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: baiye
 * @Date: 2021/07/06/9:58
 * @Description: 获取配置文件io
 */
public class Resources {
    public static InputStream getResourceAsStream(String resource) throws IOException {
        InputStream in = Resources.class.getClassLoader().getResourceAsStream(resource);
        if(in == null){
            throw new IOException("Could not find resource :" + resource);
        }
        return in;
    }
}
