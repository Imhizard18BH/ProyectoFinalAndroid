package co.edu.eam.miprimeraapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.edu.eam.miprimeraapp.databinding.ItemComentarioBinding
import co.edu.eam.miprimeraapp.modelo.Comentario
import java.text.SimpleDateFormat

class ComentarioAdapter(var lista:ArrayList<Comentario>): RecyclerView.Adapter<ComentarioAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemComentarioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindComentario(lista[position])
    }

    override fun getItemCount(): Int = lista.size

    inner class ViewHolder(private var view: ItemComentarioBinding) :
        RecyclerView.ViewHolder(view.root) {

        fun bindComentario(comentario: Comentario) {

            val sdf = SimpleDateFormat("dd/MM")

            view.txtUsuario.text = "Nombre x"
            view.txtFecha.text = sdf.format(comentario.fecha)
            view.txtComentario.text = comentario.mensaje
        }

    }

}