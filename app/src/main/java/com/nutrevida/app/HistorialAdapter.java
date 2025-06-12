package com.nutrevida.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nutrevida.app.modelos.RegistroIMC;

import java.util.List;

public class HistorialAdapter extends RecyclerView.Adapter<HistorialAdapter.HistorialViewHolder> {

    private Context context;
    private List<RegistroIMC> registros;
    private OnRegistroEliminadoListener listener;

    public interface OnRegistroEliminadoListener {
        void onRegistroEliminado(RegistroIMC registro);
    }

    public HistorialAdapter(Context context, List<RegistroIMC> registros) {
        this.context = context;
        this.registros = registros;
    }

    public void setOnRegistroEliminadoListener(OnRegistroEliminadoListener listener) {
        this.listener = listener;
    }

    public void actualizarRegistros(List<RegistroIMC> nuevosRegistros) {
        this.registros = nuevosRegistros;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public HistorialViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_historial, parent, false);
        return new HistorialViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistorialViewHolder holder, int position) {
        RegistroIMC registro = registros.get(position);
        holder.tvFecha.setText(registro.getFecha());
        holder.tvIMC.setText(String.format("%.1f", registro.getImc()));
        holder.tvCategoria.setText(registro.getResultado());
        holder.tvPeso.setText(String.format("%.1f kg", registro.getPeso()));
        holder.tvAltura.setText(String.format("%.1f cm", registro.getAltura()));

        holder.btnEliminar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onRegistroEliminado(registro);
            }
        });
    }

    @Override
    public int getItemCount() {
        return registros != null ? registros.size() : 0;
    }

    public static class HistorialViewHolder extends RecyclerView.ViewHolder {
        TextView tvFecha, tvIMC, tvCategoria, tvPeso, tvAltura;
        ImageButton btnEliminar;

        public HistorialViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFecha = itemView.findViewById(R.id.tvFecha);
            tvIMC = itemView.findViewById(R.id.tvIMC);
            tvCategoria = itemView.findViewById(R.id.tvCategoria);
            tvPeso = itemView.findViewById(R.id.tvPeso);
            tvAltura = itemView.findViewById(R.id.tvAltura);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}