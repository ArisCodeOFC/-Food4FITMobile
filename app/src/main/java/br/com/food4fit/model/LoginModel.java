package br.com.food4fit.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter @AllArgsConstructor
public class LoginModel {
    private String email;
    private String senha;
}
