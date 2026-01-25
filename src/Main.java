import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Mesa[][] salao = new Mesa[3][4];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                salao[i][j] = new Mesa();
            }
        }

        Produto[] cardapio = {
                new Produto("Cerveja Brahma", 5.0),
                new Produto("Cerveja Amstel", 5.0),
                new Produto("Cigarro Dunhill", 2.0)
        };

        JanelaPub minhaJanela = new JanelaPub(salao, cardapio);

        boolean rodando = true;
        while (rodando) {
            System.out.println("\n--- SISTEMA DE CAIXA PUB ---");
            System.out.println("1. Abrir Mesa");
            System.out.println("2. Adicionar Consumo (Cardápio)");
            System.out.println("3. Fechar Conta/Ver Saldo");
            System.out.println("4. Sair");
            System.out.print("Escolha uma opção: ");

            int opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                    System.out.print("Número da mesa (1-12): ");
                    int num = sc.nextInt();
                    sc.nextLine();
                    salao[(num - 1) / 4][(num - 1) % 4].ocuparMesa(getUserName(sc));
                    minhaJanela.atualizarPainel(); // Atualiza a cor para VERMELHO
                    break;

                case 2:
                    handleConsumo(sc, salao, cardapio);
                    break;

                case 3:
                    handleFechamento(sc, salao, minhaJanela); // Atualiza a cor para VERDE
                    break;

                case 4:
                    rodando = false;
                    break;
            }
        }
        sc.close();
        System.exit(0);
    }

    private static String getUserName(Scanner sc) {
        System.out.print("Nome do cliente/grupo: ");
        return sc.nextLine();
    }

    private static void handleConsumo(Scanner sc, Mesa[][] salao, Produto[] cardapio) {
        System.out.print("Digite o número da mesa: ");
        int numM = sc.nextInt();
        Mesa m = salao[(numM - 1) / 4][(numM - 1) % 4];

        if (m.isOcupada()) {
            int escolha = -1;
            while (escolha != 0) {
                System.out.println("\n--- Cardápio ---");
                for (int i = 0; i < cardapio.length; i++) {
                    System.out.println((i + 1) + ". " + cardapio[i].getName() + " - R$ " + cardapio[i].getPrice());
                }
                System.out.print("0. Sair | Escolha: ");
                escolha = sc.nextInt();
                if (escolha > 0 && escolha <= cardapio.length) {
                    m.adicionarItem(cardapio[escolha - 1]);
                }
            }
        } else {
            System.out.println("Erro: Mesa fechada!");
        }
    }

    private static void handleFechamento(Scanner sc, Mesa[][] salao, JanelaPub janela) {
        System.out.print("Mesa para fechar: ");
        int n = sc.nextInt();
        Mesa m = salao[(n - 1) / 4][(n - 1) % 4];

        if (m.isOcupada()) {
            System.out.println("Extrato: " + m.getIdentificacao());
            m.getItensConsumidos().forEach(p -> System.out.println("- " + p.getName()));
            System.out.printf("Total: R$ %.2f%n", m.getTotalConsumido());
            m.fecharMesa();
            janela.atualizarPainel();
        }
    }
}