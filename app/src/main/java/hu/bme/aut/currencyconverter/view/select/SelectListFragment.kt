package hu.bme.aut.currencyconverter.view.select

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import hu.bme.aut.currencyconverter.data.repository.CurrencyDatabase
import hu.bme.aut.currencyconverter.data.repository.selection.CurrencySelection
import hu.bme.aut.currencyconverter.databinding.FragmentSelectListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SelectListFragment : Fragment(), SelectListAdapter.SelectionCurrencyClickedListener {

    private lateinit var binding: FragmentSelectListBinding

    private lateinit var database: CurrencyDatabase
    private lateinit var adapter: SelectListAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSelectListBinding.inflate(inflater, container, false)

        database = CurrencyDatabase.getDatabase(requireActivity().applicationContext)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        this.initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = SelectListAdapter(this)

        binding.rvSelect.layoutManager = LinearLayoutManager(requireContext())
        binding.rvSelect.adapter = adapter
    }

    override fun onCurrencyClicked(currencyItem: CurrencySelection) {
        CoroutineScope(Dispatchers.IO).launch {
            toggleInDbAndUpdateAdapter(currencyItem)
        }
    }

    private suspend fun toggleInDbAndUpdateAdapter(currencyItem: CurrencySelection){
        var selectionsAfterToggle: List<CurrencySelection>
        withContext(Dispatchers.IO) {
            database.currencySelectionDao().toggleSelection(currencyItem)

            selectionsAfterToggle = database.currencySelectionDao().getAll()

            requireActivity().runOnUiThread {
                adapter.update(selectionsAfterToggle)
            }
        }
    }
}