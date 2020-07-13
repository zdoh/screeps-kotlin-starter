package starter

import screeps.api.*

fun periodicWork() {
    if (Game.time % 10 == 0) tick10Work()
    if (Game.time % 100 == 0) tick100Work()
}


private fun tick10Work() {
    console.log("start to do tick10Work")
}

private fun tick100Work() {
    console.log("start to do tick100Work")
    //setMaxEnergyInRoom()
}


