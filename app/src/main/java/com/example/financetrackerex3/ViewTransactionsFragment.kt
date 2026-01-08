
package com.example.financetrackerex3

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.fragment.app.Fragment

class ViewTransactionsFragment : Fragment() {

    private lateinit var transactionListView: ListView
    private lateinit var transactions: MutableList<Transaction>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_view_transactions, container, false)

        // Get the ListView and load the transactions
        transactionListView = view.findViewById(R.id.transactionListView)
        transactions = TransactionStorage.loadTransactions(requireContext())

        // Prepare the adapter with transaction titles
//        val transactionTitles = transactions.map { it.title + it.amount + it.date+ it.category}
//        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, transactionTitles)
        val transactionTitles = transactions.map {
            val title = it.title
            val rest = "\n${it.category} \n ${it.date}                          Rs.${it.amount}\n           "
            val spannable = android.text.SpannableString("\n"+title + rest)
            spannable.setSpan(
                android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                0,
                title.length,
                android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            // Change the title font size
            spannable.setSpan(
                android.text.style.AbsoluteSizeSpan(18, true),  // 20 is the font size in px, you can adjust it
                0,
                title.length,
                android.text.Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )

            spannable
        }

        val adapter = ArrayAdapter<CharSequence>(requireContext(), android.R.layout.simple_list_item_1, transactionTitles)

        transactionListView.adapter = adapter

        // Set an item click listener for editing transactions
        transactionListView.setOnItemClickListener { _, _, position, _ ->
            val transaction = transactions[position]
            openEditTransactionScreen(transaction)
        }

        // Set an item long click listener for deleting transactions
        transactionListView.setOnItemLongClickListener { _, _, position, _ ->
            val transaction = transactions[position]
            showDeleteConfirmationDialog(transaction, adapter)
            true
        }

        return view
    }

    // Open the EditTransaction activity to edit the selected transaction
    private fun openEditTransactionScreen(transaction: Transaction) {
        val intent = Intent(context, EditTransaction::class.java)
        intent.putExtra("transaction", transaction)  // Pass the transaction to the EditActivity
        startActivity(intent)
    }

    // Show a confirmation dialog for deleting the selected transaction
    private fun showDeleteConfirmationDialog(transaction: Transaction, adapter: ArrayAdapter<*>) {
        AlertDialog.Builder(requireContext())
            .setMessage("Do you want to delete this transaction?")
            .setPositiveButton("Yes") { _, _ ->
                // Remove the transaction from the list and update storage
                transactions.remove(transaction)
                TransactionStorage.saveTransactions(requireContext(), transactions)
                Toast.makeText(requireContext(), "Transaction deleted", Toast.LENGTH_SHORT).show()
                adapter.notifyDataSetChanged()  // Notify adapter to update ListView
            }
            .setNegativeButton("No", null)
            .show()
    }
}
