package co.edu.eam.miprimeraapp.fragmentos

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import co.edu.eam.miprimeraapp.databinding.FragmentDialogoCompraBinding
import co.edu.eam.miprimeraapp.modelo.Compra
import co.edu.eam.miprimeraapp.modelo.Producto
import co.edu.eam.miprimeraapp.util.FirebaseManager
import com.google.firebase.auth.FirebaseAuth
import java.util.*
import kotlin.collections.ArrayList

class DialogoCompraFragment :  DialogFragment() {

    lateinit var binding:FragmentDialogoCompraBinding
    var listaProductos:ArrayList<Producto> = ArrayList()
    lateinit var listener:CompraRealizada

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentDialogoCompraBinding.inflate(inflater, container, false)

        val opcionesPago = arrayListOf("PSE", "Tarjeta de crédito", "Nequi", "Transferencia")
        val adapterPago = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, opcionesPago)
        adapterPago.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.medioPago.adapter = adapterPago

        val opcionesEnvio = arrayListOf("Envío a domicilio", "Retiro en tienda")
        val adapterEnvio = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, opcionesEnvio)
        adapterEnvio.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.medioEnvio.adapter = adapterEnvio

        binding.btnConfirmar.setOnClickListener {

            val usuario = FirebaseAuth.getInstance().currentUser

            if(usuario!=null) {

                var medioPago = binding.medioPago.selectedItem.toString()
                var medioEnvio = binding.medioEnvio.selectedItem.toString()

                var listaIds: ArrayList<String> = ArrayList()
                var listaUnidades: ArrayList<Int> = ArrayList()
                var listaPrecios: ArrayList<Float> = ArrayList()

                listaProductos.forEach { p ->
                    listaIds.add(p.key)
                    listaUnidades.add(1)
                    listaPrecios.add(p.calcularNuevoPrecio())
                }

                var compra = Compra(listaIds, listaPrecios, listaUnidades, Date())
                compra.medioEnvio = medioEnvio
                compra.metodoPago = medioPago

                FirebaseManager.guardarCompra(compra, usuario.uid)

                listener.compraRealizada()

            }
        }

        return binding.root
    }

    interface CompraRealizada{
        fun compraRealizada()
    }

}