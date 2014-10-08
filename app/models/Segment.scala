package models

/**
 * Created by christopher on 2014-09-15.
 */
case class Segment(
  id: Long,
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
  climbCategoryDesc: String) {

}

case class SegmentSummary(
  id: Long,
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
  starred: Boolean) {

}
