import org.junit.Test
import project.MultiDeviceTest
import project.BaseActions
import project.ViewId
import project.BaseFlow

class ExampleTest : MultiDeviceTest() {
    @Test
    fun test() {
        secondDevice.apply {
            recipientPhone = randomPhoneNumber()
            registration(recipientPhone)
        }
        firstDevice.apply {
            senderPhone = randomPhoneNumber()
            registration(senderPhone)
            val Account = AccountSelect("Main")
            loadMoney(30, Account, skipLoadMoneyButton = true)
            scrollAndClick(ViewId.DefaultView.MAIN_SCROLL, ViewId.Option.SEND)
            sendToClient(10, recipientPhone)
            scrollAndClick(ViewId.DefaultView.SCROLL, ViewId.Option.FAVOURITES)
            explicitWaitInput("Olga",ViewId.SendFlow.INPUT_NAME)
            explicitWaitClick(ViewId.Navigation.SAVE_ACTION)
            scrollAndClick(ViewId.DefaultView.SCROLL, ViewId.Option.REQUEST)
            explicitWaitInput(5.toString(),ViewId.LoadFlow.INPUT_AMOUNT)
            ifIOS { findElementByAccessibilityId("Done").click() }
            try {
                explicitWaitInput("comment",ViewId.SendFlow.INPUT_COMMENT)
            } catch (e: Exception) { }
            explicitWaitClick(ViewId.Navigation.CONTINUE)
            Thread.sleep(1000L)
            explicitWaitClick(ViewId.Navigation.BACK_HOME_BUTTON,similarityPrefix = "request")
            Thread.sleep(500L)
        }
        secondDevice.apply {
            explicitWait(ViewId.DefaultView.SCROLL)
            scrollAndClick(ViewId.DefaultView.SCROLL, object : IViewId {
                override val ios = "send_request_to_+$senderPhone"
                override val iosMatchType = IosMatchType.ID
                override val android = "send_request_to_+$senderPhone"
                override val androidMatchType = AndroidMatchType.DESCRIPTION
            })
            explicitWaitClick(ViewId.Navigation.CLOSE_ACTION)
            assertViewNotExist(
                ViewId.SendFlow.INSUFFICIENT_FUNDS_ERROR,
                WebDriverWait(this, 20)
            )
        }
    }

}