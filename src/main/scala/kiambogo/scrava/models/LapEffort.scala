package kiambogo.scrava.models

/**
 * Created by christopher on 14-10-26.
 */
case class LapEffort(
  id: Int,
  resource_state: Int,
  name: String,
  activity: Map[String, Int],
  athlete: Map[String, Int],
  elapsed_time: Int,
  moving_time: Int,
  start_date: String,
  start_date_local: String,
  distance: Float,
  start_index: Int,
  end_index: Int,
  total_elevation_gain: Float,
  average_speed: Float,
  max_speed: Float,
  average_cadence: Option[Float],
  average_watts: Float,
  device_watts: Option[Boolean],
  average_heartrate: Option[Float],
  max_heartrate: Option[Float],
  lap_index: Int)