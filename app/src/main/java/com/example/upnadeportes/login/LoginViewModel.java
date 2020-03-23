package com.example.upnadeportes.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.util.Patterns;

import com.example.upnadeportes.R;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();

    LoginViewModel(){}

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public int login(String email, String password) {
        // Se realiza la operación de login de usuario
        System.out.println("email: " + email);
        System.out.println("password: " + password);

        /* La petición de login debe ser síncrona (nos quedamos esperando a que el login se complete correctamente antes de dejarle hacer nada al usuario) */

        return -1;
    }

    /* Realizamos comprobaciones de validez de los campos del formulario de login */
    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password_length));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    /* Comprobación de validez del username */
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    /* Comprobación de validez de la contraseña */
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}
