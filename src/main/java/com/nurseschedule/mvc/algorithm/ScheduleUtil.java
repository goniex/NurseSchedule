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

    protected static void createWeekSchedule(List<String> feasiblePatternsWithNight, List<String> feasiblePatternsWithoutNight, List<NurseSto> nurses,
            int weekIndex, List<ShiftCounter> dayShiftCounters, List<ShiftCounter> nightShiftCounters) {
        boolean successNights = false;
        while ( !successNights) {
            setCountersToZero(nightShiftCounters);
            setCountersToZero(dayShiftCounters);
            removeNightsNursesPatterns(nurses, weekIndex);
            for (NurseSto nurse : nurses) {
                if ( nurse.isNightAvailability() && nurse.isWeekAvailability() ) {
                    List<String> feasiblePatternsWithNightCopy = new ArrayList<String>(feasiblePatternsWithNight);
                    createWeekScheduleNightsPart(feasiblePatternsWithNightCopy, nurse, weekIndex, dayShiftCounters, nightShiftCounters);
                }
            }
            successNights = checkNightCounters(nightShiftCounters);
        }

        List<Integer> daysCounterValuesList = createDaysCounterValuesList(dayShiftCounters);
        while (checkNursesAvailability(nurses)) {
            setDaysCounterToPreviousValues(dayShiftCounters, daysCounterValuesList);
            removeDaysNursesPatterns(nurses, weekIndex);
            for (NurseSto nurse : nurses) {
                if ( nurse.isWeekAvailability() ) {
                    List<String> feasiblePatternsWithoutNightCopy = new ArrayList<String>(feasiblePatternsWithoutNight);
                    createWeekScheduleDaysPart(feasiblePatternsWithoutNightCopy, nurse, weekIndex, dayShiftCounters, nightShiftCounters);
                }
            }
        }
    }

    private static void createWeekScheduleNightsPart(List<String> feasiblePatterns, NurseSto nurse, int weekIndex, List<ShiftCounter> dayShiftCounters,
            List<ShiftCounter> nightShiftCounters) {
        int randomIndex;
        while ( !feasiblePatterns.isEmpty()) {
            randomIndex = getRandomPattern(feasiblePatterns.size());
            String pattern = feasiblePatterns.get(randomIndex);
            feasiblePatterns.remove(randomIndex);
            if ( checkPatterMatching(pattern, dayShiftCounters, nightShiftCounters) ) {
                nurse.setWeekAvailability(false);
                nurse.setNightAvailability(false);
                nurse.setWeekPattern(pattern, weekIndex);
                break;
            } else {
                continue;
            }
        }
    }

    private static void createWeekScheduleDaysPart(List<String> feasiblePatterns, NurseSto nurse, int weekIndex, List<ShiftCounter> dayShiftCounters,
            List<ShiftCounter> nightShiftCounters) {
        int randomIndex;
        while ( !feasiblePatterns.isEmpty()) {
            randomIndex = getRandomPattern(feasiblePatterns.size());
            String pattern = feasiblePatterns.get(randomIndex);
            feasiblePatterns.remove(randomIndex);
            if ( checkPatterMatching(pattern, dayShiftCounters, nightShiftCounters) ) {
                nurse.setWeekAvailability(false);
                nurse.setWeekPattern(pattern, weekIndex);
                break;
            } else {
                continue;
            }
        }
    }

    // try to set proper patterns for part time nurses
    protected static void setProperPatternsForTimeWorker(List<String> feasiblePatterns32hours, List<String> feasiblePatterns20hours,
            List<NurseSto> hours32Nurses, List<NurseSto> hours20Nurses, int weekIndex, List<ShiftCounter> dayShiftCounters,
            List<ShiftCounter> nightShiftCounters) {
        for (NurseSto nurse : hours32Nurses) {
            if ( nurse.isWeekAvailability() ) {
                createWeekScheduleDaysPart(feasiblePatterns32hours, nurse, weekIndex, dayShiftCounters, nightShiftCounters);
            }
        }
        for (NurseSto nurse : hours20Nurses) {
            if ( nurse.isWeekAvailability() ) {
                createWeekScheduleDaysPart(feasiblePatterns20hours, nurse, weekIndex, dayShiftCounters, nightShiftCounters);
            }
        }
    }

    // fill wrong patterns to fill schedule (ADD WRONG PATTERNS NUMBER INCREMENTATION)
    protected static void fillWeekScheduleUsingWrongPatterns(List<String> feasiblePatterns32hours, List<String> feasiblePatterns20hours,
            List<NurseSto> hours32Nurses, List<NurseSto> hours20Nurses, int weekIndex, List<ShiftCounter> dayShiftCounters,
            List<ShiftCounter> nightShiftCounters) {

        if ( finalCheckCountersPropriety(dayShiftCounters, nightShiftCounters) ) {
            return;
        } else {
            // set wrong patterns from all patterns to fill week schedule and increment number of wrong patterns until schedule will not be filled properly
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

    private static void setCountersToZero(List<ShiftCounter> shiftCounters) {
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
            if ( !nurse.isWeekAvailability() && !nurse.isNightAvailability() ) {
                nurse.removeWeekPattern(weekNumber);
                nurse.setWeekAvailability(true);
                nurse.setNightAvailability(true);
            }
        }
    }

    private static void removeDaysNursesPatterns(List<NurseSto> nurses, int weekNumber) {
        for (NurseSto nurse : nurses) {
            if ( !nurse.isWeekAvailability() && nurse.isNightAvailability() ) {
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

    protected static void savePatternsToFile(List<String> patterns, String fileName) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(fileName);
        for (String pattern : patterns) {
            writer.println(pattern);
        }
        writer.close();
    }

}
