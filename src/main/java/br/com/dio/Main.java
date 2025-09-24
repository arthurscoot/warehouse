package br.com.dio;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static List<BasicBasket> stock = new ArrayList<>();
    private static BigDecimal money = BigDecimal.ZERO;

    public static void main(String[] args) {
        System.out.println("Bem-vindo ao sistema de armazém!");

        while (true) {
            System.out.println("\nSelecione a opção desejada:");
            System.out.println("1 - Verificar estoque de cestas básicas");
            System.out.println("2 - Verificar caixa");
            System.out.println("3 - Receber cestas");
            System.out.println("4 - Vender cestas");
            System.out.println("5 - Remover itens vencidos");
            System.out.println("6 - Sair");

            int option = getIntInput();

            switch (option) {
                case 1 -> checkStock();
                case 2 -> checkMoney();
                case 3 -> receiveItems();
                case 4 -> sellItems();
                case 5 -> removeItemsOutOfDate();
                case 6 -> {
                    System.out.println("Saindo do sistema...");
                    System.exit(0);
                }
                default -> System.out.println("Opção inválida! Tente novamente.");
            }
        }
    }

    private static void sellItems() {
        if (stock.isEmpty()) {
            System.out.println("Estoque vazio! Não há cestas para vender.");
            return;
        }

        System.out.println("Quantas cestas serão vendidas?");
        int amount = getIntInput();

        if (amount <= 0) {
            System.out.println("Quantidade inválida!");
            return;
        }

        if (amount > stock.size()) {
            System.out.println("Não há cestas suficientes em estoque.");
            return;
        }

        stock.sort(Comparator.comparing(BasicBasket::price));
        List<BasicBasket> toSell = new ArrayList<>(stock.subList(0, amount));
        BigDecimal value = toSell.stream()
                                 .map(BasicBasket::price)
                                 .reduce(BigDecimal.ZERO, BigDecimal::add);
        money = money.add(value);
        stock.removeAll(toSell);

        System.out.printf("Venda realizada! Valor total: R$ %s\n", value);
    }

    private static void checkStock() {
        int amount = stock.size();
        long outOfDate = stock.stream()
                              .filter(b -> b.validate().isBefore(LocalDate.now()))
                              .count();
        System.out.printf("Existem %d cestas em estoque, das quais %d estão vencidas.\n", amount, outOfDate);
    }

    private static void checkMoney() {
        System.out.printf("O caixa no momento é de R$ %s\n", money);
    }

    private static void removeItemsOutOfDate() {
        List<BasicBasket> expired = stock.stream()
                                         .filter(b -> b.validate().isBefore(LocalDate.now()))
                                         .toList();

        if (expired.isEmpty()) {
            System.out.println("Nenhuma cesta vencida para remover.");
            return;
        }

        BigDecimal loss = expired.stream()
                                 .map(BasicBasket::price)
                                 .reduce(BigDecimal.ZERO, BigDecimal::add);

        stock = stock.stream()
                     .filter(b -> !b.validate().isBefore(LocalDate.now()))
                     .collect(Collectors.toList());

        System.out.printf("Foram descartadas %d cestas vencidas. Prejuízo: R$ %s\n", expired.size(), loss);
    }

    private static void receiveItems() {
        System.out.println("Informe o valor da entrega:");
        BigDecimal price = getBigDecimalInput();
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            System.out.println("Valor inválido!");
            return;
        }

        System.out.println("Informe a quantidade de cestas da entrega:");
        int amount = getIntInput();
        if (amount <= 0) {
            System.out.println("Quantidade inválida!");
            return;
        }

        System.out.println("Informe a data de vencimento (dd/MM/yyyy):");
        LocalDate validateDate = getDateInput();
        if (validateDate.isBefore(LocalDate.now())) {
            System.out.println("Data de validade inválida! Não pode ser no passado.");
            return;
        }

        var box = new Box(amount, validateDate, price);
        BigDecimal unitPrice = box.price()
                                  .divide(new BigDecimal(box.amount()), RoundingMode.CEILING);
        BigDecimal finalPrice = unitPrice.add(unitPrice.multiply(new BigDecimal("0.20")));

        List<BasicBasket> baskets = Stream.generate(() -> new BasicBasket(box.validate(), finalPrice))
                                          .limit(box.amount())
                                          .toList();

        stock.addAll(baskets);
        System.out.printf("Foram adicionadas %d cestas ao estoque.\n", baskets.size());
    }

    // Métodos auxiliares
    private static int getIntInput() {
        while (!scanner.hasNextInt()) {
            System.out.println("Entrada inválida. Digite um número inteiro:");
            scanner.next();
        }
        int value = scanner.nextInt();
        scanner.nextLine(); // limpar buffer
        return value;
    }

    private static BigDecimal getBigDecimalInput() {
        while (!scanner.hasNextBigDecimal()) {
            System.out.println("Entrada inválida. Digite um valor numérico:");
            scanner.next();
        }
        BigDecimal value = scanner.nextBigDecimal();
        scanner.nextLine(); // limpar buffer
        return value;
    }

    private static LocalDate getDateInput() {
        while (true) {
            try {
                String input = scanner.nextLine();
                String[] parts = input.split("/");
                int day = Integer.parseInt(parts[0]);
                int month = Integer.parseInt(parts[1]);
                int year = Integer.parseInt(parts[2]);
                return LocalDate.of(year, month, day);
            } catch (Exception e) {
                System.out.println("Data inválida. Digite no formato dd/MM/yyyy:");
            }
        }
    }
}
