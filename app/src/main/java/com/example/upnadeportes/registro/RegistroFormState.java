package com.example.upnadeportes.registro;

import android.support.annotation.Nullable;

/**
 * Validación de datos del formulario de registro.
 */
public class RegistroFormState {

    @Nullable
    private Integer nombreCompletoError;
    @Nullable
    private Integer emailError;
    @Nullable
    private Integer carreraError;
    @Nullable
    private Integer fechaNacimientoError;
    @Nullable
    private Integer passwordError;
    @Nullable
    private Integer sexError;
    private boolean isDataValid;

   RegistroFormState(@Nullable Integer nombreCompletoError, @Nullable Integer emailError, @Nullable Integer carreraError, @Nullable Integer fechaNacimientoError, @Nullable Integer passwordError, @Nullable Integer sexError) {
        this.nombreCompletoError = nombreCompletoError;
        this.emailError = emailError;
        this.carreraError = carreraError;
        this.fechaNacimientoError = fechaNacimientoError;
        this.passwordError = passwordError;
        this.sexError = sexError;
        this.isDataValid = false;
    }

    RegistroFormState(boolean isDataValid) {
        this.nombreCompletoError = null;
        this.emailError = null;
        this.carreraError = null;
        this.fechaNacimientoError = null;
        this.passwordError = null;
        this.sexError = null;
        this.isDataValid = isDataValid;
    }

    @Nullable
    Integer getNombreCompletoError() {
        return nombreCompletoError;
    }

    @Nullable
    Integer getEmailError() {
        return emailError;
    }

    @Nullable
    Integer getCarreraError() {
        return carreraError;
    }

    @Nullable
    Integer getFechaNacimientoError() {
        return fechaNacimientoError;
    }

    @Nullable
    Integer getPasswordError() {
        return passwordError;
    }

    @Nullable
    Integer getSexError() {
        return sexError;
    }

    boolean isDataValid() {
        return isDataValid;
    }

}
