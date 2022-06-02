package co.edu.eam.miprimeraapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding

abstract class GenericoAdapter<T, D:ViewBinding>(var lista:ArrayList<T?>) : RecyclerView.Adapter<GenericoAdapter<T,D>.ViewHolder>() {

    abstract fun onBindData(dato:T?, pos:Int, view:D)

    abstract fun onItemClick(dato:T?, pos:Int)

    abstract fun inflateView(inflater: LayoutInflater, parent: ViewGroup): D

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = inflateView(LayoutInflater.from(parent.context), parent)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        onBindData( lista[position], position, holder.binding)
        holder.binding.root.setOnClickListener { onItemClick(lista[position], position) }
    }

    override fun getItemCount(): Int = lista.size

    inner class ViewHolder(vista:ViewBinding) : RecyclerView.ViewHolder(vista.root){
        var binding:D = vista as D
    }

}