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

    // wszystkie mozliwe permutacje patternow (w oparciu o enum Shift)
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

    public static void savePatternsToFile(List<String> patterns, String fileString) throws FileNotFoundException {
        File file = new File(fileString);
        PrintWriter writer = new PrintWriter(file);
        if ( !patterns.equals("") ) {
            for (String pattern : patterns) {
                writer.println(pattern);
            }
        }
        writer.close();
    }

    public static List<String> findAllPatternsWithNight(List<String> patterns) {
        List<String> feasiblePatternsFirst = firstFiltrationStep(patterns);
        List<String> feasiblePatternsSecond = secondStepWithNight(feasiblePatternsFirst);
        List<String> feasiblePatternsThird = thirdFiltrationStep(feasiblePatternsSecond);
        return feasiblePatternsThird;
    }

    public static List<String> findAllPatternsWithoutNight(List<String> patterns) {
        List<String> feasiblePatternsFirst = firstFiltrationStep(patterns);
        List<String> feasiblePatternsSecond = secondStepWithoutNight(feasiblePatternsFirst);
        List<String> feasiblePatternsThird = thirdFiltrationStep(feasiblePatternsSecond);
        return feasiblePatternsThird;
    }

    // pierwsza filtracja: sum(d)+sum(N)=5, sum(R)=2
    // sum(d)=6, sum(R)=1
    private static List<String> firstFiltrationStep(List<String> patterns) {
        List<String> feasiblePatterns = new ArrayList<String>();
        int dNCounterFirst = 5;
        int dNCounterSecond = 6;
        int RCounter = 2;
        int RCounterSecond = 1;
        for (String pattern : patterns) {
            String stringPattern = pattern.toString();
            if ( countLetterOccurrencesInString(stringPattern, 'd') + countLetterOccurrencesInString(stringPattern, 'N') == dNCounterFirst
                    && countLetterOccurrencesInString(stringPattern, 'R') == RCounter ) {
                feasiblePatterns.add(pattern);
            }
            if ( countLetterOccurrencesInString(stringPattern, 'd') == dNCounterSecond && countLetterOccurrencesInString(stringPattern, 'R') == RCounterSecond ) {
                feasiblePatterns.add(pattern);
            }
        }
        return feasiblePatterns;
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

    // dodatkowe patterny dla pielegniarek pracujacych 32h w tygodniu (czterodniowy cykl pracy)
    public static List<String> partTimeWorkFiltrationFirstStep(List<String> patterns) {
        List<String> feasiblePatterns = new ArrayList<String>();
        int dCounter = 4;
        int Ncounter = 2;
        int NcounterSecond = 3;
        int Rcounter = 3;
        // only days
        for (String pattern : patterns) {
            if ( countLetterOccurrencesInString(pattern, 'd') == dCounter && countLetterOccurrencesInString(pattern, 'R') == Rcounter ) {
                feasiblePatterns.add(pattern);
            }
        }
        // also with nights
        List<String> feasiblePatternsSecond = new ArrayList<String>();
        Pattern nightPatternFirst = Pattern.compile(ScheduleConfig.nightSeries[0]);
        Pattern nightPatternSecond = Pattern.compile(ScheduleConfig.nightSeries[1]);
        Matcher matcherFirst, matcherSecond = null;
        for (String pattern : patterns) {
            if ( (countLetterOccurrencesInString(pattern, 'N') == Ncounter || countLetterOccurrencesInString(pattern, 'N') == NcounterSecond)
                    && countLetterOccurrencesInString(pattern, 'R') == Rcounter ) {
                matcherFirst = nightPatternFirst.matcher(pattern);
                matcherSecond = nightPatternSecond.matcher(pattern);
                if ( matcherFirst.matches() || matcherSecond.matches() ) {
                    feasiblePatternsSecond.add(pattern);
                }
            }
        }
        // only full weekend
        List<String> feasiblePatternsFinal = new ArrayList<String>();
        for (String pattern : feasiblePatterns) {
            if ( isFullWeekend(pattern) ) {
                feasiblePatternsFinal.add(pattern);
            }
        }
        for (String pattern : feasiblePatternsSecond) {
            if ( isFullWeekend(pattern) ) {
                feasiblePatternsFinal.add(pattern);
            }
        }
        return feasiblePatternsFinal;
    }

    // dodatkowe patterny dla pielegniarek pracujacych 20h w tygodniu (czterodniowy cykl pracy)
    public static List<String> partTimeWorkFiltrationSecondStep(List<String> patterns) {
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
