package com.example.cryptoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.cryptoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var rvAdapter: RvAdapter
    private lateinit var data:ArrayList<Model>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        data = ArrayList<Model>()
        apiData
        rvAdapter = RvAdapter(this, data)

        binding.rv.layoutManager = LinearLayoutManager(this)
        binding.rv.adapter = rvAdapter
    }
    val apiData:Unit
        get() {
            val url = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest"

            val queue = Volley.newRequestQueue(this)
            val jsonObjectRequest:JsonObjectRequest =
                object :JsonObjectRequest(Method.GET, url, null, Response.Listener {
                    response ->
                    try {
                        val dataArray = response.getJSONArray("data")
                        for (i in 0 until dataArray.length())
                        {
                           val dataObject = dataArray.getJSONObject(i)
                            val symbol = dataObject.getString("symbol")
                            val name = dataObject.getString("name")
                            val quote = dataObject.getJSONObject("quote")
                            val USD = quote.getJSONObject("USD")
                            val price = USD.getDouble("price")
                            data.add(Model(name,symbol,price.toString()))
                        }
                        rvAdapter.notifyDataSetChanged()
                    }catch (e:Exception){
                        Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()

                    }

                }, Response.ErrorListener {
                    Toast.makeText(this, "Error 1", Toast.LENGTH_LONG).show()
                })
                {
                    override fun getHeaders(): Map<String, String> {
                        val headers= HashMap<String, String>()
                        headers["X-CMC_PRO_API_KEY"] = "355af379-985b-48f6-85da-96b75e97ed63"
                        return headers
                    }
                }
            queue.add(jsonObjectRequest)
        }
}