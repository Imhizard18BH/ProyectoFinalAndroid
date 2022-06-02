package co.edu.eam.miprimeraapp.actividades

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import co.edu.eam.miprimeraapp.R
import co.edu.eam.miprimeraapp.data.Categorias
import co.edu.eam.miprimeraapp.databinding.ActivityMainBinding
import co.edu.eam.miprimeraapp.db.TiendaDbHelper
import co.edu.eam.miprimeraapp.fragmentos.*
import co.edu.eam.miprimeraapp.util.Idioma
import co.edu.eam.miprimeraapp.util.Utilidades
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var resultLauncher:ActivityResultLauncher<Intent>
    lateinit var binding:ActivityMainBinding
    private val MENU_HOME = "HOME"
    private val MENU_OFERTAS = "OFERTAS"
    private val MENU_FAVORITOS = "FAVORITOS"
    private val MENU_COMPRAS = "COMPRAS"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        agregarCategoriasDB()

        reemplazarFragmento(1, "HOME")

        binding.barraInferior.setOnItemSelectedListener { item ->
            when(item.itemId){
                R.id.menu_home -> reemplazarFragmento(1, MENU_HOME)
                R.id.menu_ofertas -> reemplazarFragmento(2, MENU_OFERTAS)
                R.id.menu_favs -> reemplazarFragmento(3, MENU_FAVORITOS)
                R.id.menu_compras -> reemplazarFragmento(4, MENU_COMPRAS)
            }
            true
        }

        binding.drawerLayout.setScrimColor(Color.TRANSPARENT)

        val actionBarDrawerToggle: ActionBarDrawerToggle =
            object : ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close) {
                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                    super.onDrawerSlide(drawerView, slideOffset)
                    val slideX: Float = drawerView.width * slideOffset
                    binding.layoutContent.translationX = slideX
                }
            }

        binding.drawerLayout.addDrawerListener(actionBarDrawerToggle)
        binding.navView.setNavigationItemSelectedListener(this)

    }

    fun agregarCategoriasDB(){
        val db = TiendaDbHelper.getInstance(this)
        if( db.obtenerCategorias().isEmpty() ){
            Categorias.getLista().forEach { c -> db.crearCategoria(c) }
        }
    }

    fun reemplazarFragmento(pos:Int, nombre:String){

        var fragmento:Fragment = when( pos ){
            1 -> InicioFragment()
            2 -> ListaProductosFragment.newInstance(tipoFragmento = 2)
            3 -> ListaProductosFragment.newInstance(tipoFragmento = 3)
            else -> ComprasFragment()
        }

        supportFragmentManager.beginTransaction().replace( binding.contenido.id, fragmento )
            .addToBackStack(nombre)
            .commit()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val count = supportFragmentManager.backStackEntryCount
        val nombre = supportFragmentManager.getBackStackEntryAt(count-1).name

        when(nombre){
            MENU_HOME -> binding.barraInferior.menu.getItem(0).isChecked = true
            MENU_OFERTAS -> binding.barraInferior.menu.getItem(1).isChecked = true
            MENU_FAVORITOS -> binding.barraInferior.menu.getItem(2).isChecked = true
            else -> binding.barraInferior.menu.getItem(3).isChecked = true
        }

    }

    override fun attachBaseContext(newBase: Context?) {
        val localeUpdatedContext: ContextWrapper? = Idioma.cambiarIdioma(newBase!!)
        super.attachBaseContext(localeUpdatedContext)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when( item.itemId ){

            R.id.menu_cerrar_sesion -> cerrarSesion()
            R.id.menu_crear_producto -> startActivity( Intent(this, CrearProductoActivity::class.java) )
            R.id.menu_cambiar_idioma -> cambiarIdioma()

        }

        item.isChecked = true
        //binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun cambiarIdioma(){
        Idioma.selecionarIdioma(this)
        if (intent != null) {
            intent.flags = (Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            finish()
            startActivity(intent)
        }
    }

    fun cerrarSesion(){
        Firebase.auth.signOut()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity( intent )
        finish()

    }

}