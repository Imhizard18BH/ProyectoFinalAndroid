package co.edu.eam.miprimeraapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import co.edu.eam.miprimeraapp.databinding.ItemCompraBinding
import co.edu.eam.miprimeraapp.modelo.Compra
import co.edu.eam.miprimeraapp.util.Utilidades

class CompraAdapter(private var lista:ArrayList<Compra>): RecyclerView.Adapter<CompraAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemCompraBinding.inflate( LayoutInflater.from(parent.context), parent, false )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindCompra( lista[position] )
    }

    override fun getItemCount() = lista.size

    class ViewHolder (var view:ItemCompraBinding):RecyclerView.ViewHolder(view.root){

        fun bindCompra(compra:Compra){
            view.txtTitulo.text = "Compra"
            view.txtFecha.text = compra.fecha.toString()
            view.txtPrecio.text = Utilidades.formatearDinero( compra.precio.sum() )
        }
    }
}