package it.unibo.view.component.game.gameboard.sidebar.svg

import it.unibo.model.gameboard.Direction
import it.unibo.model.gameboard.Direction.East
import it.unibo.model.gameboard.Direction.North
import it.unibo.model.gameboard.Direction.South
import it.unibo.model.gameboard.Direction.West

object SVGCommonPaths:
  def getPathFromDirection(direction: Direction): String = direction match
    case North =>
      "m 33.472634,56.953587 v -1.88525 h 0.39229 l 0.857958,1.603027 v -1.603027 h 0.265289 v 1.88525 h -0.389467 l -0.860781,-1.611494 v 1.611494 z"
    case South =>
      "m 35.990852,57.73135 q -0.155223,0 -0.301979,-0.04798 -0.146756,-0.04798 -0.259645,-0.146756 -0.110067,-0.09878 -0.160867,-0.251179 l 0.245534,-0.07338 q 0.03387,0.101601 0.110067,0.169334 0.0762,0.06491 0.174978,0.09596 0.101601,0.03105 0.203201,0.03105 0.112889,0 0.214489,-0.03669 0.101601,-0.03669 0.16369,-0.110067 0.06491,-0.0762 0.06491,-0.18909 0,-0.115711 -0.08467,-0.183445 -0.08467,-0.06773 -0.293512,-0.121356 l -0.239889,-0.05362 q -0.129823,-0.03387 -0.256823,-0.09031 -0.124178,-0.05644 -0.206023,-0.160867 -0.07902,-0.104423 -0.07902,-0.279401 0,-0.160867 0.09878,-0.273757 0.101601,-0.112889 0.259646,-0.172156 0.160867,-0.05927 0.333023,-0.05927 0.132645,0 0.259645,0.03951 0.129823,0.03669 0.231423,0.115711 0.104422,0.0762 0.163689,0.197556 l -0.242712,0.07056 q -0.05362,-0.09878 -0.174978,-0.149578 -0.118534,-0.0508 -0.248356,-0.0508 -0.1016,0 -0.194734,0.03387 -0.09313,0.03104 -0.1524,0.09595 -0.05927,0.06491 -0.05927,0.16369 0,0.09313 0.05362,0.149578 0.05644,0.05645 0.146756,0.09031 0.09313,0.03105 0.203201,0.05645 l 0.206022,0.04516 q 0.143934,0.03387 0.270934,0.09313 0.127001,0.05927 0.206023,0.166512 0.07902,0.107244 0.07902,0.287867 0,0.129823 -0.06209,0.234245 -0.06209,0.101601 -0.166512,0.172156 -0.104422,0.06773 -0.237067,0.104423 -0.129823,0.03669 -0.268112,0.03669 z"
    case East =>
      "m 33.011746,55.916172 v -1.88525 h 1.27847 v 0.228601 h -1.01318 v 0.572913 h 0.747891 v 0.2286 h -0.747891 v 0.626535 h 1.01318 v 0.228601 z"
    case West =>
      "m 32.651134,58.475361 -0.474135,-1.88525 h 0.29069 l 0.36689,1.557871 0.344312,-1.557871 h 0.310445 l 0.344312,1.557871 0.36689,-1.557871 h 0.29069 l -0.471312,1.88525 h -0.321735 l -0.364067,-1.557871 -0.364068,1.557871 z"
