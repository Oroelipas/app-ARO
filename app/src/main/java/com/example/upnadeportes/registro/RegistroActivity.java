package com.example.upnadeportes.registro;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.upnadeportes.ApiClient;
import com.example.upnadeportes.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroActivity extends AppCompatActivity {

    private RegistroViewModel registroViewModel;
    private Map<String, Integer> carreras = new HashMap<>();
    private List<String> listaCarreras = new ArrayList<>();
    private ArrayAdapter<String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registro);

        getJsonCarreras();

        registroViewModel = ViewModelProviders.of(this, new RegistroViewModelFactory())
                .get(RegistroViewModel.class);

        final EditText nombreCompletoEditText = findViewById(R.id.registro_nombre_completo);
        final EditText fechaNacimientoEditText = findViewById(R.id.registro_fecha_nacimiento);
        final Spinner carreraSpinner = findViewById(R.id.registro_carrera);
        final EditText emailEditText = findViewById(R.id.registro_email);
        final EditText password_1_EditText = findViewById(R.id.registro_password1);
        final EditText password_2_EditText = findViewById(R.id.registro_password2);
        final Button registerButton = findViewById(R.id.registro_register);
        final RadioButton radio_hombre = findViewById(R.id.radio_hombre);
        final RadioButton radio_mujer = findViewById(R.id.radio_mujer);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading_registro);

        // Creamos el adaptador para el contenido del spinner de carreras
        adapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, listaCarreras);
        carreraSpinner.setAdapter(adapter);

        // Capturaremos la introduccion de la fecha de nacimiento
        fechaNacimientoEditText.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               showDatePickerDialog();
           }
        });

        registroViewModel.getRegistroFormState().observe(this, new Observer<RegistroFormState>() {
            @Override
            public void onChanged(@Nullable RegistroFormState registroFormState) {
                if (registroFormState == null) {
                    return;
                }
                // Invalidamos el botón de registrar si el estado del formulario es incorrecto
                registerButton.setEnabled(registroFormState.isDataValid());

                // Mostramos los errores
                if (registroFormState.getNombreCompletoError() != null) {
                   nombreCompletoEditText.setError(getString(registroFormState.getNombreCompletoError()));
                }
                if (registroFormState.getEmailError() != null) {
                    emailEditText.setError(getString(registroFormState.getEmailError()));
                }
                if (registroFormState.getCarreraError() != null) {
                    String errorString = getString(registroFormState.getCarreraError());
                    Toast toast = Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.show();
                }
                if (registroFormState.getFechaNacimientoError() != null) {
                    fechaNacimientoEditText.setError(getString(registroFormState.getFechaNacimientoError()));
                }
                if (registroFormState.getPasswordError() != null) {
                    password_1_EditText.setError(getString(registroFormState.getPasswordError()));
                    password_2_EditText.setError(getString(registroFormState.getPasswordError()));
                }
                if (registroFormState.getSexError() != null) {
                    String errorString = getString(registroFormState.getSexError());
                    Toast toast = Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP, 0, 0);
                    toast.show();
                }
            }
        });

        registroViewModel.getRegistroResult().observe(this, new Observer<RegistroResult>() {
            @Override
            public void onChanged(@Nullable RegistroResult registroResult) {
                if (registroResult == null) {
                    return;
                }

                loadingProgressBar.setVisibility(View.GONE);

                if (registroResult.getError() != null) {
                    // El registro ha fallado
                    showRegistroFailed(registroResult.getError());
                } else {
                    // El registro ha sido exitoso

                }
                setResult(Activity.RESULT_OK);

                finish();
            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                registroViewModel.registroDataChanged(
                        nombreCompletoEditText.getText().toString(),
                        emailEditText.getText().toString(),
                        carreraSpinner.toString(),
                        fechaNacimientoEditText.getText().toString(),
                        password_1_EditText.getText().toString(),
                        password_2_EditText.getText().toString(),
                        radio_hombre.isChecked(),
                        radio_mujer.isChecked());
            }
        };
        nombreCompletoEditText.addTextChangedListener(afterTextChangedListener);
        emailEditText.addTextChangedListener(afterTextChangedListener);
        password_1_EditText.addTextChangedListener(afterTextChangedListener);
        password_2_EditText.addTextChangedListener(afterTextChangedListener);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sexo;
                final RadioButton radio_mujer = findViewById(R.id.radio_mujer);
                if (radio_mujer.isChecked())
                    sexo = "Masculino";
                else
                    sexo = "Femenino";

                // Ponemos el loading a funcionar
                loadingProgressBar.setVisibility(View.VISIBLE);

                registroViewModel.registrar(nombreCompletoEditText.getText().toString(),
                        emailEditText.getText().toString(),
                        password_1_EditText.getText().toString(),
                        carreraSpinner.toString(),
                        fechaNacimientoEditText.getText().toString(),
                        sexo);

                /* Comprobaremos el resultado de la función de registro, avisaremos
                    al usuario de posibles fallos

                    emailEditText.setError("Email no disponible");

                 */

            }
        });
    }

    private void showRegistroFailed(@StringRes Integer errorString) {
        Toast toast = Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
    }

    public void onRadioButtonClicked(View view) {

        final EditText nombreCompletoEditText = findViewById(R.id.registro_nombre_completo);
        final EditText fechaNacimientoEditText = findViewById(R.id.registro_fecha_nacimiento);
        final Spinner carreraSpinner = findViewById(R.id.registro_carrera);
        final EditText emailEditText = findViewById(R.id.registro_email);
        final EditText password_1_EditText = findViewById(R.id.registro_password1);
        final EditText password_2_EditText = findViewById(R.id.registro_password2);

        boolean checked = ((RadioButton) view).isChecked();

        switch(view.getId()) {
            case R.id.radio_hombre:
                if (checked) {
                    final RadioButton radio_mujer = findViewById(R.id.radio_mujer);
                    final RadioButton radio_hombre = findViewById(R.id.radio_hombre);
                    radio_mujer.setChecked(false);
                    registroViewModel.registroDataChanged(
                            nombreCompletoEditText.getText().toString(),
                            emailEditText.getText().toString(),
                            carreraSpinner.toString(),
                            fechaNacimientoEditText.getText().toString(),
                            password_1_EditText.getText().toString(),
                            password_2_EditText.getText().toString(),
                            radio_hombre.isChecked(),
                            radio_mujer.isChecked());
                }
                break;
            case R.id.radio_mujer:
                if (checked) {
                    final RadioButton radio_hombre = findViewById(R.id.radio_hombre);
                    final RadioButton radio_mujer = findViewById(R.id.radio_mujer);
                    radio_hombre.setChecked(false);
                    registroViewModel.registroDataChanged(
                            nombreCompletoEditText.getText().toString(),
                            emailEditText.getText().toString(),
                            carreraSpinner.toString(),
                            fechaNacimientoEditText.getText().toString(),
                            password_1_EditText.getText().toString(),
                            password_2_EditText.getText().toString(),
                            radio_hombre.isChecked(),
                            radio_mujer.isChecked());
                }
                break;
        }
    }

    public void showDatePickerDialog() {

        final EditText fechaNacimientoEditText = findViewById(R.id.registro_fecha_nacimiento);

        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = day + " / " + (month+1) + " / " + year;
                fechaNacimientoEditText.setText(selectedDate);
            }
        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void getJsonCarreras(){
        Call<ResponseBody> call = ApiClient.getInstance().getAwsApi().getCarreras();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    System.out.println("Contenido JSON carreras:");
                    String json = response.body().string();
                    System.out.println(json);
                    JSONArray jsonCarreras;
                    try {
                        jsonCarreras = new JSONArray(json);
                        poblarSpinnerCarreras(jsonCarreras);
                    } catch (JSONException e) {
                        System.out.println("Error leyendo JSON de carreras");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(RegistroActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void poblarSpinnerCarreras(JSONArray jsonCarreras) {
        JSONObject objeto;
        for(int i=0; i < jsonCarreras.length(); i++) {

            try {
                objeto = jsonCarreras.getJSONObject(i);
            } catch (JSONException e) {
                System.out.println("Carrera leída incorrectamente");
                objeto = null;
            }
            if (objeto != null) {
                try {
                    carreras.put((String)objeto.get("nombre"), (Integer)objeto.get("idcarrera"));
                } catch (JSONException e) {
                    System.out.println("Error almacenando carrera en el mapa");
                }
            }
        }
        listaCarreras.addAll(carreras.keySet());
        adapter.notifyDataSetChanged();
    }

}
