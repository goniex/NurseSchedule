package com.nurseschedule.mvc.controller;

import com.nurseschedule.mvc.dto.NurseDto;
import com.nurseschedule.mvc.object.Nurse;
import com.nurseschedule.mvc.service.INurseService;
import com.nurseschedule.mvc.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Nurse REST controller
 * @author Tomasz Morek
 */
@RestController
@RequestMapping(value = "/nurse")
public class NurseController {

    /**
     * Nurse service
     */
    @Autowired
    INurseService nurseService;

    /**
     * Get all nurses
     * @return ResponseUtil
     */
    @RequestMapping(value = "/get",
                    produces = MediaType.APPLICATION_JSON_VALUE,
                    consumes = MediaType.APPLICATION_JSON_VALUE,
                    method = RequestMethod.GET)
    @ResponseBody
    public ResponseUtil get() {
        List<NurseDto> nurseDtos = this.nurseService.findAll();
        List<Nurse> nurses = new ArrayList<>();
        for (NurseDto nurseDto : nurseDtos) {
            nurses.add(new Nurse(nurseDto));
        }
        return new ResponseUtil(nurses);
    }

}
