import org.knowm.xchart.*;
import org.knowm.xchart.style.Styler;
import org.knowm.xchart.style.markers.SeriesMarkers;


public class LinearRegression {

    private int dataSetSize = 1001; //количество пар в датасете

    private double[] dataSetX = new double[dataSetSize]; //x
    private double[] dataSetY = new double[dataSetSize]; //y (реальное значение)

    private double[] noiseDataSetY = new double[dataSetSize]; //y + noise

    private double[] newModelY = new double[dataSetSize]; //y для новой модели линейной регрессии
    private double a, b; //коэффициенты для линейной аппроксимации вида y = ax + b


    private double f(double x) {
        return (Math.pow(1.1, x) - 2);
    }

    private double loss(double yAns, double yRightAns) {//функция потерь
        return Math.pow(yAns - yRightAns, 2);
    }

    private double q(double[] yValue) { //функция эмпирического риска
        double res = 0;
        for (int i = 0; i < dataSetSize; i++) {
            res += loss(yValue[i], dataSetY[i]);
        }
        return res / dataSetSize;
    }

    private void generateDataSet() { //генерирование датасета с добавлением шума
        double step = 0.003;
        double currX = 0;
        for (int i = 0; i < dataSetSize; i++) {
            dataSetX[i] = currX;
            dataSetY[i] = f(currX);

            //добавление шума
            java.util.Random r = new java.util.Random();
            noiseDataSetY[i] = dataSetY[i] + r.nextGaussian() * 0.02;

            currX += step;
        }
    }

    private void printDataSet(double[] xData, double yData[], int size) {
        for (int i = 0; i < size; i++) {
            System.out.println((i + 1) + ") x = " + xData[i] + "; y = " + yData[i]);
        }
    }

    private void showGraph() {
        // Create Chart
        XYChart chart = new XYChartBuilder().width(600).height(500).title("Test chart").xAxisTitle("X").yAxisTitle("Y").build();

        // Customize Chart
        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        chart.getStyler().setChartTitleVisible(false);
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideSW);
        chart.getStyler().setMarkerSize(5);

        // Series
        chart.addSeries("Real value", dataSetX, dataSetY);
        XYSeries series = chart.addSeries("Value with noise", dataSetX, noiseDataSetY);
        chart.addSeries("New model value", dataSetX, newModelY);
        series.setMarker(SeriesMarkers.DIAMOND);

        new SwingWrapper(chart).displayChart();
    }

    private void leastSquares() {
        int n = dataSetSize;

        double sumXY = 0;
        double sumX = 0;
        double sumY = 0;
        double sumX2 = 0; //x^2

        for (int i = 0; i < n; i++) {
            sumX += dataSetX[i];
            sumY += noiseDataSetY[i];
            sumXY += dataSetX[i] * noiseDataSetY[i];
            sumX2 += Math.pow(dataSetX[i], 2);
        }

        a = (n * sumXY - sumX * sumY) / (n * sumX2 - Math.pow(sumX, 2));
        b = (sumY - a * sumX) / n;
    }

    private double linearApproximation(double x, double a, double b){
        return a*x + b;
    }

    private void setNewModel(){
        leastSquares();
        for (int i = 0; i < dataSetSize; i++){
            newModelY[i] = linearApproximation(dataSetX[i], a, b);
        }
    }

    public void solve() {
        generateDataSet();
        System.out.println("Датасет без шума:");
        printDataSet(dataSetX, dataSetY, dataSetSize);
        System.out.println("Датасет с шумом:");
        printDataSet(dataSetX, noiseDataSetY, dataSetSize);

        System.out.println("\nЗначение функции эмпирического риска до МНК: " + q(noiseDataSetY));

        setNewModel();

        System.out.println("Значение функции эмпирического риска после МНК: " + q(newModelY));

        showGraph();
    }

}
