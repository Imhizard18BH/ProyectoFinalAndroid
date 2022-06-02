package co.edu.eam.miprimeraapp.fragmentos

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.LinearLayoutManager
import co.edu.eam.miprimeraapp.adapter.ComentarioAdapter
import co.edu.eam.miprimeraapp.databinding.FragmentListaComentariosBinding
import co.edu.eam.miprimeraapp.modelo.Comentario
import co.edu.eam.miprimeraapp.modelo.Producto
import co.edu.eam.miprimeraapp.util.FirebaseManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import java.util.*
import kotlin.collections.ArrayList


class ListaComentariosFragment : Fragment() {

    lateinit var binding: FragmentListaComentariosBinding
    var listaComentarios:ArrayList<Comentario> = ArrayList()
    var codigoProducto:String = ""
    lateinit var adapter:ComentarioAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if(arguments!=null){
            codigoProducto = requireArguments().getString("producto-id", "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListaComentariosBinding.inflate(inflater, container, false)

        adapter = ComentarioAdapter(listaComentarios)
        binding.listaComentarios.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.listaComentarios.adapter = adapter

        if(listaComentarios.isEmpty()) {
            binding.txtSinComentarios.isGone = false
        }

        FirebaseManager.dataRef.child("productos").child(codigoProducto).child("comentarios").addChildEventListener(object : ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val comentario = snapshot.getValue(Comentario::class.java)
                if(comentario!=null){
                    comentario.key = snapshot.key!!
                    listaComentarios.add(comentario)
                    adapter.notifyItemInserted(listaComentarios.size-1)
                    binding.txtSinComentarios.isGone = true
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        binding.comentarProducto.setOnClickListener { comentar() }

        return binding.root
    }

    fun comentar(){
        val mensaje = binding.mensajeComentario.text

        if(mensaje.isNotEmpty()) {

            val user = FirebaseAuth.getInstance().currentUser

            if(user!=null) {

                val comentario = Comentario(mensaje.toString(), Date(), user.uid, 0)
                FirebaseManager.guardarComentario(comentario, codigoProducto)

                binding.mensajeComentario.setText("")
                binding.txtSinComentarios.isGone = true

            }
        }
    }

    companion object{

        fun newInstance(codigoProducto:String): ListaComentariosFragment{
            val args = Bundle()
            args.putString("producto-id", codigoProducto)

            val fragment = ListaComentariosFragment()
            fragment.arguments = args

            return fragment
        }

    }

}