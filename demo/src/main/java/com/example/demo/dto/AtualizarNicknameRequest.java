package com.example.demo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class AtualizarNicknameRequest {
    @NotBlank(message = "Nickname não pode estar em branco")
    @Size(min = 1, max = 50, message = "Nickname deve ter entre 1 e 50 caracteres")
    private String nickname;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}