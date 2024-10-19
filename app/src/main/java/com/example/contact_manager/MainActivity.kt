package com.example.contact_manager

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.contact_manager.databinding.ActivityMainBinding
import com.example.contact_manager.model.Contact
import com.example.contact_manager.uitls.JSonParse
import com.example.contact_manager.utils.ListViewAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private val bind: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    };
    private lateinit var fab_addContact: FloatingActionButton
    private lateinit var listView: ListView
    private lateinit var progress_view: ConstraintLayout
    lateinit var dialog: AlertDialog
    var dialog_alert: AlertDialog? = null
    var jsonRes = JSonParse(this);
    var res: List<Contact>? = null
    private lateinit var adapter: ListViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(bind.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        progress_view = bind.progressLayout
        fab_addContact = bind.fabAddContact
        listView = bind.listView

        res = jsonRes.parseJson("small.json");

        if (res != null) {
            adapter = ListViewAdapter(this, res!!)
            listView.adapter = adapter
        } else {
            alertDialog(
                R.drawable.baseline_error_24,
                getString(R.string.dialog_title_error),
                getString(R.string.dialog_error_subtittle),
                R.color.red
            )
        }

        progress_view.visibility = View.GONE
        showDialog()

    }

    override fun onStart() {
        super.onStart()
        fab_addContact.setOnClickListener {
            dialog.show()
            Toast.makeText(this, "Button Clicked", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showDialog() {
        val dialog_view = LayoutInflater.from(this).inflate(R.layout.dialog_add_contact, null)
        val builder = AlertDialog.Builder(this)
            .setView(dialog_view)
            .setTitle(R.string.dialog_title);
        dialog = builder.create();
        lateinit var dialog_button_1: TextView;
        lateinit var dialog_button_2: TextView;
        lateinit var dialog_button_3: TextView;
        lateinit var dialog_button_4: TextView;

        dialog_button_1 = dialog_view.findViewById(R.id.dialog_button_1);
        dialog_button_2 = dialog_view.findViewById(R.id.dialog_button_2);
        dialog_button_3 = dialog_view.findViewById(R.id.dialog_button_3);
        dialog_button_4 = dialog_view.findViewById(R.id.dialog_button_4);

        dialog.setOnCancelListener() {
            dialog.dismiss();
        }

        dialog_button_1.setOnClickListener {
            changeContactData(dialog, "small.json", 1000)
        }

        dialog_button_2.setOnClickListener {
            changeContactData(dialog, "medium.json", 1000)
        }

        dialog_button_3.setOnClickListener {
            changeContactData(dialog, "big.json", 1000)
        }

        dialog_button_4.setOnClickListener {
            dialog.dismiss();
            val intent = Intent(this, AddContact::class.java)
            startActivity(intent)
        }
    }

    private fun alertDialog(iconRes: Int, title: String, content: String, tintColor: Int) {

        // Inflate a new dialog_alert_view every time the dialog is shown
        val dialog_alert_view = LayoutInflater.from(this).inflate(R.layout.dialog_alert, null)

        val builder = AlertDialog.Builder(this)
            .setView(dialog_alert_view)
        dialog_alert = builder.create()

        // Find views and set the passed values
        val iconView: ImageView = dialog_alert_view.findViewById(R.id.dialogIcon)
        val titleView: TextView = dialog_alert_view.findViewById(R.id.errorTitle)
        val contentView: TextView = dialog_alert_view.findViewById(R.id.errorContent)

        // Set icon, title, content, and tint color dynamically
        iconView.setImageResource(iconRes)
        iconView.setColorFilter(ContextCompat.getColor(this, tintColor), android.graphics.PorterDuff.Mode.SRC_IN)
        titleView.text = title
        contentView.text = content

        dialog_alert!!.setOnCancelListener {
            dialog_alert!!.dismiss()
            Toast.makeText(this, "Dialog Cancelled", Toast.LENGTH_SHORT).show()
            dialog_alert = null
        }

        dialog_alert!!.show()
    }


    private fun changeContactData(dialog: AlertDialog, path: String, timeout: Long) {
        dialog.dismiss()
        progress_view.visibility = View.VISIBLE

        val handler = android.os.Handler()
        handler.postDelayed({
            try {
            val newContact = jsonRes.parseJson(path)
            res = newContact
            adapter.updateContacts(res!!)
            progress_view.visibility = View.GONE
                alertDialog(
                    R.drawable.baseline_cloud_done_24,
                    getString(R.string.dialog_success),
                    getString(R.string.dialog_success_subtitle),
                    R.color.teal_200
                )
            }catch (e: Exception){
                alertDialog(
                    R.drawable.baseline_error_24,
                    getString(R.string.dialog_title_error),
                    getString(R.string.dialog_error_subtittle),
                    R.color.red
                )
            }
        }, timeout)
    }


}