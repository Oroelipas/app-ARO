package com.example.upnadeportes;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class InicioSesionWebActivity extends Activity {

    WebView miVisorWeb;
    int codigoActividad;
    String fechaActividad;
    String horaActividad;
    String nombreActividad;
    String centroActividad;
    String nomProfActividad;
    String recursoActividad;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    protected void onCreate(Bundle savedInstanceState) {
        // Llamamos al instanciador de la actividad
        super.onCreate(savedInstanceState);
        // Ponemos el diseño que queremos
        setContentView(R.layout.web_view);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        codigoActividad = extras.getInt("codigoActividad");
        fechaActividad = extras.getString("fechaActividad");
        horaActividad = extras.getString("horaActividad");
        nombreActividad = extras.getString("nombreActividad");
        centroActividad = extras.getString("centroActividad");
        nomProfActividad = extras.getString("nomProfActividad");
        recursoActividad = extras.getString("recursoActividad");



        // El link con la web de la UPNA deportes
        String inicioSesion = "https://extuniv.unavarra.es/actividades/login";
        String reservaActividades = "https://extuniv.unavarra.es/actividades/reservas/actividades";

        miVisorWeb = (WebView) findViewById(R.id.visorWeb);
        miVisorWeb.getSettings().setJavaScriptEnabled(true);


        miVisorWeb.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url){
                System.out.println("URL CARGADA: " + url);

                if(url.equals("https://extuniv.unavarra.es/actividades/")){
                    // Si ya estamos logeados entonces vamos a la lista de actividades
                    miVisorWeb.loadUrl(reservaActividades);
                }

                if(url.equals("https://extuniv.unavarra.es/actividades/reservas/actividades")){
                    String getHTMLjs = "(function() { return ('<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>'); })();";
                    miVisorWeb.evaluateJavascript(getHTMLjs, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String html) {
                            if(html.contains("Reserva de Plazas en las Actividades")){
                                System.out.println("ESTAMOS EN LA LISTA DE ACTIVIDADES!!!!!!!!!!!!!!");
                                String javaScript = "javascript:RPCv2.selectorpago('"+codigoActividad+"','"+fechaActividad+"','"+horaActividad+"', '"+nombreActividad+"', '"+centroActividad+"', '', '"+nomProfActividad+"', '"+recursoActividad+"')";
                                System.out.println(javaScript);
                                miVisorWeb.evaluateJavascript(javaScript, null);
                            }
                            if(html.contains("ERROR")){
                                System.out.println("NO SE PUEDE EJECUTAR LA RESERVA!!!!!!!!!!!");
                                return;
                            }
                            if(html.contains("Confirmar Reserva")){
                                System.out.println("ESTAMOS A PUNTO DE RESERVAR UNA ACTIVIDAD!!!!!!!!!!!");
                                String javaScript = "javascript:enlaceProceso('PasarelaZero')";
                                System.out.println(javaScript);
                                miVisorWeb.evaluateJavascript(javaScript, null);
                            }

                            if(html.contains("Proceso realizado con éxito")){
                                System.out.println("ACABAMOS DE RESERVAR UNA ACTIVIDAD!!!!!!!!!!!");
                                finish();

                            }
                        }
                    });
                }
            }
        });

        miVisorWeb.loadUrl(inicioSesion);

    }

    public void finInicioSesion(View view) {
        Intent intent = new Intent(getApplicationContext(), ReservaPlazaActivity.class);
        startActivity(intent);
    }

}
