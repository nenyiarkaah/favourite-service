package models

import models.dao.FavouriteStudioDAO
import play.api.libs.json._
import play.api.libs.functional.syntax._

object FavouriteStudio {
  implicit val favouriteStudioWrites: Writes[FavouriteStudio] = (
    (JsPath \ "userId").write[Int] and
      (JsPath \ "studioId").write[Int]
  )(unlift(FavouriteStudio.unapply))

  def addFavourite(userId: Int, studioId: Int): FavouriteStudio = {
    val favouriteStudio = FavouriteStudio(userId, studioId)
    FavouriteStudioDAO.create(favouriteStudio)
    favouriteStudio
  }


  def delete(userId: Int, studioId: Int) =
    FavouriteStudioDAO.delete(FavouriteStudio(userId, studioId))

  def find(userId: Int, studioId: Int) = {
    val favourite = FavouriteStudio(userId, studioId)
    if (FavouriteStudioDAO.exists(favourite))
      Some(favourite)
    else
      None
  }


  def findAllByUser(userId: Int) =
    FavouriteStudioDAO.index(userId)
}

case class FavouriteStudio (userId: Int, studioId: Int)
