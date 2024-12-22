package com.example.zer.somos.operaciones;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.zer.somos.MainActivity;
import com.example.zer.somos.R;
import com.example.zer.somos.comunes.DatabaseHelper;
import com.example.zer.somos.utilidades.ImpresoraPos;
import com.example.zer.somos.utilidades.qrCodeScanner.ScanCaptureAct;

import java.util.ArrayList;
import java.util.List;

public class SelectScanner extends AppCompatActivity {

    private int contador = 0;


    private static final int PERMISSION_REQUEST_CODE = 1;
    private Drawable img_permission_approval;
    private Drawable img_permission_denied;
    private final String[] permissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.CAMERA ,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.SEND_SMS,
            Manifest.permission.READ_PHONE_STATE,
    };
    private Button openCameraButton;
    private Button openScannerButton;
    private Button checkPermissionButton;
    private ImpresoraPos impresoraPos;
    private static DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_scanner);
        iniciarComponentes();
        programarEventos();
        checkPermissions();
        impresoraPos = new ImpresoraPos(this); // Inicializar la impresora
        dbHelper = new DatabaseHelper(this);
    }

    public static DatabaseHelper getDatabaseHelper() {
        return dbHelper;
    }


    // Método para verificar si se tienen todos los permisos necesarios
    private void checkPermissions() {
        boolean allPermissionsGranted = true;
        checkPermissionButton.setBackground(img_permission_approval);
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                allPermissionsGranted = false;
                checkPermissionButton.setBackground(img_permission_denied);
                break;
            }
        }
        if (!allPermissionsGranted) {
            // Solicitar los permisos restantes
            requestPermissions();
        } else {
            // Si todos los permisos están concedidos, imprimir
            imprimir();
        }
    }

    // Método para solicitar los permisos necesarios
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
    }

    // Método para manejar la respuesta del usuario a la solicitud de permisos
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            List<String> deniedPermissions = new ArrayList<>();
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                    // Agregar el nombre del permiso denegado al array de permisos denegados
                    deniedPermissions.add(permissions[i]);
                }
            }

            if (!deniedPermissions.isEmpty()) {
                // Solicitar los permisos restantes
                String[] remainingPermissions = deniedPermissions.toArray(new String[0]);
                ActivityCompat.requestPermissions(this, remainingPermissions, PERMISSION_REQUEST_CODE);
            } else {
                // Si todos los permisos están concedidos, imprimir
                imprimir();
            }
        }
    }

    // Método para imprimir
    private void imprimir() {
        contador++;

        if (impresoraPos != null) {

            // Conectar con la impresora
            impresoraPos.conectar();

            // Verificar si la conexión se estableció correctamente
            if (impresoraPos.isConnected()) {
                // Imprimir el texto
                impresoraPos.printQrCode(impresoraPos.generarCodigoQR("\n"+"\n"+"Intentos de impresion: "+ contador+"\n"+"\n"));
                try {
                    Thread.sleep(2000); // Espera 2 segundos
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // Desconectar la impresora después de imprimir
                impresoraPos.desconectar();
            } else {
                Toast.makeText(this, "No se pudo conectar con la impresora.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "La instancia de la impresora es nula.", Toast.LENGTH_SHORT).show();
        }
    }
    protected void iniciarComponentes() {
        try {
            openCameraButton = findViewById(R.id.openCameraButton);
            openScannerButton = findViewById(R.id.openScannerButton);
            checkPermissionButton = findViewById(R.id.checkPermissionButton);
            img_permission_approval = ContextCompat.getDrawable(this, R.drawable.permission_approval);
            img_permission_denied = ContextCompat.getDrawable(this, R.drawable.permission_denied);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void programarEventos() {
        try {
            openCameraButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    finish();
                    cameraActivity();
                }
            });
            openScannerButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    finish();
                    scannerActivity();
                }
            });

            checkPermissionButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    // Verificar los permisos cuando se haga clic en el botón
                    checkPermissions();
                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void cameraActivity() {
        Intent intent = new Intent(this, ScanCaptureAct.class);
        startActivity(intent);
    }

    protected void scannerActivity(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}