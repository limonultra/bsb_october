package com.example.miau.mvp30

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.net.ConnectivityManager
import android.net.Uri
import android.net.wifi.SupplicantState
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v4.app.DialogFragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.text.format.Formatter
import android.util.Log
import android.view.*
import android.view.Menu
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import kotlinx.android.synthetic.main.activity_access.*
import kotlinx.android.synthetic.main.activity_transcription.*
import org.java_websocket.client.WebSocketClient
import org.java_websocket.drafts.Draft
import org.java_websocket.drafts.Draft_6455
import org.java_websocket.handshake.ServerHandshake
import java.lang.Exception
import java.lang.Long
import java.net.URI
import java.nio.ByteBuffer
import java.util.*

class Access : AppCompatActivity(), ConnectivityReceiver.ConnectivityReceiverListener {

    var timing = false
    var hex1 = ""
    var hex2 = ""
    var onrepeat = false
    var open = false
    var onclose = false
    var oldtext = ""
    var newtext = ""
    var salto_linea = "\n"
    lateinit var mclient: ChatClient
    lateinit var subsFragment: SubsFragment
    var isHex = false
    lateinit var regex: Regex
    var info_trans = ""
    lateinit var toInt : String
    lateinit var toInt2 :String
    lateinit var wifi:String
    var trans=false
    lateinit var connectivityReceiver: ConnectivityReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_access)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        connectivityReceiver = ConnectivityReceiver()
        registerReceiver(connectivityReceiver, //wifi
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))


        val cambiar_wifi: Button = findViewById(R.id.cambiar_wifi) as Button
        cambiar_wifi.setOnClickListener {
            startActivity(Intent(WifiManager.ACTION_PICK_WIFI_NETWORK))
        }

        profPin.setFilters(arrayOf(InputFilter.LengthFilter(4), InputFilter.AllCaps()))
        profPin.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                deviceOnline.text=""
                deviceOnline.visibility = View.INVISIBLE
                b2.isEnabled = profPin.text.toString().length >= 4
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                b2.isEnabled=false
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
//        if(ciegos_mode.isChecked) {

        b2.setOnClickListener {
            var imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(b2.getWindowToken(), 0)
            if (profPin.text.toString().length == 4) {
                isHex = true //para saber si nos han introducido texto hexadecimal
                regex = "^[0-9a-fA-F]+$".toRegex() //expresion regular para comprobar que nos han introducido un hexadecimal
                isHex = regex.matches(profPin.text.toString()) //averiguamos si el pin es hexadecimal
                if (isHex) { // si hexadecimal=true
                    mclient = ChatClient(URI(getIP()), Draft_6455(), emptyMap(), 100000)
                    mclient.connect()
                    var count = Countdown()
                    count.start()
                    deviceOnline.visibility = View.VISIBLE
                } else { // si hexadecimal=false
                    deviceOnline.setText("El PIN introducido es incorrecto")
                    deviceOnline.visibility = View.VISIBLE
                }
            }
        }
        //  }
        //else {
        /*profPin.setOnKeyListener(View.OnKeyListener { v, keyCode, event -> //NO BORRAR, ES PARA DARLE A "CONTINUAR" DESDE EL TECLADO
            if (profPin.text.toString().length == 4 && keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                if (profPin.text.toString().length == 4) {
                    isHex = true //para saber si nos han introducido texto hexadecimal
                    regex = "^[0-9a-fA-F]+$".toRegex() //expresion regular para comprobar que nos han introducido un hexadecimal
                }
                isHex = regex.matches(profPin.text.toString()) //averiguamos si el pin es hexadecimal
                if (isHex) { // si hexadecimal=true
                    var imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(b2.getWindowToken(), 0)
                    mclient = ChatClient(URI(getIP()), Draft_6455(), emptyMap(), 100000)
                    profPin.setText("")
                    mclient.connect()
                    var count = Countdown()
                    count.start()
                    deviceOnline.visibility = View.VISIBLE
                } else { // si hexadecimal=false
                    deviceOnline.setText("El PIN introducido es incorrecto")
                    deviceOnline.visibility = View.VISIBLE
                }
                return@OnKeyListener true
            } else if(profPin.text.toString().length != 4){
                deviceOnline.setText("El PIN no está completo")
                deviceOnline.visibility = View.VISIBLE
            }
            false

        })*/
        // }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                val builder = AlertDialog.Builder(this@Access)
                builder.setMessage("¿Desea salir de la sesión?")
                builder.setNegativeButton("Salir") { _, _ ->
                    onBackPressed()
                }
                builder.setPositiveButton("Seguir") { _, _ ->
                }
                builder.show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    override fun onBackPressed() {
        super.onBackPressed()
        if (open) {
            profPin.setText("")
            trans = false
            mclient.close()
        }
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(connectivityReceiver)
    }


    fun getIP(): String {
        hex1 = profPin.text.substring(0, 2)
        hex2 = profPin.text.substring(2, 4)
        var SERVER_URL = ""
        toInt = Long.parseLong(hex1, 16).toString()
        toInt2 = Long.parseLong(hex2, 16).toString()
        val prefixIp = splitIp()
        SERVER_URL = "ws://$prefixIp.$toInt.$toInt2:8080"
        return SERVER_URL
    }
    override fun onResume() {
        super.onResume()
        ConnectivityReceiver.connectivityReceiverListener = this
    }
    private fun showMessage(isConnected: Boolean) {
        if (Build.VERSION.SDK_INT <=27) {
            val connManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            if (!isConnected || !mWifi.isConnected) {
                deviceOnline.text = ""
                wifiname.text = "No hay conexión WIFI"
                profPin.setEnabled(false)
                b2.isEnabled = false
            } else {
                deviceOnline.text = ""
                wifiname.text = mWifi.extraInfo.replace("\"", "") //obtenemos nombre del wifi sin comillas
                wifi = mWifi.extraInfo.replace("\"", "")
                wifiname.visibility = View.VISIBLE
                profPin.setEnabled(true)
                b2.isEnabled = true
            }
        } else {
            val wifiManager = getApplicationContext().getSystemService(Context.WIFI_SERVICE) as WifiManager
            val wifiInfo = wifiManager.connectionInfo
            if (wifiInfo.getSupplicantState() == SupplicantState.COMPLETED) {
                wifiname.text = wifiInfo.getSSID().replace("\"", "");
            }
        }
        wifiname.visibility = View.VISIBLE
    }
    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        showMessage(isConnected)
    }
    inner class ChatClient(url: URI, draft: Draft, httpHeaders: Map<String, String>, Timeout: Int) : WebSocketClient(url, draft, httpHeaders, Timeout) {
        fun setURI(urin: URI) {
            this.uri = urin
        }
        override fun onOpen(handshakedata: ServerHandshake?) {
            subsFragment = SubsFragment()
            open = true
            trans=true
            profPin.isClickable = false
            b2.isClickable = false
            runOnUiThread {
                val fragmentManager = getSupportFragmentManager()
                val transaction = fragmentManager.beginTransaction()
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                transaction.add(android.R.id.content, subsFragment).commit()
            }
            Log.e("Open: ", "new connection opened")
        }
        override fun onMessage(message: String) {
            runOnUiThread {
                //                var imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                profText.setOnTouchListener { v, m ->
//                    val imm = v.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                    imm?.hideSoftInputFromWindow(v.windowToken, 0)
//                    true
//                }
//                if (timing) {
//                    countDownParrafo.cancel()
//                    countDownParrafo.start()
//                } else {
//                    countDownParrafo.start()
//                    timing = true
//                }
                oldtext = "$newtext $message"
                subsFragment.escribirSubs(message, newtext)
            }
            Log.e("---------- Mensaje:", message)
        }
        override fun onMessage(message: ByteBuffer) {
            if (Arrays.toString(message.array()) == "[0, 0, 1, 1]") {
                runOnUiThread {
                    wifi_text.text=wifiname.text
                    code_text.text=profPin.text
                    onrepeat = true
                    val builder = AlertDialog.Builder(this@Access)
                    builder.setMessage("Sesion Finalizada por su instructor")
                    builder.setNegativeButton("Ok") { _, _ ->
                        finish()
                    }
                    builder.show()
                }
            } else if (Arrays.toString(message.array()) == "[1, 1, 0, 0]") {
                newtext = oldtext
            } else if (Arrays.toString(message.array()) == "[0, 1, 1, 0]") {
                oldtext = "$newtext $salto_linea"
                subsFragment.appendSalto(salto_linea, newtext)
                Log.i("BYTES", "SALTO")
                Log.i("CONTENT", oldtext)
            }
            Log.e("Bytes: ", onclose.toString())
        }
        override fun onClose(code: Int, reason: String?, remote: Boolean) {
            open = false
            runOnUiThread {
                if(trans) {
                    val builder = AlertDialog.Builder(this@Access)
                    builder.setMessage("Sesion Finalizada")
                    builder.setNegativeButton("Ok") { _, _ ->
                        finish()
                    }
                    trans=false
                    builder.show()
                }
            }
            Log.e("Close: ", "closed with exit code $code additional info: $reason")
        }
        override fun onError(ex: Exception) {
            open = false
            trans=false
            Log.e("Error: ", "an error occurred:$ex")
        }
    }
    class SubsFragment : DialogFragment() {
        private var rootView: View? = null
        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            rootView = inflater.inflate(R.layout.activity_transcription, container, false)
            return rootView
        }
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            return super.onCreateDialog(savedInstanceState)
        }
        override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
            menu!!.clear()
        }
        override fun onOptionsItemSelected(item: MenuItem?): Boolean {
            val id = item!!.itemId
            if (id == android.R.id.home) {
                // handle close button click here
                dismiss()
                return true
            }
            return super.onOptionsItemSelected(item)
        }
        fun escribirSubs(message: String, newtext: String) {
            // if(ciegos_mode.isChecked)
            profText.text = Editable.Factory.getInstance().newEditable("$newtext $message")
            //if(message==""){
            //profText.text=Editable.Factory.getInstance().newEditable("\n$profText.text")
            //}
            //else
            // profText.text = Editable.Factory.getInstance().newEditable("$newtext $message")
        }
        fun appendSalto(message: String, newtext: String) {
            if (!profText.text.endsWith("\n") || profText.text.length > 0)
                profText.text = Editable.Factory.getInstance().newEditable("$newtext $message")
        }
        companion object {
            private val TAG = "Access"
        }
    }
    inner class Countdown() : CountDownTimer(3000, 1000) {
        override fun onTick(p0: kotlin.Long) {
        }
        override fun onFinish() {
            if (!open)
                deviceOnline.setText("No se encuentra conexión")
            else {
                //todo put buttons to not clickable
            }
        }
    }
    fun getIp(): String {
        val wifiMgr = applicationContext.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        @SuppressLint("MissingPermission") val wifiInfo = wifiMgr!!.connectionInfo
        val ip = wifiInfo.ipAddress
        Log.d("DEBUG" , (ip).toString())
        val ipAddress = Formatter.formatIpAddress(ip)
        return ipAddress
    }
    fun splitIp(): String {
        val ip = getIp()
        val ipNumbers = ip.split("[.]".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return ipNumbers[0] + "." + ipNumbers[1]
    }
}