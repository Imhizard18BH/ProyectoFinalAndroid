package co.edu.eam.miprimeraapp.fragmentos

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import co.edu.eam.miprimeraapp.adapter.CategoriaAdapter
import co.edu.eam.miprimeraapp.databinding.FragmentInicioBinding
import co.edu.eam.miprimeraapp.modelo.Categoria
import co.edu.eam.miprimeraapp.util.FirebaseManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

class InicioFragment: Fragment() {

    lateinit var binding: FragmentInicioBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInicioBinding.inflate(inflater, container, false)

        val categorias:ArrayList<Categoria> = ArrayList()
        val adapter = CategoriaAdapter(categorias)
        binding.categorias.layoutManager = GridLayoutManager(context, 3)
        binding.categorias.adapter = adapter

        FirebaseManager.escucharEventosCategorias { c ->
            categorias.add(c)
            adapter.notifyItemInserted(categorias.size - 1)
        }

        return binding.root
    }

}