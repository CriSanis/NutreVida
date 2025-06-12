package com.nutrevida.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nutrevida.app.modelos.Ejercicio;

import java.util.List;

public class HistorialEjerciciosAdapter extends RecyclerView.Adapter<HistorialEjerciciosAdapter.EjercicioViewHolder> {

    private Context context;
    private List<Ejercicio> ejercicios;
    private OnEjercicioEliminadoListener listener;

    public interface OnEjercicioEliminadoListener {
        void onEjercicioEliminado(Ejercicio ejercicio);
    }

    public HistorialEjerciciosAdapter(Context context, List<Ejercicio> ejercicios) {
        this.context = context;
        this.ejercicios = ejercicios;
    }

    public void setOnEjercicioEliminadoListener(OnEjercicioEliminadoListener listener) {
        this.listener = listener;
    }

    public void actualizarEjercicios(List<Ejercicio> nuevosEjercicios) {
        this.ejercicios = nuevosEjercicios;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EjercicioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ejercicio, parent, false);
        return new EjercicioViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EjercicioViewHolder holder, int position) {
        Ejercicio ejercicio = ejercicios.get(position);
        holder.tvNombre.setText(ejercicio.getNombre());
        holder.tvFecha.setText(ejercicio.getFecha());
        holder.tvDuracion.setText(String.format("%.0f min", ejercicio.getDuracion()));
        holder.tvCalorias.setText(String.format("%.0f kcal", ejercicio.getCaloriasQuemadas()));

        holder.btnEliminar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onEjercicioEliminado(ejercicio);
            }
        });
    }

    @Override
    public int getItemCount() {
        return ejercicios != null ? ejercicios.size() : 0;
    }

    public static class EjercicioViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvFecha, tvDuracion, tvCalorias;
        ImageButton btnEliminar;

        public EjercicioViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvDuracion = itemView.findViewById(R.id.tvDuracion);
            tvCalorias = itemView.findViewById(R.id.tvCalorias);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}