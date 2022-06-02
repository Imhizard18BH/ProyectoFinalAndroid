package co.edu.eam.miprimeraapp.actividades

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isGone
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import co.edu.eam.miprimeraapp.adapter.ProductoAdapter
import co.edu.eam.miprimeraapp.data.Carrito
import co.edu.eam.miprimeraapp.databinding.ActivityCarritoBinding
import co.edu.eam.miprimeraapp.fragmentos.DialogoCompraFragment
import co.edu.eam.miprimeraapp.modelo.Producto
import co.edu.eam.miprimeraapp.R
import co.edu.eam.miprimeraapp.util.FirebaseManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase

class CarritoActivity : AppCompatActivity(), DialogoCompraFragment.CompraRealizada {

    lateinit var binding:ActivityCarritoBinding
    var listaProductos:ArrayList<Producto> = ArrayList()
    lateinit var dialog:DialogoCompraFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarritoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = ProductoAdapter(listaProductos)
        binding.listaCarrito.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.listaCarrito.adapter = adapter

        val listaKeys = Carrito.obtener()

        listaKeys.forEach {

            FirebaseManager.dataRef.child("productos").child(it).addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val producto = snapshot.getValue(Producto::class.java)
                    if(producto!=null) {
                        producto.key = snapshot.key!!
                        listaProductos.add(producto)
                        adapter.notifyItemInserted( listaProductos.size-1 )

                        binding.btnComprar.isGone = false
                        binding.msjVacio.isGone = true

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })

        }

        if(listaProductos.isNotEmpty()){
            binding.btnComprar.isGone = false
            binding.msjVacio.isGone = true
        }

        binding.btnComprar.setOnClickListener { comprarProductos() }

    }

    fun comprarProductos(){
        dialog = DialogoCompraFragment()
        dialog.setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogoTitulo)
        dialog.listaProductos = listaProductos
        dialog.listener = this
        dialog.show(supportFragmentManager, "Confirmar compra")
    }

    override fun compraRealizada() {
        dialog.dismiss()
        Snackbar.make( binding.root, getString(R.string.compra_realizada), Snackbar.LENGTH_LONG).show()
        listaProductos.clear()
        binding.btnComprar.isGone = true
        binding.msjVacio.isGone = false
    }
}