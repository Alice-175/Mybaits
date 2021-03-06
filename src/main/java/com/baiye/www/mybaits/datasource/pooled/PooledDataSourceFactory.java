package com.baiye.www.mybaits.datasource.pooled;

import com.baiye.www.mybaits.confiuration.Configuration;
import com.baiye.www.mybaits.datasource.unpooled.UnpooledDataSourceFactory;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: baiye
 * @Date: 2021/07/14/15:28
 * @Description:
 */
public class PooledDataSourceFactory extends UnpooledDataSourceFactory {
    public PooledDataSourceFactory(Configuration config) {
        this.dataSource = new PooledDataSource(config);
    }
}
