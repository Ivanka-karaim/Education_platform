package com.education_platform.service;

import com.education_platform.data.LectureRepository;
import com.education_platform.dto.LectureDTO;
import com.education_platform.dto.ModuleDTO;
import com.education_platform.dto.ShortTestDTO;
import com.education_platform.model.Lecture;
import com.education_platform.model.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class LectureService {
    @Autowired
    LectureRepository lectureRepository;

    public List<LectureDTO> getLectureDTOsByModule(Long id){
        List<Lecture> lectures = lectureRepository.findAllByModuleId(id);
        lectures.sort(Comparator.comparingInt(Lecture::getNumber));
        return parsingLectureDTO(lectures);

    }

    public LectureDTO getLectureById(Long id){
        Lecture lecture = lectureRepository.findById(id).orElse(new Lecture());
        return parsingLectureDTO(List.of(lecture)).get(0);

    }

    private List<LectureDTO> parsingLectureDTO(List<Lecture> list ) {
        List<LectureDTO> LectureDTOs = new ArrayList<>();

        for (Lecture lecture : list) {
            LectureDTOs.add(LectureDTO.builder()
                            .id(lecture.getId())
                            .title(lecture.getTitle())
                            .video(lecture.getVideo())
                            .module_id(lecture.getModule().getId())
                            .description(lecture.getDescription())
                    .build());
        }

        return LectureDTOs;
    }
}
