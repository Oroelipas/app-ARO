package com.example.upnadeportes.registro;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import com.example.upnadeportes.ApiClient;
import com.example.upnadeportes.R;
import com.example.upnadeportes.login.LoggedInUserView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroViewModel extends ViewModel {

    public final String TAG = this.getClass().getName();
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

    public void registrar(String nombreCompleto, String email, String password, String idCarrera, String fechaNacimiento, String sexo) {

        System.out.println("Fecha de nacimiento: " + fechaNacimiento);

        Call<ResponseBody> call = ApiClient.getInstance().getAwsApi().postNuevoUsuario(nombreCompleto, email, idCarrera, password, fechaNacimiento, sexo);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String json = response.body().string();
                    JSONObject jsonRespuesta;
                    System.out.println(json);
                    jsonRespuesta = new JSONObject(json);
                    if (jsonRespuesta.getInt("error") == 409) {
                        Log.v(TAG,"Registro incorrecto: 409");
                        registroResult.setValue(new RegistroResult(R.string.register_failed));
                    } else {
                        Log.v(TAG,"Registro correcto");
                         String userId = (String) jsonRespuesta.get("userId");
                         registroResult.setValue(new RegistroResult(new LoggedInUserView(nombreCompleto, userId, email)));
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    Log.v(TAG,"Error procesando la respuesta a la petición");
                    registroResult.setValue(new RegistroResult(R.string.register_failed));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v(TAG,"Error realizando la petición de registro");
                registroResult.setValue(new RegistroResult(R.string.register_failed));
            }
        });

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
