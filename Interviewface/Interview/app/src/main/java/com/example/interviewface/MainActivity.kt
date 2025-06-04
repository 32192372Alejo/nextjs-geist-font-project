package com.example.interviewface

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.PopupWindow
import android.widget.TextView
// import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.interviewface.databinding.ActivityMainBinding


class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding
    private var popupWindow: PopupWindow? = null

    


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        // Inicialización de la actividad principal

        // Verificar si hay un extra para seleccionar una pestaña específica
        val selectTabId = intent.getIntExtra("select_tab", -1)
        
        if (selectTabId != -1) {
            // Si hay un ID de pestaña especificado, cargar el fragmento correspondiente
            when (selectTabId) {
                R.id.navigation_home -> loadFragmentAndSelectNavItem(HomeFragment(), R.id.navigation_home)
                R.id.navigation_interviews -> loadFragmentAndSelectNavItem(InterviewsFragment(), R.id.navigation_interviews)
                R.id.navigation_comments -> loadFragmentAndSelectNavItem(TipsFragment(), R.id.navigation_comments)
                R.id.navigation_profile -> loadFragmentAndSelectNavItem(ProfileFragment(), R.id.navigation_profile)
            }
        } else {
            // Carga inicial: Fragmento Home
            loadFragment(HomeFragment())
        }

        // Configurar la barra de navegación inferior
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> loadFragment(HomeFragment())
                R.id.navigation_interviews -> loadFragment(InterviewsFragment())
                R.id.navigation_comments -> loadFragment(TipsFragment())
                R.id.navigation_profile -> loadFragment(ProfileFragment())
                R.id.navigation_more -> {
                    // Mostrar el menú emergente y permitir que el ítem se seleccione
                    showPopupMenu()
                    return@setOnItemSelectedListener true // Seleccionar este ítem
                }
            }
            true
        }
    }
    
    private fun showPopupMenu() {
        // Inflar el layout del popup
        val inflater = getSystemService(LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val popupView = inflater.inflate(R.layout.popup_menu, null)

        // Crear el PopupWindow
        popupWindow = PopupWindow(
            popupView,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT,
            true
        )
        
        // Configurar opciones
        val calificacionOption = popupView.findViewById<TextView>(R.id.option_calificacion)
        val configuracionOption = popupView.findViewById<TextView>(R.id.option_configuracion)
        
        calificacionOption.setOnClickListener {
            loadFragment(CalificacionFragment())
            popupWindow?.dismiss()
        }
        
        configuracionOption.setOnClickListener {
            loadFragment(ConfiguracionFragment())
            popupWindow?.dismiss()
        }
        
        // Calcular la posición para mostrar el popup
        val location = IntArray(2)
        binding.bottomNavigation.getLocationOnScreen(location)
        
        // Mostrar el popup encima de la barra de navegación
        // El -10 es para dar un pequeño margen con respecto a la barra
        val yOffset = -(location[1] - 10 + popupView.measuredHeight)
        
        // Mostrar el popup alineado a la derecha
        val xOffset = binding.root.width - popupView.measuredWidth - 20 // 20dp de margen derecho
        
        // Medir el popup para calcular correctamente su altura
        popupView.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        
        // Mostrar el popup en la posición calculada (40dp más arriba)
        popupWindow?.showAtLocation(binding.root, Gravity.NO_GRAVITY, xOffset, location[1] - popupView.measuredHeight - 60)
        
        // Cerrar el popup cuando se hace clic fuera de él
        popupWindow?.setOnDismissListener {
            // No hacer nada cuando se cierra
        }
    }

    fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
    
    // Método para cargar un fragmento y seleccionar el ítem correspondiente en la barra de navegación
    fun loadFragmentAndSelectNavItem(fragment: Fragment, itemId: Int) {
        loadFragment(fragment)
        binding.bottomNavigation.selectedItemId = itemId
    }
    

    
    // Método eliminado: registerLanguageChangeReceiver
    
    override fun onDestroy() {
        super.onDestroy()
        // Limpieza de recursos si es necesario
    }
}