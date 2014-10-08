package models

/**
 * Created by christopher on 2014-09-15.
 */
case class SegmentEffort(
  id: Long,
  resource_state: Int,
  name: String,
  activity: Map[String,Int],
  athlete: Map[String,Int],
  elapsed_time: Int,
  moving_time: Int,
  start_date: String,
  start_date_local: String,
  distance: Float,
  start_index: Int,
  end_index: Int,
  average_heartrate: Float,
  max_heartrate: Int,
  segment: SegmentSummary,
  kom_rank: Int,
  pr_rank: Option[Int]) {

}
