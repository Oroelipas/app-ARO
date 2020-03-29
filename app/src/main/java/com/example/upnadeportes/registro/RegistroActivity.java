package com.example.upnadeportes.registro;

import android.app.Activity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.upnadeportes.ApiClient;
import com.example.upnadeportes.MyApplication;
import com.example.upnadeportes.R;
import com.example.upnadeportes.tabbed.ActividadesActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroActivity extends AppCompatActivity {

    private final String TAG = this.getClass().getName();
    private RegistroViewModel registroViewModel;
    private Map<String, Integer> carreras = new LinkedHashMap<>();
    private List<String> listaCarreras = new ArrayList<>();
    private ArrayAdapter<String> adapter;
    private String carrera = "";
    private String fechaSeleccionada = "";

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
        carreraSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                carrera = String.valueOf(carreraSpinner.getSelectedItem());
                registroViewModel.registroDataChanged(
                        nombreCompletoEditText.getText().toString(),
                        emailEditText.getText().toString(),
                        carrera,
                        fechaNacimientoEditText.getText().toString(),
                        password_1_EditText.getText().toString(),
                        password_2_EditText.getText().toString(),
                        radio_hombre.isChecked(),
                        radio_mujer.isChecked());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Auto generado
            }
        });

        // Capturaremos la introduccion de la fecha de nacimiento
        fechaNacimientoEditText.setOnClickListener(v -> showDatePickerDialog());

        registroViewModel.getRegistroFormState().observe(this, registroFormState -> {
            if (registroFormState == null) {
                return;
            }

            // Invalidamos el botón de registrar si el estado del formulario es incorrecto
            registerButton.setEnabled(registroFormState.isDataValid());

            if (registroFormState.isDataValid()) {
                // Si los datos son válidos no hay errores
                nombreCompletoEditText.setError(null);
                emailEditText.setError(null);
                fechaNacimientoEditText.setError(null);
                password_1_EditText.setError(null);
                password_2_EditText.setError(null);
            }

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
        });

        registroViewModel.getRegistroResult().observe(this, registroResult -> {
            if (registroResult == null) {
                return;
            }

            loadingProgressBar.setVisibility(View.GONE);

            if (registroResult.getError() != null) {
                // El registro ha fallado
                if (registroResult.getError() == 409) {
                    emailEditText.setError("Email no disponible");
                } else {
                    showRegistroFailed(R.string.register_failed);
                }
            } else {
                // El registro ha sido exitoso
                Toast toast = Toast.makeText(getApplicationContext(), "Registro OK", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP, 0, 0);
                toast.show();
                setResult(Activity.RESULT_OK);
                int userId = Integer.valueOf(registroResult.getSuccess().getUserId());
                String email = registroResult.getSuccess().getEmail();
                ((MyApplication)getApplication()).setIdUsuario(userId);
                ((MyApplication)getApplication()).setEmailUsuario(email);
                Intent intent = new Intent(getApplicationContext(), ActividadesActivity.class);
                startActivity(intent);
            }
            setResult(Activity.RESULT_OK);
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
                        carrera,
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

        registerButton.setOnClickListener(v -> {

            String sexo;
            if (radio_mujer.isChecked())
                sexo = "femenino";
            else
                sexo = "masculino";

            registroViewModel.registrar(nombreCompletoEditText.getText().toString(),
                    emailEditText.getText().toString(),
                    password_1_EditText.getText().toString(),
                    carreras.get(carrera).toString(),
                    fechaSeleccionada,
                    sexo);
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
                            carrera,
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
                            carrera,
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

        final EditText nombreCompletoEditText = findViewById(R.id.registro_nombre_completo);
        final EditText fechaNacimientoEditText = findViewById(R.id.registro_fecha_nacimiento);
        final EditText emailEditText = findViewById(R.id.registro_email);
        final EditText password_1_EditText = findViewById(R.id.registro_password1);
        final EditText password_2_EditText = findViewById(R.id.registro_password2);
        final RadioButton radio_mujer = findViewById(R.id.radio_mujer);
        final RadioButton radio_hombre = findViewById(R.id.radio_hombre);

        DatePickerFragment newFragment = DatePickerFragment.newInstance((datePicker, year, month, day) -> {

            Calendar calendar = Calendar.getInstance();
            calendar.set(year, month, day);
            Date dt = calendar.getTime();
            // java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            fechaSeleccionada = sdf.format(dt);

            // +1 because January is zero
            final String selectedDate = day + " / " + (month+1) + " / " + year;
            fechaNacimientoEditText.setText(selectedDate);
            registroViewModel.registroDataChanged(
                    nombreCompletoEditText.getText().toString(),
                    emailEditText.getText().toString(),
                    carrera,
                    fechaNacimientoEditText.getText().toString(),
                    password_1_EditText.getText().toString(),
                    password_2_EditText.getText().toString(),
                    radio_hombre.isChecked(),
                    radio_mujer.isChecked());
        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void getJsonCarreras(){
        Call<ResponseBody> call = ApiClient.getInstance().getAwsApi().getCarreras();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String json = response.body().string();
                    JSONArray jsonCarreras;
                    try {
                        jsonCarreras = new JSONArray(json);
                        poblarSpinnerCarreras(jsonCarreras);
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.v(TAG,"Error leyendo JSON de carreras");
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
                e.printStackTrace();
                Log.v(TAG,"Carrera leída incorrectamente");
                objeto = null;
            }
            if (objeto != null) {
                try {
                    carreras.put((String)objeto.get("nombre"), (Integer)objeto.get("idcarrera"));
                } catch (JSONException e) {
                    Log.v(TAG,"Error almacenando carrera en el mapa");
                }
            }
        }
        listaCarreras.add("Selecciona una carrera");
        Collection<String> carrerasOrdenadas = new TreeSet<>(Collator.getInstance());
        carrerasOrdenadas.addAll(carreras.keySet());
        listaCarreras.addAll(carrerasOrdenadas);
        adapter.notifyDataSetChanged();
    }
}
