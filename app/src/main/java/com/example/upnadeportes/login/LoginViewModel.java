package com.example.upnadeportes.login;

import android.util.Log;
import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.upnadeportes.ApiClient;
import com.example.upnadeportes.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginViewModel extends ViewModel {

    private final String TAG = this.getClass().getName();
    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();

    LoginViewModel(){}

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String email, String password) {

        // Se realiza la operación de login de usuario

        Call<ResponseBody> call = ApiClient.getInstance().getAwsApi().postLogin(email, password);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String json = response.body().string();
                    JSONObject jsonRespuesta;
                    jsonRespuesta = new JSONObject(json);
                    if (jsonRespuesta.getString("error").equals("null")) {
                        String IdUser =  ((Integer) jsonRespuesta.get("IdUser")).toString();
                        Log.v(TAG,"Login correcto, userId: " + IdUser);
                        loginResult.setValue(new LoginResult(new LoggedInUserView(null, IdUser, email)));
                    } else {
                        if (jsonRespuesta.getInt("error") == 404) {
                            Log.v(TAG, "Login incorrecto: 404");
                            loginResult.setValue(new LoginResult(404));
                        }
                    }
                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    Log.v(TAG,"Error procesando la respuesta a la petición");
                    loginResult.setValue(new LoginResult(404));
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.v(TAG,"Error realizando la petición de login");
                loginResult.setValue(new LoginResult(404));
            }
        });

    }

    /* Realizamos comprobaciones de validez de los campos del formulario de login */
    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_email, null));
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
        }
        return false;
    }

    /* Comprobación de validez de la contraseña */
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }


}
