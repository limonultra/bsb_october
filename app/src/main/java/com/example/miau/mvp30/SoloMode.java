package com.example.miau.mvp30;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.support.v7.app.AppCompatActivity;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static android.content.ContentValues.TAG;

public class SoloMode extends AppCompatActivity implements RecognitionListener {


    private SpeechRecognizer speech = null;
    private boolean listening = false;
    private Intent speechIntent;
    private static Queue<String> recordingQueue = new LinkedList<>();
    private static String fullRecord = "";
    private static boolean responseReceived = true;
    private String response;
    private ArrayList<String> speechResults;
    private static boolean isPausePressed = false;
    private static boolean chronoState = false;
    int current_volume;
    AudioManager audio;

    private Button btnPlay;
    private Button btnStop;
    private Button btnPlayPause;
    private TextView transcription;


    @Override
    public
    void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.solomode);

        transcription = findViewById( R.id.transcription );
        btnPlay = findViewById( R.id.btnStart );
        btnPlayPause = findViewById( R.id.btnPlayPause );
        btnStop = findViewById( R.id.btnStop );

        speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        saveCurrentAudio();

    }

    public void ButtonStartEvent(View view) {
        muteAudio();
        speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        btnPlay.setVisibility(View.GONE);
        btnPlayPause.setVisibility(View.VISIBLE);
        btnStop.setVisibility(View.VISIBLE);
        chronoState = false;
        startVoiceRecognitionCycle(speechIntent);

    }

    public void ButtonPlayPauseEvent(View view) {
        if (!chronoState) {
            btnPlayPause.setBackgroundResource(R.mipmap.play);
            speech.cancel();
            chronoState = true;
            isPausePressed = true;
        } else {
            btnPlayPause.setBackgroundResource(R.mipmap.pause);
            startVoiceRecognitionCycle(speechIntent);
            chronoState = false;
        }
    }

    public void ButtonStopEvent(View view) {
        if(isPausePressed){
            onBackPressed();}else{


        resetAudio();}
    }

    //Reset to beginning
    public void resetAudio() {
        audio = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC),
                AudioManager.FLAG_REMOVE_SOUND_AND_VIBRATE);
    }

    public void muteAudio() {
        AudioManager amanager = (AudioManager) getSystemService( Context.AUDIO_SERVICE);
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


    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(SoloMode.this).
                setTitle("Cerrar sesión").
                setMessage("¿Desea volver a la pantalla de creacion de sala?").
                setCancelable(false).
                setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(listening) {
                            stopVoiceRecognition();
                        }
                        finish();
                    }

                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).show();

    }

    public void stopVoiceRecognition() {
        if(isPausePressed) {
            if (speech != null) {
                speech.stopListening();
                speech.cancel();
                speech.destroy();
                speech = null;

            }
        }else{
            btnStop.setEnabled( false );
            btnStop.setBackgroundResource(R.mipmap.people );
        }
    }

    private
    SpeechRecognizer getSpeechRecognizer() {
        if (speech == null) {
            speech = SpeechRecognizer.createSpeechRecognizer( this );
            speech.setRecognitionListener( this );
        }
        return speech;
    }

    public
    void startVoiceRecognitionCycle(Intent speechIntent) {
        speechIntent.putExtra( RecognizerIntent.EXTRA_LANGUAGE, "es-ES" );
        speechIntent.putExtra( RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM );
        speechIntent.putExtra( RecognizerIntent.EXTRA_PARTIAL_RESULTS, true );
        getSpeechRecognizer().startListening( speechIntent );
        listening = true;
    }

    //Listener Class
    @Override
    public
    void onReadyForSpeech(Bundle params) {
        Log.d( TAG, "onReadyForSpeech" );
    }

    @Override
    public
    void onBeginningOfSpeech() {
        Log.d( TAG, "onBeginningOfSpeech" );
    }

    @Override
    public
    void onRmsChanged(float rmsdB) {

    }

    @Override
    public
    void onBufferReceived(byte[] buffer) {
        Log.d( TAG, "onBufferReceived" );

    }

    @Override
    public
    void onEndOfSpeech() {
        Log.d( TAG, "onEndOfSpeech" );

    }

    @Override
    public
    void onError(int error) {
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
                startVoiceRecognitionCycle( speechIntent );
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
        Log.d( TAG, "onError code:" + error + " message: " + message );
    }

    @Override
    public
    void onResults(Bundle results) {
        startVoiceRecognitionCycle( speechIntent );
    }

    @Override
    public
    void onPartialResults(Bundle partialResults) {
        receiveResults( partialResults );

    }

    @Override
    public
    void onEvent(int eventType, Bundle params) {
        Log.d( TAG, "onEvent" );
    }

    private void receiveResults(Bundle results) {
        if ((results != null) && results.containsKey( SpeechRecognizer.RESULTS_RECOGNITION )) {
            List<String> heard = results.getStringArrayList( SpeechRecognizer.RESULTS_RECOGNITION );
            recordingQueue.add( heard.get( 0 ) );
            fullRecord = fullRecord + "" + (heard.get( 0 ));
            if (responseReceived) {
                response = response + "" + (heard.get( 0 ));
            }
            Log.d( TAG, String.valueOf( results.getStringArrayList( SpeechRecognizer.RESULTS_RECOGNITION ) ) );
            speechResults = results.getStringArrayList( SpeechRecognizer.RESULTS_RECOGNITION );
            transcription.setText(speechResults.toString().replace("[", "").replace("]", ""));
        }
    }
}