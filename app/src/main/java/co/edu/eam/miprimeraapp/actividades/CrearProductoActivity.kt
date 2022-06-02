package co.edu.eam.miprimeraapp.actividades

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import co.edu.eam.miprimeraapp.databinding.ActivityCrearProductoBinding
import co.edu.eam.miprimeraapp.db.TiendaDbHelper
import co.edu.eam.miprimeraapp.modelo.Categoria
import co.edu.eam.miprimeraapp.modelo.Producto
import co.edu.eam.miprimeraapp.util.FirebaseManager
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import java.lang.Exception

class CrearProductoActivity : AppCompatActivity() {

    lateinit var binding:ActivityCrearProductoBinding
    lateinit var categorias:ArrayList<Categoria>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCrearProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        categorias = ArrayList()

        FirebaseManager.escucharEventosCategorias { cat ->
            var chip = Chip(this)
            chip.id = cat.codigo
            chip.text = cat.nombre
            chip.isCheckable = true
            categorias.add(cat)
            binding.categoriasProducto.addView( chip )
        }

        val opciones = arrayListOf("Sin descuento", "Con descuento")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.seleccionDescuento.adapter = adapter

        binding.seleccionDescuento.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, pos: Int, p3: Long) {
                binding.txtDescuento.isEnabled = pos == 1
            }

            override fun onNothingSelected(p0: AdapterView<*>?) { }
        }

        binding.btnCrearProducto.setOnClickListener {

            try{

                val usuario = FirebaseAuth.getInstance().currentUser

                if(usuario!=null){

                    val nombre = binding.txtNombre.text.toString()
                    val precio = binding.txtPrecio.text.toString()
                    val descuento = binding.txtDescuento.text.toString()
                    val descripcion = binding.txtDescripcion.text.toString()
                    val unidades = binding.txtUnidades.text.toString()

                    val lista = binding.categoriasProducto.checkedChipIds

                    if( nombre.isNotEmpty() && precio.isNotEmpty() && descripcion.isNotEmpty() && lista.size > 0 ) {

                        var dctoForm = 0f

                        if(descuento.isNotEmpty()){
                            dctoForm = descuento.toFloat()
                        }

                        val producto = Producto(
                            precio.toFloat(),
                            nombre,
                            descripcion,
                            unidades.toInt(),
                            dctoForm
                        )

                        producto.vendedor = usuario.uid
                        producto.categorias = lista
                        FirebaseManager.guardarProducto(producto)

                        Snackbar.make(binding.root, "El producto se creó correctamente", Snackbar.LENGTH_LONG).show()
                    }else{
                        Snackbar.make(binding.root, "Todos los campos son obligatorios", Snackbar.LENGTH_LONG).show()
                    }

                }

            }catch (e:Exception){
                Snackbar.make(binding.root, e.message.toString(), Snackbar.LENGTH_LONG).show()
            }

        }

    }

}