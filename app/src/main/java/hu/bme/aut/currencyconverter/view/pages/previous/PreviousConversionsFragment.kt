package hu.bme.aut.currencyconverter.view.pages.previous

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.currencyconverter.data.repository.CurrencyDatabase
import hu.bme.aut.currencyconverter.databinding.FragmentPreviousConversionsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PreviousConversionsFragment : Fragment() {

    private lateinit var binding: FragmentPreviousConversionsBinding

    private lateinit var adapter: PreviousConversionsListAdapter

    private lateinit var database: CurrencyDatabase

    private lateinit var deleteAllButton: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentPreviousConversionsBinding.inflate(inflater, container, false)

        database = CurrencyDatabase.getDatabase(requireActivity().applicationContext)

        deleteAllButton = binding.textButtonDeleteAll
        deleteAllButton.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                withContext(Dispatchers.IO) {
                    database.conversionListDao().deleteAll()

                    setAdapterItems()
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = PreviousConversionsListAdapter()

        binding.rvPrevConv.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPrevConv.adapter = adapter

        CoroutineScope(Dispatchers.IO).launch {
            setAdapterItems()
        }
    }

    private suspend fun setAdapterItems() {
        withContext(Dispatchers.IO) {
            val items = database.conversionListDao().getAll()

            requireActivity().runOnUiThread {
                adapter.setItems(items)
            }
        }
    }

}