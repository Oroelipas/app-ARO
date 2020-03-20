package com.example.upnadeportes.tabbed;

import androidx.arch.core.util.Function;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.example.upnadeportes.data.Actividad;
import com.example.upnadeportes.data.ActividadesSemana;

import java.util.List;

/**
 * Es la clase del Modelo.
 * Lo único que hace es mostrar unos valores automáticamente generados
 * */
public class PageViewModel extends ViewModel {

    private MutableLiveData<Integer> mIndex = new MutableLiveData<>();
    private LiveData<String> mText = Transformations.map(mIndex, new Function<Integer, String>() {
        @Override
        public String apply(Integer input) {
            return "Hello world from section: " + input;
        }
    });

    private ActividadesSemana actividadesSemana = ActividadesSemana.getInstance();

    // Pongo los 5 con las actividades de los días de la semana que voy a usar
    private MutableLiveData<List<Actividad>> lunes;
    private MutableLiveData<List<Actividad>> martes;
    private MutableLiveData<List<Actividad>> miercoles;
    private MutableLiveData<List<Actividad>> jueves;
    private MutableLiveData<List<Actividad>> viernes;


    public LiveData<List<Actividad>> getLunes() {
        if (lunes == null)
            lunes = new MutableLiveData<>();
        lunes.setValue(actividadesSemana.getLunes());
        return lunes;
    }

    public LiveData<List<Actividad>> getMartes() {
        if (martes == null)
            martes = new MutableLiveData<>();
        martes.setValue(actividadesSemana.getMartes());
        return martes;
    }

    public LiveData<List<Actividad>> getMiercoles() {
        if (miercoles == null)
            miercoles = new MutableLiveData<>();
        miercoles.setValue(actividadesSemana.getMiercoles());
        return miercoles;
    }

    public LiveData<List<Actividad>> getJueves() {
        if (jueves == null)
            jueves = new MutableLiveData<>();
        jueves.setValue(actividadesSemana.getJueves());
        return jueves;
    }

    public LiveData<List<Actividad>> getViernes() {
        if (viernes == null)
            viernes = new MutableLiveData<>();
        viernes.setValue(actividadesSemana.getViernes());
        return viernes;
    }


    // Este método está para cuando le demos el número de pantalla nos diga que estamos en la pestaña
    // index
    public void setIndex(int index) {
        mIndex.setValue(index);
    }

    // Este método nos devuelve el texto que va a mostrar la pestaña
    public LiveData<String> getText() {
        return mText;
    }


}