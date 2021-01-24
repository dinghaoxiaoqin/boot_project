package com.rrk.order.manage.config.mybatisConfig;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatis-plus的配置
 * com.rrk.common.modules.product.dao*
 */
@Configuration
@MapperScan("com.rrk.common.modules.*.dao*")
public class MybatisConfig {

    /**
     * 分页插件
     * @return
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        // 开启 PageHelper 的支持
        return paginationInterceptor;
    }

    /**
     * SQL执行效率插件
     */
    @Bean
    public PerformanceInterceptor performanceInterceptor() {
        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
        performanceInterceptor.setMaxTime(100000);
        performanceInterceptor.setFormat(true);
        return performanceInterceptor;
    }

//
//    @Bean(name = "bootorder")
//    @ConfigurationProperties(prefix = "spring.datasource.druid.bootorder" )
//    public DataSource bootOrder() {
//        return  DruidDataSourceBuilder.create().build();
//    }
//
//    @Bean(name = "boot")
//    @ConfigurationProperties(prefix = "spring.datasource.druid.boot" )
//    public DataSource boot() {
//        return  DruidDataSourceBuilder.create().build();
//    }
//
//    /**
//     * 动态数据源配置
//     * @return
//     */
//    @Bean
//    @Primary
//    public DataSource multipleDataSource(@Qualifier("bootorder") DataSource bootorder, @Qualifier("boot") DataSource boot) {
//        MultipleDataSource multipleDataSource = new MultipleDataSource();
//        Map< Object, Object > targetDataSources = new HashMap<>();
//        targetDataSources.put(DatasourceEnum.BOOTORDER.getValue(), bootorder);
//        targetDataSources.put(DatasourceEnum.BOOT.getValue(), boot);
//        //添加数据源
//        multipleDataSource.setTargetDataSources(targetDataSources);
//        //设置默认数据源
//        multipleDataSource.setDefaultTargetDataSource(bootorder);
//        return  multipleDataSource;
//    }
//
//    @Bean("sqlSessionFactory")
//    public SqlSessionFactory sqlSessionFactory() throws Exception {
//        MybatisSqlSessionFactoryBean sqlSessionFactory = new MybatisSqlSessionFactoryBean();
//        sqlSessionFactory.setDataSource( multipleDataSource(bootOrder(),boot()));
//        //sqlSessionFactory.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/mapper/*/*Mapper.xml"));
//
//        MybatisConfiguration configuration = new MybatisConfiguration();
//        //configuration.setDefaultScriptingLanguage(MybatisXMLLanguageDriver.class);
//        configuration.setJdbcTypeForNull(JdbcType.NULL);
//        configuration.setMapUnderscoreToCamelCase(true);
//        configuration.setCacheEnabled(false);
//        sqlSessionFactory.setConfiguration(configuration);
//        sqlSessionFactory.setPlugins(new Interceptor[]{ //PerformanceInterceptor(),OptimisticLockerInterceptor()
//                paginationInterceptor() //添加分页功能
//        });
//        //sqlSessionFactory.setGlobalConfig(globalConfiguration());
//        return sqlSessionFactory.getObject();
//    }

}
