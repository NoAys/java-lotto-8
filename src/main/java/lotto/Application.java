package lotto;

import camp.nextstep.edu.missionutils.Console;

public class Application {
    public static void main(String[] args) {
        // TODO: 프로그램 구현

        System.out.println("구입금액을 입력해 주세요.");
        int purchase = readPurchase();
        int ticketCount = purchase / 1000;
        System.out.println(ticketCount + "개를 구매했습니다.");
    }

    private static int readPurchase(){
        try {
            int amount = Integer.parseInt(Console.readLine());
            verification(amount);
            return amount;
        } catch (NumberFormatException e){
            throw new IllegalArgumentException("[ERROR] 숫자만 입력해야 합니다.");
        }
    }

    private static void verification(int amount) {
        if (amount < 1000) {
            throw new IllegalArgumentException("[ERROR] 최소 1,000원 이상 입력해야 합니다.");
        }
        if (amount % 1000 != 0){
            throw new IllegalArgumentException("[ERROR] 구입 금액은 1,000원 단위여야 합니다.");
        }
    }
}
