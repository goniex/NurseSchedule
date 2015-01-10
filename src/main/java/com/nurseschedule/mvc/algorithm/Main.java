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
        List<String> feasiblePatternsWithNight = PatternUtil.findAllPatternsWithNight(patterns);
        List<String> feasiblePatternsWithoutNight = PatternUtil.findAllPatternsWithoutNight(patterns);

        // POPRAWIC TUTAJ FILTRACJE PATTERNOW, ZEBY CZESC ETATU DOSTAWALA WLASCIWE
        // for 32h workers (all + this)
        List<String> feasiblePatterns32Hours = PatternUtil.partTimeWorkFiltrationFirstStep(patterns);
        // for 20h workers (only this)
        List<String> feasiblePatterns20Hours = PatternUtil.partTimeWorkFiltrationSecondStep(patterns);

        List<ShiftCounter> dayShiftCounters = new ArrayList<ShiftCounter>();
        List<ShiftCounter> nightShiftCounters = new ArrayList<ShiftCounter>();
        ScheduleUtil.createWorkWeekCounters(dayShiftCounters, nightShiftCounters);
        ScheduleUtil.createWeekendCounters(dayShiftCounters, nightShiftCounters);

        // tworzymy pielegniarki na caly etat i przydzielamy im patterny, zaczynajac od nocek, nastepnie poprzez dni, az nie przydzielimy dla wszystkich 12
        // pielegniarek. Zwiekszamy tez liczniki
        List<NurseSto> nursesForFullJob = createNursesToTest(ScheduleConfig.FULL_TIME_NURSES_NUMBER, 0);
        ScheduleUtil.createWeekSchedule(feasiblePatternsWithNight, feasiblePatternsWithoutNight, nursesForFullJob, 0, dayShiftCounters, nightShiftCounters);

        // najpierw trzeba znalezc patterny, ktore pasuja do tego czego nam brakuje zarowno dla tych, ktorzy sa na 32h jak i tych, ktorzy sa na 20h
        // po tym kroku sprawdzamy czy liczniki sa juz uzupelnione, jesli nie to ze zbioru wszystkich patternów bierzemy jakiejolwiek, ktore pasuja i dajemy
        List<NurseSto> nursesFor32Hours = createNursesToTest(ScheduleConfig.HOURS_32_NURSES_NUMBER + ScheduleConfig.FULL_TIME_NURSES_NUMBER,
                ScheduleConfig.FULL_TIME_NURSES_NUMBER);
        List<NurseSto> nursesFor20Hours = createNursesToTest(ScheduleConfig.HOURS_20_NURSES_NUMBER + ScheduleConfig.FULL_TIME_NURSES_NUMBER
                + ScheduleConfig.HOURS_32_NURSES_NUMBER, ScheduleConfig.FULL_TIME_NURSES_NUMBER + ScheduleConfig.HOURS_32_NURSES_NUMBER);
        ScheduleUtil.setProperPatternsForTimeWorker(feasiblePatterns32Hours, feasiblePatterns20Hours, nursesFor32Hours, nursesFor20Hours, 0, dayShiftCounters,
                nightShiftCounters);
        ScheduleUtil.fillWeekScheduleUsingWrongPatterns(feasiblePatterns32Hours, feasiblePatterns20Hours, nursesFor32Hours, nursesFor20Hours, 0,
                dayShiftCounters, nightShiftCounters);
        List<NurseSto> allNurses = new ArrayList<NurseSto>();
        allNurses.addAll(nursesForFullJob);
        allNurses.addAll(nursesFor32Hours);
        allNurses.addAll(nursesFor20Hours);
        ScheduleUtil.setEmptyPatternForNotNecessaryWorkers(allNurses, 0);

        printNursesDetails(nursesForFullJob, 0);
        printNursesDetails(nursesFor32Hours, 0);
        printNursesDetails(nursesFor20Hours, 0);
        ScheduleUtil.printCounters(dayShiftCounters, nightShiftCounters);
        System.out.println("Wrong patterns number: " + ScheduleUtil.wrongPatterns);

        // teraz calosc trzeba zapetlic dla wielu tygodni i zrobic odpowiednia filtracje patternow. Przed rozpoczeciem iteracji na nowy tydzien nalezy
        // ustawic wszystkim pielegniarkom weekAvailability na 'true'
        // zapetlajac trzeba pamietac ze trzeba przydzielac patterny przefiltrowane na zmiane: na caly etat - 32h i 40h, na 32h zawsze 32h, na 20h - 16h i 24h

    }// main method

    private static void printNursesDetails(List<NurseSto> nurses, int weekIndex) {
        for (NurseSto nurse : nurses) {
            if ( nurse != null ) {
                System.out.println("Nurse: " + nurse.getId() + " Week availability: " + nurse.isWeekAvailability() + " Night availability: "
                        + nurse.isNightAvailability() + " pattern: " + nurse.getWeekPattern(weekIndex));
            }
        }
    }

    private static List<NurseSto> createNursesToTest(int number, int previousQuantity) {
        List<NurseSto> nurses = new ArrayList<NurseSto>();
        for (int i = previousQuantity; i < number; ++i) {
            NurseSto nurse = new NurseSto();
            nurse.setId(i);
            nurse.setWeekAvailability(true);
            nurse.setNightAvailability(true);
            nurses.add(nurse);
        }
        return nurses;
    }
}
