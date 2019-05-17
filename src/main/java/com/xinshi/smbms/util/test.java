package com.xinshi.smbms.util;

import com.xinshi.smbms.mapper.UserMapper;
import com.xinshi.smbms.pojo.User;
import com.xinshi.smbms.service.UserService;
import com.xinshi.smbms.service.impl.UserServiceImpl;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class test {

    public static void main(String[] args) {


        ApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
        UserMapper userMapper =context.getBean("userMapper",UserMapper.class);
        User user=new User();
        user.setUsercode("admin");
        user.setUserpassword("admin123");
        User userCodeOrPwd = userMapper.findUserCodeOrPwd(user);
        if (userCodeOrPwd != null){

            System.out.println("成功"+userCodeOrPwd.getUsername());
        }

    }
}
