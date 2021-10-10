package com.baiye.www.mybaits.datasource.pooled;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @Author: baiye
 * @Date: 2021/07/14/15:42
 * @Description:
 */
public class PoolState {

    protected final List<PooledConnection> idleConnections = new ArrayList<>();
    protected final List<PooledConnection> activeConnections = new ArrayList<>();
    protected PooledDataSource dataSource;

    public PoolState(PooledDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public PoolState() {

    }
}
