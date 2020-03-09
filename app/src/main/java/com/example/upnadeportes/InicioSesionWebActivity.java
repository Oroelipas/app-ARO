package com.example.upnadeportes;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
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
        String inicioSesion = "https://extuniv.unavarra.es/actividades/login";
        String reserva = "https://extuniv.unavarra.es/actividades/reservas/actividades";

        miVisorWeb = (WebView) findViewById(R.id.visorWeb);
        miVisorWeb.getSettings().setJavaScriptEnabled(true);

        // Limpiamos el historial para que tenga que iniciar sesión
        miVisorWeb.clearHistory();

        miVisorWeb.setWebViewClient(new WebViewClient() {
            int i = 0;
            @Override
            public void onPageFinished(WebView view, String url){
                if (i == 0) {
                    miVisorWeb.loadUrl(reserva);
                    String javaScript = "javascript:RPCv2.selectorpago('2665','2020-03-11','16:00:00', 'SPINNING', 'DEPORTES', '', 'JAVIER', 'BICICLETAS')";
                    // Para las últimas versiones de Android
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        miVisorWeb.loadUrl(reserva);
                        miVisorWeb.evaluateJavascript(javaScript, null);
                    }
                }
                if (i == 1) {
                    miVisorWeb.loadUrl(reserva);
                    String javaScript = "javascript:RPCv2.selectorpago('2665','2020-03-11','16:00:00', 'SPINNING', 'DEPORTES', '', 'JAVIER', 'BICICLETAS')";
                    // Para versiones más antiguas
                    //miVisorWeb.loadUrl(javaScript);
                    // Para las últimas versiones de Android
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        miVisorWeb.loadUrl(reserva);
                        miVisorWeb.evaluateJavascript(javaScript, null);
                    }
                }
                if (i == 2) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        String javaScript = "javascript:RPCv2.selectorpago('2665','2020-03-11','16:00:00', 'SPINNING', 'DEPORTES', '', 'JAVIER', 'BICICLETAS')";
                        miVisorWeb.evaluateJavascript(javaScript, null);
                    }
                }

                i++;
            }


        });

        miVisorWeb.loadUrl(inicioSesion);

        /*
        // Cogemos el visor web
        miVisorWeb = (WebView) findViewById(R.id.visorWeb);
        // Y los ajutes del visor
        final WebSettings ajustesVisorWeb = miVisorWeb.getSettings();

        // Le ponemos el visor que queremos usar, para que no nos salte al navegador
        // web predeterminado del Android
        WebViewClient clienteWeb = new WebViewClient() {
            int i = 0;
                public void onPageFinished(WebView view, String url) {
                    System.out.println("Hola a todos mis amigos");
                    if (i == 1) {
                        miVisorWeb.loadUrl("https://extuniv.unavarra.es/actividades/reservas/actividades");
                        System.out.println("Vale, entramos");
                        String javaScript = "javascript:RPCv2.selectorpago('2671','2020-03-06','07:40:00', 'YOGA', 'DEPORTES', '', 'GABRIELLA ', 'SALA 1')";
                        // Para versiones más antiguas
                        miVisorWeb.loadUrl(javaScript);
                        // Para las últimas versiones de Android
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                            miVisorWeb.evaluateJavascript(javaScript, null);
                        } else {
                            miVisorWeb.loadUrl(javaScript);
                        }
                        miVisorWeb.loadUrl(javaScript);
                    }
                    i++;
                }
        };
        miVisorWeb.setWebViewClient(clienteWeb);
        // Cargamos la URL que queremos
        miVisorWeb.loadUrl(url);
        // Activamos el JavaScript, lo necesitamos porque la web lo usa
        ajustesVisorWeb.setJavaScriptEnabled(true);
        */





    }

    public void finInicioSesion(View view) {
        Intent intent = new Intent(getApplicationContext(), ReservaPlazaActivity.class);
        startActivity(intent);
    }

}
