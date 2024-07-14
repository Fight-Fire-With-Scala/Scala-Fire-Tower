package it.unibo.model.cells

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import it.unibo.model.cells.CellConstraints.*
import org.junit.runner.RunWith
import org.scalatestplus.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CellSpec extends AnyWordSpecLike with Matchers:

  "A Woods cell" should:
    "ignite and become on fire" in:
      val woods = Woods(Empty)
      val ignitedWoods = woods.ignite()
      ignitedWoods shouldBe Woods(Fire)
  
    "place firebreak and become protected" in:
      val woods = Woods(Empty)
      val protectedWoods = woods.placeFirebreak()
      protectedWoods shouldBe Woods(Firebreak)
  
  "A Tower cell" should:
    "ignite and become on fire" in:
      val tower = Tower(Empty)
      val ignitedTower = tower.ignite()
      ignitedTower shouldBe Tower(Fire)
  
    "not place firebreak" in:
      val tower = Tower(Empty)
      val protectedTower = tower.placeFirebreak()
      protectedTower shouldBe None
  
  "An Eternal Fire cell" should:
    "not ignite" in:
      val eternalFire = EternalFire(Empty)
      val ignitedEternalFire = eternalFire.ignite()
      ignitedEternalFire shouldBe None
  
    "not place firebreak" in:
      val eternalFire = EternalFire(Empty)
      val protectedEternalFire = eternalFire.placeFirebreak()
      protectedEternalFire shouldBe None