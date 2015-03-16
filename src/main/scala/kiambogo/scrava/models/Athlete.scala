package kiambogo.scrava.models

/**
 * Created by christopher on 2014-09-15.
 */
case class DetailedAthlete(
  id: Int,
  resource_state: Int,
  firstname: String,
  lastname: String,
  profile_medium: String,
  profile: String,
  city: String,
  state: String,
  country: String,
  sex: String,
  friend: Option[String],
  follower: Option[String],
  premium: Boolean,
  created_at: String,
  updated_at: String,
  follower_count: Int,
  friend_count: Int,
  mutual_friend_count: Int,
  date_preference: String,
  measurement_preference: String,
  email: String,
  ftp: Option[Int],
  weight: Option[Float],
  clubs: List[ClubSummary],
  bikes: List[GearSummary],
  shoes: List[GearSummary])

case class AthleteSummary(
  id:	Int,
  resource_state:	Int,
  firstname: String,
  lastname: String,
  profile_medium:	String,
  profile:	String,
  city:	String,
  state:	String,
  country:	String,
  sex:	String,
  friend:	String,
  follower:	String,
  premium:	Boolean,
  created_at: String,
  updated_at: String)

case class PREffort(
  id: Int,
  elapsed_time: Int,
  distance: Float,
  start_date: String,
  start_date_local: String,
  is_kom: Boolean)
