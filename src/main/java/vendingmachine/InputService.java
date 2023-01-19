package vendingmachine;

import java.util.HashMap;

import static camp.nextstep.edu.missionutils.Console.readLine;

public class InputService {

    private final String re = "\\[[가-힣]+,[0-9]+,[0-9]+\\]";

    /* 자판기에 들어있는 금액 입력 */
    public int inputAmount() {
        System.out.println("자판기가 보유하고 있는 금액을 입력해 주세요.");
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

    /* 투입 금액 입력 */
    public int inputMoney(){
        System.out.println("투입 금액을 입력해 주세요.");
        try{
            return Integer.parseInt(readLine());
        }catch (NumberFormatException e){
            System.out.println("[ERROR] 금액은 숫자여야 합니다.");
            return inputMoney();
        }
    }

    /* 상품 목록 입력 */
    public String[] inputItems(){
        System.out.println("상품명과 가격, 수량을 입력해 주세요.");
        try{
            String raw = readLine();
            for (String s : raw.split(";")) {
                if(s.matches(re) == false) throw new IllegalArgumentException("[ERROR] 상품 입력이 잘못되었습니다.");
            }
            return raw.replace("[","").replace("]","").split(";");
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            return inputItems();
        }
    }

    /* 구매할 상품 입력 */
    public String inputItemName(HashMap<String, Item> items){
        System.out.println("구매할 상품명을 입력해 주세요.");
        try{
            String value = readLine();
            items.values().stream().filter(item -> item.getName().equals(value)).findAny()
                    .orElseThrow(() -> {throw new IllegalArgumentException("[ERROR] 입력하신 상품은 자판기에 등록되어 있지 않습니다.");});
            return value;
        }catch (IllegalArgumentException e){
            System.out.println(e.getMessage());
            return inputItemName(items);
        }
    }

    /* 상품 목록 입력 문장을 파싱하여 items 셋팅 */
    public HashMap<String, Item> setItems(HashMap<String, Item> items){
        for (String s : inputItems()) {
            String[] split = s.split(",");
            items.put(split[0], new Item(split[0], Integer.parseInt(split[1]), Integer.parseInt(split[2])));
        }
        return items;
    }
}
