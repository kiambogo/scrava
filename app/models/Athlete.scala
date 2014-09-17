package models

/**
 * Created by christopher on 2014-09-15.
 */
case class Athlete(
  id: Int,
  resourceState: String,
  firstname: String,
  lastname: String,
  profile_medium: String,
  profile: String,
  city: String,
  state: String,
  country: String,
  sex: String,
  friend: String,
  follower: String,
  premium: Boolean,
  createdAt: String,
  updatedAt: String,
  approveFollowers: Boolean,
  followerCount: Int,
  friendCount: Int,
  mutualFriendCount: Int,
  datePreference: String,
  measurementPreference: String,
  email: String,
  ftp: Int,
  clubs: List[Club],
  bikes: List[Gear],
  shoes: List[Gear]) {

}
