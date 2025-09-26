package dev.squad04.projetoFlap.auth.enums;

public enum UserRole {
    ADMIN("admin"),
    USER("user");

    private String permissao;

    UserRole(String permissao) {
        this.permissao = permissao;
    }

    public String getPermissao() {
        return permissao;
    }
}
