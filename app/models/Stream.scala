package models

/**
 * Created by christopher on 2014-10-07.
 */
case class TimeStream(
  time: Time,
  distance: Distance)

case class LatLngStream(
  latlng: LatLng,
  distance: Distance)

case class AltitudeStream(
  distance: Distance,
  altitude: Altitude)

case class VelocityStream(
  distance: Distance,
  velocity: Velocity)

case class HeartrateStream(
  heartrate: Heartrate,
  distance: Distance)

case class CadenceStream(
  cadence: Cadence,
  distance: Distance)

case class WattsStream(
 watts: Watts,
 distance: Distance)

case class TempStream(
  temp: Temp,
  distance: Distance)

case class MovingStream(
  moving: Moving,
  distance: Distance)

case class GradeStream(
  grade: Grade,
  distance: Distance)

case class LatLng(
  `type`: String,
  data: List[List[Float]],
  series_type: String,
  original_size: Int,
  resolution: String)

case class Distance(
  `type`: String,
  data: List[Float],
  series_type: String,
  original_size: Int,
  resolution: String)

case class Time(
  `type`: String,
  data: List[Int],
  series_type: String,
  original_size: Int,
  resolution: String)

case class Altitude(
  `type`: String,
  data: List[Float],
  series_type: String,
  original_size: Int,
  resolution: String)

case class Velocity(
  `type`: String,
  data: List[Float],
  series_type: String,
  original_size: Int,
  resolution: String)

case class Heartrate(
  `type`: String,
  data: List[Int],
  series_type: String,
  original_size: Int,
  resolution: String)

case class Cadence(
  `type`: String,
  data: List[Int],
  series_type: String,
  original_size: Int,
  resolution: String)

case class Watts(
  `type`: String,
  data: List[Int],
  series_type: String,
  original_size: Int,
  resolution: String)

case class Temp(
  `type`: String,
  data: List[Int],
  series_type: String,
  original_size: Int,
  resolution: String)

case class Moving(
  `type`: String,
  data: List[Boolean],
  series_type: String,
  original_size: Int,
  resolution: String)

case class Grade(
  `type`: String,
  data: List[Float],
  series_type: String,
  original_size: Int,
  resolution: String)