package xyz.hsong.oexam.controller.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.hsong.oexam.common.Const;
import xyz.hsong.oexam.common.ResponseCode;
import xyz.hsong.oexam.common.ServerResponse;
import xyz.hsong.oexam.pojo.Achievement;
import xyz.hsong.oexam.pojo.User;
import xyz.hsong.oexam.service.ExamService;

import javax.servlet.http.HttpSession;
import java.util.List;


@Controller
@RequestMapping("/exam")
public class ExamController {

    private final ExamService examService;

    @Autowired
    public ExamController(ExamService examService) {
        this.examService = examService;
    }

    //提交考试试卷
    @RequestMapping("/submit.do")
    @ResponseBody
    public ServerResponse submit(@RequestParam Long userId, @RequestParam Long paperId, @RequestBody List<Achievement> achievements) {

        try {
            return examService.savePaper(userId, paperId, achievements);
        } catch (Exception e) {
            return ServerResponse.createError(ResponseCode.DATABASE_ERROR.getCode(), ResponseCode.DATABASE_ERROR.getDesc());
        }
    }

    //进入考试页面
    @RequestMapping("/detail.do")
    @ResponseBody
    public ServerResponse getUserPaperDetail(HttpSession session, @RequestParam Long paperId) {
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        return examService.getUserPaperDetail(user.getId(), paperId);
    }

    //获取考试详情
    @RequestMapping("/score.do")
    @ResponseBody
    public ServerResponse getUserPaperScore(HttpSession session, @RequestParam Long paperId) {

        User user = (User) session.getAttribute(Const.CURRENT_USER);

        return  examService.getUserSubmitPaperDetail(user.getId(), paperId);

    }

}
