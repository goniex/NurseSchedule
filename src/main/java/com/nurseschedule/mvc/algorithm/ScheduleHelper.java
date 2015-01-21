package com.nurseschedule.mvc.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import pl.nurseschedule.mvc.sto.NurseSto;

public class ScheduleHelper {

    public List<NurseSto> generateSchedule(List<NurseSto> nurses) {
        int drawNumber = 30;
        List<Shift> shifts = Arrays.asList(Shift.values());
        List<String> patterns = new ArrayList<String>();
        PatternUtil.getPermutations(patterns, shifts, ScheduleConfig.WORKWEEK_LENGTH + ScheduleConfig.WEEKEND_LEGTH, "");
        List<NursePattern> consistentPatterns = PatternUtil.filtrationBasedOnConditions(patterns);

        // for all time workers: 40h + 32h patterns
        List<NursePattern> feasiblePatternsWithNight40Hours = PatternUtil.getPatternBasedOnHours(consistentPatterns, 40, true);
        List<NursePattern> feasiblePatternsWithoutNight40Hours = PatternUtil.getPatternBasedOnHours(consistentPatterns, 40, false);
        List<NursePattern> feasiblePatternsWithNight32Hours = PatternUtil.getPatternBasedOnHours(consistentPatterns, 32, true);
        List<NursePattern> feasiblePatternsWithoutNight32Hours = PatternUtil.getPatternBasedOnHours(consistentPatterns, 32, false);

        // for 32h workers (all + this)
        List<NursePattern> feasiblePatterns32Hours = new ArrayList<NursePattern>(feasiblePatternsWithNight32Hours);
        feasiblePatterns32Hours.addAll(feasiblePatternsWithoutNight32Hours);

        // for 20h workers: 16h + 24h patterns
        List<NursePattern> feasiblePatterns16Hours = PatternUtil.getPatternFor20hWorkers(consistentPatterns, 16);
        List<NursePattern> feasiblePatterns24Hours = PatternUtil.getPatternFor20hWorkers(consistentPatterns, 24);

        // create necessary counters
        List<ShiftCounter> dayShiftCounters = new ArrayList<ShiftCounter>();
        List<ShiftCounter> nightShiftCounters = new ArrayList<ShiftCounter>();
        ScheduleUtil.createWorkWeekCounters(dayShiftCounters, nightShiftCounters);
        ScheduleUtil.createWeekendCounters(dayShiftCounters, nightShiftCounters);

        List<NurseSto> nursesForFullJob = getNursesBasedOnWorkType(nurses, 36);
        List<NurseSto> nursesFor20Hours = getNursesBasedOnWorkType(nurses, 20);
        List<NurseSto> nursesFor32Hours = getNursesBasedOnWorkType(nurses, 32);
        List<NurseSto> allNurses = new ArrayList<NurseSto>();
        allNurses.addAll(nursesForFullJob);
        allNurses.addAll(nursesFor32Hours);
        allNurses.addAll(nursesFor20Hours);

        Map<Integer, List<NurseSto>> scheduleMap = new HashMap<Integer, List<NurseSto>>();
        for (int k = 0; k < drawNumber; ++k) {
            ScheduleUtil.setNightAvailabilityForAllNurses(allNurses);
            for (int i = 0; i < ScheduleConfig.ALL_SCHEDULE_WEEK_NUMBER; ++i) {
                ScheduleUtil.setWeekAvailabilityForAllNurses(allNurses);
                if ( i < ScheduleConfig.ALL_SCHEDULE_WEEK_NUMBER - 1 ) {
                    if ( i % 2 == 0 ) {
                        ScheduleUtil.createWeekScheduleNights(feasiblePatternsWithNight40Hours, nursesForFullJob, i, dayShiftCounters, nightShiftCounters);
                        ScheduleUtil.createWeekScheduleDays(feasiblePatternsWithoutNight40Hours, nursesForFullJob, i, dayShiftCounters, nightShiftCounters);
                        ScheduleUtil.setProperPatternsForPartTimeWorker(feasiblePatterns32Hours, feasiblePatterns16Hours, nursesFor32Hours, nursesFor20Hours,
                                i, dayShiftCounters, nightShiftCounters);
                    } else {
                        ScheduleUtil.createWeekScheduleNights(feasiblePatternsWithNight32Hours, nursesForFullJob, i, dayShiftCounters, nightShiftCounters);
                        ScheduleUtil.createWeekScheduleDays(feasiblePatternsWithoutNight32Hours, nursesForFullJob, i, dayShiftCounters, nightShiftCounters);
                        ScheduleUtil.setProperPatternsForPartTimeWorker(feasiblePatterns32Hours, feasiblePatterns24Hours, nursesFor32Hours, nursesFor20Hours,
                                i, dayShiftCounters, nightShiftCounters);
                    }
                    ScheduleUtil.fillWeekScheduleUsingWrongPatterns(nursesFor32Hours, nursesFor20Hours, i, dayShiftCounters, nightShiftCounters);
                } else {
                    List<NursePattern> partTimeNightPatterns = PatternUtil.getPartTimeWorkerNightPattern(feasiblePatterns24Hours);
                    partTimeNightPatterns.addAll(feasiblePatterns16Hours);
                    ScheduleUtil.createWeekScheduleNights(partTimeNightPatterns, nursesFor20Hours, i, dayShiftCounters, nightShiftCounters);
                    ScheduleUtil.createWeekScheduleDays(feasiblePatternsWithoutNight40Hours, nursesForFullJob, i, dayShiftCounters, nightShiftCounters);
                    ScheduleUtil.fillWeekScheduleUsingWrongPatterns(nursesForFullJob, nursesFor32Hours, i, dayShiftCounters, nightShiftCounters);
                }
                ScheduleUtil.setEmptyPatternForNotNecessaryWorkers(allNurses, i);
            }
            scheduleMap.put(ScheduleUtil.wrongPatterns, allNurses);
            ScheduleUtil.wrongPatterns = 0;
        }

        SortedSet<Integer> keys = new TreeSet<Integer>(scheduleMap.keySet());
        for (Integer key : keys) {
            allNurses = scheduleMap.get(key);
            System.out.println("Wrong patterns number: " + key);
            break;
        }

        for (NurseSto nurse : allNurses) {
            for (int i = 0; i < ScheduleConfig.ALL_SCHEDULE_WEEK_NUMBER; ++i) {
                nurse.setSchedule(nurse.getSchedule() + nurse.getWeekPattern(i));
            }
        }

        return ScheduleUtil.changeAllDaysToProperShift(allNurses);
    }// generate schedule method

    private List<NurseSto> getNursesBasedOnWorkType(List<NurseSto> allNurses, int workTime) {
        List<NurseSto> nursesPart = new ArrayList<NurseSto>();
        for (NurseSto nurse : allNurses) {
            if ( nurse.getWorkTime() == workTime ) {
                nursesPart.add(nurse);
            }
        }
        return nursesPart;
    }

    private void printNursesDetails(List<NurseSto> nurses) {
        for (NurseSto nurse : nurses) {
            System.out.println(nurse.getSchedule());
        }
    }

}