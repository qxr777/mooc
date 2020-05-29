package edu.whut.cs.jee.mooc.mclass.service;

import com.google.common.collect.Lists;
import edu.whut.cs.jee.mooc.common.exception.APIException;
import edu.whut.cs.jee.mooc.common.exception.AppCode;
import edu.whut.cs.jee.mooc.common.util.BeanConvertUtils;
import edu.whut.cs.jee.mooc.mclass.dto.JoinDto;
import edu.whut.cs.jee.mooc.mclass.dto.LessonDto;
import edu.whut.cs.jee.mooc.mclass.dto.MoocClassDto;
import edu.whut.cs.jee.mooc.mclass.model.*;
import edu.whut.cs.jee.mooc.mclass.repository.CourseRepository;
import edu.whut.cs.jee.mooc.mclass.repository.ExaminationRepository;
import edu.whut.cs.jee.mooc.mclass.repository.LessonRepository;
import edu.whut.cs.jee.mooc.mclass.repository.MoocClassRepository;
import edu.whut.cs.jee.mooc.upms.model.Teacher;
import edu.whut.cs.jee.mooc.upms.model.User;
import edu.whut.cs.jee.mooc.upms.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Slf4j
@Service
@Transactional
public class MoocClassService {

    @Autowired
    private MoocClassRepository moocClassRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExaminationRepository examinationRepository;

    /**
     * 【参考】各层命名规约: 插入的方法用 save/insert 做前缀。
     * @param moocClassDto
     * @return
     */
    public MoocClassDto saveMoocClass(MoocClassDto moocClassDto) {
        Course course = null;
        if(moocClassDto.getOfflineCourse() != null) {
            Teacher teacher = new Teacher();
            teacher.setId(moocClassDto.getTeacherId());
            course = Course.builder()
                    .name(moocClassDto.getOfflineCourse())
                    .type(Course.TYPE_OFFLINE)
                    .teacher(teacher)
                    .build();
            course = courseRepository.save(course);
        }
        if(moocClassDto.getCourseId() != null) {
            course = courseRepository.findById(moocClassDto.getCourseId()).get();
        }
        MoocClass moocClass = moocClassDto.convertTo();
        moocClass.setCourse(course);
        if (moocClass.getCode() == null) {
            moocClass.setCode(this.generateMClassCode());
        }
        MoocClass saved =  moocClassRepository.save(moocClass);
        log.info("New MoocClass: {}", saved);
        return moocClassDto.convertFor(saved);
    }

    private String generateMClassCode() {
        String code = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
        while (this.getMoocClassByCode(code) != null) {
            code = RandomStringUtils.randomAlphanumeric(6).toUpperCase();
        }
        return code;
    }

    @Transactional(readOnly = true)
    public MoocClass getMoocClassByCode(String code) {
        MoocClass moocClass = null;
        List<MoocClass> moocClasses = moocClassRepository.findByCode(code);
        if (moocClasses.size() > 0) {
            moocClass = moocClasses.get(0);
        }
        return moocClass;
    }

    public MoocClassDto editMoocClass(MoocClassDto moocClassDto) {
        MoocClass moocClass = moocClassRepository.findById(moocClassDto.getId()).get();
        BeanUtils.copyProperties(moocClassDto, moocClass);
        MoocClass saved =  moocClassRepository.save(moocClass);
        return moocClassDto.convertFor(saved);
    }

    @Transactional(readOnly = true)
    public List<MoocClassDto> getAllMoocClasses() {
        Iterator<MoocClass> moocClassIterable = moocClassRepository.findAll().iterator();
        MoocClass moocClass = null;
        MoocClassDto moocClassDto = null;
        List<MoocClassDto> moocClassDtos = new ArrayList<>();
        while(moocClassIterable.hasNext()) {
            moocClassDto = new MoocClassDto();
            moocClass = moocClassIterable.next();
            moocClassDtos.add(moocClassDto.convertFor(moocClass));
        }
        return moocClassDtos;
    }

    @Transactional(readOnly = true)
    public List<MoocClassDto> getOwnMoocClasses(Long teacherId) {
        Iterator<MoocClass> moocClassIterable = moocClassRepository.findByTeacher(teacherId).iterator();
        MoocClass moocClass = null;
        MoocClassDto moocClassDto = null;
        List<MoocClassDto> moocClassDtos = new ArrayList<>();
        while(moocClassIterable.hasNext()) {
            moocClassDto = new MoocClassDto();
            moocClass = moocClassIterable.next();
            moocClassDtos.add(moocClassDto.convertFor(moocClass));
        }
        return moocClassDtos;
    }

    @Transactional(readOnly = true)
    public MoocClassDto getMoocClass(Long moocClassId) {
        MoocClass moocClass = moocClassRepository.findById(moocClassId).get();
        MoocClassDto moocClassDto = new MoocClassDto();
        return moocClassDto.convertFor(moocClass);
    }

    public boolean isServing(Long moocClassId) {
        boolean result = false;
        List<Lesson> lessons = lessonRepository.findByMoocClassIdAndStatus(moocClassId, Lesson.STATUS_SERVICING);
        if (lessons.size() > 0) {
            result = true;
        }
        return result;
    }

    @Transactional(readOnly = true)
    public List<User> getUsers(Long moocClassId) {
        List<BigInteger> useIdList = moocClassRepository.findUserIds(moocClassId);
        List<Long> userIds = Lists.newArrayList();
        useIdList.stream().forEach(bigInteger ->{
            userIds.add(bigInteger.longValue());
        });
        List<User> users = Lists.newArrayList(userRepository.findAllById(userIds));
        return users;
    }

    /**
     * 【参考】各层命名规约: 插入的方法用 save/insert 做前缀。
     * @param lessonDto
     * @return
     */
    public LessonDto saveLesson(LessonDto lessonDto) {
        Lesson lesson = lessonDto.convertTo();
        lesson = lessonRepository.save(lesson);
        log.info("New Lesson: ", lesson);
        return lessonDto.convertFor(lesson);
    }

    /**
     * 【参考】各层命名规约: 删除的方法用 remove/delete 做前缀。
     * @param lessonId
     * @return
     */
    public void removeLesson(Long lessonId) {
        if (lessonRepository.existsById(lessonId)) {
            lessonRepository.deleteById(lessonId);
        }
    }

    public void removeMoocClass(Long moocClassId) {
        if (moocClassRepository.existsById(moocClassId)) {
            moocClassRepository.deleteById(moocClassId);
        } else {
            throw new APIException(AppCode.NO_MCLASS_ERROR, AppCode.NO_MCLASS_ERROR.getMsg() + moocClassId);
        }
    }

    public void removeCourse(Long courseId) {
        if (courseRepository.existsById(courseId)) {
            courseRepository.deleteById(courseId);
        } else {
            throw new APIException(AppCode.NO_COURSE_ERROR, AppCode.NO_COURSE_ERROR.getMsg() + courseId);
        }
    }

    /**
     * 开始上课
     * @param moocClassId
     */
    public LessonDto startLesson(Long moocClassId) {
        List<Lesson> lessons = lessonRepository.findByMoocClassIdAndStatus(moocClassId, Lesson.STATUS_SERVICING);
        if (lessons.size() > 0) {
            throw new APIException(AppCode.HAS_SERVING_LESSON, AppCode.HAS_SERVING_LESSON.getMsg() + lessons.get(0).getId());
        }
        Lesson lesson = Lesson.builder()
                .moocClassId(moocClassId)
                .startTime(new Date())
                .status(Lesson.STATUS_SERVICING)
                .build();
        lesson = lessonRepository.save(lesson);
        return BeanConvertUtils.convertTo(lesson, LessonDto::new);
    }

    public LessonDto getLesson(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).get();
        return BeanConvertUtils.convertTo(lesson, LessonDto::new);
    }

    /**
     * 点击下课
     * @param lessonId
     */
    public void endLesson(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).get();
        lesson.setEndTime(new Date());
        lesson.setStatus(Lesson.STATUS_END);
        // 关闭签到
        if(lesson.getCheckIn() != null) {
            lesson.getCheckIn().setStatus(CheckIn.STATUS_CLOSED);
        }
        // 关闭随堂测试
        List<Examination> examinations = examinationRepository.findByLesson(lesson);
        for (Examination examination : examinations) {
            if (examination.getStatus() != Examination.STATUS_CLOSED) {
                examination.setStatus(Examination.STATUS_CLOSED);
            }
        }
        lesson = updateCount(lesson);
        lessonRepository.save(lesson);
    }

    /**
     * 更新上课记录的统计数据
     * @param lesson
     * @return
     */
    private Lesson updateCount(Lesson lesson) {
        // 随堂测试数量
        List<Examination> examinations = examinationRepository.findByLessonAndStatus(lesson, Examination.STATUS_CLOSED);
        lesson.setExaminationCount(examinations.size());
        return lesson;
    }

    /**
     * 学生加入慕课堂
     * @param joinDto
     * @return
     */
    public void join(JoinDto joinDto) {
        String code = joinDto.getMoocClassCode();
        MoocClass moocClass = this.getMoocClassByCode(code);
        if(moocClass == null) {
            throw new APIException(AppCode.NO_MCLASS_ERROR, code + AppCode.NO_MCLASS_ERROR.getMsg());
        }
        User user = userRepository.findById(joinDto.getUserId()).get();
        List<User> users = this.getUsers(moocClass.getId());
        if (users.contains(user)) {
            throw new APIException(AppCode.USER_HAS_JOINED_ERROR, user.getName() + AppCode.USER_HAS_JOINED_ERROR.getMsg());
        }
        moocClass.getUsers().add(user);
        moocClassRepository.save(moocClass);
    }

    /**
     * 返回慕课堂的上课记录
     * @param moocClassId
     * @return
     */
    @Transactional(readOnly = true)
    public List<LessonDto> getLessons(Long moocClassId) {
        List<Lesson> lessons = lessonRepository.findByMoocClassId(moocClassId);
        return BeanConvertUtils.convertListTo(lessons, LessonDto::new, (s, t) -> { t.setCheckInCount(s.getCheckIn() != null ? 1 : 0);t.setStatusCh(Lesson.STATUS_STRING_CH[t.getStatus()]);});
    }

}
