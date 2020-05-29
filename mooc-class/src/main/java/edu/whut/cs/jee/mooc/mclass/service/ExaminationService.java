package edu.whut.cs.jee.mooc.mclass.service;

import edu.whut.cs.jee.mooc.common.exception.APIException;
import edu.whut.cs.jee.mooc.common.exception.AppCode;
import edu.whut.cs.jee.mooc.common.util.BeanConvertUtils;
import edu.whut.cs.jee.mooc.mclass.dto.AnswerDto;
import edu.whut.cs.jee.mooc.mclass.dto.ExaminationDto;
import edu.whut.cs.jee.mooc.mclass.dto.ExaminationRecordDto;
import edu.whut.cs.jee.mooc.mclass.model.*;
import edu.whut.cs.jee.mooc.mclass.repository.*;
import edu.whut.cs.jee.mooc.mclass.vo.SubjectStatisticVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
@Transactional
public class ExaminationService {

    @Autowired
    private ExerciseRepository exerciseRepository;

    @Autowired
    private ExaminationRepository examinationRepository;

    @Autowired
    private ExaminationRecordRepository examinationRecordRepository;

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private LessonRepository lessonRepository;

    /**
     * 备课 - 添加活动 - 添加练习 - 创建练习
     * @param examination
     * @return
     */
    public Examination saveExamination(Examination examination) {
        return examinationRepository.save(examination);
    }

    /**
     * 备课 - 添加活动 - 添加练习 - 从练习库导入
     * @param lessonId
     * @param exerciseId
     * @return
     */
    public Examination importFromExercise(Long lessonId, Long exerciseId) {
        Exercise exercise = exerciseRepository.findById(exerciseId).get();
        Lesson lesson = lessonRepository.findById(lessonId).get();
        Examination examination = new Examination();
        examination.setLesson(lesson);
        examination.setName(exercise.getName());
        examination.setStatus(Examination.STATUS_PRIVATE);
        examination = examinationRepository.save(examination);
        List<Subject> subjects = new ArrayList<>();
        Subject subjectInExamination = null;
        for (Subject subject : exercise.getSubjects()) {
            subjectInExamination = (Subject) subject.clone();
            subjectInExamination.setId(null);
            subjectInExamination.setExerciseId(null);
            subjectInExamination.setExaminationId(examination.getId());
            subjectRepository.save(subjectInExamination);
            subjects.add(subjectInExamination);
        }
        examination.setSubjects(subjects);
        return examinationRepository.save(examination);
    }

    @Transactional(readOnly = true)
    public Examination getExamination(Long examinationId) {
        return examinationRepository.findById(examinationId).get();
    }

    /**
     * 批改、保存随堂测试答题卡
     * @param examinationRecordDto
     * @return
     */
    public ExaminationRecordDto saveExaminationRecord(ExaminationRecordDto examinationRecordDto) {
        Examination examination = examinationRepository.findById(examinationRecordDto.getExaminationId()).get();
        if(examination.getStatus() != Examination.STATUS_OPEN) {
            throw new APIException(AppCode.EXAMINATION_STATUS_ERROR, AppCode.EXAMINATION_STATUS_ERROR.getMsg() + examinationRecordDto.getExaminationId());
        }

        ExaminationRecord examinationRecord = examinationRecordDto.convertTo();
        List<AnswerDto> answerDtos = examinationRecordDto.getAnswerDtos();
        List<Answer> answers = new ArrayList<>();
        Answer answer = null;
        for (AnswerDto answerDto : answerDtos) {
            answer = answerDto.convertTo();
            answer.setStatus(Answer.STATUS_CHECKED);
            answer.setUserId(examinationRecordDto.getUserId());
            Subject subject = subjectRepository.findById(answer.getSubjectId()).get();
            if (subject.check(answer.getAnswer())) {
                answer.setRight(true);
                answerDto.setRight(true);
                examinationRecord.setCorrectCount(examinationRecord.getCorrectCount() + 1);
                examinationRecord.setScore(examinationRecord.getScore() + subject.getScore());
            }
            answers.add(answerRepository.save(answer));
        }
        examinationRecord.setAnswers(answers);
        examinationRecord.setSubmitTime(new Date());
        examinationRecord = examinationRecordRepository.save(examinationRecord);

        // 更新随堂练习提交数
        examination.setSubmitCount(examination.getSubmitCount() + 1);
        examinationRepository.save(examination);

        return examinationRecordDto.convertFor(examinationRecord);
    }

    /**
     * 删除随堂测试
     * @param examinationId
     * @return
     */
    public void removeExamination(Long examinationId) {
//        List<Subject> subjects = subjectRepository.findByExaminationId(examinationId);
//        subjectRepository.deleteAll(subjects);
        examinationRepository.deleteById(examinationId);
    }

    public void removeExaminationRecord(Long examinationRecordId) {
        examinationRecordRepository.deleteById(examinationRecordId);
    }

    /**
     * 向学生发布随堂练习
     * @param examinationId
     */
    public void publishExamination(Long examinationId, Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).get();
        Examination examination = examinationRepository.findById(examinationId).get();
        examination.setLesson(lesson);
        examination.setStatus(Examination.STATUS_OPEN);
        examinationRepository.save(examination);
    }

    /**
     * 获取完整随堂测试含习题
     * @param examinationId
     * @return
     */
    @Transactional(readOnly = true)
    public ExaminationDto getExaminationDto(Long examinationId) {
        Examination examination = examinationRepository.findById(examinationId).get();
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        List<Subject> subjects = subjectRepository.findByExaminationId(examinationId, sort);
        examination.setSubjects(subjects);
        ExaminationDto examinationDto = new ExaminationDto();
        return examinationDto.convertFor(examination);
    }

    /**
     * 获取随堂测试答题卡
     * @param examinationId
     * @return
     */
    @Transactional(readOnly = true)
    public List<ExaminationRecordDto> getExaminationRecords(Long examinationId) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        List<ExaminationRecord> examinationRecords = examinationRecordRepository.findByExaminationId(examinationId, sort);
        List<ExaminationRecordDto> examinationRecordDtos = new ArrayList<>();
        for (ExaminationRecord examinationRecord : examinationRecords) {
            ExaminationRecordDto examinationRecordDto = new ExaminationRecordDto();
            examinationRecordDto.convertFor(examinationRecord);
            examinationRecordDtos.add(examinationRecordDto);
        }
        return examinationRecordDtos;
    }

    /**
     * 获取此慕课堂未发布的随堂测试
     * @param moocClassId
     * @return
     */
    @Transactional(readOnly = true)
    public List<ExaminationDto> getPrivateExaminations(Long moocClassId) {
        List<Examination> examinations = examinationRepository.findByMoocClassIdAndStatus(moocClassId, Examination.STATUS_PRIVATE);
        return BeanConvertUtils.convertListTo(examinations, ExaminationDto::new);
    }

    /**
     * 获取随堂测试答题统计
     * @param examinationId
     * @return
     */
    @Transactional(readOnly = true)
    public List<SubjectStatisticVo> getSubjectStatisticVos(long examinationId) {
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        List<Subject> subjects = subjectRepository.findByExaminationId(examinationId, sort);
        List<SubjectStatisticVo> subjectStatisticVos = new ArrayList<>();
        SubjectStatisticVo subjectStatisticVo = null;
        for (Subject subject : subjects) {
            subjectStatisticVo = new SubjectStatisticVo();
            subjectStatisticVo = subjectStatisticVo.convertFor(subject);
            subjectStatisticVos.add(subjectStatisticVo);
        }
        return subjectStatisticVos;
    }
}
