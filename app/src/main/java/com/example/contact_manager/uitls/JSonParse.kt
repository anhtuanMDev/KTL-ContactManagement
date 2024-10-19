package com.example.contact_manager.uitls

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.contact_manager.model.Contact
import kotlinx.serialization.json.Json

class JSonParse(context: Context) {

    private var context: Context? = null;

    init {
        this.context = context;
    }

    fun parseJson(fielName: String) : List<Contact>? {

        val jsonString = context?.assets?.open(fielName)?.bufferedReader().use { it?.readText() }
        return try {
            if (jsonString != null) {
                Json.decodeFromString<List<Contact>>(jsonString)
            }
            else {
                throw Error("parsing JSON res, jsonString is null")
            }
        } catch (e: Exception) {
            Log.d("ERROR PARSE JSON", e.message.toString());
            Toast.makeText(context, "error " +e.message, Toast.LENGTH_LONG).show();
            null
        }
    }

}