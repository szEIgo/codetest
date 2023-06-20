package utils

import db.PostgresClient
import wvlet.log.LogSupport

import scala.concurrent.Future

class EditDataUtil(db: PostgresClient) extends LogSupport {
  def updateEntry(entryId: String, score: BigDecimal): model.DataEntry =
    db.scoreRepo.update(entryId, score).flatMap(s => db.scoreRepo.getById(entryId)).unsafeRunSync()

}
