package com.nutrevida.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nutrevida.app.modelos.Alimento;
import com.nutrevida.app.modelos.ConsumoDiario;

import java.util.List;

public class ConsumoAdapter extends RecyclerView.Adapter<ConsumoAdapter.ConsumoViewHolder> {

    private Context context;
    private List<ConsumoDiario> consumos;
    private List<Alimento> alimentos;
    private OnConsumoEliminadoListener listener;

    public interface OnConsumoEliminadoListener {
        void onConsumoEliminado(ConsumoDiario consumo);
    }

    public ConsumoAdapter(Context context, List<ConsumoDiario> consumos, List<Alimento> alimentos) {
        this.context = context;
        this.consumos = consumos;
        this.alimentos = alimentos;
    }

    public void setOnConsumoEliminadoListener(OnConsumoEliminadoListener listener) {
        this.listener = listener;
    }

    public void actualizarConsumos(List<ConsumoDiario> nuevosConsumos, List<Alimento> nuevosAlimentos) {
        this.consumos = nuevosConsumos;
        this.alimentos = nuevosAlimentos;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ConsumoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_consumo, parent, false);
        return new ConsumoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConsumoViewHolder holder, int position) {
        ConsumoDiario consumo = consumos.get(position);

        // Buscar el alimento correspondiente
        Alimento alimento = null;
        for (Alimento a : alimentos) {
            if (a.getId() == consumo.getAlimentoId()) {
                alimento = a;
                break;
            }
        }

        if (alimento != null) {
            holder.tvNombre.setText(alimento.getNombre());
            holder.tvCantidad.setText(String.format("%.0f g", consumo.getCantidad()));
            holder.tvCalorias.setText(String.format("%.0f kcal", (consumo.getCantidad() * alimento.getCalorias() / 100)));
        }

        holder.btnEliminar.setOnClickListener(v -> {
            if (listener != null) {
                listener.onConsumoEliminado(consumo);
            }
        });
    }

    @Override
    public int getItemCount() {
        return consumos != null ? consumos.size() : 0;
    }

    public static class ConsumoViewHolder extends RecyclerView.ViewHolder {
        TextView tvNombre, tvCantidad, tvCalorias;
        ImageButton btnEliminar;

        public ConsumoViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNombre = itemView.findViewById(R.id.tvNombre);
            tvCantidad = itemView.findViewById(R.id.tvCantidad);
            tvCalorias = itemView.findViewById(R.id.tvCalorias);
            btnEliminar = itemView.findViewById(R.id.btnEliminar);
        }
    }
}