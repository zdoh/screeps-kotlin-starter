package starter

import screeps.api.*
import screeps.api.structures.StructureController

enum class Role {
    UNASSIGNED,
    HARVESTER,
    BUILDER,
    UPGRADER
}

/**
 * Работа апгрейдера, если есть свободное место ищем ресурс,
 *
 * @param controller
 */
fun Creep.upgrade(controller: StructureController) {
    checkForHarvest()

    if (memory.needToHarvest) {
        if (harvest(room.find(FIND_SOURCES)[1]) == ERR_NOT_IN_RANGE) {
            moveTo(room.find(FIND_SOURCES)[1].pos)
        }
    } else {
        if (upgradeController(controller) == ERR_NOT_IN_RANGE) {
            moveTo(controller.pos)
        }
    }
}

fun Creep.pause() {
    if (memory.pause < 10) {
        //blink slowly
        if (memory.pause % 3 != 0) say("\uD83D\uDEAC")
        memory.pause++
    } else {
        memory.pause = 0
        memory.role = Role.HARVESTER
    }
}

fun Creep.build() {
    checkForHarvest()

    if (memory.needToHarvest) {
        val sources = room.find(FIND_SOURCES)
        if (harvest(sources[0]) == ERR_NOT_IN_RANGE) {
            moveTo(sources[0].pos)
        }
    } else {
        val targets = room.findMyConstructionSites()

        targets.sort { a, b ->
            (Constants.constructionSitePriority[a.structureType]
                ?: 100) - (Constants.constructionSitePriority[b.structureType] ?: 100)
        }

        if (targets.isNotEmpty()) {
            if (build(targets[0]) == ERR_NOT_IN_RANGE) {
                moveTo(targets[0].pos)
            }
        }
    }
}

/**
 * Работа собирателя. Собирает ресурс RESOURCE_ENERGY и проверят при наличие свободного места в
 * [STRUCTURE_EXTENSION] или в [STRUCTURE_SPAWN] несет туда
 *
 * @param fromRoom
 * @param toRoom
 */
fun Creep.harvest(fromRoom: Room = this.room, toRoom: Room = this.room) {
    checkForHarvest()

    if (memory.needToHarvest) {
        val sources = fromRoom.find(FIND_SOURCES)
        if (harvest(sources[0]) == ERR_NOT_IN_RANGE) {
            moveTo(sources[0].pos)
        }
    } else {

        val targets = toRoom.findMyStructure()
            .filter {
                (it.structureType == STRUCTURE_EXTENSION || it.structureType == STRUCTURE_SPAWN || it.structureType == STRUCTURE_TOWER)
            }
            .sortedBy {
                Constants.constructionSitePriority[it.structureType] ?: 100
            }
            .map {
                it.unsafeCast<StoreOwner>()
            }
            .filter {
                it.store[RESOURCE_ENERGY] < it.store.getCapacity(RESOURCE_ENERGY)
            }

        if (targets.isNotEmpty()) {
            if (transfer(targets[0], RESOURCE_ENERGY) == ERR_NOT_IN_RANGE) {
                moveTo(targets[0].pos)
            }
        } else {
            moveTo(toRoom.find(FIND_MY_SPAWNS)[0].pos)

            /*val notMyStructure = toRoom.find(FIND_STRUCTURES)
                .filter {
                    it.structureType == STRUCTURE_CONTAINER
                }
                .map {
                    it.unsafeCast<StoreOwner>()
                }
                .filter {
                    it.store[RESOURCE_ENERGY] < it.store.getCapacity(RESOURCE_ENERGY)
                }

            if (notMyStructure.isNotEmpty()) {
                if (transfer(notMyStructure[0], RESOURCE_ENERGY) == ERR_NOT_IN_RANGE) {
                    moveTo(notMyStructure[0])
                }
            } else {*/
            // }
        }
    }
}

/**
 * check:
 *  - need to harvest | if [needToHarvest] true and no free capacity in creep, change [needToHarvest] to false
 *  - not need to harvest | if [needToHarvest] false и no used capacity, change [needToHarvest] to true
 *
 */
fun Creep.checkForHarvest() {
    if (memory.needToHarvest && store.getFreeCapacity() == 0) {
        memory.needToHarvest = false
        say("🔄 work")
    }
    if (!memory.needToHarvest && store.getUsedCapacity() == 0) {
        memory.needToHarvest = true
        say("🚧 harvest")
    }
}
