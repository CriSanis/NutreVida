package com.nutrevida.app;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textfield.TextInputEditText;
import java.text.DecimalFormat;

public class CalculadoraIMCActivity extends AppCompatActivity {

    private TextInputEditText etPeso, etAltura;
    private MaterialButton btnCalcular;
    private ImageButton btnVolver;
    private TextView tvResultadoIMC, tvCategoria, tvRecomendacion;
    private MaterialCardView cardResultado;
    private DecimalFormat df = new DecimalFormat("#.##");
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculadora_imc);

        databaseHelper = new DatabaseHelper(this);
        initializeViews();
        setupClickListeners();
    }

    private void initializeViews() {
        etPeso = findViewById(R.id.etPeso);
        etAltura = findViewById(R.id.etAltura);
        btnCalcular = findViewById(R.id.btnCalcular);
        btnVolver = findViewById(R.id.btnVolver);
        tvResultadoIMC = findViewById(R.id.tvResultadoIMC);
        tvCategoria = findViewById(R.id.tvCategoria);
        tvRecomendacion = findViewById(R.id.tvRecomendacion);
        cardResultado = findViewById(R.id.cardResultado);
    }

    private void setupClickListeners() {
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnCalcular.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calcularIMC();
            }
        });
    }

    private void calcularIMC() {
        String pesoStr = etPeso.getText().toString().trim();
        String alturaStr = etAltura.getText().toString().trim();

        if (pesoStr.isEmpty()) {
            etPeso.setError("Ingresa tu peso");
            etPeso.requestFocus();
            return;
        }

        if (alturaStr.isEmpty()) {
            etAltura.setError("Ingresa tu altura");
            etAltura.requestFocus();
            return;
        }

        try {
            double peso = Double.parseDouble(pesoStr);
            double altura = Double.parseDouble(alturaStr);

            if (peso <= 0 || peso > 500) {
                etPeso.setError("Peso debe estar entre 1 y 500 kg");
                etPeso.requestFocus();
                return;
            }

            if (altura <= 0 || altura > 300) {
                etAltura.setError("Altura debe estar entre 1 y 300 cm");
                etAltura.requestFocus();
                return;
            }

            double alturaMetros = altura / 100.0;
            double imc = peso / (alturaMetros * alturaMetros);

            // Determinar categoría
            String categoria = determinarCategoria(imc);

            // Guardar en la base de datos
            long id = databaseHelper.insertarRegistroIMC(peso, altura, imc, categoria);

            if (id != -1) {
                Toast.makeText(this, "Registro guardado correctamente", Toast.LENGTH_SHORT).show();
            }

            mostrarResultado(imc, categoria);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Por favor ingresa valores numéricos válidos", Toast.LENGTH_SHORT).show();
        }
    }

    private String determinarCategoria(double imc) {
        if (imc < 18.5) {
            return "Bajo Peso";
        } else if (imc >= 18.5 && imc < 25.0) {
            return "Peso Normal";
        } else if (imc >= 25.0 && imc < 30.0) {
            return "Sobrepeso";
        } else {
            return "Obesidad";
        }
    }

    private void mostrarResultado(double imc, String categoria) {
        cardResultado.setVisibility(View.VISIBLE);
        tvResultadoIMC.setText(df.format(imc));

        String recomendacion;
        int colorCategoria;

        switch (categoria) {
            case "Bajo Peso":
                recomendacion = "Tu IMC indica bajo peso. Es recomendable consultar con un profesional de la salud para evaluar tu situación nutricional y desarrollar un plan para alcanzar un peso saludable.";
                colorCategoria = getResources().getColor(android.R.color.holo_blue_bright);
                break;
            case "Peso Normal":
                recomendacion = "¡Excelente! Tu peso está dentro del rango saludable. Mantén una dieta equilibrada y ejercicio regular para conservar tu salud óptima.";
                colorCategoria = getResources().getColor(android.R.color.holo_green_dark);
                break;
            case "Sobrepeso":
                recomendacion = "Tu IMC indica sobrepeso. Considera adoptar hábitos más saludables como una dieta balanceada y ejercicio regular. Consulta con un profesional si necesitas orientación.";
                colorCategoria = getResources().getColor(android.R.color.holo_orange_dark);
                break;
            default: // Obesidad
                recomendacion = "Tu IMC indica obesidad. Es importante que consultes con un profesional de la salud para desarrollar un plan personalizado que te ayude a mejorar tu bienestar.";
                colorCategoria = getResources().getColor(android.R.color.holo_red_dark);
                break;
        }

        tvCategoria.setText(categoria);
        tvRecomendacion.setText(recomendacion);
        tvCategoria.setTextColor(colorCategoria);
        tvResultadoIMC.setTextColor(colorCategoria);

        cardResultado.post(new Runnable() {
            @Override
            public void run() {
                cardResultado.requestFocus();
            }
        });
    }
}