package vendingmachine;

import static camp.nextstep.edu.missionutils.Console.readLine;

public class Application {

    public static void main(String[] args) {
        // TODO: 프로그램 구현
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.setVendingMachineCoin();
        vendingMachine.purchaseItem();
    }

}
