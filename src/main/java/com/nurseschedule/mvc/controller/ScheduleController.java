package com.nurseschedule.mvc.controller;

/**
 * @author Tomasz Morek
 */

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import pl.nurseschedule.mvc.sto.NurseSto;

import com.nurseschedule.mvc.algorithm.ScheduleHelper;
import com.nurseschedule.mvc.dto.NurseDto;
import com.nurseschedule.mvc.object.Event;
import com.nurseschedule.mvc.service.INurseService;
import com.nurseschedule.mvc.utils.ResponseUtil;

/**
 * Schedule REST controller
 *
 * @author Tomasz Morek
 */
@RestController
@RequestMapping(value = "/schedule")
public class ScheduleController {

    /**
     * Nurse service
     */
    @Autowired
    private INurseService nurseService;

    @Autowired
    private ScheduleHelper scheduleHelper;

    /**
     * Get all nurses schedule
     *
     * @return
     */
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public ResponseUtil get() {
        List<NurseDto> nurseDtos = this.nurseService.findAll();
        List<Event> events = new ArrayList<>();
        generateEvents(nurseDtos, events);
        return new ResponseUtil((List) events);
    }

    /**
     * Get one nurse schedule
     *
     * @return ResponseUtil
     */
    @RequestMapping(value = "/get/{id}", method = RequestMethod.GET)
    @ResponseBody
    public ResponseUtil get(@PathVariable Integer id) {
        NurseDto nurse = this.nurseService.findById(id);
        List<Event> events = new ArrayList<>();
        generateEvents(events, nurse);
        return new ResponseUtil((List) events);
    }

    @RequestMapping(value = "/generate", method = RequestMethod.GET)
    @ResponseBody
    public ResponseUtil generate() {
        // TODO: Generowanie raportu
        List<NurseDto> nurses = nurseService.findAll();
        List<NurseSto> nursesToSchedule = nursesToScheduleConvert(nurses);
        nursesToSchedule = scheduleHelper.generateSchedule(nursesToSchedule);
        assigneScheduleStringToNurses(nursesToSchedule, nurses);
        updateAllNurses(nurses);

        return new ResponseUtil(true);
    }

    /**
     * Generate events for many nurses
     *
     * @param nurseDtos
     * @param events
     */
    private void generateEvents(List<NurseDto> nurseDtos, List<Event> events) {
        for (NurseDto nurseDto : nurseDtos) {
            generateEvents(events, nurseDto);
        }
    }

    /**
     * Generate events for one nurse
     *
     * @param events
     * @param nurseDto
     */
    private void generateEvents(List<Event> events, NurseDto nurseDto) {
        final Map<String, String> types = new HashMap<>();
        types.put("R", "inverse");
        types.put("d", "warning");
        types.put("N", "info");
        if ( nurseDto.getSchedule() != null && nurseDto.getSchedule().length() > 0 ) {
            String fullName = nurseDto.getName() + " " + nurseDto.getLastName();
            String[] patterns = nurseDto.getSchedule().split("");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            calendar.set(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
            for (String pattern : patterns) {
                Event event = new Event(fullName, types.get(pattern), calendar.getTime(), calendar.getTime());
                events.add(event);
                calendar.add(Calendar.DATE, 1);
            }
        }
    }

    private List<NurseSto> nursesToScheduleConvert(List<NurseDto> nurses) {
        List<NurseSto> nursesToScheduleList = new ArrayList<NurseSto>();
        for (NurseDto nurse : nurses) {
            NurseSto nurseSto = new NurseSto();
            nurseSto.setId(nurse.getId());
            nurseSto.setWorkTime(nurse.getWorkTime());
            nurseSto.setWeekAvailability(true);
            nurseSto.setGeneralNightAvailability(true);
            nurseSto.setNightAvailablityForBegin();
            nursesToScheduleList.add(nurseSto);
        }
        return nursesToScheduleList;
    }

    private void assigneScheduleStringToNurses(List<NurseSto> stoNurses, List<NurseDto> nurses) {
        for (NurseDto nurse : nurses) {
            for (NurseSto nurseSto : stoNurses) {
                if ( nurse.getId() == nurseSto.getId() ) {
                    nurse.setSchedule(nurseSto.getSchedule());
                }
            }
        }
    }

    private void updateAllNurses(List<NurseDto> nurses) {
        for (NurseDto nurse : nurses) {
            nurseService.update(nurse);
        }
    }

}
