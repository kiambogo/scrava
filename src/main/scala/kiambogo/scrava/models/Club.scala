package kiambogo.scrava.models

/**
 * Created by christopher on 2014-09-15.
 */

case class Club(
  id: Int,
  resource_state: Int,
  name: String,
  profile_medium: String,
  profile: String,
  description: String,
  club_type: String,
  sport_type: String,
  city: String,
  state: String,
  country: String,
  `private`: Boolean,
  member_count: Int)

case class ClubSummary(
  id: Int,
  resource_state: Int,
  name: String,
  profile_medium: String,
  profile: String)