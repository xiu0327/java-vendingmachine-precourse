package vendingmachine;

import static camp.nextstep.edu.missionutils.Console.readLine;

public class Application {
    public static void main(String[] args) {
        // TODO: 프로그램 구현

        // 1. 자판기 금액 입력 및 동전 개수 랜덤 설정
        System.out.println("자판기가 보유하고 있는 금액을 입력해 주세요.");
        CoinService coinService = new CoinService(inputAmount());

        // 2. 동전 개수 출력
        System.out.println("자판기가 보유한 동전");
        coinService.printRandomCoin();

        // 3. 상품 입력
        System.out.println("상품명과 가격, 수량을 입력해 주세요.");
        coinService.settingItems(coinService.checkRawItems(readLine()));

        // 4. 투입 금액 입력
        System.out.println("투입 금액을 입력해 주세요.");
        coinService.setMoney(inputMoney());

        // 4. 상품 구매
        while(coinService.getMoney() >= 10){
            System.out.println("구매할 상품명을 입력해 주세요.");
            coinService.purchaseItem(readLine());
        }


    }

    public static int inputAmount() {
        try{
            int result = Integer.parseInt(readLine());
            if(result % 10 != 0) throw new IllegalArgumentException("[ERROR] 10의 배수 자리 금액을 입력해주세요.");
            if(result < 100) throw new IllegalArgumentException("[ERROR] 자판기의 금액은 100원 이상만 가능합니다.");
            return result;
        }catch (NumberFormatException e){
            System.out.println("[ERROR] 금액은 숫자여야 합니다.");
            return inputAmount();
        } catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            return inputAmount();
        }
    }
    
    public static int inputMoney(){
        try{
            return Integer.parseInt(readLine());
        }catch (NumberFormatException e){
            System.out.println("[ERROR] 금액은 숫자여야 합니다.");
            return inputMoney();
        }
    }
}
