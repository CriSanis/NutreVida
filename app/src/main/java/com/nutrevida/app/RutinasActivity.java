package com.nutrevida.app;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RutinasActivity extends AppCompatActivity {

    private CalendarView calendarView;
    private EditText etAlimento, etEjercicio;
    private Button btnAddAlimento, btnAddEjercicio;
    private ImageButton btnBack;
    private RecyclerView rvRutinas;
    private TextView tvSuccessMessage;
    private DatabaseHelper dbHelper;
    private RutinasAdapter adapter;
    private List<Rutina> rutinas;
    private String selectedDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_rutinas);
        } catch (Exception e) {
            Toast.makeText(this, "Error al cargar el layout: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        dbHelper = new DatabaseHelper(this);
        rutinas = new ArrayList<>(); // Inicializar rutinas aquí
        if (!initializeViews()) {
            Toast.makeText(this, "Error al inicializar vistas", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        setupCalendar();
        setupRecyclerView();
    }

    private boolean initializeViews() {
        calendarView = findViewById(R.id.calendarView);
        etAlimento = findViewById(R.id.etAlimento);
        etEjercicio = findViewById(R.id.etEjercicio);
        btnAddAlimento = findViewById(R.id.btnAddAlimento);
        btnAddEjercicio = findViewById(R.id.btnAddEjercicio);
        btnBack = findViewById(R.id.btnBack);
        rvRutinas = findViewById(R.id.rvRutinas);
        tvSuccessMessage = findViewById(R.id.tvSuccessMessage);

        if (calendarView == null || etAlimento == null || etEjercicio == null ||
                btnAddAlimento == null || btnAddEjercicio == null ||
                btnBack == null || rvRutinas == null || tvSuccessMessage == null) {
            return false;
        }

        btnBack.setOnClickListener(v -> finish());
        btnAddAlimento.setOnClickListener(v -> addAlimento());
        btnAddEjercicio.setOnClickListener(v -> addEjercicio());
        return true;
    }

    private void setupCalendar() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        selectedDate = sdf.format(new Date());
        loadRutinas(selectedDate);

        calendarView.setOnDateChangeListener((view, year, month, dayOfMonth) -> {
            selectedDate = String.format(Locale.getDefault(), "%d-%02d-%02d", year, month + 1, dayOfMonth);
            loadRutinas(selectedDate);
        });
    }

    private void setupRecyclerView() {
        adapter = new RutinasAdapter(rutinas);
        rvRutinas.setLayoutManager(new LinearLayoutManager(this));
        rvRutinas.setAdapter(adapter);
    }

    private void addAlimento() {
        String alimento = etAlimento.getText().toString().trim();
        if (alimento.isEmpty()) {
            Toast.makeText(this, "Ingresa un alimento", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            dbHelper.addRutina(selectedDate, "Alimento", alimento);
            loadRutinas(selectedDate);
            etAlimento.setText("");
            showSuccessAnimation();
        } catch (Exception e) {
            Toast.makeText(this, "Error al agregar alimento: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void addEjercicio() {
        String ejercicio = etEjercicio.getText().toString().trim();
        if (ejercicio.isEmpty()) {
            Toast.makeText(this, "Ingresa un ejercicio", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            dbHelper.addRutina(selectedDate, "Ejercicio", ejercicio);
            loadRutinas(selectedDate);
            etEjercicio.setText("");
            showSuccessAnimation();
        } catch (Exception e) {
            Toast.makeText(this, "Error al agregar ejercicio: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void loadRutinas(String date) {
        try {
            rutinas.clear();
            rutinas.addAll(dbHelper.getRutinasByDate(date));
            adapter.notifyDataSetChanged();
        } catch (Exception e) {
            Toast.makeText(this, "Error al cargar rutinas: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void showSuccessAnimation() {
        tvSuccessMessage.setVisibility(View.VISIBLE);
        try {
            Animation animation = AnimationUtils.loadAnimation(this, R.anim.success_animation);
            tvSuccessMessage.startAnimation(animation);
            tvSuccessMessage.postDelayed(() -> tvSuccessMessage.setVisibility(View.GONE), 2000);
        } catch (Exception e) {
            Toast.makeText(this, "Error en animación: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}