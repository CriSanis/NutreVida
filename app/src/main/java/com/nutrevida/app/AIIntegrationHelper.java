package com.nutrevida.app;

import android.content.Context;
import android.widget.Toast;

import com.google.ai.client.generativeai.GenerativeModel;
import com.google.ai.client.generativeai.type.Content;
import com.google.ai.client.generativeai.type.GenerateContentResponse;

import java.util.Arrays;

public class AIIntegrationHelper {
    private final GenerativeModel model;
    private final Context context;

    public AIIntegrationHelper(Context context) {
        this.context = context;
        this.model = new GenerativeModel("gemini-1.5-flash", BuildConfig.apiKey);
    }

    public interface AIResponseCallback {
        void onSuccess(String response);
        void onError(String error);
    }

    public void getHealthRecommendation(String userInput, String category, AIResponseCallback callback) {
        new Thread(() -> {
            try {
                String prompt = buildPrompt(userInput, category);
                String response = sendRequest(prompt);
                callback.onSuccess(response);
            } catch (Exception e) {
                callback.onError("Error: " + e.getMessage());
            }
        }).start();
    }

    private String buildPrompt(String userInput, String category) {
        String basePrompt = "Eres un asistente de salud especializado en salud mental, física y nutrición, con enfoque en Bolivia. Proporciona recomendaciones prácticas, culturalmente relevantes y basadas en ingredientes y prácticas comunes en Bolivia. Responde en español, de manera amigable y concisa, con un tono motivador. Categoría: %s. ";

        switch (category.toLowerCase()) {
            case "mental":
                return String.format(basePrompt + "Ofrece consejos para mejorar la salud mental, considerando el contexto boliviano (estrés, comunidad, tradiciones). Ejemplo: técnicas de relajación, actividades comunitarias. Pregunta: %s", "Salud Mental", userInput);
            case "fisica":
                return String.format(basePrompt + "Sugiere ejercicios físicos accesibles en Bolivia, adaptados a entornos urbanos o rurales, con recursos mínimos. Ejemplo: caminatas, ejercicios en casa, o deportes populares. Pregunta: %s", "Salud Física", userInput);
            case "nutrición":
                return String.format(basePrompt + "Recomienda opciones de alimentación saludable usando ingredientes típicos bolivianos (quinoa, chuño, papa, habas, etc.). Considera presupuesto y disponibilidad local. Ejemplo: recetas simples, snacks saludables. Pregunta: %s", "Nutrición", userInput);
            default:
                return String.format(basePrompt + "Proporciona consejos generales de salud adaptados a Bolivia. Pregunta: %s", "General", userInput);
        }
    }

    private String sendRequest(String prompt) throws Exception {
        try {
            Content content = new Content.Builder()
                    .addText(prompt)
                    .build();
            GenerateContentResponse response = model.generateContent(content);
            String result = response.getText();
            if (result == null || result.isEmpty()) {
                throw new Exception("Respuesta vacía del servidor.");
            }
            return result;
        } catch (Exception e) {
            throw new Exception("Error al generar contenido: " + e.getMessage());
        }
    }

    public void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}