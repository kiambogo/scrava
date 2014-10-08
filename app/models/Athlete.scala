package models

/**
 * Created by christopher on 2014-09-15.
 */
case class Athlete(
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
  clubs: List[Club],
  bikes: List[Gear],
  shoes: List[Gear]) {

}

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
  updated_at: String) {

}

