package com.nurseschedule.mvc.algorithm;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        String[] conditions = new String[] {

        "^(.)*(N{2,3}RR)?(.)*$",

        /*"^(.)*(NN(RR)*)?(.)*$",

        "^(.)*[^R]?R{2,7}[^R]?(.)*$",

        "^(.)*(NN|RR|dd){2}$" */};

        List<NursePattern> nursePatterns = new ArrayList<NursePattern>();
        for (String pattern : patterns) {
            boolean patternMatches = true;
            for (String condition : conditions) {
                patternMatches &= pattern.matches(condition);
            }
            if ( patternMatches ) {
                NursePattern nursePattern = new NursePattern(pattern);
                nursePatterns.add(nursePattern);
            }
        }
        return nursePatterns;
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

    // METODY POD SPODEM SA DO USUNIECIA
    public static List<NursePattern> findAllPatternsWithNight(List<String> patterns, int hours) {
        List<String> feasiblePatternsFirst = firstFiltrationStep(patterns, hours);
        List<String> feasiblePatternsSecond = secondStepWithNight(feasiblePatternsFirst);
        List<String> feasiblePatternsThird = thirdFiltrationStep(feasiblePatternsSecond);
        return convertToPatternList(feasiblePatternsThird);
    }

    public static List<NursePattern> findAllPatternsWithoutNight(List<String> patterns, int hours) {
        List<String> feasiblePatternsFirst = firstFiltrationStep(patterns, hours);
        List<String> feasiblePatternsSecond = secondStepWithoutNight(feasiblePatternsFirst);
        List<String> feasiblePatternsThird = thirdFiltrationStep(feasiblePatternsSecond);
        return convertToPatternList(feasiblePatternsThird);
    }

    private static List<NursePattern> convertToPatternList(List<String> stringPatterns) {
        List<NursePattern> patternList = new ArrayList<NursePattern>();
        for (String stringPattern : stringPatterns) {
            NursePattern pattern = new NursePattern(stringPattern);
            patternList.add(pattern);
        }
        return patternList;
    }

    // druga filtracja: same bez nocek
    private static List<String> secondStepWithoutNight(List<String> patterns) {
        List<String> feasiblePatterns = new ArrayList<String>();
        for (String pattern : patterns) {
            if ( countLetterOccurrencesInString(pattern, 'N') == 0 ) {
                feasiblePatterns.add(pattern);
            }
        }
        return feasiblePatterns;
    }

    // druga filtracja: same z nockami
    private static List<String> secondStepWithNight(List<String> patterns) {
        List<String> feasiblePatterns = new ArrayList<String>();
        Matcher nightMatchers[] = new Matcher[ScheduleConfig.fullJobNightRegex.length];
        for (String pattern : patterns) {
            int i = 0;
            for (String regex : ScheduleConfig.fullJobNightRegex) {
                Pattern nightPattern = Pattern.compile(regex);
                nightMatchers[i] = nightPattern.matcher(pattern);
                if ( nightMatchers[i].matches() ) {
                    feasiblePatterns.add(pattern);
                }
                ++i;
            }
        }
        return feasiblePatterns;
    }

    // trzecia filtracja: bierzemy pod uwage tylko "pelne" weekendy - 'dd', 'RR' lub 'NN'
    private static List<String> thirdFiltrationStep(List<String> patterns) {
        List<String> feasiblePatterns = new ArrayList<String>();
        for (String pattern : patterns) {
            if ( isFullWeekend(pattern) ) {
                feasiblePatterns.add(pattern);
            }
        }
        return feasiblePatterns;
    }

    public static List<NursePattern> partTimeWorkFiltration(List<String> patterns, int hours) {
        List<String> feasiblePatterns = firstFiltrationStep(patterns, hours);
        List<String> feasiblePatternsSecondStep = partTimeWorkFiltrationSecondStep(feasiblePatterns);
        List<NursePattern> nursePatterns = new ArrayList<NursePattern>();
        for (String stringPattern : feasiblePatternsSecondStep) {
            NursePattern pattern = new NursePattern(stringPattern);
            nursePatterns.add(pattern);
        }
        return nursePatterns;
    }

    // pierwsza filtracja, wyciagniecie wszystkich patternow zgadzajacych sie z przekazanymi godzinami
    private static List<String> firstFiltrationStep(List<String> patterns, int hours) {
        List<String> feasiblePatterns = new ArrayList<String>();
        int workDays = hours / DAY_HOURS;
        int restDays = WEEK_LENGTH - workDays;
        for (String pattern : patterns) {
            String stringPattern = pattern.toString();
            if ( countLetterOccurrencesInString(stringPattern, 'd') + countLetterOccurrencesInString(stringPattern, 'N') == workDays
                    && countLetterOccurrencesInString(stringPattern, 'R') == restDays ) {
                feasiblePatterns.add(pattern);
            }
        }
        return feasiblePatterns;
    }

    private static List<String> partTimeWorkFiltrationSecondStep(List<String> patterns) {
        List<String> feasiblePatterns = new ArrayList<String>();
        Matcher matchers[] = new Matcher[ScheduleConfig.halfTimeSeries.length];
        for (String pattern : patterns) {
            for (String regex : ScheduleConfig.halfTimeSeries) {
                int i = 0;
                Pattern halfPattern = Pattern.compile(regex);
                matchers[i] = halfPattern.matcher(pattern);
                if ( matchers[i].matches() ) {
                    feasiblePatterns.add(pattern);
                }
                ++i;
            }
        }
        List<String> feasiblePatternsFinal = new ArrayList<String>();
        for (String pattern : feasiblePatterns) {
            if ( isFullWeekend(pattern) ) {
                feasiblePatternsFinal.add(pattern);
            }
        }
        return feasiblePatternsFinal;
    }

    protected static List<NursePattern> getPartTimeWorkerDayPattern(List<NursePattern> patterns) {
        List<NursePattern> dayPatterns = new ArrayList<NursePattern>();
        for (NursePattern pattern : patterns) {
            if ( PatternUtil.countLetterOccurrencesInString(pattern.getPattern(), 'd') == 3 ) {
                dayPatterns.add(pattern);
            }
        }
        return dayPatterns;
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
