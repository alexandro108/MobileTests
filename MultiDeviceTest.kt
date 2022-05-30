open class MultipleDeviceAppiumTest() {

   
    var firstDriveRule = AppiumDriveRule(true,"primaryDevice",true)
    
    var secondDriveRule = AppiumDriveRule(true,"secondaryDevice",true)

    val firstDevice: AppiumDrive<*>
        get() = firstDriveRule.device!!

    val secondDevice: AppiumDrive<*>
        get() = secondDriveRule.device!!

}