package vendingmachine;

import camp.nextstep.edu.missionutils.Randoms;

import java.util.*;
import java.util.stream.Collectors;

import static camp.nextstep.edu.missionutils.Console.readLine;

public class CoinService {

    private final Coin[] coinValues = Coin.values();

    private final List<Integer> coinList = Arrays.stream(coinValues).sorted()
            .map((item) -> item.getAmount())
            .collect(Collectors.toList());

    private HashMap<Integer, Integer> randomCoin;
    private HashMap<String, Item> items;
    private int money;
    private int amount;

    public CoinService(int amount) {
        this.amount = amount;
        this.randomCoin = getRandomCoin();
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getMoney() {
        if(money != 0) System.out.println("투입 금액: " + money + "원");
        return money;
    }

    public int getAmount() {
        return amount;
    }

    public HashMap<Integer, Integer> initializationCoinMap(){
        HashMap<Integer, Integer> coins = new HashMap<>();
        for (Coin value : coinValues) {coins.put(value.getAmount(), 0);}
        return coins;
    }

    /**
     * 랜덤으로 동전 생성
     * @return
     */
    public HashMap<Integer, Integer> getRandomCoin(){
        HashMap<Integer, Integer> coins = initializationCoinMap();
        while(amount > 0){
            int i = Randoms.pickNumberInList(coinList);
            if(i > amount) continue;
            coins.replace(i, coins.get(i)+1);
            amount = amount - i;
        }
        return coins;
    }

    /**
     * 동전 개수 출력
     */
    public void printRandomCoin(){
        for (Map.Entry<Integer, Integer> item : this.randomCoin.entrySet()) {
            System.out.println(String.format("%d원 - %d개", item.getKey(), item.getValue()));
        }
    }

    /**
     * 잔여 동전 개수 출력
     * @param coins 잔여 동전 개수
     */
    public void printReturnCoin(HashMap<Integer, Integer> coins){
        System.out.println("잔돈");
        for (Map.Entry<Integer, Integer> item : coins.entrySet()) {
            if(item.getValue() != 0) System.out.println(String.format("%d원 - %d개", item.getKey(), item.getValue()));
        }
    }

    /**
     * 사용자 입력 확인 -> 입력 양식이 틀리면 ERROR 발생
     * @param raw 사용자 입력
     */
    public String checkRawItems(String raw){
        try{
            String[] split = raw.split(";");
            String re = "\\[[가-힣]+,[0-9]+,[0-9]+\\]";
            for (String s : split) {
                if(s.matches(re) == false) throw new IllegalArgumentException("[ERROR] 상품 입력이 잘못되었습니다.");
            }
            return raw.replace("[","").replace("]","");
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            return checkRawItems(readLine());
        }
    }

    /**
     * 사용자 입력 아이템 정제화
     * @param raw 사용자가 입력한 아이템 목록 원본
     * @return HashMap 으로 치환한 아이템 목록
     */
    public HashMap<String, Item> settingItems(String raw){
        HashMap<String, Item> items = new HashMap<>();
        for (String s : raw.split(";")) {
            String[] split = s.split(",");
            items.put(split[0], new Item(split[0], Integer.parseInt(split[1]), Integer.parseInt(split[2])));
        }
        this.items = items;
        return items;
    }

    /**
     * 모든 상품이 소진되었는지 아닌지 확인
     * @param items 상품 목록
     * @return 소진 O = true / 소진 X = false
     */
    public boolean checkAllItemCount(HashMap<String, Item> items){
        if (items.values().stream().mapToInt(i -> i.getCount()).sum() == 0) return true;
        return false;
    }

    public int getMinPrice(HashMap<String, Item> items){
        return items.values().stream().mapToInt(i -> i.getPrice()).min().getAsInt();
    }

    /**
     * 상품 구매
     * @param itemName
     */
    public void purchaseItem(String itemName){
        setMoney(money - items.get(itemName).getPrice());
        if(money < getMinPrice(items) || checkAllItemCount(items)) getReturnCoin();

    }

    private void getReturnCoin() {
        int i = 0;
        HashMap<Integer, Integer> returnCoins = initializationCoinMap();
        getMoney();
        while(true){
            if(this.money < 10 || i >= coinList.size()) {
                break;
            }
            if(this.money < coinList.get(i) || randomCoin.get(coinList.get(i)) == 0) {
                i++;
                continue;
            }
            setMoney(this.money - coinList.get(i));
            randomCoin.replace(coinList.get(i), randomCoin.get(coinList.get(i))-1);
            returnCoins.replace(coinList.get(i), randomCoin.get(coinList.get(i))+1);
        }
        setMoney(0);
        printReturnCoin(returnCoins);
    }
}
