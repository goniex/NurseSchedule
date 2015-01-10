package pl.nurseschedule.mvc.sto;

import java.util.ArrayList;
import java.util.List;

import com.nurseschedule.mvc.dto.NurseDto;

/**
 * 3 sty 2015 14:13:05
 *
 * @author Karol Skupieñ
 *
 *         Nurse schedule tranfer object
 *
 */
public class NurseSto extends NurseDto {

    private List<String> weekPatterns = new ArrayList<String>();

    private boolean weekAvailability;

    private boolean nightAvailability;

    public void setWeekPattern(String pattern, int weekIndex) {
        weekPatterns.add(weekIndex, pattern);
    }

    public String getWeekPattern(int weekIndex) {
        return weekPatterns.get(weekIndex);
    }

    public void removeWeekPattern(int weekIndex) {
        weekPatterns.remove(weekIndex);
    }

    public boolean isWeekAvailability() {
        return weekAvailability;
    }

    public void setWeekAvailability(boolean weekAvailability) {
        this.weekAvailability = weekAvailability;
    }

    public boolean isNightAvailability() {
        return nightAvailability;
    }

    public void setNightAvailability(boolean nightAvailability) {
        this.nightAvailability = nightAvailability;
    }

    public void printWeekPattern(int weekIndex) {
        System.out.println(weekPatterns.get(weekIndex));
    }

}
