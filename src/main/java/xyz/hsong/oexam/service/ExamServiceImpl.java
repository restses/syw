package xyz.hsong.oexam.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import xyz.hsong.oexam.bo.QuestionBo;
import xyz.hsong.oexam.common.ResponseCode;
import xyz.hsong.oexam.common.ServerResponse;
import xyz.hsong.oexam.dao.*;
import xyz.hsong.oexam.pojo.Achievement;
import xyz.hsong.oexam.pojo.ExamUser;
import xyz.hsong.oexam.pojo.Mark;
import xyz.hsong.oexam.pojo.Paper;
import xyz.hsong.oexam.vo.ExamUserVo;
import xyz.hsong.oexam.vo.ExamVo;
import xyz.hsong.oexam.vo.MarkVo;
import xyz.hsong.oexam.vo.PaperQuestionVo;

import java.util.*;

@Service
public class ExamServiceImpl implements ExamService {

	private static final Logger logger = LoggerFactory.getLogger(ExamServiceImpl.class);

	private final AchievementMapper achievementMapper;
	private final PaperDetailMapper paperDetailMapper;
	private final QuestionMapper questionMapper;
	private final MarkMapper markMapper;
	private final ExamUserMapper examUserMapper;
	private final PaperMapper paperMapper;

	@Autowired
	public ExamServiceImpl(AchievementMapper achievementMapper, PaperDetailMapper paperDetailMapper,
			QuestionMapper questionMapper, MarkMapper markMapper, ExamUserMapper examUserMapper,
			PaperMapper paperMapper) {
		this.achievementMapper = achievementMapper;
		this.paperDetailMapper = paperDetailMapper;
		this.questionMapper = questionMapper;
		this.markMapper = markMapper;
		this.examUserMapper = examUserMapper;
		this.paperMapper = paperMapper;
	}

	@Override
	@Transactional
	public ServerResponse savePaper(Long userId, Long paperId, List<Achievement> achievements) {

		ExamUser examUser = new ExamUser();
		examUser.setPaperId(paperId);
		examUser.setUserId(userId);
		examUser.setStatus(2);

		try {

			if (achievements.isEmpty()) {
				examUserMapper.updateStatus(examUser);
				return ServerResponse.createSuccess();
			}

			examUserMapper.updateStatus(examUser);
			examUserMapper.updateStatus(examUser);
			achievementMapper.insertAchievement(achievements);
			return ServerResponse.createSuccess();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}

	/**
	 * 批阅试卷
	 * @param userId
	 *            用户id
	 * @param paperId
	 *            试卷id
	 */
	@Async
	public void correctRun(Long userId, Long paperId) {

		try {
			// 试题总分
			Integer score = 0;

			// 获取试卷中所有题号和单题分数
			Map<Long, Map<String, Integer>> questionScoreMap = paperDetailMapper.selectScoreByPaperId(paperId);

			// 根据试卷中的题号获取试卷中的答案
			Map<Long, QuestionBo> questionAnswer = questionMapper.selectAnswerByQuestionId(questionScoreMap.keySet());

			// 获取用户的答卷
			Map<Long, QuestionBo> userPaper = achievementMapper.selectByUserIdAndPaperId(userId, paperId);

			// 试卷中所有试题的ID
			Set<Long> questionIds = questionScoreMap.keySet();

			for (Long questionId : questionIds) {

				// 获取用户答题的答案
				QuestionBo questionBo = userPaper.get(questionId);

				if (questionBo == null) {
					continue;
				}

				// 获取用户答题的答案
				List<Long> userAnswers = questionBo.getAnswers();
				// 获取试卷中对应题目的答案
				List<Long> paperAnswers = questionAnswer.get(questionId).getAnswers();
				// 判断某一题是否正确
				boolean flag = checkAnswer(userAnswers, paperAnswers);

				if (flag) {
					score = score + questionScoreMap.get(questionId).get("score");
				}

			}

			updateExamStatus(userId, paperId, score);

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}

	@Override
	@Async
	public void correct(Long paperId) {

		try {
			// 考试人员的id
			List<Long> userIds = examUserMapper.selectPaperCorrectUserId(paperId);

			for (Long userId : userIds) {
				correctRun(userId, paperId);
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new RuntimeException(e);
		}
	}

	/**
	 * 判断答案是否正确
	 * 
	 * @param userAnswers
	 *            用户答案
	 * @param paperAnswers
	 *            正确答案
	 * @return
	 */
	private boolean checkAnswer(List<Long> userAnswers, List<Long> paperAnswers) {

		// 比较用户答案与试卷答案是否一致
		if (userAnswers.size() == paperAnswers.size()) {
			for (Long id : paperAnswers) {
				if (!userAnswers.contains(id)) {
					return false;
				}
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 更新用户成绩和考试状态
	 * 
	 * @param userId
	 *            用户id
	 * @param paperId
	 *            试卷id
	 * @param score
	 *            分数
	 */
	@Transactional
	protected void updateExamStatus(Long userId, Long paperId, Integer score) {

		// 计算总分
		Mark mark = new Mark();
		mark.setPaperId(paperId);
		mark.setUserId(userId);
		mark.setScore(score);
		// 向成绩表中插入数据
		markMapper.insertSelective(mark);

		// 更新试卷状态
		ExamUser examUser = new ExamUser();
		examUser.setStatus(3);
		examUser.setPaperId(paperId);
		examUser.setUserId(userId);

		examUserMapper.updateStatus(examUser);

	}

	@Override
	public ServerResponse updateUserExamStatus(Long userId, Long paperId, Integer status) {

		try {
			ExamUser examUser = new ExamUser();
			examUser.setPaperId(paperId);
			examUser.setUserId(userId);
			examUser.setStatus(status);

			if (examUserMapper.updateStatus(examUser) > 0) {
				return ServerResponse.createSuccess();
			}

			return ServerResponse.createError();

		} catch (Exception e) {
			logger.error(e.getMessage());
			return ServerResponse.createError(ResponseCode.DATABASE_ERROR.getCode(),
					ResponseCode.DATABASE_ERROR.getDesc());

		}

	}

	@Override
	public ServerResponse getUserExamStatus(Long paperId, Long userId) {

		try {
			Integer integer = examUserMapper.selectExamUserStatus(paperId, userId);
			if (integer == null) {
				return ServerResponse.createError();
			}
			return ServerResponse.createSuccess();
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ServerResponse.createError(ResponseCode.DATABASE_ERROR.getCode(),
					ResponseCode.DATABASE_ERROR.getDesc());
		}
	}

	@Override
	public ServerResponse getExamScore(Long userId, Long paperId) {

		try {
			Integer integer = markMapper.selectMark(userId, paperId);

			if (integer == null) {
				return ServerResponse.createError();
			}

			return ServerResponse.createSuccess(integer);

		} catch (Exception e) {
			logger.error(e.getMessage());
			return ServerResponse.createError(ResponseCode.DATABASE_ERROR.getCode(),
					ResponseCode.DATABASE_ERROR.getDesc());
		}

	}

	@Override
	public ServerResponse getUserPaperList(Long userId, Integer page, Integer pageSize) {

		try {
			return ServerResponse
					.createSuccess(paperMapper.selectUserPaperList(userId, (page - 1) * pageSize, pageSize));
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ServerResponse.createError(ResponseCode.DATABASE_ERROR.getCode(),
					ResponseCode.DATABASE_ERROR.getDesc());
		}
	}

	@Override
	public ServerResponse getUserPaperDetail(Long userId, Long paperId) {

		Paper paper;
		List<PaperQuestionVo> paperQuestionList;

		try {

			// 判断是否已经参加过考试
			Integer integer = examUserMapper.selectExamUserStatus(paperId, userId);

			if (integer != 0) {
				return ServerResponse.createError(ResponseCode.EXAM_REPEATED.getCode(),
						ResponseCode.EXAM_REPEATED.getDesc());
			}

			paper = paperMapper.selectByPrimaryKey(paperId);

			if (paper.getStartTime() == null) {
				return ServerResponse.createError(ResponseCode.PAPER_NOT_FIND.getCode(),
						ResponseCode.PAPER_NOT_FIND.getDesc());
			}

			// 判断考试时间是否已经开始
			Date now = new Date();

			if (now.getTime() < paper.getStartTime().getTime() || now.getTime() > paper.getEndTime().getTime()) {
				return ServerResponse.createError(ResponseCode.TIME_ERROR.getCode(), ResponseCode.TIME_ERROR.getDesc());
			}

			paperQuestionList = paperMapper.selectPaperQuestionList(paperId);

		} catch (Exception e) {
			logger.error(e.getMessage());
			return ServerResponse.createError(ResponseCode.DATABASE_ERROR.getCode(),
					ResponseCode.DATABASE_ERROR.getDesc());
		}

		if (paperQuestionList.isEmpty()) {
			return ServerResponse.createError(ResponseCode.PAPER_NOT_FIND.getCode(),
					ResponseCode.PAPER_NOT_FIND.getDesc());
		}

		ExamVo examVo = new ExamVo();
		examVo.setPaper(paper);
		examVo.setPaperQuestionVos(paperQuestionList);

		return ServerResponse.createSuccess(examVo);
	}

	@Override
	public ServerResponse getUserPaperListCount(Long userId) {

		try {
			Integer integer = paperMapper.selectUserPaperListCount(userId);
			return ServerResponse.createSuccess(integer);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ServerResponse.createError(ResponseCode.DATABASE_ERROR.getCode(),
					ResponseCode.DATABASE_ERROR.getDesc());
		}
	}

	@Override
	public ServerResponse getUserSubmitPaperDetail(Long userId, Long paperId) {

		List<PaperQuestionVo> paperQuestionVos;
		List<Long> userAnswers;
		Integer score;

		try {
			// 获取试卷中的试题和选项
			paperQuestionVos = paperMapper.selectPaperQuestionList(paperId);
			// 获取用户的答卷选项
			userAnswers = achievementMapper.selectUserAnswers(userId, paperId);
			// 获取用户的考试分数
			score = markMapper.selectMark(userId, paperId);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ServerResponse.createError(ResponseCode.DATABASE_ERROR.getCode(),
					ResponseCode.DATABASE_ERROR.getDesc());
		}

		ExamVo examVo = new ExamVo();
		examVo.setPaperQuestionVos(paperQuestionVos);
		examVo.setCheckedId(userAnswers);
		examVo.setScore(score);

		return ServerResponse.createSuccess(examVo);
	}

	@Override
	public ServerResponse getExamUserList(Long paperId) {

		try {
			List<ExamUserVo> users = examUserMapper.selectExamUserDetailList(paperId);
			return ServerResponse.createSuccess(users);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ServerResponse.createError(ResponseCode.DATABASE_ERROR.getCode(),
					ResponseCode.DATABASE_ERROR.getDesc());

		}

	}

	@Override
	public ServerResponse getExamScoreList(Long paperId) {
		try {
			List<MarkVo> scores = markMapper.selectMarkList(paperId);
			return ServerResponse.createSuccess(scores);
		} catch (Exception e) {
			logger.error(e.getMessage());
			return ServerResponse.createError(ResponseCode.DATABASE_ERROR.getCode(),
					ResponseCode.DATABASE_ERROR.getDesc());
		}

	}
}
