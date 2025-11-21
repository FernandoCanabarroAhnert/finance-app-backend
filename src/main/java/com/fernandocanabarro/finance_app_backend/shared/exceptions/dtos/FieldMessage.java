package com.fernandocanabarro.finance_app_backend.shared.exceptions.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FieldMessage {
    private String fieldName;
    private String message;
}
