package cn.wxm.news_demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("cn.wxm.news_demo.mapper")
public class NewsDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(NewsDemoApplication.class, args);
    }

}
