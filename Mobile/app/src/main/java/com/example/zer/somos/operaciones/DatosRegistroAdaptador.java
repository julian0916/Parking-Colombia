package com.example.zer.somos.operaciones;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import androidx.core.content.ContextCompat;
import com.example.zer.somos.R;
import com.example.zer.somos.comunes.EstadosRegistro;
import com.example.zer.somos.utilidades.Configuracion;

import java.util.ArrayList;
import java.util.List;

public class DatosRegistroAdaptador extends BaseAdapter {

    private List<DatosRegistro> registros;
    private Context context;
    private IOperacionDatosRegistro iOperacionDatosRegistro;
    private IOperacionDatosRegistroPrepago iOperacionDatosRegistroPrepago;
    private IOperacionDuplicados iOperacionDuplicados;

    public DatosRegistroAdaptador(
            List<DatosRegistro> registros,
            Context context,
            IOperacionDuplicados iOperacionDuplicados) {
        if (registros == null) {
            registros = new ArrayList<>();
        }
        this.registros = registros;
        this.context = context;
        this.iOperacionDuplicados = iOperacionDuplicados;
    }

    public DatosRegistroAdaptador(
            List<DatosRegistro> registros,
            Context context,
            IOperacionDatosRegistro iOperacionDatosRegistro) {
        if (registros == null) {
            registros = new ArrayList<>();
        }
        this.registros = registros;
        this.context = context;
        this.iOperacionDatosRegistro = iOperacionDatosRegistro;
    }

    public DatosRegistroAdaptador(
            List<DatosRegistro> registros,
            Context context,
            IOperacionDatosRegistroPrepago iOperacionDatosRegistroPrepago) {
        if (registros == null) {
            registros = new ArrayList<>();
        }
        this.registros = registros;
        this.context = context;
        this.iOperacionDatosRegistroPrepago = iOperacionDatosRegistroPrepago;
    }

    //----------------------------------
    //  Procesa prepago y postpago
    //-----------------------------------
    public DatosRegistroAdaptador(
            List<DatosRegistro> registros,
            Context context,
            IOperacionDatosRegistroPrepago iOperacionDatosRegistroPrepago,
            IOperacionDatosRegistro iOperacionDatosRegistro) {
        if (registros == null) {
            registros = new ArrayList<>();
        }
        this.registros = registros;
        this.context = context;
        this.iOperacionDatosRegistroPrepago = iOperacionDatosRegistroPrepago;
        this.iOperacionDatosRegistro = iOperacionDatosRegistro;
    }


    @Override
    public int getCount() {
        return registros.size();
    }

    public void clear() {
        registros.clear();
    }

    public void addAll(ArrayList<DatosRegistro> datosRegistros) {
        for (int i = 0; i < datosRegistros.size(); i++) {
            registros.add(datosRegistros.get(i));
        }
    }

    public void notifyDataSetChanged(int i){
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int i) {
        return registros.get(i);
    }

    @Override
    public long getItemId(int i) {
        return registros.get(i).getId();
    }

    public View getViewReportarPagar(int i, View view, ViewGroup viewGroup) {
        View v = view;

        try {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.lista_reportar_pagar, null);
        } catch (Exception ex) {
        }

        DatosRegistro datosRegistro = registros.get(i);

        TextView estado = v.findViewById(R.id.estado);
        String textoEstado = datosRegistro.getEstado().equals(EstadosRegistro.PREPAGO) ? EstadosRegistro.T_PREPAGO : EstadosRegistro.T_ABIERTA;
        estado.setText(textoEstado);

        TextView placa = v.findViewById(R.id.placa);
        placa.setText(datosRegistro.getPlaca());

        TextView id = v.findViewById(R.id.id);
        id.setText(datosRegistro.getId().toString());

        TextView entrada = v.findViewById(R.id.entrada);
        entrada.setText(Configuracion.getFechaHora(datosRegistro.getIngreso()));

        TextView promotor = v.findViewById(R.id.promotor);
        promotor.setText(datosRegistro.getPromotorIngreso());

        TextView zona = v.findViewById(R.id.zona);
        zona.setText(datosRegistro.getNombreZona());

        TextView horas = v.findViewById(R.id.horas);
        horas.setText(datosRegistro.getHorasCobradas().toString());

        TextView valor = v.findViewById(R.id.valor);
        valor.setText("$" + datosRegistro.getValorCobro().toString());

        Button logoReportar = v.findViewById(R.id.logo_reportar);
        logoReportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iOperacionDatosRegistro != null) {
                    iOperacionDatosRegistro.botonReportarVehiculo(datosRegistro);
                }
            }
        });

        Button reportar = v.findViewById(R.id.reportar);
        reportar.setText(datosRegistro.getPlaca() + " Reportar");
        reportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iOperacionDatosRegistro != null) {
                    iOperacionDatosRegistro.botonReportarVehiculo(datosRegistro);
                }
            }
        });


        Button logoPagar = v.findViewById(R.id.logo_pagar);
        logoPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iOperacionDatosRegistro != null) {
                    iOperacionDatosRegistro.botonConfirmarPagoVehiculo(datosRegistro);
                }
            }
        });

        Button pagar = v.findViewById(R.id.pagar);
        pagar.setText(datosRegistro.getPlaca() + " Pagar");
        pagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iOperacionDatosRegistro != null) {
                    iOperacionDatosRegistro.botonConfirmarPagoVehiculo(datosRegistro);
                }
            }
        });

        return v;
    }

    public View getViewPagoExtemporaneo(int i, View view, ViewGroup viewGroup) {
        View v = view;

        try {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.lista_pagar, null);
        } catch (Exception ex) {
        }

        DatosRegistro datosRegistro = registros.get(i);

        TextView estado = v.findViewById(R.id.estado);
        estado.setText(EstadosRegistro.getEstadoNombre(datosRegistro.getEstado()));

        TextView placa = v.findViewById(R.id.placa);
        placa.setText(datosRegistro.getPlaca());

        TextView id = v.findViewById(R.id.id);
        id.setText(datosRegistro.getId().toString());

        TextView entrada = v.findViewById(R.id.entrada);
        entrada.setText("");
        try {
            entrada.setText(Configuracion.getFechaHora(datosRegistro.getIngreso()));
        } catch (Exception ex) {
        }

        TextView salida = v.findViewById(R.id.salida);
        salida.setText("");
        try {
            salida.setText(Configuracion.getFechaHora(datosRegistro.getEgreso()));
        } catch (Exception ex) {
        }

        TextView promotor = v.findViewById(R.id.promotor);
        promotor.setText(datosRegistro.getPromotorIngreso());

        TextView zona = v.findViewById(R.id.zona);
        zona.setText(datosRegistro.getNombreZona());

        TextView horas = v.findViewById(R.id.horas);
        horas.setText(datosRegistro.getHorasCobradas().toString());

        TextView valor = v.findViewById(R.id.valor);
        valor.setText(datosRegistro.getValorCobro().toString());

        Button logoPagar = v.findViewById(R.id.logo_pagar);
        logoPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iOperacionDatosRegistro != null) {
                    iOperacionDatosRegistro.botonPagoExtemporaneoVehiculo(datosRegistro);
                }
            }
        });
        Button pagar = v.findViewById(R.id.pagar);
        pagar.setText(datosRegistro.getPlaca() + " Pagar");
        pagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iOperacionDatosRegistro != null) {
                    iOperacionDatosRegistro.botonPagoExtemporaneoVehiculo(datosRegistro);
                }
            }
        });

        return v;
    }

    public View getViewPrepago(int i, View view, ViewGroup viewGroup) {
        View v = view;

        try {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.lista_alertas_prepago, null);
        } catch (Exception ex) {
        }

        DatosRegistro datosRegistro = registros.get(i);

        TextView estado = v.findViewById(R.id.estado);
        estado.setText(EstadosRegistro.getEstadoNombre(datosRegistro.getEstado()));

        TextView placa = v.findViewById(R.id.placa);
        placa.setText(datosRegistro.getPlaca());

        TextView id = v.findViewById(R.id.id);
        id.setText(datosRegistro.getId().toString());

        TextView entrada = v.findViewById(R.id.entrada);
        entrada.setText("");
        try {
            entrada.setText(Configuracion.getFechaHora(datosRegistro.getIngreso()));
        } catch (Exception ex) {
        }

        TextView salida = v.findViewById(R.id.salida);
        try {
            salida.setText("");
            salida.setText(Configuracion.getFechaHora(datosRegistro.getEgreso()));
        } catch (Exception ex) {
        }

        TextView promotor = v.findViewById(R.id.promotor);
        promotor.setText(datosRegistro.getPromotorIngreso());

        TextView zona = v.findViewById(R.id.zona);
        zona.setText(datosRegistro.getNombreZona());

        TextView horas = v.findViewById(R.id.horas);
        horas.setText(datosRegistro.getHorasCobradas().toString());

        try {
            Button logoIniciarPostpago = v.findViewById(R.id.logo_iniciarPostpago);
            logoIniciarPostpago.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (iOperacionDatosRegistroPrepago != null) {
                        iOperacionDatosRegistroPrepago.botonIniciarPostpago(datosRegistro);
                    }
                }
            });

            Button iniciarPostpago = v.findViewById(R.id.iniciarPostpago);
            iniciarPostpago.setText(datosRegistro.getPlaca() + " Postpago");
            iniciarPostpago.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (iOperacionDatosRegistroPrepago != null) {
                        iOperacionDatosRegistroPrepago.botonIniciarPostpago(datosRegistro);
                    }
                }
            });
        } catch (Exception ex) {
        }

        try {
            Button logoActualizarCupoPrepago = v.findViewById(R.id.logo_actualizarCupoPrepago);
            logoActualizarCupoPrepago.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (iOperacionDatosRegistroPrepago != null) {
                        iOperacionDatosRegistroPrepago.botonReportarSalio(datosRegistro);
                    }
                }
            });

            Button actualizarCupoPrepago = v.findViewById(R.id.actualizarCupoPrepago);
            actualizarCupoPrepago.setText(datosRegistro.getPlaca() + " Salio");
            actualizarCupoPrepago.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (iOperacionDatosRegistroPrepago != null) {
                        iOperacionDatosRegistroPrepago.botonReportarSalio(datosRegistro);
                    }
                }
            });
        } catch (Exception ex) {

        }

        return v;
    }

    public View getViewDuplicadosIngreso(int i, View view, ViewGroup viewGroup) {
        View v = view;

        DatosRegistro datosRegistro = registros.get(i);

        try {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.lista_duplicados_ingreso, null);
        } catch (Exception ex) {
        }

        TextView placa = v.findViewById(R.id.placa);
        placa.setText(datosRegistro.getPlaca());

        TextView estado = v.findViewById(R.id.estado);
        estado.setText(EstadosRegistro.getEstadoNombre(datosRegistro.getEstado()));

        TextView id = v.findViewById(R.id.id);
        id.setText(datosRegistro.getId().toString());

        TextView entrada = v.findViewById(R.id.entrada);
        entrada.setText("");
        try {
            entrada.setText(Configuracion.getFechaHora(datosRegistro.getIngreso()));
        } catch (Exception ex) {
        }

        TextView promotor = v.findViewById(R.id.promotor);
        promotor.setText(datosRegistro.getPromotorIngreso());

        TextView zona = v.findViewById(R.id.zona);
        zona.setText(datosRegistro.getNombreZona());

        Button logoPagar = v.findViewById(R.id.logo_pagar);
        logoPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iOperacionDuplicados != null) {
                    iOperacionDuplicados.botonReimprimirIngreso(datosRegistro);
                }
            }
        });

        Button pagar = v.findViewById(R.id.pagar);
        pagar.setText(datosRegistro.getPlaca() + " Entrada");
        //pagar.setBackground(ContextCompat.getDrawable(context, R.drawable.boton_redondeado_verde));
        pagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iOperacionDuplicados != null) {
                    iOperacionDuplicados.botonReimprimirIngreso(datosRegistro);
                }
            }
        });

        return v;
    }

    public View getViewDuplicadosIngresoEgreso(int i, View view, ViewGroup viewGroup) {
        View v = view;

        DatosRegistro datosRegistro = registros.get(i);

        try {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.lista_duplicados_egreso, null);
        } catch (Exception ex) {
        }

        TextView estado = v.findViewById(R.id.estado);
        estado.setText(EstadosRegistro.getEstadoNombre(datosRegistro.getEstado()));

        TextView placa = v.findViewById(R.id.placa);
        placa.setText(datosRegistro.getPlaca());

        TextView id = v.findViewById(R.id.id);
        id.setText(datosRegistro.getId().toString());

        TextView entrada = v.findViewById(R.id.entrada);
        entrada.setText("");
        try {
            entrada.setText(Configuracion.getFechaHora(datosRegistro.getIngreso()));
        } catch (Exception ex) {
        }

        TextView salida = v.findViewById(R.id.salida);
        salida.setText("");
        try {
            salida.setText(Configuracion.getFechaHora(datosRegistro.getEgreso()));
        } catch (Exception ex) {
        }

        TextView promotor = v.findViewById(R.id.promotor);
        promotor.setText(datosRegistro.getPromotorIngreso());

        TextView zona = v.findViewById(R.id.zona);
        zona.setText(datosRegistro.getNombreZona());

        TextView horas = v.findViewById(R.id.horas);
        horas.setText(datosRegistro.getHorasCobradas().toString());

        TextView valor = v.findViewById(R.id.valor);
        valor.setText("$" + datosRegistro.getValorCobro());

        Button logoReportar = v.findViewById(R.id.logo_reportar);
        logoReportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iOperacionDuplicados != null) {
                    iOperacionDuplicados.botonReimprimirIngreso(datosRegistro);
                }
            }
        });

        Button reportar = v.findViewById(R.id.reportar);
        reportar.setText(datosRegistro.getPlaca() + " Entrada");
        //reportar.setBackground(ContextCompat.getDrawable(context, R.drawable.boton_redondeado_verde));
        reportar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iOperacionDuplicados != null) {
                    iOperacionDuplicados.botonReimprimirIngreso(datosRegistro);
                }
            }
        });

        Button logoPagar = v.findViewById(R.id.logo_pagar);
        logoPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iOperacionDuplicados != null) {
                    iOperacionDuplicados.botonReimprimirEgreso(datosRegistro);
                }
            }
        });

        Button pagar = v.findViewById(R.id.pagar);
        pagar.setText(datosRegistro.getPlaca() + " Salida");
        //pagar.setBackground(ContextCompat.getDrawable(context, R.drawable.boton_redondeado_azul));
        pagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iOperacionDuplicados != null) {
                    iOperacionDuplicados.botonReimprimirEgreso(datosRegistro);
                }
            }
        });


        return v;
    }

    public View getViewDuplicadosPrepago(int i, View view, ViewGroup viewGroup) {
        View v = view;

        DatosRegistro datosRegistro = registros.get(i);

        try {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.lista_pagar, null);
        } catch (Exception ex) {
        }

        TextView estado = v.findViewById(R.id.estado);
        estado.setText(EstadosRegistro.getEstadoNombre(datosRegistro.getEstado()));

        TextView placa = v.findViewById(R.id.placa);
        placa.setText(datosRegistro.getPlaca());

        TextView id = v.findViewById(R.id.id);
        id.setText(datosRegistro.getId().toString());

        TextView entrada = v.findViewById(R.id.entrada);
        entrada.setText("");
        try {
            entrada.setText(Configuracion.getFechaHora(datosRegistro.getIngreso()));
        } catch (Exception ex) {
        }

        TextView salida = v.findViewById(R.id.salida);
        salida.setText("");
        try {
            salida.setText(Configuracion.getFechaHora(datosRegistro.getEgreso()));
        } catch (Exception ex) {
        }

        TextView promotor = v.findViewById(R.id.promotor);
        promotor.setText(datosRegistro.getPromotorIngreso());

        TextView zona = v.findViewById(R.id.zona);
        zona.setText(datosRegistro.getNombreZona());

        TextView horas = v.findViewById(R.id.horas);
        horas.setText(datosRegistro.getHorasCobradas().toString());

        TextView valor = v.findViewById(R.id.valor);
        valor.setText("$" + datosRegistro.getValorCobro());

        Button logo_pagar = v.findViewById(R.id.logo_pagar);
        logo_pagar.setBackground(ContextCompat.getDrawable(context, R.drawable.logo_duplicado));

        Button logoPagar = v.findViewById(R.id.logo_pagar);
        logoPagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iOperacionDuplicados != null) {
                    iOperacionDuplicados.botonReimprimirPrepago(datosRegistro);
                }
            }
        });

        Button pagar = v.findViewById(R.id.pagar);
        pagar.setText(datosRegistro.getPlaca() + " Prepago");
        pagar.setBackground(ContextCompat.getDrawable(context, R.drawable.boton_redondeado_azul_claro));
        pagar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (iOperacionDuplicados != null) {
                    iOperacionDuplicados.botonReimprimirPrepago(datosRegistro);
                }
            }
        });


        return v;
    }

    public View getViewDuplicados(int i, View view, ViewGroup viewGroup) {
        DatosRegistro datosRegistro = registros.get(i);
        if (datosRegistro.getEstado().equals(EstadosRegistro.ABIERTA)) {
            return getViewDuplicadosIngreso(i, view, viewGroup);
        }
        if (datosRegistro.getEstado().equals(EstadosRegistro.PREPAGO)) {
            return getViewDuplicadosPrepago(i, view, viewGroup);
        }
        return getViewDuplicadosIngresoEgreso(i, view, viewGroup);
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        notifyDataSetChanged(i);

        if (iOperacionDuplicados != null) {
            return getViewDuplicados(i, view, viewGroup);
        }

        DatosRegistro datosRegistro = registros.get(i);
        long estado = datosRegistro.getEstado();
        if (estado == EstadosRegistro.SALIO_REPORTADO) {
            return getViewPagoExtemporaneo(i, view, viewGroup);
        }

        if (estado == EstadosRegistro.PREPAGO) {
            return getViewPrepago(i, view, viewGroup);
        }

        return getViewReportarPagar(i, view, viewGroup);

    }
}
