package com.baiye.www.mybaits.datasource.unpooled;

import com.baiye.www.mybaits.datasource.DataSourceFactory;

import javax.sql.DataSource;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: baiye
 * @Date: 2021/07/08/15:07
 * @Description:
 */
public class UnpooledDataSourceFactory implements DataSourceFactory {
    protected DataSource dataSource;
    @Override
    public DataSource getDataSource() {
        return new UnpooledDataSource();
    }
}
