package com.nurseschedule.mvc.algorithm;

import java.io.FileNotFoundException;
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

        // for all time workers: 40h + 32h patterns
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

        // tworzymy pielegniarki na caly etat i przydzielamy im patterny, zaczynajac od nocek, nastepnie poprzez dni, az nie przydzielimy dla wszystkich 12
        // pielegniarek. Zwiekszamy tez liczniki
        List<NurseSto> nursesForFullJob = createNursesToTest(ScheduleConfig.FULL_TIME_NURSES_NUMBER, 0);
        List<NurseSto> nursesFor20Hours = createNursesToTest(ScheduleConfig.HOURS_20_NURSES_NUMBER + ScheduleConfig.FULL_TIME_NURSES_NUMBER
                + ScheduleConfig.HOURS_32_NURSES_NUMBER, ScheduleConfig.FULL_TIME_NURSES_NUMBER + ScheduleConfig.HOURS_32_NURSES_NUMBER);
        List<NurseSto> nursesFor32Hours = createNursesToTest(ScheduleConfig.HOURS_32_NURSES_NUMBER + ScheduleConfig.FULL_TIME_NURSES_NUMBER,
                ScheduleConfig.FULL_TIME_NURSES_NUMBER);
        List<NurseSto> allNurses = new ArrayList<NurseSto>();
        allNurses.addAll(nursesForFullJob);
        allNurses.addAll(nursesFor32Hours);
        allNurses.addAll(nursesFor20Hours);

        for (int i = 0; i < ScheduleConfig.ALL_SCHEDULE_WEEK_NUMBER; ++i) {
            ScheduleUtil.setWeekAvailabilityForAllNurses(allNurses);
            if ( i < ScheduleConfig.ALL_SCHEDULE_WEEK_NUMBER - 1 ) {
                ScheduleUtil.createWeekSchedule(feasiblePatternsWithNight40Hours, feasiblePatternsWithoutNight40Hours, nursesForFullJob, i, dayShiftCounters,
                        nightShiftCounters);
                ScheduleUtil.setProperPatternsForPartTimeWorker(feasiblePatterns32Hours, feasiblePatterns24Hours, nursesFor32Hours, nursesFor20Hours, i,
                        dayShiftCounters, nightShiftCounters);
                ScheduleUtil.fillWeekScheduleUsingWrongPatterns(nursesFor32Hours, nursesFor20Hours, i, dayShiftCounters, nightShiftCounters);
                ScheduleUtil.setEmptyPatternForNotNecessaryWorkers(allNurses, i);

                System.out.println("********************************* WEEK " + (i + 1) + " ************************************");
                printNursesDetails(nursesForFullJob, i);
                printNursesDetails(nursesFor32Hours, i);
                printNursesDetails(nursesFor20Hours, i);
                ScheduleUtil.printCounters(dayShiftCounters, nightShiftCounters);
                System.out.println("Wrong patterns all number: " + ScheduleUtil.wrongPatterns);
            } else {
                //POPRACOWAC NAD TYM, NIE DZIALA BO NIE MA ODPOWIEDNICH PATTERNOW, KTORE MOGLYBY ZAPELNIC LICZNIKI NOCEK 
                //(MOZE ROZWIAZANIEM BEDZIE W TEJ SYTUACJI DOROBIENIE W METODZIE CREATEWEEKSCHEDULE JAKIEOS BOOLA, KTORY ROZROZNI
                //CZY AKTUALNIE ZACZYNAMY OD NORMALNYCH PIELEGNIAREK CZY TYCH NA CZESC ETATU I W TAKIEJ SYTUACJI BEDZIEMY 
                //PRZYDZIELAC FEJKOWE PATTERNY NA NOCKI I ZWIEKSZAC ZNANY JUZ LICZNIK,
                List<NursePattern> partTimeNightPatterns = PatternUtil.getPartTimeWorkerNightPattern(feasiblePatterns24Hours);
                List<NursePattern> partTimeDayPatterns = PatternUtil.getPartTimeWorkerDayPattern(feasiblePatterns24Hours);
                ScheduleUtil.createWeekSchedule(partTimeNightPatterns, partTimeDayPatterns, nursesFor20Hours, i, dayShiftCounters, nightShiftCounters);
                ScheduleUtil.printCounters(dayShiftCounters, nightShiftCounters);
            }

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
