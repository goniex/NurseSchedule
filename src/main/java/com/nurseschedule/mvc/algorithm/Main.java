package com.nurseschedule.mvc.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.nurseschedule.mvc.sto.NurseSto;

/**
 * @author Caro
 * @date 5 gru 2014
 */
public class Main {

    private static List<Shift> shifts = Arrays.asList(Shift.values());

    private static List<String> patterns = new ArrayList<String>();

    public static void main(String[] args) {
        PatternUtil.getPermutations(patterns, shifts, ScheduleConfig.WORKWEEK_LENGTH + ScheduleConfig.WEEKEND_LEGTH, "");

        List<NursePattern> consistentPatterns = PatternUtil.filtrationBasedOnConditions(patterns);
        for (NursePattern pattern : consistentPatterns) {
            System.out.println(pattern.getPattern());
        }
        System.out.println(consistentPatterns.size());

        // for all time workers: 40h + 32h patterns
        /*List<NursePattern> feasiblePatternsWithNight40Hours = PatternUtil.getPatternBasedOnHours(consistentPatterns, 40, true);
        List<NursePattern> feasiblePatternsWithoutNight40Hours = PatternUtil.getPatternBasedOnHours(consistentPatterns, 40, false);
        List<NursePattern> feasiblePatternsWithNight32Hours = PatternUtil.getPatternBasedOnHours(consistentPatterns, 32, true);
        List<NursePattern> feasiblePatternsWithoutNight32Hours = PatternUtil.getPatternBasedOnHours(consistentPatterns, 32, false);*/

        // for 32h workers (all + this)
        /*List<NursePattern> feasiblePatterns32Hours = new ArrayList<NursePattern>(feasiblePatternsWithNight32Hours);
        feasiblePatterns32Hours.addAll(feasiblePatternsWithoutNight32Hours);*/

        // for 20h workers: 16h + 24h patterns
        /*List<NursePattern> feasiblePatterns16Hours = PatternUtil.getPatternBasedOnHours(consistentPatterns, 16, true);
        feasiblePatterns16Hours.addAll(PatternUtil.getPatternBasedOnHours(consistentPatterns, 16, false));
        List<NursePattern> feasiblePatterns24Hours = PatternUtil.getPatternBasedOnHours(consistentPatterns, 24, true);
        feasiblePatterns24Hours.addAll(PatternUtil.getPatternBasedOnHours(consistentPatterns, 24, false));*/

        List<NursePattern> feasiblePatternsWithNight40Hours = PatternUtil.findAllPatternsWithNight(patterns, 40);
        List<NursePattern> feasiblePatternsWithoutNight40Hours = PatternUtil.findAllPatternsWithoutNight(patterns, 40);
        List<NursePattern> feasiblePatternsWithNight32Hours = PatternUtil.findAllPatternsWithNight(patterns, 32);
        List<NursePattern> feasiblePatternsWithoutNight32Hours = PatternUtil.findAllPatternsWithoutNight(patterns, 32);

        // for 32h workers (all + this)
        List<NursePattern> feasiblePatterns32Hours = new ArrayList<NursePattern>(feasiblePatternsWithNight32Hours);
        feasiblePatterns32Hours.addAll(feasiblePatternsWithoutNight32Hours);

        // for 20h workers: 16h + 24h patterns
        List<NursePattern> feasiblePatterns16Hours = PatternUtil.partTimeWorkFiltration(patterns, 16);
        List<NursePattern> feasiblePatterns24Hours = PatternUtil.partTimeWorkFiltration(patterns, 24);

        // create necessary counters
        List<ShiftCounter> dayShiftCounters = new ArrayList<ShiftCounter>();
        List<ShiftCounter> nightShiftCounters = new ArrayList<ShiftCounter>();
        ScheduleUtil.createWorkWeekCounters(dayShiftCounters, nightShiftCounters);
        ScheduleUtil.createWeekendCounters(dayShiftCounters, nightShiftCounters);

        List<NurseSto> nursesForFullJob = createNursesToTest(ScheduleConfig.FULL_TIME_NURSES_NUMBER, 0);
        List<NurseSto> nursesFor20Hours = createNursesToTest(ScheduleConfig.HOURS_20_NURSES_NUMBER + ScheduleConfig.FULL_TIME_NURSES_NUMBER
                + ScheduleConfig.HOURS_32_NURSES_NUMBER, ScheduleConfig.FULL_TIME_NURSES_NUMBER + ScheduleConfig.HOURS_32_NURSES_NUMBER);
        List<NurseSto> nursesFor32Hours = createNursesToTest(ScheduleConfig.HOURS_32_NURSES_NUMBER + ScheduleConfig.FULL_TIME_NURSES_NUMBER,
                ScheduleConfig.FULL_TIME_NURSES_NUMBER);
        List<NurseSto> allNurses = new ArrayList<NurseSto>();
        allNurses.addAll(nursesForFullJob);
        allNurses.addAll(nursesFor32Hours);
        allNurses.addAll(nursesFor20Hours);

        // po rozbiciu i losowaniu na zmiane 32/40h dla pielegniarek na caly etat i 16/24h dla pielegniarek na 20h czasami liczniki sa niedorobione
        // tutaj mogloby zamiast takiego rozbijania pomoc liczenie godzin i zastosowanie tez patternow z 6 zmianami (na razie sa tylko 4 i 5)
        for (int i = 0; i < ScheduleConfig.ALL_SCHEDULE_WEEK_NUMBER; ++i) {
            ScheduleUtil.setWeekAvailabilityForAllNurses(allNurses);
            if ( i < ScheduleConfig.ALL_SCHEDULE_WEEK_NUMBER - 1 ) {
                if ( i % 2 == 0 ) {
                    ScheduleUtil.createWeekScheduleNights(feasiblePatternsWithNight40Hours, nursesForFullJob, i, dayShiftCounters, nightShiftCounters);
                    ScheduleUtil.createWeekScheduleDays(feasiblePatternsWithoutNight40Hours, nursesForFullJob, i, dayShiftCounters, nightShiftCounters);
                    ScheduleUtil.setProperPatternsForPartTimeWorker(feasiblePatterns32Hours, feasiblePatterns16Hours, nursesFor32Hours, nursesFor20Hours, i,
                            dayShiftCounters, nightShiftCounters);
                } else {
                    ScheduleUtil.createWeekScheduleNights(feasiblePatternsWithNight32Hours, nursesForFullJob, i, dayShiftCounters, nightShiftCounters);
                    ScheduleUtil.createWeekScheduleDays(feasiblePatternsWithoutNight32Hours, nursesForFullJob, i, dayShiftCounters, nightShiftCounters);
                    ScheduleUtil.setProperPatternsForPartTimeWorker(feasiblePatterns32Hours, feasiblePatterns24Hours, nursesFor32Hours, nursesFor20Hours, i,
                            dayShiftCounters, nightShiftCounters);
                }
                ScheduleUtil.fillWeekScheduleUsingWrongPatterns(nursesFor32Hours, nursesFor20Hours, i, dayShiftCounters, nightShiftCounters);
                ScheduleUtil.setEmptyPatternForNotNecessaryWorkers(allNurses, i);
            } else {
                List<NursePattern> partTimeNightPatterns = PatternUtil.getPartTimeWorkerNightPattern(feasiblePatterns24Hours);
                partTimeNightPatterns.addAll(feasiblePatterns16Hours);
                ScheduleUtil.createWeekScheduleNights(partTimeNightPatterns, nursesFor20Hours, i, dayShiftCounters, nightShiftCounters);
                ScheduleUtil.createWeekScheduleDays(feasiblePatternsWithoutNight40Hours, nursesForFullJob, i, dayShiftCounters, nightShiftCounters);
                ScheduleUtil.fillWeekScheduleUsingWrongPatterns(nursesForFullJob, nursesFor32Hours, i, dayShiftCounters, nightShiftCounters);
                ScheduleUtil.setEmptyPatternForNotNecessaryWorkers(allNurses, i);
            }

            System.out.println("********************************* WEEK " + (i + 1) + " ************************************");
            printNursesDetails(nursesForFullJob, i);
            printNursesDetails(nursesFor32Hours, i);
            printNursesDetails(nursesFor20Hours, i);
            ScheduleUtil.printCounters(dayShiftCounters, nightShiftCounters);
            System.out.println("Wrong patterns all number: " + ScheduleUtil.wrongPatterns);

        }

    }// main method

    private static void printNursesDetails(List<NurseSto> nurses, int weekIndex) {
        for (NurseSto nurse : nurses) {
            if ( nurse != null ) {
                System.out.println("Nurse: " + nurse.getId() + " Week availability: " + nurse.isWeekAvailability() + " Night availability: "
                        + nurse.isGeneralNightAvailability() + " pattern: " + nurse.getWeekPattern(weekIndex));
            }
        }
    }

    private static List<NurseSto> createNursesToTest(int number, int previousQuantity) {
        List<NurseSto> nurses = new ArrayList<NurseSto>();
        for (int i = previousQuantity; i < number; ++i) {
            NurseSto nurse = new NurseSto();
            nurse.setId(i);
            nurse.setWeekAvailability(true);
            nurse.setGeneralNightAvailability(true);
            nurse.setNightAvailablityForBegin();
            nurses.add(nurse);
        }
        return nurses;
    }
}
