package xyz.hsong.oexam.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import xyz.hsong.oexam.dao.QuestionMapper;
import xyz.hsong.oexam.vo.PaperQuestionVo;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-config.xml")
public class QuestionServiceImplTest {

    @Autowired
    private QuestionMapper questionMapper;


    @Test
    public void getQuestionDetail() throws JsonProcessingException {
        PaperQuestionVo paperQuestionVo = questionMapper.selectQuestionDetail(2L);
        ObjectMapper objectMapper=new ObjectMapper();
        String s = objectMapper.writeValueAsString(paperQuestionVo);
        System.out.println(s);
    }
}