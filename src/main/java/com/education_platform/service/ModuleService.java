package com.education_platform.service;

import com.education_platform.data.ModuleRepository;
import com.education_platform.dto.ShortModuleDTO;
import com.education_platform.model.Module;
import com.education_platform.dto.ModuleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class ModuleService {

    @Autowired
    ModuleRepository moduleRepository;

    @Autowired
    LectureService lectureService;

    @Autowired
    TestService testService;

    public List<ShortModuleDTO> getModuleDTOsByCourse(Long id){
        List<Module> modules = moduleRepository.findAllByCourseId(id);
        modules.sort(Comparator.comparingInt(Module::getNumber));
        return parsingShortModuleDTO(modules);
    }



    public ModuleDTO getModuleById(Long id){
        Module module = moduleRepository.findById(id).orElse(new Module());
        return parsingModuleDTO(List.of(module)).get(0);
    }

    private List<ModuleDTO> parsingModuleDTO(List<Module> list) {
        List<ModuleDTO> ModuleDTOs = new ArrayList<>();

        for (Module module : list) {
            ModuleDTOs.add(ModuleDTO.builder()
                            .id(module.getId())
                            .title(module.getTitle())
                            .duration(module.getDuration())
                            .number(module.getNumber())
                            .course_id(module.getCourse().getId())
                            .lectures(lectureService.getLectureDTOsByModule(module.getId()))
                            .tests(testService.getTestDTOByModule(module.getId()))
                    .build());
        }

        return ModuleDTOs;
    }


    private List<ShortModuleDTO> parsingShortModuleDTO(List<Module> list) {
        List<ShortModuleDTO> ModuleDTOs = new ArrayList<>();

        for (Module module : list) {
            ModuleDTOs.add(ShortModuleDTO.builder()
                    .id(module.getId())
                    .title(module.getTitle())
                    .duration(module.getDuration())
                    .build());
        }

        return ModuleDTOs;
    }


}
