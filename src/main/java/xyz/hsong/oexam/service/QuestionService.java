package xyz.hsong.oexam.service;

import xyz.hsong.oexam.common.ServerResponse;
import xyz.hsong.oexam.pojo.Question;

public interface QuestionService {

    ServerResponse addQuestion(Question question);

    ServerResponse getQuestionList(Integer page, Integer pageSize);

    ServerResponse getQuestion(Long id);

    ServerResponse searchQuestion(Question question,Integer page,Integer pageSize);

    ServerResponse updateQuestion(Question question);

    ServerResponse deleteQuestion(Long id);

    ServerResponse getQuestionCount(Question question);

    ServerResponse getQuestionDetail(Long id);
}
