package dto;


import com.example.demo.model.ItemPedido;
import java.util.List;

public class MesaResponse {
    private int id;
    private String name;
    private String nickname;
    private List<ItemPedido> orders;

    public MesaResponse(int id, String name, String nickname, List<ItemPedido> orders) {
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.orders = orders;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public List<ItemPedido> getOrders() {
        return orders;
    }
}