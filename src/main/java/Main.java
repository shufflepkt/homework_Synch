import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class Main {
    static final int NUMBER_OF_ROUTES = 1_000;
    public static final Map<Integer, Integer> sizeToFreq = new HashMap<>();

    public static void main(String[] args) throws InterruptedException {
        List<Thread> threads = new ArrayList<>();

        for (int i = 0; i < NUMBER_OF_ROUTES; i++) {
            Thread thread = new Thread(() -> {
                String route = generateRoute("RLRFR", 100);
                Integer numberOfChars = StringUtils.countMatches(route, 'R');

                synchronized (sizeToFreq) {
                    if (sizeToFreq.containsKey(numberOfChars)) {
                        sizeToFreq.replace(numberOfChars, sizeToFreq.get(numberOfChars) + 1);
                    } else {
                        sizeToFreq.put(numberOfChars, 1);
                    }
                }
            });
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }

        Map.Entry<Integer, Integer> maxFreq =
                sizeToFreq.entrySet().stream().max(new Comparator<Map.Entry<Integer, Integer>>() {
                    @Override
                    public int compare(Map.Entry<Integer, Integer> o1, Map.Entry<Integer, Integer> o2) {
                        return o1.getValue().compareTo(o2.getValue());
                    }
                }).get();

        System.out.println("Самое частое количество повторений " + maxFreq.getKey() +
                " (встретилось " + maxFreq.getValue() + " раз)\n" + "Другие размеры:");

        for (Integer key : sizeToFreq.keySet()) {
            if (key != maxFreq.getKey()) {
                System.out.println("- "+ key + " (" + sizeToFreq.get(key) + " раз)");
            }
        }
    }

    public static String generateRoute(String letters, int length) {
        Random random = new Random();
        StringBuilder route = new StringBuilder();
        for (int i = 0; i < length; i++) {
            route.append(letters.charAt(random.nextInt(letters.length())));
        }
        return route.toString();
    }
}
