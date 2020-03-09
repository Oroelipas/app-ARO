package com.example.upnadeportes;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ReservaPlazaActivity extends Activity {
    WebView miVisorWeb;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_view);
        // El link con la web de la UPNA deportes
        String url = "https://extuniv.unavarra.es/actividades/reservas/actividades";
        // Cogemos el visor web
        miVisorWeb = (WebView) findViewById(R.id.visorWeb);
        // Y los ajutes del visor
        /*final WebSettings ajustesVisorWeb = miVisorWeb.getSettings();
        // Le ponemos el visor que queremos usar, para que no nos salte al navegador
        // web predeterminado del Android
        WebViewClient clienteWeb = new WebViewClient();
        miVisorWeb.setWebViewClient(clienteWeb);
        // Cargamos la URL que queremos
        miVisorWeb.loadUrl(url);
        // Activamos el JavaScript, lo necesitamos porque la web lo usa
        clienteWeb.onPageFinished(miVisorWeb, );
        ajustesVisorWeb.setJavaScriptEnabled(true);
        // Ahora es cuando le vamos a pasar la función JavaScript con la actividad que queremos reservar
        String script = "RPCv2.selectorpago('2671','2020-03-06','07:40:00', 'YOGA', 'DEPORTES', '', 'GABRIELLA ', 'SALA 1')";

        miVisorWeb.evaluateJavascript(script, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {
                System.out.println("Hola");
            }
        });*/


        miVisorWeb.getSettings().setJavaScriptEnabled(true);
        //miVisorWeb.setWebChromeClient(new WebChromeClient());
        miVisorWeb.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url){
                String javaScript ="javascript:RPCv2.selectorpago('2671','2020-03-06','07:40:00', 'YOGA', 'DEPORTES', '', 'GABRIELLA ', 'SALA 1')";
                // Para versiones más antiguas
                /*miVisorWeb.loadUrl(javaScript);*/
                // Para las últimas versiones de Android
                miVisorWeb.evaluateJavascript(javaScript, null);

            }

        });
        miVisorWeb.loadUrl(url);


    }

}
