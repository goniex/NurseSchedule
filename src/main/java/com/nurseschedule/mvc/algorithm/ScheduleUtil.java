package com.nurseschedule.mvc.algorithm;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pl.nurseschedule.mvc.sto.NurseSto;

public class ScheduleUtil {

    protected static int wrongPatterns = 0;

    protected static void createWorkWeekCounters(List<ShiftCounter> dayShiftCounters, List<ShiftCounter> nightShiftCounters) {
        for (int i = 0; i < ScheduleConfig.WORKWEEK_LENGTH; ++i) {
            ShiftCounter dayShift = new ShiftCounter(ScheduleConfig.WEKK_DAYS[i], ScheduleConfig.WORKWEEK_DAY_SHIFT_NURSES_NUMBER);
            ShiftCounter nightShift = new ShiftCounter(ScheduleConfig.WEKK_DAYS[i], ScheduleConfig.NIGHT_SHIFT_NURSES_NUMBER);
            dayShiftCounters.add(dayShift);
            nightShiftCounters.add(nightShift);
        }
    }

    protected static void createWeekendCounters(List<ShiftCounter> dayShiftCounters, List<ShiftCounter> nightShiftCounters) {
        for (int i = 0; i < ScheduleConfig.WEEKEND_LEGTH; ++i) {
            ShiftCounter dayShift = new ShiftCounter(ScheduleConfig.WEKK_DAYS[ScheduleConfig.WORKWEEK_LENGTH + i],
                    ScheduleConfig.WEEKEND_DAY_SHIFT_NURSES_NUMBER);
            ShiftCounter nightShift = new ShiftCounter(ScheduleConfig.WEKK_DAYS[ScheduleConfig.WORKWEEK_LENGTH + i], ScheduleConfig.NIGHT_SHIFT_NURSES_NUMBER);
            dayShiftCounters.add(dayShift);
            nightShiftCounters.add(nightShift);
        }
    }

    protected static void createWeekScheduleNights(List<NursePattern> feasiblePatternsWithNight, List<NurseSto> nurses, int weekIndex,
            List<ShiftCounter> dayShiftCounters, List<ShiftCounter> nightShiftCounters) {
        boolean successNights = false;
        while ( !successNights) {
            setCountersToZero(nightShiftCounters);
            setCountersToZero(dayShiftCounters);
            removeNightsNursesPatterns(nurses, weekIndex);
            for (NurseSto nurse : nurses) {
                if ( nurse.isGeneralNightAvailability() && nurse.isWeekAvailability() ) {
                    List<NursePattern> feasiblePatternsWithNightCopy = new ArrayList<NursePattern>(feasiblePatternsWithNight);
                    createWeekScheduleNightsPart(feasiblePatternsWithNightCopy, nurse, weekIndex, dayShiftCounters, nightShiftCounters);
                }
            }
            successNights = checkNightCounters(nightShiftCounters);
        }
    }

    protected static void createWeekScheduleDays(List<NursePattern> feasiblePatternsWithoutNight, List<NurseSto> nurses, int weekIndex,
            List<ShiftCounter> dayShiftCounters, List<ShiftCounter> nightShiftCounters) {
        List<Integer> daysCounterValuesList = createDaysCounterValuesList(dayShiftCounters);
        while (checkNursesAvailability(nurses)) {
            setDaysCounterToPreviousValues(dayShiftCounters, daysCounterValuesList);
            removeDaysNursesPatterns(nurses, weekIndex);
            for (NurseSto nurse : nurses) {
                if ( nurse.isWeekAvailability() ) {
                    List<NursePattern> feasiblePatternsWithoutNightCopy = new ArrayList<NursePattern>(feasiblePatternsWithoutNight);
                    createWeekScheduleDaysPart(feasiblePatternsWithoutNightCopy, nurse, weekIndex, dayShiftCounters, nightShiftCounters);
                }
            }
            if ( weekIndex == 4 ) {
                break;
            }
        }
    }

    private static void createWeekScheduleNightsPart(List<NursePattern> feasiblePatterns, NurseSto nurse, int weekIndex, List<ShiftCounter> dayShiftCounters,
            List<ShiftCounter> nightShiftCounters) {
        int randomIndex;
        while ( !feasiblePatterns.isEmpty()) {
            randomIndex = getRandomPattern(feasiblePatterns.size());
            String pattern = feasiblePatterns.get(randomIndex).getPattern();
            feasiblePatterns.remove(randomIndex);
            if ( checkPatterMatching(pattern, dayShiftCounters, nightShiftCounters) ) {
                nurse.setWeekAvailability(false);
                nurse.setGeneralNightAvailability(false);
                nurse.setNightAvailabilityInWeeks(weekIndex, false);
                nurse.setWeekPattern(pattern, weekIndex);
                break;
            } else {
                continue;
            }
        }
    }

    private static void createWeekScheduleDaysPart(List<NursePattern> feasiblePatterns, NurseSto nurse, int weekIndex, List<ShiftCounter> dayShiftCounters,
            List<ShiftCounter> nightShiftCounters) {
        int randomIndex;
        while ( !feasiblePatterns.isEmpty()) {
            randomIndex = getRandomPattern(feasiblePatterns.size());
            String pattern = feasiblePatterns.get(randomIndex).getPattern();
            feasiblePatterns.remove(randomIndex);
            if ( checkPatterMatching(pattern, dayShiftCounters, nightShiftCounters) ) {
                if ( weekIndex > 0 && nurse.getWeekPattern(weekIndex - 1).charAt(6) == 'N' ) {
                    if ( isPatternCollideWithPreviousWeekendEnd(pattern) ) {
                        break;
                    }
                }
                nurse.setWeekAvailability(false);
                nurse.setWeekPattern(pattern, weekIndex);
                break;
            } else {
                continue;
            }
        }
    }

    private static boolean isPatternCollideWithPreviousWeekendEnd(String pattern) {
        if ( pattern.charAt(0) == 'R' && pattern.charAt(1) == 'R' ) {
            return false;
        } else {
            return true;
        }
    }

    // try to set proper patterns for part time nurses
    protected static void setProperPatternsForPartTimeWorker(List<NursePattern> feasiblePatterns32hours, List<NursePattern> feasiblePatterns20hours,
            List<NurseSto> hours32Nurses, List<NurseSto> hours20Nurses, int weekIndex, List<ShiftCounter> dayShiftCounters,
            List<ShiftCounter> nightShiftCounters) {
        for (NurseSto nurse : hours32Nurses) {
            if ( nurse.isWeekAvailability() ) {
                List<NursePattern> feasiblePatterns32HoursCopy = new ArrayList<NursePattern>(feasiblePatterns32hours);
                createWeekScheduleDaysPart(feasiblePatterns32HoursCopy, nurse, weekIndex, dayShiftCounters, nightShiftCounters);
            }
        }
        for (NurseSto nurse : hours20Nurses) {
            if ( nurse.isWeekAvailability() ) {
                List<NursePattern> feasiblePatterns20HoursCopy = new ArrayList<NursePattern>(feasiblePatterns20hours);
                createWeekScheduleDaysPart(feasiblePatterns20HoursCopy, nurse, weekIndex, dayShiftCounters, nightShiftCounters);
            }
        }
    }

    // fill wrong patterns to fill schedule
    protected static void fillWeekScheduleUsingWrongPatterns(List<NurseSto> hours32Nurses, List<NurseSto> hours20Nurses, int weekIndex,
            List<ShiftCounter> dayShiftCounters, List<ShiftCounter> nightShiftCounters) {
        if ( finalCheckCountersPropriety(dayShiftCounters, nightShiftCounters) ) {
            return;
        } else {
            for (NurseSto nurse : hours32Nurses) {
                if ( nurse.isWeekAvailability() ) {
                    nurse.setWeekPattern(createWrongPatternToFillSchedule(dayShiftCounters, nightShiftCounters), weekIndex);
                    nurse.setWeekAvailability(false);
                    wrongPatterns++;
                }
                if ( finalCheckCountersPropriety(dayShiftCounters, nightShiftCounters) ) {
                    break;
                }
            }
            for (NurseSto nurse : hours20Nurses) {
                if ( nurse.isWeekAvailability() ) {
                    nurse.setWeekPattern(createWrongPatternToFillSchedule(dayShiftCounters, nightShiftCounters), weekIndex);
                    nurse.setWeekAvailability(false);
                    wrongPatterns++;
                }
                if ( finalCheckCountersPropriety(dayShiftCounters, nightShiftCounters) ) {
                    break;
                }
            }
        }
    }

    protected static void setEmptyPatternForNotNecessaryWorkers(List<NurseSto> allNurses, int weekIndex) {
        for (NurseSto nurse : allNurses) {
            if ( nurse.isWeekAvailability() ) {
                nurse.setWeekPattern("RRRRRRR", weekIndex);
                nurse.setWeekAvailability(false);
            }
        }
    }

    protected static void setWeekAvailabilityForAllNurses(List<NurseSto> allNurses) {
        for (NurseSto nurse : allNurses) {
            nurse.setWeekAvailability(true);
        }
    }

    protected static void setNightAvailabilityForAllNurses(List<NurseSto> allNurses) {
        for (NurseSto nurse : allNurses) {
            nurse.setNightAvailablityForBegin();
            nurse.setGeneralNightAvailability(true);
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

    public static void setCountersToZero(List<ShiftCounter> shiftCounters) {
        for (ShiftCounter counter : shiftCounters) {
            counter.zeroCounter();
        }
    }

    private static boolean checkNightCounters(List<ShiftCounter> nightShiftCounters) {
        for (ShiftCounter counter : nightShiftCounters) {
            if ( counter.getCounter() == 0 ) {
                return false;
            }
        }
        return true;
    }

    private static boolean finalCheckCountersPropriety(List<ShiftCounter> dayShiftCounters, List<ShiftCounter> nightShiftCounters) {
        int i = 0;
        for (; i < ScheduleConfig.WORKWEEK_LENGTH; ++i) {
            if ( dayShiftCounters.get(i).getCounter() != ScheduleConfig.WORKWEEK_DAY_SHIFT_NURSES_NUMBER ) {
                return false;
            }
        }
        for (; i < ScheduleConfig.WEEKEND_LEGTH; ++i) {
            if ( dayShiftCounters.get(i).getCounter() != ScheduleConfig.WEEKEND_DAY_SHIFT_NURSES_NUMBER ) {
                return false;
            }
        }
        for (ShiftCounter nightShift : nightShiftCounters) {
            if ( nightShift.getCounter() != ScheduleConfig.NIGHT_SHIFT_NURSES_NUMBER ) {
                return false;
            }
        }
        return true;
    }

    private static String createWrongPatternToFillSchedule(List<ShiftCounter> dayShiftCounter, List<ShiftCounter> nighShiftCounters) {
        StringBuilder pattern = new StringBuilder("RRRRRRR");
        for (int i = 0; i < ScheduleConfig.WORKWEEK_LENGTH; ++i) {
            if ( dayShiftCounter.get(i).getCounter() < ScheduleConfig.WORKWEEK_DAY_SHIFT_NURSES_NUMBER ) {
                pattern.setCharAt(i, 'd');
            }
        }
        for (int i = 0; i < ScheduleConfig.WEEKEND_LEGTH; ++i) {
            if ( dayShiftCounter.get(i).getCounter() < ScheduleConfig.WEEKEND_DAY_SHIFT_NURSES_NUMBER ) {
                pattern.setCharAt(i, 'd');
            }
        }
        incrementShiftCounters(pattern.toString(), dayShiftCounter, nighShiftCounters);
        return pattern.toString();
    }

    private static void removeNightsNursesPatterns(List<NurseSto> nurses, int weekNumber) {
        for (NurseSto nurse : nurses) {
            if ( !nurse.isWeekAvailability() && !nurse.isGeneralNightAvailability() ) {
                nurse.removeWeekPattern(weekNumber);
                nurse.setWeekAvailability(true);
                nurse.setGeneralNightAvailability(true);
                nurse.setNightAvailabilityInWeeks(weekNumber, true);
            }
        }
    }

    private static void removeDaysNursesPatterns(List<NurseSto> nurses, int weekNumber) {
        for (NurseSto nurse : nurses) {
            if ( !nurse.isWeekAvailability() && nurse.isNightAvailabilityInWeek(weekNumber) ) {
                nurse.removeWeekPattern(weekNumber);
                nurse.setWeekAvailability(true);
            }
        }
    }

    private static boolean checkNursesAvailability(List<NurseSto> nurses) {
        for (NurseSto nurse : nurses) {
            if ( nurse.isWeekAvailability() ) {
                return true;
            }
        }
        return false;
    }

    private static List<Integer> createDaysCounterValuesList(List<ShiftCounter> shiftList) {
        List<Integer> counterValues = new ArrayList<Integer>();
        for (ShiftCounter shift : shiftList) {
            counterValues.add(shift.getCounter());
        }
        return counterValues;
    }

    private static void setDaysCounterToPreviousValues(List<ShiftCounter> list, List<Integer> counterValues) {
        for (int i = 0; i < list.size(); ++i) {
            list.get(i).value = counterValues.get(i);
        }
    }

    protected static void printCounters(List<ShiftCounter> dayShiftCounters, List<ShiftCounter> nightShiftCounters) {
        System.out.println(dayShiftCounters.toString());
        System.out.println(nightShiftCounters.toString());
    }

    protected static void savePatternsToFile(List<NursePattern> patterns, String fileName) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(fileName);
        for (NursePattern pattern : patterns) {
            writer.println(pattern.getPattern());
        }
        writer.close();
    }

    protected static List<NurseSto> changeAllDaysToProperShift(List<NurseSto> allNurses) {
        int dayShiftOccurences = 0;
        for (int i = 0; i < ScheduleConfig.ALL_SCHEDULE_WEEK_NUMBER * (ScheduleConfig.WORKWEEK_LENGTH + ScheduleConfig.WEEKEND_LEGTH) /*chuj wi o co chodzi!?*/; ++i) {
            for (NurseSto nurse : allNurses) {
                String pattern = nurse.getSchedule();
                StringBuilder patternBuilder = new StringBuilder(pattern);
                if ( pattern.charAt(i) == 'd' ) {
                    if ( dayShiftOccurences == 0 ) {
                        patternBuilder.setCharAt(i, 'E');
                    } else if ( dayShiftOccurences == 1 ) {
                        patternBuilder.setCharAt(i, 'D');
                    } else if ( dayShiftOccurences == 2 ) {
                        patternBuilder.setCharAt(i, 'L');
                    }
                    dayShiftOccurences++;
                    if ( dayShiftOccurences == 3 ) {
                        dayShiftOccurences = 0;
                    }
                }
                nurse.setSchedule(patternBuilder.toString());
            }
        }
        return allNurses;
    }

}
