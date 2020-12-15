package com.ti.test.model.constants;

import lombok.Getter;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum PresentationChoice {

    ACCOUNT("1", "Visualizza il saldo dell'account"),
    TRANSACTIONS("2", "Visualizza la lista delle transazioni"),
    BANK_TRANSFER("3", "Effettua un bonifico");

    private final String value;
    private final String description;

    PresentationChoice(String value, String description) {
        this.value = value;
        this.description = description;
    }

    private static final Map<String, PresentationChoice> mappedValues = Stream
            .of(PresentationChoice.values())
            .collect(Collectors.toMap(PresentationChoice::getValue, Function.identity()));

    public static PresentationChoice getByValue(String value) {
        return mappedValues.get(value);
    }
}
