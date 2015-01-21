package com.nurseschedule.mvc.algorithm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Caro
 * @date 5 gru 2014
 */
public class PatternUtil {

    private static final int WEEK_LENGTH = 7;

    private static final int DAY_HOURS = 8;

    public static void getPermutations(List<String> patterns, List<Shift> list, int depth, String val) {
        if ( depth <= 0 ) {
            return;
        }
        if ( depth == 1 ) {
            for (Shift shift : list) {
                patterns.add(shift.toString() + val);
            }
        } else {
            for (Shift shift : list) {
                getPermutations(patterns, list, depth - 1, shift.toString() + val);
            }
        }
    }

    public static List<NursePattern> filtrationBasedOnConditions(List<String> patterns) {
        /** max six workdays (one dayoff) */
        final String WORKDAYS = "^(.)*R+(.)*$";
        /** max two or three nights */
        final String NIGHTS = "^[^N]*(N{2,3})?[^N]*$";
        /** complete weekends */
        final String WEEKENDS = "^(.)*(NN|RR|dd)$";

        String[] conditions = new String[] { WORKDAYS, NIGHTS, WEEKENDS };
        List<NursePattern> nursePatterns = new ArrayList<NursePattern>();
        for (String pattern : patterns) {
            boolean lineMatches = true;
            for (String condition : conditions) {
                lineMatches &= pattern.matches(condition);
            }
            if ( pattern.matches("^[^N]+$") ) {
                lineMatches &= true;
            } else {
                pattern = pattern + "RR";
                for (int i = pattern.length() - 3; i >= 0; i--) {
                    if ( (pattern.charAt(i) == 'N') ) {
                        if ( (pattern.charAt(i + 1) == 'R') && (pattern.charAt(i + 2) == 'R') ) {
                            lineMatches &= true;
                        } else {
                            lineMatches &= false;
                        }
                        break;
                    }
                }
                pattern = pattern.substring(0, 7);
            }
            if ( lineMatches ) {
                NursePattern nursePattern = new NursePattern(pattern);
                nursePatterns.add(nursePattern);
            }
        }
        return nursePatterns;
    }

    public static List<NursePattern> getPatternFor20hWorkers(List<NursePattern> consistentPatterns, int hours) {
        List<NursePattern> patterns = new ArrayList<NursePattern>();
        int workDays = hours / DAY_HOURS;
        for (NursePattern nursePattern : consistentPatterns) {
            String pattern = nursePattern.getPattern();
            if ( pattern.matches("^([^Nd])*[dN]{2,3}([^Nd])*$") ) {
                if ( (countLetterOccurrencesInString(pattern, 'd') + countLetterOccurrencesInString(pattern, 'N')) == workDays ) {
                    patterns.add(new NursePattern(pattern));
                }
            }
        }
        System.out.println();
        return patterns;
    }

    public static List<NursePattern> getPatternBasedOnHours(List<NursePattern> patterns, int hours, boolean isNight) {
        List<NursePattern> feasiblePatterns = new ArrayList<NursePattern>();
        int workDays = hours / DAY_HOURS;
        int restDays = WEEK_LENGTH - workDays;
        for (NursePattern pattern : patterns) {
            String stringPattern = pattern.getPattern();
            if ( isNight ) {
                if ( countLetterOccurrencesInString(stringPattern, 'N') > 0
                        && countLetterOccurrencesInString(stringPattern, 'N') + countLetterOccurrencesInString(stringPattern, 'd') == workDays
                        && countLetterOccurrencesInString(stringPattern, 'R') == restDays ) {
                    feasiblePatterns.add(pattern);
                }
            } else {
                if ( countLetterOccurrencesInString(stringPattern, 'd') == workDays && countLetterOccurrencesInString(stringPattern, 'R') == restDays ) {
                    feasiblePatterns.add(pattern);
                }
            }
        }
        return feasiblePatterns;
    }

    public static void savePatternsToFile(List<NursePattern> patterns, String fileString) throws FileNotFoundException {
        File file = new File(fileString);
        PrintWriter writer = new PrintWriter(file);
        if ( !patterns.equals("") ) {
            for (NursePattern pattern : patterns) {
                writer.println(pattern.getPattern());
            }
        }
        writer.close();
    }

    protected static List<NursePattern> getPartTimeWorkerNightPattern(List<NursePattern> patterns) {
        List<NursePattern> nightPatterns = new ArrayList<NursePattern>();
        for (NursePattern pattern : patterns) {
            if ( PatternUtil.countLetterOccurrencesInString(pattern.getPattern(), 'd') != 3 ) {
                nightPatterns.add(pattern);
            }
        }
        return nightPatterns;
    }

    private static int countLetterOccurrencesInString(String pattern, char letter) {
        int occurrences = 0;
        for (int i = 0; i < pattern.length(); ++i) {
            if ( pattern.charAt(i) == letter ) {
                occurrences++;
            }
        }
        return occurrences;
    }

    private static boolean isFullWeekend(String pattern) {
        int size = pattern.length();
        char first = pattern.charAt(size - 2);
        char second = pattern.charAt(size - 1);
        if ( first != second ) {
            return (first == 'R' || second == 'R') ? false : true;
        }
        return true;
    }

}
