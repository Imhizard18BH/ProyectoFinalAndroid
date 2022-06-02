package co.edu.eam.miprimeraapp.util

import android.content.Context
import android.widget.Toast
import co.edu.eam.miprimeraapp.db.TiendaDbHelper
import java.text.NumberFormat
import java.util.*

object Utilidades {

    fun mostrarMensaje(context:Context, texto:String){
        Toast.makeText(context, texto, Toast.LENGTH_LONG).show()
    }

    fun formatearDinero(valor:Float):String{
        val locale = Locale("es", "CO")
        val currencyFormatter: NumberFormat = NumberFormat.getCurrencyInstance(locale)
        return currencyFormatter.format( valor )
    }

}