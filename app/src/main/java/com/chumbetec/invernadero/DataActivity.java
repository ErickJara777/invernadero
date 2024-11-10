package com.chumbetec.invernadero;



import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Random;

public class DataActivity extends AppCompatActivity {

    private TextView tempTextView;
    private TextView humTextView;
    private TextView statusTextView;
    private Button disconnectButton;
    private Button updateButton;
    private Button activateIrrigationButton;
    private Button controlTemperatureButton;

    private static final String CHANNEL_ID = "monitoring_channel";
    private int currentTemperature = 20;
    private int currentHumidity = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        tempTextView = findViewById(R.id.tempTextView);
        humTextView = findViewById(R.id.humTextView);
        statusTextView = findViewById(R.id.statusTextView);
        disconnectButton = findViewById(R.id.disconnectButton);
        updateButton = findViewById(R.id.updateButton);
        activateIrrigationButton = findViewById(R.id.activateIrrigationButton);
        controlTemperatureButton = findViewById(R.id.controlTemperatureButton);

        mostrarDatosIniciales();
        enviarNotificacion();

        // Configura el botón de "Desconectar"
        disconnectButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarDatos();
            }
        });


        activateIrrigationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentHumidity < 50 && currentTemperature >= 10 && currentTemperature <= 25) {

                    currentHumidity = 80;
                    Toast.makeText(DataActivity.this, "Riego Activado", Toast.LENGTH_SHORT).show();
                    statusTextView.setText("Riego en Proceso");
                    humTextView.setText("Humedad: " + currentHumidity + "%");
                } else {
                    Toast.makeText(DataActivity.this, "Condiciones no adecuadas para riego", Toast.LENGTH_SHORT).show();
                }
            }
        });


        controlTemperatureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentTemperature < 30) {
                    // Set the temperature to an ideal value after controlling the temperature
                    currentTemperature = 22;  // Example ideal temperature
                    Toast.makeText(DataActivity.this, "Temperatura en Rango Ideal", Toast.LENGTH_SHORT).show();
                    statusTextView.setText("Temp dentro de Rango Ideal");
                    tempTextView.setText("Temperatura: " + currentTemperature + " ºC");
                } else {
                    Toast.makeText(DataActivity.this, "Temp fuera de Rango Ideal", Toast.LENGTH_SHORT).show();
                }
            }
        });

        createNotificationChannel();
    }

    private void mostrarDatosIniciales() {
        currentTemperature = 20;
        currentHumidity = 60;
        tempTextView.setText("Temperatura: " + currentTemperature + " ºC");
        humTextView.setText("Humedad: " + currentHumidity + "%");
        statusTextView.setText("Temp y Humedad Ideal");
    }

    private void actualizarDatos() {
        Random random = new Random();
        currentTemperature = 18 + random.nextInt(10);
        currentHumidity = 40 + random.nextInt(30);

        tempTextView.setText("Temperatura: " + currentTemperature + " ºC");
        humTextView.setText("Humedad: " + currentHumidity + "%");

        if (currentTemperature > 30) {
            statusTextView.setText("No Regar - Temp > 30ºC");
        } else if (currentTemperature >= 10 && currentTemperature <= 25) {
            if (currentHumidity < 50) {
                statusTextView.setText("Habilitado para riego ");
            } else {
                statusTextView.setText("Temp y Humedad Ideal");
            }
        } else {
            statusTextView.setText("Temp fuera de rango ideal");
        }
    }

    private void enviarNotificacion() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("Recordatorio")
                .setContentText("No se olvide de monitorear")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(1, builder.build());
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Canal de Monitoreo";
            String description = "Canal para recordatorio de monitoreo";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
