fun AppiumDriver<*>.randomPhoneNumber() = "79611${Random.nextInt(0,999999).toString().padStart(6,'0')}"

fun AppiumDriver<*>.sendToClient(amount:Int,clientPhone:String){
    explicitWaitClick(BankViewId.SendFlow.SEND_OPERATION_BANK_CLIENT)
    explicitWaitInput(clientPhone,BankViewId.SendFlow.INPUT_PHONE)
    explicitWaitClick(object : IViewId {
        override val ios = "//XCUIElementType[1]"
        override val iosMatchType = IosMatchType.XPATH
        override val android = "client_+$clientPhone"
        override val androidMatchType = AndroidMatchType.DESCRIPTION
        override fun toString() = "SEND_TO_CLIENT_+$clientPhone"
    })
    inputAmountAndConfirmSendFlow(amount)
}

fun AppiumDriver<*>.inputAmountAndConfirmSendFlow(
    amount:Int,
    checkBothCurrencies: Boolean = false,
    swapCurrency: Boolean = false
){
    explicitWaitInput(amount.toString(),BankViewId.LoadFlow.INPUT_AMOUNT)
    if (checkBothCurrencies) {
        explicitWaitClick(BankViewId.SendFlow.FIXED_SIDE_SELL)
        Thread.sleep(3000L)
        explicitWaitClick(BankViewId.SendFlow.FIXED_SIDE_BUY)
        Thread.sleep(3000L)
    }
    if (swapCurrency) {
        explicitWaitClick(BankViewId.SendFlow.FIXED_SIDE_SELL)
        Thread.sleep(3000L)
    }
    val view = scroll(BankViewId.DefaultView.SCROLL,BankViewId.Navigation.CONTINUE)
    try {
        explicitWaitInput("comment",BankViewId.SendFlow.INPUT_COMMENT)
    } catch (e: Exception) { }
    view?.click()
    ifAndroid {
        explicitWait(BankViewId.SendFlow.CHECK_SEND_FRAGMENT)
        scrollToBottom(BankViewId.DefaultView.SCROLL)
        scrollAndClick(BankViewId.DefaultView.SCROLL,BankViewId.SendFlow.SEND_PAYMENT)
        explicitWaitClick(BankViewId.TransactionFlow.CONFIRM_TRANSACTION_BUTTON)
        explicitWait(BankViewId.SendFlow.SUCCESS_FRAGMENT)
    }
    ifIOS {
        Thread.sleep(2000L)
        scrollToBottom(BankViewId.DefaultView.SCROLL)
        explicitWaitClick(BankViewId.SendFlow.SEND_PAYMENT)
        Thread.sleep(2000L)
        findElementByXPath(ViewId.ANY_FIRST_TEXTFIELD.ios).apply {
            click()
            sendKeys("4565767")
        }
        Thread.sleep(5000L)
    }
}