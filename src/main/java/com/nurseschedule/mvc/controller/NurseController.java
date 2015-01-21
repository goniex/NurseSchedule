package com.nurseschedule.mvc.controller;

import com.nurseschedule.mvc.dto.NurseDto;
import com.nurseschedule.mvc.object.Nurse;
import com.nurseschedule.mvc.service.INurseService;
import com.nurseschedule.mvc.utils.RequestUtil;
import com.nurseschedule.mvc.utils.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @ResponseBody
    public ResponseUtil get() {
        List<NurseDto> nurseDtos = this.nurseService.findAll();
        List<Nurse> nurses = new ArrayList<>();
        for (NurseDto nurseDto : nurseDtos) {
            nurses.add(new Nurse(nurseDto));
        }
        return new ResponseUtil((List) nurses);
    }

    /**
     * Get nurse by email
     * @return ResponseUtil
     */
    @RequestMapping(value = "/getByEmail", method = RequestMethod.POST)
    @ResponseBody
    public ResponseUtil get(@RequestBody Object requestObject) {
        Nurse nurse = (Nurse) RequestUtil.convert(requestObject, Nurse.class);
        NurseDto nurseDto = this.nurseService.findByEmail(nurse.getEmail());
        nurse = new Nurse(nurseDto);
        return new ResponseUtil(nurse);
    }

    /**
     * Save new nurse
     * @return ResponseUtil
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ResponseBody
    public ResponseUtil save(@RequestBody Object requestObject) {
        Nurse nurse = (Nurse) RequestUtil.convert(requestObject, Nurse.class);
        System.out.println(requestObject.toString());
        Integer id = this.nurseService.create(nurse.getEmail(), "haslo123", nurse.getName(),
                nurse.getLastName(), nurse.getWorkTime(), "N");
        nurse.setId(id);
        return new ResponseUtil(nurse, "Save correct");
    }

    /**
     * Delete nurse
     * @return ResponseUtil
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public ResponseUtil delete(@PathVariable Integer id) {
        this.nurseService.delete(id);
        return new ResponseUtil(true);
    }
}
