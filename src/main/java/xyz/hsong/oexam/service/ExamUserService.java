package xyz.hsong.oexam.service;

import xyz.hsong.oexam.common.ServerResponse;
import xyz.hsong.oexam.pojo.ExamUser;

import java.util.List;

public interface ExamUserService {

    ServerResponse addExamUser(List<ExamUser> examUsers);

    ServerResponse deleteExamUser(Long userId, Long paperId);

    ServerResponse updateExamUser(ExamUser examUser);

    ServerResponse getExamUserList(Long paperId,Integer page, Integer pageSize);
}
