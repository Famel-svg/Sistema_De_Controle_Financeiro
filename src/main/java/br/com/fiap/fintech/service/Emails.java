package br.com.fiap.fintech.service;

import java.util.Locale;

/** E-mail é a identidade de login: comparado sempre sem espaços e em minúsculas. */
public final class Emails {

    private Emails() {
    }

    public static String normalizar(String email) {
        return email == null ? null : email.trim().toLowerCase(Locale.ROOT);
    }
}
