package com.baiye.www.mybaits.datasource;

import javax.sql.DataSource;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: baiye
 * @Date: 2021/07/08/15:04
 * @Description:
 */
public interface DataSourceFactory {
    DataSource getDataSource();
}
