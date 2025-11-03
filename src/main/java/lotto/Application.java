package lotto;

import camp.nextstep.edu.missionutils.Console;
import camp.nextstep.edu.missionutils.Randoms;
import java.awt.SystemColor;
import java.util.*;


public class Application {
    private static final Map<Integer, Integer> PRIZE_MAP = Map.of(
            6, 2_000_000_000,
            5, 1_500_000,
            4, 50_000,
            3, 5_000
    );

    public static void main(String[] args) {
        // TODO: 프로그램 구현

        try {
            System.out.println("구입금액을 입력해 주세요.");
            int purchase = readPurchase();
            int ticketCount = purchase / 1000;

            System.out.println(ticketCount + "개를 구매했습니다.");
            List<Lotto> lottos = buyLottos(ticketCount);
            lottos.forEach(lotto -> System.out.println(lotto.getNumbers()));

            System.out.println("당첨 번호를 입력해 주세요.");
            List<Integer> winningNumbers = readWinningNumbers();
            System.out.println("보너스 번호를 입력해 주세요.");
            int bonus = readBonusNumber(winningNumbers);

            Map<String, Integer> results = checkResults(lottos, winningNumbers, bonus);
            printResults(results);
            printProfit(results, purchase);
        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
        }
    }

    // 로또 당첨 결과 판별
    private static Map<String, Integer> checkResults(List<Lotto> lottos, List<Integer> winningNumbers, int bonus){
        Map<String, Integer> resultCount = new LinkedHashMap<>();
        resultCount.put("3", 0);
        resultCount.put("4", 0);
        resultCount.put("5", 0);
        resultCount.put("5_bonus", 0);
        resultCount.put("6", 0);

        for (Lotto lotto : lottos) {
            int matchCount = (int) lotto.getNumbers().stream()
                    .filter(winningNumbers::contains)
                    .count();
            if (matchCount == 6) resultCount.put("6", resultCount.get("6") + 1);
            if (matchCount == 5 && lotto.getNumbers().contains(bonus)) {
                resultCount.put("5_bonus", resultCount.get("5_bonus") + 1);
            }
            if (matchCount == 5 && !lotto.getNumbers().contains(bonus)) {
                resultCount.put("5", resultCount.get("5") + 1);
            }
            if (matchCount == 4) resultCount.put("4", resultCount.get("4") + 1);
            if (matchCount == 3) resultCount.put("3", resultCount.get("3") + 1);
        }

        return resultCount;
    }

    // 출력
    private static void printResults(Map<String, Integer> results) {
        System.out.println("당첨 통계");
        System.out.println("---");
        System.out.println("3개 일치 (5,000)원 - " + results.get("3") + "개");
        System.out.println("4개 일치 (50,000)원 -" + results.get("4") + "개");
        System.out.println("5개 일치 (1,500,000)원 -" + results.get("5") + "개");
        System.out.println("5개 일치, 보너스 볼 일치 (30,000,000)원 -" + results.get("5_bonus") + "개");
        System.out.println("6개 일치 (2,000,000,000)원 -" + results.get("6") + "개");
    }

    private static void printProfit(Map<String, Integer> results, int purchase) {
        long totalPrize = 0;
        totalPrize += (long) results.get("3") * 5_000;
        totalPrize += (long) results.get("4") * 50_000;
        totalPrize += (long) results.get("5") * 1_500_000;
        totalPrize += (long) results.get("5_bonus") * 30_000_000;
        totalPrize += (long) results.get("6") * 2_000_000_000;

        double profitRate = ((double) totalPrize / purchase) * 100;
        System.out.printf("총 수익률은 %.1f%%입니다.%n", profitRate);
    }

    // 구입금액 입력
    private static int readPurchase(){
        try {
            int amount = Integer.parseInt(Console.readLine());
            verification(amount);
            return amount;
        } catch (NumberFormatException e){
            throw new IllegalArgumentException("[ERROR] 숫자만 입력해야 합니다.");
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

    // 보너스 번호 확인
    private static List<Integer> readWinningNumbers(){
        String input = Console.readLine();
        try {
            List<Integer> numbers = Arrays.stream(input.split(","))
                    .map(String::trim)
                    .map(Integer::parseInt)
                    .toList();
            validateWinningNumbers(numbers);
            return numbers;
        } catch (Exception e){
            throw new IllegalArgumentException("[ERROR] 잘못된 당첨 번호 입력입니다.");
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

    private static int readBonusNumber(List<Integer> winnngNumbers) {
        try {
            int bonus = Integer.parseInt(Console.readLine());
            if (winnngNumbers.contains(bonus)) {
                throw new IllegalArgumentException("[ERROR] 보너스 번호는 당첨 번호와 중복 될수 없습니다.");
            }
            if (bonus < 1 || bonus > 45) {
                throw new IllegalArgumentException("[ERROR] 보너스 번호는 1~45 사이여야 합니다.");
            }
            return bonus;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("[ERROR] 숫자만 입력해야 합니다.");
        }
    }
}
