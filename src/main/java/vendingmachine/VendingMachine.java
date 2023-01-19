package vendingmachine;

import camp.nextstep.edu.missionutils.Randoms;

import java.util.*;
import java.util.stream.Collectors;

public class VendingMachine {

    private int amount;
    private int customerMoney;
    private HashMap<Integer, Integer> coins = new HashMap<>();
    private HashMap<String, Item> items = new HashMap<>();
    private final InputService inputService = new InputService();
    private final List<Integer> coinList = getCoinList();

    public void setVendingMachineCoin() {
        amount = inputService.inputAmount();
        getRandomCoin();
        items = inputService.setItems(items);
        customerMoney = inputService.inputMoney();
    }

    public void initializationCoinMap(){
        for (Coin value : Coin.values()) {coins.put(value.getAmount(), 0);}
    }

    public void getRandomCoin(){
        initializationCoinMap();
        while(amount > 0){
            int i = Randoms.pickNumberInList(coinList);
            if(i > amount) continue;
            coins.replace(i, coins.get(i)+1);
            amount -= i;
        }
        System.out.println("자판기가 보유한 동전");
        printCoinMap(coins);
    }

    public void printCoinMap(HashMap<Integer, Integer> map){
        for (Map.Entry<Integer, Integer> item : map.entrySet()) {
            System.out.println(String.format("%d원 - %d개", item.getKey(), item.getValue()));
        }
    }

    public static List<Integer> getCoinList(){
        return Arrays.stream(Coin.values())
                .map((item) -> item.getAmount())
                .collect(Collectors.toList());
    }

    /* 비즈니스 로직 */

    // 상품 재고 확인
    public boolean checkAllItemQuantity(){
        if (items.values().stream().mapToInt(i -> i.getQuantity()).sum() == 0) return true;
        return false;
    }

    // 최소 가격 찾기
    public int getMinPrice(){
        return items.values().stream().mapToInt(i -> i.getPrice()).min().getAsInt();
    }

    // 상품 구매
    public void purchaseItem(){
        while(customerMoney >= getMinPrice() && !checkAllItemQuantity()){
            System.out.println("투입 금액: " + customerMoney + "원");
            String itemName = inputService.inputItemName(items);
            customerMoney -= items.get(itemName).getPrice();
        }
        returnCoin();
    }

    public int availableCoinNumber(Integer i){
        return customerMoney / i;
    }

    // 동전 반환
    private void returnCoin() {
        HashMap<Integer, Integer> returnCoins = new HashMap<>();
        for (Integer i : coinList) {
            int minCoin = Math.min(availableCoinNumber(i), coins.get(i));
            coins.replace(i, coins.get(i)-minCoin);
            amount -= i * minCoin;
            if(minCoin != 0) {
                customerMoney -= (i*minCoin);
                returnCoins.put(i, minCoin);
            }
        }
        if(customerMoney > 0) amount += customerMoney;
        printCoinMap(returnCoins);
    }
}
