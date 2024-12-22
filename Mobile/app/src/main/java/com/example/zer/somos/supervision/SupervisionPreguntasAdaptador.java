package com.example.zer.somos.supervision;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.zer.somos.R;

import java.util.ArrayList;
import java.util.List;

public class SupervisionPreguntasAdaptador extends BaseAdapter {

    private List<Pregunta> preguntas;
    private Context context;
    private ISupervisionPreguntas iSupervisionPreguntas;

    public SupervisionPreguntasAdaptador(
            List<Pregunta> preguntas,
            Context context,
            ISupervisionPreguntas iSupervisionPreguntas) {
        if (preguntas == null) {
            preguntas = new ArrayList<>();
        }
        this.preguntas = preguntas;
        this.context = context;
        this.iSupervisionPreguntas = iSupervisionPreguntas;
    }


    @Override
    public int getCount() {
        return preguntas.size();
    }

    public void clear() {
        preguntas.clear();
    }

    public void addAll(ArrayList<Pregunta> preguntas) {
        for (int i = 0; i < preguntas.size(); i++) {
            preguntas.add(preguntas.get(i));
        }
    }

    @Override
    public Object getItem(int i) {
        return preguntas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return preguntas.get(i).getId();
    }

    public View getViewSupervisionPreguntas(int i, View view, ViewGroup viewGroup) {
        View v = view;

        if (view == null) {
            LayoutInflater inf = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inf.inflate(R.layout.lista_supervision_pregunta, null);
        }

        Pregunta pregunta = preguntas.get(i);

        TextView contenidoPregunta = v.findViewById(R.id.pregunta);
        contenidoPregunta.setText(pregunta.getPregunta());

        EditText valor = v.findViewById(R.id.valor);
        //valor.setText(pregunta.getValor());

        CheckBox cumple = v.findViewById(R.id.cumple);
        try {
            cumple.setChecked(pregunta.getCumple());
        }catch(Exception ex){}

        //eventos
        contenidoPregunta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean actual = !cumple.isChecked();
                cumple.setChecked(actual);
                notifyDataSetChanged();
            }
        });

        cumple.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                pregunta.setCumple(b);
                iSupervisionPreguntas.cambioPregunta(pregunta,i);
            }
        });

        valor.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                pregunta.setValor(s.toString());
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        return v;
    }


    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return getViewSupervisionPreguntas(i, view, viewGroup);
    }
}
