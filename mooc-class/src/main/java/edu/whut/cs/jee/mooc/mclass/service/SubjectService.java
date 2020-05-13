package edu.whut.cs.jee.mooc.mclass.service;

import edu.whut.cs.jee.mooc.mclass.model.Choice;
import edu.whut.cs.jee.mooc.mclass.model.Option;
import edu.whut.cs.jee.mooc.mclass.model.Subject;
import edu.whut.cs.jee.mooc.mclass.repository.OptionRepository;
import edu.whut.cs.jee.mooc.mclass.repository.SubjectRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Slf4j
@Service
@Transactional
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    @Autowired
    private OptionRepository optionRepository;

    public Choice saveChoice(Choice choice) {
        List<Option> options = choice.getOptions();
        for (Option option : options) {
            optionRepository.save(option);
        }
        return subjectRepository.save(choice);
    }

    public Subject saveSubject(Subject subject) {
        return subjectRepository.save(subject);
    }
}
