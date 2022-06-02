package co.edu.eam.miprimeraapp.fragmentos

import android.graphics.Paint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import co.edu.eam.miprimeraapp.databinding.FragmentDetalleProductoBinding
import co.edu.eam.miprimeraapp.db.TiendaDbHelper
import co.edu.eam.miprimeraapp.modelo.Producto
import co.edu.eam.miprimeraapp.util.FirebaseManager
import co.edu.eam.miprimeraapp.util.Utilidades
import com.google.android.material.chip.Chip
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class DetalleProductoFragment : Fragment() {

    lateinit var binding:FragmentDetalleProductoBinding
    lateinit var db:TiendaDbHelper
    lateinit var codigoProducto:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        db = TiendaDbHelper.getInstance(requireContext())

        if(arguments != null){
            codigoProducto = requireArguments().getString("producto-id", "")
            //producto = Productos.obtener(codigoProducto)
            //producto = db.obtenerProducto(codigoProducto)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetalleProductoBinding.inflate(inflater, container, false)

        FirebaseManager.dataRef.child("productos").child(codigoProducto).get().addOnCompleteListener {
            if(it.isSuccessful) {
                val producto = it.result!!.getValue(Producto::class.java)
                if (producto != null) {
                    producto.key = it.result!!.key!!
                    cargarInformacion(producto)
                }
            }
        }

        return binding.root
    }

    fun cargarInformacion(producto: Producto){
        binding.nombreProducto.text = producto!!.nombre
        binding.precioProducto.text = Utilidades.formatearDinero(producto!!.precio)

        if(producto!!.descuento > 0){
            binding.precioProducto.paintFlags = binding.precioProducto.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            binding.descuentoProducto.text = "${String.format("%.0f", producto!!.descuento*100 )} % OFF"
            binding.descuentoProducto.isGone = false

            val nuevoPrecio = producto!!.calcularNuevoPrecio()
            binding.nuevoPrecioProducto.text = Utilidades.formatearDinero(nuevoPrecio)
            binding.nuevoPrecioProducto.isGone = false
        }

        binding.descProducto.text = producto!!.descripcion

        val categorias = producto!!.categorias

        for( catID in categorias ){
            var chip = Chip(context)
            //val cat = Categorias.obtener(catID)!!
            val cat = db.obtenerCategoria(catID)!!
            chip.id = cat.codigo
            chip.text = cat.nombre
            chip.isCheckable = true
            binding.categorias.addView( chip )
        }

    }

    companion object{

        fun newInstance(codigoProducto:String):DetalleProductoFragment{
            var args = Bundle()
            args.putString("producto-id", codigoProducto)
            val fragment = DetalleProductoFragment()
            fragment.arguments = args

            return fragment
        }

    }

}