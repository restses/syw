package xyz.hsong.oexam.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-config.xml")
public class ExamServiceImplTest {

    @Autowired
    private ExamServiceImpl examService;

    @Test
    public void correctRun() {
        examService.correctRun(1002L,5L);
    }
}