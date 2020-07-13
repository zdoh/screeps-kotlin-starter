package starter

import screeps.api.*
import screeps.api.structures.Structure

fun Room.findStructure() = find<Structure>(FIND_STRUCTURES)
fun Room.findMyStructure() = find<Structure>(FIND_MY_STRUCTURES)
fun Room.findMyConstructionSites() = find<ConstructionSite>(FIND_MY_CONSTRUCTION_SITES)