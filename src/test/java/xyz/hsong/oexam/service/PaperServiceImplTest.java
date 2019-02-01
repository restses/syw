package xyz.hsong.oexam.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import xyz.hsong.oexam.common.ServerResponse;
import xyz.hsong.oexam.dao.PaperMapper;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring-config.xml", "classpath:mybatis-config.xml"})
public class PaperServiceImplTest {

}