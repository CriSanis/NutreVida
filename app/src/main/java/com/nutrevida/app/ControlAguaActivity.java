package com.nutrevida.app;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nutrevida.app.modelos.RegistroAgua;

import java.util.List;

public class ControlAguaActivity extends AppCompatActivity implements AguaAdapter.OnRegistroEliminadoListener {

    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerViewAgua;
    private AguaAdapter adapter;
    private TextView tvTotalAgua, tvFecha, tvProgreso;
    private ProgressBar progressBarAgua;
    private LinearLayout layoutSinRegistros;
    private ImageButton btnVolver, btnLimpiarAgua;
    private Button btnAgregar250, btnAgregar500;
    private String fechaActual;
    private static final double META_DIARIA = 2000; // 2 litros en ml

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_agua);

        // Inicializar base de datos
        databaseHelper = new DatabaseHelper(this);

        // Inicializar vistas
        inicializarVistas();

        // Configurar RecyclerView
        configurarRecyclerView();

        // Configurar botones
        configurarBotones();

        // Cargar datos
        cargarRegistros();
    }

    private void inicializarVistas() {
        recyclerViewAgua = findViewById(R.id.recyclerViewAgua);
        tvTotalAgua = findViewById(R.id.tvTotalAgua);
        tvFecha = findViewById(R.id.tvFecha);
        tvProgreso = findViewById(R.id.tvProgreso);
        progressBarAgua = findViewById(R.id.progressBarAgua);
        layoutSinRegistros = findViewById(R.id.layoutSinRegistros);
        btnVolver = findViewById(R.id.btnVolver);
        btnLimpiarAgua = findViewById(R.id.btnLimpiarAgua);
        btnAgregar250 = findViewById(R.id.btnAgregar250);
        btnAgregar500 = findViewById(R.id.btnAgregar500);
        fechaActual = databaseHelper.getCurrentDate();
        tvFecha.setText(fechaActual);
    }

    private void configurarRecyclerView() {
        recyclerViewAgua.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AguaAdapter(this, databaseHelper.obtenerRegistrosAguaPorFecha(fechaActual));
        adapter.setOnRegistroEliminadoListener(this);
        recyclerViewAgua.setAdapter(adapter);
    }

    private void configurarBotones() {
        btnVolver.setOnClickListener(v -> finish());

        btnLimpiarAgua.setOnClickListener(v -> mostrarDialogoLimpiarAgua());

        btnAgregar250.setOnClickListener(v -> agregarAgua(250));

        btnAgregar500.setOnClickListener(v -> agregarAgua(500));
    }

    private void cargarRegistros() {
        List<RegistroAgua> registros = databaseHelper.obtenerRegistrosAguaPorFecha(fechaActual);
        double totalAgua = databaseHelper.obtenerTotalAguaPorFecha(fechaActual);

        if (registros.isEmpty()) {
            layoutSinRegistros.setVisibility(View.VISIBLE);
            recyclerViewAgua.setVisibility(View.GONE);
            tvTotalAgua.setText("0 ml");
            tvProgreso.setText("0% de la meta diaria (2000 ml)");
            progressBarAgua.setProgress(0);
        } else {
            layoutSinRegistros.setVisibility(View.GONE);
            recyclerViewAgua.setVisibility(View.VISIBLE);
            tvTotalAgua.setText(String.format("%.0f ml", totalAgua));
            int progreso = (int) ((totalAgua / META_DIARIA) * 100);
            tvProgreso.setText(String.format("%d%% de la meta diaria (2000 ml)", progreso));
            progressBarAgua.setProgress((int) totalAgua);
            adapter.actualizarRegistros(registros);
        }
    }

    private void agregarAgua(double cantidad) {
        long id = databaseHelper.insertarRegistroAgua(cantidad);
        if (id > 0) {
            Toast.makeText(this, getString(R.string.agua_agregada), Toast.LENGTH_SHORT).show();
            cargarRegistros();
        } else {
            Toast.makeText(this, getString(R.string.error_agregar_agua), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRegistroEliminado(RegistroAgua registro) {
        boolean eliminado = databaseHelper.eliminarRegistroAgua(registro.getId());
        if (eliminado) {
            Toast.makeText(this, getString(R.string.agua_eliminada), Toast.LENGTH_SHORT).show();
            cargarRegistros();
        } else {
            Toast.makeText(this, getString(R.string.error_eliminar_agua), Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarDialogoLimpiarAgua() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.limpiar_agua))
                .setMessage(getString(R.string.confirmar_limpiar_agua))
                .setPositiveButton(getString(R.string.eliminar_todo), (dialog, which) -> limpiarRegistrosAgua())
                .setNegativeButton(getString(R.string.cancelar), null)
                .show();
    }

    private void limpiarRegistrosAgua() {
        boolean eliminado = databaseHelper.eliminarRegistrosAguaPorFecha(fechaActual);
        if (eliminado) {
            Toast.makeText(this, getString(R.string.agua_eliminada_completa), Toast.LENGTH_SHORT).show();
            cargarRegistros();
        } else {
            Toast.makeText(this, getString(R.string.error_eliminar_agua), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarRegistros();
    }
}