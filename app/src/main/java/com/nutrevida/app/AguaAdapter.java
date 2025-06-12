package com.nutrevida.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nutrevida.app.modelos.RegistroAgua;

import java.util.List;

public class AguaAdapter extends RecyclerView.Adapter<AguaAdapter.AguaViewHolder> {

    private Context context;
    private List<RegistroAgua> registros;
    private OnRegistroEliminadoListener listener;

    public interface OnRegistroEliminadoListener {
        void onRegistroEliminado(RegistroAgua registro);
    }

    public AguaAdapter(Context context, List<RegistroAgua> registros) {
        this.context = context;
        this.registros = registros;
    }

    public void setOnRegistroEliminadoListener(OnRegistroEliminadoListener listener) {
        this.listener = listener;
    }

    public void actualizarRegistros(List<RegistroAgua> nuevosRegistros) {
        this.registros = nuevosRegistros;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AguaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_agua, parent, false);
        return new AguaViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AguaViewHolder holder, int position) {
        RegistroAgua registro = registros.get(position);

        holder.tvCantidad.setText(String.format("%.0f ml", registro.getCantidad()));
        holder.tvHora.setText(registro.getHora());

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

    public static class AguaViewHolder extends RecyclerView.ViewHolder {
        TextView tvCantidad, tvHora;
        ImageButton btnEliminar;

        public AguaViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCantidad = itemView.findViewById(R.id.tvCantidad);
            tvHora = itemView.findViewById(R.id.tvHora);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}