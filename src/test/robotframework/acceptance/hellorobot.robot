*** Settings ***
Resource    hellores.robot

*** Variables ***


*** Test Cases ***
My Test
    [Documentation]    Example test

    Open Bing page
    Use keyword "agile" to search
    Go to page    5
    Can see searching result from record 41 to record 50
    Go to page    3
    Can see searching result from record 21 to record 30

    Use keyword "scrum" to search
    Go to page    4
    Can see searching result from record 31 to record 40
    Go to page    6
    Can see searching result from record 51 to record 60

*** Keywords ***

