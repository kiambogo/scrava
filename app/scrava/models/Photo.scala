package scrava.models

/**
 * Created by christopher on 14-10-23.
 */
case class Photo(
id:	Int,
activity_id:	Int,
resource_state:	Int,
ref:	String,
uid:	String,
caption:	String,
`type`:	String,
uploaded_at: String,
created_at: String,
location:	List[Float]) {

}
