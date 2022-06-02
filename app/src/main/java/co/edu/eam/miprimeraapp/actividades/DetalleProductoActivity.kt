package co.edu.eam.miprimeraapp.actividades

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import co.edu.eam.miprimeraapp.R
import co.edu.eam.miprimeraapp.adapter.ViewPagerAdapter
import co.edu.eam.miprimeraapp.data.Carrito
import co.edu.eam.miprimeraapp.databinding.ActivityDetalleProductoBinding
import co.edu.eam.miprimeraapp.fragmentos.BarraDetalleFragment
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator

class DetalleProductoActivity : AppCompatActivity() {

    lateinit var binding:ActivityDetalleProductoBinding
    var productoCodigo:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetalleProductoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        productoCodigo = intent.extras?.getString("producto-id")

        supportFragmentManager.beginTransaction()
            .replace( binding.barraSuperior.id, BarraDetalleFragment.newInstance(productoCodigo!!))
            .commit()

        if(productoCodigo!=null) {
            binding.viewPager.adapter = ViewPagerAdapter(this, productoCodigo!!)
            TabLayoutMediator(binding.tabsDetalle, binding.viewPager) { tab, pos ->
                when (pos) {
                    0 -> tab.text = getString(R.string.detalle_producto)
                    1 -> tab.text = getString(R.string.comentarios)
                }
            }.attach()
        }

        binding.btnAgregarCarrito.setOnClickListener {
            Snackbar.make(binding.root, getString(R.string.producto_carrito_msj), Snackbar.LENGTH_LONG).show()
            if(productoCodigo!=null){
                Carrito.agregar( productoCodigo!! )
            }
        }

    }
}