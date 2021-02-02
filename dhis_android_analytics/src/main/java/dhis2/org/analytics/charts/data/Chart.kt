package dhis2.org.analytics.charts.data

import android.content.Context
import android.view.View
import dhis2.org.analytics.charts.mappers.GraphToBarChart
import dhis2.org.analytics.charts.mappers.GraphToLineChart
import dhis2.org.analytics.charts.mappers.GraphToTable
import dhis2.org.analytics.charts.mappers.GraphToValue
import dhis2.org.analytics.charts.mappers.GraphToNutritionChart

class Chart private constructor(
    private val chartType: ChartType,
    private val graphData: Graph
) {
    private val graphToLineChartMapper: GraphToLineChart by lazy { GraphToLineChart() }
    private val graphToNutritionChartMapper: GraphToNutritionChart by lazy { GraphToNutritionChart() }
    private val graphToBarChartMapper: GraphToBarChart by lazy { GraphToBarChart() }
    private val graphToTableMapper: GraphToTable by lazy { GraphToTable() }
    private val graphToValueMapper: GraphToValue by lazy { GraphToValue() }

    fun getChartView(context: Context): View {
        return when (chartType) {
            ChartType.LINE_CHART -> graphToLineChartMapper.map(context, graphData)
            ChartType.NUTRITION -> graphToNutritionChartMapper.map(context, graphData)
            ChartType.BAR_CHART -> graphToBarChartMapper.map(context, graphData)
            ChartType.TABLE -> graphToTableMapper.map(context, graphData)
            ChartType.SINGLE_VALUE -> graphToValueMapper.map(context, graphData)
        }
    }

    class ChartBuilder {
        private var chartType: ChartType? = null
        private var graphData: Graph? = null

        fun withType(chartType: ChartType): ChartBuilder {
            this.chartType = chartType
            return this
        }

        fun withGraphData(graph: Graph): ChartBuilder {
            this.graphData = graph
            return this
        }

        fun build(): Chart {
            return Chart(chartType!!, graphData!!)
        }
    }
}
