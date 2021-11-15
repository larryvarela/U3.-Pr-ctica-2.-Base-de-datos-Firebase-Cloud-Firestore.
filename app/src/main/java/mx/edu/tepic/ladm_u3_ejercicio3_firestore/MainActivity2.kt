package mx.edu.tepic.ladm_u3_ejercicio3_firestore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main2.*

class MainActivity2 : AppCompatActivity() {
    var id = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        var extra = intent.extras
        id = extra!!.getString("idActualizar")!!

        //recuperar la data

        val notas = Notas(this).consultar(id)
        titulo.setText(notas.titulo)
        contenido.setText(notas.contenido)
        hora.setText(notas.hora)
        fecha.setText(notas.fecha)

        button2.setOnClickListener {
            val notasActualizar = Notas(this)
            notasActualizar.titulo = titulo.text.toString()
            notasActualizar.contenido = contenido.text.toString()
            notasActualizar.hora = hora.text.toString()
            notasActualizar.fecha = fecha.text.toString()

            val resultado = notasActualizar.actualizar(id)
            if(resultado){
                Toast.makeText(this,"EXITO SE ACTUALIZO", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(this,"NO SE ACTUALIZO", Toast.LENGTH_LONG).show()
            }
        }

        button3.setOnClickListener {
            finish()
        }


    }
}