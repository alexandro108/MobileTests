fun  AppiumDriver<*>.explicitWaitClick(viewId: IViewId, driverWait: WebDriverWait? = null, optional:Boolean = false, similarityPrefix:String? = null, similarityMinScope:Double? = null){
    ifIOS { Thread.sleep(300L) }
    fun ewc() = explicitWait(viewId,driverWait,optional)?.apply {
        screenSimilarity((similarityPrefix?.let { it + "_" } ?: "") + viewId.toString(),similarityMinScope)
        Thread.sleep(500L)
        click()
    }
    if (optional){
        try {
            ewc()
        } catch (e: Exception) { }
    } else ewc()
    ifIOS { Thread.sleep(1000L) }
}
fun  AppiumDriver<*>.explicitWait(viewId: IViewId, driverWait: WebDriverWait? = null, optional: Boolean = false): WebElement? = uiAction(optional)  {
    ifIOS { Thread.sleep(300L) }
    when (this){
        is AndroidDriver<*> -> {
            val currentDriverWait = driverWait ?: (this as? AndroidDevice)?.defaultWait ?: WebDriverWait(this, 30)
            when (viewId.androidMatchType){
                AndroidMatchType.DESCRIPTION -> currentDriverWait.explicitWaitForElementByDescription(viewId.android)
                AndroidMatchType.TEXT -> currentDriverWait.explicitWaitForElementByText(viewId.android)
                AndroidMatchType.TEXT_IGNORE_CASE -> currentDriverWait.explicitWaitForElementByText(viewId.android,true)
                AndroidMatchType.ID -> currentDriverWait.until(ExpectedConditions.presenceOfElementLocated(
                    By.id("${this.capabilities.getCapability("appPackage")}:id/${viewId.android}"))
                )
            }
        }
        is IOSDriver<*> -> {
            val currentDriverWait = driverWait ?: (this as? IOSDevice)?.defaultWait ?: WebDriverWait(this, 30)
            when (viewId.iosMatchType){
                IosMatchType.NAME -> findElementByClassName(viewId.ios)
                IosMatchType.TEXT -> currentDriverWait.explicitWaitForElementByText(viewId.ios)
                IosMatchType.XPATH -> currentDriverWait.until(ExpectedConditions.presenceOfElementLocated(MobileBy.xpath(viewId.ios)))
                IosMatchType.ID -> currentDriverWait.until(ExpectedConditions.presenceOfElementLocated(
                    MobileBy.AccessibilityId(viewId.ios)
                ))
                else -> throw NotImplementedError()
            }
        }
        else -> throw NotImplementedError()
    }
}

fun  AppiumDriver<*>.explicitWaitInput(input:String, viewId: IViewId, driverWait: WebDriverWait? = null, hideKeyboard:Boolean = true) = uiAction {
    explicitWait(viewId,driverWait)?.apply {
        tapOnElement(driver = this@explicitWaitInput)
        ifIOS { repeat(text.length) { sendKeys(Keys.DELETE) } }
        ifAndroid { clear() }
        sendKeys(input)
        ifAndroid { if (hideKeyboard) hideKeyboard() }
        ifIOS { sendKeys("\n") }
    }
}
fun  AppiumDriver<*>.scrollAndClick(inScroll: IViewId, toView: IViewId, optional: Boolean = false){
    scroll(inScroll,toView,optional)?.click()
}
fun  AppiumDriver<*>.scroll(inScroll: IViewId, toView: IViewId, optional: Boolean = false): WebElement? = uiAction(optional) {
    when (this){
        is AndroidDriver<*> -> {
            findElementByAndroidUIAutomator(
                "new UiScrollable(${inScroll.uiSelector()}).scrollIntoView(${toView.uiSelector()})")
        }
        is IOSDriver<*> -> {
            var i = 0
            while (i < 25) {
                try {
                    if (findElementByViewId(toView)?.isDisplayed == true) {
                        break
                    }
                } catch (e: Exception) { }
                i = i + 1
                val javascriptExecutor = this as JavascriptExecutor
                val scrollObject = HashMap<String, Any>()
                if (i % 3 == 0) {

                    scrollObject["direction"] = "down"
                }else {
                    scrollObject["direction"] = "up"
                }

                javascriptExecutor.executeScript("mobile:swipe", scrollObject)
                Thread.sleep(1500L)
            }
            this.findElementByViewId(toView)
        }
        else -> throw NotImplementedError()
    }
}
fun AppiumDriver<*>.assertViewNotExist(viewId: IViewId, driverWait: WebDriverWait = WebDriverWait(this, 5)) {
    if (explicitWait(viewId, optional = true, driverWait = driverWait) != null) {
        throw AssertionError()
    }
}