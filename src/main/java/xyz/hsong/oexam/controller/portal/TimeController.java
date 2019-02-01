package xyz.hsong.oexam.controller.portal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import xyz.hsong.oexam.common.ServerResponse;


@Controller
@RequestMapping("/time")
public class TimeController {

    @RequestMapping("/now.do")
    @ResponseBody
    public ServerResponse getHeartbeat() {
        return ServerResponse.createSuccess(System.currentTimeMillis());
    }
}
