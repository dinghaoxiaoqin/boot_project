//package com.rrk.manage.demo;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@SpringBootTest
//@RunWith(SpringRunner.class)
//public class Demo {
//
//    @Test
//    public void test(){
//        List<String> aList = new ArrayList<>();
//        aList.add("刘一");
//        aList.add("陈二");
//        aList.add("张三");
//        aList.add("李四");
//        aList.add("王五");
//        aList.add("赵六");
//        aList.add("孙七");
//        aList.add("周八");
//        List<String> bList = new ArrayList<>();
//        bList.add("张三");
//        bList.add("李四");
//        bList.add("王五");
//        bList.add("赵六");
//        bList.add("吴九");
//        bList.add("郑十");
//         List<String> collect = aList.stream().filter(a -> bList.contains(a)).collect(Collectors.toList());
//        System.out.println(collect);
//
//    }
//}
