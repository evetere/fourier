import breeze.linalg._
import breeze.plot._
import breeze.math._
import breeze.numerics._
import breeze.stats.mean

import scala.math.Pi

object Main {

  def wound(signal : Double=>Double, sampleFrequency: Double)(t : Double) =
    signal(t) * exp(2 * Pi * i * sampleFrequency * t)

  def center(signal : Double=>Double, timepoints: DenseVector[Double])(sampleFrequency: Double) :Complex =
    mean(timepoints.map{t => wound(signal, sampleFrequency)(t)})

  def main(args: Array[String]): Unit = {
    val f = Figure()
    val complexPlanePlot = f.subplot(0)
    val timeDomainPlot = f.subplot(2,2,1)
    val freqDomainPlot = f.subplot(2,2,2)

    val timepoints = linspace(0.0, 5.0, 1000)

    val sampleFrequency=3.0;

    def signal(t :  Double) :Double = { cos(2 * Pi * 3 * t) + cos(2 * Pi * 2 * t) + 2.0}

    val zs = timepoints.map{t => wound(signal, sampleFrequency)(t)};

    val centerx = DenseVector(mean(zs))

    complexPlanePlot.title = "Complex Plane"
    complexPlanePlot.xlabel = "Real"
    complexPlanePlot.ylabel = "Imag"
    complexPlanePlot += plot(zs.map(_.real), zs.map(_.imag))
    complexPlanePlot += plot(centerx.map(_.real), centerx.map(_.imag),'+')

    timeDomainPlot.title = "Time Domain"
    timeDomainPlot.xlabel = "t"
    timeDomainPlot.ylabel = "amplitude"
    timeDomainPlot += plot(timepoints, timepoints.map(signal(_)))

    val frequencyPoints = linspace(0.0, 6.0, 1000)
    freqDomainPlot.title = "Frequency Domain"
    freqDomainPlot.xlabel = "frequency"
    freqDomainPlot.ylabel = "amplitude"
    freqDomainPlot += plot(frequencyPoints, frequencyPoints.map(f=> (center(signal, timepoints)(f)).real  ))
  }

}
