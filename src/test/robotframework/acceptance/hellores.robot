*** Settings ***
Library       com.ckr.otms.keyword.BingLib

*** Variables ***




*** Keywords ***
Use keyword "${content}" to search
    Search        ${content}

Can see searching result from record ${from} to record ${to}
    Can see result    ${from}  ${to}