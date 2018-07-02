package controllers
import javax.inject.Inject
import models.FavouriteStudio
import play.api.cache.EhCacheApi
import play.api.libs.json.Json
import play.api.mvc.{Action, Controller}
import play.cache._
class FavouriteStudios @Inject() (cached: EhCacheApi) extends Controller {

  private def clearCache(userId: Int, studioId: Int) =
    List(
      "find_" + userId + "_" + studioId,
      "findAll_" + userId
    ).map { key =>
      cached.remove(key)
    }

  def add(userId: Int, studioId: Int) = Action {
    val favourite = FavouriteStudio.addFavourite(userId, studioId)
    clearCache(userId, studioId)
    Ok(Json.obj("result" -> favourite))
  }

  def remove(userId: Int, studioId: Int) = Action {
    FavouriteStudio.delete(userId, studioId)
    clearCache(userId, studioId)
    Ok(Json.obj("result" -> Json.obj()))
  }

  def find(userId: Int, studioId: Int) =
    cached("find_" + userId + "_" + studioId) {
      Action {
        val oFavourite = FavouriteStudio.find(userId, studioId)

        oFavourite match {
          case None => NotFound(Json.obj("error" -> "NOT FOUND"))
          case Some(favourite) => Ok(Json.obj("result" -> favourite))
        }
      }
    }

  def findAll(userId: Int) =
    cached("find_" + userId) {
      Action {
      val allFavourites = FavouriteStudio.findAllByUser(userId)
      Ok(Json.obj("result" -> allFavourites))
    }
  }
}
