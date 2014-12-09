package com.nurseschedule.mvc.algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.nurseschedule.mvc.dto.NurseDto;

/**
 * @author Caro
 * @date 5 gru 2014
 */
public class Main {

    private static List<Shift> shifts = Arrays.asList(Shift.values());

    private static List<String> patterns = new ArrayList<String>();

    public static void main(String[] args) {
        PatternUtil.getPermutations(patterns, shifts, 7, "");
        List<String> feasiblePatternsWithNight = PatternUtil.findAllPatternsWithNight(patterns);
        List<String> feasiblePatternsWithoutNight = PatternUtil.findAllPatternsWithoutNight(patterns);

        // for 32h workers (all + this)
        List<String> feasiblePatternForPartTimeFirst = PatternUtil.partTimeWorkFiltrationFirstStep(patterns);
        // for 20h workers (only this)
        List<String> feasiblePatternForPartTimeSecond = PatternUtil.partTimeWorkFiltrationSecondStep(patterns);

        // stworzenie licznikow zmian na kazdy dzien i noc
        ScheduleUtil.createWorkWeekCounters();
        ScheduleUtil.createWeekendCounters();
        // stworzenie pielegniarek do testow (na caly etat)
        List<NurseDto> nursesForFullJob = ScheduleUtil.createNursesToTest(ScheduleConfig.FULL_TIME_NURSES_NUMBER);
        // tworzymy schedule na 1 tydzieñ
        ScheduleUtil.createWeekSchedule(feasiblePatternsWithNight, feasiblePatternsWithoutNight, nursesForFullJob, ScheduleConfig.FULL_TIME_NURSES_NUMBER);
        ScheduleUtil.printCounters();

        // uzupelnianie harmonogramu pielegniarkami pracujacymi na czesc etatu
        // TUTAJ TRZEBA COS WYKMINIC, BO TERAZ DOCHODZE DO SYTUACJI, ZE BRAKUJE ODPOWIEDNICH PATTERNOW PO PRZYDZIELENIU PIELEGNIARKOM NA CALY ETAT
        // LICZNIKI SA NIEMAL ZAPELNIONE TUTAJ
        List<NurseDto> nursesForPartJob = ScheduleUtil.createNursesToTest(ScheduleConfig.PART_TIME_NURSES_NUMBER);
        feasiblePatternForPartTimeFirst.addAll(feasiblePatternsWithNight);
        feasiblePatternForPartTimeFirst.addAll(feasiblePatternsWithoutNight);
        ScheduleUtil.fillWeekScheduleWithPartTimeWorkers(feasiblePatternForPartTimeFirst, nursesForPartJob, ScheduleConfig.FULL_TIME_NURSES_NUMBER
                + ScheduleConfig.PART_TIME_NURSES_NUMBER);

        /*List<NurseDto> nursesForPartJobSecond = ScheduleUtil.createNursesToTest(ScheduleConfig.HALF_TIME_NURSES_NUMBER);
        ScheduleUtil.fillWeekScheduleWithPartTimeWorkers(feasiblePatternForPartTimeSecond, nursesForPartJobSecond, ScheduleConfig.FULL_TIME_NURSES_NUMBER
                + ScheduleConfig.PART_TIME_NURSES_NUMBER + ScheduleConfig.HALF_TIME_NURSES_NUMBER);*/

        // sprawdzanie na koncu czy udalo sie zapelnic wszystkie tygodniowe zmiany, jesli nie to generujemy week schedule od nowa

    }// main method

}
