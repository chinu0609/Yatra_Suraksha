package com.gb.yatrasuraksha.probability

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.gb.yatrasuraksha.R
import com.gb.yatrasuraksha.databinding.ActivityProbabilityBinding

class ProbabilityActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProbabilityBinding

    private lateinit var spinnerOneMap: Map<String, Double>
    private lateinit var spinnerTwoMap: Map<String, Double>
    private lateinit var spinnerThreeMap: Map<String, Double>
    private lateinit var spinnerFourMap: Map<String, Double>
    private lateinit var spinnerFiveMap: Map<String, Double>
    private lateinit var spinnerSixMap: Map<String, Double>

    private var v1 = 1.0
    private var v2 = 1.0
    private var v3 = 1.0
    private var v4 = 1.0
    private var v5 = 1.0
    private var v6 = 1.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProbabilityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //change status bar color to white
        window.statusBarColor = resources.getColor(R.color.white, theme)

        //add values into spinner
        spinnerDataSet()
        val listOfMaps = listOf(
            spinnerOneMap,
            spinnerTwoMap,
            spinnerThreeMap,
            spinnerFourMap,
            spinnerFiveMap,
            spinnerSixMap
        )

        //add values into spinners in xml
        addValuesIntoSpinner(listOfMaps)

        //do numeric calculations
        doNumericCalculation(listOfMaps)

        binding.probability.visibility = View.GONE
        //on clicking submit
        binding.submit.setOnClickListener {
            binding.probability.visibility = View.VISIBLE
            val result = v1 * v2 * v3 * v4 * v5 * v6 * 10 * 100
            val formattedResult = String.format("%.8f", result)
            binding.result.text = "$formattedResult%"
        }
    }


    private fun addValuesIntoSpinner(listOfMaps: List<Map<String, Double>>) {
        //road type
        val spinnerOne = listOfMaps[0].keys.toTypedArray()
        val spinnerAdapterOne =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinnerOne)
        binding.spinnerOne.adapter = spinnerAdapterOne

        //accident type
        val spinnerTwo = listOfMaps[1].keys.toTypedArray()
        val spinnerAdapterTwo =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinnerTwo)
        binding.spinnerTwo.adapter = spinnerAdapterTwo

        //surface type
        val spinnerThree = listOfMaps[2].keys.toTypedArray()
        val spinnerAdapterThree =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinnerThree)
        binding.spinnerThree.adapter = spinnerAdapterThree

        //road character
        val spinnerFour = listOfMaps[3].keys.toTypedArray()
        val spinnerAdapterFour =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinnerFour)
        binding.spinnerFour.adapter = spinnerAdapterFour

        //location
        val spinnerFive = listOfMaps[4].keys.toTypedArray()
        val spinnerAdapterFive =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinnerFive)
        binding.spinnerFive.adapter = spinnerAdapterFive

        //surface condition
        val spinnerSix = listOfMaps[5].keys.toTypedArray()
        val spinnerAdapterSix =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, spinnerSix)
        binding.spinnerSix.adapter = spinnerAdapterSix
    }

    private fun doNumericCalculation(listOfMaps: List<Map<String, Double>>) {
        binding.spinnerOne.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                v1 = listOfMaps[0][selectedItem]!!
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.spinnerTwo.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                v2 = listOfMaps[1][selectedItem]!!
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.spinnerThree.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                v3 = listOfMaps[2][selectedItem]!!
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.spinnerFour.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                v4 = listOfMaps[3][selectedItem]!!
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.spinnerFive.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                v5 = listOfMaps[4][selectedItem]!!
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        binding.spinnerSix.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedItem = parent?.getItemAtPosition(position).toString()
                v6 = listOfMaps[5][selectedItem]!!
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }


    private fun spinnerDataSet() {
        spinnerOneMap = mapOf(
            "Select Road Type" to 0.0,
            "NH" to 0.3878,
            "SH" to 0.2632,
            "City or Town roads" to 0.08510,
            "Village Roads" to 0.07050,
            "Two way" to 0.05430,
            "One way" to 0.02610,
            "Major District Roads" to 0.02370,
            "Residential Street" to 0.02370,
            "Service Road" to 0.01800,
            "Others" to 0.024
        )

        spinnerTwoMap = mapOf(
            "Select Accident Type" to 0.0,
            "Narrow Road" to 0.3946,
            "Cross Road" to 0.1625,
            "Curves" to 0.1554,
            "Junction" to 0.09810,
            "Circle" to 0.07980,
            "Road Hump" to 0.0220,
            "Offset" to 0.02800,
            "Bridge" to 0.01900,
            "Bottleneck" to 0.0179,
            "Others" to 0.0296
        )

        spinnerThreeMap = mapOf(
            "Select Surface Type" to 0.0,
            "Bitumen Tar" to 0.8808,
            "Concrete" to 0.053,
            "Surfaced" to 0.0244,
            "Kutcha" to 0.0221,
            "Gravel" to 0.0109,
            "Metalled" to 0.0082
        )

        spinnerFourMap = mapOf(
            "Select Road Character" to 0.0,
            "Straight and flat" to 0.5953,
            "Curve" to 0.2632,
            "Hump" to 0.03740,
            "Incline" to 0.032,
            "Slight Curve" to 0.026,
            "Others" to 0.0461
        )

        spinnerFiveMap = mapOf(
            "Select Location" to 0.0,
            "Rural" to 0.5825,
            "City or Town" to 0.3793,
            "Village area" to 0.0328
        )

        spinnerSixMap = mapOf(
            "Select Surface Condition" to 0.0,
            "Dry" to 0.9614,
            "Wet" to 0.0140,
            "Muddy" to 0.117,
            "Ditch or Potholed" to 0.088,
            "Flooded" to 0.041
        )
    }
}