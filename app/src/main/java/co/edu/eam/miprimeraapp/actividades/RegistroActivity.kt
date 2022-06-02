package co.edu.eam.miprimeraapp.actividades

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import co.edu.eam.miprimeraapp.R
import co.edu.eam.miprimeraapp.databinding.ActivityRegistroBinding
import co.edu.eam.miprimeraapp.modelo.Usuario
import co.edu.eam.miprimeraapp.util.FirebaseManager
import co.edu.eam.miprimeraapp.util.Utilidades
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegistroActivity : AppCompatActivity() {

    lateinit var binding: ActivityRegistroBinding
    lateinit var mAuth:FirebaseAuth
    lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtLayoutCedula.isErrorEnabled = true
        binding.txtLayoutNombre.isErrorEnabled = true
        binding.txtLayoutCorreo.isErrorEnabled = true
        binding.txtLayoutPassword.isErrorEnabled = true

        val builder:AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setView(R.layout.dialogo_progreso)
        dialog = builder.create()

        mAuth = FirebaseAuth.getInstance()

        binding.btnRegistro.setOnClickListener {
            registrarUsuario()
        }

    }

    fun registrarUsuario(){

        val cedula = binding.txtCedula.text.toString()
        val nombre = binding.txtNombre.text.toString()
        val correo = binding.txtCorreo.text.toString()
        val password = binding.txtPassword.text.toString()
        var valido = true

        if(cedula.isEmpty()){
            valido = false
            binding.txtLayoutCedula.error = "Es un campo obligatorio"
        }else{
            binding.txtLayoutCedula.error = null
        }

        if(nombre.isEmpty()){
            valido = false
            binding.txtLayoutNombre.error = "Es un campo obligatorio"
        }else{
            binding.txtLayoutNombre.error = null
        }

        if(correo.isEmpty()){
            valido = false
            binding.txtLayoutCorreo.error = "Es un campo obligatorio"
        }else{
            binding.txtLayoutCorreo.error = null
        }

        if(password.isEmpty()){
            valido = false
            binding.txtLayoutPassword.error = "Es un campo obligatorio"
        }else{
            binding.txtLayoutPassword.error = null
        }

        if(valido){
            setDialog(true)

            mAuth.createUserWithEmailAndPassword(correo, password).addOnCompleteListener{

                if(it.isSuccessful){

                    val usuario = mAuth.currentUser

                    if(usuario!=null){

                        verificarEmail(usuario)

                        val usuarioActual = FirebaseManager.dataRef.child("usuarios").child(usuario.uid)
                        usuarioActual.child("nombre").setValue( nombre )
                        usuarioActual.child("cedula").setValue( cedula )

                        setDialog(false)

                        val intent = Intent(this, MainActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                        startActivity(intent)
                        finish()
                    }
                }


            }.addOnFailureListener {
                Snackbar.make(binding.root, "Error: ${it.message}", Snackbar.LENGTH_LONG).show()
                setDialog(false)
            }

        }

    }

    fun verificarEmail(user:FirebaseUser){

        user.sendEmailVerification().addOnCompleteListener {
            if(it.isSuccessful){
                Snackbar.make(binding.root, "Se ha enviado un correo de verificación", Snackbar.LENGTH_LONG).show()
            }else{
                Snackbar.make(binding.root, "Error", Snackbar.LENGTH_LONG).show()
            }
        }

    }

    private fun setDialog(show: Boolean) {
        if (show) dialog.show() else dialog.dismiss()
    }


}