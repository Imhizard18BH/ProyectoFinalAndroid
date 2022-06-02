package co.edu.eam.miprimeraapp.fragmentos

import android.graphics.Canvas
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import co.edu.eam.miprimeraapp.R
import co.edu.eam.miprimeraapp.adapter.ProductoAdapter
import co.edu.eam.miprimeraapp.databinding.FragmentListaProductosBinding
import co.edu.eam.miprimeraapp.modelo.Producto
import co.edu.eam.miprimeraapp.util.FirebaseManager
import com.google.android.material.snackbar.Snackbar
import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator

class ListaProductosFragment : Fragment(), FirebaseManager.OnActualizarAdaptador {

    lateinit var binding:FragmentListaProductosBinding
    lateinit var adapterProductos:ProductoAdapter
    lateinit var listaProductos:ArrayList<Producto>
    var codigoCategoria: Int = -1
    var tipoFragmento:Int = -1
    var textoBusqueda:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseManager.listener = this
        FirebaseManager.escucharEventosProductos()

        if(arguments!=null){
            codigoCategoria = requireArguments().getInt("categoria-id")
            tipoFragmento = requireArguments().getInt("tipo")
            textoBusqueda = requireArguments().getString("busqueda", "")
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListaProductosBinding.inflate(inflater, container, false)

        /*listaProductos = when(tipoFragmento){
            1 -> ArrayList() //TiendaDbHelper.getInstance(requireContext()).obtenerProductos(codigoCategoria)
            2 -> Productos.listarOfertas()
            3 -> Favoritos.listar()
            else -> Productos.buscar(textoBusqueda)
        }*/

        listaProductos = ArrayList()

        adapterProductos = ProductoAdapter(listaProductos)
        binding.listaProductos.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.listaProductos.adapter = adapterProductos

        if(tipoFragmento==3) {
            crearFuncionSwipe()
        }

        mostrarMensajeVacio()

        return binding.root
    }

    fun mostrarMensajeVacio(){
        if(listaProductos.isEmpty()) {
            binding.textoVacio.isGone = false
            when (tipoFragmento) {
                1 -> binding.textoVacio.text = getString(R.string.sin_productos)
                2 -> binding.textoVacio.text = getString(R.string.sin_ofertas)
                3 -> binding.textoVacio.text = getString(R.string.sin_favoritos)
            }
        }
    }

    fun crearFuncionSwipe(){

        val simpleCallback: ItemTouchHelper.SimpleCallback = object :
            ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT /*or ItemTouchHelper.RIGHT*/
            ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                val producto = listaProductos[pos]

                when (direction) {
                    ItemTouchHelper.LEFT -> {

                        //Favoritos.eliminar(producto.key)
                        //adapterProductos.notifyItemRemoved(pos)

                        Snackbar.make(
                            binding.listaProductos,
                            "${producto.nombre} se ha eliminado!",
                            Snackbar.LENGTH_LONG
                        )
                            .setAction("Deshacer") {
                                //Favoritos.agregar(producto, pos)
                                //adapterProductos.notifyItemInserted(pos)
                            }.show()

                    }
                    /*ItemTouchHelper.RIGHT -> {
                    Log.e("ListaProductosActivity", "Swipe derecha")
                }*/
                }
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {

                RecyclerViewSwipeDecorator.Builder(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(context!!, R.color.red))
                    .addSwipeLeftLabel("Eliminar")
                    .setSwipeLeftLabelColor(ContextCompat.getColor(context!!, R.color.white))
                    .setSwipeLeftLabelTextSize(TypedValue.COMPLEX_UNIT_SP, 18f)
                    .create()
                    .decorate()

                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
            }

        }

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(binding.listaProductos)
    }

    companion object{

        fun newInstance(codigoCategoria:Int = -1, tipoFragmento:Int, textoBusqueda:String = ""):ListaProductosFragment{
            var bundle = Bundle()
            bundle.putInt("categoria-id", codigoCategoria)
            bundle.putInt("tipo", tipoFragmento)
            bundle.putString("busqueda", textoBusqueda)

            var fragment = ListaProductosFragment()
            fragment.arguments = bundle

            return fragment
        }

    }

    override fun productoAgregado(producto: Producto) {
        if( producto.categorias.contains(codigoCategoria) ) {
            listaProductos.add(producto)
            adapterProductos.notifyItemInserted(listaProductos.size - 1)
            binding.textoVacio.isGone = true
        }
    }

}