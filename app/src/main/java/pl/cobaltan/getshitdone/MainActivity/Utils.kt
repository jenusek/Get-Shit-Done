package pl.cobaltan.getshitdone.MainActivity

import java.util.*

fun Calendar.deleteTimeInfo() {
    this.set(Calendar.HOUR_OF_DAY, 0);
    this.set(Calendar.MINUTE, 0);
    this.set(Calendar.SECOND, 0);
    this.set(Calendar.MILLISECOND, 0);
}