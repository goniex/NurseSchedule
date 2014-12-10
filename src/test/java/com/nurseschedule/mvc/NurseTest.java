package com.nurseschedule.mvc;

import com.nurseschedule.mvc.dto.NurseDto;
import com.nurseschedule.mvc.service.INurseService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @author Tomasz Morek
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"file:src/main/webapp/WEB-INF/NurseSchedule-servlet.xml",
        "classpath:hibernate.cfg.xml"})
public class NurseTest {

    @Autowired
    INurseService nurseService;

    @Test
    public void testGetNurseById() throws Exception {
        NurseDto nurse = this.nurseService.findById(25);
        Assert.assertNotNull(nurse);
    }

    @Test
    public void testGetAllNurses() throws Exception {
        List<NurseDto> nurses = this.nurseService.findAll();
        Assert.assertNotNull(nurses);
    }

    @Test
    public void testCreateNurse() throws Exception {
        Integer id = this.nurseService.create("mail@mail.com", "haslo", "test", "test", 30, "N");
        Assert.assertNotNull(this.nurseService.findById(id));
        this.nurseService.delete(id);
    }

    @Test
    public void testDeleteNurse() throws Exception {
        Integer id = this.nurseService.create("mail@mail.com", "haslo", "test", "test", 30, "N");
        Assert.assertNotNull(this.nurseService.findById(id));
        NurseDto nurseToDelete = new NurseDto();
        this.nurseService.delete(id);
        Assert.assertNull(this.nurseService.findById(id));
    }

    @Test
    public void testUpdateNurse() throws Exception {
        Integer id = this.nurseService.create("mail@mail.com", "haslo", "test", "test", 30, "N");
        NurseDto nurse = this.nurseService.findById(id);
        String newName = "Newname";
        nurse.setName(newName);
        this.nurseService.update(nurse);
        nurse = this.nurseService.findById(id);
        Assert.assertEquals(newName, nurse.getName());
        this.nurseService.delete(id);
    }

    @Test
    public void testGetNurseByEmail() throws Exception {
        final String email = "Zuzanna.Kaczmarek@mail.com";
        NurseDto nurse = this.nurseService.findByEmail(email);
        Assert.assertEquals("Zuzanna", nurse.getName());
    }
}
