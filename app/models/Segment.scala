package models

/**
 * Created by christopher on 2014-09-15.
 */
case class Segment(
  id: Long,
  resourceState: Int,
  name: String,
  activityType: String,
  distance: Float,
  averageGrade: Float,
  maximumGrade: Float,
  maxElevation: Float,
  minElevation: Float,
  startCoordinates: List[String],
  endCoordinates: List[String],
  climbCategory: Int,
  city: String,
  state: String,
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
