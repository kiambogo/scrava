package kiambogo.scrava.models

/**
 * Created by christopher on 14-10-26.
 */
case class UploadStatus(
  id: Int,
  external_id: String,
  error: Option[String],
  status: String,
  activity_id: Option[Int])