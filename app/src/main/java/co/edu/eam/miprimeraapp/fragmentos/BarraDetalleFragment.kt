package co.edu.eam.miprimeraapp.fragmentos

import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import co.edu.eam.miprimeraapp.R
import co.edu.eam.miprimeraapp.actividades.CarritoActivity
import co.edu.eam.miprimeraapp.databinding.FragmentBarraDetalleBinding
import co.edu.eam.miprimeraapp.util.FirebaseManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class BarraDetalleFragment : Fragment() {

    lateinit var binding:FragmentBarraDetalleBinding
    private var codigoProducto:String = ""
    private var esFavorito:Boolean = false
    private var typefaceSolid:Typeface? = null
    private var typefaceRegular:Typeface? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        typefaceSolid = ResourcesCompat.getFont(requireContext(), R.font.font_awesome_6_free_solid_900)
        typefaceRegular = ResourcesCompat.getFont(requireContext(), R.font.font_awesome_6_free_regular_400)

        if(arguments!=null){
            codigoProducto = requireArguments().getString("producto-id", "")
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBarraDetalleBinding.inflate(inflater, container, false)

        binding.btnFavorito.setOnClickListener {
            if(codigoProducto.isNotEmpty()){
                marcarFavorito()
            }
        }

        binding.btnCarrito.setOnClickListener {
            startActivity( Intent(context, CarritoActivity::class.java) )
        }

        return binding.root
    }

    fun marcarFavorito(){
        var mensaje = ""
        val usuario = FirebaseAuth.getInstance().currentUser

        if(usuario!=null) {

            if (!esFavorito) {
                //binding.btnFavorito.text = '\uf004'.toString()
                //binding.btnFavorito.setTextColor( ContextCompat.getColor(requireContext(), R.color.purple_500) )

                mensaje = getString(R.string.producto_fav_msj_1)
                binding.btnFavorito.typeface = typefaceSolid

                FirebaseManager.guardarFavorito(codigoProducto, usuario.uid)
                esFavorito = true
            } else {
                mensaje = getString(R.string.producto_fav_msj_2)
                binding.btnFavorito.typeface = typefaceRegular

                FirebaseManager.eliminarFavorito(codigoProducto, usuario.uid)
                esFavorito = false
            }

            Snackbar.make(
                binding.root,
                mensaje,
                Snackbar.LENGTH_LONG
            ).show()

        }
    }

    companion object{

        fun newInstance(codigoProducto:String):BarraDetalleFragment{
            var bundle = Bundle()
            bundle.putString("producto-id", codigoProducto)

            var fragment = BarraDetalleFragment()
            fragment.arguments = bundle

            return fragment
        }

    }

}