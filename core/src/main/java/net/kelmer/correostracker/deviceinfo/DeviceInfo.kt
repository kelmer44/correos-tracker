package net.kelmer.correostracker.deviceinfo

interface DeviceInfo {

    val model: String
    val manufacturer: String
    val deviceWidthPixels: Int
    val deviceHeightPixels: Int
    val density: Int
    val density1dp: Float
}
