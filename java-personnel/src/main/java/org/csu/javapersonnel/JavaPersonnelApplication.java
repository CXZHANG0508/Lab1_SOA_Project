package org.csu.javapersonnel;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.csu.javapersonnel.mapper")
public class JavaPersonnelApplication
{

    public static void main(String[] args)
    {
        SpringApplication.run(JavaPersonnelApplication.class, args);
    }

}
