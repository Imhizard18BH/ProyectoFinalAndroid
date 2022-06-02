package co.edu.eam.miprimeraapp.actividades

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import co.edu.eam.miprimeraapp.databinding.ActivityListaProductosBinding
import co.edu.eam.miprimeraapp.fragmentos.ListaProductosFragment

class ListaProductosActivity : AppCompatActivity() {

    lateinit var binding: ActivityListaProductosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListaProductosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val codigoCategoria = intent.extras?.getInt("categoria-id")
        val nombreCategoria = intent.extras?.getString("categoria-nombre")

        if(codigoCategoria!=null) {
            val fragment = ListaProductosFragment.newInstance(codigoCategoria = codigoCategoria, tipoFragmento = 1)

            supportFragmentManager.beginTransaction()
                .replace(binding.listaProductos.id, fragment)
                .commit()

            binding.tituloCategoria.text = nombreCategoria
        }

    }
}