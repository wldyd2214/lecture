package com.hhplus.lecture.spring.api.controller.lecture.dto.common;

import com.hhplus.lecture.spring.api.controller.lecture.dto.response.LectureApplyResponseDTO;
import com.hhplus.lecture.spring.api.controller.lecture.dto.response.LectureListResponseDTO;
import com.hhplus.lecture.spring.domain.application.Application;
import com.hhplus.lecture.spring.domain.lecture.Lecture;
import com.hhplus.lecture.spring.domain.schedule.LectureSchedule;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LectureAssembler {
    public static LectureApplyResponseDTO toApplyResponseDTO(LectureSchedule schedule) {

        LectureDTO lectureDTO =
            LectureDTO.builder()
                      .key(schedule.getLecture().getKey())
                      .title(schedule.getLecture().getTitle())
                      .desc(schedule.getLecture().getDesc())
                      .schedules(
                          List.of(toScheduleDTO(schedule))
                      )
                      .build();

        return LectureApplyResponseDTO.of(lectureDTO);
    }

    public static LectureListResponseDTO toListResponseDTO(List<Lecture> lectures) {

        List<LectureDTO> lectureDTOList = new ArrayList<>();

        for (Lecture lecture : lectures) {
            lectureDTOList.add(toLectureDTO(lecture));
        }

        return LectureListResponseDTO.of(lectureDTOList);
    }

    private static LectureDTO toLectureDTO(Lecture lecture) {
        return LectureDTO.builder()
                         .key(lecture.getKey())
                         .title(lecture.getTitle())
                         .desc(lecture.getDesc())
                         .schedules(
                             lecture.getLectureSchedules()
                                    .stream()
                                    .map(LectureAssembler::toScheduleDTO)
                                    .collect(Collectors.toList())
                         )
                         .build();
    }

    public static LectureScheduleDTO toScheduleDTO(LectureSchedule schedule) {
        LectureScheduleDTO scheduleDTO =
            LectureScheduleDTO.builder()
                              .key(schedule.getKey())
                              .startDate(schedule.getRegDate())
                              .maxCount(schedule.getMaxCount())
                              .applications(toApplicationListDTO(schedule.getApplications()))
                              .build();

        return scheduleDTO;
    }

    public static List<ApplicationDTO> toApplicationListDTO(List<Application> applications) {

        List<ApplicationDTO> applicationDTOList = new ArrayList<>();

        for (Application application : applications) {
            applicationDTOList.add(toApplicationDTO(application));
        }

        return applicationDTOList;
    }

    public static ApplicationDTO toApplicationDTO(Application application) {
        ApplicationDTO applicationDTO =
            ApplicationDTO.builder()
                          .key(application.getKey())
                          .userId(application.getUserId())
                          .regDate(application.getRegDate())
                          .build();

        return applicationDTO;
    }

}
