package xyz.hsong.oexam.controller.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.hsong.oexam.common.ResponseCode;
import xyz.hsong.oexam.common.ServerResponse;
import xyz.hsong.oexam.pojo.Question;
import xyz.hsong.oexam.service.QuestionService;


@Controller
@RequestMapping("/admin/question")
public class ManagerQuestionController {

    private final QuestionService questionService;

    @Autowired
    public ManagerQuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }
    
    // 添加试题
    @RequestMapping("/add.do")
    @ResponseBody
    public ServerResponse addQuestion(@RequestBody Question question) {

        try {
            return questionService.addQuestion(question);
        } catch (Exception e) {
            return ServerResponse.createError(ResponseCode.DATABASE_ERROR.getCode(), ResponseCode.DATABASE_ERROR.getDesc());
        }

    }

    // 分页搜索试题
    @RequestMapping("/search.do")
    @ResponseBody
    public ServerResponse searchQuestion(@RequestBody(required = false) Question question, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pageSize) {
        return questionService.searchQuestion(question, page, pageSize);
    }

    // 获得试题详情
    @RequestMapping("/detail.do")
    @ResponseBody
    public ServerResponse getQuestionDetail(@RequestParam Long id) {
        return questionService.getQuestionDetail(id);
    }

    // 获取试题列表
    @RequestMapping("/list.do")
    @ResponseBody
    public ServerResponse getQuestionList(@RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer pageSize) {
        return questionService.getQuestionList(page, pageSize);
    }

    // 更新试题
    @RequestMapping("/update.do")
    @ResponseBody
    public ServerResponse updateQuestion(@RequestBody Question question) {

        try {
            return questionService.updateQuestion(question);
        } catch (RuntimeException e) {
            return ServerResponse.createError(ResponseCode.DATABASE_ERROR.getCode(), ResponseCode.DATABASE_ERROR.getDesc());
        }
    }

    // 试题统计
    @RequestMapping("/count.do")
    @ResponseBody
    public ServerResponse getQuestionCount(@RequestBody(required = false) Question question) {
        return questionService.getQuestionCount(question);
    }

    // 删除试题
    @RequestMapping("/delete.do")
    @ResponseBody
    public ServerResponse deleteQuestion(@RequestParam Long questionId) {

        try {
            return questionService.deleteQuestion(questionId);
        } catch (Exception e) {
            return ServerResponse.createError(ResponseCode.DATABASE_ERROR.getCode(), ResponseCode.DATABASE_ERROR.getDesc());
        }

    }

}
