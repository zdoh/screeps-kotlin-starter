package starter

import screeps.api.*
import screeps.api.structures.StructureTower


fun healByTower() {
    for ((roomName, room) in Game.rooms) {
        // console.log("$roomName | $room")

        val towers = room.findStructure()
            .filter { it.structureType == STRUCTURE_TOWER }
            .map {
                it.unsafeCast<StructureTower>()
            }

        if (towers.isNotEmpty()) {
            val needToBeHealed = room.findStructure()
                .filter {
                    when (it.structureType) {
                        STRUCTURE_ROAD -> it.hits < Constants.ROAD_NEED_TO_HEAL_HITS
                        else -> it.hitsMax > it.hits
                    }
                }
                .sortedByDescending { it.hits }

            if (needToBeHealed.isNotEmpty()) {
                towers[0].repair(needToBeHealed[0])
                console.log("In room $roomName tower ${towers[0]} begin to heal ${needToBeHealed[0]}")
            }
        }
    }
}