package com.nurseschedule.mvc.controller;

/**
 * @author Tomasz Morek
 */

import com.nurseschedule.mvc.dto.NurseDto;
import com.nurseschedule.mvc.object.Event;
import com.nurseschedule.mvc.service.INurseService;
import com.nurseschedule.mvc.utils.ResponseUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /**
     * Get all nurses schedule
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
    	//TODO: Generowanie raportu
        return new ResponseUtil(new ArrayList<String>());
    }

    /**
     * Generate events for many nurses
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
     * @param events
     * @param nurseDto
     */
    private void generateEvents(List<Event> events, NurseDto nurseDto) {
        final Map<String, String> types = new HashMap<>();
        types.put("R", "inverse");
        types.put("d", "warning");
        types.put("N", "info");
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
