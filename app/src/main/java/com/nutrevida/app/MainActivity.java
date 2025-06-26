package com.nutrevida.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.card.MaterialCardView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configurar clics en las tarjetas de acciones rápidas
        setupQuickActions();
        
        // Configurar clics en las herramientas
        setupTools();
        
        // Configurar clics en entretenimiento
        setupEntertainment();
    }

    private void setupQuickActions() {
        // IMC
        MaterialCardView cardIMC = findViewById(R.id.cardIMC);
        cardIMC.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CalculadoraIMCActivity.class)));

        // Calorías
        MaterialCardView cardCalorias = findViewById(R.id.cardCalorias);
        cardCalorias.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ContadorCaloriasActivity.class)));

        // Agua
        MaterialCardView cardAgua = findViewById(R.id.cardAgua);
        cardAgua.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ControlAguaActivity.class)));

        // Ejercicios
        MaterialCardView cardEjercicios = findViewById(R.id.cardEjercicios);
        cardEjercicios.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, EjerciciosActivity.class)));
    }

    private void setupTools() {
        // Historial
        MaterialCardView cardHistorial = findViewById(R.id.cardHistorial);
        cardHistorial.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, HistorialActivity.class)));

        // Resumen
        MaterialCardView cardResumen = findViewById(R.id.cardResumen);
        cardResumen.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, ResumenActivity.class)));

        // Rutinas
        MaterialCardView cardRutinas = findViewById(R.id.cardRutinas);
        cardRutinas.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, RutinasActivity.class)));
    }

    private void setupEntertainment() {
        // Juego
        MaterialCardView cardJuego = findViewById(R.id.cardJuego);
        cardJuego.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, GameActivity.class)));

        // IA
        MaterialCardView cardIA = findViewById(R.id.cardIA);
        cardIA.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AIHealthActivity.class)));
    }
}