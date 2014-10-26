package models

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
  start_latitude: Float,
  start_longitude: Float,
  end_latitude: Float,
  end_longitude: Float,
  climb_category: Int,
  city: String,
  state: String,
  country: String,
  `private`: Boolean,
  createdAt: String,
  updatedAt: String,
  totalElevationGain: Float,
  map: Polyline,
  effortCount: Int,
  athleteCount: Int,
  hazardous: Boolean,
  PRTime: Int,
  PRDistance: Float,
  starred: Boolean,
  climbCategoryDesc: String)

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
  start_latitude: Float,
  start_longitude: Float,
  end_latitude: Float,
  end_longitude: Float,
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