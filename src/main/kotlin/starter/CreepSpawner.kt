package starter

import screeps.api.*
import screeps.api.structures.StructureSpawn
import screeps.utils.unsafe.jsObject

fun spawnCreeps(creeps: Array<Creep>, spawn: StructureSpawn, rooms: Record<String, Room>) {
    for ((_, room) in rooms) {

        console.log("${room.energyAvailable} | ${room.energyCapacityAvailable}")
        when {
            creeps.count { it.memory.role == Role.HARVESTER } < Constants.CREEP_HARVESTER_COUNT ->
                spawnCreep(Role.HARVESTER, room, spawn)
            creeps.count { it.memory.role == Role.UPGRADER } < Constants.CREEP_UPGRADER_COUNT ->
                spawnCreep(Role.UPGRADER, room, spawn)
            spawn.room.find(FIND_MY_CONSTRUCTION_SITES).isNotEmpty() &&
                    creeps.count { it.memory.role == Role.BUILDER } < Constants.CREEP_BUILDER_COUNT ->
                spawnCreep(Role.BUILDER, room, spawn)
        }
    }
}

private fun spawnCreep(role: Role, room: Room, spawn: StructureSpawn) {
    val newName = "${role.name}_${Game.time}"

    val body = when {
        room.energyAvailable >= Constants.MAX_ENERGY_FOR_BIG_WORKER -> Constants.BIG_WORKER
        else -> Constants.SMALL_WORKER
    }

    val code = spawn.spawnCreep(body, newName, options {
        memory = jsObject<CreepMemory> { this.role = role }
    })

    when (code) {
        OK -> console.log("in room ${room.name} spawn $newName with body $body")
        ERR_BUSY, ERR_NOT_ENOUGH_ENERGY -> run { } // do nothing
        else -> console.log("unhandled error code $code")
    }
}