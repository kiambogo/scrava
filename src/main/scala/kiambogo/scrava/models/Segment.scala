package kiambogo.scrava.models

/**
 * Created by christopher on 2014-09-15.
 */
case class Segment(
  id: Int,
  resource_state: Int,
  name: String,
  activity_type: String,
  distance: Float,
  average_grade: Float,
  maximum_grade: Float,
  elevation_high: Float,
  elevation_low: Float,
  start_latlng: List[Float],
  end_latlng: List[Float],
  climb_category: Int,
  city: String,
  state: String,
  country: String,
  `private`: Boolean,
  starred: Boolean,
  created_at: String,
  updated_at: String,
  total_elevation_gain: Float,
  map: Polyline,
  effort_count: Int,
  athlete_count: Int,
  hazardous: Boolean,
  star_count: Int)

case class SegmentSummary(
  id: Int,
  resource_state: Int,
  name: String,
  activity_type: String,
  distance: Float,
  average_grade: Float,
  maximum_grade: Float,
  elevation_high: Float,
  elevation_low: Float,
  start_latlng: List[Float],
  end_latlng: List[Float],
  climb_category: Int,
  city: String,
  state: String,
  country: String,
  `private`: Boolean,
  starred: Boolean,
  pr_time: Option[Int],
  athlete_pr_effort: Option[PREffort],
  starred_date: Option[String])

case class SegmentCondensed(
  id: Int,
  name: String,
  climb_category: Int,
  climb_category_desc: String,
  avg_grade: Float,
  start_latlng: List[Float],
  end_latlng: List[Float],
  elev_difference: Float,
  distance: Float,
  points: String)

case class SegmentLeaderBoards(
  entry_count: Int,
  entries: List[LeaderBoardEntry])

case class LeaderBoardEntry(
  athlete_name: String,
  athlete_id: Int,
  athlete_gender: String,
  average_hr: Option[Float],
  average_watts: Float,
  distance: Float,
  elapsed_time: Int,
  moving_time: Int,
  start_date: String,
  start_date_local: String,
  activity_id: Int,
  effort_id: Int,
  rank: Int,
  athlete_profile: String)