package com.zhixiangmain;

import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ImportResource;

/**
 * Hello world!
 *
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})//配置exclude避免Failed to configure a DataSource: 'url' attribute is not specified and no embedded datasource问题
@ImportResource({ "classpath:config/spring-dubbo.xml" })
public class App 
{
    private static Logger logger = Logger.getLogger(App.class);

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
        logger.info("SpringBoot-web********** Start Success");
    }
}
