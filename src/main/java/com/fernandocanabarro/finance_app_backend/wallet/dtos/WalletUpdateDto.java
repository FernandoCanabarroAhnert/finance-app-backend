package com.fernandocanabarro.finance_app_backend.wallet.dtos;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class WalletUpdateDto {

    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Color is required")
    private String color;
    
}
