package kiambogo.scrava.models

case class Stats(
  biggest_ride_distance: Double,
  biggest_climb_elevation_gain: Double,
  recent_ride_totals: RecentRideTotals,
  recent_run_totals: RecentRunTotals,
  ytd_ride_totals: ytdRideTotals,
  ytd_run_totals: ytdRunTotals,
  all_ride_totals: allRideTotals,
  all_run_totals: allRunTotals)

case class RecentRideTotals(
  count: Int,
  distance: Double,
  moving_time: Int,
  elapsed_time: Int,
  elevation_gain: Double,
  achievement_count: Int)

case class RecentRunTotals(
  count: Int,
  distance: Double,
  moving_time: Int,
  elapsed_time: Int,
  elevation_gain: Double,
  achievement_count: Int)

case class ytdRideTotals(
  count: Int,
  distance: Int,
  moving_time: Int,
  elapsed_time: Int,
  elevation_gain: Int)

case class ytdRunTotals(
  count: Int,
  distance: Int,
  moving_time: Int,
  elapsed_time: Int,
  elevation_gain: Int)

case class allRideTotals(
  count: Int,
  distance: Int,
  moving_time: Int,
  elapsed_time: Int,
  elevation_gain: Int)

case class allRunTotals(
  count: Int,
  distance: Int,
  moving_time: Int,
  elapsed_time: Int,
  elevation_gain: Int)


