package com.example.zer.somos.utilidades.qrCodeScanner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.zer.somos.R;
import com.google.zxing.ResultPoint;
import com.journeyapps.barcodescanner.BarcodeCallback;
import com.journeyapps.barcodescanner.BarcodeResult;
import com.journeyapps.barcodescanner.BarcodeView;

import java.util.List;

public class QRCodeScanDialogFragment extends DialogFragment {

    public interface QRCodeScanListener {
        void onQRCodeScanned(String result);
    }

    private BarcodeView barcodeView;
    private QRCodeScanListener listener;

    public QRCodeScanDialogFragment() {
        // Constructor vacío requerido
    }

    public static QRCodeScanDialogFragment newInstance(QRCodeScanListener listener) {
        QRCodeScanDialogFragment fragment = new QRCodeScanDialogFragment();
        fragment.setQRCodeScanListener(listener);
        return fragment;
    }

    public void setQRCodeScanListener(QRCodeScanListener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_q_r_code_dialog, container, false);
        barcodeView = view.findViewById(R.id.barcode_scanner);
        startScanning();
        return view;
    }

    private void startScanning() {
        barcodeView.decodeContinuous(new BarcodeCallback() {
            @Override
            public void barcodeResult(BarcodeResult result) {
                if (listener != null) {
                    listener.onQRCodeScanned(result.getText());
                }
                dismiss(); // Cerrar el dialog una vez se escanee un código
            }

            @Override
            public void possibleResultPoints(List<ResultPoint> resultPoints) {
                // No es necesario manejar esto para la mayoría de los casos
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        barcodeView.pause(); // Pausar la cámara cuando el fragmento está en pausa
    }

    @Override
    public void onResume() {
        super.onResume();
        barcodeView.resume(); // Reanudar la cámara cuando el fragmento está activo nuevamente
    }
}