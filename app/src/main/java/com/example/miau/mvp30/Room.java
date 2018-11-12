package com.example.miau.mvp30;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static android.content.ContentValues.TAG;

public class Room extends AppCompatActivity implements RecognitionListener {

    private SpeechRecognizer speech = null;
    private static Queue<String> recordingQueue = new LinkedList<>();
    private static String fullRecord = "";
    private static boolean responseReceived = true;
    private static boolean isPausePressed = false;
    private String response;
    private CountDownParrafo countDownParrafo;

    private static boolean chronoState = false;
    long stopTime = 0;

    private ArrayList<String> speechResults;

    byte[] endSpeech = new byte[]{0, 0, 1, 1};
    byte[] speechRestart = new byte[]{1, 1, 0, 0};
    byte[] speechSalto = new byte[]{0, 1, 1, 0};

    int current_volume;
    AudioManager audio;
    String WIFI;
    String PIN;

    //private TranscriptionDialog transcriptionDialog;

    private Button btnPlay;
    private Button btnStop;
    private Button btnPlayPause;
    private Chronometer chrono;
    private TextView pinName;
    private TextView wifiName;
    private TextView pupilsNo;
    private boolean listening = false;

    private Intent speechIntent;

    InetSocketAddress inetSocketAddress = new InetSocketAddress(8080);
    Server serverControl;

    @Override
    public void onCreate(Bundle SavedInstanceState) {
        super.onCreate(SavedInstanceState);
        setContentView(R.layout.activity_room );
        //pupilsNo = findViewById(R.id.pupilNumber);
        serverControl = ServerSingleton.getInstance(inetSocketAddress, pupilsNo, this);
        btnPlay = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
        btnPlayPause = findViewById(R.id.btnPlayPause);
        chrono = findViewById(R.id.chronometer);
        countDownParrafo = new CountDownParrafo(4000, 2000);

        speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        //pinName = findViewById(R.id.pinName);

        //wifiName = findViewById(R.id.wifiName);

        //transcriptionDialog = new TranscriptionDialog();

        saveCurrentAudio();
        Bundle bundle;
        bundle = getIntent().getExtras();
        PIN = bundle.getString("PIN");
        WIFI = bundle.getString("wifiName");
        //pinName.setText(PIN);
        //wifiName.setText(WIFI);

    }

    public void ButtonStartEvent(View view) {
        muteAudio();
        btnPlay.setVisibility(View.GONE);
        btnPlayPause.setVisibility(View.VISIBLE);
        btnStop.setVisibility(View.VISIBLE);
        chrono.start();
        chrono.setBase(SystemClock.elapsedRealtime() + stopTime);
        chronoState = false;
        startVoiceRecognitionCycle(speechIntent);
        countDownParrafo.start();

    }

    public void ButtonStopEvent(View view) {
        chrono.stop();
        onBackPressed();
        resetAudio();
    }

    public void ButtonPlayPauseEvent(View view) {
        if (!chronoState) {
            chrono.stop();
            listening = false;
            btnPlayPause.setBackgroundResource(R.drawable.ic_pauseboton2);
            stopTime = chrono.getBase() - SystemClock.elapsedRealtime();
            speech.cancel();
            serverControl.broadcast(speechRestart);
            chronoState = true;
            isPausePressed = true;
            btnStop.setEnabled(true);
            countDownParrafo.cancel();
        } else {
            chrono.start();
            btnPlayPause.setBackgroundResource(R.drawable.ic_pauseboton1);
            chrono.setBase(SystemClock.elapsedRealtime() + stopTime);
            startVoiceRecognitionCycle(speechIntent);
            chronoState = false;
            isPausePressed = false;
            listening = true;
            btnStop.setEnabled(false);
            countDownParrafo.start();
        }
    }

    /*public void TranscriptionButtonEvent(View view){

            TranscriptionDialog fragment = new TranscriptionDialog();
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(android.R.id.content, fragment );
            fragmentTransaction.commit();
    }*/

    private SpeechRecognizer getSpeechRecognizer() {
        if (speech == null) {
            speech = SpeechRecognizer.createSpeechRecognizer(this);
            speech.setRecognitionListener(this);
        }
        return speech;
    }

    //Init speech recognizer
    public void startVoiceRecognitionCycle(Intent speechIntent) {
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES");
        speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true);
        getSpeechRecognizer().startListening(speechIntent);
        listening = true;
    }

    //Cancel speech recognition
    public void stopVoiceRecognition() {
        if(isPausePressed) {
            btnStop.setBackgroundResource(R.drawable.ic_stopboton2);
            if (speech != null) {
                speech.stopListening();
                speech.cancel();
                speech.destroy();
                speech = null;

            }
        }else{
            btnStop.setEnabled( false );
            btnStop.setBackgroundResource(R.drawable.ic_stopboton1);
        }
    }

    //Mutes Beep Speech Sound
    public void muteAudio() {
        AudioManager amanager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        amanager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true);
        amanager.setStreamMute(AudioManager.STREAM_ALARM, true);
        amanager.setStreamMute(AudioManager.STREAM_MUSIC, true);
        amanager.setStreamMute(AudioManager.STREAM_RING, true);
    }

    //Save User Audio
    public void saveCurrentAudio() {
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        current_volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    //Reset to beginning
    public void resetAudio() {
        audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }

    //Listener Class
    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.d(TAG, "onReadyForSpeech");
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.d(TAG, "onBeginningOfSpeech");
    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.d(TAG, "onBufferReceived");

    }

    @Override
    public void onEndOfSpeech() {
        Log.d(TAG, "onEndOfSpeech");

    }

    @Override
    public void onError(int error) {
        String message = null;
        switch (error) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                startVoiceRecognitionCycle(speechIntent);
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Not recognised";
                break;
        }
        Log.d(TAG, "onError code:" + error + " message: " + message);
    }

    @Override
    public void onResults(Bundle results) {
        startVoiceRecognitionCycle(speechIntent);
        serverControl.broadcast(speechRestart);
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        receiveResults(partialResults);

        countDownParrafo.cancel();
        countDownParrafo.start();

        //this.pupilsNo.setText(String.valueOf(serverControl.clientCount));
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.d(TAG, "onEvent");
    }

    private void receiveResults(Bundle results) {
        if ((results != null) && results.containsKey(SpeechRecognizer.RESULTS_RECOGNITION)) {
            List<String> heard = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            recordingQueue.add(heard.get(0));
            fullRecord = fullRecord + "" + (heard.get(0));
            if (responseReceived) {
                response = response + "" + (heard.get(0));
            }
            Log.d(TAG, String.valueOf(results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)));
            speechResults = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            String transcription = speechResults.toString().replace("[", "").replace("]", "");
            serverControl.broadcast(transcription);
            //transcriptionDialog.escribirSubs(transcription);

        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(Room.this).
                setTitle("Cerrar sesión").
                setMessage("¿Desea volver a la pantalla de creacion de sala?").
                setCancelable(false).
                setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(listening) {
                            serverControl.broadcast(endSpeech);
                            stopVoiceRecognition();
                        }
                        try {
                            serverControl.stop();
                            ServerSingleton.setServerNull();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.i("SERVER", "serverClosed");
                        finish();
                    }

                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();

    }

    private BroadcastReceiver mWifiStateChangedReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            int extraWifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);

            switch (extraWifiState) {
                case WifiManager.WIFI_STATE_DISABLED:
                case WifiManager.WIFI_STATE_DISABLING:
                    updateUI(false);
                    break;
                case WifiManager.WIFI_STATE_ENABLED:
                    ConnectivityManager conMan = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    while (conMan.getActiveNetworkInfo() == null || conMan.getActiveNetworkInfo().getState() != NetworkInfo.State.CONNECTED) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case WifiManager.WIFI_STATE_ENABLING:

                    break;
                case WifiManager.WIFI_STATE_UNKNOWN:
                    break;
            }

        }
    };

    private void updateUI(boolean isConnected) {

        if (!isConnected) {
            wifiName.setText("No estás conectado");
            pinName.setText("0000");
            chrono.stop();
            serverControl.broadcast(endSpeech);
            stopVoiceRecognition();
            resetAudio();
            new AlertDialog.Builder(Room.this).
                    setTitle("Conexion Perdida").
                    setMessage("Vas a volver a la pantalla de creacion de sala").
                    setCancelable(false).
                    setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent backToCreateRoom = new Intent(Room.this, RoomCreate.class);
                            startActivity(backToCreateRoom);
                        }
                    }).show();

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(this, RoomCreate.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public class CountDownParrafo extends CountDownTimer {

        public CountDownParrafo(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

        }

        @Override
        public void onFinish() {
            serverControl.broadcast(speechSalto);
            //transcriptionDialog.appendSalto();
            restartSpeechOnNewConnection();
            this.cancel();
        }
    }

    public void restartSpeechOnNewConnection() {
        if(listening) {
            stopVoiceRecognition();
            startVoiceRecognitionCycle(speechIntent);
        }
    }


    /*public static class TranscriptionDialog extends DialogFragment {

        private static final String TAG = "AKDialogFragment";

        private View rootView;
        private TextView profText;




        @Override
        public  View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

            rootView = inflater.inflate(R.layout.activity_transcription, container, false);
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());

            profText = rootView.findViewById(R.id.profText);


            setHasOptionsMenu(true);
            return rootView;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            Dialog dialog = super.onCreateDialog(savedInstanceState);
            return dialog;
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            menu.clear();
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();

            if (id == android.R.id.home) {
                // handle close button click here
                dismiss();
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        public void  escribirSubs(String transcription) {

            profText.setText(profText.toString().concat("$newtext"));
        }

        public void  appendSalto() {
            if(!profText.getText().toString().endsWith("\n"))
                profText.setText(profText.toString().concat("$newtext"));
        }
    }*/

}

