package edn.stratodonut.drivebywire.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class DicewareGenerator {
    private static final List<String> words;

    static {
        InputStream fileStream = DicewareGenerator.class.getClassLoader().getResourceAsStream("diceware_eff.txt");
        if (fileStream == null) throw new NullPointerException("Diceware word list can not be opened!");
        BufferedReader reader = new BufferedReader(new InputStreamReader(fileStream));
        words = reader.lines().toList();
    }

    public static String generate(int wordCount) {
        return ThreadLocalRandom.current()
                .ints(wordCount, 0, words.size())
                .mapToObj(words::get)
            .collect(Collectors.joining("-"));
    }
}
