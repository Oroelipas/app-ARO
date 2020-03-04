package com.example.upnadeportes;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class InicioSesionWebActivity extends Activity {

    WebView miVisorWeb;

    protected void onCreate(Bundle savedInstanceState) {
        // Llamamos al instanciador de la actividad
        super.onCreate(savedInstanceState);
        // Ponemos el diseño que queremos
        setContentView(R.layout.web_view);

        // El link con la web de la UPNA deportes
        String url = "https://extuniv.unavarra.es/actividades/login";
        // Cogemos el visor web
        miVisorWeb = (WebView) findViewById(R.id.visorWeb);
        // Y los ajutes del visor
        final WebSettings ajustesVisorWeb = miVisorWeb.getSettings();
        // Le ponemos el visor que queremos usar, para que no nos salte al navegador
        // web predeterminado del Android
        WebViewClient clienteWeb = new WebViewClient();
        miVisorWeb.setWebViewClient(clienteWeb);
        // Cargamos la URL que queremos
        miVisorWeb.loadUrl(url);
        // Activamos el JavaScript, lo necesitamos porque la web lo usa
        ajustesVisorWeb.setJavaScriptEnabled(true);


    }

    public void finInicioSesion(View view) {
        Intent intent = new Intent(getApplicationContext(), ReservaPlazaActivity.class);
        startActivity(intent);
    }

}
