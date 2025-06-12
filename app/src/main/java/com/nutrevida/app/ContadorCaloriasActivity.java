package com.nutrevida.app;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nutrevida.app.modelos.Alimento;
import com.nutrevida.app.modelos.ConsumoDiario;

import java.util.List;

public class ContadorCaloriasActivity extends AppCompatActivity implements ConsumoAdapter.OnConsumoEliminadoListener {

    private DatabaseHelper databaseHelper;
    private RecyclerView recyclerViewConsumo;
    private ConsumoAdapter adapter;
    private TextView tvTotalCalorias, tvFecha;
    private LinearLayout layoutSinConsumos;
    private ImageButton btnVolver, btnLimpiarConsumo;
    private Button btnAgregarAlimento;
    private String fechaActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contador_calorias);

        // Inicializar base de datos
        databaseHelper = new DatabaseHelper(this);

        // Inicializar vistas
        inicializarVistas();

        // Configurar RecyclerView
        configurarRecyclerView();

        // Configurar botones
        configurarBotones();

        // Cargar datos
        cargarConsumos();
    }

    private void inicializarVistas() {
        recyclerViewConsumo = findViewById(R.id.recyclerViewConsumo);
        tvTotalCalorias = findViewById(R.id.tvTotalCalorias);
        tvFecha = findViewById(R.id.tvFecha);
        layoutSinConsumos = findViewById(R.id.layoutSinConsumos);
        btnVolver = findViewById(R.id.btnVolver);
        btnLimpiarConsumo = findViewById(R.id.btnLimpiarConsumo);
        btnAgregarAlimento = findViewById(R.id.btnAgregarAlimento);
        fechaActual = databaseHelper.getCurrentDate();
        tvFecha.setText(fechaActual);
    }

    private void configurarRecyclerView() {
        recyclerViewConsumo.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ConsumoAdapter(this, databaseHelper.obtenerConsumoPorFecha(fechaActual), databaseHelper.obtenerTodosLosAlimentos());
        adapter.setOnConsumoEliminadoListener(this);
        recyclerViewConsumo.setAdapter(adapter);
    }

    private void configurarBotones() {
        btnVolver.setOnClickListener(v -> finish());

        btnLimpiarConsumo.setOnClickListener(v -> mostrarDialogoLimpiarConsumo());

        btnAgregarAlimento.setOnClickListener(v -> {
            Intent intent = new Intent(this, AgregarAlimentoActivity.class);
            startActivityForResult(intent, 1);
        });
    }

    private void cargarConsumos() {
        List<ConsumoDiario> consumos = databaseHelper.obtenerConsumoPorFecha(fechaActual);
        List<Alimento> alimentos = databaseHelper.obtenerTodosLosAlimentos();

        if (consumos.isEmpty()) {
            layoutSinConsumos.setVisibility(View.VISIBLE);
            recyclerViewConsumo.setVisibility(View.GONE);
            tvTotalCalorias.setText("0 kcal");
        } else {
            layoutSinConsumos.setVisibility(View.GONE);
            recyclerViewConsumo.setVisibility(View.VISIBLE);
            double totalCalorias = databaseHelper.obtenerCaloriasTotalesPorFecha(fechaActual);
            tvTotalCalorias.setText(String.format("%.0f kcal", totalCalorias));
            adapter.actualizarConsumos(consumos, alimentos);
        }
    }

    @Override
    public void onConsumoEliminado(ConsumoDiario consumo) {
        boolean eliminado = databaseHelper.eliminarConsumoDiario(consumo.getId());
        if (eliminado) {
            Toast.makeText(this, getString(R.string.consumo_eliminado), Toast.LENGTH_SHORT).show();
            cargarConsumos();
        } else {
            Toast.makeText(this, getString(R.string.error_eliminar_consumo), Toast.LENGTH_SHORT).show();
        }
    }

    private void mostrarDialogoLimpiarConsumo() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.limpiar_consumo))
                .setMessage(getString(R.string.confirmar_limpiar_consumo))
                .setPositiveButton(getString(R.string.eliminar_todo), (dialog, which) -> limpiarConsumoCompleto())
                .setNegativeButton(getString(R.string.cancelar), null)
                .show();
    }

    private void limpiarConsumoCompleto() {
        boolean eliminado = databaseHelper.eliminarConsumosPorFecha(fechaActual);
        if (eliminado) {
            Toast.makeText(this, getString(R.string.consumo_eliminado_completo), Toast.LENGTH_SHORT).show();
            cargarConsumos();
        } else {
            Toast.makeText(this, getString(R.string.error_eliminar_consumo), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            cargarConsumos();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarConsumos();
    }
}