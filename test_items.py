import time



def test_guest_should_see_button(browser):
    link = "http://selenium1py.pythonanywhere.com/catalogue/coders-at-work_207/"
    browser.get(link)
    time.sleep(30)
    assert browser.find_element_by_css_selector("#add_to_basket_form > button").is_displayed(), \
        'Кнопка добавления товара в корзину отсутсвует'
     
    