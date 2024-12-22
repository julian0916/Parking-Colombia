package com.example.zer.somos.supervision;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zer.somos.R;
import com.example.zer.somos.comunicacion.IRespuestaHttpCliente;
import com.example.zer.somos.permisos.GlobalPermisos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.HttpResponseException;

import static com.example.zer.somos.comunes.BluetoothPrint.getContext;

public class ListarBloqueados extends AppCompatActivity implements IRespuestaHttpCliente {

    private LinearLayout linearLayout;
    private List<PromotorDTO> listaBloqueados;
    private Button salir;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_bloqueados);
        salir = findViewById(R.id.salir);
        listaBloqueados = new ArrayList<>();
        String cuenta = GlobalPermisos.getDatosSesionActual().getCuenta();
        SupervisionAPI supervisionAPI = new SupervisionAPI();
        supervisionAPI.listarBloqueados(this, this, SupervisionAPI.Acciones.LISTAR_BLOQUEADOS, cuenta);
        linearLayout = findViewById(R.id.linearLayoutBloqueados);
        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    public void respuestaJSON(String accion, int statusCode, Header[] headers, byte[] responseBytes, String responseString, JSONObject responseJSON, JSONArray responseArrayJSON) {
        try {

            if (responseArrayJSON != null) {
                List<PromotorDTO> listaBloqueados = new ArrayList<>();

                for (int i = 0; i < responseArrayJSON.length(); i++) {
                    JSONObject jsonObject = responseArrayJSON.getJSONObject(i);
                    Long id = jsonObject.getLong("id");
                    String nombre = jsonObject.getString("nombre");
                    String apellido = jsonObject.getString("apellido");
                    Timestamp horaBloqueo = null;

                    PromotorDTO promotor = new PromotorDTO(id, nombre, apellido,  horaBloqueo);
                    listaBloqueados.add(promotor);
                }

                // Luego de llenar la lista, puedes actualizar la UI
                // Por ejemplo, puedes llamar a un método en tu actividad o fragmento para mostrar los datos
                mostrarListaBloqueados(listaBloqueados);

            } else {
                Log.e("LISTAR_BLOQUEADOS", "El array JSON recibido es nulo.");
            }
        } catch (JSONException e) {
            Log.e("LISTAR_BLOQUEADOS", "Error al parsear JSON: " + e.getMessage());
        }
    }

    @Override
    public void errorJSON(String accion, int statusCode, Header[] headers, byte[] responseBytes, String responseString, JSONObject JSONResponse, JSONArray JSONArrayResponse, Throwable throwable) {
        if (SupervisionAPI.Acciones.LISTAR_BLOQUEADOS.equals(accion)) {
            if (responseString != null) {
                Log.e("LISTAR_BLOQUEADOS", "Error en la respuesta JSON: " + responseString, throwable);
            } else {
                Log.e("LISTAR_BLOQUEADOS", "Error en la respuesta JSON", throwable);
            }
        }

        if (throwable instanceof HttpResponseException) {
            HttpResponseException httpResponseException = (HttpResponseException) throwable;
            int statusCodes = httpResponseException.getStatusCode();
            String reasonPhrase = httpResponseException.getMessage();
            Log.e("HTTP_ERROR", "Error HTTP: " + statusCodes + " - " + reasonPhrase);

        }
    }
    private void mostrarDialogoConfirmacion(Long idPromotor) {
        new AlertDialog.Builder(this)
                .setTitle("Confirmar Desbloqueo")
                .setMessage("¿Estás seguro de que quieres desbloquear este promotor?")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                  SupervisionAPI supervisionAPI = new SupervisionAPI();
                   supervisionAPI.desbloquearPromotor(this, this, SupervisionAPI.Acciones.DESBLOQUEAR_PROMOTOR, GlobalPermisos.getDatosSesionActual().getCuenta(), idPromotor, GlobalPermisos.getDatosSesionActual().getIdPromotor());
                    finish();
                })
                .setNegativeButton(android.R.string.no, null)
                .show();
    }
    private void mostrarListaBloqueados(List<PromotorDTO> listaBloqueados) {
        linearLayout.removeAllViews();

        for (PromotorDTO promotor : listaBloqueados) {
            View view = crearVistaPromotor(promotor);
            linearLayout.addView(view);
        }
    }


    private View crearVistaPromotor(PromotorDTO promotor) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        layout.setPadding(8, 8, 8, 8);

        TextView textViewNombre = new TextView(this);
        textViewNombre.setText(promotor.getNombre() + " " + promotor.getApellido());
        textViewNombre.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                1));

        Button buttonDesbloquear = new Button(this);
        buttonDesbloquear.setText("Desbloquear");
        buttonDesbloquear.setOnClickListener(v -> {
            mostrarDialogoConfirmacion(promotor.getId());
        });

        layout.addView(textViewNombre);
        layout.addView(buttonDesbloquear);

        return layout;
    }


    @Override
    public void termino() {

    }
}