package starter


import screeps.api.*
import screeps.api.structures.StructureSpawn
import screeps.utils.isEmpty
import screeps.utils.unsafe.delete

fun gameLoop() {
    val mainSpawn: StructureSpawn = Game.spawns.values.firstOrNull() ?: return


    // console.log(Game.cpu.bucket)
    //delete memories of creeps that have passed away
    houseKeeping(Game.creeps)

    // периодические работы производящиеся каждое определенного кол-во тиков
    // periodicWork()

    // just an example of how to use room memory
    mainSpawn.room.memory.numberOfCreeps = mainSpawn.room.find(FIND_CREEPS).count()

    //make sure we have at least some creeps
    spawnCreeps(Game.creeps.values, mainSpawn, Game.rooms)

    healByTower()

    // build a few extensions so we can have 550 energy
/*    val controller = mainSpawn.room.controller
    if (controller != null && controller.level >= 2) {
        when (controller.room.find(FIND_MY_STRUCTURES).count { it.structureType == STRUCTURE_EXTENSION }) {
            0 -> controller.room.createConstructionSite(29, 27, STRUCTURE_EXTENSION)
            1 -> controller.room.createConstructionSite(28, 27, STRUCTURE_EXTENSION)
            2 -> controller.room.createConstructionSite(27, 27, STRUCTURE_EXTENSION)
            3 -> controller.room.createConstructionSite(26, 27, STRUCTURE_EXTENSION)
            4 -> controller.room.createConstructionSite(25, 27, STRUCTURE_EXTENSION)
            5 -> controller.room.createConstructionSite(24, 27, STRUCTURE_EXTENSION)
            6 -> controller.room.createConstructionSite(23, 27, STRUCTURE_EXTENSION)
        }
    }*/

    for ((_, creep) in Game.creeps) {
        when (creep.memory.role) {
            Role.HARVESTER -> creep.harvest()
            Role.BUILDER -> creep.build()
            Role.UPGRADER -> creep.upgrade(mainSpawn.room.controller!!)
            else -> creep.pause()
        }
    }

}

private fun houseKeeping(creeps: Record<String, Creep>) {
    if (Game.creeps.isEmpty()) return  // this is needed because Memory.creeps is undefined

    for ((creepName, _) in Memory.creeps) {
        if (creeps[creepName] == null) {
            console.log("deleting obsolete memory entry for creep $creepName")
            delete(Memory.creeps[creepName])
        }
    }
}
