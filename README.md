# Sistema de Armazém - Cestas Básicas

Este projeto é um sistema simples de gerenciamento de cestas básicas, permitindo controlar estoque, vendas, caixa e validade dos produtos.  

## Melhorias Implementadas

1. **Validação de datas**  
   - Evita inserir cestas com data de validade no passado, garantindo que o estoque seja sempre válido.

2. **Validação de quantidade e valores negativos**  
   - Previne que o usuário insira quantidade negativa ou preço negativo ao receber cestas.

3. **Evitar `IndexOutOfBounds` ao vender cestas**  
   - Confere se há cestas suficientes para venda antes de realizar a operação, evitando erros de índice.

4. **Remover itens vencidos corretamente**  
   - Corrige a lógica de remoção, garantindo que apenas itens com validade anterior à data atual sejam descartados.

5. **Uso de `scanner.nextLine()` após `nextInt()` ou `nextBigDecimal()`**  
   - Evita problemas de leitura de entrada, onde `nextLine()` pulava inputs após números.

6. **Melhoria de legibilidade**  
   - Métodos menores e organizados, mensagens mais claras, uso correto de `BigDecimal.ZERO` para somatórios.

7. **Uso de `RoundingMode` para evitar erros de divisão**  
   - Mantida a divisão com arredondamento para cima (`CEILING`) no cálculo do preço unitário das cestas.

---

## Funcionalidades

- Visualizar estoque de cestas básicas
- Visualizar saldo do caixa
- Receber novas cestas com preço e validade
- Vender cestas e atualizar caixa
- Remover cestas vencidas automaticamente

---

## Observações

- Todas as entradas do usuário são validadas para prevenir erros comuns.
- A aplicação utiliza `BigDecimal` para cálculos de preços, garantindo precisão financeira.
- Datas devem ser informadas no formato `dd/MM/yyyy`.

