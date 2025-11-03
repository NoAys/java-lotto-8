package lotto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Lotto {
    private final List<Integer> numbers;

    public Lotto(List<Integer> numbers) {
        validate(numbers);
        this.numbers = numbers;
    }

    private void validate(List<Integer> numbers) {
        if (numbers.size() != 6) {
            throw new IllegalArgumentException("[ERROR] 로또 번호는 6개여야 합니다.");
        }
        if (new HashSet<>(numbers).size() != 6){
            throw new IllegalArgumentException("[ERROR] 로또 번호에 중복된 숫자가 있습니다.");
        }
        if (numbers.stream().anyMatch(n -> n < 1 || n > 45)) {
            throw new IllegalArgumentException("[ERROR] 로또 번호는 1에서 45사이의 숫자여야 합니다.");
        }
    }

    // TODO: 추가 기능 구현
    // 당첨 번호와 일치하는 개수 계산
    public int countMatching(List<Integer> winningNumbers){
        return (int) numbers.stream()
                .filter(winningNumbers::contains)
                .count();
    }

    // 보너스 번호 포함 여부 확인
    public boolean containBonus(int bonusNumber){
        return numbers.contains(bonusNumber);
    }

    // 로또 번호 반환
    public List<Integer> getNumbers() {
        return new ArrayList<>(numbers);
    }

    @Override
    public String toString() {
        return numbers.toString();
    }
}
