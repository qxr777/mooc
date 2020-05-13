package edu.whut.cs.jee.mooc.mclass.service;

import edu.whut.cs.jee.mooc.mclass.dto.JoinDto;
import edu.whut.cs.jee.mooc.mclass.dto.LessonDto;
import edu.whut.cs.jee.mooc.mclass.dto.MoocClassDto;
import edu.whut.cs.jee.mooc.mclass.model.Course;
import edu.whut.cs.jee.mooc.mclass.model.Lesson;
import edu.whut.cs.jee.mooc.mclass.model.MoocClass;
import edu.whut.cs.jee.mooc.mclass.repository.CourseRepository;
import edu.whut.cs.jee.mooc.mclass.repository.LessonRepository;
import edu.whut.cs.jee.mooc.mclass.repository.MoocClassRepository;
import edu.whut.cs.jee.mooc.upms.model.User;
import edu.whut.cs.jee.mooc.upms.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

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
            course = Course.builder()
                    .name(moocClassDto.getOfflineCourse())
                    .type(Course.TYPE_OFFLINE)
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
     * @param lessonDto
     * @return
     */
    public Long removeLesson(LessonDto lessonDto) {
        Lesson lesson = lessonDto.convertTo();
        if (lessonRepository.existsById(lessonDto.getId())) {
            lessonRepository.delete(lesson);
        }
        return lessonDto.getId();
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

}
