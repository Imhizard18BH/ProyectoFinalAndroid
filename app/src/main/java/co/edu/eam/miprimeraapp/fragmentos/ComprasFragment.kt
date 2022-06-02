package co.edu.eam.miprimeraapp.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import co.edu.eam.miprimeraapp.adapter.CompraAdapter
import co.edu.eam.miprimeraapp.databinding.FragmentComprasBinding
import co.edu.eam.miprimeraapp.modelo.Compra

class ComprasFragment : Fragment() {

    lateinit var binding:FragmentComprasBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentComprasBinding.inflate(inflater, container, false)

        val listaCompra = ArrayList<Compra>()

        if(listaCompra.isEmpty()){
            binding.textoVacio.isGone = false
        }else{
            val adapter = CompraAdapter(listaCompra)
            binding.listaCompras.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            binding.listaCompras.adapter = adapter
        }

        return binding.root
    }

}