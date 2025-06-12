package com.nutrevida.app;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nutrevida.app.modelos.RegistroIMC;

public class ResumenActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private TextView tvFecha, tvIMC, tvCalorias, tvAgua, tvEjercicios;
    private ImageButton btnVolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen);

        databaseHelper = new DatabaseHelper(this);
        inicializarVistas();
        cargarResumen();
    }

    private void inicializarVistas() {
        tvFecha = findViewById(R.id.tvFechaResumen);
        tvIMC = findViewById(R.id.tvResumenIMC);
        tvCalorias = findViewById(R.id.tvResumenCalorias);
        tvAgua = findViewById(R.id.tvResumenAgua);
        tvEjercicios = findViewById(R.id.tvResumenEjercicios);
        btnVolver = findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(v -> finish());
    }

    private void cargarResumen() {
        try {
            String fechaActual = databaseHelper.getCurrentDate();
            tvFecha.setText(fechaActual);

            // IMC
            RegistroIMC registro = databaseHelper.obtenerUltimoIMC(fechaActual);
            tvIMC.setText(registro != null
                    ? String.format("IMC: %.1f (%s)", registro.getImc(), registro.getResultado())
                    : "IMC: No registrado");

            // Calorías consumidas
            double calorias = databaseHelper.obtenerTotalCaloriasConsumidas(fechaActual);
            tvCalorias.setText(String.format("Calorías consumidas: %.0f kcal", calorias));

            // Agua consumida
            double agua = databaseHelper.obtenerTotalAgua(fechaActual);
            tvAgua.setText(String.format("Agua consumida: %.2f L", agua / 1000));

            // Calorías quemadas por ejercicio
            double ejercicios = databaseHelper.obtenerTotalCaloriasQuemadasPorFecha(fechaActual);
            tvEjercicios.setText(String.format("Calorías quemadas: %.0f kcal", ejercicios));
        } catch (Exception e) {
            Toast.makeText(this, "Error al cargar resumen: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}