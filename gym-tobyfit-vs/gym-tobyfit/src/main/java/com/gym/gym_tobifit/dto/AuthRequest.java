package com.gym.gym_tobifit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/** S08: mismos campos username y password; username contiene el correo. */
@Data
public class AuthRequest {
    @NotBlank
    @Size(max = 100)
    private String username;

    @NotBlank
    @Size(max = 72)
    private String password;
}
