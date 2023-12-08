import enums.ActionLetter;
import model.*;
import util.UniversalArray;
import util.UniversalArrayImpl;

import java.util.Scanner;

public class AppRunner {

    private final UniversalArray<Product> products = new UniversalArrayImpl<>();

    private CardAcceptor cardAcceptor;
    private static CoinAcceptor coinAcceptor;

    private static boolean isExit = false;

    private AppRunner() {
        products.addAll(new Product[]{
                new Water(ActionLetter.B, 20),
                new CocaCola(ActionLetter.C, 50),
                new Soda(ActionLetter.D, 30),
                new Snickers(ActionLetter.E, 80),
                new Mars(ActionLetter.F, 80),
                new Pistachios(ActionLetter.G, 130)
        });
        coinAcceptor = new CoinAcceptor();
    }

    public static void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Выберите способ оплаты: ");
        System.out.println("1. Карта");
        System.out.println("2. Монеты");
        int paymentMethod = scanner.nextInt();

        if (paymentMethod == 1) {
            performCardPayment();
        } else if (paymentMethod == 2) {
            performCoinPayment();
        } else {
            System.out.println("Неверный выбор способа оплаты.");
        }
    }

    public static void main(String[] args) {
        AppRunner app = new AppRunner();
        while (!isExit) {
            app.startSimulation();
        }
    }
    private void showProducts(UniversalArray<Product> products) {
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            System.out.println(product.getActionLetter() + " - " + product.getName() + " (Цена: " + product.getPrice() + ")");
        }
    }

    private void startSimulation() {
        System.out.println("В автомате доступны:");
        showProducts(products);

        System.out.println("Монет на сумму: " + coinAcceptor.getAmount());


        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        allowProducts.addAll(getAllowedProducts().toArray());
        chooseAction(allowProducts);
    }

    private static void performCardPayment() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите номер карты: ");
        String cardNumber = scanner.nextLine();

        System.out.println("Введите одноразовый пароль: ");
        String password = scanner.nextLine();

        // Логика оплаты картой
        // ...
    }

    private static void performCoinPayment() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите сумму в монетах: ");
        int amount = scanner.nextInt();

        coinAcceptor.setAmount(amount);
    }

    private UniversalArray<Product> getAllowedProducts() {
        UniversalArray<Product> allowProducts = new UniversalArrayImpl<>();
        for (int i = 0; i < products.size(); i++) {
            if (coinAcceptor.getAmount() >= products.get(i).getPrice()) {
                allowProducts.add(products.get(i));
            }
        }
        return allowProducts;
    }

    private String fromConsole() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }
    private void chooseAction(UniversalArray<Product> products) {
        System.out.println(" a - Пополнить баланс");

        UniversalArray<Product> allowProducts = getAllowedProducts();

        if (allowProducts.size() == 0) {
            System.out.println("Нет доступных продуктов для покупки.");
            return;
        }
        System.out.println(" h - Выйти");
        String action = fromConsole().substring(0, 1);
        if ("a".equalsIgnoreCase(action)) {
            System.out.println("Введите сумму для пополнения: ");
            int amount = Integer.parseInt(fromConsole());
            coinAcceptor.setAmount(coinAcceptor.getAmount() + amount);
            System.out.println("Вы пополнили баланс на " + amount);
            return;
        }

        try {
            int paymentMethod;
            if (coinAcceptor.getAmount() > 0) {
                System.out.println("Выберите метод оплаты:");
                System.out.println("1. Карта");
                System.out.println("2. Монеты");
                paymentMethod = Integer.parseInt(fromConsole());
            } else {
                paymentMethod = 1; // Если нет средств в монетах, автомат использует оплату картой
            }

            for (int i = 0; i < allowProducts.size(); i++) {
                if (allowProducts.get(i).getActionLetter().equals(ActionLetter.valueOf(action.toUpperCase()))) {
                    int productPrice = allowProducts.get(i).getPrice();
                    if (paymentMethod == 1) {
                        performCardPayment();
                    } else if (paymentMethod == 2) {
                        if (coinAcceptor.getAmount() >= productPrice) {
                            coinAcceptor.setAmount(coinAcceptor.getAmount() - productPrice);
                            System.out.println("Вы купили " + allowProducts.get(i).getName());
                        } else {
                            System.out.println("Недостаточно средств для покупки.");
                        }
                    }
                    break;
                }
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Неверный выбор действия.");
        }
    }
}