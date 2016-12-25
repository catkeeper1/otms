*** Settings ***
Library       OperatingSystem
Library       com.ckr.otms.keyword.BingLib

*** Variables ***
${MESSAGE}    Hello, world!

*** Test Cases ***
My Test
    [Documentation]    Example test

    Open Bing
    Search         agile

*** Keywords ***
