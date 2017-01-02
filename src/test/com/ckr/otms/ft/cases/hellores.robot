*** Settings ***
Library       com.ckr.otms.ft.keywords.BingLib
Library       BuiltIn
*** Variables ***




*** Keywords ***
Use keyword "${content}" to search
    Search        ${content}

User can see searching result from record ${from} to record ${to}
    Can see result    ${from}  ${to}



使用关键字"${content}"进行搜索
    Search        ${content}