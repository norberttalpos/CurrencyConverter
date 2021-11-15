package hu.bme.aut.currencyconverter.view.select

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputLayout
import hu.bme.aut.currencyconverter.data.repository.CurrencyDatabase
import hu.bme.aut.currencyconverter.data.repository.selection.CurrencySelection
import hu.bme.aut.currencyconverter.databinding.FragmentSelectListBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.withContext

class SelectListFragment : Fragment(), SelectListAdapter.SelectionCurrencyClickedListener {

    private lateinit var binding: FragmentSelectListBinding

    private lateinit var database: CurrencyDatabase
    private lateinit var adapter: SelectListAdapter

    private lateinit var textField: TextInputLayout

    private lateinit var selectLayout: LinearLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentSelectListBinding.inflate(inflater, container, false)

        database = CurrencyDatabase.getDatabase(requireActivity().applicationContext)

        textField = binding.outlinedTextField
        textField.editText?.doOnTextChanged { inputText, _, _, _ ->
            CoroutineScope(Dispatchers.IO).launch {
                updateShownSelections(inputText.toString())
            }
        }
        textField.isFocusableInTouchMode = true


        selectLayout = binding.selectLayout
        selectLayout.setOnClickListener {
            selectLayout.hideKeyboard()
            binding.rvSelect.requestFocus()
        }

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

        CoroutineScope(Dispatchers.IO).launch {
            updateItems()
        }
    }

    private suspend fun updateItems() {
        withContext(Dispatchers.IO) {
            val items = database.currencySelectionDao().getAll()

            requireActivity().runOnUiThread {
                adapter.update(items)
            }
        }
    }

    private suspend fun updateShownSelections(searchText: String) {
        withContext(Dispatchers.IO) {
            val items = database.currencySelectionDao().search(searchText)

            requireActivity().runOnUiThread {
                adapter.update(items)
            }
        }
    }

    override fun onSelectionToggled(currencyItem: CurrencySelection) {
        CoroutineScope(Dispatchers.IO).launch {
            toggleInDb(currencyItem)
        }
    }

    private suspend fun toggleInDb(currencyItem: CurrencySelection){
        withContext(Dispatchers.IO) {
            database.currencySelectionDao().toggleSelection(currencyItem)
        }
    }

    fun View.hideKeyboard() {
        val inputMethodManager = requireContext().getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(this.windowToken, 0)
    }

}