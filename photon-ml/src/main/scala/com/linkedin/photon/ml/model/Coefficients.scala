package com.linkedin.photon.ml.model

import breeze.linalg.{Vector, norm}
import breeze.stats.meanAndVariance

/**
 * Representation of model coefficients
 *
 * @param means the mean of the model coefficients
 * @param variancesOption optional variance of the model coefficients
 * @author xazhang
 */
case class Coefficients(means: Vector[Double], variancesOption: Option[Vector[Double]]) {

  lazy val meansL2Norm: Double = norm(means, 2)
  lazy val variancesL2NormOption: Option[Double] = variancesOption.map(variances => norm(variances, 2))

  /**
   * Compute the score for the given features
   *
   * @param features features to score
   * @return the score
   */
  def computeScore(features: Vector[Double]): Double = {
    assert(means.length == features.length,
      s"Coefficients length (${means.length}}) != features length (${features.length}})")
    means.dot(features)
  }

  /**
   * Build a summary string for the coefficients
   *
   * @return string representation
   */
  def toSummaryString: String = {
    val stringBuilder = new StringBuilder()
    stringBuilder.append(s"meanAndVarianceAndCount of the mean: ${meanAndVariance(means)}")
    stringBuilder.append(s"\nl2 norm of the mean: ${norm(means, 2)}")
    variancesOption.foreach(variances => s"\nmeanAndVarianceAndCount of variance: ${meanAndVariance(variances)}")
    stringBuilder.toString()
  }
}

object Coefficients {

  /**
   * Create a zero coefficient vector
   *
   * @param dimension dimensionality of the coefficient vector
   * @return zero coefficient vector
   */
  def initializeZeroCoefficients(dimension: Int): Coefficients = {
    Coefficients(Vector.zeros[Double](dimension), variancesOption = None)
  }
}