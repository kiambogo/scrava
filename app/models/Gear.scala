package models

/**
 * Created by christopher on 2014-09-15.
 */

case class Gear(
  id: String,
  primary: Boolean,
  name: String,
  distance: Int,
  brand_name: Option[String],
  model_name: Option[String],
  frame_type: Option[String],
  description: Option[String],
  resource_state: Int)

case class GearSummary(
  id: String,
  primary: Boolean,
  name: String,
  distance: Int,
  resource_state: Int)
