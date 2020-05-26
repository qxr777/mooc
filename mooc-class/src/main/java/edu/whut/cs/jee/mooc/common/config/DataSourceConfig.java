package edu.whut.cs.jee.mooc.common.config;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 动态数据源（数据源切换，读写分离）
 */
@Configuration
@Slf4j
public class DataSourceConfig {

//    private final static Logger LOGGER = LoggerFactory.getLogger(DataSourceConfig.class);

    @Autowired
    private Environment props;

    /**
     * basic setting
     */
    private DruidDataSource abstractDataSource() {
        DruidDataSource abstractDataSource = new DruidDataSource();
        abstractDataSource.setDriverClassName(props.getProperty("datasource.driverClass"));
        abstractDataSource.setTestOnBorrow(true);
        abstractDataSource.setTestWhileIdle(true);
        abstractDataSource.setValidationQuery("SELECT 1");
        abstractDataSource.setMinEvictableIdleTimeMillis(30000);
        abstractDataSource.setPoolPreparedStatements(true);
        abstractDataSource.setMaxOpenPreparedStatements(100);
        return abstractDataSource;
    }

    /**
     * master setting
     */
    @Bean(destroyMethod = "close", name="master")
    @Primary
    public DruidDataSource masterDataSource() {
        DruidDataSource masterDataSource = abstractDataSource();
        masterDataSource.setUrl(props.getProperty("datasource.master.url"));
        masterDataSource.setUsername(props.getProperty("datasource.master.username"));
        masterDataSource.setPassword(props.getProperty("datasource.master.password"));
        masterDataSource.setMaxActive(Integer.parseInt(props.getProperty("datasource.master.maxActive")));
        masterDataSource.setMinIdle(Integer.parseInt(props.getProperty("datasource.master.minIdle")));
        return masterDataSource;
    }

    /**
     * slave1 setting
     */
    @Bean(destroyMethod = "close", name="slave1")
    public DruidDataSource slaveDataSource1() {
        DruidDataSource slaveDataSource = abstractDataSource();
        slaveDataSource.setUrl(props.getProperty("datasource.slave1.url"));
        slaveDataSource.setUsername(props.getProperty("datasource.slave1.username"));
        slaveDataSource.setPassword(props.getProperty("datasource.slave1.password"));
        slaveDataSource.setMaxActive(Integer.parseInt(props.getProperty("datasource.slave1.maxActive")));
        slaveDataSource.setMinIdle(Integer.parseInt(props.getProperty("datasource.slave1.minIdle")));
        return slaveDataSource;
    }

    /**
     * slave2 setting
     */
    @Bean(destroyMethod = "close", name="slave2")
    public DruidDataSource slaveDataSource2() {
        DruidDataSource slaveDataSource = abstractDataSource();
        slaveDataSource.setUrl(props.getProperty("datasource.slave2.url"));
        slaveDataSource.setUsername(props.getProperty("datasource.slave2.username"));
        slaveDataSource.setPassword(props.getProperty("datasource.slave2.password"));
        slaveDataSource.setMaxActive(Integer.parseInt(props.getProperty("datasource.slave2.maxActive")));
        slaveDataSource.setMinIdle(Integer.parseInt(props.getProperty("datasource.slave2.minIdle")));
        return slaveDataSource;
    }

    @Bean(name="dynamicDataSource")
    public DataSource dynamicDataSource() throws IOException {
        Map<Object, Object> targetDataSources = new HashMap<>();
        targetDataSources.put("master", masterDataSource());
        targetDataSources.put("slave1", slaveDataSource1());
        targetDataSources.put("slave2", slaveDataSource2());

        AbstractRoutingDataSource dynamicDataSource = new AbstractRoutingDataSource() {
            @Override
            protected Object determineCurrentLookupKey() {
                String lookupKey = TransactionSynchronizationManager.isCurrentTransactionReadOnly() ? "slave" : "master";
                if (lookupKey.equals("slave")) {
                    lookupKey += System.currentTimeMillis() % 2 + 1;
                }
                log.info("当前操作使用的数据源：{}", lookupKey);
                return lookupKey;
            }
        };

        dynamicDataSource.setDefaultTargetDataSource(targetDataSources.get("master"));
        dynamicDataSource.setTargetDataSources(targetDataSources);
        return dynamicDataSource;
    }

    @Bean
    public DataSource dataSource() throws IOException {
        return new LazyConnectionDataSourceProxy(dynamicDataSource());
    }

    @Bean(name = "entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(EntityManagerFactoryBuilder builder) throws IOException {
        return builder
                .dataSource(dataSource())
                .packages("edu.whut.cs.jee.mooc")
                .build();
    }

    @Bean(name = "transactionManager")
    JpaTransactionManager transactionManager(EntityManagerFactoryBuilder builder) throws IOException {
        return new JpaTransactionManager(entityManagerFactory(builder).getObject());
    }
}
