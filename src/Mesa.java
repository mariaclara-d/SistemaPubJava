import java.util.ArrayList;
import java.util.List;

public class Mesa {
    private String identificacao;
    private boolean ocupada;
    private List<Produto> itensConsumidos;

    public Mesa() {
        this.identificacao = "Livre";
        this.ocupada = false;
        this.itensConsumidos = new ArrayList<>();
    }

    public void adicionarItem(Produto p) {
        this.itensConsumidos.add(p);
    }

    public void ocuparMesa(String nome) {
        this.identificacao = nome;
        this.ocupada = true;
    }

    public String getIdentificacao() {
        return identificacao;
    }

    public double getTotalConsumido() {
        return itensConsumidos.stream().mapToDouble(Produto::getPrice).sum();
    }

    public List<Produto> getItensConsumidos() {
        return itensConsumidos;
    }

    public boolean isOcupada() {
        return ocupada;
    }

    public void fecharMesa() {
        this.ocupada = false;
        this.identificacao = "Livre";
        this.itensConsumidos.clear();
    }
}