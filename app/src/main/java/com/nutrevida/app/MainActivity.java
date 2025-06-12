package com.nutrevida.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button btnIMC, btnContadorCalorias, btnControlAgua, btnHistorial, btnEjercicios, btnResumen, btnRutinas, btnConfiguracion, btnAIHealth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar botones
        btnIMC = findViewById(R.id.btnCalculadoraIMC);
        btnContadorCalorias = findViewById(R.id.btnContadorCalorias);
        btnControlAgua = findViewById(R.id.btnControlAgua);
        btnHistorial = findViewById(R.id.btnHistorialIMC);
        btnEjercicios = findViewById(R.id.btnEjercicios);
        btnResumen = findViewById(R.id.btnResumenSalud);
        btnRutinas = findViewById(R.id.btnRutinas);
        btnConfiguracion = findViewById(R.id.btnConfiguracion);
        btnAIHealth = findViewById(R.id.btnAIHealth);

        // Configurar clics
        btnIMC.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CalculadoraIMCActivity.class)));

        btnContadorCalorias.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ContadorCaloriasActivity.class)));

        btnControlAgua.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ControlAguaActivity.class)));

        btnHistorial.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, HistorialActivity.class)));

        btnEjercicios.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, EjerciciosActivity.class)));

        btnResumen.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ResumenActivity.class)));

        btnRutinas.setOnClickListener(v -> btnRutinas.setEnabled(false)); // Deshabilitado

        btnConfiguracion.setOnClickListener(v -> btnConfiguracion.setEnabled(false)); // Deshabilitado

        btnAIHealth.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AIHealthActivity.class)));
    }
}