//
//package com.example.financetrackerex3
//
//import android.Manifest
//import android.annotation.SuppressLint
//import android.content.pm.PackageManager
//import android.graphics.Color
//import android.os.Build
//import android.os.Bundle
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.ProgressBar
//import android.widget.TextView
//import androidx.core.app.NotificationCompat
//import androidx.core.app.NotificationManagerCompat
//import androidx.core.content.ContextCompat
//import androidx.fragment.app.Fragment
//import com.github.mikephil.charting.charts.PieChart
//import com.github.mikephil.charting.data.PieData
//import com.github.mikephil.charting.data.PieDataSet
//import com.github.mikephil.charting.data.PieEntry
//import com.github.mikephil.charting.utils.ColorTemplate
//
//class HomeFragment : Fragment() {
//
//    private lateinit var tvBudget: TextView
//    private lateinit var tvRemaining: TextView
//    private lateinit var progressBar: ProgressBar
//    private lateinit var pieChart: PieChart
//
//    @SuppressLint("SetTextI18n")
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        val view = inflater.inflate(R.layout.fragment_home, container, false)
//
//        // UI components
//        tvBudget = view.findViewById(R.id.tvTotalBudget)
//        tvRemaining = view.findViewById(R.id.tvRemainingBudget)
//        progressBar = view.findViewById(R.id.progressBar)
//        pieChart = view.findViewById(R.id.pieChart)
//
//        // Load budget and spent amount
//        val budget = SharedPrefManager.getBudget(requireContext())
//        val spent = TransactionStorage.getTotalSpent(requireContext())
//        val remaining = budget - spent
//
//        // Show values
//        tvBudget.text = "Budget: Rs. $budget\nSpent: Rs. $spent"
//        tvRemaining.text = "Rs. $remaining"
//        progressBar.progress = ((spent / budget) * 100).toInt()
//
//        // Alert if over budget
//        if (spent >= budget) {
//            sendBudgetNotification()
//        }
//
//        // Show Pie Chart for category-wise spending
//        showPieChart()
//
//        return view
//    }
//
//    private fun sendBudgetNotification() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            if (ContextCompat.checkSelfPermission(
//                    requireContext(),
//                    Manifest.permission.POST_NOTIFICATIONS
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                return
//            }
//        }
//
//        val builder = NotificationCompat.Builder(requireContext(), "budget_channel")
//            .setSmallIcon(R.drawable.ic_dialog_alert)
//            .setContentTitle("Budget Alert")
//            .setContentText("You have reached your monthly budget!")
//            .setPriority(NotificationCompat.PRIORITY_HIGH)
//
//        val notificationManager = NotificationManagerCompat.from(requireContext())
//        notificationManager.notify(1, builder.build())
//    }
//
////    private fun showPieChart() {
////        val transactions = TransactionStorage.loadTransactions(requireContext())
////
////        val categoryTotals = mutableMapOf<String, Double>()
////
////        // Calculate category-wise totals
////        for (transaction in transactions) {
////            val category = transaction.category
////            val amount = transaction.amount
////            categoryTotals[category] = categoryTotals.getOrDefault(category, 0.0) + amount
////        }
////
////        val pieEntries = ArrayList<PieEntry>()
////        for ((category, total) in categoryTotals) {
////            pieEntries.add(PieEntry(total.toFloat(), category))
////        }
////
////        val dataSet = PieDataSet(pieEntries, "Category Wise Spending")
////        dataSet.colors = ColorTemplate.MATERIAL_COLORS.toList()
////        dataSet.valueTextColor = Color.BLACK
////        dataSet.valueTextSize = 14f
////
////        val data = PieData(dataSet)
////
////        pieChart.data = data
////        pieChart.description.isEnabled = false
////        pieChart.centerText = "Spending Summary"
////        pieChart.animateY(1000)
////        pieChart.invalidate()
////    }
//private fun showPieChart() {
//    val transactions = TransactionStorage.loadTransactions(requireContext())
//
//    val categoryTotals = mutableMapOf<String, Double>()
//
//    // 🧮 Calculate category-wise totals
//    for (transaction in transactions) {
//        val category = transaction.category
//        val amount = transaction.amount
//        categoryTotals[category] = categoryTotals.getOrDefault(category, 0.0) + amount
//    }
//
//    val pieEntries = ArrayList<PieEntry>()
//    for ((category, total) in categoryTotals) {
//        pieEntries.add(PieEntry(total.toFloat(), category))
//    }
//
//    val dataSet = PieDataSet(pieEntries, "")
//    dataSet.colors = listOf(
//        Color.parseColor("#4B2273"), // Green
//        Color.parseColor("#091140"), // Blue
//        Color.parseColor("#275673"), // Orange
//        Color.parseColor("#341959"), // Pink
//        Color.parseColor("#9C27B0")  // Purple
//    )
//    dataSet.sliceSpace = 3f                    // 🎯 Space between slices
//    dataSet.selectionShift = 6f                // 🎯 Highlight distance on tap
//    dataSet.valueTextColor = Color.WHITE       // 📝 Text color on slices
//    dataSet.valueTextSize = 13f
//
//    val data = PieData(dataSet)
//    pieChart.data = data
//
//    // 🔧 Styling the Pie Chart
//    pieChart.setUsePercentValues(true)         // 📊 Show percentage values
//    pieChart.setDrawEntryLabels(true)         // 🧹 Clean up labels on slices
//    pieChart.description.isEnabled = false     // ❌ Disable default description
//    pieChart.isDrawHoleEnabled = true
//    pieChart.setHoleColor(Color.TRANSPARENT)
//    pieChart.setTransparentCircleAlpha(110)
//    pieChart.holeRadius = 45f                  // 🕳️ Adjust hole size
//    pieChart.setCenterText("Expenses")         // 🏷️ Center label
//    pieChart.setCenterTextSize(16f)
//    pieChart.setEntryLabelColor(Color.WHITE)
//
//    // 🧭 Configure legend
//    val legend = pieChart.legend
//    legend.isEnabled = true
//    legend.orientation = com.github.mikephil.charting.components.Legend.LegendOrientation.HORIZONTAL
//    legend.horizontalAlignment = com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment.CENTER
//    legend.verticalAlignment = com.github.mikephil.charting.components.Legend.LegendVerticalAlignment.BOTTOM
//    legend.setDrawInside(false)
//    legend.textSize = 16f
//
//    // 🔄 Animate and refresh
//    pieChart.animateY(1200, com.github.mikephil.charting.animation.Easing.EaseInOutQuad)
//    pieChart.invalidate()
//}
//
//}
//


package com.example.financetrackerex3

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate

class HomeFragment : Fragment() {

    private lateinit var tvBudget: TextView
    private lateinit var tvRemaining: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var pieChart: PieChart

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // UI components
        tvBudget = view.findViewById(R.id.tvTotalBudget)
        tvRemaining = view.findViewById(R.id.tvRemainingBudget)
        progressBar = view.findViewById(R.id.progressBar)
        pieChart = view.findViewById(R.id.pieChart)

        // Get currency symbol from SharedPreferences
        val currency = SharedPrefManager.getCurrencySymbol(requireContext())

        // Load budget and spent amount
        val budget = SharedPrefManager.getBudget(requireContext())
        val spent = TransactionStorage.getTotalSpent(requireContext())
        val remaining = budget - spent

        // Show values with currency
        tvBudget.text = "Budget: $currency $budget\nSpent: $currency $spent"
        tvRemaining.text = "$currency $remaining"
        progressBar.progress = ((spent / budget) * 100).toInt()

        // Alert if over budget
        if (spent >= budget) {
            sendBudgetNotification()
        }

        // Show Pie Chart for category-wise spending
        showPieChart()

        return view
    }

    private fun sendBudgetNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }

        val builder = NotificationCompat.Builder(requireContext(), "budget_channel")
            .setSmallIcon(R.drawable.ic_dialog_alert)
            .setContentTitle("Budget Alert")
            .setContentText("You have reached your monthly budget!")
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val notificationManager = NotificationManagerCompat.from(requireContext())
        notificationManager.notify(1, builder.build())
    }

    private fun showPieChart() {
        val transactions = TransactionStorage.loadTransactions(requireContext())

        val categoryTotals = mutableMapOf<String, Double>()

        for (transaction in transactions) {
            val category = transaction.category
            val amount = transaction.amount
            categoryTotals[category] = categoryTotals.getOrDefault(category, 0.0) + amount
        }

        val pieEntries = ArrayList<PieEntry>()
        for ((category, total) in categoryTotals) {
            pieEntries.add(PieEntry(total.toFloat(), category))
        }

        val dataSet = PieDataSet(pieEntries, "")
        dataSet.colors = listOf(
            Color.parseColor("#4B2273"),
            Color.parseColor("#091140"),
            Color.parseColor("#275673"),
            Color.parseColor("#341959"),
            Color.parseColor("#9C27B0")
        )
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 6f
        dataSet.valueTextColor = Color.WHITE
        dataSet.valueTextSize = 13f

        val data = PieData(dataSet)
        pieChart.data = data

        pieChart.setUsePercentValues(true)
        pieChart.setDrawEntryLabels(true)
        pieChart.description.isEnabled = false
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(Color.TRANSPARENT)
        pieChart.setTransparentCircleAlpha(110)
        pieChart.holeRadius = 45f
        pieChart.setCenterText("Expenses")
        pieChart.setCenterTextSize(16f)
        pieChart.setEntryLabelColor(Color.WHITE)

        val legend = pieChart.legend
        legend.isEnabled = true
        legend.orientation = com.github.mikephil.charting.components.Legend.LegendOrientation.HORIZONTAL
        legend.horizontalAlignment = com.github.mikephil.charting.components.Legend.LegendHorizontalAlignment.CENTER
        legend.verticalAlignment = com.github.mikephil.charting.components.Legend.LegendVerticalAlignment.BOTTOM
        legend.setDrawInside(false)
        legend.textSize = 16f

        pieChart.animateY(1200, com.github.mikephil.charting.animation.Easing.EaseInOutQuad)
        pieChart.invalidate()
    }
}
