package lotto;

import camp.nextstep.edu.missionutils.Console;
import camp.nextstep.edu.missionutils.Randoms;
import java.sql.SQLOutput;
import java.util.*;


public class Application {
    public static void main(String[] args) {
        // TODO: 프로그램 구현

        try {
            int purchase = readPurchase();
            int ticketCount = purchase / 1000;

            System.out.println();
            System.out.println(ticketCount + "개를 구매했습니다.");
            List<Lotto> lottos = buyLottos(ticketCount);
            lottos.forEach(lotto -> System.out.println(lotto.getNumbers()));

            List<Integer> winningNumbers = readWinningNumbers();
            int bonus = readBonusNumber(winningNumbers);

            Map<Rank, Integer> results = checkResults(lottos, winningNumbers, bonus);
            printResults(results);
            printProfit(results, purchase);

        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }

    // 로또 당첨 결과 판별
    private static Map<Rank, Integer> checkResults(List<Lotto> lottos, List<Integer> winningNumbers, int bonus){
        Map<Rank, Integer> resultCount = new EnumMap<>(Rank.class);
        for (Rank rank : Rank.values()){
            resultCount.put(rank, 0);
        }

        for (Lotto lotto : lottos) {
            int matchCount = (int) lotto.getNumbers().stream()
                    .filter(winningNumbers::contains)
                    .count();
            boolean hasBonus = lotto.getNumbers().contains(bonus);
            Rank rank = Rank.valueOf(matchCount, hasBonus);
            if (rank != null) {
                resultCount.put(rank, resultCount.get(rank) + 1);
            }
        }

        return resultCount;
    }

    // 출력
    private static void printResults(Map<Rank, Integer> results) {
        System.out.println();
        System.out.println("당첨 통계");
        System.out.println("---");
        for (Rank rank : Rank.values()) {
            System.out.println(rank.getMessage() + " - " + results.get(rank) + "개");
        }
    }

    private static void printProfit(Map<Rank, Integer> results, int purchase) {
        long totalPrize = results.entrySet().stream()
                .mapToLong(e -> (long) e.getKey().getPrize() * e.getValue())
                .sum();

        double profitRate = ((double) totalPrize / purchase) * 100;
        System.out.printf("총 수익률은 %.1f%%입니다.%n", profitRate);
    }

    // 로또 번호 자동 생성
    private static List<Lotto> buyLottos(int count){
        List<Lotto> lottos = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            List<Integer> numbers = Randoms.pickUniqueNumbersInRange(1, 45, 6);
            Collections.sort(numbers);
            lottos.add(new Lotto(numbers));
        }
        return lottos;
    }

    // 구입금액 입력
    private static int readPurchase(){
        while (true) {
            try {
                System.out.println("구입금액을 입력해 주세요.");
                int amount = Integer.parseInt(Console.readLine());
                verification(amount);
                return amount;
            } catch (NumberFormatException e){
                System.out.println("[ERROR] 숫자만 입력해야 합니다.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // 당첨 번호 확인
    private static List<Integer> readWinningNumbers(){
        while (true) {
            try {
                System.out.println();
                System.out.println("당첨 번호를 입력해 주세요.");
                String input = Console.readLine();
                List<Integer> numbers = Arrays.stream(input.split(","))
                        .map(String::trim)
                        .map(Integer::parseInt)
                        .toList();
                validateWinningNumbers(numbers);
                return numbers;
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] 잘못된 당첨 번호 입력입니다.");
            } catch (IllegalArgumentException e){
                System.out.println(e.getMessage());
            }
        }
    }

    // 보너스 번호 입력
    private static int readBonusNumber(List<Integer> winningNumbers) {
        while (true) {
            try {
                System.out.println();
                System.out.println("보너스 번호를 입력해 주세요.");
                int bonus = Integer.parseInt(Console.readLine());
                validateBonusNumber(winningNumbers, bonus);
                return bonus;
            } catch (NumberFormatException e) {
                System.out.println("[ERROR] 숫자만 입력해야 합니다.");
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // 구입 금액 유효성 검사
    private static void verification(int amount) {
        if (amount < 1000) {
            throw new IllegalArgumentException("[ERROR] 최소 1,000원 이상 입력해야 합니다.");
        }
        if (amount % 1000 != 0){
            throw new IllegalArgumentException("[ERROR] 구입 금액은 1,000원 단위여야 합니다.");
        }
    }

    // 당첨번호 유효성 검사
    private static void validateWinningNumbers(List<Integer> numbers) {
        if (numbers.size() != 6){
            throw new IllegalArgumentException("[ERROR] 당첨 번호는 6개여야 합니다.");
        }
        if (new HashSet<>(numbers).size() != 6) {
            throw new IllegalArgumentException("[ERROR] 중복된 번호가 있습니다.");
        }
        if (numbers.stream().anyMatch(n -> n < 1 || n > 45)) {
            throw new IllegalArgumentException("[ERROR] 번호는 1~45 사이여야 합니다.");
        }
    }

    // 보너스 번호 유효성 검사
    public static void validateBonusNumber(List<Integer> winningNumbers, int bonus) {
        if (winningNumbers.contains(bonus)) {
            throw new IllegalArgumentException("[ERROR] 보너스 번호는 당첨 번호와 중복 될 수 없습니다.");
        }
        if (bonus < 1 || bonus > 45) {
            throw new IllegalArgumentException("[ERROR] 보너스 번호는 1~45 사이여야 합니다.");
        }
    }
}
