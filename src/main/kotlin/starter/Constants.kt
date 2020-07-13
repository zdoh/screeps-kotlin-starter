package starter

import screeps.api.*
import screeps.api.structures.StructureContainer

class Constants {
    companion object {
        const val CREEP_HARVESTER_COUNT = 3
        const val CREEP_UPGRADER_COUNT = 3
        const val CREEP_BUILDER_COUNT = 3

        const val MAX_ENERGY_FOR_BIG_WORKER = 500

        const val ROAD_NEED_TO_HEAL_HITS = 2500

        val SMALL_WORKER: Array<BodyPartConstant> = arrayOf(WORK, CARRY, MOVE)
        val BIG_WORKER: Array<BodyPartConstant> = arrayOf(WORK, WORK, WORK, CARRY, CARRY, MOVE, MOVE)

        val constructionSitePriority: MutableMap<BuildableStructureConstant, Int> = mutableMapOf(
            STRUCTURE_EXTENSION to 1,
            STRUCTURE_TOWER to 2,
            STRUCTURE_ROAD to 3,
            STRUCTURE_CONTAINER to 4
        )

        val myEnergyStorageWithPriority: MutableMap<BuildableStructureConstant, Int> = mutableMapOf(
            STRUCTURE_EXTENSION to 1,
            STRUCTURE_SPAWN to 2
        )

        val containerStorage: MutableMap<BuildableStructureConstant, Int> = mutableMapOf(
            STRUCTURE_CONTAINER to 1
        )
    }
}

data class RoomConfig(
    val roomName: String,
    val minersTeam: List<MinerTeam>
)

data class MinerTeam(
    val container: RoomPosition,
    val energyGenerate: RoomPosition,
    val minersPositions: List<RoomPosition>,
    val minersCount: Int
)