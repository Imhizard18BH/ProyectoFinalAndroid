package co.edu.eam.miprimeraapp.actividades

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isGone
import co.edu.eam.miprimeraapp.databinding.ActivityListaComprasBinding
import co.edu.eam.miprimeraapp.modelo.Compra

class ListaComprasActivity : AppCompatActivity() {

    lateinit var binding:ActivityListaComprasBinding
    private lateinit var compras:ArrayList<Compra>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaComprasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        compras = ArrayList()

        if(compras.isEmpty()){
            binding.txtVacio.isGone = false
        }else{
            Log.e("COMPRAS", compras.toString())
        }

    }



}