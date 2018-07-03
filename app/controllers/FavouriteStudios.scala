package controllers
import javax.inject.Inject
import models.FavouriteStudio
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
class FavouriteStudios @Inject() extends Controller {

  def add(userId: Int, studioId: Int) = Action {
    val favourite = FavouriteStudio.addFavourite(userId, studioId)
    Ok(Json.obj("result" -> favourite))
  }

  def remove(userId: Int, studioId: Int) = Action {
    FavouriteStudio.delete(userId, studioId)
    Ok(Json.obj("result" -> Json.obj()))
  }

  def find(userId: Int, studioId: Int) =
      Action {
        val oFavourite = FavouriteStudio.find(userId, studioId)

        oFavourite match {
          case None => NotFound(Json.obj("error" -> "NOT FOUND"))
          case Some(favourite) => Ok(Json.obj("result" -> favourite))
        }
    }

  def findAll(userId: Int) =
      Action {
      val allFavourites = FavouriteStudio.findAllByUser(userId)
      Ok(Json.obj("result" -> allFavourites))
    }
}
