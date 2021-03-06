package com.example.miau.mvp30;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static android.content.ContentValues.TAG;
import static com.example.miau.mvp30.Adapter.CustomAdapter.getStringFromIdiom;

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
    private TextView playText;
    private TextView pauseText;
    private TextView stopText;
    private TextView transcription;
    private TextView idiom;
    private EditText editableText;


    private String newText ="";
    private String oldText="";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView(R.layout.solomode);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        transcription = findViewById( R.id.transcription );
        btnPlay = findViewById( R.id.btnStart );
        btnPlayPause = findViewById( R.id.btnPlayPause );
        btnStop = findViewById( R.id.btnStop );
        pauseText = findViewById( R.id.pauseText );
        playText = findViewById( R.id.playText );
        stopText = findViewById( R.id.stopText );
        editableText = findViewById( R.id.editableText );
        idiom = findViewById( R.id.idiom );

        String[] tempPerms = {Manifest.permission.RECORD_AUDIO};
        hasPermissions( this, tempPerms );

        SharedPreferences sharedPref =  this.getSharedPreferences( "config",MODE_PRIVATE );
        SharedPreferences colorSharedPref =  this.getSharedPreferences( "colors",MODE_PRIVATE );

        String idiomPref = getStringFromIdiom( sharedPref.getString( "idioma", "es_ES" ) );
        idiom.setText("Reconocimiento de voz: ".concat(idiomPref));

        String backgroundPref = colorSharedPref.getString("background", "#f4f4f4");
        int textPref = colorSharedPref.getInt("text", ContextCompat.getColor(this, R.color.black));

        editableText.setBackgroundColor( Color.parseColor(backgroundPref) );
        editableText.setTextColor(textPref);
        transcription.setBackgroundColor( Color.parseColor(backgroundPref) );
        transcription.setTextColor(textPref);

        speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        saveCurrentAudio();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("oldText", transcription.getText().toString());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        transcription.setText(savedInstanceState.getString("oldText"));
    }

    public void supressEditableEvent(View view){
        editableText.setText( "" );
    }
    public void setSupressbtn1(View view){
        transcription.setText( "" );
        oldText = "";
        newText = "";
    }

    public void ButtonStartEvent(View view) {
        muteAudio();
        speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        btnPlay.setVisibility(View.GONE);
        playText.setVisibility( View.GONE );
        btnStop.setBackgroundResource( R.drawable.ic_stopbutton_gray );
        btnPlayPause.setVisibility(View.VISIBLE);
        pauseText.setVisibility( View.VISIBLE );
        btnStop.setVisibility(View.VISIBLE);
        btnStop.setEnabled(false);
        stopText.setVisibility( View.VISIBLE );
        chronoState = false;

        startVoiceRecognitionCycle(speechIntent);

    }

    public void ButtonPlayPauseEvent(View view) {
        if (!chronoState) {
            listening = false;
            btnPlayPause.setBackgroundResource(R.drawable.ic_pauseboton2);
            speech.cancel();
            chronoState = true;
            isPausePressed = false;
            btnStop.setBackgroundResource( R.drawable.ic_stopboton1 );
            stopText.setTextColor( Color.parseColor("#00b6c7"));
            btnStop.setEnabled(true);
        } else {
            btnPlayPause.setBackgroundResource(R.drawable.ic_pauseboton1);
            startVoiceRecognitionCycle(speechIntent);
            btnStop.setBackgroundResource( R.drawable.ic_stopbutton_gray );
            stopText.setTextColor( Color.parseColor("#999999"));
            chronoState = false;
            isPausePressed = true;
            listening = true;
            btnStop.setEnabled(false);
        }
    }

    public void ButtonStopEvent(View view) {
        if(!isPausePressed) {
            btnStop.setBackgroundResource(R.drawable.ic_stopboton2);
            btnStop.setEnabled( true );
            onBackPressed();

        }else{
            btnStop.setEnabled( false );
            btnStop.setBackgroundResource(R.drawable.ic_stopboton1);
        }
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
                            resetAudio();
                        }
                        finish();
                    }

                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                btnStop.setBackgroundResource( R.drawable.ic_stopboton1 );
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
                isPausePressed = true;

            }
        }
    }

    private SpeechRecognizer getSpeechRecognizer() {
        if (speech == null) {
            speech = SpeechRecognizer.createSpeechRecognizer( this );
            speech.setRecognitionListener( this );
        }
        return speech;
    }

    public void startVoiceRecognitionCycle(Intent speechIntent) {
        speechIntent.putExtra( RecognizerIntent.EXTRA_LANGUAGE, "es-ES" );
        speechIntent.putExtra( RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM );
        speechIntent.putExtra( RecognizerIntent.EXTRA_PARTIAL_RESULTS, true );
        getSpeechRecognizer().startListening( speechIntent );
        listening = true;
    }

    //Listener Class
    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.d( TAG, "onReadyForSpeech" );
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.d( TAG, "onBeginningOfSpeech" );
    }

    @Override
    public void onRmsChanged(float rmsdB) {

    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.d( TAG, "onBufferReceived" );

    }

    @Override
    public void onEndOfSpeech() {
        Log.d( TAG, "onEndOfSpeech" );

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
    public void onResults(Bundle results) {
        startVoiceRecognitionCycle( speechIntent );
        newText = oldText.concat(" ");
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        receiveResults( partialResults );

    }

    @Override
    public void onEvent(int eventType, Bundle params) {
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
            String message = speechResults.toString().replace("[", "").replace("]", "");
            oldText = newText.concat(message);
            transcription.setText(newText.concat(message));
        }
    }
    private boolean hasPermissions (Context ctx, String[] permissions) {
        if(ctx != null && permissions != null) {
            for(String permission: permissions) {
                if(ActivityCompat.checkSelfPermission(ctx, permission) != PackageManager.PERMISSION_GRANTED)
                    ActivityCompat.requestPermissions( this, permissions, 2 );
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2)
            if (grantResults[0] == PackageManager.PERMISSION_DENIED)
                finish();


    }
}