package com.nurseschedule.mvc.algorithm;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.nurseschedule.mvc.dto.NurseDto;

public class ScheduleUtil {

    private static List<String> weekSchedule = new ArrayList<String>();

    private static List<ShiftCounter> dayShiftCounters = new ArrayList<ShiftCounter>();

    private static List<ShiftCounter> nightShiftCounters = new ArrayList<ShiftCounter>();

    protected static List<NurseDto> createNursesToTest(int number) {
        List<NurseDto> nurses = new ArrayList<NurseDto>();
        for (int i = 0; i < number; ++i) {
            NurseDto nurse = new NurseDto();
            nurse.setId(i);
            nurses.add(nurse);
        }
        return nurses;
    }

    protected static void createWorkWeekCounters() {
        for (int i = 0; i < ScheduleConfig.WORKWEEK_LENGTH; ++i) {
            ShiftCounter dayShift = new ShiftCounter(ScheduleConfig.WEKK_DAYS[i], ScheduleConfig.WORKWEEK_DAY_SHIFT_NURSES_NUMBER);
            ShiftCounter nightShift = new ShiftCounter(ScheduleConfig.WEKK_DAYS[i], ScheduleConfig.NIGHT_SHIFT_NURSES_NUMBER);
            dayShiftCounters.add(dayShift);
            nightShiftCounters.add(nightShift);
        }
    }

    protected static void createWeekendCounters() {
        for (int i = 0; i < ScheduleConfig.WEEKEND_LEGTH; ++i) {
            ShiftCounter dayShift = new ShiftCounter(ScheduleConfig.WEKK_DAYS[ScheduleConfig.WORKWEEK_LENGTH + i],
                    ScheduleConfig.WEEKEND_DAY_SHIFT_NURSES_NUMBER);
            ShiftCounter nightShift = new ShiftCounter(ScheduleConfig.WEKK_DAYS[ScheduleConfig.WORKWEEK_LENGTH + i], ScheduleConfig.NIGHT_SHIFT_NURSES_NUMBER);
            dayShiftCounters.add(dayShift);
            nightShiftCounters.add(nightShift);
        }
    }

    protected static void createWeekSchedule(List<String> feasiblePatternsWithNight, List<String> feasiblePatternsWithoutNight, List<NurseDto> nurses, int size) {
        boolean success = false;
        while ( !success) {
            weekSchedule.clear();
            List<String> feasiblePatternsWithNightCopy = new ArrayList<String>(feasiblePatternsWithNight);
            List<String> feasiblePatternsWithoutNightCopy = new ArrayList<String>(feasiblePatternsWithoutNight);
            setAllCountersToZero(dayShiftCounters, nightShiftCounters);
            for (int i = 0; i < nurses.size(); ++i) {
                createWeekSchedulePart(feasiblePatternsWithNightCopy);
            }
            for (int i = 0; i < nurses.size(); ++i) {
                createWeekSchedulePart(feasiblePatternsWithoutNightCopy);
            }
            success = checkWeekScheduler(weekSchedule, size);
            System.out.println(weekSchedule.toString() + " " + weekSchedule.size());
        }
    }

    protected static void fillWeekScheduleWithPartTimeWorkers(List<String> feasiblePatterns, List<NurseDto> nurses, int size) {
        boolean success = false;
        while ( !success) {
            for (int i = 0; i < nurses.size(); ++i) {
                createWeekSchedulePart(feasiblePatterns);
            }
            success = checkWeekScheduler(weekSchedule, size);
        }
    }

    private static void createWeekSchedulePart(List<String> feasiblePatternsCopy) {
        int numbersCounter = 0;
        int randomIndex;
        while (numbersCounter < feasiblePatternsCopy.size() - 1) {
            randomIndex = getRandomPattern(feasiblePatternsCopy.size());
            numbersCounter++;
            String pattern = feasiblePatternsCopy.get(randomIndex);
            if ( checkPatterMatching(pattern, dayShiftCounters, nightShiftCounters) ) {
                weekSchedule.add(pattern);
                break;
            } else {
                if ( feasiblePatternsCopy.size() == 0 ) {
                    break;
                }
                continue;
            }
        }
    }

    private static boolean checkPatterMatching(String pattern, List<ShiftCounter> dayShiftCounters, List<ShiftCounter> nightShiftCounters) {
        for (int i = 0; i < pattern.length(); ++i) {
            if ( String.valueOf(pattern.charAt(i)).equals(Shift.d.toString()) ) {
                if ( !dayShiftCounters.get(i).canIncrement() ) {
                    return false;
                }
            } else if ( String.valueOf(pattern.charAt(i)).equals(Shift.N.toString()) ) {
                if ( !nightShiftCounters.get(i).canIncrement() ) {
                    return false;
                }
            }
        }
        incrementShiftCounters(pattern, dayShiftCounters, nightShiftCounters);
        return true;
    }

    private static void incrementShiftCounters(String pattern, List<ShiftCounter> dayShiftCounters, List<ShiftCounter> nightShiftCounters) {
        for (int i = 0; i < pattern.length(); ++i) {
            if ( String.valueOf(pattern.charAt(i)).equals(Shift.d.toString()) ) {
                dayShiftCounters.get(i).incrementCounter();
            } else if ( String.valueOf(pattern.charAt(i)).equals(Shift.N.toString()) ) {
                nightShiftCounters.get(i).incrementCounter();
            }
        }
    }

    private static int getRandomPattern(int size) {
        Random random = new Random();
        return random.nextInt(size);
    }

    private static void setAllCountersToZero(List<ShiftCounter> dayShiftCounters, List<ShiftCounter> nightShiftCounters) {
        for (ShiftCounter counter : dayShiftCounters) {
            counter.zeroCounter();
        }
        for (ShiftCounter counter : nightShiftCounters) {
            counter.zeroCounter();
        }
    }

    private static boolean checkWeekScheduler(List<String> scheduler, int size) {
        return scheduler.size() == size;
    }

    protected static void savePatternsToFile(List<String> patterns, String fileName) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(fileName);
        for (String pattern : patterns) {
            writer.println(pattern);
        }
        writer.close();
    }

    protected static void printCounters() {
        System.out.println(dayShiftCounters.toString());
        System.out.println(nightShiftCounters.toString());
    }

}
