package co.edu.eam.miprimeraapp.actividades

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import co.edu.eam.miprimeraapp.R
import co.edu.eam.miprimeraapp.databinding.ActivityLoginBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    lateinit var binding:ActivityLoginBinding
    lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        mAuth = FirebaseAuth.getInstance()

        if( mAuth.currentUser != null ){
            irAMainActivity()
        }

        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mAuth = FirebaseAuth.getInstance()

        binding.btnLogin.setOnClickListener {
            autenticarUsuario()
        }

        binding.btnRegistro.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity( intent )
        }

    }


    fun autenticarUsuario(){

        val correo = binding.txtEmail.text.toString()
        val password = binding.txtPass.text.toString()

        if( correo.isNotEmpty() && password.isNotEmpty() ){

            binding.txtEmail.error = null
            binding.txtPass.error = null

            mAuth.signInWithEmailAndPassword(correo, password).addOnCompleteListener {

                if(it.isSuccessful){
                    irAMainActivity()
                }else{
                    Snackbar.make(binding.root, "Login incorrecto", Snackbar.LENGTH_LONG).show()
                }

            }.addOnFailureListener {
                Snackbar.make(binding.root, "Error: ${it.message}", Snackbar.LENGTH_LONG).show()
            }

        }else{

            if(correo.isEmpty()){
                binding.txtEmail.error = "Es un campo obligatorio"
            }else{
                binding.txtEmail.error = null
            }

            if(password.isEmpty()){
                binding.txtPass.error = "Es un campo obligatorio"
            }else{
                binding.txtPass.error = null
            }

        }

    }


    fun irAMainActivity(){
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity( intent )
        finish()
    }


}