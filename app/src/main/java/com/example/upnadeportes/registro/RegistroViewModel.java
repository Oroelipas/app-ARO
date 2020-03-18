package com.example.upnadeportes.registro;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.util.Patterns;

import com.example.upnadeportes.R;

public class RegistroViewModel extends ViewModel {

    private MutableLiveData<RegistroFormState> registroFormState = new MutableLiveData<>();
    private MutableLiveData<RegistroResult> registroResult = new MutableLiveData<>();

    RegistroViewModel() {}

    LiveData<RegistroFormState> getRegistroFormState() {
        return registroFormState;
    }

    LiveData<RegistroResult> getRegistroResult() {
        return registroResult;
    }

    public void registrar(String nombreCompleto, String email, String password, String carrera, String fechaNacimiento, String sexo) {

        // Se realiza la operación de registro de usuario

    }

    public void registroDataChanged(String nombreCompleto, String email, String carrera, String fechaNacimiento, String password1, String password2, boolean sex_hombre, boolean sex_mujer) {
        if (!isNombreCompletoValid(nombreCompleto)) {
            registroFormState.setValue(new RegistroFormState(R.string.invalid_nombre_completo, null, null, null, null, null));
        } else if (!isEmailValid(email)) {
            registroFormState.setValue(new RegistroFormState(null, R.string.invalid_email, null, null,null, null));
        } else if (!isCarreraValid(carrera)) {
            registroFormState.setValue(new RegistroFormState(null, null, R.string.invalid_carrera_registro, null,null, null));
        } else if (!isFechaNacimientoValid(fechaNacimiento)) {
            registroFormState.setValue(new RegistroFormState(null, null, null, R.string.invalid_fecha_nacimiento_registro, null, null));
        } else if (!isPasswordValid(password1, password2)) {
            registroFormState.setValue(new RegistroFormState(null, null, null, null, R.string.invalid_password_registro, null));
        } else if (!isSexValid(sex_hombre, sex_mujer)) {
            registroFormState.setValue(new RegistroFormState(null, null, null, null, null,  R.string.invalid_sex_registro));
        } else {
            registroFormState.setValue(new RegistroFormState(true));
        }
    }

    // Validación de nombre completo
    private boolean isNombreCompletoValid(String nombreCompleto) {
        if (nombreCompleto == null) {
            return false;
        }
        if (nombreCompleto.trim().isEmpty())
            return false;
        return true;
    }

    // Validación de email
    private boolean isEmailValid(String email) {
        if (email == null) {
            return false;
        }
        if (email.contains("@")) {
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                return false;
            }
        } else {
            return false;
        }
        return true;
    }

    // Validación de carrera
    private boolean isCarreraValid(String carrera) {
        return !(carrera == null);
    }

    // Validación de fecha de nacimiento
    private boolean isFechaNacimientoValid(String fechaNacimiento) {
        return !(fechaNacimiento == null);
    }

    // Validación de contraseña
    private boolean isPasswordValid(String password1, String password2) {
        if ((password1 == null) || (password2 == null)) {
            return false;
        }
        if (password1.trim().isEmpty() || password2.trim().isEmpty()) {
            return false;
        }
        if ((password1.trim().length() <= 5) || (password2.trim().length() <= 5)) {
            return false;
        }
        return (password1.trim().compareTo(password2.trim()) == 0);
    }

    // Validación de sexo
    private boolean isSexValid(boolean sex_hombre, boolean sex_mujer) {
        return (sex_hombre || sex_mujer);
    }

}
