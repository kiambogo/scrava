package models

/**
 * Created by christopher on 2014-09-15.
 */
case class Gear(
  id: String,
  primary: Boolean,
  name: String,
  resource_state: Int,
  distance: Int) {
}
