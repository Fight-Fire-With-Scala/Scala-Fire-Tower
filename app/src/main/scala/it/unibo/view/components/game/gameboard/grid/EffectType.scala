package it.unibo.view.components.game.gameboard.grid

enum EffectType(val effectCode: Int):
  case None extends EffectType(-1)
  case Esplosione extends EffectType(0)
  case Vampata extends EffectType(1)
  case AlberoMortoCheBrucia extends EffectType(2)
  case Nord extends EffectType(4)
  case Sud extends EffectType(5)
  case Est extends EffectType(6)
  case Ovest extends EffectType(7)
  case LineaDiBulldozer extends EffectType(8)
  case LineaProvvisoria extends EffectType(9)
  case RiDeforesta extends EffectType(10)
  case VigileDelFuocoParacadutista extends EffectType(11)
  case BombardamentoDAcqua extends EffectType(12)
  case Autopompa extends EffectType(13)
  case Secchio extends EffectType(14)

object EffectType:
  def fromEffectCode(code: Int): EffectType = 
    EffectType.values.find(_.effectCode == code).get
