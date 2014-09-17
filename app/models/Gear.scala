package models

/**
 * Created by christopher on 2014-09-15.
 */
case class Gear(
  id: String,
  primary: Boolean,
  name: String,
  distance: Float,
  brandName: String,
  modelName: String,
  frameType: String,
  description: String,
  resourceState: Int) {
}
