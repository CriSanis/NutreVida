package com.nutrevida.app;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nutrevida.app.modelos.Ejercicio;

import java.util.List;

public class EjerciciosActivity extends AppCompatActivity implements EjercicioAdapter.OnEjercicioEliminadoListener {

    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerViewEjercicios;
    private EjercicioAdapter adapter;
    private TextView tvTotalCalorias, tvFecha;
    private LinearLayout layoutSinEjercicios;
    private ImageButton btnVolver, btnLimpiarEjercicios;
    private Button btnAgregarEjercicio;
    private String fechaActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ejercicios);

        databaseHelper = new DatabaseHelper(this);
        inicializarVistas();
        configurarRecyclerView();
        configurarBotones();
        cargarEjercicios();
    }

    private void inicializarVistas() {
        recyclerViewEjercicios = findViewById(R.id.recyclerViewEjercicios);
        tvTotalCalorias = findViewById(R.id.tvTotalCalorias);
        tvFecha = findViewById(R.id.tvFecha);
        layoutSinEjercicios = findViewById(R.id.layoutSinEjercicios);
        btnVolver = findViewById(R.id.btnVolver);
        btnLimpiarEjercicios = findViewById(R.id.btnLimpiarEjercicios);
        btnAgregarEjercicio = findViewById(R.id.btnAgregarEjercicio);
        fechaActual = databaseHelper.getCurrentDate();
        tvFecha.setText(fechaActual);
    }

    private void configurarRecyclerView() {
        recyclerViewEjercicios.setLayoutManager(new LinearLayoutManager(this));
        adapter = new EjercicioAdapter(this, databaseHelper.obtenerEjerciciosPorFecha(fechaActual));
        adapter.setOnEjercicioEliminadoListener(this);
        recyclerViewEjercicios.setAdapter(adapter);
    }

    private void configurarBotones() {
        btnVolver.setOnClickListener(v -> finish());

        btnLimpiarEjercicios.setOnClickListener(v -> mostrarDialogoLimpiarEjercicios());

        btnAgregarEjercicio.setOnClickListener(v -> mostrarDialogoAgregarEjercicio());
    }

    private void cargarEjercicios() {
        List<Ejercicio> ejercicios = databaseHelper.obtenerEjerciciosPorFecha(fechaActual);

        if (ejercicios.isEmpty()) {
            layoutSinEjercicios.setVisibility(View.VISIBLE);
            recyclerViewEjercicios.setVisibility(View.GONE);
            tvTotalCalorias.setText("0 kcal");
        } else {
            layoutSinEjercicios.setVisibility(View.GONE);
            recyclerViewEjercicios.setVisibility(View.VISIBLE);
            double totalCalorias = databaseHelper.obtenerTotalCaloriasQuemadasPorFecha(fechaActual);
            tvTotalCalorias.setText(String.format("%.0f kcal", totalCalorias));
            adapter.actualizarEjercicios(ejercicios);
        }
    }

    private void mostrarDialogoAgregarEjercicio() {
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_agregar_ejercicio, null);
        EditText etNombre = dialogView.findViewById(R.id.etNombreEjercicio);
        EditText etDuracion = dialogView.findViewById(R.id.etDuracion);
        EditText etCalorias = dialogView.findViewById(R.id.etCalorias);

        new AlertDialog.Builder(this)
                .setTitle("Agregar Ejercicio")
                .setView(dialogView)
                .setPositiveButton("Agregar", (dialog, which) -> {
                    try {
                        String nombre = etNombre.getText().toString().trim();
                        String duracionStr = etDuracion.getText().toString().trim();
                        String caloriasStr = etCalorias.getText().toString().trim();

                        if (nombre.isEmpty() || duracionStr.isEmpty() || caloriasStr.isEmpty()) {
                            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        int duracion = Integer.parseInt(duracionStr);
                        double calorias = Double.parseDouble(caloriasStr);

                        if (duracion <= 0 || calorias < 0) {
                            Toast.makeText(this, "Ingresa valores válidos", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        long id = databaseHelper.insertarEjercicio(nombre, duracion, calorias, fechaActual);
                        if (id != -1) {
                            Toast.makeText(this, "Ejercicio registrado", Toast.LENGTH_SHORT).show();
                            cargarEjercicios();
                        } else {
                            Toast.makeText(this, "Error al registrar", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NumberFormatException e) {
                        Toast.makeText(this, "Ingresa valores numéricos válidos", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void mostrarDialogoLimpiarEjercicios() {
        new AlertDialog.Builder(this)
                .setTitle("Limpiar Ejercicios")
                .setMessage("¿Estás seguro de que quieres eliminar todos los ejercicios de hoy? Esta acción no se puede deshacer.")
                .setPositiveButton("Eliminar todo", (dialog, which) -> {
                    boolean eliminado = databaseHelper.eliminarEjerciciosPorFecha(fechaActual);
                    if (eliminado) {
                        Toast.makeText(this, "Ejercicios eliminados", Toast.LENGTH_SHORT).show();
                        cargarEjercicios();
                    } else {
                        Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    @Override
    public void onEjercicioEliminado(Ejercicio ejercicio) {
        boolean eliminado = databaseHelper.eliminarEjercicio(ejercicio.getId());
        if (eliminado) {
            Toast.makeText(this, "Ejercicio eliminado", Toast.LENGTH_SHORT).show();
            cargarEjercicios();
        } else {
            Toast.makeText(this, "Error al eliminar", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarEjercicios();
    }
}