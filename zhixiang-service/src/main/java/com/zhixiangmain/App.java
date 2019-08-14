package com.zhixiangmain;

import com.zhixiangmain.config.DynamicDataSourceRegister;
import org.apache.log4j.Logger;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;


/**
 * Hello world!
 *
 */
@SpringBootApplication/*(exclude = {DataSourceAutoConfiguration.class})*/
@ImportResource({"classpath:config/spring-dubbo.xml"})
@MapperScan("com.zhixiangmain.resource")
@Import(DynamicDataSourceRegister.class)
@ComponentScan({"com.zhixiangmain.config", "com.zhixiangmain.component"})
public class App 
{
    private static Logger logger = Logger.getLogger(App.class);

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
        logger.info("SpringBoot-impl_|_|_|_| Start Success");
    }
}
