package pl.nurseschedule.mvc.sto;

import java.util.ArrayList;
import java.util.List;

import com.nurseschedule.mvc.algorithm.ScheduleConfig;
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

    private boolean generalNightAvailability;
    
    private ArrayList<Boolean> nightAvailabilityInWeeks;

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

    public boolean isGeneralNightAvailability() {
        return generalNightAvailability;
    }

    public void setGeneralNightAvailability(boolean generalNightAvailability) {
        this.generalNightAvailability = generalNightAvailability;
    }
    
    public void setNightAvailablityForBegin() {
        nightAvailabilityInWeeks = new ArrayList<Boolean>();
        for(int i=0; i<ScheduleConfig.ALL_SCHEDULE_WEEK_NUMBER; ++i) {
            nightAvailabilityInWeeks.add(true);
        }
    }
    
    public void setNightAvailabilityInWeeks(int week, boolean value) {
        nightAvailabilityInWeeks.set(week, value);
    }
    
    public boolean isNightAvailabilityInWeek(int week) {
        return nightAvailabilityInWeeks.get(week);
    }

    public void printWeekPattern(int weekIndex) {
        System.out.println(weekPatterns.get(weekIndex));
    }
    
}
