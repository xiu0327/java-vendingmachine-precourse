package vendingmachine;

import camp.nextstep.edu.missionutils.Randoms;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.stream.Collectors;


public class CoinServiceTest {

    public int getAmount(){
        while(true){
            int amount = Randoms.pickNumberInRange(100, 10000);
            if (amount % 10 == 0) return amount;
        }
    }


    @Test
    void 랜덤_동전_초기화(){
        HashMap<Coin, Integer> coins = new HashMap<>();
        for (Coin value : Coin.values()) {coins.put(value, 0);}
        System.out.println(coins);
    }

    @Test
    void 랜덤_동전_테스트(){
        CoinService coinService = new CoinService(getAmount());
        HashMap<Integer, Integer> randomCoin = coinService.getRandomCoin();
        int total = randomCoin.entrySet().stream().map(
                (item) -> item.getValue() * item.getKey()
        ).collect(Collectors.toList()).stream().mapToInt(Integer::intValue).sum();

        coinService.printRandomCoin();

        Assertions.assertThat(total).isEqualTo(coinService.getAmount());
    }

    @Test
    void 아이템_목록_셋팅(){
        String raw = "[콜라,1500,20];[사이다,1000,10]".replace("[", "").replace("]", "");
        HashMap<String, Item> items = new HashMap<>();
        for (String s : raw.split(";")) {
            String[] split = s.split(",");
            items.put(split[0], new Item(split[0], Integer.parseInt(split[1]), Integer.parseInt(split[2])));
        }
        System.out.println(items);
    }

    @Test
    void 아이템_입력_확인(){
        String[] split = "[콜라,1500,20];[사이다,1000,10]".split(";");
        String re = "\\[[가-힣]+,[0-9]+,[0-9]+\\]";
        for (String s : split) {
            if(s.matches(re) == false) throw new IllegalArgumentException("[ERROR] 상품 입력이 잘못되었습니다.");
        }
    }

    @Test
    void 상품_구매(){
        CoinService coinService = new CoinService(getAmount());

        System.out.println("자판기에 있는 돈 : " + coinService.getAmount());

        coinService.settingItems(coinService.checkRawItems("[콜라,1500,20];[사이다,1000,10]"));
        coinService.setMoney(3000);

        String[] inputs = "콜라 사이다".split(" ");

        while(coinService.getMoney() > 10){
        for (String input : inputs) {
                System.out.println("구매할 상품명을 입력해 주세요.\n" + input);
                coinService.purchaseItem(input);
            }
        }
    }

    @Test
    void 통합_테스트(){

    }

}
