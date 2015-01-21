package com.nurseschedule.mvc.controller;

/**
 * @author Tomasz Morek
 */

import com.nurseschedule.mvc.dto.NurseDto;
import com.nurseschedule.mvc.object.Event;
import com.nurseschedule.mvc.service.INurseService;
import com.nurseschedule.mvc.utils.ResponseUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Schedule REST controller
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

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public ResponseUtil get() {
        List<NurseDto> nurseDtos = this.nurseService.findAll();
        List<Event> events = new ArrayList<>();
        generateEvents(nurseDtos, events);
        return new ResponseUtil((List) events);
    }
    
    @RequestMapping(value = "/generate", method = RequestMethod.GET)
    @ResponseBody
    public ResponseUtil generate() {
    	//TODO: Generowanie raportu
        return new ResponseUtil(new ArrayList<String>());
    }

    /**
     * Generate events
     * @param nurseDtos
     * @param events
     */
    private void generateEvents(List<NurseDto> nurseDtos, List<Event> events) {
        final Map<String, String> types = new HashMap<>();
        types.put("R", "inverse");
        types.put("d", "warning");
        types.put("N", "info");

        for (NurseDto nurseDto : nurseDtos) {
            if (nurseDto.getSchedule() != null && nurseDto.getSchedule().length() > 0) {
                String fullName = nurseDto.getName() + " " + nurseDto.getLastName();
                String[] patterns = nurseDto.getSchedule().split("");
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                calendar.set(Calendar.DAY_OF_WEEK_IN_MONTH, 1);
                for (String pattern : patterns) {
                    Event event = new Event(fullName, types.get(pattern),
                            calendar.getTime(), calendar.getTime());
                    events.add(event);
                    calendar.add(Calendar.DATE, 1);
                }
            }
        }
    }

}
