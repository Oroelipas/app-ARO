package com.example.upnadeportes.tabbed;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.upnadeportes.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 * Esta es la clase que se encarga de devolvernos la sección de la página que hemos seleccionado
 * Esta clase no hay que tocarla
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2,
            R.string.tab_text_3, R.string.tab_text_4, R.string.tab_text_5};
    private final Context mContext;


    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }


    /**
     * Llama a la creación del fragmento del que queremos obtener la información.
     * */
    @Override
    public Fragment getItem(int position) {

        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        // Llama al fragmento correspondiente, el cual tiene la información de la siguiente
        // pestaña que queremos mostrar
        return PlaceholderFragment.newInstance(position + 1);
    }

    /**
     * Obtiene el título de la pestaña seleccionada.
     * */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    /**
     * Devuelve el número de páginas total que tenemos para mostrar
     * */
    @Override
    public int getCount() {
        // Show 5 total pages.
        return 5;
    }



}