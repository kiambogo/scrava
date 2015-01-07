package kiambogo.scrava.models

/**
 * Created by christopher on 2014-10-07.
 */
sealed trait Streams

case class LatLng(
  `type`: String = "latlng",
  data: List[List[Double]] = List(),
  series_type: String = "distance",
  original_size: Int = 0,
  resolution: String = "high") extends Streams

case class Distance(
  `type`: String = "distance",
  data: List[Float] = List(),
  series_type: String = "distance",
  original_size: Int = 0,
  resolution: String = "high") extends Streams

case class Time(
  `type`: String = "time",
  data: List[Int] = List(),
  series_type: String = "distance",
  original_size: Int = 0,
  resolution: String = "high") extends Streams

case class Altitude(
  `type`: String = "altitude",
  data: List[Float] = List(),
  series_type: String = "distance",
  original_size: Int = 0,
  resolution: String = "high") extends Streams

case class Velocity(
  `type`: String = "velocity",
  data: List[Float] = List(),
  series_type: String = "distance",
  original_size: Int = 0,
  resolution: String = "high") extends Streams

case class Heartrate(
  `type`: String = "heartrate",
  data: List[Int] = List(),
  series_type: String = "distance",
  original_size: Int = 0,
  resolution: String = "high") extends Streams

case class Cadence(
  `type`: String = "cadence",
  data: List[Int] = List(),
  series_type: String = "distance",
  original_size: Int = 0,
  resolution: String = "high") extends Streams

case class Watts(
  `type`: String = "watts",
  data: List[Int] = List(),
  series_type: String = "distance",
  original_size: Int = 0,
  resolution: String = "high") extends Streams

case class Temp(
  `type`: String = "temp",
  data: List[Int] = List(),
  series_type: String = "distance",
  original_size: Int = 0,
  resolution: String = "high") extends Streams

case class Moving(
  `type`: String = "moving",
  data: List[Boolean] = List(),
  series_type: String = "distance",
  original_size: Int = 0,
  resolution: String = "high") extends Streams

case class Grade(
  `type`: String = "grade_smooth",
  data: List[Float] = List(),
  series_type: String = "distance",
  original_size: Int = 0,
  resolution: String = "high") extends Streams
