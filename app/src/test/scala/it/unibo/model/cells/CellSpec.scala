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
      val woods = Woods()
      val ignitedWoods = woods.isFlammable
      ignitedWoods shouldBe true
  
    "place firebreak and become protected" in:
      val woods = Woods()
      val protectedWoods = woods.isLockable
      protectedWoods shouldBe true
  
  "A Tower cell" should:
    "ignite and become on fire" in:
      val tower = Tower()
      val ignitedTower = tower.isFlammable
      ignitedTower shouldBe true
  
    "not place firebreak" in:
      val tower = Tower()
      val protectedTower = tower.isLockable
      protectedTower shouldBe false
  
  "An Eternal Fire cell" should:
    "not ignite" in:
      val eternalFire = EternalFire()
      val ignitedEternalFire = eternalFire.isFlammable
      ignitedEternalFire shouldBe false
  
    "not place firebreak" in:
      val eternalFire = EternalFire()
      val protectedEternalFire = eternalFire.isLockable
      protectedEternalFire shouldBe false