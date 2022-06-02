package co.edu.eam.miprimeraapp.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import co.edu.eam.miprimeraapp.fragmentos.DetalleProductoFragment
import co.edu.eam.miprimeraapp.fragmentos.ListaComentariosFragment

class ViewPagerAdapter(var fragment:FragmentActivity, var codigoProducto:String) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return if(position == 0){
            DetalleProductoFragment.newInstance(codigoProducto)
        }else{
            ListaComentariosFragment.newInstance(codigoProducto)
        }
    }


}