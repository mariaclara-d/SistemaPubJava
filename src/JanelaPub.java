import javax.swing.*;
import java.awt.*;
import java.util.stream.Stream;
import java.util.stream.Collectors;

public class JanelaPub extends JFrame {
    private Mesa[][] salao;
    private Produto[] cardapio;
    private JPanel painelMesas;

    public JanelaPub(Mesa[][] salao, Produto[] cardapio) {
        this.salao = salao;
        this.cardapio = cardapio;

        setTitle("Gestão do Pub - 12 Mesas");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("Mapa do Salão", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        add(lblTitulo, BorderLayout.NORTH);

        painelMesas = new JPanel();
        painelMesas.setLayout(new GridLayout(3, 4, 10, 10));
        add(painelMesas, BorderLayout.CENTER);

        atualizarPainel();
        setVisible(true);
    }

    public void atualizarPainel() {
        painelMesas.removeAll();

        for (int i = 1; i <= 12; i++) {
            int linha = (i - 1) / 4;
            int coluna = (i - 1) % 4;
            Mesa mesaAtual = salao[linha][coluna];

            JButton btnMesa = new JButton("Mesa " + i);
            btnMesa.setOpaque(true);
            btnMesa.setBorderPainted(false);

            if (mesaAtual.isOcupada()) {
                btnMesa.setBackground(Color.RED);
                btnMesa.setForeground(Color.WHITE);
                btnMesa.setText("<html>Mesa " + i + "<br>(" + mesaAtual.getIdentificacao() + ")</html>");
            } else {
                btnMesa.setBackground(Color.GREEN);
                btnMesa.setForeground(Color.BLACK);
            }

            final int numeroMesa = i;

            btnMesa.addActionListener(e -> {
                Mesa mesaSelecionada = salao[(numeroMesa - 1) / 4][(numeroMesa - 1) % 4];

                if (!mesaSelecionada.isOcupada()) {
                    String nome = JOptionPane.showInputDialog(this, "Nome do cliente:");
                    if (nome != null && !nome.trim().isEmpty()) {
                        mesaSelecionada.ocuparMesa(nome);
                        atualizarPainel();
                    }
                } else {
                    Object[] opçõesMenu = {"Adicionar Item", "Fechar Conta", "Cancelar"};

                    int acao = JOptionPane.showOptionDialog(
                            this,
                            "O que deseja fazer na mesa de " + mesaSelecionada.getIdentificacao() + "?",
                            "Ações da Mesa " + numeroMesa,
                            JOptionPane.DEFAULT_OPTION,
                            JOptionPane.QUESTION_MESSAGE,
                            null,
                            opçõesMenu,
                            opçõesMenu[0]
                    );

                    if (acao == 0) {
                        String[] itensCardapio = Stream.of(cardapio).map(Produto::getName).toArray(String[]::new);
                        String escolha = (String) JOptionPane.showInputDialog(this, "Selecione o produto:", "Cardápio",
                                JOptionPane.QUESTION_MESSAGE, null, itensCardapio, itensCardapio[0]);

                        if (escolha != null) {
                            Produto p = Stream.of(cardapio).filter(prod -> prod.getName().equals(escolha)).findFirst().orElse(null);
                            if (p != null) {
                                mesaSelecionada.adicionarItem(p);
                                JOptionPane.showMessageDialog(this, p.getName() + " adicionado!");
                            }
                        }
                    } else if (acao == 1) {
                        if (mesaSelecionada.getItensConsumidos().isEmpty()) {
                            JOptionPane.showMessageDialog(this, "Esta mesa não possui consumo para fechar!");
                        } else {
                            String extrato = mesaSelecionada.getItensConsumidos().stream()
                                    .map(p -> p.getName() + " - R$ " + p.getPrice())
                                    .collect(Collectors.joining("\n"));

                            String mensagemFinal = "Extrato:\n" + extrato + "\n\nTotal: R$ " + mesaSelecionada.getTotalConsumido();
                            JOptionPane.showMessageDialog(this, mensagemFinal);

                            mesaSelecionada.fecharMesa();
                            JOptionPane.showMessageDialog(this, "Mesa liberada com sucesso!");
                            atualizarPainel();
                        }
                    }
                }
            });

            painelMesas.add(btnMesa);
        }
        painelMesas.revalidate();
        painelMesas.repaint();
    }
}