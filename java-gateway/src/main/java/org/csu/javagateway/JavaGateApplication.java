package org.csu.javagateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 网关排除数据库
@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class
})
public class JavaGateApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(JavaGateApplication.class, args);
    }

}
