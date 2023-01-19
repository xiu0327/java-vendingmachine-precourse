package vendingmachine;

import camp.nextstep.edu.missionutils.Randoms;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

public class VendingMachineTest {

    @Test
    void 상품_입력_실패(){
        //given
        HashMap<String, Item> items = getItemHashMap();
        String value = "소금빵";

        // then
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            items.values().stream().filter(item -> item.getName().equals(value)).findAny()
                    .orElseThrow(() -> {
                        throw new IllegalArgumentException("[ERROR] 입력하신 상품은 자판기에 등록되어 있지 않습니다.");
                    });
        });
    }

    @Test
    void 상품_입력_성공(){
        //given
        HashMap<String, Item> items = getItemHashMap();
        String value = "콜라";

        // then
        assertThat(
                items.values().stream().anyMatch(item -> item.getName().equals(value)))
                .isEqualTo(true);
    }

    @Test
    void 동전_반환(){
        HashMap<Integer, Integer> returnCoins = new HashMap<>();
        List<Integer> coinList = getCoinList();

        int amount = 450;
        HashMap<Integer, Integer> coins = getRandomCoin(amount, coinList);
        int customerMoney = 500;
        int expectValue = amount;

        for (Integer i : coinList) {
            int minCoin = Math.min(availableCoinNumber(i, customerMoney), coins.get(i));
            coins.replace(i, coins.get(i)-minCoin);
            amount -= i * minCoin;
            if(minCoin != 0) {
                customerMoney -= (i*minCoin);
                returnCoins.put(i, minCoin);
            }
        }
        if(customerMoney > 0) System.out.println(customerMoney);
        assertThat(returnCoins.entrySet().stream().mapToInt(i -> i.getValue() * i.getKey()).sum())
                .isEqualTo(expectValue);
    }

    public HashMap<Integer, Integer> getRandomCoin(double amount, List<Integer> coinList){
        HashMap<Integer, Integer> result = initializationCoinMap();
        while(amount > 0){
            int i = Randoms.pickNumberInList(coinList);
            if(i > amount) continue;
            result.replace(i, result.get(i)+1);
            amount -= i;
        }
        return result;
    }

    public HashMap<Integer, Integer> initializationCoinMap(){
        HashMap<Integer, Integer> result = new HashMap<>();
        for (Coin value : Coin.values()) {result.put(value.getAmount(), 0);}
        return result;
    }

    public int availableCoinNumber(int i, int customerMoney){
        return customerMoney / i;
    }

    public static List<Integer> getCoinList(){
        return Arrays.stream(Coin.values())
                .map((item) -> item.getAmount())
                .collect(Collectors.toList());
    }

    private HashMap<String, Item> getItemHashMap() {
        HashMap<String, Item> items = new HashMap<>();
        items.put("콜라", new Item("콜라", 1500, 5));
        items.put("사이다", new Item("사이다", 1800, 10));
        return items;
    }
}
