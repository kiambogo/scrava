package scrava.models

/**
 * Created by christopher on 2014-09-15.
 */
case class Comment(
  id: Int,
  resourceState: Int,
  text: String,
  athlete: Athlete,
  createdAt: String) {

}
