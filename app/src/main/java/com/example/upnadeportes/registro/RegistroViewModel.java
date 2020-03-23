package com.example.upnadeportes.registro;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import android.util.Patterns;
import android.widget.Toast;

import com.example.upnadeportes.ApiClient;
import com.example.upnadeportes.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroViewModel extends ViewModel {

    private MutableLiveData<RegistroFormState> registroFormState = new MutableLiveData<>();
    private MutableLiveData<RegistroResult> registroResult = new MutableLiveData<>();
    private Integer passwordErrorCode = 0;

    RegistroViewModel() {}

    LiveData<RegistroFormState> getRegistroFormState() {
        return registroFormState;
    }

    LiveData<RegistroResult> getRegistroResult() {
        return registroResult;
    }

    public int registrar(String nombreCompleto, String email, String password, String idCarrera, String fechaNacimiento, String sexo) {
        /* Se realiza la operación de registro de usuario, este método devolverá:
            -> Un id de usuario > 0 si no hay ningún fallo
            -> -1 si hay algún fallo
         */

        System.out.println("nombreCompleto: " + nombreCompleto);
        System.out.println("email: " + email);
        System.out.println("idCarrera: " + idCarrera);
        System.out.println("fechaNacimiento: " + fechaNacimiento);
        System.out.println("password: " + password);
        System.out.println("sexo: " + sexo);

        /* La petición de registro debe ser síncrona (nos quedamos esperando a que el registro se complete correctamente antes de dejarle hacer nada al usuario) */

        return -1;
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

            System.out.println("Password errorCode: " + passwordErrorCode);

            if (passwordErrorCode == -1) {
                // Las contraseñas no contienen valores válidos
                registroFormState.setValue(new RegistroFormState(null, null, null, null, R.string.invalid_password_values, null));
            }
            if (passwordErrorCode == -2) {
                // Las contraseñas no tienen una longitud > 5
                registroFormState.setValue(new RegistroFormState(null, null, null, null, R.string.invalid_password_length_registro, null));
            }
            if (passwordErrorCode == -3) {
                // Las contraseñas no coinciden
                registroFormState.setValue(new RegistroFormState(null, null, null, null, R.string.invalid_password_equality, null));
            }

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
        return ((carrera != null) && (!carrera.trim().isEmpty()) && (!carrera.equals("Selecciona una carrera")));
    }

    // Validación de fecha de nacimiento
    private boolean isFechaNacimientoValid(String fechaNacimiento) {
        return ((fechaNacimiento != null) && (!fechaNacimiento.trim().isEmpty()));
    }

    // Validación de contraseña
    private boolean isPasswordValid(String password1, String password2) {

        if ((password1 == null) || (password2 == null)) {
            passwordErrorCode = -1;
            return false;
        }

        if (password1.trim().isEmpty() || password2.trim().isEmpty()) {
            passwordErrorCode = -1;
            return false;
        }

        if ((password1.trim().length() <= 5) || (password2.trim().length() <= 5)) {
            passwordErrorCode = -2;
            return false;
        }

        if (password1.trim().compareTo(password2.trim()) != 0) {
            passwordErrorCode = -3;
            return false;
        }

        passwordErrorCode = 0;
        return true;
    }

    // Validación de sexo
    private boolean isSexValid(boolean sex_hombre, boolean sex_mujer) {
        return (sex_hombre || sex_mujer);
    }

}
