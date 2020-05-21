package edu.whut.cs.jee.mooc.mclass.service;

import com.google.common.collect.Lists;
import edu.whut.cs.jee.mooc.common.util.BeanConvertUtils;
import edu.whut.cs.jee.mooc.mclass.dto.JoinDto;
import edu.whut.cs.jee.mooc.mclass.dto.LessonDto;
import edu.whut.cs.jee.mooc.mclass.dto.MoocClassDto;
import edu.whut.cs.jee.mooc.mclass.model.Course;
import edu.whut.cs.jee.mooc.mclass.model.Lesson;
import edu.whut.cs.jee.mooc.mclass.model.MoocClass;
import edu.whut.cs.jee.mooc.mclass.repository.CourseRepository;
import edu.whut.cs.jee.mooc.mclass.repository.LessonRepository;
import edu.whut.cs.jee.mooc.mclass.repository.MoocClassRepository;
import edu.whut.cs.jee.mooc.upms.model.Teacher;
import edu.whut.cs.jee.mooc.upms.model.User;
import edu.whut.cs.jee.mooc.upms.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
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
        MoocClass saved =  moocClassRepository.save(moocClass);
        log.info("New MoocClass: {}", saved);
        return moocClassDto.convertFor(saved);
    }

    @Transactional(readOnly = true)
    public List<MoocClassDto> getAllMoocClasses() {
        Iterator<MoocClass> moocClassIterable = moocClassRepository.findAll().iterator();
        MoocClass moocClass = null;
        MoocClassDto moocClassDto = new MoocClassDto();
        List<MoocClassDto> moocClassDtos = new ArrayList<>();
        while(moocClassIterable.hasNext()) {
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

    /**
     * 开始上课
     * @param lessonId
     */
    public void startLesson(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).get();
        lesson.setStartTime(new Date());
        lesson.setStatus(Lesson.STATUS_SERVICING);
        lessonRepository.save(lesson);
    }

    /**
     * 点击下课
     * @param lessonId
     */
    public void endLesson(Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId).get();
        lesson.setEndTime(new Date());
        lesson.setStatus(Lesson.STATUS_END);
        lessonRepository.save(lesson);
    }

    /**
     * 学生加入慕课堂
     * @param joinDto
     * @return
     */
    public void join(JoinDto joinDto) {
        User user = userRepository.findById(joinDto.getUserId()).get();
        MoocClass moocClass = moocClassRepository.findById(joinDto.getMoocClassId()).get();
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
        return BeanConvertUtils.convertListTo(lessons, LessonDto::new, (s, t) -> { t.setCheckInCount(s.getCheckIn() != null ? 1 : 0); t.setExaminationCount(s.getExaminations().size());});
    }

}
