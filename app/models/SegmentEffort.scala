package models

/**
 * Created by christopher on 2014-09-15.
 */
case class SegmentEffort(
  id: Long,
  resourceState: Int,
  name: String,
  activity: Activity,
  athlete: Athlete,
  elapsedTime: Int,
  movingTime: Int,
  startDate: String,
  startDateLocal: String,
  distance: Float,
  startIndex: Int,
  endIndex: Int,
  averageCadence: Float,
  averageWatts: Float,
  averageHeartrate: Float,
  maxHeartrate: Int,
  segment: Segment,
  KOMRank: Int,
  PRRank: Int,
  hidden: Boolean) {

}
