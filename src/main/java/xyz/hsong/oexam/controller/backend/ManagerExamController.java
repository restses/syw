package xyz.hsong.oexam.controller.backend;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import xyz.hsong.oexam.common.ResponseCode;
import xyz.hsong.oexam.common.ServerResponse;
import xyz.hsong.oexam.pojo.ExamUser;
import xyz.hsong.oexam.service.ExamService;
import xyz.hsong.oexam.service.ExamUserService;

@Controller
@RequestMapping("/admin/exam")
public class ManagerExamController {

    private final ExamService examService;
    private final ExamUserService examUserService;

    
    @Autowired
    public ManagerExamController(ExamService examService, ExamUserService examUserService) {
        this.examService = examService;
        this.examUserService = examUserService;
    }

    // 批阅试卷
    @RequestMapping("/correct.do")
    @ResponseBody
    public ServerResponse correctExam(@RequestParam Long paperId) {

        try {
            examService.correct(paperId);
            return ServerResponse.createSuccess();
        } catch (Exception e) {
            return ServerResponse.createError(ResponseCode.DATABASE_ERROR.getCode(), ResponseCode.DATABASE_ERROR.getDesc());
        }
    }
    
    // 根据试卷id查看考试人员信息
    @RequestMapping("/examUser.do")
    @ResponseBody
    public ServerResponse getExamUserList(@RequestParam Long paperId) {
        return examService.getExamUserList(paperId);
    }

    // 添加考试人员
    @RequestMapping("/addExamUser.do")
    @ResponseBody
    public ServerResponse addExamUser(@RequestBody List<ExamUser> list) {
        return examUserService.addExamUser(list);
    }

    // 删除考试人员
    @RequestMapping("/deleteExamUser.do")
    @ResponseBody
    public ServerResponse deleteExamUser(@RequestParam Long paperId, @RequestParam Long userId) {

        return examUserService.deleteExamUser(userId, paperId);
    }

    // 查询用户成绩
    @RequestMapping("/score.do")
    @ResponseBody
    public ServerResponse getUserExamScore(@RequestParam Long userId, @RequestParam Long paperId) {
        return examService.getUserSubmitPaperDetail(userId, paperId);
    }

    // 
    @RequestMapping("/scoreList.do")
    @ResponseBody
    public ServerResponse getExamScoreList(@RequestParam Long paperId) {
        return examService.getExamScoreList(paperId);
    }
    
}
